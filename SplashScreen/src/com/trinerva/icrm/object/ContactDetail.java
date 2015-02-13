package com.trinerva.icrm.object;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.Utility;

public class ContactDetail implements KvmSerializable {
	private String NAMESPACE = "http://schemas.datacontract.org/2004/07/TACRM.DataContract.Mobile";
	private String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	private String internal_num;
	private String contact_id;
	private String first_name;
	private String last_name;
	private String prefix;
	private String birthday;
	private String company_name;
	private String company_id;
	private String department;
	private String job_title;
	private String mobile;
	private String home_phone;
	private String work_phone;
	private String other_phone;
	private String work_fax;
	private String mailing_city;
	private String mailing_country;
	private String mailing_state;
	private String mailing_street;
	private String mailing_zip;
	private String email1;
	private String email2;
	private String email3;
	private String skype_id;
	private String assistant_name;
	private String assistant_phone;
	private String owner;
	private String active;
	private String description;
	private String gps_lat;
	private String gps_long;
	private String update_gps_location;
	private String photo;
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
	
	public ContactDetail() {
		
	}
	
	public String getInternalNumber() {
		return internal_num;
	}
	
	public String getContactId() { 
		return contact_id;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public String getCompanyName() {
		return company_name;
	}
	
	public String getCompanyId() {
	return company_id;
}
	
	public String getDepartment() {
		return department;
	}
	
	public String getJobTitle() {
		return job_title;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public String getHomePhone() {
		return home_phone;
	}
	
	public String getWorkPhone() {
		return work_phone;
	}
	
	public String getOtherPhone() {
		return other_phone;
	}
	
	public String getWorkFax() {
		return work_fax;
	}
	
	public String getMailingCity() {
		return mailing_city;
	}
	
	public String getMailingCountry() {
		return mailing_country;
	}
	
	public String getMailingState() {
		return mailing_state;
	}
	
	public String getMailingStreet() {
		return mailing_street;
	}
	
	public String getMailingZip() {
		return mailing_zip;
	}
	
	public String getEmail1() {
		return email1;
	}
	
	public String getEmail2() {
		return email2;
	}
	
	public String getEmail3() {
		return email3;
	}
	
	public String getSkypeId() {
		return skype_id;
	}
	
	public String getAssistantName() {
		return assistant_name;
	}
	
	public String getAssistantPhone() {
		return assistant_phone;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getActive() {
		return active;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getGpsLat() {
		return gps_lat;
	}
	
	public String getGpsLong() {
		return gps_long;
	}
	
	public String getUpdateGpsLocation() {
		return update_gps_location;
	}
	
	public String getPhoto() {
		return photo;
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
	
	//SET
	public void setInternalNumber(String internal_num) {
		this.internal_num = internal_num;
	}
	
	public void setContactId(String contact_id) { 
		this.contact_id=contact_id;
	}
	
	public void setFirstName(String first_name) {
		this.first_name=first_name;
		
	}
	
	public void setLastName(String last_name) {
		this.last_name=last_name;
	}
	
	public void setPrefix(String prefix) {
		this.prefix=prefix;
	}
	
	public void setBirthday(String birthday) {
		this.birthday=birthday;
	}
	
	public void setCompanyName(String company_name) {
		this.company_name=company_name;
	}
	
	public void setCompanyId(String company_id) {
		this.company_id=company_id;
	}
	
	public void setDepartment(String department) {
		this.department=department;
	}
	
	public void setJobTitle(String job_title) {
		this.job_title=job_title;
	}
	
	public void setMobile(String mobile) {
		this.mobile=mobile;
	}
	
	public void setHomePhone(String home_phone) {
		this.home_phone=home_phone;
	}
	
	public void setWorkPhone(String work_phone) {
		this.work_phone=work_phone;
	}
	
	public void setOtherPhone(String other_phone) {
		this.other_phone=other_phone;
	}
	
	public void setWorkFax(String work_fax) {
		this.work_fax=work_fax;
	}
	
	public void setMailingCity(String mailing_city) {
		this.mailing_city=mailing_city;
	}
	
	public void setMailingCountry(String mailing_country) {
		this.mailing_country=mailing_country;
	}
	
	public void setMailingState(String mailing_state) {
		this.mailing_state=mailing_state;
	}
	
	public void setMailingStreet(String mailing_street) {
		this.mailing_street=mailing_street;
	}
	
	public void setMailingZip(String mailing_zip) {
		this.mailing_zip=mailing_zip;
	}
	
	public void setEmail1(String email1) {
		this.email1=email1;
	}
	
	public void setEmail2(String email2) {
		this.email2=email2;
	}
	
	public void setEmail3(String email3) {
		this.email3=email3;
	}
	
	public void setSkypeId(String skype_id) {
		this.skype_id=skype_id;
	}
	
	public void setAssistantName(String assistant_name) {
		this.assistant_name=assistant_name;
	}
	
	public void setAssistantPhone(String assistant_phone) {
		this.assistant_phone=assistant_phone;
	}
	
	public void setOwner(String owner) {
		this.owner=owner;
	}
	
	public void setActive(String active) {
		this.active=active;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	
	public void setGpsLat(String gps_lat) {
		this.gps_lat=gps_lat;
	}
	
	public void setGpsLong(String gps_long) {
		this.gps_long=gps_long;
	}
	
	public void setUpdateGpsLocation(String update_gps_location) {
		this.update_gps_location=update_gps_location;
	}
	
	public void setPhoto(String photo) {
		this.photo=photo;
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
		return "{" + internal_num + "," + contact_id + "," + first_name + ","
				+ last_name + "," + prefix + "," + birthday + "," + company_name + ","
				+ department + "," + job_title + "," + mobile + "," + home_phone + ","
				+ work_phone + "," + other_phone + "," + work_fax + "," + mailing_city + ","
				+ mailing_country + "," + mailing_state + "," + mailing_street + ","
				+ mailing_zip + "," + email1 + "," + email2 + "," + email3 + ","
				+ skype_id + "," + assistant_name + "," + assistant_phone + ","
				+ owner + "," + active + "," + description + "," + gps_lat + ","
				+ gps_long + "," + update_gps_location + "," + photo + ","
				+ user_def1 + "," + user_def2 + "," + user_def3 + ","
				+ user_def4 + "," + user_def5 + "," + user_def6 + ","
				+ user_def7 + "," + user_def8 + "," + user_stamp + ","
				+ created_timestamp + "," + modified_timestamp + "," + is_update + "}";
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
			case 0:
				return active;
			case 1:
				return assistant_name;
			case 2:
				return Utility.toXmlPhone(Utility.doPhoneVerification(assistant_phone));
			case 3:				
				//String strBirthday = Utility.toXmlDate(birthday, Constants.DATE_FORMAT);
				//return (strBirthday == null ? Constants.DEFAULT_DATE : strBirthday);
				return Utility.toXmlDate(birthday, Constants.DATE_FORMAT);
			case 4:
				return company_name;
			case 5:
				return company_id;
			case 6:
				return contact_id;
			case 7:
				return department;
			case 8:
				return description;
			case 9:
				return email1;
			case 10:
				return email2;
			case 11:
				return email3;
			case 12:
				return first_name;
			case 13:
				if (gps_lat == null) {
					return "0";
				} else {
					return gps_lat;
				}
			case 14:
				if (gps_long == null) {
					return "0";
				} else {
					return gps_long;
				}
			case 15:
				return Utility.toXmlPhone(Utility.doPhoneVerification(home_phone));
			case 16:
				return internal_num;
			case 17:
				return job_title;
			case 18:
				return last_name;
			case 19:
				return mailing_city;
			case 20:
				return mailing_country;
			case 21:
				return mailing_state;
			case 22:
				return mailing_street;
			case 23:
				return mailing_zip;
			case 24:
				return Utility.toXmlMobile(Utility.doPhoneVerification(mobile));
			case 25:
				return Utility.toXmlDate(modified_timestamp, Constants.DATETIMESEC_FORMAT);
				//return modified_timestamp;
			case 26:
				return Utility.toXmlPhone(Utility.doPhoneVerification(other_phone));
			case 27:
				return owner;
			case 28:
				return prefix;  
			case 29:
				return skype_id;
			case 30:
				return update_gps_location;
			case 31:
				return work_fax;
			case 32:
				return work_phone;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 33;
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
				arg2.name = "AssistantName";
				arg2.namespace = this.NAMESPACE;
				break;
			case 2:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "AssistantPhone";
				arg2.namespace = this.NAMESPACE;
				break;
			case 3:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Birthday";
				if (birthday == null || birthday.equals("")) {
					arg2.namespace = this.W3CNAMESPACE;
				} else {
					arg2.namespace = this.NAMESPACE;
				}
				break;
			case 4:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "CompanyName";
				arg2.namespace = this.NAMESPACE;
				break;
			case 5:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ContactAccount";
				arg2.namespace = this.NAMESPACE;
				break;
			case 6:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ContactID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 7:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Department";
				arg2.namespace = this.NAMESPACE;
				break;
			case 8:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Description";
				arg2.namespace = this.NAMESPACE;
				break;
			case 9:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Email1";
				arg2.namespace = this.NAMESPACE;
				break;
			case 10:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Email2";
				arg2.namespace = this.NAMESPACE;
				break;
			case 11:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Email3";
				arg2.namespace = this.NAMESPACE;
				break;
			case 12:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "FirstName";
				arg2.namespace = this.NAMESPACE;
				break;
			case 13:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "GpsLatitude";
				arg2.namespace = this.NAMESPACE;
				break;
			case 14:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "GpsLongitude";
				arg2.namespace = this.NAMESPACE;
				break;
			case 15:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "HomePhone";
				arg2.namespace = this.NAMESPACE;
				break;
			case 16:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "IContactID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 17:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "JobTitle";
				arg2.namespace = this.NAMESPACE;
				break;
			case 18:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "LastName";
				arg2.namespace = this.NAMESPACE;
				break;
			case 19:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "MailingCity";
				arg2.namespace = this.NAMESPACE;
				break;
			case 20:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "MailingCountry";
				arg2.namespace = this.NAMESPACE;
				break;
			case 21:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "MailingState";
				arg2.namespace = this.NAMESPACE;
				break;
			case 22:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "MailingStreet";
				arg2.namespace = this.NAMESPACE;
				break;
			case 23:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "MailingZip";
				arg2.namespace = this.NAMESPACE;
				break;
			case 24:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Mobile";
				arg2.namespace = this.NAMESPACE;
				break;
			case 25:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ModifiedDate";
				if (modified_timestamp == null || modified_timestamp.equals("")) {
					arg2.namespace = this.W3CNAMESPACE;
				} else {
					arg2.namespace = this.NAMESPACE;
				}
				break;
			case 26:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "OtherPhone";
				arg2.namespace = this.NAMESPACE;
				break;
			case 27:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Owner";
				arg2.namespace = this.NAMESPACE;
				break;
			case 28:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Prefix";
				arg2.namespace = this.NAMESPACE;
				break;
			case 29:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "SkypeID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 30:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "UpdateGpsLocation";
				arg2.namespace = this.NAMESPACE;
				break;
			case 31:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "WorkFax";
				arg2.namespace = this.NAMESPACE;
				break;
			case 32:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "WorkPhone";
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
				assistant_name = arg1.toString();
				break;
			case 2:
				assistant_phone = arg1.toString();
				break;
			case 3:
				birthday = arg1.toString();
				break;
			case 4:
				company_name = arg1.toString();
				break;
			case 5:
				company_id = arg1.toString();
				break;
			case 6:
				contact_id = arg1.toString();
				break;
			case 7:
				department = arg1.toString();
				break;
			case 8:
				description = arg1.toString();
				break;
			case 9:
				email1 = arg1.toString();
				break;
			case 10:
				email2 = arg1.toString();
				break;
			case 11:
				email3 = arg1.toString();
				break;
			case 12:
				first_name = arg1.toString();
				break;
			case 13:
				gps_lat = arg1.toString();
				break;
			case 14:
				gps_long = arg1.toString();
				break;
			case 15:
				home_phone = arg1.toString();
				break;
			case 16:
				internal_num = arg1.toString();
				break;
			case 17:
				job_title = arg1.toString();
				break;
			case 18:
				last_name = arg1.toString();
				break;
			case 19:
				mailing_city = arg1.toString();
				break;
			case 20:
				mailing_country = arg1.toString();
				break;
			case 21:
				mailing_state = arg1.toString();
				break;
			case 22:
				mailing_street = arg1.toString();
				break;
			case 23:
				mailing_zip = arg1.toString();
				break;
			case 24:
				mobile = arg1.toString();
				break;
			case 25:
				modified_timestamp = arg1.toString();
				break;
			case 26:
				other_phone = arg1.toString();
				break;
			case 27:
				owner = arg1.toString();
				break;
			case 28:
				prefix = arg1.toString();
				break;
			case 29:
				skype_id = arg1.toString();
				break;
			case 30:
				update_gps_location = arg1.toString();
				break;
			case 31:
				work_fax = arg1.toString();
				break;
			case 32:
				work_phone = arg1.toString();
				break;
			default:
				break;
		}
	}
}
