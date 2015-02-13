package com.trinerva.icrm.tasks;

import com.trinerva.icrm.CrmLogin;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.SynchronizationData;
import com.trinerva.icrm.contacts.ContactFilterListActivity;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

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
import android.view.View.OnLongClickListener;
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

public class TaskList extends Activity {
	private String TAG = "TaskList";
	private SimpleCursorAdapter adapter;
	private TextView empty_task_list;
	ImageView add_new;
	private EditText search;
	private ListView list;
	SaveData saveData;
	private Dialog loadingDialog;

	int nowSelectStatus = 0;

	TextView mTarget;
	TextView mUnSyncText;
	Button mSync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);

		saveData = new SaveData(this);

		add_new = (ImageView) findViewById(R.id.add_new);
		search = (EditText) findViewById(R.id.search);
		list = (ListView) findViewById(R.id.list);
		empty_task_list = (TextView) findViewById(R.id.empty_task_list);
		mTarget = (TextView) findViewById(R.id.target);
		mUnSyncText = (TextView) findViewById(R.id.unsyncText);
		mSync = (Button) findViewById(R.id.syn);
		search.addTextChangedListener(filterTextWatcher);
		add_new.setOnClickListener(doAddTask);

		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);

		if (!Utility.getConfigByText(this, Constants.DELETE_TASK).equals("0")) {
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {

					GuiUtility.alert(TaskList.this, "",
							getString(R.string.confirm_delete_task),
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
													TaskList.this,
													false,
													getString(R.string.processing));
									DatabaseUtility
											.getDatabaseHandler(TaskList.this);
									Task task = new Task(Constants.DBHANDLER);

									Cursor cursor = adapter.getCursor();
									cursor.moveToPosition(position);

									task.delete(
											TaskList.this,
											cursor.getString(cursor
													.getColumnIndex("INTERNAL_NUM")));
									loading.dismiss();

									LoadTask myTask = new LoadTask();
									myTask.execute();

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
					Intent sync = new Intent(TaskList.this,
							SynchronizationData.class);
					Bundle bFlag = new Bundle();
					bFlag.putString("ACTION", "NORMAL");
					bFlag.putString("EMAIL",
							saveData.getStringData(GetData.getUserEmail));
					bFlag.putString("PASSWORD",
							saveData.getStringData(GetData.getUserPassword));
					bFlag.putString("FROM", "CRMLOGIN");
					sync.putExtras(bFlag);
					TaskList.this.startActivity(sync);
				} else {
					Intent contact = new Intent(TaskList.this, CrmLogin.class);
					startActivity(contact);
				}
			}
		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		mTarget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("nowSelete", nowSelectStatus);
				intent.putExtra("filterPage", 2);
				intent.setClass(TaskList.this, ContactFilterListActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		mTarget.setText(getString(R.string.today_task));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSync.setClickable(true);
		LoadTask task = new LoadTask();
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
			Intent info = new Intent(TaskList.this, TaskInfo.class);
			info.putExtra("ID",
					cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			info.putExtra("VIEW", true);
			info.putExtra("ACTIVE", strActive);
			TaskList.this.startActivity(info);
		}
	};

	private OnClickListener doAddTask = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent event = new Intent(TaskList.this, TaskInfo.class);
			event.putExtra("EDIT_NAME_TO", true);
			TaskList.this.startActivity(event);
		}
	};

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

	private class LoadTask extends AsyncTask<String, Void, Cursor> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(TaskList.this, false,
					null);
			
			if(getUnsyncData()){
				mUnSyncText.setVisibility(View.GONE);
			}else{
				mUnSyncText.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(TaskList.this);
			Task task = new Task(Constants.DBHANDLER);

			switch (nowSelectStatus) {
			case 0:

				return task.getTodayTaskListDisplay();
			case 1:

				return task.getTodayAndNew7DayTaskListDisplay();
			case 2:

				return task.getOverdueTaskListDisplay();
			default:
				return task.getTaskListDisplay();
			}

		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			adapter = new SimpleCursorAdapter(
					TaskList.this,
					R.layout.task_item,
					result,
					new String[] { "_id", "SUBJECT", "DUE_DATE", "IS_UPDATE",
							"ACTIVE" },
					new int[] { R.id.subject, R.id.start_date, R.id.event_sync ,R.id.nameTo});
			adapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
			adapter.setFilterQueryProvider(filter);
			list.setAdapter(adapter);
			String strSearch = search.getText().toString();
			if (strSearch.length() > 0) {
				adapter.getFilter().filter(strSearch);
			}

			if (result.getCount() == 0) {
				empty_task_list.setVisibility(View.VISIBLE);
			} else {
				empty_task_list.setVisibility(View.GONE);
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
					company.setText(cursor.getString(cursor
							.getColumnIndex("SUBJECT")));
					break;
				case R.id.start_date:
					TextView start = (TextView) view;
					start.setText("");
					start.setText(Utility.convertTaskTime(cursor
							.getString(cursor.getColumnIndex("DUE_DATE"))));
					break;
				case R.id.event_sync:
//					ImageView cSync = (ImageView) view;
//					DeviceUtility.log(
//							TAG,
//							"IS_UPDATE: "
//									+ cursor.getString(cursor
//											.getColumnIndex("IS_UPDATE")));
//					String strIsUpdate = cursor.getString(cursor
//							.getColumnIndex("IS_UPDATE"));
//					String strActive = cursor.getString(cursor
//							.getColumnIndex("ACTIVE"));
//
//					if (strActive.equalsIgnoreCase("0")
//							&& strIsUpdate.equalsIgnoreCase("true")) {
//						cSync.setVisibility(View.GONE);
//					} else {
//						cSync.setVisibility(View.VISIBLE);
//						if (strActive.equalsIgnoreCase("1")) {
//							cSync.setImageResource(R.drawable.btn_sync_deleted);
//						} else {
//							cSync.setImageResource(R.drawable.btn_sync);
//						}
//					}
					break;
				case R.id.nameTo:
//					TextView mNameTo = (TextView) view;
//	        		try {
//	        			System.out.println("Last name = "+cursor.getString(cursor.getColumnIndex("NAME_TO_LAST_NAME")));
//		        		System.out.println("first name = "+cursor.getString(cursor.getColumnIndex("NAME_TO_FIRST_NAME")));
//				
//		        		String strFirstName = cursor.getString(cursor
//		        				.getColumnIndex("NAME_TO_FIRST_NAME"));
//		        		String strLastName = cursor.getString(cursor.getColumnIndex("NAME_TO_LAST_NAME"));
//		        		if (strFirstName == null) {
//		        			strLastName = strLastName;
//		        		} else {
//		        			strLastName = strFirstName + " " + strLastName;
//		        		}
//		        		mNameTo.setText(strLastName);
//
//	        		} catch (Exception e) {
//						// TODO: handle exception
//					}
					
					TextView mNameTo = (TextView) view;
					mNameTo.setText("");
					try {
						System.out.println("Last name = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_LAST_NAME")));
						System.out.println("first name = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_FIRST_NAME")));

						System.out.println("name - = "
								+ cursor.getString(cursor
										.getColumnIndex("NAME_TO_NAME")));
						
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
				}
				return true;
			}
		};
	}

	private FilterQueryProvider filter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			DeviceUtility.log(TAG, "filter: " + constraint);
			DatabaseUtility.getDatabaseHandler(TaskList.this);
			Task task = new Task(Constants.DBHANDLER);
