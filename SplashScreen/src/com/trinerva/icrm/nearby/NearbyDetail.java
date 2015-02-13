package com.trinerva.icrm.nearby;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.contacts.ContactInfoTab;
import com.trinerva.icrm.leads.LeadInfo;

public class NearbyDetail extends android.support.v4.app.FragmentActivity {
	// private MapView mapView;
	private TextView contact_info, address;
	ImageView detail;
	private ImageView show;
	private String ID, SYSTEM_ID, ACTIVE, TYPE, TITLE, ADDRESS;
	private double LATITUDE, LONGITUDE;
	private GoogleMap map;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_detail); // bind the layout to the
												// activity

		detail = (ImageView) findViewById(R.id.detail);
		contact_info = (TextView) findViewById(R.id.contact_info);
		address = (TextView) findViewById(R.id.address);

		show = (ImageView) findViewById(R.id.show);
		// mapView = (MapView) findViewById(R.id.mapview);

		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			NearbyDetail.this.finish();
		} else {
			ID = bundle.getString("ID");
			SYSTEM_ID = bundle.getString("SYSTEM_ID");
			ACTIVE = bundle.getString("ACTIVE");
			TYPE = bundle.getString("TYPE");
			TITLE = bundle.getString("TITLE");
			ADDRESS = bundle.getString("ADDRESS");
			LATITUDE = bundle.getDouble("LATITUDE");
			LONGITUDE = bundle.getDouble("LONGITUDE");

			contact_info.setText("[" + TYPE + "] " + TITLE);
			address.setText(ADDRESS);
			show.setOnClickListener(doShowMap);
			detail.setOnClickListener(doShowDetail);

			// List<Overlay> mapOverlays = mapView.getOverlays();
			// Drawable drawable =
			// this.getResources().getDrawable(R.drawable.red_dot);
			// FirstLinkItemizedOverlay itemizedoverlay = new
			// FirstLinkItemizedOverlay(drawable);

//			GeoPoint point = new GeoPoint((int) (LATITUDE * 1E6),
//					(int) (LONGITUDE * 1E6));
			// OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
			// "I'm in Mexico City!");
			// itemizedoverlay.addOverlay(overlayitem);
			// mapOverlays.add(itemizedoverlay);
			// mapView.getController().animateTo(point);
			// mapView.getController().setZoom(16);

			if (map == null) {
				map = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.mapview)).getMap();
				if (map != null) {
					map.addMarker(new MarkerOptions().position(new LatLng(
							LATITUDE, LONGITUDE)));
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
							LATITUDE, LONGITUDE), 15.5f), 4000, null);
				}
			}

			// mapView.getController().setCenter(point);//try this.
			// mapView.getController().zoomToSpan((int) (LATITUDE * 1E6), (int)
			// (LONGITUDE * 1E6));
		}

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	// @Override
	// protected boolean isRouteDisplayed() {
	// return false;
	// }

	private OnClickListener doShowMap = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String uri = "geo:" + LATITUDE + "," + LONGITUDE + "?q=" + LATITUDE
					+ "," + LONGITUDE + "(" + TITLE + ")";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(uri));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);
		}
	};

	private OnClickListener doShowDetail = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (TYPE.equalsIgnoreCase("LEAD")) {
				// Intent info = new Intent(NearbyDetail.this,
				// LeadProfile.class);
				// info.putExtra("ID", ID);
				// info.putExtra("ACTIVE", ACTIVE);
				// NearbyDetail.this.startActivity(info);

				Intent info = new Intent(NearbyDetail.this, LeadInfo.class);
				info.putExtra("ID", ID);
				info.putExtra("VIEW", true);
				info.putExtra("ACTIVE", ACTIVE);
				NearbyDetail.this.startActivity(info);
			} else {

				Intent info = new Intent(NearbyDetail.this,
						ContactInfoTab.class);
				info.putExtra("ID", ID);
				info.putExtra("ACTIVE", ACTIVE);
				info.putExtra("VIEW", true);
				info.putExtra("CONTACT_ID", SYSTEM_ID);
				NearbyDetail.this.startActivity(info);

				// Intent info = new Intent(NearbyDetail.this,
				// ContactProfile.class);
				// info.putExtra("ID", ID);
				// info.putExtra("ACTIVE", ACTIVE);
				// info.putExtra("CONTACT_ID", SYSTEM_ID);
				// NearbyDetail.this.startActivity(info);
			}
		}
	};
}
