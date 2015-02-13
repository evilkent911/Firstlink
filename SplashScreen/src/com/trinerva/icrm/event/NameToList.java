package com.trinerva.icrm.event;

import java.util.ArrayList;
import java.util.List;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.contacts.ContactInfoTab;
import com.trinerva.icrm.contacts.ContactList;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.leads.LeadInfo;
import com.trinerva.icrm.leads.LeadList;
import com.trinerva.icrm.object.ProfileDisplay;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.StorageUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NameToList extends Activity {
	private String TAG = "EventInvitee";
	private SimpleCursorAdapter leadAdapter, contactAdapter, userListAdapter;
	private EditText search, searchContact;
	private ListView lead_list, contact_list;
	private String strSector = "INVITEE";
	private Dialog loadingDialog;
	private InvitedAdapter invitedAdapter;
	private String strSelectedInvitee = "";
	private boolean bEmptyContactList = false;
	private String strFinalSelectedEmail = "";
	private String strFinalSelectedPhone = "";
	RelativeLayout tablayout;
	ImageView arrow;

	private TextView call, sms;
	private String strMode = Constants.ACTION_CALL;
	LinearLayout hideLayout;

	List<StoreData> leadData = new ArrayList<StoreData>();
	List<StoreData> contactData = new ArrayList<StoreData>();

	InviteeData allData;
	List<InviteeData> listAllData = new ArrayList<InviteeData>();

	InviteeAllAdapter allAdapter;

	StoreData storeData;
	LinearLayout mLeadLayout, mContactLayout;
	Button mCreate, mCreateContact;
	private static final int PICK_CONTACT = 130;
	// List<ProfileDisplay> leadData = new ArrayList<ProfileDisplay>();
	String lastName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_to);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strSelectedInvitee = bundle.getString("SELECTED_INVITEE");
			System.out.println("strSelectedInvitee = " + strSelectedInvitee);
			String s = new String(strSelectedInvitee);
			String a[] = s.split(";");
			for (int i = 0; i < a.length; i++) {
				System.out.println("a[i] = " + a[i]);
				if (a[i].indexOf("2-") != -1) {
					storeData = new StoreData();
					storeData.strBy = "contact";
					storeData.id = a[i].substring(2);
					contactData.add(storeData);
				} else if (a[i].indexOf("3-") != -1) {
					storeData = new StoreData();
					storeData.strBy = "lead";
					storeData.id = a[i].substring(2);
					leadData.add(storeData);
				}

			}
		}

		// invitees = (TextView) findViewById(R.id.invitees);
		// add_new = (TextView) findViewById(R.id.add_new);
		search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(filterTextWatcher);
		searchContact = (EditText) findViewById(R.id.searchContact);
		searchContact.addTextChangedListener(contactFilterTextWatcher);
		tablayout = (RelativeLayout) findViewById(R.id.tablayout);
		hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
		call = (TextView) findViewById(R.id.call);
		sms = (TextView) findViewById(R.id.sms);
		mLeadLayout = (LinearLayout) findViewById(R.id.leadLayout);
		mContactLayout = (LinearLayout) findViewById(R.id.contactLayout);
		mLeadLayout.setVisibility(View.VISIBLE);
		mContactLayout.setVisibility(View.GONE);
		call.setOnClickListener(showLog);
		sms.setOnClickListener(showLog);

		mCreate = (Button) findViewById(R.id.create);
		mCreateContact = (Button) findViewById(R.id.createContact);
		// add_new.setOnClickListener(doShowInvitee);

		lead_list = (ListView) findViewById(R.id.lead_list);
		contact_list = (ListView) findViewById(R.id.contact_list);

		arrow = (ImageView) findViewById(R.id.hideCalendar);

		invitedAdapter = new InvitedAdapter(NameToList.this,
				R.layout.invited_item);
		search.setVisibility(View.VISIBLE);
		// doShowSelectedSector(strSector);
		doShowSelectedMenu(strMode);

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

				// // ProfileDisplay profile = new ProfileDisplay(cursor);
				//
				// storeData = new StoreData();
				// storeData.removeId = leadData.size();
				// storeData.strBy = "lead";
				// storeData.id = cursor
				// .getString(cursor.getColumnIndex("EMAIL1")).toString();
				// leadData.add(storeData);
				//
				// LoadLeadContact task = new LoadLeadContact();
				// task.execute();
				String strFirstName = cursor.getString(cursor
						.getColumnIndex("FIRST_NAME"));
				String strLastName = cursor.getString(cursor
						.getColumnIndex("LAST_NAME"));
				if (strFirstName == null) {
					strLastName = strLastName;
				} else {
					strLastName = strFirstName + " " + strLastName;
				}
				System.out.println("Test = "+cursor.getString(
										cursor.getColumnIndex("INTERNAL_NUM"))
										.toString());
				Intent intent = new Intent();
				intent.putExtra("NameTo", strLastName);
				intent.putExtra(
						"NameToId",
						"3-"
								+ cursor.getString(
										cursor.getColumnIndex("INTERNAL_NUM"))
										.toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		mCreateContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						NameToList.this);
