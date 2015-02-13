package com.trinerva.icrm.settings;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String strVersion = DeviceUtility.getVersionName(About.this);
		String strVersionTitle = getString(R.string.version).replace("[VERSION_CODE]", strVersion);
		setContentView(R.layout.about);
		TextView tv_version = (TextView) findViewById(R.id.version);
		tv_version.setText(strVersionTitle);
		
		Button contact = (Button) findViewById(R.id.sales);
		contact.setOnClickListener(doEmailSales);
		
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private OnClickListener doEmailSales = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			registerForContextMenu(v);
			About.this.openContextMenu(v);
			unregisterForContextMenu(v);
		}
	};
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.sales_enquiry:
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,  new String[]{Constants.SALES_EMAIL});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.SALES_SUBJECT);
				emailIntent.setType("text/plain");
				About.this.startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_in)));
				break;
			case R.id.support:
				Intent support = new Intent(android.content.Intent.ACTION_SEND);
				support.setType("plain/text");
				support.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Constants.SUPPORT_EMAIL});
				support.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.SUPPORT_SUBJECT);
				support.setType("text/plain");
				About.this.startActivity(Intent.createChooser(support, getString(R.string.send_email_in)));
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.sales_selection);
		MenuInflater inflater=About.this.getMenuInflater();
        inflater.inflate(R.menu.sales_enquiry, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
}

