package com.trinerva.icrm.database.source;

import java.util.ArrayList;
import java.util.List;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.utility.DeviceUtility;

import android.content.ContentValues;
import android.database.Cursor;

public class Master extends BaseSource {
	//private SQLiteDatabase database;
	//private DatabaseHandler dbHandler;
	private static String TAG = "Master";
	
	public Master(DatabaseHandler dbHandler) {
		super(dbHandler);
		//this.dbHandler = dbHandler;
	}
	/*
	public void open() throws SQLException {
		database = dbHandler.getWritableDatabase();
	}

	public void close() {
		dbHandler.close();
	}*/
	
	public void insert(ArrayList<MasterInfo> aInsert) {
		this.openWrite();
		DeviceUtility.log(TAG, "Insert master list");
		ContentValues values = new ContentValues();
		for(int i = 0; i < aInsert.size(); i++) {
			MasterInfo dInsert = aInsert.get(i);
			DeviceUtility.log(TAG, dInsert.toString());
		    values.put("MASTER_TYPE", dInsert.getType());
		    values.put("MASTER_VALUE", dInsert.getValue());
		    values.put("MASTER_TEXT", dInsert.getText());
		    values.put("USER_DEF1", String.valueOf(dInsert.getDefaultValue()));
		    values.put("USER_DEF2", dInsert.getUser2());
		    values.put("USER_DEF3", dInsert.getUser3());
		    database.insert(DatabaseHandler.TABLE_FL_MASTER, null, values);
		}
		this.close();
	}
	
//	public void insertOpportunityStage(ArrayList<MasterInfo> aInsert) {
//		this.openWrite();
//		DeviceUtility.log(TAG, "Insert master list");
//		ContentValues values = new ContentValues();
//		for(int i = 0; i < aInsert.size(); i++) {
//			MasterInfo dInsert = aInsert.get(i);
//			DeviceUtility.log(TAG, dInsert.toString());
//		    values.put("MASTER_TYPE", dInsert.getType());
//		    values.put("MASTER_VALUE", dInsert.getValue());
//		    values.put("MASTER_TEXT", dInsert.getText());
//		    values.put("USER_DEF1", String.valueOf(dInsert.getDefaultValue()));
//		    values.put("USER_DEF2", dInsert.getUser2());
//		    values.put("USER_DEF3", dInsert.getUser3());
//		    
//		    
//		    database.insert(DatabaseHandler.TABLE_FL_MASTER, null, values);
//		}
//		this.close();
//	}
	
	public void truncate() {
		DeviceUtility.log(TAG, "truncate master list");
		this.openWrite();
		database.execSQL("DROP TABLE " + DatabaseHandler.TABLE_FL_MASTER);
		database.execSQL("VACUUM");
		database.execSQL(DatabaseHandler.strCreateMasterSql);
		this.close();
	}
	
	public List<MasterInfo> getAllMaster() {
		this.openRead();
		DeviceUtility.log(TAG, "getAllMaster");
		List<MasterInfo> masterList = new ArrayList<MasterInfo>();

		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_MASTER,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MasterInfo object = setToObject(cursor);
			masterList.add(object);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return masterList;
	}
	
	public List<MasterInfo> getAllMasterByType(String strType) {
		this.openRead();
		DeviceUtility.log(TAG, "getAllMasterByType: " + strType);
		List<MasterInfo> masterList = new ArrayList<MasterInfo>();
System.out.println("kiat 1 = "+strType);
		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_MASTER,
				null, "MASTER_TYPE = ?", new String[] {strType}, null, null, null);

try {
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
		
		MasterInfo object = setToObject(cursor);
		masterList.add(object);
		cursor.moveToNext();
	}
} catch (Exception e) {
	e.printStackTrace();
}
System.out.println("kiat 2 = "+masterList.size());
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return masterList;
	}
	
	public List<MasterInfo> getAllMasterByTypeCategory(String strType,String strGategory) {
		this.openRead();
		DeviceUtility.log(TAG, "getAllMasterByType: " + strType);
		List<MasterInfo> masterList = new ArrayList<MasterInfo>();

		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_MASTER,
				null, "MASTER_TYPE = ? AND MASTER_TEXT = ?", new String[] {strType,strGategory}, null, null, null);

try {
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
		
		MasterInfo object = setToObject(cursor);
		masterList.add(object);
		cursor.moveToNext();
	}
} catch (Exception e) {
	e.printStackTrace();
}
		
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return masterList;
	}
	
	public List<MasterInfo> getAllMasterByTypeCategoryName(String strType,String strGategory) {
		this.openRead();
		DeviceUtility.log(TAG, "getAllMasterByType: " + strType);
		List<MasterInfo> masterList = new ArrayList<MasterInfo>();

		Cursor cursor = database.query(DatabaseHandler.TABLE_FL_MASTER,
				null, "MASTER_TYPE = ? AND MASTER_VALUE = ?", new String[] {strType,strGategory}, null, null, null);

try {
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
		
		MasterInfo object = setToObject(cursor);
		masterList.add(object);
		cursor.moveToNext();
	}
} catch (Exception e) {
	e.printStackTrace();
}
		
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return masterList;
	}
	
	
	public Cursor getAllMasterCompany(String strType) {
		this.openRead();

		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id,MASTER_VALUE,MASTER_TEXT FROM "+DatabaseHandler.TABLE_FL_MASTER+" where MASTER_TYPE = ?", new String[] {strType});
		return cursor;
	}
	
	public Cursor getAllMasterCompanyFilter(String strType,String filter) {
		this.openRead();

		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id,MASTER_VALUE,MASTER_TEXT FROM "+DatabaseHandler.TABLE_FL_MASTER+" where MASTER_TYPE = ? AND MASTER_TEXT like ? ", new String[] {strType,"%" + filter + "%"});
		return cursor;
	}
	
	public Cursor getAllMasterCompanyFilter(String strType,String filter,String strFilterIn) {
		System.out.println("user f = "+strFilterIn);
		this.openRead();
		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id,MASTER_VALUE,MASTER_TEXT,USER_DEF2 FROM "+DatabaseHandler.TABLE_FL_MASTER+" where MASTER_TYPE = ? AND MASTER_VALUE NOT IN ("+strFilterIn+") AND MASTER_VALUE NOT IN ( SELECT USER_DEF1 FROM FL_CONFIG WHERE CONFIG_TEXT = 'USER_EMAIL')  AND MASTER_TEXT like ? ", new String[] {strType,"%" + filter + "%"});
		return cursor;
	}
	
	public Cursor getSelectedAllMasterCompanyFilter(String strType,String filter,String strFilterIn) {
		this.openRead();
		System.out.println("user f 1 = "+strFilterIn);
		Cursor cursor = database.rawQuery("SELECT INTERNAL_NUM AS _id,MASTER_VALUE,MASTER_TEXT,USER_DEF2 FROM "+DatabaseHandler.TABLE_FL_MASTER+" where MASTER_TYPE = ? AND MASTER_VALUE IN ("+strFilterIn+")  AND MASTER_TEXT like ? ", new String[] {strType,"%" + filter + "%"});
		return cursor;
	}
	
	private MasterInfo setToObject(Cursor cursor) {
		MasterInfo master = new MasterInfo();
		master.setType(cursor.getString(1));
		master.setValue(cursor.getString(2));
		master.setText(cursor.getString(3));
		master.setDefaultValue(Boolean.parseBoolean(cursor.getString(5)));
		return master;
	}
}
