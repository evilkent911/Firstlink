package com.trinerva.icrm;

import java.util.HashMap;
import com.trinerva.icrm.forgetpassword.ForgetPasswordActivity;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.SoapUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import asia.firstlink.icrm.R;

public class Login extends Activity {
	private EditText ed_email, ed_password;
	 private ImageView settings;
	private Button btn_login, btn_cancel;
	private String TAG = "Login";
	private ProgressDialog progress;
	SaveData saveData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		saveData = new SaveData(this);

		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_password = (EditText) findViewById(R.id.ed_password);
		// ed_mobile = (EditText) findViewById(R.id.ed_mobile);
		// ed_web = (EditText) findViewById(R.id.ed_web);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		 settings = (ImageView) findViewById(R.id.settings);
		btn_login.setOnClickListener(login);
		btn_cancel.setOnClickListener (cancel);
		 settings.setOnClickListener(doSetting);
		 
		
		// ed_mobile.setText(Constants.MOBILE_SERVER);
		// ed_web.setText(Constants.WEB_SERVER);
		
		findViewById(R.id.forgetPassword).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, ForgotPasswordDialogActivity.class);
				intent.putExtra("email", ed_email.getText().toString());
				Login.this.startActivity(intent);
			}
		});

	}

	 private OnClickListener doSetting = new OnClickListener() {
	 @Override
	 public void onClick(View arg0) {
	 Intent intent = new Intent(Login.this, UrlSetting.class);
	 Login.this.startActivity(intent);
	 }
	 };

	private OnClickListener login = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String strEmail = ed_email.getText().toString();
			String strPassword = ed_password.getText().toString();
			if (strEmail.length() == 0
					|| Utility.isValidEmail(strEmail) == false) {
				GuiUtility.alert(Login.this,
						getString(R.string.email_error_title),
						getString(R.string.email_error_msg),
						getString(R.string.ok));
				ed_email.requestFocus();
			} else if (strPassword.length() == 0) {
				GuiUtility.alert(Login.this,
						getString(R.string.password_error_title),
						getString(R.string.password_error_msg),
						getString(R.string.ok));
				ed_password.requestFocus();
			} else {
				final HashMap<String, String> hData = new HashMap<String, String>();
				hData.put("EMAIL", strEmail);
				hData.put("PASSWORD", strPassword);
				hData.put("DEVICE_ID", DeviceUtility.getDeviceId(Login.this));

				GuiUtility.alert(
						Login.this,
						getString(R.string.login_confirmation_title),
						getString(R.string.login_confirmation_desc).replace(
								"[EMAIL]", strEmail), Gravity.CENTER,
						getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								/*
								 * progress = ProgressDialog.show(Login.this,
								 * "", getString(R.string.loading)); LoginSoap
								 * task = new LoginSoap(); task.execute(hData);
								 */

								Intent sync = new Intent(Login.this,
										SynchronizationData.class);
								Bundle bFlag = new Bundle();
//								bFlag.putString("ACTION", "NORMAL");
								bFlag.putString("ACTION", "ACTIVATION");
								bFlag.putString("EMAIL",
										(String) hData.get("EMAIL"));
								bFlag.putString("PASSWORD",
										(String) hData.get("PASSWORD"));
								bFlag.putString("FROM", "LOGIN");
								sync.putExtras(bFlag);
								Login.this.finish();
								Login.this.startActivity(sync);
							}
						}, getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
			}
		}
	};

	private OnClickListener cancel = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Login.this.finish();
		}
	};

//	private class LoginSoap extends AsyncTask<HashMap<String, String>, Void, Boolean> {
//
//		@Override
//		protected Boolean doInBackground(HashMap... arg0) {
//			DeviceUtility.log(TAG, "doInBackground: " + arg0[0]);
//			HashMap<String, Object> hReturn = SoapUtility.doActivate(
//					Login.this, arg0[0].get("EMAIL").toString(),
//					arg0[0].get("PASSWORD").toString(), arg0[0]
//							.get("DEVICE_ID").toString());
//			boolean bReturn = (Boolean) hReturn.get("RESULT");
//			DeviceUtility.log(TAG, "doInBackground return: " + bReturn);
//			return new Boolean(bReturn);
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			if (progress.isShowing()) {
//				progress.dismiss();
//			}
//
//			if (result.booleanValue() == true) {
//				// go to other page.
//				// Login.this.finish();
//				// GuiUtility.alert(Login.this, "Success", "Login Succcess",
//				// getString(R.string.ok));
//				
//				Intent intent = new Intent(Login.this, HomeMenuActivity.class);
//				Login.this.startActivity(intent);
//				Login.this.finish();
//				System.out.println("?? = 1");
//				intent = new Intent();
//				intent.setAction("finish");
//				sendBroadcast(intent);
//			} else {
//				// show error
//				GuiUtility.alert(Login.this,
//						getString(R.string.login_fail_title),
//						getString(R.string.login_fail_desc),
//						getString(R.string.ok));
//			}
//		}
//	}
}
