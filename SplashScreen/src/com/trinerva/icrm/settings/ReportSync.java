package com.trinerva.icrm.settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.trinerva.icrm.ForgotPassword;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.SynchronizationData;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class ReportSync extends Activity {
	private String TAG = "ReportSync";
	private Dialog loadingDialog;
	private TextView username, today, this_week, this_month, custom;
	private EditText ed_password, ed_start_date, ed_end_date;
	private Button connect, forgot;
	private static final int START_DATE_DIALOG_ID = 54221;
	private static final int END_DATE_DIALOG_ID = 35412;
	private int iReportRange = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_sync);
		
		username = (TextView) findViewById(R.id.username);
		today = (TextView) findViewById(R.id.today);
		this_week = (TextView) findViewById(R.id.this_week);
		this_month = (TextView) findViewById(R.id.this_month);
		custom = (TextView) findViewById(R.id.custom);
		
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_start_date = (EditText) findViewById(R.id.ed_start_date);
		ed_end_date = (EditText) findViewById(R.id.ed_end_date);
		
		connect = (Button) findViewById(R.id.connect);
		forgot = (Button) findViewById(R.id.forgot);
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		ed_start_date.setText(sdf.format(c.getTime()));
		ed_end_date.setText(sdf.format(c.getTime()));
		ed_start_date.setFocusable(false);
		ed_end_date.setFocusable(false);
		ed_start_date.setEnabled(false);
		ed_end_date.setEnabled(false);
		
		today.setOnClickListener(doSelectDate);
		this_week.setOnClickListener(doSelectDate);
		this_month.setOnClickListener(doSelectDate);
		custom.setOnClickListener(doSelectDate);
		
		
		String strEmail = Utility.getConfigByText(this, "USER_EMAIL");
		username.setText(strEmail);
		
		connect.setOnClickListener(doConnect);
		forgot.setOnClickListener(doForgot);
	}
	
	private OnClickListener doForgot = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ReportSync.this, ForgotPassword.class);
			ReportSync.this.startActivity(intent);
		}
	};
	
	private OnClickListener doSelectDate = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
			today.setBackgroundResource(R.drawable.blue_menu_left);
			this_week.setBackgroundResource(R.drawable.blue_menu_middle);
			this_month.setBackgroundResource(R.drawable.blue_menu_middle);
			custom.setBackgroundResource(R.drawable.blue_menu_right);
			ed_start_date.setEnabled(true);
			ed_end_date.setEnabled(true);
			
			switch(v.getId()) {
				case R.id.today:
					iReportRange = 1;
					//set the today date.
					today.setBackgroundResource(R.drawable.blue_menu_selected_left);
					ed_start_date.setText(sdf.format(c.getTime()));
					ed_end_date.setText(sdf.format(c.getTime()));
					ed_start_date.setOnClickListener(null);
					ed_end_date.setOnClickListener(null);
					break;
				case R.id.this_week:
					iReportRange = 2;
					this_week.setBackgroundResource(R.drawable.blue_menu_selected_middle);
					//set the week start date and end date
					c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					ed_start_date.setText(sdf.format(c.getTime()));
					c.add(Calendar.DATE, 6);
					ed_end_date.setText(sdf.format(c.getTime()));
					ed_start_date.setOnClickListener(null);
					ed_end_date.setOnClickListener(null);
					break;
				case R.id.this_month:
					iReportRange = 3;
					this_month.setBackgroundResource(R.drawable.blue_menu_selected_middle);
					//set the month start date and end date.					
					c.set(Calendar.DATE, 1);
					ed_start_date.setText(sdf.format(c.getTime()));
					
					int lastDate = c.getActualMaximum(Calendar.DATE);
					c.set(Calendar.DATE, lastDate);
					ed_end_date.setText(sdf.format(c.getTime()));
					ed_start_date.setOnClickListener(null);
					ed_end_date.setOnClickListener(null);
					break;
				case R.id.custom:
					iReportRange = 4;
					custom.setBackgroundResource(R.drawable.blue_menu_selected_right);
					//initial the date picker.
					ed_start_date.setOnClickListener(showDatePicker);
					ed_end_date.setOnClickListener(showDatePicker);
					break;
			}
		}
		
	};
	
	private OnClickListener showDatePicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ed_start_date:
					showDialog(START_DATE_DIALOG_ID);
					break;
				case R.id.ed_end_date:
					showDialog(END_DATE_DIALOG_ID);
					break;
			}			
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case START_DATE_DIALOG_ID :
				DatePickerDialog dateDlg = new DatePickerDialog(this,
				         new DatePickerDialog.OnDateSetListener() {
				         public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				                    Time chosenDate = new Time();
				                    chosenDate.set(dayOfMonth, monthOfYear, year);
				                    long dtDob = chosenDate.toMillis(true);
				                    CharSequence strDate = DateFormat.format(Constants.DATE_FORMAT, dtDob);
				                    ed_start_date.setText(strDate);
				        }}, 2011,0, 1);

				      dateDlg.setMessage(getString(R.string.start_date));
				      return dateDlg;
			case END_DATE_DIALOG_ID :
				DatePickerDialog dateDlg2 = new DatePickerDialog(this,
				         new DatePickerDialog.OnDateSetListener() {
				         public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				                    Time chosenDate = new Time();
				                    chosenDate.set(dayOfMonth, monthOfYear, year);
				                    long dtDob = chosenDate.toMillis(true);
				                    CharSequence strDate = DateFormat.format(Constants.DATE_FORMAT, dtDob);
				                    ed_end_date.setText(strDate);
				        }}, 2011,0, 1);

					dateDlg2.setMessage(getString(R.string.end_date));
				    return dateDlg2;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		int iDay,iMonth,iYear;
		Calendar cal = Calendar.getInstance();
		
		switch (id) {
			case START_DATE_DIALOG_ID:
				DatePickerDialog dateDlg = (DatePickerDialog) dialog;
			    iDay = cal.get(Calendar.DAY_OF_MONTH);
			    iMonth = cal.get(Calendar.MONTH);
			    iYear = cal.get(Calendar.YEAR);
			    if (ed_start_date.getText().toString().length() == 10) {
			    	iDay = Integer.parseInt(ed_start_date.getText().toString().substring(0, 2));
			    	iMonth = Integer.parseInt(ed_start_date.getText().toString().substring(3, 5)) - 1;
			    	iYear = Integer.parseInt(ed_start_date.getText().toString().substring(6, 10));
			    }
			    dateDlg.updateDate(iYear, iMonth, iDay);
			    break;
			case END_DATE_DIALOG_ID:
				DatePickerDialog dateDlg2 = (DatePickerDialog) dialog;
			    iDay = cal.get(Calendar.DAY_OF_MONTH);
			    iMonth = cal.get(Calendar.MONTH);
			    iYear = cal.get(Calendar.YEAR);
			    if (ed_end_date.getText().toString().length() == 10) {
			    	iDay = Integer.parseInt(ed_end_date.getText().toString().substring(0, 2));
			    	iMonth = Integer.parseInt(ed_end_date.getText().toString().substring(3, 5)) - 1;
			    	iYear = Integer.parseInt(ed_end_date.getText().toString().substring(6, 10));
			    }
			    dateDlg2.updateDate(iYear, iMonth, iDay);
				break;
		}
	}
	
	private OnClickListener doConnect = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String strEmail = username.getText().toString();
			String strPassword = ed_password.getText().toString();
			if (strEmail.length() == 0) {
				GuiUtility.alert(ReportSync.this, getString(R.string.email_error_title), getString(R.string.email_error_msg), getString(R.string.ok));
			} else if (strPassword.length() == 0) {
				GuiUtility.alert(ReportSync.this, getString(R.string.password_error_title), getString(R.string.password_error_msg), getString(R.string.ok));
			} else if (!Utility.isValidEmail(strEmail)) {
				GuiUtility.alert(ReportSync.this, getString(R.string.email_error_title), getString(R.string.email_error_msg), getString(R.string.ok));
			} else if (strEmail.length() > 0 && strPassword.length() > 0) {
				//pass to sync activity.
				Intent sync = new Intent(ReportSync.this, SynchronizationData.class);
				Bundle bFlag = new Bundle();
				bFlag.putString("ACTION", "REPORT");
				bFlag.putString("EMAIL", strEmail);
				bFlag.putString("PASSWORD", strPassword);
				bFlag.putString("FROM", "REPORT");
				bFlag.putString("START_DATE", ed_start_date.getText().toString());
				bFlag.putString("END_DATE", ed_end_date.getText().toString());
				sync.putExtras(bFlag);
				ReportSync.this.startActivity(sync);
				ReportSync.this.finish();
			}
		}
	};
	
}
