package com.trinerva.icrm.object;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.Utility;

public class OpportunityDetail implements KvmSerializable{
	private String NAMESPACE = "http://schemas.datacontract.org/2004/07/TACRM.DataContract.Mobile";
	private String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	private String internal_num;
	private String contact_internal_num;
	private String contact_id;
	private String opp_id;
	private String opp_name;
	private String opp_desc;
	private String opp_amount;
	private String opp_date;
	private String opp_stage;
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
	private String is_update;

	public OpportunityDetail() {
	}

	public String getInternalNum() {
		return internal_num;
	}

	public String getContactInternalNum() {
		return contact_internal_num;
	}

	public String getContactId() {
		return contact_id;
	}

	public String getOppId() {
		return opp_id;
	}

	public String getOppName() {
		return opp_name;
	}

	public String getOppDesc() {
		return opp_desc;
	}

	public String getOppAmount() {
		return opp_amount;
	}

	public String getOppDate() {
		return opp_date;
	}

	public String getOppStage() {
		return opp_stage;
	}

	public String getActive() {
		return active;
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

	public String getIsUpdate() {
		return is_update;
	}

	public void setInternalNum(String internal_num) {
		this.internal_num=internal_num;
	}

	public void setContactInternalNum(String contact_internal_num) {
		this.contact_internal_num=contact_internal_num;
	}

	public void setContactId(String contact_id) {
		this.contact_id=contact_id;
	}

	public void setOppId(String opp_id) {
		this.opp_id=opp_id;
	}

	public void setOppName(String opp_name) {
		this.opp_name=opp_name;
	}

	public void setOppDesc(String opp_desc) {
		this.opp_desc=opp_desc;
	}

	public void setOppAmount(String opp_amount) {
		this.opp_amount=opp_amount;
	}

	public void setOppDate(String opp_date) {
		this.opp_date=opp_date;
	}

	public void setOppStage(String opp_stage) {
		this.opp_stage=opp_stage;
	}

	public void setActive(String active) {
		this.active=active;
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

	public void setIsUpdate(String is_update) {
		this.is_update=is_update;
	}

	public String toString() {
		return "{" + internal_num + " , " + contact_internal_num + " , " + contact_id
			 + " , " + opp_id + " , " + opp_name + " , " + opp_desc
			 + " , " + opp_amount + " , " + opp_date + " , " + opp_stage
			 + " , " + active + " , " + user_def1 + " , " + user_def2
			 + " , " + user_def3 + " , " + user_def4 + " , " + user_def5
			 + " , " + user_def6 + " , " + user_def7 + " , " + user_def8
			 + " , " + user_stamp + " , " + created_timestamp + " , " + modified_timestamp
			 + " , " + is_update + "}";
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
			case 0:
				return active;
			case 1:
				return contact_id;
			case 2:
				return internal_num;
			case 3:
				return Utility.toXmlDate(modified_timestamp, Constants.DATETIMESEC_FORMAT);
				//return modified_timestamp;
			case 4:
				return opp_amount;
			case 5:
				return Utility.toXmlDate(opp_date, Constants.DATE_FORMAT);
			case 6:
				return opp_desc;
			case 7:
				return opp_id;
			case 8:
				return opp_name;
			case 9:
				return opp_stage;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 10;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0) {
			case 0:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Active";
				arg2.namespace = this.NAMESPACE;
				break;
			case 1:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ContactID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 2:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "IOpportunityID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 3:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ModifiedDate";
				if (modified_timestamp == null || modified_timestamp.equals("")) {
					arg2.namespace = this.W3CNAMESPACE;
				} else {
					arg2.namespace = this.NAMESPACE;
				}
				break;
			case 4:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OpportunityAmount";
				arg2.namespace = this.NAMESPACE;
				break;
			case 5:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OpportunityDate";
				if (opp_date == null || opp_date.equals("")) {
					arg2.namespace = this.W3CNAMESPACE;
				} else {
					arg2.namespace = this.NAMESPACE;
				}
				break;
			case 6:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OpportunityDescription";
				arg2.namespace = this.NAMESPACE;
				break;
			case 7:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OpportunityID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 8:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OpportunityName";
				arg2.namespace = this.NAMESPACE;
				break;
			case 9:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OpportunityStage";
				arg2.namespace = this.NAMESPACE;
				break;			
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0) {
		case 0:
			active = arg1.toString();
			break;
		case 1:
			contact_id = arg1.toString();
			break;
		case 2:
			internal_num = arg1.toString();
			break;
		case 3:
			modified_timestamp = arg1.toString();
			break;
		case 4:
			opp_amount = arg1.toString();
			break;
		case 5:
			opp_date = arg1.toString();
			break;
		case 6:
			opp_desc = arg1.toString();
			break;
		case 7:
			opp_id = arg1.toString();
			break;
		case 8:
			opp_name = arg1.toString();
			break;
		case 9:
			opp_stage = arg1.toString();
			break;
		default:
			break;
		}
	}

}