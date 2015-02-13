package com.trinerva.icrm.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.event.EventInvitee.StoreData;
import com.trinerva.icrm.object.CalendarDetail;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DateTimePicker;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class EventInfo extends Activity {
	private String TAG = "EventInfo";
	private static final int START_DATE_DIALOG_ID = 9807;
	private static final int END_DATE_DIALOG_ID = 9837;
	private static final int ALERT_DATE_DIALOG_ID = 9867;
	private static final int INVITEE_ID = 9897;
	private static final int NAME_TO_ID = 9899;
	private static final int EMAIL_SCHEDULE1_DIALOG_ID = 5313;
	private static final int EMAIL_SCHEDULE2_DIALOG_ID = 5314;
	private static final int EMAIL_SCHEDULE3_DIALOG_ID = 5315;
	private ArrayList<MasterInfo> aPriority, aAvailability, aCategory;
	private TextView invitee, start_date, end_date, alert, mEmailSchedule1, mEmailSchedule2, mEmailSchedule3;
	ImageView save;
	private EditText subject, location, description;
	private ToggleButton all_day, private_set, email_notification;
	private ImageView alert_clear, emailSchedule1_clear, emailSchedule2_clear,
			emailSchedule3_clear;;
	private View save_divider;
	private Spinner priority, availability, mStatusId, mCategory;
	// private String strEventId = "";
	private HashMap<String, String> hLoad = new HashMap<String, String>();
	private boolean bView = false;
	private boolean bActive = true;
	private Dialog loadingDialog;
	private CalendarDetail calendar;
	private String InviteeList = "";
	private String InviteePhoneList = "";
	private LinearLayout bottom_menu;
	private ImageView delete;
	private boolean bReload = true;
	// private String strDateFormat = "yyyy-MM-dd kk:mm";

	TableRow mCommentLayout;
	EditText mComment;

	public final int NOT_STARTED = 1;
	public final int COMPLETED = 4;
	public final int CANCELLED = 6;
	public final int POSTPONED = 7;
	int myStatus;

	int statusArrary[] = { NOT_STARTED, COMPLETED, CANCELLED, POSTPONED };

	boolean calendarAdd;
	String calendarDate;
	String nameTo;
	String nameToId;
	EditText mNameTo;
	boolean editNameTo;
	String checkNameToId;
	String checkNameToIdSet = null;
	// TextView mCategory;
	boolean otherPageTo;
	// ImageView mClearCategory;
	String strNameToName;
	boolean editNameToName = true;
	List<TextView> listData = new ArrayList<TextView>();
	boolean setNameToData = false;
	
	StoreData storeData;
	List<StoreData> leadData = new ArrayList<StoreData>();
	List<StoreData> contactData = new ArrayList<StoreData>();
	List<StoreData> userData = new ArrayList<StoreData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get extra.
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("ID")) {
				String strEventId = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strEventId);
				hLoad.put("ACTION", "EDIT");
				hLoad.put("ID", strEventId);
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
			nameToId = bundle.getString("NAME_TO");
			System.out.println("XXX = " + nameToId);
			calendarAdd = bundle.getBoolean("calendarAdd", false);
			calendarDate = bundle.getString("date");
			editNameTo = bundle.getBoolean("EDIT_NAME_TO", true);
			strNameToName = bundle.getString("NAME_TO_NAME");
			otherPageTo = bundle.getBoolean("OTHER_PAGE_TO", false);
		}
		setContentView(R.layout.event_info);
		bottom_menu = (LinearLayout) findViewById(R.id.bottom_menu);
		delete = (ImageView) findViewById(R.id.delete);
		invitee = (TextView) findViewById(R.id.invitee);
		save = (ImageView) findViewById(R.id.save);
		start_date = (TextView) findViewById(R.id.start_date);
		mNameTo = (EditText) findViewById(R.id.nameTo);
		mCategory = (Spinner) findViewById(R.id.category);

		if (nameToId != null) {
			if (getNameto(nameToId, "").equals("")) {
				mNameTo.setText(getNameto(nameToId, ""));
			} else {
				mNameTo.setText(strNameToName);
			}
		} else {
			if (strNameToName != null)
				mNameTo.setText(strNameToName);
		}

		SimpleDateFormat sdf;
		if (calendarAdd == true) {
			sdf = new SimpleDateFormat(calendarDate + " h:00");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd h:00");
		}

		start_date.setHint(sdf.format(new Date()));

		end_date = (TextView) findViewById(R.id.end_date);
		end_date.setHint(sdf.format(new Date()));
		alert = (TextView) findViewById(R.id.alert);
		alert.setText(sdf.format(new Date()));
		subject = (EditText) findViewById(R.id.subject);
		location = (EditText) findViewById(R.id.location);
		description = (EditText) findViewById(R.id.description);

		mEmailSchedule1 = (TextView) findViewById(R.id.emailSchedule1);
		mEmailSchedule2 = (TextView) findViewById(R.id.emailSchedule2);
		mEmailSchedule3 = (TextView) findViewById(R.id.emailSchedule3);

		all_day = (ToggleButton) findViewById(R.id.all_day);
		private_set = (ToggleButton) findViewById(R.id.private_set);
		email_notification = (ToggleButton) findViewById(R.id.email_notification);

		alert_clear = (ImageView) findViewById(R.id.alert_clear);
		emailSchedule1_clear = (ImageView) findViewById(R.id.emailSchedule1_clear);
		emailSchedule2_clear = (ImageView) findViewById(R.id.emailSchedule2_clear);
		emailSchedule3_clear = (ImageView) findViewById(R.id.emailSchedule3_clear);

		priority = (Spinner) findViewById(R.id.priority);
		availability = (Spinner) findViewById(R.id.availability);
		mStatusId = (Spinner) findViewById(R.id.statusId);

		save_divider = (View) findViewById(R.id.save_divider);

		mCommentLayout = (TableRow) findViewById(R.id.commentLayout);
		mComment = (EditText) findViewById(R.id.comment);
		start_date.setFocusable(false);
		end_date.setFocusable(false);
		alert.setFocusable(false);
		mEmailSchedule1.setFocusable(false);
		mEmailSchedule2.setFocusable(false);
		mEmailSchedule3.setFocusable(false);

		if (bView == true) {
			doLockField(true);
			if (bActive) {
				bottom_menu.setVisibility(View.VISIBLE);
				delete.setOnClickListener(doShowPopUpDelete);
				invitee.setOnClickListener(doShowInvitedList);
				save.setImageResource(R.drawable.ic_icon_edit);
				save.setOnClickListener(doChangeToSave);
			} else {
				bottom_menu.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
				save_divider.setVisibility(View.GONE);
				invitee.setOnClickListener(doShowInvitedList); //added by jared
			}
		} else {
			doLockField(false);
			bottom_menu.setVisibility(View.GONE);
			start_date.setOnClickListener(doShowDatePicker);
			end_date.setOnClickListener(doShowDatePicker);
			alert.setOnClickListener(doShowDatePicker);
			
			invitee.setOnClickListener(doShowInviteeList);
			save.setOnClickListener(doSaveEvent);
			alert_clear.setOnClickListener(doClearAlert);
			// ca
			emailSchedule1_clear.setOnClickListener(doClearEmailSchedule1);
			emailSchedule2_clear.setOnClickListener(doClearEmailSchedule2);
			emailSchedule3_clear.setOnClickListener(doClearEmailSchedule3);

			mEmailSchedule1.setOnClickListener(doShowDatePicker);
			mEmailSchedule2.setOnClickListener(doShowDatePicker);
			mEmailSchedule3.setOnClickListener(doShowDatePicker);

			mNameTo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(EventInfo.this, NameToList.class);
					startActivityForResult(intent, NAME_TO_ID);

				}
			});
		}

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		switch (listData.size()) {
		case 0:
			mEmailSchedule1.setVisibility(View.VISIBLE);
			mEmailSchedule2.setVisibility(View.GONE);
			emailSchedule2_clear.setVisibility(View.GONE);
			mEmailSchedule3.setVisibility(View.GONE);
			emailSchedule3_clear.setVisibility(View.GONE);
			break;
		case 1:
			mEmailSchedule1.setVisibility(View.VISIBLE);
			mEmailSchedule2.setVisibility(View.VISIBLE);
			emailSchedule2_clear.setVisibility(View.VISIBLE);
			mEmailSchedule3.setVisibility(View.GONE);
			emailSchedule3_clear.setVisibility(View.GONE);
			break;
		case 2:
			mEmailSchedule1.setVisibility(View.VISIBLE);
			mEmailSchedule2.setVisibility(View.VISIBLE);
			mEmailSchedule3.setVisibility(View.VISIBLE);
			mEmailSchedule1.setVisibility(View.VISIBLE);
			emailSchedule1_clear.setVisibility(View.VISIBLE);
			mEmailSchedule2.setVisibility(View.VISIBLE);
			emailSchedule2_clear.setVisibility(View.VISIBLE);
			mEmailSchedule3.setVisibility(View.VISIBLE);
			emailSchedule3_clear.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
		if (bReload) {
			LoadData load = new LoadData();
			load.execute(hLoad);
		} else {
			bReload = true;

		}

		// mCategory.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Master master = new Master(Constants.DBHANDLER);
		// List<MasterInfo> masterList = master
		// .getAllMasterByType("eventcategory");
		//
		// final String[] str = new String[masterList.size()];
		//
		// for (int i = 0; i < str.length; i++) {
		// str[i] = masterList.get(i).getText();
		// }
		//
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// EventInfo.this).setItems(str,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // The 'which' argument contains the index
		// // position
		// // of the selected item
		// mCategory.setText(str[which]);
		// }
		// });
		// builder.show();
		// }
		// });
		
		
		if(!editNameTo){
			mNameTo.setEnabled(false);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("saveTrue")) {
				save.setClickable(true);
			}else if (intent.getAction().equals("setNameTo")) {
				setNameToData = true;
				String nameToContact = intent.getStringExtra("nameToContact");
				nameToId = intent.getStringExtra(Constants.NAME_TO_ID);
				mNameTo.setText(nameToContact);
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		DeviceUtility.log(TAG, "onResume");

		IntentFilter filter = new IntentFilter();
		filter.addAction("saveTrue");
		filter.addAction("setNameTo");
		registerReceiver(this.broadcastReceiver, filter);
	}

	private OnClickListener doChangeToSave = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLockField(false);
			save.setClickable(false);
			//check here jared
			if(checkDBAvailable()){
				invitee.setOnClickListener(doShowInviteeList);
			}else{
				invitee.setOnClickListener(null);
				invitee.setEnabled(false);
			}			
			
			bottom_menu.setVisibility(View.GONE);
			save.setImageResource(R.drawable.ic_icon_save);
			start_date.setOnClickListener(doShowDatePicker);
			end_date.setOnClickListener(doShowDatePicker);
			alert.setOnClickListener(doShowDatePicker);
			save.setOnClickListener(doSaveEvent);
			alert_clear.setOnClickListener(doClearAlert);
			emailSchedule1_clear.setOnClickListener(doClearEmailSchedule1);
			emailSchedule2_clear.setOnClickListener(doClearEmailSchedule2);
			emailSchedule3_clear.setOnClickListener(doClearEmailSchedule3);
			mEmailSchedule1.setOnClickListener(doShowDatePicker);
			mEmailSchedule2.setOnClickListener(doShowDatePicker);
			mEmailSchedule3.setOnClickListener(doShowDatePicker);
			
			switch (listData.size()) {
			case 0:
				mEmailSchedule1.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.GONE);
				emailSchedule2_clear.setVisibility(View.GONE);
				mEmailSchedule3.setVisibility(View.GONE);
				emailSchedule3_clear.setVisibility(View.GONE);
				break;
			case 1:
				mEmailSchedule1.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.VISIBLE);
				emailSchedule2_clear.setVisibility(View.VISIBLE);
				mEmailSchedule3.setVisibility(View.GONE);
				emailSchedule3_clear.setVisibility(View.GONE);
				break;
			case 2:
				mEmailSchedule1.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.VISIBLE);
				mEmailSchedule3.setVisibility(View.VISIBLE);
				mEmailSchedule1.setVisibility(View.VISIBLE);
				emailSchedule1_clear.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.VISIBLE);
				emailSchedule2_clear.setVisibility(View.VISIBLE);
				mEmailSchedule3.setVisibility(View.VISIBLE);
				emailSchedule3_clear.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener doShowInviteeList = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent inviteelist = new Intent(EventInfo.this, EventInvitee.class);
			System.out.println("showsssssssss = " + InviteeList);
			inviteelist.putExtra("SELECTED_INVITEE", InviteeList);
			EventInfo.this.startActivityForResult(inviteelist, INVITEE_ID);
		}
	};
	
	private boolean checkDBAvailable(){
		if (!InviteeList.equals("")) {
			String s = new String(InviteeList);
			String a[] = s.split(";");
			
			String t = s.replaceAll(Pattern.quote("|*|"), "-");
			a = t.split(";");

			for(int i = 0; i<a.length; i++){
				a[i] = a[i].substring(0, 38);
			}

			for (int i = 0; i < a.length; i++) {
				System.out.println("aaa[i] = " + a[i]);
				if (a[i].substring(0, 1).equals("0")) {
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
		
		String LeadString = "";
		String ContactString = "";
		String UserString="";
		Lead lead = new Lead(Constants.DBHANDLER);
		Contact contact = new Contact(Constants.DBHANDLER);
		Master master = new Master(Constants.DBHANDLER);
		
		for (int i = 0; i < leadData.size(); i++) {
			if (i == 0) {
				LeadString = "'" + leadData.get(i).id + "'";
			} else {
				LeadString = LeadString + ",'" + leadData.get(i).id + "'";
			}
		}
		
		for (int i = 0; i < contactData.size(); i++) {
			if (i == 0) {
				ContactString = "'" + contactData.get(i).id + "'";
			} else {
				ContactString = ContactString + ",'" + contactData.get(i).id + "'";
			}
		}
		
		for (int i = 0; i < userData.size(); i++) {
			if (i == 0) {
				UserString = "'" + userData.get(i).id + "'";
			} else {
				UserString = UserString + ",'" + userData.get(i).id + "'";
			}
		}
		
		Cursor leadCursor = lead.getSelectLeadInviteeListDisplayByFilter("", LeadString);
		Cursor contactCursor = contact.getSelectedContactListDisplayByFilter("",ContactString);
		//Cursor UserlistCursor  = master.getAllMasterCompanyFilter("UserList", "", UserString);
		//System.out.println("USER STRING IS "+ UserString);
		
		if(leadData.size() == leadCursor.getCount()){
			System.out.println("LEAD SAME SIZE");
		}else{
			System.out.println("LEAD NOT SAME SIZE");
			System.out.println("leadData.size() " + leadData.size());
			System.out.println("leadCursor.getCount()" + leadCursor.getCount());
			return false;
		}
		
		if(contactData.size() == contactCursor.getCount()){
			System.out.println("CONTACT SAME SIZE");
		}else{
			System.out.println("CONTACT NOT SAME SIZE");
			System.out.println("contactData.size() " + contactData.size());
			System.out.println("contactCursor.getCount()" + contactCursor.getCount());
			return false;
		}
//		if(userData.size() == UserlistCursor.getCount()){
//			System.out.println("USER SAME SIZE");
//		}else{
//			System.out.println("USER NOT SAME SIZE");
//			System.out.println("userData.size() " + userData.size());
//			System.out.println("UserlistCursor.getCount()" + UserlistCursor.getCount());
//			return false;
//		}		
		return true;
	}
	
	class StoreData {
		String id;
		int removeId;
		String strBy;
	}

	private OnClickListener doShowInvitedList = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DeviceUtility
					.log(TAG, "doShowInvitedList" + calendar.getInvitees());
			
			System.out.println("show = " + calendar.getInvitees());
			Intent inviteelist = new Intent(EventInfo.this, InvitedList.class);
			inviteelist.putExtra("LIST", calendar.getInvitees());
			EventInfo.this.startActivity(inviteelist);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		DeviceUtility.log(TAG, "onActivityResult(" + requestCode + ","
				+ resultCode + ")");
		switch (requestCode) {
		case INVITEE_ID:
			if (resultCode == RESULT_OK) {
				bReload = false;
				int TOTAL = data.getIntExtra("TOTAL", 0);
				InviteeList = data.getStringExtra("EMAIL");
				// InviteePhoneList = data.getStringExtra("PHONE");
				DeviceUtility.log(TAG, "TOTAL: " + TOTAL);
				DeviceUtility.log(TAG, "InviteeList: " + InviteeList);
				String strNumber = getString(R.string.event_invitee_member_number);
				strNumber = strNumber
						.replace("[NUMBER]", String.valueOf(TOTAL));
				DeviceUtility.log(TAG, "strNumber: " + strNumber);
				invitee.setText(strNumber);
			}
			break;
		case NAME_TO_ID:
			if(setNameToData){
				setNameToData = false;
			}else{
			try {
				nameToId = data.getStringExtra("NameToId");
				if (nameToId != null) {
					
					System.out.println("xxxx = " + nameToId);
					checkNameToIdSet = null;
					String[] nameToDBId = nameToId.split("-");
					System.out.println("set xxxx = " + nameToDBId[0]);
					if (nameToDBId[0].equals("3")) {
						calendar.setEventNameTo("0");
					} else {
						calendar.setEventNameTo(nameToDBId[0]);
					}
					mNameTo.setText(getNameto(nameToId, ""));

					checkNameToId = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			break;
		}
	}

	private OnClickListener doShowPopUpDelete = new OnClickListener() {
		@Override
		public void onClick(View v) {
			GuiUtility.alert(EventInfo.this, "",
					getString(R.string.confirm_delete_event), Gravity.CENTER,
					getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}, getString(R.string.delete),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							Dialog loading = GuiUtility.getLoadingDialog(
									EventInfo.this, false,
									getString(R.string.processing));
							DatabaseUtility.getDatabaseHandler(EventInfo.this);
							FLCalendar calendar = new FLCalendar(
									Constants.DBHANDLER);
							// calendar.delete(strEventId);
							calendar.delete(EventInfo.this, hLoad.get("ID")
									.toString());
							loading.dismiss();
							// back to contact list.
							EventInfo.this.finish();

						}
					});
		}
	};

	private OnClickListener doSaveEvent = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLockField(true);
			String strPriority = "";
			if (priority.getSelectedItemPosition() >= 0) {
				strPriority = aPriority.get(priority.getSelectedItemPosition())
						.getValue();
			} else {
				strPriority = aPriority.get(0).getValue();
			}

			String strCategory = "";
			if (mCategory.getSelectedItemPosition() >= 0) {
				strCategory = aCategory
						.get(mCategory.getSelectedItemPosition()).getValue();
			} else {
				strCategory = aCategory.get(0).getValue();
			}

			String strAvailability = "";
			if (availability.getSelectedItemPosition() >= 0) {
				strAvailability = aAvailability.get(
						availability.getSelectedItemPosition()).getValue();
			} else {
				strAvailability = aAvailability.get(0).getValue();
			}

			System.out.println("Select = "
					+ mStatusId.getSelectedItemPosition());

			switch (mStatusId.getSelectedItemPosition()) {
			case 0:
				myStatus = NOT_STARTED;
				mCommentLayout.setVisibility(View.GONE);
				break;

			case 1:
				myStatus = COMPLETED;
				mCommentLayout.setVisibility(View.VISIBLE);
				break;
			case 2:
				myStatus = CANCELLED;
				mCommentLayout.setVisibility(View.VISIBLE);
				break;
			case 3:
				myStatus = POSTPONED;
				mCommentLayout.setVisibility(View.VISIBLE);
				break;
			default:
				mCommentLayout.setVisibility(View.GONE);
				break;
			}
			String strNameTo = mNameTo.getText().toString();
			String strSubject = subject.getText().toString();
			String strLocation = location.getText().toString();
			String strStartDate = start_date.getText().toString();
			String strEndDate = end_date.getText().toString();
			String strAlert = alert.getText().toString();
			String strEmailSchedule1 = mEmailSchedule1.getText().toString();
			String strEmailSchedule2 = mEmailSchedule2.getText().toString();
			String strEmailSchedule3 = mEmailSchedule3.getText().toString();

			String strAllDay = String.valueOf(all_day.getText().toString()
					.equalsIgnoreCase(getString(R.string.event_on)));
			String strPrivate = String.valueOf(private_set.getText().toString()
					.equalsIgnoreCase(getString(R.string.event_on)));
			String strEmailNotification = String.valueOf(email_notification
					.getText().toString()
					.equalsIgnoreCase(getString(R.string.event_on)));
			String strDescription = description.getText().toString();
			String strComment = mComment.getText().toString();

			if (checkSpace(strSubject).equals("")) {
				GuiUtility.alert(EventInfo.this, "",
						getString(R.string.subject_is_required),
						getString(R.string.ok));
				doLockField(false);
				subject.requestFocus();
			} else if (strStartDate.length() == 0) {
				GuiUtility.alert(EventInfo.this, "",
						getString(R.string.start_date_is_required),
						getString(R.string.ok));
				doLockField(false);
				start_date.requestFocus();
			} else if (strEndDate.length() == 0) {
				GuiUtility.alert(EventInfo.this, "",
						getString(R.string.end_date_is_required),
						getString(R.string.ok));
				doLockField(false);
				end_date.requestFocus();
			} else {
				// check start date n end date.
				Date dStartDate = Utility.stringToDate(strStartDate,
						Constants.DATETIME_FORMAT);
				Date dEndDate = Utility.stringToDate(strEndDate,
						Constants.DATETIME_FORMAT);
				DeviceUtility.log(TAG, "start date:" + dStartDate.getTime());
				DeviceUtility.log(TAG, "end date:" + dEndDate.getTime());
				if (!dStartDate.before(dEndDate)) {
					GuiUtility.alert(EventInfo.this, "",
							getString(R.string.end_date_start_date_is_same),
							getString(R.string.ok));
					doLockField(false);
					end_date.requestFocus();
				} else {
					// save
					if (hLoad.containsKey("ACTION")) {
						if (hLoad.get("ACTION").equalsIgnoreCase("EDIT")) {
							calendar.setInternalNum(hLoad.get("ID"));
						}
					}
					calendar.setPriority(strPriority);
					calendar.setAvailability(strAvailability);
					calendar.setCategory(strCategory);
					calendar.setSubject(strSubject);
					calendar.setNameToId(nameToId);
					calendar.setEmailSchedule1(strEmailSchedule1);
					calendar.setEmailSchedule2(strEmailSchedule2);
					calendar.setEmailSchedule3(strEmailSchedule3);
					calendar.setUserDef7(String.valueOf(myStatus));

					// if (strCategory != null && !strCategory.equals("")) {
					// Master master = new Master(Constants.DBHANDLER);
					// List<MasterInfo> masterList = master
					// .getAllMasterByTypeCategory("eventcategory",
					// strCategory);
					// calendar.setCategory(masterList.get(0).getValue());
					// }else{
					// calendar.setCategory(null);
					// }
					if (myStatus == COMPLETED || myStatus == CANCELLED || myStatus == POSTPONED) {
						calendar.setUserDef3(mComment.getText().toString());
					} else {
						calendar.setUserDef3(null);
					}

					if (strLocation.length() == 0) {
						calendar.setLocation(null);
					} else {
						calendar.setLocation(strLocation);
					}

					calendar.setStartDate(strStartDate);
					calendar.setEndDate(strEndDate);

					if (strAlert.length() == 0) {
						calendar.setAlert(null);
					} else {
						calendar.setAlert(strAlert);
					}

					calendar.setAllDay(strAllDay);
					calendar.setIsPrivate(strPrivate);
					calendar.setEmailNotification(strEmailNotification);

					if (strEmailSchedule1.length() == 0) {
						calendar.setEmailSchedule1(null);
					} else {
						calendar.setEmailSchedule1(strEmailSchedule1);
					}

					if (strEmailSchedule2.length() == 0) {
						calendar.setEmailSchedule2(null);
					} else {
						calendar.setEmailSchedule2(strEmailSchedule2);
					}

					if (strEmailSchedule3.length() == 0) {
						calendar.setEmailSchedule3(null);
					} else {
						calendar.setEmailSchedule3(strEmailSchedule3);
					}

					if (strDescription.length() == 0) {
						calendar.setDescription(null);
					} else {
						calendar.setDescription(strDescription);
					}

					// if (strComment.length() == 0) {
					// calendar.setUserDef1(null);
					// } else {
					// calendar.setUserDef1(strDescription);
					// }

					calendar.setInvitees(InviteeList);

					// get owner
					String strOwner = Utility.getConfigByText(EventInfo.this,
							"USER_EMAIL");
					if (strOwner.length() > 0) {
						if (calendar.getInternalNum() == null) {
							calendar.setOwner(strOwner);
						}
						calendar.setUserStamp(strOwner);
					}

					try {

						System.out.println("no ? = " + nameToId);
						if (nameToId != null && nameToId.length() < 10) {
							String[] nameToDBId = nameToId.split("-");
							if (!nameToDBId[0].equals("3")) {
								System.out.println("Sync 3");
								calendar.setEventNameTo("3");
							} else {
								System.out.println("Sync 0");
								calendar.setEventNameTo("0");
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						calendar.setEventNameTo("0");
					}

					SaveEvent task = new SaveEvent();
					task.execute(calendar);
				}
			}
		}
	};

	private OnClickListener doShowDatePicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.start_date:
				showDateTimeDialog(START_DATE_DIALOG_ID);
				DeviceUtility.log(TAG, "START DATE DIALOG REQUEST");
				break;
			case R.id.end_date:
				showDateTimeDialog(END_DATE_DIALOG_ID);
				DeviceUtility.log(TAG, "END DATE DIALOG REQUEST");
				break;
			case R.id.alert:
				showDateTimeDialog(ALERT_DATE_DIALOG_ID);
				DeviceUtility.log(TAG, "ALERT DATE DIALOG REQUEST");
				break;
			case R.id.emailSchedule1:
				showDateTimeDialog(EMAIL_SCHEDULE1_DIALOG_ID);
				DeviceUtility.log(TAG, "ALERT DATE DIALOG REQUEST");
				break;
			case R.id.emailSchedule2:
				showDateTimeDialog(EMAIL_SCHEDULE2_DIALOG_ID);
				DeviceUtility.log(TAG, "ALERT DATE DIALOG REQUEST");
				break;
			case R.id.emailSchedule3:
				showDateTimeDialog(EMAIL_SCHEDULE3_DIALOG_ID);
				DeviceUtility.log(TAG, "ALERT DATE DIALOG REQUEST");
				break;
			}
		}
	};

	private OnClickListener doClearEmailSchedule1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mEmailSchedule1.setText("");
		}
	};

	private OnClickListener doClearEmailSchedule2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mEmailSchedule2.setText("");
		}
	};

	private OnClickListener doClearEmailSchedule3 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mEmailSchedule3.setText("");
		}
	};

	private void showDateTimeDialog(int iDialogId) {
		DeviceUtility.log(TAG, "showDateTimeDialog(" + iDialogId + ")");
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);

		final Button btnOk = (Button) mDateTimeDialogView
				.findViewById(R.id.SetDateTime);
		final Button btnCancel = (Button) mDateTimeDialogView
				.findViewById(R.id.CancelDialog);
		final Button btnReset = (Button) mDateTimeDialogView
				.findViewById(R.id.ResetDateTime);
		Date date = null;

		switch (iDialogId) {
		case START_DATE_DIALOG_ID:
			if (start_date.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(start_date.getText().toString());
				} catch (ParseException e2) {
					e2.printStackTrace();
					date = null;
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.clearFocus();
					start_date.setText(new SimpleDateFormat(
							Constants.DATETIME_FORMAT).format(new Date(
							mDateTimePicker.getDateTimeMillis())));
					if (end_date.getText().toString().length() == 0) {
						end_date.setText(new SimpleDateFormat(
								Constants.DATETIME_FORMAT).format(new Date(
								mDateTimePicker.getDateTimeMillis()
										+ (60 * 60 * 1000))));
					}
					mDateTimeDialog.dismiss();
				}
			});

			// Cancel the dialog when the "Cancel" button is clicked
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimeDialog.cancel();
				}
			});

			// Reset Date and Time pickers when the "Reset" button is clicked
			btnReset.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});
			break;
		case END_DATE_DIALOG_ID:
			if (end_date.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(end_date.getText().toString());
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
				}
			} else if (start_date.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(start_date.getText().toString());
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.clearFocus();
					end_date.setText(new SimpleDateFormat(
							Constants.DATETIME_FORMAT).format(new Date(
							mDateTimePicker.getDateTimeMillis())));
					mDateTimeDialog.dismiss();
				}
			});

			// Cancel the dialog when the "Cancel" button is clicked
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimeDialog.cancel();
				}
			});

			// Reset Date and Time pickers when the "Reset" button is clicked
			btnReset.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});
			break;
		case ALERT_DATE_DIALOG_ID:
			if (alert.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(alert.getText().toString());
				} catch (ParseException e) {
					date = null;
					e.printStackTrace();
				}
			} else if (start_date.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(start_date.getText().toString());
					// make it early 15min as default alert.
					long lStartDate = date.getTime();
					date.setTime(lStartDate - (15 * 60 * 1000));
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.clearFocus();
					alert.setText(new SimpleDateFormat(
							Constants.DATETIME_FORMAT).format(new Date(
							mDateTimePicker.getDateTimeMillis())));
					mDateTimeDialog.dismiss();
				}
			});

			// Cancel the dialog when the "Cancel" button is clicked
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimeDialog.cancel();
				}
			});

			// Reset Date and Time pickers when the "Reset" button is clicked
			btnReset.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});

			break;
		case EMAIL_SCHEDULE1_DIALOG_ID:
			if (mEmailSchedule1.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(mEmailSchedule1.getText().toString());

					if (mEmailSchedule1.getText().toString().length() == 0) {
						date.setMinutes(0);
					}
				} catch (ParseException e2) {
					e2.printStackTrace();
					date = null;
					date = new Date();
					date.setMinutes(0);
				}
			} else {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(mEmailSchedule1.getText().toString());
					if (mEmailSchedule1.getText().toString().length() == 0) {
						date.setMinutes(0);
					}
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
					date = new Date();
					date.setMinutes(0);
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					mDateTimePicker.clearFocus();
					// task_alert.setText(DateFormat.format(strDateFormat,
					// mDateTimePicker.getDateTimeMillis()));
					mEmailSchedule1.setText(new SimpleDateFormat(
							Constants.DATETIME_FORMAT).format(new Date(
							mDateTimePicker.getDateTimeMillis())));
					mDateTimeDialog.dismiss();

					mEmailSchedule2.setVisibility(View.VISIBLE);
					emailSchedule2_clear.setVisibility(View.VISIBLE);
				}
			});

			// Cancel the dialog when the "Cancel" button is clicked
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimeDialog.cancel();
				}
			});

			// Reset Date and Time pickers when the "Reset" button is clicked
			btnReset.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});
			break;
		case EMAIL_SCHEDULE2_DIALOG_ID:
			if (mEmailSchedule2.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(mEmailSchedule2.getText().toString());

					if (mEmailSchedule2.getText().toString().length() == 0) {
						date.setMinutes(0);
					}
				} catch (ParseException e2) {
					e2.printStackTrace();
					date = null;
					date = new Date();
					date.setMinutes(0);
				}
			} else {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(mEmailSchedule2.getText().toString());
					if (mEmailSchedule2.getText().toString().length() == 0) {
						date.setMinutes(0);
					}
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
					date = new Date();
					date.setMinutes(0);
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					mDateTimePicker.clearFocus();
					// task_alert.setText(DateFormat.format(strDateFormat,
					// mDateTimePicker.getDateTimeMillis()));
					mEmailSchedule2.setText(new SimpleDateFormat(
							Constants.DATETIME_FORMAT).format(new Date(
							mDateTimePicker.getDateTimeMillis())));
					mDateTimeDialog.dismiss();
					mEmailSchedule3.setVisibility(View.VISIBLE);
					emailSchedule3_clear.setVisibility(View.VISIBLE);

				}
			});

			// Cancel the dialog when the "Cancel" button is clicked
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimeDialog.cancel();
				}
			});

			// Reset Date and Time pickers when the "Reset" button is clicked
			btnReset.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});
			break;
		case EMAIL_SCHEDULE3_DIALOG_ID:
			if (mEmailSchedule3.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(mEmailSchedule3.getText().toString());

					if (mEmailSchedule3.getText().toString().length() == 0) {
						date.setMinutes(0);
					}
				} catch (ParseException e2) {
					e2.printStackTrace();
					date = null;
					date = new Date();
					date.setMinutes(0);
				}
			} else {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(mEmailSchedule3.getText().toString());

					if (mEmailSchedule3.getText().toString().length() == 0) {
						date.setMinutes(0);
					}
				} catch (ParseException e2) {
					e2.printStackTrace();
					date = null;
					date = new Date();
					date.setMinutes(0);
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					mDateTimePicker.clearFocus();
					// task_alert.setText(DateFormat.format(strDateFormat,
					// mDateTimePicker.getDateTimeMillis()));
					mEmailSchedule3.setText(new SimpleDateFormat(
							Constants.DATETIME_FORMAT).format(new Date(
							mDateTimePicker.getDateTimeMillis())));
					mDateTimeDialog.dismiss();
				}
			});

			// Cancel the dialog when the "Cancel" button is clicked
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimeDialog.cancel();
				}
			});

			// Reset Date and Time pickers when the "Reset" button is clicked
			btnReset.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});
			break;
		}
		// Setup TimePicker
		mDateTimePicker.setIs24HourView(true);
		if (date != null) {
			mDateTimePicker.updateDateTime(date.getTime());
		}

		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}

	private OnClickListener doClearAlert = new OnClickListener() {

		@Override
		public void onClick(View v) {
			alert.setText("");
		}
	};

	private class LoadData extends
			AsyncTask<HashMap<String, String>, Void, CalendarDetail> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventInfo.this, false,
					null);
		}

		@Override
		protected CalendarDetail doInBackground(
				HashMap<String, String>... params) {
			aPriority = (ArrayList<MasterInfo>) Utility.getMasterByType(
					EventInfo.this, Constants.MASTER_CALENDAR_PRIORITY);
			aAvailability = (ArrayList<MasterInfo>) Utility.getMasterByType(
					EventInfo.this, Constants.MASTER_CALENDAR_AVAILABILITY);
			System.out
					.println("kiat 3 = " + Constants.MASTER_CALENDAR_CATEGORY);
			aCategory = (ArrayList<MasterInfo>) Utility.getMasterByType(
					EventInfo.this, Constants.MASTER_CALENDAR_CATEGORY);
			HashMap<String, String> hAction = params[0];
			CalendarDetail oCalendar = new CalendarDetail();

			if (hAction.containsKey("ID") && hAction.containsKey("ACTION")) {
				String strId = hAction.get("ID");
				if (hAction.get("ACTION").equalsIgnoreCase("EDIT")) {
					// load the data.
					DatabaseUtility.getDatabaseHandler(EventInfo.this);
					FLCalendar oCalendarSource = new FLCalendar(
							Constants.DBHANDLER);
					oCalendar = oCalendarSource.getCalendarDetailById(strId);
				}
			}
			return oCalendar;
		}

		@Override
		protected void onPostExecute(CalendarDetail result) {
			super.onPostExecute(result);
			calendar = result;
			int iPrioritySelection = 0;
			int iAvailabilitySelection = 0;
			int iStatusSelection = 0;
			int iCategorySelection = 0;

			ArrayAdapter<String> aStatusAdapter = new ArrayAdapter<String>(
					EventInfo.this, android.R.layout.simple_spinner_item);
			aStatusAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// DeviceUtility.log(TAG, "aPriority.size(): " + aPriority.size());
			// DeviceUtility.log(TAG, "result.getPriority(): " +
			// result.getPriority());

			String str[] = { "Not Started", "Completed", "Cancelled", "Postponed" };

			int selectStatus = 0;

			for (int i = 0; i < str.length; i++) {
				aStatusAdapter.add(str[i]);
				try {
					if (calendar.getUserDef7() != null) {
						if (calendar.getUserDef7().equals(
								String.valueOf(statusArrary[i]))) {
							selectStatus = i;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("inf o 7 " + calendar.getUserDef7());
			mStatusId.setAdapter(aStatusAdapter);
			mStatusId.setSelection(selectStatus);

			mStatusId.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg2) {
					case 1:
						mCommentLayout.setVisibility(View.VISIBLE);
						mCommentLayout.requestFocus();
						break;
					case 2:
						mCommentLayout.setVisibility(View.VISIBLE);
						mCommentLayout.requestFocus();
						break;
					case 3:
						mCommentLayout.setVisibility(View.VISIBLE);
						mCommentLayout.requestFocus();
						break;
					default:
						mCommentLayout.setVisibility(View.GONE);
						mComment.setText("");
						break;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			ArrayAdapter<String> aPriorityAdapter = new ArrayAdapter<String>(
					EventInfo.this, android.R.layout.simple_spinner_item);
			aPriorityAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			DeviceUtility.log(TAG, "aPriority.size(): " + aPriority.size());
			DeviceUtility.log(TAG,
					"result.getPriority(): " + result.getPriority());
			for (int i = 0; i < aPriority.size(); i++) {
				aPriorityAdapter.add(aPriority.get(i).getText());
				if (result.getPriority() != null) {
					if (result.getPriority().equalsIgnoreCase(
							aPriority.get(i).getValue())) {
						iPrioritySelection = i;
					}
				}
			}
			priority.setAdapter(aPriorityAdapter);
			priority.setSelection(iPrioritySelection);

			ArrayAdapter<String> aAvailabilityAdapter = new ArrayAdapter<String>(
					EventInfo.this, android.R.layout.simple_spinner_item);
			aAvailabilityAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			for (int i = 0; i < aAvailability.size(); i++) {
				aAvailabilityAdapter.add(aAvailability.get(i).getText());
				if (result.getAvailability() != null) {
					if (result.getAvailability().equalsIgnoreCase(
							aAvailability.get(i).getValue())) {
						iAvailabilitySelection = i;
					}
				}
			}
			availability.setAdapter(aAvailabilityAdapter);
			availability.setSelection(iAvailabilitySelection);

			ArrayAdapter<String> aCategoryAdapter = new ArrayAdapter<String>(
					EventInfo.this, android.R.layout.simple_spinner_item);
			aCategoryAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			for (int i = 0; i < aCategory.size(); i++) {
				aCategoryAdapter.add(aCategory.get(i).getText());
				if (result.getCategory() != null) {
					if (result.getCategory().equalsIgnoreCase(
							aCategory.get(i).getValue())) {
						iCategorySelection = i;
					}
				}
			}
			// for (int i = 0; i < aCategory.size(); i++) {
			// System.out.println("get ca = "+aCategory.get(i).getText());
			// aCategoryAdapter.add(aCategory.get(i).getText());
			// }
			// System.out.println("now is = "+result.getCategory());
			// if (result.getCategory() == null) {
			// for (int j = 0; j < aCategory.size(); j++) {
			// if(aCategory.get(j).getDefaultValue() == true){
			// iCategorySelection = j;
			// break;
			// }
			// }
			// }else{
			// for (int i = 0; i < aCategory.size(); i++) {
			//
			// if (result.getCategory() != null) {
			// if (result.getCategory().equalsIgnoreCase(
			// aCategory.get(i).getValue())) {
			// iCategorySelection = i;
			// }
			// }
			// }
			// }
			mCategory.setAdapter(aCategoryAdapter);

			if (bView == false) {
				for (int i = 0; i < aCategory.size(); i++) {
					if (aCategory.get(i).getDefaultValue()) {
						iCategorySelection = i;
						break;
					}
				}
			}
			mCategory.setSelection(iCategorySelection);

			if (result.getSubject() != null) {
				subject.setText(result.getSubject());
			}

			if (result.getEmailSchedule1() != null) {
				mEmailSchedule1.setText(result.getEmailSchedule1());
			}

			if (result.getEmailSchedule2() != null) {
				mEmailSchedule2.setText(result.getEmailSchedule2());
			}

			if (result.getEmailSchedule3() != null) {
				mEmailSchedule3.setText(result.getEmailSchedule3());
			}

			if (mEmailSchedule1.getText().toString().length() != 0) {
				listData.add(mEmailSchedule1);
			}
			if (mEmailSchedule2.getText().toString().length() != 0) {
				listData.add(mEmailSchedule2);
			}
			if (mEmailSchedule3.getText().toString().length() != 0) {
				listData.add(mEmailSchedule3);
			}
			System.out.println("my size " + listData.size());

			
	switch (listData.size()) {
	case 1:
		mEmailSchedule1.setVisibility(View.VISIBLE);
		mEmailSchedule2.setVisibility(View.GONE);
		emailSchedule2_clear.setVisibility(View.GONE);
		mEmailSchedule3.setVisibility(View.GONE);
		emailSchedule3_clear.setVisibility(View.GONE);
		break;
	case 2:
//		mEmailSchedule1.setVisibility(View.VISIBLE);
//		mEmailSchedule2.setVisibility(View.VISIBLE);
//		mEmailSchedule3.setVisibility(View.VISIBLE);
//		mEmailSchedule1.setVisibility(View.VISIBLE);
//		emailSchedule1_clear.setVisibility(View.VISIBLE);
//		mEmailSchedule2.setVisibility(View.VISIBLE);
//		emailSchedule2_clear.setVisibility(View.VISIBLE);
//		mEmailSchedule3.setVisibility(View.VISIBLE);
//		emailSchedule3_clear.setVisibility(View.VISIBLE);
		mEmailSchedule1.setVisibility(View.VISIBLE);
		mEmailSchedule2.setVisibility(View.VISIBLE);
		emailSchedule2_clear.setVisibility(View.VISIBLE);
		mEmailSchedule3.setVisibility(View.GONE);
		emailSchedule3_clear.setVisibility(View.GONE);
		break;
	case 3:
		mEmailSchedule1.setVisibility(View.VISIBLE);
		mEmailSchedule2.setVisibility(View.VISIBLE);
		mEmailSchedule3.setVisibility(View.VISIBLE);
		emailSchedule2_clear.setVisibility(View.VISIBLE);
		emailSchedule3_clear.setVisibility(View.VISIBLE);
		break;
	default:
		break;
	}

			if (result.getNameToId() != null && nameToId == null) {
				checkNameToId = result.getEventNameTo();
				mNameTo.setText(getNameto(result.getNameToId(),
						result.getNameToName()));
				nameToId = result.getNameToId();
			} else if (nameToId != null) {

				System.out.println("nameToId = " + nameToId);
				Lead event = new Lead(Constants.DBHANDLER);
				mNameTo.setText(getNameto(nameToId, result.getNameToName()));
			}

			if (result.getLocation() != null) {
				location.setText(result.getLocation());
			}

			if (calendarAdd == true) {
				if (result.getStartDate() != null) {
					start_date.setText(result.getStartDate());
				} else {
					start_date.setText(new SimpleDateFormat(calendarDate
							+ " HH:00").format(new Date()));
				}
			} else {
				if (result.getStartDate() != null) {
					start_date.setText(result.getStartDate());
				} else {
					start_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:00")
							.format(new Date()));
				}
			}

			if (result.getEndDate() != null) {
				end_date.setText(result.getEndDate());
			} else {

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:00");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00");
				String currentDateandTime = sdf.format(new Date());

				Date date = null;
				try {
					date = formatter.parse(currentDateandTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.HOUR, 1);

				if (calendarAdd == true) {
					end_date.setText(new SimpleDateFormat(calendarDate
							+ " HH:00").format(calendar.getTime()).toString());
				} else {
					end_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:00")
							.format(calendar.getTime()).toString());
				}
			}

			if (result.getEmailSchedule1() != null) {
				mEmailSchedule1.setText(result.getEmailSchedule1());
			}

			if (result.getEmailSchedule2() != null) {
				mEmailSchedule2.setText(result.getEmailSchedule2());
			}

			if (result.getEmailSchedule3() != null) {
				mEmailSchedule3.setText(result.getEmailSchedule3());
			}

			if (result.getAlert() != null) {
				alert.setText(result.getAlert());
			}

			if (result.getAllDay() != null) {
				all_day.setChecked(Boolean.parseBoolean(result.getAllDay()));
			}

			if (result.getIsPrivate() != null) {
				private_set.setChecked(Boolean.parseBoolean(result
						.getIsPrivate()));
			}

			String strNumber = getString(R.string.event_invitee_member_number);
			DeviceUtility
					.log(TAG,
							"*****************************UPDATE INVITEE LIST *************************8");
			if (result.getInvitees() != null
					&& result.getInvitees().length() > 1) {
				InviteeList = result.getInvitees();
				String[] strInvitees = InviteeList.split(";");
				strNumber = strNumber.replace("[NUMBER]",
						String.valueOf(strInvitees.length));
				invitee.setText(strNumber);
			} else {
				strNumber = strNumber.replace("[NUMBER]", "0");
				invitee.setText(strNumber);
				if (bActive && bView == false) {
					invitee.setOnClickListener(doShowInviteeList);
				} else {
					invitee.setOnClickListener(null);
				}
			}

			if (result.getEmailNotification() != null) {
				email_notification.setChecked(Boolean.parseBoolean(result
						.getEmailNotification()));
			}
			/*
			 * if (result.getSmsNotification() != null) {
			 * 
			 * }
			 * 
			 * if (result.getEventType() != null) {
			 * 
			 * }
			 */

			if (result.getDescription() != null) {
				description.setText(result.getDescription());
			}

			if (result.getUserDef3() != null) {
				mComment.setText(result.getUserDef3());
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	private class SaveEvent extends AsyncTask<CalendarDetail, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(EventInfo.this, false,
					getString(R.string.processing));
		}

		@Override
		protected Boolean doInBackground(CalendarDetail... params) {
			CalendarDetail calendarDetail = params[0];
			DatabaseUtility.getDatabaseHandler(EventInfo.this);
			FLCalendar calendar = new FLCalendar(Constants.DBHANDLER);
			calendarDetail.setIsUpdate("false");
			if (calendarDetail.getInternalNum() == null) {
				long lRow = calendar.insert(EventInfo.this, calendarDetail);
				if (lRow == -1) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			} else {
				int iRowEffected = calendar.update(EventInfo.this,
						calendarDetail);
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
			if (result.booleanValue() == true) {
				if (InviteePhoneList.length() > 0) {
					GuiUtility
							.alert(EventInfo.this,
									getString(R.string.success_store_event_title),
									getString(R.string.success_store_event_send_sms_desc),
									Gravity.CENTER,
									getString(R.string.yes),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// show sent sms.
											if (InviteePhoneList.length() > 0) {
												String strSmsBody = getString(R.string.sms_event_created);
												strSmsBody = strSmsBody
														.replace(
																"[SUBJECT]",
																calendar.getSubject());
												strSmsBody = strSmsBody
														.replace(
																"[START_DATE]",
																calendar.getStartDate());
												strSmsBody = strSmsBody
														.replace(
																"[END_DATE]",
																calendar.getEndDate());

												Intent smsIntent = new Intent(
														android.content.Intent.ACTION_VIEW);
												smsIntent.setData(Uri
														.parse("sms:"
																+ InviteePhoneList));
												smsIntent.putExtra("sms_body",
														strSmsBody);
												EventInfo.this
														.startActivity(smsIntent);
											}
											EventInfo.this.finish();
										}
									}, getString(R.string.no),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											EventInfo.this.finish();
										}
									});
				} else {
					// no phone number.
					// GuiUtility.alert(EventInfo.this,
					// getString(R.string.success_store_event_title),
					// getString(R.string.success_store_event_desc),
					// Gravity.CENTER, getString(R.string.ok),
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface arg0,
					// int arg1) {
					EventInfo.this.finish();
					// }
					//
					// }, "", null);
				}
			} else {
				GuiUtility.alert(EventInfo.this,
						getString(R.string.fail_store_event_title),
						getString(R.string.fail_store_event_desc),
						getString(R.string.ok));
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}

	}

	public void doLockField(boolean bLock) {
		DeviceUtility.log(TAG, "doLockField(" + bLock + ")");
		boolean bEnabled = true;
		boolean bFocusabled = true;
		if (bLock == true) {

			save.setClickable(true);
			bEnabled = false;
			bFocusabled = false;
		}

		// enabled/disabled edit.
		subject.setEnabled(bEnabled);
		location.setEnabled(bEnabled);
		start_date.setEnabled(bEnabled);
		end_date.setEnabled(bEnabled);
		alert.setEnabled(bEnabled);
		all_day.setClickable(bEnabled);
		private_set.setClickable(bEnabled);
		priority.setEnabled(bEnabled);
		availability.setEnabled(bEnabled);
		mStatusId.setEnabled(bEnabled);
		mComment.setEnabled(bEnabled);
		// invitee.setEnabled(bEnabled);
		email_notification.setClickable(bEnabled);
		description.setEnabled(bEnabled);
		// mNameTo.setEnabled(bEnabled);
		mEmailSchedule1.setEnabled(bEnabled);
		mEmailSchedule2.setEnabled(bEnabled);
		mEmailSchedule3.setEnabled(bEnabled);
		mCategory.setEnabled(bEnabled);

		if (bEnabled && editNameToName) {
			mNameTo.setEnabled(true);
		} else {
			mNameTo.setEnabled(false);
			mNameTo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(EventInfo.this, NameToList.class);
					startActivityForResult(intent, NAME_TO_ID);

				}
			});
		}

		// enabled/disabled focus
		subject.setFocusable(bFocusabled);
		mNameTo.setFocusable(bFocusabled);
		location.setFocusable(bFocusabled);
		all_day.setFocusable(bFocusabled);
		private_set.setFocusable(bFocusabled);
		priority.setFocusable(bFocusabled);
		availability.setFocusable(bFocusabled);
		mStatusId.setFocusable(bFocusabled);
		mComment.setFocusable(bFocusabled);
		email_notification.setFocusable(bFocusabled);
		description.setFocusable(bFocusabled);
		mEmailSchedule1.setEnabled(bFocusabled);
		mEmailSchedule2.setEnabled(bFocusabled);
		mEmailSchedule3.setEnabled(bFocusabled);
		mCategory.setEnabled(bFocusabled);

		if (bFocusabled) {
			subject.setFocusableInTouchMode(bFocusabled);
			location.setFocusableInTouchMode(bFocusabled);
			// all_day.setFocusableInTouchMode(bFocusabled);
			// private_set.setFocusableInTouchMode(bFocusabled);
			// priority.setFocusableInTouchMode(bFocusabled);
			// availability.setFocusableInTouchMode(bFocusabled);
			// email_notification.setFocusableInTouchMode(bFocusabled);
			description.setFocusableInTouchMode(bFocusabled);
			mComment.setFocusableInTouchMode(bFocusabled);
		}
	}

	String checkSpace(String str) {

		String myStr = str.replace(" ", "");

		if (myStr.length() == 0) {
			myStr = "";
		}
		return myStr;
	}

	String getNameto(String nameToId, String nameToName) {
		Lead lead = new Lead(Constants.DBHANDLER);
		Contact contacts = new Contact(Constants.DBHANDLER);
		String[] nameToDBId = nameToId.split("-");
		Cursor cursor;
		System.out.println("checkNameToId = " + checkNameToId);
		System.out.println("nameToId = " + nameToId);
		// System.out.println("task get " + calendar.getEventNameTo());

		if (otherPageTo == true) {
			System.out.println("nameToDBId[1] - " + nameToDBId[1]);
			if (nameToDBId[0].equals("3")) {
				if (nameToId.length() < 10) {
					cursor = lead.getNameToName(nameToDBId[1]);
				} else {
					cursor = lead.getSyncNameToName(nameToId);
				}
			} else {
				if (nameToId.length() < 10) {
					cursor = contacts.getNameToName(nameToDBId[1]);
				} else {
					cursor = contacts.getSyncNameToName(nameToId);
				}
			}
		} else {

			if (calendar.getEventNameTo().equals("0")) {
				System.out.println("load lead");
				if (nameToId.length() < 10) {
					cursor = lead.getNameToName(nameToDBId[1]);
				} else {
					cursor = lead.getSyncNameToName(nameToId);
				}
			} else {
				if (nameToId.length() < 10) {
					cursor = contacts.getNameToName(nameToDBId[1]);
				} else {
					cursor = contacts.getSyncNameToName(nameToId);
				}
			}
		}

		cursor.moveToFirst();
		System.out.println("now my count = " + cursor.getCount());
		if (cursor.getCount() == 0) {
			try {
				if (nameToName.length() != 0) {

					mNameTo.setEnabled(false);
					editNameToName = false;
					mNameTo.setInputType(InputType.TYPE_NULL);
					mNameTo.setFocusable(false);

				}
				System.out.println("now set = " + nameToName);
				return nameToName;
			} catch (Exception e) {
				return strNameToName;
			}

		} else {

			mNameTo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(EventInfo.this, NameToList.class);
					startActivityForResult(intent, NAME_TO_ID);

				}
			});
		}

		String strFirstName = cursor.getString(cursor
				.getColumnIndex("FIRST_NAME"));
		String strLastName = cursor.getString(cursor
				.getColumnIndex("LAST_NAME"));
		if (strFirstName == null) {
			strLastName = strLastName;
		} else {
			strLastName = strFirstName + " " + strLastName;
		}

		System.out.println("PP = " + strLastName);

		return strLastName;
	}
}
