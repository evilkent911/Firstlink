package com.trinerva.icrm.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.trinerva.icrm.CrmLogin;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.SynchronizationData;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EventListActivity extends Activity {
	private String TAG = "EventList";
	private SimpleCursorAdapter adapter;
	private TextView empty_event_list;
	// ImageView add_new;
	private EditText search;
	private ListView list;
	private Dialog loadingDialog;
	SaveData saveData;
	TextView mUnSyncText;
	Button mSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		// add_new = (ImageView) findViewById(R.id.add_new);
		saveData = new SaveData(this);
		empty_event_list = (TextView) findViewById(R.id.empty_event_list);
		search = (EditText) findViewById(R.id.search);
		list = (ListView) findViewById(R.id.list);
		search.addTextChangedListener(filterTextWatcher);
		mUnSyncText = (TextView) findViewById(R.id.unsyncText);
		mSync = (Button) findViewById(R.id.syn);

		// add_new.setOnClickListener(doAddEvent);
		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);

		if (!Utility.getConfigByText(EventListActivity.this,
				Constants.DELETE_EVENT).equals("0")) {
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {

					GuiUtility.alert(EventListActivity.this, "",
							getString(R.string.confirm_delete_event),
							Gravity.CENTER, getString(R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}, getString(R.string.delete),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Cursor cursor = adapter.getCursor();
									cursor.moveToPosition(position);

									Dialog loading = GuiUtility
											.getLoadingDialog(
													EventListActivity.this,
													false,
													getString(R.string.processing));
									DatabaseUtility
											.getDatabaseHandler(EventListActivity.this);
									FLCalendar calendar = new FLCalendar(
											Constants.DBHANDLER);
									// calendar.delete(strEventId);
									calendar.delete(
											EventListActivity.this,
											cursor.getString(cursor
													.getColumnIndex("INTERNAL_NUM")));
									loading.dismiss();
									// back to contact list.
									LoadEvent task = new LoadEvent();
									task.execute();
								}
							});

					return true;
				}
			});

		}

		mSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saveData.getBooleanData(GetData.checkLogin)) {
					v.setClickable(false);
					Intent sync = new Intent(EventListActivity.this,
							SynchronizationData.class);
					Bundle bFlag = new Bundle();
					bFlag.putString("ACTION", "NORMAL");
					bFlag.putString("EMAIL",
							saveData.getStringData(GetData.getUserEmail));
					bFlag.putString("PASSWORD",
							saveData.getStringData(GetData.getUserPassword));
					bFlag.putString("FROM", "CRMLOGIN");
					sync.putExtras(bFlag);
					EventListActivity.this.startActivity(sync);
				} else {
					Intent contact = new Intent(EventListActivity.this,
							CrmLogin.class);
					startActivity(contact);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSync.setClickable(true);
		System.out.println("on Resume el");
		LoadEvent task = new LoadEvent();
		task.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		search.removeTextChangedListener(filterTextWatcher);
		if (adapter != null) {
			adapter.getCursor().close();
			adapter = null;
		}
	}

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			String strActive = cursor
					.getString(cursor.getColumnIndex("ACTIVE"));
			Intent info = new Intent(EventListActivity.this, EventInfo.class);
			System.out.println("ID = "
					+ cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			// System.out.println("NAME_TO_NAME = "+
			// cursor.getString(cursor.getColumnIndex("NAME_TO_NAME")));
			info.putExtra("ID",
					cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			// info.putExtra("NAME_TO_NAME",
			// cursor.getString(cursor.getColumnIndex("NAME_TO_NAME")).toString());
			
			if(cursor.getString(cursor.getColumnIndex("OWNER")).equals(Utility.getConfigByText(EventListActivity.this,Constants.USER_EMAIL))){
				info.putExtra("ACTIVE", strActive);
			}else{
				info.putExtra("ACTIVE", "1");
			}
			info.putExtra("VIEW", true);
		
			EventListActivity.this.startActivity(info);
		}
	};

	private OnClickListener doAddEvent = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent event = new Intent(EventListActivity.this, EventInfo.class);
			EventListActivity.this.startActivity(event);
		}
	};

	private class LoadEvent extends AsyncTask<String, Void, Cursor> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventListActivity.this,
					false, null);
			if (getUnsyncData()) {
				mUnSyncText.setVisibility(View.GONE);
			} else {
				mUnSyncText.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(EventListActivity.this);
			FLCalendar event = new FLCalendar(Constants.DBHANDLER);
			return event.getCalendarListDisplay();
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			adapter = new SimpleCursorAdapter(EventListActivity.this,
					R.layout.event_item, result, new String[] { "_id",
							"SUBJECT", "START_DATE", "IS_UPDATE", "ACTIVE" },
					new int[] { R.id.subject, R.id.start_date, R.id.event_sync,
							R.id.nameTo, R.id.location });
			adapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
			adapter.setFilterQueryProvider(filter);
			list.setAdapter(adapter);
			String strSearch = search.getText().toString();
			if (strSearch.length() > 0) {
				adapter.getFilter().filter(strSearch);
			}

			if (result.getCount() == 0) {
				empty_event_list.setVisibility(View.VISIBLE);
			} else {
				empty_event_list.setVisibility(View.GONE);
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int viewId = view.getId();

				switch (viewId) {
				case R.id.subject:

					TextView company = (TextView) view;
					company.setText("");
//					String location = cursor.getString(cursor
//							.getColumnIndex("LOCATION"));
//
//					if (location != null && location.length() != 0) {
//						company.setText(getString(
//								R.string.subject_and_location, cursor
//										.getString(cursor
//												.getColumnIndex("SUBJECT")),
//								location));
//					} else {
						company.setText(cursor.getString(cursor
								.getColumnIndex("SUBJECT")));
//					}
					break;
				case R.id.start_date:
					TextView start = (TextView) view;
					if (cursor.getString(cursor.getColumnIndex("ALL_DAY"))
							.equals("true")) {

						if (stringToDate(
								cursor.getString(cursor
										.getColumnIndex("START_DATE")),
								"dd-MM-yyyy").equals(
								stringToDate(cursor.getString(cursor
										.getColumnIndex("END_DATE")),
										"dd-MM-yyyy"))) {
							start.setText(changeAllDayToDate(cursor
									.getString(cursor
											.getColumnIndex("START_DATE"))));
						} else {
							start.setText(changeAllDayToDateRange(cursor
									.getString(cursor
											.getColumnIndex("START_DATE")),
									cursor.getString(cursor
											.getColumnIndex("END_DATE"))));
							// start.setText(changeToDateRange(cursor
							// .getString(cursor
							// .getColumnIndex("START_DATE")),
							// cursor.getString(cursor
							// .getColumnIndex("END_DATE"))));
						}

					} else {
						if (stringToDate(
								cursor.getString(cursor
										.getColumnIndex("START_DATE")),
								"dd-MM-yyyy").equals(
								stringToDate(cursor.getString(cursor
										.getColumnIndex("END_DATE")),
										"dd-MM-yyyy"))) {
							start.setText(changeToDate(cursor.getString(cursor
									.getColumnIndex("START_DATE")), cursor
									.getString(cursor
											.getColumnIndex("END_DATE"))));
						} else {
							start.setText(changeToDateRange(cursor
									.getString(cursor
											.getColumnIndex("START_DATE")),
									cursor.getString(cursor
											.getColumnIndex("END_DATE"))));
						}
					}
					break;
				case R.id.event_sync:
					// ImageView cSync = (ImageView) view;
					// DeviceUtility.log(TAG, "IS_UPDATE: " +
					// cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
					// String strIsUpdate =
					// cursor.getString(cursor.getColumnIndex("IS_UPDATE"));
					// String strActive =
					// cursor.getString(cursor.getColumnIndex("ACTIVE"));
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
					break;
				case R.id.nameTo:
					TextView mNameTo = (TextView) view;
					mNameTo.setText("");
					try {
						System.out.println("Last name = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_LAST_NAME")));
						System.out.println("first name = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_FIRST_NAME")));

						String strFirstName = cursor.getString(cursor
								.getColumnIndex("NAME_TO_FIRST_NAME"));
						String strLastName = cursor.getString(cursor
								.getColumnIndex("NAME_TO_LAST_NAME"));

						if (strFirstName == null && strLastName == null) {

						} else {
							if (strFirstName == null) {
								strLastName = strLastName;
							} else {
								strLastName = strFirstName + " " + strLastName;
							}
							mNameTo.setText(strLastName);
						}

						// System.out.println("strLastName.length() = "+strLastName.length());
						System.out.println("name - = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_NAME")));
						if (strLastName == null) {
							String nameToName = cursor.getString(cursor
									.getColumnIndex("NAME_TO_NAME"));
							if (nameToName != null)
								mNameTo.setText(nameToName);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case R.id.location:
					TextView mLocation = (TextView) view;
					mLocation.setText("");
					mLocation.setText(cursor.getString(cursor
							.getColumnIndex("LOCATION")));
					break;
				}
				return true;
			}
		};
	}

	boolean getUnsyncData() {
		FLCalendar lead = new FLCalendar(Constants.DBHANDLER);
		Cursor cursor = lead.getCalendarListDisplay();
		do {
			if (cursor.getCount() != 0) {
				if (cursor.getString(cursor.getColumnIndex("IS_UPDATE"))
						.equals("false")) {
					return false;
				}
			}
		} while (cursor.moveToNext());
		return true;
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (adapter != null) {
				adapter.getFilter().filter(s);
			}
		}
	};

	private FilterQueryProvider filter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			DeviceUtility.log(TAG, "filter: " + constraint);
			DatabaseUtility.getDatabaseHandler(EventListActivity.this);
			FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
			Cursor cursor = calendar.getCalendarListDisplayByFilter(constraint
					.toString());
			return cursor;
		}
	};

	String changeToDate(String date, String endDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
		Date d = null;
		Date end = null;
		try {
			d = dateFormat.parse(date);
			end = dateFormat.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy\nh:mm");

		SimpleDateFormat dateFormat3 = new SimpleDateFormat("h:mm");

		return dateFormat2.format(d).toString() + "-" + dateFormat3.format(end);
	}

	String changeToDateRange(String date, String endDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
		Date d = null;
		Date end = null;
		try {
			d = dateFormat.parse(date);
			end = dateFormat.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy\nh:mm");

		SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd-MM-yyyy\nh:mm");

		return dateFormat2.format(d).toString() + "\n to \n"
				+ dateFormat3.format(end);
	}

	String changeAllDayToDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
		Date d = null;
		try {
			d = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy\n");

		return dateFormat2.format(d).toString() + getString(R.string.all_day);
	}

	String changeAllDayToDateRange(String startDate, String endDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		Date end = null;
		try {
			d = dateFormat.parse(startDate);
			end = dateFormat.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

		SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd-MM-yyyy");

		// return dateFormat2.format(d).toString() +
		// getString(R.string.all_day);
		return dateFormat2.format(d).toString() + "\n to \n"
				+ dateFormat3.format(end) + "\n" + getString(R.string.all_day);
	}

	Date stringToDate(String date, String format) {
		try {
			return new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	String dataToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date).toString();
	}
}
