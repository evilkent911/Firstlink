package com.trinerva.icrm.contacts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.ContactUtility;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

public class ContactInfo extends Activity {
	private String TAG = "ContactInfo";
	private ContactDetail contact = new ContactDetail();
	private HashMap<String, String> hLoad = new HashMap<String, String>();
	private String strContactUri = null;
	private ArrayList<MasterInfo> aPrefix;
	private static final int MY_DATE_DIALOG_ID = 3990;
	private Spinner prefixSpinner;
	private TableLayout others_field;
	private LinearLayout basic_field;
	private ScrollView scroll_view;
	private ImageView basicAndothers;
	private TextView save;
	private Dialog loadingDialog;
	private ImageView birthday_clear;
	private View save_divider;
	private EditText first_name, last_name, company_name, job_title, department, mobile, work_fax, work_phone, other_phone, email, home_phone, birthday, email2, email3, skype_id, assistant_name, assistant_phone, street, city, state, zip, country, description;
	private String strSector = "BASIC";
	boolean bView = false;
	boolean bActive = true;
	boolean nowSelectStatic = false;
	
	RelativeLayout mNavigationBar;
	TextView mModified;
	HorizontalScrollView hs;
	ImageView mArrowR,mArrowL;
	private String strId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info);

		scroll_view = (ScrollView) findViewById(R.id.scroll_view);
		prefixSpinner = (Spinner) findViewById(R.id.prefix);
		others_field = (TableLayout) findViewById(R.id.others_field);
		basic_field = (LinearLayout) findViewById(R.id.basic_field);
//		basic = (TextView) findViewById(R.id.basic);
		basicAndothers = (ImageView) findViewById(R.id.others);
		save = (TextView) findViewById(R.id.save);
		first_name = (EditText) findViewById(R.id.first_name);
		last_name  = (EditText) findViewById(R.id.last_name);
		company_name = (EditText) findViewById(R.id.company_name);
		job_title = (EditText) findViewById(R.id.job_title);
		department = (EditText) findViewById(R.id.department);
		mobile = (EditText) findViewById(R.id.mobile);
		work_fax = (EditText) findViewById(R.id.work_fax);
		work_phone = (EditText) findViewById(R.id.work_phone);
		other_phone = (EditText) findViewById(R.id.other_phone);
		email = (EditText) findViewById(R.id.email);
		home_phone = (EditText) findViewById(R.id.home_phone);
		birthday = (EditText) findViewById(R.id.birthday);
		birthday_clear = (ImageView) findViewById(R.id.birthday_clear);
		email2 = (EditText) findViewById(R.id.email2);
		email3 = (EditText) findViewById(R.id.email3);
		skype_id = (EditText) findViewById(R.id.skype_id);
		assistant_name = (EditText) findViewById(R.id.assistant_name);
		assistant_phone = (EditText) findViewById(R.id.assistant_phone);
		street = (EditText) findViewById(R.id.street);
		city = (EditText) findViewById(R.id.city);
		state = (EditText) findViewById(R.id.state);
		zip = (EditText) findViewById(R.id.zip);
		country = (EditText) findViewById(R.id.country);
		description = (EditText) findViewById(R.id.description);
		save_divider = (View) findViewById(R.id.save_divider);
		mModified = (TextView) findViewById(R.id.modified);
		mNavigationBar = (RelativeLayout) findViewById(R.id.navigationBar);
		 hs = (HorizontalScrollView) findViewById(R.id.HorizontalScrollView);
		 mArrowR = (ImageView) findViewById(R.id.arrowR);
		 mArrowL = (ImageView) findViewById(R.id.arrowL);
		 

		 
		 mArrowR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 hs.arrowScroll(View.FOCUS_RIGHT);
			}
		});
		 
		 mArrowL.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				hs.arrowScroll(View.FOCUS_LEFT);
			}
		}); 

		 
		//get extra.
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
			
			if (bundle.containsKey("VIEW")) {
				bView = bundle.getBoolean("VIEW");
			}
			
			if (bundle.containsKey("ACTIVE")) {
				String strActive = bundle.getString("ACTIVE");
				if (strActive.equals("1")) {
					bActive = false;
				}
			}
			
			if (bundle.containsKey("ID")) {
				strId = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strId);
			}
			
			
		}

		birthday.setFocusable(false);
		
		//birthday.setOnTouchListener(showDatePicker2);
		if (bView) {
			doLockField(true);
			if (bActive) {
				save.setText(getString(R.string.edit));
				save.setOnClickListener(doChangeToSave);
				
	
				

				
			} else {
				//hide the save button.
				save_divider.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
			
				
			}
		} else {
			mModified.setVisibility(View.GONE);
			mNavigationBar.setVisibility(View.GONE);
			save.setOnClickListener(saveContact);
			birthday.setOnClickListener(showDatePicker);
			birthday_clear.setOnClickListener(clearBirthday);
		}
		LoadContact task = new LoadContact();
		task.execute(hLoad);
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private OnClickListener doChangeToSave = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			doLockField(false);
			save.setText(getString(R.string.save));
			save.setOnClickListener(saveContact);
			birthday.setOnClickListener(showDatePicker);
			birthday_clear.setOnClickListener(clearBirthday);
		}
	};

	private void doShowSelectedSector(String strField) {
//		basic.setBackgroundResource(R.drawable.blue_menu_left);
//		others.setBackgroundResource(R.drawable.blue_menu_right);
//		if (strField.equalsIgnoreCase("BASIC")) {
//			basic.setBackgroundResource(R.drawable.blue_menu_selected_left);
//			basic.setOnClickListener(null);
		basicAndothers.setOnClickListener(showOthersField);
//		}
//		
//		if (strField.equalsIgnoreCase("OTHERS")) {
//			others.setBackgroundResource(R.drawable.blue_menu_selected_right);
//			others.setOnClickListener(null);
//			basic.setOnClickListener(showBasicField);
//		}
	}
	
	private static String formatChangeToSaveNumber(String phoneNo){
		phoneNo = phoneNo.replaceAll("[\\s\\-()]", ""); 
		phoneNo = "+" + phoneNo;
		return phoneNo;
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
				                    CharSequence strDate = DateFormat.format(Constants.DATE_FORMAT, dtDob);
				                    birthday.setText(strDate);
				        }}, 2011,0, 1);

				      dateDlg.setMessage(getString(R.string.birthday));
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
			    if (birthday.getText().toString().length() == 10) {
			    	iDay = Integer.parseInt(birthday.getText().toString().substring(0, 2));
			    	iMonth = Integer.parseInt(birthday.getText().toString().substring(3, 5)) - 1;
			    	iYear = Integer.parseInt(birthday.getText().toString().substring(6, 10));

			    	DeviceUtility.log(TAG, "iDay: "+iDay+" iMonth: "+iMonth+" iYear: " + iYear);
			    }
			    dateDlg.updateDate(iYear, iMonth, iDay);
		     break;
		}
	}

