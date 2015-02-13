package com.trinerva.icrm.event;

import java.util.ArrayList;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.object.ProfileDisplay;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InvitedAdapter extends BaseAdapter {
	private ArrayList<ProfileDisplay> data;
	private static LayoutInflater inflater = null;
	private Activity activity;
	private ProfileDisplay profile;
	private int iLayout;
	
	public InvitedAdapter(Activity a, int iLayout) {
		data = new ArrayList<ProfileDisplay>();
		activity = a;
		this.iLayout = iLayout;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void addData(ArrayList<ProfileDisplay> data) {
		this.data = data;
	}
	
	public void add(ProfileDisplay profile) {
		data.add(profile);
	}
	
	public void remove(int position) {
		data.remove(position);
	}
	
	public ArrayList<ProfileDisplay> getData() {
		return data;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public ProfileDisplay getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public static class ViewHolder {
		public TextView name;
		public TextView company;
	}
	
	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(iLayout, null);
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.company = (TextView) vi.findViewById(R.id.company);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		
		profile = data.get(position);
		
		if (profile != null) {
			String strName = profile.getFirstName();
			if (strName != null && strName.length() > 0)  {
				strName += " ";
			} else {
				strName = "";
			}
			strName += profile.getLastName();
			holder.name.setText(strName);
			holder.company.setText("["+profile.getContactType()+"] " + profile.getCompanyName());
		}
		return vi;
	}

}
