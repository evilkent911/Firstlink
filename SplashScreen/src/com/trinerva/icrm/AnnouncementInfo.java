package com.trinerva.icrm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.trinerva.icrm.database.source.Broadcast;
import com.trinerva.icrm.object.BroadcastDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import asia.firstlink.icrm.R;

public class AnnouncementInfo extends Activity {
	private String TAG = "AnnouncementInfo";
	private Dialog loadingDialog;
	private TextView released_date, released_by, announcement_subject,
			announcement_content;
	private String strId = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get announcement id.
		Bundle bundle = getIntent().getExtras();
		strId = bundle.getString("ID");
		setContentView(R.layout.announcement_info);
		released_date = (TextView) findViewById(R.id.released_date);
		released_by = (TextView) findViewById(R.id.released_by);
		announcement_subject = (TextView) findViewById(R.id.announcement_subject);
		announcement_content = (TextView) findViewById(R.id.announcement_content);

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
		load.execute(new String[] { strId });
	}

	private class LoadData extends AsyncTask<String, Void, BroadcastDetail> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(AnnouncementInfo.this,
					false, null);
		}

		@Override
		protected BroadcastDetail doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(AnnouncementInfo.this);
			Broadcast broadcast = new Broadcast(Constants.DBHANDLER);
			return broadcast.getBroadcastDetail(params[0]);
		}

		@Override
		protected void onPostExecute(BroadcastDetail result) {
			super.onPostExecute(result);
			DeviceUtility.log(TAG, result.toString());
			released_date.setText(convertTime(checkNull(result.getReleasedDate())));
			released_by.setText(checkNull(result.getReleasedBy()));
			announcement_subject.setText(checkNull(result.getSubject()));
			announcement_content.setText(Html.fromHtml(checkNull(result
					.getBroadcastContent())));
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	String checkNull(String str) {

		try {
			if (str.equals("anyType{}")) {
				str = "";
			}
		} catch (Exception e) {
			str = "";
		}

		return str;
	}

	String convertTime(String time) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;

		try {
			d = dateFormat.parse(time);
		} catch (Exception e) {
			return "";
		}
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

		return dateFormat2.format(d);
	}
}
