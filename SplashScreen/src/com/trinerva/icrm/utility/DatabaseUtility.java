package com.trinerva.icrm.utility;

import com.trinerva.icrm.database.DatabaseHandler;

import android.content.Context;

public class DatabaseUtility {
	
	public static void getDatabaseHandler(Context context) {
		if (Constants.DBHANDLER == null) {
			Constants.DBHANDLER = new DatabaseHandler(context);
		}
	}
}
