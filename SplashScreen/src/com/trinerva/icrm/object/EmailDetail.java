package com.trinerva.icrm.object;

public class EmailDetail {
	private String strEmailAddress;
	private String strEmailType;
	
	public EmailDetail(String strEmailAddress, String strEmailType) {
		this.strEmailAddress = strEmailAddress;
		this.strEmailType = strEmailType;
	}
	
	public String getEmailAddress() {
		return strEmailAddress;
	}
	
	public String getEmailType() {
		return strEmailType;
	}
	
	public String toString() {
		return strEmailAddress + " ("+ strEmailType + ")";
	}
}