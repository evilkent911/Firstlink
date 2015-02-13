package com.trinerva.icrm.reports;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.Report;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Individual extends Activity {
	private String TAG = "Individual";
	private TextView main_title, empty_report_list;
	private ListView list;
	private String strReportType = Constants.REPORT_TYPE_ACTIVITY;
	private Dialog loadingDialog;
	private SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_report);
		//check mode.
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("MODE")) {
				strReportType = bundle.getString("MODE").toString();
			}
		}
		
		main_title = (TextView) findViewById(R.id.main_title);
		empty_report_list = (TextView) findViewById(R.id.empty_report_list);
		String strTitle = "";
		if (strReportType.equals(Constants.REPORT_TYPE_ACTIVITY)) {
			strTitle = getString(R.string.activity_individual_title);
		} else {
			strTitle = getString(R.string.opportunity_individual_title);
		}
		
		strTitle = strTitle.replace("[[START_DATE] to [END_DATE]]", "");
		main_title.setText(strTitle);
		
		
		list = (ListView) findViewById(R.id.list);
		list.setFocusableInTouchMode(true);
		list.requestFocus(0);
		list.setSelection(0);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LoadReport task = new LoadReport();
		task.execute(strReportType);
	}
	
	private class LoadReport extends AsyncTask<String, Void, Cursor> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(Individual.this, false, null);
		}

		@Override
		protected Cursor doInBackground(String... arg0) {
			DatabaseUtility.getDatabaseHandler(Individual.this);
			Report report = new Report(Constants.DBHANDLER);
			return report.getReport(arg0[0]);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			adapter = new SimpleCursorAdapter(Individual.this, R.layout.individual_item, result, new String[] {"_id", "REPORT_START_DATE", "REPORT_END_DATE", "REPORT_PERSON", "MASTER_TEXT", "TOTAL"}, new int[]{R.id.subject, R.id.start_date});
			adapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
			list.setAdapter(adapter);

			if (result.getCount() == 0) {
				empty_report_list.setVisibility(View.VISIBLE);
			} else {
				result.moveToFirst();
				String strStartDate = result.getString(result.getColumnIndex("REPORT_START_DATE"));
				String strEndDate = result.getString(result.getColumnIndex("REPORT_END_DATE"));
				
				String strTitle = "";
				if (strReportType.equals(Constants.REPORT_TYPE_ACTIVITY)) {
					strTitle = getString(R.string.activity_individual_title);
				} else {
					strTitle = getString(R.string.opportunity_individual_title);
				}
				
				strTitle = strTitle.replace("[START_DATE]", strStartDate);
				strTitle = strTitle.replace("[END_DATE]", strEndDate);
				main_title.setText(strTitle);
				DeviceUtility.log(TAG, strTitle);
				
				empty_report_list.setVisibility(View.GONE);
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				int viewId = view.getId();
		        switch(viewId) {
		        	case R.id.subject:
		        		TextView company = (TextView) view;
		        		company.setText(cursor.getString(cursor.getColumnIndex("REPORT_PERSON")));
		        		break;
		        	case R.id.start_date:
		        		TextView start = (TextView) view;
		        		start.setText(cursor.getString(cursor.getColumnIndex("MASTER_TEXT")) + ": " + cursor.getString(cursor.getColumnIndex("TOTAL")));
		        		break;
		        }
				return true;
			}
		};
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adapter != null) {
			adapter.getCursor().close();
			adapter = null;
		}
	}
}
