package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.OpportunityDetail;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.database.Cursor;

public class Opportunity extends BaseSource {
	private static String TAG = "Opportunity";

	public Opportunity(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public int update(OpportunityDetail dInsert) {
		this.openWrite();
		int iResult = 0;
		DeviceUtility.log(TAG, "update opportunity to list: " + dInsert.toString());
		ContentValues values = new ContentValues();
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		
		if (dInsert.getIsUpdate()!= null && dInsert.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dInsert.getIsUpdate());
		}
		
		/*if (dInsert.getModifiedTimestamp()!= null && dInsert.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", dInsert.getModifiedTimestamp());
		}*/
		
		values.put("ACTIVE", dInsert.getActive());
		values.put("CONTACT_INTERNAL_NUM", dInsert.getContactInternalNum());
		values.put("CONTACT_ID", dInsert.getContactId());
		values.put("OPP_ID", dInsert.getOppId());
		values.put("OPP_NAME", dInsert.getOppName());
		values.put("OPP_DESC", dInsert.getOppDesc());
		values.put("OPP_AMOUNT", dInsert.getOppAmount());
		values.put("OPP_DATE", dInsert.getOppDate());
		values.put("OPP_STAGE", dInsert.getOppStage());
		
		if (dInsert.getUserDef1()!= null && dInsert.getUserDef1().length() > 0) {
			values.put("USER_DEF1", dInsert.getUserDef1());
		}
		
		if (dInsert.getUserDef2()!= null && dInsert.getUserDef2().length() > 0) {
			values.put("USER_DEF2", dInsert.getUserDef2());
		}
		
		if (dInsert.getUserDef3()!= null && dInsert.getUserDef3().length() > 0) {
			values.put("USER_DEF3", dInsert.getUserDef3());
		}
		
		if (dInsert.getUserDef4()!= null && dInsert.getUserDef4().length() > 0) {
			values.put("USER_DEF4", dInsert.getUserDef4());
		}
		
		if (dInsert.getUserDef5()!= null && dInsert.getUserDef5().length() > 0) {
			values.put("USER_DEF5", dInsert.getUserDef5());
		}
		
		if (dInsert.getUserDef6()!= null && dInsert.getUserDef6().length() > 0) {
			values.put("USER_DEF6", dInsert.getUserDef6());
		}
		
		if (dInsert.getUserDef7()!= null && dInsert.getUserDef7().length() > 0) {
			values.put("USER_DEF7", dInsert.getUserDef7());
		}
		
		if (dInsert.getUserDef8()!= null && dInsert.getUserDef8().length() > 0) {
			values.put("USER_DEF8", dInsert.getUserDef8());
		}
		
		values.put("USER_STAMP", dInsert.getUserStamp());

		iResult = database.update(DatabaseHandler.TABLE_FL_OPPORTUNITY, values, "INTERNAL_NUM = ?", new String[] {dInsert.getInternalNum()});
		/*if (iResult > 0 && dInsert.getContactId() == null) {
			//do check on the contact and update on opportunity table.
			checkContactId(dInsert.getContactInternalNum(), dInsert.getInternalNum());
		}*/
		this.close();
		return iResult;
	}

	public long insert(OpportunityDetail dInsert) {
		this.openWrite();
		long lResult = -1;
		DeviceUtility.log(TAG, "Insert opportunity to list: " + dInsert.toString());
		ContentValues values = new ContentValues();

		values.put("ACTIVE", "0");
		values.put("IS_UPDATE", "false");
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		
		if (dInsert.getModifiedTimestamp()!= null && dInsert.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", dInsert.getModifiedTimestamp());
		}
		if (dInsert.getCreatedTimestamp()!= null && dInsert.getCreatedTimestamp().length() > 0) {
			values.put("CREATED_TIMESTAMP", dInsert.getCreatedTimestamp());
		}
		
		if (dInsert.getIsUpdate()!= null && dInsert.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dInsert.getIsUpdate());
		}
		
		if (dInsert.getActive()!= null && dInsert.getActive().length() > 0) {
			values.put("ACTIVE", dInsert.getActive());
		}
		
