package com.trinerva.icrm;

import java.util.HashMap;
import org.ksoap2.serialization.SoapObject;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.settings.AndroidSetup;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.SoapUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import asia.firstlink.icrm.R;

public class CrmLogin extends Activity {
	// @Override
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// super.onBackPressed();
	// System.out.println("PPP");
	// Intent intent = new Intent();
	// intent.setAction("finish");
	// sendBroadcast(intent);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setAction("homeMenuFinish");
			sendBroadcast(intent);
			return true;
		}
		return false;
	}

	private EditText ed_password;
	private Button btn_login, btn_forgot, btn_reset;
	private TextView ed_email;
	Context context;
	boolean mReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crm_login);
		String strEmail = Utility.getConfigByText(this, Constants.USER_EMAIL);
		ed_email = (TextView) findViewById(R.id.ed_email);
		ed_email.setText(strEmail);
		ed_email.setFocusable(false);
		ed_password = (EditText) findViewById(R.id.ed_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_forgot = (Button) findViewById(R.id.forgetPassword);
		btn_reset = (Button) findViewById(R.id.reset);
		btn_login.setOnClickListener(login);
		btn_forgot.setOnClickListener(forgot);

		context = this;
		
		Intent intent = getIntent();
		mReset = intent.getBooleanExtra("reset", false);
		if(mReset){
			btn_reset.setVisibility(View.VISIBLE);
		}else{
			
		}

//		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});

		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

				Intent intent = new Intent();
				intent.setAction("homeMenuFinish");
				sendBroadcast(intent);
			}
		});

		findViewById(R.id.forgetPassword).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Intent intent = new Intent(CrmLogin.this,
						// ForgotPasswordDialogActivity.class);
						// CrmLogin.this.startActivity(intent);
						
						
						Intent intent = new Intent(CrmLogin.this,
								ForgotPasswordDialogActivity.class);
						intent.putExtra("email", ed_email.getText().toString());
						CrmLogin.this.startActivity(intent);
						

					}
				});
		
		findViewById(R.id.reset).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				GuiUtility.alert(CrmLogin.this, "", getString(R.string.reset_android_phone_desc), Gravity.CENTER, getString(R.string.yes), 
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								//clean all the table. as well as calendar.
								DatabaseUtility.getDatabaseHandler(CrmLogin.this);
								FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
								calendar.cleanCalendar(CrmLogin.this);
								Task task = new Task(Constants.DBHANDLER);
								task.cleanCalendar(CrmLogin.this);
								Constants.DBHANDLER.onReset(CrmLogin.this, Constants.DBHANDLER.getWritableDatabase());
								//clean all others.
								Constants.CONFIG_LIST = null;
								GuiUtility.alert(CrmLogin.this, getString(R.string.reset_android_phone_status), getString(R.string.reset_android_phone_status_desc), Gravity.CENTER, getString(R.string.ok), 
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0, int arg1) {
												Intent iLogout = new Intent("LOGOUT");
												
												LocalBroadcastManager.getInstance(CrmLogin.this).sendBroadcast(iLogout);
												
												
												iLogout.setAction("LOGOUT");
												sendBroadcast(iLogout);
												CrmLogin.this.finish();
											}								
								}, "", null);
							}
					
				}, getString(R.string.no), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//do nothing
					}
				});
			
			}
		});
		
	}

	private OnClickListener login = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String strEmail = ed_email.getText().toString();
			String strPassword = ed_password.getText().toString();
			if (strEmail.length() == 0) {
				GuiUtility.alert(CrmLogin.this,
						getString(R.string.email_error_title),
						getString(R.string.email_error_msg),
						getString(R.string.ok));
			} else if (strPassword.length() == 0) {
				GuiUtility.alert(CrmLogin.this,
						getString(R.string.password_error_title),
						getString(R.string.password_error_msg),
						getString(R.string.ok));
			} else if (!Utility.isValidEmail(strEmail)) {
				GuiUtility.alert(CrmLogin.this,
						getString(R.string.email_error_title),
						getString(R.string.email_error_msg),
						getString(R.string.ok));
			} else if (strEmail.length() > 0 && strPassword.length() > 0) {
				// pass to sync activity.
				Intent sync = new Intent(CrmLogin.this,
						SynchronizationData.class);
				Bundle bFlag = new Bundle();
				bFlag.putString("ACTION", "NORMAL");
				bFlag.putString("EMAIL", strEmail);
				bFlag.putString("PASSWORD", strPassword);
				bFlag.putString("FROM", "CRMLOGIN");
				sync.putExtras(bFlag);
				CrmLogin.this.startActivity(sync);
				CrmLogin.this.finish();
			}
		}
	};

	private OnClickListener forgot = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

}
