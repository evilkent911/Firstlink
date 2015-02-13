package com.trinerva.icrm.database.source;

import java.util.ArrayList;
import java.util.HashMap;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.ReportDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;

import android.content.ContentValues;
import android.database.Cursor;

public class Report extends BaseSource {
	private String TAG = "Report";
	public Report(DatabaseHandler dbHandler) {
		super(dbHandler);
	}

	public long insert(ArrayList<ReportDetail> aReportDetail) {
		DeviceUtility.log(TAG, "aReportDetail.size(): " + aReportDetail.size());
		this.openWrite();
		long lResult = -1;
		int iCount = aReportDetail.size();
		for (int i = 0; i < iCount; i++) {
			ReportDetail oReportDetail = aReportDetail.get(i);
			DeviceUtility.log(TAG, oReportDetail.toString());
			
			ContentValues values = new ContentValues();	
			
			if (oReportDetail.getReportType() != null) {
				values.put("REPORT_TYPE", oReportDetail.getReportType());
			}

			if (oReportDetail.getReportStartDate() != null) {
				values.put("REPORT_START_DATE", oReportDetail.getReportStartDate());
			}
			
			if (oReportDetail.getReportEndDate() != null) {
				values.put("REPORT_END_DATE", oReportDetail.getReportEndDate());
			}
			
			if (oReportDetail.getReportPerson() != null) {
				values.put("REPORT_PERSON", oReportDetail.getReportPerson());
			}
			
			if (oReportDetail.getReportValue() != null) {
				values.put("REPORT_VALUE", oReportDetail.getReportValue());
			}
			
			if (oReportDetail.getReportText() != null) {
				values.put("REPORT_TEXT", oReportDetail.getReportText());
			}
			
			if (oReportDetail.getUserDef1() != null) {
				values.put("USER_DEF1", oReportDetail.getUserDef1());
			}
			
			if (oReportDetail.getUserDef2() != null) {
				values.put("USER_DEF2", oReportDetail.getUserDef2());
			}
			
			if (oReportDetail.getUserDef3() != null) {
				values.put("USER_DEF3", oReportDetail.getUserDef3());
			}
			
			if (oReportDetail.getUserDef4() != null) {
				values.put("USER_DEF4", oReportDetail.getUserDef4());
			}
			
			if (oReportDetail.getUserDef5() != null) {
				values.put("USER_DEF5", oReportDetail.getUserDef5());
			}
			
			if (oReportDetail.getUserDef6() != null) {
				values.put("USER_DEF6", oReportDetail.getUserDef6());
			}
			
			if (oReportDetail.getUserDef7() != null) {
				values.put("USER_DEF7", oReportDetail.getUserDef7());
			}
			
			if (oReportDetail.getUserDef8() != null) {
				values.put("USER_DEF8", oReportDetail.getUserDef8());
			}
			
			lResult = database.insert(DatabaseHandler.TABLE_FL_REPORT, null, values);
		}
		this.close();
		return lResult;
	}

	public void deleteReport(String strStartDate, String strEndDate, String strType) {
		this.openWrite();
		database.delete(DatabaseHandler.TABLE_FL_REPORT, "REPORT_TYPE = ?", new String[] {strType});
		this.close();
	}
	
