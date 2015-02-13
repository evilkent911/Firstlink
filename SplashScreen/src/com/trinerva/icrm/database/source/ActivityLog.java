package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.ActivitiesLogDetail;
import com.trinerva.icrm.object.ContactActivity;
import com.trinerva.icrm.object.LeadActivity;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.database.Cursor;

public class ActivityLog extends BaseSource {
	private static String TAG = "ActivityLog";

	public ActivityLog(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public long insert(ActivitiesLogDetail dInsert) {
		this.openWrite();
		long lResult = -1;
		DeviceUtility.log(TAG, "Insert activity log list");
		ContentValues values = new ContentValues();
		DeviceUtility.log(TAG, dInsert.toString());
		values.put("ACTIVE", "0");
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		
		if (dInsert.getModifiedTimestamp() != null && dInsert.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", dInsert.getModifiedTimestamp());
		}

		if (dInsert.getIsUpdate()!= null && dInsert.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dInsert.getIsUpdate());
		}
		
		if (dInsert.getActType()!= null && dInsert.getActType().length() > 0) {
			values.put("ACT_TYPE", dInsert.getActType());
		}

		if (dInsert.getPersonType()!= null && dInsert.getPersonType().length() > 0) {
			values.put("PERSON_TYPE", dInsert.getPersonType());
		}

		if (dInsert.getContactId()!= null && dInsert.getContactId().length() > 0) {
			values.put("CONTACT_ID", dInsert.getContactId());
		}

		if (dInsert.getContactNum()!= null && dInsert.getContactNum().length() > 0) {
			values.put("CONTACT_NUM", dInsert.getContactNum());
		}

		if (dInsert.getFirstName()!= null && dInsert.getFirstName().length() > 0) {
			values.put("FIRST_NAME", dInsert.getFirstName());
		}

		if (dInsert.getLastName()!= null && dInsert.getLastName().length() > 0) {
			values.put("LAST_NAME", dInsert.getLastName());
		}

		if (dInsert.getMobile()!= null && dInsert.getMobile().length() > 0) {
			values.put("MOBILE", dInsert.getMobile());
		}

		if (dInsert.getEmail()!= null && dInsert.getEmail().length() > 0) {
			values.put("EMAIL", dInsert.getEmail());
		}

		if (dInsert.getOwner()!= null && dInsert.getOwner().length() > 0) {
			values.put("OWNER", dInsert.getOwner());
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

		if (dInsert.getUserStamp() != null && dInsert.getUserStamp().length() > 0) {
			values.put("USER_STAMP", dInsert.getUserStamp());
		}

		lResult = database.insert(DatabaseHandler.TABLE_FL_ACTIVITIES_LOG, null, values);
		this.close();
		return lResult;
	}
	
	public long update(ActivitiesLogDetail dUpdate) {
		DeviceUtility.log(TAG, "Update activity log list");
		this.openWrite();
		int iResult = -1;
		
		ContentValues values = new ContentValues();
		DeviceUtility.log(TAG, dUpdate.toString());
		//values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());

		if (dUpdate.getModifiedTimestamp()!= null && dUpdate.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", dUpdate.getModifiedTimestamp());
		}
		
		if (dUpdate.getActive()!= null && dUpdate.getActive().length() > 0) {
			values.put("ACTIVE", dUpdate.getActive());
		}
		
		if (dUpdate.getIsUpdate()!= null && dUpdate.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dUpdate.getIsUpdate());
		}
		
		if (dUpdate.getActType()!= null && dUpdate.getActType().length() > 0) {
			values.put("ACT_TYPE", dUpdate.getActType());
		}

		if (dUpdate.getPersonType()!= null && dUpdate.getPersonType().length() > 0) {
			values.put("PERSON_TYPE", dUpdate.getPersonType());
		}

		if (dUpdate.getContactId()!= null && dUpdate.getContactId().length() > 0) {
			values.put("CONTACT_ID", dUpdate.getContactId());
		}

		if (dUpdate.getContactNum()!= null && dUpdate.getContactNum().length() > 0) {
			values.put("CONTACT_NUM", dUpdate.getContactNum());
		}

		if (dUpdate.getFirstName()!= null && dUpdate.getFirstName().length() > 0) {
			values.put("FIRST_NAME", dUpdate.getFirstName());
		}

		if (dUpdate.getLastName()!= null && dUpdate.getLastName().length() > 0) {
			values.put("LAST_NAME", dUpdate.getLastName());
		}

		if (dUpdate.getMobile()!= null && dUpdate.getMobile().length() > 0) {
			values.put("MOBILE", dUpdate.getMobile());
		}

		if (dUpdate.getEmail()!= null && dUpdate.getEmail().length() > 0) {
			values.put("EMAIL", dUpdate.getEmail());
		}

		if (dUpdate.getOwner()!= null && dUpdate.getOwner().length() > 0) {
			values.put("OWNER", dUpdate.getOwner());
		}

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

		if (dUpdate.getUserStamp() != null && dUpdate.getUserStamp().length() > 0) {
			values.put("USER_STAMP", dUpdate.getUserStamp());
		}

		iResult = database.update(DatabaseHandler.TABLE_FL_ACTIVITIES_LOG, values, "INTERNAL_NUM = ?", new String[] {dUpdate.getInternalNum()});
		this.close();
		return iResult;
	}

