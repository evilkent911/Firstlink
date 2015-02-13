package com.trinerva.icrm;

import java.util.ArrayList;
import com.trinerva.icrm.contacts.ContactList;
import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Broadcast;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Opportunity;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.event.EventList;
import com.trinerva.icrm.leads.LeadList;
import com.trinerva.icrm.object.CalendarDetail;
import com.trinerva.icrm.object.ContactActivity;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.LeadActivity;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.object.OpportunityDetail;
import com.trinerva.icrm.object.TaskDetail;
import com.trinerva.icrm.tasks.TaskList;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import asia.firstlink.icrm.R;

public class TabHomeFragment extends Fragment {
	private String TAG = "TabHomeFragment";
	private Button contact, lead, calendar, task;
	private LinearLayout ll_sync, ll_announcement;
	private ImageView announcement;
	private Dialog loadingDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		RelativeLayout mainLayout = (RelativeLayout)inflater.inflate(R.layout.tab_home, container, false);
		contact = (Button) mainLayout.findViewById(R.id.contact);
		lead = (Button) mainLayout.findViewById(R.id.lead);
		calendar = (Button) mainLayout.findViewById(R.id.calendar);
		task = (Button) mainLayout.findViewById(R.id.task);
		announcement = (ImageView) mainLayout.findViewById(R.id.announcement);
		
		contact.setOnClickListener(onContactClick);
		lead.setOnClickListener(onLeadClick);
		calendar.setOnClickListener(onCalendarClick);
		task.setOnClickListener(onTaskClick);
		
		ll_sync = (LinearLayout) mainLayout.findViewById(R.id.ll_sync);
		ll_announcement = (LinearLayout) mainLayout.findViewById(R.id.ll_announcement);
		ll_sync.setOnClickListener(onSyncClick);
		ll_announcement.setOnClickListener(onAnnouncementClick);
		return mainLayout;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		PreloadingCheck load = new PreloadingCheck();
		load.execute();
	}

	private OnClickListener onContactClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent contact = new Intent(v.getContext(), ContactList.class);
			TabHomeFragment.this.startActivity(contact);
		}
	};
	
	private OnClickListener onLeadClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent lead = new Intent(v.getContext(), LeadList.class);
			TabHomeFragment.this.startActivity(lead);
		}
	};
	
	private OnClickListener onCalendarClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent event = new Intent(v.getContext(), EventList.class);
			TabHomeFragment.this.startActivity(event);
		}
	};
	
	private OnClickListener onTaskClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent event = new Intent(v.getContext(), TaskList.class);
			TabHomeFragment.this.startActivity(event);
		}
	};
	
	private OnClickListener onSyncClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent sync = new Intent(v.getContext(), CrmLogin.class);
			TabHomeFragment.this.startActivity(sync);
		}
	};
	
	private OnClickListener onAnnouncementClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent annouce = new Intent(v.getContext(), Announcement.class);
			TabHomeFragment.this.startActivity(annouce);
		}
	};
	
	private class PreloadingCheck extends AsyncTask<String, Void, Integer> {
		private boolean bUnsync = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(getActivity(), false, null);
		}

		@Override
		protected Integer doInBackground(String... params) {
			//check any unread broadcast.
			DatabaseUtility.getDatabaseHandler(getActivity());
			Broadcast broadcast = new Broadcast(Constants.DBHANDLER);
			int iUnreadCount = broadcast.getNewBroadcastCount();
			DeviceUtility.log(TAG, "total unread: " + iUnreadCount);
			
			//check any unsync item.
			Contact oContactSource = new Contact(Constants.DBHANDLER);
			ArrayList<ContactDetail> aContact = oContactSource.getUnsyncContact();
			if (aContact != null && aContact.size() > 0) {
				bUnsync = true;
			}
			
			if (bUnsync == false) {
				Opportunity oOpportunitySource = new Opportunity(Constants.DBHANDLER);
				ArrayList<OpportunityDetail> aOpportunity = oOpportunitySource.getUnsyncOpportunity();
				if (aOpportunity != null && aOpportunity.size() > 0) {
					bUnsync = true;
				}
			}
			
			if (bUnsync == false) {
				Lead oLeadSource = new Lead(Constants.DBHANDLER);
				ArrayList<LeadDetail> aLead = oLeadSource.getUnsyncLead();
				if (aLead != null && aLead.size() > 0) {
					bUnsync = true;
				}
			}
			
			if (bUnsync == false) {
				FLCalendar oCalendarSource = new FLCalendar(Constants.DBHANDLER);
				ArrayList<CalendarDetail> aCalendar = oCalendarSource.getUnsyncCalendar();
				if (aCalendar != null && aCalendar.size() > 0) {
					bUnsync = true;
				}
			}
			
			if (bUnsync == false) {
				Task oTaskSource = new Task(Constants.DBHANDLER);
				ArrayList<TaskDetail> aTask = oTaskSource.getUnsyncTask();
				if (aTask != null && aTask.size() > 0) {
					bUnsync = true;
				}
			}
			
			if (bUnsync == false) {
				ActivityLog oActivityLogSource = new ActivityLog(Constants.DBHANDLER);
				ArrayList<ContactActivity> aActivityLog = oActivityLogSource.getUnsyncContactActivityLog();
				if (aActivityLog != null && aActivityLog.size() > 0) {
					bUnsync = true;
				} else {
					ArrayList<LeadActivity> aActivityLeadLog = oActivityLogSource.getUnsyncLeadActivityLog();
					if (aActivityLeadLog != null && aActivityLeadLog.size() > 0) {
						bUnsync = true;
					}
				}
			}
			
			return new Integer(iUnreadCount);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result.intValue() > 0) {
				announcement.setImageResource(R.drawable.btn_announcement_unread);
			} else {
				announcement.setImageResource(R.drawable.btn_announcement);
			}
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			
			if (bUnsync) {
				GuiUtility.alert(getActivity(), getString(R.string.unsync_title), getString(R.string.unsync_desc), Gravity.CENTER, getString(R.string.sync_now), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent sync = new Intent(getActivity(), CrmLogin.class);
						TabHomeFragment.this.startActivity(sync);
					}
					
				}, getString(R.string.cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						return;
					}
					
					
				});
			}
		}
	}
}
