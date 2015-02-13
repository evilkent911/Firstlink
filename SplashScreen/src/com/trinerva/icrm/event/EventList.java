package com.trinerva.icrm.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.calendar.DayActivity;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;

import android.app.ActivityGroup;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class EventList extends ActivityGroup {
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("calendarSelect");
		registerReceiver(this.broadcastReceiver, filter);
		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("calendarSelect")) {
				selectDate = intent.getStringExtra("date");
			}
		}
	};

	private String TAG = "EventList";
	private SimpleCursorAdapter adapter;
	private TextView add_new, empty_event_list;
	private EditText search;
	private ListView list;
	private Dialog loadingDialog;

	private TabHost tabHost;
	TextView tv, mTitle;
	
	String selectDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_list);

		try {
			tabHost = (TabHost) this.findViewById(R.id.tabHostId);
			tabHost.setup();
			tabHost.setup(getLocalActivityManager());
			Intent intent;
			intent = new Intent().setClass(this, EventListActivity.class);
			tabHost.addTab(tabHost.newTabSpec("tab_1")
					.setIndicator(createTabView("List")).setContent(intent));
			intent = new Intent().setClass(this, DayActivity.class);
			tabHost.addTab(tabHost.newTabSpec("tab_2")
					.setIndicator(createTabView("Day")).setContent(intent));
			intent = new Intent().setClass(this, WeekActivity.class);
			tabHost.addTab(tabHost.newTabSpec("tab_3")
					.setIndicator(createTabView("Week")).setContent(intent));
			intent = new Intent().setClass(this, MonthEventActivity.class);
			tabHost.addTab(tabHost.newTabSpec("tab_4")
					.setIndicator(createTabView("Month")).setContent(intent));
			tabHost.setCurrentTab(0);
			updateTabStyle(tabHost);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			tabHost.setOnTabChangedListener(new OnTabChangeListener() {
				@Override
				public void onTabChanged(String tabId) {
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					updateTabStyle(tabHost);
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		findViewById(R.id.add).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent event = new Intent(EventList.this, EventInfo.class);
				if(selectDate != null){
				event.putExtra("calendarAdd", true);
				System.out.println("PRESSED ADD AND SELECTED DATE IS = "+selectDate);
				System.out.println("selectDate = "+selectDate);
				event.putExtra("date", selectDate);
				}else{
					event.putExtra("calendarAdd", false);
				}
				
				EventList.this.startActivity(event);
			}
		});
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private View createTabView(String text) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_tab_indicator, null);
		tv = (TextView) view.findViewById(R.id.tv_tab);
		tv.setText(text);
		return view;
	}

	private void updateTabStyle(final TabHost mTabHost) {
		TabWidget tabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < tabWidget.getChildCount(); i++) {

			tv = (TextView) tabWidget.getChildAt(i).findViewById(R.id.tv_tab);
			if (mTabHost.getCurrentTab() == i) {

				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.white));
			} else {
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.white));
			}
		}
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// LoadEvent task = new LoadEvent();
	// task.execute();
	// }
	//
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// search.removeTextChangedListener(filterTextWatcher);
	// if (adapter != null) {
	// adapter.getCursor().close();
	// adapter = null;
	// }
	// }

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			String strActive = cursor
					.getString(cursor.getColumnIndex("ACTIVE"));
			Intent info = new Intent(EventList.this, EventInfo.class);
			info.putExtra("ID",
					cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			info.putExtra("VIEW", true);
			info.putExtra("ACTIVE", strActive);
			info.putExtra("OTHER_PAGE_TO", false);
			EventList.this.startActivity(info);
		}
	};

	private OnClickListener doAddEvent = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent event = new Intent(EventList.this, EventInfo.class);
			EventList.this.startActivity(event);
		}
	};

	// private class LoadEvent extends AsyncTask<String, Void, Cursor> {
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// loadingDialog = GuiUtility.getLoadingDialog(EventList.this, false, null);
	// }
	//
	// @Override
	// protected Cursor doInBackground(String... arg0) {
	// DatabaseUtility.getDatabaseHandler(EventList.this);
	// FLCalendar event = new FLCalendar(Constants.DBHANDLER);
	// return event.getCalendarListDisplay();
	// }
	//
	// @Override
	// protected void onPostExecute(Cursor result) {
	// super.onPostExecute(result);
	// adapter = new SimpleCursorAdapter(EventList.this, R.layout.event_item,
	// result, new String[] {"_id", "SUBJECT", "START_DATE", "IS_UPDATE",
	// "ACTIVE"}, new int[]{R.id.subject, R.id.start_date, R.id.event_sync});
	// adapter.setViewBinder(binder);
	// DeviceUtility.log(TAG, "cursor count: " + result.getCount());
	// adapter.setFilterQueryProvider(filter);
	// list.setAdapter(adapter);
	// String strSearch = search.getText().toString();
	// if (strSearch.length() > 0) {
	// adapter.getFilter().filter(strSearch);
	// }
	//
	// if (result.getCount() == 0) {
	// empty_event_list.setVisibility(View.VISIBLE);
	// } else {
	// empty_event_list.setVisibility(View.GONE);
	// }
	//
	// if (loadingDialog.isShowing()) {
	// loadingDialog.dismiss();
	// }
	// }
	//
	// private SimpleCursorAdapter.ViewBinder binder = new
	// SimpleCursorAdapter.ViewBinder() {
	//
	// @Override
	// public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
	// int viewId = view.getId();
	// switch(viewId) {
	// case R.id.subject:
	// TextView company = (TextView) view;
	// company.setText(cursor.getString(cursor.getColumnIndex("SUBJECT")));
	// break;
	// case R.id.start_date:
	// TextView start = (TextView) view;
	// start.setText(cursor.getString(cursor.getColumnIndex("START_DATE")));
	// break;
	// case R.id.event_sync:
	// ImageView cSync = (ImageView) view;
	// DeviceUtility.log(TAG, "IS_UPDATE: " +
	// cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
	// String strIsUpdate =
	// cursor.getString(cursor.getColumnIndex("IS_UPDATE"));
	// String strActive = cursor.getString(cursor.getColumnIndex("ACTIVE"));
	//
	// if (strActive.equalsIgnoreCase("0") &&
	// strIsUpdate.equalsIgnoreCase("true")) {
	// cSync.setVisibility(View.GONE);
	// } else {
	// cSync.setVisibility(View.VISIBLE);
	// if (strActive.equalsIgnoreCase("1")) {
	// cSync.setImageResource(R.drawable.btn_sync_deleted);
	// } else {
	// cSync.setImageResource(R.drawable.btn_sync);
	// }
	// }
	// break;
	// }
	// return true;
	// }
	// };
	// }

	// private TextWatcher filterTextWatcher = new TextWatcher() {
	//
	// public void afterTextChanged(Editable s) {
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// if (adapter != null) {
	// adapter.getFilter().filter(s);
	// }
	// }
	// };

	private FilterQueryProvider filter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			DeviceUtility.log(TAG, "filter: " + constraint);
			DatabaseUtility.getDatabaseHandler(EventList.this);
			FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
			Cursor cursor = calendar.getCalendarListDisplayByFilter(constraint
					.toString());
			return cursor;
		}
	};

}
