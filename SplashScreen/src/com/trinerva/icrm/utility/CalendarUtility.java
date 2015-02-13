/*
 * Copyright (C) 2011 John Malizia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * If you use this in your application, please make not of it so
 * the user can see. Thank you.
 */
package com.trinerva.icrm.utility;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.format.Time;

public class CalendarUtility
{
	public String TAG = "CalendarUtility";
	private int calendarID = -1;
	static String contentProvider;
	static Uri remindersUri;
	static Uri eventsUri;
	static Uri calendars;
	static String eventsTable;
	static long EVENT_DURATION = 3600000;
	static long REMINDER_TIME = 0;
	private int SDK = 10;
	
	
	public Context context = null;

	/**
	 * @param a
	 *            Constructor
	 */
	public CalendarUtility(Context c)
	{
		
		try {
			SDK = new Integer(Build.VERSION.SDK_INT).intValue();
		} catch(Exception e) {
			SDK = 10;
		}
		
		this.context = c;
		if (SDK >= 14) {
			//ics
			eventsTable = "view_events";
			contentProvider = "com.android.calendar";
		} else if (SDK >= 8) {
			//2.2 or higher
			eventsTable = "view_events";
			contentProvider = "com.android.calendar";
		} else {
			//anything older
			eventsTable = "Events";
			contentProvider = "calendar";
		}
		
		remindersUri = Uri.parse(String.format("content://%s/reminders",contentProvider));
		eventsUri = Uri.parse(String.format("content://%s/events",contentProvider));
		calendars = Uri.parse(String.format("content://%s/calendars",contentProvider));
		getCalendars();
	}
	
	public void getCalendars() {
		try {
			Cursor managedCursor = context.getContentResolver().query(calendars, null, null, null, null);
			if (managedCursor.getCount() > 0) {
				int idColumn = managedCursor.getColumnIndex(CalendarColumns.ID);
				if (managedCursor.moveToNext()) {
					this.calendarID = managedCursor.getInt(idColumn);
				}
			}
			
			if(managedCursor != null) {
				managedCursor.close();
			}
		} catch (Exception e) {
			DeviceUtility.log(TAG, "error: " + e.getMessage());
			
		}
	}
	

	/**
	 * @param strTitle
	 *            as the Title of the event
	 * @param strDescription
	 *            as the description of the event
	 * @param startTime
	 *            as the time in millis the event begins
	 */
	public long insertEvent(String strTitle, String strDescription, long startTime, long endTime, int iAllDay) throws Exception {
		if (calendarID != -1) {
			// Source: http://sgap.springnote.com/pages/5150959
System.out.println("startTime = "+startTime);
			ContentValues event = new ContentValues();
			event.put(EventColumns.ID, calendarID);
			event.put(EventColumns.TITLE, strTitle);
			event.put(EventColumns.DESC, strDescription);
			event.put(EventColumns.START, startTime);
			event.put(EventColumns.END, endTime);
			event.put(EventColumns.ALLDAY, iAllDay); //0 or 1
			event.put(EventColumns.EVENT_TIMEZONE, Time.getCurrentTimezone());
			if (SDK < 14) {
				event.put(EventColumns.VIS, "0");
				event.put(EventColumns.TRANS, "0");
				event.put(EventColumns.ALARM, "1");
				event.put(EventColumns.STATUS, "1");
			}
			
			return Long.parseLong(context.getContentResolver().insert(eventsUri, event).getLastPathSegment());
		}
		return -1;
		//throw exception
	}
	
	public int updateEvent(long iEventID, String strTitle, String strDescription, long startTime, long endTime, int iAllDay) {
		if (calendarID != -1) {
			if (contains(iEventID)) {
				Uri updateEventUri = Uri.withAppendedPath(eventsUri, String.valueOf(iEventID));
				ContentValues event = new ContentValues();
				event.put(EventColumns.TITLE, strTitle);
				event.put(EventColumns.DESC, strDescription);
				event.put(EventColumns.START, startTime);
				event.put(EventColumns.END, endTime);
				event.put(EventColumns.ALLDAY, iAllDay);
				return context.getContentResolver().update(updateEventUri, event, null, null);
			}
		}
		return -1;
	}

//	public long insertEvent(HashMap<String, String> args) {
//		//Convert to use ContentValues?
//		if (calendarID != -1) {
//			// Source: http://sgap.springnote.com/pages/5150959
//			ContentValues event = new ContentValues();
//			event.put(EventColumns.ID, calendarID);
//			
//			event.putAll(HashMapToContentValues(args));
//			event.put(EventColumns.ALLDAY, "0");
//			event.put(EventColumns.STATUS, "1");
//			event.put(EventColumns.VIS, "0");
//			event.put(EventColumns.TRANS, "0");
//			event.put(EventColumns.ALARM, "1");
//
//			return Long.parseLong(context.getContentResolver().insert(eventsUri, event).getLastPathSegment());
//		}
//		//throw exception
//		return -1;
//	}

