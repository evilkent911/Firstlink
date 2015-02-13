package com.trinerva.icrm.object;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.Utility;

public class AccountDetail{
	private String NAMESPACE = "http://schemas.datacontract.org/2004/07/TACRM.DataContract.Mobile";
	private String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	private String internal_num;
	private String account_id;
	private String account_name;
	private String owner;
	private String active;
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

	public AccountDetail() {
	}

	public String getInternalNum() {
		return internal_num;
	}

	public String getAccountId() {
		return account_id;
	}
	
	public String getAccountName() {
		return account_name;
	}

	public String getActive() {
		return active;
	}
	
	public String getOwner() {
		return owner;
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
		this.internal_num = internal_num;
	}

	public void setAccountId(String account_id) {
		this.account_id = account_id;
	}
	
	public void setAccountName(String account_name) {
		this.account_name = account_name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setUserDef1(String user_def1) {
		this.user_def1 = user_def1;
	}

	public void setUserDef2(String user_def2) {
		this.user_def2 = user_def2;
	}

	public void setUserDef3(String user_def3) {
		this.user_def3 = user_def3;
	}

	public void setUserDef4(String user_def4) {
		this.user_def4 = user_def4;
	}

	public void setUserDef5(String user_def5) {
		this.user_def5 = user_def5;
	}

	public void setUserDef6(String user_def6) {
		this.user_def6 = user_def6;
	}

	public void setUserDef7(String user_def7) {
		this.user_def7 = user_def7;
	}

	public void setUserDef8(String user_def8) {
		this.user_def8 = user_def8;
	}

	public void setUserStamp(String user_stamp) {
		this.user_stamp = user_stamp;
	}

	public void setCreatedTimestamp(String created_timestamp) {
		this.created_timestamp = created_timestamp;
	}

	public void setModifiedTimestamp(String modified_timestamp) {
		this.modified_timestamp = modified_timestamp;
	}

}