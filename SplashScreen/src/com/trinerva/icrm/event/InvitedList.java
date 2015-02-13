package com.trinerva.icrm.event;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.event.EventInvitee.StoreData;
import com.trinerva.icrm.utility.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

//Display only. so no need onResume.
public class InvitedList extends Activity {
	private String TAG = "InvitedList";
	private TextView empty_invited_list;
	private ListView invited_list;
	private String strInvitedList = "";
	private Dialog loadingDialog;
	private InvitedAdapter invitedAdapter;

	List<StoreData> leadData = new ArrayList<StoreData>();
	List<StoreData> contactData = new ArrayList<StoreData>();
	List<StoreData> userData = new ArrayList<StoreData>();
	InviteeData allData;
	List<InviteeData> listAllData = new ArrayList<InviteeData>();
	
	List<String> leadListData = new ArrayList<String>();
	List<String> leadContactData = new ArrayList<String>();
	List<String> leadUserData = new ArrayList<String>();
	
	List<String> userCompany = new ArrayList<String>();
	List<String> userContact = new ArrayList<String>();
	List<String> userLead = new ArrayList<String>();

	InviteeAllAdapter allAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invited_list);
		empty_invited_list = (TextView) findViewById(R.id.empty_invited_list);
		invited_list = (ListView) findViewById(R.id.invited_list);

		Bundle invited = getIntent().getExtras();
		strInvitedList = invited.getString("LIST");
		// invitedAdapter = new InvitedAdapter(InvitedList.this,
		// R.layout.invited_view_item);
		// invited_list.setAdapter(invitedAdapter);
		String str[] = strInvitedList.split(";");
		
		for (int i = 0; i < str.length; i++) {
			System.out.println("wa " + str[i].subSequence(2, str[i].length()));
			
			if (str[i].subSequence(0, 1).equals("0")) {
				String user0[] = str[i].split(Pattern.quote("|*|"));
				if(user0.length > 2)
					userCompany.add(user0[2]);
			//	else
			//		leadUserData.add(str[i].subSequence(2, str[i].length()).toString());
//				allData = new InviteeData();
//				allData.subject = getString(R.string.leads);
//				allData.isTitle = true;
//				allData.strBy = "lead";
//				listAllData.add(allData);
//				leadUserData.add(str[i].subSequence(2, str[i].length()).toString());
			} else if (str[i].subSequence(0, 1).equals("2")) {
				String user1[] = str[i].split(Pattern.quote("|*|"));
				if(user1.length > 2)
					userContact.add(user1[2]);
				else if(user1.length > 3)
						userContact.add(user1[2]+";"+user1[3]);
				else
					
//				allData = new InviteeData();
//				allData.subject = getString(R.string.contacts);
//				allData.isTitle = true; 
//				allData.strBy = "contact";
//				listAllData.add(allData);
				leadContactData.add(str[i].subSequence(2, str[i].length()).toString());
			} else if (str[i].subSequence(0, 1).equals("3")) {
				String user2[] = str[i].split(Pattern.quote("|*|"));
				System.out.println("users 3 " + user2[2]);
				if(user2.length > 2)
					userLead.add(user2[2]);
				else if(user2.length > 3)
					userLead.add(user2[2]+";"+user2[3]);
				
				else
					leadListData.add(str[i].subSequence(2, str[i].length()).toString());
//				allData = new InviteeData();
//				allData.subject = getString(R.string.users);
//				allData.isTitle = true;
//				allData.strBy = "user";
//				listAllData.add(allData);
			}
		}
		
		if(userLead.size()!=0){
			allData = new InviteeData();
			allData.subject = getString(R.string.leads);
			allData.isTitle = true;
			allData.strBy = "lead";
			listAllData.add(allData);
			for(int i = 0 ; i < userLead.size();i++){
				String subject[] = userLead.get(i).split(";");
				allData = new InviteeData();
				allData.subject = subject[0];
				if(subject.length>1)
				allData.account = subject[1];
				allData.isTitle = false;
				allData.strBy = "lead";
				listAllData.add(allData);
			}
		}
		
		if(userContact.size()!=0){
			allData = new InviteeData();
			allData.subject = getString(R.string.contacts);
			allData.isTitle = true;
			allData.strBy = "contact";
			listAllData.add(allData);
			
			for(int i = 0 ; i < userContact.size();i++){
				String subject[] = userContact.get(i).split(";");
				allData = new InviteeData();
				allData.subject = subject[0];
				if(subject.length>1)
				allData.account = subject[1];
				allData.isTitle = false;
				allData.strBy = "contact";
				listAllData.add(allData);
			}
		}
		
		if(userCompany.size()!=0){
			allData = new InviteeData();
			allData.subject = getString(R.string.users);
			allData.isTitle = true;
			allData.strBy = "user";
			listAllData.add(allData);
			for(int i = 0 ; i < userCompany.size();i++){
				allData = new InviteeData();
				allData.subject = userCompany.get(i);
				allData.isTitle = false;
				allData.strBy = "user";
				listAllData.add(allData);
			}
		}
		
		
		
		
		if(leadListData.size() != 0){
			Lead lead = new Lead(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s

			Cursor cursor = lead
					.getSelectLeadInviteeListDisplayByFilter("",
							getSelectedLead());
		System.out.println("OO = "+getSelectedLead());
			if (cursor.getCount() != 0) {
				allData = new InviteeData();
				allData.subject = getString(R.string.leads);
				allData.isTitle = true;
				allData.strBy = "lead";
				listAllData.add(allData);
				cursor.moveToFirst();
				
				System.out.println("cursor = "+cursor);
				do {

					String name;
					String strFirstName = cursor.getString(cursor
							.getColumnIndex("FIRST_NAME"));
					String strLastName = cursor.getString(cursor
							.getColumnIndex("LAST_NAME"));
					if (strFirstName == null) {
						name = strLastName;
					} else {
						name = strFirstName + " " + strLastName;
					}

					allData = new InviteeData();
					allData.subject = name;
					allData.account = cursor.getString(cursor
							.getColumnIndex("COMPANY_NAME"));
					allData.isTitle = false;
					allData.strBy = "lead";
					listAllData.add(allData);

				} while (cursor.moveToNext());

			}
		}
		
		if(leadContactData.size() != 0){
			
			Contact contact = new Contact(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s
System.out.println("getSelectedContact()  = "+getSelectedContact());
			Cursor contactCursor = contact
					.getSelectedContactListDisplayByFilter("",
							getSelectedContact());
			contactCursor.moveToFirst();
			allData = new InviteeData();
			allData.subject = getString(R.string.contacts);
			allData.isTitle = true;
			allData.strBy = "contact";
			listAllData.add(allData);
			do {

				String name;
				String strFirstName = contactCursor
						.getString(contactCursor
								.getColumnIndex("FIRST_NAME"));
				String strLastName = contactCursor
						.getString(contactCursor
								.getColumnIndex("LAST_NAME"));
				if (strFirstName == null) {
					name = strLastName;
				} else {
					name = strFirstName + " " + strLastName;
				}

				allData = new InviteeData();
				allData.subject = name;
				allData.account = contactCursor.getString(contactCursor
						.getColumnIndex("COMPANY_NAME"));
				allData.strBy = "contact";
				allData.isTitle = false;
				listAllData.add(allData);

			} while (contactCursor.moveToNext());
		}
		
		Master master = new Master(Constants.DBHANDLER);

		Cursor userCursor = master.getSelectedAllMasterCompanyFilter(
				"UserList", "", getSelectedUser());
		userCursor.moveToFirst();
		if(leadUserData.size() != 0){
			allData = new InviteeData();
			allData.subject = getString(R.string.users);
			allData.isTitle = true;
			allData.strBy = "user";
			listAllData.add(allData);
			do {

				allData = new InviteeData();
				allData.subject = userCursor.getString(userCursor
						.getColumnIndex("MASTER_TEXT"));
				allData.strBy = "user";
				allData.isTitle = false;
				listAllData.add(allData);

			} while (userCursor.moveToNext());
		}
		
		System.out.println("listAllData = "+listAllData.size());
//		getInviteeList();
		allAdapter = new InviteeAllAdapter(InvitedList.this, listAllData);
		invited_list.setAdapter(allAdapter);
		
		if (strInvitedList.length() > 0) {
			empty_invited_list.setVisibility(View.GONE);
			invited_list.setVisibility(View.VISIBLE);
		} else {
			empty_invited_list.setVisibility(View.VISIBLE);
			invited_list.setVisibility(View.GONE);
		}

		/*
		 * String[] aInvited = strInvitedList.split(";"); //check for (String
		 * email: aInvited) { DeviceUtility.log(TAG, "email: " + email); }
		 * 
		 * if (aInvited.length > 0) { ArrayAdapter<String> adapter = new
		 * ArrayAdapter<String>(this, R.layout.invited_list_item, R.id.name,
		 * aInvited); invited_list.setAdapter(adapter);
		 * empty_invited_list.setVisibility(View.GONE);
		 * invited_list.setVisibility(View.VISIBLE); } else {
		 * empty_invited_list.setVisibility(View.VISIBLE);
		 * invited_list.setVisibility(View.GONE); }
		 */
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

//	void getInviteeList() {
//		Lead lead = new Lead(Constants.DBHANDLER);
//		// return lead.getLeadListDisplay();s
//		String str = "";
//
//		for (int i = 0; i < leadData.size(); i++) {
//			if (i == 0) {
//				str = "'" + leadData.get(i).id + "'";
//			} else {
//				str = str + ",'" + leadData.get(i).id + "'";
//			}
//		}
//
//		Cursor cursor = lead.getSelectLeadInviteeListDisplayByFilter("", str);
//
//		if (cursor.getCount() != 0) {
//			allData = new InviteeData();
//			allData.subject = getString(R.string.leads);
//			allData.isTitle = true;
//			allData.strBy = "lead";
//			listAllData.add(allData);
//
//			do {
//
//				String name;
//				String strFirstName = cursor.getString(cursor
//						.getColumnIndex("FIRST_NAME"));
//				String strLastName = cursor.getString(cursor
//						.getColumnIndex("LAST_NAME"));
//				if (strFirstName == null) {
//					name = strLastName;
//				} else {
//					name = strFirstName + " " + strLastName;
//				}
//
//				allData = new InviteeData();
//				allData.subject = name;
//				allData.account = cursor.getString(cursor
//						.getColumnIndex("COMPANY_NAME"));
//				allData.account = cursor.getString(cursor
//						.getColumnIndex("COMPANY_NAME"));
//				allData.isTitle = false;
//				allData.strBy = "lead";
//				listAllData.add(allData);
//
//			} while (cursor.moveToNext());
//
//		}
//
//		// ////////////////
//
//		Contact contact = new Contact(Constants.DBHANDLER);
//		// return lead.getLeadListDisplay();s
//
//		Cursor contactCursor = contact.getSelectedContactListDisplayByFilter(
//				"", getSelectedContact());
//
//		if (contactCursor.getCount() != 0) {
//			allData = new InviteeData();
//			allData.subject = getString(R.string.contacts);
//			allData.isTitle = true;
//			allData.strBy = "contact";
//			listAllData.add(allData);
//
//			do {
//
//				String name;
//				String strFirstName = contactCursor.getString(contactCursor
//						.getColumnIndex("FIRST_NAME"));
//				String strLastName = contactCursor.getString(contactCursor
//						.getColumnIndex("LAST_NAME"));
//				if (strFirstName == null) {
//					name = strLastName;
//				} else {
//					name = strFirstName + " " + strLastName;
//				}
//
//				allData = new InviteeData();
//				allData.subject = name;
//				allData.account = contactCursor.getString(contactCursor
//						.getColumnIndex("COMPANY_NAME"));
//				allData.isTitle = false;
//				allData.strBy = "contact";
//				listAllData.add(allData);
//
//			} while (contactCursor.moveToNext());
//
//		}
//
//		Master master = new Master(Constants.DBHANDLER);
//		System.out.println("getSelectedUser() = " + getSelectedUser());
//		Cursor userCursor = master.getSelectedAllMasterCompanyFilter(
//				"UserList", "", getSelectedUser());
//
//		if (userCursor.getCount() != 0) {
//			allData = new InviteeData();
//			allData.subject = getString(R.string.users);
//			allData.isTitle = true;
//			allData.strBy = "user";
//			listAllData.add(allData);
//			userCursor.moveToFirst();
//			do {
//				allData = new InviteeData();
//				allData.subject = userCursor.getString(userCursor
//						.getColumnIndex("MASTER_TEXT"));
//				allData.strBy = "user";
//				allData.isTitle = false;
//				listAllData.add(allData);
//
//			} while (userCursor.moveToNext());
//
//		}
//		System.out.println("listAllData = " + listAllData.size());
//		allAdapter = new InviteeAllAdapter(InvitedList.this, listAllData);
//		invited_list.setAdapter(allAdapter);
//	}

	@Override
	protected void onResume() {
		super.onResume();
		// InvitedProfile oProfile = new InvitedProfile();
		// oProfile.execute(strInvitedList);
	}

	// private class InvitedProfile extends AsyncTask<String, Void,
	// ArrayList<ProfileDisplay>> {
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// loadingDialog = GuiUtility.getLoadingDialog(InvitedList.this, false,
	// getString(R.string.processing));
	// }
	//
	//
	// @Override
	// protected ArrayList<ProfileDisplay> doInBackground(String... params) {
	// DatabaseUtility.getDatabaseHandler(InvitedList.this);
	// Invitee invite = new Invitee(Constants.DBHANDLER);
	// ArrayList<ProfileDisplay> profile =
	// invite.getSelectedInviteeList(params[0]);
	// return profile;
	// }
	//
	// @Override
	// protected void onPostExecute(ArrayList<ProfileDisplay> result) {
	// super.onPostExecute(result);
	// if (loadingDialog.isShowing()) {
	// loadingDialog.dismiss();
	// }
	// invitedAdapter.addData(result);
	// invitedAdapter.notifyDataSetChanged();
	// }
	// }

	String getSelectedLead() {
		String str = "";

		for (int i = 0; i < leadListData.size(); i++) {
			if (i == 0) {
				str = "'" + leadListData.get(i) + "'";
			} else {
				str = str + ",'" + leadListData.get(i) + "'";
			}
		}
		return str;
	}

	String getSelectedContact() {
		String str = "";

		for (int i = 0; i < leadContactData.size(); i++) {
			if (i == 0) {
				str = "'" + leadContactData.get(i) + "'";
			} else {
				str = str + ",'" + leadContactData.get(i) + "'";
			}
		}
		return str;
	}

	String getSelectedUser() {
		String str = "";

		for (int i = 0; i < leadUserData.size(); i++) {
			if (i == 0) {
				str = "'" + leadUserData.get(i) + "'";
			} else {
				str = str + ",'" + leadUserData.get(i) + "'";
			}
		}
		return str;
	}
}
