package com.trinerva.icrm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "FirstLink";

	// Contacts table name
	public static final String TABLE_FL_CONTACT = "FL_CONTACT";
	public static final String TABLE_FL_OPPORTUNITY = "FL_OPPORTUNITY";
	public static final String TABLE_FL_LEAD = "FL_LEAD";
	public static final String TABLE_FL_BROADCAST = "FL_BROADCAST";
	public static final String TABLE_FL_SERVER_RESPONSE = "FL_SERVER_RESPONSE";
	public static final String TABLE_FL_CONFIG = "FL_CONFIG";
	public static final String TABLE_FL_MASTER = "FL_MASTER";
	public static final String TABLE_FL_CALENDAR = "FL_CALENDAR";
	public static final String TABLE_FL_TASK = "FL_TASK";
	public static final String TABLE_FL_ACTIVITIES_LOG = "FL_ACTIVITIES_LOG";
	public static final String TABLE_FL_REPORT = "FL_REPORT";
	public static final String TABLE_FL_ACCOUNT = "FL_ACCOUNT";

	// create table query
	public static final String strCreateContactSql = "CREATE TABLE "
			+ TABLE_FL_CONTACT + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "CONTACT_ID TEXT," + "FIRST_NAME TEXT," + "LAST_NAME TEXT,"
			+ "PREFIX TEXT," + "BIRTHDAY DATE," + "COMPANY_NAME TEXT,"
			+ "COMPANY_ID TEXT," + "DEPARTMENT TEXT," + "JOB_TITLE TEXT,"
			+ "MOBILE TEXT," + "HOME_PHONE TEXT," + "WORK_PHONE TEXT,"
			+ "OTHER_PHONE TEXT," + "WORK_FAX TEXT," + "MAILING_CITY TEXT,"
			+ "MAILING_COUNTRY TEXT," + "MAILING_STATE TEXT,"
			+ "MAILING_STREET TEXT," + "MAILING_ZIP TEXT," + "EMAIL1 TEXT,"
			+ "EMAIL2 TEXT," + "EMAIL3 TEXT," + "SKYPE_ID TEXT,"
			+ "ASSISTANT_NAME TEXT," + "ASSISTANT_PHONE TEXT," + "OWNER TEXT,"
			+ "ACTIVE INTEGER," + "DESCRIPTION TEXT," + "GPS_LAT DOUBLE,"
			+ "GPS_LONG DOUBLE," + "UPDATE_GPS_LOCATION NVARCHAR(50),"
			+ "PHOTO TEXT," + "USER_DEF1 NVARCHAR(500),"
			+ "USER_DEF2 NVARCHAR(500)," + "USER_DEF3 NVARCHAR(500),"
			+ "USER_DEF4 NVARCHAR(500)," + "USER_DEF5 NVARCHAR(500),"
			+ "USER_DEF6 NVARCHAR(500)," + "USER_DEF7 DOUBLE,"
			+ "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP,"
			+ "IS_UPDATE NVARCHAR(20)" + ");";

	public static final String strCreateOpportunitySql = "CREATE TABLE "
			+ TABLE_FL_OPPORTUNITY + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "CONTACT_INTERNAL_NUM INTEGER," + "CONTACT_ID TEXT,"
			+ "OPP_ID TEXT," + "OPP_NAME TEXT," + "OPP_DESC TEXT,"
			+ "OPP_AMOUNT TEXT," + "OPP_DATE DATE," + "OPP_STAGE TEXT,"
			+ "ACTIVE INTEGER," + "USER_DEF1 NVARCHAR(500),"
			+ "USER_DEF2 NVARCHAR(500)," + "USER_DEF3 NVARCHAR(500),"
			+ "USER_DEF4 NVARCHAR(500)," + "USER_DEF5 NVARCHAR(500),"
			+ "USER_DEF6 NVARCHAR(500)," + "USER_DEF7 DOUBLE,"
			+ "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP DATETIME," + "MODIFIED_TIMESTAMP DATETIME,"
			+ "IS_UPDATE NVARCHAR(20)" + ");";

	public static final String strCreateLeadSql = "CREATE TABLE "
			+ TABLE_FL_LEAD + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "LEAD_ID TEXT," + "FIRST_NAME TEXT," + "LAST_NAME TEXT,"
			+ "PREFIX TEXT," + "LEAD_SOURCE TEXT," + "LEAD_STATUS TEXT,"
			+ "ATTITUDE TEXT," + "ANNUAL_REVENUE TEXT," + "BIRTHDAY TEXT,"
			+ "COMPANY_NAME TEXT," + "JOB_TITLE TEXT," + "MOBILE TEXT,"
			+ "WORK_PHONE TEXT," + "WORK_FAX TEXT," + "MAILING_CITY TEXT,"
			+ "MAILING_COUNTRY TEXT," + "MAILING_STATE TEXT,"
			+ "MAILING_STREET TEXT," + "MAILING_ZIP TEXT," + "EMAIL1 TEXT,"
			+ "EMAIL2 TEXT," + "EMAIL3 TEXT,"+ "SKYPE_ID TEXT,"
			+ "WEBSITE TEXT," + "OWNER TEXT," + "NO_OF_EMPLOYEE TEXT,"
			+ "INDUSTRY TEXT," + "ACTIVE INTEGER," + "DESCRIPTION TEXT,"
			+ "GPS_LAT DOUBLE," + "GPS_LONG DOUBLE,"
			+ "UPDATE_GPS_LOCATION NVARCHAR(50)," + "PHOTO TEXT,"
			+ "USER_DEF1 NVARCHAR(500)," + "USER_DEF2 NVARCHAR(500),"
			+ "USER_DEF3 NVARCHAR(500)," + "USER_DEF4 NVARCHAR(500),"
			+ "USER_DEF5 NVARCHAR(500)," + "USER_DEF6 NVARCHAR(500),"
			+ "USER_DEF7 DOUBLE," + "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP,"
			+ "IS_UPDATE NVARCHAR(20)" + ");";

	public static final String strCreateBroadcastSql = "CREATE TABLE "
			+ TABLE_FL_BROADCAST + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "SUBJECT TEXT," + "RELEASED_BY TEXT," + "RELEASED_DATE DATETIME,"
			+ "BROADCAST_TYPE TEXT," + "BROADCAST_ID TEXT,"+ "BROADCAST_CONTENT TEXT,"
			+ "USER_DEF1 NVARCHAR(500)," + "USER_DEF2 NVARCHAR(500),"
			+ "USER_DEF3 NVARCHAR(500)," + "USER_DEF4 NVARCHAR(500),"
			+ "USER_DEF5 NVARCHAR(500)," + "USER_DEF6 NVARCHAR(500),"
			+ "USER_DEF7 DOUBLE," + "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP"
			+ ");";

	public static final String strCreateServerResponseSql = "CREATE TABLE "
			+ TABLE_FL_SERVER_RESPONSE + " ("
			+ "INTERNAL_NUM INTEGER PRIMARY KEY," + "REQUEST_TYPE TEXT,"
			+ "REQUEST_TEXT TEXT," + "RESPONSE_TEXT TEXT,"
			+ "RETURN_CODE TEXT," + "RETURN_MESSAGE TEXT,"
			+ "USER_DEF1 NVARCHAR(500)," + "USER_DEF2 NVARCHAR(500),"
			+ "USER_DEF3 NVARCHAR(500)," + "USER_DEF4 NVARCHAR(500),"
			+ "USER_DEF5 NVARCHAR(500)," + "USER_DEF6 NVARCHAR(500),"
			+ "USER_DEF7 DOUBLE," + "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP"
			+ ");";

	public static final String strCreateConfigSql = "CREATE TABLE "
			+ TABLE_FL_CONFIG + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "CONFIG_TYPE TEXT," + "CONFIG_VALUE TEXT," + "CONFIG_TEXT TEXT,"
			+ "SEQUENCE INTEGER," + "USER_DEF1 NVARCHAR(500),"
			+ "USER_DEF2 NVARCHAR(500)," + "USER_DEF3 NVARCHAR(500),"
			+ "USER_DEF4 NVARCHAR(500)," + "USER_DEF5 NVARCHAR(500),"
			+ "USER_DEF6 NVARCHAR(500)," + "USER_DEF7 DOUBLE,"
			+ "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP"
			+ ");";

	public static final String strCreateMasterSql = "CREATE TABLE "
			+ TABLE_FL_MASTER + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "MASTER_TYPE TEXT," + "MASTER_VALUE TEXT," + "MASTER_TEXT TEXT,"
			+ "SEQUENCE INTEGER," + "USER_DEF1 NVARCHAR(500),"
			+ "USER_DEF2 NVARCHAR(500)," + "USER_DEF3 NVARCHAR(500),"
			+ "USER_DEF4 NVARCHAR(500)," + "USER_DEF5 NVARCHAR(500),"
			+ "USER_DEF6 NVARCHAR(500)," + "USER_DEF7 DOUBLE,"
			+ "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP"
			+ ");";
	
	public static final String strCreateAccountSql = "CREATE TABLE "
			+ TABLE_FL_ACCOUNT + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "ACCOUNT_ID TEXT," + "ACCOUNT_NAME TEXT," + "ACTIVE TEXT,"+ "OWNER TEXT,"
			+ "SEQUENCE INTEGER," + "USER_DEF1 NVARCHAR(500),"
			+ "USER_DEF2 NVARCHAR(500)," + "USER_DEF3 NVARCHAR(500),"
			+ "USER_DEF4 NVARCHAR(500)," + "USER_DEF5 NVARCHAR(500),"
			+ "USER_DEF6 NVARCHAR(500)," + "USER_DEF7 DOUBLE,"
			+ "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP"
			+ ");";
	/*
	 * public static final String strCreateCalendarSql = "CREATE TABLE " +
	 * TABLE_FL_CALENDAR + " ("+ "INTERNAL_NUM INTEGER PRIMARY KEY,"+
	 * "CALENDAR_ID TEXT,"+ "SUBJECT TEXT,"+ "START_DATE DATETIME,"+
	 * "END_DATE DATETIME,"+ "PRIORITY INTEGER,"+ "LOCATION TEXT,"+
	 * "ALL_DAY INTEGER,"+ "AVAILABILITY INTEGER,"+ "INVITEES TEXT,"+
	 * "ALERT DATETIME,"+ "EMAIL_NOTIFICATION INTEGER,"+
	 * "SMS_NOTIFICATION INTEGER,"+ "EVENT_TYPE TEXT,"+ "OWNER TEXT,"+
	 * "ACTIVE INTEGER,"+ "DESCRIPTION TEXT,"+ "USER_DEF1 NVARCHAR(500),"+
	 * "USER_DEF2 NVARCHAR(500),"+ "USER_DEF3 NVARCHAR(500),"+
	 * "USER_DEF4 NVARCHAR(500),"+ "USER_DEF5 NVARCHAR(500),"+
	 * "USER_DEF6 NVARCHAR(500),"+ "USER_DEF7 DOUBLE,"+ "USER_DEF8 DOUBLE,"+
	 * "USER_STAMP TEXT,"+ "CREATED_TIMESTAMP TIMESTAMP,"+
	 * "MODIFIED_TIMESTAMP TIMESTAMP,"+ "IS_PRIVATE NVARCHAR(50),"+
	 * "IS_UPDATE NVARCHAR(20)"+ ");";
	 */

	public static final String strCreateCalendarSql = "CREATE TABLE "
			+ TABLE_FL_CALENDAR + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "CALENDAR_ID TEXT," + "SUBJECT TEXT," + "START_DATE DATETIME,"
			+ "END_DATE DATETIME," + "PRIORITY TEXT," + "LOCATION TEXT,"
			+ "ALL_DAY TEXT," + "AVAILABILITY TEXT," + "INVITEES TEXT,"
			+ "ALERT DATETIME," + "EMAIL_NOTIFICATION TEXT,"
			+ "SMS_NOTIFICATION TEXT," + "EVENT_TYPE TEXT," + "OWNER TEXT,"
			+ "ACTIVE INTEGER," + "DESCRIPTION TEXT," + "NAME_TO TEXT,"+ "EVENT_NAME_TO_ID TEXT,"+ "NAME_TO_NAME TEXT,"+"EMAIL_SCHEDULE1 TEXT," +"EMAIL_SCHEDULE2 TEXT," +"EMAIL_SCHEDULE3 TEXT,"+"CATEGORY TEXT,"
			+ "USER_DEF1 NVARCHAR(500)," + "USER_DEF2 NVARCHAR(500),"
			+ "USER_DEF3 NVARCHAR(500)," + "USER_DEF4 NVARCHAR(500),"
			+ "USER_DEF5 NVARCHAR(500)," + "USER_DEF6 NVARCHAR(500),"
			+ "USER_DEF7 DOUBLE," + "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP,"
			+ "IS_PRIVATE NVARCHAR(50)," + "IS_UPDATE NVARCHAR(20)" + ");";

	public static final String strCreateTaskSql = "CREATE TABLE "
			+ TABLE_FL_TASK + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "TASK_ID TEXT," + "SUBJECT TEXT," + "DUE_DATE DATETIME,"
			+ "PRIORITY INTEGER," + "AVAILABILITY INTEGER,"
			+ "ASSIGNED_TO TEXT," + "ALERT DATETIME,"
			+ "EMAIL_NOTIFICATION INTEGER," +"EMAIL_SCHEDULE1 TEXT," +"EMAIL_SCHEDULE2 TEXT," +"EMAIL_SCHEDULE3 TEXT," +"TASK_KIND TEXT,"  + "SMS_NOTIFICATION INTEGER,"
			+ "TASK_TYPE TEXT," + "OWNER TEXT," + "ACTIVE INTEGER,"
			+ "DESCRIPTION TEXT,"+ "NAME_TO TEXT," + "TASK_NAME_TO_ID TEXT,"+ "NAME_TO_NAME TEXT," + "USER_DEF1 NVARCHAR(500),"
			+ "USER_DEF2 NVARCHAR(500)," + "USER_DEF3 NVARCHAR(500),"
			+ "USER_DEF4 NVARCHAR(500)," + "USER_DEF5 NVARCHAR(500),"
			+ "USER_DEF6 NVARCHAR(500)," + "USER_DEF7 DOUBLE,"
			+ "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP,"
			+ "IS_PRIVATE NVARCHAR(50)," + "IS_UPDATE NVARCHAR(20)" + ");";

	public static final String strCreateActivityLogSql = "CREATE TABLE "
			+ TABLE_FL_ACTIVITIES_LOG + " ("
			+ "INTERNAL_NUM INTEGER PRIMARY KEY," + "ACT_TYPE TEXT,"
			+ "PERSON_TYPE TEXT," + "CONTACT_ID TEXT," + "CONTACT_NUM INTEGER,"
			+ "FIRST_NAME TEXT," + "LAST_NAME TEXT," + "MOBILE TEXT,"
			+ "EMAIL TEXT," + "OWNER TEXT," + "ACTIVE INTEGER,"
			+ "USER_DEF1 NVARCHAR(25)," + "USER_DEF2 NVARCHAR(25),"
			+ "USER_DEF3 NVARCHAR(25)," + "USER_DEF4 NVARCHAR(25),"
			+ "USER_DEF5 NVARCHAR(25)," + "USER_DEF6 NVARCHAR(25),"
			+ "USER_DEF7 DOUBLE," + "USER_DEF8 DOUBLE," + "USER_STAMP TEXT,"
			+ "CREATED_TIMESTAMP TIMESTAMP," + "MODIFIED_TIMESTAMP TIMESTAMP,"
			+ "IS_UPDATE NVARCHAR(20)" + ");";

	public static final String strCreateReportSql = "CREATE TABLE "
			+ TABLE_FL_REPORT + " (" + "INTERNAL_NUM INTEGER PRIMARY KEY,"
			+ "REPORT_TYPE TEXT," + "REPORT_START_DATE  DATETIME,"
			+ "REPORT_END_DATE  DATETIME," + "REPORT_PERSON NVARCHAR(500),"
			+ "REPORT_VALUE TEXT," + "REPORT_TEXT TEXT,"
			+ "USER_DEF1 NVARCHAR(500)," + "USER_DEF2 NVARCHAR(500),"
			+ "USER_DEF3 NVARCHAR(500)," + "USER_DEF4 NVARCHAR(500),"
			+ "USER_DEF5 NVARCHAR(500)," + "USER_DEF6 NVARCHAR(500),"
			+ "USER_DEF7 DOUBLE," + "USER_DEF8 DOUBLE" + ");";

	// end create table query

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(strCreateContactSql);
		database.execSQL(strCreateOpportunitySql);
		database.execSQL(strCreateLeadSql);
		database.execSQL(strCreateBroadcastSql);
		database.execSQL(strCreateServerResponseSql);
		database.execSQL(strCreateConfigSql);
		database.execSQL(strCreateCalendarSql);
		database.execSQL(strCreateTaskSql);
		database.execSQL(strCreateActivityLogSql);
		database.execSQL(strCreateReportSql);
		database.execSQL(strCreateMasterSql);
		database.execSQL(strCreateAccountSql);

		// initial data
		String strData1 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYSTEM', '', 'USER_EMAIL', datetime('now'), datetime('now'));";

		String strData2 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYSTEM', '0', 'PHOTO_INTERNAL_NUM', datetime('now'), datetime('now'));";

		String strData3 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYSTEM', 'FALSE', 'PHONE_PIN_ENABLED', datetime('now'), datetime('now'));";

		String strData4 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYSTEM', '', 'PHONE_PIN_VALUE', datetime('now'), datetime('now'));";

		// String strData5 = "INSERT INTO " + TABLE_FL_CONFIG +
		// " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"+
		// //"VALUES ('SYSTEM', 'http://www.firstlink.asia:2001/MobileService/crm.svc', 'CRM_SERVER_MOBILE', datetime('now'), datetime('now'));";
		// "VALUES ('SYSTEM', 'http://210.5.42.74:2002/MobileService/crm.svc', 'CRM_SERVER_MOBILE', datetime('now'), datetime('now'));";
		//
		// String strData6 = "INSERT INTO " + TABLE_FL_CONFIG +
		// " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"+
		// //"VALUES ('SYSTEM', 'https://www.firstlink.asia/Login.aspx', 'CRM_SERVER_WEB', datetime('now'), datetime('now'));";
		// "VALUES ('SYSTEM', 'http://210.5.42.72/Login.aspx', 'CRM_SERVER_WEB', datetime('now'), datetime('now'));";

		String strData5 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+
