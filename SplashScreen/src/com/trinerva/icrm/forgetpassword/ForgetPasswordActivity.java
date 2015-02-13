package com.trinerva.icrm.forgetpassword;

import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import asia.firstlink.icrm.R;
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
import android.widget.TextView;

public class ForgetPasswordActivity extends Activity {

	Context context;
	String PwdQuestion;
	String PwdAnswear;
	EditText mAnswear;
	TextView mQuestion;
	ProgressDialog dialogLoading;
	SendForgotPasswordEmailSoap task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_forget_password);

		context = this;

		Intent intent = getIntent();
		PwdQuestion = intent.getStringExtra("PwdQuestion");
		PwdAnswear = intent.getStringExtra("PwdAnswear");

		mQuestion = (TextView) findViewById(R.id.question);
		mQuestion.setClickable(true);
		mAnswear = (EditText) findViewById(R.id.answear);
		mQuestion.setText(PwdQuestion);

		findViewById(R.id.confing).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mAnswear.getText().toString().equals(PwdAnswear)) {

					HashMap<String, String> hData = new HashMap<String, String>();
					hData.put("EMAIL", Utility.getConfigByText(context,
							Constants.USER_EMAIL));
					hData.put("PASSWORD", "");
					hData.put("DEVICE_ID", DeviceUtility
							.getDeviceId(ForgetPasswordActivity.this));

					dialogLoading = new ProgressDialog(ForgetPasswordActivity.this);
					dialogLoading.setMessage(getString(R.string.loading));
					dialogLoading.show();

					task = new SendForgotPasswordEmailSoap();
					task.execute(hData);

				} else {
					GuiUtility.alert(ForgetPasswordActivity.this,
							getString(R.string.answer_error_title),
							getString(R.string.answer_error_msg),
							getString(R.string.ok));
				}

			}
		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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

			if (intent.getAction().equals("finish")) {
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
					.sendForgotPasswordEmail(ForgetPasswordActivity.this,
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
				startActivity(new Intent(ForgetPasswordActivity.this,
						PasswordResetSuccess.class));
			} else {
				GuiUtility.alert(ForgetPasswordActivity.this,
						getString(R.string.connect_crm_server),
						getString(R.string.connection_error),
						getString(R.string.ok));
			}
		}
	}
}
