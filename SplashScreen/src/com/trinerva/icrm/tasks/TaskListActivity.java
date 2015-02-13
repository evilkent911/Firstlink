package com.trinerva.icrm.tasks;
//package asia.firstlink.tasks;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.EditText;
//import android.widget.FilterQueryProvider;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//import android.widget.TextView;
//import asia.firstlink.CrmLogin;
//import asia.firstlink.R;
//import asia.firstlink.SynchronizationData;
//import asia.firstlink.database.source.Task;
//import asia.firstlink.model.GetData;
//import asia.firstlink.model.SaveData;
//import asia.firstlink.utility.Constants;
//import asia.firstlink.utility.DatabaseUtility;
//import asia.firstlink.utility.DeviceUtility;
//import asia.firstlink.utility.GuiUtility;
//
//public class TaskListActivity extends Activity {
//	private String TAG = "TaskList";
//	private SimpleCursorAdapter adapter;
//	private TextView  empty_task_list;
//	private EditText search;
//	private ListView list;
//	private Dialog loadingDialog;
//SaveData saveData;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_task_list);
//
//		search = (EditText) findViewById(R.id.search);
//		list = (ListView) findViewById(R.id.list);
//		empty_task_list = (TextView) findViewById(R.id.empty_task_list);
//
//		search.addTextChangedListener(filterTextWatcher);
//
//		saveData = new SaveData(this);
//		list.setFocusableInTouchMode(true);
//		list.setCacheColorHint(0000000);
//		list.setFastScrollEnabled(true);
//		list.setVerticalFadingEdgeEnabled(true);
//		list.setOnItemClickListener(doItemClick);
//		
//		findViewById(R.id.syn).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(saveData.getBooleanData(GetData.checkLogin)){
//					Intent sync = new Intent(TaskListActivity.this, SynchronizationData.class);
//					Bundle bFlag = new Bundle();
//					bFlag.putString("ACTION", "NORMAL");
//					bFlag.putString("EMAIL", saveData.getStringData(GetData.getUserEmail));
//					bFlag.putString("PASSWORD", saveData.getStringData(GetData.getUserPassword));
//					bFlag.putString("FROM", "CRMLOGIN");
//					sync.putExtras(bFlag);
//					TaskListActivity.this.startActivity(sync);
//				}else{
//					Intent contact = new Intent(TaskListActivity.this, CrmLogin.class);
//					startActivity(contact);
//				}
//			}
//		}); 
//	}
//
//	String changeToDate(String date){
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
//		Date d = null;
//		try {
//			 d = dateFormat.parse(date);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy\nh:mm");
//		
//		return dateFormat2.format(d).toString();
//	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		LoadTask task = new LoadTask();
//		task.execute();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		search.removeTextChangedListener(filterTextWatcher);
//		if (adapter != null) {
//			adapter.getCursor().close();
//			adapter = null;
//		}
//	}
//
//	private OnItemClickListener doItemClick = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			Cursor cursor = adapter.getCursor();
//			cursor.moveToPosition(position);
//			String strActive = cursor.getString(cursor.getColumnIndex("ACTIVE"));
//			Intent info = new Intent(TaskListActivity.this, TaskInfo.class);
//			info.putExtra("ID", cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
//			info.putExtra("VIEW", true);
//			info.putExtra("ACTIVE", strActive);
//			TaskListActivity.this.startActivity(info);
//		}
//	};
//
//	private OnClickListener doAddTask = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent event = new Intent(TaskListActivity.this, TaskInfo.class);
//			TaskListActivity.this.startActivity(event);
//		}
//	};
//
//	private TextWatcher filterTextWatcher = new TextWatcher() {
//
//	    public void afterTextChanged(Editable s) {
//	    }
//
//	    public void beforeTextChanged(CharSequence s, int start, int count,
//	            int after) {
//	    }
//
//	    public void onTextChanged(CharSequence s, int start, int before,
//	            int count) {
//	    	if (adapter != null) {
//	    		adapter.getFilter().filter(s);
//	    	}
//	    }
//	};
//
//	private class LoadTask extends AsyncTask<String, Void, Cursor> {
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			loadingDialog = GuiUtility.getLoadingDialog(TaskListActivity.this, false, null);
//		}
//
//		@Override
//		protected Cursor doInBackground(String... arg0) {
//			DatabaseUtility.getDatabaseHandler(TaskListActivity.this);
//			Task task = new Task(Constants.DBHANDLER);
//			return task.getTaskListDisplay();
//		}
//
//		@Override
//		protected void onPostExecute(Cursor result) {
//			super.onPostExecute(result);
//
//			adapter = new SimpleCursorAdapter(TaskListActivity.this, R.layout.task_item, result, new String[] {"_id", "SUBJECT", "DUE_DATE", "IS_UPDATE", "ACTIVE"}, new int[]{R.id.subject, R.id.start_date, R.id.event_sync});
//			adapter.setViewBinder(binder);
//			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
//			adapter.setFilterQueryProvider(filter);
//			list.setAdapter(adapter);
//			String strSearch = search.getText().toString();
//			if (strSearch.length() > 0) {
//				adapter.getFilter().filter(strSearch);
//			}
//
//			if (result.getCount() == 0) {
//				empty_task_list.setVisibility(View.VISIBLE);
//			} else {
//				empty_task_list.setVisibility(View.GONE);
//			}
//
//			if (loadingDialog.isShowing()) {
//				loadingDialog.dismiss();
//			}
//		}
//
//		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
//
//			@Override
//			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//				int viewId = view.getId();
//		        switch(viewId) {
//		        	case R.id.subject:
//		        		TextView company = (TextView) view;
//		        		company.setText(cursor.getString(cursor.getColumnIndex("SUBJECT")));
//		        		break;
//		        	case R.id.start_date:
//		        		TextView start = (TextView) view;
//		        		start.setText(changeToDate(cursor.getString(cursor.getColumnIndex("DUE_DATE"))));
//		        		break;
//		        	case R.id.event_sync:
//		        		ImageView cSync = (ImageView) view;
//		        		DeviceUtility.log(TAG, "IS_UPDATE: " + cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
//		        		String strIsUpdate = cursor.getString(cursor.getColumnIndex("IS_UPDATE"));
//		        		String strActive = cursor.getString(cursor.getColumnIndex("ACTIVE"));
//
//		        		if (strActive.equalsIgnoreCase("0") && strIsUpdate.equalsIgnoreCase("true")) {
//		        			cSync.setVisibility(View.GONE);
//		        		} else {
//		        			cSync.setVisibility(View.VISIBLE);
//		        			if (strActive.equalsIgnoreCase("1")) {
//		        				cSync.setImageResource(R.drawable.btn_sync_deleted);
//		        			} else {
//		        				cSync.setImageResource(R.drawable.btn_sync);
//		        			}
//		        		}
//		        		break;
//		        }
//				return true;
//			}
//		};
//	}
//
//	private FilterQueryProvider filter = new FilterQueryProvider() {
//
//		@Override
//		public Cursor runQuery(CharSequence constraint) {
//			DeviceUtility.log(TAG, "filter: " + constraint);
//			DatabaseUtility.getDatabaseHandler(TaskListActivity.this);
//			Task task = new Task(Constants.DBHANDLER);
//			Cursor cursor = task.getTaskListDisplayByFilter(constraint.toString());
//			return cursor;
//		}
//	};
//}
