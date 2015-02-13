package com.trinerva.icrm.calendar;

import java.util.List;

import asia.firstlink.icrm.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DayAdapter extends ArrayAdapter<CalendarData> {

	LayoutInflater inflater;
	TextView txt1;

	public DayAdapter(Context context, List<CalendarData> data) {
		super(context, 0, data);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
//		convertView = null;
		if (convertView == null) {
			holder = new ViewHolder();
			// if (getItemViewType(position) == 0) {
			convertView = inflater.inflate(R.layout.item_day, parent, false);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.layoutTask);
			holder.mHour = (TextView) convertView.findViewById(R.id.hour);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CalendarData data = getItem(position);
		holder.mHour.setText(data.dateDayHour);

		if(data.task.size() == 0){
			txt1 = null;
		}
		for (int i = 0; i < data.task.size(); i++) {
			LinearLayout.LayoutParams layoutParams;
			layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(10, 10, 10, 10);
			txt1 = new TextView(getContext());
			txt1.setText(data.task.get(i).toString());
			txt1.setTextColor(Color.parseColor("#404447"));
			txt1.setBackgroundColor(Color.parseColor("#f4d0cd"));
			txt1.setLayoutParams(layoutParams);
			holder.linearLayout.addView(txt1);
		}

		return convertView;
	}

	class ViewHolder {
		public TextView mHour;
		public LinearLayout linearLayout;
	}

}
