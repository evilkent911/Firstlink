package com.trinerva.icrm.object;

public class ReportDetail{

	private String report_type;
	private String report_start_date;
	private String report_end_date;
	private String report_person;
	private String report_value;
	private String report_text;
	private String user_def1;
	private String user_def2;
	private String user_def3;
	private String user_def4;
	private String user_def5;
	private String user_def6;
	private String user_def7;
	private String user_def8;

	public ReportDetail() {
	}

	public String getReportType() {
		return report_type;
	}

	public String getReportStartDate() {
		return report_start_date;
	}

	public String getReportEndDate() {
		return report_end_date;
	}

	public String getReportPerson() {
		return report_person;
	}

	public String getReportValue() {
		return report_value;
	}

	public String getReportText() {
		return report_text;
	}

	public String getUserDef1() {
		return user_def1;
	}

	public String getUserDef2() {
		return user_def2;
	}

	public String getUserDef3() {
		return user_def3;
	}

	public String getUserDef4() {
		return user_def4;
	}

	public String getUserDef5() {
		return user_def5;
	}

	public String getUserDef6() {
		return user_def6;
	}

	public String getUserDef7() {
		return user_def7;
	}

	public String getUserDef8() {
		return user_def8;
	}

	public void setReportType(String report_type) {
		this.report_type=report_type;
	}

	public void setReportStartDate(String report_start_date) {
		this.report_start_date=report_start_date;
	}

	public void setReportEndDate(String report_end_date) {
		this.report_end_date=report_end_date;
	}

	public void setReportPerson(String report_person) {
		this.report_person=report_person;
	}

	public void setReportValue(String report_value) {
		this.report_value=report_value;
	}

	public void setReportText(String report_text) {
		this.report_text=report_text;
	}

	public void setUserDef1(String user_def1) {
		this.user_def1=user_def1;
	}

	public void setUserDef2(String user_def2) {
		this.user_def2=user_def2;
	}

	public void setUserDef3(String user_def3) {
		this.user_def3=user_def3;
	}

	public void setUserDef4(String user_def4) {
		this.user_def4=user_def4;
	}

	public void setUserDef5(String user_def5) {
		this.user_def5=user_def5;
	}

	public void setUserDef6(String user_def6) {
		this.user_def6=user_def6;
	}

	public void setUserDef7(String user_def7) {
		this.user_def7=user_def7;
	}

	public void setUserDef8(String user_def8) {
		this.user_def8=user_def8;
	}

	public String toString() {
		return "{" + report_type + " , " + report_start_date + " , " + report_end_date
			 + " , " + report_person + " , " + report_value + " , " + report_text
			 + " , " + user_def1 + " , " + user_def2 + " , " + user_def3
			 + " , " + user_def4 + " , " + user_def5 + " , " + user_def6
			 + " , " + user_def7 + " , " + user_def8 + "}";
	}

}