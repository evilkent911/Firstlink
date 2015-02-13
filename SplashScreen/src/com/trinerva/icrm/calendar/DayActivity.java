package com.trinerva.icrm.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.event.EventInfo;
import com.trinerva.icrm.event.WeekAdapter;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DayActivity extends FragmentActivity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadList(allDate);
	}

	// Button mDayButton1, mDayButton3, mDayButton5;
	ImageView mDayButton2, mDayButton4;
	DayAdapter dayAdapter;
	Button mHideCalendar;
	// List<CalendarData> dayList;
	ListView mDayList;
	CalendarData mDay;
	Calendar calendarDay;
	Context context;

	List<CalendarData> weekList;
	CalendarData week;
	WeekAdapter weekAdapter;

	Date allDate;
	Calendar dayCal;
	TextView mDays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_view);

		context = this;
		weekList = new ArrayList<CalendarData>();

		// dayAdapter = new DayAdapter(context, dayList);
		mDayList = (ListView) findViewById(R.id.dayList);
		// mDayList.setAdapter(dayAdapter);

		// mDayButton1 = (Button) findViewById(R.id.dayButton1);
		mDayButton2 = (ImageView) findViewById(R.id.dayButton2);
		// mDayButton3 = (Button) findViewById(R.id.dayButton3);
		mDayButton4 = (ImageView) findViewById(R.id.dayButton4);
		// mDayButton5 = (Button) findViewById(R.id.dayButton5);
		mDays = (TextView) findViewById(R.id.dayDate);

		dayCal = Calendar.getInstance();

		mDays.setText(dataDay(dayCal.getTime()));

		calendarDay = Calendar.getInstance();

		allDate = calendarDay.getTime();
		// calendarDay.add(Calendar.DAY_OF_MONTH, -2);
		// mDayButton1.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton2.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton3.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton4.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton5.setText(getDay(calendarDay.getTime()));

		mDayButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dayCal.add(Calendar.DAY_OF_MONTH, -1);
				allDate = dayCal.getTime();
				loadList(dayCal.getTime());
				mDays.setText(dataDay(dayCal.getTime()));
			}
		});

		mDayButton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dayCal.add(Calendar.DAY_OF_MONTH, 1);
				allDate = dayCal.getTime();
				loadList(dayCal.getTime());
				mDays.setText(dataDay(dayCal.getTime()));
			}
		});

		mDays.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "timePicker");
			}
		});
		// mDayButton1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// calendarDay.add(Calendar.DAY_OF_MONTH, -6);
		// mDayButton1.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton2.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton3.setText(getDay(calendarDay.getTime()));
		// loadList(calendarDay.getTime());
		// allDate = calendarDay.getTime();
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton4.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton5.setText(getDay(calendarDay.getTime()));
		// }
		// });

		// mDayButton2.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// calendarDay.add(Calendar.DAY_OF_MONTH, -5);
		// mDayButton1.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton2.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton3.setText(getDay(calendarDay.getTime()));
		// loadList(calendarDay.getTime());
		// allDate = calendarDay.getTime();
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton4.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton5.setText(getDay(calendarDay.getTime()));
		// }
		// });

		// mDayButton4.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// calendarDay.add(Calendar.DAY_OF_MONTH, -3);
		// mDayButton1.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton2.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton3.setText(getDay(calendarDay.getTime()));
		// loadList(calendarDay.getTime());
		// allDate = calendarDay.getTime();
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton4.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton5.setText(getDay(calendarDay.getTime()));
		// }
		// });

		// mDayButton5.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// calendarDay.add(Calendar.DAY_OF_MONTH, -2);
		// mDayButton1.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton2.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton3.setText(getDay(calendarDay.getTime()));
		// loadList(calendarDay.getTime());
		// allDate = calendarDay.getTime();
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton4.setText(getDay(calendarDay.getTime()));
		// calendarDay.add(Calendar.DAY_OF_MONTH, 1);
		// mDayButton5.setText(getDay(calendarDay.getTime()));
		// }
		// });
		// loadList();

		mDayList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (weekList.get(position).isTitle != true) {
					Intent info = new Intent(DayActivity.this, EventInfo.class);
					info.putExtra("ID", weekList.get(position).strInternalNum);
					info.putExtra("VIEW", true);
					info.putExtra("ACTIVE", weekList.get(position).strActive);
					startActivity(info);
				}

			}
		});

		if (!Utility.getConfigByText(DayActivity.this, Constants.DELETE_EVENT)
				.equals("0")) {
			mDayList.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {

					GuiUtility.alert(DayActivity.this, "",
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

									Dialog loading = GuiUtility
											.getLoadingDialog(
													DayActivity.this,
													false,
													getString(R.string.processing));
									DatabaseUtility
											.getDatabaseHandler(DayActivity.this);
									FLCalendar calendar = new FLCalendar(
											Constants.DBHANDLER);
									// calendar.delete(strEventId);
									calendar.delete(
											DayActivity.this,
											weekList.get(position).strInternalNum);
									loading.dismiss();
									// back to contact list.
									loadList(allDate);
								}
							});

					return true;
				}
			});

		}

	}

	void loadList(final Date date) {
		FLCalendar event = new FLCalendar(Constants.DBHANDLER);

		try {
			weekList.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Cursor cur = event
				.getCalendarListDisplay(android.text.format.DateFormat.format(
						"yyyy-MM-dd", date.getTime()).toString());
		System.out.println("mt count = " + cur.getCount());
		if (cur.getCount() != 0) {
			// week = new CalendarData();
			// week.dateDay = getWeekDay(date);
			// week.isTitle = true;
			// weekList.add(week);

			do {
				week = new CalendarData();

				if (cur.getString(cur.getColumnIndex("ALL_DAY")).equals("true")
						|| (!android.text.format.DateFormat
								.format("yyyy-MM-dd",
										StringToDate(cur.getString(cur
												.getColumnIndex("START_DATE"))))
								.toString()
								.equals(android.text.format.DateFormat.format(
										"yyyy-MM-dd", date.getTime())
										.toString()) && !android.text.format.DateFormat
								.format("yyyy-MM-dd",
										StringToDate(cur.getString(cur
												.getColumnIndex("END_DATE"))))
								.toString()
								.equals(android.text.format.DateFormat.format(
										"yyyy-MM-dd", date.getTime())
										.toString()))) {

					// week.startDate =
					// changeAllDayToDate(cur.getString(cur.getColumnIndex("START_DATE")));
					week.startDate = getString(R.string.all_day);
				} else if (!android.text.format.DateFormat
						.format("yyyy-MM-dd",
								StringToDate(cur.getString(cur
										.getColumnIndex("START_DATE"))))
						.toString()
						.equals(android.text.format.DateFormat.format(
								"yyyy-MM-dd",
								StringToDate(cur.getString(cur
										.getColumnIndex("END_DATE"))))
								.toString())) {
					// Event more than 1 day
					if (android.text.format.DateFormat
							.format("yyyy-MM-dd",
									StringToDate(cur.getString(cur
											.getColumnIndex("START_DATE"))))
							.toString()
							.equals(android.text.format.DateFormat.format(
									"yyyy-MM-dd", date.getTime())
									.toString())) {
						// Show Start Time until 00:00

						week.startDate = data(
								stringToDate(cur.getString(cur
										.getColumnIndex("START_DATE")),
										"yyyy-MM-dd HH:mm"),
								"yyyy-MM-dd\nHH:mm-00:00");

					} else {
						// show 00:00 to End Time
						week.startDate = data(
								stringToDate(cur.getString(cur
										.getColumnIndex("END_DATE")),
										"yyyy-MM-dd HH:mm"),
								"yyyy-MM-dd\n00:00-HH:mm");
					}

				} else {
					week.startDate = changeToDate(
							cur.getString(cur.getColumnIndex("START_DATE")),
							cur.getString(cur.getColumnIndex("END_DATE")));
				}
				week.locaion = cur.getString(cur.getColumnIndex("LOCATION"));
				week.weekTask = cur.getString(cur.getColumnIndex("SUBJECT"));
				week.strIsUpdate = cur.getString(cur
						.getColumnIndex("IS_UPDATE"));
				week.strActive = cur.getString(cur.getColumnIndex("ACTIVE"));
				week.strInternalNum = cur.getString(cur
						.getColumnIndex("INTERNAL_NUM"));

				String strFirstName = null;
				try {
					System.out.println("Last name = "
							+ cur.getString(cur
									.getColumnIndex("NAME_TO_LAST_NAME")));
					System.out.println("first name = "
							+ cur.getString(cur
									.getColumnIndex("NAME_TO_FIRST_NAME")));

					strFirstName = cur.getString(cur
							.getColumnIndex("NAME_TO_FIRST_NAME"));
					String strLastName = cur.getString(cur
							.getColumnIndex("NAME_TO_LAST_NAME"));
					String strNameToName = cur.getString(cur
							.getColumnIndex("NAME_TO_NAME"));
					if (strFirstName == null && strLastName == null) {
						week.nameTo = strNameToName;
					} else {
						if (strFirstName == null) {
							week.nameTo = strLastName;
						} else {
							week.nameTo = strFirstName + " " + strLastName;
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				weekList.add(week);
			} while (cur.moveToNext());

		}

		try {
			weekAdapter = new WeekAdapter(context, weekList);
			mDayList.setAdapter(weekAdapter);
		} catch (Exception e) {
			e.printStackTrace();
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

	String getWeekDay(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd MMM yyyy");

		return sdf.format(time);
	}

	String getDay(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE\ndd\nMMM");

		return sdf.format(time);
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

	String dataDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		return sdf.format(date).toString();
	}

	@SuppressLint("ValidFragment")
	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener, OnDateChangedListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			int year = dayCal.get(Calendar.YEAR);
			int month = dayCal.get(Calendar.MONTH);
			int day = dayCal.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			dayCal.set(year, month, day);
			allDate = dayCal.getTime();
			loadList(dayCal.getTime());
			mDays.setText(dataDay(dayCal.getTime()));
		}

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub

		}
	}
	
	Date stringToDate(String date,String format) {
		try {
			return new SimpleDateFormat(format, Locale.ENGLISH)
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
