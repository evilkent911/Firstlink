package com.trinerva.icrm.contacts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.event.EventInfo;
import com.trinerva.icrm.leads.LeadInfo;
import com.trinerva.icrm.object.ActivitiesLogDetail;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.EmailDetail;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.object.PhoneDetail;
import com.trinerva.icrm.tasks.TaskInfo;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

public class ContactInfoTab extends FragmentActivity {
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction("page");
		filter.addAction("modifiedDate");
		filter.addAction("saveTrue");
		registerReceiver(this.broadcastReceiver, filter);

		if (bReload && strId != null) {
			LoadContact task = new LoadContact();
			task.execute(new String[] { strId });
		}

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("page")) {
				mViewPager.setCurrentItem(intent.getIntExtra("page", 0));
			} else if (intent.getAction().equals("modifiedDate")) {// 06 Nov
																	// 2013
																	// 01:59:40

				SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");

				Date date;
				try {
					date = sf2.parse(intent.getStringExtra("date"));
					mModifiedDate.setText(getString(
							R.string.last_modified_date, sf.format(date)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(intent.getAction().equals("saveTrue")){
				save.setClickable(true);
			}
		}
	};

	private boolean bReload = true;

	private String TAG = "ContactInfoTab";
	private ContactDetail contact = new ContactDetail();
	private HashMap<String, String> hLoad = new HashMap<String, String>();
	private String strContactUri = null;
	private ArrayList<MasterInfo> aPrefix;
	private static final int MY_DATE_DIALOG_ID = 3990;
	private Spinner prefixSpinner;
	private TableLayout others_field;
	private LinearLayout basic_field;
	private ScrollView scroll_view;
	private TextView basic, others;
	ImageView save;
	private Dialog loadingDialog;
	// private ImageView birthday_clear;
	private View save_divider;
	private EditText first_name, last_name, company_name, job_title,
			department, mobile, work_fax, work_phone, other_phone, email,
			home_phone, email2, email3, skype_id, assistant_name,
			assistant_phone, street, city, state, zip, country, description;
	private String strSector = "BASIC";
	boolean bView = false;
	boolean bActive = true;

	LinearLayout mNavigationLayout;
	ViewPager mViewPager;
	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	private String strId = null;
	private String strActive = null;
	private String strContactId = null;
	private ArrayList<PhoneDetail> aContactNumberList;
	private ArrayList<EmailDetail> aEmailList;
	private ArrayList<String> aPhotoSelection;
	private static final int EMAIL_GROUP = 0;
	private static final int SMS_GROUP = 1;
	private static final int CALL_GROUP = 2;
	private static final int PHOTO_GROUP = 3;
	ImageView mEmail, mCall, mSms, mDelete;
	TextView mModifiedDate;
	ImageView mDot1, mDot2;
	Context context;
	String nameTo;
	String strSms;
	
	public static String lastName;
boolean bSetNameTo;
boolean bIsContact;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactinfo_tab);
		context = this;
		// scroll_view = (ScrollView) findViewById(R.id.scroll_view);
		// prefixSpinner = (Spinner) findViewById(R.id.prefix);
		// others_field = (TableLayout) findViewById(R.id.others_field);
		// basic_field = (LinearLayout) findViewById(R.id.basic_field);
		// basic = (TextView) findViewById(R.id.basic);
		// others = (TextView) findViewById(R.id.others);
		save = (ImageView) findViewById(R.id.save);
		mModifiedDate = (TextView) findViewById(R.id.modified);
		// first_name = (EditText) findViewById(R.id.first_name);
		// last_name = (EditText) findViewById(R.id.last_name);
		// company_name = (EditText) findViewById(R.id.company_name);
		// job_title = (EditText) findViewById(R.id.job_title);
		// department = (EditText) findViewById(R.id.department);
		// mobile = (EditText) findViewById(R.id.mobile);
		// work_fax = (EditText) findViewById(R.id.work_fax);
		// work_phone = (EditText) findViewById(R.id.work_phone);
		// other_phone = (EditText) findViewById(R.id.other_phone);
		// email = (EditText) findViewById(R.id.email);
		// home_phone = (EditText) findViewById(R.id.home_phone);
		// birthday = (EditText) findViewById(R.id.birthday);
		// birthday_clear = (ImageView) findViewById(R.id.birthday_clear);
		// email2 = (EditText) findViewById(R.id.email2);
		// email3 = (EditText) findViewById(R.id.email3);
		// skype_id = (EditText) findViewById(R.id.skype_id);
		// assistant_name = (EditText) findViewById(R.id.assistant_name);
		// assistant_phone = (EditText) findViewById(R.id.assistant_phone);
		// street = (EditText) findViewById(R.id.street);
		// city = (EditText) findViewById(R.id.city);
		// state = (EditText) findViewById(R.id.state);
		// zip = (EditText) findViewById(R.id.zip);
		// country = (EditText) findViewById(R.id.country);
		// description = (EditText) findViewById(R.id.description);
		save_divider = (View) findViewById(R.id.save_divider);

		mEmail = (ImageView) findViewById(R.id.email);
		mCall = (ImageView) findViewById(R.id.call);
		mSms = (ImageView) findViewById(R.id.sms);

		mDelete = (ImageView) findViewById(R.id.delete);
		mDot1 = (ImageView) findViewById(R.id.dot);
		mDot2 = (ImageView) findViewById(R.id.dot2);

		mNavigationLayout = (LinearLayout) findViewById(R.id.navigationLayout);
		mViewPager = (ViewPager) findViewById(R.id.pager);


		
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					mDot1.setImageResource(R.drawable.ic_dot);
					mDot2.setImageResource(R.drawable.ic_un_select_dot);
					break;
				case 1:
					mDot1.setImageResource(R.drawable.ic_un_select_dot);
					mDot2.setImageResource(R.drawable.ic_dot);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("CONTACT_URI")) {
				strContactUri = bundle.getString("CONTACT_URI");
				DeviceUtility.log(TAG, "strContactUri: " + strContactUri);
				hLoad.put("ACTION", "LOAD");
				hLoad.put("ID", strContactUri);
			} else if (bundle.containsKey("ID")) {
				strContactUri = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strContactUri);
				hLoad.put("ACTION", "EDIT");
				hLoad.put("ID", strContactUri);
			}

			if (bundle.containsKey("CONTACT_ID")) {
				strContactId = bundle.getString("CONTACT_ID");
				DeviceUtility.log(TAG, "EDIT OPP for CONTACT_ID: "
						+ strContactId);
			}

			if (bundle.containsKey("ID")) {
				strId = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strId);
			}

			if (bundle.containsKey("VIEW")) {
				bView = bundle.getBoolean("VIEW");
			}

			if (bundle.containsKey("ACTIVE")) {
				String strActive = bundle.getString("ACTIVE");
				if (strActive.equals("1")) {
					bActive = false;
				}
			}
			
			nameTo = bundle.getString("NAME_TO");
			lastName = bundle.getString("lastName");
			bSetNameTo = bundle.getBoolean("setNameTo", false);
			bIsContact = bundle.getBoolean(Constants.IS_CONTACT_NAME_TO, false);
		}

		// birthday.setFocusable(false);

		// birthday.setOnTouchListener(showDatePicker2);
		if (bView) {
			// doLockField(true);
			if (bActive) {
				save.setImageResource(R.drawable.ic_icon_edit);
				save.setOnClickListener(doChangeToSave);
			} else {
				// hide the save button.
				save_divider.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
				mNavigationLayout.setVisibility(View.GONE);
			}
		} else {
			save.setOnClickListener(saveContact);
			mNavigationLayout.setVisibility(View.GONE);

			// birthday.setOnClickListener(showDatePicker);
			// birthday_clear.setOnClickListener(clearBirthday);
		}
		// LoadContact task = new LoadContact();
		// task.execute(hLoad);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.history).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent contactInfo = new Intent(ContactInfoTab.this,
						ActivityHistory.class);
				contactInfo.putExtra("ID", strId);
				contactInfo.putExtra("TYPE", Constants.PERSON_TYPE_CONTACT);
				ContactInfoTab.this.startActivity(contactInfo);
			}
		});

		findViewById(R.id.opportunity).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent iOpportunity = new Intent(ContactInfoTab.this,
								OpportunityList.class);
						iOpportunity.putExtra("ID", strId);
						iOpportunity.putExtra("CONTACT_ID", strContactId);
						ContactInfoTab.this.startActivity(iOpportunity);
					}
				});

		if (!Utility.getConfigByText(context, Constants.DELETE_CONTACT).equals(
				"0")) {
			mDelete.setVisibility(View.VISIBLE);
		} else {
			mDelete.setVisibility(View.GONE);
		}

		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GuiUtility.alert(ContactInfoTab.this, "",
						getString(R.string.delete_contact), Gravity.CENTER,
						getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}, getString(R.string.delete),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Dialog loading = GuiUtility.getLoadingDialog(
										ContactInfoTab.this, false,
										getString(R.string.processing));
								DatabaseUtility
										.getDatabaseHandler(ContactInfoTab.this);
								Contact contact = new Contact(
										Constants.DBHANDLER);
								contact.delete(strId);
								loading.dismiss();
								// back to contact list.
								ContactInfoTab.this.finish();
							}
						});
			}
		});

		findViewById(R.id.createEvent).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent event = new Intent(ContactInfoTab.this,
								EventInfo.class);
						event.putExtra("calendarAdd", false);
						event.putExtra("NAME_TO", nameTo);
						event.putExtra("EDIT_NAME_TO", false);
						event.putExtra("OTHER_PAGE_TO", true);
						ContactInfoTab.this.startActivity(event);
					}
				});

		findViewById(R.id.createTask).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent event = new Intent(ContactInfoTab.this, TaskInfo.class);
				event.putExtra("NAME_TO", nameTo);
				event.putExtra("EDIT_NAME_TO", false);
				event.putExtra("OTHER_PAGE_TO", true);
				ContactInfoTab.this.startActivity(event);
			}
		});
		
	}

	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			switch (i) {
			case 0:
				fragment = new ContactInfoBesiaFragment();
				break;
			case 1:
				fragment = new ContactInfoOtherFragment();
				break;
			}
			Bundle args = new Bundle();
			args.putString("ID", strId);
			args.putBoolean("VIEW", true);
			args.putString("ACTIVE", strActive);
			args.putBoolean("setNameTo", bSetNameTo);
			args.putBoolean(Constants.IS_CONTACT_NAME_TO, bIsContact);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "OBJECT " + (position + 1);
		}
	}

	private OnClickListener doChangeToSave = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// doLockField(false);
			save.setImageResource(R.drawable.ic_icon_save);
			save.setOnClickListener(saveContact);
			
			

			mNavigationLayout.setVisibility(View.GONE);

			Intent intent = new Intent();
			intent.setAction("editPage");
			sendBroadcast(intent);

			// birthday.setOnClickListener(showDatePicker);
			// birthday_clear.setOnClickListener(clearBirthday);
		}
	};

	private void doShowSelectedSector(String strField) {
		basic.setBackgroundResource(R.drawable.blue_menu_left);
		others.setBackgroundResource(R.drawable.blue_menu_right);
		if (strField.equalsIgnoreCase("BASIC")) {
			basic.setBackgroundResource(R.drawable.blue_menu_selected_left);
			basic.setOnClickListener(null);
			others.setOnClickListener(showOthersField);
		}

		if (strField.equalsIgnoreCase("OTHERS")) {
			others.setBackgroundResource(R.drawable.blue_menu_selected_right);
			others.setOnClickListener(null);
			basic.setOnClickListener(showBasicField);
		}
	}
	
	private static String formatCallNumber(String phoneNo){
		phoneNo = phoneNo.toUpperCase();
		if(phoneNo.contains("E")){
			phoneNo = phoneNo.substring(0, phoneNo.indexOf('E'));
		}
		phoneNo = phoneNo.replaceAll("\\D", "");
		if (phoneNo.charAt(0) == '0')		
			phoneNo = "+6" + phoneNo;
		else
			phoneNo = "+" + phoneNo;
		
		return phoneNo;
	}
	
	// @Override
	// protected Dialog onCreateDialog(int id) {
	// switch (id) {
	// case MY_DATE_DIALOG_ID:
	// DatePickerDialog dateDlg = new DatePickerDialog(this,
	// new DatePickerDialog.OnDateSetListener() {
	// public void onDateSet(DatePicker view, int year, int monthOfYear, int
	// dayOfMonth) {
	// Time chosenDate = new Time();
	// chosenDate.set(dayOfMonth, monthOfYear, year);
	// long dtDob = chosenDate.toMillis(true);
	// CharSequence strDate = DateFormat.format(Constants.DATE_FORMAT, dtDob);
	// birthday.setText(strDate);
	// }}, 2011,0, 1);
	//
	// dateDlg.setMessage(getString(R.string.birthday));
	// return dateDlg;
	// }
	// return super.onCreateDialog(id);
	// }

	// @Override
	// protected void onPrepareDialog(int id, Dialog dialog) {
	// super.onPrepareDialog(id, dialog);
	// switch (id) {
	// case MY_DATE_DIALOG_ID:
	// DatePickerDialog dateDlg = (DatePickerDialog) dialog;
	// int iDay,iMonth,iYear;
	// Calendar cal = Calendar.getInstance();
	// iDay = cal.get(Calendar.DAY_OF_MONTH);
	// iMonth = cal.get(Calendar.MONTH);
	// iYear = cal.get(Calendar.YEAR);
	// if (birthday.getText().toString().length() == 10) {
	// iDay = Integer.parseInt(birthday.getText().toString().substring(0, 2));
	// iMonth = Integer.parseInt(birthday.getText().toString().substring(3, 5))
	// - 1;
	// iYear = Integer.parseInt(birthday.getText().toString().substring(6, 10));
	//
	// DeviceUtility.log(TAG, "iDay: "+iDay+" iMonth: "+iMonth+" iYear: " +
	// iYear);
	// }
	// dateDlg.updateDate(iYear, iMonth, iDay);
	// break;
	// }
	// }

	private OnClickListener showBasicField = new OnClickListener() {

		@Override
		public void onClick(View v) {
			basic_field.setVisibility(View.VISIBLE);
			others_field.setVisibility(View.GONE);
			scroll_view.fullScroll(ScrollView.FOCUS_UP);
			strSector = "BASIC";
			doShowSelectedSector(strSector);
		}
	};

	private OnClickListener showOthersField = new OnClickListener() {

		@Override
		public void onClick(View v) {
			others_field.setVisibility(View.VISIBLE);
			basic_field.setVisibility(View.GONE);
			scroll_view.fullScroll(ScrollView.FOCUS_UP);
			strSector = "OTHERS";
			doShowSelectedSector(strSector);
		}
	};

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

	// private OnClickListener clearBirthday = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// birthday.setText("");
	// }
	// };

	private OnClickListener saveContact = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			v.setClickable(false);
			
			Intent intent = new Intent();
			intent.setAction("save");
			sendBroadcast(intent);
		}
	};

	// public void doLockField(boolean bLock) {
	// DeviceUtility.log(TAG, "doLockField("+bLock+")");
	// boolean bEnabled = true;
	// boolean bFocusabled = true;
	// if (bLock == true) {
	// bEnabled = false;
	// bFocusabled = false;
	// }
	//
	// //enabled/disabled edit.
	// prefixSpinner.setEnabled(bEnabled);
	// first_name.setEnabled(bEnabled);
	// last_name.setEnabled(bEnabled);
	// company_name.setEnabled(bEnabled);
	// job_title.setEnabled(bEnabled);
	// department.setEnabled(bEnabled);
	// mobile.setEnabled(bEnabled);
	// work_fax.setEnabled(bEnabled);
	// work_phone.setEnabled(bEnabled);
	// other_phone.setEnabled(bEnabled);
	// email.setEnabled(bEnabled);
	// home_phone.setEnabled(bEnabled);
	// // birthday.setEnabled(bEnabled);
	// email2.setEnabled(bEnabled);
	// email3.setEnabled(bEnabled);
	// skype_id.setEnabled(bEnabled);
	// assistant_name.setEnabled(bEnabled);
	// assistant_phone.setEnabled(bEnabled);
	// street.setEnabled(bEnabled);
	// city.setEnabled(bEnabled);
	// state.setEnabled(bEnabled);
	// zip.setEnabled(bEnabled);
	// country.setEnabled(bEnabled);
	// description.setEnabled(bEnabled);
	//
	// //enabled/disabled focus
	// prefixSpinner.setFocusable(bFocusabled);
	// first_name.setFocusable(bFocusabled);
	// last_name.setFocusable(bFocusabled);
	// company_name.setFocusable(bFocusabled);
	// job_title.setFocusable(bFocusabled);
	// department.setFocusable(bFocusabled);
	// mobile.setFocusable(bFocusabled);
	// work_fax.setFocusable(bFocusabled);
	// work_phone.setFocusable(bFocusabled);
	// other_phone.setFocusable(bFocusabled);
	// email.setFocusable(bFocusabled);
	// home_phone.setFocusable(bFocusabled);
	// //birthday.setFocusable(bFocusabled);
	// email2.setFocusable(bFocusabled);
	// email3.setFocusable(bFocusabled);
	// skype_id.setFocusable(bFocusabled);
	// assistant_name.setFocusable(bFocusabled);
	// assistant_phone.setFocusable(bFocusabled);
	// street.setFocusable(bFocusabled);
	// city.setFocusable(bFocusabled);
	// state.setFocusable(bFocusabled);
	// zip.setFocusable(bFocusabled);
	// country.setFocusable(bFocusabled);
	// description.setFocusable(bFocusabled);
	//
	// if (bFocusabled) {
	// //prefixSpinner.setFocusableInTouchMode(bFocusabled);
	// first_name.setFocusableInTouchMode(bFocusabled);
	// last_name.setFocusableInTouchMode(bFocusabled);
	// company_name.setFocusableInTouchMode(bFocusabled);
	// job_title.setFocusableInTouchMode(bFocusabled);
	// department.setFocusableInTouchMode(bFocusabled);
	// mobile.setFocusableInTouchMode(bFocusabled);
	// work_fax.setFocusableInTouchMode(bFocusabled);
	// work_phone.setFocusableInTouchMode(bFocusabled);
	// other_phone.setFocusableInTouchMode(bFocusabled);
	// email.setFocusableInTouchMode(bFocusabled);
	// home_phone.setFocusableInTouchMode(bFocusabled);
	// //birthday.setFocusableInTouchMode(bFocusabled);
	// email2.setFocusableInTouchMode(bFocusabled);
	// email3.setFocusableInTouchMode(bFocusabled);
	// skype_id.setFocusableInTouchMode(bFocusabled);
	// assistant_name.setFocusableInTouchMode(bFocusabled);
	// assistant_phone.setFocusableInTouchMode(bFocusabled);
	// street.setFocusableInTouchMode(bFocusabled);
	// city.setFocusableInTouchMode(bFocusabled);
	// state.setFocusableInTouchMode(bFocusabled);
	// zip.setFocusableInTouchMode(bFocusabled);
	// country.setFocusableInTouchMode(bFocusabled);
	// description.setFocusableInTouchMode(bFocusabled);
	// }
	// }

	private class SaveContact extends AsyncTask<ContactDetail, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactInfoTab.this,
					false, getString(R.string.processing));
		}

		@Override
		protected Boolean doInBackground(ContactDetail... params) {
			ContactDetail contactDetail = params[0];
			contactDetail.setIsUpdate("false");
			contactDetail.setUpdateGpsLocation("true");
			DatabaseUtility.getDatabaseHandler(ContactInfoTab.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			if (contactDetail.getInternalNumber() == null) {
				long lRow = contact.insert(contactDetail);
				if (lRow == -1) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			} else {
				int iRowEffected = contact.update(contactDetail);
				if (iRowEffected == 0) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			if (result.booleanValue() == true) {
				// GuiUtility.alert(ContactInfoTab.this,
				// getString(R.string.success_store_contact_title),
				// getString(R.string.success_store_contact_desc),
				// getString(R.string.ok));
				GuiUtility.alert(ContactInfoTab.this,
						getString(R.string.success_store_contact_title),
						getString(R.string.success_store_contact_desc),
						Gravity.CENTER, getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// Intent iContact = new
								// Intent(ContactInfoTab.this,
								// ContactList.class);
								// ContactInfoTab.this.startActivity(iContact);
								ContactInfoTab.this.finish();
							}
						}, "", null);
			} else {
				GuiUtility.alert(ContactInfoTab.this,
						getString(R.string.fail_store_contact_title),
						getString(R.string.fail_store_contact_desc),
						getString(R.string.ok));
			}
		}
	}

	private class LoadContact extends AsyncTask<String, Void, ContactDetail> {

		@Override
		protected ContactDetail doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(ContactInfoTab.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			return contact.getContactDetailById(params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactInfoTab.this,
					false, null);
		}

		@Override
		protected void onPostExecute(ContactDetail result) {
			super.onPostExecute(result);
			aEmailList = new ArrayList<EmailDetail>();
			aContactNumberList = new ArrayList<PhoneDetail>();

			contact = result;
			// first_name.setText(result.getFirstName());
			// last_name.setText(result.getLastName());
			// company.setText(result.getCompanyName());
			//
			// if (result.getPhoto() != null) {
			// StorageUtility storage = new StorageUtility(ContactInfoTab.this,
			// Constants.CACHE_FOLDER);
			// if (mBitmap != null) {
			// mBitmap.recycle();
			// mBitmap = null;
			// }
			// mBitmap = storage.doReadImage(result.getPhoto());
			// if (mBitmap != null) {
			// photo.setImageBitmap(mBitmap);
			// } else {
			// photo.setImageResource(R.drawable.contacts);
			// }
			// }
			//
			if (result.getEmail1() != null && result.getEmail1().length() > 0) {
				aEmailList.add(new EmailDetail(result.getEmail1(),
						getString(R.string.email)));
			}

			if (result.getEmail2() != null && result.getEmail2().length() > 0) {
				aEmailList.add(new EmailDetail(result.getEmail2(),
						getString(R.string.email2)));
			}

			if (result.getEmail3() != null && result.getEmail3().length() > 0) {
				aEmailList.add(new EmailDetail(result.getEmail3(),
						getString(R.string.email3)));
			}

			if (result.getMobile() != null && result.getMobile().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getMobile(),
						getString(R.string.mobile)));
				mSms.setVisibility(View.VISIBLE);
				strSms = result.getMobile();
				mSms.setOnClickListener(doComposeSms);
				
				
			}else{
				mSms.setVisibility(View.GONE);
			}

			if (result.getWorkPhone() != null
					&& result.getWorkPhone().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getWorkPhone(),
						getString(R.string.work_phone)));
			}

			if (result.getHomePhone() != null
					&& result.getHomePhone().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getHomePhone(),
						getString(R.string.home_phone)));
			}

			if (result.getOtherPhone() != null
					&& result.getOtherPhone().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getOtherPhone(),
						getString(R.string.other_phone)));
			}
			
			if (aContactNumberList.size() == 0) {
				mCall.setVisibility(View.GONE);
				// mCall.setBackgroundResource(R.drawable.disabled_button);
				mCall.setOnClickListener(null);
				mSms.setOnClickListener(null);
			} else {
				mCall.setVisibility(View.VISIBLE);
				// mCall.setBackgroundResource(R.drawable.orange_button);
				// mSms.setBackgroundResource(R.drawable.orange_button);
				if (aContactNumberList.size() != 0) {
					mCall.setVisibility(View.VISIBLE);
					mCall.setOnClickListener(doCall);
		
				} else {
					mCall.setVisibility(View.VISIBLE);
					mCall.setOnClickListener(doShowContextMenu);
					mSms.setOnClickListener(doShowContextMenu);
				}
			}

			if (aEmailList.size() == 0) {
				mEmail.setVisibility(View.GONE);
				// mEmail.setBackgroundResource(R.drawable.disabled_button);
				mEmail.setOnClickListener(null);
			} else {
				mEmail.setVisibility(View.VISIBLE);
				// mEmail.setBackgroundResource(R.drawable.orange_button);
				if (aEmailList.size() != 0) {
					mEmail.setOnClickListener(doComposeEmail);
				} else {
					mEmail.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							registerForContextMenu(v);
							openContextMenu(v);
							unregisterForContextMenu(v);
						}
					});
				}
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

		private OnClickListener doShowContextMenu = new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				System.out.println("yyyi");
				registerForContextMenu(v);
				openContextMenu(v);
				unregisterForContextMenu(v);
			}
		};

		private OnClickListener doComposeEmail = new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String str[]=new String[aEmailList.size()];
				for (int i = 0; i < str.length; i++) {
					str[i] = aEmailList.get(i).getEmailAddress();
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfoTab.this);
			    builder.setTitle(R.string.email)
			           .setItems(str, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	    
			   				doLogActivity(Constants.ACTION_EMAIL, aEmailList.get(which)
			   						.getEmailAddress());
			   				Intent emailIntent = new Intent(
			   						android.content.Intent.ACTION_SEND);
			   				emailIntent.setType("plain/text");
			   				String sEmailList[] = { aEmailList.get(which).getEmailAddress() };
			   				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
			   						sEmailList);
			   				emailIntent.setType("text/plain");
			   				startActivity(Intent.createChooser(emailIntent,
			   						getString(R.string.send_email_in)));
			               }
			    });
			    builder.create();
			    builder.show();
			
			}
		};

		private OnClickListener doComposeSms = new OnClickListener() {

			@Override
			public void onClick(View v) {
		
				
//				String str[]=new String[aContactNumberList.size()];
//				for (int i = 0; i < str.length; i++) {
//					str[i] = aContactNumberList.get(i).getPhoneNo();
//				}
//				
//				AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfoTab.this);
//			    builder.setTitle(R.string.sms)
//			           .setItems(str, new DialogInterface.OnClickListener() {
//			               public void onClick(DialogInterface dialog, int which) {

			           		doLogActivity(Constants.ACTION_SMS, strSms);
							Intent smsIntent = new Intent(
									android.content.Intent.ACTION_VIEW);
							smsIntent.setData(Uri.parse("sms:"
									+ strSms));
							startActivity(smsIntent);
//							}
//			    });
//			    builder.create();
//			    builder.show();
			}
		};
		
		

		private OnClickListener doCall = new OnClickListener() {

			@Override
			public void onClick(View v) {
//				System.out.println("call = "+aContactNumberList.get(0).getPhoneNo());
				
				String str[]=new String[aContactNumberList.size()];
				for (int i = 0; i < str.length; i++) {
					str[i] = aContactNumberList.get(i).getPhoneNo();
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfoTab.this);
			    builder.setTitle(R.string.call)
			           .setItems(str, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	String phoneNo = formatCallNumber(aContactNumberList.get(which).getPhoneNo());
			            	System.out.println("call = "+phoneNo);
			       			doLogActivity(Constants.ACTION_CALL, phoneNo);
							Intent callIntent = new Intent(
									android.content.Intent.ACTION_CALL, Uri.parse("tel:"+ phoneNo));
							startActivity(callIntent);
			           }
			    });
			    builder.create();
			    builder.show();
			}
		};
	}

	private void doLogActivity(String strActionType, String strActionInfo) {
		DatabaseUtility.getDatabaseHandler(ContactInfoTab.this);
		String strOwner = Utility.getConfigByText(ContactInfoTab.this,
				"USER_EMAIL");
		ActivityLog log = new ActivityLog(Constants.DBHANDLER);

		ActivitiesLogDetail detail = new ActivitiesLogDetail();
		detail.setContactId(contact.getContactId());
		detail.setContactNum(contact.getInternalNumber());
		detail.setActType(strActionType);
		detail.setFirstName(contact.getFirstName());
		detail.setLastName(contact.getLastName());
		detail.setPersonType(Constants.PERSON_TYPE_CONTACT);
		detail.setIsUpdate("false");
		detail.setOwner(strOwner);
		detail.setUserStamp(strOwner);
		if (strActionType.equalsIgnoreCase(Constants.ACTION_EMAIL)) {
			detail.setEmail(strActionInfo);
		} else {
			detail.setMobile(strActionInfo);
		}
		log.insert(detail);
	}
}
