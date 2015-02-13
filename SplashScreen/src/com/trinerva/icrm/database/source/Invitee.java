package com.trinerva.icrm.database.source;

import java.util.ArrayList;
import java.util.HashMap;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.ProfileDisplay;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;

import android.database.Cursor;

public class Invitee extends BaseSource {
	private String TAG = "Invitee";
	public Invitee(DatabaseHandler dbHandler) {
		super(dbHandler);
	}
	
	public String getIdTypeList(String strSelectedId, String strPrefix) {
		//filter those with contact id
		String strList = "";
		if (strSelectedId.length() > 0) {
			String[] strSplitList = strSelectedId.split(";");
			
			for (String item : strSplitList) {
				if (item.startsWith(strPrefix)) {
					if (strList.length() > 0) {
						strList += ",";
					}
					strList += "'" + item.substring(2) + "'";
				}
			}
			DeviceUtility.log(TAG, "strList:("+ strSelectedId + "," + strPrefix+")" + strList);
			
		} else {
			strList = "''";
		}
		return strList.toLowerCase();
	}
	
	public Cursor getQualifyInvitee(String strSelectedId) {
		this.openRead();
		ArrayList<ProfileDisplay> aProfile = new ArrayList<ProfileDisplay>();
		DeviceUtility.log(TAG, "getQualifyInvitee");
		String strLeadId = getIdTypeList(strSelectedId, "3-");
		String strContactId = getIdTypeList(strSelectedId, "2-");
		
		Cursor cursor = database.rawQuery(	"SELECT _id, CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, PERSON_ID, CRM_PERSON_TYPE FROM (" + 
											"SELECT INTERNAL_NUM AS _id, 'CONTACT' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '2-' || CONTACT_ID AS PERSON_ID, '2' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND CONTACT_ID IS NOT NULL AND CONTACT_ID <> '' AND ACTIVE = ? AND CONTACT_ID NOT IN ("+strContactId+") " +
											"UNION " +
											" SELECT INTERNAL_NUM AS _id, 'LEAD' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '3-' || LEAD_ID AS PERSON_ID, '3' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND LEAD_ID IS NOT NULL AND LEAD_ID <> '' AND ACTIVE = ?  AND LEAD_ID NOT IN ("+strLeadId+")) AS U",
				new String[] {"0", "0"});
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getQualifyInvitee: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public Cursor getQualifyLeadInvitee(String strSelectedId) {
		this.openRead();
		ArrayList<ProfileDisplay> aProfile = new ArrayList<ProfileDisplay>();
		DeviceUtility.log(TAG, "getQualifyInvitee");
		String strLeadId = getIdTypeList(strSelectedId, "3-");
		String strContactId = getIdTypeList(strSelectedId, "2-");
		
		Cursor cursor = database.rawQuery(	"SELECT _id, CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, PERSON_ID, CRM_PERSON_TYPE FROM (" + 
											"SELECT INTERNAL_NUM AS _id, 'CONTACT' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '2-' || CONTACT_ID AS PERSON_ID, '2' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND CONTACT_ID IS NOT NULL AND CONTACT_ID <> '' AND ACTIVE = ? AND CONTACT_ID NOT IN ("+strContactId+") " +
											"UNION " +
											" SELECT INTERNAL_NUM AS _id, 'LEAD' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '3-' || LEAD_ID AS PERSON_ID, '3' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND LEAD_ID IS NOT NULL AND LEAD_ID <> '' AND ACTIVE = ?  AND LEAD_ID NOT IN ("+strLeadId+")) AS U",
				new String[] {"0", "0"});
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getQualifyInvitee: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	/*
	public ArrayList<ProfileDisplay> getInvitedPerson(String strInvitedList) {
		this.openRead();
		ArrayList<ProfileDisplay> aProfile = new ArrayList<ProfileDisplay>();
		DeviceUtility.log(TAG, "getInvitedPerson");
		
		String strEmailList = "'" + strInvitedList.replace(",", "','") + "'";
		
		Cursor cursor = database.rawQuery("SELECT _id, IDENTITY, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, PHONE1, PHONE2, PHONE3, PHONE4, CONTACT_TYPE FROM ( " +
				" SELECT 'C' || INTERNAL_NUM AS _id, CONTACT_ID AS IDENTITY, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, MOBILE AS PHONE1, HOME_PHONE AS PHONE2, WORK_PHONE AS PHONE3, OTHER_PHONE AS PHONE4, 'CONTACT' AS CONTACT_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE ((EMAIL1 IS NOT NULL AND EMAIL1 NOT IN ("+strEmailList+")) OR (EMAIL2 IS NOT NULL AND EMAIL2 NOT IN ("+strEmailList+")) OR  (EMAIL3 IS NOT NULL AND EMAIL3 NOT IN ("+strEmailList+"))) AND ACTIVE = ? "+
				" UNION " +
				"SELECT 'L' || INTERNAL_NUM AS _id, LEAD_ID AS IDENTITY, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, MOBILE AS PHONE1, WORK_PHONE AS PHONE2, '' AS PHONE3, '' AS PHONE4, 'LEAD' AS CONTACT_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE ((EMAIL1 IS NOT NULL AND EMAIL1 NOT IN ("+strEmailList+")) OR (EMAIL2 IS NOT NULL AND EMAIL2 NOT IN ("+strEmailList+")) OR  (EMAIL3 IS NOT NULL AND EMAIL3 NOT IN ("+strEmailList+"))) AND ACTIVE = ?)",
				new String[] {"0", "0"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getInvitedPerson: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			ProfileDisplay profile = new ProfileDisplay(cursor);
			aProfile.add(profile);
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aProfile;
	}
	
	public String getSelectedPhoneNumber(ArrayList<String> strEmail) {
		String strSelectedPhone = "";
		if (strEmail.size() > 0) {
			this.openRead();
			String strEmailList = doFormatEmailToList(strEmail);
			Cursor cursor = database.rawQuery("SELECT PHONE1, PHONE2, PHONE3, PHONE4 FROM ( " +
												" SELECT MOBILE AS PHONE1, HOME_PHONE AS PHONE2, WORK_PHONE AS PHONE3, OTHER_PHONE AS PHONE4 FROM " + DatabaseHandler.TABLE_FL_CONTACT + " WHERE ACTIVE = ? AND (EMAIL1 IN (" + strEmailList + ") OR EMAIL2 IN (" + strEmailList + ") OR EMAIL3 IN(" + strEmailList + "))" +
												" UNION " +
												"SELECT MOBILE AS PHONE1, WORK_PHONE AS PHONE2, '' AS PHONE3, '' AS PHONE4 FROM " + DatabaseHandler.TABLE_FL_LEAD + " WHERE ACTIVE = ? AND (EMAIL1 IN (" + strEmailList + ") OR EMAIL2 IN (" + strEmailList + ") OR EMAIL3 IN(" + strEmailList + ")))",
												new String[] {"0", "0"});
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "getSelectedPhoneNumber: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				String strPhone1 = cursor.getString(cursor.getColumnIndex("PHONE1"));
				String strPhone2 = cursor.getString(cursor.getColumnIndex("PHONE2"));
				String strPhone3 = cursor.getString(cursor.getColumnIndex("PHONE3"));
				String strPhone4 = cursor.getString(cursor.getColumnIndex("PHONE4"));

				if (strPhone1 != null  && strPhone1.length() > 0) {
					strSelectedPhone += strPhone1 + ";";
				} else if (strPhone2 != null  && strPhone2.length() > 0) {
					strSelectedPhone += strPhone2 + ";";
				} else if (strPhone3 != null  && strPhone3.length() > 0) {
					strSelectedPhone += strPhone3 + ";";
				}  else if (strPhone4 != null  && strPhone4.length() > 0) {
					strSelectedPhone += strPhone4 + ";";
				}
				cursor.moveToNext();
			}
			//remove the last semi colon
			strSelectedPhone = strSelectedPhone.substring(0, strSelectedPhone.length()-1);
			DeviceUtility.log(TAG, "strSelectedPhone: " + strSelectedPhone);
			this.close();
		}
		return strSelectedPhone;
	}*/

	public ArrayList<ProfileDisplay> getSelectedInviteeList(String strSelectedId) {
		DeviceUtility.log(TAG, "getSelectedInviteeList: " + strSelectedId);
		ArrayList<ProfileDisplay> aProfile = new ArrayList<ProfileDisplay>();
		this.openRead();
		String strLeadId = getIdTypeList(strSelectedId, "3-");
		String strContactId = getIdTypeList(strSelectedId, "2-");
			
		Cursor cursor = database.rawQuery(	"SELECT _id, CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, PERSON_ID, CRM_PERSON_TYPE FROM (" + 
											"SELECT INTERNAL_NUM AS _id, 'CONTACT' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '2-' || CONTACT_ID AS PERSON_ID, '2' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND CONTACT_ID IS NOT NULL AND CONTACT_ID <> '' AND ACTIVE = ? AND CONTACT_ID IN ("+strContactId+") " +
											"UNION " +
											" SELECT INTERNAL_NUM AS _id, 'LEAD' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '3-' || LEAD_ID AS PERSON_ID, '3' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND LEAD_ID IS NOT NULL AND LEAD_ID <> '' AND ACTIVE = ?  AND LEAD_ID IN ("+strLeadId+")) AS U",
		new String[] {"0", "0"});
			
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getSelectedInviteeList: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			ProfileDisplay profile = new ProfileDisplay(cursor);
			DeviceUtility.log(TAG, profile.toString());
			aProfile.add(profile);
			cursor.moveToNext();
		}
		return aProfile;
	}

	public Cursor getAllContact(ArrayList<ProfileDisplay> aSelected) {
		this.openRead();
		DeviceUtility.log(TAG, "getAllContact: ");
		int iCount = aSelected.size();
		String strContactId = "";
		String strLeadId = "";
		for (int i = 0; i < iCount; i++) {
			ProfileDisplay profile = aSelected.get(i);
			if (profile.getContactType().equals("CONTACT")) {
				if (strContactId.length() > 0) {
					strContactId += ",";
				}
				strContactId += "'"+profile.getPersonId().substring(2)+"'";
			} else if (profile.getContactType().equals("LEAD")) {
				if (strLeadId.length() > 0) {
					strLeadId += ",";
				}
				strLeadId += "'"+profile.getPersonId().substring(2)+"'";
			}
		}
		
		strContactId.toLowerCase();
		strLeadId.toLowerCase();
		
		Cursor cursor = database.rawQuery(	"SELECT _id, CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, PERSON_ID, CRM_PERSON_TYPE FROM (" + 
				"SELECT INTERNAL_NUM AS _id, 'CONTACT' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '2-' || CONTACT_ID AS PERSON_ID, '2' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND CONTACT_ID IS NOT NULL AND CONTACT_ID <> '' AND ACTIVE = ? AND CONTACT_ID NOT IN ("+strContactId+") " +
				"UNION " +
				" SELECT INTERNAL_NUM AS _id, 'LEAD' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '3-' || LEAD_ID AS PERSON_ID, '3' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND LEAD_ID IS NOT NULL AND LEAD_ID <> '' AND ACTIVE = ?  AND LEAD_ID NOT IN ("+strLeadId+")) AS U",
				new String[] {"0", "0"});
		
		cursor.moveToFirst();
		
		DeviceUtility.log(TAG, "getAllContact: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public String doFormatEmailToList(ArrayList<String> strEmail) {
		String strEmailList = "";
		if (strEmail != null && strEmail.size() > 0) {
			for (int k = 0; k < strEmail.size(); k++) {
				strEmailList += "'"+strEmail.get(k)+"', ";
			}
			strEmailList = strEmailList.substring(0, strEmailList.length()-2);
			DeviceUtility.log(TAG, "strEmailList: " + strEmailList);
		}
		return strEmailList;
	}
	
	/*public Cursor getAllContact(ArrayList<ProfileDisplay> aSelected) {
		this.openRead();
		DeviceUtility.log(TAG, "getAllContact: ");
		HashMap<String, String> aResult = getInternalNumList(aSelected);

		Cursor cursor = database.rawQuery("SELECT _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, PHONE1, PHONE2, PHONE3, PHONE4, CONTACT_TYPE FROM ( " +
				" SELECT 'C' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, MOBILE AS PHONE1, HOME_PHONE AS PHONE2, WORK_PHONE AS PHONE3, OTHER_PHONE AS PHONE4, 'CONTACT' AS CONTACT_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE (EMAIL1 IS NOT NULL OR EMAIL2 IS NOT NULL OR  EMAIL3 IS NOT NULL) AND ACTIVE = ? "+ (aResult.get(Constants.PERSON_TYPE_CONTACT) != null ? " AND INTERNAL_NUM NOT IN ("+aResult.get(Constants.PERSON_TYPE_CONTACT)+")" : "") +
				" UNION " +
				"SELECT 'L' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, MOBILE AS PHONE1, WORK_PHONE AS PHONE2, '' AS PHONE3, '' AS PHONE4, 'LEAD' AS CONTACT_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE (EMAIL1 IS NOT NULL OR EMAIL2 IS NOT NULL OR  EMAIL3 IS NOT NULL) AND ACTIVE = ? " + (aResult.get(Constants.PERSON_TYPE_LEAD) != null ? " AND INTERNAL_NUM NOT IN ("+aResult.get(Constants.PERSON_TYPE_LEAD)+")" : "") + ")",
				new String[] {"0", "0"});

		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getAllContact: " + cursor.getCount());
		this.close();
		return cursor;
	}*/

	/*
	public Cursor getInviteeDisplayByFilter(String strFilter, ArrayList<ProfileDisplay> aSelected) {
		this.openRead();
		DeviceUtility.log(TAG, "getInviteeDisplayByFilter("+strFilter+")");
		HashMap<String, String> aResult = getInternalNumList(aSelected);

		Cursor cursor = database.rawQuery("SELECT _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, PHONE1, PHONE2, PHONE3, PHONE4, CONTACT_TYPE FROM ( " +
				" SELECT 'C' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, MOBILE AS PHONE1, HOME_PHONE AS PHONE2, WORK_PHONE AS PHONE3, OTHER_PHONE AS PHONE4, 'CONTACT' AS CONTACT_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE (EMAIL1 IS NOT NULL OR EMAIL2 IS NOT NULL OR  EMAIL3 IS NOT NULL) AND ACTIVE = ? AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ?) "+(aResult.get(Constants.PERSON_TYPE_CONTACT) != null ? " AND INTERNAL_NUM NOT IN ("+aResult.get(Constants.PERSON_TYPE_CONTACT)+") " : "") +
				" UNION " +
				"SELECT 'L' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, MOBILE AS PHONE1, WORK_PHONE AS PHONE2, '' AS PHONE3, '' AS PHONE4, 'LEAD' AS CONTACT_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE (EMAIL1 IS NOT NULL OR EMAIL2 IS NOT NULL OR  EMAIL3 IS NOT NULL) AND ACTIVE = ? AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ?) "+(aResult.get(Constants.PERSON_TYPE_LEAD) != null ? " AND INTERNAL_NUM NOT IN ("+aResult.get(Constants.PERSON_TYPE_LEAD) + ")" : "") + ");",
				new String[] {"0", "%"+strFilter+"%", "%"+strFilter+"%", "%"+strFilter+"%", "0", "%"+strFilter+"%", "%"+strFilter+"%", "%"+strFilter+"%",});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getInviteeDisplayByFilter: " + cursor.getCount());
		this.close();
		return cursor;
	}*/
	
	public Cursor getInviteeDisplayByFilter(String strFilter, ArrayList<ProfileDisplay> aSelected) {
		this.openRead();
		DeviceUtility.log(TAG, "getInviteeDisplayByFilter");
		int iCount = aSelected.size();
		String strContactId = "";
		String strLeadId = "";
		for (int i = 0; i < iCount; i++) {
			ProfileDisplay profile = aSelected.get(i);
			if (profile.getContactType().equals("CONTACT")) {
				if (strContactId.length() > 0) {
					strContactId += ",";
				}
				strContactId += "'"+profile.getPersonId().substring(2) + "'";
			} else if (profile.getContactType().equals("LEAD")) {
				if (strLeadId.length() > 0) {
					strLeadId += ",";
				}
				strLeadId += "'"+profile.getPersonId().substring(2) + "'";
			}
		}
		
		strContactId.toLowerCase();
		strLeadId.toLowerCase();
		
		Cursor cursor = database.rawQuery(	"SELECT _id, CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, PERSON_ID, CRM_PERSON_TYPE FROM (" + 
											"SELECT INTERNAL_NUM AS _id, 'CONTACT' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '2-' || CONTACT_ID AS PERSON_ID, '2' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_CONTACT+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND CONTACT_ID IS NOT NULL AND CONTACT_ID <> '' AND ACTIVE = ? AND CONTACT_ID NOT IN ("+strContactId+") AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ?) " +
											"UNION " +
											" SELECT INTERNAL_NUM AS _id, 'LEAD' AS CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, '3-' || LEAD_ID AS PERSON_ID, '3' AS CRM_PERSON_TYPE FROM "+DatabaseHandler.TABLE_FL_LEAD+" WHERE EMAIL1 IS NOT NULL AND EMAIL1 <> '' AND LEAD_ID IS NOT NULL AND LEAD_ID <> '' AND ACTIVE = ?  AND LEAD_ID NOT IN ("+strLeadId+") AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR COMPANY_NAME LIKE ?) ) AS U",
				new String[] {"0", "%"+strFilter+"%", "%"+strFilter+"%", "%"+strFilter+"%", "0", "%"+strFilter+"%", "%"+strFilter+"%", "%"+strFilter+"%"});
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getInviteeDisplayByFilter: " + cursor.getCount());
		this.close();
		return cursor;
	}

	/*public HashMap<String, String> getInternalNumList(ArrayList<ProfileDisplay> aSelected) {
		HashMap<String, String> aResult = new HashMap<String, String>();
		String strContact = "";
		String strLead = "";
		for (int i = 0; i < aSelected.size(); i++) {
			ProfileDisplay profile = aSelected.get(i);
			if (profile.getContactType().equalsIgnoreCase(Constants.PERSON_TYPE_LEAD)){
				strLead += "'"+profile.getInternalNum()+"',";
			} else if (profile.getContactType().equalsIgnoreCase(Constants.PERSON_TYPE_CONTACT)){
				strContact += "'"+profile.getInternalNum()+"',";
			}
		}

		if (strContact.length() > 0) {
			aResult.put(Constants.PERSON_TYPE_CONTACT, strContact.substring(0, strContact.length()-1));
		}

		if (strLead.length() > 0) {
			aResult.put(Constants.PERSON_TYPE_LEAD, strLead.substring(0, strLead.length()-1));
		}
		return aResult;
	}*/

}