	public Cursor getActivityLogDisplay(String strContactNum, String strPersonType, String strMode) {

		this.openRead();
		DeviceUtility.log(TAG, "getActivityLogDisplay("+strContactNum+", "+strPersonType+", "+strMode+")");
		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, ACT_TYPE, PERSON_TYPE, MOBILE, EMAIL, CREATED_TIMESTAMP FROM " + DatabaseHandler.TABLE_FL_ACTIVITIES_LOG + " WHERE ACTIVE = ? AND CONTACT_NUM = ? AND PERSON_TYPE = ? AND ACT_TYPE = ? ORDER BY INTERNAL_NUM ASC", new String[] {"0", strContactNum, strPersonType, strMode});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getActivityLogDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	////update the contact id
	public ArrayList<ContactActivity> getUnsyncContactActivityLog() {
		DeviceUtility.log(TAG, "getUnsyncContactActivityLog");
		this.openRead();
		ArrayList<ContactActivity> aActivity = new ArrayList<ContactActivity>();
		
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACTIVITIES_LOG, null, "IS_UPDATE = ? AND PERSON_TYPE = ?", new String[] {"false", Constants.PERSON_TYPE_CONTACT}, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncContactActivityLog: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			ContactActivity activity = new ContactActivity();
			String strActionType = cursor.getString(cursor.getColumnIndex("ACT_TYPE"));
			activity.setActivityTime(Utility.toXmlDateTime(cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")), Constants.DATETIMESEC_FORMAT));
			activity.setActivityType(strActionType);
			activity.setContactID(cursor.getString(cursor.getColumnIndex("CONTACT_ID")));
			if (strActionType.equals(Constants.ACTION_CALL)) {
				activity.setSubject(Constants.CALL_SUBJECT);
				activity.setDescription(Constants.CALL_DESCRIPTION);
			} else if (strActionType.equals(Constants.ACTION_EMAIL)) {
				activity.setSubject(Constants.EMAIL_SUBJECT);
				activity.setDescription(Constants.EMAIL_DESCRIPTION);
			} else if (strActionType.equals(Constants.ACTION_SMS)) {
				activity.setSubject(Constants.SMS_SUBJECT);
				activity.setDescription(Constants.SMS_DESCRIPTION);
			}
			aActivity.add(activity);
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aActivity;
	}
	
	public ArrayList<LeadActivity> getUnsyncLeadActivityLog() {
		DeviceUtility.log(TAG, "getUnsyncLeadActivityLog");
		this.openRead();
		ArrayList<LeadActivity> aActivity = new ArrayList<LeadActivity>();
		
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACTIVITIES_LOG, null, "IS_UPDATE = ? AND PERSON_TYPE = ?", new String[] {"false", Constants.PERSON_TYPE_LEAD}, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncLeadActivityLog: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			LeadActivity activity = new LeadActivity();
			String strActionType = cursor.getString(cursor.getColumnIndex("ACT_TYPE"));
			activity.setActivityTime(Utility.toXmlDateTime(cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")), Constants.DATETIMESEC_FORMAT));
			activity.setActivityType(strActionType);
			activity.setLeadId(cursor.getString(cursor.getColumnIndex("CONTACT_ID")));
			if (strActionType.equals(Constants.ACTION_CALL)) {
				activity.setSubject(Constants.CALL_SUBJECT);
				activity.setDescription(Constants.CALL_DESCRIPTION);
			} else if (strActionType.equals(Constants.ACTION_EMAIL)) {
				activity.setSubject(Constants.EMAIL_SUBJECT);
				activity.setDescription(Constants.EMAIL_DESCRIPTION);
			} else if (strActionType.equals(Constants.ACTION_SMS)) {
				activity.setSubject(Constants.SMS_SUBJECT);
				activity.setDescription(Constants.SMS_DESCRIPTION);
			}
			aActivity.add(activity);
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aActivity;
	}
	
	public int updateSystemId(String strInternalNumber, String strSystemId, String strType) {
		DeviceUtility.log(TAG, "updateSystemId("+strInternalNumber+", "+strSystemId+", "+strType+")");
		this.openWrite();
		int iSuccess = 0;
		ContentValues values = new ContentValues();
		values.put("CONTACT_ID", strSystemId);
		iSuccess = database.update(DatabaseHandler.TABLE_FL_ACTIVITIES_LOG, values, "CONTACT_NUM = ? AND PERSON_TYPE = ?", new String[] {strInternalNumber, strType});
		this.close();
		return iSuccess;
	}
	
	public int updateSyncedActivityLog(String strType) {
		DeviceUtility.log(TAG, "updateSyncedActivityLog("+strType+")");
		this.openWrite();
		int iSuccess = 0;
		ContentValues values = new ContentValues();
		values.put("IS_UPDATE", "true");
		iSuccess = database.update(DatabaseHandler.TABLE_FL_ACTIVITIES_LOG, values, "PERSON_TYPE = ?", new String[] {strType});
		this.close();
		return iSuccess;
	}
	
	/*
	private ActivitiesLogDetail setToObject(Cursor cursor) {
		ActivitiesLogDetail activity = new ActivitiesLogDetail();
		activity.setInternalNum(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
		activity.setActType(cursor.getString(cursor.getColumnIndex("ACT_TYPE")));
		activity.setPersonType(cursor.getString(cursor.getColumnIndex("PERSON_TYPE")));
		activity.setContactId(cursor.getString(cursor.getColumnIndex("CONTACT_ID")));
		activity.setContactNum(cursor.getString(cursor.getColumnIndex("CONTACT_NUM")));
		activity.setFirstName(cursor.getString(cursor.getColumnIndex("FIRST_NAME")));
		activity.setLastName(cursor.getString(cursor.getColumnIndex("LAST_NAME")));
		activity.setMobile(cursor.getString(cursor.getColumnIndex("MOBILE")));
		activity.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
		activity.setOwner(cursor.getString(cursor.getColumnIndex("OWNER")));
		activity.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		activity.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
		activity.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
		activity.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
		activity.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
		activity.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
		activity.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
		activity.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
		activity.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
		activity.setUserStamp(cursor.getString(cursor.getColumnIndex("USER_STAMP")));
		activity.setCreatedTimestamp(cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")));
		activity.setModifiedTimestamp(cursor.getString(cursor.getColumnIndex("MODIFIED_TIMESTAMP")));
		activity.setIsUpdate(cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
		return activity;
	}*/
}
