package com.trinerva.icrm.event;

import java.util.List;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.calendar.CalendarData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InviteeAllAdapter extends ArrayAdapter<InviteeData> {

	LayoutInflater inflater;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		int type = getItemViewType(position);
		final InviteeData data = getItem(position);
		System.out.println("type = "+type);
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.item_week_title,
						parent, false);

				holder.mDayTitle = (TextView) convertView
						.findViewById(R.id.dayTitle);
				convertView.setTag(holder);
				break;
			case TYPE_2:
				if (!data.strBy.equals("user")){
				convertView = inflater.inflate(R.layout.lead_item, parent,
						false);
				holder.mAccount = (TextView) convertView
						.findViewById(R.id.company_name);
				holder.mSubject = (TextView) convertView
						.findViewById(R.id.contact_name);
				holder.cSync = (ImageView) convertView
						.findViewById(R.id.contact_sync);
				}else{
					convertView = inflater.inflate(R.layout.company_item, parent,
							false);
					holder.mSubject = (TextView) convertView
							.findViewById(R.id.company_name);
					holder.mAccount = (TextView) convertView
							.findViewById(R.id.company_name);
				}
				convertView.setTag(holder);
				
				break;
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		switch (type) {
		case TYPE_1:
			holder.mDayTitle.setText(data.subject);
			break;
		case TYPE_2:
			
			if (!data.strBy.equals("user")){
			if(data.account != null){
			holder.mAccount.setText(data.account);
			}
			holder.mSubject.setText(data.subject);
			//holder.cSync.setVisibility(View.GONE);
			}else{
				holder.mSubject.setText(data.subject);
			}
			break;
		}


		return convertView;
	}

	public InviteeAllAdapter(Context context, List<InviteeData> data) {
		super(context, 0, data);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		InviteeData data = getItem(position);
		if (data.isTitle)
			return TYPE_1;
		else
			return TYPE_2;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	class ViewHolder {
		TextView mDay, mDayTitle, mStartDate, mSubject, mAccount,mUserName;
		ImageView cSync;
		LinearLayout linearLayout;
	}

}
