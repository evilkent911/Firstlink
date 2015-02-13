package com.trinerva.icrm.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtility {
	
	public static boolean getNetworkStatus(Context context) {
		boolean bConnect = false;
		ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connection.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			bConnect = true;
		}
		return bConnect;
	}
}
