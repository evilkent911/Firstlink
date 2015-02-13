package com.trinerva.icrm.utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.trinerva.icrm.database.DatabaseHandler;
import com.trinerva.icrm.database.source.Account;
import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Broadcast;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.database.source.Opportunity;
import com.trinerva.icrm.database.source.Report;
import com.trinerva.icrm.database.source.Task;
import com.trinerva.icrm.event.ArrayEmailSchedule;
import com.trinerva.icrm.object.AccountDetail;
import com.trinerva.icrm.object.ActivitiesLogDetail;
import com.trinerva.icrm.object.BroadcastDetail;
import com.trinerva.icrm.object.CalendarDetail;
import com.trinerva.icrm.object.ContactActivity;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.LeadActivity;
import com.trinerva.icrm.object.LeadDetail;
import com.trinerva.icrm.object.MasterInfo;
import com.trinerva.icrm.object.OpportunityDetail;
import com.trinerva.icrm.object.ReportDetail;
import com.trinerva.icrm.object.TaskDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.os.NetworkOnMainThreadException;
import android.renderscript.Sampler;

public class SoapUtility {
	// public static final String URL =
	// "http://210.5.42.74:2002/MobileService/crm.svc";
	public static final String NAMESPACE = "http://www.firstlink.asia";
	public static final String TAG = "SoapUtility";
	public static final String CREDENTIAL_NAMESPACE = "http://schemas.datacontract.org/2004/07/TACRM.DataContract.Mobile";
	static int timeOut = 1000 * 60 * 30 * 2;

	public static enum STATUS {
		SUCCESS, NETWORK_ERROR, FAIL, SYNC_ERROR, AUTHENTICATE_FAIL,SYNC_ERROR_RESET
	};

	@SuppressLint("NewApi")
	public static HashMap<String, Object> call(Context context,
			SoapObject oRequest, String strSoapAction) {
		HashMap<String, Object> hReturn = new HashMap<String, Object>();
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.xsi = SoapEnvelope.XSI;
		envelope.xsd = SoapEnvelope.XSD;
		SoapObject oResult = null;
		envelope.dotNet = true;
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		envelope.setOutputSoapObject(oRequest);
		String strUrl = Utility.getConfigByText(context, "CRM_SERVER_MOBILE");
		HttpTransportSE aht = new HttpTransportSE(strUrl, timeOut);
		aht.debug = Constants.DEBUG;
		hReturn.put("STATUS", STATUS.FAIL);
		if (NetworkUtility.getNetworkStatus(context)) {
			try {
				aht.call(strSoapAction, envelope);
			} catch (Error e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				hReturn.put("STATUS", STATUS.FAIL);
			} catch (NetworkOnMainThreadException e) {
				e.printStackTrace();
			}
			if (Constants.DEBUG) {
				 //DeviceUtility.log(TAG, aht.requestDump);
				// DeviceUtility.log(TAG, aht.responseDump);
			}
			try {
				// System.out.println("aaa = "+envelope.getResponse());
				oResult = (SoapObject) envelope.getResponse();
			} catch (SoapFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (oResult != null) {
				hReturn.put("STATUS", STATUS.SUCCESS);
				// //DeviceUtility.log(TAG, oResult.toString());
			} else {
				hReturn.put("STATUS", STATUS.AUTHENTICATE_FAIL);
			}
		} else {
			hReturn.put("STATUS", STATUS.NETWORK_ERROR);
		}

		hReturn.put("SOAP_OBJECT", oResult);
		return hReturn;
	}

	public static HashMap<String, String> SoapObjectToHashMap(
			SoapObject oResponse) {
		// DeviceUtility.log(TAG, "SoapObjectToHashMap");
		HashMap<String, String> hData = null;
		if (oResponse != null) {
			int iResponseProperty = oResponse.getPropertyCount();
			if (iResponseProperty > 0) {
				hData = new HashMap<String, String>();
				for (int iDetail = 0; iDetail < iResponseProperty; iDetail++) {
					PropertyInfo pi = new PropertyInfo();
					oResponse.getPropertyInfo(iDetail, pi);
					if (oResponse.getProperty(iDetail) == null) {
						hData.put(pi.name, "");
					} else {
						hData.put(pi.name, oResponse.getProperty(iDetail)
								.toString());
					}
				}
				DeviceUtility
						.log(TAG, "SoapObjectToHashMap" + hData.toString());
			}
		}
		return hData;
	}

	public static HashMap<String, Object> doActivate(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		boolean bReturn = false;
		String strMethod = "ActivateAccount";
		String strSoapAction = "http://www.firstlink.asia/ICrm/ActivateAccount";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		oRequest.addProperty("username", strUsername);
		oRequest.addProperty("password", strPassword);
		oRequest.addProperty("refNo", strDeviceId);
		oRequest.addProperty("productKey", "000");

		// SoapObject oResponse = call(context, oRequest, strSoapAction);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done oResponse:" + oResponse);
			HashMap<String, String> hData = SoapObjectToHashMap(oResponse);

			if (hData != null) {
				// {ReturnCode=1, ReturnMessage=null}
				if (hData.containsKey("ReturnCode")) {
					if (hData.get("ReturnCode").equals("0")) {
						// activate success.
						Utility.updateConfigByText(context,
								Constants.USER_EMAIL, strUsername);
						// DeviceUtility.log(TAG,
						// "return code: " + hData.get("ReturnCode"));
						String strEmail = Utility.getConfigByText(context,
								Constants.USER_EMAIL);
						// DeviceUtility.log(TAG, "email update: " + strEmail);
						bReturn = true;
					} else {
						oResult.put("STATUS", STATUS.SYNC_ERROR);
						String strReturnMessage = hData.get("ReturnMessage")
								.toString();
						if (strReturnMessage.length() > 0) {
							oResult.put("ERROR_MESSAGE", strReturnMessage);
						}
					}
				}
			}
		}
		oResult.put("RESULT", Boolean.valueOf(bReturn));
		return oResult;
	}

	public static HashMap<String, Object> getMasterList(Context context,
			String strUsername, String strPassword, String strDeviceId,
			String strType, boolean bClean) {
		ArrayList<MasterInfo> aMasterList = null;
		String strMethod = "DownloadMasterList";
		String strSoapAction = "http://www.firstlink.asia/ICrm/DownloadMasterList";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("type", strType);

		// SoapObject oResponse = call(context, oRequest, strSoapAction);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aMasterList = new ArrayList<MasterInfo>();
				int iPropertyCount = oResponse.getPropertyCount();
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					if (iPropertyCount > 0) {
						
						SoapObject response = (SoapObject)oResponse.getProperty("ResultListItems");
						// TODO: handle exception
						for (int i = 0; i < response.getPropertyCount(); i++) {
						SoapObject pii = (SoapObject) response.getProperty(i);
						MasterInfo master = new MasterInfo(
								Boolean.parseBoolean(pii.getProperty(0)
										.toString()), pii.getProperty(1)
										.toString(), pii.getProperty(2)
										.toString(), pii.getProperty(3)
										.toString(), strType);

						aMasterList.add(master);
					}

					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else{
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}

				
				if (!aMasterList.isEmpty()) {
					DatabaseUtility.getDatabaseHandler(context);
					Master master = new Master(Constants.DBHANDLER);
					if (bClean) {
						Constants.MASTER_PREFIX_LIST = null;
						Constants.MASTER_LEADATTITUDE_LIST = null;
						Constants.MASTER_LEADSTATUS_LIST = null;
						Constants.MASTER_LEADSOURCE_LIST = null;
						Constants.MASTER_CALENDARPRIORITY_LIST = null;
						Constants.MASTER_CALENDARAVAILABILITY_LIST = null;
						Constants.MASTER_TASKPRIORITY_LIST = null;
						Constants.MASTER_TASKAVAILABILITY_LIST = null;
						Constants.MASTER_USERLIST_LIST = null;
						Constants.MASTER_OPPORTUNITYSTAGE_LIST = null;
						Constants.MASTER_TASKKIND_LIST = null;
						Constants.MASTER_LEADINDUSTRY_LIST = null;
						master.truncate();
					}
					master.insert(aMasterList);
				}
			}
		}
		oResult.put("RESULT", aMasterList);
		//System.out.println("oResult xxxxx 1 "+oResult);
		return oResult;
	}