//	private OnClickListener showBasicField = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//	
//		}
//	};

	private OnClickListener showOthersField = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if(nowSelectStatic){
				nowSelectStatic = false;
				basic_field.setVisibility(View.VISIBLE);
				others_field.setVisibility(View.GONE);
				scroll_view.fullScroll(ScrollView.FOCUS_UP);
				strSector = "BASIC";
				doShowSelectedSector(strSector);
//				basicAndothers.setText("Other");
			}else{
				nowSelectStatic = true;
			others_field.setVisibility(View.VISIBLE);
			basic_field.setVisibility(View.GONE);
			scroll_view.fullScroll(ScrollView.FOCUS_UP);
			strSector = "OTHERS";
//			basicAndothers.setText("Basic");
			doShowSelectedSector(strSector);
			}
		}
	};

	private OnClickListener showDatePicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog(MY_DATE_DIALOG_ID);
		}
	};

//	private OnTouchListener showDatePicker2 = new OnTouchListener() {
//
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			showDialog(MY_DATE_DIALOG_ID);
//			return true;
//		}
//	};
	
	private OnClickListener clearBirthday = new OnClickListener() {
		@Override
		public void onClick(View v) {
			birthday.setText("");
		}
	};
	
	
	private static boolean validatePhoneNumber(String phoneNo) {
		if (phoneNo.matches("^\\+?[1-9]\\d{1,4}(\\(\\d{1,5}\\)\\d{6,15}\\s?(([Ee][Xx][Tt])\\d{1,5})?|\\-?\\d{6,15}\\s?(([Ee][Xx][Tt])\\d{1,5})?)$"))
		  {
			  return true;
		  }
        else return false;
    }
	private static boolean validateFaxNumber(String phoneNo) {
		if (phoneNo.matches("^\\+?[1-9]\\d{1,4}(\\(\\d{1,5}\\)\\d{6,15}|\\-?\\d{6,15})$"))
		  {
			  return true;
		  }
        else return false;
    }
	private static String formatPhoneNumber(String phoneNo) {
		if(phoneNo.charAt(0) == '+') //remove + sign if exist
			phoneNo = phoneNo.substring(1, phoneNo.length());
			phoneNo = phoneNo.replaceAll("[\\s\\-()]", "");
		if(phoneNo.charAt(1) == '0' && phoneNo.charAt(2) == '0'){//if there was 2 zeros in call code
			StringBuilder sb = new StringBuilder(phoneNo);
			sb = sb.deleteCharAt(1);
			phoneNo = sb.toString();
		}
		if(phoneNo.charAt(0) == '6'){
			if(phoneNo.charAt(2) == '8' || phoneNo.charAt(2) == '9'){//if 3rd digit is 8 then add bracket
				String temp = "";
				for (int i = 0; i<phoneNo.length(); i++){
					if(i == 2)
						temp += "(" + phoneNo.charAt(i) + phoneNo.charAt(i+1) + ")";
					else if (i==3){
					} else
						temp += phoneNo.charAt(i) + "";
				}
				phoneNo = temp;
			}else {
				phoneNo = phoneNo.replaceFirst(phoneNo.charAt(2) + "", "(" + phoneNo.charAt(2) + ")");
			}
		}
		return phoneNo;
    }
	private static String formatFaxNumber(String phoneNo) {
		if(phoneNo.charAt(0) == '+') //remove + sign if exist
			phoneNo = phoneNo.substring(1, phoneNo.length());
			phoneNo = phoneNo.replaceAll("[\\s\\-()]", "");
		if(phoneNo.charAt(1) == '0' && phoneNo.charAt(2) == '0'){//if there was 2 zeros in call code
			StringBuilder sb = new StringBuilder(phoneNo);
			sb = sb.deleteCharAt(1);
			phoneNo = sb.toString();
		}
		if(phoneNo.charAt(0) == '6'){
			if(phoneNo.charAt(2) == '8' || phoneNo.charAt(2) == '9'){//if 3rd digit is 8 then add bracket
				String temp = "";
				for (int i = 0; i<phoneNo.length(); i++){
					if(i == 2)
						temp += "(" + phoneNo.charAt(i) + phoneNo.charAt(i+1) + ")";
					else if (i==3){
					} else
						temp += phoneNo.charAt(i) + "";
				}
				phoneNo = temp;
			}else {
				phoneNo = phoneNo.replaceFirst(phoneNo.charAt(2) + "", "(" + phoneNo.charAt(2) + ")");
			}
		}
		return phoneNo;
    }
	

	private OnClickListener saveContact = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLockField(true);
			
			//do checking.
			//String strPrefix = aPrefix.get(prefixSpinner.getSelectedItemPosition()).getValue();
			String strPrefix = "0";
			if (prefixSpinner.getSelectedItemPosition() >= 0) {
				strPrefix = aPrefix.get(prefixSpinner.getSelectedItemPosition()).getValue();
			}

			String strFirstName = first_name.getText().toString();
			String strLastName = last_name.getText().toString();
			String strCompanyName = company_name.getText().toString();
			String strJobTitle = job_title.getText().toString();
			String strDepartment = department.getText().toString();
			String strMobile = mobile.getText().toString();
			String strWorkFax = work_fax.getText().toString();
			String strWorkPhone = work_phone.getText().toString();
			String strOtherPhone = other_phone.getText().toString();
			String strEmail = email.getText().toString();
			String strHomePhone = home_phone.getText().toString();
			String strBirthday = birthday.getText().toString();
			String strEmail2 = email2.getText().toString();
			String strEmail3 = email3.getText().toString();
			String strSkype = skype_id.getText().toString();
			String strAssistantName = assistant_name.getText().toString();
			String strAssistantPhone = assistant_phone.getText().toString();
			String strStreet = street.getText().toString();
			String strCity = city.getText().toString();
			String strState = state.getText().toString();
			String strZip = zip.getText().toString();
			String strCountry = country.getText().toString();
			String strDescription = description.getText().toString();

			if (strLastName.length() == 0) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.last_name_is_required), getString(R.string.ok));
				doLockField(false);
				last_name.requestFocus();
			} else if (strCompanyName.length() == 0) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.company_name_is_required), getString(R.string.ok));
				doLockField(false);
				company_name.requestFocus();
			} else if (strEmail.length() > 0 && !Utility.isValidEmail(strEmail)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.email_error_msg), getString(R.string.ok));
				doLockField(false);
				email.requestFocus();
			} else if (strEmail2.length() > 0 && !Utility.isValidEmail(strEmail2)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.email_error_msg), getString(R.string.ok));
				doLockField(false);
				email2.requestFocus();
			} else if (strEmail3.length() > 0 && !Utility.isValidEmail(strEmail3)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.email_error_msg), getString(R.string.ok));
				doLockField(false);
				email3.requestFocus();
			} else if (strMobile.length() > 0 && !Utility.isValidPhone(strMobile)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg), getString(R.string.ok));
				doLockField(false);
				mobile.requestFocus();			
