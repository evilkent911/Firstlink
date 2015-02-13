package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.BroadcastDetail;
import com.trinerva.icrm.object.CalendarDetail;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Broadcast extends BaseSource {
	private static String TAG = "Broadcast";

	public Broadcast(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public long insert(Context context, BroadcastDetail oBroadcastDetail) {
		this.openWrite();
		long lResult = -1;
		DeviceUtility.log(TAG, oBroadcastDetail.toString());

		ContentValues values = new ContentValues();
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());

		if (oBroadcastDetail.getAnnouncementId() != null) {
			values.put("BROADCAST_ID", oBroadcastDetail.getAnnouncementId());
		}

		if (oBroadcastDetail.getSubject() != null) {
			values.put("SUBJECT", oBroadcastDetail.getSubject());
		}

		if (oBroadcastDetail.getReleasedBy() != null) {
			values.put("RELEASED_BY", oBroadcastDetail.getReleasedBy());
		}

		if (oBroadcastDetail.getReleasedDate() != null) {
			values.put("RELEASED_DATE", oBroadcastDetail.getReleasedDate());
		}

		if (oBroadcastDetail.getCreatedTimestamp() != null) {
			values.put("CREATED_TIMESTAMP", oBroadcastDetail.getCreatedTimestamp());
		}

		if (oBroadcastDetail.getBroadcastType() != null) {
			values.put("BROADCAST_TYPE", oBroadcastDetail.getBroadcastType());
		}

		if (oBroadcastDetail.getBroadcastContent() != null) {
			values.put("BROADCAST_CONTENT", oBroadcastDetail.getBroadcastContent());
		}

		if (oBroadcastDetail.getUserDef1() != null) {
			values.put("USER_DEF1", oBroadcastDetail.getUserDef1());
		}

		if (oBroadcastDetail.getUserDef2() != null) {
			values.put("USER_DEF2", oBroadcastDetail.getUserDef2());
		}

		if (oBroadcastDetail.getUserDef3() != null) {
			values.put("USER_DEF3", oBroadcastDetail.getUserDef3());
		}

		if (oBroadcastDetail.getUserDef4() != null) {
			values.put("USER_DEF4", oBroadcastDetail.getUserDef4());
		}

		if (oBroadcastDetail.getUserDef5() != null) {
			values.put("USER_DEF5", oBroadcastDetail.getUserDef5());
		}

		if (oBroadcastDetail.getUserDef6() != null) {
			values.put("USER_DEF6", oBroadcastDetail.getUserDef6());
		}

		if (oBroadcastDetail.getUserDef7() != null) {
			values.put("USER_DEF7", oBroadcastDetail.getUserDef7());
		}

		if (oBroadcastDetail.getUserDef8() != null) {
			values.put("USER_DEF8", oBroadcastDetail.getUserDef8());
		}

		if (oBroadcastDetail.getUserStamp() != null) {
			values.put("USER_STAMP", oBroadcastDetail.getUserStamp());
		}
		lResult = database.insert(DatabaseHandler.TABLE_FL_BROADCAST, null, values);

		this.close();
		return lResult;
	}

	public int update(Context context, BroadcastDetail oUpdatel) {
		this.openWrite();
		int iResult = 0;
		DeviceUtility.log(TAG, oUpdatel.toString());

		ContentValues values = new ContentValues();
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());

		values.put("BROADCAST_ID", oUpdatel.getAnnouncementId());

		values.put("SUBJECT", oUpdatel.getSubject());

		values.put("RELEASED_BY", oUpdatel.getReleasedBy());

		values.put("RELEASED_DATE", oUpdatel.getReleasedDate());

		values.put("CREATED_TIMESTAMP", oUpdatel.getCreatedTimestamp());

		values.put("BROADCAST_TYPE", oUpdatel.getBroadcastType());

		values.put("BROADCAST_CONTENT", oUpdatel.getBroadcastContent());

		values.put("USER_DEF1", oUpdatel.getUserDef1());

		values.put("USER_DEF2", oUpdatel.getUserDef2());

		values.put("USER_DEF3", oUpdatel.getUserDef3());

		values.put("USER_DEF4", oUpdatel.getUserDef4());

		values.put("USER_DEF5", oUpdatel.getUserDef5());

		values.put("USER_DEF6", oUpdatel.getUserDef6());

		values.put("USER_DEF7", oUpdatel.getUserDef7());

		values.put("USER_DEF8", oUpdatel.getUserDef8());

		values.put("USER_STAMP", oUpdatel.getUserStamp());
		iResult = database.update(DatabaseHandler.TABLE_FL_BROADCAST, values, "INTERNAL_NUM = ?", new String[] { oUpdatel.getInternalNum() });
		this.close();
		return iResult;
	}

	public int isBroadcastExist(String strBroadcastId) {
		this.openRead();
		DeviceUtility.log(TAG, "isBroadcastExist");
		int iInternal = -1;
		if (strBroadcastId.length() > 0) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_BROADCAST, new String[] { "INTERNAL_NUM" }, "BROADCAST_ID = ?", new String[] { strBroadcastId }, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isBroadcastExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternal = cursor.getInt(cursor.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			cursor.close();
		}

		// this.close();
		return iInternal;
	}

	public void insertUpdate(Context context, ArrayList<BroadcastDetail> aBroadcastDetail) {
		if (aBroadcastDetail != null) {
			int iBroadcastCount = aBroadcastDetail.size();
			for (int i = 0; i < iBroadcastCount; i++) {
				int iInternal = isBroadcastExist(aBroadcastDetail.get(i).getAnnouncementId());
				if (iInternal > 0) {
					BroadcastDetail calen = aBroadcastDetail.get(i);
					calen.setInternalNum(String.valueOf(iInternal));
					update(context, calen);
				} else {
					if (aBroadcastDetail.get(i).getInternalNum() != null && aBroadcastDetail.get(i).getInternalNum().length() > 0) {
						update(context, aBroadcastDetail.get(i));
					} else {
						insert(context, aBroadcastDetail.get(i));
					}
				}
			}
		}
	}

	public Cursor getBroadcastDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getBroadcastDisplay()");
		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, RELEASED_BY, DATE(RELEASED_DATE) AS RELEASED_DATE_ONLY, TIME(RELEASED_DATE) AS RELEASED_TIME_ONLY, BROADCAST_TYPE FROM " + DatabaseHandler.TABLE_FL_BROADCAST + " WHERE USER_DEF7 = ? AND USER_DEF8 = ? ORDER BY DATE(RELEASED_DATE) DESC", new String[] { "1", "0" });
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getBroadcastDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public int getNewBroadcastCount() {
		int iCount = 0;
		this.openRead();
		Cursor cursor = database.rawQuery("SELECT COUNT(1) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_BROADCAST + " WHERE USER_DEF8 IS NULL", null);
		cursor.moveToFirst();
		iCount = cursor.getInt(cursor.getColumnIndex("TOTAL"));
		cursor.close();
		this.close();
		return iCount;
	}

	// public int updateReadFlag(String strInternalNum) {
	// DeviceUtility.log(TAG, "updateReadFlag("+strInternalNum+")");
	// this.openWrite();
	// int iUpdate = 0;
	// ContentValues values = new ContentValues();
	// values.put("USER_DEF8", "1");
	// iUpdate = database.update(DatabaseHandler.TABLE_FL_BROADCAST, values,
	// "INTERNAL_NUM = ?", new String[] {strInternalNum});
	// this.close();
	// DeviceUtility.log(TAG, "updateReadFlag("+strInternalNum+"): " + iUpdate);
	// return iUpdate;
	// }

	public BroadcastDetail getBroadcastDetail(String id) {
		DeviceUtility.log(TAG, "BroadcastDetail(" + id + ")");
		this.openRead();
		BroadcastDetail object = new BroadcastDetail();
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_BROADCAST, null, "INTERNAL_NUM = ?", new String[] { id }, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			// this.updateReadFlag(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			object.setInternalNum(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			object.setSubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
			object.setReleasedBy(cursor.getString(cursor.getColumnIndex("RELEASED_BY")));
			object.setReleasedDate(cursor.getString(cursor.getColumnIndex("RELEASED_DATE")));
			object.setBroadcastType(cursor.getString(cursor.getColumnIndex("BROADCAST_TYPE")));
			object.setBroadcastContent(cursor.getString(cursor.getColumnIndex("BROADCAST_CONTENT")));
			object.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
			object.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
			object.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
			object.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
			object.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
			object.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
			object.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
			object.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
			object.setUserStamp(cursor.getString(cursor.getColumnIndex("USER_STAMP")));
			object.setCreatedTimestamp(cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")));
			object.setModifiedTimestamp(cursor.getString(cursor.getColumnIndex("MODIFIED_TIMESTAMP")));
		}
		cursor.close();
		this.close();
		return object;
	}
}