//				builder.setItems(R.array.string_array_name,
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// The 'which' argument contains the index
//								// position
//								// of the selected item
//
//								switch (which) {
//								case 0:
//									Intent intent = new Intent(
//											Intent.ACTION_PICK,
//											ContactsContract.Contacts.CONTENT_URI);
//									startActivityForResult(intent, PICK_CONTACT);
//									break;
//								case 1:
									Intent contact = new Intent(
											NameToList.this,
											ContactInfoTab.class);
									contact.putExtra("setNameTo", true);
									contact.putExtra(Constants.IS_CONTACT_NAME_TO, true);
									contact.putExtra("lastName", lastName);
									NameToList.this.startActivity(contact);
//									break;
//								case 2:
//
//									break;
//								default:
//									break;
//								}
//							}
//						});
//				builder.show();

			}
		});

		mCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						NameToList.this);
//				builder.setItems(R.array.string_array_name,
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// The 'which' argument contains the index
//								// position
//								// of the selected item
//
//								switch (which) {
//								case 0:
//									Intent intent = new Intent(
//											Intent.ACTION_PICK,
//											ContactsContract.Contacts.CONTENT_URI);
//									startActivityForResult(intent, PICK_CONTACT);
//									break;
//								case 1:
//									System.out
//											.println(" mCreate.getText().toString() = "
//													+ lastName);
									Intent contact = new Intent(
											NameToList.this, LeadInfo.class);
									contact.putExtra(Constants.IS_CONTACT_NAME_TO, false);
									contact.putExtra("setNameTo", true);
									contact.putExtra("lastName", lastName);
									startActivity(contact);
