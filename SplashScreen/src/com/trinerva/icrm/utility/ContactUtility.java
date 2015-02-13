package com.trinerva.icrm.utility;

import java.util.ArrayList;
import java.util.HashMap;

import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.LeadDetail;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactUtility {
	public static String TAG = "ContactUtility";
	/*
	public static ContactDetail getContact(Context context, String strUri) {
		Uri contactUri = Uri.parse(strUri);
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(contactUri,  null, null, null, null);
		ContactDetail contact = new ContactDetail();
		
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				
				//Phone
				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                              new String[] {ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                              new String[]{id}, null);
                    
                    while (pCur.moveToNext()) {
                    	int iType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    	String strNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    	strNumber = strNumber.replace("(", "");
                    	strNumber = strNumber.replace(")", "");
                    	strNumber = strNumber.replace("-", "");
                    	strNumber = strNumber.replace(" ", "");
                    	
                    	if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                    		contact.setHomePhone(strNumber);
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    		contact.setMobile(strNumber);
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                    		contact.setWorkPhone(strNumber);
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE) {
                    		if (contact.getWorkPhone() == null || contact.getWorkPhone().length() == 0) {
                    			contact.setWorkPhone(strNumber);
                    		} else if (contact.getOtherPhone() == null || contact.getOtherPhone().length() == 0) {
                    			contact.setOtherPhone(strNumber);
                    		}
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK) {	
                    		contact.setWorkFax(strNumber);
                    	} else {
                    		if (contact.getOtherPhone() == null || contact.getOtherPhone().length() == 0) {
                    			contact.setOtherPhone(strNumber);
                    		}
                    	}
                    }
				}
				
				//Email
				Cursor eCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[] {ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.DATA1}, ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ?",
                        new String[]{id}, null);
				while (eCur.moveToNext()) {
					String strEmail = eCur.getString(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
					int iType = eCur.getInt(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
					if (iType == ContactsContract.CommonDataKinds.Email.TYPE_HOME) {
						contact.setEmail1(strEmail);
					} else if (iType == ContactsContract.CommonDataKinds.Email.TYPE_MOBILE) {
						contact.setEmail2(strEmail);
					} else if (iType == ContactsContract.CommonDataKinds.Email.TYPE_WORK) {
						 if (contact.getEmail2() == null || contact.getEmail2().length() == 0) {
							 contact.setEmail2(strEmail);
						 } else {
							 contact.setEmail3(strEmail);
						 }
					} else if (iType == ContactsContract.CommonDataKinds.Email.TYPE_OTHER) {
						if (contact.getEmail1() == null || contact.getEmail1().length() == 0) {
							contact.setEmail1(strEmail);
						} else if (contact.getEmail2() == null || contact.getEmail2().length() == 0) {
							contact.setEmail2(strEmail);
						} else if (contact.getEmail3() == null || contact.getEmail3().length() == 0) {
							contact.setEmail3(strEmail);
						}
					}
				}
				
				//Name, Organization, 
				Cursor dCur = cr.query(ContactsContract.Data.CONTENT_URI,
                        null, ContactsContract.Data.CONTACT_ID +" = ? AND ("  + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE +" = ? OR " + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ?) ",
                        new String[]{id, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE}, null);
				
				while (dCur.moveToNext()) {
                	String strType = dCur.getString(dCur.getColumnIndex(ContactsContract.Data.MIMETYPE));
                	if (strType.equalsIgnoreCase(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    	//StructuredName
                		String strFirstName = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    	String strLastName = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    	contact.setFirstName(strFirstName);
                    	contact.setLastName(strLastName);
                    	
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                		//Organization
                		String strCompany = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                    	String strDepartment = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT));
                    	String strPosition = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    	
                    	contact.setCompanyName(strCompany);
                    	contact.setDepartment(strDepartment);
                    	contact.setJobTitle(strPosition);
                    	
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)) {
                		//IM
                		int iIm = dCur.getInt(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
                		if (iIm == ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE) {
                			String strSkype = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                			contact.setSkypeId(strSkype);
                		}
                		
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                		//Photo
                		byte[] photoBlob = dCur.getBlob(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
                		if (photoBlob != null) {
	                		final Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
	                		StorageUtility cache = new StorageUtility(context, Constants.CACHE_FOLDER);
	                		String strImage = cache.doStoreImage(photoBitmap);
	                		contact.setPhoto(strImage);
                		}
                	} else if (strType.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
                		//Address
                		String strCity = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                		String strCountry = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                		String strStreet = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                		String strPostCode = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                		contact.setMailingCity(strCity);
                		contact.setMailingCountry(strCountry);
                		contact.setMailingStreet(strStreet);
                		contact.setMailingZip(strPostCode);
                	}
				}
			}
		}
		return contact;
	}*/
	
	public static ContactDetail getContact(Context context, String strUri) {
		HashMap<String, String> hDetail = getContactDetail(context, strUri);
		DeviceUtility.log(TAG, "hDetail: " + hDetail);
		ContactDetail contact = new ContactDetail();
		
		if (hDetail.containsKey("HOME_PHONE")) {
			contact.setHomePhone(hDetail.get("HOME_PHONE").toString());
		}
		
		if (hDetail.containsKey("MOBILE_PHONE")) {
			contact.setMobile(hDetail.get("MOBILE_PHONE").toString());
		}
		
		if (hDetail.containsKey("WORK_PHONE")) {
			contact.setWorkPhone(hDetail.get("WORK_PHONE").toString());
		}
		
		if (hDetail.containsKey("OTHER_PHONE")) {
			contact.setOtherPhone(hDetail.get("OTHER_PHONE").toString());
		}
		
		if (hDetail.containsKey("WORK_FAX")) {
			contact.setWorkFax(hDetail.get("WORK_FAX").toString());
		}
		
		if (hDetail.containsKey("EMAIL1")) {
			contact.setEmail1(hDetail.get("EMAIL1").toString());
		}
		
		if (hDetail.containsKey("EMAIL2")) {
			contact.setEmail2(hDetail.get("EMAIL2").toString());
		}
		
		if (hDetail.containsKey("EMAIL3")) {
			contact.setEmail3(hDetail.get("EMAIL3").toString());
		}
		
		if (hDetail.containsKey("FIRSTNAME")) {
			contact.setFirstName(hDetail.get("FIRSTNAME").toString());
		}
		
		if (hDetail.containsKey("LASTNAME")) {
			contact.setLastName(hDetail.get("LASTNAME").toString());
		}
		
		if (hDetail.containsKey("COMPANY_NAME")) {
			contact.setCompanyName(hDetail.get("COMPANY_NAME").toString());
		}
		
		if (hDetail.containsKey("COMPANY_ID")) {
			contact.setCompanyId(hDetail.get("COMPANY_ID").toString());
		}
		
		if (hDetail.containsKey("DEPARTMENT")) {
			contact.setDepartment(hDetail.get("DEPARTMENT").toString());
		}
		
		if (hDetail.containsKey("JOB_TITLE")) {
			contact.setJobTitle(hDetail.get("JOB_TITLE").toString());
		}
		
		if (hDetail.containsKey("SKYPE_ID")) {
			contact.setSkypeId(hDetail.get("SKYPE_ID").toString());
		}
		
		if (hDetail.containsKey("PHOTO")) {
			contact.setPhoto(hDetail.get("PHOTO").toString());
		}
		
		if (hDetail.containsKey("CITY")) {
			contact.setMailingCity(hDetail.get("CITY").toString());
		}
		
		if (hDetail.containsKey("COUNTRY")) {
			contact.setMailingCountry(hDetail.get("COUNTRY").toString());
		}
		
		if (hDetail.containsKey("STREET")) {
			contact.setMailingStreet(hDetail.get("STREET").toString());
		}
		
		if (hDetail.containsKey("STATE")) {
			contact.setMailingState(hDetail.get("STATE").toString());
		}
		
		if (hDetail.containsKey("ZIP")) {
			contact.setMailingZip(hDetail.get("ZIP").toString());
		}
		
		DeviceUtility.log(TAG, "contact: " + contact);
		return contact;
	}
	
	
	public static LeadDetail getLeadFromPhoneBook(Context context, String strUri) {
		HashMap<String, String> hDetail = getContactDetail(context, strUri);
		DeviceUtility.log(TAG, "hDetail: " + hDetail);
		LeadDetail contact = new LeadDetail();
		
		/*if (hDetail.containsKey("HOME_PHONE")) {
			contact.setHomePhone(hDetail.get("HOME_PHONE").toString());
		}*/
		
		if (hDetail.containsKey("MOBILE_PHONE")) {
			contact.setMobile(hDetail.get("MOBILE_PHONE").toString());
		}
		
		if (hDetail.containsKey("WORK_PHONE")) {
			contact.setWorkPhone(hDetail.get("WORK_PHONE").toString());
		}
		
		/*if (hDetail.containsKey("OTHER_PHONE")) {
			contact.setOtherPhone(hDetail.get("OTHER_PHONE").toString());
		}*/
		
		if (hDetail.containsKey("WORK_FAX")) {
			contact.setWorkFax(hDetail.get("WORK_FAX").toString());
		}
		
		if (hDetail.containsKey("EMAIL1")) {
			contact.setEmail1(hDetail.get("EMAIL1").toString());
		}
		
		if (hDetail.containsKey("EMAIL2")) {
			contact.setEmail2(hDetail.get("EMAIL2").toString());
		}
		
		if (hDetail.containsKey("EMAIL3")) {
			contact.setEmail3(hDetail.get("EMAIL3").toString());
		}
		
		if (hDetail.containsKey("FIRSTNAME")) {
			contact.setFirstName(hDetail.get("FIRSTNAME").toString());
		}
		
		if (hDetail.containsKey("LASTNAME")) {
			contact.setLastName(hDetail.get("LASTNAME").toString());
		}
		
		if (hDetail.containsKey("BIRTHDAY")) {
			contact.setBirthday(hDetail.get("BIRTHDAY").toString());
		}
		
		if (hDetail.containsKey("COMPANY_NAME")) {
			contact.setCompanyName(hDetail.get("COMPANY_NAME").toString());
		}
		
		/*if (hDetail.containsKey("DEPARTMENT")) {
			contact.setDepartment(hDetail.get("DEPARTMENT").toString());
		}*/
		
		if (hDetail.containsKey("JOB_TITLE")) {
			contact.setJobTitle(hDetail.get("JOB_TITLE").toString());
		}
		
		if (hDetail.containsKey("SKYPE_ID")) {
			contact.setSkypeId(hDetail.get("SKYPE_ID").toString());
		}
		
		if (hDetail.containsKey("PHOTO")) {
			contact.setPhoto(hDetail.get("PHOTO").toString());
		}
		
		if (hDetail.containsKey("CITY")) {
			contact.setMailingCity(hDetail.get("CITY").toString());
		}
		
		if (hDetail.containsKey("COUNTRY")) {
			contact.setMailingCountry(hDetail.get("COUNTRY").toString());
		}
		
		if (hDetail.containsKey("STREET")) {
			contact.setMailingStreet(hDetail.get("STREET").toString());
		}
		
		if (hDetail.containsKey("STATE")) {
			contact.setMailingState(hDetail.get("STATE").toString());
		}
		
		if (hDetail.containsKey("ZIP")) {
			contact.setMailingZip(hDetail.get("ZIP").toString());
		}
		DeviceUtility.log(TAG, "contact: " + contact);
		return contact;
	}
	
	public static HashMap<String, String> getContactDetail(Context context, String strUri) {
		Uri contactUri = Uri.parse(strUri);
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(contactUri,  null, null, null, null);
		HashMap<String, String> contact = new HashMap<String, String>();
		
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				
				//Phone
				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                              new String[] {ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                              new String[]{id}, null);
                    
                    while (pCur.moveToNext()) {
                    	int iType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    	String strNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    	strNumber = strNumber.replaceAll("\\D", "");
                    	
                    	if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                    		//contact.setHomePhone(strNumber);
                    		contact.put("HOME_PHONE", strNumber);
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    		contact.put("MOBILE_PHONE", strNumber);
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                    		contact.put("WORK_PHONE", strNumber);
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE) {
                    		if (contact.get("WORK_PHONE") == null || contact.get("WORK_PHONE").length() == 0) {
                    			contact.put("WORK_PHONE", strNumber);
                    		} else if (contact.get("OTHER_PHONE") == null || contact.get("OTHER_PHONE").length() == 0) {
                    			contact.put("OTHER_PHONE", strNumber);
                    		}
                    	} else if (iType == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK) {
                    		contact.put("WORK_FAX", strNumber);
                    	} else {
                    		if (contact.get("OTHER_PHONE") == null || contact.get("OTHER_PHONE").length() == 0) {
                    			contact.put("OTHER_PHONE", strNumber);
                    		}
                    	}
                    }
				}
				
				//Email
				Cursor eCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[] {ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.DATA1}, ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ?",
                        new String[]{id}, null);
				while (eCur.moveToNext()) {
					String strEmail = eCur.getString(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
					int iType = eCur.getInt(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
					if (iType == ContactsContract.CommonDataKinds.Email.TYPE_HOME) {
						contact.put("EMAIL1", strEmail);
					} else if (iType == ContactsContract.CommonDataKinds.Email.TYPE_MOBILE) {
						contact.put("EMAIL2", strEmail);
					} else if (iType == ContactsContract.CommonDataKinds.Email.TYPE_WORK) {
						 if (contact.get("EMAIL2") == null || contact.get("EMAIL2").length() == 0) {
							 contact.put("EMAIL2", strEmail);
						 } else {
							 contact.put("EMAIL3", strEmail);
						 }
					} else if (iType == ContactsContract.CommonDataKinds.Email.TYPE_OTHER) {
						if (contact.get("EMAIL1") == null || contact.get("EMAIL1").length() == 0) {
							contact.put("EMAIL1", strEmail);
						} else if (contact.get("EMAIL2") == null || contact.get("EMAIL2").length() == 0) {
							contact.put("EMAIL2", strEmail);
						} else if (contact.get("EMAIL3") == null || contact.get("EMAIL3").length() == 0) {
							contact.put("EMAIL3", strEmail);
						}
					}
				}
				
				//Name, Organization, 
				Cursor dCur = cr.query(ContactsContract.Data.CONTENT_URI,
                        null, ContactsContract.Data.CONTACT_ID +" = ? AND ("  + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE +" = ? OR " + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ?) ",
                        new String[]{id, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE}, null);
				
				while (dCur.moveToNext()) {
                	String strType = dCur.getString(dCur.getColumnIndex(ContactsContract.Data.MIMETYPE));
                	if (strType.equalsIgnoreCase(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    	//StructuredName
                		String strFirstName = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    	String strLastName = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
//                    	if (strFirstName != null) {
//                    		contact.put("FIRSTNAME", strFirstName);
//                    	}
//                    	
//                    	if (strLastName != null) {
//                    		contact.put("LASTNAME", strLastName);
//                    	}
                    	
//                      	if (strFirstName != null) {
//                    		contact.put("FIRSTNAME", strFirstName);
//                    	}
                    	
                    	if (strFirstName != null && strLastName != null) {
                    		contact.put("LASTNAME", strFirstName+" "+strLastName);
                    	}else if (strLastName != null) {
                    		contact.put("LASTNAME", strLastName);
                    	}else if (strFirstName != null) {
                    		contact.put("LASTNAME", strFirstName);
                    	}
                    	
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                		//Organization
                		String strCompany = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                    	String strDepartment = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT));
                    	String strPosition = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    	if (strCompany != null) {
                    		contact.put("COMPANY_NAME", strCompany);
                    	}
                    	
                    	if (strDepartment != null) {
                    		contact.put("DEPARTMENT", strDepartment);
                    	}
                    	
                    	if (strPosition != null) {
                    		contact.put("JOB_TITLE", strPosition);
                    	}
                    	
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)) {
                		//IM
                		int iIm = dCur.getInt(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
                		if (iIm == ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE) {
                			String strSkype = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                			contact.put("SKYPE_ID", strSkype);
                		}
                		
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                		//Photo
                		byte[] photoBlob = dCur.getBlob(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
                		if (photoBlob != null) {
	                		final Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
	                		StorageUtility cache = new StorageUtility(context, Constants.CACHE_FOLDER);
	                		String strImage = cache.doStoreImage(photoBitmap);
	                		contact.put("PHOTO", strImage);
                		}
                	} else if (strType.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
                		//Address
                		String strCity = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                		String strRegion = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                		String strCountry = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                		String strStreet = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                		String strPostCode = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                		
                		if (strCity != null) {
                			contact.put("CITY", strCity);
                		}
                		
                		if (strCountry != null) {
                			contact.put("COUNTRY", strCountry);
                		}
                		
                		if (strStreet != null) {
                			contact.put("STREET", strStreet);
                		}
                		
                		if (strPostCode != null) {
                			contact.put("ZIP", strPostCode);
                		}
                		
                		if (strRegion != null) {
                			contact.put("STATE", strRegion);
                		}
                	}
				}
			}
		}
		return contact;
	}
	
	public static ArrayList<HashMap<String, String>> getAllQualifyContact(Context context) {
		ArrayList<HashMap<String, String>> aContact = new ArrayList<HashMap<String, String>>();
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,  null, null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {				
				HashMap<String, String> contact = new HashMap<String, String>();
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				contact.put("ID", id);
				
				String lookup = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
				contact.put("LOOKUP", lookup);
				
				//Name, Organization
				Cursor dCur = cr.query(ContactsContract.Data.CONTENT_URI,
                        null, ContactsContract.Data.CONTACT_ID +" = ? AND ("  + ContactsContract.Data.MIMETYPE + " = ? OR " + ContactsContract.Data.MIMETYPE + " = ?) ",
                        new String[]{id, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE}, null);
				
				while (dCur.moveToNext()) {
                	String strType = dCur.getString(dCur.getColumnIndex(ContactsContract.Data.MIMETYPE));
                	if (strType.equalsIgnoreCase(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    	//StructuredName
                		String strFirstName = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    	String strLastName = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    	if (strFirstName != null) {
                    		contact.put("FIRSTNAME", strFirstName);
                    		//DeviceUtility.log(TAG, "strFirstName: " + strFirstName);
                    	}
                    	
                    	if (strLastName != null) {
                    		contact.put("LASTNAME", strLastName);
                    		//DeviceUtility.log(TAG, "strLastName: " + strLastName);
                    	}
                    	
                	} else if (strType.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                		//Organization
                		String strCompany = dCur.getString(dCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                    	if (strCompany != null) {
                    		//DeviceUtility.log(TAG, "strCompany: " + strCompany);
                    		contact.put("COMPANY_NAME", strCompany);
                    	}
                	}
				}
				dCur.close();
				
				if (contact.containsKey("COMPANY_NAME") && contact.containsKey("LASTNAME")) {
					DeviceUtility.log(TAG, "found: ");
					contact.put("CHECKED", "false");
					aContact.add(contact);
				}
			}
		}
		cur.close();
		return aContact;
	}
}
