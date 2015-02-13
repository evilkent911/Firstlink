package com.trinerva.icrm.database.source;

import com.trinerva.icrm.database.DatabaseHandler;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

abstract class BaseSource {
	public static SQLiteDatabase database;
	protected DatabaseHandler dbHandler;
	
	public BaseSource(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
	
	public void openWrite() throws SQLException {
		database = dbHandler.getWritableDatabase();
	}
	
	public void close() {
		dbHandler.close();
	}
	
	public void openRead() throws SQLException {
		database = dbHandler.getReadableDatabase();
	}
}
