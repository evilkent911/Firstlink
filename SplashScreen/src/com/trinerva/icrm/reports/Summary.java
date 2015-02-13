package com.trinerva.icrm.reports;

import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.trinerva.icrm.database.source.Report;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class Summary {
	private String[] sColorCode = new String[] {"#FF0000", "#00FFFF", "#0000FF", "#ADD8E6", "#800080", "#FFFF00", "#00FF00", "#FF00FF", "#C0C0C0", "#808080", "#FFA500", "#A52A2A", "#800000", "#008000", "#808000", "#0000A0"};
	private String TAG = "Summary";
	public Intent execute(Context context, String strType, String strReportTitle) {
		DatabaseUtility.getDatabaseHandler(context);
		Report report = new Report(Constants.DBHANDLER);
		ArrayList<HashMap<String, String>> aData = report.getSummaryReport(strType);
		
		if (aData.size() > 0) {
			DefaultRenderer renderer = buildCategoryRenderer(sColorCode, aData.size());
			
			CategorySeries categorySeries = new CategorySeries("Report");
			for (int i = 0; i < aData.size(); i++) {
				HashMap<String, String> hData = aData.get(i);
				DeviceUtility.log(TAG, hData.toString());
				categorySeries.add(hData.get("TEXT")+ " : " + hData.get("TOTAL"), Double.parseDouble(hData.get("TOTAL")));
				strReportTitle = strReportTitle.replace("[START_DATE]", hData.get("START_DATE"));
				strReportTitle = strReportTitle.replace("[END_DATE]", hData.get("END_DATE"));
			}
			return ChartFactory.getPieChartIntent(context, categorySeries, renderer, strReportTitle);
		}
		return null;
	}

	protected DefaultRenderer buildCategoryRenderer(String[] colors, int iSize) {
		DefaultRenderer renderer = new DefaultRenderer();
		int iTotal = colors.length;
		if (iTotal > iSize) {
			for (int iCount = 0; iCount < iSize; iCount++) {
				SimpleSeriesRenderer r = new SimpleSeriesRenderer();
				r.setColor(Color.parseColor(colors[iCount]));
				renderer.addSeriesRenderer(r);
			}
		}		
		return renderer;
	}
}
