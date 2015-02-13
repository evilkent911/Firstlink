package com.trinerva.icrm;

import java.util.ArrayList;
import com.trinerva.icrm.database.source.Coordinate;
import com.trinerva.icrm.nearby.NearbyDetail;
import com.trinerva.icrm.object.CoordinateAdapter;
import com.trinerva.icrm.object.CoordinateDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import asia.firstlink.icrm.R;

public class TabNearbyFragment extends Fragment implements LocationListener {
	public FragmentActivity fa;
	
	private String TAG = "TabNearbyFragment";
	private LocationManager service;
	private String provider;
	private Location location;
	private ArrayList<CoordinateDetail> aCoordinateList = new ArrayList<CoordinateDetail>();
	private CoordinateAdapter adapter;
	
	private TextView km0, km5, km10, km15, empty_list;
	private EditText search;
	private ListView list;
	
	private Dialog loadingDialog;
	private int DISTANCE_CAT = 1;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fa = super.getActivity();
		if (container == null) {
			return null;
		}
		
		RelativeLayout mainLayout = (RelativeLayout)inflater.inflate(R.layout.tab_nearby, container, false);
		km0 = (TextView) mainLayout.findViewById(R.id.km0);
		km5 = (TextView) mainLayout.findViewById(R.id.km5);
		km10 = (TextView) mainLayout.findViewById(R.id.km10);
		km15 = (TextView) mainLayout.findViewById(R.id.km15);
		empty_list = (TextView) mainLayout.findViewById(R.id.empty_list);
		search = (EditText) mainLayout.findViewById(R.id.search);
		list = (ListView) mainLayout.findViewById(R.id.list);
		
		km0.setOnClickListener(distanceSelection);
		km5.setOnClickListener(distanceSelection);
		km10.setOnClickListener(distanceSelection);
		km15.setOnClickListener(distanceSelection);
		
		list.setFocusableInTouchMode(true);
		list.requestFocus(0);
		list.setSelection(0);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(doGetDetail);
		
		search.addTextChangedListener(filterTextWatcher);
		