	public long addReminder(long eventID, int minutes) {
		System.out.println("add reminder = "+eventID);
		try {
			if (contains(eventID)) {
				DeviceUtility.log(TAG, "Reminder Added for event " + eventID);
				ContentValues values = new ContentValues();
				values.put("event_id", eventID);
				values.put("method", 1);
				values.put("minutes", 15);
				
				long one =  Long.parseLong(context.getContentResolver().insert(remindersUri, values).getLastPathSegment());
				
				values = new ContentValues();
				values.put("event_id", eventID);
				values.put("method", 1);
				values.put("minutes", 60);
				
				return Long.parseLong(context.getContentResolver().insert(remindersUri, values).getLastPathSegment());
			}
			else
				DeviceUtility.log(TAG, "Reminder Not Added");
		} catch(Exception e) {
			
		}
		return -1;
	}
	
	public int updateReminderWithReminderId(long eventID, long reminderID, int minutes) {
		if (reminderExist(reminderID)) {
			Uri updateReminderUri = Uri.withAppendedPath(remindersUri, String.valueOf(eventID));
			ContentValues values = new ContentValues();
			values.put(ReminderColumns.ID, eventID);
			values.put(ReminderColumns.METHOD, 1);
			values.put(ReminderColumns.TIME, minutes);
	
			return context.getContentResolver().update(updateReminderUri, values, "_id = ?", new String[]{String.valueOf(reminderID)});
		}
		return -1;
	}

	public void updateReminder(long eventID, int minutes) {

		Uri updateReminderUri = Uri.withAppendedPath(remindersUri, String.valueOf(eventID));
		ContentValues values = new ContentValues();
		values.put(ReminderColumns.ID, eventID);
		values.put(ReminderColumns.METHOD, 1);
		values.put(ReminderColumns.TIME, minutes);

		context.getContentResolver().update(updateReminderUri, values, null, null);
		DeviceUtility.log("Calendar Event", "Alarm Updated");
	}
	
	public int deleteReminder(long eventID)
	{

		Uri reminderUri = Uri.withAppendedPath(remindersUri, String.valueOf(eventID));
		int iResult = context.getContentResolver().delete(reminderUri, null, null);
		DeviceUtility.log(TAG, "Reminder deleted");
		return iResult;
	}

	public boolean containsAlarm(long eventID) {
		String selection = ReminderColumns.ID + "=" + eventID;
		String[] projection = new String[] { ReminderColumns.METHOD };
		Cursor cursor = context.getContentResolver().query(remindersUri, projection, selection, null, null);

		if (cursor.getCount() > 0) {
			cursor.close();
			DeviceUtility.log(TAG, "Contains Reminder");
			return true;
		}
		if(cursor != null)
			cursor.close();
		DeviceUtility.log(TAG, "Does not contain a reminder for " + eventID);
		return false;
	}

	/**
	 * Removes the eventID passed Returns the number of rows removed
	 * 
	 * @param iEventID
	 *            as the eventID to remove
	 */
	public int removeEvent(long iEventID) {
		DeviceUtility.log(TAG, "removing event.. " + iEventID);
		if (calendarID != -1 && iEventID != -1) {
			if (this.contains(iEventID)) {
				Uri deleteEventUri = Uri.withAppendedPath(eventsUri, String.valueOf(iEventID));
				return context.getContentResolver().delete(deleteEventUri, null, null);
			}
		}
		return -1;
	}

	/**
	 * Returns boolean specifying if the passed EventID is in the Calendar
	 * 
	 * @param iEventID
	 */
	public boolean contains(long iEventID) {
		if (calendarID != -1) {
			//Wrong table name for android 2.2
			String[] projection = new String[] { EventColumns.TITLE, CalendarColumns.ID };
			Cursor managedCursor = context.getContentResolver().query(eventsUri, projection, eventsTable+"._id=" + iEventID, null, null);
			while (managedCursor.moveToNext()) {
				managedCursor.close();
				return true;
			}
			if(managedCursor != null)
				managedCursor.close();
		}
		return false;
	}
	
	/**
	 * Returns boolean specifying if the passed RemindID is in the Calendar
	 * 
	 * @param iReminderId
	 */
	public boolean reminderExist(long iReminderId) {
		if (calendarID != -1) {
			Cursor managedCursor = context.getContentResolver().query(remindersUri, null, "_id=?", new String[] {String.valueOf(iReminderId)}, null);
			while (managedCursor.moveToNext()) {
				managedCursor.close();
				return true;
			}
			if(managedCursor != null)
				managedCursor.close();
		}
		return false;
	}

