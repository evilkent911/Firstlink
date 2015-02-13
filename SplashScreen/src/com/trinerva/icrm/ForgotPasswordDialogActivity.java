package com.trinerva.icrm;

import java.util.HashMap;
import org.ksoap2.serialization.SoapObject;
import com.trinerva.icrm.forgetpassword.ForgetPasswordActivity;
import com.trinerva.icrm.forgetpassword.PasswordResetSuccess;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.SoapUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import asia.firstlink.icrm.R;

public class ForgotPasswordDialogActivity extends Activity {

	String PwdQuestion;
	String PwdAnswear;

	EditText ed_email;

	String strEmail;

	ProgressDialog dialogLoading;

	PasswordRecoverySoap task;

	Context context;
	
	SendForgotPasswordEmailSoap sendEmailTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password_dialog_box);

		Intent intent = getIntent();
		PwdQuestion = intent.getStringExtra("PwdQuestion");
		PwdAnswear = intent.getStringExtra("PwdAnswear");

		context = this;

		ed_email = (EditText) findViewById(R.id.ed_email);

		strEmail = Utility.getConfigByText(this, Constants.USER_EMAIL);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("strEmail = " + strEmail);
				if (ed_email.getText().toString().equals(strEmail)
						|| strEmail.equals("")) {
					// Intent intent = new
					// Intent(ForgotPasswordDialogActivity.this,
					// ForgetPasswordActivity.class);
					// intent.putExtra("PwdQuestion", PwdQuestion);
					// intent.putExtra("PwdAnswear", PwdAnswear);
					// ForgotPasswordDialogActivity.this.startActivity(intent);

					HashMap<String, String> hData = new HashMap<String, String>();
					String strEmail = ed_email.getText().toString();

					//
					//
					// Intent intent = new Intent(CrmLogin.this,
					// ForgetPasswordActivity.class);
					// CrmLogin.this.startActivity(intent);

					if (strEmail.length() == 0) {
						GuiUtility.alert(ForgotPasswordDialogActivity.this,
								getString(R.string.email_error_title),
								getString(R.string.email_error_msg),
								getString(R.string.ok));
					} else if (!Utility.isValidEmail(strEmail)) {
						GuiUtility.alert(ForgotPasswordDialogActivity.this,
								getString(R.string.email_error_title),
								getString(R.string.email_error_msg),
								getString(R.string.ok));
					} else if (strEmail.length() > 0) {

						hData.put("EMAIL", strEmail);
						hData.put("PASSWORD", "");
						hData.put("DEVICE_ID", DeviceUtility
								.getDeviceId(ForgotPasswordDialogActivity.this));

						dialogLoading = new ProgressDialog(context);
						dialogLoading.setMessage(getString(R.string.loading));
						dialogLoading.show();
						task = new PasswordRecoverySoap();
						task.execute(hData);

					}

				} else {
					GuiUtility.alert(ForgotPasswordDialogActivity.this,
							getString(R.string.email_not_match_title),
							getString(R.string.email_not_match_msg),
							getString(R.string.ok));
				}
			}
		});

	}

	private class PasswordRecoverySoap extends
			AsyncTask<HashMap<String, String>, Void, Boolean> {

		@Override
		protected Boolean doInBackground(HashMap... arg0) {

			HashMap<String, String> hInfo = arg0[0];
			String email = hInfo.get("EMAIL").toString();
			String device_id = hInfo.get("DEVICE_ID").toString();
			System.out.println(device_id+" haha = omg = "+email);
			HashMap<String, Object> hSoapResult = SoapUtility.passwordRecovery(
					ForgotPasswordDialogActivity.this, email, "", device_id);

			System.out.println("hSoapResult = " + hSoapResult);

			// HashMap<String, Object> hReturn = SoapUtility.doActivate(
			// CrmLogin.this, arg0[0].get("EMAIL").toString(),
			// arg0[0].get("PASSWORD").toString(), arg0[0]
			// .get("DEVICE_ID").toString());

			if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SUCCESS) {

				SoapObject oResponse = (SoapObject) hSoapResult
						.get("SOAP_OBJECT");

				System.out.println("haha = " + oResponse);

				SoapObject pii = (SoapObject) oResponse.getProperty(2);
				System.out.println("haha s = " + pii);
				
				if(pii != null){
					
				
				// System.out.println("pp = " + pii.toString());
				SoapObject pii2 = (SoapObject) pii.getProperty(0);
				if (pii2.getProperty("PwdQuestion").toString()
								.equals("anyType{}")) {
					return false;
				} else {
					System.out.println("qqq = "
							+ pii2.getProperty("PwdQuestion").toString());
					PwdQuestion = pii2.getProperty("PwdQuestion").toString();
					PwdAnswear = pii2.getProperty("PwdAnswear").toString();
					
					System.out.println("id and email = "+pii2.getProperty("PwdAnswear").toString());

					return true;
				}
				
				}else{
					return false;
				}

			} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
			} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
			} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
			} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
			}

			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// if (progress.isShowing()) {
			// progress.dismiss();
			// }
			//
			dialogLoading.dismiss();

			if (result != null) {
				if (result.booleanValue() == true) {
					Intent intent = new Intent(
							ForgotPasswordDialogActivity.this,
							ForgetPasswordActivity.class);
					intent.putExtra("PwdQuestion", PwdQuestion);
					intent.putExtra("PwdAnswear", PwdAnswear);
					ForgotPasswordDialogActivity.this.startActivity(intent);
				} else {
//					GuiUtility
//							.alert(ForgotPasswordDialogActivity.this,
//									getString(R.string.reset_password_successfully_title),
//									getString(R.string.reset_password_successfully),
//									getString(R.string.ok));
					
					HashMap<String, String> hData = new HashMap<String, String>();
					hData.put("EMAIL", Utility.getConfigByText(context,
							Constants.USER_EMAIL));
					hData.put("PASSWORD", "");
					hData.put("DEVICE_ID", DeviceUtility
							.getDeviceId(ForgotPasswordDialogActivity.this));

					dialogLoading = new ProgressDialog(ForgotPasswordDialogActivity.this);
					dialogLoading.setMessage(getString(R.string.loading));
					dialogLoading.show();
					
					sendEmailTask = new SendForgotPasswordEmailSoap();
					sendEmailTask.execute(hData);
					
				}
			} else {
				GuiUtility.alert(ForgotPasswordDialogActivity.this,
						getString(R.string.connect_crm_server),
						getString(R.string.connection_error),
						getString(R.string.ok));
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");
		registerReceiver(this.broadcastReceiver, filter);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
System.out.println("ppp = "+intent.getAction());
			if(intent.getAction().equals("finish")){
				finish();
			}
			}
	};
	
	private class SendForgotPasswordEmailSoap extends
	AsyncTask<HashMap<String, String>, Void, Boolean> {

@Override
protected Boolean doInBackground(HashMap... arg0) {

	HashMap<String, String> hInfo = arg0[0];
	String email = hInfo.get("EMAIL").toString();
	String device_id = hInfo.get("DEVICE_ID").toString();
	HashMap<String, Object> hSoapResult = SoapUtility
			.sendForgotPasswordEmail(ForgotPasswordDialogActivity.this,
					email, "", device_id);

	if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SUCCESS) {

		SoapObject oResponse = (SoapObject) hSoapResult
				.get("SOAP_OBJECT");
		return true;
	} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
	} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
	} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
	} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
	}

	return null;

}

@Override
protected void onPostExecute(Boolean result) {
	// if (progress.isShowing()) {
	// progress.dismiss();
	// }
	//
	dialogLoading.dismiss();

	if (result != null) {
		startActivity(new Intent(ForgotPasswordDialogActivity.this,
				PasswordResetSuccess.class));
	} else {
		GuiUtility.alert(ForgotPasswordDialogActivity.this,
				getString(R.string.connect_crm_server),
				getString(R.string.connection_error),
				getString(R.string.ok));
	}
}
}
}
