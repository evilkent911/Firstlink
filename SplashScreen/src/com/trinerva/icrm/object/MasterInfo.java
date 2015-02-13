package com.trinerva.icrm.object;

public class MasterInfo {
	private boolean bDefaultValue,checkQuestion;
	private String strText;
	private String strValue;
	private String strType;
	private String strUser2;
	private String probability;
	private String question;
	private String answear;
	
	public MasterInfo(){
		
	}
	public MasterInfo (boolean bDefaultValue,  String strProbability, String strText, String strValue, String strType) {
		this.bDefaultValue = bDefaultValue;
		this.strText = strText;
		this.strValue = strValue;
		this.strType = strType;
		this.probability = strProbability;
	}
	
	public MasterInfo (boolean bDefaultValue, String strText, String strValue, String strType) {
		this.bDefaultValue = bDefaultValue;
		this.strText = strText;
		this.strValue = strValue;
		this.strType = strType;
	}
	
	public MasterInfo (String strText, String strValue,String strUser2, String strType) {
		this.strText = strText;
		this.strValue = strValue;
		this.strType = strType;
		this.strUser2 = strUser2;
	}
	
	public MasterInfo (String strText, String strValue, String strType) {
		this.strText = strText;
		this.strValue = strValue;
		this.strType = strType;
	}
	
	public MasterInfo (boolean checkQuestion,String question, String answear) {
		this.question = question;
		this.answear = answear;
		this.checkQuestion = checkQuestion;
	}
	
	public boolean getDefaultValue() {
		return this.bDefaultValue;
	}
	
	public String getText() {
		return this.strText;
	}
	
	public String getValue() {
		return this.strValue;
	}
	
	public String getType() {
		return this.strType;
	}
	
	public String getUser2() {
		return this.strUser2;
	}
	
	public String getUser3() {
		return this.probability;
	}
	
	public void setDefaultValue(boolean bDefaultValue) {
		this.bDefaultValue = bDefaultValue;
	}
	
	public void setText(String strText) {
		this.strText = strText;
	}
	
	public void setValue(String strValue) {
		this.strValue = strValue;
	}
	
	public void setType(String strType) {
		this.strType = strType;
	}
	
	public void setUser2(String strUser2) {
		this.strUser2 = strUser2;
	}
	
	public String toString() {
		return "{"+this.bDefaultValue+", "+this.strText+", "+this.strValue+", "+this.strType+","+checkQuestion+"}";
	}
}
