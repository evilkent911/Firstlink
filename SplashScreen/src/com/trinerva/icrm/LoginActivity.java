 package com.trinerva.icrm;

import java.util.HashMap;
import com.trinerva.icrm.forgetpassword.ForgetPasswordActivity;
import com.trinerva.icrm.forgetpassword.PasswordResetSuccess;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import asia.firstlink.icrm.R;

public class LoginActivity extends Activity {
	Context context;

	private EditText ed_email, ed_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_password = (EditText) findViewById(R.id.ed_password);

		context = this;

		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String strEmail = ed_email.getText().toString();
				String strPassword = ed_password.getText().toString();
				if (strEmail.length() == 0
						|| Utility.isValidEmail(strEmail) == false) {
					GuiUtility.alert(LoginActivity.this,
							getString(R.string.email_error_title),
							getString(R.string.email_error_msg),
							getString(R.string.ok));
					ed_email.requestFocus();
				} else if (strPassword.length() == 0) {
					GuiUtility.alert(LoginActivity.this,
							getString(R.string.password_error_title),
							getString(R.string.password_error_msg),
							getString(R.string.ok));
					ed_password.requestFocus();
				} else {
					// final HashMap<String, String> hData = new HashMap<String,
					// String>();
					// hData.put("EMAIL", strEmail);
					// hData.put("PASSWORD", strPassword);
					// hData.put("DEVICE_ID",
					// DeviceUtility.getDeviceId(Login.this));
					//
					// GuiUtility.alert(Login.this,
					// getString(R.string.login_confirmation_title),
					// getString(R.string.login_confirmation_desc).replace("[EMAIL]",
					// strEmail), Gravity.CENTER,
					// getString(R.string.ok), new
					// DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface dialog,
					// int which) {
					// /*progress = ProgressDialog.show(Login.this, "",
					// getString(R.string.loading));
					// LoginSoap task = new LoginSoap();
					// task.execute(hData);*/
					//
					// Intent sync = new Intent(Login.this,
					// SynchronizationData.class);
					// Bundle bFlag = new Bundle();
					// bFlag.putString("ACTION", "ACTIVATION");
					// bFlag.putString("EMAIL", (String) hData.get("EMAIL"));
					// bFlag.putString("PASSWORD", (String)
					// hData.get("PASSWORD"));
					// bFlag.putString("FROM", "LOGIN");
					// sync.putExtras(bFlag);
					// Login.this.finish();
					// Login.this.startActivity(sync);
					// }
					// }, getString(R.string.cancel), new
					// DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface dialog,
					// int which) {
					// return;
					// }
					// });

					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle(R.string.login_info_confirmation);
					builder.setMessage(getString(
							R.string.login_info_confirmation_message, ed_email
									.getText().toString()));
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									// startActivity(new
									// Intent(LoginActivity.this,PasswordResetSuccess.class));
								}
							});
					builder.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancelled the dialog
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();

				}
			}
		});

		findViewById(R.id.forgetPassword).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(LoginActivity.this,
								ForgotPasswordDialogActivity.class));
					}
				});
	}

}
