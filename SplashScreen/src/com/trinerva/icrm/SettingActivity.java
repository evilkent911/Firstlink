package com.trinerva.icrm;

import com.trinerva.icrm.contacts.ContactList;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.settings.About;
import com.trinerva.icrm.settings.AndroidSetup;
import com.trinerva.icrm.settings.PasscodeLock;
import com.trinerva.icrm.settings.ReportSync;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import asia.firstlink.icrm.R;

public class SettingActivity extends Activity {
	private Button sync_report, sales, passcode, setup, about;
	private String TAG = "TabSettingFragment";
	Context context;
SaveData saveData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.tab_setting);

		context = this;
		saveData = new SaveData(this);
		// RelativeLayout main = (RelativeLayout)
		// inflater.inflate(R.layout.tab_setting, container, false);
		sync_report = (Button) findViewById(R.id.sync_report);
		// sales = (Button)findViewById(R.id.sales);
		passcode = (Button) findViewById(R.id.passcode);
		setup = (Button) findViewById(R.id.setup);
		about = (Button) findViewById(R.id.about);

		sync_report.setOnClickListener(doSyncReport);
		// sales.setOnClickListener(doSalesMarketing);
		about.setOnClickListener(doShowAbout);
		passcode.setOnClickListener(doPasscodeLock);
		setup.setOnClickListener(doSetup);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.signout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(getString(R.string.synchronization_message));
				builder.setPositiveButton(R.string.sync_now,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								if(saveData.getBooleanData(GetData.checkLogin)){
									Intent sync = new Intent(SettingActivity.this, SynchronizationData.class);
									Bundle bFlag = new Bundle();
									bFlag.putString("ACTION", "NORMAL");
									bFlag.putString("EMAIL", saveData.getStringData(GetData.getUserEmail));
									bFlag.putString("PASSWORD", saveData.getStringData(GetData.getUserPassword));
									bFlag.putString("FROM", "CRMLOGIN");
									bFlag.putBoolean("SIGNOUT", true);
									sync.putExtras(bFlag);
									SettingActivity.this.startActivity(sync);
									
									signOutDialog();
								}else{
									Intent contact = new Intent(SettingActivity.this, CrmLogin.class);
									startActivity(contact);
								}
								
							}
						});
				builder.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								signOutDialog();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});
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
	 * private OnClickListener doSalesMarketing = new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { registerForContextMenu(v);
	 * getActivity().openContextMenu(v); unregisterForContextMenu(v); } };
	 */

	private OnClickListener doShowAbout = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent about = new Intent(v.getContext(), About.class);
			SettingActivity.this.startActivity(about);
		}
	};

	/*
	 * @Override public boolean onContextItemSelected(MenuItem item) {
	 * switch(item.getItemId()) { case R.id.sales_enquiry: Intent emailIntent =
	 * new Intent(android.content.Intent.ACTION_SEND);
	 * emailIntent.setType("plain/text");
	 * emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new
	 * String[]{Constants.SALES_EMAIL});
	 * emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
	 * Constants.SALES_SUBJECT); emailIntent.setType("text/plain");
	 * startActivity(Intent.createChooser(emailIntent,
	 * getString(R.string.send_email_in))); break; case R.id.support: Intent
	 * support = new Intent(android.content.Intent.ACTION_SEND);
	 * support.setType("plain/text");
	 * support.putExtra(android.content.Intent.EXTRA_EMAIL, new
	 * String[]{Constants.SUPPORT_EMAIL});
	 * support.putExtra(android.content.Intent.EXTRA_SUBJECT,
	 * Constants.SUPPORT_SUBJECT); support.setType("text/plain");
	 * startActivity(Intent.createChooser(support,
	 * getString(R.string.send_email_in))); break; } return
	 * super.onContextItemSelected(item); }
	 * 
	 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
	 * ContextMenuInfo menuInfo) {
	 * menu.setHeaderTitle(R.string.sales_selection); MenuInflater
	 * inflater=getActivity().getMenuInflater();
	 * inflater.inflate(R.menu.sales_enquiry, menu);
	 * super.onCreateContextMenu(menu, v, menuInfo); }
	 */

	void signOutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(getString(R.string.sign_out_confirmation));
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
//						startActivity(new Intent(SettingActivity.this,
//								LoginActivity.class));
						
						saveData.saveBooleanData(GetData.checkLogin, false);
						
						finish();
						
						Intent contact = new Intent(SettingActivity.this, CrmLogin.class);
						startActivity(contact);
						
						// startActivity(new
						// Intent(LoginActivity.this,PasswordResetSuccess.class));
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
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
		filter.addAction("LOGOUT");
		registerReceiver(this.broadcastReceiver, filter);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals("LOGOUT")){
			SettingActivity.this.finish();
			}
		}
	};

}