//			} else if (strWorkFax.length() > 0 && !Utility.isValidPhone(strWorkFax)) {
//				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg), getString(R.string.ok));
//				doLockField(false);
//				work_fax.requestFocus();
//			} else if (strWorkPhone.length() > 0 && !Utility.isValidPhone(strWorkPhone)) {
//				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg), getString(R.string.ok));
//				doLockField(false);
//				work_phone.requestFocus();
			} else if (strOtherPhone.length() > 0 && !validatePhoneNumber(strOtherPhone)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg) + " Example: 60(3)12345678 Ext1234", getString(R.string.ok));
				doLockField(false);
				other_phone.requestFocus();
			} else if (strHomePhone.length() > 0 && !Utility.isValidPhone(strHomePhone)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg), getString(R.string.ok));
				doLockField(false);
				home_phone.requestFocus();
			} else if (strAssistantPhone.length() > 0 && !Utility.isValidPhone(strAssistantPhone)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg), getString(R.string.ok));
				doLockField(false);
				assistant_phone.requestFocus();
			} else if (strSkype.length() > 0 && Utility.isContainWhitespace(strSkype)) {
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.skype_error_msg), getString(R.string.ok));
				doLockField(false);
				skype_id.requestFocus();
			} else if(strWorkPhone.length() > 0 && !validatePhoneNumber(strWorkPhone)){
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg) + " Example: 60(3)12345678 Ext1234", getString(R.string.ok));
				doLockField(false);				
				work_phone.requestFocus();
			} else if (strWorkFax.length() > 0 && !validateFaxNumber(strWorkFax)){
				GuiUtility.alert(ContactInfo.this, "", getString(R.string.phone_error_msg) + " Example: 60(3)12345678", getString(R.string.ok));
				doLockField(false);
				work_fax.requestFocus();
			} else {
				DeviceUtility.log(TAG, "isContainWhitespace: " + Utility.isContainWhitespace(strSkype));
				//save
				if (hLoad.containsKey("ACTION")) {
					if (hLoad.get("ACTION").equalsIgnoreCase("EDIT")) {
						contact.setInternalNumber(hLoad.get("ID"));
					}
				}

				contact.setPrefix(strPrefix);				
				if (strFirstName.length() == 0) {
					contact.setFirstName(null);
				} else {
					contact.setFirstName(strFirstName);
				}
				
				contact.setLastName(strLastName);
				contact.setCompanyName(strCompanyName);
				
				if (strJobTitle.length() == 0) {
					contact.setJobTitle(null);
				} else {
					contact.setJobTitle(strJobTitle);
				}
				
				if (strDepartment.length() == 0) {
					contact.setDepartment(null);
				} else {
					contact.setDepartment(strDepartment);
				}
				
				if (strMobile.length() == 0) {
					contact.setMobile(null);
				} else {
					contact.setMobile(strMobile);
				}
				
				if (strWorkFax.length() == 0) {
					contact.setWorkFax(null);
				} else {
					//format phone here
					strWorkFax = formatFaxNumber(strWorkFax);
					contact.setWorkFax(strWorkFax);
				}
				
				if (strWorkPhone.length() == 0) {
					contact.setWorkPhone(null);
				} else {
					//format phone here					
					strWorkPhone = formatPhoneNumber(strWorkPhone);
					contact.setWorkPhone(strWorkPhone);
				}
				
				if (strOtherPhone.length() == 0) {
					contact.setOtherPhone(null);
				} else {
					strOtherPhone = formatPhoneNumber(strOtherPhone);
					contact.setOtherPhone(strOtherPhone);
				}
				
				if (strEmail.length() == 0) {
					contact.setEmail1(null);
				} else {
					contact.setEmail1(strEmail);
				}
				
				if (strHomePhone.length() == 0) {
					contact.setHomePhone(null);
				} else {
					contact.setHomePhone(strHomePhone);
				}
				
				if (strBirthday.length() == 0) {
					contact.setBirthday(null);
				} else {
					contact.setBirthday(strBirthday);
				}
				
				if (strEmail2.length() == 0) {
					contact.setEmail2(null);
				} else {
					contact.setEmail2(strEmail2);
				}
				
				if (strEmail3.length() == 0) {
					contact.setEmail3(null);
				} else {
					contact.setEmail3(strEmail3);
				}
				
				if (strSkype.length() == 0) {
					contact.setSkypeId(null);
				} else {
					contact.setSkypeId(strSkype);
				}
				
				if (strAssistantName.length() == 0) {
					contact.setAssistantName(null);
				} else {
					contact.setAssistantName(strAssistantName);
				}
				
				if (strAssistantPhone.length() == 0) {
					contact.setAssistantPhone(null);
				} else {
					contact.setAssistantPhone(strAssistantPhone);
				}
				
				if (strStreet.length() == 0) {
					contact.setMailingStreet(null);
				} else {
					contact.setMailingStreet(strStreet);
				}
				
				if (strCity.length() == 0) {
					contact.setMailingCity(null);
				} else {
					contact.setMailingCity(strCity);
				}
				
				if (strState.length() == 0) {
					contact.setMailingState(null);
				} else {
					contact.setMailingState(strState);
				}
				
				if (strZip.length() == 0) {
					contact.setMailingZip(null);
				} else {
					contact.setMailingZip(strZip);
				}
				
				if (strCountry.length() == 0) {
					contact.setMailingCountry(null);
				} else {
					contact.setMailingCountry(strCountry);
				}
				
				if (strDescription.length() == 0) {
					contact.setDescription(null);
				} else {
					contact.setDescription(strDescription);
				}

				//get owner
				String strOwner = Utility.getConfigByText(ContactInfo.this, "USER_EMAIL");
				if (strOwner.length() > 0) {
					contact.setOwner(strOwner);
				}
				SaveContact task = new SaveContact();
				task.execute(contact);
			}
		}
	};


	public void doLockField(boolean bLock) {
		DeviceUtility.log(TAG, "doLockField("+bLock+")");
		boolean bEnabled = true;
		boolean bFocusabled = true;
		if (bLock == true) {
			bEnabled = false;
			bFocusabled = false;
			mModified.setVisibility(View.VISIBLE);
			mNavigationBar.setVisibility(View.VISIBLE);
		}else{


			mModified.setVisibility(View.GONE);
			mNavigationBar.setVisibility(View.GONE);

		}

		//enabled/disabled edit.
		prefixSpinner.setEnabled(bEnabled);
		first_name.setEnabled(bEnabled);
		last_name.setEnabled(bEnabled);
		company_name.setEnabled(bEnabled);
		job_title.setEnabled(bEnabled);
		department.setEnabled(bEnabled);
		mobile.setEnabled(bEnabled);
		work_fax.setEnabled(bEnabled);
		work_phone.setEnabled(bEnabled);
		other_phone.setEnabled(bEnabled);
		email.setEnabled(bEnabled);
		home_phone.setEnabled(bEnabled);
		birthday.setEnabled(bEnabled);
		email2.setEnabled(bEnabled);
		email3.setEnabled(bEnabled);
		skype_id.setEnabled(bEnabled);
		assistant_name.setEnabled(bEnabled);
		assistant_phone.setEnabled(bEnabled);
		street.setEnabled(bEnabled);
		city.setEnabled(bEnabled);
		state.setEnabled(bEnabled);
		zip.setEnabled(bEnabled);
		country.setEnabled(bEnabled);
		description.setEnabled(bEnabled);

		//enabled/disabled focus
		prefixSpinner.setFocusable(bFocusabled);
		first_name.setFocusable(bFocusabled);
		last_name.setFocusable(bFocusabled);
		company_name.setFocusable(bFocusabled);
		job_title.setFocusable(bFocusabled);
		department.setFocusable(bFocusabled);
		mobile.setFocusable(bFocusabled);
		work_fax.setFocusable(bFocusabled);
		work_phone.setFocusable(bFocusabled);
		other_phone.setFocusable(bFocusabled);
		email.setFocusable(bFocusabled);
		home_phone.setFocusable(bFocusabled);
		//birthday.setFocusable(bFocusabled);
		email2.setFocusable(bFocusabled);
		email3.setFocusable(bFocusabled);
		skype_id.setFocusable(bFocusabled);
		assistant_name.setFocusable(bFocusabled);
		assistant_phone.setFocusable(bFocusabled);
		street.setFocusable(bFocusabled);
		city.setFocusable(bFocusabled);
		state.setFocusable(bFocusabled);
		zip.setFocusable(bFocusabled);
		country.setFocusable(bFocusabled);
		description.setFocusable(bFocusabled);

		if (bFocusabled) {
			//prefixSpinner.setFocusableInTouchMode(bFocusabled);
			first_name.setFocusableInTouchMode(bFocusabled);
			last_name.setFocusableInTouchMode(bFocusabled);
			company_name.setFocusableInTouchMode(bFocusabled);
			job_title.setFocusableInTouchMode(bFocusabled);
			department.setFocusableInTouchMode(bFocusabled);
			mobile.setFocusableInTouchMode(bFocusabled);
			work_fax.setFocusableInTouchMode(bFocusabled);
			work_phone.setFocusableInTouchMode(bFocusabled);
			other_phone.setFocusableInTouchMode(bFocusabled);
			email.setFocusableInTouchMode(bFocusabled);
			home_phone.setFocusableInTouchMode(bFocusabled);
			//birthday.setFocusableInTouchMode(bFocusabled);
			email2.setFocusableInTouchMode(bFocusabled);
			email3.setFocusableInTouchMode(bFocusabled);
			skype_id.setFocusableInTouchMode(bFocusabled);
			assistant_name.setFocusableInTouchMode(bFocusabled);
			assistant_phone.setFocusableInTouchMode(bFocusabled);
			street.setFocusableInTouchMode(bFocusabled);
			city.setFocusableInTouchMode(bFocusabled);
			state.setFocusableInTouchMode(bFocusabled);
			zip.setFocusableInTouchMode(bFocusabled);
			country.setFocusableInTouchMode(bFocusabled);
			description.setFocusableInTouchMode(bFocusabled);
		}
	}

	private class SaveContact extends AsyncTask<ContactDetail, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactInfo.this, false, getString(R.string.processing));
		}
		
		@Override
		protected Boolean doInBackground(ContactDetail... params) {
			ContactDetail contactDetail = params[0];
			contactDetail.setIsUpdate("false");
			contactDetail.setUpdateGpsLocation("true");
			DatabaseUtility.getDatabaseHandler(ContactInfo.this);
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
				//GuiUtility.alert(ContactInfo.this, getString(R.string.success_store_contact_title), getString(R.string.success_store_contact_desc), getString(R.string.ok));
				GuiUtility.alert(ContactInfo.this, getString(R.string.success_store_contact_title), getString(R.string.success_store_contact_desc), Gravity.CENTER, getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Intent iContact = new Intent(ContactInfo.this, ContactList.class);
								//ContactInfo.this.startActivity(iContact);
								ContactInfo.this.finish();
							}
				}, "", null);
			} else {
				GuiUtility.alert(ContactInfo.this, getString(R.string.fail_store_contact_title), getString(R.string.fail_store_contact_desc), getString(R.string.ok));
			}
		}
	}

	private class LoadContact extends AsyncTask<HashMap<String, String>, Void, ContactDetail> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactInfo.this, false, null);
		}

		@Override
		protected ContactDetail doInBackground(HashMap<String, String>... params) {
			aPrefix = (ArrayList<MasterInfo>) Utility.getMasterByType(ContactInfo.this, Constants.MASTER_PREFIX);
			DeviceUtility.log(TAG, "get PREFIX: " + aPrefix.size());
			HashMap<String, String> hAction = params[0];
			ContactDetail oContactDetail = new ContactDetail();

			if (hAction.containsKey("ID") && hAction.containsKey("ACTION")) {
				String strId = hAction.get("ID");
				if (hAction.get("ACTION").equalsIgnoreCase("EDIT")) {
					//load the data.
					DatabaseUtility.getDatabaseHandler(ContactInfo.this);
					Contact contact = new Contact(Constants.DBHANDLER);
					oContactDetail = contact.getContactDetailById(strId);
					DeviceUtility.log(TAG, "oContactDetail: " + oContactDetail.toString());
					DeviceUtility.log(TAG, "oContactDetail.getContactId: " + oContactDetail.getContactId());
				} else if (hAction.get("ACTION").equalsIgnoreCase("LOAD")) {
					oContactDetail = ContactUtility.getContact(ContactInfo.this, strId);
				}
			}
			DeviceUtility.log(TAG, oContactDetail.toString());
			return oContactDetail;
		}

		@Override
		protected void onPostExecute(ContactDetail result) {
			contact = result;
			super.onPostExecute(result);
			doShowSelectedSector(strSector);
			int iPrefixSelection = 0;

			ArrayAdapter<String> prefixAdapter = new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_item);
			prefixAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			DeviceUtility.log(TAG, "PREFIX: " + aPrefix.size());
			for (int i = 0; i < aPrefix.size(); i++) {
				prefixAdapter.add(aPrefix.get(i).getText());
				if (result.getPrefix() != null) {
					if (result.getPrefix().equalsIgnoreCase(aPrefix.get(i).getValue())) {
						iPrefixSelection = i;
					}
				}
			}
			prefixSpinner.setAdapter(prefixAdapter);
			prefixSpinner.setSelection(iPrefixSelection);
			
			//set the obj to data field.
			if (result.getFirstName() != null) {
				first_name.setText(result.getFirstName());
			}

			if (result.getLastName() != null) {
				last_name.setText(result.getLastName());
			}

			if (result.getPrefix() != null) {
				prefixSpinner.setSelection(Integer.parseInt(result.getPrefix()));
			}

			if (result.getBirthday() != null && !result.getBirthday().equals("01-01-1900")) {
				birthday.setText(result.getBirthday());
			}

			if (result.getCompanyName() != null) {
				company_name.setText(result.getCompanyName());
			}

			if (result.getDepartment() != null) {
				department.setText(result.getDepartment());
			}

			if (result.getJobTitle() != null) {
				job_title.setText(result.getJobTitle());
			}

			if (result.getMobile() != null) {
				mobile.setText(result.getMobile());
			}

			if (result.getHomePhone() != null) {
				home_phone.setText(result.getHomePhone());
			}

			if (result.getWorkPhone() != null) {				
				work_phone.setText(formatChangeToSaveNumber(result.getWorkPhone()));
			}

			if (result.getOtherPhone() != null) {
				other_phone.setText(formatChangeToSaveNumber(result.getOtherPhone()));
			}

			if (result.getWorkFax() != null) {
				work_fax.setText(formatChangeToSaveNumber(result.getWorkFax()));
			}

			if (result.getMailingCity() != null) {
				city.setText(result.getMailingCity());
			}

			if (result.getMailingCountry() != null) {
				country.setText(result.getMailingCountry());
			}

			if (result.getMailingState() != null) {
				state.setText(result.getMailingState());
			}

			if (result.getMailingStreet() != null) {
				street.setText(result.getMailingStreet());
			}

			if (result.getMailingZip() != null) {
				zip.setText(result.getMailingZip());
			}

			if (result.getEmail1() != null) {
				email.setText(result.getEmail1());
			}

			if (result.getEmail2() != null) {
				email2.setText(result.getEmail2());
			}

			if (result.getEmail3() != null) {
				email3.setText(result.getEmail3());
			}

			if (result.getSkypeId() != null) {
				skype_id.setText(result.getSkypeId());
			}

			if (result.getAssistantName() != null) {
				assistant_name.setText(result.getAssistantName());
			}

			if (result.getAssistantPhone() != null) {
				assistant_phone.setText(result.getAssistantPhone());
			}

			if (result.getDescription() != null) {
				description.setText(result.getDescription());
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

	}
}