		if (dInsert.getContactInternalNum()!= null && dInsert.getContactInternalNum().length() > 0) {
			values.put("CONTACT_INTERNAL_NUM", dInsert.getContactInternalNum());
		}
		
		if (dInsert.getContactId()!= null && dInsert.getContactId().length() > 0) {
			values.put("CONTACT_ID", dInsert.getContactId());
		}
		
		if (dInsert.getOppId()!= null && dInsert.getOppId().length() > 0) {
			values.put("OPP_ID", dInsert.getOppId());
		}
		
		if (dInsert.getOppName()!= null && dInsert.getOppName().length() > 0) {
			values.put("OPP_NAME", dInsert.getOppName());
		}
		
		if (dInsert.getOppDesc()!= null && dInsert.getOppDesc().length() > 0) {
			values.put("OPP_DESC", dInsert.getOppDesc());
		}
		
		if (dInsert.getOppAmount()!= null && dInsert.getOppAmount().length() > 0) {
			values.put("OPP_AMOUNT", dInsert.getOppAmount());
		}
		
		if (dInsert.getOppDate()!= null && dInsert.getOppDate().length() > 0) {
			values.put("OPP_DATE", dInsert.getOppDate());
		}
		
		if (dInsert.getOppStage()!= null && dInsert.getOppStage().length() > 0) {
			values.put("OPP_STAGE", dInsert.getOppStage());
		}
		
		if (dInsert.getUserDef1()!= null && dInsert.getUserDef1().length() > 0) {
			values.put("USER_DEF1", dInsert.getUserDef1());
		}
		
		if (dInsert.getUserDef2()!= null && dInsert.getUserDef2().length() > 0) {
			values.put("USER_DEF2", dInsert.getUserDef2());
		}
		
		if (dInsert.getUserDef3()!= null && dInsert.getUserDef3().length() > 0) {
			values.put("USER_DEF3", dInsert.getUserDef3());
		}
		
		if (dInsert.getUserDef4()!= null && dInsert.getUserDef4().length() > 0) {
			values.put("USER_DEF4", dInsert.getUserDef4());
		}
		
		if (dInsert.getUserDef5()!= null && dInsert.getUserDef5().length() > 0) {
			values.put("USER_DEF5", dInsert.getUserDef5());
		}
		
		if (dInsert.getUserDef6()!= null && dInsert.getUserDef6().length() > 0) {
			values.put("USER_DEF6", dInsert.getUserDef6());
		}
		
		if (dInsert.getUserDef7()!= null && dInsert.getUserDef7().length() > 0) {
			values.put("USER_DEF7", dInsert.getUserDef7());
		}
		
		if (dInsert.getUserDef8()!= null && dInsert.getUserDef8().length() > 0) {
			values.put("USER_DEF8", dInsert.getUserDef8());
		}
		
		if (dInsert.getUserStamp()!= null && dInsert.getUserStamp().length() > 0) {
			values.put("USER_STAMP", dInsert.getUserStamp());
		}

