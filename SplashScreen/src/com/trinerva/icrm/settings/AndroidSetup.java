package com.trinerva.icrm.settings;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AndroidSetup extends Activity {
	private String TAG = "AndroidSetup";
	private Button import_contact, clear_calendar, reset_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_setup);
		import_contact = (Button) findViewById(R.id.import_contact);
		clear_calendar = (Button) findViewById(R.id.clear_calendar);
		reset_phone = (Button) findViewById(R.id.reset_phone);
		
		import_contact.setOnClickListener(doImportContact);
		clear_calendar.setOnClickListener(doClearCalendar);
		reset_phone.setOnClickListener(doResetPhone);
		
	findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private OnClickListener doResetPhone = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			GuiUtility.alert(AndroidSetup.this, "", getString(R.string.reset_android_phone_desc), Gravity.CENTER, getString(R.string.yes), 
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//clean all the table. as well as calendar.
							DeviceUtility.log(TAG, "clean the calendar.");
							DatabaseUtility.getDatabaseHandler(AndroidSetup.this);
							FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
							calendar.cleanCalendar(AndroidSetup.this);
							Task task = new Task(Constants.DBHANDLER);
							task.cleanCalendar(AndroidSetup.this);
							Constants.DBHANDLER.onReset(AndroidSetup.this, Constants.DBHANDLER.getWritableDatabase());
							//clean all others.
							Constants.CONFIG_LIST = null;
							GuiUtility.alert(AndroidSetup.this, getString(R.string.reset_android_phone_status), getString(R.string.reset_android_phone_status_desc), Gravity.CENTER, getString(R.string.ok), 
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											Intent iLogout = new Intent("LOGOUT");
											
											LocalBroadcastManager.getInstance(AndroidSetup.this).sendBroadcast(iLogout);
											
											
											iLogout.setAction("LOGOUT");
											sendBroadcast(iLogout);
											AndroidSetup.this.finish();
										}								
							}, "", null);
						}
				
			}, getString(R.string.no), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});
		}
	};
	
	private OnClickListener doImportContact = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AndroidSetup.this, ImportContact.class);
			AndroidSetup.this.startActivity(intent);
		}
	};
	
	private OnClickListener doClearCalendar = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			GuiUtility.alert(AndroidSetup.this, getString(R.string.clear_android_calendar), getString(R.string.clear_android_calendar_desc), Gravity.CENTER, getString(R.string.yes), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//clean the calendar.
					DeviceUtility.log(TAG, "clean the calendar.");
					DatabaseUtility.getDatabaseHandler(AndroidSetup.this);
					FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
					calendar.cleanCalendar(AndroidSetup.this);
					Task task = new Task(Constants.DBHANDLER);
					task.cleanCalendar(AndroidSetup.this);
					
				}
			}, getString(R.string.no), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
					DeviceUtility.log(TAG, "Do nothing");
				}
			});
		}
	};

}
