package com.trinerva.icrm.contacts;

import java.util.ArrayList;
import java.util.Calendar;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.Opportunity;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.object.OpportunityDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class OpportunityInfo extends Activity {
	private String TAG = "OpportunityInfo";
	private ImageView save;
	private EditText description, close_date, amount, name;
	private Spinner stage;
	private String strContactInternalNum;
	private String strOppInternalNum;
	private String strAction = "NEW";
	private String strContactId = null;
	private Dialog loadingDialog;
	private ArrayList<MasterInfo> aStage;
	private OpportunityDetail opportunity;
	private static final int MY_DATE_DIALOG_ID = 1210;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("ACTION")) {
				strAction = bundle.getString("ACTION");
			}
			
			if (bundle.containsKey("CONTACT_INTERNAL_ID")) {
				strContactInternalNum = bundle.getString("CONTACT_INTERNAL_ID");
			}
			
			if (bundle.containsKey("CONTACT_ID")) {
				strContactId = bundle.getString("CONTACT_ID");
			}
			
			if (strAction.equalsIgnoreCase("EDIT")) {
				strOppInternalNum = bundle.getString("ID");
			}
		}
		
		DeviceUtility.log(TAG, "strOppInternalNum: " + strOppInternalNum); 
		DeviceUtility.log(TAG, "strContactInternalNum: " + strContactInternalNum);
		
		setContentView(R.layout.opportunity_info);
		description = (EditText) findViewById(R.id.description);
		close_date = (EditText) findViewById(R.id.close_date);
		amount = (EditText) findViewById(R.id.amount);
		name = (EditText) findViewById(R.id.name);
		stage = (Spinner) findViewById(R.id.stage);
		
		save = (ImageView) findViewById(R.id.save);
		
		close_date.setFocusable(false);
		close_date.setOnClickListener(showDatePicker);
		close_date.setOnTouchListener(showDatePicker2);
		
		save.setOnClickListener(doSaveOpportunity);
		
		LoadOppStage task = new LoadOppStage();
		task.execute(new String[] {strOppInternalNum});
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case MY_DATE_DIALOG_ID:
				DatePickerDialog dateDlg = new DatePickerDialog(this,
				         new DatePickerDialog.OnDateSetListener() {
				         public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				                    Time chosenDate = new Time();
				                    chosenDate.set(dayOfMonth, monthOfYear, year);
				                    long dtDob = chosenDate.toMillis(true);
				                    //CharSequence strDate = DateFormat.format("dd-MM-yyyy", dtDob);
				                    CharSequence strDate = DateFormat.format(Constants.DATE_FORMAT, dtDob);
				                    close_date.setText(strDate);
				        }}, 2011,0, 1);
				 
				      dateDlg.setMessage(getString(R.string.close_date));
				      return dateDlg;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
			case MY_DATE_DIALOG_ID:
				DatePickerDialog dateDlg = (DatePickerDialog) dialog;
			    int iDay,iMonth,iYear;
			    Calendar cal = Calendar.getInstance();
			    iDay = cal.get(Calendar.DAY_OF_MONTH);
			    iMonth = cal.get(Calendar.MONTH);
			    iYear = cal.get(Calendar.YEAR);
			    if (close_date.getText().toString().length() == 10) {
			    	iDay = Integer.parseInt(close_date.getText().toString().substring(0, 2));
			    	iMonth = Integer.parseInt(close_date.getText().toString().substring(3, 5)) - 1;
			    	iYear = Integer.parseInt(close_date.getText().toString().substring(6, 10));
			    	DeviceUtility.log(TAG, "iDay: "+iDay+" iMonth: "+iMonth+" iYear: " + iYear);
			    }
			    dateDlg.updateDate(iYear, iMonth, iDay);
		     break;
		}
	}
	
	private OnClickListener showDatePicker = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDialog(MY_DATE_DIALOG_ID);
		}
	};
	
	private OnTouchListener showDatePicker2 = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			showDialog(MY_DATE_DIALOG_ID);
			return true;
		}
	};
	
	
	private OnClickListener doSaveOpportunity = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			doLockField(true);
			String strStage = "";
			if (stage.getSelectedItemPosition() >= 0) {
				strStage = aStage.get(stage.getSelectedItemPosition()).getValue();
			}
			
			String strName = name.getText().toString();
			String strCloseDate = close_date.getText().toString();
			String strAmount = amount.getText().toString();
			String strDesc = description.getText().toString();
			
			if (strStage.length() == 0) {
				if (aStage.size() == 0) {
					GuiUtility.alert(OpportunityInfo.this, "", getString(R.string.stage_option_missing), getString(R.string.ok));
					doLockField(false);
					stage.requestFocus();
				} else {
					GuiUtility.alert(OpportunityInfo.this, "", getString(R.string.stage_is_required), getString(R.string.ok));
					doLockField(false);
					stage.requestFocus();
				}
			} else if (strName.length() == 0) {
				GuiUtility.alert(OpportunityInfo.this, "", getString(R.string.name_is_required), getString(R.string.ok));
				doLockField(false);
				name.requestFocus();
			} else if (strCloseDate.length() == 0) {
				GuiUtility.alert(OpportunityInfo.this, "", getString(R.string.close_date_is_required), getString(R.string.ok));
				doLockField(false);
				close_date.requestFocus();
			} else if (strAmount.length() == 0) {
				GuiUtility.alert(OpportunityInfo.this, "", getString(R.string.amount_is_required), getString(R.string.ok));
				doLockField(false);
				amount.requestFocus();
			} else {
				/*if (strAction.equalsIgnoreCase("EDIT")) {
					opportunity.setInternalNum(strOppInternalNum);
				}*/
				
				if (strContactId != null) {
					opportunity.setContactId(strContactId);
				}
				
				opportunity.setContactInternalNum(strContactInternalNum);
				opportunity.setOppName(strName);
				opportunity.setOppStage(strStage);
				opportunity.setOppDate(strCloseDate);
				opportunity.setOppAmount(strAmount);
				opportunity.setOppDesc(strDesc);
				String strOwner = Utility.getConfigByText(OpportunityInfo.this, "USER_EMAIL");
				if (strOwner.length() > 0) {
					opportunity.setUserStamp(strOwner);
				}
				SaveOpportunity task = new SaveOpportunity();
				task.execute(opportunity);
			}
		}
	};
	
	
	private class SaveOpportunity extends AsyncTask<OpportunityDetail, Void, Boolean> {

		@Override
		protected Boolean doInBackground(OpportunityDetail... params) {
			OpportunityDetail detail = params[0];
			DatabaseUtility.getDatabaseHandler(OpportunityInfo.this);
			Opportunity opportunity = new Opportunity(Constants.DBHANDLER);
			detail.setIsUpdate("false");
			if (strAction.equals("NEW")) {
				long lRow = opportunity.insert(detail);
				if (lRow == -1) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			} else {
				long iRow = opportunity.update(detail);
				if (iRow == 0) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(OpportunityInfo.this, false, getString(R.string.processing));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			
			if (result.booleanValue() == true) {
//				GuiUtility.alert(OpportunityInfo.this, getString(R.string.success_store_opportunity_title), getString(R.string.success_store_opportunity_desc), Gravity.CENTER, getString(R.string.ok), 
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
								OpportunityInfo.this.finish();
//							}
//				}, "", null);
			} else {
				GuiUtility.alert(OpportunityInfo.this, getString(R.string.fail_store_opportunity_title), getString(R.string.fail_store_opportunity_desc), getString(R.string.ok));
			}
		}
	}
	
	public void doLockField(boolean bLock) {
		boolean bEnabled = true;
		boolean bFocusabled = true;
		if (bLock == true) {
			bEnabled = false;
			bFocusabled = false;
		}
		
		stage.setEnabled(bEnabled);
		stage.setFocusable(bFocusabled);
		
		description.setEnabled(bEnabled);
		description.setFocusable(bFocusabled);
		
		close_date.setEnabled(bEnabled);
		close_date.setFocusable(bFocusabled);
		
		amount.setEnabled(bEnabled);
		amount.setFocusable(bFocusabled);
		
		name.setEnabled(bEnabled);
		name.setFocusable(bFocusabled);
		
		if (bFocusabled) {
			stage.setFocusableInTouchMode(bFocusabled);
			description.setFocusableInTouchMode(bFocusabled);
			close_date.setFocusableInTouchMode(bFocusabled);
			amount.setFocusableInTouchMode(bFocusabled);
			name.setFocusableInTouchMode(bFocusabled);
		}
	}
	
	private class LoadOppStage extends AsyncTask<String, Void, OpportunityDetail> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(OpportunityInfo.this, false, null);
		}
		
		@Override
		protected OpportunityDetail doInBackground(String... params) {
			aStage = (ArrayList<MasterInfo>) Utility.getMasterByType(OpportunityInfo.this, Constants.MASTER_OPPORTUNITY_STAGE);
		
			OpportunityDetail opp = new OpportunityDetail();
			if (strAction.equals("EDIT")) {
				//load the obj.
				DatabaseUtility.getDatabaseHandler(OpportunityInfo.this);
				Opportunity source = new Opportunity(Constants.DBHANDLER);
				opp = source.getOpportunityById(params[0]);
				DeviceUtility.log(TAG, "Edit: " + opp.toString());
			}
			return opp;
		}

		@Override
		protected void onPostExecute(OpportunityDetail result) {
			super.onPostExecute(result);
			opportunity = result;
			DeviceUtility.log(TAG, result.toString());
			int iPosition = 0;
					
			ArrayAdapter<String> stageAdapter = new ArrayAdapter<String>(OpportunityInfo.this, android.R.layout.simple_spinner_item);
			stageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			DeviceUtility.log(TAG, "Stage: " + aStage.size());
			
			for (int i = 0; i < aStage.size(); i++) {
				stageAdapter.add(aStage.get(i).getText());
				System.out.println("atage xxxxxxxxxxx" + aStage.get(i).getUser3());
				if (result.getOppStage() != null) {
					if (result.getOppStage().equals(aStage.get(i).getValue())) {
						iPosition = i;
					}
				}
			}
			stage.setAdapter(stageAdapter);
			if (strAction.equals("EDIT")) {
				description.setText(result.getOppDesc());
				close_date.setText(result.getOppDate());
				amount.setText(result.getOppAmount());
				name.setText(result.getOppName());
				stage.setSelection(iPosition);
			}
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
}
