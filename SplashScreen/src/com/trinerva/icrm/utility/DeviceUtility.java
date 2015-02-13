package com.trinerva.icrm.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceUtility {
	
	public static String getDeviceId(Context context) {
		//TODO: Comment the hardcoded IMEI id.
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
		//return "000000000000000";
	}
	
	public static void log(String strTag, String strMsg) {
		int iChunkLength = 3000;
		if (Constants.DEBUG) {
			if (strMsg.length() > iChunkLength) {
				int chunkCount = (int) Math.ceil(strMsg.length() / (double)iChunkLength);
				Log.v(strTag, "strMsg.length(): " + strMsg.length());
				Log.v(strTag, "chunkCount: " + chunkCount);
				for (int i = 0; i < chunkCount; i++) {
			        int max = iChunkLength * (i + 1);
			        if (max >= strMsg.length()) {
			            Log.v(strTag, strMsg.substring(iChunkLength * i));
			        } else {
			            Log.v(strTag, strMsg.substring(iChunkLength * i, max));
			        }
			    }
			} else {
				Log.v(strTag, strMsg);
			}
		}
	}
	
	public static String getVersionName(Context context) {
        String versionName = null;

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
        	info = manager.getPackageInfo(context.getPackageName(), 0);
        	versionName = info.versionName;
        } catch (NameNotFoundException e) {
                versionName = "1.0";
        }
        
        return versionName;
	}
}
