package com.trinerva.icrm.object;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class LeadActivity implements KvmSerializable {
	private String NAMESPACE = "http://schemas.datacontract.org/2004/07/TACRM.DataContract.Mobile";
	private String ActivityTime;
	private String ActivityType;
	private String LeadID;
	private String Description;
	private String Subject;
	
	public void setActivityTime(String activity_time) {
		this.ActivityTime = activity_time;
	}
	
	public void setActivityType(String activity_type) {
		this.ActivityType = activity_type;
	}
	
	public void setLeadId(String lead_id) {
		this.LeadID = lead_id;
	}
	
	public void setDescription(String description) {
		this.Description = description;
	}
	
	public void setSubject(String subject) {
		this.Subject = subject;
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
			case 0:
				return ActivityTime;
			case 1:
				return ActivityType;
			case 2:
				return Description;
			case 3:
				return LeadID;
			case 4:
				return Subject;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 5;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0) {
			case 0:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ActivityTime";
				arg2.namespace = this.NAMESPACE;
				break;
			case 1:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "ActivityType";
				arg2.namespace = this.NAMESPACE;
				break;
			case 2:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Description";
				arg2.namespace = this.NAMESPACE;
				break;
			case 3:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "LeadID";
				arg2.namespace = this.NAMESPACE;
				break;
			case 4:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Subject";
				arg2.namespace = this.NAMESPACE;
				break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0) {
			case 0:
				ActivityTime = arg1.toString();
				break;
			case 1:
				ActivityType = arg1.toString();
				break;
			case 2:
				Description = arg1.toString();
				break;
			case 3:
				LeadID = arg1.toString();
				break;
			case 4:
				Subject = arg1.toString();
				break;
			default:
				break;
		}
	}
}