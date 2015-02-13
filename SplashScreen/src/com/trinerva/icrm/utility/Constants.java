package com.trinerva.icrm.utility;

import java.util.ArrayList;
import java.util.List;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.ConfigInfo;
import com.trinerva.icrm.object.MasterInfo;

public class Constants {
	public static final String WEB_SERVER = "CRM_SERVER_WEB";
	public static final String MOBILE_SERVER = "CRM_SERVER_MOBILE";
	public static final String CACHE_FOLDER = ".FIRSTLINK";
	public static final boolean DEBUG = false;
	public static DatabaseHandler DBHANDLER = null;
	public static ArrayList<ConfigInfo> CONFIG_LIST = null;
	public static List<MasterInfo> MASTER_PREFIX_LIST = null;
	public static List<MasterInfo> MASTER_LEADATTITUDE_LIST = null;
	public static List<MasterInfo> MASTER_LEADSTATUS_LIST = null;
	public static List<MasterInfo> MASTER_LEADSOURCE_LIST = null;
	public static List<MasterInfo> MASTER_CALENDARPRIORITY_LIST = null;
	public static List<MasterInfo> MASTER_CALENDARAVAILABILITY_LIST = null;
	public static List<MasterInfo> MASTER_TASKPRIORITY_LIST = null;
	public static List<MasterInfo> MASTER_TASKAVAILABILITY_LIST = null;
	public static List<MasterInfo> MASTER_TASKSTATUS_LIST = null;
	public static List<MasterInfo> MASTER_TASKS_LIST = null;
	public static List<MasterInfo> MASTER_USERLIST_LIST = null;
	public static List<MasterInfo> MASTER_OPPORTUNITYSTAGE_LIST = null;
	public static List<MasterInfo> MASTER_TASKKIND_LIST = null;
	public static List<MasterInfo> MASTER_LEADINDUSTRY_LIST = null;
	public static List<MasterInfo> MASTER_CATEGORY_LIST = null;

	public static String MASTER_PREFIX = "Prefix";
	public static String MASTER_COMPANY = "Company";
	public static String MASTER_OPPORTUNITY_STAGE = "OpportunityStage";
	public static String MASTER_LEAD_ATTITUDE = "LeadAttitude";
	public static String MASTER_LEAD_STATUS = "LeadStatus";
	public static String MASTER_LEAD_SOURCE = "LeadSource";
	public static String MASTER_LEAD_INDUSTRY = "LeadIndustry";
	public static String MASTER_CALENDAR_PRIORITY = "CalendarPriority";
	public static String MASTER_CALENDAR_AVAILABILITY = "CalendarAvailability";
	public static String MASTER_CALENDAR_CATEGORY = "eventcategory";
	public static String MASTER_TASK_PRIORITY = "TaskPriority";
	public static String MASTER_TASK_AVAILABILITY = "TaskAvailability";
	public static String MASTER_TASK_STATUS = "TaskStatus";
	public static String MASTER_TASK_USER_LIST = "UserList";

	public static String ACTION_CALL = "2";
	public static String ACTION_EMAIL = "13";
	public static String ACTION_SMS = "12";
	public static String PERSON_TYPE_LEAD = "LEAD";
	public static String PERSON_TYPE_CONTACT = "CONTACT";

	//DATE TIME FORMAT
	public static String DATE_FORMAT = "dd-MM-yyyy";
	public static String DATETIMESEC_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static String XML_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static String XML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	public static String XML_DATETIMEMICRO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SS";
	public static String DEFAULT_DATE = "1900-01-01T00:00:00";

	public static String SALES_EMAIL = "sales@firstlink.asia";
	public static String SALES_SUBJECT = "Sales Enquiry of FirstLink CRM Android";
	public static String SUPPORT_EMAIL = "support@firstlink.asia";
	public static String SUPPORT_SUBJECT = "Support of FirstLink CRM Android";

	//Subject and description for activity log.
	public static String CALL_SUBJECT = "CALL";
	public static String CALL_DESCRIPTION = "CALL TO THIS CONTACT";

	public static String EMAIL_SUBJECT = "EMAIL";
	public static String EMAIL_DESCRIPTION = "EMAIL TO THIS CONTACT";

	public static String SMS_SUBJECT = "SMS";
	public static String SMS_DESCRIPTION = "SMS TO THIS CONTACT";

	public static String REPORT_TYPE_OPPORTUNITY = "OPPORTUNITY";
	public static String REPORT_TYPE_ACTIVITY = "ACTIVITY";

	public static int IMPORT_CONTACT = 1;
	public static int IMPORT_LEAD = 2;

	public static String PHONE_PIN_ENABLED = "PHONE_PIN_ENABLED";
	public static String PHONE_PIN_VALUE = "PHONE_PIN_VALUE";
	public static String USER_EMAIL = "USER_EMAIL";
	public static String USER_DEF1 = "USER_DEF1";
	
	
	public static String DELETE_ALL = "DeleteALL";
	public static String DELETE_CONTACT = "DeleteContact";
	public static String DELETE_EVENT = "DeleteEvent";
	public static String DELETE_LEAD = "DeleteLead";
	public static String DELETE_OPPORTUNITY = "DeleteOpportunity";
	public static String DELETE_TASK = "DeleteTask";
	
	public static String IS_CONTACT_NAME_TO = "IS_CONTACT_NAME_TO";
	public static String NAME_TO_ID = "IS_CONTACT_NAME_TO_ID";
}
