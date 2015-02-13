package com.trinerva.icrm.settings;

import java.util.ArrayList;
import java.util.HashMap;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.ContactUtility;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ImportContact extends Activity {
	private String TAG = "ImportContact";
	private Dialog loadingDialog;
	private ArrayList<HashMap<String, String>> aData;
	private ContactAdapter adapter;
	private ListView list;
	private TextView import_text, select_all, contact, lead, empty_contact_list;
	private int iContactType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_contact);
		iContactType = Constants.IMPORT_CONTACT;
		import_text = (TextView) findViewById(R.id.import_text);
		select_all = (TextView) findViewById(R.id.select_all);
		contact = (TextView) findViewById(R.id.contact);
		empty_contact_list = (TextView) findViewById(R.id.empty_contact_list);
		lead = (TextView) findViewById(R.id.lead);
		list = (ListView) findViewById(R.id.address_book_list);
		setSelectedButton(iContactType);
		
		list.setFocusableInTouchMode(true);
		list.requestFocus(0);
		list.setSelection(0);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		
		import_text.setOnClickListener(doImport);
		select_all.setOnClickListener(doSelectAll);
		lead.setOnClickListener(selectImportType);
		contact.setOnClickListener(selectImportType);
		
		
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
		LoadContact task = new LoadContact();
		task.execute();
	}
	
	private OnClickListener selectImportType = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.contact:
					iContactType = Constants.IMPORT_CONTACT;
					break;
				case R.id.lead:
					iContactType = Constants.IMPORT_LEAD;
					break;
			}
			setSelectedButton(iContactType);
		}
	};
	
	private void setSelectedButton(int iSelected) {
		contact.setBackgroundResource(R.drawable.blue_menu_left);
		lead.setBackgroundResource(R.drawable.blue_menu_right);
		
		//set clicked button.
		if (iSelected == Constants.IMPORT_CONTACT) {
			contact.setBackgroundResource(R.drawable.blue_menu_selected_left);
		}
		
		if (iSelected == Constants.IMPORT_LEAD) {
			lead.setBackgroundResource(R.drawable.blue_menu_selected_right);
		}
	}
	
	private OnClickListener doSelectAll = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (aData.size() > 0) {
				for (int i = 0; i < aData.size(); i++) {
					HashMap<String, String> temp = aData.get(i);
					temp.put("CHECKED", "true");
					aData.set(i, temp);
				}
			}
			
			adapter = new ContactAdapter(ImportContact.this, aData);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	};
	
	private OnClickListener doImport = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (adapter.getCount() > 0) {
				SaveContact save = new SaveContact();
				save.execute(adapter);
			}
		}
	};
	
	private class SaveContact extends AsyncTask<ContactAdapter, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ImportContact.this, false, null);
		}
		
		@Override
		protected Boolean doInBackground(ContactAdapter... params) {
			ContactAdapter selected = params[0];
			boolean bSuccess = false;
			int iCount = selected.getCount();
			ArrayList<ContactDetail> aContact = new ArrayList<ContactDetail>();
			ArrayList<LeadDetail> aLead = new ArrayList<LeadDetail>();
			try {
				for (int i = 0; i<iCount; i++) {
					HashMap<String, String> hData = selected.getItem(i);
					if (hData.containsKey("CHECKED")) {
						if (hData.get("CHECKED").equalsIgnoreCase("true")) {
							//build contact uri.
							Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, hData.get("ID").toString());
							if (iContactType == Constants.IMPORT_CONTACT) {
								ContactDetail contact = ContactUtility.getContact(ImportContact.this, uri.toString());
								aContact.add(contact);
							} else if (iContactType == Constants.IMPORT_LEAD) {
								LeadDetail lead = ContactUtility.getLeadFromPhoneBook(ImportContact.this, uri.toString());
								aLead.add(lead);
							}
						}
					}
				}
				
				DatabaseUtility.getDatabaseHandler(ImportContact.this);
				String strOwner = Utility.getConfigByText(ImportContact.this, "USER_EMAIL");
				if (aContact.size() > 0) {
					Contact oContact = new Contact(Constants.DBHANDLER);
					for (int j=0; j < aContact.size(); j++) {
						ContactDetail contact = aContact.get(j);
						contact.setIsUpdate("false");
						contact.setUpdateGpsLocation("true");
						if (strOwner.length() > 0) {
							contact.setOwner(strOwner);
						}
						oContact.insert(contact);
					}
				}
				
				if (aLead.size() > 0) {
					Lead oLead = new Lead(Constants.DBHANDLER);
					for (int k=0; k < aLead.size(); k++) {
						LeadDetail lead = aLead.get(k);
						lead.setIsUpdate("false");
						lead.setUpdateGpsLocation("true");
						if (strOwner.length() > 0) {
							lead.setOwner(strOwner);
							lead.setUserStamp(strOwner);
						}
						oLead.insert(lead);
					}
				}
				bSuccess = true;
			} catch (Exception e) {
			}
			
			return new Boolean(bSuccess);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			
			if (result.booleanValue() == true) {
				String strDesc = getString(R.string.success_store_batch_lead_desc);
				if (iContactType == Constants.IMPORT_CONTACT) {
					strDesc = getString(R.string.success_store_batch_contact_desc);
				}
				
				GuiUtility.alert(ImportContact.this, getString(R.string.success_store_batch_title), strDesc, Gravity.CENTER, getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								ImportContact.this.finish();
							}
				}, "", null);
			} else {
				String strFailDesc = getString(R.string.fail_store_batch_lead_desc);
				if (iContactType == Constants.IMPORT_CONTACT) {
					strFailDesc = getString(R.string.fail_store_batch_contact_desc);
				}
				GuiUtility.alert(ImportContact.this, getString(R.string.fail_store_batch_contact_title), strFailDesc, getString(R.string.ok));
			}
		}	
	}
	
	//load data
	private class LoadContact extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ImportContact.this, false, null);
		}
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			return ContactUtility.getAllQualifyContact(ImportContact.this);
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			aData = result;
			if (result.size() > 0) {
				adapter = new ContactAdapter(ImportContact.this, result);
				list.setAdapter(adapter);
				list.setVisibility(View.VISIBLE);
				empty_contact_list.setVisibility(View.GONE);
			} else {
				empty_contact_list.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
		}
	}
}
