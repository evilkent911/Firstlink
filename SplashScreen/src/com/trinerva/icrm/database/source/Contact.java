package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.database.Cursor;

public class Contact extends BaseSource {
	private static String TAG = "Contact";

	public Contact(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public int delete(String Id) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("ACTIVE", "1");
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		iResult = database.update(DatabaseHandler.TABLE_FL_CONTACT, values,
				"INTERNAL_NUM = ?", new String[] { Id });
		//this.close();
		return iResult;
	}

	public int update(ContactDetail oUpdate) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
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

		// if (oUpdate.getActive()!= null && oUpdate.getActive().length() > 0) {
		values.put("ACTIVE", oUpdate.getActive());
		// }

		if (oUpdate.getUpdateGpsLocation() != null
				&& oUpdate.getUpdateGpsLocation().length() > 0) {
			values.put("UPDATE_GPS_LOCATION", oUpdate.getUpdateGpsLocation());
		}

		// if (oUpdate.getContactId()!= null && oUpdate.getContactId().length()
		// > 0) {
		values.put("CONTACT_ID", oUpdate.getContactId());
		// }

		// if (oUpdate.getFirstName()!= null && oUpdate.getFirstName().length()
		// > 0) {
		values.put("FIRST_NAME", oUpdate.getFirstName());
		// }

		// if (oUpdate.getLastName() != null && oUpdate.getLastName().length() >
		// 0) {
		values.put("LAST_NAME", oUpdate.getLastName());
		// }

		// if (oUpdate.getPrefix() != null && oUpdate.getPrefix().length() > 0)
		// {
		values.put("PREFIX", oUpdate.getPrefix());
		// }

		// if (oUpdate.getBirthday() != null && oUpdate.getBirthday().length() >
		// 0) {
		values.put("BIRTHDAY", oUpdate.getBirthday());
		// }

		// if (oUpdate.getCompanyName() != null &&
		// oUpdate.getCompanyName().length() > 0) {
		values.put("COMPANY_NAME", oUpdate.getCompanyName());
		// }
		
		values.put("COMPANY_ID", oUpdate.getCompanyId());

		// if (oUpdate.getDepartment() != null &&
		// oUpdate.getDepartment().length() > 0) {
		values.put("DEPARTMENT", oUpdate.getDepartment());
		// }

		// if (oUpdate.getJobTitle() != null && oUpdate.getJobTitle().length() >
		// 0) {
		values.put("JOB_TITLE", oUpdate.getJobTitle());
		// }

		// if (oUpdate.getMobile() != null && oUpdate.getMobile().length() > 0)
		// {
		values.put("MOBILE", oUpdate.getMobile());
		// }

		// if (oUpdate.getHomePhone() != null && oUpdate.getHomePhone().length()
		// > 0) {
		values.put("HOME_PHONE", oUpdate.getHomePhone());
		// }

		// if (oUpdate.getWorkPhone() != null && oUpdate.getWorkPhone().length()
		// > 0) {
		values.put("WORK_PHONE", oUpdate.getWorkPhone());
		// }

		// if (oUpdate.getOtherPhone() != null &&
		// oUpdate.getOtherPhone().length() > 0) {
		values.put("OTHER_PHONE", oUpdate.getOtherPhone());
		// }

		// if (oUpdate.getWorkFax() != null && oUpdate.getWorkFax().length() >
		// 0) {
		values.put("WORK_FAX", oUpdate.getWorkFax());
		// }

		// if (oUpdate.getMailingCity() != null &&
		// oUpdate.getMailingCity().length() > 0) {
		values.put("MAILING_CITY", oUpdate.getMailingCity());
		// }

		// if (oUpdate.getMailingCountry() != null &&
		// oUpdate.getMailingCountry().length() > 0) {
		values.put("MAILING_COUNTRY", oUpdate.getMailingCountry());
		// }

		// if (oUpdate.getMailingState() != null &&
		// oUpdate.getMailingState().length() > 0) {
		values.put("MAILING_STATE", oUpdate.getMailingState());
		// }

		// if (oUpdate.getMailingStreet() != null &&
		// oUpdate.getMailingStreet().length() > 0) {
		values.put("MAILING_STREET", oUpdate.getMailingStreet());
		// }

