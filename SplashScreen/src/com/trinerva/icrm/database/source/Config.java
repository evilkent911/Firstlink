package com.trinerva.icrm.database.source;

import java.util.ArrayList;
import java.util.List;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.ConfigInfo;
import com.trinerva.icrm.utility.DeviceUtility;

import android.content.ContentValues;
import android.database.Cursor;

public class Config extends BaseSource {
	//private DatabaseHandler dbHandler;
	private static String TAG = "Config";
	
	public Config(DatabaseHandler dbHandler) {
		super(dbHandler);
		//this.dbHandler = dbHandler;
	}
	
	public List<ConfigInfo> getAllConfig() {
		this.openRead();
		DeviceUtility.log(TAG, "getAllConfig");
		List<ConfigInfo> configList = new ArrayList<ConfigInfo>();

		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_CONFIG,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ConfigInfo object = setToObject(cursor);
			configList.add(object);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return configList;
	}
	
	public String getUserId(){
		this.openRead();
		Cursor cursor = database.rawQuery("select * from "
				+ DatabaseHandler.TABLE_FL_CONFIG + " where CONFIG_EMAIL = ?",
				new String[] { "USER_EMAIL" });

	try {
	cursor.moveToFirst();
		
		return cursor.getString(cursor
				.getColumnIndex("USER_DEF1"));
	} catch (Exception e) {
		return "";
	}
	}
	
	public int update(String strKey, String strValue) {
		this.openWrite();
		DeviceUtility.log(TAG, "update(" + strKey + "," + strValue + ")");
		int iResult = 0;
		ContentValues values = new ContentValues();
		values.put("CONFIG_VALUE", strValue);
		iResult = database.update(DatabaseHandler.TABLE_FL_CONFIG, values, "CONFIG_TEXT = ?", new String[] {strKey});
		this.close();
		return iResult;
	}
	
	public void updateId(String strValue) {
		this.openWrite();
		ContentValues values = new ContentValues();
		values.put("USER_DEF1", strValue);
		database.update(DatabaseHandler.TABLE_FL_CONFIG, values, "CONFIG_TEXT = ?", new String[] {"USER_EMAIL"});
		this.close();
	}
	
	private ConfigInfo setToObject(Cursor cursor) {
		ConfigInfo config = new ConfigInfo();
		config.setConfigType(cursor.getString(1));
		config.getConfigValue(cursor.getString(2));
		config.getConfigText(cursor.getString(3));
		config.getUserDEF1(cursor.getString(5));
		config.getCreatedTimestamp(cursor.getString(14));
		config.getModifiedTimestamp(cursor.getString(15));
		return config;
	}
}
