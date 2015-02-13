package com.trinerva.icrm.database.source;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.TaskDetail;
import com.trinerva.icrm.utility.CalendarUtility;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Task extends BaseSource {
	private String TAG = "Task";
	// private String strDateFormat = "yyyy-MM-dd HH:mm";
	private String strDateFormat = Constants.DATETIME_FORMAT;

	public Task(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public TaskDetail getTaskDetailById(String strId) {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskDetailById(" + strId + ")");
		TaskDetail object = new TaskDetail();
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_TASK, null,
				"INTERNAL_NUM = ?", new String[] { strId }, null, null, null);
		cursor.moveToFirst();
		object = setToObject(cursor);

		DeviceUtility.log(TAG, object.toString());
		cursor.close();
		this.close();
		return object;
	}

	private TaskDetail setToObject(Cursor cursor) {
		TaskDetail task = new TaskDetail();
		task.setInternalNum(cursor.getString(cursor
				.getColumnIndex("INTERNAL_NUM")));
		task.setTaskId(cursor.getString(cursor.getColumnIndex("TASK_ID")));
		task.setSubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
		task.setNameToName(cursor.getString(cursor.getColumnIndex("NAME_TO_NAME")));
		task.setNameToId(cursor.getString(cursor.getColumnIndex("NAME_TO")));
		task.setDueDate(cursor.getString(cursor.getColumnIndex("DUE_DATE")));
		task.setPriority(cursor.getString(cursor.getColumnIndex("PRIORITY")));
		task.setAvailability(cursor.getString(cursor
				.getColumnIndex("AVAILABILITY")));
		task.setAssignedTo(cursor.getString(cursor
				.getColumnIndex("ASSIGNED_TO")));
		task.setAlert(cursor.getString(cursor.getColumnIndex("ALERT")));
		task.setEmailNotification(cursor.getString(cursor
				.getColumnIndex("EMAIL_NOTIFICATION")));
		task.setSmsNotification(cursor.getString(cursor
				.getColumnIndex("SMS_NOTIFICATION")));
		task.setTaskType(cursor.getString(cursor.getColumnIndex("TASK_TYPE")));
		task.setOwner(cursor.getString(cursor.getColumnIndex("OWNER")));
		task.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		task.setDescription(cursor.getString(cursor
				.getColumnIndex("DESCRIPTION")));
		task.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
		task.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
		task.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
		task.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
		task.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
		task.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
		task.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
		task.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
		task.setUserStamp(cursor.getString(cursor.getColumnIndex("USER_STAMP")));
		task.setCreatedTimestamp(cursor.getString(cursor
				.getColumnIndex("CREATED_TIMESTAMP")));
		task.setModifiedTimestamp(cursor.getString(cursor
				.getColumnIndex("MODIFIED_TIMESTAMP")));
		task.setIsPrivate(cursor.getString(cursor.getColumnIndex("IS_PRIVATE")));
		task.setIsUpdate(cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
		task.setTaskNameToId(cursor.getString(cursor.getColumnIndex("TASK_NAME_TO_ID")));
		task.setEmailSchedule1(cursor.getString(cursor.getColumnIndex("EMAIL_SCHEDULE1")));
		task.setEmailSchedule2(cursor.getString(cursor.getColumnIndex("EMAIL_SCHEDULE2")));
		task.setEmailSchedule3(cursor.getString(cursor.getColumnIndex("EMAIL_SCHEDULE3")));
		task.setCategory(cursor.getString(cursor.getColumnIndex("TASK_KIND")));
		
		
//		task.setEmailSchedules(emailSchedules.toString());
try {
	task.setNameToId(cursor.getString(cursor.getColumnIndex("NAME_TO_ID")));
	task.setTaskNameTo(cursor.getString(cursor.getColumnIndex("TASK_NAME_TO")));
} catch (Exception e) {
	// TODO: handle exception
}
		return task;
	}

	public long insert(Context context, TaskDetail dInsert) {
		this.openWrite();
		long lResult = -1;
		ContentValues values = new ContentValues();
		// by default should be 1
		values.put("ACTIVE", "0");
		values.put("IS_UPDATE", "false");
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());

		if (dInsert.getModifiedTimestamp() != null
				&& dInsert.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", dInsert.getModifiedTimestamp());
		}

		if (dInsert.getCreatedTimestamp() != null
				&& dInsert.getCreatedTimestamp().length() > 0) {
			values.put("CREATED_TIMESTAMP", dInsert.getCreatedTimestamp());
		}

		if (dInsert.getIsUpdate() != null && dInsert.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dInsert.getIsUpdate());
		}

		if (dInsert.getActive() != null && dInsert.getActive().length() > 0) {
			values.put("ACTIVE", dInsert.getActive());
		}

		if (dInsert.getTaskId() != null && dInsert.getTaskId().length() > 0) {
			values.put("TASK_ID", dInsert.getTaskId());
		}

		if (dInsert.getSubject() != null && dInsert.getSubject().length() > 0) {
			values.put("SUBJECT", dInsert.getSubject());
		}
		if (dInsert.getCategory()!= null && dInsert.getCategory().length() > 0) {
			values.put("TASK_KIND", dInsert.getCategory());
		}
		
		if (dInsert.getEmailSchedule1() != null
				&& dInsert.getEmailSchedule1().length() > 0) {
			values.put("EMAIL_SCHEDULE1", dInsert.getEmailSchedule1());
		}
		
		if (dInsert.getEmailSchedule2() != null
				&& dInsert.getEmailSchedule2().length() > 0) {
			values.put("EMAIL_SCHEDULE2", dInsert.getEmailSchedule2());
		}
		
		if (dInsert.getEmailSchedule3() != null
				&& dInsert.getEmailSchedule3().length() > 0) {
			values.put("EMAIL_SCHEDULE3", dInsert.getEmailSchedule3());
		}
		
		if (dInsert.getNameToName() != null && dInsert.getNameToName().length() > 0) {
			values.put("NAME_TO_NAME", dInsert.getNameToName());
		}

		if (dInsert.getNameToId() != null && dInsert.getNameToId().length() > 0) {
			values.put("NAME_TO", dInsert.getNameToId());
		}

		if (dInsert.getDueDate() != null && dInsert.getDueDate().length() > 0) {
			values.put("DUE_DATE", dInsert.getDueDate());
		}

		if (dInsert.getPriority() != null && dInsert.getPriority().length() > 0) {
			values.put("PRIORITY", dInsert.getPriority());
		}

		if (dInsert.getAvailability() != null
				&& dInsert.getAvailability().length() > 0) {
			values.put("AVAILABILITY", dInsert.getAvailability());
		}

		if (dInsert.getAssignedTo() != null
				&& dInsert.getAssignedTo().length() > 0) {
			values.put("ASSIGNED_TO", dInsert.getAssignedTo());
		}

		if (dInsert.getAlert() != null && dInsert.getAlert().length() > 0) {
			values.put("ALERT", dInsert.getAlert());
		}

		if (dInsert.getEmailNotification() != null
				&& dInsert.getEmailNotification().length() > 0) {
			values.put("EMAIL_NOTIFICATION", dInsert.getEmailNotification());
		}

		if (dInsert.getSmsNotification() != null
				&& dInsert.getSmsNotification().length() > 0) {
			values.put("SMS_NOTIFICATION", dInsert.getSmsNotification());
		}

		if (dInsert.getTaskType() != null && dInsert.getTaskType().length() > 0) {
			values.put("TASK_TYPE", dInsert.getTaskType());
		}

		if (dInsert.getOwner() != null && dInsert.getOwner().length() > 0) {
			values.put("OWNER", dInsert.getOwner());
		}

		if (dInsert.getDescription() != null && dInsert.getDescription().length() > 0) {
			values.put("DESCRIPTION", dInsert.getDescription());
		}

		if (dInsert.getUserDef1() != null && dInsert.getUserDef1().length() > 0) {
			values.put("USER_DEF1", dInsert.getUserDef1());
		}

		if (dInsert.getUserDef2() != null && dInsert.getUserDef2().length() > 0) {
			values.put("USER_DEF2", dInsert.getUserDef2());
		}

		if (dInsert.getUserDef3() != null && dInsert.getUserDef3().length() > 0) {
			values.put("USER_DEF3", dInsert.getUserDef3());
		}

		if (dInsert.getUserDef4() != null && dInsert.getUserDef4().length() > 0) {
			values.put("USER_DEF4", dInsert.getUserDef4());
		}

		if (dInsert.getUserDef5() != null && dInsert.getUserDef5().length() > 0) {
			values.put("USER_DEF5", dInsert.getUserDef5());
		}

		if (dInsert.getUserDef6() != null && dInsert.getUserDef6().length() > 0) {
			values.put("USER_DEF6", dInsert.getUserDef6());
		}

		if (dInsert.getUserDef7() != null && dInsert.getUserDef7().length() > 0) {
			values.put("USER_DEF7", dInsert.getUserDef7());
		}

		if (dInsert.getUserDef8() != null && dInsert.getUserDef8().length() > 0) {
			values.put("USER_DEF8", dInsert.getUserDef8());
		}

		if (dInsert.getUserStamp() != null
				&& dInsert.getUserStamp().length() > 0) {
			values.put("USER_STAMP", dInsert.getUserStamp());
		}

		if (dInsert.getIsPrivate() != null
				&& dInsert.getIsPrivate().length() > 0) {
			values.put("IS_PRIVATE", dInsert.getIsPrivate());
		}

		if (dInsert.getTaskNameTo() != null
				&& dInsert.getTaskNameTo().length() > 0) {
			values.put("NAME_TO", dInsert.getNameToId());
		}
		
		if (dInsert.getTaskNameToId() != null
				&& dInsert.getTaskNameToId().length() > 0) {
//			if(dInsert.getTaskNameToId().equals("3")){
//				values.put("TASK_NAME_TO_ID", "0");
//			}else{
//				values.put("TASK_NAME_TO_ID", "3");
//			}
			values.put("TASK_NAME_TO_ID", dInsert.getTaskNameToId());
		}


		lResult = database.insert(DatabaseHandler.TABLE_FL_TASK, null, values);
		if (lResult != -1) {
			// do insert into the phone calendar.
			writeToPhoneCalendar(context, dInsert, lResult);
		}
		this.close();
		return lResult;
	}

	public int update(Context context, TaskDetail dUpdate) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());

		/*
		 * if (dUpdate.getModifiedTimestamp()!= null &&
		 * dUpdate.getModifiedTimestamp().length() > 0) {
		 * values.put("MODIFIED_TIMESTAMP", dUpdate.getModifiedTimestamp()); }
		 */

		if (dUpdate.getIsUpdate() != null && dUpdate.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dUpdate.getIsUpdate());
		}

		values.put("TASK_ID", dUpdate.getTaskId());
		values.put("SUBJECT", dUpdate.getSubject());
		values.put("DUE_DATE", dUpdate.getDueDate());
		values.put("PRIORITY", dUpdate.getPriority());
		values.put("AVAILABILITY", dUpdate.getAvailability());
		values.put("ASSIGNED_TO", dUpdate.getAssignedTo());
		values.put("ALERT", dUpdate.getAlert());
		values.put("EMAIL_NOTIFICATION", dUpdate.getEmailNotification());
		values.put("SMS_NOTIFICATION", dUpdate.getSmsNotification());
		values.put("TASK_TYPE", dUpdate.getTaskType());
		values.put("OWNER", dUpdate.getOwner());
		values.put("ACTIVE", dUpdate.getActive());
		values.put("DESCRIPTION", dUpdate.getDescription());
		values.put("USER_DEF2", dUpdate.getUserDef2());
		values.put("USER_DEF3", dUpdate.getUserDef3());
		values.put("NAME_TO_NAME", dUpdate.getNameToName());
		values.put("NAME_TO", dUpdate.getNameToId());
		values.put("TASK_NAME_TO_ID", dUpdate.getTaskNameToId());
		values.put("TASK_KIND", dUpdate.getCategory());
		System.out.println("1 = "+dUpdate.getEmailSchedule1());
		System.out.println("2 = "+dUpdate.getEmailSchedule2());
		System.out.println("3 = "+dUpdate.getEmailSchedule3());
		
		
		List<String> listData = new ArrayList<String>();
		
				if (dUpdate.getEmailSchedule1() != null) {
					listData.add(dUpdate.getEmailSchedule1());
				}
				if (dUpdate.getEmailSchedule2() != null) {
					listData.add(dUpdate.getEmailSchedule2());
				}
				if (dUpdate.getEmailSchedule3() != null) {
					listData.add(dUpdate.getEmailSchedule3());
				}
				System.out.println("PPP = "+listData.size());
				for (int i = 0; i < listData.size(); i++) {
					System.out.println("prin = "+listData.get(i));
				}
					switch (listData.size()) {
					case 1:
						values.put("EMAIL_SCHEDULE1", listData.get(0));
						values.put("EMAIL_SCHEDULE2", "");
						values.put("EMAIL_SCHEDULE3", "");
						break;
					case 2:
						values.put("EMAIL_SCHEDULE1", listData.get(0));
						values.put("EMAIL_SCHEDULE2", listData.get(1));
						values.put("EMAIL_SCHEDULE3", "");
						break;
					case 3:
						values.put("EMAIL_SCHEDULE1", listData.get(0));
						values.put("EMAIL_SCHEDULE2", listData.get(1));
						values.put("EMAIL_SCHEDULE3", listData.get(2));
						break;
					default:
						values.put("EMAIL_SCHEDULE1", "");
						values.put("EMAIL_SCHEDULE2", "");
						values.put("EMAIL_SCHEDULE3", "");
						break;
					}
					