		// if (oUpdate.getMailingZip() != null &&
		// oUpdate.getMailingZip().length() > 0) {
		values.put("MAILING_ZIP", oUpdate.getMailingZip());
		// }

		// if (oUpdate.getEmail1() != null && oUpdate.getEmail1().length() > 0)
		// {
		values.put("EMAIL1", oUpdate.getEmail1());
		// }

		// if (oUpdate.getEmail2() != null && oUpdate.getEmail2().length() > 0)
		// {
		values.put("EMAIL2", oUpdate.getEmail2());
		// }

		// if (oUpdate.getEmail3() != null && oUpdate.getEmail3().length() > 0)
		// {
		values.put("EMAIL3", oUpdate.getEmail3());
		// }

		// if (oUpdate.getSkypeId() != null && oUpdate.getSkypeId().length() >
		// 0) {
		values.put("SKYPE_ID", oUpdate.getSkypeId());
		// }

		// if (oUpdate.getAssistantName() != null &&
		// oUpdate.getAssistantName().length() > 0) {
		values.put("ASSISTANT_NAME", oUpdate.getAssistantName());
		// }

		// if (oUpdate.getAssistantPhone() != null &&
		// oUpdate.getAssistantPhone().length() > 0) {
		values.put("ASSISTANT_PHONE", oUpdate.getAssistantPhone());
		// }

		// if (oUpdate.getOwner() != null && oUpdate.getOwner().length() > 0) {
		values.put("OWNER", oUpdate.getOwner());
		// }

		// if (oUpdate.getDescription() != null &&
		// oUpdate.getDescription().length() > 0) {
		values.put("DESCRIPTION", oUpdate.getDescription());
		// }

		// if (oUpdate.getGpsLat() != null && oUpdate.getGpsLat().length() > 0)
		// {
		values.put("GPS_LAT", oUpdate.getGpsLat());
		// }

		// if (oUpdate.getGpsLong() != null && oUpdate.getGpsLong().length() >
		// 0) {
		values.put("GPS_LONG", oUpdate.getGpsLong());
		// }

		// if (oUpdate.getPhoto() != null && oUpdate.getPhoto().length() > 0) {
		values.put("PHOTO", oUpdate.getPhoto());
		// }

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

