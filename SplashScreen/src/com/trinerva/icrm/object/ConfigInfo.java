package com.trinerva.icrm.object;

public class ConfigInfo {
	private String strConfigType, strConfigValue, strConfigText, strCreatedTimestamp, strModifiedTimestamp,strConfigUserDEF1;
	
	public ConfigInfo() {}
	
	public ConfigInfo(String strConfigType, String strConfigValue, String strConfigText, String strCreatedTimestamp, String strModifiedTimestamp,String strConfigUserDEF1) {
		this.strConfigType = strConfigType;
		this.strConfigValue = strConfigValue;
		this.strConfigText = strConfigText;
		this.strCreatedTimestamp = strCreatedTimestamp;
		this.strModifiedTimestamp = strModifiedTimestamp;
		this.strConfigUserDEF1 = strConfigUserDEF1;
	}
	
	public String getConfigType() {
		return strConfigType;
	}
	
	public String getConfigValue() {
		return strConfigValue;
	}
	
	public String getConfigText() {
		return strConfigText;
	}
	
	public String getCreatedTimestamp() {
		return strCreatedTimestamp;
	}
	
	public String getModifiedTimestamp() {
		return strModifiedTimestamp;
	}
	
	public String getUserDEF1() {
		return strConfigUserDEF1;
	}
	
	public void setConfigType(String strConfigType) {
		this.strConfigType = strConfigType;
	}
	
	public void getConfigValue(String strConfigValue) {
		this.strConfigValue = strConfigValue;
	}
	
	public void getConfigText(String strConfigText) {
		this.strConfigText = strConfigText;
	}
	
	public void getCreatedTimestamp(String strCreatedTimestamp) {
		this.strCreatedTimestamp = strCreatedTimestamp;
	}
	
	public void getModifiedTimestamp(String strModifiedTimestamp) {
		this.strModifiedTimestamp = strModifiedTimestamp;
	}
	
	public void getUserDEF1(String strConfigUserDEF1) {
		this.strConfigUserDEF1 = strConfigUserDEF1;
	}
	
	
	public String toString() {
		return "(" + strConfigType + ", " + strConfigValue + ", " + strConfigText + ", " + strCreatedTimestamp + ", " + strModifiedTimestamp + ")";
	}
	
	
	
}
