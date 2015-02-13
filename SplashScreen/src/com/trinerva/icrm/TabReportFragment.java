package com.trinerva.icrm;

import java.util.ArrayList;
import java.util.HashMap;
import com.trinerva.icrm.database.source.Report;
import com.trinerva.icrm.reports.Individual;
import com.trinerva.icrm.reports.Summary;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.GuiUtility;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import asia.firstlink.icrm.R;

public class TabReportFragment extends Fragment {
	private String TAG = "TabReportFragment";
	private TextView empty_report, activity_summary, activity_individual, opportunity_summary, opportunity_individual;
	private Dialog loadingDialog;
	private LinearLayout activity_report, opprtunity_report;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		RelativeLayout mainLayout = (RelativeLayout)inflater.inflate(R.layout.tab_report, container, false);
		activity_summary = (TextView) mainLayout.findViewById(R.id.activity_summary);
		activity_individual = (TextView) mainLayout.findViewById(R.id.activity_individual);
		opportunity_summary = (TextView) mainLayout.findViewById(R.id.opportunity_summary);
		opportunity_individual = (TextView) mainLayout.findViewById(R.id.opportunity_individual);
		activity_report = (LinearLayout) mainLayout.findViewById(R.id.activity_report);
		opprtunity_report = (LinearLayout) mainLayout.findViewById(R.id.opprtunity_report);
		
		empty_report = (TextView) mainLayout.findViewById(R.id.empty_report);
		activity_summary.setOnClickListener(showSummary);
		activity_individual.setOnClickListener(showIndividual);
		opportunity_summary.setOnClickListener(showSummary);
		opportunity_individual.setOnClickListener(showIndividual);
		
		return mainLayout;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		PreloadingCheck load = new PreloadingCheck();
		load.execute();
	}

	private OnClickListener showSummary = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.activity_summary:
					Intent achartActIntent = new Summary().execute(v.getContext(), Constants.REPORT_TYPE_ACTIVITY, getString(R.string.activity_summary_title));
					startActivity(achartActIntent);
					break;
				case R.id.opportunity_summary:
					Intent achartOppIntent = new Summary().execute(v.getContext(), Constants.REPORT_TYPE_OPPORTUNITY, getString(R.string.opportunity_summary_title));
					startActivity(achartOppIntent);
					break;
			}
		}
	};
	
	private OnClickListener showIndividual = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Bundle bFlag = new Bundle();
			switch (v.getId()) {
				case R.id.activity_individual:
					bFlag.putString("MODE", Constants.REPORT_TYPE_ACTIVITY);
					break;
				case R.id.opportunity_individual:
					bFlag.putString("MODE", Constants.REPORT_TYPE_OPPORTUNITY);
					break;
			}
			Intent intent = new Intent(v.getContext(), Individual.class);
			intent.putExtras(bFlag);
			v.getContext().startActivity(intent);
		}
	};
	
	private class PreloadingCheck extends AsyncTask<String, Void, HashMap<String, Integer>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(getActivity(), false, null);
		}
		
		@Override
		protected HashMap<String, Integer> doInBackground(String... params) {
			//check if any report available.
			HashMap<String, Integer> hResult = new HashMap<String, Integer>();
			DatabaseUtility.getDatabaseHandler(getActivity());
			Report report = new Report(Constants.DBHANDLER);
			int iActivityTotal = report.getReportCount(Constants.REPORT_TYPE_ACTIVITY);
			hResult.put(Constants.REPORT_TYPE_ACTIVITY, iActivityTotal);
			
			int iOpportunityTotal = report.getReportCount(Constants.REPORT_TYPE_OPPORTUNITY);
			hResult.put(Constants.REPORT_TYPE_OPPORTUNITY, iOpportunityTotal);
			
			return hResult;
		}

		@Override
		protected void onPostExecute(HashMap<String, Integer> result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			boolean bShowEmpty = true;
			if (result != null && result.size() > 0) {
				Integer iOpportunity = result.get(Constants.REPORT_TYPE_OPPORTUNITY);
				if (iOpportunity != null) {
					if (iOpportunity.intValue() > 0) {
						opprtunity_report.setVisibility(View.VISIBLE);
						bShowEmpty = false;
					} else {
						opprtunity_report.setVisibility(View.GONE);
					}
				} else {
					opprtunity_report.setVisibility(View.GONE);
				}
				
				Integer iActivity = result.get(Constants.REPORT_TYPE_ACTIVITY);
				if (iActivity != null) {
					if (iActivity.intValue() > 0) {
						activity_report.setVisibility(View.VISIBLE);
						bShowEmpty = false;
					} else {
						activity_report.setVisibility(View.GONE);
					}
				} else {
					activity_report.setVisibility(View.GONE);
				}
				
				if (bShowEmpty) {
					empty_report.setVisibility(View.VISIBLE);
				} else {
					empty_report.setVisibility(View.GONE);
				}
			} else {
				empty_report.setVisibility(View.VISIBLE);
				activity_report.setVisibility(View.GONE);
				opprtunity_report.setVisibility(View.GONE);
			}
		}
	}
}