	/**
	 * Returns the number of rows updated
	 * 
	 * @param iEventID
	 *            as the eventID to update
	 * @param whereArgs
	 *            as the Set arguments see Where class
	 */
	public int updateEvent(long iEventID, HashMap<String, String> args) {
		if (calendarID != -1) {
			if (contains(iEventID)) {
				Uri updateEventUri = Uri.withAppendedPath(eventsUri, String.valueOf(iEventID));
				return context.getContentResolver().update(updateEventUri, HashMapToContentValues(args), null, null);
			}
		}
		return -1;
	}
	
	private ContentValues HashMapToContentValues(HashMap<String,String> hm) {
		ContentValues cv = new ContentValues();
		for (Entry<String, String> kvp : hm.entrySet())	{
			cv.put(kvp.getKey(), kvp.getValue());
		}
		return cv;
	}
	
	
	public String getAllEvent() {
		String strDisplay = "";
		Cursor cursor = context.getContentResolver().query(eventsUri, new String[] {"_id", "title", "calendar_id"}, null, null, null);
		if (cursor.getCount() > 0) {
			int iColumn = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				DeviceUtility.log(TAG, "***********************************NEW ROW***********************************");
				strDisplay += "\n_id: " + cursor.getString(cursor.getColumnIndex("_id"));
				strDisplay += "\ntitle: " + cursor.getString(cursor.getColumnIndex("title"));
				strDisplay += "\ncalendar_id: " + cursor.getString(cursor.getColumnIndex("calendar_id"));
			}
		}
		cursor.close();
		return strDisplay;
	}

	/**
	 * @return ArrayList as list of calendars available
	 */
	//needs rewritten
	/*public ArrayList<CalendarListItem> getCalendars()
	{
		ArrayList<CalendarListItem> calendarList = new ArrayList<CalendarListItem>();
		String[] projection = new String[] { CalendarColumns.ID, CalendarColumns.NAME, CalendarColumns.DISPLAYNAME};
		Cursor managedCursor = context.getContentResolver().query(calendars, projection, null, null, null);

		if (managedCursor.getCount() > 0)
		{
			int nameColumn = managedCursor.getColumnIndex(CalendarColumns.NAME);
			int displayNameColumn = managedCursor.getColumnIndex(CalendarColumns.DISPLAYNAME);
			int idColumn = managedCursor.getColumnIndex(CalendarColumns.ID);
			while (managedCursor.moveToNext())
			{
				if (!managedCursor.isNull(nameColumn))
					calendarList.add(new CalendarListItem(managedCursor.getString(nameColumn), managedCursor.getLong(idColumn)));
				else
					calendarList.add(new CalendarListItem(managedCursor.getString(displayNameColumn), managedCursor.getLong(idColumn)));
			}
		}
		if(managedCursor != null)
			managedCursor.close();
		
		return calendarList;

	}*/

	/**
	 * 
	 * @return calendarID as selected calendar's id
	 */
	public long getSelectedCalendar() {
		return calendarID;
	}

	/**
	 * @param newCalendarID
	 *            as new calendar
	 */
	public void setSelectedCalendar(int newCalendarID) {
		calendarID = newCalendarID;
	}
	
	class CalendarException extends Exception {
		private static final long serialVersionUID = 0;

		public CalendarException(String desc) {
			super(desc);		
		}
	}
	
	public static class CalendarListItem {

		String name;
		long id;

		public CalendarListItem(String calendarName, long calendarID) {
			this.name = calendarName;
			this.id = calendarID;
		}
		
		public String toString() {
			return this.id +" : "+ this.name;
		}
	}
	
	public static class EventColumns {
		public static final String ID = "calendar_id";
		public static final String TITLE = "title";
		public static final String DESC = "description";
		public static final String START = "dtstart";
		public static final String END = "dtend";
		public static final String ALLDAY = "allDay";
		public static final String STATUS = "eventStatus";
		public static final String VIS = "visibility";
		public static final String TRANS = "transparency";
		public static final String ALARM = "hasAlarm";
		public static final String EVENT_TIMEZONE = "eventTimezone";
	}
	
	public static class ReminderColumns	{
		public static final String ID = "event_id";
		public static final String METHOD = "method";
		public static final String TIME = "minutes";
	}
	
	public static class CalendarColumns {
		public static final String ID = "_id";
		public static final String TITLE = "title";
		public static final String NAME = "name";
		public static final String DISPLAYNAME = "displayName";
	}
}
