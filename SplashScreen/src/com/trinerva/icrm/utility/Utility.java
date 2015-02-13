package com.trinerva.icrm.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.trinerva.icrm.database.source.Config;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.object.ConfigInfo;
import com.trinerva.icrm.object.MasterInfo;

import android.content.Context;
import android.database.Cursor;

public class Utility {
	private static String TAG = "Utility";
	
	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );
	
	public final static Pattern PHONE_PATTERN = Pattern.compile("\\d+");
	public final static Pattern SPACE_PATTERN = Pattern.compile("\\s+");
	
	
	public static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	
	public static boolean isValidPhone(String strPhone) {
		boolean isDigit = PHONE_PATTERN.matcher(strPhone).matches();
		DeviceUtility.log(TAG, "isDigit: "+ isDigit);
		if (isDigit) {
			if (strPhone.length() >= 9) {
				return true;
			}
		} else {
			return isDigit;
		}
		return false;
	}
	
	public static boolean isContainWhitespace(String strSpace) {
		return SPACE_PATTERN.matcher(strSpace).find();
	}
	
	public static String getDatabaseCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMESEC_FORMAT);
		return sdf.format(new Date());
	}
	
	public static String getConfigByText(Context context, String strKey) {
		String strReturn = null;
		if (Constants.CONFIG_LIST == null) {
			//get the data from db.
			DatabaseUtility.getDatabaseHandler(context);
			Config config = new Config(Constants.DBHANDLER);
			Constants.CONFIG_LIST = (ArrayList<ConfigInfo>) config.getAllConfig();
		}
		
		if (Constants.CONFIG_LIST != null) {
			for (int i = 0; i < Constants.CONFIG_LIST.size(); i++) {
				ConfigInfo oConfig = Constants.CONFIG_LIST.get(i);
				if (oConfig.getConfigText().equalsIgnoreCase(strKey)) {
					strReturn = oConfig.getConfigValue();
					break;
				}
			}
		}
		System.out.println("strReturn ="+strReturn);
		return strReturn;
	}
	
	public static String getEmployeeId(){
		Config config = new Config(Constants.DBHANDLER);
		return config.getUserId();
	}
	
	public static String getEmployeeId(Context context, String strKey) {
		String strReturn = null;
		if (Constants.CONFIG_LIST == null) {
			//get the data from db.
			DatabaseUtility.getDatabaseHandler(context);
			Config config = new Config(Constants.DBHANDLER);
			Constants.CONFIG_LIST = (ArrayList<ConfigInfo>) config.getAllConfig();
		}
		
		if (Constants.CONFIG_LIST != null) {
			for (int i = 0; i < Constants.CONFIG_LIST.size(); i++) {
				ConfigInfo oConfig = Constants.CONFIG_LIST.get(i);
				if (oConfig.getConfigValue().equalsIgnoreCase(strKey)) {
					strReturn = oConfig.getUserDEF1();
					break;
				}
			}
		}
		System.out.println("strReturn ="+strReturn);
		return strReturn;
	}
	
	public static void updateConfigByText(Context context, String strKey, String strNewValue) {
		DatabaseUtility.getDatabaseHandler(context);
		Config config = new Config(Constants.DBHANDLER);
		int iSuccess = config.update(strKey, strNewValue);
		if (iSuccess > 0) {
			Constants.CONFIG_LIST = null;
		}
	}
	
	public static void updateConfigUserId(String strNewValue) {
		Config config = new Config(Constants.DBHANDLER);
		config.updateId(strNewValue);
	}
	
	
	public static List<MasterInfo> getMasterByType(Context context, String strKey) {
		List<MasterInfo> aResult = new ArrayList<MasterInfo>();
		DatabaseUtility.getDatabaseHandler(context);
		Master master = new Master(Constants.DBHANDLER);
		DeviceUtility.log(TAG, "strKey: " + strKey);
		DeviceUtility.log(TAG, "Constants.MASTER_PREFIX_LIST: " + Constants.MASTER_PREFIX_LIST);
		System.out.println("kiat 4 = "+strKey);
		if (strKey.equals("Prefix")) {
			if (Constants.MASTER_PREFIX_LIST == null) {
				DeviceUtility.log(TAG, "Prefix");
				Constants.MASTER_PREFIX_LIST = master.getAllMasterByType(strKey);
				DeviceUtility.log(TAG, "Prefix: " + Constants.MASTER_PREFIX_LIST);
			}
			return Constants.MASTER_PREFIX_LIST;
			
		} else if (strKey.equals("LeadAttitude")) {
			if (Constants.MASTER_LEADATTITUDE_LIST == null) {
				Constants.MASTER_LEADATTITUDE_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_LEADATTITUDE_LIST;
			
		} else if (strKey.equals("LeadStatus")) {
			if (Constants.MASTER_LEADSTATUS_LIST == null) {
				Constants.MASTER_LEADSTATUS_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_LEADSTATUS_LIST;
			
		} else if (strKey.equals("LeadSource")) {
			if (Constants.MASTER_LEADSOURCE_LIST == null) {
				Constants.MASTER_LEADSOURCE_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_LEADSOURCE_LIST;
			
		} else if (strKey.equals("CalendarPriority")) {
			if (Constants.MASTER_CALENDARPRIORITY_LIST == null) {
				Constants.MASTER_CALENDARPRIORITY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_CALENDARPRIORITY_LIST;
			
		} else if (strKey.equals("CalendarAvailability")) {
			if (Constants.MASTER_CALENDARAVAILABILITY_LIST == null) {
				Constants.MASTER_CALENDARAVAILABILITY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_CALENDARAVAILABILITY_LIST;
			
		} else if (strKey.equals("TaskPriority")) {
			if (Constants.MASTER_TASKPRIORITY_LIST == null) {
				Constants.MASTER_TASKPRIORITY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_TASKPRIORITY_LIST;
			
		} else if (strKey.equals("TaskAvailability")) {
			if (Constants.MASTER_TASKAVAILABILITY_LIST == null) {
				Constants.MASTER_TASKAVAILABILITY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_TASKAVAILABILITY_LIST;
			
		} else if (strKey.equals("TaskStatus")) {
			if (Constants.MASTER_TASKSTATUS_LIST == null) {
				Constants.MASTER_TASKSTATUS_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_TASKSTATUS_LIST;
			
		} else if (strKey.equals("UserList")) {
			if (Constants.MASTER_USERLIST_LIST == null) {
				Constants.MASTER_USERLIST_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_USERLIST_LIST;
			
		} else if (strKey.equals("OpportunityStage")) {
			if (Constants.MASTER_OPPORTUNITYSTAGE_LIST == null) {
				Constants.MASTER_OPPORTUNITYSTAGE_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_OPPORTUNITYSTAGE_LIST;
			
		} else if (strKey.equals("TaskKind")) {
			if (Constants.MASTER_TASKKIND_LIST == null) {
				Constants.MASTER_TASKKIND_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_TASKKIND_LIST;
			
		} else if (strKey.equals("LeadIndustry")) {
			if (Constants.MASTER_LEADINDUSTRY_LIST == null) {
				Constants.MASTER_LEADINDUSTRY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_LEADINDUSTRY_LIST;
			
		} else if (strKey.equals("Company")) {
			if (Constants.MASTER_LEADINDUSTRY_LIST == null) {
				Constants.MASTER_LEADINDUSTRY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_LEADINDUSTRY_LIST;
			
		} else if (strKey.equals("eventcategory")) {
			System.out.println("kiat 5 = "+Constants.MASTER_CATEGORY_LIST);
			if (Constants.MASTER_CATEGORY_LIST == null) {
				Constants.MASTER_CATEGORY_LIST = master.getAllMasterByType(strKey);
			}
			return Constants.MASTER_CATEGORY_LIST;
		}
		return aResult;
	}
	
	public static String toXmlDate(String strDate, String strDateFormat) {
		Date date = null;
		String strReturn = null;
		if (strDate != null && strDate.length() > 0) {
			try {
				date = new SimpleDateFormat(strDateFormat).parse(strDate);
			} catch (ParseException e2) {
				e2.printStackTrace();
				date = null;
			}
			
			if (date != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.XML_DATETIME_FORMAT);
				String strFormattedDate = sdf.format(date);
				//split it and add colon to it.
				strReturn = strFormattedDate.substring(0, strFormattedDate.length() - 2);
				strReturn += ":" + strFormattedDate.substring(strFormattedDate.length() - 2);
			}else{
				return strDate;
			}
		}
		return strReturn;
	}
	
	public static String toXmlDateTime(String strDate, String strDateFormat) {
		Date date = null;
		String strReturn = null;
		if (strDate != null) {
			try {
				date = new SimpleDateFormat(strDateFormat).parse(strDate);
			} catch (ParseException e2) {
				e2.printStackTrace();
				date = null;
			}
			
			if (date != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.XML_DATE_FORMAT);
				strReturn = sdf.format(date);
			}
		}
		return strReturn;
	}
	
	public static String xmlDateFormat(String strDate, String strFromFormat, String strToDateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(strFromFormat);
		String strReturn = null;
		Date d = null;
		try {
			d = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (d != null) {
			SimpleDateFormat sdf2 = new SimpleDateFormat(strToDateFormat);
			strReturn = sdf2.format(d);
		}
		
		return strReturn;
	}
	
	public static String xmlToDateTime(String strDate, String strDateFormat) {
		return xmlDateFormat(strDate, Constants.XML_DATETIME_FORMAT, strDateFormat);
	}
	
	public static String xmlToDate(String strDate, String strDateFormat) {
		return xmlDateFormat(strDate, Constants.XML_DATE_FORMAT, strDateFormat);
	}
	
	public static Date stringToDate(String strDate, String strFromFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(strFromFormat);
		Date d = null;
		try {
			d = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return d;
	}
	
	public static String doShowValidPhoneNumber(String strPhone) {
		String strReturn = strPhone;
		//if (strPhone != null) {
		//	if (strPhone.startsWith("6")) {
				//strReturn = strPhone.substring(1);
		//	}
		//}
		return strReturn;		
	}
	
	public static String doPhoneVerification(String strPhone) {
		String strReturn = strPhone;
		if (strPhone != null) {
			if (strPhone.startsWith("0")) {
				strReturn = "6" + strPhone;

			}
		}
		return strReturn;
	}
	
	public static String toXmlPhone(String strPhone) {
		String strReturn = strPhone;
		//if (strPhone != null && strPhone.length() > 6) {
		//	strReturn = String.format("%s(%s)%s", strPhone.substring(0, 2), strPhone.substring(2, 3), strPhone.substring(3));
		//}
		return strReturn;
	}
	
	public static String toXmlMobile(String strPhone) {
		String strReturn = strPhone;
		if (strPhone != null && strPhone.length() > 6) {
			strReturn = String.format("%s-%s", strPhone.substring(0, 2), strPhone.substring(2));
		}
		return strReturn;
	}
	
	public static String MobileFormat(String strPhone) {
		String strReturn = strPhone;
		if (strPhone != null) {
			strReturn = strPhone.replaceAll("\\D", "");
			if (strReturn.startsWith("0")) {
				strReturn = strReturn.substring(0);
			}
		}
		return strReturn;
	}
	
	public static String xmlToPhone(String strPhone) {
		String strReturn = strPhone;
		if (strPhone != null) {
			strReturn = strPhone.replaceAll("\\D", "");
			if (strReturn.startsWith("6")) {
				strReturn = strReturn.substring(1);
			}
		}
		return strReturn;
	}
	
	public static String xmlToNull(String strResponse) {
		String strReturn = strResponse;
		if (strResponse != null && strResponse.equalsIgnoreCase("anyType{}")) {
			strReturn = null;
		}
		return strReturn;
	}
	
//	public static String convertTime(String time){
//		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date d = null;
//		try {
//			d = dateFormat.parse(time);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
//		
//		return dateFormat2.format(d);
//	}
	
	public static String convertLeadTime(String time){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = dateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
		
		return dateFormat2.format(d);
	}
	
	public static String convertTaskTime(String time){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			d = dateFormat.parse(time);
			return dateFormat2.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			Date da = null;
			try {
				da = dateFormat3.parse(time);
			} catch (ParseException ex) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return dateFormat2.format(da);
		}
		
		
		
		
	}
}
