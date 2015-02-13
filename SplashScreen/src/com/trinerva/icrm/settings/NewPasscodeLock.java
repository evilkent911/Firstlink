package com.trinerva.icrm.settings;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewPasscodeLock extends Activity {
	private String strOldPin = null;
	private ImageView save;
	private EditText passcode, old, confirm;
	private LinearLayout old_passcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_passcode_lock);

		save = (ImageView) findViewById(R.id.save);
		old_passcode = (LinearLayout) findViewById(R.id.old_passcode);
		passcode = (EditText) findViewById(R.id.passcode);
		old = (EditText) findViewById(R.id.old);
		confirm = (EditText) findViewById(R.id.confirm);

		String strPin = Utility.getConfigByText(NewPasscodeLock.this, Constants.PHONE_PIN_ENABLED);
		if (strPin.equalsIgnoreCase("TRUE")) {
			strOldPin = Utility.getConfigByText(NewPasscodeLock.this, Constants.PHONE_PIN_VALUE);
		}

		if (strOldPin != null) {
			old_passcode.setVisibility(View.VISIBLE);
		} else {
			old_passcode.setVisibility(View.GONE);
		}

		save.setOnClickListener(doSave);
		
	findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private OnClickListener doSave = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String strOldPincode = null;
			String strNewPincode = null;
			String strConfirmPinCode = null;
			boolean bOldPinMatch = false;
			boolean bPinMatch = false;

			if (strOldPin != null) {
				strOldPincode = old.getText().toString();
				//compare.
				if (strOldPincode.equalsIgnoreCase(strOldPin)) {
					bOldPinMatch = true;
				}
			} else {
				//since is new pin. old pin consider match.
				bOldPinMatch = true;
			}

			if (bOldPinMatch) {
				strNewPincode = passcode.getText().toString();
				strConfirmPinCode = confirm.getText().toString();
				if (strNewPincode != null && strNewPincode.length() > 0) {
					if (strNewPincode.equalsIgnoreCase(strConfirmPinCode)) {
						Utility.updateConfigByText(NewPasscodeLock.this, Constants.PHONE_PIN_ENABLED, "TRUE");
						Utility.updateConfigByText(NewPasscodeLock.this, Constants.PHONE_PIN_VALUE, strNewPincode);
						GuiUtility.alert(NewPasscodeLock.this, getString(R.string.passcode_success_title), getString(R.string.passcode_success_desc), Gravity.CENTER, getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										NewPasscodeLock.this.finish();
									}
						}, "", null);
					} else {
						//pin not match
						//passcode_error_not_match
						GuiUtility.alert(NewPasscodeLock.this, getString(R.string.passcode_error_title), getString(R.string.passcode_error_not_match), getString(R.string.ok));
					}
				} else {
					//pin shouldn't empty.
					//passcode_error_not_empty
					GuiUtility.alert(NewPasscodeLock.this, getString(R.string.passcode_error_title), getString(R.string.passcode_error_not_empty), getString(R.string.ok));
				}
			} else {
				GuiUtility.alert(NewPasscodeLock.this, getString(R.string.passcode_error_title), getString(R.string.passcode_error_old_passcode_not_match), getString(R.string.ok));
			}
		}
	};

}
