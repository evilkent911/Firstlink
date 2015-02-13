package com.trinerva.icrm.contacts;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Opportunity;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.StorageUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class OpportunityList extends Activity {
	private String TAG = "OpportunityList";
	private String strID = null;
	private String strContactId = null;
	private ImageView photo;
	private TextView first_name, last_name, company, empty_opportunity,mUserContact;
	ImageView add_new;
	private ListView list;
	private ContactDetail contact;
	private Dialog loadingDialog;
	private SimpleCursorAdapter adapter;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opportunity_list);
		
		context = this;
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("ID")) {
				strID = bundle.getString("ID");
			}
			
			if (bundle.containsKey("CONTACT_ID")) {
				strContactId = bundle.getString("CONTACT_ID");
			}
		}
		
		photo = (ImageView) findViewById(R.id.photo);
		
		first_name = (TextView) findViewById(R.id.first_name);
		last_name = (TextView) findViewById(R.id.last_name);
		company = (TextView) findViewById(R.id.company);
		add_new = (ImageView) findViewById(R.id.save);
		mUserContact = (TextView) findViewById(R.id.userContact);
		empty_opportunity = (TextView) findViewById(R.id.empty_opportunity);

		add_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(OpportunityList.this, OpportunityInfo.class);
				intent.putExtra("ACTION", "NEW");
				//intent.putExtra("CONTACT_ID", strID);
				intent.putExtra("CONTACT_INTERNAL_ID", strID);
				intent.putExtra("CONTACT_ID", strContactId);
				OpportunityList.this.startActivity(intent);
			
			}
		});

		list = (ListView) findViewById(R.id.opportunity_list);
		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);
		
		if(!Utility.getConfigByText(context, Constants.DELETE_OPPORTUNITY).equals("0")){
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					
					
			GuiUtility.alert(OpportunityList.this, "", getString(R.string.delete_opportunity), Gravity.CENTER, getString(R.string.cancel), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							
						
							
						}
					}, getString(R.string.delete), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							loadingDialog = GuiUtility.getLoadingDialog(OpportunityList.this, false, getString(R.string.processing));
							DatabaseUtility.getDatabaseHandler(OpportunityList.this);
							Opportunity source = new Opportunity(Constants.DBHANDLER);
							
							Cursor cursor = adapter.getCursor();
							cursor.moveToPosition(position);
							
							source.delete(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
							loadingDialog.dismiss();
							
							LoadOpportunity load = new LoadOpportunity();
							load.execute(new String[] {strID});
						}
					});
					return true;
				}
			});
		}
	
		
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
		LoadOpportunity load = new LoadOpportunity();
		load.execute(new String[] {strID});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adapter != null) {
			adapter.getCursor().close();
			adapter = null;
		}
	}

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			//pass to contact info
			Intent info = new Intent(OpportunityList.this, OpportunityDisplay.class);
			info.putExtra("ACTIVE", cursor.getString(cursor.getColumnIndex("ACTIVE")));
			info.putExtra("OPP_ID", cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			
			System.out.println("ACTIVE STATUS " + cursor.getString(cursor.getColumnIndex("ACTIVE")));
			OpportunityList.this.startActivity(info);
		}
		
	};

//	private OnClickListener doShowOppInfo = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent(OpportunityList.this, OpportunityInfo.class);
//			intent.putExtra("ACTION", "NEW");
//			//intent.putExtra("CONTACT_ID", strID);
//			intent.putExtra("CONTACT_INTERNAL_ID", strID);
//			intent.putExtra("CONTACT_ID", strContactId);
//			OpportunityList.this.startActivity(intent);
//		}
//	};
	

	private class LoadOpportunity extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(OpportunityList.this);
			Contact source = new Contact(Constants.DBHANDLER);
			contact = source.getContactDetailById(params[0]);

			Opportunity opp = new Opportunity(Constants.DBHANDLER);
			return opp.getOpportunityDisplay(params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(OpportunityList.this,
					false, null);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			first_name.setText(contact.getFirstName());
			last_name.setText(contact.getLastName());
			company.setText(contact.getCompanyName());

			try {

				String userName;
				if (contact.getFirstName() == null && contact.getLastName() != null) {
					userName = contact.getLastName();
				} else if(contact.getFirstName() != null && contact.getLastName() == null){
					userName = contact.getFirstName();
				} else if(contact.getFirstName() != null && contact.getLastName() != null){
					userName = contact.getFirstName() + " "
							+ contact.getLastName();
				}else{
					userName = "";
				}

				if (userName.equals("") && contact.getCompanyName() != null) {
					mUserContact.setText(contact.getCompanyName());
				} else {
					String data = userName+"\n"+contact.getCompanyName();
					
					System.out.println("Alld daat = "+data);
					mUserContact.setText(data);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (contact.getPhoto() != null) {
				StorageUtility storage = new StorageUtility(OpportunityList.this, Constants.CACHE_FOLDER);
				Bitmap bitmap = storage.doReadImage(contact.getPhoto());
				if (bitmap != null) {
					photo.setImageBitmap(bitmap);
				} else {
					photo.setImageResource(R.drawable.contacts);
				}
			}
			
			adapter = new SimpleCursorAdapter(OpportunityList.this, R.layout.opportunity_item, result, new String[] {"_id", "OPP_NAME", "OPP_AMOUNT", "OPP_DATE", "OPP_STAGE", "IS_UPDATE"}, new int[]{R.id.opp_name, R.id.opp_amount, R.id.opp_sync});
			adapter.setViewBinder(binder);
			if (result.getCount() == 0) {
				list.setVisibility(View.GONE);
				empty_opportunity.setText(getString(R.string.empty_opportunity_list));
			} else {
				list.setVisibility(View.VISIBLE);
				list.setAdapter(adapter);
				empty_opportunity.setText(getString(R.string.my_opportunity_list));
			}
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

	}

	private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			int viewId = view.getId();
	        switch(viewId) {
	        	case R.id.opp_name:
	        		TextView oppName = (TextView) view;
	        		oppName.setText(cursor.getString(cursor.getColumnIndex("OPP_DATE")) + " " + cursor.getString(cursor.getColumnIndex("OPP_NAME")));
	        		break;
	        		
	        	case R.id.opp_amount:
	        		TextView oppAmount = (TextView) view;
	        		oppAmount.setText(getString(R.string.rm) + " " + cursor.getString(cursor.getColumnIndex("OPP_AMOUNT")));
	        		break;
	        		
	        	case R.id.opp_sync:
	        		ImageView oppSync = (ImageView) view;
//	        		DeviceUtility.log(TAG, "IS_UPDATE: " + cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
//	        		DeviceUtility.log(TAG, "ACTIVE: " + cursor.getString(cursor.getColumnIndex("OPP_STAGE")));
//	        		if (cursor.getString(cursor.getColumnIndex("IS_UPDATE")).equalsIgnoreCase("true")) {
	        			oppSync.setVisibility(View.GONE);
//	        		} else {
//	        			oppSync.setVisibility(View.VISIBLE);
//System.out.println("AAAA"+cursor.getString(cursor.getColumnIndex("ACTIVE")));
//	        			if (cursor.getString(cursor.getColumnIndex("ACTIVE")).equals("1")) {
//							oppSync.setImageResource(R.drawable.btn_sync_deleted);
//						} else {
//							DeviceUtility.log(TAG, "show sync button");
//							oppSync.setImageResource(R.drawable.btn_sync);
//						}
//	        		}
	        		break;
	        }
			return true;
		}
	};	
}
