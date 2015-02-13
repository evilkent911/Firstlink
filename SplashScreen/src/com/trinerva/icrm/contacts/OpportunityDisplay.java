package com.trinerva.icrm.contacts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OpportunityDisplay extends Activity {
	private String TAG = "OpportunityDisplay";
	private String OPP_ID = "";
	String ACTIVE = null;
	private String CONTACT_INTERNAL_ID = "";
	private String CONTACT_ID = null;
	private TextView name, amount, close_date, stage, description;
	ImageView edit;
	private ImageView delete;
	private Dialog loadingDialog;
	private ArrayList<MasterInfo> aStage;
	TextView mModified;
	LinearLayout mNavigationLayout;
	RelativeLayout  relativeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opportunity_display);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			OPP_ID = bundle.getString("OPP_ID");
			ACTIVE = bundle.getString("ACTIVE");
		}
		
		name = (TextView) findViewById(R.id.name);
		amount = (TextView)  findViewById(R.id.amount);
		close_date = (TextView)  findViewById(R.id.close_date);
		stage = (TextView)  findViewById(R.id.stage);
		description = (TextView)  findViewById(R.id.description);
		edit = (ImageView)  findViewById(R.id.edit);
		delete = (ImageView) findViewById(R.id.delete);
		edit.setOnClickListener(editOpportunity);
		delete.setOnClickListener(deleteOpportunity);
		mModified = (TextView)  findViewById(R.id.modified);
		mNavigationLayout = (LinearLayout)findViewById(R.id.navigationLayout);
		relativeLayout = (RelativeLayout)findViewById(R.id.navigationBar);
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		if(ACTIVE != null){
			if(ACTIVE.equals("1")){
				mNavigationLayout.setVisibility(View.GONE);
				edit.setVisibility(View.GONE);
			}
		}
		
		
		
		if(Utility.getConfigByText(OpportunityDisplay.this, Constants.DELETE_OPPORTUNITY).equals("0")){

			relativeLayout.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LoadOpportunity load = new LoadOpportunity();
		load.execute(new String[] {OPP_ID});
	}

	private OnClickListener editOpportunity = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent editOpp = new Intent(OpportunityDisplay.this, OpportunityInfo.class);
			editOpp.putExtra("ACTION", "EDIT");
			editOpp.putExtra("CONTACT_INTERNAL_ID", CONTACT_INTERNAL_ID);
			editOpp.putExtra("ID", OPP_ID);
			editOpp.putExtra("CONTACT_ID", CONTACT_ID);
			OpportunityDisplay.this.startActivity(editOpp);
		}
	};
	
	private OnClickListener deleteOpportunity = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			GuiUtility.alert(OpportunityDisplay.this, "", getString(R.string.delete_opportunity), Gravity.CENTER, getString(R.string.cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			}, getString(R.string.delete),  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					loadingDialog = GuiUtility.getLoadingDialog(OpportunityDisplay.this, false, getString(R.string.processing));
					DatabaseUtility.getDatabaseHandler(OpportunityDisplay.this);
					Opportunity source = new Opportunity(Constants.DBHANDLER);
					source.delete(OPP_ID);
					loadingDialog.dismiss();
					OpportunityDisplay.this.finish();
				
				}
			});
		}
	};
	
	
	private class LoadOpportunity extends AsyncTask<String, Void, OpportunityDetail> {
		
		@Override
		protected OpportunityDetail doInBackground(String... params) {
			aStage = (ArrayList<MasterInfo>) Utility.getMasterByType(OpportunityDisplay.this, Constants.MASTER_OPPORTUNITY_STAGE);
			DeviceUtility.log(TAG, "aStage: " + aStage.size());
			DatabaseUtility.getDatabaseHandler(OpportunityDisplay.this);
			Opportunity source = new Opportunity(Constants.DBHANDLER);
			return source.getOpportunityById(params[0]);
		}
		
		@Override
		protected void onPostExecute(OpportunityDetail result) {
			super.onPostExecute(result);
			DeviceUtility.log(TAG, result.toString());
			name.setText(result.getOppName());
			amount.setText(result.getOppAmount());
			
			close_date.setText(result.getOppDate());
			stage.setText(getDisplayStage(result.getOppStage()));
			description.setText(result.getOppDesc());
			
			
			
			//06 Nov 2013 01:59:40
			//2014-08-09T12:34:42.247
			
			//"2014-08-27 12:01:30"
			
			SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat sf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		    Date date;
		    try {
				date = sf4.parse(result.getModifiedTimestamp());
				mModified.setText(getString(R.string.last_modified_date,sf.format(date)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				date = sf3.parse(result.getModifiedTimestamp());
				mModified.setText(getString(R.string.last_modified_date,sf.format(date)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			CONTACT_INTERNAL_ID = result.getContactInternalNum();
			CONTACT_ID = result.getContactId();
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(OpportunityDisplay.this, false, null);
		}
		
		private String getDisplayStage(String strValue) {
			int iCount = aStage.size();
			DeviceUtility.log(TAG, "strValue: " + strValue);
			for (int i = 0;  i < iCount; i++) {
				MasterInfo master = aStage.get(i);
				DeviceUtility.log(TAG, "master.getValue(): " + master.getValue());
				if (master.getValue().equalsIgnoreCase(strValue)) {
					return master.getText();
				}
			}
			return "";
		}		
	}
}
