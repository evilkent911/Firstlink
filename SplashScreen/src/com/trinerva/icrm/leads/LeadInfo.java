package com.trinerva.icrm.leads;

import java.util.ArrayList;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.contacts.ActivityHistory;
import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.event.EventInfo;
import com.trinerva.icrm.object.ActivitiesLogDetail;
import com.trinerva.icrm.object.EmailDetail;
import com.trinerva.icrm.object.LeadDetail;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

public class LeadInfo extends FragmentActivity {
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
				mModifiedDate
						.setText(getString(R.string.last_modified_date, Utility
								.convertLeadTime(intent.getStringExtra("date"))));
			} else if (intent.getAction().equals("saveTrue")) {
				save.setClickable(true);
			}
		}
	};

	private boolean bReload = true;
	private static String TAG = "LeadInfo";
	private EditText first_name, last_name, company_name, job_title, mobile,
			work_fax, work_phone, website, email, email2, email3, skype_id,
			no_of_employee, annual_revenue, street, city, state, zip, country,
			description;
	private Spinner prefix, lead_source, lead_status, attitude, industry;
	private TextView basic, others;
	ImageView save;
	private ArrayList<MasterInfo> aPrefix, aLeadStatus, aLeadSource, aAttitude,
			aLeadIndustry;
	private TableLayout others_field;
	private LinearLayout basic_field;
	private ScrollView scroll_view;
	private View save_divider;
	private String strSector = "BASIC";
	private String strContactUri;
	// private HashMap<String, String> hLoad = new HashMap<String, String>();
	private Dialog loadingDialog;
	private LeadDetail lead = new LeadDetail();
	private boolean bView = false;
	private boolean bActive = true;
	String strActive;
	ViewPager mViewPager;
	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	private String strId = null;
	LinearLayout mNavigationLayout;
	ImageView mEmail, mCall, mSms, mDelete;
	private static final int EMAIL_GROUP = 0;
	private static final int SMS_GROUP = 1;
	private static final int CALL_GROUP = 2;
	private static final int PHOTO_GROUP = 3;

	private ArrayList<PhoneDetail> aContactNumberList;
	private ArrayList<EmailDetail> aEmailList;
	private ArrayList<String> aPhotoSelection;
	TextView mModifiedDate;
	Context context;
	ImageView mDot1, mDot2;
	String nameTo;
	String strSms;
	public static String lastName;
	boolean bSetNameTo;
	boolean bIsContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lead_info_tab);

		context = this;
		// get extra.
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("CONTACT_URI")) {
				strContactUri = bundle.getString("CONTACT_URI");
				DeviceUtility.log(TAG, "strContactUri: " + strContactUri);
				// hLoad.put("ACTION", "LOAD");
				// hLoad.put("ID", strContactUri);
			} else if (bundle.containsKey("ID")) {
				strContactUri = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strContactUri);
				// hLoad.put("ACTION", "EDIT");
				// hLoad.put("ID", strContactUri);
			}

			if (bundle.containsKey("ID")) {
				strId = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strId);
			}

			if (bundle.containsKey("VIEW")) {
				bView = bundle.getBoolean("VIEW");
			}

			if (bundle.containsKey("ACTIVE")) {
				strActive = bundle.getString("ACTIVE");
				if (strActive.equals("1")) {
					bActive = false;
				}
			}
			
			
			nameTo = bundle.getString("NAME_TO");
			lastName = bundle.getString("lastName");
			bSetNameTo = bundle.getBoolean("setNameTo", false);
			bIsContact = bundle.getBoolean(Constants.IS_CONTACT_NAME_TO, false);
			System.out.println("leadInfo - "+lastName);
		}

		// prefix = (Spinner) findViewById(R.id.prefix);
		// first_name = (EditText) findViewById(R.id.first_name);
		// last_name = (EditText) findViewById(R.id.last_name);
		// company_name = (EditText) findViewById(R.id.company_name);
		// job_title = (EditText) findViewById(R.id.job_title);
		// industry = (Spinner) findViewById(R.id.industry);
		// mobile = (EditText) findViewById(R.id.mobile);
		// work_fax = (EditText) findViewById(R.id.work_fax);
		// work_phone = (EditText) findViewById(R.id.work_phone);
		// lead_source = (Spinner) findViewById(R.id.lead_source);
		// lead_status = (Spinner) findViewById(R.id.lead_status);
		// attitude = (Spinner) findViewById(R.id.attitude);
		// website = (EditText) findViewById(R.id.website);
		// email = (EditText) findViewById(R.id.email);
		// email2 = (EditText) findViewById(R.id.email2);
		// email3 = (EditText) findViewById(R.id.email3);
		// skype_id = (EditText) findViewById(R.id.skype_id);
		// no_of_employee = (EditText) findViewById(R.id.no_of_employee);
		// annual_revenue = (EditText) findViewById(R.id.annual_revenue);
		// street = (EditText) findViewById(R.id.street);
		// city = (EditText) findViewById(R.id.city);
		// state = (EditText) findViewById(R.id.state);
		// zip = (EditText) findViewById(R.id.zip);
		// country = (EditText) findViewById(R.id.country);
		// description = (EditText) findViewById(R.id.description);
		save = (ImageView) findViewById(R.id.save);
		mEmail = (ImageView) findViewById(R.id.email);
		mCall = (ImageView) findViewById(R.id.call2);
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
		// others_field = (TableLayout) findViewById(R.id.others_field);
		// basic_field = (LinearLayout) findViewById(R.id.basic_field);
		// basic = (TextView) findViewById(R.id.basic);
		// others = (TextView) findViewById(R.id.others);
		// scroll_view = (ScrollView) findViewById(R.id.scroll_view);
		save_divider = (View) findViewById(R.id.save_divider);
		mModifiedDate = (TextView) findViewById(R.id.modified);
		//
		// basic.setOnClickListener(showBasicField);
		// others.setOnClickListener(showOthersField);
		if (bView) {
			// doLockField(true);
			if (bActive) {
				save.setImageResource(R.drawable.ic_icon_edit);
				save.setOnClickListener(doChangeToSave);
			} else {
				save.setVisibility(View.GONE);
				save_divider.setVisibility(View.GONE);
				mNavigationLayout.setVisibility(View.GONE);
			}
		} else {
			mNavigationLayout.setVisibility(View.GONE);
			save.setOnClickListener(saveLead);
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
				Intent contactInfo = new Intent(LeadInfo.this,
						ActivityHistory.class);
				contactInfo.putExtra("ID", strId);
				contactInfo.putExtra("TYPE", Constants.PERSON_TYPE_LEAD);
				LeadInfo.this.startActivity(contactInfo);
			}
		});

		if (!Utility.getConfigByText(context, Constants.DELETE_LEAD)
				.equals("0")) {
			mDelete.setVisibility(View.VISIBLE);
		} else {
			mDelete.setVisibility(View.GONE);
		}

		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GuiUtility.alert(LeadInfo.this, "",
						getString(R.string.delete_lead), Gravity.CENTER,
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
										LeadInfo.this, false,
										getString(R.string.processing));
								DatabaseUtility
										.getDatabaseHandler(LeadInfo.this);
								Lead lead = new Lead(Constants.DBHANDLER);
								lead.delete(strId);
								loading.dismiss();
								// back to contact list.
								LeadInfo.this.finish();
							}
						});

			}
		});
		

		findViewById(R.id.createEvent).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent event = new Intent(LeadInfo.this,
								EventInfo.class);
						event.putExtra("calendarAdd", false);
						event.putExtra("NAME_TO", nameTo);
						event.putExtra("EDIT_NAME_TO", false);
						event.putExtra("OTHER_PAGE_TO", true);
						LeadInfo.this.startActivity(event);
					}
				});

		findViewById(R.id.createTask).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent event = new Intent(LeadInfo.this, TaskInfo.class);
				event.putExtra("NAME_TO", nameTo);
				event.putExtra("EDIT_NAME_TO", false);
				event.putExtra("OTHER_PAGE_TO", true);
				LeadInfo.this.startActivity(event);
			}
		});
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
	private static String formatChangeToSaveNumber(String phoneNo){
		phoneNo = phoneNo.replaceAll("[\\s\\-()]", ""); 
		phoneNo = "+" + phoneNo;
		return phoneNo;
	}


	private void doCreateMenu(Menu menu, int iGroupId) {
		switch (iGroupId) {
		case EMAIL_GROUP:
			int iEmail = aEmailList.size();
			for (int i = 0; i < iEmail; i++) {
				menu.add(iGroupId, i, i, aEmailList.get(i).toString());
			}
			break;
		case SMS_GROUP:
		case CALL_GROUP:
			int iContact = aContactNumberList.size();
			for (int i = 0; i < iContact; i++) {
				menu.add(iGroupId, i, i, aContactNumberList.get(i).toString());
			}
			break;
		case PHOTO_GROUP:
			int iPhotoType = aPhotoSelection.size();
			for (int i = 0; i < iPhotoType; i++) {
				menu.add(iGroupId, i, i, aPhotoSelection.get(i).toString());
			}
			break;
		}
	}

	private OnClickListener doComposeEmail = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLogActivity(Constants.ACTION_EMAIL, aEmailList.get(0)
					.getEmailAddress());
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			String sEmailList[] = { aEmailList.get(0).getEmailAddress() };
			emailIntent
					.putExtra(android.content.Intent.EXTRA_EMAIL, sEmailList);
			emailIntent.setType("text/plain");
			startActivity(Intent.createChooser(emailIntent,
					getString(R.string.send_email_in)));
		}
	};

	private OnClickListener doComposeSms = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLogActivity(Constants.ACTION_SMS, aContactNumberList.get(0)
					.getPhoneNo());
			Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
			smsIntent.setData(Uri.parse("sms:"
					+ aContactNumberList.get(0).getPhoneNo()));
			startActivity(smsIntent);
		}
	};

	private OnClickListener doCall = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLogActivity(Constants.ACTION_CALL, aContactNumberList.get(0)
					.getPhoneNo());
			Intent callIntent = new Intent(android.content.Intent.ACTION_CALL,
					Uri.parse("tel:" + aContactNumberList.get(0).getPhoneNo()));
			startActivity(callIntent);
		}
	};

	private void doLogActivity(String strActionType, String strActionInfo) {
		DatabaseUtility.getDatabaseHandler(LeadInfo.this);
		String strOwner = Utility.getConfigByText(LeadInfo.this, "USER_EMAIL");
		ActivityLog log = new ActivityLog(Constants.DBHANDLER);

		ActivitiesLogDetail detail = new ActivitiesLogDetail();
		detail.setContactId(lead.getLeadId());
		detail.setContactNum(lead.getInternalNum());
		detail.setActType(strActionType);
		detail.setFirstName(lead.getFirstName());
		detail.setLastName(lead.getLastName());
		detail.setPersonType(Constants.PERSON_TYPE_LEAD);
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

	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			Bundle args = new Bundle();
			args.putString("ID", strId);
			args.putBoolean("VIEW", true);
			args.putString("ACTIVE", strActive);
			args.putBoolean("setNameTo", bSetNameTo);
			args.putBoolean(Constants.IS_CONTACT_NAME_TO, bIsContact);
	
			switch (i) {
			case 0:
				fragment = new LeadInfoBesiaFragment();
				
				break;
			case 1:
				fragment = new LeadInfoOtherFragment();
				break;
			}
	
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
			Intent intent = new Intent();
			intent.setAction("editPage");
			sendBroadcast(intent);
			save.setImageResource(R.drawable.ic_icon_save);
			save.setOnClickListener(saveLead);

			mNavigationLayout.setVisibility(View.GONE);
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

	OnClickListener saveLead = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setAction("save");
			sendBroadcast(intent);
			// String strPrefix =
			//aPrefix.get(prefix.getSelectedItemPosition()).getValue();
			// String strFirstName = first_name.getText().toString();
			// String strLastName = last_name.getText().toString();
			//String strCompanyName = company_name.getText().toString();
			// String strJobTitle = job_title.getText().toString();
			// String strIndustry =
			// aLeadIndustry.get(industry.getSelectedItemPosition()).getValue();
			/// String strMobile = mobile.getText().toString();
			// String strWorkFax = work_fax.getText().toString();
			// String strWorkPhone = work_phone.getText().toString();
			// String strLeadSource =
			// aLeadSource.get(lead_source.getSelectedItemPosition()).getValue();
			// String strLeadStatus =
			// aLeadStatus.get(lead_status.getSelectedItemPosition()).getValue();
			// String strAttitude =
			// aAttitude.get(attitude.getSelectedItemPosition()).getValue();
			// String strWebsite = website.getText().toString();
			// String strEmail = email.getText().toString();
		// String strEmail2 = email2.getText().toString();
			// String strEmail3 = email3.getText().toString();
			// String strSkypeId = skype_id.getText().toString();
			// String strNoOfEmployee = no_of_employee.getText().toString();
			// String strAnnualRevenue = annual_revenue.getText().toString();
			// String strStreet = street.getText().toString();
			 //String strCity = city.getText().toString();
			// String strState = state.getText().toString();
			// String strZip = zip.getText().toString();
			// String strCountry = country.getText().toString();
			// String strDescription = description.getText().toString();
			
		// DeviceUtility.log(TAG, "strNoOfEmployee: " + strNoOfEmployee);
		///	int iNoOfEmployee = 0;
			//try {
			// iNoOfEmployee =
			// Integer.parseInt(no_of_employee.getText().toString());
			// } catch (Exception e) {
			// DeviceUtility.log(TAG, "no of employee can't parse to integer. "
			// + no_of_employee.getText().toString());
			// }
			
			// if (strLastName.length() == 0) {
			// GuiUtility.alert(LeadInfo.this, "",
		//	 getString(R.string.last_name_is_required),
			// getString(R.string.ok));
			// doLockField(false);
			// last_name.requestFocus();
			// } else if (strCompanyName.length() == 0) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.company_name_is_required),
			// getString(R.string.ok));
			// doLockField(false);
			// company_name.requestFocus();
			// } else if (strIndustry.length() == 0) {
			//i/ GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.industry_is_required),
			// getString(R.string.ok));
			// doLockField(false);
			//industry.requestFocus();
			// } else if (strLeadSource.length() == 0) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.lead_source_is_required),
			// getString(R.string.ok));
			 //doLockField(false);
			// lead_source.requestFocus();
			// } else if (strLeadStatus.length() == 0) {
			 //GuiUtility.alert(LeadInfo.this, "",
			 //getString(R.string.lead_status_is_required),
			// getString(R.string.ok));
			// doLockField(false);
			// lead_status.requestFocus();
			// } else if (strEmail.length() > 0 &&
			// !Utility.isValidEmail(strEmail)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.email_error_msg), getString(R.string.ok));
			// doLockField(false);
			// email.requestFocus();
			// } else if (strEmail2.length() > 0 &&
			// !Utility.isValidEmail(strEmail2)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.email_error_msg), getString(R.string.ok));
			// doLockField(false);
			// email2.requestFocus();
			// } else if (strEmail3.length() > 0 &&
			// !Utility.isValidEmail(strEmail3)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.email_error_msg), getString(R.string.ok));
			// doLockField(false);
			// email3.requestFocus();
			// } else if (strMobile.length() > 0 &&
			// !Utility.isValidPhone(strMobile)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.phone_error_msg), getString(R.string.ok));
			// doLockField(false);
			// mobile.requestFocus();
			// } else if (strWorkFax.length() > 0 &&
			// !Utility.isValidPhone(strWorkFax)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.phone_error_msg), getString(R.string.ok));
			// doLockField(false);
			// work_fax.requestFocus();
			// } else if (strWorkPhone.length() > 0 &&
			// !Utility.isValidPhone(strWorkPhone)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.phone_error_msg), getString(R.string.ok));
			// doLockField(false);
			// work_phone.requestFocus();
			// } else if (strSkypeId.length() > 0 &&
			// Utility.isContainWhitespace(strSkypeId)) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.skype_error_msg), getString(R.string.ok));
			// doLockField(false);
			// skype_id.requestFocus();
			// } else if (strNoOfEmployee.length() > 0 && iNoOfEmployee <= 0) {
			// GuiUtility.alert(LeadInfo.this, "",
			// getString(R.string.no_of_employee_error_msg),
			// getString(R.string.ok));
			// doLockField(false);
			// no_of_employee.requestFocus();
			// } else {
			// // if (hLoad.containsKey("ACTION")) {
			// // if (hLoad.get("ACTION").equalsIgnoreCase("EDIT")) {
			// // lead.setInternalNum(hLoad.get("ID"));
			// // }
			// // }
			//
			// lead.setPrefix(strPrefix);
			//
			// if (strFirstName.length() == 0) {
			// lead.setFirstName(null);
			// } else {
			// lead.setFirstName(strFirstName);
			// }
			//
			// lead.setLastName(strLastName);
			// lead.setCompanyName(strCompanyName);
			//
			// if (strJobTitle.length() == 0) {
			// lead.setJobTitle(null);
			// } else {
			// lead.setJobTitle(strJobTitle);
			// }
			//
			// lead.setIndustry(strIndustry);
			//
			// if (strMobile.length() == 0) {
			// lead.setMobile(null);
			// } else {
			// lead.setMobile(strMobile);
			// }
			//
			// if (strWorkFax.length() == 0) {
			// lead.setWorkFax(null);
			// } else {
			// lead.setWorkFax(strWorkFax);
			// }
			//
			// if (strWorkPhone.length() == 0) {
			// lead.setWorkPhone(null);
			// } else {
			// lead.setWorkPhone(strWorkPhone);
			// }
			//
			// lead.setLeadSource(strLeadSource);
			// lead.setLeadStatus(strLeadStatus);
			// lead.setAttitude(strAttitude);
			//
			// if (strWebsite.length() == 0) {
			// lead.setWebsite(null);
			// } else {
			// lead.setWebsite(strWebsite);
			// }
			//
			// if (strEmail.length() == 0) {
			// lead.setEmail1(null);
			// } else {
			// lead.setEmail1(strEmail);
			// }
			//
			// if (strEmail2.length() == 0) {
			// lead.setEmail2(null);
			// } else {
			// lead.setEmail2(strEmail2);
			// }
			//
			// if (strEmail3.length() == 0) {
			// lead.setEmail3(null);
			// } else {
			// lead.setEmail3(strEmail3);
			// }
			//
			// if (strSkypeId.length() == 0) {
			// lead.setSkypeId(null);
			// } else {
			// lead.setSkypeId(strSkypeId);
			// }
			//
			// if (strNoOfEmployee.length() == 0) {
			// lead.setNoOfEmployee(null);
			// } else {
			// lead.setNoOfEmployee(strNoOfEmployee);
			// }
			//
			// if (strAnnualRevenue.length() == 0) {
			// lead.setAnnualRevenue(null);
			// } else {
			// lead.setAnnualRevenue(strAnnualRevenue);
			// }
			//
			// if (strStreet.length() == 0) {
			// lead.setMailingStreet(null);
			// } else {
			// lead.setMailingStreet(strStreet);
			// }
			//
			// if (strCity.length() == 0) {
			// lead.setMailingCity(null);
			// } else {
			// lead.setMailingCity(strCity);
			// }
			//
			// if (strState.length() == 0) {
			// lead.setMailingState(null);
			// } else {
			// lead.setMailingState(strState);
			// }
			//
			// if (strZip.length() == 0) {
			// lead.setMailingZip(null);
			// } else {
			// lead.setMailingZip(strZip);
			// }
			//
			// if (strCountry.length() == 0) {
			// lead.setMailingCountry(null);
			// } else {
			// lead.setMailingCountry(strCountry);
			// }
			//
			// if (strDescription.length() == 0) {
			// lead.setDescription(null);
			// } else {
			// lead.setDescription(strDescription);
			// }
			// //get owner
			// String strOwner = Utility.getConfigByText(LeadInfo.this,
			// "USER_EMAIL");
			// if (strOwner.length() > 0) {
			// lead.setOwner(strOwner);
			// lead.setUserStamp(strOwner);
			// }
			//
			// SaveLead task = new SaveLead();
			// task.execute(lead);
			// }
		}
	};

	public void doLockField(boolean bLock) {
		DeviceUtility.log(TAG, "doLockField(" + bLock + ")");
		boolean bEnabled = true;
		boolean bFocusabled = true;
		if (bLock == true) {
			bEnabled = false;
			bFocusabled = false;
		}

		// enabled/disabled edit.
		prefix.setEnabled(bEnabled);
		first_name.setEnabled(bEnabled);
		last_name.setEnabled(bEnabled);
		company_name.setEnabled(bEnabled);
		job_title.setEnabled(bEnabled);
		industry.setEnabled(bEnabled);
		mobile.setEnabled(bEnabled);
		work_fax.setEnabled(bEnabled);
		work_phone.setEnabled(bEnabled);
		lead_source.setEnabled(bEnabled);
		lead_status.setEnabled(bEnabled);
		attitude.setEnabled(bEnabled);
		website.setEnabled(bEnabled);
		email.setEnabled(bEnabled);
		email2.setEnabled(bEnabled);
		email3.setEnabled(bEnabled);
		skype_id.setEnabled(bEnabled);
		no_of_employee.setEnabled(bEnabled);
		annual_revenue.setEnabled(bEnabled);
		street.setEnabled(bEnabled);
		city.setEnabled(bEnabled);
		state.setEnabled(bEnabled);
		zip.setEnabled(bEnabled);
		country.setEnabled(bEnabled);
		description.setEnabled(bEnabled);

		// enabled/disabled focus
		prefix.setFocusable(bFocusabled);
		first_name.setFocusable(bFocusabled);
		last_name.setFocusable(bFocusabled);
		company_name.setFocusable(bFocusabled);
		job_title.setFocusable(bFocusabled);
		industry.setFocusable(bFocusabled);
		mobile.setFocusable(bFocusabled);
		work_fax.setFocusable(bFocusabled);
		work_phone.setFocusable(bFocusabled);
		lead_source.setFocusable(bFocusabled);
		lead_status.setFocusable(bFocusabled);
		attitude.setFocusable(bFocusabled);
		website.setFocusable(bFocusabled);
		email.setFocusable(bFocusabled);
		email2.setFocusable(bFocusabled);
		email3.setFocusable(bFocusabled);
		skype_id.setFocusable(bFocusabled);
		no_of_employee.setFocusable(bFocusabled);
		annual_revenue.setFocusable(bFocusabled);
		street.setFocusable(bFocusabled);
		city.setFocusable(bFocusabled);
		state.setFocusable(bFocusabled);
		zip.setFocusable(bFocusabled);
		country.setFocusable(bFocusabled);
		description.setFocusable(bFocusabled);

		if (bFocusabled) {
			// prefix.setFocusableInTouchMode(bFocusabled);
			first_name.setFocusableInTouchMode(bFocusabled);
			last_name.setFocusableInTouchMode(bFocusabled);
			company_name.setFocusableInTouchMode(bFocusabled);
			job_title.setFocusableInTouchMode(bFocusabled);
			industry.setFocusableInTouchMode(bFocusabled);
			mobile.setFocusableInTouchMode(bFocusabled);
			work_fax.setFocusableInTouchMode(bFocusabled);
			work_phone.setFocusableInTouchMode(bFocusabled);
			// lead_source.setFocusableInTouchMode(bFocusabled);
			// lead_status.setFocusableInTouchMode(bFocusabled);
			// attitude.setFocusableInTouchMode(bFocusabled);
			website.setFocusableInTouchMode(bFocusabled);
			email.setFocusableInTouchMode(bFocusabled);
			email2.setFocusableInTouchMode(bFocusabled);
			email3.setFocusableInTouchMode(bFocusabled);
			skype_id.setFocusableInTouchMode(bFocusabled);
			no_of_employee.setFocusableInTouchMode(bFocusabled);
			annual_revenue.setFocusableInTouchMode(bFocusabled);
			street.setFocusableInTouchMode(bFocusabled);
			city.setFocusableInTouchMode(bFocusabled);
			state.setFocusableInTouchMode(bFocusabled);
			zip.setFocusableInTouchMode(bFocusabled);
			country.setFocusableInTouchMode(bFocusabled);
			description.setFocusableInTouchMode(bFocusabled);
		}
	}

	// private class LoadContact extends
	// AsyncTask<HashMap<String, String>, Void, LeadDetail> {
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// loadingDialog = GuiUtility.getLoadingDialog(LeadInfo.this, false,
	// null);
	// }
	//
	// @Override
	// protected LeadDetail doInBackground(HashMap<String, String>... params) {
	// aPrefix = (ArrayList<MasterInfo>) Utility.getMasterByType(
	// LeadInfo.this, Constants.MASTER_PREFIX);
	// aLeadStatus = (ArrayList<MasterInfo>) Utility.getMasterByType(
	// LeadInfo.this, Constants.MASTER_LEAD_STATUS);
	// aLeadSource = (ArrayList<MasterInfo>) Utility.getMasterByType(
	// LeadInfo.this, Constants.MASTER_LEAD_SOURCE);
	// aAttitude = (ArrayList<MasterInfo>) Utility.getMasterByType(
	// LeadInfo.this, Constants.MASTER_LEAD_ATTITUDE);
	// aLeadIndustry = (ArrayList<MasterInfo>) Utility.getMasterByType(
	// LeadInfo.this, Constants.MASTER_LEAD_INDUSTRY);
	// HashMap<String, String> hAction = params[0];
	// LeadDetail oLeadDetail = new LeadDetail();
	//
	// if (hAction.containsKey("ID") && hAction.containsKey("ACTION")) {
	// String strId = hAction.get("ID");
	// if (hAction.get("ACTION").equalsIgnoreCase("EDIT")) {
	// // load the data.
	// DatabaseUtility.getDatabaseHandler(LeadInfo.this);
	// Lead lead = new Lead(Constants.DBHANDLER);
	// oLeadDetail = lead.getLeadDetailById(strId);
	// } else if (hAction.get("ACTION").equalsIgnoreCase("LOAD")) {
	// oLeadDetail = ContactUtility.getLeadFromPhoneBook(
	// LeadInfo.this, strId);
	// }
	// }
	// DeviceUtility.log(TAG, oLeadDetail.toString());
	// return oLeadDetail;
	// }
	//
	// @Override
	// protected void onPostExecute(LeadDetail result) {
	// super.onPostExecute(result);
	// lead = result;
	// int iLeadSourcePosition = 0;
	// int iPrefixPosition = 0;
	// doShowSelectedSector(strSector);
	// ArrayAdapter<String> prefixAdapter = new ArrayAdapter<String>(
	// LeadInfo.this, android.R.layout.simple_spinner_item);
	// prefixAdapter
	// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// DeviceUtility.log(TAG, "PREFIX: " + aPrefix.size());
	// for (int i = 0; i < aPrefix.size(); i++) {
	// prefixAdapter.add(aPrefix.get(i).getText());
	// if (result.getPrefix() != null) {
	// if (result.getPrefix().equalsIgnoreCase(
	// aPrefix.get(i).getValue())) {
	// iPrefixPosition = i;
	// }
	// }
	// }
	// prefix.setAdapter(prefixAdapter);
	// prefix.setSelection(iPrefixPosition);
	//
	// ArrayAdapter<String> aLeadSourceAdapter = new ArrayAdapter<String>(
	// LeadInfo.this, android.R.layout.simple_spinner_item);
	// aLeadSourceAdapter
	// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	//
	// for (int i = 0; i < aLeadSource.size(); i++) {
	// aLeadSourceAdapter.add(aLeadSource.get(i).getText());
	// if (result.getLeadSource() != null) {
	// if (result.getLeadSource().equalsIgnoreCase(
	// aLeadSource.get(i).getValue())) {
	// iLeadSourcePosition = i;
	// }
	// }
	// }
	// lead_source.setAdapter(aLeadSourceAdapter);
	// lead_source.setSelection(iLeadSourcePosition);
	//
	// ArrayAdapter<String> aLeadStatusAdapter = new ArrayAdapter<String>(
	// LeadInfo.this, android.R.layout.simple_spinner_item);
	// aLeadStatusAdapter
	// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// int iLeadStatus = 0;
	// for (int i = 0; i < aLeadStatus.size(); i++) {
	// aLeadStatusAdapter.add(aLeadStatus.get(i).getText());
	// if (result.getLeadStatus() != null) {
	// if (result.getLeadStatus().equalsIgnoreCase(
	// aLeadStatus.get(i).getValue())) {
	// iLeadStatus = i;
	// }
	// }
	// }
	// lead_status.setAdapter(aLeadStatusAdapter);
	// lead_status.setSelection(iLeadStatus);
	//
	// ArrayAdapter<String> aAttitudeAdapter = new ArrayAdapter<String>(
	// LeadInfo.this, android.R.layout.simple_spinner_item);
	// aAttitudeAdapter
	// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// int iAttitude = 0;
	// for (int i = 0; i < aAttitude.size(); i++) {
	// aAttitudeAdapter.add(aAttitude.get(i).getText());
	// if (result.getAttitude() != null) {
	// if (result.getAttitude().equalsIgnoreCase(
	// aAttitude.get(i).getValue())) {
	// iAttitude = i;
	// }
	// }
	// }
	// attitude.setAdapter(aAttitudeAdapter);
	// attitude.setSelection(iAttitude);
	//
	// ArrayAdapter<String> aIndustryAdapter = new ArrayAdapter<String>(
	// LeadInfo.this, android.R.layout.simple_spinner_item);
	// aIndustryAdapter
	// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// int iIndustry = 0;
	// for (int i = 0; i < aLeadIndustry.size(); i++) {
	// aIndustryAdapter.add(aLeadIndustry.get(i).getText());
	// if (result.getIndustry() != null) {
	// if (result.getIndustry().equalsIgnoreCase(
	// aLeadIndustry.get(i).getValue())) {
	// iIndustry = i;
	// }
	// }
	// }
	// industry.setAdapter(aIndustryAdapter);
	// industry.setSelection(iIndustry);
	//
	// if (result.getFirstName() != null) {
	// first_name.setText(result.getFirstName());
	// }
	//
	// if (result.getLastName() != null) {
	// last_name.setText(result.getLastName());
	// }
	//
	// if (result.getCompanyName() != null) {
	// company_name.setText(result.getCompanyName());
	// }
	//
	// if (result.getJobTitle() != null) {
	// job_title.setText(result.getJobTitle());
	// }
	//
	// if (result.getMobile() != null) {
	// mobile.setText(result.getMobile());
	// }
	//
	// if (result.getWorkFax() != null) {
	// work_fax.setText(result.getWorkFax());
	// }
	//
	// if (result.getWorkPhone() != null) {
	// work_phone.setText(result.getWorkPhone());
	// }
	//
	// if (result.getWebsite() != null) {
	// website.setText(result.getWebsite());
	// }
	//
	// if (result.getEmail1() != null) {
	// email.setText(result.getEmail1());
	// }
	//
	// if (result.getEmail2() != null) {
	// email2.setText(result.getEmail2());
	// }
	//
	// if (result.getEmail3() != null) {
	// email3.setText(result.getEmail3());
	// }
	//
	// if (result.getSkypeId() != null) {
	// skype_id.setText(result.getSkypeId());
	// }
	//
	// if (result.getNoOfEmployee() != null) {
	// no_of_employee.setText(result.getNoOfEmployee());
	// }
	//
	// if (result.getAnnualRevenue() != null) {
	// annual_revenue.setText(result.getAnnualRevenue());
	// }
	//
	// if (result.getMailingCity() != null) {
	// city.setText(result.getMailingCity());
	// }
	//
	// if (result.getMailingStreet() != null) {
	// street.setText(result.getMailingStreet());
	// }
	//
	// if (result.getMailingState() != null) {
	// state.setText(result.getMailingState());
	// }
	//
	// if (result.getMailingZip() != null) {
	// zip.setText(result.getMailingZip());
	// }
	//
	// if (result.getMailingCountry() != null) {
	// country.setText(result.getMailingCountry());
	// }
	//
	// if (result.getDescription() != null) {
	// description.setText(result.getDescription());
	// }
	//
	// if (loadingDialog.isShowing()) {
	// loadingDialog.dismiss();
	// }
	// }
	// }

	private class SaveLead extends AsyncTask<LeadDetail, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(LeadInfo.this, false,
					getString(R.string.processing));
		}

		@Override
		protected Boolean doInBackground(LeadDetail... params) {
			LeadDetail oLeadDetail = params[0];
			DeviceUtility.log(TAG, oLeadDetail.toString());
			DatabaseUtility.getDatabaseHandler(LeadInfo.this);
			Lead lead = new Lead(Constants.DBHANDLER);

			oLeadDetail.setIsUpdate("false");
			oLeadDetail.setUpdateGpsLocation("true");

			if (oLeadDetail.getInternalNum() == null) {
				long lRow = lead.insert(oLeadDetail);
				if (lRow == -1) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			} else {
				int iRowEffected = lead.update(oLeadDetail);
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
				GuiUtility.alert(LeadInfo.this,
						getString(R.string.success_store_lead_title),
						getString(R.string.success_store_lead_desc),
						Gravity.CENTER, getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LeadInfo.this.finish();
							}
						}, "", null);
			} else {
				GuiUtility.alert(LeadInfo.this,
						getString(R.string.fail_store_lead_title),
						getString(R.string.fail_store_lead_desc),
						getString(R.string.ok));
			}
		}

	}

	private class LoadContact extends AsyncTask<String, Void, LeadDetail> {

		@Override
		protected LeadDetail doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(LeadInfo.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			return lead.getLeadDetailById(params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(LeadInfo.this, false,
					null);
		}

		@Override
		protected void onPostExecute(LeadDetail result) {
			super.onPostExecute(result);
			aEmailList = new ArrayList<EmailDetail>();
			aContactNumberList = new ArrayList<PhoneDetail>();

			lead = result;
			// first_name.setText(result.getFirstName());
			// last_name.setText(result.getLastName());
			// company.setText(result.getCompanyName());
			//
			// if (result.getPhoto() != null) {
			// StorageUtility storage = new StorageUtility(LeadInfo.this,
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

			if (aContactNumberList.size() == 0) {
				// mCall.setBackgroundResource(R.drawable.disabled_button);
				mCall.setOnClickListener(null);
				mCall.setVisibility(View.GONE);

				// mSms.setBackgroundResource(R.drawable.disabled_button);
			} else {
				mCall.setVisibility(View.VISIBLE);
				// mCall.setBackgroundResource(R.drawable.orange_button);
				// mSms.setBackgroundResource(R.drawable.orange_button);
				if (aContactNumberList.size() != 0) {
					mCall.setOnClickListener(doCall);
				} else {
					mCall.setOnClickListener(doShowContextMenu);
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
				registerForContextMenu(v);
				openContextMenu(v);
				unregisterForContextMenu(v);
			}
		};

		private OnClickListener doComposeEmail = new OnClickListener() {

			@Override
			public void onClick(View v) {

				String str[] = new String[aEmailList.size()];
				for (int i = 0; i < str.length; i++) {
					str[i] = aEmailList.get(i).getEmailAddress();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LeadInfo.this);
				builder.setTitle(R.string.email).setItems(str,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								doLogActivity(Constants.ACTION_EMAIL,
										aEmailList.get(which).getEmailAddress());
								Intent emailIntent = new Intent(
										android.content.Intent.ACTION_SEND);
								emailIntent.setType("plain/text");
								String sEmailList[] = { aEmailList.get(which)
										.getEmailAddress() };
								emailIntent.putExtra(
										android.content.Intent.EXTRA_EMAIL,
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
//				String str[] = new String[aContactNumberList.size()];
//				for (int i = 0; i < str.length; i++) {
//					str[i] = aContactNumberList.get(i).getPhoneNo();
//				}
//
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						LeadInfo.this);
//				builder.setTitle(R.string.sms).setItems(str,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								doLogActivity(Constants.ACTION_SMS,
//										aContactNumberList.get(which)
//												.getPhoneNo());
//								Intent smsIntent = new Intent(
//										android.content.Intent.ACTION_VIEW);
//								smsIntent.setData(Uri.parse("sms:"
//										+ aContactNumberList.get(which)
//												.getPhoneNo()));
//								startActivity(smsIntent);
//							}
//						});
//				builder.create();
//				builder.show();
				
           		doLogActivity(Constants.ACTION_SMS, strSms);
					Intent smsIntent = new Intent(
							android.content.Intent.ACTION_VIEW);
					smsIntent.setData(Uri.parse("sms:"
							+ strSms));
					startActivity(smsIntent);
			}
		};
		

		private OnClickListener doCall = new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str[] = new String[aContactNumberList.size()];
				for (int i = 0; i < str.length; i++) {
					str[i] = aContactNumberList.get(i).getPhoneNo();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LeadInfo.this);
				builder.setTitle(R.string.call).setItems(str,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String phoneNo = formatCallNumber(aContactNumberList.get(which).getPhoneNo());
				            	System.out.println("call = "+phoneNo);
				            	doLogActivity(Constants.ACTION_CALL, phoneNo);
								Intent callIntent = new Intent(
										android.content.Intent.ACTION_CALL, Uri
												.parse("tel:"
														+ phoneNo
																));
								startActivity(callIntent);
							}
						});
				builder.create();
				builder.show();
			}
		};
	}

}