//	public static HashMap<String, Object> getMasterListOpportunityStage(Context context,
//			String strUsername, String strPassword, String strDeviceId,
//			String strType, boolean bClean) {
//		ArrayList<MasterInfo> aMasterList = null;
//		String strMethod = "DownloadMasterList";
//		String strSoapAction = "http://www.firstlink.asia/ICrm/DownloadMasterList";
//
//		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
//		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
//				"credential");
//
//		PropertyInfo password = new PropertyInfo();
//		password.setName("Password");
//		password.setValue(strPassword);
//		password.setType(String.class);
//		password.setNamespace(CREDENTIAL_NAMESPACE);
//
//		PropertyInfo username = new PropertyInfo();
//		username.setName("Username");
//		username.setValue(strUsername);
//		username.setType(String.class);
//		username.setNamespace(CREDENTIAL_NAMESPACE);
//
//		PropertyInfo refno = new PropertyInfo();
//		refno.setName("RefNo");
//		refno.setValue(strDeviceId);
//		refno.setType(String.class);
//		refno.setNamespace(CREDENTIAL_NAMESPACE);
//
//		oCredential.addProperty(password);
//		oCredential.addProperty(refno);
//		oCredential.addProperty(username);
//
//		oRequest.addProperty("credential", oCredential);
//		oRequest.addProperty("type", strType);
//
//		// SoapObject oResponse = call(context, oRequest, strSoapAction);
//		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
//		if (oResult.get("STATUS") == STATUS.SUCCESS) {
//			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
//			// DeviceUtility.log(TAG, "call done: " + oResponse);
//
//			if (oResponse != null) {
//				aMasterList = new ArrayList<MasterInfo>();
//				int iPropertyCount = oResponse.getPropertyCount();
//				String strReturnCode = oResponse
//						.getPropertySafelyAsString("ReturnCode");
//				if (strReturnCode.equalsIgnoreCase("0")) {
//					if (iPropertyCount > 0) {
//						
//						SoapObject response = (SoapObject)oResponse.getProperty("ResultListItems");
//						// TODO: handle exception
//						for (int i = 0; i < response.getPropertyCount(); i++) {
//						SoapObject pii = (SoapObject) response.getProperty(i);
//						System.out.println("pii.getProperty(0) " + pii.getProperty(0));
//						System.out.println("pii.getProperty(1) " + pii.getProperty(1));
//						System.out.println("pii.getProperty(2) " + pii.getProperty(2));
//						System.out.println("pii.getProperty(3) " + pii.getProperty(3));
//						
//						MasterInfo master = new MasterInfo(
//								Boolean.parseBoolean(pii.getProperty(0)
//										.toString()), pii.getProperty(1)
//										.toString(), pii.getProperty(2)
//										.toString(), pii.getProperty(3)
//										.toString(), strType);
//
//						aMasterList.add(master);
//					}
//
//					}
//				} else if (strReturnCode.equalsIgnoreCase("14")) {
//					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
//				} else{
//					oResult.put("STATUS", STATUS.SYNC_ERROR);
//					String strReturnMessage = oResponse
//							.getPropertySafelyAsString("ReturnMessage");
//					if (strReturnMessage.length() > 0) {
//						oResult.put("ERROR_MESSAGE", strReturnMessage);
//					}
//				}
//
//				
//				if (!aMasterList.isEmpty()) {
//					DatabaseUtility.getDatabaseHandler(context);
//					Master master = new Master(Constants.DBHANDLER);
//					if (bClean) {
//						Constants.MASTER_PREFIX_LIST = null;
//						Constants.MASTER_LEADATTITUDE_LIST = null;
//						Constants.MASTER_LEADSTATUS_LIST = null;
//						Constants.MASTER_LEADSOURCE_LIST = null;
//						Constants.MASTER_CALENDARPRIORITY_LIST = null;
//						Constants.MASTER_CALENDARAVAILABILITY_LIST = null;
//						Constants.MASTER_TASKPRIORITY_LIST = null;
//						Constants.MASTER_TASKAVAILABILITY_LIST = null;
//						Constants.MASTER_USERLIST_LIST = null;
//						Constants.MASTER_OPPORTUNITYSTAGE_LIST = null;
//						Constants.MASTER_TASKKIND_LIST = null;
//						Constants.MASTER_LEADINDUSTRY_LIST = null;
//						master.truncate();
//					}
//					master.insertOpportunityStage(aMasterList);
//				}
//			}
//		}
//		oResult.put("RESULT", aMasterList);
//		System.out.println("oResult xxxxx 1 "+oResult);
//		return oResult;
//	}
	
	public static HashMap<String, Object> GetEmployeeListForMobileSync(
			Context context, String strUsername, String strPassword,
			String strDeviceId, String strType, boolean bClean) {
		ArrayList<MasterInfo> aMasterList = null;
		String strMethod = "GetEmployeeListForMobileSync";
		String strSoapAction = "http://www.firstlink.asia/ICrm/GetEmployeeListForMobileSync";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);
		// oRequest.addProperty("type", strType);

		// SoapObject oResponse = call(context, oRequest, strSoapAction);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aMasterList = new ArrayList<MasterInfo>();
				int iPropertyCount = oResponse.getPropertyCount();
				if (iPropertyCount > 0) {
					for (int i = 0; i < iPropertyCount; i++) {
						//System.out.println("user list 1 = " + oResponse);
						try {
							SoapPrimitive pii = (SoapPrimitive) oResponse
									.getProperty(i);
							//System.out.println("user list a = " + pii);
						} catch (Exception e) {
							// TODO: handle exception
							SoapObject pii = (SoapObject) oResponse
									.getProperty(i);
							for (int j = 0; j < pii.getPropertyCount(); j++) {
								System.out.println("======= ");
								SoapObject pii2 = (SoapObject) pii
										.getProperty(j);
//								for (int k = 0; k < pii2.getPropertyCount(); k++) {
									System.out.println("user list a = "
											+ pii2.getProperty(6));
									System.out.println("user list b = "
											+ pii2.getProperty(7));
									System.out.println("user list c = "
											+ pii2.getProperty(8));
									MasterInfo master = new MasterInfo(pii2
											.getProperty(8).toString(), pii2
											.getProperty(6).toString(), pii2
											.getProperty(7).toString(), strType);

									aMasterList.add(master);
//								}
							}
						}

						// SoapObject pii = (SoapObject)
						// oResponse.getProperty(i);
						// MasterInfo master = new MasterInfo(
						// Boolean.parseBoolean(pii.getProperty(0)
						// .toString()), pii.getProperty(1)
						// .toString(), pii.getProperty(2)
						// .toString(), strType);
						//
						// aMasterList.add(master);
					}
				}

				if (!aMasterList.isEmpty()) {
					DatabaseUtility.getDatabaseHandler(context);
					Master master = new Master(Constants.DBHANDLER);
					if (bClean) {
						Constants.MASTER_PREFIX_LIST = null;
						Constants.MASTER_LEADATTITUDE_LIST = null;
						Constants.MASTER_LEADSTATUS_LIST = null;
						Constants.MASTER_LEADSOURCE_LIST = null;
						Constants.MASTER_CALENDARPRIORITY_LIST = null;
						Constants.MASTER_CALENDARAVAILABILITY_LIST = null;
						Constants.MASTER_TASKPRIORITY_LIST = null;
						Constants.MASTER_TASKAVAILABILITY_LIST = null;
						Constants.MASTER_USERLIST_LIST = null;
						Constants.MASTER_OPPORTUNITYSTAGE_LIST = null;
						Constants.MASTER_TASKKIND_LIST = null;
						Constants.MASTER_LEADINDUSTRY_LIST = null;
						master.truncate();
					}
					master.insert(aMasterList);
				}
			}
		}
		oResult.put("RESULT", aMasterList);
		return oResult;
	}

	public static HashMap<String, Object> getSyncAccount(Context context,
			String strUsername, String strPassword, String strDeviceId, String strType,
			boolean bClean) {
		ArrayList<AccountDetail> aMasterList = null;
		String strMethod = "SyncAccount";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncAccount";
		String strConfigKey = "Account";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);
		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");

			if (oResponse != null) {
				aMasterList = new ArrayList<AccountDetail>();

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp");
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oContactList = (SoapObject) oResponse
							.getProperty("Accounts");
					int iContactList = oContactList.getPropertyCount();
					for (int i = 0; i < iContactList; i++) {
						SoapObject oContactObject = (SoapObject) oContactList
								.getProperty(i);
						AccountDetail contactResponse = new AccountDetail();
						contactResponse.setAccountId(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("AccountID")
										.toString()));
						contactResponse.setAccountName(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("AccountName")
										.toString()));
						contactResponse.setActive(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Active")
										.toString()));
						contactResponse.setCreatedTimestamp(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("CreatedDate")
										.toString()));
						contactResponse.setModifiedTimestamp(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("ModifiedDate")
										.toString()));
						aMasterList.add(contactResponse);
					}

					if (aMasterList.size() > 0) {
						DatabaseUtility.getDatabaseHandler(context);
						Account oContactSource = new Account(
								Constants.DBHANDLER);
						oContactSource.insertUpdate(aMasterList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aMasterList);
		
/////////////////

//ArrayList<ContactDetail> aContactsList = null;
//String strMethod = "SyncContact";
//String strSoapAction = "http://www.firstlink.asia/ICrm/SyncContact";
//String strConfigKey = "Contact";
//String strTimestamp = Utility.getConfigByText(context, strConfigKey);
//
//SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
//SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
//		"credential");
//
//PropertyInfo password = new PropertyInfo();
//password.setName("Password");
//password.setValue(strPassword);
//password.setType(String.class);
//password.setNamespace(CREDENTIAL_NAMESPACE);
//
//PropertyInfo username = new PropertyInfo();
//username.setName("Username");
//username.setValue(strUsername);
//username.setType(String.class);
//username.setNamespace(CREDENTIAL_NAMESPACE);
//
//PropertyInfo refno = new PropertyInfo();
//refno.setName("RefNo");
//refno.setValue(strDeviceId);
//refno.setType(String.class);
//refno.setNamespace(CREDENTIAL_NAMESPACE);
//
//oCredential.addProperty(password);
//oCredential.addProperty(refno);
//oCredential.addProperty(username);
//
//SoapObject oContacts = new SoapObject(CREDENTIAL_NAMESPACE, "contacts");
//// add all unsync into contacts
//DatabaseUtility.getDatabaseHandler(context);
//Contact contact = new Contact(Constants.DBHANDLER);
//ArrayList<ContactDetail> aContact = contact.getUnsyncContact();
//int iUnsyncContact = aContact.size();
//if (iUnsyncContact > 0) {
//	for (int i = 0; i < iUnsyncContact; i++) {
//		ContactDetail c = aContact.get(i);
//		PropertyInfo Obj1 = new PropertyInfo();
//		Obj1.setName("Contact");
//		Obj1.setValue(c);
//		Obj1.setType(c.getClass());
//		Obj1.setNamespace(CREDENTIAL_NAMESPACE);
//		oContacts.addProperty(Obj1);
//	}
//}
//// DeviceUtility.log(TAG,
//// "oContacts.getNamespace(): " + oContacts.getNamespace());
//
//oRequest.addProperty("credential", oCredential);
//oRequest.addProperty("lastSyncTimestamp", strTimestamp);
//oRequest.addProperty("contacts", oContacts);
//
//HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
//if (oResult.get("STATUS") == STATUS.SUCCESS) {
//	SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
//	// DeviceUtility.log(TAG, "call done: " + oResponse);
//
//	if (oResponse != null) {
//		aContactsList = new ArrayList<ContactDetail>();
//
//		DeviceUtility.log(
//				TAG,
//				"oResult.getProperty(0): "
//						+ oResponse
//								.getPropertySafelyAsString("Contacts"));
//		DeviceUtility
//				.log(TAG,
//						"oResult.getProperty(1): "
//								+ oResponse
//										.getPropertySafelyAsString("LastSyncTimestamp"));
//		DeviceUtility
//				.log(TAG,
//						"oResult.getProperty(2): "
//								+ oResponse
//										.getPropertySafelyAsString("ReturnCode"));
//		DeviceUtility
//				.log(TAG,
//						"oResult.getProperty(3): "
//								+ oResponse
//										.getPropertySafelyAsString("ReturnMessage"));
//
//		String strLastUpdateTimestamp = oResponse
//				.getPropertySafelyAsString("LastSyncTimestamp");
//		String strReturnCode = oResponse
//				.getPropertySafelyAsString("ReturnCode");
//		if (strReturnCode.equalsIgnoreCase("0")) {
//			// update the timestamp.
//			Utility.updateConfigByText(context, strConfigKey,
//					strLastUpdateTimestamp);
//			SoapObject oContactList = (SoapObject) oResponse
//					.getProperty("Contacts");
//			int iContactList = oContactList.getPropertyCount();
//			for (int i = 0; i < iContactList; i++) {
//				SoapObject oContactObject = (SoapObject) oContactList
//						.getProperty(i);
//				ContactDetail contactResponse = new ContactDetail();
//				contactResponse.setIsUpdate("true");
//				contactResponse.setActive(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("Active")
//								.toString()));
//				contactResponse.setAssistantName(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"AssistantName").toString()));
//				contactResponse.setAssistantPhone(Utility
//						.xmlToPhone(Utility.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"AssistantPhone").toString())));
//				String strBirthday = Utility.xmlToNull(oContactObject
//						.getPropertySafelyAsString("Birthday")
//						.toString());
//				if (strBirthday.equals(Constants.DEFAULT_DATE)) {
//					contactResponse.setBirthday(null);
//				} else {
//					contactResponse.setBirthday(Utility.xmlToDate(
//							strBirthday, Constants.DATE_FORMAT));
//				}
//
//				contactResponse.setCompanyName(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"CompanyName").toString()));
//
//				contactResponse.setCompanyId(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"ContactAccount").toString()));
//				contactResponse.setContactId(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("ContactID")
//								.toString()));
//				contactResponse
//						.setDepartment(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString(
//												"Department")
//										.toString()));
//				contactResponse.setDescription(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"Description").toString()));
//				contactResponse.setEmail1(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("Email1")
//								.toString()));
//				contactResponse.setEmail2(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("Email2")
//								.toString()));
//				contactResponse.setEmail3(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("Email3")
//								.toString()));
//				contactResponse.setFirstName(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("FirstName")
//								.toString()));
//				contactResponse.setGpsLat(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"GpsLatitude").toString()));
//				contactResponse.setGpsLong(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"GpsLongitude").toString()));
//				contactResponse.setHomePhone(Utility.xmlToPhone(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("HomePhone")
//								.toString())));
//				contactResponse
//						.setInternalNumber(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString(
//												"IContactID")
//										.toString()));
//				contactResponse.setJobTitle(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("JobTitle")
//								.toString()));
//				contactResponse.setLastName(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("LastName")
//								.toString()));
//				contactResponse.setMailingCity(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"MailingCity").toString()));
//				contactResponse.setMailingCountry(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"MailingCountry").toString()));
//				contactResponse.setMailingState(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"MailingState").toString()));
//				contactResponse.setMailingStreet(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"MailingStreet").toString()));
//				contactResponse
//						.setMailingZip(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString(
//												"MailingZip")
//										.toString()));
//				contactResponse.setMobile(Utility
//						.xmlToPhone(oContactObject
//								.getPropertySafelyAsString("Mobile")
//								.toString()));
//				contactResponse.setModifiedTimestamp(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"ModifiedDate").toString()));
//				contactResponse.setCreatedTimestamp(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString(
//										"CreatedDate").toString()));
//				contactResponse.setOtherPhone(Utility
//						.xmlToPhone(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString(
//												"OtherPhone")
//										.toString())));
//				contactResponse.setOwner(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("Owner")
//								.toString()));
//				contactResponse.setPrefix(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("Prefix")
//								.toString()));
//				contactResponse.setSkypeId(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("SkypeID")
//								.toString()));
//				contactResponse
//						.setUpdateGpsLocation(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString(
//												"UpdateGpsLocation")
//										.toString()));
//				contactResponse.setWorkFax(Utility.xmlToPhone(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("WorkFax")
//								.toString())));
//				contactResponse.setWorkPhone(Utility.xmlToPhone(Utility
//						.xmlToNull(oContactObject
//								.getPropertySafelyAsString("WorkPhone")
//								.toString())));
//				aContactsList.add(contactResponse);
//			}
//
//			if (aContactsList.size() > 0) {
//				DatabaseUtility.getDatabaseHandler(context);
//				Contact oContactSource = new Contact(
//						Constants.DBHANDLER);
//				oContactSource.insertUpdate(aContactsList);
//			}
//		} else {
//			oResult.put("STATUS", STATUS.SYNC_ERROR);
//			String strReturnMessage = oResponse
//					.getPropertySafelyAsString("ReturnMessage");
//			if (strReturnMessage.length() > 0) {
//				oResult.put("ERROR_MESSAGE", strReturnMessage);
//			}
//		}
//	}
//}
//oResult.put("RESULT", aContactsList);
//return oResult;

///////////////////
		return oResult;
	}

	public static HashMap<String, Object> sendForgotPasswordEmail(
			Context context, String strUsername, String strPassword,
			String strDeviceId) {
		ArrayList<MasterInfo> aMasterList = null;
		String strMethod = "SendForgotPasswordEmail";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SendForgotPasswordEmail";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);

		// SoapObject oResponse = call(context, oRequest, strSoapAction);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			// if (oResponse != null) {
			// aMasterList = new ArrayList<MasterInfo>();
			// int iPropertyCount = oResponse.getPropertyCount();
			// if (iPropertyCount > 0) {
			// for (int i = 0; i < iPropertyCount; i++) {
			// SoapObject pii = (SoapObject) oResponse.getProperty(i);
			//
			// System.out.println("pppsss = "+pii.getProperty(0));
			// // MasterInfo master = new MasterInfo(
			// // Boolean.parseBoolean(pii.getProperty(0)
			// // .toString()), pii.getProperty(1)
			// // .toString(), pii.getProperty(2)
			// // .toString(), strType);
			// // aMasterList.add(master);
			// }
			// }
			//
			// }
		}
		oResult.put("RESULT", aMasterList);
		
		return oResult;
	}

	public static HashMap<String, Object> passwordRecovery(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<MasterInfo> aMasterList = null;
		String strMethod = "GetEmployeeSecurityQuestion";
		String strSoapAction = "http://www.firstlink.asia/ICrm/GetEmployeeSecurityQuestion";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);

		// SoapObject oResponse = call(context, oRequest, strSoapAction);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);

		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aMasterList = new ArrayList<MasterInfo>();
				int iPropertyCount = oResponse.getPropertyCount();

				System.out.println("iPropertyCount = " + iPropertyCount);
				// if (iPropertyCount > 0) {
				// for (int i = 0; i < iPropertyCount; i++) {
				//
				// if (i == 2) {
				// SoapObject pii = (SoapObject) oResponse
				// .getProperty(2);
				//
				// System.out.println("pp = " + pii.toString());
				// SoapObject pii2 = (SoapObject) pii.getProperty(0);
				// System.out.println("2 = " + pii2);
				// MasterInfo master;
				// if (pii2.getProperty("PwdQuestion").toString() == null) {
				// master = new MasterInfo(false, pii2
				// .getProperty("PwdQuestion").toString(),
				// pii2.getProperty("PwdAnswear")
				// .toString());
				// } else {
				// master = new MasterInfo(true, pii2.getProperty(
				// "PwdQuestion").toString(), pii2
				// .getProperty("PwdAnswear").toString());
				// }
				// aMasterList.add(master);
				// oResult.put("RESULT", aMasterList);
				//
				// }
				// }
				// }
			}
		}

		return oResult;
	}

	public static HashMap<String, Object> deletePermission(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<MasterInfo> aMasterList = null;
		String strMethod = "GetEmployeeSecurityQuestion";
		String strSoapAction = "http://www.firstlink.asia/ICrm/GetEmployeeSecurityQuestion";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);

		// SoapObject oResponse = call(context, oRequest, strSoapAction);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);

		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aMasterList = new ArrayList<MasterInfo>();
				int iPropertyCount = oResponse.getPropertyCount();

				System.out.println("iPropertyCount = " + iPropertyCount);

				aMasterList = new ArrayList<MasterInfo>();
				// int iPropertyCount = oResponse.getPropertyCount();
				// if (iPropertyCount > 0) {
				// for (int i = 0; i < iPropertyCount; i++) {
				// SoapObject pii = (SoapObject) oResponse.getProperty(i);
				// MasterInfo master = new MasterInfo(
				// Boolean.parseBoolean(pii.getProperty(0)
				// .toString()), pii.getProperty(1)
				// .toString(), pii.getProperty(2)
				// .toString(), strType);
				// aMasterList.add(master);
				// }
				// }

				SoapObject pii = (SoapObject) oResponse.getProperty(2);

				SoapObject pii2 = (SoapObject) pii.getProperty(0);

				DatabaseHandler dataHandler = new DatabaseHandler(context);
				dataHandler.getWritableDatabase().delete(
						DatabaseHandler.TABLE_FL_CONFIG, "CONFIG_TEXT = ?",
						new String[] { "DeleteAll" });
				dataHandler.getWritableDatabase().delete(
						DatabaseHandler.TABLE_FL_CONFIG, "CONFIG_TEXT = ?",
						new String[] { "DeleteContact" });
				dataHandler.getWritableDatabase().delete(
						DatabaseHandler.TABLE_FL_CONFIG, "CONFIG_TEXT = ?",
						new String[] { "DeleteEvent" });
				dataHandler.getWritableDatabase().delete(
						DatabaseHandler.TABLE_FL_CONFIG, "CONFIG_TEXT = ?",
						new String[] { "DeleteLead" });
				dataHandler.getWritableDatabase().delete(
						DatabaseHandler.TABLE_FL_CONFIG, "CONFIG_TEXT = ?",
						new String[] { "DeleteOpportunity" });
				dataHandler.getWritableDatabase().delete(
						DatabaseHandler.TABLE_FL_CONFIG, "CONFIG_TEXT = ?",
						new String[] { "DeleteTask" });
				// dataHandler.getWritableDatabase().delete(
				// DatabaseHandler.TABLE_FL_CONFIG, "INTERNAL_NUM = ?",
				// new String[] { "18" });
				// dataHandler.getWritableDatabase().delete(
				// DatabaseHandler.TABLE_FL_CONFIG, "INTERNAL_NUM = ?",
				// new String[] { "19" });
				// dataHandler.getWritableDatabase().delete(
				// DatabaseHandler.TABLE_FL_CONFIG, "INTERNAL_NUM = ?",
				// new String[] { "20" });
				// dataHandler.getWritableDatabase().delete(
				// DatabaseHandler.TABLE_FL_CONFIG, "INTERNAL_NUM = ?",
				// new String[] { "21" });
				// dataHandler.getWritableDatabase().delete(
				// DatabaseHandler.TABLE_FL_CONFIG, "INTERNAL_NUM = ?",
				// new String[] { "22" });
				// dataHandler.getWritableDatabase().delete(
				// DatabaseHandler.TABLE_FL_CONFIG, "INTERNAL_NUM = ?",
				// new String[] { "23" });
				// String deleteData = "INSERT INTO "
				// + DatabaseHandler.TABLE_FL_CONFIG
				// +
				// " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
				// + "VALUES ('SYSTEM', '" + pii2.getProperty("DeleteTask")
				// + "', '" + "DeleteTask" + "','SYSTEM',datetime('now'));";
				// Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				String strData = "INSERT INTO "
						+ DatabaseHandler.TABLE_FL_CONFIG
						+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
						+ "VALUES ('SYSTEM', '" + pii2.getProperty("DeleteAll")
						+ "', '" + "DeleteAll" + "','SYSTEM',datetime('now'));";
				Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				strData = "INSERT INTO "
						+ DatabaseHandler.TABLE_FL_CONFIG
						+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
						+ "VALUES ('SYSTEM', '"
						+ pii2.getProperty("DeleteContact") + "', '"
						+ "DeleteContact" + "','SYSTEM',datetime('now'));";
				Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				strData = "INSERT INTO "
						+ DatabaseHandler.TABLE_FL_CONFIG
						+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
						+ "VALUES ('SYSTEM', '"
						+ pii2.getProperty("DeleteEvent") + "', '"
						+ "DeleteEvent" + "','SYSTEM',datetime('now'));";
				Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				strData = "INSERT INTO "
						+ DatabaseHandler.TABLE_FL_CONFIG
						+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
						+ "VALUES ('SYSTEM', '"
						+ pii2.getProperty("DeleteLead") + "', '"
						+ "DeleteLead" + "','SYSTEM',datetime('now'));";
				Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				strData = "INSERT INTO "
						+ DatabaseHandler.TABLE_FL_CONFIG
						+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
						+ "VALUES ('SYSTEM', '"
						+ pii2.getProperty("DeleteOpportunity") + "', '"
						+ "DeleteOpportunity" + "','SYSTEM',datetime('now'));";
				Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				strData = "INSERT INTO "
						+ DatabaseHandler.TABLE_FL_CONFIG
						+ " (CONFIG_TYPE, CONFIG_VALUE, CONFIG_TEXT,USER_STAMP,CREATED_TIMESTAMP)"
						+ "VALUES ('SYSTEM', '"
						+ pii2.getProperty("DeleteTask") + "', '"
						+ "DeleteTask" + "','SYSTEM',datetime('now'));";
				Constants.DBHANDLER.getWritableDatabase().execSQL(strData);

				// for (int i = 0; i < pii2.getPropertyCount(); i++) {
				// System.out.println("xxx my = "+pii2.getProperty("PwdQuestion"));
				// }

				// if (iPropertyCount > 0) {
				// for (int i = 0; i < iPropertyCount; i++) {
				//
				// if (i == 2) {
				// SoapObject pii = (SoapObject) oResponse
				// .getProperty(2);
				//
				// System.out.println("pp = " + pii.toString());
				// SoapObject pii2 = (SoapObject) pii.getProperty(0);
				// System.out.println("2 = " + pii2);
				// MasterInfo master;
				// if (pii2.getProperty("PwdQuestion").toString() == null) {
				// master = new MasterInfo(false, pii2
				// .getProperty("PwdQuestion").toString(),
				// pii2.getProperty("PwdAnswear")
				// .toString());
				// } else {
				// master = new MasterInfo(true, pii2.getProperty(
				// "PwdQuestion").toString(), pii2
				// .getProperty("PwdAnswear").toString());
				// }
				// aMasterList.add(master);
				// oResult.put("RESULT", aMasterList);
				//
				// }
				// }
				// }

				Utility.updateConfigUserId(pii2.getProperty("EmployeeID")
						.toString());
			}
		}

		return oResult;
	}

	public static HashMap<String, Object> getBroadcast(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<BroadcastDetail> aBroadcastList = null;
		String strMethod = "SyncBroadcast";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncBroadcast";
		String strConfigKey = "Broadcast";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oBroadcast = new SoapObject(CREDENTIAL_NAMESPACE,
				"broadcasts");

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);
		oRequest.addProperty("broadcasts", oBroadcast);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aBroadcastList = new ArrayList<BroadcastDetail>();

				DeviceUtility
						.log(TAG,
								"oResult.getProperty(0): "
										+ oResponse
												.getPropertySafelyAsString("Broadcasts"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(1): "
										+ oResponse
												.getPropertySafelyAsString("LastSyncTimestamp"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(2): "
										+ oResponse
												.getPropertySafelyAsString("ReturnCode"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(3): "
										+ oResponse
												.getPropertySafelyAsString("ReturnMessage"));

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp")
						.toString();
				String strReturnCode = oResponse.getPropertySafelyAsString(
						"ReturnCode").toString();
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oBroadcastList = (SoapObject) oResponse
							.getProperty("Broadcasts");
					int iBroadcastList = oBroadcastList.getPropertyCount();
					for (int i = 0; i < iBroadcastList; i++) {
						SoapObject oBroadcastObject = (SoapObject) oBroadcastList
								.getProperty(i);
						BroadcastDetail broadcast = new BroadcastDetail();
						broadcast.setSubject(oBroadcastObject
								.getPropertySafelyAsString("Subject")
								.toString());
						broadcast.setAnnouncementID(oBroadcastObject
								.getPropertySafelyAsString("AnnouncementID")
								.toString());

						broadcast
								.setUserDef7(oBroadcastObject
										.getPropertySafelyAsString(
												"AnnouncementStatus")
										.toString());
						broadcast
								.setUserDef8(oBroadcastObject
										.getPropertySafelyAsString("Active")
										.toString());
						broadcast.setReleasedDate(oBroadcastObject
								.getPropertySafelyAsString("ReleasedDate")
								.toString().replace("T", " "));
						broadcast.setReleasedBy(oBroadcastObject
								.getPropertySafelyAsString("ReleasedBy")
								.toString());
						broadcast.setBroadcastContent(oBroadcastObject
								.getPropertySafelyAsString(
										"AnnouncementContent").toString());

						broadcast.setCreatedTimestamp(oBroadcastObject
								.getPropertySafelyAsString("CreatedDate")
								.toString());
						broadcast.setUserStamp(strUsername);

						aBroadcastList.add(broadcast);
					}
					if (aBroadcastList.size() > 0) {
						DatabaseUtility.getDatabaseHandler(context);
						Broadcast oBroadcastSource = new Broadcast(
								Constants.DBHANDLER);
						oBroadcastSource.insertUpdate(context,aBroadcastList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aBroadcastList);
		return oResult;
	}

	public static HashMap<String, Object> doSyncContact(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<ContactDetail> aContactsList = null;
		String strMethod = "SyncContact";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncContact";
		String strConfigKey = "Contact";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oContacts = new SoapObject(CREDENTIAL_NAMESPACE, "contacts");
		// add all unsync into contacts
		DatabaseUtility.getDatabaseHandler(context);
		Contact contact = new Contact(Constants.DBHANDLER);
		ArrayList<ContactDetail> aContact = contact.getUnsyncContact();
		int iUnsyncContact = aContact.size();
		if (iUnsyncContact > 0) {
			for (int i = 0; i < iUnsyncContact; i++) {
				ContactDetail c = aContact.get(i);
				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("Contact");
				Obj1.setValue(c);
				Obj1.setType(c.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oContacts.addProperty(Obj1);
			}
		}
		// DeviceUtility.log(TAG,
		// "oContacts.getNamespace(): " + oContacts.getNamespace());

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);
		oRequest.addProperty("contacts", oContacts);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aContactsList = new ArrayList<ContactDetail>();

				DeviceUtility.log(
						TAG,
						"oResult.getProperty(0): "
								+ oResponse
										.getPropertySafelyAsString("Contacts"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(1): "
										+ oResponse
												.getPropertySafelyAsString("LastSyncTimestamp"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(2): "
										+ oResponse
												.getPropertySafelyAsString("ReturnCode"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(3): "
										+ oResponse
												.getPropertySafelyAsString("ReturnMessage"));

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp");
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oContactList = (SoapObject) oResponse
							.getProperty("Contacts");
					int iContactList = oContactList.getPropertyCount();
					for (int i = 0; i < iContactList; i++) {
						SoapObject oContactObject = (SoapObject) oContactList
								.getProperty(i);
						ContactDetail contactResponse = new ContactDetail();
						contactResponse.setIsUpdate("true");
						contactResponse.setActive(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Active")
										.toString()));
						contactResponse.setAssistantName(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"AssistantName").toString()));
						contactResponse.setAssistantPhone(Utility
								.xmlToPhone(Utility.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"AssistantPhone").toString())));
						String strBirthday = Utility.xmlToNull(oContactObject
								.getPropertySafelyAsString("Birthday")
								.toString());
						if (strBirthday.equals(Constants.DEFAULT_DATE)) {
							contactResponse.setBirthday(null);
						} else {
							contactResponse.setBirthday(Utility.xmlToDate(
									strBirthday, Constants.DATE_FORMAT));
						}

						contactResponse.setCompanyName(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"CompanyName").toString()));

						contactResponse.setCompanyId(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"ContactAccount").toString()));
						contactResponse.setContactId(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("ContactID")
										.toString()));
						contactResponse
								.setDepartment(Utility
										.xmlToNull(oContactObject
												.getPropertySafelyAsString(
														"Department")
												.toString()));
						contactResponse.setDescription(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"Description").toString()));
						contactResponse.setEmail1(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Email1")
										.toString()));
						contactResponse.setEmail2(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Email2")
										.toString()));
						contactResponse.setEmail3(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Email3")
										.toString()));
						contactResponse.setFirstName(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("FirstName")
										.toString()));
						contactResponse.setGpsLat(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"GpsLatitude").toString()));
						contactResponse.setGpsLong(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"GpsLongitude").toString()));
						contactResponse.setHomePhone(Utility.xmlToPhone(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("HomePhone")
										.toString())));
						contactResponse
								.setInternalNumber(Utility
										.xmlToNull(oContactObject
												.getPropertySafelyAsString(
														"IContactID")
												.toString()));
						contactResponse.setJobTitle(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("JobTitle")
										.toString()));
						contactResponse.setLastName(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("LastName")
										.toString()));
						contactResponse.setMailingCity(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"MailingCity").toString()));
						contactResponse.setMailingCountry(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"MailingCountry").toString()));
						contactResponse.setMailingState(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"MailingState").toString()));
						contactResponse.setMailingStreet(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"MailingStreet").toString()));
						contactResponse
								.setMailingZip(Utility
										.xmlToNull(oContactObject
												.getPropertySafelyAsString(
														"MailingZip")
												.toString()));
						
						contactResponse.setMobile(Utility
								.xmlToPhone(Utility.xmlToNull(oContactObject
										.getPropertySafelyAsString("Mobile")
										.toString())));
						
						contactResponse.setModifiedTimestamp(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"ModifiedDate").toString()));
						contactResponse.setCreatedTimestamp(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString(
												"CreatedDate").toString()));
