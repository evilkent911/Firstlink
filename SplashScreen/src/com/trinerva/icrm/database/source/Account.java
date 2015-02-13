package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.AccountDetail;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.database.Cursor;

public class Account extends BaseSource {
	private static String TAG = "Account";

	public Account(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

//	public int delete(String Id) {
//		this.openWrite();
//		int iResult = 0;
//		ContentValues values = new ContentValues();
//		values.put("ACTIVE", "1");
//		values.put("IS_UPDATE", "false");
//		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
//		iResult = database.update(DatabaseHandler.TABLE_FL_ACCOUNT, values,
//				"INTERNAL_NUM = ?", new String[] { Id });
//		this.close();
//		return iResult;
//	}
	
	public Cursor getAccountListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, ACCOUNT_ID, ACCOUNT_NAME, ACTIVE FROM " + DatabaseHandler.TABLE_FL_ACCOUNT + " WHERE (ACTIVE <> ?) ORDER BY INTERNAL_NUM ASC", new String[] {"1"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getAccountListFilterDisplay(String filter) {
		this.openRead();
		DeviceUtility.log(TAG, "getTaskListDisplay");
		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, ACCOUNT_ID, ACCOUNT_NAME, ACTIVE FROM " + DatabaseHandler.TABLE_FL_ACCOUNT + " WHERE (ACTIVE <> ?) AND ACCOUNT_NAME LIKE ? ORDER BY INTERNAL_NUM ASC", new String[] {"1","%"+filter+"%"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getTaskListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public int update(AccountDetail oUpdate) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());

		values.put("ACTIVE", oUpdate.getActive());

		// if (oUpdate.getAccountId()!= null && oUpdate.getAccountId().length()
		// > 0) {
		values.put("ACCOUNT_ID", oUpdate.getAccountId());
		// }

		// if (oUpdate.getFirstName()!= null && oUpdate.getFirstName().length()
		// > 0) {
		values.put("ACCOUNT_NAME", oUpdate.getAccountName());
		// }

		// if (oUpdate.getLastName() != null && oUpdate.getLastName().length() >
		// 0) {
		values.put("OWNER", oUpdate.getOwner());
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
		iResult = database.update(DatabaseHandler.TABLE_FL_ACCOUNT, values,
				"INTERNAL_NUM = ?",
				new String[] { oUpdate.getInternalNum() });
		this.close();
		return iResult;
	}

