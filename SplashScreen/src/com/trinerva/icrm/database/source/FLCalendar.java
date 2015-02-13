package com.trinerva.icrm.database.source;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.CalendarDetail;
import com.trinerva.icrm.utility.CalendarUtility;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FLCalendar extends BaseSource {
	private String TAG = "FLCalendar";
	private String strDateFormat = "yyyy-MM-dd kk:mm";

	public FLCalendar(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public CalendarDetail getCalendarDetailById(String strId) {
		this.openRead();
		DeviceUtility.log(TAG, "getCalendarDetailById("+strId+")");
		CalendarDetail object = new CalendarDetail();
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CALENDAR,
				null, "INTERNAL_NUM = ?", new String[] {strId}, null, null, null);
		cursor.moveToFirst();
		object = setToObject(cursor);

		DeviceUtility.log(TAG, object.toString());
		cursor.close();
		//this.close();
		return object;
	}

	private CalendarDetail setToObject(Cursor cursor) {
		CalendarDetail calendar = new CalendarDetail();
		calendar.setInternalNum(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
		calendar.setCalendarId(cursor.getString(cursor.getColumnIndex("CALENDAR_ID")));
		calendar.setSubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
		calendar.setCategory(cursor.getString(cursor.getColumnIndex("CATEGORY")));
		calendar.setNameToId(cursor.getString(cursor.getColumnIndex("NAME_TO")));
		calendar.setNameToName(cursor.getString(cursor.getColumnIndex("NAME_TO_NAME")));
		calendar.setStartDate(cursor.getString(cursor.getColumnIndex("START_DATE")));
		calendar.setEndDate(cursor.getString(cursor.getColumnIndex("END_DATE")));
		calendar.setPriority(cursor.getString(cursor.getColumnIndex("PRIORITY")));
		calendar.setLocation(cursor.getString(cursor.getColumnIndex("LOCATION")));
		calendar.setAllDay(cursor.getString(cursor.getColumnIndex("ALL_DAY")));
		calendar.setAvailability(cursor.getString(cursor.getColumnIndex("AVAILABILITY")));
		calendar.setInvitees(cursor.getString(cursor.getColumnIndex("INVITEES")));
		calendar.setAlert(cursor.getString(cursor.getColumnIndex("ALERT")));
		calendar.setEmailNotification(cursor.getString(cursor.getColumnIndex("EMAIL_NOTIFICATION")));
		calendar.setSmsNotification(cursor.getString(cursor.getColumnIndex("SMS_NOTIFICATION")));
		calendar.setEventType(cursor.getString(cursor.getColumnIndex("EVENT_TYPE")));
		calendar.setOwner(cursor.getString(cursor.getColumnIndex("OWNER")));
		calendar.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		calendar.setDescription(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
		calendar.setUserDef1(cursor.getString(cursor.getColumnIndex("USER_DEF1")));
		calendar.setUserDef2(cursor.getString(cursor.getColumnIndex("USER_DEF2")));
		calendar.setUserDef3(cursor.getString(cursor.getColumnIndex("USER_DEF3")));
		calendar.setUserDef4(cursor.getString(cursor.getColumnIndex("USER_DEF4")));
		calendar.setUserDef5(cursor.getString(cursor.getColumnIndex("USER_DEF5")));
		calendar.setUserDef6(cursor.getString(cursor.getColumnIndex("USER_DEF6")));
		calendar.setUserDef7(cursor.getString(cursor.getColumnIndex("USER_DEF7")));
		calendar.setUserDef8(cursor.getString(cursor.getColumnIndex("USER_DEF8")));
		calendar.setUserStamp(cursor.getString(cursor.getColumnIndex("USER_STAMP")));
		System.out.println("create time = "+cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")));
		calendar.setCreatedTimestamp(cursor.getString(cursor.getColumnIndex("CREATED_TIMESTAMP")));
		calendar.setModifiedTimestamp(cursor.getString(cursor.getColumnIndex("MODIFIED_TIMESTAMP")));
		calendar.setIsPrivate(cursor.getString(cursor.getColumnIndex("IS_PRIVATE")));
		calendar.setIsUpdate(cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
		calendar.setEmailSchedule1(cursor.getString(cursor.getColumnIndex("EMAIL_SCHEDULE1")));
		calendar.setEmailSchedule2(cursor.getString(cursor.getColumnIndex("EMAIL_SCHEDULE2")));
		calendar.setEmailSchedule3(cursor.getString(cursor.getColumnIndex("EMAIL_SCHEDULE3")));
		calendar.setEventNameTo(cursor.getString(cursor.getColumnIndex("EVENT_NAME_TO_ID")));
		try {
			calendar.setNameToId(cursor.getString(cursor.getColumnIndex("NAME_TO_ID")));
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return calendar;
	}

	public long insert(Context context, CalendarDetail dInsert) {
		this.openWrite();
		long lResult = -1;

		ContentValues values = new ContentValues();
		//by default should be 1
		values.put("ACTIVE", "0");
		values.put("IS_UPDATE", "false");
		values.put("CREATED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		
		if (dInsert.getModifiedTimestamp()!= null && dInsert.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", dInsert.getModifiedTimestamp());
		}
		
		if (dInsert.getCreatedTimestamp()!= null && dInsert.getCreatedTimestamp().length() > 0) {
			values.put("CREATED_TIMESTAMP", dInsert.getCreatedTimestamp());
		}
		
		if (dInsert.getIsUpdate()!= null && dInsert.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", dInsert.getIsUpdate());
		}
		
		if (dInsert.getActive()!= null && dInsert.getActive().length() > 0) {
			values.put("ACTIVE", dInsert.getActive());
		}
		
		if (dInsert.getCalendarId()!= null && dInsert.getCalendarId().length() > 0) {
			values.put("CALENDAR_ID", dInsert.getCalendarId());
		}
		
		if (dInsert.getSubject()!= null && dInsert.getSubject().length() > 0) {
			values.put("SUBJECT", dInsert.getSubject());
		}
		
		if (dInsert.getCategory()!= null && dInsert.getCategory().length() > 0) {
			values.put("CATEGORY", dInsert.getCategory());
		}
		
		if (dInsert.getEmailSchedule1() != null
				&& dInsert.getEmailSchedule1().length() > 0) {
			values.put("EMAIL_SCHEDULE1", dInsert.getEmailSchedule1());
		}
		
		if (dInsert.getEmailSchedule2() != null
				&& dInsert.getEmailSchedule2().length() > 0) {
			values.put("EMAIL_SCHEDULE2", dInsert.getEmailSchedule2());
		}
		
		if (dInsert.getEmailSchedule3() != null
				&& dInsert.getEmailSchedule3().length() > 0) {
			values.put("EMAIL_SCHEDULE3", dInsert.getEmailSchedule3());
		}
		
		if (dInsert.getNameToId()!= null && dInsert.getNameToId().length() > 0) {
			values.put("NAME_TO", dInsert.getNameToId());
		}
		
		if (dInsert.getEventNameTo()!= null && dInsert.getEventNameTo().length() > 0) {
			values.put("EVENT_NAME_TO_ID", dInsert.getEventNameTo());
		}
		
		if (dInsert.getNameToName()!= null && dInsert.getNameToName().length() > 0) {
			values.put("NAME_TO_NAME", dInsert.getNameToName());
		}

		if (dInsert.getStartDate()!= null && dInsert.getStartDate().length() > 0) {
			values.put("START_DATE", dInsert.getStartDate());
		}
		
		if (dInsert.getEndDate()!= null && dInsert.getEndDate().length() > 0) {
			values.put("END_DATE", dInsert.getEndDate());
		}
		
		if (dInsert.getPriority()!= null && dInsert.getPriority().length() > 0) {
			values.put("PRIORITY", dInsert.getPriority());
		}
		
		if (dInsert.getLocation()!= null && dInsert.getLocation().length() > 0) {
			values.put("LOCATION", dInsert.getLocation());
		}
		
		if (dInsert.getAllDay()!= null && dInsert.getAllDay().length() > 0) {
			values.put("ALL_DAY", dInsert.getAllDay());
		}
		
		if (dInsert.getAvailability()!= null && dInsert.getAvailability().length() > 0) {
			values.put("AVAILABILITY", dInsert.getAvailability());
		}
		
		if (dInsert.getInvitees()!= null && dInsert.getInvitees().length() > 0) {
			values.put("INVITEES", dInsert.getInvitees());
		}
		
		if (dInsert.getAlert()!= null && dInsert.getAlert().length() > 0) {
			values.put("ALERT", dInsert.getAlert());
		}
		
		if (dInsert.getEmailNotification()!= null && dInsert.getEmailNotification().length() > 0) {
			values.put("EMAIL_NOTIFICATION", dInsert.getEmailNotification());
		}
		
		if (dInsert.getSmsNotification()!= null && dInsert.getSmsNotification().length() > 0) {
			values.put("SMS_NOTIFICATION", dInsert.getSmsNotification());
		}
		
		if (dInsert.getEventType()!= null && dInsert.getEventType().length() > 0) {
			values.put("EVENT_TYPE", dInsert.getEventType());
		}
		
		if (dInsert.getOwner()!= null && dInsert.getOwner().length() > 0) {
			values.put("OWNER", dInsert.getOwner());
		}
		
		if (dInsert.getDescription()!= null && dInsert.getDescription().length() > 0) {
			values.put("DESCRIPTION", dInsert.getDescription());
		}
		
		if (dInsert.getUserDef1()!= null && dInsert.getUserDef1().length() > 0) {
			values.put("USER_DEF1", dInsert.getUserDef1());
		}
		
		if (dInsert.getUserDef2()!= null && dInsert.getUserDef2().length() > 0) {
			values.put("USER_DEF2", dInsert.getUserDef2());
		}
		
		if (dInsert.getUserDef3()!= null && dInsert.getUserDef3().length() > 0) {
			values.put("USER_DEF3", dInsert.getUserDef3());
		}
		
		if (dInsert.getUserDef4()!= null && dInsert.getUserDef4().length() > 0) {
			values.put("USER_DEF4", dInsert.getUserDef4());
		}
		
		if (dInsert.getUserDef5()!= null && dInsert.getUserDef5().length() > 0) {
			values.put("USER_DEF5", dInsert.getUserDef5());
		}
		
		if (dInsert.getUserDef6()!= null && dInsert.getUserDef6().length() > 0) {
			values.put("USER_DEF6", dInsert.getUserDef6());
		}
		
		if (dInsert.getUserDef7()!= null && dInsert.getUserDef7().length() > 0) {
			values.put("USER_DEF7", dInsert.getUserDef7());
		}
		
		if (dInsert.getUserDef8()!= null && dInsert.getUserDef8().length() > 0) {
			values.put("USER_DEF8", dInsert.getUserDef8());
		}
		
		if (dInsert.getUserStamp()!= null && dInsert.getUserStamp().length() > 0) {
			values.put("USER_STAMP", dInsert.getUserStamp());
		}
		
		if (dInsert.getIsPrivate()!= null && dInsert.getIsPrivate().length() > 0) {
			values.put("IS_PRIVATE", dInsert.getIsPrivate());
		}
		
		lResult = database.insert(DatabaseHandler.TABLE_FL_CALENDAR, null, values);
		if (lResult != -1) {
			//do insert into the phone calendar.
			writeToPhoneCalendar(context, dInsert, lResult);
		}

		//this.close();
		return lResult;
	}

	//USER_DEF1 : calendar's event id
	//USER_DEF2 : reminder id
	public void writeToPhoneCalendar(Context context, CalendarDetail object, long lInternalNum) {
		CalendarUtility local = new CalendarUtility(context);
		int iAllDay = Boolean.getBoolean(object.getAllDay()) ? 1 : 0;
		DateFormat formatter;
		Date date;
		long lEventId = -1;
		int iUpdateStatus = 0;
		long lReminderId = -1;

		try {
			formatter = new SimpleDateFormat(strDateFormat);
			date = (Date)formatter.parse(object.getStartDate());
			long startTime = date.getTime();
			date = (Date)formatter.parse(object.getEndDate());
			long endTime = date.getTime();
			//calendar id
			String strCalendarId = object.getUserDef1();
			if (strCalendarId == null || strCalendarId.length() == 0) {
				lEventId = local.insertEvent(object.getSubject(), (object.getDescription() == null ? "" : object.getDescription()), startTime, endTime, iAllDay);
				DeviceUtility.log(TAG, "lEventId: " + lEventId);
				//update calendar event id into fl_calendar.
				if (lEventId != -1) {
					iUpdateStatus = updateEventCalendarId(lEventId, lInternalNum);
					DeviceUtility.log(TAG, "updateEventCalendarId: " + iUpdateStatus);
				}
			} else {
				lEventId = Long.parseLong(strCalendarId);
				//update.
				iUpdateStatus = local.updateEvent(lEventId, object.getSubject(), (object.getDescription() == null ? "" : object.getDescription()), startTime, endTime, iAllDay);
				//check is event exist in phone calendar?
				DeviceUtility.log(TAG, "local.updateEvent: " + iUpdateStatus);
				if (iUpdateStatus == -1) {
					//event not found in phone.
					lEventId = local.insertEvent(object.getSubject(), (object.getDescription() == null ? "" : object.getDescription()), startTime, endTime, iAllDay);
					DeviceUtility.log(TAG, "lEventId: " + lEventId);
					if (lEventId != -1) {
						iUpdateStatus = updateEventCalendarId(lEventId, lInternalNum);
						DeviceUtility.log(TAG, "updateEventCalendarId: " + iUpdateStatus);
					}
				}
			}

			if (object.getAlert() != null && object.getAlert().length() > 0 && !object.getAlert().equals(Constants.DEFAULT_DATE)) {
				//insert reminder.
				date = (Date)formatter.parse(object.getAlert());
				long alert = date.getTime();
				long diff = (startTime - alert);
					int minutes = (int) diff/(1000*60);
					String strReminderId = object.getUserDef2();
					if (strReminderId == null || strReminderId.length() == 0) {
						lReminderId = local.addReminder(lEventId, minutes);
						if (lReminderId != -1) {
							iUpdateStatus = updateReminderId(lReminderId, lInternalNum);
							DeviceUtility.log(TAG, "updateReminderId: " + iUpdateStatus);
						}
					} else {
						lReminderId = Long.parseLong(strReminderId);
						iUpdateStatus = local.updateReminderWithReminderId(lEventId, lReminderId, minutes);
						DeviceUtility.log(TAG, "updateReminderId: " + iUpdateStatus);
						if (iUpdateStatus == -1) {
							lReminderId = local.addReminder(lEventId, minutes);
							if (lReminderId != -1) {
								iUpdateStatus = updateReminderId(lReminderId, lInternalNum);
								DeviceUtility.log(TAG, "updateReminderId: " + iUpdateStatus);
							}
						}
					}
					DeviceUtility.log(TAG, "lReminderId: " + lReminderId);
			} else {
				//remove reminder.
				String strReminderId = object.getUserDef2();
				if (strReminderId != null && strReminderId.length() > 0) {
					long lCalendarReminderId = 0;
					try {
						lCalendarReminderId = Long.parseLong(strReminderId);
					} catch (Exception e) {
						
					} finally {
						
					}
					
					if (lCalendarReminderId > 0) {
						int iResult2 = local.deleteReminder(lCalendarReminderId);
						DeviceUtility.log(TAG, "local.deleteReminder: " + iResult2);
					}
				}
			}
		} catch (Exception e) {
			DeviceUtility.log(TAG, "ERROR INSERT INTO PHONE CALENDAR");
		}
	}

	public void removeFromPhoneCalendar(Context context, long lEventId) {
		//get and delete it from local.
		this.openRead();
		CalendarUtility local = new CalendarUtility(context);
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CALENDAR, new String[] {"USER_DEF1", "USER_DEF2"}, "INTERNAL_NUM = ?", new String[] {String.valueOf(lEventId)}, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "removeFromPhoneCalendar: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			String strCalendarEventId = cursor.getString(cursor.getColumnIndex("USER_DEF1"));
			String strCalendarReminderId = cursor.getString(cursor.getColumnIndex("USER_DEF2"));
			
			long lCalendarEventId = 0;
			long lCalendarReminderId = 0;
			
			if (strCalendarEventId != null && strCalendarEventId.length() > 0) {
				try {
					lCalendarEventId = Long.parseLong(strCalendarEventId);
				} catch (Exception e) {
					
				}
			}
			
			if (strCalendarReminderId != null && strCalendarReminderId.length() > 0) {
				try {
					lCalendarReminderId = Long.parseLong(strCalendarReminderId);
				} catch (Exception e) {
					
				} finally {
					
				}
			}
			
			if (lCalendarEventId > 0) {
				int iResult = local.removeEvent(lCalendarEventId);
				DeviceUtility.log(TAG, "local.removeEvent: " + iResult);
			}
			
			if (lCalendarReminderId > 0) {
				int iResult2 = local.deleteReminder(lCalendarReminderId);
				DeviceUtility.log(TAG, "local.deleteReminder: " + iResult2);
			}
			
			cursor.moveToNext();
		}
		cursor.close();
		//this.close();		
	}

	public int updateReminderId(long lReminder, long lFLCalendarId) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("USER_DEF2", String.valueOf(lReminder));
		iResult = database.update(DatabaseHandler.TABLE_FL_CALENDAR, values, "INTERNAL_NUM = ?", new String[] {String.valueOf(lFLCalendarId)});
		//this.close();
		return iResult;
	}

	public int updateEventCalendarId(long lEventId, long lFLCalendarId) {
		System.out.println("add calendar = "+lEventId);
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("USER_DEF1", String.valueOf(lEventId));
		iResult = database.update(DatabaseHandler.TABLE_FL_CALENDAR, values, "INTERNAL_NUM = ?", new String[] {String.valueOf(lFLCalendarId)});
		//this.close();
		return iResult;
	}

	public int update(Context context, CalendarDetail oUpdate) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		
		/*if (oUpdate.getModifiedTimestamp()!= null && oUpdate.getModifiedTimestamp().length() > 0) {
			values.put("MODIFIED_TIMESTAMP", oUpdate.getModifiedTimestamp());
		}*/
		
		if (oUpdate.getIsUpdate()!= null && oUpdate.getIsUpdate().length() > 0) {
			values.put("IS_UPDATE", oUpdate.getIsUpdate());
		}
		
		values.put("CALENDAR_ID", oUpdate.getCalendarId());
		values.put("ACTIVE", oUpdate.getActive());
		values.put("SUBJECT", oUpdate.getSubject());
		values.put("START_DATE", oUpdate.getStartDate());
		values.put("END_DATE", oUpdate.getEndDate());
		values.put("PRIORITY", oUpdate.getPriority());
		values.put("LOCATION", oUpdate.getLocation());
		values.put("ALL_DAY", oUpdate.getAllDay());
		values.put("AVAILABILITY", oUpdate.getAvailability());
		values.put("INVITEES", oUpdate.getInvitees());
		values.put("ALERT", oUpdate.getAlert());
		values.put("EMAIL_NOTIFICATION", oUpdate.getEmailNotification());
		values.put("SMS_NOTIFICATION", oUpdate.getSmsNotification());
		values.put("EVENT_TYPE", oUpdate.getEventType());
		values.put("OWNER", oUpdate.getOwner());
		values.put("DESCRIPTION", oUpdate.getDescription());
		values.put("NAME_TO", oUpdate.getNameToId());
		values.put("EVENT_NAME_TO_ID", oUpdate.getEventNameTo());
		values.put("NAME_TO_NAME", oUpdate.getNameToName());
		values.put("CATEGORY", oUpdate.getCategory());
		
		
		List<String> listData = new ArrayList<String>();
		
				if (oUpdate.getEmailSchedule1() != null) {
					listData.add(oUpdate.getEmailSchedule1());
				}
				if (oUpdate.getEmailSchedule2() != null) {
					listData.add(oUpdate.getEmailSchedule2());
				}
				if (oUpdate.getEmailSchedule3() != null) {
					listData.add(oUpdate.getEmailSchedule3());
				}
				System.out.println("PPP = "+listData.size());
				for (int i = 0; i < listData.size(); i++) {
					System.out.println("prin = "+listData.get(i));
				}
					switch (listData.size()) {
					case 1:
						values.put("EMAIL_SCHEDULE1", listData.get(0));
						values.put("EMAIL_SCHEDULE2", "");
						values.put("EMAIL_SCHEDULE3", "");
						break;
					case 2:
						values.put("EMAIL_SCHEDULE1", listData.get(0));
						values.put("EMAIL_SCHEDULE2", listData.get(1));
						values.put("EMAIL_SCHEDULE3", "");
						break;
					case 3:
						values.put("EMAIL_SCHEDULE1", listData.get(0));
						values.put("EMAIL_SCHEDULE2", listData.get(1));
						values.put("EMAIL_SCHEDULE3", listData.get(2));
						break;
					default:
						values.put("EMAIL_SCHEDULE1", "");
						values.put("EMAIL_SCHEDULE2", "");
						values.put("EMAIL_SCHEDULE3", "");
						break;
					}
					
		
		if (oUpdate.getUserDef1() != null && oUpdate.getUserDef1().length() > 0) {
			values.put("USER_DEF1", oUpdate.getUserDef1());
		}
		
		if (oUpdate.getUserDef2() != null && oUpdate.getUserDef2().length() > 0) {
			values.put("USER_DEF2", oUpdate.getUserDef2());
		}
		
		if (oUpdate.getUserDef3() != null && oUpdate.getUserDef3().length() > 0) {
			values.put("USER_DEF3", oUpdate.getUserDef3());
		}
		
		if (oUpdate.getUserDef4() != null && oUpdate.getUserDef4().length() > 0) {
			values.put("USER_DEF4", oUpdate.getUserDef4());
		}
		
		if (oUpdate.getUserDef5() != null && oUpdate.getUserDef5().length() > 0) {
			values.put("USER_DEF5", oUpdate.getUserDef5());
		}
		
		if (oUpdate.getUserDef6() != null && oUpdate.getUserDef6().length() > 0) {
			values.put("USER_DEF6", oUpdate.getUserDef6());
		}
		
		if (oUpdate.getUserDef7() != null && oUpdate.getUserDef7().length() > 0) {
			values.put("USER_DEF7", oUpdate.getUserDef7());
		}
		
		if (oUpdate.getUserDef8() != null && oUpdate.getUserDef8().length() > 0) {
			values.put("USER_DEF8", oUpdate.getUserDef8());
		}
		
		values.put("USER_STAMP", oUpdate.getUserStamp());
		values.put("IS_PRIVATE", oUpdate.getIsPrivate());

		iResult = database.update(DatabaseHandler.TABLE_FL_CALENDAR, values, "INTERNAL_NUM = ?", new String[] {oUpdate.getInternalNum()});
		if (iResult > 0) {
			CalendarDetail oCalendar = getCalendarDetailById(oUpdate.getInternalNum());
			writeToPhoneCalendar(context, oCalendar, Long.parseLong(oUpdate.getInternalNum()));
		}
		//this.close();
		return iResult;
	}

	public Cursor getCalendarListDisplay() {
		this.openRead();
		DeviceUtility.log(TAG, "getCalendarListDisplay");
		Cursor cursor = database.rawQuery("SELECT cl.INTERNAL_NUM AS _id, cl.INTERNAL_NUM, cl.SUBJECT,cl.ALL_DAY, cl.START_DATE,cl.OWNER,cl.END_DATE, cl.ACTIVE,cl.NAME_TO_NAME, cl.IS_UPDATE,cl.LOCATION,  " +
				"CASE cl.EVENT_NAME_TO_ID  " +
				"WHEN '3' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
				"ELSE  " +
				"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
				"END  " +
				"WHEN '0' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
				"END   " +
				"END AS NAME_TO_LAST_NAME,  " +
				"CASE cl.EVENT_NAME_TO_ID  " +
				"WHEN '3' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
				"END  " +
				"WHEN '0' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
				"END    " +
				"END AS NAME_TO_FIRST_NAME  " +
"FROM  FL_CALENDAR  cl WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY START_DATE DESC",  new String[] {"1", "true"});
		try {
			if(cursor.getCount() != 0){
				cursor.moveToFirst();
				
				DeviceUtility.log(TAG, "getCalendarListDisplay: " + cursor.getCount());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		//this.close();
		return cursor; 
	}
	
	public Cursor getCalendarListDisplay(String date) {
		
		System.out.println("yes = "+date);
		this.openRead();
		DeviceUtility.log(TAG, "getCalendarListDisplay");
		Cursor cursor = database.rawQuery("SELECT cl.INTERNAL_NUM AS _id, cl.INTERNAL_NUM, cl.SUBJECT,cl.ALL_DAY, cl.START_DATE,cl.OWNER,cl.END_DATE, cl.ACTIVE,cl.NAME_TO_NAME,cl.LOCATION, cl.IS_UPDATE, cl.NAME_TO_NAME, " +
				"CASE cl.EVENT_NAME_TO_ID  " +
				"WHEN '3' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
				"ELSE  " +
				"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
				"END  " +
				"WHEN '0' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
				"END   " +
				"END AS NAME_TO_LAST_NAME,  " +
				"CASE cl.EVENT_NAME_TO_ID  " +
				"WHEN '3' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
				"END  " +
				"WHEN '0' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
				"END    " +
				"END AS NAME_TO_FIRST_NAME FROM " +
		DatabaseHandler.TABLE_FL_CALENDAR + " cl  WHERE ? BETWEEN Date(START_DATE) AND Date(END_DATE) AND (ACTIVE <> ? OR IS_UPDATE <> ?) ORDER BY START_DATE ASC", new String[] {date,"1", "true"});
		if(cursor.getCount() != 0)
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getCalendarListDisplay: " + cursor.getCount());
		//this.close();
		return cursor;
	}
	

	public Cursor getCalendarListDisplayByFilter(String strFilter) {
		this.openRead();
		DeviceUtility.log(TAG, "getCalendarListDisplayByFilter("+strFilter+")");
		Cursor cursor = database.rawQuery("SELECT cl.INTERNAL_NUM AS _id, cl.INTERNAL_NUM, cl.SUBJECT,cl.ALL_DAY, cl.START_DATE,cl.OWNER,cl.END_DATE, cl.ACTIVE,cl.LOCATION, cl.IS_UPDATE,  " +
				"CASE cl.EVENT_NAME_TO_ID  " +
				"WHEN '3' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.LAST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3))  " +
				"ELSE  " +
				"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
				"END  " +
				"WHEN '0' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.LAST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT IFNULL(ct.LAST_NAME,cl.NAME_TO_NAME) FROM FL_LEAD ct WHERE ct.LEAD_ID = cl.NAME_TO) " +
				"END   " +
				"END AS NAME_TO_LAST_NAME,  " +
				"CASE cl.EVENT_NAME_TO_ID  " +
				"WHEN '3' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT ct.FIRST_NAME FROM FL_CONTACT ct WHERE ct.CONTACT_ID = cl.NAME_TO) " +
				"END  " +
				"WHEN '0' THEN  " +
				"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  " +
				"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) " +
				"ELSE  " +
				"(SELECT ct.FIRST_NAME FROM FL_LEAD ct WHERE ct.LEAd_ID = cl.NAME_TO) " +
				"END    " +
				"END AS NAME_TO_FIRST_NAME FROM " +
				DatabaseHandler.TABLE_FL_CALENDAR +
				" cl WHERE (ACTIVE <> ? OR IS_UPDATE <> ?) AND (SUBJECT LIKE ?) ORDER BY START_DATE DESC", new String[] {"1", "true", "%"+strFilter+"%"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getCalendarListDisplayByFilter: " + cursor.getCount());
		//this.close();
		return cursor;
	}

//	public Cursor getCalendarListDisplayByFilter(String strFilter,String date) {
//		this.openRead();
//		DeviceUtility.log(TAG, "getCalendarListDisplayByFilter("+strFilter+")");
//		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id, INTERNAL_NUM, SUBJECT, START_DATE, ACTIVE, IS_UPDATE FROM " + DatabaseHandler.TABLE_FL_CALENDAR + " WHERE Date(START_DATE)= ? AND (ACTIVE <> ? OR IS_UPDATE <> ?) AND (SUBJECT LIKE ?) ORDER BY INTERNAL_NUM ASC", new String[] {date,"1", "true", "%"+strFilter+"%"});
//		cursor.moveToFirst();
//		DeviceUtility.log(TAG, "getCalendarListDisplayByFilter: " + cursor.getCount());
//		//this.close();
//		return cursor;
//	}

	public int delete(Context context, String Id) {
		this.openWrite();
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("ACTIVE", "1");
		values.put("IS_UPDATE", "false");
		values.put("MODIFIED_TIMESTAMP", Utility.getDatabaseCurrentDateTime());
		iResult = database.update(DatabaseHandler.TABLE_FL_CALENDAR, values, "INTERNAL_NUM = ?", new String[]{Id});
		if (iResult > 0) {
			//remove it from calendar.
			removeFromPhoneCalendar(context, Long.parseLong(Id));
		}
		//this.close();
		return iResult;
	}

	public ArrayList<CalendarDetail> getUnsyncCalendar() {
		this.openRead();
		ArrayList<CalendarDetail> aCalendar = new ArrayList<CalendarDetail>();
		DeviceUtility.log(TAG, "getUnsyncCalendar");
//		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CALENDAR, null, "IS_UPDATE = ?", new String[] {"false"}, null, null, null);
//		Cursor cursor = database.rawQuery("SELECT *,CASE substr(cl.Name_To,1,1) WHEN '2' THEN (SELECT ct.Contact_ID FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = substr(cl.Name_To,3)) WHEN '3' THEN (SELECT ct.Lead_ID FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = substr(cl.Name_To,3)) END AS NAME_TO_ID,CASE substr(cl.Name_To,1,1)  WHEN '3' THEN '3' ELSE '0' END AS EVENT_NAME_TO FROM FL_CALENDAR cl WHERE IS_UPDATE = ?", new String[] {"false"});
		Cursor cursor = database.rawQuery("SELECT *,CASE cl.EVENT_NAME_TO_ID  "+
"WHEN '3' THEN  "+
"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  "+
"(SELECT ct.CONTACT_ID FROM FL_CONTACT ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) "+ 
"ELSE  cl.NAME_TO END  "+
"WHEN '0' THEN  "+
"CASE WHEN IFNULL(LENGTH(cl.NAME_TO),0) < 10 THEN  "+
"(SELECT ct.LEAD_ID FROM FL_LEAD ct WHERE ct.INTERNAL_NUM = SUBSTR(cl.NAME_TO,3)) "+
"ELSE cl.NAME_TO END   "+
"END AS NAME_TO_ID,"+
"CASE substr(cl.Name_To,1,1) WHEN '3' THEN '3' ELSE '0' "+
"END AS EVENT_NAME_TO "+
"FROM FL_CALENDAR cl WHERE IS_UPDATE = ?", new String[] {"false"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getUnsyncCalendar: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aCalendar.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		//this.close();
		return aCalendar;
	}
	
	public int isCalendarExist(String strCalendarId) {
		this.openRead();
		DeviceUtility.log(TAG, "isCalendarExist");
		int iInternal = -1;
		if (strCalendarId.length() > 0) {
			Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CALENDAR, new String[] {"INTERNAL_NUM"}, "CALENDAR_ID = ?", new String[] {strCalendarId}, null, null, null);
			cursor.moveToFirst();
			DeviceUtility.log(TAG, "isCalendarExist: " + cursor.getCount());
			while (!cursor.isAfterLast()) {
				iInternal = cursor.getInt(cursor.getColumnIndex("INTERNAL_NUM"));
				cursor.moveToNext();
			}
			cursor.close();
		}
		
		//this.close();
		return iInternal;
	}
	
	public void insertUpdate(Context context, ArrayList<CalendarDetail> calendar) {
		if (calendar != null) {
			int iCalendarCount = calendar.size();
			for (int i = 0; i < iCalendarCount; i++) {
				int iInternal = isCalendarExist(calendar.get(i).getCalendarId());
				if (iInternal > 0) {
					CalendarDetail calen = calendar.get(i);
					calen.setInternalNum(String.valueOf(iInternal));
					update(context, calen);
				} else {
					if (calendar.get(i).getInternalNum() != null && calendar.get(i).getInternalNum().length() > 0) {
						update(context, calendar.get(i));
					} else {
						insert(context, calendar.get(i));
					}
				}
			}
		}
	}
	
	public void cleanCalendar(Context context) {
		this.openRead();
		DeviceUtility.log(TAG, "cleanCalendar");
		CalendarUtility local = new CalendarUtility(context);
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CALENDAR, new String[] {"USER_DEF1", "USER_DEF2"}, "USER_DEF1 > 0 OR USER_DEF2 > 0", null, null, null, null);
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "cleanCalendar: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			String strCalendarEventId = cursor.getString(cursor.getColumnIndex("USER_DEF1"));
			String strCalendarReminderId = cursor.getString(cursor.getColumnIndex("USER_DEF2"));
			
			long lCalendarEventId = 0;
			long lCalendarReminderId = 0;
			
			if (strCalendarEventId != null && strCalendarEventId.length() > 0) {
				try {
					lCalendarEventId = Long.parseLong(strCalendarEventId);
				} catch (Exception e) {
					
				}
			}
			
			if (strCalendarReminderId != null && strCalendarReminderId.length() > 0) {
				try {
					lCalendarReminderId = Long.parseLong(strCalendarReminderId);
				} catch (Exception e) {
					
				} finally {
					
				}
			}
			
			if (lCalendarEventId > 0) {
				int iResult = local.removeEvent(lCalendarEventId);
				DeviceUtility.log(TAG, "local.removeEvent: " + iResult);
			}
			
			if (lCalendarReminderId > 0) {
				int iResult2 = local.deleteReminder(lCalendarReminderId);
				DeviceUtility.log(TAG, "local.deleteReminder: " + iResult2);
			}
			
			cursor.moveToNext();
		}
		cursor.close();
		//this.close();
	}
}
