package com.trinerva.icrm.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.calendar.CalendarAdapter;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MonthEventActivity extends Activity {

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter calendarAdapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker

	GridView gridview;

	TextView title;
	ImageView arrow;
	private ListView list;

	private SimpleCursorAdapter adapter;
	private Dialog loadingDialog;
	String selectDate;
	LinearLayout mHideCalendarLayout;

	String gSelectPosition;

	int day;
LinearLayout mTitleBar;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_event_calendar);
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		// System.out.println("day 1 = "+android.text.format.DateFormat.format("dd MMMM yyyy",
		// month));
		// System.out.println("day 2 = "+android.text.format.DateFormat.format("dd MMMM yyyy",
		// itemmonth));

		selectDate = android.text.format.DateFormat.format("yyyy-MM-dd", month)
				.toString();

		day = Integer.parseInt(android.text.format.DateFormat.format("dd",
				month).toString());

		System.out.println("op = " + selectDate);
		items = new ArrayList<String>();

		calendarAdapter = new CalendarAdapter(this, month);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(calendarAdapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		mHideCalendarLayout = (LinearLayout) findViewById(R.id.hideCalendarLayout);
		title = (TextView) findViewById(R.id.title);
		arrow = (ImageView) findViewById(R.id.hideCalendar);
		mTitleBar = (LinearLayout) findViewById(R.id.titleBar);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				gSelectPosition = selectedGridDate;
			    showToast(selectedGridDate);
				selectDate = selectedGridDate;
				LoadEvent task = new LoadEvent();
				task.execute();

			}
		});

		findViewById(R.id.title).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// final int year = month.get(month.YEAR);
				// final int months = month.get(month.MONTH);
				// final int day = month.get(month.DAY_OF_MONTH);
				//
				System.out.println("day = "
						+ android.text.format.DateFormat.format("MMMM yyyy",
								itemmonth));
				// System.out.println("month = "+new
				// Date(month.getTimeInMillis()).getMonth());
				// System.out.println("year = "+new
				// Date(month.getTimeInMillis()).getYear());

				DatePickerDialog dateDlg = new DatePickerDialog(
						MonthEventActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// Time chosenDate = new Time();
								// chosenDate.set(dayOfMonth, monthOfYear,
								// year);
								// long dtDob = chosenDate.toMillis(true);
								// CharSequence strDate = DateFormat.format(
								// Constants.DATE_FORMAT, dtDob);
								// birthday.setText(strDate);

								month.set(year, monthOfYear, dayOfMonth);
								title.setText(android.text.format.DateFormat
										.format("MMMM yyyy", month));
								day = dayOfMonth;

								selectDate = android.text.format.DateFormat
										.format("yyyy-MM-dd", month).toString();

								System.out
										.println("selectDate = " + selectDate);
								LoadEvent task = new LoadEvent();
								task.execute();

								calendarAdapter = new CalendarAdapter(
										MonthEventActivity.this, month);

								GridView gridview = (GridView) findViewById(R.id.gridview);
								gridview.setAdapter(calendarAdapter);
								refreshCalendar();

							}
						}, Integer.parseInt(android.text.format.DateFormat
								.format("yyyy", month).toString()), month
								.getTime().getMonth(), day);

				// dateDlg.setMessage(getString(R.string.birthday));
				dateDlg.show();
			}
		});

		list = (ListView) findViewById(R.id.list);
		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);
		if (!Utility.getConfigByText(MonthEventActivity.this,
				Constants.DELETE_EVENT).equals("0")) {
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {

					GuiUtility.alert(MonthEventActivity.this, "",
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
													MonthEventActivity.this,
													false,
													getString(R.string.processing));
									DatabaseUtility
											.getDatabaseHandler(MonthEventActivity.this);
									FLCalendar calendar = new FLCalendar(
											Constants.DBHANDLER);
									// calendar.delete(strEventId);
									calendar.delete(
											MonthEventActivity.this,
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

		arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mHideCalendarLayout.getVisibility() == View.GONE) {
					mHideCalendarLayout.setVisibility(View.VISIBLE);
					arrow.setImageResource(R.drawable.ic_btn_go_up);
					mTitleBar.setVisibility(View.GONE);
				} else {
					mHideCalendarLayout.setVisibility(View.GONE);
					arrow.setImageResource(R.drawable.ic_btn_go_down);
					mTitleBar.setVisibility(View.VISIBLE);
				}

			}
		});
	}

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			String strActive = cursor
					.getString(cursor.getColumnIndex("ACTIVE"));
			Intent info = new Intent(MonthEventActivity.this, EventInfo.class);
			info.putExtra("ID",
					cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			info.putExtra("VIEW", true);
			info.putExtra("ACTIVE", strActive);
			MonthEventActivity.this.startActivity(info);
		}
	};

	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		//Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setAction("calendarSelect");
		intent.putExtra("date", string);
		
		sendBroadcast(intent);

	}

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.title);

		calendarAdapter.refreshDays();
		calendarAdapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);

			}

			FLCalendar event = new FLCalendar(Constants.DBHANDLER);
			Cursor cursor = event.getCalendarListDisplay();
			System.out.println("=============Start===========");
			if (cursor.getCount() != 0) {
				do {
					Date startDate = stringToDate(
							changeToMarkDate(cursor.getString(cursor
									.getColumnIndex("START_DATE"))),
							"yyyy-MM-dd");

					Date endDate = stringToDate(
							changeToMarkDate(cursor.getString(cursor
									.getColumnIndex("END_DATE"))), "yyyy-MM-dd");

					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(startDate);
					cal2.setTime(endDate);

					int dayCount = (int) (cal2.getTimeInMillis() - cal1
							.getTimeInMillis()) / (1000 * 3600 * 24);

					System.out.println("day = " + dayCount);

					for (int j = 0; j < dayCount; j++) {
						cal1.add(Calendar.DAY_OF_MONTH, 1);
						if (!dateToString(cal1.getTime(), "yyyy-MM-dd")
								.equals(stringToDate(
										changeToMarkDate(cursor
												.getString(cursor
														.getColumnIndex("START_DATE"))),
										"yyyy-MM-dd"))
								&& !dateToString(cal1.getTime(), "yyyy-MM-dd")
										.equals(stringToDate(
												changeToMarkDate(cursor
														.getString(cursor
																.getColumnIndex("END_DATE"))),
												"yyyy-MM-dd"))) {
							items.add(dateToString(cal1.getTime(), "yyyy-MM-dd"));
						}
					}

					// System.out.println("Star = "
					// + changeToMarkDate(cursor.getString(cursor
					// .getColumnIndex("START_DATE"))));
					// System.out.println("Day = " +
					// itemmonth.getTime().getDay());
					// System.out.println("End = "
					// + changeToMarkDate(cursor.getString(cursor
					// .getColumnIndex("END_DATE"))));
					items.add(changeToMarkDate(cursor.getString(cursor
							.getColumnIndex("START_DATE"))));

				} while (cursor.moveToNext());
				// items.add("2014-05-26");
				calendarAdapter.setItems(items);
			}
			System.out.println("=============End===========");

			calendarAdapter.notifyDataSetChanged();
		}
	};

	private class LoadEvent extends AsyncTask<String, Void, Cursor> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(
					MonthEventActivity.this, false, null);
		}

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(MonthEventActivity.this);
			FLCalendar event = new FLCalendar(Constants.DBHANDLER);
			return event.getCalendarListDisplay(selectDate);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			System.out.println("result zz = " + result.getCount());
			
			adapter = new SimpleCursorAdapter(MonthEventActivity.this,
					R.layout.event_item, result, new String[] { "_id",
							"SUBJECT", "START_DATE", "IS_UPDATE", "ACTIVE" },
					new int[] { R.id.subject, R.id.start_date, R.id.event_sync,
							R.id.nameTo,R.id.location });
			adapter.setViewBinder(binder);
			// adapter.setFilterQueryProvider(filter);

			list.setAdapter(adapter);

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
					company.setText(cursor.getString(cursor
							.getColumnIndex("SUBJECT")));
					break;
				case R.id.start_date:
					TextView start = (TextView) view;
					if (cursor.getString(cursor.getColumnIndex("ALL_DAY"))
							.equals("true")) {
						start.setText(changeAllDayToDate(cursor
								.getString(cursor.getColumnIndex("START_DATE"))));
					} else if (!android.text.format.DateFormat
							.format("yyyy-MM-dd",
									StringToDate(cursor.getString(cursor
											.getColumnIndex("START_DATE"))))
							.toString()
							.equals(android.text.format.DateFormat.format(
									"yyyy-MM-dd",
									StringToDate(cursor.getString(cursor
											.getColumnIndex("END_DATE"))))
									.toString())) {
						// Event more than 1 day
						if (android.text.format.DateFormat
								.format("yyyy-MM-dd",
										StringToDate(cursor.getString(cursor
												.getColumnIndex("START_DATE"))))
								.toString()
								.equals(selectDate)) {
							// Show Start Time until 00:00

							start.setText(data(
									stringToDate(cursor.getString(cursor
											.getColumnIndex("START_DATE")),
											"yyyy-MM-dd HH:mm"),
									"yyyy-MM-dd\nHH:mm-00:00"));

						} else {
							// show 00:00 to End Time
							start.setText(data(
									stringToDate(cursor.getString(cursor
											.getColumnIndex("END_DATE")),
											"yyyy-MM-dd HH:mm"),
									"yyyy-MM-dd\n00:00-HH:mm"));
						}

					} else {
						start.setText(changeToDate(cursor.getString(cursor
								.getColumnIndex("START_DATE")), cursor
								.getString(cursor.getColumnIndex("END_DATE"))));
					}

					break;
				case R.id.nameTo:
					TextView mNameTo = (TextView) view;
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
						String strNameToName = cursor.getString(cursor
								.getColumnIndex("NAME_TO_NAME"));
