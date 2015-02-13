package com.trinerva.icrm.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.event.EventInfo;
import com.trinerva.icrm.event.NameToList;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.object.TaskDetail;
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

public class TaskInfo extends Activity {
	private String TAG = "TaskInfo";
	private EditText task_subject, task_description;
	private TextView task_due_date, task_alert, mEmailSchedule1,
			mEmailSchedule2, mEmailSchedule3;
	ImageView save;
	private Spinner task_priority, task_availability, task_assigned_to,
			mStatusId, mCategory;
	private ToggleButton task_server_email_notification, task_private;
	private ImageView alert_clear, emailSchedule1_clear, emailSchedule2_clear,
			emailSchedule3_clear;
	private HashMap<String, String> hLoad = new HashMap<String, String>();
	private Dialog loadingDialog;
	private ArrayList<MasterInfo> aPriority, aAvailability, aUserList, aStatus,
			aCategory;
	private TaskDetail task;
	private static final int ALERT_DIALOG_ID = 5312;
	private static final int DUE_DATE_DIALOG_ID = 5310;
	private static final int EMAIL_SCHEDULE1_DIALOG_ID = 5313;
	private static final int EMAIL_SCHEDULE2_DIALOG_ID = 5314;
	private static final int EMAIL_SCHEDULE3_DIALOG_ID = 5315;
	private boolean bView = false;
	private boolean bActive = true;
	private LinearLayout bottom_menu;
	private ImageView delete;
	private View save_divider;
	// private String strDateFormat = "yyyy-MM-dd kk:mm";

	TableRow mCommentLayout;
	EditText mComment;
	TextView mModifiedDate;

	int myStatus;
	public final int NOT_STARTED = 1;
	public final int IN_PROCESS = 2;
	public final int DELAYED = 7;
	public final int REFUSED = 3;
	public final int COMPLETED = 4;
	public final int FAILED = 5;
	public final int CANCELLED = 6;
	private static final int NAME_TO_ID = 9899;

	int myCategory;
	public final int TASK = 1;
	public final int CALL = 2;
	public final int SEND_QUOTATION = 11;
	public final int FAX = 3;
	public final int FOLLOW_UP = 8;
	public final int SMS = 12;
	public final int EMAIL = 13;
	public final int OTHER = 10;

	int statusArrary[] = { NOT_STARTED, IN_PROCESS, DELAYED, REFUSED,
			COMPLETED, FAILED, CANCELLED };

	int categoryArrary[] = { TASK, CALL, SEND_QUOTATION, FAX, FOLLOW_UP, SMS,
			EMAIL, OTHER };

	boolean calendarAdd;
	String calendarDate;

	String nameTo;
	String nameToId;
	EditText mNameTo;
	boolean editNameTo;
	String checkNameToId;
	String checkNameToIdSet = null;
	boolean otherPageTo;
	boolean setNameToData = false;
	String taskKindArray[];

