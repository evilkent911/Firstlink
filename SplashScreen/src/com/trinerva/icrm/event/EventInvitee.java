package com.trinerva.icrm.event;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.object.ProfileDisplay;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.StorageUtility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EventInvitee extends Activity {
	private String TAG = "EventInvitee";
	private SimpleCursorAdapter leadAdapter, contactAdapter, userListAdapter;
	ImageView done;
	private EditText search, searchContact, searchUser;
	private ListView lead_list, contact_list, user_list;
	private String strSector = "INVITEE";
	private Dialog loadingDialog;
	private InvitedAdapter invitedAdapter;
	private String strSelectedInvitee = "";
	private boolean bEmptyContactList = false;
	private String strFinalSelectedEmail = "";
	private String strFinalSelectedPhone = "";
	RelativeLayout tablayout;
	ImageView arrow;

	private TextView call, sms, email;
	private String strMode = Constants.ACTION_CALL;
	LinearLayout hideLayout;
	TextView mSelected, mHideSelected;
	LinearLayout mLeadLayout, mContactLayout, mUserList;

	List<StoreData> leadData = new ArrayList<StoreData>();
	List<StoreData> contactData = new ArrayList<StoreData>();
	List<StoreData> userData = new ArrayList<StoreData>();

	ListView allList;
	InviteeData allData;
	List<InviteeData> listAllData = new ArrayList<InviteeData>();

	InviteeAllAdapter allAdapter;

	StoreData storeData;

	// List<ProfileDisplay> leadData = new ArrayList<ProfileDisplay>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_invitee);

		mSelected = (TextView) findViewById(R.id.selected);
		mHideSelected = (TextView) findViewById(R.id.hideSelected);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strSelectedInvitee = bundle.getString("SELECTED_INVITEE");
			System.out.println("strSelectedInvitee = " + strSelectedInvitee);
			if (!strSelectedInvitee.equals("")) {
				String s = new String(strSelectedInvitee);
				String a[] = s.split(";");
				
				String t = s.replaceAll(Pattern.quote("|*|"), "-");
				a = t.split(";");
				
				for(int i = 0; i<a.length; i++){
					a[i] = a[i].substring(0, 38);
					//System.out.println("the output is of a[" + i + "] " + a[i]);
				}

				for (int i = 0; i < a.length; i++) {
					System.out.println("a[i] = " + a[i]);
					if (a[i].substring(0, 1).equals("0")) {
						System.out.println("user");
						storeData = new StoreData();
						storeData.strBy = "user";
						storeData.id = a[i].substring(2);
						userData.add(storeData);
					} else if (a[i].substring(0, 1).equals("2")) {
						storeData = new StoreData();
						storeData.strBy = "contact";
						storeData.id = a[i].substring(2);
						contactData.add(storeData);
					} else if (a[i].substring(0, 1).equals("3")) {
						storeData = new StoreData();
						storeData.strBy = "lead";
						storeData.id = a[i].substring(2);
						leadData.add(storeData);
					}
				}
			}
			mSelected.setText(getString(
					R.string.selected,
					String.valueOf(leadData.size() + contactData.size()
							+ userData.size())));
			mHideSelected.setText(getString(
					R.string.selected,
					String.valueOf(leadData.size() + contactData.size()
							+ userData.size())));
			for (int i = 0; i < leadData.size(); i++) {
				System.out.println("leadData = " + leadData.get(i).id);
			}
			for (int i = 0; i < contactData.size(); i++) {
				System.out.println("contactData = " + contactData.get(i).id);
			}
			for (int i = 0; i < userData.size(); i++) {
				System.out.println("userData = " + userData.get(i).id);
			}
		}

		// invitees = (TextView) findViewById(R.id.invitees);
		// add_new = (TextView) findViewById(R.id.add_new);
		done = (ImageView) findViewById(R.id.done);
		search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(filterTextWatcher);
		searchContact = (EditText) findViewById(R.id.searchContact);
		searchContact.addTextChangedListener(contactFilterTextWatcher);
		searchUser = (EditText) findViewById(R.id.searchUser);
		searchUser.addTextChangedListener(userFilterTextWatcher);
		tablayout = (RelativeLayout) findViewById(R.id.tablayout);
		hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
		call = (TextView) findViewById(R.id.call);
		sms = (TextView) findViewById(R.id.sms);
		email = (TextView) findViewById(R.id.email);

		mLeadLayout = (LinearLayout) findViewById(R.id.leadLayout);
		mContactLayout = (LinearLayout) findViewById(R.id.contactLayout);
		mUserList = (LinearLayout) findViewById(R.id.userListLayout);

		mLeadLayout.setVisibility(View.VISIBLE);
		mContactLayout.setVisibility(View.GONE);
		mUserList.setVisibility(View.GONE);

		call.setOnClickListener(showLog);
		sms.setOnClickListener(showLog);
		email.setOnClickListener(showLog);

		// add_new.setOnClickListener(doShowInvitee);
		lead_list = (ListView) findViewById(R.id.lead_list);
		contact_list = (ListView) findViewById(R.id.contact_list);
		user_list = (ListView) findViewById(R.id.user_list);

		allList = (ListView) findViewById(R.id.all_list);

		arrow = (ImageView) findViewById(R.id.hideCalendar);

		invitedAdapter = new InvitedAdapter(EventInvitee.this,
				R.layout.invited_item);
		search.setVisibility(View.VISIBLE);
		done.setOnClickListener(doSaveResult);
		// doShowSelectedSector(strSector);
		doShowSelectedMenu(strMode);

		findViewById(R.id.hideCalendar).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						listAllData.clear();
						hideLayout.setVisibility(View.VISIBLE);

						Lead lead = new Lead(Constants.DBHANDLER);
						// return lead.getLeadListDisplay();
						String str = "";

						for (int i = 0; i < leadData.size(); i++) {
							if (i == 0) {
								str = "'" + leadData.get(i).id + "'";
							} else {
								str = str + ",'" + leadData.get(i).id + "'";
							}
						}

						Cursor cursor = lead
								.getSelectLeadInviteeListDisplayByFilter("",
										str);

						if (cursor.getCount() != 0) {
							allData = new InviteeData();
							allData.subject = getString(R.string.leads);
							allData.isTitle = true;
							allData.strBy = "lead";
							listAllData.add(allData);
							leadData.clear();
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
								allData.account = cursor.getString(cursor
										.getColumnIndex("COMPANY_NAME"));
								allData.isTitle = false;
								allData.strBy = "lead";
								listAllData.add(allData);
								
								storeData = new StoreData();
								storeData.strBy = "lead";
								storeData.id = cursor.getString(cursor
										.getColumnIndex("LEAD_ID"));
								leadData.add(storeData);

							} while (cursor.moveToNext());

						}

						// ////////////////

						Contact contact = new Contact(Constants.DBHANDLER);
						// return lead.getLeadListDisplay();s

						Cursor contactCursor = contact
								.getSelectedContactListDisplayByFilter("",
										getSelectedContact());
						

						if (contactCursor.getCount() != 0) {
							allData = new InviteeData();
							allData.subject = getString(R.string.contacts);
							allData.isTitle = true;
							allData.strBy = "contact";
							listAllData.add(allData);
							contactData.clear();
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
								allData.isTitle = false;
								allData.strBy = "contact";
								listAllData.add(allData);
								
								storeData = new StoreData();
								storeData.strBy = "contact";
								storeData.id = contactCursor.getString(contactCursor
										.getColumnIndex("CONTACT_ID"));
								contactData.add(storeData);

							} while (contactCursor.moveToNext());

						}

						Master master = new Master(Constants.DBHANDLER);
						System.out.println("getSelectedUser() = "
								+ getSelectedUser());
						Cursor userCursor = master
								.getSelectedAllMasterCompanyFilter("UserList",
										"", getSelectedUser());

						if (userCursor.getCount() != 0) {
							allData = new InviteeData();
							allData.subject = getString(R.string.users);
							allData.isTitle = true;
							allData.strBy = "user";
							listAllData.add(allData);
							userCursor.moveToFirst();
							userData.clear();
							do {
								allData = new InviteeData();
								allData.subject = userCursor
										.getString(userCursor
												.getColumnIndex("MASTER_TEXT"));
								allData.strBy = "user";
								// allData.strId = userCursor
								// .getString(userCursor
								// .getColumnIndex("MASTER_VALUE"));
								storeData = new StoreData();
								storeData.strBy = "user";
								storeData.id = userCursor.getString(userCursor
										.getColumnIndex("MASTER_VALUE"));
								userData.add(storeData);
								allData.isTitle = false;
								listAllData.add(allData);

							} while (userCursor.moveToNext());

						}

						allAdapter = new InviteeAllAdapter(EventInvitee.this,
								listAllData);
						allList.setAdapter(allAdapter);
					}
				});

		findViewById(R.id.showLayout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideLayout.setVisibility(View.GONE);

				if (strMode.equalsIgnoreCase(Constants.ACTION_EMAIL)) {

					LoadUserListContact user = new LoadUserListContact();
					user.execute();
				}

				if (strMode.equalsIgnoreCase(Constants.ACTION_CALL)) {
					LoadLeadContact task = new LoadLeadContact();
					task.execute();
				}

				if (strMode.equalsIgnoreCase(Constants.ACTION_SMS)) {
					LoadContact contact = new LoadContact();
					contact.execute();
				}
				// LoadContact contact = new LoadContact();
				// contact.execute();
				// LoadUserListContact userList = new LoadUserListContact();
				// userList.execute();

				// if (leadAdapter != null) {
				// leadAdapter.notifyDataSetChanged();
				// }
				//
				// if (contactAdapter != null) {
				// contactAdapter.notifyDataSetChanged();
				// }
				//
				// if (userListAdapter != null) {
				// userListAdapter.notifyDataSetChanged();
				// }
			}
		});
		
		
		allList.setOnItemClickListener(new OnItemClickListener() {
			//List<InviteeData> LeadList = new ArrayList<InviteeData>();
			//List<InviteeData> ContactList = new ArrayList<InviteeData>();
			//List<InviteeData> UserList = new ArrayList<InviteeData>();
			
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				System.out.println("remove = " + position);
				System.out.println("xxx 1 = " + listAllData.get(position).strBy);
				
				
					if(listAllData.get(position).isTitle == false){
						if (listAllData.get(position -1).strBy.equals("lead")) {
							System.out.println("remove lead = " + (position -1));
							leadData.remove(position -1);
						} 
						else if (listAllData.get(position).strBy.equals("contact")) {
							if (leadData.size() != 0) {
								// contactData.remove(contactData.get(position-2).removeId);
								System.out.println("remove contact = "
										+ (position - 2 - leadData.size()));
								contactData.remove(position - 2 - leadData.size());
							} else {
								// contactData.remove(contactData.get(position-1).removeId);
								System.out.println("remove contact = "
										+ (position - 1 - leadData.size()));
								contactData.remove(position - 1 - leadData.size());
							}
						} else if (listAllData.get(position).strBy.equals("user")) {
							if (leadData.size() != 0 && contactData.size() != 0) {
								System.out.println("in 1 = "
										+ userData.get(position - 3 - leadData.size()
												- contactData.size()).id);
								userData.remove(position - 3 - leadData.size()
										- contactData.size());
							}else if ((leadData.size() != 0 && contactData.size() == 0) || (leadData.size() == 0 && contactData.size() != 0)) {
								userData.remove(position - 2 - leadData.size()
										- contactData.size());
							} else if (leadData.size() != 0 || contactData.size() != 0) {
								System.out.println("in 2 = "
										+ userData.get(position - 1 - leadData.size()
												- contactData.size()).id);
								userData.remove(position - 1 - leadData.size()
										- contactData.size());
							} else {
								System.out.println("in 3 = "
										+ userData.get(position - 1 - leadData.size()
												- contactData.size()).id);
								userData.remove(position - 1 - leadData.size()
										- contactData.size());
							}
						}
					}
				
				
					
				
				

				listAllData.clear();
				hideLayout.setVisibility(View.VISIBLE);

				Lead lead = new Lead(Constants.DBHANDLER);
				// return lead.getLeadListDisplay();s

				Cursor cursor = lead.getSelectLeadInviteeListDisplayByFilter(
						"", getSelectedLead());
				if (cursor.getCount() != 0) {
					allData = new InviteeData();
					allData.subject = getString(R.string.leads);
					allData.strBy = "lead";
					allData.isTitle = true;
					listAllData.add(allData);

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
						allData.strBy = "lead";
						allData.isTitle = false;
						listAllData.add(allData);

					} while (cursor.moveToNext());

				}
				// /////////////
				Contact contact = new Contact(Constants.DBHANDLER);
				// return lead.getLeadListDisplay();s

				Cursor contactCursor = contact
						.getSelectedContactListDisplayByFilter("",
								getSelectedContact());

				if (contactCursor.getCount() != 0) {
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

				// ////////////////////////

				Master master = new Master(Constants.DBHANDLER);

				Cursor userCursor = master.getSelectedAllMasterCompanyFilter(
						"UserList", "", getSelectedUser());
				userCursor.moveToFirst();
				if (userCursor.getCount() != 0) {
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
				// ////////////////////////

				allAdapter = new InviteeAllAdapter(EventInvitee.this,
						listAllData);
				allList.setAdapter(allAdapter);

				allAdapter.notifyDataSetChanged();

				mSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
				mHideSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));

			}
		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		lead_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Cursor cursor = leadAdapter.getCursor();
				cursor.moveToPosition(position);

				// ProfileDisplay profile = new ProfileDisplay(cursor);

				storeData = new StoreData();
				storeData.removeId = leadData.size();
				storeData.strBy = "lead";
				storeData.id = cursor.getString(
						cursor.getColumnIndex("LEAD_ID")).toString();
				leadData.add(storeData);

				LoadLeadContact task = new LoadLeadContact();
				task.execute();
				mSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
				mHideSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
			}
		});

		contact_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Cursor cursor = contactAdapter.getCursor();
				cursor.moveToPosition(position);
				System.out.println("Click");
				// ProfileDisplay profile = new ProfileDisplay(cursor);
				storeData = new StoreData();
				storeData.removeId = leadData.size();
				storeData.strBy = "contact";
				storeData.id = cursor.getString(
						cursor.getColumnIndex("CONTACT_ID")).toString();
				contactData.add(storeData);

				LoadContact task = new LoadContact();
				task.execute();
				mSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
				mHideSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
			}
		});

		user_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Cursor cursor = userListAdapter.getCursor();
				cursor.moveToPosition(position);

				// ProfileDisplay profile = new ProfileDisplay(cursor);
				storeData = new StoreData();
				storeData.removeId = leadData.size();
				storeData.strBy = "user";
				storeData.id = cursor.getString(
						cursor.getColumnIndex("MASTER_VALUE")).toString();
				userData.add(storeData);

				LoadUserListContact task = new LoadUserListContact();
				task.execute();
				mSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
				mHideSelected.setText(getString(
						R.string.selected,
						String.valueOf(leadData.size() + contactData.size()
								+ userData.size())));
			}
		});
	}

	private OnClickListener showLog = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.call:
				strMode = Constants.ACTION_CALL;
				System.out.println("1");
				break;
			case R.id.sms:
				strMode = Constants.ACTION_SMS;
				System.out.println("2");
				break;
			case R.id.email:
				strMode = Constants.ACTION_EMAIL;
				System.out.println("3");

				break;
			}
			doShowSelectedMenu(strMode);
			// if (adapter != null) {
			// adapter.getFilter().filter(strMode);
			// }
		}
	};

	public void doShowSelectedMenu(String strMode) {
		call.setBackgroundResource(R.drawable.blue_menu_middle);
		sms.setBackgroundResource(R.drawable.blue_menu_middle);
		email.setBackgroundResource(R.drawable.blue_menu_middle);

		if (strMode.equalsIgnoreCase(Constants.ACTION_EMAIL)) {
			email.setBackgroundResource(R.drawable.blue_menu_selected_middle);
			mLeadLayout.setVisibility(View.GONE);
			mContactLayout.setVisibility(View.GONE);
			mUserList.setVisibility(View.VISIBLE);
			LoadUserListContact task = new LoadUserListContact();
			task.execute(new String[] { null });
		}

		if (strMode.equalsIgnoreCase(Constants.ACTION_CALL)) {
			call.setBackgroundResource(R.drawable.blue_menu_selected_middle);
			LoadLeadContact task = new LoadLeadContact();
			task.execute();
			// contact_list.setVisibility(View.GONE);
			// lead_list.setVisibility(View.VISIBLE);
			// user_list.setVisibility(View.GONE);
			mLeadLayout.setVisibility(View.VISIBLE);
			mContactLayout.setVisibility(View.GONE);
			mUserList.setVisibility(View.GONE);
		}

		if (strMode.equalsIgnoreCase(Constants.ACTION_SMS)) {
			LoadContact task = new LoadContact();
			task.execute();
			sms.setBackgroundResource(R.drawable.blue_menu_selected_middle);
			// contact_list.setVisibility(View.VISIBLE);
			// lead_list.setVisibility(View.GONE);
			// user_list.setVisibility(View.GONE);

			mLeadLayout.setVisibility(View.GONE);
			mContactLayout.setVisibility(View.VISIBLE);
			mUserList.setVisibility(View.GONE);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		search.removeTextChangedListener(filterTextWatcher);
		searchContact.removeTextChangedListener(contactFilterTextWatcher);
		searchUser.removeTextChangedListener(userFilterTextWatcher);

		if (leadAdapter != null) {
			leadAdapter.getCursor().close();
			leadAdapter = null;
		}

		if (contactAdapter != null) {
			contactAdapter.getCursor().close();
			contactAdapter = null;
		}

		if (userListAdapter != null) {
			userListAdapter.getCursor().close();
			userListAdapter = null;
		}
	}

	// private OnItemClickListener doAddInvitee = new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// Cursor cursor = adapter.getCursor();
	// cursor.moveToPosition(position);
	// DeviceUtility.log(TAG, "position: " + position);
	// DeviceUtility.log(TAG, "cursor position: " + cursor.getPosition());
	//
	// ProfileDisplay profile = new ProfileDisplay(cursor);
	// invitedAdapter.add(profile);
	// invitedAdapter.notifyDataSetChanged();
	// // requeryInvitee();
	// }
	// };

	private OnItemClickListener doRemoveInvitee = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			invitedAdapter.remove(position);
			invitedAdapter.notifyDataSetChanged();
			// requeryInvitee();
		}
	};

	private OnClickListener doSaveResult = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// save the invitee.
			// doSaveInvitee();

			listAllData.clear();
			hideLayout.setVisibility(View.VISIBLE);

			Lead lead = new Lead(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s

			Cursor cursor = lead.getSelectLeadInviteeListDisplayByFilter("",
					getSelectedLead());

			if (cursor.getCount() != 0) {
				allData = new InviteeData();
				allData.subject = getString(R.string.leads);
				allData.isTitle = true;
				allData.strBy = "lead";
				listAllData.add(allData);

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
					allData.email = cursor.getString(cursor
							.getColumnIndex("LEAD_ID"));
					allData.strBy = "lead";
					listAllData.add(allData);

				} while (cursor.moveToNext());

			}

			// ////////////////

			Contact contact = new Contact(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s

			Cursor contactCursor = contact
					.getSelectedContactListDisplayByFilter("",
							getSelectedContact());

			if (contactCursor.getCount() != 0) {
				allData = new InviteeData();
				allData.subject = getString(R.string.contacts);
				allData.isTitle = true;
				allData.strBy = "contact";
				listAllData.add(allData);

				do {

					String name;
					String strFirstName = contactCursor.getString(contactCursor
							.getColumnIndex("FIRST_NAME"));
					String strLastName = contactCursor.getString(contactCursor
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
					allData.email = contactCursor.getString(contactCursor
							.getColumnIndex("CONTACT_ID"));
					allData.isTitle = false;
					allData.strBy = "contact";
					listAllData.add(allData);

				} while (contactCursor.moveToNext());

			}

			Master master = new Master(Constants.DBHANDLER);
			System.out.println("getSelectedUser() = " + getSelectedUser());
			Cursor userCursor = master.getSelectedAllMasterCompanyFilter(
					"UserList", "", getSelectedUser());

			if (userCursor.getCount() != 0) {
				allData = new InviteeData();
				allData.subject = getString(R.string.users);
				allData.isTitle = true;
				allData.strBy = "user";
				listAllData.add(allData);
				userCursor.moveToFirst();
				do {
					allData = new InviteeData();
					allData.subject = userCursor.getString(userCursor
							.getColumnIndex("MASTER_TEXT"));
					allData.email = userCursor.getString(userCursor
							.getColumnIndex("MASTER_VALUE"));
					allData.strBy = "user";
					allData.isTitle = false;
					listAllData.add(allData);

				} while (userCursor.moveToNext());

			}

			SaveInvitee save = new SaveInvitee();
			save.execute();
		}
	};

	private class SaveInvitee extends AsyncTask<String, Void, Integer> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventInvitee.this,
					false, getString(R.string.processing));
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			ArrayList<ProfileDisplay> oProfile = invitedAdapter.getData();
			int iCount = oProfile.size();
			strFinalSelectedEmail = "";
			strFinalSelectedPhone = "";
			for (int i = 0; i < iCount; i++) {
				ProfileDisplay profile = oProfile.get(i);
				strFinalSelectedEmail += profile.getPersonId() + ";";
				strFinalSelectedPhone += profile.getPhoneNo() + ";";
			}
			return new Integer(iCount);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			List<String> getList = new ArrayList<String>();
			String passDatap[] = new String[listAllData.size()];
			strFinalSelectedEmail = "";
			for (int i = 0; i < listAllData.size(); i++) {
				if (listAllData.get(i).strBy.equals("user")
						&& listAllData.get(i).isTitle == false) {
					getList.add("0-" + listAllData.get(i).email);
					strFinalSelectedEmail += "0-" + listAllData.get(i).email
							+ ";";
				} else if (listAllData.get(i).strBy.equals("lead")
						&& listAllData.get(i).isTitle == false) {
					getList.add("3-" + listAllData.get(i).email);
					strFinalSelectedEmail += "3-" + listAllData.get(i).email
							+ ";";
				} else if (listAllData.get(i).isTitle == false) {
					getList.add("2-" + listAllData.get(i).email);
					strFinalSelectedEmail += "2-" + listAllData.get(i).email
							+ ";";
				}
			}

			System.out.println("all -= " + listAllData.size());

			Intent returnIntent = new Intent();
			returnIntent.putExtra("EMAIL", strFinalSelectedEmail);
			returnIntent.putExtra("PHONE", strFinalSelectedPhone);
			returnIntent.putExtra("TOTAL", getList.size());
			setResult(RESULT_OK, returnIntent);

			finish();
		}
	}

	/*
	 * private void doSaveInvitee() { Intent returnIntent = new Intent();
	 * //ArrayList<ProfileDisplay> oProfile = invitedAdapter.getData();
	 * ArrayList<String> oProfile = invitedAdapter.getData(); int iCount =
	 * oProfile.size(); String strEmailAddress = ""; String strPhoneNumber = "";
	 * for (int i = 0; i < iCount; i++ ) { //ProfileDisplay profile =
	 * oProfile.get(i); String profile = oProfile.get(i); //strEmailAddress +=
	 * profile.getEmail() + ";"; strEmailAddress += profile + ";"; //if
	 * (profile.getPhoneNo() != null && profile.getPhoneNo().length() > 0) { //
	 * strPhoneNumber += profile.getPhoneNo() + ";"; //} }
	 * 
	 * if (iCount > 0) { strEmailAddress.substring(0,
	 * strEmailAddress.length()-1); if (strPhoneNumber.length() > 0) {
	 * strPhoneNumber.substring(0, strPhoneNumber.length()-1); } }
	 * 
	 * returnIntent.putExtra("EMAIL", strEmailAddress);
	 * returnIntent.putExtra("PHONE", strPhoneNumber);
	 * returnIntent.putExtra("TOTAL", iCount); setResult(RESULT_OK,
	 * returnIntent); finish(); }
	 */

	// private void requeryInvitee() {
	// Cursor cursor = adapter.getCursor();
	// String strSearch = search.getText().toString();
	// // requery
	// Invitee invitee = new Invitee(Constants.DBHANDLER);
	// cursor = invitee.getAllContact(invitedAdapter.getData());
	// adapter = new SimpleCursorAdapter(EventInvitee.this,
	// R.layout.invitee_item, cursor, new String[] { "_id",
	// "FIRST_NAME", "LAST_NAME", "COMPANY_NAME",
	// "CONTACT_TYPE" }, new int[] { R.id.name, R.id.company });
	// adapter.setViewBinder(binder);
	// DeviceUtility.log(TAG, "cursor count: " + cursor.getCount());
	// adapter.setFilterQueryProvider(filter);
	// if (strSearch.length() > 0) {
	// adapter.getFilter().filter(strSearch);
	// }
	// adapter.notifyDataSetChanged();
	// }

	private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			int viewId = view.getId();
			switch (viewId) {
			case R.id.name:
				TextView name = (TextView) view;
				String strFirstName = cursor.getString(cursor
						.getColumnIndex("FIRST_NAME"));
				String strLastName = cursor.getString(cursor
						.getColumnIndex("LAST_NAME"));
				if (strFirstName == null) {
					name.setText(strLastName);
				} else {
					name.setText(strFirstName + " " + strLastName);
				}
				break;
			case R.id.company:
				TextView company = (TextView) view;
				String strContactType = cursor.getString(cursor
						.getColumnIndex("CONTACT_TYPE"));
				String strCompanyName = cursor.getString(cursor
						.getColumnIndex("COMPANY_NAME"));
				company.setText("[" + strContactType + "] " + strCompanyName);
				break;
			}
			return true;
		}
	};

	// private FilterQueryProvider filter = new FilterQueryProvider() {
	//
	// @Override
	// public Cursor runQuery(CharSequence constraint) {
	// DeviceUtility.log(TAG, "filter: " + constraint);
	// DatabaseUtility.getDatabaseHandler(EventInvitee.this);
	// Invitee invitee = new Invitee(Constants.DBHANDLER);
	// Cursor cursor = invitee.getInviteeDisplayByFilter(
	// constraint.toString(), invitedAdapter.getData());
	// return cursor;
	// }
	// };

	private FilterQueryProvider leadFilter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			Lead lead = new Lead(Constants.DBHANDLER);

			String str = "";

			for (int i = 0; i < leadData.size(); i++) {
				if (i == 0) {
					str = "'" + leadData.get(i).id + "'";
				} else {
					str = str + ",'" + leadData.get(i).id + "'";
				}
			}
			return lead.getLeadInviteeListDisplayByFilter(
					constraint.toString(), str);
		}
	};

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (leadAdapter != null) {
				leadAdapter.getFilter().filter(s);
			}
		}
	};

	private TextWatcher contactFilterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (contactAdapter != null) {
				contactAdapter.getFilter().filter(s);
			}
		}
	};

	private TextWatcher userFilterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (userListAdapter != null) {
				userListAdapter.getFilter().filter(s);
			}
		}
	};

	// private OnClickListener doShowInvitee = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// invitee_list.setVisibility(View.GONE);
	// if (adapter.isEmpty()) {
	// empty_contact_list.setText(getString(R.string.empty_contact_list));
	// if (bEmptyContactList) {
	// empty_contact_list.setVisibility(View.VISIBLE);
	// add_list.setVisibility(View.GONE);
	// } else {
	// empty_contact_list.setVisibility(View.GONE);
	// add_list.setVisibility(View.VISIBLE);
	// }
	// } else {
	// add_list.setVisibility(View.VISIBLE);
	// empty_contact_list.setVisibility(View.GONE);
	// }
	// search.setVisibility(View.VISIBLE);
	// strSector = "ADDNEW";
	// doShowSelectedSector(strSector);
	// }
	// };

	// private OnClickListener showInvitedList = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// empty_contact_list.setVisibility(View.GONE);
	// add_list.setVisibility(View.GONE);
	// search.setVisibility(View.GONE);
	// if (invitedAdapter.getCount() > 0) {
	// invitee_list.setVisibility(View.VISIBLE);
	// } else {
	// invitee_list.setVisibility(View.GONE);
	// empty_contact_list.setVisibility(View.VISIBLE);
	// empty_contact_list.setText(getString(R.string.empty_invitee));
	// }
	// strSector = "INVITEE";
	// doShowSelectedSector(strSector);
	// }
	// };

	// private void doShowSelectedSector(String strField) {
	// // invitees.setBackgroundResource(R.drawable.blue_menu_left);
	// add_new.setBackgroundResource(R.drawable.blue_menu_right);
	// if (strField.equalsIgnoreCase("ADDNEW")) {
	// add_new.setBackgroundResource(R.drawable.blue_menu_selected_right);
	// add_new.setOnClickListener(null);
	// // invitees.setOnClickListener(showInvitedList);
	// }
	//
	// if (strField.equalsIgnoreCase("INVITEE")) {
	// // invitees.setBackgroundResource(R.drawable.blue_menu_selected_left);
	// // invitees.setOnClickListener(null);
	// // add_new.setOnClickListener(doShowInvitee);
	// }
	// }

	// ///

	private class LoadLeadContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(EventInvitee.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s

			return lead
					.getLeadInviteeListDisplayByFilter("", getSelectedLead());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventInvitee.this,
					false, null);
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int viewId = view.getId();
				switch (viewId) {
				case R.id.company_name:
					TextView company = (TextView) view;
					company.setText(cursor.getString(cursor
							.getColumnIndex("COMPANY_NAME")));
					break;
				case R.id.contact_name:
					TextView name = (TextView) view;
					String strFirstName = cursor.getString(cursor
							.getColumnIndex("FIRST_NAME"));
					String strLastName = cursor.getString(cursor
							.getColumnIndex("LAST_NAME"));
					if (strFirstName == null) {
						name.setText(strLastName);
					} else {
						name.setText(strFirstName + " " + strLastName);
					}
					break;
				case R.id.photo:
					ImageView photo = (ImageView) view;
					String photoId = cursor.getString(cursor
							.getColumnIndex("PHOTO"));
					if (photoId != null) {
						StorageUtility storage = new StorageUtility(
								EventInvitee.this, Constants.CACHE_FOLDER);
						Bitmap bitmap = storage.doReadImage(photoId);
						if (bitmap != null) {
							photo.setImageBitmap(bitmap);
						} else {
							photo.setImageResource(R.drawable.contacts);
						}
					} else {
						photo.setImageResource(R.drawable.contacts);
					}
					break;
				case R.id.contact_sync:
//					ImageView cSync = (ImageView) view;
//					DeviceUtility.log(
//							TAG,
//							"IS_UPDATE: "
//									+ cursor.getString(cursor
//											.getColumnIndex("IS_UPDATE")));
//					String strIsUpdate = cursor.getString(cursor
//							.getColumnIndex("IS_UPDATE"));
//					String strActive = cursor.getString(cursor
//							.getColumnIndex("ACTIVE"));
//
//					if (strActive.equalsIgnoreCase("0")
//							&& strIsUpdate.equalsIgnoreCase("true")) {
//						cSync.setVisibility(View.GONE);
//					} else {
//						cSync.setVisibility(View.VISIBLE);
//						if (strActive.equalsIgnoreCase("1")) {
//							cSync.setImageResource(R.drawable.btn_sync_deleted);
//						} else {
//							cSync.setImageResource(R.drawable.btn_sync);
//						}
//					}
					break;
				}
				return true;
			}
		};

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			leadAdapter = new SimpleCursorAdapter(EventInvitee.this,
					R.layout.lead_item, result, new String[] { "_id",
							"FIRST_NAME", "LAST_NAME", "COMPANY_NAME", "PHOTO",
							"IS_UPDATE", "ACTIVE" }, new int[] { R.id.photo,
							R.id.contact_name, R.id.company_name,
							R.id.contact_sync });
			leadAdapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
			leadAdapter.setFilterQueryProvider(leadFilter);
			lead_list.setAdapter(leadAdapter);
			String strSearch = search.getText().toString();
			if (strSearch.length() > 0) {
				leadAdapter.getFilter().filter(strSearch);
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	// ///
	private class LoadContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(EventInvitee.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			return contact.getContactListDisplayByFilter("",
					getSelectedContact());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventInvitee.this,
					false, null);
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int viewId = view.getId();
				switch (viewId) {
				case R.id.company_name:
					TextView company = (TextView) view;
					company.setText(cursor.getString(cursor
							.getColumnIndex("COMPANY_NAME")));
					break;
				case R.id.contact_name:
					TextView name = (TextView) view;
					String strFirstName = cursor.getString(cursor
							.getColumnIndex("FIRST_NAME"));
					String strLastName = cursor.getString(cursor
							.getColumnIndex("LAST_NAME"));
					if (strFirstName == null) {
						name.setText(strLastName);
					} else {
						name.setText(strFirstName + " " + strLastName);
					}
					break;
				case R.id.photo:
					ImageView photo = (ImageView) view;
					String photoId = cursor.getString(cursor
							.getColumnIndex("PHOTO"));
					if (photoId != null) {
						StorageUtility storage = new StorageUtility(
								EventInvitee.this, Constants.CACHE_FOLDER);
						Bitmap bitmap = storage.doReadImage(photoId);
						if (bitmap != null) {
							photo.setImageBitmap(bitmap);
						} else {
							photo.setImageResource(R.drawable.contacts);
						}
					} else {
						photo.setImageResource(R.drawable.contacts);
					}
					break;
				case R.id.contact_sync:
//					ImageView cSync = (ImageView) view;
//					DeviceUtility.log(
//							TAG,
//							"IS_UPDATE: "
//									+ cursor.getString(cursor
//											.getColumnIndex("IS_UPDATE"))
//									+ " ACTIVE: "
//									+ cursor.getString(cursor
//											.getColumnIndex("ACTIVE")));
//					String strIsUpdate = cursor.getString(cursor
//							.getColumnIndex("IS_UPDATE"));
//					String strActive = cursor.getString(cursor
//							.getColumnIndex("ACTIVE"));
//					String strOppUpdate = cursor.getString(cursor
//							.getColumnIndex("OPP_UPDATE"));
//
//					if (strActive.equalsIgnoreCase("0")
//							&& strIsUpdate.equalsIgnoreCase("true")) {
//						if (strOppUpdate != null
//								&& strOppUpdate.equalsIgnoreCase("false")) {
//							cSync.setVisibility(View.VISIBLE);
//							cSync.setImageResource(R.drawable.btn_sync);
//						} else {
//							cSync.setVisibility(View.GONE);
//						}
//					} else {
//						cSync.setVisibility(View.VISIBLE);
//						if (strActive.equalsIgnoreCase("1")) {
//							cSync.setImageResource(R.drawable.btn_sync_deleted);
//						} else {
//							DeviceUtility.log(TAG, "show sync button");
//							cSync.setImageResource(R.drawable.btn_sync);
//						}
//					}
					break;
				}
				return true;
			}
		};

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			contactAdapter = new SimpleCursorAdapter(EventInvitee.this,
					R.layout.contact_item, result, new String[] { "_id",
							"CONTACT_ID", "FIRST_NAME", "LAST_NAME",
							"COMPANY_NAME", "PHOTO", "IS_UPDATE", "ACTIVE",
							"IS_UPDATE" }, new int[] { R.id.photo,
							R.id.contact_name, R.id.company_name,
							R.id.contact_sync });
			contactAdapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
			contactAdapter.setFilterQueryProvider(contactFilter);
			contact_list.setAdapter(contactAdapter);
			String strSearch = searchContact.getText().toString();
			if (strSearch.length() > 0) {
				contactAdapter.getFilter().filter(strSearch);
			}

			if (loadingDialog.isShowing()) {
				try {
					loadingDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	// ///

	private class LoadUserListContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(EventInvitee.this);
			Contact contact = new Contact(Constants.DBHANDLER);

			Master master = new Master(Constants.DBHANDLER);
			return master.getAllMasterCompanyFilter("UserList", "",
					getSelectedUser());

			// return contact.getContactListDisplay();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventInvitee.this,
					false, null);
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int viewId = view.getId();
				switch (viewId) {
				case R.id.company_name:
					TextView company = (TextView) view;
					company.setText(cursor.getString(cursor
							.getColumnIndex("MASTER_TEXT")));
					break;
				}
				return true;
			}
		};

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			userListAdapter = new SimpleCursorAdapter(EventInvitee.this,
					R.layout.company_item, result, new String[] { "_id",
							"MASTER_VALUE" }, new int[] { R.id.company_name });
			userListAdapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());

			userListAdapter.setFilterQueryProvider(userFilter);
			user_list.setAdapter(userListAdapter);
			String strSearch = searchUser.getText().toString();
			if (strSearch.length() > 0) {
				userListAdapter.getFilter().filter(strSearch);
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	// ///

	private FilterQueryProvider contactFilter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			try {
				DeviceUtility.log(TAG, "filter: " + constraint);
				DatabaseUtility.getDatabaseHandler(EventInvitee.this);
				Contact contact = new Contact(Constants.DBHANDLER);

				return contact.getContactListDisplayByFilter(
						constraint.toString(), getSelectedContact());

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	};

	private FilterQueryProvider userFilter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			try {
				DeviceUtility.log(TAG, "filter: " + constraint);
				DatabaseUtility.getDatabaseHandler(EventInvitee.this);
				Master contact = new Master(Constants.DBHANDLER);

				return contact.getAllMasterCompanyFilter("UserList",
						constraint.toString(), getSelectedUser());

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	};

	String getSelectedLead() {
		String str = "";

		for (int i = 0; i < leadData.size(); i++) {
			if (i == 0) {
				str = "'" + leadData.get(i).id + "'";
			} else {
				str = str + ",'" + leadData.get(i).id + "'";
			}
		}
		System.out.println("lead list = " + str);
		return str;
	}

	String getSelectedContact() {
		String str = "";

		for (int i = 0; i < contactData.size(); i++) {
			if (i == 0) {
				str = "'" + contactData.get(i).id + "'";
			} else {
				str = str + ",'" + contactData.get(i).id + "'";
			}
		}
		System.out.println("Contact list = " + str);
		return str;
	}

	String getSelectedUser() {
		String str = "";

		for (int i = 0; i < userData.size(); i++) {
			if (i == 0) {
				str = "'" + userData.get(i).id + "'";
			} else {
				str = str + ",'" + userData.get(i).id + "'";
			}
		}
		System.out.println("user list = " + str);
		return str;
	}

	class StoreData {
		String id;
		int removeId;
		String strBy;
	}
}