		// if (oUpdate.getUserStamp() != null && oUpdate.getUserStamp().length()
		// > 0) {
		values.put("USER_STAMP", oUpdate.getUserStamp());
		// }
		iResult = database.update(DatabaseHandler.TABLE_FL_CONTACT, values,
				"INTERNAL_NUM = ?",
				new String[] { oUpdate.getInternalNumber() });
		//this.close();
		return iResult;
	}

	public long insert(ContactDetail dInsert) {
		this.openWrite();
		long lResult = -1;
		DeviceUtility.log(TAG, "Insert contact list");
		ContentValues values = new ContentValues();
		DeviceUtility.log(TAG, dInsert.toString());
		// by default should be 1
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

		if (dInsert.getContactId() != null
				&& dInsert.getContactId().length() > 0) {
			values.put("CONTACT_ID", dInsert.getContactId());
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

		if (dInsert.getBirthday() != null && dInsert.getBirthday().length() > 0) {
			values.put("BIRTHDAY", dInsert.getBirthday());
		}

		if (dInsert.getCompanyName() != null
				&& dInsert.getCompanyName().length() > 0) {
			values.put("COMPANY_NAME", dInsert.getCompanyName());
		}
		
		if (dInsert.getCompanyId() != null
				&& dInsert.getCompanyId().length() > 0) {
			values.put("COMPANY_ID", dInsert.getCompanyId());
		}

		if (dInsert.getDepartment() != null
				&& dInsert.getDepartment().length() > 0) {
			values.put("DEPARTMENT", dInsert.getDepartment());
		}

		if (dInsert.getJobTitle() != null && dInsert.getJobTitle().length() > 0) {
			values.put("JOB_TITLE", dInsert.getJobTitle());
		}

		if (dInsert.getMobile() != null && dInsert.getMobile().length() > 0) {
			values.put("MOBILE", dInsert.getMobile());
		}

		if (dInsert.getHomePhone() != null
				&& dInsert.getHomePhone().length() > 0) {
			values.put("HOME_PHONE", dInsert.getHomePhone());
		}

		if (dInsert.getWorkPhone() != null
				&& dInsert.getWorkPhone().length() > 0) {
			values.put("WORK_PHONE", dInsert.getWorkPhone());
		}

		if (dInsert.getOtherPhone() != null
				&& dInsert.getOtherPhone().length() > 0) {
			values.put("OTHER_PHONE", dInsert.getOtherPhone());
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

		if (dInsert.getAssistantName() != null
				&& dInsert.getAssistantName().length() > 0) {
			values.put("ASSISTANT_NAME", dInsert.getAssistantName());
		}

		if (dInsert.getAssistantPhone() != null
				&& dInsert.getAssistantPhone().length() > 0) {
			values.put("ASSISTANT_PHONE", dInsert.getAssistantPhone());
		}

		if (dInsert.getOwner() != null && dInsert.getOwner().length() > 0) {
			values.put("OWNER", dInsert.getOwner());
		}

		if (dInsert.getDescription() != null
				&& dInsert.getDescription().length() > 0) {
			values.put("DESCRIPTION", dInsert.getDescription());
		}

		if (dInsert.getGpsLat() != null && dInsert.getGpsLat().length() > 0) {
			values.put("GPS_LAT", dInsert.getGpsLat());
		}

		if (dInsert.getGpsLong() != null && dInsert.getGpsLong().length() > 0) {
			values.put("GPS_LONG", dInsert.getGpsLong());
		}

		if (dInsert.getPhoto() != null && dInsert.getPhoto().length() > 0) {
			values.put("PHOTO", dInsert.getPhoto());
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

		lResult = database.insert(DatabaseHandler.TABLE_FL_CONTACT, null,
				values);

		//this.close();
		return lResult;
	}

	public ContactDetail getContactDetailById(String ID) {
		this.openRead();
		ContactDetail object = null;
		try {
			DeviceUtility.log(TAG, "getContactDetailById(" + ID + ")");
			 object = new ContactDetail();
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONTACT,
					null, "INTERNAL_NUM = ?", new String[] { ID }, null, null,
					null);
			cursor.moveToFirst();
			object = setToObject(cursor);

			DeviceUtility.log(TAG, object.toString());
			//cursor.close();
			//this.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return object;
	}

	public Cursor getNewThisWeekContactListDisplay(String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.CREATED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE ,IFNULL(A.FIRST_NAME,'') || A.LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "false", "1", "true", weekCount,
								weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getAllDataCount() {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
//		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONTACT,
//				null, "INTERNAL_NUM = ?", new String[] { "" }, null, null,
//				null);
		Cursor cursor = database.rawQuery("select * from "+DatabaseHandler.TABLE_FL_CONTACT, new String[]{});
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getModifiedThisWeekContactListDisplay(String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.MODIFIED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE ,IFNULL(A.FIRST_NAME,'') || A.LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "false", "1", "true", weekCount,
								weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getContactListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE ,IFNULL(A.FIRST_NAME,'') || A.LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) GROUP BY A.INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "false", "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getNameToName(String str) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND _id = ? GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "1", "true" ,str});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getSyncNameToName(String str) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
//								+ DatabaseHandler.TABLE_FL_CONTACT
//								+ " AS A LEFT JOIN "
//								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
//								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND C = ? GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
//						new String[] { "false", "1", "true" ,str});
//		cursor.moveToFirst();
//		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONTACT, null,
				"CONTACT_ID = ?", new String[] { str }, null, null, null);
		cursor.moveToFirst();
		
		//this.close();
		return cursor;
	}
	
	public Cursor getContactListDisplayByFilter(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT INTERNAL_NUM AS _id, CONTACT_ID, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
//								+ DatabaseHandler.TABLE_FL_CONTACT
//								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true", "%" + strFilter + "%",
//								"%" + strFilter + "%", "%" + strFilter + "%",
//								"%" + strFilter + "%" });
		
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME,A.MOBILE, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE  ,IFNULL(A.FIRST_NAME,'') || A.LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ? WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) GROUP BY A.INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "false", "%" + strFilter + "%","%" + strFilter + "%", "%" + strFilter + "%","%" + strFilter + "%", "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getContactListDisplayByFilter(String strFilter,String strFilterIn) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX,A.EMAIL1, A.COMPANY_NAME,A.MOBILE, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ? WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (EMAIL1 NOT NULL OR EMAIL2 NOT NULL OR EMAIL3 NOT NULL) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND A.CONTACT_ID IS NOT NULL AND A.CONTACT_ID <> '' AND A.CONTACT_ID NOT IN ("+strFilterIn+") GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "%" + strFilter + "%","%" + strFilter + "%", "%" + strFilter + "%","%" + strFilter + "%","1", "true" });
		if(cursor.getCount() != 0){
			cursor.moveToFirst();
		}
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	
	public Cursor getSelectedContactListDisplayByFilter(String strFilter,String strFilterIn) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME,A.EMAIL1, A.PREFIX, A.COMPANY_NAME,A.MOBILE, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ? WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND A.CONTACT_ID IN ("+strFilterIn+") GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "%" + strFilter + "%","%" + strFilter + "%", "%" + strFilter + "%","%" + strFilter + "%", "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getSelectedContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getNewThisWeekContactListDisplayByFilter(String strFilter,
			String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT strftime('%Y',CREATED_TIMESTAMP) CreatedYear,strftime('%W',CREATED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, CONTACT_ID, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
//								+ DatabaseHandler.TABLE_FL_CONTACT
//								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true", "%" + strFilter + "%",
//								"%" + strFilter + "%", "%" + strFilter + "%",
//								"%" + strFilter + "%", weekCount, weekCount });
		
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.CREATED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE ,IFNULL(A.FIRST_NAME,'') || A.LAST_NAME AS 'FULLNAME' FROM "
								+ DatabaseHandler.TABLE_FL_CONTACT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
						new String[] { "false", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "1", "true", weekCount,
								weekCount });
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public Cursor getModifiedThisWeekContactListDisplayByFilter(
			String strFilter, String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT strftime('%Y',CREATED_TIMESTAMP) CreatedYear,strftime('%W',MODIFIED_TIMESTAMP)+0 WeekNumber,INTERNAL_NUM AS _id, CONTACT_ID, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, MOBILE,PHOTO, IS_UPDATE, ACTIVE FROM "
//								+ DatabaseHandler.TABLE_FL_CONTACT
//								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true", "%" + strFilter + "%",
//								"%" + strFilter + "%", "%" + strFilter + "%",
//								"%" + strFilter + "%", weekCount, weekCount });
		
		Cursor cursor = database
				.rawQuery(		"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.MODIFIED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE ,IFNULL(A.FIRST_NAME,'') || A.LAST_NAME AS 'FULLNAME' FROM "
						+ DatabaseHandler.TABLE_FL_CONTACT
						+ " AS A LEFT JOIN "
						+ DatabaseHandler.TABLE_FL_OPPORTUNITY
						+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY FULLNAME COLLATE NOCASE ASC",
				new String[] { "false", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "1", "true", weekCount,
						weekCount });
		


		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

	public int updatePhoto(ContactDetail oContact) {
		DeviceUtility.log(TAG, "updatePhoto(" + oContact.toString() + ")");
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("PHOTO", oContact.getPhoto());
		// values.put("MODIFIED_TIMESTAMP",
		// Utility.getDatabaseCurrentDateTime());
		iResult = database.update(DatabaseHandler.TABLE_FL_CONTACT, values,
				"INTERNAL_NUM = ?",
				new String[] { oContact.getInternalNumber() });
		//this.close();
		return iResult;
	}

	private ContactDetail setToObject(Cursor cursor) {
		ContactDetail contact = new ContactDetail();
		contact.setInternalNumber(cursor.getString(cursor
				.getColumnIndex("INTERNAL_NUM")));
		contact.setContactId(cursor.getString(cursor
				.getColumnIndex("CONTACT_ID")));
		contact.setFirstName(cursor.getString(cursor
				.getColumnIndex("FIRST_NAME")));
		contact.setLastName(cursor.getString(cursor.getColumnIndex("LAST_NAME")));
		contact.setPrefix(cursor.getString(cursor.getColumnIndex("PREFIX")));
		contact.setBirthday(cursor.getString(cursor.getColumnIndex("BIRTHDAY")));
		contact.setCompanyName(cursor.getString(cursor
				.getColumnIndex("COMPANY_NAME")));
		contact.setCompanyId(cursor.getString(cursor
				.getColumnIndex("COMPANY_ID")));
		contact.setDepartment(cursor.getString(cursor
				.getColumnIndex("DEPARTMENT")));
		contact.setJobTitle(cursor.getString(cursor.getColumnIndex("JOB_TITLE")));
		contact.setMobile(Utility.MobileFormat(cursor.getString(cursor.getColumnIndex("MOBILE"))));
		contact.setHomePhone(cursor.getString(cursor
				.getColumnIndex("HOME_PHONE")));
		contact.setWorkPhone(cursor.getString(cursor
				.getColumnIndex("WORK_PHONE")));
		contact.setOtherPhone(cursor.getString(cursor
				.getColumnIndex("OTHER_PHONE")));
		contact.setWorkFax(cursor.getString(cursor.getColumnIndex("WORK_FAX")));
		contact.setMailingCity(cursor.getString(cursor
				.getColumnIndex("MAILING_CITY")));
		contact.setMailingCountry(cursor.getString(cursor
				.getColumnIndex("MAILING_COUNTRY")));
		contact.setMailingState(cursor.getString(cursor
				.getColumnIndex("MAILING_STATE")));
		contact.setMailingStreet(cursor.getString(cursor
				.getColumnIndex("MAILING_STREET")));
		contact.setMailingZip(cursor.getString(cursor
				.getColumnIndex("MAILING_ZIP")));
		contact.setEmail1(cursor.getString(cursor.getColumnIndex("EMAIL1")));
		contact.setEmail2(cursor.getString(cursor.getColumnIndex("EMAIL2")));
		contact.setEmail3(cursor.getString(cursor.getColumnIndex("EMAIL3")));
		contact.setSkypeId(cursor.getString(cursor.getColumnIndex("SKYPE_ID")));
		contact.setAssistantName(cursor.getString(cursor
				.getColumnIndex("ASSISTANT_NAME")));
		contact.setAssistantPhone(cursor.getString(cursor
				.getColumnIndex("ASSISTANT_PHONE")));
		contact.setOwner(cursor.getString(cursor.getColumnIndex("OWNER")));
		contact.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		contact.setDescription(cursor.getString(cursor
				.getColumnIndex("DESCRIPTION")));
		contact.setGpsLat(cursor.getString(cursor.getColumnIndex("GPS_LAT")));
		contact.setGpsLong(cursor.getString(cursor.getColumnIndex("GPS_LONG")));
		contact.setUpdateGpsLocation(cursor.getString(cursor
				.getColumnIndex("UPDATE_GPS_LOCATION")));
		contact.setPhoto(cursor.getString(cursor.getColumnIndex("PHOTO")));
		contact.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
		contact.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
		contact.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
		contact.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
		contact.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
		contact.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
		contact.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
		contact.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
		contact.setUserStamp(cursor.getString(cursor
				.getColumnIndex("USER_STAMP")));
		contact.setCreatedTimestamp(cursor.getString(cursor
				.getColumnIndex("CREATED_TIMESTAMP")));
		contact.setModifiedTimestamp(cursor.getString(cursor
				.getColumnIndex("MODIFIED_TIMESTAMP")));
		contact.setIsUpdate(cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
		return contact;
	}

	public ArrayList<ContactDetail> getUnsyncContact() {
		this.openRead();
		ArrayList<ContactDetail> aContact = new ArrayList<ContactDetail>();
		DeviceUtility.log(TAG, "getUnsyncContact");
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONTACT, null,
				"IS_UPDATE = ?", new String[] { "false" }, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncContact: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aContact.add(setToObject(cursor));
			cursor.moveToNext();
		}
		//cursor.close();
		//this.close();
		return aContact;
	}

	/*
	 * public boolean isContactExist(String strContactServerId, String
	 * strInternalNum) { this.openRead(); boolean bExist = false;
	 * DeviceUtility.log(TAG,
	 * "isContactExist("+strContactServerId+", "+strInternalNum+")"); if
	 * (strContactServerId != null && strInternalNum != null) { Cursor cursor =
	 * database.query(DatabaseHandler.TABLE_FL_CONTACT, null,
	 * "CONTACT_ID = ? AND INTERNAL_NUM = ?", new String[] {strContactServerId,
	 * strInternalNum}, null, null, null); cursor.moveToFirst(); if
	 * (cursor.getCount() > 0) { DeviceUtility.log(TAG, "cursor.getCount(): " +
	 * cursor.getCount()); bExist = true; } //cursor.close(); } else if
	 * (strContactServerId != null) { Cursor cursor =
	 * database.query(DatabaseHandler.TABLE_FL_CONTACT, null, "CONTACT_ID = ?",
	 * new String[] {strContactServerId}, null, null, null);
	 * cursor.moveToFirst(); if (cursor.getCount() > 0) { DeviceUtility.log(TAG,
	 * "cursor.getCount(): " + cursor.getCount()); bExist = true; }
	 * //cursor.close(); } else if (strInternalNum != null) { Cursor cursor =
	 * database.query(DatabaseHandler.TABLE_FL_CONTACT, null,
	 * "INTERNAL_NUM = ?", new String[] {strInternalNum}, null, null, null);
	 * cursor.moveToFirst(); if (cursor.getCount() > 0) { DeviceUtility.log(TAG,
	 * "cursor.getCount(): " + cursor.getCount()); bExist = true; }
	 * //cursor.close(); } //this.close(); return bExist; }
	 */

	public int isContactExist(String strContactId) {
		this.openRead();
		DeviceUtility.log(TAG, "isContactExist");
		int iInternal = -1;
		if (strContactId.length() > 0) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONTACT,
					new String[] { "INTERNAL_NUM" }, "CONTACT_ID = ?",
					new String[] { strContactId }, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isContactExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternal = cursor
						.getInt(cursor.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			//cursor.close();
		}

		//this.close();
		return iInternal;
	}

	public void insertUpdate(ArrayList<ContactDetail> contact) {
		int iChange = 0;
		if (contact != null) {
			int iContactCount = contact.size();
			for (int i = 0; i < iContactCount; i++) {
				int iInternal = isContactExist(contact.get(i).getContactId());
				if (iInternal > 0) {
					ContactDetail contactObj = contact.get(i);
					contactObj.setInternalNumber(String.valueOf(iInternal));
					iChange = update(contact.get(i));
					if (iChange > 0) {
						// update the contact id for opportunity
						Opportunity opportunity = new Opportunity(dbHandler);
						opportunity.updateContactId(
								contactObj.getInternalNumber(),
								contactObj.getContactId());
						// update activity log.
						ActivityLog log = new ActivityLog(dbHandler);
						log.updateSystemId(contactObj.getInternalNumber(),
								contactObj.getContactId(),
								Constants.PERSON_TYPE_CONTACT);
					}
				} else {
					if (contact.get(i).getInternalNumber() != null
							&& contact.get(i).getInternalNumber().length() > 0) {
						iChange = update(contact.get(i));
						if (iChange > 0) {
							// update the contact id for opportunity
							Opportunity opportunity = new Opportunity(dbHandler);
							opportunity.updateContactId(contact.get(i)
									.getInternalNumber(), contact.get(i)
									.getContactId());
							// update activity log.
							ActivityLog log = new ActivityLog(dbHandler);
							log.updateSystemId(contact.get(i)
									.getInternalNumber(), contact.get(i)
									.getContactId(),
									Constants.PERSON_TYPE_CONTACT);
						}
					} else {
						insert(contact.get(i));
					}
				}
			}
		}
	}

	public String getContactIdByInternalNum(String strInternal) {
		this.openRead();
		String strResult = null;
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONTACT,
				new String[] { "CONTACT_ID" }, "INTERNAL_NUM = ?",
				new String[] { strInternal }, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			strResult = cursor.getString(cursor.getColumnIndex("CONTACT_ID"));
		}
		//cursor.close();
		//this.close();
		return strResult;
	}
}
