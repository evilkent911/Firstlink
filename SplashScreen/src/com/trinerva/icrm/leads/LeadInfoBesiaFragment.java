package com.trinerva.icrm.leads;

import java.util.ArrayList;
import java.util.HashMap;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.contacts.CompanyList;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.ContactUtility;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

public class LeadInfoBesiaFragment extends Fragment {

	private static String TAG = "LeadInfo";
	private EditText first_name, last_name, company_name, job_title, mobile,
			work_fax, work_phone, website, email, email2, email3, skype_id,
			no_of_employee, annual_revenue, street, city, state, zip, country,
			description;
	private Spinner prefix, lead_source, lead_status, attitude, industry;
	private ArrayList<MasterInfo> aPrefix, aLeadStatus, aLeadSource, aAttitude,
			aLeadIndustry;
	private TableLayout others_field;
	private LinearLayout basic_field;
	private ScrollView scroll_view;
	private View save_divider;
	private String strSector = "BASIC";
	private String strContactUri;
	private HashMap<String, String> hLoad = new HashMap<String, String>();
	private Dialog loadingDialog;
	private LeadDetail lead = new LeadDetail();
	private boolean bView = false;
	private boolean bActive = true;
	LinearLayout mMandatoryLayout;
	String companyId = "";
	private ImageView basic, others;
	String lastName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View v = inflater.inflate(R.layout.lead_info, container, false);

		prefix = (Spinner) v.findViewById(R.id.prefix);
		first_name = (EditText) v.findViewById(R.id.first_name);
		last_name = (EditText) v.findViewById(R.id.last_name);
		company_name = (EditText) v.findViewById(R.id.company_name);
//		company_name.setInputType(InputType.TYPE_NULL); 
//		company_name.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(getActivity(),CompanyList.class));
//			}
//		});
		job_title = (EditText) v.findViewById(R.id.job_title);
		industry = (Spinner) v.findViewById(R.id.industry);
		mobile = (EditText) v.findViewById(R.id.mobile);
		work_fax = (EditText) v.findViewById(R.id.work_fax);
		work_phone = (EditText) v.findViewById(R.id.work_phone);
		lead_source = (Spinner) v.findViewById(R.id.lead_source);
		lead_status = (Spinner) v.findViewById(R.id.lead_status);
		attitude = (Spinner) v.findViewById(R.id.attitude);
		website = (EditText) v.findViewById(R.id.website);
		email = (EditText) v.findViewById(R.id.email);
		email2 = (EditText) v.findViewById(R.id.email2);
		email3 = (EditText) v.findViewById(R.id.email3);
		skype_id = (EditText) v.findViewById(R.id.skype_id);
		no_of_employee = (EditText) v.findViewById(R.id.no_of_employee);
		annual_revenue = (EditText) v.findViewById(R.id.annual_revenue);
		street = (EditText) v.findViewById(R.id.street);
		city = (EditText) v.findViewById(R.id.city);
		state = (EditText) v.findViewById(R.id.state);
		zip = (EditText) v.findViewById(R.id.zip);
		country = (EditText) v.findViewById(R.id.country);
		description = (EditText) v.findViewById(R.id.description);

