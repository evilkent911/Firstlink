package com.trinerva.icrm.contacts;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.object.ProfileDisplay;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.StorageUtility;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ActivityHistory extends Activity {
	private String TAG = "ActivityHistory";
	private String strID = null;
	private String strType = null;
	private ImageView photo;
	private TextView first_name, last_name, company, call, sms, email;
	private String strMode = Constants.ACTION_CALL;
	private ListView list;
	private Dialog loadingDialog;
	private ContactDetail contact;
	private LeadDetail lead;
	private SimpleCursorAdapter adapter;
	private ProfileDisplay displayInfo = new ProfileDisplay();
	
	TextView mUserContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		Bundle bundle = getIntent().getExtras();
		strID = bundle.getString("ID");
		strType = bundle.getString("TYPE");
		
		System.out.println("strID = "+strID);
		System.out.println("strType = "+strType);

		photo = (ImageView) findViewById(R.id.photo);
		
		first_name = (TextView) findViewById(R.id.first_name);
		last_name = (TextView) findViewById(R.id.last_name);
		company = (TextView) findViewById(R.id.company);
		mUserContact = (TextView) findViewById(R.id.userContact);
		
		call = (TextView) findViewById(R.id.call);
		sms = (TextView) findViewById(R.id.sms);
		email = (TextView) findViewById(R.id.email);
		
		call.setOnClickListener(showLog);
		sms.setOnClickListener(showLog);
		email.setOnClickListener(showLog);
		
		list = (ListView) findViewById(R.id.activity_list);
		list.setFocusableInTouchMode(true);
		list.requestFocus(0);
		list.setSelection(0);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private OnClickListener showLog = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.call:
					strMode = Constants.ACTION_CALL;
					break;
				case R.id.sms:
					strMode = Constants.ACTION_SMS;
					break;
				case R.id.email:
					strMode = Constants.ACTION_EMAIL;
					break;
			}
			doShowSelectedMenu(strMode);
			if (adapter != null) {
				adapter.getFilter().filter(strMode);
			}
		}
	};
	
	public void doShowSelectedMenu(String strMode) {
		call.setBackgroundResource(R.drawable.blue_menu_middle);
		sms.setBackgroundResource(R.drawable.blue_menu_middle);
		email.setBackgroundResource(R.drawable.blue_menu_middle);
		
		if (strMode.equalsIgnoreCase(Constants.ACTION_EMAIL)) {
			email.setBackgroundResource(R.drawable.blue_menu_selected_middle);
		}
		
		if (strMode.equalsIgnoreCase(Constants.ACTION_CALL)) {
			call.setBackgroundResource(R.drawable.blue_menu_selected_middle);
		}
		
		if (strMode.equalsIgnoreCase(Constants.ACTION_SMS)) {
			sms.setBackgroundResource(R.drawable.blue_menu_selected_middle);
		}
	}
	
	
	private class LoadContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(ActivityHistory.this);
			String strAccounType = params[1];
			if (strAccounType.equalsIgnoreCase(Constants.PERSON_TYPE_LEAD)) {
				Lead source = new Lead(Constants.DBHANDLER);
				lead = source.getLeadDetailById(params[0]);
				displayInfo = new ProfileDisplay(lead.getInternalNum(), lead.getFirstName(), lead.getLastName(), lead.getCompanyName(), lead.getPhoto());
				
			} else if (strAccounType.equalsIgnoreCase(Constants.PERSON_TYPE_CONTACT)) {
				Contact source = new Contact(Constants.DBHANDLER);
				contact = source.getContactDetailById(params[0]);
				displayInfo = new ProfileDisplay(contact.getInternalNumber(), contact.getFirstName(), contact.getLastName(), contact.getCompanyName(), contact.getPhoto());
			}
			
			
			ActivityLog log = new ActivityLog(Constants.DBHANDLER);
			return log.getActivityLogDisplay(params[0], params[1], params[2]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ActivityHistory.this, false, null);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			first_name.setText(displayInfo.getFirstName());
			last_name.setText(displayInfo.getLastName());
			company.setText(displayInfo.getCompanyName());
			
		try {
			String userName;
			if (displayInfo.getFirstName() == null && displayInfo.getLastName() != null) {
				userName = displayInfo.getLastName();
			} else if(displayInfo.getFirstName() != null && displayInfo.getLastName() == null){
				userName = displayInfo.getFirstName();
			} else if(displayInfo.getFirstName() != null && displayInfo.getLastName() != null){
				userName = displayInfo.getFirstName() + " "
						+ displayInfo.getLastName();
			}else{
				userName = "";
			}

			if (userName.equals("") && displayInfo.getCompanyName() != null) {
				mUserContact.setText(displayInfo.getCompanyName());
			} else {
				mUserContact.setText(userName + "\n"
						+ displayInfo.getCompanyName());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			doShowSelectedMenu(strMode);
			if (displayInfo.getPhoto() != null) {
				StorageUtility storage = new StorageUtility(ActivityHistory.this, Constants.CACHE_FOLDER);
				Bitmap bitmap = storage.doReadImage(displayInfo.getPhoto());
				if (bitmap != null) {
					photo.setImageBitmap(bitmap);
				} else {
					photo.setImageResource(R.drawable.contacts);
				}
			}
			
			adapter = new SimpleCursorAdapter(ActivityHistory.this, R.layout.activity_item, result, new String[] {"_id", "EMAIL", "CREATED_TIMESTAMP"}, new int[]{R.id.action_string, R.id.time});
			adapter.setViewBinder(binder);
			adapter.setFilterQueryProvider(filter);
			list.setAdapter(adapter);
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
		
		private FilterQueryProvider filter = new FilterQueryProvider() {
			
			@Override
			public Cursor runQuery(CharSequence constraint) {
				DeviceUtility.log(TAG, "runQuery: " + constraint);
				DatabaseUtility.getDatabaseHandler(ActivityHistory.this);
				
				ActivityLog log = new ActivityLog(Constants.DBHANDLER);
				Cursor cursor = log.getActivityLogDisplay(strID, strType, strMode);
				return cursor;
			}
		};
		
		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				int viewId = view.getId();
		        switch(viewId) {
		        	case R.id.action_string:
		        		TextView action = (TextView) view;
		        		String strInfo = "";
		        		//check mode.
		        		if (strMode.equalsIgnoreCase(Constants.ACTION_CALL)) {
		        			strInfo = getString(R.string.call_to_number);
		        			strInfo = strInfo.replace("[NUMBER]", cursor.getString(cursor.getColumnIndex("MOBILE")));
		        		} else if (strMode.equalsIgnoreCase(Constants.ACTION_EMAIL)) {
		        			strInfo = getString(R.string.email_to_address);
		        			strInfo = strInfo.replace("[ADDRESS]", cursor.getString(cursor.getColumnIndex("EMAIL")));
		        		} else if (strMode.equalsIgnoreCase(Constants.ACTION_SMS)) {
		        			strInfo = getString(R.string.sms_to_number);
		        			strInfo = strInfo.replace("[NUMBER]", cursor.getString(cursor.getColumnIndex("MOBILE")));
		        		}
		        		action.setText(strInfo);
		        		break;
		        	case R.id.time:
		        		TextView time = (TextView) view;
		        		String strCreatedTime = cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP"));
		        		time.setText(strCreatedTime);
		        		break;
		        }
				return true;
			}
		};
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LoadContact load = new LoadContact();
		load.execute(new String[] {strID, strType, strMode});
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

}
