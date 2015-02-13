package com.trinerva.icrm.calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.event.EventInfo;
import com.trinerva.icrm.tasks.TaskInfo;
import com.trinerva.icrm.tasks.TaskList;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.GuiUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends Activity {

	MonthAdapter monthAdapter;
	List<CalendarData> monthList = new ArrayList<CalendarData>();
	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	public ArrayList<String> taskItems;

	private ListView list;

	String selectDate;

	Context context;

	CalendarData calendarTask;

	String gSelectPosition = null;
	View vSelected;

	TextView title;

	ImageView arrow;
	LinearLayout mHideCalendarLayout;
	int myDay;
	int myMonth;
	int myYear;
	GridView gridview;

	ArrayList<Integer> mSelectedItems = new ArrayList<Integer>(); // Where we
																	// track the
																	// selected

	Dialog loadingDialog;
	// items
	boolean[] bClick;
	String filterQuery = "";
	String categoryData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		context = this;

		selectDate = android.text.format.DateFormat.format("yyyy-MM-dd", month)
				.toString();

		myDay = Integer.parseInt(android.text.format.DateFormat.format("dd",
				month).toString());
		myMonth = Integer.parseInt(android.text.format.DateFormat.format("MM",
				month).toString());
		myYear = Integer.parseInt(android.text.format.DateFormat.format("yyyy",
				month).toString());

		String[] planets = getResources().getStringArray(
				R.array.string_task_filter);

		bClick = new boolean[planets.length];
		categoryData = "12,2,13";
		for (int i = 0; i < planets.length; i++) {
			bClick[i] = true;
			mSelectedItems.add(i);
		}

		// for (int i = 0; i < mSelectedItems.size(); i++) {
		// if(mSelectedItems.get(i).toString().equals("0")){
		// filterQuery += "CONTACT_ID IS NOT NULL AND A.CONTACT_ID <> ''";
		// }else if(mSelectedItems.get(i).toString().equals("1")){
		// filterQuery += "Call";
		// }if(mSelectedItems.get(i).toString().equals("2")){
		// filterQuery += "EMAIL";
		// }
		// }

		mHideCalendarLayout = (LinearLayout) findViewById(R.id.hideCalendarLayout);
		items = new ArrayList<String>();
		taskItems = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);
		arrow = (ImageView) findViewById(R.id.hideCalendar);
		list = (ListView) findViewById(R.id.list);
		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		title = (TextView) findViewById(R.id.title);
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
				vSelected = v;
				gSelectPosition = selectedGridDate;
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				// showToast(selectedGridDate);

				selectDate = selectedGridDate;

				monthList.clear();

				Task task = new Task(Constants.DBHANDLER);
				Cursor taskCursor = task.getTaskListDisplay(selectDate,
						categoryData);

				if (taskCursor.getCount() != 0) {
					calendarTask = new CalendarData();
					calendarTask.dateDay = getString(R.string.tasks);
					calendarTask.isTitle = true;
					calendarTask.type = 0;
					monthList.add(calendarTask);

					do {
						calendarTask = new CalendarData();
						calendarTask.type = 1;
						// calendarTask.startDate = convertTime(taskCursor
						// .getString(taskCursor
						// .getColumnIndex("DUE_DATE")))
						// + "-"
						// + convertTime(taskCursor.getString(taskCursor
						// .getColumnIndex("DUE_DATE")));
						calendarTask.monthTask = taskCursor
								.getString(taskCursor.getColumnIndex("SUBJECT"));
						calendarTask.strIsUpdate = taskCursor
								.getString(taskCursor
										.getColumnIndex("IS_UPDATE"));
						calendarTask.strActive = taskCursor
								.getString(taskCursor.getColumnIndex("ACTIVE"));
						calendarTask.strInternalNum = taskCursor
								.getString(taskCursor
										.getColumnIndex("INTERNAL_NUM"));
						monthList.add(calendarTask);
					} while (taskCursor.moveToNext());

				}

				FLCalendar event = new FLCalendar(Constants.DBHANDLER);
				Cursor cursor = event.getCalendarListDisplay(selectDate);

				if (cursor.getCount() != 0) {
					calendarTask = new CalendarData();
					calendarTask.dateDay = getString(R.string.events);
					calendarTask.isTitle = true;
					calendarTask.type = 0;
					monthList.add(calendarTask);

					do {
						calendarTask = new CalendarData();
						calendarTask.type = 2;
						if (cursor.getString(cursor.getColumnIndex("ALL_DAY"))
								.equals("true")) {
							calendarTask.startDate = getString(R.string.all_day);
						} else {
							calendarTask.startDate = convertTime(cursor
									.getString(cursor
											.getColumnIndex("START_DATE")))
									+ "-"
									+ convertTime(cursor.getString(cursor
											.getColumnIndex("END_DATE")));
						}
						calendarTask.monthTask = cursor.getString(cursor
								.getColumnIndex("SUBJECT"));
						calendarTask.strIsUpdate = cursor.getString(cursor
								.getColumnIndex("IS_UPDATE"));
						calendarTask.strActive = cursor.getString(cursor
								.getColumnIndex("ACTIVE"));
						calendarTask.strInternalNum = cursor.getString(cursor
								.getColumnIndex("INTERNAL_NUM"));

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
								calendarTask.nameToName = strLastName;
							}

							// System.out.println("strLastName.length() = "+strLastName.length());
							System.out.println("name - = "
									+ cursor.getString(cursor
											.getColumnIndex("NAME_TO_NAME")));
							if (strLastName == null) {
								String nameToName = cursor.getString(cursor
										.getColumnIndex("NAME_TO_NAME"));
								if (nameToName != null)
									calendarTask.nameToName = nameToName;
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						
						monthList.add(calendarTask);
					} while (cursor.moveToNext());
				}
				for (int i = 0; i < monthList.size(); i++) {
					System.out.println("now all = "+monthList.get(i).type);
				}
				System.out.println();
				monthAdapter = new MonthAdapter(context, monthList);
				list.setAdapter(monthAdapter);

			}
		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mHideCalendarLayout.getVisibility() == View.GONE) {
					mHideCalendarLayout.setVisibility(View.VISIBLE);
					arrow.setImageResource(R.drawable.ic_btn_go_up);
				} else {
					mHideCalendarLayout.setVisibility(View.GONE);
					arrow.setImageResource(R.drawable.ic_btn_go_down);
				}

			}
		});

		findViewById(R.id.title).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog dateDlg = new DatePickerDialog(
						CalendarActivity.this,
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
								myDay = dayOfMonth;
								myMonth = month.getTime().getMonth();
								myYear = monthOfYear;

								selectDate = android.text.format.DateFormat
										.format("yyyy-MM-dd", month).toString();

								LoadTaskDate dateTask = new LoadTaskDate();
								dateTask.execute();
								

							}
						}, Integer.parseInt(android.text.format.DateFormat
								.format("yyyy", month).toString()), month
								.getTime().getMonth(), myDay);

				// dateDlg.setMessage(getString(R.string.birthday));
				dateDlg.show();
			}
		});

		findViewById(R.id.add).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setItems(R.array.string_array_select,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// The 'which' argument contains the index
								// position
								// of the selected item

								switch (which) {
								case 0:
									addEvent();
									break;
								case 1:
									addTask();
									break;
								case 2:

									break;
								default:
									break;
								}
							}
						});
				builder.show();

			}
		});

		findViewById(R.id.target).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final boolean bDfClick[] = bClick;

				AlertDialog.Builder builder = new AlertDialog.Builder(
						CalendarActivity.this);
				// Set the dialog title
				builder.setTitle(R.string.show)
				// Specify the list array, the items to be selected by
				// default (null for none),
				// and the listener through which to receive callbacks
				// when items are selected
						.setMultiChoiceItems(
								R.array.string_task_filter,
								bClick,
								new DialogInterface.OnMultiChoiceClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										if (isChecked) {
											// If the user checked the item, add
											// it to the selected items
											mSelectedItems.add(which);
										} else {
											// // Else, if the item is already
											// in
											// // the array, remove it
											mSelectedItems.remove(Integer
													.valueOf(which));
										}
									}
								})
						// Set the action buttons
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										String sms;
										String call;
										String email;
										categoryData = "";
										for (int i = 0; i < mSelectedItems
												.size(); i++) {
											// bClick[mSelectedItems.get(i)] =
											// true;
											switch (mSelectedItems.get(i)) {
											case 0:
												categoryData += "12,";
												break;
											case 1:
												categoryData += "2,";
												break;
											case 2:
												categoryData += "13,";
												break;
											default:
												break;
											}
										}

										if (categoryData.length() > 0) {
											categoryData = categoryData
													.substring(0, categoryData
															.length() - 1);

										} else {
											categoryData = "0";
										}

										loadList();
									}
								});

				builder.show();

			}
		});
	}

	void addEvent() {

		Intent event = new Intent(CalendarActivity.this, EventInfo.class);
		event.putExtra("calendarAdd", true);
		event.putExtra("date", selectDate);
		CalendarActivity.this.startActivity(event);

	}

	void addTask() {
		Intent event = new Intent(CalendarActivity.this, TaskInfo.class);
		event.putExtra("calendarAdd", true);
		event.putExtra("date", selectDate);
		CalendarActivity.this.startActivity(event);
		// TODO Auto-generated method stub

	}

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (monthList.get(position).isTitle == false) {

				Intent info;
				switch (monthList.get(position).type) {
				case 0:
				
					break;
				case 1:
					String strActive = monthList.get(position).strActive;

					info = new Intent(CalendarActivity.this, TaskInfo.class);
					info.putExtra("ID", monthList.get(position).strInternalNum);
					info.putExtra("VIEW", true);
					info.putExtra("ACTIVE", strActive);
					startActivity(info);
					break;
				case 2:

					info = new Intent(CalendarActivity.this, EventInfo.class);
					info.putExtra("ID", monthList.get(position).strInternalNum);
					info.putExtra("VIEW", true);
					info.putExtra("ACTIVE", monthList.get(position).strActive);
					startActivity(info);
					break;
				default:
					break;
				}
				// Cursor cursor = adapter.getCursor();
				// cursor.moveToPosition(position);
				// String strActive =
				// cursor.getString(cursor.getColumnIndex("ACTIVE"));
				// Intent info = new Intent(MonthEventActivity.this,
				// EventInfo.class);
				// info.putExtra("ID",
				// cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
				// info.putExtra("VIEW", true);
				// info.putExtra("ACTIVE", strActive);
				// MonthEventActivity.this.startActivity(info);

			}
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
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			LoadTaskCalendar loadCalendar = new LoadTaskCalendar();
			loadCalendar.execute();
		}
	};

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

	String convertTime(String date) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyy-MM-dd h:mm");
		Date myDate = null;
		try {
			myDate = simpledateformat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat formatDate = new SimpleDateFormat("h:mm");

		return formatDate.format(myDate).toString();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadList();

		// if(vSelected != null){
		// ((CalendarAdapter) pp.getAdapter()).setSelected(vSelected);
		// }
	}

	void loadList() {

		if (gSelectPosition != null) {
			monthList.clear();
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
			selectDate = android.text.format.DateFormat.format("yyyy-MM-dd",
					month).toString();

			LoadTask myTask = new LoadTask();
			myTask.execute();

		} else {

			monthList.clear();
			
			LoadTaskCheck myTask = new LoadTaskCheck();
			myTask.execute();
			
		}
	}

	Date stringToDate(String strDate, String toFormat) {
		SimpleDateFormat format = new SimpleDateFormat(toFormat);
		try {
			Date date = format.parse(strDate);

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

	private class LoadTask extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(CalendarActivity.this);

			Task task = new Task(Constants.DBHANDLER);
			Cursor taskCursor = task.getTaskListDisplay(selectDate,
					categoryData);

			if (taskCursor.getCount() != 0) {
				calendarTask = new CalendarData();
				calendarTask.dateDay = getString(R.string.tasks);
				calendarTask.isTitle = true;
				calendarTask.type = 0;
				monthList.add(calendarTask);

				do {
					calendarTask = new CalendarData();
					calendarTask.type = 1;
					// calendarTask.startDate = convertTime(taskCursor
					// .getString(taskCursor
					// .getColumnIndex("DUE_DATE")))
					// + "-"
					// + convertTime(taskCursor.getString(taskCursor
					// .getColumnIndex("DUE_DATE")));
					calendarTask.monthTask = taskCursor.getString(taskCursor
							.getColumnIndex("SUBJECT"));
					calendarTask.strIsUpdate = taskCursor.getString(taskCursor
							.getColumnIndex("IS_UPDATE"));
					calendarTask.strActive = taskCursor.getString(taskCursor
							.getColumnIndex("ACTIVE"));
					calendarTask.strInternalNum = taskCursor
							.getString(taskCursor
									.getColumnIndex("INTERNAL_NUM"));
					monthList.add(calendarTask);
				} while (taskCursor.moveToNext());

			}

			FLCalendar event = new FLCalendar(Constants.DBHANDLER);
			Cursor cursor = event.getCalendarListDisplay(selectDate);

			if (cursor.getCount() != 0) {
				calendarTask = new CalendarData();
				calendarTask.dateDay = getString(R.string.events);
				calendarTask.isTitle = true;
				calendarTask.type = 0;
				monthList.add(calendarTask);

				do {
					calendarTask = new CalendarData();
					calendarTask.type = 2;
					calendarTask.startDate = convertTime(cursor
							.getString(cursor.getColumnIndex("START_DATE")))
							+ "-"
							+ convertTime(cursor.getString(cursor
									.getColumnIndex("END_DATE")));
					calendarTask.monthTask = cursor.getString(cursor
							.getColumnIndex("SUBJECT"));
					calendarTask.strIsUpdate = cursor.getString(cursor
							.getColumnIndex("IS_UPDATE"));
					calendarTask.strActive = cursor.getString(cursor
							.getColumnIndex("ACTIVE"));
					calendarTask.strInternalNum = cursor.getString(cursor
							.getColumnIndex("INTERNAL_NUM"));

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
							calendarTask.nameToName = strLastName;
						}

						// System.out.println("strLastName.length() = "+strLastName.length());
						System.out.println("name - = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_NAME")));
						if (strLastName == null) {
							String nameToName = cursor.getString(cursor
									.getColumnIndex("NAME_TO_NAME"));
							if (nameToName != null)
								calendarTask.nameToName = nameToName;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					
					monthList.add(calendarTask);
				} while (cursor.moveToNext());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			monthAdapter = new MonthAdapter(context, monthList);
			list.setAdapter(monthAdapter);

			adapter = new CalendarAdapter(context, month);

			gridview.setAdapter(adapter);
			refreshCalendar();
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	
	private class LoadTaskCheck extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(CalendarActivity.this);

			Task task = new Task(Constants.DBHANDLER);
			Cursor taskCursor = task.getTaskListDisplay(selectDate,
					categoryData);

			if (taskCursor.getCount() != 0) {
				calendarTask = new CalendarData();
				calendarTask.dateDay = getString(R.string.tasks);
				calendarTask.type = 0;
				calendarTask.isTitle = true;
				monthList.add(calendarTask);

				do {
					calendarTask = new CalendarData();
					calendarTask.type = 1;
					// calendarTask.startDate = convertTime(taskCursor
					// .getString(taskCursor.getColumnIndex("DUE_DATE")))
					// + "-"
					// + convertTime(taskCursor.getString(taskCursor
					// .getColumnIndex("DUE_DATE")));
					calendarTask.monthTask = taskCursor.getString(taskCursor
							.getColumnIndex("SUBJECT"));
					calendarTask.strIsUpdate = taskCursor.getString(taskCursor
							.getColumnIndex("IS_UPDATE"));
					calendarTask.strActive = taskCursor.getString(taskCursor
							.getColumnIndex("ACTIVE"));
					calendarTask.strInternalNum = taskCursor
							.getString(taskCursor
									.getColumnIndex("INTERNAL_NUM"));
					monthList.add(calendarTask);
				} while (taskCursor.moveToNext());
			}
			FLCalendar event = new FLCalendar(Constants.DBHANDLER);
			Cursor cursor = event.getCalendarListDisplay(selectDate);

			if (cursor.getCount() != 0) {
				calendarTask = new CalendarData();
				calendarTask.dateDay = getString(R.string.events);
				calendarTask.isTitle = true;
				calendarTask.type = 0;
				monthList.add(calendarTask);

				do {
					calendarTask = new CalendarData();
					calendarTask.type = 2;
					if (cursor.getString(cursor.getColumnIndex("ALL_DAY"))
							.equals("true")) {
						calendarTask.startDate = getString(R.string.all_day);
					} else {
						calendarTask.startDate = convertTime(cursor
								.getString(cursor.getColumnIndex("START_DATE")))
								+ "-"
								+ convertTime(cursor.getString(cursor
										.getColumnIndex("END_DATE")));
					}
					calendarTask.monthTask = cursor.getString(cursor
							.getColumnIndex("SUBJECT"));
					calendarTask.strIsUpdate = cursor.getString(cursor
							.getColumnIndex("IS_UPDATE"));
					calendarTask.strActive = cursor.getString(cursor
							.getColumnIndex("ACTIVE"));
					calendarTask.strInternalNum = cursor.getString(cursor
							.getColumnIndex("INTERNAL_NUM"));
					
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
							calendarTask.nameToName = strLastName;
						}

						// System.out.println("strLastName.length() = "+strLastName.length());
						System.out.println("name - = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_NAME")));
						if (strLastName == null) {
							String nameToName = cursor.getString(cursor
									.getColumnIndex("NAME_TO_NAME"));
							if (nameToName != null)
								calendarTask.nameToName = nameToName;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					
					monthList.add(calendarTask);
				} while (cursor.moveToNext());
			}
		

			return null;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			monthAdapter = new MonthAdapter(context, monthList);
			list.setAdapter(monthAdapter);

			refreshCalendar();
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	
	private class LoadTaskDate extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(CalendarActivity.this);

			Task task = new Task(Constants.DBHANDLER);
			Cursor taskCursor = task.getTaskListDisplay(
					selectDate, categoryData);

			if (taskCursor.getCount() != 0) {
				calendarTask = new CalendarData();
				calendarTask.dateDay = getString(R.string.tasks);
				calendarTask.isTitle = true;
				calendarTask.type = 0;
				monthList.add(calendarTask);

				do {
					calendarTask = new CalendarData();
					calendarTask.type = 1;
					// calendarTask.startDate =
					// convertTime(taskCursor
					// .getString(taskCursor
					// .getColumnIndex("DUE_DATE")))
					// + "-"
					// +
					// convertTime(taskCursor.getString(taskCursor
					// .getColumnIndex("DUE_DATE")));
					calendarTask.monthTask = taskCursor.getString(taskCursor
							.getColumnIndex("SUBJECT"));
					calendarTask.strIsUpdate = taskCursor.getString(taskCursor
							.getColumnIndex("IS_UPDATE"));
					calendarTask.strActive = taskCursor.getString(taskCursor
							.getColumnIndex("ACTIVE"));
					calendarTask.strInternalNum = taskCursor.getString(taskCursor
							.getColumnIndex("INTERNAL_NUM"));
					monthList.add(calendarTask);
				} while (taskCursor.moveToNext());

			}

			FLCalendar event = new FLCalendar(
					Constants.DBHANDLER);
			Cursor cursor = event
					.getCalendarListDisplay(selectDate);

			if (cursor.getCount() != 0) {
				calendarTask = new CalendarData();
				calendarTask.dateDay = getString(R.string.events);
				calendarTask.isTitle = true;
				calendarTask.type = 0;
				monthList.add(calendarTask);

				do {
					calendarTask = new CalendarData();
					calendarTask.type = 2;
					calendarTask.startDate = convertTime(cursor.getString(cursor
							.getColumnIndex("START_DATE")))
							+ "-"
							+ convertTime(cursor.getString(cursor
									.getColumnIndex("END_DATE")));
					calendarTask.monthTask = cursor.getString(cursor
							.getColumnIndex("SUBJECT"));
					calendarTask.strIsUpdate = cursor.getString(cursor
							.getColumnIndex("IS_UPDATE"));
					calendarTask.strActive = cursor.getString(cursor
							.getColumnIndex("ACTIVE"));
					calendarTask.strInternalNum = cursor.getString(cursor
							.getColumnIndex("INTERNAL_NUM"));
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
							calendarTask.nameToName = strLastName;
						}

						// System.out.println("strLastName.length() = "+strLastName.length());
						System.out.println("name - = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_NAME")));
						if (strLastName == null) {
							String nameToName = cursor.getString(cursor
									.getColumnIndex("NAME_TO_NAME"));
							if (nameToName != null)
								calendarTask.nameToName = nameToName;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					monthList.add(calendarTask);
				} while (cursor.moveToNext());
			}
		

			return null;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			monthAdapter = new MonthAdapter(context,
					monthList);
			list.setAdapter(monthAdapter);

			adapter = new CalendarAdapter(context, month);

			gridview.setAdapter(adapter);
			refreshCalendar();
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	
	private class LoadTaskCalendar extends AsyncTask<String, Void, Cursor> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(CalendarActivity.this, false,
					null);
			
		}

		
		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(CalendarActivity.this);


			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				FLCalendar event = new FLCalendar(Constants.DBHANDLER);
				Cursor cursor = event.getCalendarListDisplay();

				if (cursor.getCount() != 0) {
					do {
						Date startDate = stringToDate(
								changeToMarkDate(cursor.getString(cursor
										.getColumnIndex("START_DATE"))),
								"yyyy-MM-dd");

						Date endDate = stringToDate(
								changeToMarkDate(cursor.getString(cursor
										.getColumnIndex("END_DATE"))),
								"yyyy-MM-dd");

						Calendar cal1 = Calendar.getInstance();
						Calendar cal2 = Calendar.getInstance();
						cal1.setTime(startDate);
						cal2.setTime(endDate);

						int dayCount = (int) (cal2.getTimeInMillis() - cal1
								.getTimeInMillis()) / (1000 * 3600 * 24);

						for (int j = 0; j < dayCount; j++) {
							cal1.add(Calendar.DAY_OF_MONTH, 1);
							if (!dateToString(cal1.getTime(), "yyyy-MM-dd")
									.equals(stringToDate(
											changeToMarkDate(cursor
													.getString(cursor
															.getColumnIndex("START_DATE"))),
											"yyyy-MM-dd"))
									&& !dateToString(cal1.getTime(),
											"yyyy-MM-dd")
											.equals(stringToDate(
													changeToMarkDate(cursor
															.getString(cursor
																	.getColumnIndex("END_DATE"))),
													"yyyy-MM-dd"))) {
								items.add(dateToString(cal1.getTime(),
										"yyyy-MM-dd"));
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
				}

				Task task = new Task(Constants.DBHANDLER);
				Cursor Taskcursor = task.getTaskListDisplay();
				if (Taskcursor.getCount() != 0) {
					do {
						taskItems.add(changeToMarkDate(Taskcursor
								.getString(Taskcursor
										.getColumnIndex("DUE_DATE"))));

					} while (Taskcursor.moveToNext());
				}
			}

		

			return null;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			adapter.setItems(items, taskItems);
			adapter.notifyDataSetChanged();
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	

}