		service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		return mainLayout;
	}
	
	private OnItemClickListener doGetDetail = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CoordinateDetail coordinate = adapter.getItem(position);
			DeviceUtility.log(TAG, coordinate.getCompanyName());
			Intent iMap = new Intent(view.getContext(), NearbyDetail.class);
			Bundle bundle = new Bundle();
			String strTitle = coordinate.getFirstName();
			if (strTitle != null && strTitle.length() > 0) {
				strTitle += " ";
			} else {
				strTitle = "";
			}
			strTitle += coordinate.getLastName() + ", " + coordinate.getCompanyName();
			String strAddress = "";
			if (coordinate.getStreet() != null && coordinate.getStreet().length() > 0) {
				strAddress += coordinate.getStreet();
			}
			
			if (coordinate.getCity() != null && coordinate.getCity().length() > 0) {
				if (strAddress.length() > 0) {
					strAddress += ", ";
				}
				strAddress += coordinate.getCity();
			}
			
			if (coordinate.getState() != null && coordinate.getState().length() > 0) {
				if (strAddress.length() > 0) {
					strAddress += ", ";
				}
				strAddress += coordinate.getState();
			}
			
			bundle.putString("ID", coordinate.getInternalNum());
			bundle.putString("SYSTEM_ID", coordinate.getServerId());
			bundle.putString("ACTIVE", coordinate.getActive());
			bundle.putString("TYPE", coordinate.getType());
			bundle.putString("TITLE", strTitle);
			bundle.putString("ADDRESS", strAddress);
			bundle.putDouble("LATITUDE", coordinate.getLatitude());
			bundle.putDouble("LONGITUDE", coordinate.getLongitude());
			iMap.putExtras(bundle);
			TabNearbyFragment.this.startActivity(iMap);			
		}
	};

	@Override
	public void onResume() {
		DeviceUtility.log(TAG, "onResume");
		doShowSelectedDistance(DISTANCE_CAT);
		Criteria criteria = new Criteria();
		provider = service.getBestProvider(criteria, false);
		
		service.requestLocationUpdates(provider, 5000, 1, TabNearbyFragment.this);
		//service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, TabNearbyFragment.this);
		location = service.getLastKnownLocation(provider);
		if (location != null) {
			DeviceUtility.log(TAG, "location:" + location.getLatitude() + "," + location.getLongitude());
		}
		Nearby load = new Nearby();
		load.execute(location);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		/* GPS, as it turns out, consumes battery like crazy */
		service.removeUpdates(this);
		super.onPause();
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
	    public void afterTextChanged(Editable s) {
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	    	DeviceUtility.log(TAG, "onTextChanged: " + s);
	    	if (adapter != null) {
	    		adapter.filter(s.toString());
	    	}
	    }
	};
	
	private OnClickListener distanceSelection = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (loadingDialog == null) {
				loadingDialog = GuiUtility.getLoadingDialog(getActivity(), false, null);
			} else {
				if (!loadingDialog.isShowing()) {
					loadingDialog.show();
				}
			}
			switch(v.getId()) {
				case R.id.km0:
					doShowSelectedDistance(1);
					break;
				case R.id.km5:
					doShowSelectedDistance(2);
					break;
				case R.id.km10:
					doShowSelectedDistance(3);
					break;
				case R.id.km15:
					doShowSelectedDistance(4);
					break;
			}
		}
	};
	
	private void doShowSelectedDistance(int iCategory) {
		//reset all.
		km0.setBackgroundResource(R.drawable.blue_menu_middle);
		km5.setBackgroundResource(R.drawable.blue_menu_middle);
		km10.setBackgroundResource(R.drawable.blue_menu_middle);
		km15.setBackgroundResource(R.drawable.blue_menu_middle);
		switch (iCategory) {
			case 1:
				km0.setBackgroundResource(R.drawable.blue_menu_selected_middle);
				DISTANCE_CAT = 1;
				break;
			case 2:
				km5.setBackgroundResource(R.drawable.blue_menu_selected_middle);
				DISTANCE_CAT = 2;
				break;
			case 3:
				km10.setBackgroundResource(R.drawable.blue_menu_selected_middle);
				DISTANCE_CAT = 3;
				break;
			case 4:
				km15.setBackgroundResource(R.drawable.blue_menu_selected_middle);
				DISTANCE_CAT = 4;
				break;
		}
		
		if (aCoordinateList != null && aCoordinateList.size() > 0) {
			adapter.showType(DISTANCE_CAT);
			if (search.getText().length() > 0) {
				adapter.filter(search.getText().toString());
			}
			
			if (adapter.getCount() > 0) {
				list.setAdapter(adapter);
				empty_list.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			} else {
				empty_list.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
		}
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}
	
	private class Nearby extends AsyncTask<Location, Void, ArrayList<CoordinateDetail>> {
		private boolean bCloseLoading = true;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(getActivity(), false, null);
		}
		
		@Override
		protected ArrayList<CoordinateDetail> doInBackground(Location... params) {			
			Location param = params[0];
			DeviceUtility.log(TAG, "param: " + param);
			DatabaseUtility.getDatabaseHandler(getActivity());
			Coordinate coordinate = new Coordinate(Constants.DBHANDLER);
			ArrayList<CoordinateDetail> aCoordinate = coordinate.getAllContactLead();
			if (aCoordinate.size() > 0 && param != null) {
				//calculate distance.
				for (CoordinateDetail oCoordinate : aCoordinate) {
					Location dest = new Location(provider);
					if (oCoordinate.getLatitude() > 0 && oCoordinate.getLongitude() > 0) {
						dest.setLatitude(oCoordinate.getLatitude());
						dest.setLongitude(oCoordinate.getLongitude());
						float fMeter = param.distanceTo(dest);
						double dDistance = (double) fMeter/1000;
						DeviceUtility.log(TAG, oCoordinate.getCompanyName() + " : " + dDistance);
						oCoordinate.setDistance(dDistance);
					}
				}
			}
			
			if (param == null) {
				bCloseLoading = false;
			}
						
			return aCoordinate;
		}

		@Override
		protected void onPostExecute(ArrayList<CoordinateDetail> result) {
			DeviceUtility.log(TAG, "onPostExecute: " + result.size());
			super.onPostExecute(result);
			aCoordinateList = result;
			//do adapter.
			adapter = new CoordinateAdapter(fa, aCoordinateList, DISTANCE_CAT);
			if (adapter.isEmpty()) {
				empty_list.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			} else {
				empty_list.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				list.setAdapter(adapter);
			}
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			
			//show loading location cause location haven't get it.
			boolean bEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
			//boolean bEnabled = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if (!bCloseLoading && bEnabled) {
				loadingDialog = GuiUtility.getLoadingDialog(getActivity(), true, getString(R.string.loading_location));
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		DeviceUtility.log(TAG, "onLocationChanged");
		if (aCoordinateList.size() > 0 && location != null) {
			DeviceUtility.log(TAG, "location.getLongitude(): " + location.getLongitude());
			DeviceUtility.log(TAG, "location.getLatitude(): " + location.getLatitude());
			//calculate distance.
			for (CoordinateDetail oCoordinate : aCoordinateList) {
				Location dest = new Location(provider);
				if (oCoordinate.getLatitude() > 0 && oCoordinate.getLongitude() > 0) {
					dest.setLatitude(oCoordinate.getLatitude());
					dest.setLongitude(oCoordinate.getLongitude());
					float fMeter = location.distanceTo(dest);
					double dDistance = (double) fMeter/1000;
					DeviceUtility.log(TAG, oCoordinate.getCompanyName() + " : " + dDistance);
					oCoordinate.setDistance(dDistance);
				}
			}
			
			if (!aCoordinateList.isEmpty()) {
				adapter = new CoordinateAdapter(fa, aCoordinateList, DISTANCE_CAT);
				if (search.getText().length() > 0) {
					adapter.filter(search.getText().toString());
				}
				list.setAdapter(adapter);
				
				if (adapter.getCount() > 0) {
					empty_list.setVisibility(View.GONE);
					list.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				} else {
					empty_list.setVisibility(View.VISIBLE);
					list.setVisibility(View.GONE);
				}
			}
		}
		
		if (location != null) {
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		DeviceUtility.log(TAG, "onProviderDisabled");
		/* this is called if/when the GPS is disabled in settings */
		/* bring up the GPS settings */
		GuiUtility.alert(getActivity(), getString(R.string.turn_on_location_title), getString(R.string.turn_on_location_desc), Gravity.CENTER, getString(R.string.settings),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
			}, getString(R.string.skip), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					return;
				}
			});
	}

	@Override
	public void onProviderEnabled(String arg0) {
		DeviceUtility.log(TAG, "onProviderEnabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		DeviceUtility.log(TAG, "onStatusChanged");
		/*switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			DeviceUtility.log(TAG, "Status Changed: Out of Service");
			GuiUtility.makeToast(getActivity(), "Status Changed: Out of Service", Toast.LENGTH_SHORT);
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			DeviceUtility.log(TAG, "Status Changed: Temporarily Unavailable");
			GuiUtility.makeToast(getActivity(), "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT);
			break;
		case LocationProvider.AVAILABLE:
			DeviceUtility.log(TAG, "Status Changed: Available");
			GuiUtility.makeToast(getActivity(), "Status Changed: Available", Toast.LENGTH_SHORT);
			break;
		}*/
	}
}