//						contactResponse.setOtherPhone(Utility
//								.xmlToPhone(Utility
//										.xmlToNull(oContactObject
//												.getPropertySafelyAsString(
//														"OtherPhone")
//												.toString())));
						contactResponse.setOtherPhone(Utility
								.xmlToNull(oContactObject
												.getPropertySafelyAsString(
														"OtherPhone")
												.toString()));
						contactResponse.setOwner(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Owner")
										.toString()));
						contactResponse.setPrefix(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("Prefix")
										.toString()));
						contactResponse.setSkypeId(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("SkypeID")
										.toString()));
						contactResponse
								.setUpdateGpsLocation(Utility
										.xmlToNull(oContactObject
												.getPropertySafelyAsString(
														"UpdateGpsLocation")
												.toString()));
//						contactResponse.setWorkFax(Utility.xmlToPhone(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString("WorkFax")
//										.toString())));
//						contactResponse.setWorkPhone(Utility.xmlToPhone(Utility
//								.xmlToNull(oContactObject
//										.getPropertySafelyAsString("WorkPhone")
//										.toString())));
						contactResponse.setWorkFax(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("WorkFax")
										.toString()));
						contactResponse.setWorkPhone(Utility
								.xmlToNull(oContactObject
										.getPropertySafelyAsString("WorkPhone")
										.toString()));
						aContactsList.add(contactResponse);
					}

					if (aContactsList.size() > 0) {
						DatabaseUtility.getDatabaseHandler(context);
						Contact oContactSource = new Contact(
								Constants.DBHANDLER);
						oContactSource.insertUpdate(aContactsList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aContactsList);
		return oResult;
	}

	public static HashMap<String, Object> doSyncOpportunity(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<OpportunityDetail> aOpportunityList = null;
		String strMethod = "SyncOpportunity";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncOpportunity";
		String strConfigKey = "Opportunity";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oOpportunity = new SoapObject(CREDENTIAL_NAMESPACE,
				"opportunities");
		// add all unsync into contacts
		DatabaseUtility.getDatabaseHandler(context);
		Opportunity oOpportunitySource = new Opportunity(Constants.DBHANDLER);
		ArrayList<OpportunityDetail> aOpportunity = oOpportunitySource
				.getUnsyncOpportunity();
		int iUnsyncOpportunity = aOpportunity.size();
		if (iUnsyncOpportunity > 0) {
			for (int i = 0; i < iUnsyncOpportunity; i++) {
				OpportunityDetail opp = aOpportunity.get(i);
				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("Opportunity");
				Obj1.setValue(opp);
				Obj1.setType(opp.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oOpportunity.addProperty(Obj1);
			}
		}
		// DeviceUtility.log(TAG,
		// "oOpportunity.getNamespace(): " + oOpportunity.getNamespace());
		
		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);
		oRequest.addProperty("opportunities", oOpportunity);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			 //DeviceUtility.log(TAG, "call done: " + oResponse);
			System.out.println("call done: " + oResponse);
			if (oResponse != null) {
				aOpportunityList = new ArrayList<OpportunityDetail>();

				DeviceUtility
						.log(TAG,
								"oResult.getProperty(0): "
										+ oResponse
												.getPropertySafelyAsString("Opportunities"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(1): "
										+ oResponse
												.getPropertySafelyAsString("LastSyncTimestamp"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(2): "
										+ oResponse
												.getPropertySafelyAsString("ReturnCode"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(3): "
										+ oResponse
												.getPropertySafelyAsString("ReturnMessage"));

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp");
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oOppList = (SoapObject) oResponse
							.getProperty("Opportunities");
					int iOppList = oOppList.getPropertyCount();
					for (int i = 0; i < iOppList; i++) {
						SoapObject oOppObject = (SoapObject) oOppList
								.getProperty(i);
						OpportunityDetail opportunityResponse = new OpportunityDetail();
						opportunityResponse.setIsUpdate("true");
						opportunityResponse.setActive(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Active")
										.toString()));
						opportunityResponse.setContactId(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("ContactID")
										.toString()));
						opportunityResponse.setInternalNum(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"IOpportunityID").toString()));
						opportunityResponse.setModifiedTimestamp(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"ModifiedDate").toString()));
						opportunityResponse.setCreatedTimestamp(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"CreatedDate").toString()));
						opportunityResponse
								.setOppAmount(Utility
										.xmlToNull(oOppObject
												.getPropertySafelyAsString(
														"OpportunityAmount")
												.toString()));
						opportunityResponse.setOppDate(Utility.xmlToDate(
								Utility.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"OpportunityDate").toString()),
								Constants.DATE_FORMAT));
						opportunityResponse.setOppDesc(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"OpportunityDescription")
										.toString()));
						opportunityResponse.setOppId(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"OpportunityID").toString()));
						opportunityResponse.setOppName(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"OpportunityName").toString()));
						opportunityResponse
								.setOppStage(Utility.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"OpportunityStage").toString()));
						aOpportunityList.add(opportunityResponse);
					}

					if (aOpportunityList.size() > 0) {
						oOpportunitySource.insertUpdate(aOpportunityList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aOpportunityList);
		return oResult;
	}

	public static HashMap<String, Object> doSyncLead(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<LeadDetail> aLeadList = null;
		String strMethod = "SyncLead";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncLead";
		String strConfigKey = "Lead";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oLead = new SoapObject(CREDENTIAL_NAMESPACE, "leads");
		// add all unsync into contacts
		DatabaseUtility.getDatabaseHandler(context);
		Lead oLeadSource = new Lead(Constants.DBHANDLER);
		ArrayList<LeadDetail> aLead = oLeadSource.getUnsyncLead();
		int iUnsyncLead = aLead.size();
		if (iUnsyncLead > 0) {
			for (int i = 0; i < iUnsyncLead; i++) {
				LeadDetail lead = aLead.get(i);
				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("Lead");
				Obj1.setValue(lead);
				Obj1.setType(lead.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oLead.addProperty(Obj1);
			}
		}
		// DeviceUtility.log(TAG, "oLead.getNamespace(): " +
		// oLead.getNamespace());

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);
		oRequest.addProperty("leads", oLead);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aLeadList = new ArrayList<LeadDetail>();
				// DeviceUtility.log(
				// TAG,
				// "oResult.getProperty(0): "
				// + oResponse.getPropertySafelyAsString("Leads"));
				// DeviceUtility
				// .log(TAG,
				// "oResult.getProperty(1): "
				// + oResponse
				// .getPropertySafelyAsString("LastSyncTimestamp"));
				// DeviceUtility
				// .log(TAG,
				// "oResult.getProperty(2): "
				// + oResponse
				// .getPropertySafelyAsString("ReturnCode"));
				// DeviceUtility
				// .log(TAG,
				// "oResult.getProperty(3): "
				// + oResponse
				// .getPropertySafelyAsString("ReturnMessage"));

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp");
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oLeadList = (SoapObject) oResponse
							.getProperty("Leads");
					int iOppList = oLeadList.getPropertyCount();
					for (int i = 0; i < iOppList; i++) {
						SoapObject oOppObject = (SoapObject) oLeadList
								.getProperty(i);
						LeadDetail leadResponse = new LeadDetail();
						leadResponse.setIsUpdate("true");
						leadResponse.setActive(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Active")
										.toString()));
						leadResponse.setAnnualRevenue(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"AnnualRevenue").toString()));
						leadResponse.setAttitude(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("Attitude")
								.toString()));
						leadResponse.setMailingCity(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("City")
										.toString()));
						String strBirthday = Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("Birthday")
								.toString());
						if (strBirthday.equals(Constants.DEFAULT_DATE)) {
							leadResponse.setBirthday(null);
						} else {
							leadResponse.setBirthday(Utility.xmlToDate(
									strBirthday, Constants.DATE_FORMAT));
						}
						
						leadResponse.setCompanyName(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Company")
										.toString()));
						leadResponse.setMailingCountry(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Country")
										.toString()));
						leadResponse.setDescription(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"Description").toString()));
						leadResponse.setEmail1(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Email1")
										.toString()));
						leadResponse.setEmail2(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Email2")
										.toString()));
						leadResponse.setEmail3(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Email3")
										.toString()));
						leadResponse.setFirstName(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("FirstName")
								.toString()));
						leadResponse.setGpsLat(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("GpsLatitude")
								.toString()));
						leadResponse.setGpsLong(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("GpsLongitude")
								.toString()));
						leadResponse.setInternalNum(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("ILeadID")
										.toString()));
						leadResponse.setIndustry(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("Industry")
								.toString()));
						leadResponse.setJobTitle(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("JobTitle")
								.toString()));
						leadResponse.setLastName(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("LastName")
								.toString()));
						leadResponse.setLeadId(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("LeadID")
										.toString()));
						leadResponse.setLeadSource(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("LeadSource")
								.toString()));
						leadResponse.setLeadStatus(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("LeadStatus")
								.toString()));
						leadResponse.setMobile(Utility.xmlToPhone(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Mobile")
										.toString())));
						leadResponse.setModifiedTimestamp(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"ModifiedDate").toString()));
						leadResponse.setCreatedTimestamp(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"CreatedDate").toString()));
						leadResponse.setNoOfEmployee(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString(
												"NoOfEmployee").toString()));
						leadResponse
								.setOwner(Utility.xmlToNull(oOppObject
										.getPropertySafelyAsString("Owner")
										.toString()));
						leadResponse.setPrefix(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Prefix")
										.toString()));
						leadResponse.setSkypeId(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("SkypeID")
								.toString()));
						leadResponse.setMailingState(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("State")
										.toString()));
						leadResponse.setMailingStreet(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("Street")
										.toString()));
						leadResponse
								.setUpdateGpsLocation(Utility
										.xmlToNull(oOppObject
												.getPropertySafelyAsString(
														"UpdateGpsLocation")
												.toString()));
						leadResponse.setWebsite(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("Website")
								.toString()));
						//leadResponse.setWorkFax(Utility.xmlToPhone(Utility
								//.xmlToNull(oOppObject
									//	.getPropertySafelyAsString("WorkFax")
									//	.toString())));
						//leadResponse.setWorkPhone(Utility.xmlToPhone(Utility
								//.xmlToNull(oOppObject
										//.getPropertySafelyAsString("WorkPhone")
									//	.toString())));
						leadResponse.setWorkFax(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("WorkFax")
										.toString()));
						leadResponse.setWorkPhone(Utility
								.xmlToNull(oOppObject
										.getPropertySafelyAsString("WorkPhone")
										.toString()));
						leadResponse.setMailingZip(Utility.xmlToNull(oOppObject
								.getPropertySafelyAsString("Zip").toString()));
						aLeadList.add(leadResponse);
					}

					if (aLeadList.size() > 0) {
						oLeadSource.insertUpdate(aLeadList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aLeadList);
		return oResult;
	}

	public static HashMap<String, Object> doSyncEvent(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<CalendarDetail> aEventList = null;
		String strMethod = "SyncCalendar";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncCalendar";
		String strConfigKey = "Calendar";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");
		SoapObject emailSchedules = new SoapObject();
		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);
		
		
		SoapObject oEvent = new SoapObject(CREDENTIAL_NAMESPACE, "calendars");
		SoapObject oTimeDate = new SoapObject("","EmailSchedules");
		// add all unsync into contacts
		DatabaseUtility.getDatabaseHandler(context);
		FLCalendar oCalendarSource = new FLCalendar(Constants.DBHANDLER);
		ArrayList<CalendarDetail> aCalendar = oCalendarSource
				.getUnsyncCalendar();
		int iUnsyncLead = aCalendar.size();

		if (iUnsyncLead > 0) {
			for (int i = 0; i < iUnsyncLead; i++) {
				CalendarDetail calendar = aCalendar.get(i);
				
				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("Calendar");
				Obj1.setValue(calendar);
				Obj1.setType(calendar.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oEvent.addProperty(Obj1);
			}
		}
		DeviceUtility.log(TAG,
				"oEvent.getNamespace(): " + oEvent.getNamespace());
		
		
		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);
		oRequest.addProperty("calendars", oEvent);
		
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aEventList = new ArrayList<CalendarDetail>();
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(0): "
										+ oResponse
												.getPropertySafelyAsString("Calendars"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(1): "
										+ oResponse
												.getPropertySafelyAsString("LastSyncTimestamp"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(2): "
										+ oResponse
												.getPropertySafelyAsString("ReturnCode"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(3): "
										+ oResponse
												.getPropertySafelyAsString("ReturnMessage"));

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp");
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oCalendarList = (SoapObject) oResponse
							.getProperty("Calendars");
					int iCalendarList = oCalendarList.getPropertyCount();
					for (int i = 0; i < iCalendarList; i++) {
						SoapObject oCalendarObject = (SoapObject) oCalendarList
								.getProperty(i);
						CalendarDetail calendarResponse = new CalendarDetail();
						calendarResponse.setIsUpdate("true");
						calendarResponse.setActive(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Active")
										.toString()));
						String strAlert = Utility.xmlToNull(oCalendarObject
								.getPropertySafelyAsString("Alert").toString());
						if (strAlert.equals(Constants.DEFAULT_DATE)) {
							calendarResponse.setAlert(null);
						} else {
							calendarResponse.setAlert(Utility.xmlToDate(
									strAlert, Constants.DATETIME_FORMAT));
						}
						calendarResponse.setAllDay(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("AllDay")
										.toString()));
						calendarResponse.setAvailability(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"Availability").toString()));
						calendarResponse
								.setCalendarId(Utility
										.xmlToNull(oCalendarObject
												.getPropertySafelyAsString(
														"CalendarID")
												.toString()));
						calendarResponse.setDescription(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"Description").toString()));
						calendarResponse
								.setEmailNotification(Utility
										.xmlToNull(oCalendarObject
												.getPropertySafelyAsString(
														"EmailNotification")
												.toString()));
						calendarResponse.setEndDate(Utility.xmlToDate(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"EndDateTime").toString()),
								Constants.DATETIME_FORMAT));
						calendarResponse.setInternalNum(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"ICalendarID").toString()));
						calendarResponse.setInvitees(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Invitees")
										.toString()));
						calendarResponse.setIsPrivate(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("IsPrivate")
										.toString()));
						calendarResponse.setLocation(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Location")
										.toString()));
						calendarResponse.setModifiedTimestamp(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"ModifiedDate").toString()));
						calendarResponse.setCreatedTimestamp(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"CreatedDate").toString()));
						calendarResponse.setNameToId(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("NameToID")
										.toString()));
						calendarResponse.setEventNameTo(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("EventNameTo")
										.toString()));
						calendarResponse.setOwner(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Owner")
										.toString()));
						calendarResponse.setPriority(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Priority")
										.toString()));
						calendarResponse.setSmsNotification(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"SMSNotification").toString()));
						calendarResponse.setStartDate(Utility.xmlToDate(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString(
												"StartDateTime").toString()),
								Constants.DATETIME_FORMAT));
						calendarResponse.setSubject(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Subject")
										.toString()));
						calendarResponse.setNameToName(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("NameTo")
										.toString()));
						calendarResponse.setUserDef3(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Comment")
										.toString()));

						calendarResponse.setUserDef7(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Status")
										.toString()));
						calendarResponse.setCategory(Utility
								.xmlToNull(oCalendarObject
										.getPropertySafelyAsString("Category")
										.toString()));
						String strEmailNotification1Date = Utility.xmlToNull(oCalendarObject
								.getPropertySafelyAsString("EmailSchedule1").toString());
						if (strEmailNotification1Date.equals(Constants.DEFAULT_DATE)) {
							calendarResponse.setEmailSchedule1(null);
						} else {
							calendarResponse.setEmailSchedule1(Utility.xmlToDate(
									strEmailNotification1Date, Constants.DATETIME_FORMAT));
						}
						
						String strEmailNotification2Date = Utility.xmlToNull(oCalendarObject
								.getPropertySafelyAsString("EmailSchedule2").toString());
						if (strEmailNotification2Date.equals(Constants.DEFAULT_DATE)) {
							calendarResponse.setEmailSchedule2(null);
						} else {
							calendarResponse.setEmailSchedule2(Utility.xmlToDate(
									strEmailNotification2Date, Constants.DATETIME_FORMAT));
						}
						
						String strEmailNotification3Date = Utility.xmlToNull(oCalendarObject
								.getPropertySafelyAsString("EmailSchedule3").toString());
						if (strEmailNotification3Date.equals(Constants.DEFAULT_DATE)) {
							calendarResponse.setEmailSchedule3(null);
						} else {
							calendarResponse.setEmailSchedule3(Utility.xmlToDate(
									strEmailNotification3Date, Constants.DATETIME_FORMAT));
						}
						
						aEventList.add(calendarResponse);
					}

					if (aEventList.size() > 0) {
						oCalendarSource.insertUpdate(context, aEventList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aEventList);
		return oResult;
	}

	public static HashMap<String, Object> doSyncTask(Context context,
			String strUsername, String strPassword, String strDeviceId) {
		ArrayList<TaskDetail> aTaskList = null;
		String strMethod = "SyncTask";
		String strSoapAction = "http://www.firstlink.asia/ICrm/SyncTask";
		String strConfigKey = "Task";
		String strTimestamp = Utility.getConfigByText(context, strConfigKey);

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oTask = new SoapObject(CREDENTIAL_NAMESPACE, "tasks");
		// add all unsync into contacts
		DatabaseUtility.getDatabaseHandler(context);
		Task oTaskSource = new Task(Constants.DBHANDLER);
		ArrayList<TaskDetail> aTask = oTaskSource.getUnsyncTask();
		int iUnsyncTask = aTask.size();
		if (iUnsyncTask > 0) {
			for (int i = 0; i < iUnsyncTask; i++) {
				TaskDetail task = aTask.get(i);

				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("Task");
				Obj1.setValue(task);
				Obj1.setType(task.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oTask.addProperty(Obj1);

				System.out.println("Obj1 = " + Obj1);
			}
		}
		// DeviceUtility.log(TAG, "oTask.getNamespace(): " +
		// oTask.getNamespace());

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("lastSyncTimestamp", strTimestamp);
		oRequest.addProperty("tasks", oTask);

		System.out.println("my oRequest = " + oRequest);
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				aTaskList = new ArrayList<TaskDetail>();
				DeviceUtility.log(
						TAG,
						"oResult.getProperty(0): "
								+ oResponse.getPropertySafelyAsString("Tasks"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(1): "
										+ oResponse
												.getPropertySafelyAsString("LastSyncTimestamp"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(2): "
										+ oResponse
												.getPropertySafelyAsString("ReturnCode"));
				DeviceUtility
						.log(TAG,
								"oResult.getProperty(3): "
										+ oResponse
												.getPropertySafelyAsString("ReturnMessage"));

				String strLastUpdateTimestamp = oResponse
						.getPropertySafelyAsString("LastSyncTimestamp");
				String strReturnCode = oResponse
						.getPropertySafelyAsString("ReturnCode");
				if (strReturnCode.equalsIgnoreCase("0")) {
					// update the timestamp.
					Utility.updateConfigByText(context, strConfigKey,
							strLastUpdateTimestamp);
					SoapObject oTaskList = (SoapObject) oResponse
							.getProperty("Tasks");
					int iTaskList = oTaskList.getPropertyCount();
					for (int i = 0; i < iTaskList; i++) {
						SoapObject oTaskObject = (SoapObject) oTaskList
								.getProperty(i);
						TaskDetail taskResponse = new TaskDetail();
						taskResponse.setIsUpdate("true");
						taskResponse.setActive(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("Active")
										.toString()));
						// String strAlertDate =
						// Utility.xmlToDate(Utility.xmlToNull(oTaskObject.getProperty("Alert").toString()),
						// Constants.DATETIMESEC_FORMAT);
						String strAlertDate = Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("Alert").toString());
						if (strAlertDate.equals(Constants.DEFAULT_DATE)) {
							taskResponse.setAlert(null);
						} else {
							taskResponse.setAlert(Utility.xmlToDate(
									strAlertDate, Constants.DATETIME_FORMAT));
						}
						// DeviceUtility.log(TAG,
						// "Alert: " + taskResponse.getAlert());

						taskResponse
								.setAssignedTo(Utility
										.xmlToNull(oTaskObject
												.getPropertySafelyAsString(
														"AssignedTo")
												.toString()));
						taskResponse.setAvailability(Utility
								.xmlToNull(oTaskObject//Comment
										.getPropertySafelyAsString(
												"Availability").toString()));//setUserDef2//Comment
						taskResponse.setUserDef3(Utility.xmlToNull(oTaskObject.getPropertySafelyAsString("Comment").toString()));
						taskResponse.setDescription(Utility.xmlToNull(oTaskObject.getPropertySafelyAsString("Description").toString()));
						taskResponse
								.setDueDate(Utility.xmlToDate(Utility
										.xmlToNull(oTaskObject
												.getPropertySafelyAsString(
														"DueDate").toString()),
										Constants.DATETIME_FORMAT));
						taskResponse
								.setEmailNotification(Utility
										.xmlToNull(oTaskObject
												.getPropertySafelyAsString(
														"EmailNotification")
												.toString()));
						taskResponse.setInternalNum(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("ITaskID")
										.toString()));
						taskResponse.setIsPrivate(Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("IsPrivate")
								.toString()));
						taskResponse.setModifiedTimestamp(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString(
												"ModifiedDate").toString()));
						taskResponse.setCreatedTimestamp(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString(
												"CreatedDate").toString()));
						taskResponse
								.setOwner(Utility.xmlToNull(oTaskObject
										.getPropertySafelyAsString("Owner")
										.toString()));
						taskResponse.setPriority(Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("Priority")
								.toString()));
						taskResponse.setSmsNotification(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString(
												"SMSNotification").toString()));
						taskResponse.setUserDef7(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("Status")
										.toString()));
						// DeviceUtility.log(TAG, taskResponse.toString());
						aTaskList.add(taskResponse);
						taskResponse.setSubject(Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("Subject")
								.toString()));
						taskResponse.setNameToName(Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("NameTo")
								.toString()));
						taskResponse.setTaskId(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("TaskID")
										.toString()));
						taskResponse.setNameToId(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("NameToID")
										.toString()));
						taskResponse.setTaskNameToId(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("TaskNameTo")
										.toString()));
						taskResponse.setCategory(Utility
								.xmlToNull(oTaskObject
										.getPropertySafelyAsString("TaskKind")
										.toString()));
						
						String strEmailNotification1Date = Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("EmailSchedule1").toString());
						if (strEmailNotification1Date.equals(Constants.DEFAULT_DATE)) {
							taskResponse.setEmailSchedule1(null);
						} else {
							taskResponse.setEmailSchedule1(Utility.xmlToDate(
									strEmailNotification1Date, Constants.DATETIME_FORMAT));
						}
						
						String strEmailNotification2Date = Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("EmailSchedule2").toString());
						if (strEmailNotification2Date.equals(Constants.DEFAULT_DATE)) {
							taskResponse.setEmailSchedule2(null);
						} else {
							taskResponse.setEmailSchedule2(Utility.xmlToDate(
									strEmailNotification2Date, Constants.DATETIME_FORMAT));
						}
						
						String strEmailNotification3Date = Utility.xmlToNull(oTaskObject
								.getPropertySafelyAsString("EmailSchedule3").toString());
						if (strEmailNotification3Date.equals(Constants.DEFAULT_DATE)) {
							taskResponse.setEmailSchedule3(null);
						} else {
							taskResponse.setEmailSchedule3(Utility.xmlToDate(
									strEmailNotification3Date, Constants.DATETIME_FORMAT));
						}
					}

					if (aTaskList.size() > 0) {
						oTaskSource.insertUpdate(context, aTaskList);
					}
				} else if (strReturnCode.equalsIgnoreCase("14")) {
					oResult.put("STATUS", STATUS.SYNC_ERROR_RESET);
				} else {
					// show error.
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aTaskList);
		return oResult;
	}

	public static HashMap<String, Object> doGetActivityReport(Context context,
			String strUsername, String strPassword, String strDeviceId,
			String strStartDate, String strEndDate) {
		ArrayList<ReportDetail> aReport = new ArrayList<ReportDetail>();
		String strMethod = "GetActivityReport";
		String strSoapAction = "http://www.firstlink.asia/ICrm/GetActivityReport";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("startDate",
				Utility.toXmlDateTime(strStartDate, Constants.DATE_FORMAT));
		oRequest.addProperty("endDate",
				Utility.toXmlDateTime(strEndDate, Constants.DATE_FORMAT));
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		// DeviceUtility.log(TAG, "call done: " + oResult);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "oResponse: " + oResponse);
			if (oResponse != null) {
				int iRow = oResponse.getPropertyCount();
				// DeviceUtility.log(TAG, "iRow: " + iRow);
				if (iRow > 0) {
					for (int i = 0; i < iRow; i++) {
						SoapObject oReportObject = (SoapObject) oResponse
								.getProperty(i);
						// DeviceUtility.log(TAG, "oReportObject: "
						// + oReportObject);
						// get column.
						ReportDetail oReport = new ReportDetail();
						oReport.setReportType(Constants.REPORT_TYPE_ACTIVITY);
						oReport.setReportStartDate(strStartDate);
						oReport.setReportEndDate(strEndDate);
						oReport.setReportPerson(oReportObject
								.getPropertySafelyAsString("SalesPersonName")
								.toString());
						oReport.setReportValue(oReportObject
								.getPropertySafelyAsString("Total").toString());
						oReport.setReportText(oReportObject
								.getPropertySafelyAsString("TaskKind")
								.toString());
						// DeviceUtility.log(TAG, "oReport: " + oReport);
						aReport.add(oReport);
					}
				}

				if (aReport.size() > 0) {
					DatabaseUtility.getDatabaseHandler(context);
					Report oReport = new Report(Constants.DBHANDLER);
					// delete the related date report.
					oReport.deleteReport(strStartDate, strEndDate,
							Constants.REPORT_TYPE_ACTIVITY);
					oReport.insert(aReport);
				}
			}
		}
		return oResult;
	}

	public static HashMap<String, Object> doGetOpportunityReport(
			Context context, String strUsername, String strPassword,
			String strDeviceId, String strStartDate, String strEndDate) {
		ArrayList<ReportDetail> aReport = new ArrayList<ReportDetail>();
		String strMethod = "GetOpportunityReport";
		String strSoapAction = "http://www.firstlink.asia/ICrm/GetOpportunityReport";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("startDate",
				Utility.toXmlDateTime(strStartDate, Constants.DATE_FORMAT));
		oRequest.addProperty("endDate",
				Utility.toXmlDateTime(strEndDate, Constants.DATE_FORMAT));
		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		// DeviceUtility.log(TAG, "call done: " + oResult);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "oResponse: " + oResponse);
			if (oResponse != null) {
				int iRow = oResponse.getPropertyCount();
				// DeviceUtility.log(TAG, "iRow: " + iRow);
				if (iRow > 0) {
					for (int i = 0; i < iRow; i++) {
						SoapObject oReportObject = (SoapObject) oResponse
								.getProperty(i);
						// DeviceUtility.log(TAG, "oReportObject: "
						// + oReportObject);
						// get column.
						ReportDetail oReport = new ReportDetail();
						oReport.setReportType(Constants.REPORT_TYPE_OPPORTUNITY);
						oReport.setReportStartDate(strStartDate);
						oReport.setReportEndDate(strEndDate);
						oReport.setReportPerson(oReportObject
								.getPropertySafelyAsString("SalesPersonName")
								.toString());
						oReport.setReportValue(oReportObject
								.getPropertySafelyAsString("Total").toString());
						oReport.setReportText(oReportObject
								.getPropertySafelyAsString("Stage").toString());
						// DeviceUtility.log(TAG, "oReport: " + oReport);
						aReport.add(oReport);
					}
				}

				if (aReport.size() > 0) {
					DatabaseUtility.getDatabaseHandler(context);
					Report oReport = new Report(Constants.DBHANDLER);
					// delete the related date report.
					oReport.deleteReport(strStartDate, strEndDate,
							Constants.REPORT_TYPE_OPPORTUNITY);
					oReport.insert(aReport);
				}
			}
		}
		return oResult;
	}

	public static HashMap<String, Object> doSyncContactActivityLog(
			Context context, String strUsername, String strPassword,
			String strDeviceId) {
		ArrayList<ActivitiesLogDetail> aActivityList = null;
		String strMethod = "UploadContactActivity";
		String strSoapAction = "http://www.firstlink.asia/ICrm/UploadContactActivity";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oActivities = new SoapObject(CREDENTIAL_NAMESPACE,
				"activities");
		DatabaseUtility.getDatabaseHandler(context);
		ActivityLog oActivityLogSource = new ActivityLog(Constants.DBHANDLER);
		ArrayList<ContactActivity> aActivityLog = oActivityLogSource
				.getUnsyncContactActivityLog();
		int iUnsyncActivity = aActivityLog.size();
		if (iUnsyncActivity > 0) {
			for (int i = 0; i < iUnsyncActivity; i++) {
				ContactActivity activityLog = aActivityLog.get(i);
				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("ContactActivity");
				Obj1.setValue(activityLog);
				Obj1.setType(activityLog.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oActivities.addProperty(Obj1);
			}
		}
		// DeviceUtility.log(TAG,
		// "oActivities.getNamespace(): " + oActivities.getNamespace());

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("activities", oActivities);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				if (oResponse.getPropertySafelyAsString("ReturnCode").equals(
						"0")) {
					oActivityLogSource
							.updateSyncedActivityLog(Constants.PERSON_TYPE_CONTACT);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage");
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aActivityList);
		return oResult;
	}

	public static HashMap<String, Object> doSyncLeadActivityLog(
			Context context, String strUsername, String strPassword,
			String strDeviceId) {
		ArrayList<ActivitiesLogDetail> aActivityList = null;
		String strMethod = "UploadLeadActivity";
		String strSoapAction = "http://www.firstlink.asia/ICrm/UploadLeadActivity";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		SoapObject oCredential = new SoapObject(CREDENTIAL_NAMESPACE,
				"credential");

		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(strPassword);
		password.setType(String.class);
		password.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo username = new PropertyInfo();
		username.setName("Username");
		username.setValue(strUsername);
		username.setType(String.class);
		username.setNamespace(CREDENTIAL_NAMESPACE);

		PropertyInfo refno = new PropertyInfo();
		refno.setName("RefNo");
		refno.setValue(strDeviceId);
		refno.setType(String.class);
		refno.setNamespace(CREDENTIAL_NAMESPACE);

		oCredential.addProperty(password);
		oCredential.addProperty(refno);
		oCredential.addProperty(username);

		SoapObject oActivities = new SoapObject(CREDENTIAL_NAMESPACE,
				"activities");
		DatabaseUtility.getDatabaseHandler(context);
		ActivityLog oActivityLogSource = new ActivityLog(Constants.DBHANDLER);
		ArrayList<LeadActivity> aActivityLog = oActivityLogSource
				.getUnsyncLeadActivityLog();
		int iUnsyncActivity = aActivityLog.size();
		if (iUnsyncActivity > 0) {
			for (int i = 0; i < iUnsyncActivity; i++) {
				LeadActivity activityLog = aActivityLog.get(i);
				PropertyInfo Obj1 = new PropertyInfo();
				Obj1.setName("LeadActivity");
				Obj1.setValue(activityLog);
				Obj1.setType(activityLog.getClass());
				Obj1.setNamespace(CREDENTIAL_NAMESPACE);
				oActivities.addProperty(Obj1);
			}
		}
		// DeviceUtility.log(TAG,
		// "oActivities.getNamespace(): " + oActivities.getNamespace());

		oRequest.addProperty("credential", oCredential);
		oRequest.addProperty("activities", oActivities);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				if (oResponse.getPropertySafelyAsString("ReturnCode")
						.toString().equals("0")) {
					oActivityLogSource
							.updateSyncedActivityLog(Constants.PERSON_TYPE_LEAD);
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage")
							.toString();
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}
		oResult.put("RESULT", aActivityList);
		return oResult;
	}

	public static HashMap<String, Object> doForgotPhonePin(Context context,
			String strDeviceId, String strPasscode) {
		// DeviceUtility.log(TAG, "doForgotPhonePin(" + strDeviceId + ", "
		// + strPasscode + ")");
		String strMethod = "ForgotPhonePin";
		String strSoapAction = "http://www.firstlink.asia/ICrm/ForgotPhonePin";

		SoapObject oRequest = new SoapObject(NAMESPACE, strMethod);
		oRequest.addProperty("refNo", strDeviceId);
		oRequest.addProperty("emailSubject", "Request PIN");
		oRequest.addProperty("emailBody", "Your PIN is " + strPasscode);

		HashMap<String, Object> oResult = call(context, oRequest, strSoapAction);
		if (oResult.get("STATUS") == STATUS.SUCCESS) {
			oResult.put("RESULT", "FAIL");
			SoapObject oResponse = (SoapObject) oResult.get("SOAP_OBJECT");
			// DeviceUtility.log(TAG, "call done: " + oResponse);

			if (oResponse != null) {
				// DeviceUtility
				// .log(TAG,
				// "return code: "
				// + oResponse
				// .getPropertySafelyAsString("ReturnCode"));
				// DeviceUtility.log(TAG, "return code: "
				// + oResponse.getPropertySafelyAsString("ReturnCode")
				// .equals("0"));
				if (oResponse.getPropertySafelyAsString("ReturnCode")
						.toString().equals("0")) {
					// DeviceUtility.log(TAG, "set to success");
					oResult.put("RESULT", "SUCCESS");
				} else {
					oResult.put("STATUS", STATUS.SYNC_ERROR);
					String strReturnMessage = oResponse
							.getPropertySafelyAsString("ReturnMessage")
							.toString();
					if (strReturnMessage.length() > 0) {
						oResult.put("ERROR_MESSAGE", strReturnMessage);
					}
				}
			}
		}

		return oResult;
	}
}