	public long insert(AccountDetail dInsert) {
		this.openWrite();
		long lResult = -1;
		DeviceUtility.log(TAG, "Insert contact list");
		ContentValues values = new ContentValues();
		DeviceUtility.log(TAG, dInsert.toString());
		// by default should be 1
		
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

		if (dInsert.getActive() != null && dInsert.getActive().length() > 0) {
			values.put("ACTIVE", dInsert.getActive());
		}
		
		if (dInsert.getAccountId() != null
				&& dInsert.getAccountId().length() > 0) {
			values.put("ACCOUNT_ID", dInsert.getAccountId());
		}
		
		if (dInsert.getAccountName() != null && dInsert.getAccountName().length() > 0) {
			values.put("ACCOUNT_NAME", dInsert.getAccountName());
		}

		if (dInsert.getOwner() != null && dInsert.getOwner().length() > 0) {
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

		if (dInsert.getUserStamp() != null
				&& dInsert.getUserStamp().length() > 0) {
			values.put("USER_STAMP", dInsert.getUserStamp());
		}

		lResult = database.insert(DatabaseHandler.TABLE_FL_ACCOUNT, null,
				values);

		this.close();
		return lResult;
	}

	public AccountDetail getAccountDetailById(String ID) {
		this.openRead();
		AccountDetail object = null;
		try {
			DeviceUtility.log(TAG, "getAccountDetailById(" + ID + ")");
			 object = new AccountDetail();
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACCOUNT,
					null, "INTERNAL_NUM = ?", new String[] { ID }, null, null,
					null);
			cursor.moveToFirst();
			object = setToObject(cursor);

			DeviceUtility.log(TAG, object.toString());
			cursor.close();
			this.close();

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
						"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.CREATED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "1", "true", weekCount,
								weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public Cursor getModifiedThisWeekContactListDisplay(String weekCount) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.MODIFIED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "1", "true", weekCount,
								weekCount });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public Cursor getContactListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public Cursor getNameToName(String str) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND _id = ? GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "1", "true" ,str});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getSyncNameToName(String str) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplay");
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
//								+ DatabaseHandler.TABLE_FL_ACCOUNT
//								+ " AS A LEFT JOIN "
//								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
//								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND C = ? GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
//						new String[] { "false", "1", "true" ,str});
//		cursor.moveToFirst();
//		DeviceUtility.log(TAG, "getContactListDisplay: " + cursor.getCount());
		
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACCOUNT, null,
				"CONTACT_ID = ?", new String[] { str }, null, null, null);
		cursor.moveToFirst();
		
		this.close();
		return cursor;
	}
	
	public Cursor getContactListDisplayByFilter(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
//		Cursor cursor = database
//				.rawQuery(
//						"SELECT INTERNAL_NUM AS _id, CONTACT_ID, INTERNAL_NUM, FIRST_NAME, LAST_NAME, PREFIX, COMPANY_NAME, PHOTO, IS_UPDATE, ACTIVE FROM "
//								+ DatabaseHandler.TABLE_FL_ACCOUNT
//								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true", "%" + strFilter + "%",
//								"%" + strFilter + "%", "%" + strFilter + "%",
//								"%" + strFilter + "%" });
		
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME,A.MOBILE, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ? WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "%" + strFilter + "%","%" + strFilter + "%", "%" + strFilter + "%","%" + strFilter + "%", "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getContactListDisplayByFilter(String strFilter,String strFilterIn) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX,A.EMAIL1, A.COMPANY_NAME,A.MOBILE, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ? WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND A.CONTACT_ID IS NOT NULL AND A.CONTACT_ID <> '' AND A.CONTACT_ID NOT IN ("+strFilterIn+") GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "%" + strFilter + "%","%" + strFilter + "%", "%" + strFilter + "%","%" + strFilter + "%", "1", "true" });
		if(cursor.getCount() != 0){
			cursor.moveToFirst();
		}
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getSelectedContactListDisplayByFilter(String strFilter,String strFilterIn) {
		this.openRead();
		DeviceUtility.log(TAG, "getContactListDisplayByFilter(" + strFilter
				+ ")");
		Cursor cursor = database
				.rawQuery(
						"SELECT A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME,A.EMAIL1, A.PREFIX, A.COMPANY_NAME,A.MOBILE, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ? WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND A.CONTACT_ID IN ("+strFilterIn+") GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "%" + strFilter + "%","%" + strFilter + "%", "%" + strFilter + "%","%" + strFilter + "%", "1", "true" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getSelectedContactListDisplayByFilter: " + cursor.getCount());
		this.close();
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
//								+ DatabaseHandler.TABLE_FL_ACCOUNT
//								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true", "%" + strFilter + "%",
//								"%" + strFilter + "%", "%" + strFilter + "%",
//								"%" + strFilter + "%", weekCount, weekCount });
		
		Cursor cursor = database
				.rawQuery(
						"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.CREATED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
								+ DatabaseHandler.TABLE_FL_ACCOUNT
								+ " AS A LEFT JOIN "
								+ DatabaseHandler.TABLE_FL_OPPORTUNITY
								+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
						new String[] { "false", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "1", "true", weekCount,
								weekCount });
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		this.close();
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
//								+ DatabaseHandler.TABLE_FL_ACCOUNT
//								+ " WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) ORDER BY INTERNAL_NUM ASC",
//						new String[] { "1", "true", "%" + strFilter + "%",
//								"%" + strFilter + "%", "%" + strFilter + "%",
//								"%" + strFilter + "%", weekCount, weekCount });
		
		Cursor cursor = database
				.rawQuery(		"SELECT strftime('%Y',A.CREATED_TIMESTAMP) CreatedYear,strftime('%W',A.MODIFIED_TIMESTAMP)+0 WeekNumber,A.INTERNAL_NUM AS _id, A.CONTACT_ID, A.INTERNAL_NUM, A.FIRST_NAME, A.LAST_NAME, A.PREFIX, A.COMPANY_NAME, A.PHOTO, A.IS_UPDATE, A.ACTIVE, B.IS_UPDATE AS OPP_UPDATE FROM "
						+ DatabaseHandler.TABLE_FL_ACCOUNT
						+ " AS A LEFT JOIN "
						+ DatabaseHandler.TABLE_FL_OPPORTUNITY
						+ " AS B ON (A.INTERNAL_NUM = B.CONTACT_INTERNAL_NUM AND B.IS_UPDATE = ?) WHERE (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ? OR MOBILE LIKE ?) AND (A.ACTIVE <> ? OR A.IS_UPDATE <> ?) AND WeekNumber=strftime('%W',?)+0 AND CreatedYear=strftime('%Y',?) GROUP BY A.INTERNAL_NUM ORDER BY A.INTERNAL_NUM ASC",
				new String[] { "false", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "%" + strFilter + "%", "1", "true", weekCount,
						weekCount });
		


		cursor.moveToFirst();
		DeviceUtility.log(TAG,
				"getContactListDisplayByFilter: " + cursor.getCount());
		this.close();
		return cursor;
	}

	private AccountDetail setToObject(Cursor cursor) {
		AccountDetail contact = new AccountDetail();
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
		return contact;
	}

	public ArrayList<AccountDetail> getUnsyncContact() {
		this.openRead();
		ArrayList<AccountDetail> aContact = new ArrayList<AccountDetail>();
		DeviceUtility.log(TAG, "getUnsyncContact");
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACCOUNT, null,
				"IS_UPDATE = ?", new String[] { "false" }, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncContact: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aContact.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aContact;
	}

	/*
	 * public boolean isContactExist(String strContactServerId, String
	 * strInternalNum) { this.openRead(); boolean bExist = false;
	 * DeviceUtility.log(TAG,
	 * "isContactExist("+strContactServerId+", "+strInternalNum+")"); if
	 * (strContactServerId != null && strInternalNum != null) { Cursor cursor =
	 * database.query(DatabaseHandler.TABLE_FL_ACCOUNT, null,
	 * "CONTACT_ID = ? AND INTERNAL_NUM = ?", new String[] {strContactServerId,
	 * strInternalNum}, null, null, null); cursor.moveToFirst(); if
	 * (cursor.getCount() > 0) { DeviceUtility.log(TAG, "cursor.getCount(): " +
	 * cursor.getCount()); bExist = true; } cursor.close(); } else if
	 * (strContactServerId != null) { Cursor cursor =
	 * database.query(DatabaseHandler.TABLE_FL_ACCOUNT, null, "CONTACT_ID = ?",
	 * new String[] {strContactServerId}, null, null, null);
	 * cursor.moveToFirst(); if (cursor.getCount() > 0) { DeviceUtility.log(TAG,
	 * "cursor.getCount(): " + cursor.getCount()); bExist = true; }
	 * cursor.close(); } else if (strInternalNum != null) { Cursor cursor =
	 * database.query(DatabaseHandler.TABLE_FL_ACCOUNT, null,
	 * "INTERNAL_NUM = ?", new String[] {strInternalNum}, null, null, null);
	 * cursor.moveToFirst(); if (cursor.getCount() > 0) { DeviceUtility.log(TAG,
	 * "cursor.getCount(): " + cursor.getCount()); bExist = true; }
	 * cursor.close(); } this.close(); return bExist; }
	 */

	public int isAccountExist(String strAccountId) {
		this.openRead();
		DeviceUtility.log(TAG, "isLeadExist");
		int iInternal = -1;
		if (strAccountId.length() > 0) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACCOUNT,
					new String[] { "INTERNAL_NUM" }, "ACCOUNT_ID = ?",
					new String[] { strAccountId }, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isLeadExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternal = cursor
						.getInt(cursor.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			cursor.close();
		}

		this.close();
		return iInternal;
	}

	public void insertUpdate(ArrayList<AccountDetail> account) {
		int iChange = 0;
		if (account != null) {
			int iAccountCount = account.size();
			for (int i = 0; i < iAccountCount; i++) {
				int iInternal = isAccountExist(account.get(i).getAccountId());
				if (iInternal > 0) {
					AccountDetail leadObj = account.get(i);
					leadObj.setInternalNum(String.valueOf(iInternal));
					iChange = update(leadObj);
					if (iChange > 0) {
						// update activity log.
						ActivityLog log = new ActivityLog(dbHandler);
						log.updateSystemId(leadObj.getInternalNum(),
								leadObj.getAccountId(), Constants.PERSON_TYPE_LEAD);
					}
				} else {
					if (account.get(i).getInternalNum() != null
							&& account.get(i).getInternalNum().length() > 0) {
						iChange = update(account.get(i));
						if (iChange > 0) {
							// update activity log.
							ActivityLog log = new ActivityLog(dbHandler);
							log.updateSystemId(account.get(i).getInternalNum(),
									account.get(i).getAccountId(),
									Constants.PERSON_TYPE_LEAD);
						}
					} else {
						insert(account.get(i));
					}
				}
			}
		}
	}

	public String getAccountIdByInternalNum(String strInternal) {
		this.openRead();
		String strResult = null;
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_ACCOUNT,
				new String[] { "CONTACT_ID" }, "INTERNAL_NUM = ?",
				new String[] { strInternal }, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			strResult = cursor.getString(cursor.getColumnIndex("CONTACT_ID"));
		}
		cursor.close();
		this.close();
		return strResult;
	}
}