		others_field = (TableLayout) v.findViewById(R.id.others_field);
		basic_field = (LinearLayout) v.findViewById(R.id.basic_field);
		basic = (ImageView) v.findViewById(R.id.basic);
		others = (ImageView) v.findViewById(R.id.others);
		scroll_view = (ScrollView) v.findViewById(R.id.scroll_view);
		save_divider = (View) v.findViewById(R.id.save_divider);
		
		
		Bundle bundle = getActivity().getIntent().getExtras();
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
		}
		
		if(LeadInfo.lastName != null ){
			lastName = LeadInfo.lastName;
			}
			System.out.println("Lead info b "+lastName);
			if(lastName != null){
				last_name.setText(lastName);
				last_name.requestFocus();
			}
		

		basic.setOnClickListener(showBasicField);
		others.setOnClickListener(showOthersField);
		basic.setVisibility(View.GONE);
		if (bView) {
			doLockField(true);
		} else {
		}
		LoadContact task = new LoadContact();
		task.execute(hLoad);
		
		mMandatoryLayout = (LinearLayout) v.findViewById(R.id.mandatoryLayout);
		mMandatoryLayout.setVisibility(View.VISIBLE);


		basic.setOnClickListener(showBasicField);
		others.setOnClickListener(showOthersField);
		
		if(lastName != null){
			last_name.setText(lastName);
		}

		return v;
	}

	private OnClickListener doChangeToSave = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLockField(false);
			// save.setText(getString(R.string.save));
			// save.setOnClickListener(saveLead);
		}
	};

	private void doShowSelectedSector(String strField) {
		if (strField.equalsIgnoreCase("BASIC")) {
			basic.setOnClickListener(null);
			others.setOnClickListener(showOthersField);
		}

		if (strField.equalsIgnoreCase("OTHERS")) {
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
//			others_field.setVisibility(View.VISIBLE);
//			basic_field.setVisibility(View.GONE);
//			scroll_view.fullScroll(ScrollView.FOCUS_UP);
//			strSector = "OTHERS";
//			doShowSelectedSector(strSector);
			

			Intent intent = new Intent();
			intent.putExtra("page", 1);
			intent.setAction("page");
			getActivity().sendBroadcast(intent);
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
		if(phoneNo.charAt(0) == '6' && phoneNo.charAt(1) == '0'){
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
		if (phoneNo.charAt(0)=='0'){
			phoneNo = '6' + phoneNo ;
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
		if(phoneNo.charAt(0) == '6' && phoneNo.charAt(1) == '0'){
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
		if (phoneNo.charAt(0)=='0'){
			phoneNo = '6' + phoneNo ;
		}
		return phoneNo;
    }
	
	
	
	
	

	OnClickListener saveLead = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLockField(true);
			String strPrefix = aPrefix.get(prefix.getSelectedItemPosition())
					.getValue();
			String strFirstName = first_name.getText().toString();
			String strLastName = last_name.getText().toString();
			String strCompanyName = company_name.getText().toString();
			String strJobTitle = job_title.getText().toString();
			String strIndustry = aLeadIndustry.get(
					industry.getSelectedItemPosition()).getValue();
			String strMobile = mobile.getText().toString();
			String strWorkFax = work_fax.getText().toString();
			String strWorkPhone = work_phone.getText().toString();
			String strLeadSource = aLeadSource.get(
					lead_source.getSelectedItemPosition()).getValue();
			String strLeadStatus = aLeadStatus.get(
					lead_status.getSelectedItemPosition()).getValue();
			String strAttitude = aAttitude.get(
					attitude.getSelectedItemPosition()).getValue();
			String strWebsite = website.getText().toString();
			String strEmail = email.getText().toString();
			String strEmail2 = email2.getText().toString();
			String strEmail3 = email3.getText().toString();
			String strSkypeId = skype_id.getText().toString();
			String strNoOfEmployee = no_of_employee.getText().toString();
			String strAnnualRevenue = annual_revenue.getText().toString();
			String strStreet = street.getText().toString();
			String strCity = city.getText().toString();
			String strState = state.getText().toString();
			String strZip = zip.getText().toString();
			String strCountry = country.getText().toString();
			String strDescription = description.getText().toString();

			DeviceUtility.log(TAG, "strNoOfEmployee: " + strNoOfEmployee);
			int iNoOfEmployee = 0;
			try {
				iNoOfEmployee = Integer.parseInt(no_of_employee.getText()
						.toString());
			} catch (Exception e) {
				DeviceUtility.log(TAG,
						"no of employee can't parse to integer. "
								+ no_of_employee.getText().toString());
			}

			if (strLastName.length() == 0) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.last_name_is_required),
						getString(R.string.ok));
				doLockField(false);
				last_name.requestFocus();
			} else if (strCompanyName.length() == 0) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.company_name_is_required),
						getString(R.string.ok));
				doLockField(false);
				company_name.requestFocus();
			} else if (strIndustry.length() == 0) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.industry_is_required),
						getString(R.string.ok));
				doLockField(false);
				industry.requestFocus();
			} else if (strLeadSource.length() == 0) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.lead_source_is_required),
						getString(R.string.ok));
				doLockField(false);
				lead_source.requestFocus();
			} else if (strLeadStatus.length() == 0) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.lead_status_is_required),
						getString(R.string.ok));
				doLockField(false);
				lead_status.requestFocus();
			} else if (strEmail.length() > 0 && !Utility.isValidEmail(strEmail)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.email_error_msg),
						getString(R.string.ok));
				doLockField(false);
				email.requestFocus();
			} else if (strEmail2.length() > 0
					&& !Utility.isValidEmail(strEmail2)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.email_error_msg),
						getString(R.string.ok));
				doLockField(false);
				email2.requestFocus();
			} else if (strEmail3.length() > 0
					&& !Utility.isValidEmail(strEmail3)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.email_error_msg),
						getString(R.string.ok));
				doLockField(false);
				email3.requestFocus();
			} else if (strMobile.length() > 0 
					&& !Utility.isValidPhone(strMobile)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.phone_error_msg),
						getString(R.string.ok));
				doLockField(false);
				mobile.requestFocus();
			} else if (strWorkFax.length() > 0 && !validateFaxNumber(strWorkFax)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.phone_error_msg) + " Example: 60(3)12345678",
						getString(R.string.ok));
				doLockField(false);
				work_fax.requestFocus();
			} else if (strWorkPhone.length() > 0 && !validatePhoneNumber(strWorkPhone)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.phone_error_msg) + " Example: 60(3)12345678 Ext1234",
						getString(R.string.ok));
				doLockField(false);
				work_phone.requestFocus();
			} else if (strSkypeId.length() > 0
					&& Utility.isContainWhitespace(strSkypeId)) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.skype_error_msg),
						getString(R.string.ok));
				doLockField(false);
				skype_id.requestFocus();
			} else if (strNoOfEmployee.length() > 0 && iNoOfEmployee <= 0) {
				GuiUtility.alert(getActivity(), "",
						getString(R.string.no_of_employee_error_msg),
						getString(R.string.ok));
				doLockField(false);
				no_of_employee.requestFocus();
			} else {
				if (hLoad.containsKey("ACTION")) {
					if (hLoad.get("ACTION").equalsIgnoreCase("EDIT")) {
						lead.setInternalNum(hLoad.get("ID"));
					}
				}

				lead.setPrefix(strPrefix);

				if (strFirstName.length() == 0) {
					lead.setFirstName(null);
				} else {
					lead.setFirstName(strFirstName);
				}

				lead.setLastName(strLastName);
				lead.setCompanyName(strCompanyName);

				if (strJobTitle.length() == 0) {
					lead.setJobTitle(null);
				} else {
					lead.setJobTitle(strJobTitle);
				}

				lead.setIndustry(strIndustry);

				if (strMobile.length() == 0) {
					lead.setMobile(null);
				} else {
					lead.setMobile(strMobile);
				}

				if (strWorkFax.length() == 0) {
					lead.setWorkFax(null);
				} else {
					//format phone here
					strWorkFax = formatFaxNumber(strWorkFax);
					lead.setWorkFax(strWorkFax);
				}

				if (strWorkPhone.length() == 0) {
					lead.setWorkPhone(null);
				} else {
					//format phone here					
					strWorkPhone = formatPhoneNumber(strWorkPhone);
					lead.setWorkPhone(strWorkPhone);
				}

				lead.setLeadSource(strLeadSource);
				lead.setLeadStatus(strLeadStatus);
				lead.setAttitude(strAttitude);

				if (strWebsite.length() == 0) {
					lead.setWebsite(null);
				} else {
					lead.setWebsite(strWebsite);
				}

				if (strEmail.length() == 0) {
					lead.setEmail1(null);
				} else {
					lead.setEmail1(strEmail);
				}

				if (strEmail2.length() == 0) {
					lead.setEmail2(null);
				} else {
					lead.setEmail2(strEmail2);
				}

				if (strEmail3.length() == 0) {
					lead.setEmail3(null);
				} else {
					lead.setEmail3(strEmail3);
				}

				if (strSkypeId.length() == 0) {
					lead.setSkypeId(null);
				} else {
					lead.setSkypeId(strSkypeId);
				}

				if (strNoOfEmployee.length() == 0) {
					lead.setNoOfEmployee(null);
				} else {
					lead.setNoOfEmployee(strNoOfEmployee);
				}

				if (strAnnualRevenue.length() == 0) {
					lead.setAnnualRevenue(null);
				} else {
					lead.setAnnualRevenue(strAnnualRevenue);
				}

				if (strStreet.length() == 0) {
					lead.setMailingStreet(null);
				} else {
					lead.setMailingStreet(strStreet);
				}

				if (strCity.length() == 0) {
					lead.setMailingCity(null);
				} else {
					lead.setMailingCity(strCity);
				}

				if (strState.length() == 0) {
					lead.setMailingState(null);
				} else {
					lead.setMailingState(strState);
				}

				if (strZip.length() == 0) {
					lead.setMailingZip(null);
				} else {
					lead.setMailingZip(strZip);
				}

				if (strCountry.length() == 0) {
					lead.setMailingCountry(null);
				} else {
					lead.setMailingCountry(strCountry);
				}

				if (strDescription.length() == 0) {
					lead.setDescription(null);
				} else {
					lead.setDescription(strDescription);
				}
				// get owner
				String strOwner = Utility.getConfigByText(getActivity(),
						"USER_EMAIL");
				if (strOwner.length() > 0) {
					lead.setOwner(strOwner);
					lead.setUserStamp(strOwner);
				}

				SaveLead task = new SaveLead();
				task.execute(lead);
			}
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

	private class LoadContact extends
			AsyncTask<HashMap<String, String>, Void, LeadDetail> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(getActivity(), false,
					null);
		}

		@Override
		protected LeadDetail doInBackground(HashMap<String, String>... params) {
			aPrefix = (ArrayList<MasterInfo>) Utility.getMasterByType(
					getActivity(), Constants.MASTER_PREFIX);
			aLeadStatus = (ArrayList<MasterInfo>) Utility.getMasterByType(
					getActivity(), Constants.MASTER_LEAD_STATUS);
			aLeadSource = (ArrayList<MasterInfo>) Utility.getMasterByType(
					getActivity(), Constants.MASTER_LEAD_SOURCE);
			aAttitude = (ArrayList<MasterInfo>) Utility.getMasterByType(
					getActivity(), Constants.MASTER_LEAD_ATTITUDE);
			aLeadIndustry = (ArrayList<MasterInfo>) Utility.getMasterByType(
					getActivity(), Constants.MASTER_LEAD_INDUSTRY);
			HashMap<String, String> hAction = params[0];
			LeadDetail oLeadDetail = new LeadDetail();

			if (hAction.containsKey("ID") && hAction.containsKey("ACTION")) {
				String strId = hAction.get("ID");
				if (hAction.get("ACTION").equalsIgnoreCase("EDIT")) {
					// load the data.
					DatabaseUtility.getDatabaseHandler(getActivity());
					Lead lead = new Lead(Constants.DBHANDLER);
					oLeadDetail = lead.getLeadDetailById(strId);
				} else if (hAction.get("ACTION").equalsIgnoreCase("LOAD")) {
					oLeadDetail = ContactUtility.getLeadFromPhoneBook(
							getActivity(), strId);
				}
			}
			DeviceUtility.log(TAG, oLeadDetail.toString());
			return oLeadDetail;
		}

		@Override
		protected void onPostExecute(LeadDetail result) {
			super.onPostExecute(result);
			lead = result;
			int iLeadSourcePosition = 0;
			int iPrefixPosition = 0;
			doShowSelectedSector(strSector);
			ArrayAdapter<String> prefixAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item);
			prefixAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			DeviceUtility.log(TAG, "PREFIX: " + aPrefix.size());
			for (int i = 0; i < aPrefix.size(); i++) {
				prefixAdapter.add(aPrefix.get(i).getText());
				if (result.getPrefix() != null) {
					if (result.getPrefix().equalsIgnoreCase(
							aPrefix.get(i).getValue())) {
						iPrefixPosition = i;
					}
				}
			}
			prefix.setAdapter(prefixAdapter);
			prefix.setSelection(iPrefixPosition);

			ArrayAdapter<String> aLeadSourceAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item);
			aLeadSourceAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			for (int i = 0; i < aLeadSource.size(); i++) {
				aLeadSourceAdapter.add(aLeadSource.get(i).getText());
				if (result.getLeadSource() != null) {
					if (result.getLeadSource().equalsIgnoreCase(
							aLeadSource.get(i).getValue())) {
						iLeadSourcePosition = i;
					}
				}
			}
			lead_source.setAdapter(aLeadSourceAdapter);
			lead_source.setSelection(iLeadSourcePosition);

			ArrayAdapter<String> aLeadStatusAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item);
			aLeadStatusAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			int iLeadStatus = 0;
			for (int i = 0; i < aLeadStatus.size(); i++) {
				aLeadStatusAdapter.add(aLeadStatus.get(i).getText());
				if (result.getLeadStatus() != null) {
					if (result.getLeadStatus().equalsIgnoreCase(
							aLeadStatus.get(i).getValue())) {
						iLeadStatus = i;
					}
				}
			}
			lead_status.setAdapter(aLeadStatusAdapter);
			lead_status.setSelection(iLeadStatus);

			ArrayAdapter<String> aAttitudeAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item);
			aAttitudeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			int iAttitude = 0;
			for (int i = 0; i < aAttitude.size(); i++) {
				aAttitudeAdapter.add(aAttitude.get(i).getText());
				if (result.getAttitude() != null) {
					if (result.getAttitude().equalsIgnoreCase(
							aAttitude.get(i).getValue())) {
						iAttitude = i;
					}
				}
			}
			attitude.setAdapter(aAttitudeAdapter);
			attitude.setSelection(iAttitude);

			ArrayAdapter<String> aIndustryAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item);
			aIndustryAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			int iIndustry = 0;
			for (int i = 0; i < aLeadIndustry.size(); i++) {
				aIndustryAdapter.add(aLeadIndustry.get(i).getText());
				if (result.getIndustry() != null) {
					if (result.getIndustry().equalsIgnoreCase(
							aLeadIndustry.get(i).getValue())) {
						iIndustry = i;
					}
				}
			}
			
			industry.setAdapter(aIndustryAdapter);
			industry.setSelection(iIndustry);
			
			if(result.getModifiedTimestamp() != null){
				Intent intent = new Intent();
				intent.putExtra("date", result.getModifiedTimestamp());
				intent.setAction("modifiedDate");
				getActivity().sendBroadcast(intent);
				}

			if (result.getFirstName() != null) {
				first_name.setText(result.getFirstName());
			}

			if (result.getLastName() != null) {
				last_name.setText(result.getLastName());
			}

			if (result.getCompanyName() != null) {
				company_name.setText(result.getCompanyName());
			}

			if (result.getJobTitle() != null) {
				job_title.setText(result.getJobTitle());
			}

			if (result.getMobile() != null) {
				mobile.setText(result.getMobile());
			}

			if (result.getWorkFax() != null) {
				work_fax.setText(formatFaxNumber(result.getWorkFax()));
			}

			if (result.getWorkPhone() != null) {
				work_phone.setText(formatPhoneNumber(result.getWorkPhone()));
			}

			if (result.getWebsite() != null) {
				website.setText(result.getWebsite());
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

			if (result.getNoOfEmployee() != null) {
				no_of_employee.setText(result.getNoOfEmployee());
			}

			if (result.getAnnualRevenue() != null) {
				annual_revenue.setText(result.getAnnualRevenue());
			}

			if (result.getMailingCity() != null) {
				city.setText(result.getMailingCity());
			}

			if (result.getMailingStreet() != null) {
				street.setText(result.getMailingStreet());
			}

			if (result.getMailingState() != null) {
				state.setText(result.getMailingState());
			}

			if (result.getMailingZip() != null) {
				zip.setText(result.getMailingZip());
			}

			if (result.getMailingCountry() != null) {
				country.setText(result.getMailingCountry());
			}

			if (result.getDescription() != null) {
				description.setText(result.getDescription());
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	private class SaveLead extends AsyncTask<LeadDetail, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(getActivity(), false,
					getString(R.string.processing));
		}

		@Override
		protected Boolean doInBackground(LeadDetail... params) {
			LeadDetail oLeadDetail = params[0];
			DeviceUtility.log(TAG, oLeadDetail.toString());
			DatabaseUtility.getDatabaseHandler(getActivity());
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
				GuiUtility.alert(getActivity(),
						getString(R.string.success_store_lead_title),
						getString(R.string.success_store_lead_desc),
						Gravity.CENTER, getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getActivity().finish();
							}
						}, "", null);
			} else {
				GuiUtility.alert(getActivity(),
						getString(R.string.fail_store_lead_title),
						getString(R.string.fail_store_lead_desc),
						getString(R.string.ok));
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction("editPage");
		filter.addAction("save");	
		filter.addAction("companyName");
		getActivity().registerReceiver(this.broadcastReceiver, filter);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("editPage")) {
				doLockField(false);
				// birthday.setOnClickListener(showDatePicker);
				// birthday_clear.setOnClickListener(clearBirthday);
			} else if (intent.getAction().equals("save")) {

//				doLockField(true);
				String strPrefix = aPrefix
						.get(prefix.getSelectedItemPosition()).getValue();
				String strFirstName = first_name.getText().toString();
				String strLastName = last_name.getText().toString();
				String strCompanyName = company_name.getText().toString();
				String strJobTitle = job_title.getText().toString();
				String strIndustry = aLeadIndustry.get(
						industry.getSelectedItemPosition()).getValue();
				String strMobile = mobile.getText().toString();
				String strWorkFax = work_fax.getText().toString();
				String strWorkPhone = work_phone.getText().toString();
				String strLeadSource = aLeadSource.get(
						lead_source.getSelectedItemPosition()).getValue();
				String strLeadStatus = aLeadStatus.get(
						lead_status.getSelectedItemPosition()).getValue();
				String strAttitude = aAttitude.get(
						attitude.getSelectedItemPosition()).getValue();
				String strWebsite = website.getText().toString();
				String strEmail = email.getText().toString();
				String strEmail2 = email2.getText().toString();
				String strEmail3 = email3.getText().toString();
				String strSkypeId = skype_id.getText().toString();
				String strNoOfEmployee = no_of_employee.getText().toString();
				String strAnnualRevenue = annual_revenue.getText().toString();
				String strStreet = street.getText().toString();
				String strCity = city.getText().toString();
				String strState = state.getText().toString();
				String strZip = zip.getText().toString();
				String strCountry = country.getText().toString();
				String strDescription = description.getText().toString();

				System.out.println("ok 1");
				Intent intent2 = new Intent();
				intent2.putExtra("strPrefix", strPrefix);
				
				intent2.putExtra("company_name", company_name.getText()
						.toString());
				intent2.putExtra("last_name", last_name.getText().toString());
				intent2.putExtra("first_name", first_name.getText().toString());
		
				intent2.putExtra("job_title", job_title.getText().toString());
				intent2.putExtra("mobile", mobile.getText().toString());
				
				intent2.putExtra("email", strEmail);//new
				
				intent2.putExtra("work_phone", work_phone.getText().toString());
				intent2.putExtra("work_fax", work_fax.getText().toString());
				intent2.putExtra("street", street.getText().toString());//new
				intent2.putExtra("city", city.getText().toString());//new
				intent2.putExtra("state", state.getText().toString());//new
				intent2.putExtra("zip", zip.getText().toString());//new
				intent2.putExtra("country", country.getText().toString());//new
				
//				intent2.putExtra("strIndustry", strIndustry);
//		
//		
//				intent2.putExtra("strLeadSource", strLeadSource);
//				intent2.putExtra("strLeadStatus", strLeadStatus);
//				intent2.putExtra("strAttitude", strAttitude);

				intent2.setAction("saveDone");
				getActivity().sendBroadcast(intent2);
			}else if(intent.getAction().equals("companyName")){
				company_name.setText(intent.getStringExtra("companyName"));
				companyId = intent.getStringExtra("companyId");
			}
		}
	};
}
