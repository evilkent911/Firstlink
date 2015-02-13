package com.trinerva.icrm.object;

import java.util.ArrayList;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.StorageUtility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoordinateAdapter extends BaseAdapter {
	private String TAG = "CoordinateAdapter";
	private Activity activity;
	private ArrayList<CoordinateDetail> data;
	private ArrayList<CoordinateDetail> original;
	private static LayoutInflater inflater = null;
	private int iType = 0;
	
	public CoordinateAdapter(Activity a, ArrayList<CoordinateDetail> d, int type) {
		activity = a;
		original = d;
		filterType(type, original);
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void showType(int type) {
		DeviceUtility.log(TAG, "showType("+type+")");
		filterType(type, original);
	}
	
	public void filterType(int type, ArrayList<CoordinateDetail> filterDetails) {
		DeviceUtility.log(TAG, "filterType("+type+")");
		iType = type;
		data = new ArrayList<CoordinateDetail>();
		DeviceUtility.log(TAG, "d.size(): " + filterDetails.size());
		if (filterDetails.size() > 0) {
			for (CoordinateDetail oCoordinate : filterDetails) {
				switch (iType) {
					case 1: //0-5km
						DeviceUtility.log(TAG, "oCoordinate.getDistance(): " + oCoordinate.getDistance());
						if (oCoordinate.getDistance() >= 0 && oCoordinate.getDistance() <= 5) {
							DeviceUtility.log(TAG, "Category 1");
							data.add(oCoordinate);
						}
						break;
					case 2: //5-10km
						if (oCoordinate.getDistance() > 5 && oCoordinate.getDistance() <= 10) {
							data.add(oCoordinate);
						}
						break;
					case 3: //10-15km
						if (oCoordinate.getDistance() > 10 && oCoordinate.getDistance() <= 15) {
							data.add(oCoordinate);
						}
						break;
					case 4: //15-20km
						if (oCoordinate.getDistance() > 15 && oCoordinate.getDistance() <= 20) {
							data.add(oCoordinate);
						}
						break;
				}
			}
		}
		DeviceUtility.log(TAG, "data.size(): " + data.size());
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CoordinateDetail getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public static class ViewHolder {
		public ImageView photo;
		public TextView contact_name;
		public TextView distance_type;
	}
	
	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.nearby_item, null);
			holder = new ViewHolder();
			holder.photo = (ImageView) vi.findViewById(R.id.photo);
			holder.contact_name = (TextView) vi.findViewById(R.id.contact_name);
			holder.distance_type = (TextView) vi.findViewById(R.id.distance_type);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		CoordinateDetail aData = data.get(position);
		
		if (aData != null) {
			String photoId = aData.getPhoto();
			String strFirstName = aData.getFirstName();
			String strLastName = aData.getLastName();
			String strCompanyName = aData.getCompanyName();
			String strType = aData.getType();
			String strDistance = String.valueOf((int) aData.getDistance());
			
			String strInfo = activity.getString(R.string.km_away);
			strInfo = strInfo.replace("[DISTANCE]", strDistance);
			strInfo = strInfo.replace("[TYPE]", strType);
			
			String strContactName = strLastName;
//			if (strContactName != null && strContactName.length() > 0) {
//				strContactName += " " + strLastName;
//			}
			
			strContactName += ", " + strCompanyName;
						
			holder.distance_type.setText(strInfo);
			
			holder.contact_name.setText(strLastName);
			
			if (photoId != null && photoId != "") {
				StorageUtility storage = new StorageUtility(activity, Constants.CACHE_FOLDER);
				Bitmap bitmap = storage.doReadImage(photoId);
				if (bitmap != null) {
					holder.photo.setImageBitmap(bitmap);
				} else {
					holder.photo.setImageResource(R.drawable.contacts);
				}
			} else {
				holder.photo.setImageResource(R.drawable.contacts);
			}
		}
		return vi;
	}
	
	public void filter(String filter) {
		ArrayList<CoordinateDetail> aFilter = new ArrayList<CoordinateDetail>();
		if (filter.length() > 0) {
			if (original.size() > 0) {
				DeviceUtility.log(TAG, "filter: " + filter);
				for (CoordinateDetail coor : original) {
					DeviceUtility.log(TAG, "getCompanyName: " + coor.getCompanyName().toLowerCase().contains(filter.toLowerCase()));
					DeviceUtility.log(TAG, "getFirstName: " + coor.getFirstName().toLowerCase().contains(filter.toLowerCase()));
					DeviceUtility.log(TAG, "getLastName: " + coor.getLastName().toLowerCase().contains(filter.toLowerCase()));
					if (coor.getCompanyName().toLowerCase().contains(filter.toLowerCase())) {
						aFilter.add(coor);
					} else if (coor.getFirstName().toLowerCase().contains(filter.toLowerCase())) {
						aFilter.add(coor);
					} else if (coor.getLastName().toLowerCase().contains(filter.toLowerCase())) {
						aFilter.add(coor);
					}
				}
				filterType(iType, aFilter);
			}
			/*if (data.size() > 0) {
				DeviceUtility.log(TAG, "filter: " + filter);
				for (CoordinateDetail coor : data) {
					DeviceUtility.log(TAG, "getCompanyName: " + coor.getCompanyName().toLowerCase().contains(filter.toLowerCase()));
					DeviceUtility.log(TAG, "getFirstName: " + coor.getFirstName().toLowerCase().contains(filter.toLowerCase()));
					DeviceUtility.log(TAG, "getLastName: " + coor.getLastName().toLowerCase().contains(filter.toLowerCase()));
					if (coor.getCompanyName().toLowerCase().contains(filter.toLowerCase())) {
						aFilter.add(coor);
					} else if (coor.getFirstName().toLowerCase().contains(filter.toLowerCase())) {
						aFilter.add(coor);
					} else if (coor.getLastName().toLowerCase().contains(filter.toLowerCase())) {
						aFilter.add(coor);
					}
				}
				data = aFilter;
			}*/
		} else {
			DeviceUtility.log(TAG, "no filter: " + filter);
			showType(iType);
		}
		notifyDataSetChanged();
    }
}
