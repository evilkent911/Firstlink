package com.trinerva.icrm.object;

public class PhoneDetail {
	private String strPhoneNo;
	private String strPhoneType;
	
	public PhoneDetail(String strPhoneNo, String strPhoneType) {
		this.strPhoneNo = strPhoneNo;
		this.strPhoneType = strPhoneType;
	}
	
	public String getPhoneNo() {
		return strPhoneNo;
	}
	
	public String getPhoneType() {
		return strPhoneType;
	}
	
	public String toString() {
		return strPhoneNo + " ("+ strPhoneType + ")";
	}
}