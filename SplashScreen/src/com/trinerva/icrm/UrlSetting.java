package com.trinerva.icrm;

import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import asia.firstlink.icrm.R;

public class UrlSetting extends Activity {
	private EditText ed_mobile, ed_web;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.url_setting);
		ed_mobile = (EditText) findViewById(R.id.ed_mobile);
		ed_web = (EditText) findViewById(R.id.ed_web);
		
		ed_mobile.setText(Utility.getConfigByText(UrlSetting.this, Constants.MOBILE_SERVER));
		ed_web.setText(Utility.getConfigByText(UrlSetting.this,Constants.WEB_SERVER));
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		//update the mobile n crm.
		String strMobileUrl = ed_mobile.getText().toString();
		String strWebUrl = ed_web.getText().toString();
		if (strMobileUrl.length() == 0) {
			GuiUtility.alert(UrlSetting.this, getString(R.string.mobileurl_error_title), getString(R.string.mobileurl_error_msg), getString(R.string.ok));
			ed_mobile.requestFocus();
		} else if (strWebUrl.length() == 0) {
			GuiUtility.alert(UrlSetting.this, getString(R.string.weburl_error_title), getString(R.string.weburl_error_msg), getString(R.string.ok));
			ed_web.requestFocus();
		} else {
			Utility.updateConfigByText(UrlSetting.this, Constants.MOBILE_SERVER, strMobileUrl);
			Utility.updateConfigByText(UrlSetting.this, Constants.WEB_SERVER, strWebUrl);
			super.onBackPressed();
			UrlSetting.this.finish();
		}
	}
}
