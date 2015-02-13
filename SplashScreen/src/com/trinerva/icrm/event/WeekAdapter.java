package com.trinerva.icrm.event;

import java.util.List;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.calendar.CalendarData;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeekAdapter extends ArrayAdapter<CalendarData> {

	LayoutInflater inflater;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		int type = getItemViewType(position);
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
				convertView = inflater.inflate(R.layout.event_item, parent,
						false);
				holder.mStartDate = (TextView) convertView
						.findViewById(R.id.start_date);
				holder.mSubject = (TextView) convertView
						.findViewById(R.id.subject);
				holder.mNameTo = (TextView) convertView
						.findViewById(R.id.nameTo);
				holder.mLocation = (TextView) convertView
						.findViewById(R.id.location);
				holder.cSync = (ImageView) convertView
						.findViewById(R.id.event_sync);
				convertView.setTag(holder);
				break;
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CalendarData data = getItem(position);

		switch (type) {
		case TYPE_1:
			holder.mDayTitle.setText(data.dateDay);
			break;
		case TYPE_2:
			holder.mStartDate.setText(data.startDate);
			holder.mSubject.setText(data.weekTask);
			holder.mNameTo.setText(data.nameTo);
			String strIsUpdate = data.strIsUpdate;
			String strActive = data.strActive;
			holder.mLocation.setText(data.locaion);
			System.out.println("strIsUpdate = ="+strIsUpdate);
			System.out.println("strActive = ="+strActive);

//			if (strActive.equalsIgnoreCase("0")
//					&& strIsUpdate.equalsIgnoreCase("true")) {
//				holder.cSync.setVisibility(View.GONE);
//			} else {
//				holder.cSync.setVisibility(View.VISIBLE);
//				if (strActive.equalsIgnoreCase("1")) {
//					holder.cSync.setImageResource(R.drawable.btn_sync_deleted);
//				} else {
//					holder.cSync.setImageResource(R.drawable.btn_sync);
//				}
//			}

			break;
		}

//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent info = new Intent(getContext(), EventInfo.class);
//				info.putExtra("ID", data.strInternalNum);
//				info.putExtra("VIEW", true);
//				info.putExtra("ACTIVE", data.strActive);
//				getContext().startActivity(info);
//			}
//		});
		// if (convertView == null) {
		// holder = new ViewHolder();
		// // if (getItemViewType(position) == 0) {
		// convertView = inflater.inflate(R.layout.item_week, parent, false);
		// holder.linearLayout = (LinearLayout) convertView
		// .findViewById(R.id.layoutTask);
		// holder.mDay = (TextView) convertView.findViewById(R.id.day);
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		// CalendarData data = getItem(position);
		// holder.mDay.setText(data.dateDay);

		// for (int i = 0; i < data.task.size(); i++) {
		// LinearLayout.LayoutParams layoutParams;
		// layoutParams = new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutParams.setMargins(10, 10, 10, 10);
		// TextView txt1 = new TextView(getContext());
		// txt1.setText(data.task.get(i).toString());
		// txt1.setTextColor(Color.parseColor("#404447"));
		// txt1.setBackgroundColor(Color.parseColor("#f4d0cd"));
		// txt1.setLayoutParams(layoutParams);
		// holder.linearLayout.addView(txt1);
		// }

		return convertView;
	}

	public WeekAdapter(Context context, List<CalendarData> data) {
		super(context, 0, data);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		CalendarData data = getItem(position);
		if (data.isTitle == true)
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
		TextView mDay, mDayTitle, mStartDate, mSubject,mNameTo,mLocation;
		ImageView cSync;
		LinearLayout linearLayout;
	}

}