//									break;
//								case 2:
//
//									break;
//								default:
//									break;
//								}
//							}
//						});
//				builder.show();
			}
		});

		contact_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Cursor cursor = contactAdapter.getCursor();
				cursor.moveToPosition(position);
				// System.out.println("Click");
				// // ProfileDisplay profile = new ProfileDisplay(cursor);
				// storeData = new StoreData();
				// storeData.removeId = leadData.size();
				// storeData.strBy = "contact";
				// storeData.id = cursor
				// .getString(cursor.getColumnIndex("EMAIL1")).toString();
				// contactData.add(storeData);
				//
				// LoadContact task = new LoadContact();
				// task.execute();

				String strFirstName = cursor.getString(cursor
						.getColumnIndex("FIRST_NAME"));
				String strLastName = cursor.getString(cursor
						.getColumnIndex("LAST_NAME"));
				if (strFirstName == null) {
					strLastName = strLastName;
				} else {
					strLastName = strFirstName + " " + strLastName;
				}
				Intent intent = new Intent();
				intent.putExtra("NameTo", strLastName);
				intent.putExtra(
						"NameToId",
						"2-"
								+ cursor.getString(
										cursor.getColumnIndex("INTERNAL_NUM"))
										.toString());
				setResult(RESULT_OK, intent);
				finish();
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

		if (strMode.equalsIgnoreCase(Constants.ACTION_CALL)) {
			call.setBackgroundResource(R.drawable.blue_menu_selected_middle);
			LoadLeadContact task = new LoadLeadContact();
			task.execute();
			mLeadLayout.setVisibility(View.VISIBLE);
			mContactLayout.setVisibility(View.GONE);
			call.setBackgroundResource(R.drawable.blue_menu_selected_middle);
		}

		if (strMode.equalsIgnoreCase(Constants.ACTION_SMS)) {
			LoadContact task = new LoadContact();
			task.execute();
			sms.setBackgroundResource(R.drawable.blue_menu_selected_middle);
			mLeadLayout.setVisibility(View.GONE);
			mContactLayout.setVisibility(View.VISIBLE);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("nameToFinish");
		registerReceiver(this.broadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		search.removeTextChangedListener(filterTextWatcher);
		searchContact.removeTextChangedListener(contactFilterTextWatcher);

		if (leadAdapter != null) {
			leadAdapter.getCursor().close();
			leadAdapter = null;
		}

		if (contactAdapter != null) {
			contactAdapter.getCursor().close();
			contactAdapter = null;
		}
		unregisterReceiver(broadcastReceiver);
	}

	private FilterQueryProvider leadFilter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(final CharSequence constraint) {
			Lead lead = new Lead(Constants.DBHANDLER);

			String str = "";

			for (int i = 0; i < leadData.size(); i++) {
				if (i == 0) {
					str = "'" + leadData.get(i).id + "'";
				} else {
					str = str + ",'" + leadData.get(i).id + "'";
				}
			}
			lastName = constraint.toString();
			final Cursor cursor = lead.getLeadListDisplayByFilter(constraint
					.toString());

			runOnUiThread(new Runnable() {
				public void run() {
					if (constraint.toString().length() == 0) {
						System.out.println("-1 Show");
						mCreate.setVisibility(View.GONE);
					} else if (constraint.toString().length() == 0
							&& getNameToLead(
									cursor.getString(cursor
											.getColumnIndex("FIRST_NAME")),
									cursor.getString(cursor
											.getColumnIndex("LAST_NAME")))
									.equals(constraint.toString())) {
						mCreate.setVisibility(View.GONE);

					} else if (constraint.toString().length() != 0) {

						System.out.println("Show");
						mCreate.setVisibility(View.VISIBLE);
						mCreate.setText(getString(R.string.create,
								constraint.toString()));
						cursor.moveToFirst();
						do {
							if (cursor.getCount() != 0) {

								if (getNameToLead(
										cursor.getString(cursor
												.getColumnIndex("FIRST_NAME")),
										cursor.getString(cursor
												.getColumnIndex("LAST_NAME")))
										.equalsIgnoreCase(constraint.toString())) {
									mCreate.setVisibility(View.GONE);
									return;
								} else {
									mCreate.setVisibility(View.VISIBLE);
								}
							}
						} while (cursor.moveToNext());

					}
				}
			});

			return cursor;
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
			DatabaseUtility.getDatabaseHandler(NameToList.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s

			return lead.getLeadListDisplayByFilter("");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(NameToList.this, false,
					null);
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
								NameToList.this, Constants.CACHE_FOLDER);
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

			leadAdapter = new SimpleCursorAdapter(NameToList.this,
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
			DatabaseUtility.getDatabaseHandler(NameToList.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			return contact.getContactListDisplayByFilter("");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(NameToList.this, false,
					null);
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
								NameToList.this, Constants.CACHE_FOLDER);
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

			contactAdapter = new SimpleCursorAdapter(NameToList.this,
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

	// ///

	private FilterQueryProvider contactFilter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(final CharSequence constraint) {
			try {
				DeviceUtility.log(TAG, "filter: " + constraint);

				lastName = constraint.toString();

				DatabaseUtility.getDatabaseHandler(NameToList.this);
				Contact contact = new Contact(Constants.DBHANDLER);

				final Cursor cursor = contact
						.getContactListDisplayByFilter(constraint.toString());
				runOnUiThread(new Runnable() {
					public void run() {
						if (constraint.toString().length() == 0) {
							System.out.println("-1 Show");
							mCreateContact.setVisibility(View.GONE);
						} else if (constraint.toString().length() == 0
								&& getNameToLead(
										cursor.getString(cursor
												.getColumnIndex("FIRST_NAME")),
										cursor.getString(cursor
												.getColumnIndex("LAST_NAME")))
										.equals(constraint.toString())) {
							mCreateContact.setVisibility(View.GONE);

						} else if (constraint.toString().length() != 0) {

							System.out.println("Show");
							mCreateContact.setVisibility(View.VISIBLE);
							mCreateContact.setText(getString(R.string.create,
									constraint.toString()));
							cursor.moveToFirst();
							do {
								if (cursor.getCount() != 0) {

									if (getNameToLead(
											cursor.getString(cursor
													.getColumnIndex("FIRST_NAME")),
											cursor.getString(cursor
													.getColumnIndex("LAST_NAME")))
											.equalsIgnoreCase(
													constraint.toString())) {
										mCreateContact.setVisibility(View.GONE);
										return;
									} else {
										mCreateContact
												.setVisibility(View.VISIBLE);
									}
								}
							} while (cursor.moveToNext());

						}
					}
				});

				return cursor;

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
		return str;
	}

	class StoreData {
		String id;
		int removeId;
		String strBy;
	}

	String getNameToLead(String strFirstName, String strLastName) {

		if (strFirstName == null) {
			return strLastName = strLastName;
		} else {
			return strLastName = strFirstName + " " + strLastName;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case PICK_CONTACT:
				if (resultCode == Activity.RESULT_OK) {
					String strUri = data.getDataString();
					DeviceUtility.log(TAG, "strUri: " + strUri);
					Intent contact = new Intent(NameToList.this, LeadInfo.class);
					contact.putExtra("CONTACT_URI", strUri);
					contact.putExtra("lastName", lastName);
					NameToList.this.startActivity(contact);
				}
				break;
			}

		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("nameToFinish")) {
			if (strMode.equalsIgnoreCase(Constants.ACTION_CALL)) {
				
				Lead lead = new Lead(Constants.DBHANDLER);
				Cursor cursor = lead.getLeadListDisplay();
				cursor.moveToLast();
				
				String strFirstName = cursor.getString(cursor
						.getColumnIndex("FIRST_NAME"));
				String strLastName = cursor.getString(cursor
						.getColumnIndex("LAST_NAME"));
				if (strFirstName == null) {
					strLastName = strLastName;
				} else {
					strLastName = strFirstName + " " + strLastName;
				}
				
				intent.putExtra("NameTo", strLastName);
				intent.putExtra(
						"NameToId",
						"3-"
								+ cursor.getString(
										cursor.getColumnIndex("INTERNAL_NUM"))
										.toString());
				NameToList.this.setResult(RESULT_OK, intent);
			}

			if (strMode.equalsIgnoreCase(Constants.ACTION_SMS)) {
				
				Contact contact = new Contact(Constants.DBHANDLER);
				final Cursor cursor = contact.getContactListDisplayByFilter("");
				cursor.moveToLast();
				
				String strFirstName = cursor.getString(cursor
						.getColumnIndex("FIRST_NAME"));
				String strLastName = cursor.getString(cursor
						.getColumnIndex("LAST_NAME"));
				if (strFirstName == null) {
					strLastName = strLastName;
				} else {
					strLastName = strFirstName + " " + strLastName;
				}
				intent.putExtra("NameTo", strLastName);
				intent.putExtra(
						"NameToId",
						"2-"
								+ cursor.getString(
										cursor.getColumnIndex("INTERNAL_NUM"))
										.toString());
				NameToList.this.setResult(RESULT_OK, intent);

			}
			finish();
			}
			
		}
	};
}
