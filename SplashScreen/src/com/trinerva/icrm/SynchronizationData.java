package com.trinerva.icrm;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.maps.MyLocationOverlay;
import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.settings.ReportSync;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.SoapUtility;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import asia.firstlink.icrm.R;

public class SynchronizationData extends Activity {

	@Override
	public void onBackPressed() {
	}
	

	public static String TAG = "SynchronizationData";
	private ProgressBar long_progress_bar;
	private TextView progress_status;
	ImageView cancel;
	private String strEmail = "";
	private String strPassword = "";
	private String strAction = "NORMAL";
	private String strFrom = "";
	private String strStartDate = "";
	private String strEndDate = "";
	private HashMap<String, String> hData = new HashMap<String, String>();
	private InitialisingSoap task;
	SaveData saveData;
	boolean signOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_data);

		saveData = new SaveData(this);

		long_progress_bar = (ProgressBar) findViewById(R.id.long_progress_bar);
		progress_status = (TextView) findViewById(R.id.progress_status);
		cancel = (ImageView) findViewById(R.id.cancel);
		Bundle bFlag = getIntent().getExtras();
		if (bFlag != null) {
			strEmail = bFlag.getString("EMAIL");
			strPassword = bFlag.getString("PASSWORD");
			strAction = bFlag.getString("ACTION");
			strFrom = bFlag.getString("FROM");
			strStartDate = bFlag.getString("START_DATE");
			strEndDate = bFlag.getString("END_DATE");
			signOut = bFlag.getBoolean("SIGNOUT");
			hData.put("EMAIL", strEmail);
			hData.put("PASSWORD", strPassword);
			hData.put("DEVICE_ID",
					DeviceUtility.getDeviceId(SynchronizationData.this));
			hData.put("ACTION", strAction);
		}
		
		if(!strAction.equalsIgnoreCase("ACTIVATION")){
//			cancel.setVisibility(View.VISIBLE);
		}
		

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (task != null
						&& task.getStatus() != AsyncTask.Status.FINISHED) {
					task.cancel(true);
					// kill the intent.
					GuiUtility.makeToast(SynchronizationData.this,
							getString(R.string.synchronization_cancelled),
							Toast.LENGTH_SHORT);

					if (saveData.getBooleanData(GetData.checkLogin)) {
						SynchronizationData.this.finish();
					} else {
						SynchronizationData.this.finish();
						Intent intent = new Intent(SynchronizationData.this,
								CrmLogin.class);
						startActivity(intent);
					}

				}

			}
		});

		// cancel.setOnClickListener(doCancelSync);

		// if(strAction.equals("ACTIVATION")){
		// String strData1 = "INSERT INTO " + DatabaseHandler.TABLE_FL_CONFIG +
		// " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"+
		// "VALUES ('SYSTEM', '0', 'delete','SYSTEM',datetime('now'));";
		// // DatabaseHandler.onDeletePermission
		// Constants.DBHANDLER.getWritableDatabase().execSQL(strData1);
		//
		//
		// }

		task = new InitialisingSoap();
		task.execute(hData);
	}

	private OnClickListener doCancelSync = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
				task.cancel(true);
				// kill the intent.
				GuiUtility.makeToast(SynchronizationData.this,
						getString(R.string.synchronization_cancelled),
						Toast.LENGTH_SHORT);

				if (saveData.getBooleanData(GetData.checkLogin)) {
					SynchronizationData.this.finish();
				} else {
					SynchronizationData.this.finish();
					Intent intent = new Intent(SynchronizationData.this,
							CrmLogin.class);
					startActivity(intent);
				}
			}
		}
	};

	private class InitialisingSoap extends
			AsyncTask<HashMap<String, String>, Integer, Integer> {
		public String[] aActivationSync = new String[] { "Activate", "Prefix","Account","EventCategory",
				"LeadAttitude", "LeadStatus", "LeadSource", "CalendarPriority",
				"CalendarAvailability", "TaskPriority", "TaskAvailability",
				"UserList", "OpportunityStage", "TaskKind", "LeadIndustry",
				"Broadcast", "Contact", "Opportunity", "Lead", "Calendar",
				"Task", "ContactActivity", "LeadActivity" };
		public String[] aNormalSync = new String[] { "Prefix","Account","EventCategory","LeadAttitude",
				"LeadStatus", "LeadSource", "CalendarPriority",
				"CalendarAvailability", "TaskPriority", "TaskAvailability",
				"UserList", "OpportunityStage", "TaskKind", "LeadIndustry",
				"Broadcast", "Contact", "Opportunity", "Lead", "Calendar",
				"Task", "ContactActivity", "LeadActivity" };
		public String[] aReportSync = new String[] { "GetActivityReport",
				"GetOpportunityReport" };
		public String[] aSyncList;
		private String strErrorMessage = "";

		/*
		 * private String[] concat(String[] a, String[] b) { String[] c= new
		 * String[a.length+b.length]; System.arraycopy(a, 0, c, 0, a.length);
		 * System.arraycopy(b, 0, c, a.length, b.length); return c; }
		 */

		/**
		 * Error msg: 0: no error 1: no network. 2: activation error 3: fail to
		 * connect 4: missing report date
		 * */

		@Override
		protected Integer doInBackground(HashMap<String, String>... params) {
			boolean bActivateStatus = true;
			int iError = 0;
			//DeviceUtility.log(TAG, "doInBackground: " + params[0]);
			HashMap<String, String> hInfo = params[0];
			String email = hInfo.get("EMAIL").toString();
			String password = hInfo.get("PASSWORD").toString();
			String device_id = hInfo.get("DEVICE_ID").toString();
			String action = hInfo.get("ACTION").toString();
			publishProgress(0);
			if (action.equalsIgnoreCase("ACTIVATION")) {

				System.out.println("Sync Data do in Activation");
				aSyncList = aActivationSync;
			} else if (action.equalsIgnoreCase("REPORT")) {
				System.out.println("Sync Data do in report");
				aSyncList = aReportSync;
			} else {
				System.out.println("Sync Data do in normal sync");
				aSyncList = aNormalSync;
				Constants.MASTER_CATEGORY_LIST = null;
//				Constants.MASTER_LEAD_STATUS = null;
			}

			int iSyncSize = aSyncList.length;
			boolean bClean = true;
			HashMap<String, Object> hSoapResult = null;
			if (aSyncList.length > 0) {
				System.out.println("Sync Data length more den 0");
				//DeviceUtility.log(TAG, aSyncList.toString());
				for (int i = 0; i < iSyncSize; i++) {
					if (!isCancelled()) {
						publishProgress(i);
						if (aSyncList[i].equalsIgnoreCase("Activate")) {
							hSoapResult = SoapUtility.doActivate(
									SynchronizationData.this, email, password,
									device_id);
							//DeviceUtility.log(TAG,
//									"ACTIVATE: " + hSoapResult.toString());
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SUCCESS) {
								bActivateStatus = (Boolean) hSoapResult
										.get("RESULT");
								if (bActivateStatus == false) {
									iError = 2;
									break;
								}
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}

						} else if (aSyncList[i].equalsIgnoreCase("Prefix")) {
							System.out.println("Sync Data do in prefix");
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							bClean = false;
							ArrayList<MasterInfo> aMasterList = ((ArrayList<MasterInfo>) hSoapResult
									.get("RESULT"));
							////DeviceUtility.log(TAG,
//									"PREFIX: " + hSoapResult.toString());
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								System.out.println("Sync Data error : " + iError);
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								System.out.println("Sync Data error : " + iError);
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								System.out.println("Sync Data error : " + iError);
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								System.out.println("Sync Data error : " + iError);
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								System.out.println("Sync Data error : " + iError);
								break;
							} else if (aMasterList.isEmpty()) {
								iError = 2;
								System.out.println("Sync Data error : " + iError);
								break;
							}
							
							hSoapResult = SoapUtility
									.deletePermission(
											SynchronizationData.this,
											strEmail,
											strPassword,
											DeviceUtility
													.getDeviceId(SynchronizationData.this));

							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SUCCESS) {
								System.out.println("Sync Data success");
								//kiat
								
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Account")) {
							hSoapResult = SoapUtility.getSyncAccount(
									SynchronizationData.this, email, password,
									device_id,aSyncList[i],bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("EventCategory")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, "eventcategory", bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("LeadAttitude")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("LeadStatus")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("LeadSource")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("CalendarPriority")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("CalendarAvailability")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("TaskPriority")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("TaskAvailability")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("UserList")) {
							hSoapResult = SoapUtility.GetEmployeeListForMobileSync(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("OpportunityStage")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("TaskKind")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("LeadIndustry")) {
							hSoapResult = SoapUtility.getMasterList(
									SynchronizationData.this, email, password,
									device_id, aSyncList[i], bClean);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Broadcast")) {
							hSoapResult = SoapUtility.getBroadcast(
									SynchronizationData.this, email, password,
									device_id);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Contact")) {
							hSoapResult = SoapUtility.doSyncContact(
									SynchronizationData.this, email, password,
									device_id);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Opportunity")) {
							hSoapResult = SoapUtility.doSyncOpportunity(
									SynchronizationData.this, email, password,
									device_id);
							System.out.println("++++++++++++++++++++++ ");
							System.out.println(hSoapResult.toString());
							System.out.println("++++++++++++++++++++++ ");
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Lead")) {
							hSoapResult = SoapUtility.doSyncLead(
									SynchronizationData.this, email, password,
									device_id);
//							//DeviceUtility.log(TAG, hSoapResult.toString());
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								strErrorMessage = hSoapResult.get(
										"ERROR_MESSAGE").toString();
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Calendar")) {
							hSoapResult = SoapUtility.doSyncEvent(
									SynchronizationData.this, email, password,
									device_id);
//							System.out.println("++++++++++++++++++++++ ");
//							System.out.println(hSoapResult.toString());
//							System.out.println("++++++++++++++++++++++ ");
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i].equalsIgnoreCase("Task")) {
							hSoapResult = SoapUtility.doSyncTask(
									SynchronizationData.this, email, password,
									device_id);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("ContactActivity")) {
							hSoapResult = SoapUtility.doSyncContactActivityLog(
									SynchronizationData.this, email, password,
									device_id);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("LeadActivity")) {
							hSoapResult = SoapUtility.doSyncLeadActivityLog(
									SynchronizationData.this, email, password,
									device_id);
							if (hSoapResult.get("STATUS") == SoapUtility.STATUS.NETWORK_ERROR) {
								iError = 1;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.FAIL) {
								iError = 3;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("GetActivityReport")) {
							if (strStartDate != null && strEndDate != null) {
								hSoapResult = SoapUtility.doGetActivityReport(
										SynchronizationData.this, email,
										password, device_id, strStartDate,
										strEndDate);
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							} else {
								iError = 4;
								break;
							}
						} else if (aSyncList[i]
								.equalsIgnoreCase("GetOpportunityReport")) {
							if (strStartDate != null && strEndDate != null) {
								hSoapResult = SoapUtility
										.doGetOpportunityReport(
												SynchronizationData.this,
												email, password, device_id,
												strStartDate, strEndDate);
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR) {
								iError = 5;
								if (hSoapResult.get("ERROR_MESSAGE") != null) {
									strErrorMessage = hSoapResult.get(
											"ERROR_MESSAGE").toString();
								}
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.AUTHENTICATE_FAIL) {
								iError = 6;
								break;
							} else if (hSoapResult.get("STATUS") == SoapUtility.STATUS.SYNC_ERROR_RESET) {
								iError = 7;
								break;
							} else {
								iError = 4;
								break;
							}
						}
					}
				}
			}
			return new Integer(iError);
		}

		@Override
		protected void onPostExecute(final Integer result) {
			System.out.println("post execute result : " + result);
			if (result == 0) {
				// GuiUtility.alert(SynchronizationData.this,
				// getString(R.string.synchronization_status_success_title),
				// getString(R.string.synchronization_status_success_desc),
				// Gravity.CENTER, getString(R.string.ok), new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				//
				// if (strAction.equalsIgnoreCase("ACTIVATION")) {
				saveData.saveStringData(GetData.getUserEmail, strEmail);
				saveData.saveStringData(GetData.getUserPassword, strPassword);
				saveData.saveBooleanData(GetData.checkLogin, true);

				Intent intent;
				if (!signOut) {
					intent = new Intent(SynchronizationData.this,
							HomeMenuActivity.class);
					SynchronizationData.this.startActivity(intent);
				}

				intent = new Intent();
				intent.setAction("finish");
				sendBroadcast(intent);
				// }
				SynchronizationData.this.finish();

				// }
				// }, "", null);
				// go to other page.
				// Login.this.finish();
				// GuiUtility.alert(Login.this, "Success", "Login Succcess",
				// getString(R.string.ok));
				// Intent intent = new Intent(SynchronizationData.this,
				// TabsFragmentActivity.class);
				// SynchronizationData.this.startActivity(intent);
				// SynchronizationData.this.finish();
			} else {
				String strTitle = "";
				String strMessage = "";
				switch (result) {
				case 1: // no network
					strTitle = getString(R.string.synchronization_status_network_title);
					strMessage = getString(R.string.synchronization_status_network_desc);
					break;
				case 2: // activation error
					strTitle = getString(R.string.login_fail_title);
					strMessage = getString(R.string.login_fail_desc);
					break;
				case 3: // fail to connect
					strTitle = getString(R.string.synchronization_status_fail_title);
					strMessage = getString(R.string.synchronization_status_fail_desc);
					break;
				case 4:
					strTitle = getString(R.string.synchronization_invalid_report_date_title);
					strMessage = getString(R.string.synchronization_invalid_report_date_desc);
					break;
				case 5: // sync error.
					strTitle = getString(R.string.synchronization_status_sync_fail_title);
					strMessage = getString(R.string.synchronization_status_sync_fail_desc);
					break;
				case 6: // authenticate error.
					strTitle = getString(R.string.synchronization_status_sync_authenticate_fail_title);
					strMessage = getString(R.string.synchronization_status_sync_authenticate_fail_desc);
					break;
				case 7: // authenticate error.
					strTitle = getString(R.string.synchronization_status_fail_title);
					strMessage = getString(R.string.synchronization_status_fail_desc);
					break;
				}

				if (strErrorMessage.length() > 0) {
					strMessage = strErrorMessage;
				}

				// show error and back to previous intent.
				GuiUtility.alert(SynchronizationData.this, strTitle,
						strMessage, Gravity.CENTER, getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = null;
								if(result == 7){
									intent = new Intent(
											SynchronizationData.this,
											CrmLogin.class);
									intent.putExtra("reset", true);
									finish();
								}else if (strFrom.equalsIgnoreCase("CRMLOGIN")) {	
									intent = new Intent(
											SynchronizationData.this,
											CrmLogin.class);
									finish();
									
								} else if (strFrom.equalsIgnoreCase("LOGIN")) {
									intent = new Intent(
											SynchronizationData.this,
											Login.class);
								} else if (strFrom.equalsIgnoreCase("REPORT")) {
									intent = new Intent(
											SynchronizationData.this,
											ReportSync.class);
								}
								if (intent != null) {
									SynchronizationData.this
											.startActivity(intent);
								}
								SynchronizationData.this.finish();
							}
						}, "", null);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int iProcess = values[0];
			//DeviceUtility.log(TAG, "onProgressUpdate: " + iProcess);
			int diff = Math.round(100 / aSyncList.length);
			long_progress_bar.setProgress((iProcess + 1) * diff);
			//DeviceUtility.log(TAG, "onProgressUpdate progress: "
//					+ (iProcess + 1) * diff);
			int iLast = aSyncList.length;

			if (iProcess == iLast) {
				long_progress_bar.setProgress(100);
			} else {
				if (aSyncList[iProcess].equalsIgnoreCase("Activate")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Activation]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Prefix")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-Prefix]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("LeadAttitude")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-LeadAttitude]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("LeadStatus")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-LeadStatus]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("LeadSource")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-LeadSource]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("CalendarPriority")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-CalendarPriority]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("CalendarAvailability")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-CalendarAvailability]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("TaskPriority")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-TaskPriority]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Account")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-Account]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("TaskAvailability")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-TaskAvailability]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("UserList")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-UserList]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("OpportunityStage")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-OpportunityStage]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("TaskKind")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-TaskKind]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("LeadIndustry")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [MasterList-LeadIndustry]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Broadcast")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Broadcast]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Contact")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Contact]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Opportunity")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Opportunity]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Lead")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Lead]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Calendar")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Calendar]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("Task")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [Task]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("ContactActivity")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [ContactActivity]");
				} else if (aSyncList[iProcess].equalsIgnoreCase("LeadActivity")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [LeadActivity]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("GetActivityReport")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [GetActivityReport]");
				} else if (aSyncList[iProcess]
						.equalsIgnoreCase("GetOpportunityReport")) {
					progress_status
							.setText("Synchronization data to CRM Server ... [GetOpportunityReport]");
				}
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
}