//			Cursor cursor = task.getTaskListDisplayByFilter(constraint
//					.toString());
			
			switch (nowSelectStatus) {
			case 0:

				return task.getFilterTodayTaskListDisplay(constraint
						.toString());
			case 1:

				return task.getFilterTodayAndNew7DayTaskListDisplay(constraint
						.toString());
			case 2:

				return task.getFilterOverdueTaskListDisplay(constraint
						.toString());
			default:
				return task.getTaskListDisplay();
			}
			
//			return cursor;
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// String strUri = data.getDataString();
			// DeviceUtility.log(TAG, "strUri: " + strUri);
			// Intent contact = new Intent(ContactList.this,
			// ContactInfoTab.class);
			// contact.putExtra("CONTACT_URI", strUri);
			// ContactList.this.startActivity(contact);

			nowSelectStatus = data.getIntExtra("selete", 0);

			System.out.println("haha = " + nowSelectStatus);

			switch (nowSelectStatus) {
			case 0:
				mTarget.setText(getString(R.string.today_task));
				break;
			case 1:
				mTarget.setText(getString(R.string.today_next_7_day));
				break;
			case 2:
				mTarget.setText(getString(R.string.overdue));
				break;
			default:
				break;
			}
		}
	}
	
	boolean getUnsyncData(){
		Task task = new Task(Constants.DBHANDLER);
		Cursor cursor =  task.getTaskListDisplay();
		do {
			if (cursor.getCount() != 0) {
				System.out.println("AAAA = "+cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
				if (cursor.getString(cursor.getColumnIndex("IS_UPDATE"))
						.equals("false")) {
					return false;
				}
			}
		} while (cursor.moveToNext());
		return true;
	}
}