	//get report.
	public ArrayList<HashMap<String, String>> getSummaryReport(String strType) {
		this.openRead();
		ArrayList<HashMap<String, String>> aData = null;
		Cursor cursor;
		if (strType.equalsIgnoreCase(Constants.REPORT_TYPE_ACTIVITY)) {
			cursor = database.rawQuery("SELECT REPORT_START_DATE, REPORT_END_DATE, M.MASTER_TEXT, SUM(REPORT_VALUE) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_REPORT + " AS R, " + DatabaseHandler.TABLE_FL_MASTER + " AS M WHERE R.REPORT_TEXT = M.MASTER_VALUE AND M.MASTER_TYPE = 'TaskKind' AND REPORT_TYPE = ? GROUP BY R.REPORT_TEXT", new String[] {strType});
		} else {
			cursor = database.rawQuery("SELECT REPORT_START_DATE, REPORT_END_DATE, M.MASTER_TEXT, SUM(REPORT_VALUE) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_REPORT + " AS R, " + DatabaseHandler.TABLE_FL_MASTER + " AS M WHERE R.REPORT_TEXT = M.MASTER_VALUE AND M.MASTER_TYPE = 'OpportunityStage' AND REPORT_TYPE = ? GROUP BY R.REPORT_TEXT", new String[] {strType});
		}
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getSummaryReport: " + cursor.getCount());
		if (cursor.getCount() > 0) {
			aData = new ArrayList<HashMap<String, String>>();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> hData = new HashMap<String, String>();
				hData.put("START_DATE", cursor.getString(cursor.getColumnIndex("REPORT_START_DATE")));
				hData.put("END_DATE", cursor.getString(cursor.getColumnIndex("REPORT_END_DATE")));
				hData.put("TOTAL", cursor.getString(cursor.getColumnIndex("TOTAL")));
				hData.put("TEXT", cursor.getString(cursor.getColumnIndex("MASTER_TEXT")));
				DeviceUtility.log(TAG, hData.toString());
				aData.add(hData);
				cursor.moveToNext();
			}
		}
		cursor.close();
		this.close();
		return aData;
	}

	public Cursor getReport(String strReportType) {
		this.openRead();
		DeviceUtility.log(TAG, "getReport");
		Cursor cursor;
		
		if (strReportType.equalsIgnoreCase(Constants.REPORT_TYPE_ACTIVITY)) {
			cursor = database.rawQuery("SELECT R.INTERNAL_NUM AS _id, REPORT_START_DATE, REPORT_PERSON, REPORT_END_DATE, M.MASTER_TEXT, SUM(REPORT_VALUE) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_REPORT + " AS R, " + DatabaseHandler.TABLE_FL_MASTER + " AS M WHERE R.REPORT_TEXT = M.MASTER_VALUE AND M.MASTER_TYPE = 'TaskKind' AND REPORT_TYPE = ? GROUP BY R.REPORT_TEXT", new String[] {strReportType});
		} else {
			cursor = database.rawQuery("SELECT R.INTERNAL_NUM AS _id, REPORT_START_DATE, REPORT_PERSON, REPORT_END_DATE, M.MASTER_TEXT, SUM(REPORT_VALUE) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_REPORT + " AS R, " + DatabaseHandler.TABLE_FL_MASTER + " AS M WHERE R.REPORT_TEXT = M.MASTER_VALUE AND M.MASTER_TYPE = 'OpportunityStage' AND REPORT_TYPE = ? GROUP BY R.REPORT_TEXT", new String[] {strReportType});
		}
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getReport: " + cursor.getCount());
		this.close();
		return cursor;
	}
	
	public int getReportCount(String strReportType) {
		this.openRead();
		int iIotal = 0;
		DeviceUtility.log(TAG, "getReportCount");
		Cursor cursor;
		
		if (strReportType.equalsIgnoreCase(Constants.REPORT_TYPE_ACTIVITY)) {
			cursor = database.rawQuery("SELECT COUNT(1) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_REPORT + " AS R, " + DatabaseHandler.TABLE_FL_MASTER + " AS M WHERE R.REPORT_TEXT = M.MASTER_VALUE AND M.MASTER_TYPE = 'TaskKind' AND REPORT_TYPE = ? GROUP BY R.REPORT_TEXT", new String[] {strReportType});
		} else {
			cursor = database.rawQuery("SELECT COUNT(1) AS TOTAL FROM " + DatabaseHandler.TABLE_FL_REPORT + " AS R, " + DatabaseHandler.TABLE_FL_MASTER + " AS M WHERE R.REPORT_TEXT = M.MASTER_VALUE AND M.MASTER_TYPE = 'OpportunityStage' AND REPORT_TYPE = ? GROUP BY R.REPORT_TEXT", new String[] {strReportType});
		}
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "getReportCount: " + cursor.getCount());
		iIotal = cursor.getCount();
		cursor.close();
		this.close();
		return iIotal;
	}
}
