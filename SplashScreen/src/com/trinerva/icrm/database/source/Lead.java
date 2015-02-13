package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.database.Cursor;

public class Lead extends BaseSource {
	private static String TAG = "Lead";

	public Lead(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public Cursor getLeadListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE ,IFNULL(FIRST_NAME,'') || LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) GROUP BY INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getLeadListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getAllDataCount() {
		this.openRead();
		Cursor cursor = database.rawQuery("select * from "+DatabaseHandler.TABLE_FL_LEAD, new String[]{});
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getNewThisWeekLeadListDisplay(String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',CREATED_TIMESTAMP) CreatedYear,strftime('%W',CREATED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE ,IFNULL(FIRST_NAME,'') || LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "1", "true", weekCount, weekCount });
		// Cursor cursor =
		// database.rawQuery("SELECT strftime('%Y',MODIFIED_TIMESTAMP) CreatedYear,strftime('%W',MODIFIED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
		// + DatabaseHandler.TABLE_FL_LEAD +
		// " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) ORDER BY INTERNAL_NUM ASC",
		// new String[] {"1", "true",weekCount,weekCount});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getLeadListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getModifiedThisWeekLeadListDisplay(String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',MODIFIED_TIMESTAMP) CreatedYear,strftime('%W',MODIFIED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE ,IFNULL(FIRST_NAME,'') || LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "1", "true", weekCount, weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getLeadListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getNameToName() {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND _id = ? ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getLeadListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getSyncListNameTo(String str) {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND LEAD_ID = ? ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true",str });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getLeadListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getUnSyncListNameTo(String str) {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND INTERNAL_NUM = ? ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true",str });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getLeadListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getLeadListDisplayByFilter(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE ,IFNULL(FIRST_NAME,'') || LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) GROUP BY INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "1", "true", "%" + strFilter + "%",
								"%" + strFilter + "%", "%" + strFilter + "%",
								"%" + strFilter + "%" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getLeadInviteeListDisplayByFilter(String strFilter,
			String strFilterIn) {
		this.openRead();
		System.out.println("lead - " + strFilterIn);
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM,LEAD_ID, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME,EMAIL1, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE LEAD_ID IS NOT NULL AND LEAD_ID <> '' AND (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?)  AND (EMAIL1 NOT NULL OR EMAIL2 NOT NULL OR EMAIL3 NOT NULL) AND LEAD_ID NOT IN ("
								+ strFilterIn + ") ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true", "%" + strFilter + "%",
								"%" + strFilter + "%", "%" + strFilter + "%",
								"%" + strFilter + "%" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getSelectLeadInviteeListDisplayByFilter(String strFilter,
			String strFilterIn) {
		this.openRead();
		System.out.println("lead - " + strFilterIn);
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM,LEAD_ID, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME,EMAIL1, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND LEAD_ID IN ("
								+ strFilterIn + ") ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true", "%" + strFilter + "%",
								"%" + strFilter + "%", "%" + strFilter + "%",
								"%" + strFilter + "%" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getNameToName(String strFilter) {
		this.openRead();
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND _id = ? ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true", strFilter });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getSyncNameToName(String strFilter) {
		this.openRead();
		Cursor cursor = database
				.rawQuery(
						"SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX,LEAD_ID, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND LEAD_ID = ? ORDER BY INTERNAL_NUM ASC",
						new String[] { "1", "true", strFilter });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getNewThisWeekLeadListDisplayByFilter(String strFilter,
			String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',CREATED_TIMESTAMP) CreatedYear,strftime('%W',CREATED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE ,IFNULL(FIRST_NAME,'') || LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "1", "true", "%" + strFilter + "%",
								"%" + strFilter + "%", "%" + strFilter + "%",
								"%" + strFilter + "%", weekCount, weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getModifiedThisWeekLeadListDisplayByFilter(String strFilter,
			String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',MODIFIED_TIMESTAMP) CreatedYear,strftime('%W',MODIFIED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE ,IFNULL(FIRST_NAME,'') || LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_LEAD
								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "1", "true", "%" + strFilter + "%",
								"%" + strFilter + "%", "%" + strFilter + "%",
								"%" + strFilter + "%", weekCount, weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public LeadDetail getLeadDetailById(String ID) {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadDetailById(" + ID + ")");
		LeadDetail object = new LeadDetail();
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_LEAD, null,
				"INTERNAL_NUM = ?", new String[] { ID }, null, null, null);
		cursor.moveToFirst();
		object = setToObject(cursor);

		DeviceUtility.log(TAG, object.toString());
		cursor.close();
		//this.close();
		return object;
	}
	
	public Cursor getLeadDetailByLeadId(String ID) {
		this.openRead();
		DeviceUtility.log(TAG, "getLeadDetailById(" + ID + ")");
		LeadDetail object = new LeadDetail();
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_LEAD, null,
				"LEAD_ID = ?", new String[] { ID }, null, null, null);
		cursor.moveToFirst();
//		cursor.close();
//		//this.close();
		return cursor;
	}

	public long insert(LeadDetail dInsert) {
		this.openWrite();
		long lResult = -1;
		DeviceUtility.log(TAG, "Insert lead list");
		ContentValues values = new ContentValues();
		DeviceUtility.log(TAG, dInsert.toString());

		values.put("ACTIVE", "0");
		values.put("IS_UPDATE", "false");
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("UPDATE_GPS_LOCATION", "true");

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

		if (dInsert.getUpdateGpsLocation() != null
				&& dInsert.getUpdateGpsLocation().length() > 0) {
			values.put("UPDATE_GPS_LOCATION", dInsert.getUpdateGpsLocation());
		}

		if (dInsert.getLeadId() != null && dInsert.getLeadId().length() > 0) {
			values.put("LEAD_ID", dInsert.getLeadId());
		}

		if (dInsert.getFirstName() != null
				&& dInsert.getFirstName().length() > 0) {
			values.put("FIRST_NAME", dInsert.getFirstName());
		}

		if (dInsert.getLastName() != null && dInsert.getLastName().length() > 0) {
			values.put("LAST_NAME", dInsert.getLastName());
		}

		if (dInsert.getPrefix() != null && dInsert.getPrefix().length() > 0) {
			values.put("PREFIX", dInsert.getPrefix());
		}

		if (dInsert.getLeadSource() != null
				&& dInsert.getLeadSource().length() > 0) {
			values.put("LEAD_SOURCE", dInsert.getLeadSource());
		}

		if (dInsert.getLeadStatus() != null
				&& dInsert.getLeadStatus().length() > 0) {
			values.put("LEAD_STATUS", dInsert.getLeadStatus());
		}

		if (dInsert.getAttitude() != null && dInsert.getAttitude().length() > 0) {
			values.put("ATTITUDE", dInsert.getAttitude());
		}

		if (dInsert.getAnnualRevenue() != null
				&& dInsert.getAnnualRevenue().length() > 0) {
			values.put("ANNUAL_REVENUE", dInsert.getAnnualRevenue());
		}

		if (dInsert.getBirthday() != null && dInsert.getBirthday().length() > 0) {
			values.put("BIRTHDAY", dInsert.getBirthday());
		}

		if (dInsert.getCompanyName() != null
				&& dInsert.getCompanyName().length() > 0) {
			values.put("COMPANY_NAME", dInsert.getCompanyName());
		}

		if (dInsert.getJobTitle() != null && dInsert.getJobTitle().length() > 0) {
			values.put("JOB_TITLE", dInsert.getJobTitle());
		}

		if (dInsert.getMobile() != null && dInsert.getMobile().length() > 0) {
			values.put("MOBILE", dInsert.getMobile());
		}

		if (dInsert.getWorkPhone() != null
				&& dInsert.getWorkPhone().length() > 0) {
			values.put("WORK_PHONE", dInsert.getWorkPhone());
		}

		if (dInsert.getWorkFax() != null && dInsert.getWorkFax().length() > 0) {
			values.put("WORK_FAX", dInsert.getWorkFax());
		}

		if (dInsert.getMailingCity() != null
				&& dInsert.getMailingCity().length() > 0) {
			values.put("MAILING_CITY", dInsert.getMailingCity());
		}

		if (dInsert.getMailingCountry() != null
				&& dInsert.getMailingCountry().length() > 0) {
			values.put("MAILING_COUNTRY", dInsert.getMailingCountry());
		}

		if (dInsert.getMailingState() != null
				&& dInsert.getMailingState().length() > 0) {
			values.put("MAILING_STATE", dInsert.getMailingState());
		}

		if (dInsert.getMailingStreet() != null
				&& dInsert.getMailingStreet().length() > 0) {
			values.put("MAILING_STREET", dInsert.getMailingStreet());
		}

		if (dInsert.getMailingZip() != null
				&& dInsert.getMailingZip().length() > 0) {
			values.put("MAILING_ZIP", dInsert.getMailingZip());
		}

		if (dInsert.getEmail1() != null && dInsert.getEmail1().length() > 0) {
			values.put("EMAIL1", dInsert.getEmail1());
		}

		if (dInsert.getEmail2() != null && dInsert.getEmail2().length() > 0) {
			values.put("EMAIL2", dInsert.getEmail2());
		}

		if (dInsert.getEmail3() != null && dInsert.getEmail3().length() > 0) {
			values.put("EMAIL3", dInsert.getEmail3());
		}

		if (dInsert.getSkypeId() != null && dInsert.getSkypeId().length() > 0) {
			values.put("SKYPE_ID", dInsert.getSkypeId());
		}

		if (dInsert.getWebsite() != null && dInsert.getWebsite().length() > 0) {
			values.put("WEBSITE", dInsert.getWebsite());
		}

		if (dInsert.getOwner() != null && dInsert.getOwner().length() > 0) {
			values.put("OWNER", dInsert.getOwner());
		}

		if (dInsert.getNoOfEmployee() != null
				&& dInsert.getNoOfEmployee().length() > 0) {
			values.put("NO_OF_EMPLOYEE", dInsert.getNoOfEmployee());
		}

		if (dInsert.getIndustry() != null && dInsert.getIndustry().length() > 0) {
			values.put("INDUSTRY", dInsert.getIndustry());
		}

		if (dInsert.getDescription() != null
				&& dInsert.getDescription().length() > 0) {
			values.put("DESCRIPTION", dInsert.getDescription());
		}

		if (dInsert.getPhoto() != null && dInsert.getPhoto().length() > 0) {
			values.put("PHOTO", dInsert.getPhoto());
		}

		if (dInsert.getGpsLat() != null && dInsert.getGpsLat().length() > 0) {
			values.put("GPS_LAT", dInsert.getGpsLat());
		}

		if (dInsert.getGpsLong() != null && dInsert.getGpsLong().length() > 0) {
			values.put("GPS_LONG", dInsert.getGpsLong());
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

		lResult = database.insert(DatabaseHandler.TABLE_FL_LEAD, null, values);

		//this.close();
		return lResult;
	}

	public int update(LeadDetail oUpdate) {
		this.openWrite();
		int iResult = 0;
		DeviceUtility.log(TAG, "update contact list");
		ContentValues values = new ContentValues();
		DeviceUtility.log(TAG, oUpdate.toString());

		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("UPDATE_GPS_LOCATION", "true");

		/*
		 * if (oUpdate.getModifiedTimestamp()!= null &&
		 * oUpdate.getModifiedTimestamp().length() > 0) {
		 * values.put("MODIFIED_TIMESTAMP", oUpdate.getModifiedTimestamp()); }
		 */

		if (oUpdate.getIsUpdate() != null && oUpdate.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", oUpdate.getIsUpdate());
		}
		values.put("LEAD_ID", oUpdate.getLeadId());
		values.put("ACTIVE", oUpdate.getActive());
		values.put("UPDATE_GPS_LOCATION", oUpdate.getUpdateGpsLocation());
		values.put("FIRST_NAME", oUpdate.getFirstName());
		values.put("LAST_NAME", oUpdate.getLastName());
		values.put("PREFIX", oUpdate.getPrefix());
		values.put("LEAD_SOURCE", oUpdate.getLeadSource());
		values.put("LEAD_STATUS", oUpdate.getLeadStatus());
		values.put("ATTITUDE", oUpdate.getAttitude());
		values.put("ANNUAL_REVENUE", oUpdate.getAnnualRevenue());
		values.put("BIRTHDAY", oUpdate.getBirthday());
		values.put("COMPANY_NAME", oUpdate.getCompanyName());
		values.put("JOB_TITLE", oUpdate.getJobTitle());
		values.put("MOBILE", oUpdate.getMobile());
		values.put("WORK_PHONE", oUpdate.getWorkPhone());
		values.put("WORK_FAX", oUpdate.getWorkFax());
		values.put("MAILING_CITY", oUpdate.getMailingCity());
		values.put("MAILING_COUNTRY", oUpdate.getMailingCountry());
		values.put("MAILING_STATE", oUpdate.getMailingState());
		values.put("MAILING_STREET", oUpdate.getMailingStreet());
		values.put("MAILING_ZIP", oUpdate.getMailingZip());
		values.put("EMAIL1", oUpdate.getEmail1());
		values.put("EMAIL2", oUpdate.getEmail2());
		values.put("EMAIL3", oUpdate.getEmail3());
		values.put("SKYPE_ID", oUpdate.getSkypeId());
		values.put("WEBSITE", oUpdate.getWebsite());
		values.put("OWNER", oUpdate.getOwner());
		values.put("NO_OF_EMPLOYEE", oUpdate.getNoOfEmployee());
		values.put("INDUSTRY", oUpdate.getIndustry());
		values.put("DESCRIPTION", oUpdate.getDescription());
		values.put("GPS_LAT", oUpdate.getGpsLat());
		values.put("GPS_LONG", oUpdate.getGpsLong());
		values.put("PHOTO", oUpdate.getPhoto());

		if (oUpdate.getUserDef1() != null && oUpdate.getUserDef1().length() > 0) {
			values.put("USER_DEF1", oUpdate.getUserDef1());
		}

		if (oUpdate.getUserDef2() != null && oUpdate.getUserDef2().length() > 0) {
			values.put("USER_DEF2", oUpdate.getUserDef2());
		}

		if (oUpdate.getUserDef3() != null && oUpdate.getUserDef3().length() > 0) {
			values.put("USER_DEF3", oUpdate.getUserDef3());
		}

		if (oUpdate.getUserDef4() != null && oUpdate.getUserDef4().length() > 0) {
			values.put("USER_DEF4", oUpdate.getUserDef4());
		}

		if (oUpdate.getUserDef5() != null && oUpdate.getUserDef5().length() > 0) {
			values.put("USER_DEF5", oUpdate.getUserDef5());
		}

		if (oUpdate.getUserDef6() != null && oUpdate.getUserDef6().length() > 0) {
			values.put("USER_DEF6", oUpdate.getUserDef6());
		}

		if (oUpdate.getUserDef7() != null && oUpdate.getUserDef7().length() > 0) {
			values.put("USER_DEF7", oUpdate.getUserDef7());
		}

		if (oUpdate.getUserDef8() != null && oUpdate.getUserDef8().length() > 0) {
			values.put("USER_DEF8", oUpdate.getUserDef8());
		}

		values.put("USER_STAMP", oUpdate.getUserStamp());

		iResult = database.update(DatabaseHandler.TABLE_FL_LEAD, values,
				"INTERNAL_NUM = ?", new String[] { oUpdate.getInternalNum() });

		//this.close();
		return iResult;
	}

	public int updatePhoto(LeadDetail oLead) {
		DeviceUtility.log(TAG, "updatePhoto(" + oLead.toString() + ")");
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("PHOTO", oLead.getPhoto());
		// values.put("MODIFIED_TIMESTAMP",
		// Utility.getDatabaseCurrentDateTime());
		iResult = database.update(DatabaseHandler.TABLE_FL_LEAD, values,
				"INTERNAL_NUM = ?", new String[] { oLead.getInternalNum() });
		//this.close();
		return iResult;
	}

	public int delete(String Id) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("ACTIVE", "1");
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		iResult = database.update(DatabaseHandler.TABLE_FL_LEAD, values,
				"INTERNAL_NUM = ?", new String[] { Id });
		//this.close();
		return iResult;
	}

	public LeadDetail setToObject(Cursor cursor) {
		LeadDetail lead = new LeadDetail();
		lead.setInternalNum(cursor.getString(cursor
				.getColumnIndex("INTERNAL_NUM")));
		lead.setLeadId(cursor.getString(cursor.getColumnIndex("LEAD_ID")));
		lead.setFirstName(cursor.getString(cursor.getColumnIndex("FIRST_NAME")));
		lead.setLastName(cursor.getString(cursor.getColumnIndex("LAST_NAME")));
		lead.setPrefix(cursor.getString(cursor.getColumnIndex("PREFIX")));
		lead.setLeadSource(cursor.getString(cursor
				.getColumnIndex("LEAD_SOURCE")));
		lead.setLeadStatus(cursor.getString(cursor
				.getColumnIndex("LEAD_STATUS")));
		lead.setAttitude(cursor.getString(cursor.getColumnIndex("ATTITUDE")));
		lead.setAnnualRevenue(cursor.getString(cursor
				.getColumnIndex("ANNUAL_REVENUE")));
		lead.setBirthday(cursor.getString(cursor.getColumnIndex("BIRTHDAY")));
		lead.setCompanyName(cursor.getString(cursor
				.getColumnIndex("COMPANY_NAME")));
		lead.setJobTitle(cursor.getString(cursor.getColumnIndex("JOB_TITLE")));
		lead.setMobile(Utility.MobileFormat(cursor.getString(cursor.getColumnIndex("MOBILE"))));
		lead.setWorkPhone(cursor.getString(cursor.getColumnIndex("WORK_PHONE")));
		lead.setWorkFax(cursor.getString(cursor.getColumnIndex("WORK_FAX")));
		lead.setMailingCity(cursor.getString(cursor
				.getColumnIndex("MAILING_CITY")));
		lead.setMailingCountry(cursor.getString(cursor
				.getColumnIndex("MAILING_COUNTRY")));
		lead.setMailingState(cursor.getString(cursor
				.getColumnIndex("MAILING_STATE")));
		lead.setMailingStreet(cursor.getString(cursor
				.getColumnIndex("MAILING_STREET")));
		lead.setMailingZip(cursor.getString(cursor
				.getColumnIndex("MAILING_ZIP")));
		lead.setEmail1(cursor.getString(cursor.getColumnIndex("EMAIL1")));
		lead.setEmail2(cursor.getString(cursor.getColumnIndex("EMAIL2")));
		lead.setEmail3(cursor.getString(cursor.getColumnIndex("EMAIL3")));
		lead.setSkypeId(cursor.getString(cursor.getColumnIndex("SKYPE_ID")));
		lead.setWebsite(cursor.getString(cursor.getColumnIndex("WEBSITE")));
		lead.setOwner(cursor.getString(cursor.getColumnIndex("OWNER")));
		lead.setNoOfEmployee(cursor.getString(cursor
				.getColumnIndex("NO_OF_EMPLOYEE")));
		lead.setIndustry(cursor.getString(cursor.getColumnIndex("INDUSTRY")));
		lead.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		lead.setDescription(cursor.getString(cursor
				.getColumnIndex("DESCRIPTION")));
		lead.setGpsLat(cursor.getString(cursor.getColumnIndex("GPS_LAT")));
		lead.setGpsLong(cursor.getString(cursor.getColumnIndex("GPS_LONG")));
		lead.setUpdateGpsLocation(cursor.getString(cursor
				.getColumnIndex("UPDATE_GPS_LOCATION")));
		lead.setPhoto(cursor.getString(cursor.getColumnIndex("PHOTO")));
		lead.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
		lead.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
		lead.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
		lead.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
		lead.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
		lead.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
		lead.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
		lead.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
		lead.setUserStamp(cursor.getString(cursor.getColumnIndex("USER_STAMP")));
		lead.setCreatedTimestamp(cursor.getString(cursor
				.getColumnIndex("CREATED_TIMESTAMP")));
		lead.setModifiedTimestamp(cursor.getString(cursor
				.getColumnIndex("MODIFIED_TIMESTAMP")));
		lead.setIsUpdate(cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
		return lead;
	}

	public ArrayList<LeadDetail> getUnsyncLead() {
		this.openRead();
		ArrayList<LeadDetail> aLead = new ArrayList<LeadDetail>();
		DeviceUtility.log(TAG, "getUnsyncLead");
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_LEAD, null,
				"IS_UPDATE = ?", new String[] { "false" }, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncLead: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aLead.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		//this.close();
		return aLead;
	}

	public int isLeadExist(String strLeadId) {
		this.openRead();
		DeviceUtility.log(TAG, "isLeadExist");
		int iInternal = -1;
		if (strLeadId.length() > 0) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_LEAD,
					new String[] { "INTERNAL_NUM" }, "LEAD_ID = ?",
					new String[] { strLeadId }, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isLeadExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternal = cursor
						.getInt(cursor.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			cursor.close();
		}

		//this.close();
		return iInternal;
	}

	public void insertUpdate(ArrayList<LeadDetail> lead) {
		int iChange = 0;
		if (lead != null) {
			int iLeadCount = lead.size();
			for (int i = 0; i < iLeadCount; i++) {
				int iInternal = isLeadExist(lead.get(i).getLeadId());
				if (iInternal > 0) {
					LeadDetail leadObj = lead.get(i);
					leadObj.setInternalNum(String.valueOf(iInternal));
					iChange = update(leadObj);
					if (iChange > 0) {
						// update activity log.
						ActivityLog log = new ActivityLog(dbHandler);
						log.updateSystemId(leadObj.getInternalNum(),
								leadObj.getLeadId(), Constants.PERSON_TYPE_LEAD);
					}
				} else {
					if (lead.get(i).getInternalNum() != null
							&& lead.get(i).getInternalNum().length() > 0) {
						iChange = update(lead.get(i));
						if (iChange > 0) {
							// update activity log.
							ActivityLog log = new ActivityLog(dbHandler);
							log.updateSystemId(lead.get(i).getInternalNum(),
									lead.get(i).getLeadId(),
									Constants.PERSON_TYPE_LEAD);
						}
					} else {
						insert(lead.get(i));
					}
				}
			}
		}
	}
}
