package com.trinerva.icrm.object;

public class ResponseDetail{

	private String internal_num;
	private String request_type;
	private String request_text;
	private String response_text;
	private String return_code;
	private String return_message;
	private String user_def1;
	private String user_def2;
	private String user_def3;
	private String user_def4;
	private String user_def5;
	private String user_def6;
	private String user_def7;
	private String user_def8;
	private String user_stamp;
	private String created_timestamp;
	private String modified_timestamp;

	public ResponseDetail() {
	}

	public String getInternalNum() {
		return internal_num;
	}

	public String getRequestType() {
		return request_type;
	}

	public String getRequestText() {
		return request_text;
	}

	public String getResponseText() {
		return response_text;
	}

	public String getReturnCode() {
		return return_code;
	}

	public String getReturnMessage() {
		return return_message;
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

	public String getUserStamp() {
		return user_stamp;
	}

	public String getCreatedTimestamp() {
		return created_timestamp;
	}

	public String getModifiedTimestamp() {
		return modified_timestamp;
	}

	public void setInternalNum(String internal_num) {
		this.internal_num=internal_num;
	}

	public void setRequestType(String request_type) {
		this.request_type=request_type;
	}

	public void setRequestText(String request_text) {
		this.request_text=request_text;
	}

	public void setResponseText(String response_text) {
		this.response_text=response_text;
	}

	public void setReturnCode(String return_code) {
		this.return_code=return_code;
	}

	public void setReturnMessage(String return_message) {
		this.return_message=return_message;
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

	public void setUserStamp(String user_stamp) {
		this.user_stamp=user_stamp;
	}

	public void setCreatedTimestamp(String created_timestamp) {
		this.created_timestamp=created_timestamp;
	}

	public void setModifiedTimestamp(String modified_timestamp) {
		this.modified_timestamp=modified_timestamp;
	}

	public String toString() {
		return "{" + internal_num + " , " + request_type + " , " + request_text
			 + " , " + response_text + " , " + return_code + " , " + return_message
			 + " , " + user_def1 + " , " + user_def2 + " , " + user_def3
			 + " , " + user_def4 + " , " + user_def5 + " , " + user_def6
			 + " , " + user_def7 + " , " + user_def8 + " , " + user_stamp
			 + " , " + created_timestamp + " , " + modified_timestamp + "}";
	}

}