//				 "VALUES ('SYSTEM', 'http://www.firstlink.asia:2002/MobileService/crm.svc', 'CRM_SERVER_MOBILE', datetime('now'), datetime('now'));";
				// "VALUES ('SYSTEM', 'http://103.10.157.59:2001/MobileService/crm.svc', 'CRM_SERVER_MOBILE', datetime('now'), datetime('now'));";
				"VALUES ('SYSTEM', 'http://14.102.150.186:2001/MobileService/crm.svc', 'CRM_SERVER_MOBILE', datetime('now'), datetime('now'));";

		String strData6 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+
//				 "VALUES ('SYSTEM', 'https://www.firstlink.asia/Login.aspx', 'CRM_SERVER_WEB', datetime('now'), datetime('now'));";
				// "VALUES ('SYSTEM', 'http://103.10.157.59/login.aspx', 'CRM_SERVER_WEB', datetime('now'), datetime('now'));";
				"VALUES ('SYSTEM', 'http://14.102.150.186/Login.aspx', 'CRM_SERVER_WEB', datetime('now'), datetime('now'));";

		String strData7 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Broadcast', datetime('now'), datetime('now'));";

		String strData8 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Contact', datetime('now'), datetime('now'));";

		String strData9 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Opportunity', datetime('now'), datetime('now'));";

		String strData10 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Lead', datetime('now'), datetime('now'));";

		String strData11 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Calendar', datetime('now'), datetime('now'));";

		String strData12 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Task', datetime('now'), datetime('now'));";

		String strData13 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Account', datetime('now'), datetime('now'));";
		
		String strData14 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'ContactActivity', datetime('now'), datetime('now'));";

		String strData15 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'LeadActivity', datetime('now'), datetime('now'));";

		String strData16 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Report', datetime('now'), datetime('now'));";

		String strData17 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Report', datetime('now'), datetime('now'));";

		String strData18 = "INSERT INTO "
				+ TABLE_FL_CONFIG
				+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
				+ "VALUES ('SYNCRONIZATION', '2000-01-01T00:00:00.0000+08:00', 'Report', datetime('now'), datetime('now'));";
		/*
		 * String strData18 = "INSERT INTO " + TABLE_FL_CONFIG +
		 * " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
		 * +
		 * "VALUES ('SYSTEM', 'FALSE', 'IPHONE_ADDRESS_BOOK_ENABLED', datetime('now'), datetime('now'));"
		 * ;
		 * 
		 * String strData19 = "INSERT INTO " + TABLE_FL_CONFIG +
		 * " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
		 * +
		 * "VALUES ('SYSTEM', 'FALSE', 'IPHONE_CALENDAR_ENABLED', datetime('now'), datetime('now'));"
		 * ;
		 * 
		 * String strData20 = "INSERT INTO " + TABLE_FL_CONFIG +
		 * " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT, CREATED_TIMESTAMP, MODIFIED_TIMESTAMP)"
		 * +
		 * "VALUES ('SYSTEM', 'FALSE', 'IPHONE_INFO_ENABLED', datetime('now'), datetime('now'));"
		 * ;
		 */

		database.execSQL(strData1);
		database.execSQL(strData2);
		database.execSQL(strData3);
		database.execSQL(strData4);
		database.execSQL(strData5);
		database.execSQL(strData6);
		database.execSQL(strData7);
		database.execSQL(strData8);
		database.execSQL(strData9);
		database.execSQL(strData10);
		database.execSQL(strData11);
		database.execSQL(strData12);
		database.execSQL(strData13);
		database.execSQL(strData14);
		database.execSQL(strData15);
		database.execSQL(strData16);
		database.execSQL(strData17);
		database.execSQL(strData18);
//		 database.execSQL(strData19);
		// database.execSQL(strData20);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_CONTACT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_OPPORTUNITY);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_LEAD);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_BROADCAST);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_SERVER_RESPONSE);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_CONFIG);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_MASTER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_CALENDAR);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_TASK);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_ACTIVITIES_LOG);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_REPORT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_ACCOUNT);

		onCreate(database);
	}

	public void onReset(Context context, SQLiteDatabase database) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_CONTACT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_OPPORTUNITY);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_LEAD);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_BROADCAST);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_SERVER_RESPONSE);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_CONFIG);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_MASTER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_CALENDAR);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_TASK);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_ACTIVITIES_LOG);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_REPORT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FL_ACCOUNT);
		
		onCreate(database);
	}

	public static void onDeletePermission(SQLiteDatabase database,
			String strData) {
		database.execSQL(strData);
	}
}
