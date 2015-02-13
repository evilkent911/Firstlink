package com.trinerva.icrm.object;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.Utility;

public class CalendarDetail implements KvmSerializable {
	private String NAMESPACE = "http://schemas.datacontract.org/2004/07/TACRM.DataContract.Mobile";
	private String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	private String internal_num;
	private String calendar_id;
	private String subject;
	private String start_date;
	private String end_date;
	private String priority;
	private String location;
	private String all_day;
	private String availability;
	private String invitees;
	private String alert;
	private String email_notification;
	private String sms_notification;
	private String event_type;
	private String owner;
	private String active;
	private String description;
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
	private String is_private;
	private String is_update;
	private String eventNameTo = "0";
	private String nameToId;
	String nameToName;
	String category;
	String scheduleTime;
	String emailSchedule1;
	String emailSchedule2;
	String emailSchedule3;

	public CalendarDetail() {
	}

	public CalendarDetail(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getInternalNum() {
		return internal_num;
	}

	public String getCalendarId() {
		return calendar_id;
	}

	public String getSubject() {
		return subject;
	}

	public String getEmailSchedule1() {
		return emailSchedule1;
	}

	public String getEmailSchedule2() {
		return emailSchedule2;
	}

	public String getEmailSchedule3() {
		return emailSchedule3;
	}

	public String getStartDate() {
		return start_date;
	}

	public String getEndDate() {
		return end_date;
	}

	public String getPriority() {
		return priority;
	}

	public String getLocation() {
		return location;
	}

	public String getAllDay() {
		return all_day;
	}

	public String getAvailability() {
		return availability;
	}

	public String getInvitees() {
		return invitees;
	}

	public String getAlert() {
		return alert;
	}

	public String getEmailNotification() {
		return email_notification;
	}

	public String getSmsNotification() {
		return sms_notification;
	}

	public String getEventType() {
		return event_type;
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

	public String getIsPrivate() {
		return is_private;
	}

	public String getIsUpdate() {
		return is_update;
	}

	public String getEventNameTo() {
		return eventNameTo;
	}

	public String getNameToId() {
		return nameToId;
	}

	public String getNameToName() {
		return nameToName;
	}
	
	public String getCategory() {
		return category;
	}

	public void setInternalNum(String internal_num) {
		this.internal_num = internal_num;
	}

	public void setCalendarId(String calendar_id) {
		this.calendar_id = calendar_id;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setEmailSchedule1(String emailSchedule1) {
		this.emailSchedule1 = emailSchedule1;
	}

	public void setEmailSchedule2(String emailSchedule2) {
		this.emailSchedule2 = emailSchedule2;
	}

	public void setEmailSchedule3(String emailSchedule3) {
		this.emailSchedule3 = emailSchedule3;
	}

	public void setStartDate(String start_date) {
		this.start_date = start_date;
	}

	public void setEndDate(String end_date) {
		this.end_date = end_date;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setAllDay(String all_day) {
		this.all_day = all_day;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public void setInvitees(String invitees) {
		this.invitees = invitees;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public void setEmailNotification(String email_notification) {
		this.email_notification = email_notification;
	}

	public void setSmsNotification(String sms_notification) {
		this.sms_notification = sms_notification;
	}

	public void setEventType(String event_type) {
		this.event_type = event_type;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setIsPrivate(String is_private) {
		this.is_private = is_private;
	}

	public void setIsUpdate(String is_update) {
		this.is_update = is_update;
	}

	public void setEventNameTo(String eventNameTo) {
		this.eventNameTo = eventNameTo;
	}

	public void setNameToId(String nameToId) {
		this.nameToId = nameToId;
	}	
	
	public void setNameToName(String nameToName) {
		this.nameToName = nameToName;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String toString() {
		return "{" + internal_num + " , " + calendar_id + " , " + subject
				+ " , " + start_date + " , " + end_date + " , " + priority
				+ " , " + location + " , " + all_day + " , " + availability
				+ " , " + invitees + " , " + alert + " , " + email_notification
				+ " , " + sms_notification + " , " + event_type + " , " + owner
				+ " , " + active + " , " + description + " , " + user_def1
				+ " , " + user_def2 + " , " + user_def3 + " , " + user_def4
				+ " , " + user_def5 + " , " + user_def6 + " , " + user_def7
				+ " , " + user_def8 + " , " + user_stamp + " , "
				+ created_timestamp + " , " + modified_timestamp + " , "
				+ is_private + " , " + is_update + "}";
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return active;
		case 1:
			// String strAlert = Utility.toXmlDate(alert,
			// Constants.DATETIME_FORMAT);
			// return (strAlert == null ? Constants.DEFAULT_DATE : strAlert);
			return Utility.toXmlDate(alert, Constants.DATETIME_FORMAT);
		case 2:
			return all_day;
		case 3:
			return availability;
		case 4:
			return calendar_id;
		case 5:
			return category;
		case 6:
			return user_def3;
		case 7:
//			System.out.println("created_timestamp = "+created_timestamp);
//			System.out.println("lol = "+Utility.toXmlDate(created_timestamp, Constants.DATETIME_FORMAT));
			return Utility.toXmlDate(created_timestamp, Constants.DATETIME_FORMAT);
		case 8:
			return description;
		case 9:
			return (email_notification == null) ? "false" : email_notification;
		case 10:
			if (Utility.toXmlDate(emailSchedule1, Constants.DATETIME_FORMAT) == null) {
				return emailSchedule1;
			}
			return Utility.toXmlDate(emailSchedule1, Constants.DATETIME_FORMAT);
		case 11:
			if (Utility.toXmlDate(emailSchedule2, Constants.DATETIME_FORMAT) == null) {
				return emailSchedule2;
			}
			return Utility.toXmlDate(emailSchedule2, Constants.DATETIME_FORMAT);
		case 12:
			if (Utility.toXmlDate(emailSchedule3, Constants.DATETIME_FORMAT) == null) {
				return emailSchedule3;
			}
			return Utility.toXmlDate(emailSchedule3, Constants.DATETIME_FORMAT);
		case 13:
			return Utility.toXmlDate(end_date, Constants.DATETIME_FORMAT);
		case 14:
			return eventNameTo;
		case 15:
			return internal_num;
		case 16:
			if (invitees != null && invitees.length() > 1) {
				return invitees;
			} else {
				return null;
			}
		case 17:
			return is_private;
		case 18:
			return location;
		case 19:
			return Utility.toXmlDate(modified_timestamp,
					Constants.DATETIMESEC_FORMAT);
			// return modified_timestamp;
		case 20:
			return nameToId;
		case 21:
			return owner;
		case 22:
			return priority;
		case 23:
			return (sms_notification == null) ? "false" : sms_notification;
		case 24:
			return Utility.toXmlDate(start_date, Constants.DATETIME_FORMAT);
		case 25:
			return user_def7;
		case 26:
			return subject;

		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 27;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch (arg0) {
		case 0:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Active";
			arg2.namespace = this.NAMESPACE;
			break;
		case 1:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Alert";
			if (alert == null || alert.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 2:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "AllDay";
			arg2.namespace = this.NAMESPACE;
			break;
		case 3:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Availability";
			arg2.namespace = this.NAMESPACE;
			break;
		case 4:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "CalendarID";
			arg2.namespace = this.NAMESPACE;
			break;
		case 5:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Category";
			arg2.namespace = this.NAMESPACE;
			break;
		case 6:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Comment";
			arg2.namespace = this.NAMESPACE;
			break;
		case 7:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "CreatedDate";
			arg2.namespace = this.NAMESPACE;
			break;
		case 8:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Description";
			arg2.namespace = this.NAMESPACE;
			break;
		case 9:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "EmailNotification";
			arg2.namespace = this.NAMESPACE;
			break;
		case 10:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "EmailSchedule1";
			if (emailSchedule1 == null || emailSchedule1.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 11:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "EmailSchedule2";
			if (emailSchedule2 == null || emailSchedule2.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 12:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "EmailSchedule3";
			if (emailSchedule3 == null || emailSchedule3.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 13:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "EndDateTime";
			if (end_date == null || end_date.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 14:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "EventNameTo";
			arg2.namespace = this.NAMESPACE;
			break;
		case 15:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "ICalendarID";
			arg2.namespace = this.NAMESPACE;
			break;
		case 16:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Invitees";
			arg2.namespace = this.NAMESPACE;
			break;
		case 17:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "IsPrivate";
			arg2.namespace = this.NAMESPACE;
			break;
		case 18:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Location";
			arg2.namespace = this.NAMESPACE;
			break;
		case 19:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "ModifiedDate";
			if (modified_timestamp == null || modified_timestamp.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 20:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "NameToID";
			arg2.namespace = this.NAMESPACE;
			break;
		case 21:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Owner";
			arg2.namespace = this.NAMESPACE;
			break;
		case 22:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Priority";
			arg2.namespace = this.NAMESPACE;
			break;
		case 23:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "SMSNotification";
			arg2.namespace = this.NAMESPACE;
			break;
		case 24:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "StartDateTime";
			if (start_date == null || start_date.equals("")) {
				arg2.namespace = this.W3CNAMESPACE;
			} else {
				arg2.namespace = this.NAMESPACE;
			}
			break;
		case 25:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Status";
			arg2.namespace = this.NAMESPACE;
			break;
		case 26:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "Subject";
			arg2.namespace = this.NAMESPACE;
			break;

		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch (arg0) {
		case 0:
			active = arg1.toString();
			break;
		case 1:
			alert = arg1.toString();
			break;
		case 2:
			all_day = arg1.toString();
			break;
		case 3:
			availability = arg1.toString();
			break;
		case 4:
			calendar_id = arg1.toString();
			break;
		case 5:
			category = arg1.toString();
			break;
		case 6:
			user_def1 = arg1.toString();
			break;
		case 7:
			created_timestamp = arg1.toString();
			break;
		case 8:
			description = arg1.toString();
			break;
		case 9:
			email_notification = arg1.toString();
			break;
		case 10:
			emailSchedule1 = arg1.toString();
		case 11:
			emailSchedule2 = arg1.toString();
		case 12:
			emailSchedule3 = arg1.toString();
			break;
		case 13:
			end_date = arg1.toString();
			break;
		case 14:
			eventNameTo = arg1.toString();
			break;
		case 15:
			internal_num = arg1.toString();
			break;
		case 16:
			invitees = arg1.toString();
			break;
		case 17:
			is_private = arg1.toString();
			break;
		case 18:
			location = arg1.toString();
			break;
		case 19:
			modified_timestamp = arg1.toString();
			break;
		case 20:
			nameToId = arg1.toString();
			break;
		case 21:
			owner = arg1.toString();
			break;
		case 22:
			priority = arg1.toString();
			break;
		case 23:
			sms_notification = arg1.toString();
			break;
		case 24:
			start_date = arg1.toString();
			break;
		case 25:
			user_def7 = arg1.toString();
			break;
		case 26:
			subject = arg1.toString();
			break;
		default:
			break;
		}
	}

	private void add(String string) {
		// TODO Auto-generated method stub

	}

}