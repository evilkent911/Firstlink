package com.trinerva.icrm.database.source;

import java.util.ArrayList;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.object.CoordinateDetail;
import com.trinerva.icrm.utility.DeviceUtility;

import android.database.Cursor;

public class Coordinate extends BaseSource {
	private String TAG = "Coordinate";
	
	public Coordinate(DatabaseHandler dbHandler) {
		super(dbHandler);
	}
	
	private CoordinateDetail setToObject(Cursor cursor) {
		CoordinateDetail coordinate = new CoordinateDetail();
		coordinate.setId(cursor.getString(cursor.getColumnIndex("_id")));
		coordinate.setInternalNum(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
		coordinate.setServerId(cursor.getString(cursor.getColumnIndex("SYSTEM_ID")));
		coordinate.setActive(cursor.getString(cursor.getColumnIndex("ACTIVE")));
		coordinate.setFirstName(cursor.getString(cursor.getColumnIndex("FIRST_NAME")));
		coordinate.setLastName(cursor.getString(cursor.getColumnIndex("LAST_NAME")));
		coordinate.setCompanyName(cursor.getString(cursor.getColumnIndex("COMPANY_NAME")));
		coordinate.setCity(cursor.getString(cursor.getColumnIndex("MAILING_CITY")));
		coordinate.setCountry(cursor.getString(cursor.getColumnIndex("MAILING_COUNTRY")));
		coordinate.setState(cursor.getString(cursor.getColumnIndex("MAILING_STATE")));
		coordinate.setStreet(cursor.getString(cursor.getColumnIndex("MAILING_STREET")));
		coordinate.setZip(cursor.getString(cursor.getColumnIndex("MAILING_ZIP")));
		coordinate.setLatitude(cursor.getDouble(cursor.getColumnIndex("GPS_LAT")));
		coordinate.setLongitude(cursor.getDouble(cursor.getColumnIndex("GPS_LONG")));
		coordinate.setType(cursor.getString(cursor.getColumnIndex("CONTACT_TYPE")));
		coordinate.setPhoto(cursor.getString(cursor.getColumnIndex("PHOTO")));
		return coordinate;
	}
	
	
	public ArrayList<CoordinateDetail> getAllContactLead() {
		this.openRead();
		ArrayList<CoordinateDetail> aCoordinate = new ArrayList<CoordinateDetail>();
		Cursor cursor = database.rawQuery("SELECT _id, INTERNAL_NUM, SYSTEM_ID, ACTIVE, FIRST_NAME, LAST_NAME, COMPANY_NAME, GPS_LAT, GPS_LONG, MAILING_STREET, MAILING_CITY, MAILING_STATE, MAILING_COUNTRY, MAILING_ZIP, PHOTO, CONTACT_TYPE" +
				" FROM (" +
				" SELECT 'C' || INTERNAL_NUM AS _id, INTERNAL_NUM, CONTACT_ID AS SYSTEM_ID, ACTIVE, FIRST_NAME, LAST_NAME, COMPANY_NAME, GPS_LAT, GPS_LONG, MAILING_STREET, MAILING_CITY, MAILING_STATE, MAILING_COUNTRY, MAILING_ZIP, PHOTO, 'CONTACT' AS CONTACT_TYPE" +
				" FROM " + DatabaseHandler.TABLE_FL_CONTACT + 
				" WHERE ACTIVE = ? AND GPS_LAT IS NOT NULL AND GPS_LAT > 0 AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG > 0 AND GPS_LONG <> ''" +
				" UNION " +
				" SELECT 'L' || INTERNAL_NUM AS _id, INTERNAL_NUM, LEAD_ID AS SYSTEM_ID, ACTIVE, FIRST_NAME, LAST_NAME, COMPANY_NAME, GPS_LAT, GPS_LONG, MAILING_STREET, MAILING_CITY, MAILING_STATE, MAILING_COUNTRY, MAILING_ZIP, PHOTO, 'LEAD' AS CONTACT_TYPE" +
				" FROM " + DatabaseHandler.TABLE_FL_LEAD +
				" WHERE ACTIVE = ? AND GPS_LAT IS NOT NULL AND GPS_LAT > 0 AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG > 0 AND GPS_LONG <> ''" +
				")", new String[] {"0", "0"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "count: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aCoordinate.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aCoordinate;
	}
	/*
	public ArrayList<CoordinateDetail> getAllContactLeadFilter(String sFilter) {
		this.openRead();
		ArrayList<CoordinateDetail> aCoordinate = new ArrayList<CoordinateDetail>();
		Cursor cursor = database.rawQuery("SELECT _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, GPS_LAT, GPS_LONG, MAILING_STREET, MAILING_CITY, MAILING_STATE, MAILING_COUNTRY, MAILING_ZIP, PHOTO, CONTACT_TYPE" +
				" FROM (" +
				" SELECT 'C' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, GPS_LAT, GPS_LONG, MAILING_STREET, MAILING_CITY, MAILING_STATE, MAILING_COUNTRY, MAILING_ZIP, PHOTO, 'CONTACT' AS CONTACT_TYPE" +
				" FROM " + DatabaseHandler.TABLE_FL_CONTACT + 
				" WHERE ACTIVE = ? AND (FIRST_NAME LIKE ? OR LAST_NAME = ? OR COMPANY_NAME = ?) AND GPS_LAT IS NOT NULL AND GPS_LAT > 0 AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG > 0 AND GPS_LONG <> ''" +
				" UNION " +
				" SELECT 'L' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, GPS_LAT, GPS_LONG, MAILING_STREET, MAILING_CITY, MAILING_STATE, MAILING_COUNTRY, MAILING_ZIP, PHOTO, 'LEAD' AS CONTACT_TYPE" +
				" FROM " + DatabaseHandler.TABLE_FL_LEAD +
				" WHERE ACTIVE = ? AND (FIRST_NAME LIKE ? OR LAST_NAME = ? OR COMPANY_NAME = ?) AND GPS_LAT IS NOT NULL AND GPS_LAT > 0 AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG > 0 AND GPS_LONG <> ''" +
				")", new String[] {"0", "%"+sFilter+"%", "%"+sFilter+"%", "%"+sFilter+"%", "0", "%"+sFilter+"%", "%"+sFilter+"%", "%"+sFilter+"%"});
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "count: " + cursor.getCount());
		while (!cursor.isAfterLast()) {
			aCoordinate.add(setToObject(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		this.close();
		return aCoordinate;
	}*/
	
	/*
	public Cursor findPlacesWithinDistance(double radius, GeoLocation location, int iDistanceCategory) {
		this.openRead();
		DeviceUtility.log(TAG, "findPlacesWithinDistance");
		Cursor cursor;
		double dMinDistance = 0;
		double dMaxDistance = 0;
		switch (iDistanceCategory) {
			case 1:
				dMinDistance = 0;
				dMaxDistance = 5;
				break;
			case 2:
				dMinDistance = 5;
				dMaxDistance = 10;
				break;
			case 3:
				dMinDistance = 10;
				dMaxDistance = 15;
				break;
			case 4:
				dMinDistance = 15;
				dMaxDistance = 20;
				break;
		}
		
		GeoLocation[] boundingCoordinates1 = location.boundingCoordinates(dMaxDistance, radius);
		boolean meridian180WithinDistance1 = boundingCoordinates1[0].getLongitudeInRadians() > boundingCoordinates1[1].getLongitudeInRadians();
		
		if (dMinDistance > 0){
			GeoLocation[] boundingCoordinates2 = location.boundingCoordinates(dMinDistance, radius);
			boolean meridian180WithinDistance2 = boundingCoordinates2[0].getLongitudeInRadians() > boundingCoordinates2[1].getLongitudeInRadians();
			
			cursor = database.rawQuery("SELECT _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, PHONE1, PHONE2, PHONE3, PHONE4, CONTACT_TYPE FROM ( " +
					" SELECT 'C' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, PHOTO, 'CONTACT' AS CONTACT_TYPE " +
					" FROM " + DatabaseHandler.TABLE_FL_CONTACT + 
					" WHERE GPS_LAT IS NOT NULL AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG <> '' AND ACTIVE = ? " + 
					" AND (Lat >= ? AND Lat <= ?) AND (Lon >= ? " + (meridian180WithinDistance1 ? "OR" : "AND") + " Lon <= ?) AND INTERNAL_NUM NOT IN (" +
							"SELCT INTERNAL_NUM FROM " + DatabaseHandler.TABLE_FL_CONTACT + " WHERE " +
							"GPS_LAT IS NOT NULL AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG <> '' AND ACTIVE = ?" +
							"AND (Lat >= ? AND Lat <= ?) AND (Lon >= ? " + (meridian180WithinDistance2 ? "OR" : "AND") + " Lon <= ?) ) " +
					" UNION " +
					" SELECT 'L' || INTERNAL_NUM AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, PHOTO, 'LEAD' AS CONTACT_TYPE " +
					" FROM " + DatabaseHandler.TABLE_FL_LEAD + 
					" WHERE GPS_LAT IS NOT NULL AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG <> '' AND ACTIVE = ? " +
					" AND (Lat >= ? AND Lat <= ?) AND (Lon >= ? " + (meridian180WithinDistance1 ? "OR" : "AND") + " Lon <= ?) AND INTERNAL_NUM NOT IN (" +
							"SELCT INTERNAL_NUM FROM " + DatabaseHandler.TABLE_FL_CONTACT + " WHERE " +
							"GPS_LAT IS NOT NULL AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG <> '' AND ACTIVE = ?" +
							"AND (Lat >= ? AND Lat <= ?) AND (Lon >= ? " + (meridian180WithinDistance2 ? "OR" : "AND") + " Lon <= ?)) )",
					new String[] {"0", String.valueOf(boundingCoordinates1[0].getLatitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLatitudeInRadians()), 
				String.valueOf(boundingCoordinates1[0].getLongitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLongitudeInRadians()), 
				"0", String.valueOf(boundingCoordinates2[0].getLatitudeInRadians()), String.valueOf(boundingCoordinates2[1].getLatitudeInRadians()), 
				String.valueOf(boundingCoordinates2[0].getLongitudeInRadians()), String.valueOf(boundingCoordinates2[1].getLongitudeInRadians()), 
				"0", String.valueOf(boundingCoordinates1[0].getLatitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLatitudeInRadians()), 
				String.valueOf(boundingCoordinates1[0].getLongitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLongitudeInRadians()),
				"0", String.valueOf(boundingCoordinates2[0].getLatitudeInRadians()), String.valueOf(boundingCoordinates2[1].getLatitudeInRadians()), 
				String.valueOf(boundingCoordinates2[0].getLongitudeInRadians()), String.valueOf(boundingCoordinates2[1].getLongitudeInRadians())});
		} else {
			cursor = database.rawQuery("SELECT _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMAIL1, EMAIL2, EMAIL3, PHONE1, PHONE2, PHONE3, PHONE4, CONTACT_TYPE FROM ( " +
					" SELECT CONCAT('C', INTERNAL_NUM) AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, PHOTO, 'CONTACT' AS CONTACT_TYPE " +
					" FROM " + DatabaseHandler.TABLE_FL_CONTACT + 
					" WHERE GPS_LAT IS NOT NULL AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG <> '' AND ACTIVE = ? " + 
					" AND (Lat >= ? AND Lat <= ?) AND (Lon >= ? " + (meridian180WithinDistance1 ? "OR" : "AND") + " Lon <= ?) " +
					" UNION " +
					" SELECT CONCAT('L', INTERNAL_NUM) AS _id, INTERNAL_NUM, FIRST_NAME, LAST_NAME, COMPANY_NAME, PHOTO, 'LEAD' AS CONTACT_TYPE " +
					" FROM " + DatabaseHandler.TABLE_FL_LEAD + 
					" WHERE GPS_LAT IS NOT NULL AND GPS_LAT <> '' AND GPS_LONG IS NOT NULL AND GPS_LONG <> '' AND ACTIVE = ? " +
					" AND (Lat >= ? AND Lat <= ?) AND (Lon >= ? " + (meridian180WithinDistance1 ? "OR" : "AND") + " Lon <= ?))",
					new String[] {"0", String.valueOf(boundingCoordinates1[0].getLatitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLatitudeInRadians()), 
				String.valueOf(boundingCoordinates1[0].getLongitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLongitudeInRadians()), 
				"0", String.valueOf(boundingCoordinates1[0].getLatitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLatitudeInRadians()), 
				String.valueOf(boundingCoordinates1[0].getLongitudeInRadians()), String.valueOf(boundingCoordinates1[1].getLongitudeInRadians())});
		}
		
		cursor.moveToFirst();
		DeviceUtility.log(TAG, "findPlacesWithinDistance: " + cursor.getCount());
		this.close();
		return cursor;
	}*/
}
