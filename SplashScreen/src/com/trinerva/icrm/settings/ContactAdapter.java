package com.trinerva.icrm.settings;

import java.util.ArrayList;
import java.util.HashMap;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.utility.DeviceUtility;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private String TAG = "ContactAdapter";
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private Activity activity;
	
	public ContactAdapter (Activity a, ArrayList<HashMap<String, String>> mData) {
		data = mData;
		activity = a;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
		
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public static class ViewHolder {
		public CheckBox contact_check_box;
		public LinearLayout my_main_layout;
	}
	
	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.import_contact_item, null);
			holder = new ViewHolder();
			holder.contact_check_box = (CheckBox) vi.findViewById(R.id.contact_check_box);
			holder.my_main_layout = (LinearLayout) vi.findViewById(R.id.my_main_layout);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		
		HashMap<String, String> hData = data.get(position);
				
		if (hData != null) {
			DeviceUtility.log(TAG, "hData: " + hData);
			String strName = "";
			if (hData.containsKey("FIRSTNAME") && hData.get("FIRSTNAME") != null) {
				strName = hData.get("FIRSTNAME").toString();
			}
			
			if (strName.length() > 0) {
				strName += " ";
			}
			
			if (hData.containsKey("LASTNAME") && hData.get("LASTNAME") != null) {
				strName += hData.get("LASTNAME").toString();
			}
			
			if (hData.containsKey("CHECKED")) {
				if (hData.get("CHECKED").equalsIgnoreCase("true")) {
					holder.contact_check_box.setChecked(true);
				} else {
					holder.contact_check_box.setChecked(false);
				}
			}
			
			DeviceUtility.log(TAG, "strName: " + strName);
			
			holder.contact_check_box.setTag(new Integer(position));
			holder.contact_check_box.setText(strName);
			DeviceUtility.log(TAG, "holder.check_box.getText(): " + holder.contact_check_box.getText());
			DeviceUtility.log(TAG, "holder.check_box.getTag(): " + holder.contact_check_box.getTag());
			
			holder.contact_check_box.setOnClickListener(doItemCheck);
			holder.my_main_layout.setOnClickListener(doCheckItem);
		}
		return vi;
	}
	
	private OnClickListener doItemCheck = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CheckBox cb = (CheckBox) v.findViewById(R.id.contact_check_box);
			int position = ((Integer) cb.getTag()).intValue();
			HashMap<String, String> tData = data.get(position);
			if (cb.isChecked()) {
				tData.put("CHECKED", "true");
			} else {
				tData.put("CHECKED", "false");
			}
			data.set(position, tData);
		}
	};
	
	private OnClickListener doCheckItem = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CheckBox cb = (CheckBox) v.findViewById(R.id.contact_check_box);
			int position = ((Integer) cb.getTag()).intValue();
			HashMap<String, String> tData = data.get(position);
			if (cb.isChecked()) {
				tData.put("CHECKED", "false");
				cb.setChecked(false);
			} else {
				tData.put("CHECKED", "true");
				cb.setChecked(true);
			}
			data.set(position, tData);
		}
	};
}