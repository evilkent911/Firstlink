package com.trinerva.icrm;

import java.util.HashMap;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.SoapUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import asia.firstlink.icrm.R;

public class Lock extends Activity {
	private String TAG = "Lock";
	private Dialog loadingDialog;
	private EditText passcode;
	private Button unlock, forgot;
	private String strPin, strPasscode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock);
		passcode = (EditText) findViewById(R.id.passcode);
		unlock = (Button) findViewById(R.id.unlock);
		forgot = (Button) findViewById(R.id.forgot);

		strPin = Utility.getConfigByText(Lock.this, Constants.PHONE_PIN_ENABLED);
		strPasscode = Utility.getConfigByText(Lock.this, Constants.PHONE_PIN_VALUE);

		if (strPin != null && strPin.equalsIgnoreCase("true") && strPasscode != null && strPasscode.length() > 0) {
			unlock.setOnClickListener(doCheckUnlock);
			forgot.setOnClickListener(doGetForgotPasswcode);
		} else {
			//redirect to normal screen.
			Utility.updateConfigByText(Lock.this, Constants.PHONE_PIN_ENABLED, "FALSE");
			Utility.updateConfigByText(Lock.this, Constants.PHONE_PIN_VALUE, "");
			Lock.this.finish();
			Intent intent = new Intent(Lock.this, HomeMenuActivity.class);
			Lock.this.startActivity(intent);
		}

	}

	private OnClickListener doGetForgotPasswcode = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ForgotPasscode forgot = new ForgotPasscode();
			forgot.execute();
		}
	};

	private OnClickListener doCheckUnlock = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String code = passcode.getText().toString();
			if (code.length() != 0) {
				if (code.equals(strPasscode)) {
					//unlock
					Intent intent = new Intent(Lock.this, HomeMenuActivity.class);
					Lock.this.finish();
					Lock.this.startActivity(intent);
				} else {
					//show error
					GuiUtility.alert(Lock.this, getString(R.string.passcode_error), getString(R.string.passcode_error_not_empty), getString(R.string.ok));
				}
			} else {
				//passcode can't be empty.
				GuiUtility.alert(Lock.this, getString(R.string.passcode_error), getString(R.string.empty_passcode), getString(R.string.ok));
			}
		}
	};

	private class ForgotPasscode extends AsyncTask<String, Void, HashMap<String, Object>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(Lock.this, false, getString(R.string.processing));
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			HashMap<String, Object> hSoapResult = SoapUtility.doForgotPhonePin(Lock.this, DeviceUtility.getDeviceId(Lock.this), strPasscode);
			return hSoapResult;
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			String strTitle = "";
			String strMessage = "";

			DeviceUtility.log(TAG, result.toString());
			if (result.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
				strTitle = getString(R.string.synchronization_status_network_title);
				strMessage = getString(R.string.synchronization_status_network_desc);
			} else if (result.get("STATUS") == SoapUtility.STATUS.FAIL) {
				strTitle = getString(R.string.synchronization_status_fail_title);
				strMessage = getString(R.string.synchronization_status_fail_desc);
			} else if (result.get("RESULT").equals("SUCCESS")) {
				strTitle = getString(R.string.passcode_sent_title);
				strMessage = getString(R.string.passcode_sent_desc);
			}

			DeviceUtility.log(TAG, strTitle + " :: " + strMessage);

			if (strTitle != "" && strMessage != "") {
				GuiUtility.alert(Lock.this, strTitle, strMessage, getString(R.string.ok));
			} else {
				Lock.this.finish();
			}
		}
	}
}
