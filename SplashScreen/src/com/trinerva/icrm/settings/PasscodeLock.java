package com.trinerva.icrm.settings;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class PasscodeLock extends Activity {
	private ToggleButton lock;
	private Button change_passcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passcode_lock);
		lock = (ToggleButton) findViewById(R.id.lock);
		change_passcode = (Button) findViewById(R.id.change_passcode);

		lock.setOnClickListener(doCheck);
		change_passcode.setOnClickListener(doChangePasscode);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		String strPin = Utility.getConfigByText(PasscodeLock.this,
				Constants.PHONE_PIN_ENABLED);
		if (strPin.equalsIgnoreCase("TRUE")) {
			lock.setChecked(true);
			change_passcode.setVisibility(View.VISIBLE);
		} else {
			change_passcode.setVisibility(View.GONE);
		}
	}

	private OnClickListener doCheck = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (lock.getText().toString()
					.equalsIgnoreCase(getString(R.string.passcode_on))) {
				// turn on the passcode
				Intent intent = new Intent(PasscodeLock.this,
						NewPasscodeLock.class);
				PasscodeLock.this.startActivity(intent);
			} else {
				// turn off and update db.
				Utility.updateConfigByText(PasscodeLock.this,
						Constants.PHONE_PIN_ENABLED, "FALSE");
				Utility.updateConfigByText(PasscodeLock.this,
						Constants.PHONE_PIN_VALUE, "");
				change_passcode.setVisibility(View.GONE);
			}
		}
	};

	private OnClickListener doChangePasscode = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PasscodeLock.this, NewPasscodeLock.class);
			PasscodeLock.this.startActivity(intent);
		}
	};
}