//		if(dUpdate.getEmailSchedule1() != null && dUpdate.getEmailSchedule2() != null && dUpdate.getEmailSchedule3() != null){
//			values.put("EMAIL_SCHEDULE1", dUpdate.getEmailSchedule1());
//			values.put("EMAIL_SCHEDULE2", dUpdate.getEmailSchedule2());
//			values.put("EMAIL_SCHEDULE3", dUpdate.getEmailSchedule3());
//		}else if(dUpdate.getEmailSchedule1() != null && dUpdate.getEmailSchedule2() == null && dUpdate.getEmailSchedule3() != null){
//			values.put("EMAIL_SCHEDULE1", dUpdate.getEmailSchedule1());
//			values.put("EMAIL_SCHEDULE2", dUpdate.getEmailSchedule3());
//			values.put("EMAIL_SCHEDULE3", "");
//		}
//		values.put("EMAIL_SCHEDULE1", dUpdate.getEmailSchedule1());
//		values.put("EMAIL_SCHEDULE2", dUpdate.getEmailSchedule2());
//		values.put("EMAIL_SCHEDULE3", dUpdate.getEmailSchedule3());

		if (dUpdate.getUserDef1() != null && dUpdate.getUserDef1().length() > 0) {
			values.put("USER_DEF1", dUpdate.getUserDef1());
		}

		if (dUpdate.getUserDef2() != null && dUpdate.getUserDef2().length() > 0) {
			values.put("USER_DEF2", dUpdate.getUserDef2());
		}

		if (dUpdate.getUserDef3() != null && dUpdate.getUserDef3().length() > 0) {
			values.put("USER_DEF3", dUpdate.getUserDef3());
		}

		if (dUpdate.getUserDef4() != null && dUpdate.getUserDef4().length() > 0) {
			values.put("USER_DEF4", dUpdate.getUserDef4());
		}

		if (dUpdate.getUserDef5() != null && dUpdate.getUserDef5().length() > 0) {
			values.put("USER_DEF5", dUpdate.getUserDef5());
		}

		if (dUpdate.getUserDef6() != null && dUpdate.getUserDef6().length() > 0) {
			values.put("USER_DEF6", dUpdate.getUserDef6());
		}

		if (dUpdate.getUserDef7() != null && dUpdate.getUserDef7().length() > 0) {
			values.put("USER_DEF7", dUpdate.getUserDef7());
		}

		if (dUpdate.getUserDef8() != null && dUpdate.getUserDef8().length() > 0) {
			values.put("USER_DEF8", dUpdate.getUserDef8());
		}

		values.put("USER_STAMP", dUpdate.getUserStamp());
		values.put("IS_PRIVATE", dUpdate.getIsPrivate());
		DeviceUtility.log(TAG, values.toString());
		iResult = database.update(DatabaseHandler.TABLE_FL_TASK, values,
				"INTERNAL_NUM = ?", new String[] { dUpdate.getInternalNum() });
		if (iResult > 0) {
			// get the task. since not all field will be updated.
			TaskDetail oTaskInfo = getTaskDetailById(dUpdate.getInternalNum());
			writeToPhoneCalendar(context, oTaskInfo,
					Long.parseLong(dUpdate.getInternalNum()));
		}
		this.close();
		return iResult;
	}

	public int delete(Context context, String Id) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("ACTIVE", "1");
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		iResult = database.update(DatabaseHandler.TABLE_FL_TASK, values,
				"INTERNAL_NUM = ?", new String[] { Id });
		if (iResult > 0) {
			// remove it from calendar.
			removeFromPhoneCalendar(context, Long.parseLong(Id));
		}
		this.close();
		return iResult;
	}

	public Cursor getTaskListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " 
								+ DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true" });