		lResult = database.insert(DatabaseHandler.TABLE_FL_OPPORTUNITY, null, values);
		/*if (lResult > 0 && dInsert.getContactId() == null) {
			//do check on the contact and update on opportunity table.
			checkContactId(dInsert.getContactInternalNum(), String.valueOf(lResult));
		}*/
		this.close();
		return lResult;
	}

	public Cursor getOpportunityDisplay(String strId) {
		this.openRead();
		DeviceUtility.log(TAG, "getOpportunityDisplay("+strId+")");
		Cursor cursor = database.rawQuery("SELECT a.INTERNAL_NUM AS _id, a.INTERNAL_NUM, a.OPP_NAME, a.OPP_AMOUNT, a.OPP_DATE, a.OPP_STAGE, a.ACTIVE, a.IS_UPDATE "+
				" FROM " + DatabaseHandler.TABLE_FL_OPPORTUNITY + " a " +
				" INNER JOIN " + DatabaseHandler.TABLE_FL_MASTER + " b ON b.MASTER_VALUE = a.OPP_STAGE AND b.MASTER_TYPE = 'OpportunityStage' " +
				" WHERE (b.User_Def3 > 0 or a.OPP_DATE > strftime('%d-%m-%Y',date('now'))) " +
				" and b.User_Def3 <> 100 " +
				" and (a.Active<> ? OR a.IS_Update<> ?) and a.CONTACT_INTERNAL_NUM = ?  ORDER BY a.INTERNAL_NUM ASC", new String[] {"1","true", strId});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getOpportunityDisplay: " + cursor.getCount());
		this.close();
		return cursor;
	}

	public OpportunityDetail getOpportunityById(String strId) {
		this.openRead();
		OpportunityDetail result = new OpportunityDetail();
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_OPPORTUNITY, null, "INTERNAL_NUM=?", new String[] {strId}, null, null, null);
		cursor.moveToFirst();
		result = setToObject(cursor);
		cursor.close();
		this.close();
		return result;
	}

	private OpportunityDetail setToObject(Cursor cursor) {
		OpportunityDetail opp = new OpportunityDetail();
		opp.setInternalNum(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
		opp.setContactInternalNum(cursor.getString(cursor.getColumnIndex("CONTACT_INTERNAL_NUM")));
		opp.setContactId(cursor.getString(cursor.getColumnIndex("CONTACT_ID")));
		opp.setOppId(cursor.getString(cursor.getColumnIndex("OPP_ID")));
		opp.setOppName(cursor.getString(cursor.getColumnIndex("OPP_NAME")));
		opp.setOppDesc(cursor.getString(cursor.getColumnIndex("OPP_DESC")));
		opp.setOppAmount(cursor.getString(cursor.getColumnIndex("OPP_AMOUNT")));
		opp.setOppDate(cursor.getString(cursor.getColumnIndex("OPP_DATE")));
		opp.setOppStage(cursor.getString(cursor.getColumnIndex("OPP_STAGE")));
		opp.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		opp.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
		opp.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
		opp.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
		opp.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
		opp.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
		opp.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
		opp.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
		opp.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
		opp.setUserStamp(cursor.getString(cursor.getColumnIndex("USER_STAMP")));
		opp.setCreatedTimestamp(cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")));
		opp.setModifiedTimestamp(cursor.getString(cursor.getColumnIndex("MODIFIED_TIMESTAMP")));
		opp.setIsUpdate(cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
		return opp;
	}

	public int delete(String strOppId) {
		this.openWrite();
		int iSuccess = 0;
		ContentValues values = new ContentValues();
		values.put("ACTIVE", "1");
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		iSuccess = database.update(DatabaseHandler.TABLE_FL_OPPORTUNITY, values, "INTERNAL_NUM = ?", new String[] {strOppId});
		this.close();
		return iSuccess;
	}

	public int updateContactId(String strInternalNumber, String strContactId) {
		DeviceUtility.log(TAG, "updateContactId("+strInternalNumber+", "+strContactId+")");
		this.openWrite();
		int iSuccess = 0;
		ContentValues values = new ContentValues();
		values.put("CONTACT_ID", strContactId);
		iSuccess = database.update(DatabaseHandler.TABLE_FL_OPPORTUNITY, values, "CONTACT_INTERNAL_NUM = ?", new String[] {strInternalNumber});
		this.close();
		return iSuccess;
	}
	/*
	public void checkContactId(String strContactInternalNum, String strOppInternalNum) {
		DeviceUtility.log(TAG, "checkContactId("+strContactInternalNum+", "+strOppInternalNum+")");
		Contact contact = new Contact(dbHandler);
		String strContactID = contact.getContactIdByInternalNum(strContactInternalNum);
		int iSuccess = 0;
		if (strContactID != null) {
			iSuccess = updateContactId(strContactInternalNum, strContactID);
		}
		DeviceUtility.log(TAG, "updateContactId: " + iSuccess);
	}*/
	
	
	public ArrayList<OpportunityDetail> getUnsyncOpportunity() {
		this.openRead();
		ArrayList<OpportunityDetail> aOpportunity = new ArrayList<OpportunityDetail>();
		DeviceUtility.log(TAG, "getUnsyncOpportunity");
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_OPPORTUNITY, null, "IS_UPDATE = ?", new String[] {"false"}, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncOpportunity: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aOpportunity.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aOpportunity;
	}
	/*
	public int isOpportunityExist(String strOpportunityServerId, String strInternalNum) {
		this.openRead();
		int iExist = 0;
		DeviceUtility.log(TAG, "isOpportunityExist("+strOpportunityServerId+", "+strInternalNum+")");
		if (strOpportunityServerId != null  && strInternalNum != null) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_OPPORTUNITY,
					null, "OPP_ID = ? AND INTERNAL_NUM = ?", new String[] {strOpportunityServerId, strInternalNum}, null, null, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				DeviceUtility.log(TAG, "cursor.getCount(): " + cursor.getCount());
				iExist = 1;
			}
			cursor.close();
		} else if (strOpportunityServerId != null) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_OPPORTUNITY,
					null, "OPP_ID = ?", new String[] {strOpportunityServerId}, null, null, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				DeviceUtility.log(TAG, "cursor.getCount(): " + cursor.getCount());
				iExist = 2;
			}
			cursor.close();
		} else if (strInternalNum != null) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_OPPORTUNITY,
					null, "INTERNAL_NUM = ?", new String[] {strInternalNum}, null, null, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				DeviceUtility.log(TAG, "cursor.getCount(): " + cursor.getCount());
				iExist = 3;
			}
			cursor.close();
		}
		this.close();
		return iExist;
	}*/
	
	public int isOpportunityExist(String strOppId) {
		this.openRead();
		DeviceUtility.log(TAG, "isOpportunityExist");
		int iInternal = -1;
		if (strOppId.length() > 0) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_OPPORTUNITY, new String[] {"INTERNAL_NUM"}, "OPP_ID = ?", new String[] {strOppId}, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isOpportunityExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternal = cursor.getInt(cursor.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			cursor.close();
		}
		
		this.close();
		return iInternal;
	}
	
	public void insertUpdate(ArrayList<OpportunityDetail> opportunity) {
		if (opportunity != null) {
			int iOppCount = opportunity.size();
			for (int i = 0; i < iOppCount; i++) {
				int iInternal = isOpportunityExist(opportunity.get(i).getOppId());
				if (iInternal > 0) {
					OpportunityDetail opp = opportunity.get(i);
					opp.setInternalNum(String.valueOf(iInternal));
					update(opp);
				} else {
					if (opportunity.get(i).getInternalNum() != null && opportunity.get(i).getInternalNum().length() > 0) {
						update(opportunity.get(i));
					} else {
						insert(opportunity.get(i));
					}
				}
			}
		}
		
		//check any opportunity without contact internal num and update it.
		checkInternalNum();
	}
	
	public void checkInternalNum() {
		DeviceUtility.log(TAG, "checkInternalNum()");
		this.openWrite();
		Cursor cursor = database.rawQuery("SELECT c.INTERNAL_NUM AS CONTACT_INTERNAL, o.INTERNAL_NUM AS OPPORTUNITY_INTERNAL FROM "+DatabaseHandler.TABLE_FL_CONTACT+" c, "+DatabaseHandler.TABLE_FL_OPPORTUNITY+" o WHERE c.CONTACT_ID = o.CONTACT_ID AND (o.CONTACT_INTERNAL_NUM is NULL OR o.CONTACT_INTERNAL_NUM = '')", null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "cursor.getCount(): " + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (!cursor.isAfterLast()) {
				ContentValues values = new ContentValues();
				values.put("CONTACT_INTERNAL_NUM", cursor.getString(cursor.getColumnIndex("CONTACT_INTERNAL")));
				int iUpdate = database.update(DatabaseHandler.TABLE_FL_OPPORTUNITY, values, "INTERNAL_NUM = ?", new String[]{cursor.getString(cursor.getColumnIndex("OPPORTUNITY_INTERNAL"))});
				DeviceUtility.log(TAG, "iUpdate: " + iUpdate);
				cursor.moveToNext();
			}
		}
		cursor.close();
		this.close();
	}
}
