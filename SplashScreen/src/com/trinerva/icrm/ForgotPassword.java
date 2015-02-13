package com.trinerva.icrm;

import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothClass.Device;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import asia.firstlink.icrm.R;

public class ForgotPassword extends Activity {
	private String TAG = "ForgotPassword";
	private WebView webView;
	private Dialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		
		webView = (WebView) findViewById(R.id.forgot_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setInitialScale(0);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}
			}
		});
		
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				loadingDialog = GuiUtility.getLoadingDialog(ForgotPassword.this, false, getString(R.string.loading));
			}
			
		});
		LoadAddress load = new LoadAddress();
		load.execute();
	}
	
	private class LoadAddress extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ForgotPassword.this, false, getString(R.string.processing));
		}

		@Override
		protected String doInBackground(String... arg0) {
			return Utility.getConfigByText(ForgotPassword.this, "CRM_SERVER_WEB");
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			DeviceUtility.log(TAG, result);
			webView.loadUrl(result);
		}
	}
}
