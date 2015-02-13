package com.trinerva.icrm.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.calendar.CalendarData;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeekActivity extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadList();
	}

	void loadList() {
		FLCalendar event = new FLCalendar(Constants.DBHANDLER);
		weekList.clear();
		for (int i = 0; i < 7; i++) {
			System.out.println(" storeDate.get(i)).toString() = "
					+ android.text.format.DateFormat.format("yyyy-MM-dd",
							storeDate.get(i)).toString());
			Cursor cur = event
					.getCalendarListDisplay(android.text.format.DateFormat
							.format("yyyy-MM-dd", storeDate.get(i)).toString());
			System.out.println("mt count = " + cur.getCount());
			if (cur.getCount() != 0) {
				 week = new CalendarData();
				 week.dateDay = getWeekDay(storeDate.get(i));
				 week.isTitle = true;
				 weekList.add(week);

				do {
					week = new CalendarData();

					if (cur.getString(cur.getColumnIndex("ALL_DAY")).equals(
							"true")
							|| (!android.text.format.DateFormat
									.format("yyyy-MM-dd",
											StringToDate(cur.getString(cur
													.getColumnIndex("START_DATE"))))
									.toString()
									.equals(android.text.format.DateFormat
											.format("yyyy-MM-dd",
													storeDate.get(i))
											.toString()) && !android.text.format.DateFormat
									.format("yyyy-MM-dd",
											StringToDate(cur.getString(cur
													.getColumnIndex("END_DATE"))))
									.toString()
									.equals(android.text.format.DateFormat
											.format("yyyy-MM-dd",
													storeDate.get(i))
											.toString()))) {


						week.startDate = getString(R.string.all_day);
					}
					else if (!android.text.format.DateFormat
							.format("yyyy-MM-dd",
									StringToDate(cur.getString(cur
											.getColumnIndex("START_DATE"))))
							.toString()
							.equals(android.text.format.DateFormat
							.format("yyyy-MM-dd",
									StringToDate(cur.getString(cur
											.getColumnIndex("END_DATE"))))
							.toString())){
						//Event more than 1 day
						if (android.text.format.DateFormat
						.format("yyyy-MM-dd",
								StringToDate(cur.getString(cur
										.getColumnIndex("START_DATE"))))
						.toString()
						.equals(android.text.format.DateFormat
								.format("yyyy-MM-dd",
										storeDate.get(i))
								.toString())) {
							//Show Start Time until 00:00
							

							week.startDate = data(stringToDate(cur.getString(cur.getColumnIndex("START_DATE")),"yyyy-MM-dd HH:mm"), "yyyy-MM-dd\nHH:mm-00:00");
							
						} else {
							//show 00:00 to End Time
							week.startDate = data(stringToDate(cur.getString(cur.getColumnIndex("END_DATE")),"yyyy-MM-dd HH:mm"), "yyyy-MM-dd\n00:00-HH:mm");
						}
						
					}
					else {

						week.startDate = changeToDate(
								cur.getString(cur.getColumnIndex("START_DATE")),
								cur.getString(cur.getColumnIndex("END_DATE")));
					}
					week.weekTask = cur
							.getString(cur.getColumnIndex("SUBJECT"));
					week.strIsUpdate = cur.getString(cur
							.getColumnIndex("IS_UPDATE"));
					week.locaion = cur.getString(cur.getColumnIndex("LOCATION"));
					week.strActive = cur
							.getString(cur.getColumnIndex("ACTIVE"));
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
		}
		calendar.add(Calendar.WEEK_OF_MONTH, 2);

		weekAdapter = new WeekAdapter(context, weekList);
		mWeekList.setAdapter(weekAdapter);
	}

	// Button mButton1, mButton2, mButton3, mButton4, mButton5;

	ImageView mButton2, mButton4;
	ListView mWeekList;
	Calendar calendar;
	CalendarData week;
	List<CalendarData> weekList;
	Context context;
	WeekAdapter weekAdapter;
	List<Date> storeDate = new ArrayList<Date>();

	Calendar cal;
	TextView mWeek;
	String startDay;
	String endDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week_view);

		context = this;
		calendar = Calendar.getInstance();

		mWeek = (TextView) findViewById(R.id.dayDate);

		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		System.out.println("Start of this week:       " + cal.getTime());
		System.out
				.println("... in milliseconds:      " + cal.getTimeInMillis());
		startDay = data(cal.getTime());
		// cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.DATE, 6);
		System.out.println("end of this week:       " + cal.getTime());
		System.out
				.println("... in milliseconds:      " + cal.getTimeInMillis());
		endDay = data(cal.getTime());

		mWeek.setText(startDay + " - " + endDay);

		mWeekList = (ListView) findViewById(R.id.weekList);
		mWeekList.setFocusableInTouchMode(true);
		mWeekList.setCacheColorHint(0000000);
		mWeekList.setFastScrollEnabled(true);
		mWeekList.setVerticalFadingEdgeEnabled(true);

		// mButton1 = (Button) findViewById(R.id.button1);
		mButton2 = (ImageView) findViewById(R.id.dayButton2);
		// mButton3 = (Button) findViewById(R.id.button3);
		mButton4 = (ImageView) findViewById(R.id.dayButton4);
		// mButton5 = (Button) findViewById(R.id.button5);

		weekList = new ArrayList<CalendarData>();

		// calendar.add(Calendar.WEEK_OF_MONTH, -2);
		// mButton1.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton2.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton3.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton4.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton5.setText(convertWeekByDate(calendar.getTime()));

		// calendar.add(Calendar.WEEK_OF_MONTH, -2);

		FLCalendar event = new FLCalendar(Constants.DBHANDLER);

		for (int i = 0; i < 7; i++) {

			Calendar cle = getDayData(calendar.getTime());
			cle.add(Calendar.DAY_OF_MONTH, i);
			System.out.println("time = " + getWeekDay(cle.getTime()));
			storeDate.add(cle.getTime());
		}

		mWeekList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// FLCalendar event = new FLCalendar(Constants.DBHANDLER);
				// Cursor cursor = event.getCalendarListDisplay();
				// cursor.moveToPosition(position);
				// String strActive =
				// cursor.getString(cursor.getColumnIndex("ACTIVE"));
				// Intent info = new Intent(WeekActivity.this, EventInfo.class);
				// info.putExtra("ID",
				// cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
				// info.putExtra("VIEW", true);
				// info.putExtra("ACTIVE", strActive);
				// startActivity(info);
				if (weekList.get(position).isTitle != true) {
					Intent info = new Intent(WeekActivity.this, EventInfo.class);
					info.putExtra("ID", weekList.get(position).strInternalNum);
					info.putExtra("VIEW", true);
					info.putExtra("ACTIVE", weekList.get(position).strActive);
					startActivity(info);
				}

			}
		});

		mButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal.add(Calendar.WEEK_OF_YEAR, -1);
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				startDay = data(cal.getTime());
				Calendar mStartDate = cal;
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				endDay = data(cal.getTime());
				mWeek.setText(startDay + " - " + endDay);

				storeDate.clear();

				for (int i = 0; i < 7; i++) {

					Calendar cle = getDayData(mStartDate.getTime());
					cle.add(Calendar.DAY_OF_MONTH, i);
					storeDate.add(cle.getTime());
				}

				// ============
				// cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				// storeDate.clear();
				// calendar = cal;
				// calendar.set(Calendar.DAY_OF_WEEK,
				// calendar.getFirstDayOfWeek());
				// for (int i = 0; i < 7; i++) {
				//
				// Calendar cle = getDayData(calendar.getTime());
				// cle.add(Calendar.DAY_OF_MONTH, i);
				// storeDate.add(cle.getTime());
				// }
				//
				loadList();
			}
		});

		mButton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal.add(Calendar.WEEK_OF_YEAR, 1);
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

				Calendar mStartDate = cal;
				startDay = data(cal.getTime());
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				endDay = data(cal.getTime());
				mWeek.setText(startDay + " - " + endDay);

				// ==================

				storeDate.clear();
				// calendar = cal;
				// calendar.set(Calendar.DAY_OF_WEEK,
				// calendar.getFirstDayOfWeek());
				for (int i = 0; i < 7; i++) {

					Calendar cle = getDayData(mStartDate.getTime());
					cle.add(Calendar.DAY_OF_MONTH, i);
					storeDate.add(cle.getTime());
				}
				//
				loadList();

				// cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				// storeDate.clear();
				// calendar = cal;
				// for (int i = 0; i < 7; i++) {
				//
				// Calendar cle = getDayData(calendar.getTime());
				// cle.add(Calendar.DAY_OF_MONTH, i);
				// System.out.println("time = " + getWeekDay(cle.getTime()));
				// storeDate.add(cle.getTime());
				// }
				//
				// loadList();
			}
		});

		if (!Utility.getConfigByText(WeekActivity.this, Constants.DELETE_EVENT)
				.equals("0")) {
			mWeekList.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {

					GuiUtility.alert(WeekActivity.this, "",
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
													WeekActivity.this,
													false,
													getString(R.string.processing));
									DatabaseUtility
											.getDatabaseHandler(WeekActivity.this);
									FLCalendar calendar = new FLCalendar(
											Constants.DBHANDLER);
									// calendar.delete(strEventId);
									calendar.delete(
											WeekActivity.this,
											weekList.get(position).strInternalNum);
									loading.dismiss();
									// back to contact list.

									loadList();
								}
							});

					return true;
				}
			});

		}
		// calendar.add(Calendar.WEEK_OF_MONTH, 2);
		//
		// weekAdapter = new WeekAdapter(context, weekList);
		// mWeekList.setAdapter(weekAdapter);
		//
		// mButton1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -6);
		// mButton1.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton2.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton3.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton4.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton5.setText(convertWeekByDate(calendar.getTime()));
		//
		// weekList.clear();
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -2);
		// week = null;
		// for (int i = 0; i < 7; i++) {
		//
		// Calendar cle = getDayData(calendar.getTime());
		// cle.add(Calendar.DAY_OF_MONTH, i);
		// System.out.println("time = " + getWeekDay(cle.getTime()));
		//
		// week = new CalendarData();
		// week.dateDay = getWeekDay(cle.getTime());
		// week.isTitle = true;
		// weekList.add(week);
		//
		// for (int j = 0; j < 3; j++) {
		// week = new CalendarData();
		// week.weekTask = "Test 10:30 AM ~ 11.30 AM";
		// weekList.add(week);
		// }
		//
		// }
		//
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, 2);
		// weekAdapter.notifyDataSetChanged();
		// }
		// });
		//
		// mButton2.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -5);
		// mButton1.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton2.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton3.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton4.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton5.setText(convertWeekByDate(calendar.getTime()));
		//
		// weekList.clear();
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -2);
		// week = null;
		// for (int i = 0; i < 7; i++) {
		//
		// Calendar cle = getDayData(calendar.getTime());
		// cle.add(Calendar.DAY_OF_MONTH, i);
		// System.out.println("time = " + getWeekDay(cle.getTime()));
		//
		// week = new CalendarData();
		// week.dateDay = getWeekDay(cle.getTime());
		// week.isTitle = true;
		// weekList.add(week);
		//
		// for (int j = 0; j < 3; j++) {
		// week = new CalendarData();
		// week.weekTask = "Test 10:30 AM 11.30 AM";
		// weekList.add(week);
		// }
		//
		// }
		// calendar.add(Calendar.WEEK_OF_MONTH, 2);
		// weekAdapter.notifyDataSetChanged();
		// }
		// });
		//
		// mButton3.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		//
		// mButton4.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// calendar.add(Calendar.WEEK_OF_MONTH, -3);
		// mButton1.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton2.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton3.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton4.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton5.setText(convertWeekByDate(calendar.getTime()));
		//
		// weekList.clear();
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -2);
		// week = null;
		// for (int i = 0; i < 7; i++) {
		//
		// Calendar cle = getDayData(calendar.getTime());
		// cle.add(Calendar.DAY_OF_MONTH, i);
		// System.out.println("time = " + getWeekDay(cle.getTime()));
		//
		// week = new CalendarData();
		// week.dateDay = getWeekDay(cle.getTime());
		// week.isTitle = true;
		// weekList.add(week);
		//
		// for (int j = 0; j < 3; j++) {
		// week = new CalendarData();
		// week.weekTask = "Test 10:30 AM ~ 11.30 AM";
		// weekList.add(week);
		// }
		//
		// }
		// calendar.add(Calendar.WEEK_OF_MONTH, 2);
		// weekAdapter.notifyDataSetChanged();
		//
		// }
		// });
		//
		// mButton5.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -2);
		// mButton1.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton2.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton3.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton4.setText(convertWeekByDate(calendar.getTime()));
		// calendar.add(Calendar.WEEK_OF_MONTH, 1);
		// mButton5.setText(convertWeekByDate(calendar.getTime()));
		//
		// weekList.clear();
		//
		// calendar.add(Calendar.WEEK_OF_MONTH, -2);
		// week = null;
		// for (int i = 0; i < 7; i++) {
		//
		// Calendar cle = getDayData(calendar.getTime());
		// cle.add(Calendar.DAY_OF_MONTH, i);
		// System.out.println("time = " + getWeekDay(cle.getTime()));
		//
		// week = new CalendarData();
		// week.dateDay = getWeekDay(cle.getTime());
		// week.isTitle = true;
		// weekList.add(week);
		//
		// for (int j = 0; j < 3; j++) {
		// week = new CalendarData();
		// week.weekTask = "Test 10:30 AM ~ 11.30 AM";
		// weekList.add(week);
		// }
		//
		// }
		// calendar.add(Calendar.WEEK_OF_MONTH, 2);
		// weekAdapter.notifyDataSetChanged();
		//
		// }
		// });
	}

	String getWeekDay(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd MMM yyyy");

		return sdf.format(time);
	}

	Calendar getDayData(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE\ndd");

		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断�?计算的日期是�?�是周日，如果是则�?一天计算周六的，�?�则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当�?日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当�?日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

		return cal;
	}

	private String convertWeekByDate(Date time) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd"); // 设置时间格�?
		SimpleDateFormat monet = new SimpleDateFormat("MMM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断�?计算的日期是�?�是周日，如果是则�?一天计算周六的，�?�则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当�?日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		System.out.println("�?计算日期为:" + sdf.format(cal.getTime())); // 输出�?计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当�?日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根�?�日历的规则，给当�?日期�?去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());
		String monetBegin = monet.format(cal.getTime());
		System.out.println("所在周星期一的日期：" + imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		String monetEnd = monet.format(cal.getTime());
		System.out.println("所在周星期日的日期：" + imptimeEnd);

		if (monetBegin.equals(monetEnd)) {
			return monetBegin + "\n" + imptimeBegin + " - " + imptimeEnd;
		} else {
			// mButton1.setText(cal.getTime().getDate()+" - "+cal.getTime().getDate());
			return monetBegin + "-" + monetEnd + "\n" + imptimeBegin + " - "
					+ imptimeEnd;
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

	String data(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		return sdf.format(date).toString();
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