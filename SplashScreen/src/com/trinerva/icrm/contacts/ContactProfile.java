package com.trinerva.icrm.contacts;

import java.io.File;
import java.util.ArrayList;

import asia.firstlink.icrm.R;
import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.object.ActivitiesLogDetail;
import com.trinerva.icrm.object.ContactDetail;
import com.trinerva.icrm.object.EmailDetail;
import com.trinerva.icrm.object.PhoneDetail;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.StorageUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactProfile extends Activity {
	private String TAG = "ContactProfile";
	private String strId = null;
	private String strActive = null;
	private String strContactId = null;
	private TextView delete, first_name, last_name, company;
	private ImageView photo;
	private Button contact_info, opportunity_info, activity_history, email, call, sms;
	private Dialog loadingDialog;
	private ContactDetail contact;
	private ArrayList<PhoneDetail> aContactNumberList;
	private ArrayList<EmailDetail> aEmailList;
	private ArrayList<String> aPhotoSelection;
	private static final int EMAIL_GROUP = 0;
	private static final int SMS_GROUP = 1;
	private static final int CALL_GROUP = 2;
	private static final int PHOTO_GROUP = 3;
	private int PICK_IMAGE = 2132;
	private int SHOOT_PHOTO = 2354;
	private boolean bReload = true;
	private Bitmap mBitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_profile);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("ID")) {
				strId = bundle.getString("ID");
				DeviceUtility.log(TAG, "EDIT ID: " + strId);
			}
			
			if (bundle.containsKey("ACTIVE")) {
				strActive = bundle.getString("ACTIVE");
				DeviceUtility.log(TAG, "EDIT ACTIVE: " + strActive);
			}
			
			if (bundle.containsKey("CONTACT_ID")) {
				strContactId = bundle.getString("CONTACT_ID");
				DeviceUtility.log(TAG, "EDIT OPP for CONTACT_ID: " + strContactId);
			}
		}
		aPhotoSelection = new ArrayList<String>();
		aPhotoSelection.add(getString(R.string.camera_shoot));
		aPhotoSelection.add(getString(R.string.photo_library));
		
		delete = (TextView) findViewById(R.id.delete);
		first_name = (TextView) findViewById(R.id.first_name);
		last_name = (TextView) findViewById(R.id.last_name);
		company = (TextView) findViewById(R.id.company);
		
		photo = (ImageView) findViewById(R.id.photo);
		
		contact_info = (Button) findViewById(R.id.contact_info);
		opportunity_info = (Button) findViewById(R.id.opportunity_info);
		activity_history = (Button) findViewById(R.id.activity_history);
		email = (Button) findViewById(R.id.email);
		call = (Button) findViewById(R.id.call);
		sms = (Button) findViewById(R.id.sms);
		
		photo.setOnClickListener(doChangePhoto);
		contact_info.setOnClickListener(doShowContactInfo);
		opportunity_info.setOnClickListener(doShowOpportunityList);
		activity_history.setOnClickListener(doShowActivityHistory);
		delete.setOnClickListener(doShowPopUpDelete);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (bReload) {
			LoadContact task = new LoadContact();
			task.execute(new String[] {strId});
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
		
		photo.setImageBitmap(null);
	}

	private void doCreateMenu(Menu menu, int iGroupId) {
		switch (iGroupId) {
			case EMAIL_GROUP:
				int iEmail = aEmailList.size();
				for (int i = 0; i < iEmail; i++) {
					menu.add(iGroupId, i, i, aEmailList.get(i).toString());
				}
				break;
			case SMS_GROUP:
			case CALL_GROUP:
				int iContact = aContactNumberList.size();
				for (int i = 0; i < iContact; i++) {
					menu.add(iGroupId, i, i, aContactNumberList.get(i).toString());
				}
				break;
			case PHOTO_GROUP:
				int iPhotoType = aPhotoSelection.size();
				for (int i = 0; i < iPhotoType; i++) {
					menu.add(iGroupId, i, i, aPhotoSelection.get(i).toString());
				}
				break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		switch (v.getId()) {
			case R.id.email:
				doCreateMenu(menu, EMAIL_GROUP);
				break;
			case R.id.call:
				doCreateMenu(menu, CALL_GROUP);
				break;
			case R.id.sms:
				doCreateMenu(menu, SMS_GROUP);
				break;
			case R.id.photo:
				doCreateMenu(menu, PHOTO_GROUP);
				break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getGroupId()) {
			case EMAIL_GROUP:
				doLogActivity(Constants.ACTION_EMAIL, aEmailList.get(item.getItemId()).getEmailAddress());
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				String sEmailList[] = {aEmailList.get(item.getItemId()).getEmailAddress()};
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, sEmailList);
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_in)));  
				return true;
			case CALL_GROUP:
				doLogActivity(Constants.ACTION_CALL, aContactNumberList.get(item.getItemId()).getPhoneNo());
				String strPhoneNo = aContactNumberList.get(item.getItemId()).getPhoneNo();
				if (strPhoneNo.startsWith("6")) {
					strPhoneNo = "+" + strPhoneNo;
				}
				Intent callIntent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:"+strPhoneNo));
				startActivity(callIntent);
				return true;
			case SMS_GROUP:
				doLogActivity(Constants.ACTION_SMS, aContactNumberList.get(item.getItemId()).getPhoneNo());
				String strSMSNo = aContactNumberList.get(item.getItemId()).getPhoneNo();
				if (strSMSNo.startsWith("6")) {
					strSMSNo = "+" + strSMSNo;
				}
				
				Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("sms:"+strSMSNo));
				startActivity(smsIntent);
				return true;
			case PHOTO_GROUP:
				//do photo shooting.
				switch (item.getItemId()) {
					case 0: //camera shoot
						try {
							bReload = false;
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
							File fDirectory;
							File fPhoto;
							if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
								fDirectory = new File(android.os.Environment.getExternalStorageDirectory(), Constants.CACHE_FOLDER);
							} else {
								fDirectory = ContactProfile.this.getCacheDir();
							}
							
							if (!fDirectory.exists()) {
								fDirectory.mkdirs();
							}							
							
							fPhoto = new File(fDirectory, "firstlink.jpg");
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fPhoto));
							startActivityForResult(intent, SHOOT_PHOTO);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case 1: //library pick
						bReload = false;
						startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), PICK_IMAGE);
						break;
				}
		}
		return super.onContextItemSelected(item);
	}

	private OnClickListener doShowPopUpDelete = new OnClickListener() {
		@Override
		public void onClick(View v) {
			GuiUtility.alert(ContactProfile.this, "", getString(R.string.confirm_delete_contact), Gravity.CENTER, getString(R.string.delete), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Dialog loading = GuiUtility.getLoadingDialog(ContactProfile.this, false, getString(R.string.processing));
					DatabaseUtility.getDatabaseHandler(ContactProfile.this);
					Contact contact = new Contact(Constants.DBHANDLER);
					contact.delete(strId);
					loading.dismiss();
					//back to contact list.
					ContactProfile.this.finish();					
				}
			}, getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});
		}
	};
	
	private OnClickListener doChangePhoto = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			registerForContextMenu(v);
			openContextMenu(v);
		    unregisterForContextMenu(v);
		}
	};
	
	private OnClickListener doShowContactInfo = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			bReload = true;
			Intent contactInfo = new Intent(ContactProfile.this, ContactInfo.class);
			System.out.println("strId = "+strId);
			System.out.println("strActive = "+strActive);
			contactInfo.putExtra("ID", strId);
			contactInfo.putExtra("VIEW", true);
			contactInfo.putExtra("ACTIVE", strActive);
			ContactProfile.this.startActivity(contactInfo);
		}
	};
	
	private OnClickListener doShowOpportunityList = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent iOpportunity = new Intent(ContactProfile.this, OpportunityList.class);
			iOpportunity.putExtra("ID", strId);
			iOpportunity.putExtra("CONTACT_ID", strContactId);
			ContactProfile.this.startActivity(iOpportunity);
		}
	};
	
	private OnClickListener doShowActivityHistory = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent contactInfo = new Intent(ContactProfile.this, ActivityHistory.class);
			contactInfo.putExtra("ID", strId);
			contactInfo.putExtra("TYPE", Constants.PERSON_TYPE_CONTACT);
			ContactProfile.this.startActivity(contactInfo);
		}
	};
	
	private class LoadContact extends AsyncTask<String, Void, ContactDetail> {

		@Override
		protected ContactDetail doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(ContactProfile.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			return contact.getContactDetailById(params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactProfile.this, false, null);
		}

		@Override
		protected void onPostExecute(ContactDetail result) {
			super.onPostExecute(result);
			aEmailList = new ArrayList<EmailDetail>();
			aContactNumberList = new ArrayList<PhoneDetail>();
			
			contact = result;
			first_name.setText(result.getFirstName());
			last_name.setText(result.getLastName());
			company.setText(result.getCompanyName());
			
			if (result.getPhoto() != null) {
				StorageUtility storage = new StorageUtility(ContactProfile.this, Constants.CACHE_FOLDER);
				if (mBitmap != null) {
					mBitmap.recycle();
					mBitmap = null;
				}
				mBitmap = storage.doReadImage(result.getPhoto());
				if (mBitmap != null) {
					photo.setImageBitmap(mBitmap);
				} else {
					photo.setImageResource(R.drawable.contacts);
				}
			}
			
			if (result.getEmail1() != null && result.getEmail1().length() > 0) {
				aEmailList.add(new EmailDetail(result.getEmail1(), getString(R.string.email)));
			}
			
			if (result.getEmail2() != null && result.getEmail2().length() > 0) {
				aEmailList.add(new EmailDetail(result.getEmail2(), getString(R.string.email2)));
			}
			
			if (result.getEmail3() != null && result.getEmail3().length() > 0) {
				aEmailList.add(new EmailDetail(result.getEmail3(), getString(R.string.email3)));
			}
			
			if (result.getMobile() != null && result.getMobile().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getMobile(), getString(R.string.mobile)));
			}
			
			if (result.getWorkPhone() != null && result.getWorkPhone().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getWorkPhone(), getString(R.string.work_phone)));
			}
			
			if (result.getHomePhone() != null && result.getHomePhone().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getHomePhone(), getString(R.string.home_phone)));
			}
			
			if (result.getOtherPhone() != null && result.getOtherPhone().length() > 0) {
				aContactNumberList.add(new PhoneDetail(result.getOtherPhone(), getString(R.string.other_phone)));
			}
			
			if (aContactNumberList.size() == 0) {
				call.setBackgroundResource(R.drawable.disabled_button);
				call.setOnClickListener(null);
				
				sms.setBackgroundResource(R.drawable.disabled_button);
				sms.setOnClickListener(null);
			} else {
				call.setBackgroundResource(R.drawable.orange_button);
				sms.setBackgroundResource(R.drawable.orange_button);
				if (aContactNumberList.size() == 1) {
					call.setOnClickListener(doCall);
					sms.setOnClickListener(doComposeSms);
				} else {
					call.setOnClickListener(doShowContextMenu);
					sms.setOnClickListener(doShowContextMenu);
				}
			}
			
			if (aEmailList.size() == 0) {
				email.setBackgroundResource(R.drawable.disabled_button);
				email.setOnClickListener(null);
			} else {
				email.setBackgroundResource(R.drawable.orange_button);
				if (aEmailList.size() == 1) {
					email.setOnClickListener(doComposeEmail);
				} else {
					email.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							registerForContextMenu(v);
							openContextMenu(v);
						    unregisterForContextMenu(v);
						}
					});
				}
			}
			
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
		
		private OnClickListener doShowContextMenu = new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				registerForContextMenu(v);
				openContextMenu(v);
			    unregisterForContextMenu(v);
			}
		};
		
		private OnClickListener doComposeEmail = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doLogActivity(Constants.ACTION_EMAIL, aEmailList.get(0).getEmailAddress());
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				String sEmailList[] = {aEmailList.get(0).getEmailAddress()};
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, sEmailList);
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_in)));  
			}
		};
		
		private OnClickListener doComposeSms = new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				doLogActivity(Constants.ACTION_SMS, aContactNumberList.get(0).getPhoneNo());
				Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("sms:"+aContactNumberList.get(0).getPhoneNo()));
				startActivity(smsIntent);
			}
		};
		
		private OnClickListener doCall = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doLogActivity(Constants.ACTION_CALL, aContactNumberList.get(0).getPhoneNo());
				Intent callIntent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:"+aContactNumberList.get(0).getPhoneNo()));
				startActivity(callIntent);
			}
		};
	}
	
	private void doLogActivity(String strActionType, String strActionInfo) {
		DatabaseUtility.getDatabaseHandler(ContactProfile.this);
		String strOwner = Utility.getConfigByText(ContactProfile.this, "USER_EMAIL");
		ActivityLog log = new ActivityLog(Constants.DBHANDLER);
		
		ActivitiesLogDetail detail = new ActivitiesLogDetail();
		detail.setContactId(contact.getContactId());
		detail.setContactNum(contact.getInternalNumber());
		detail.setActType(strActionType);
		detail.setFirstName(contact.getFirstName());
		detail.setLastName(contact.getLastName());
		detail.setPersonType(Constants.PERSON_TYPE_CONTACT);
		detail.setIsUpdate("false");
		detail.setOwner(strOwner);
		detail.setUserStamp(strOwner);
		if (strActionType.equalsIgnoreCase(Constants.ACTION_EMAIL)) {
			detail.setEmail(strActionInfo);
		} else {
			detail.setMobile(strActionInfo);
		}
		log.insert(detail);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			StorageUtility storage = new StorageUtility(ContactProfile.this, Constants.CACHE_FOLDER);
			if (requestCode == SHOOT_PHOTO && resultCode == Activity.RESULT_OK) {
				File fDirectory;
				File fPhoto;
				if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
					fDirectory = new File(android.os.Environment.getExternalStorageDirectory(), Constants.CACHE_FOLDER);
				} else {
					fDirectory = ContactProfile.this.getCacheDir();
				}
				
				if (!fDirectory.exists()) {
					fDirectory.mkdirs();
				}
				
				fPhoto = new File(fDirectory, "firstlink.jpg");
				String strFilename = null;
				if (contact.getPhoto() == null) {
					strFilename = storage.doGenerateUUID();
					contact.setPhoto(strFilename);
				} else {
					strFilename = contact.getPhoto();
				}
				File fStoreImage = new File(fDirectory, strFilename);
				fPhoto.renameTo(fStoreImage);
				try {
					if (mBitmap != null) {
						mBitmap.recycle();
						mBitmap = null;
					}
					
					mBitmap = BitmapFactory.decodeFile(fStoreImage.getAbsolutePath());
					if (mBitmap != null) {
						DeviceUtility.log(TAG, "Bitmap success fully get");
						photo.setImageBitmap(mBitmap);
					} else {
						DeviceUtility.log(TAG, "Unable to get bitmap.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
				Uri uPhotoUri = data.getData();
				Cursor cursor = getContentResolver().query(uPhotoUri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
				cursor.moveToFirst();
				String fPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
				cursor.close();
				try {
					File fImage = new File(fPath);
					if (mBitmap != null) {
						mBitmap.recycle();
						mBitmap = null;
					}
					mBitmap = BitmapFactory.decodeFile(fImage.getAbsolutePath());
					if (mBitmap != null) {
						photo.setImageBitmap(mBitmap);
						if (contact.getPhoto() == null) {
							String strUUID = storage.doStoreImage(mBitmap);
							contact.setPhoto(strUUID);
						} else {
							storage.doStoreImage(mBitmap, contact.getPhoto());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			SaveImage saveImage = new SaveImage();
			saveImage.execute(contact);
		} catch (Exception e) {
			
		}
	}
	
	private class SaveImage extends AsyncTask<ContactDetail, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactProfile.this, false, getString(R.string.processing));
		}

		@Override
		protected Void doInBackground(ContactDetail... params) {
			DatabaseUtility.getDatabaseHandler(ContactProfile.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			contact.updatePhoto(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
}
