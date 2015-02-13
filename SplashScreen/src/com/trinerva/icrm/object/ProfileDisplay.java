package com.trinerva.icrm.object;
import android.database.Cursor;

public class ProfileDisplay {
	private String TAG = "ProfileDisplay";
	private String INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, PHOTO, CONTACT_TYPE, EMAIL, PHONE_NO, PERSON_ID;
	
	public ProfileDisplay() {
		
	}
	
	//lead use.
	public ProfileDisplay(String INTERNAL_NUM, String FIRST_NAME, String LAST_NAME, String COMPANY_NAME, String PHOTO) {
		this.INTERNAL_NUM = INTERNAL_NUM;
		this.FIRST_NAME = FIRST_NAME;
		this.LAST_NAME = LAST_NAME;
		this.COMPANY_NAME = COMPANY_NAME;
		this.PHOTO = PHOTO;
	}
	
	//_id, CONTACT_TYPE, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, MOBILE, PERSON_ID
	public ProfileDisplay(Cursor cursor) {
		this.INTERNAL_NUM = cursor.getString(cursor.getColumnIndex("_id"));
		this.CONTACT_TYPE = cursor.getString(cursor.getColumnIndex("CONTACT_TYPE"));
		this.FIRST_NAME = cursor.getString(cursor.getColumnIndex("FIRST_NAME"));
		this.LAST_NAME = cursor.getString(cursor.getColumnIndex("LAST_NAME"));
		this.COMPANY_NAME = cursor.getString(cursor.getColumnIndex("COMPANY_NAME"));
		this.EMAIL = cursor.getString(cursor.getColumnIndex("EMAIL1"));
		this.PHONE_NO = cursor.getString(cursor.getColumnIndex("MOBILE"));
		this.PERSON_ID = cursor.getString(cursor.getColumnIndex("PERSON_ID"));
	}
	/*public ProfileDisplay(Cursor cursor) {
		this.INTERNAL_NUM = cursor.getString(cursor.getColumnIndex("INTERNAL_NUM"));
		this.FIRST_NAME = cursor.getString(cursor.getColumnIndex("FIRST_NAME"));
		this.LAST_NAME = cursor.getString(cursor.getColumnIndex("LAST_NAME"));
		this.COMPANY_NAME = cursor.getString(cursor.getColumnIndex("COMPANY_NAME"));
		this.CONTACT_TYPE = cursor.getString(cursor.getColumnIndex("CONTACT_TYPE"));
		
		String strEmail1 = cursor.getString(cursor.getColumnIndex("EMAIL1"));
		String strEmail2 = cursor.getString(cursor.getColumnIndex("EMAIL2"));
		String strEmail3 = cursor.getString(cursor.getColumnIndex("EMAIL3"));
		
		String strPhone1 = cursor.getString(cursor.getColumnIndex("PHONE1"));
		String strPhone2 = cursor.getString(cursor.getColumnIndex("PHONE2"));
		String strPhone3 = cursor.getString(cursor.getColumnIndex("PHONE3"));
		String strPhone4 = cursor.getString(cursor.getColumnIndex("PHONE4"));
		
		if (strEmail1 != null && strEmail1.length() > 0) {
			this.EMAIL = strEmail1;
		} else if (strEmail2 != null && strEmail2.length() > 0) {
			this.EMAIL = strEmail2;
		} else if (strEmail3 != null && strEmail3.length() > 0) {
			this.EMAIL = strEmail3;
		}
		
		if (strPhone1 != null  && strPhone1.length() > 0) {
			this.PHONE_NO = strPhone1;
		} else if (strPhone2 != null  && strPhone2.length() > 0) {
			this.PHONE_NO = strPhone2;
		} else if (strPhone3 != null  && strPhone3.length() > 0) {
			this.PHONE_NO = strPhone3;
		}  else if (strPhone4 != null  && strPhone4.length() > 0) {
			this.PHONE_NO = strPhone4;
		}
	}*/
	
		
	public void setInternalNum(String INTERNAL_NUM) {
		this.INTERNAL_NUM = INTERNAL_NUM;
	}
	
	public void setFirstName(String FIRST_NAME) {
		this.FIRST_NAME = FIRST_NAME;
	}
	
	public void setLastName(String LAST_NAME) {
		this.LAST_NAME = LAST_NAME;
	}
	
	public void setCompanyName(String COMPANY_NAME) {
		this.COMPANY_NAME = COMPANY_NAME;
	}
	
	public void setPhoto(String PHOTO) {
		this.PHOTO = PHOTO;
	}
	
	public void setContactType(String CONTACT_TYPE) {
		this.CONTACT_TYPE = CONTACT_TYPE;
	}
	
	public void setEmail(String EMAIL) {
		this.EMAIL = EMAIL;
	}
	
	public void setPhoneNo(String PHONE_NO) {
		this.PHONE_NO = PHONE_NO;
	}
	
	public void setPersonId(String PERSON_ID) {
		this.PERSON_ID = PERSON_ID;
	}
	
	public String getInternalNum() {
		return this.INTERNAL_NUM;
	}
	
	public String getFirstName() {
		return this.FIRST_NAME;
	}
	
	public String getLastName() {
		return this.LAST_NAME;
	}
	
	public String getCompanyName() {
		return this.COMPANY_NAME;
	}
	
	public String getPhoto() {
		return this.PHOTO;
	}
	
	public String getContactType() {
		return this.CONTACT_TYPE;
	}
	
	public String getEmail() {
		return this.EMAIL;
	}
	
	public String getPhoneNo() {
		return this.PHONE_NO;
	}
	
	public String getPersonId() {
		return this.PERSON_ID;
	}
	
	public String toString() {
		return "{"+this.INTERNAL_NUM + "," + this.FIRST_NAME + "," + this.LAST_NAME + "," + this.COMPANY_NAME + "," + this.PHOTO + "," + this.CONTACT_TYPE + "," + this.EMAIL + "," + this.PHONE_NO+", "+this.PERSON_ID+"}";
		
	}
}
