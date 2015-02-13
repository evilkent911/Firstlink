package com.trinerva.icrm;

import com.trinerva.icrm.settings.About;
import com.trinerva.icrm.settings.AndroidSetup;
import com.trinerva.icrm.settings.PasscodeLock;
import com.trinerva.icrm.settings.ReportSync;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import asia.firstlink.icrm.R;

public class TabSettingFragment extends Fragment {
	private Button sync_report, sales, passcode, setup, about;
	private String TAG = "TabSettingFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		RelativeLayout main = (RelativeLayout) inflater.inflate(R.layout.tab_setting, container, false);
		sync_report = (Button) main.findViewById(R.id.sync_report);
		//sales = (Button) main.findViewById(R.id.sales);
		passcode = (Button) main.findViewById(R.id.passcode);
		setup = (Button) main.findViewById(R.id.setup);
		about = (Button) main.findViewById(R.id.about);
		
		sync_report.setOnClickListener(doSyncReport);
		//sales.setOnClickListener(doSalesMarketing);
		about.setOnClickListener(doShowAbout);
		passcode.setOnClickListener(doPasscodeLock);
		setup.setOnClickListener(doSetup);
		return main;
	}
	
	private OnClickListener doSetup = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), AndroidSetup.class);
			v.getContext().startActivity(intent);
		}
	};
	
	private OnClickListener doPasscodeLock = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), PasscodeLock.class);
			v.getContext().startActivity(intent);
		}
	};
	
	private OnClickListener doSyncReport = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), ReportSync.class);
			v.getContext().startActivity(intent);
		}
	};
	/*
	private OnClickListener doSalesMarketing = new OnClickListener() {
		@Override
		public void onClick(View v) {
			registerForContextMenu(v);
			getActivity().openContextMenu(v);
			unregisterForContextMenu(v);
		}
	};*/
	
	private OnClickListener doShowAbout = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent about = new Intent(v.getContext(), About.class);
			TabSettingFragment.this.startActivity(about);
		}
	};
	/*
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.sales_enquiry:
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,  new String[]{Constants.SALES_EMAIL});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.SALES_SUBJECT);
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_in)));
				break;
			case R.id.support:
				Intent support = new Intent(android.content.Intent.ACTION_SEND);
				support.setType("plain/text");
				support.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Constants.SUPPORT_EMAIL});
				support.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.SUPPORT_SUBJECT);
				support.setType("text/plain");
				startActivity(Intent.createChooser(support, getString(R.string.send_email_in)));
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.sales_selection);
		MenuInflater inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.sales_enquiry, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}*/
}
