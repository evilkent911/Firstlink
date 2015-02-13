package com.trinerva.icrm.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveData {

	String PREFS_NAME = "firstlink";
	Context context;

	public SaveData(Context context) {
		this.context = context;
	}

	public void saveStringData(String key, String value) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);

		// Commit the edits!
		editor.commit();
	}

	public String getStringData(String key) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(key, "null");
	}

	public void saveBooleanData(String key, boolean value) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBooleanData(String key) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean(key, false);
	}

	public void saveIntData(String key, int value) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getIntData(String key) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		return settings.getInt(key, 0);
	}
}