//						if (strFirstName == null && strLastName == null) {
//							week.nameTo = strNameToName;
//						} else {
//							if (strFirstName == null) {
//								week.nameTo = strLastName;
//							} else {
//								week.nameTo = strFirstName + " " + strLastName;
//							}
//						}						
						if (strFirstName == null && strLastName == null) {
							mNameTo.setText(strNameToName);
						}else{
							if (strFirstName == null) {
								strLastName = strLastName;
							} else {
								strLastName = strFirstName + " " + strLastName;
							}
							mNameTo.setText(strLastName);
						}						

					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case R.id.event_sync:
					// ImageView cSync = (ImageView) view;
					// String strIsUpdate = cursor.getString(cursor
					// .getColumnIndex("IS_UPDATE"));
					// String strActive = cursor.getString(cursor
					// .getColumnIndex("ACTIVE"));
					//
					// if (strActive.equalsIgnoreCase("0")
					// && strIsUpdate.equalsIgnoreCase("true")) {
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
				case R.id.location:
					System.out.println("yyy - "+cursor.getString(cursor
										.getColumnIndex("LOCATION")));
					TextView mLocation = (TextView) view;
					mLocation.setText(cursor.getString(cursor
										.getColumnIndex("LOCATION")));
					break;
				}
				return true;
			}
		};
	}

	// private FilterQueryProvider filter = new FilterQueryProvider() {
	//
	// @Override
	// public Cursor runQuery(CharSequence constraint) {
	// DatabaseUtility.getDatabaseHandler(MonthEventActivity.this);
	// FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
	// Cursor cursor =
	// calendar.getCalendarListDisplayByFilter(constraint.toString(),selectDate);
	// return cursor;
	// }
	// };

	String changeToMarkDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
		java.util.Date d = null;
		try {
			d = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat2.format(d).toString();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (gSelectPosition == null) {
			LoadEvent task = new LoadEvent();
			task.execute();
			refreshCalendar();
		} else {
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd")
						.parse(gSelectPosition);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			month.set(Integer.parseInt(new SimpleDateFormat("yyyy")
					.format(date).toString()), Integer
					.parseInt(new SimpleDateFormat("MM").format(date)
							.toString()) - 1, Integer
					.parseInt(new SimpleDateFormat("dd").format(date)
							.toString()));
			title.setText(android.text.format.DateFormat.format("MMMM yyyy",
					month));

			selectDate = android.text.format.DateFormat.format("yyyy-MM-dd",
					month).toString();

			System.out.println("selectDate = " + selectDate);
			LoadEvent task = new LoadEvent();
			task.execute();

			calendarAdapter = new CalendarAdapter(MonthEventActivity.this,
					month);

			GridView gridview = (GridView) findViewById(R.id.gridview);
			gridview.setAdapter(calendarAdapter);
			refreshCalendar();

		}

	}

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

	Date stringToDate(String strDate, String toFormat) {
		SimpleDateFormat format = new SimpleDateFormat(toFormat);
		try {
			Date date = format.parse(strDate);
			System.out.println(date);

			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	String dateToString(Date strDate, String toFormat) {
		SimpleDateFormat format = new SimpleDateFormat(toFormat);
		return format.format(strDate).toString();
	}
	
	Date StringToDate(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
					.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	String data(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date).toString();
	}
}