if(cursor.getCount() != 0)
	cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
//		this.close();
		return cursor;
	}

	public Cursor getTaskListDisplay(String date,String category) {
		
		System.out.println("category = "+category);
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE, IS_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_TASK
								+ " WHERE Date(DUE_DATE)= ? AND (ACTIVE <> ? OR IS_UPDATE <> ?) AND TASK_KIND IN(1,11,3,8,10,"+category+") ORDER BY INTERNAL_NUM ASC",
						new String[] { date, "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
//		this.close();
		return cursor;
	}

	public Cursor getTodayTaskListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");

		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE,TASK_NAME_TO_ID,NAME_TO, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " 
								+ DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE Date(DUE_DATE) = DATE('now','localtime') AND (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getFilterTodayTaskListDisplay(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");

		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE,TASK_NAME_TO_ID,NAME_TO, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " 
								+ DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE Date(DUE_DATE) = DATE('now','localtime') AND (ACTIVE <> ? OR IS_UPDATE <> ?) AND (SUBJECT LIKE ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true", "%" + strFilter + "%" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public Cursor getTodayAndNew7DayTaskListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " 
								+ DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE Date(DUE_DATE) between Date('now','localtime') and Date('now','7 days') AND (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getFilterTodayAndNew7DayTaskListDisplay(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " 
								+ DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE Date(DUE_DATE) between Date('now','localtime') and Date('now','7 days') AND (ACTIVE <> ? OR IS_UPDATE <> ?) AND (SUBJECT LIKE ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true" , "%" + strFilter + "%" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}


	public Cursor getOverdueTaskListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " +
								DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE Date(DUE_DATE) BETWEEN DATE('now','localtime','-31 day') AND DATE('now','localtime','-1 day') AND (ACTIVE <> ? OR IS_UPDATE <> ?) AND (USER_DEF7 NOT IN ("+4+") OR IS_UPDATE <> ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true" , "true"});
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT A.INTERNAL_NUM AS _id, A.INTERNAL_NUM, A.SUBJECT,A.NAME_TO, A.DUE_DATE, A.ACTIVE, A.IS_UPDATE FROM "
//								+ DatabaseHandler.TABLE_FL_TASK
//								+ " AS A LEFT JOIN "
//								+ DatabaseHandler.TABLE_FL_LEAD
//								+ " AS B LEFT JOIN ON (A.NAME_TO = B.NAME_TO) "
////								+ DatabaseHandler.TABLE_FL_CONTACT
//								+ " WHERE LEAD_ID AND (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getFilterOverdueTaskListDisplay(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE,NAME_TO_NAME, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " +
								DatabaseHandler.TABLE_FL_TASK
								+ " cl WHERE Date(DUE_DATE) BETWEEN DATE('now','localtime','-31 day') AND DATE('now','localtime','-1 day') AND (ACTIVE <> ? OR IS_UPDATE <> ?)  AND (USER_DEF7 NOT IN ("+4+") OR IS_UPDATE <> ?) AND (SUBJECT LIKE ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true","true", "%" + strFilter + "%"  });
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT A.INTERNAL_NUM AS _id, A.INTERNAL_NUM, A.SUBJECT,A.NAME_TO, A.DUE_DATE, A.ACTIVE, A.IS_UPDATE FROM "
//								+ DatabaseHandler.TABLE_FL_TASK
//								+ " AS A LEFT JOIN "
//								+ DatabaseHandler.TABLE_FL_LEAD
//								+ " AS B LEFT JOIN ON (A.NAME_TO = B.NAME_TO) "
////								+ DatabaseHandler.TABLE_FL_CONTACT
//								+ " WHERE LEAD_ID AND (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getTaskListDisplayByFilter(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplayByFilter(" + strFilter + ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, DUE_DATE, ACTIVE, IS_UPDATE, " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
								"END   " +
								"END AS NAME_TO_LAST_NAME,  " +
								"CASE cl.TASK_NAME_TO_ID  " +
								"WHEN '3' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
								"END  " +
								"WHEN '0' THEN  " +
								"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
								"ELSE  " +
								"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
								"END    " +
								"END AS NAME_TO_FIRST_NAME  " +
								"FROM " 
								+ DatabaseHandler.TABLE_FL_TASK
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (SUBJECT LIKE ?) ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true", "%" + strFilter + "%" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getTaskListDisplayByFilter: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public int updateReminderId(long lReminder, long lTaskId) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("USER_DEF2", String.valueOf(lReminder));
		iResult = database.update(DatabaseHandler.TABLE_FL_TASK, values,
				"INTERNAL_NUM = ?", new String[] { String.valueOf(lTaskId) });
		this.close();
		return iResult;
	}

	public int updateEventCalendarId(long lEventId, long lTaskId) {
		System.out.println("update calendar");
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("USER_DEF1", String.valueOf(lEventId));
		iResult = database.update(DatabaseHandler.TABLE_FL_TASK, values,
				"INTERNAL_NUM = ?", new String[] { String.valueOf(lTaskId) });
		this.close();
		return iResult;
	}

	/*
	 * public void removeFromPhoneCalendar(Context context, long lTaskId) {
	 * CalendarUtility local = new CalendarUtility(context); int iResult =
	 * local.removeEvent(lTaskId); DeviceUtility.log(TAG, "local.removeEvent: "
	 * + iResult); iResult = local.deleteReminder(lTaskId);
	 * DeviceUtility.log(TAG, "local.deleteReminder: " + iResult); }
	 */

	public void removeFromPhoneCalendar(Context context, long lTaskId) {
		// get and delete it from local.
		this.openRead();
		CalendarUtility local = new CalendarUtility(context);
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_TASK,
				new String[] { "USER_DEF1", "USER_DEF2" }, "INTERNAL_NUM = ?",
				new String[] { String.valueOf(lTaskId) }, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "removeFromPhoneCalendar: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			String strCalendarEventId = cursor.getString(cursor
					.getColumnIndex("USER_DEF1"));
			String strCalendarReminderId = cursor.getString(cursor
					.getColumnIndex("USER_DEF2"));

			long lCalendarEventId = 0;
			long lCalendarReminderId = 0;

			if (strCalendarEventId != null && strCalendarEventId.length() > 0) {
				try {
					lCalendarEventId = Long.parseLong(strCalendarEventId);
				} catch (Exception e) {

				}
			}

			if (strCalendarReminderId != null
					&& strCalendarReminderId.length() > 0) {
				try {
					lCalendarReminderId = Long.parseLong(strCalendarReminderId);
				} catch (Exception e) {

				} finally {

				}
			}

			if (lCalendarEventId > 0) {
				int iResult = local.removeEvent(lCalendarEventId);
				DeviceUtility.log(TAG, "local.removeEvent: " + iResult);
			}

			if (lCalendarReminderId > 0) {
				int iResult2 = local.deleteReminder(lCalendarReminderId);
				DeviceUtility.log(TAG, "local.deleteReminder: " + iResult2);
			}

			cursor.moveToNext();
		}
		cursor.close();
		this.close();
	}

	// USER_DEF1 : calendar's event id
	// USER_DEF2 : reminder id
	public void writeToPhoneCalendar(Context context, TaskDetail object,
			long lInternalNum) {
		CalendarUtility local = new CalendarUtility(context);
		DateFormat formatter;
		Date date;
		long lEventId = -1;
		int iUpdateStatus = 0;
		long lReminderId = -1;

		try {
			formatter = new SimpleDateFormat(strDateFormat);
			date = (Date)formatter.parse(object.getDueDate());
			long startTime = date.getTime();
			long endTime = date.getTime() + (60 * 60 * 1000);
			//calendar id
			String strCalendarId = object.getUserDef1();
			if (strCalendarId == null || strCalendarId.length() == 0) {
				lEventId = local.insertEvent(object.getSubject(), (object.getDescription() == null ? "" : object.getDescription()), startTime, endTime, 1);
				DeviceUtility.log(TAG, "lEventId: " + lEventId);
				//update calendar event id into fl_calendar.
				if (lEventId != -1) {
					iUpdateStatus = updateEventCalendarId(lEventId, lInternalNum);
					DeviceUtility.log(TAG, "updateEventCalendarId: " + iUpdateStatus);
				}
			} else {
				lEventId = Long.parseLong(strCalendarId);
				//update.
				iUpdateStatus = local.updateEvent(lEventId, object.getSubject(), (object.getDescription() == null ? "" : object.getDescription()), startTime, endTime, 1);
				//check is event exist in phone calendar?
				DeviceUtility.log(TAG, "local.updateEvent: " + iUpdateStatus);
				if (iUpdateStatus == -1) {
					//event not found in phone.
					lEventId = local.insertEvent(object.getSubject(), (object.getDescription() == null ? "" : object.getDescription()), startTime, endTime, 1);
					DeviceUtility.log(TAG, "lEventId: " + lEventId);
					if (lEventId != -1) {
						iUpdateStatus = updateEventCalendarId(lEventId, lInternalNum);
						DeviceUtility.log(TAG, "updateEventCalendarId: " + iUpdateStatus);
					}
				}
			}

			if (object.getAlert() != null && object.getAlert().length() > 0 && !object.getAlert().equals(Constants.DEFAULT_DATE)) {
				//insert reminder.
				date = (Date)formatter.parse(object.getAlert());
				long alert = date.getTime();
				long diff = (startTime - alert);
					int minutes = (int) diff/(1000*60);
					String strReminderId = object.getUserDef2();
					if (strReminderId == null || strReminderId.length() == 0) {
						lReminderId = local.addReminder(lEventId, minutes);
						if (lReminderId != -1) {
							iUpdateStatus = updateReminderId(lReminderId, lInternalNum);
							DeviceUtility.log(TAG, "updateReminderId: " + iUpdateStatus);
						}
					} else {
						lReminderId = Long.parseLong(strReminderId);
						iUpdateStatus = local.updateReminderWithReminderId(lEventId, lReminderId, minutes);
						DeviceUtility.log(TAG, "updateReminderId: " + iUpdateStatus);
						if (iUpdateStatus == -1) {
							lReminderId = local.addReminder(lEventId, minutes);
							if (lReminderId != -1) {
								iUpdateStatus = updateReminderId(lReminderId, lInternalNum);
								DeviceUtility.log(TAG, "updateReminderId: " + iUpdateStatus);
							}
						}
					}
					DeviceUtility.log(TAG, "lReminderId: " + lReminderId);
				
				
			} else {
				//remove reminder.
				String strReminderId = object.getUserDef2();
				if (strReminderId != null && strReminderId.length() > 0) {
					long lCalendarReminderId = 0;
					try {
						lCalendarReminderId = Long.parseLong(strReminderId);
					} catch (Exception e) {
						
					} finally {
						
					}
					
					if (lCalendarReminderId > 0) {
						int iResult2 = local.deleteReminder(lCalendarReminderId);
						DeviceUtility.log(TAG, "local.deleteReminder: " + iResult2);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			DeviceUtility.log(TAG, "ERROR INSERT INTO PHONE CALENDAR");
		}
	}

	public ArrayList<TaskDetail> getUnsyncTask() {
		this.openRead();
		ArrayList<TaskDetail> aTask = new ArrayList<TaskDetail>();
		DeviceUtility.log(TAG, "getUnsyncTask");
//		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_TASK, null, "IS_UPDATE = ?", new String[] {"false"}, null, null, null);
//		Cursor cursor = database.rawQuery("SELECT *,CASE substr(cl.Name_To,1,1) WHEN '2' THEN (SELECT ct.Contact_ID FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = substr(cl.Name_To,3)) WHEN '3' THEN (SELECT ct.Lead_ID FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = substr(cl.Name_To,3)) END AS NAME_TO_ID,CASE substr(cl.Name_To,1,1)  WHEN '3' THEN '3' ELSE '0' END AS TASK_NAME_TO FROM FL_TASK cl WHERE IS_UPDATE = ?", new String[] {"false"});
		Cursor cursor = database.rawQuery("SELECT *, "+
"CASE cl.TASK_NAME_TO_ID  "+
"WHEN '3' THEN  "+
"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  "+
"(SELECT ct.CONTACT_ID FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  "+
"ELSE  cl.NAME_TO END  "+
"WHEN '0' THEN  "+
"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  "+
"(SELECT ct.LEAD_ID FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) "+
"ELSE cl.NAME_TO END   "+
"END AS NAME_TO_ID, "+
"CASE substr(cl.Name_To,1,1) WHEN '3' THEN '3' ELSE '0' "+
"END AS EVENT_NAME_TO "+
"FROM FL_TASK cl WHERE IS_UPDATE = ?", new String[] {"false"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncTask: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aTask.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aTask;
	}

	public void insertUpdate(Context context, ArrayList<TaskDetail> aTaskList) {
		DeviceUtility.log(TAG, "insertUpdate Task");
		if (aTaskList != null) {
			int iTaskCount = aTaskList.size();
			for (int i = 0; i < iTaskCount; i++) {
				int iInternal = isTaskExist(aTaskList.get(i).getTaskId());
				if (iInternal > 0) {
					TaskDetail task = aTaskList.get(i);
					task.setInternalNum(String.valueOf(iInternal));
					update(context, task);
				} else {
					if (aTaskList.get(i).getInternalNum() != null
							&& aTaskList.get(i).getInternalNum().length() > 0) {
						update(context, aTaskList.get(i));
					} else {
						insert(context, aTaskList.get(i));
					}
				}
			}
		}
	}

	public int isTaskExist(String strTaskId) {
		this.openRead();
		int iInternalNum = -1;
		if (strTaskId.length() > 0) {
			DeviceUtility.log(TAG, "isTaskExist");
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_TASK,
					new String[] { "INTERNAL_NUM" }, "TASK_ID = ?",
					new String[] { strTaskId }, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isTaskExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternalNum = cursor.getInt(cursor
						.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			cursor.close();
		}
		this.close();
		return iInternalNum;
	}

	public void cleanCalendar(Context context) {
		this.openRead();
		DeviceUtility.log(TAG, "cleanCalendar");
		CalendarUtility local = new CalendarUtility(context);
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_TASK,
				new String[] { "USER_DEF1", "USER_DEF2" },
				"USER_DEF1 > 0 OR USER_DEF2 > 0", null, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "cleanCalendar: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			String strCalendarEventId = cursor.getString(cursor
					.getColumnIndex("USER_DEF1"));
			String strCalendarReminderId = cursor.getString(cursor
					.getColumnIndex("USER_DEF2"));

			long lCalendarEventId = 0;
			long lCalendarReminderId = 0;

			if (strCalendarEventId != null && strCalendarEventId.length() > 0) {
				try {
					lCalendarEventId = Long.parseLong(strCalendarEventId);
				} catch (Exception e) {

				}
			}

			if (strCalendarReminderId != null
					&& strCalendarReminderId.length() > 0) {
				try {
					lCalendarReminderId = Long.parseLong(strCalendarReminderId);
				} catch (Exception e) {

				} finally {

				}
			}

			if (lCalendarEventId > 0) {
				int iResult = local.removeEvent(lCalendarEventId);
				DeviceUtility.log(TAG, "local.removeEvent: " + iResult);
			}

			if (lCalendarReminderId > 0) {
				int iResult2 = local.deleteReminder(lCalendarReminderId);
				DeviceUtility.log(TAG, "local.deleteReminder: " + iResult2);
			}

			cursor.moveToNext();
		}
		cursor.close();
		this.close();
	}
}