	List<TextView> listData = new ArrayList<TextView>();
	boolean editNameToName = true;
	String strNameToName;
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("setNameTo")) {
				String nameToContact = intent.getStringExtra("nameToContact");
				nameToId = intent.getStringExtra(Constants.NAME_TO_ID);
				mNameTo.setText(nameToContact);
				setNameToData = true;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get extra.
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("ID")) {
				String strTaskId = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strTaskId);
				hLoad.put("ACTION", "EDIT");
				hLoad.put("ID", strTaskId);
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
			otherPageTo = bundle.getBoolean("OTHER_PAGE_TO", false);
			nameToId = bundle.getString("NAME_TO");
			calendarAdd = bundle.getBoolean("calendarAdd", false);
			calendarDate = bundle.getString("date");
			strNameToName = bundle.getString("NAME_TO_NAME");
			editNameTo = bundle.getBoolean("EDIT_NAME_TO", true);

			System.out.println("editNameTo = " + editNameTo);
		}

		System.out.println("editNameTo 1 = " + editNameTo);

		setContentView(R.layout.task_info);
		bottom_menu = (LinearLayout) findViewById(R.id.bottom_menu);
		delete = (ImageView) findViewById(R.id.delete);
		task_subject = (EditText) findViewById(R.id.task_subject);
		task_description = (EditText) findViewById(R.id.task_description);
		mComment = (EditText) findViewById(R.id.comment);
		mNameTo = (EditText) findViewById(R.id.nameTo);

		if (nameToId != null) {
			if (getNameto(nameToId).equals("")) {
				mNameTo.setText(getNameto(nameToId));
			} else {
				mNameTo.setText(strNameToName);
			}
		} else {
			if (strNameToName != null)
				mNameTo.setText(strNameToName);
		}

		taskKindArray = getResources().getStringArray(
				R.array.task_kind_array_name);
		SimpleDateFormat sdf;
		if (calendarAdd == true) {
			sdf = new SimpleDateFormat(calendarDate + " h:00");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd h:00");
		}

		task_due_date = (TextView) findViewById(R.id.task_due_date);
		task_due_date.setHint(sdf.format(new Date()));
		task_alert = (TextView) findViewById(R.id.task_alert);
		task_alert.setText(sdf.format(new Date()));

		mEmailSchedule1 = (TextView) findViewById(R.id.emailSchedule1);
		mEmailSchedule2 = (TextView) findViewById(R.id.emailSchedule2);
		mEmailSchedule3 = (TextView) findViewById(R.id.emailSchedule3);

		mCategory = (Spinner) findViewById(R.id.category);

		save = (ImageView) findViewById(R.id.save);

		task_priority = (Spinner) findViewById(R.id.task_priority);
		task_availability = (Spinner) findViewById(R.id.task_availability);
		task_assigned_to = (Spinner) findViewById(R.id.task_assigned_to);

		task_server_email_notification = (ToggleButton) findViewById(R.id.task_server_email_notification);
		task_private = (ToggleButton) findViewById(R.id.task_private);

		alert_clear = (ImageView) findViewById(R.id.alert_clear);
		emailSchedule1_clear = (ImageView) findViewById(R.id.emailSchedule1_clear);
		emailSchedule2_clear = (ImageView) findViewById(R.id.emailSchedule2_clear);
		emailSchedule3_clear = (ImageView) findViewById(R.id.emailSchedule3_clear);
		save_divider = (View) findViewById(R.id.save_divider);

		mModifiedDate = (TextView) findViewById(R.id.modified);

		mStatusId = (Spinner) findViewById(R.id.statusId);

		mCommentLayout = (TableRow) findViewById(R.id.commentLayout);

		task_due_date.setFocusable(false);
		task_alert.setFocusable(false);

		mEmailSchedule1.setFocusable(false);
		mEmailSchedule2.setFocusable(false);
		mEmailSchedule3.setFocusable(false);

		if (bView == true) {
			doLockField(true);
			if (bActive) {
				bottom_menu.setVisibility(View.VISIBLE);

				if (Utility.getConfigByText(this, Constants.DELETE_TASK)
						.equals("0")) {
					bottom_menu.setVisibility(View.GONE);
				}
				delete.setOnClickListener(doShowPopUpDelete);
				save.setImageResource(R.drawable.ic_icon_edit);
				save.setOnClickListener(doChangeToSave);
			} else {
				bottom_menu.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
				save_divider.setVisibility(View.GONE);

			}
		} else {
			
			bottom_menu.setVisibility(View.GONE);
			alert_clear.setOnClickListener(doClearAlert);
			emailSchedule1_clear.setOnClickListener(doClearEmailSchedule1);
			emailSchedule2_clear.setOnClickListener(doClearEmailSchedule2);
			emailSchedule3_clear.setOnClickListener(doClearEmailSchedule3);
			task_due_date.setOnClickListener(doShowDatePicker);
			task_alert.setOnClickListener(doShowDatePicker);
			save.setOnClickListener(doSaveTask);
			mModifiedDate.setVisibility(View.GONE);

			mEmailSchedule1.setOnClickListener(doShowDatePicker);
			mEmailSchedule2.setOnClickListener(doShowDatePicker);
			mEmailSchedule3.setOnClickListener(doShowDatePicker);

			mNameTo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(TaskInfo.this, NameToList.class);
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
		// if (editNameTo) {
		// mNameTo.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent intent = new Intent();
		// intent.setClass(TaskInfo.this, NameToList.class);
		// startActivityForResult(intent, NAME_TO_ID);
		//
		// }
		// });
		// } else {
		// mNameTo.setEnabled(false);
		// }
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
		
		LoadTask task = new LoadTask();
		task.execute(hLoad);
		
		if(!editNameTo){
			mNameTo.setEnabled(false);
		}
	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("setNameTo");
		registerReceiver(this.broadcastReceiver, filter);
	}

	private OnClickListener doShowPopUpDelete = new OnClickListener() {
		@Override
		public void onClick(View v) {
			GuiUtility.alert(TaskInfo.this, "",
					getString(R.string.confirm_delete_task), Gravity.CENTER,
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
									TaskInfo.this, false,
									getString(R.string.processing));
							DatabaseUtility.getDatabaseHandler(TaskInfo.this);
							Task task = new Task(Constants.DBHANDLER);
							task.delete(TaskInfo.this, hLoad.get("ID")
									.toString());
							loading.dismiss();
							TaskInfo.this.finish();

						}
					});
		}
	};

	private OnClickListener doChangeToSave = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doLockField(false);

			bottom_menu.setVisibility(View.GONE);
			save.setImageResource(R.drawable.ic_icon_save);

			alert_clear.setOnClickListener(doClearAlert);
			emailSchedule1_clear.setOnClickListener(doClearEmailSchedule1);
			emailSchedule2_clear.setOnClickListener(doClearEmailSchedule2);
			emailSchedule3_clear.setOnClickListener(doClearEmailSchedule3);
			task_due_date.setOnClickListener(doShowDatePicker);
			mEmailSchedule1.setOnClickListener(doShowDatePicker);
			mEmailSchedule2.setOnClickListener(doShowDatePicker);
			mEmailSchedule3.setOnClickListener(doShowDatePicker);
			task_alert.setOnClickListener(doShowDatePicker);
			save.setOnClickListener(doSaveTask);

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

	private OnClickListener doSaveTask = new OnClickListener() {

		@Override
		public void onClick(View v) {
			save.setClickable(false);
			doLockField(true);
			String strNameTo = mNameTo.getText().toString();
			String strSubject = task_subject.getText().toString();
			String strDueDate = task_due_date.getText().toString();
			System.out.println("psps = "
					+ task_assigned_to.getSelectedItemPosition());
			System.out.println("user = " + aUserList.size());
			String strAssignedTo = "";
			if (task_assigned_to.getSelectedItemPosition() >= 0) {
				strAssignedTo = aUserList.get(
						task_assigned_to.getSelectedItemPosition()).getValue();
			} else {
				strAssignedTo = aUserList.get(0).getValue();
			}

			String strPriority = "";
			if (task_priority.getSelectedItemPosition() >= 0) {
				strPriority = aPriority.get(
						task_priority.getSelectedItemPosition()).getValue();
			} else {
				strPriority = aPriority.get(0).getValue();
			}

			String strAvailability = "";
			if (task_availability.getSelectedItemPosition() >= 0) {
				strAvailability = aAvailability.get(
						task_availability.getSelectedItemPosition()).getValue();
			} else {
				strAvailability = aAvailability.get(0).getValue();
			}

			switch (mStatusId.getSelectedItemPosition()) {
			case 0:
				myStatus = NOT_STARTED;
				mCommentLayout.setVisibility(View.GONE);
				break;
			case 1:
				myStatus = IN_PROCESS;
				mCommentLayout.setVisibility(View.GONE);
				break;
			case 2:
				myStatus = DELAYED;
				mCommentLayout.setVisibility(View.GONE);
				break;
			case 3:
				myStatus = REFUSED;
				mCommentLayout.setVisibility(View.VISIBLE);
				break;
			case 4:
				myStatus = COMPLETED;
				mCommentLayout.setVisibility(View.VISIBLE);
				break;
			case 5:
				myStatus = FAILED;
				mCommentLayout.setVisibility(View.VISIBLE);
				break;
			case 6:
				myStatus = CANCELLED;
				mCommentLayout.setVisibility(View.GONE);
				break;
			default:
				mCommentLayout.setVisibility(View.GONE);
				break;
			}

			switch (mCategory.getSelectedItemPosition()) {
			case 0:
				myCategory = TASK;
				break;
			case 1:
				myCategory = CALL;
				break;
			case 2:
				myCategory = SEND_QUOTATION;
				break;
			case 3:
				myCategory = FAX;
				break;
			case 4:
				myCategory = FOLLOW_UP;
				break;
			case 5:
				myCategory = SMS;
				break;
			case 6:
				myCategory = EMAIL;
				break;
			case 7:
				myCategory = OTHER;
				break;
			default:
				break;
			}

			// String strStatus = "";
			// if (mStatusId.getSelectedItemPosition() >= 0) {
			// strStatus = aStatus.get(
			// mStatusId.getSelectedItemPosition()).getValue();
			// } else {
			// strStatus = aStatus.get(0).getValue();
			// }

			String strPrivate = String.valueOf(task_private.getText()
					.toString().equalsIgnoreCase(getString(R.string.task_on)));
			String strServerEmail = String
					.valueOf(task_server_email_notification.getText()
							.toString()
							.equalsIgnoreCase(getString(R.string.task_on)));
			String strAlert = task_alert.getText().toString();

			String strEmailSchedule1 = mEmailSchedule1.getText().toString();
			String strEmailSchedule2 = mEmailSchedule2.getText().toString();
			String strEmailSchedule3 = mEmailSchedule3.getText().toString();

			String strDescription = task_description.getText().toString();
			String strComment = mComment.getText().toString();

			if (checkSpace(strSubject).equals("")) {
				GuiUtility.alert(TaskInfo.this, "",
						getString(R.string.task_subject_is_required),
						getString(R.string.ok));
				doLockField(false);
			} else if (strDueDate.length() == 0) {
				GuiUtility.alert(TaskInfo.this, "",
						getString(R.string.task_due_date_is_required),
						getString(R.string.ok));
				doLockField(false);
			} else if (strAssignedTo.length() == 0) {
				GuiUtility.alert(TaskInfo.this, "",
						getString(R.string.task_assigned_to_is_required),
						getString(R.string.ok));
				doLockField(false);
			} else {
				if (hLoad.containsKey("ACTION")) {
					if (hLoad.get("ACTION").equalsIgnoreCase("EDIT")) {
						task.setInternalNum(hLoad.get("ID"));
					}
				}
				task.setNameToId(nameToId);
				task.setSubject(strSubject);
				task.setDueDate(strDueDate);

				task.setEmailSchedule1(strEmailSchedule1);
				task.setEmailSchedule2(strEmailSchedule2);
				task.setEmailSchedule3(strEmailSchedule3);
				task.setAssignedTo(strAssignedTo);
				task.setPriority(strPriority);
				task.setAvailability(strAvailability);
				task.setIsPrivate(strPrivate);

				task.setUserDef7(String.valueOf(myStatus));
				task.setCategory(String.valueOf(myCategory));

				if (myStatus == COMPLETED || myStatus == FAILED
						|| myStatus == REFUSED) {
					task.setUserDef3(mComment.getText().toString());
				} else {
					task.setUserDef3(null);
				}

				if (strAlert.length() == 0) {
					task.setAlert(null);
				} else {
					task.setAlert(strAlert);
				}

				if (strEmailSchedule1.length() == 0) {
					task.setEmailSchedule1(null);
				} else {
					task.setEmailSchedule1(strEmailSchedule1);
				}

				if (strEmailSchedule2.length() == 0) {
					task.setEmailSchedule2(null);
				} else {
					task.setEmailSchedule2(strEmailSchedule2);
				}

				if (strEmailSchedule3.length() == 0) {
					task.setEmailSchedule3(null);
				} else {
					task.setEmailSchedule3(strEmailSchedule3);
				}

				task.setEmailNotification(strServerEmail);

				if (strDescription.length() == 0) {
					task.setDescription(null);
				} else {
					task.setDescription(strDescription);
				}

				String strOwner = Utility.getConfigByText(TaskInfo.this,
						"USER_EMAIL");
				if (strOwner.length() > 0) {
					if (task.getInternalNum() == null) {
						task.setOwner(strOwner);
					}

					task.setUserStamp(strOwner);
				}

				// if (nameToId != null && nameToId.length() > 0) {
				// String[] nameToDBId = nameToId.split("-");
				// if(nameToDBId[0].equals("3")){
				// task.setTaskNameToId("3");
				// }else{
				// task.setTaskNameToId("0");
				// }
				// }

				try {

					System.out.println("no ? = " + nameToId);
					if (nameToId != null && nameToId.length() < 10) {
						String[] nameToDBId = nameToId.split("-");
						if (!nameToDBId[0].equals("3")) {
							System.out.println("Sync 3");
							task.setTaskNameToId("3");
						} else {
							System.out.println("Sync 0");
							task.setTaskNameToId("0");
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					task.setTaskNameToId("0");
				}

				SaveTask oSave = new SaveTask();
				oSave.execute(task);
			}
		}
	};

	private OnClickListener doShowDatePicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.task_alert:
				showDateTimeDialog(ALERT_DIALOG_ID);
				DeviceUtility.log(TAG, "ALERT DATE DIALOG REQUEST");
				break;
			case R.id.task_due_date:
				showDateTimeDialog(DUE_DATE_DIALOG_ID);
				DeviceUtility.log(TAG, "DUE DATE DIALOG REQUEST");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		DeviceUtility.log(TAG, "onActivityResult(" + requestCode + ","
				+ resultCode + ")");
		switch (requestCode) {
		case NAME_TO_ID:
	if(setNameToData){
				setNameToData = false;
			}else{
			try {
				if (data.getStringExtra("NameToId") != null) {
					nameToId = data.getStringExtra("NameToId");
					System.out.println("xxxx = " + nameToId);
					checkNameToIdSet = null;
					String[] nameToDBId = nameToId.split("-");
					System.out.println("set xxxx = " + nameToDBId[0]);
					if (nameToDBId[0].equals("3")) {
						task.setTaskNameToId("0");
					} else {
						task.setTaskNameToId(nameToDBId[0]);
					}
					mNameTo.setText(getNameto(nameToId));

					checkNameToId = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			break;
		}
	}

	private void showDateTimeDialog(int iDialogId) {
		DeviceUtility.log(TAG, "showDateTimeDialog(" + iDialogId + ")");

		// the dialog
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
		case ALERT_DIALOG_ID:
			if (task_alert.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(task_alert.getText().toString());
					date.setMinutes(0);
				} catch (ParseException e2) {
					e2.printStackTrace();
					date = null;
				}
			} else if (task_due_date.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(task_due_date.getText().toString());
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
					// task_alert.setText(DateFormat.format(strDateFormat,
					// mDateTimePicker.getDateTimeMillis()));
					task_alert.setText(new SimpleDateFormat(
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
		case DUE_DATE_DIALOG_ID:
			if (task_due_date.getText().toString().length() > 0) {
				try {
					date = new SimpleDateFormat(Constants.DATETIME_FORMAT)
							.parse(task_due_date.getText().toString());
					System.out.println(date.getHours() + " due date = " + date);
					// mDateTimePicker.updateTime(date.getTime());
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
				}
			}

			// Update demo TextViews when the "OK" button is clicked
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mDateTimePicker.clearFocus();
					// task_due_date.setText(DateFormat.format(strDateFormat,
					// mDateTimePicker.getDateTimeMillis()));
					task_due_date.setText(new SimpleDateFormat(
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
		System.out.println("date.getTime() = " + date);
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
			task_alert.setText("");
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

	private class LoadTask extends
			AsyncTask<HashMap<String, String>, Void, TaskDetail> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(TaskInfo.this, false,
					null);
		}

		@Override
		protected TaskDetail doInBackground(HashMap<String, String>... params) {
			aPriority = (ArrayList<MasterInfo>) Utility.getMasterByType(
					TaskInfo.this, Constants.MASTER_TASK_PRIORITY);
			aAvailability = (ArrayList<MasterInfo>) Utility.getMasterByType(
					TaskInfo.this, Constants.MASTER_TASK_AVAILABILITY);
			aUserList = (ArrayList<MasterInfo>) Utility.getMasterByType(
					TaskInfo.this, Constants.MASTER_TASK_USER_LIST);

			// aStatus = (ArrayList<MasterInfo>) Utility.getMasterByType(
			// TaskInfo.this, Constants.MASTER_TASK_STATUS);

			HashMap<String, String> hAction = params[0];
			TaskDetail oTask = new TaskDetail();

			if (hAction.containsKey("ID") && hAction.containsKey("ACTION")) {
				String strId = hAction.get("ID");
				if (hAction.get("ACTION").equalsIgnoreCase("EDIT")) {
					// load the data.
					DatabaseUtility.getDatabaseHandler(TaskInfo.this);
					Task oTaskSource = new Task(Constants.DBHANDLER);
					oTask = oTaskSource.getTaskDetailById(strId);
				}
			}
			return oTask;
		}

		@Override
		protected void onPostExecute(TaskDetail result) {
			super.onPostExecute(result);
			task = result;

			int iPrioritySelection = 0;
			int iAvailabilitySelection = 0;
			int iStatusSelectiona = 0;
			int iCategory = 0;

			ArrayAdapter<String> aStatusAdapter = new ArrayAdapter<String>(
					TaskInfo.this, android.R.layout.simple_spinner_item);
			aStatusAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// DeviceUtility.log(TAG, "aPriority.size(): " + aPriority.size());
			// DeviceUtility.log(TAG, "result.getPriority(): " +
			// result.getPriority());

			String str[] = { "Not Started", "In Process", "Delayed", "Refused",
					"Completed", "Failed", "Cancelled" };
			for (int i = 0; i < str.length; i++) {
				aStatusAdapter.add(str[i]);

				if (task.getUserDef7() != null) {
					if (task.getUserDef7().equals(
							String.valueOf(statusArrary[i]))) {
						iPrioritySelection = i;
					}
				}
				// if (result.getPriority() != null) {
				// if
				// (result.getPriority().equalsIgnoreCase(aPriority.get(i).getValue()))
				// {
				// iStatusSelection = i;
				// }
				// }
			}

			mStatusId.setAdapter(aStatusAdapter);
			mStatusId.setSelection(iPrioritySelection);
			mStatusId.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg2) {
					case 0:
						myStatus = NOT_STARTED;
						mCommentLayout.setVisibility(View.GONE);
						mComment.setText("");
						break;
					case 1:
						myStatus = IN_PROCESS;
						mCommentLayout.setVisibility(View.GONE);
						mComment.setText("");
						break;
					case 2:
						myStatus = DELAYED;
						mCommentLayout.setVisibility(View.GONE);
						mComment.setText("");
						break;
					case 3:
						myStatus = REFUSED;
						mCommentLayout.setVisibility(View.VISIBLE);
						break;
					case 4:
						myStatus = COMPLETED;
						mCommentLayout.setVisibility(View.VISIBLE);
						break;
					case 5:
						myStatus = FAILED;
						mCommentLayout.setVisibility(View.VISIBLE);
						break;
					case 6:
						myStatus = CANCELLED;
						mCommentLayout.setVisibility(View.GONE);
						mComment.setText("");
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

			// ArrayAdapter<String> aCategoryAdapter = new ArrayAdapter<String>(
			// TaskInfo.this, android.R.layout.simple_spinner_item);
			// aCategoryAdapter
			// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// for (int i = 0; i < aCategory.size(); i++) {
			// aCategoryAdapter.add(aCategory.get(i).getText());
			// if (result.getAvailability() != null) {
			// if (result.getAvailability().equalsIgnoreCase(
			// aCategory.get(i).getValue())) {
			// iCategory = i;
			// }
			// }
			// }
			//

			ArrayAdapter<String> aCategoryAdapter = new ArrayAdapter<String>(
					TaskInfo.this, android.R.layout.simple_spinner_item);
			aCategoryAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// DeviceUtility.log(TAG, "aPriority.size(): " + aPriority.size());
			// DeviceUtility.log(TAG, "result.getPriority(): " +
			// result.getPriority());

			String category[] = { "Task", "Call", "Send Quotation", "Fax",
					"Follow Up", "SMS", "Email", "Others" };
			for (int i = 0; i < category.length; i++) {
				aCategoryAdapter.add(category[i]);

				if (task.getCategory() != null) {
					if (task.getCategory().equals(
							String.valueOf(categoryArrary[i]))) {
						iCategory = i;
					}
				}
			}

			mCategory.setAdapter(aCategoryAdapter);
			mCategory.setSelection(iCategory);
			mCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg2) {
					case 0:
						myCategory = TASK;
						break;
					case 1:
						myCategory = CALL;
						break;
					case 2:
						myCategory = SEND_QUOTATION;
						break;
					case 3:
						myCategory = FAX;
						break;
					case 4:
						myCategory = FOLLOW_UP;
						break;
					case 5:
						myCategory = SMS;
						break;
					case 6:
						myCategory = EMAIL;
						break;
					case 7:
						myCategory = OTHER;
						break;
					default:
						break;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			// if (result.getCategory() != null) {
			//
			// if(result.getCategory().equals("1")){
			// mCategory.setText(taskKindArray[0]);
			// }else if(result.getCategory().equals("2")){
			// mCategory.setText(taskKindArray[1]);
			// }else if(result.getCategory().equals("11")){
			// mCategory.setText(taskKindArray[2]);
			// }else if(result.getCategory().equals("3")){
			// mCategory.setText(taskKindArray[3]);
			// }else if(result.getCategory().equals("8")){
			// mCategory.setText(taskKindArray[4]);
			// }else if(result.getCategory().equals("12")){
			// mCategory.setText(taskKindArray[5]);
			// }else if(result.getCategory().equals("13")){
			// mCategory.setText(taskKindArray[6]);
			// }else if(result.getCategory().equals("10")){
			// mCategory.setText(taskKindArray[7]);
			// }
			// }

			// int iPrioritySelection = 0;
			// int iAvailabilitySelection = 0;
			int iUserListSelection = 0;

			ArrayAdapter<String> aPriorityAdapter = new ArrayAdapter<String>(
					TaskInfo.this, android.R.layout.simple_spinner_item);
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
			task_priority.setAdapter(aPriorityAdapter);
			task_priority.setSelection(iPrioritySelection);

			ArrayAdapter<String> aAvailabilityAdapter = new ArrayAdapter<String>(
					TaskInfo.this, android.R.layout.simple_spinner_item);
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
			task_availability.setAdapter(aAvailabilityAdapter);
			task_availability.setSelection(iAvailabilitySelection);

			ArrayAdapter<String> aUserListAdapter = new ArrayAdapter<String>(
					TaskInfo.this, android.R.layout.simple_spinner_item);
			aUserListAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			String strMyEmail = Utility.getConfigByText(TaskInfo.this,
					Constants.USER_EMAIL);
			String employeeId = Utility
					.getEmployeeId(TaskInfo.this, strMyEmail);
			for (int i = 0; i < aUserList.size(); i++) {
				aUserListAdapter.add(aUserList.get(i).getText());
				if (result.getAssignedTo() != null) {
					if (result.getAssignedTo().equalsIgnoreCase(
							aUserList.get(i).getValue())) {
						iUserListSelection = i;
					}
				} else {
					// default select user him self.
					// can't match due to all the text might not same as the
					// email address.
					DeviceUtility.log(TAG, aUserList.get(i).getText());
					if (aUserList.get(i).getValue()
							.equalsIgnoreCase(employeeId)) {
						iUserListSelection = i;
					}
				}
			}
			task_assigned_to.setAdapter(aUserListAdapter);
			task_assigned_to.setSelection(iUserListSelection);
			if (result.getModifiedTimestamp() != null) {
				mModifiedDate
						.setText(getString(R.string.last_modified_date, Utility
								.convertTaskTime(result.getModifiedTimestamp())));
			}

			if (result.getUserDef7() != null) {
				task_subject.setText(result.getUserDef7());
			}

			if (result.getSubject() != null) {
				task_subject.setText(result.getSubject());
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

			// listData.add(mEmailSchedule1);
			// listData.add(mEmailSchedule2);
			// listData.add(mEmailSchedule3);
			//
			// for (int j = 0; j < listData.size(); j++) {
			// if (listData.get(j).length() == 0) {
			// listData.remove(j);
			// }
			// }
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

			// switch (listData.size()) {
			// case 0:
			// mEmailSchedule1.setVisibility(View.VISIBLE);
			// mEmailSchedule2.setVisibility(View.GONE);
			// emailSchedule2_clear.setVisibility(View.GONE);
			// mEmailSchedule3.setVisibility(View.GONE);
			// emailSchedule3_clear.setVisibility(View.GONE);
			// break;
			// case 1:
			// mEmailSchedule1.setVisibility(View.VISIBLE);
			// mEmailSchedule2.setVisibility(View.VISIBLE);
			// emailSchedule2_clear.setVisibility(View.VISIBLE);
			// mEmailSchedule3.setVisibility(View.GONE);
			// emailSchedule3_clear.setVisibility(View.GONE);
			// break;
			// case 2:
			// mEmailSchedule1.setVisibility(View.VISIBLE);
			// mEmailSchedule2.setVisibility(View.VISIBLE);
			// mEmailSchedule3.setVisibility(View.VISIBLE);
			// mEmailSchedule1.setVisibility(View.VISIBLE);
			// emailSchedule1_clear.setVisibility(View.VISIBLE);
			// mEmailSchedule2.setVisibility(View.VISIBLE);
			// emailSchedule2_clear.setVisibility(View.VISIBLE);
			// mEmailSchedule3.setVisibility(View.VISIBLE);
			// emailSchedule3_clear.setVisibility(View.VISIBLE);
			// break;
			// default:
			// break;
			// }

			switch (listData.size()) {
			case 1:
				mEmailSchedule1.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.GONE);
				emailSchedule2_clear.setVisibility(View.GONE);
				mEmailSchedule3.setVisibility(View.GONE);
				emailSchedule3_clear.setVisibility(View.GONE);
				break;
			case 2:
				mEmailSchedule1.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.VISIBLE);
				emailSchedule2_clear.setVisibility(View.VISIBLE);
				mEmailSchedule3.setVisibility(View.GONE);
				emailSchedule3_clear.setVisibility(View.GONE);
				break;
			case 3:
				mEmailSchedule1.setVisibility(View.VISIBLE);
				mEmailSchedule2.setVisibility(View.VISIBLE);
				emailSchedule2_clear.setVisibility(View.VISIBLE);
				mEmailSchedule3.setVisibility(View.VISIBLE);
				emailSchedule3_clear.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}

			if (result.getNameToId() != null && nameToId == null) {
				checkNameToId = result.getTaskNameToId();
				mNameTo.setText(getNameto(result.getNameToId()));
				nameToId = result.getNameToId();
			} else if (nameToId != null) {

				System.out.println("nameToId = " + nameToId);
				Lead event = new Lead(Constants.DBHANDLER);
				mNameTo.setText(getNameto(nameToId));
			}

			if (result.getDescription() != null) {
				task_description.setText(result.getDescription());
			}

			if (result.getUserDef3() != null) {
				mComment.setText(result.getUserDef3());
			}
			
			if (result.getDueDate() != null) {
				task_due_date.setText(result.getDueDate());
			} else {
				if (calendarAdd == true) {
					task_due_date.setText(new SimpleDateFormat(calendarDate
							+ " HH:00").format(new Date()));
				} else {
					task_due_date.setText(new SimpleDateFormat(
							"yyyy-MM-dd HH:00").format(new Date()));
				}
			}

			if (result.getAlert() != null) {
				task_alert.setText(result.getAlert());
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

			if (result.getEmailNotification() != null) {
				task_server_email_notification.setChecked(Boolean
						.parseBoolean(result.getEmailNotification()));
			}

			if (result.getIsPrivate() != null) {
				task_private.setChecked(Boolean.parseBoolean(result
						.getIsPrivate()));
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	private class SaveTask extends AsyncTask<TaskDetail, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			loadingDialog = GuiUtility.getLoadingDialog(TaskInfo.this, false,
					getString(R.string.processing));
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(TaskDetail... params) {
			TaskDetail task = params[0];
			DatabaseUtility.getDatabaseHandler(TaskInfo.this);
			Task oTask = new Task(Constants.DBHANDLER);
			task.setIsUpdate("false");

			if (task.getInternalNum() == null) {
				long lRow = oTask.insert(TaskInfo.this, task);
				if (lRow == -1) {
					return new Boolean(false);
				} else {
					return new Boolean(true);
				}
			} else {
				int iRowEffected = oTask.update(TaskInfo.this, task);
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
				// GuiUtility.alert(TaskInfo.this,
				// getString(R.string.success_store_task_title),
				// getString(R.string.success_store_task_desc), Gravity.CENTER,
				// getString(R.string.ok),
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				TaskInfo.this.finish();

				// }
				// }, "", null);
			} else {
				save.setClickable(true);
				GuiUtility.alert(TaskInfo.this,
						getString(R.string.fail_store_task_title),
						getString(R.string.fail_store_task_desc),
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
		task_subject.setEnabled(bEnabled);
		task_description.setEnabled(bEnabled);
		mComment.setEnabled(bEnabled);
		task_due_date.setEnabled(bEnabled);
		mEmailSchedule1.setEnabled(bEnabled);
		mEmailSchedule2.setEnabled(bEnabled);
		mEmailSchedule3.setEnabled(bEnabled);
		task_alert.setEnabled(bEnabled);
		task_availability.setEnabled(bEnabled);
		task_priority.setEnabled(bEnabled);
		task_assigned_to.setEnabled(bEnabled);
		mStatusId.setEnabled(bEnabled);
		// task_server_email_notification.setEnabled(bEnabled);
		task_server_email_notification.setClickable(bEnabled);
		task_private.setClickable(bEnabled);
		// mNameTo.setEnabled(bEnabled);
		mCategory.setEnabled(bEnabled);

		if (bEnabled && editNameToName) {
			mNameTo.setEnabled(true);
		} else {
			mNameTo.setEnabled(false);
			mNameTo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(TaskInfo.this, NameToList.class);
					startActivityForResult(intent, NAME_TO_ID);

				}
			});
		}

		// enabled/disabled focus
		task_subject.setFocusable(bFocusabled);
		mNameTo.setFocusable(bFocusabled);
		task_description.setFocusable(bFocusabled);
		mComment.setFocusable(bFocusabled);
		task_availability.setFocusable(bFocusabled);
		task_priority.setFocusable(bFocusabled);
		task_assigned_to.setFocusable(bFocusabled);
		task_server_email_notification.setFocusable(bFocusabled);
		task_private.setFocusable(bFocusabled);
		mStatusId.setFocusable(bEnabled);
		mCategory.setEnabled(bFocusabled);
		mEmailSchedule1.setEnabled(bFocusabled);
		mEmailSchedule2.setEnabled(bFocusabled);
		mEmailSchedule3.setEnabled(bFocusabled);

		if (bFocusabled) {
			task_subject.setFocusableInTouchMode(bFocusabled);
			task_description.setFocusableInTouchMode(bFocusabled);
			mComment.setFocusableInTouchMode(bFocusabled);
		}
	}

	String checkSpace(String str) {

		String myStr = str.replace(" ", "");

		if (myStr.length() == 0) {
			myStr = "";
		}

		System.out.println("cccc = " + myStr.length());
		return myStr;
	}

	String getNameto(String nameToId) {
		Lead lead = new Lead(Constants.DBHANDLER);
		Contact contacts = new Contact(Constants.DBHANDLER);
		String[] nameToDBId = nameToId.split("-");
		Cursor cursor;
		System.out.println("checkNameToId = " + checkNameToId);
		System.out.println("nameToId = " + nameToId);
		// System.out.println("task get " + task.getTaskNameToId());

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
			if (task.getTaskNameToId().equals("0")) {
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
				if (task.getNameToName().length() != 0) {

					mNameTo.setEnabled(false);
					editNameToName = false;
					mNameTo.setInputType(InputType.TYPE_NULL);
					mNameTo.setFocusable(false);

				}
				return task.getNameToName();
			} catch (Exception e) {
				return strNameToName;
			}

		} else {

			mNameTo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(TaskInfo.this, NameToList.class);
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
