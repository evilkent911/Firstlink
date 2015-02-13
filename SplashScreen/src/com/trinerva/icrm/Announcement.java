package com.trinerva.icrm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.trinerva.icrm.database.source.Broadcast;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.SectionCursorAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import asia.firstlink.icrm.R;

public class Announcement extends Activity {
	private String TAG = "Announcement";
	private ListView list;
	private Dialog loadingDialog;
	private ArrayList<String> aReadFlag = new ArrayList<String>();
	private ItemSectionAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		list = (ListView) findViewById(R.id.listview);
		list.setOnItemClickListener(showDetailsView);
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LoadData load = new LoadData();
		load.execute();
	}
	
	private OnItemClickListener showDetailsView = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			DeviceUtility.log(TAG, "position:" + position + " id : " + id);
			Cursor cursor = (Cursor) adapter.getItem(position);
			String strId = cursor.getString(cursor.getColumnIndex("INTERNAL_NUM"));
			Intent detail = new Intent(Announcement.this, AnnouncementInfo.class);
			detail.putExtra("ID", strId);
			Announcement.this.startActivity(detail);
		}
	};

	private class LoadData extends AsyncTask<String, Void, Cursor> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(Announcement.this, false, null);
		}

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(Announcement.this);
			Broadcast broadcast = new Broadcast(Constants.DBHANDLER);	
			return broadcast.getBroadcastDisplay();
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			adapter = new ItemSectionAdapter(Announcement.this, result);
			list.setAdapter(adapter);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	
	private class ItemSectionAdapter extends SectionCursorAdapter {

		public ItemSectionAdapter(Context context, Cursor c) {
			//super(context, c, android.R.layout.preference_category, c.getColumnIndex("RELEASED_DATE_ONLY"));
	
			super(context, c, R.layout.announcement_section_item, c.getColumnIndex("RELEASED_DATE_ONLY"));
			
			System.out.println("== f "+c.getColumnIndex("RELEASED_DATE_ONLY"));
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView subject = (TextView) view.findViewById(R.id.subject);
			TextView detail = (TextView) view.findViewById(R.id.detail);
			if(!cursor.getString(cursor.getColumnIndex("RELEASED_BY")).equals("anyType{}")){
				
				detail.setText(cursor.getString(cursor.getColumnIndex("RELEASED_BY")));
			}else{
				detail.setText("");
			}
			subject.setText(cursor.getString(cursor.getColumnIndex("SUBJECT")));
//			detail.setText(cursor.getString(cursor.getColumnIndex("RELEASED_TIME_ONLY")) + " " + cursor.getString(cursor.getColumnIndex("RELEASED_BY")));
			
			//aReadFlag.add(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
			//DatabaseUtility.getDatabaseHandler(Announcement.this);
			//Broadcast broadcast = new Broadcast(Constants.DBHANDLER);
			//broadcast.updateReadFlag(cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.announcement_item, null);
		}
	}
	
	String convertTime(String time){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = dateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEEE, dd-MMMM-yyyy");
		
		return dateFormat2.format(d);
	}

	//update broadcast read flag.
	/*public class ReadFlag extends AsyncTask<ArrayList<String>, Void, Void> {

		@Override
		protected Void doInBackground(ArrayList<String>... params) {
			DatabaseUtility.getDatabaseHandler(Announcement.this);
			Broadcast broadcast = new Broadcast(Constants.DBHANDLER);
			int iCount = params[0].size();
			DeviceUtility.log(TAG, "total read Record: " + iCount);
			for (int i=0; i < iCount; i++) {
				broadcast.updateReadFlag(params[0].get(i));
			}
			return null;
		}
	}*/
}
