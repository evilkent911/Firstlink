package com.trinerva.icrm.leads;

import java.io.File;
import java.util.ArrayList;

import asia.firstlink.icrm.R;

import com.trinerva.icrm.contacts.ActivityHistory;
import com.trinerva.icrm.database.source.ActivityLog;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.object.ActivitiesLogDetail;
import com.trinerva.icrm.object.EmailDetail;
import com.trinerva.icrm.object.LeadDetail;
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


public class LeadProfile extends Activity {
	private String TAG = "LeadProfile";
	private ImageView photo;
	private TextView first_name, last_name, company, delete;
	private Button lead_info, activity_history, email, call, sms;
	private String strId = null;
	private String strActive = null;
	private ArrayList<PhoneDetail> aContactNumberList;
	private ArrayList<EmailDetail> aEmailList;
	private ArrayList<String> aPhotoSelection;
	private static final int EMAIL_GROUP = 0;
	private static final int SMS_GROUP = 1;
	private static final int CALL_GROUP = 2;
	private static final int PHOTO_GROUP = 3;
	private LeadDetail lead;
	private boolean bReload = true;
	private int PICK_IMAGE = 2135;
	private int SHOOT_PHOTO = 2356;
	private Dialog loadingDialog;
	private Bitmap mBitmap = null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		}
		aPhotoSelection = new ArrayList<String>();
		aPhotoSelection.add(getString(R.string.camera_shoot));
		aPhotoSelection.add(getString(R.string.photo_library));
		
		setContentView(R.layout.lead_profile);
		first_name = (TextView) findViewById(R.id.first_name);
		last_name = (TextView) findViewById(R.id.last_name);
		company = (TextView) findViewById(R.id.company);
		delete = (TextView) findViewById(R.id.delete);
		
		lead_info = (Button) findViewById(R.id.lead_info);
		activity_history = (Button) findViewById(R.id.activity_history);
		email = (Button) findViewById(R.id.email);
		call = (Button) findViewById(R.id.call);
		sms = (Button) findViewById(R.id.sms);
		
		photo = (ImageView) findViewById(R.id.photo);
		
		photo.setOnClickListener(doChangePhoto);
		lead_info.setOnClickListener(doShowLeadInfo);
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

	private OnClickListener doChangePhoto = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			registerForContextMenu(v);
			openContextMenu(v);
		    unregisterForContextMenu(v);
		}
	};
	
	private OnClickListener doShowLeadInfo = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			bReload = true;
			Intent contactInfo = new Intent(LeadProfile.this, LeadInfo.class);
			contactInfo.putExtra("ID", strId);
			contactInfo.putExtra("VIEW", true);
			contactInfo.putExtra("ACTIVE", strActive);
			LeadProfile.this.startActivity(contactInfo);
		}
	};
	
	private OnClickListener doShowActivityHistory = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent contactInfo = new Intent(LeadProfile.this, ActivityHistory.class);
			contactInfo.putExtra("ID", strId);
			contactInfo.putExtra("TYPE", Constants.PERSON_TYPE_LEAD);
			LeadProfile.this.startActivity(contactInfo);
		}
	};
	
	private OnClickListener doShowPopUpDelete = new OnClickListener() {
		@Override
		public void onClick(View v) {
			GuiUtility.alert(LeadProfile.this, "", getString(R.string.confirm_delete_lead), Gravity.CENTER, getString(R.string.delete), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Dialog loading = GuiUtility.getLoadingDialog(LeadProfile.this, false, getString(R.string.processing));
					DatabaseUtility.getDatabaseHandler(LeadProfile.this);
					Lead lead = new Lead(Constants.DBHANDLER);
					lead.delete(strId);
					loading.dismiss();
					//back to contact list.
					LeadProfile.this.finish();					
				}
			}, getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});
		}
	};
	
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
				String strSmsNo = aContactNumberList.get(item.getItemId()).getPhoneNo();
				if (strSmsNo.startsWith("6")) {
					strSmsNo = "+" + strSmsNo;
				}
				
				Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("sms:"+strSmsNo));
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
								fDirectory = LeadProfile.this.getCacheDir();
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
	
	private void doLogActivity(String strActionType, String strActionInfo) {
		DatabaseUtility.getDatabaseHandler(LeadProfile.this);
		String strOwner = Utility.getConfigByText(LeadProfile.this, "USER_EMAIL");
		ActivityLog log = new ActivityLog(Constants.DBHANDLER);
		
		ActivitiesLogDetail detail = new ActivitiesLogDetail();
		detail.setContactId(lead.getLeadId());
		detail.setContactNum(lead.getInternalNum());
		detail.setActType(strActionType);
		detail.setFirstName(lead.getFirstName());
		detail.setLastName(lead.getLastName());
		detail.setPersonType(Constants.PERSON_TYPE_LEAD);
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
	
	private class LoadContact extends AsyncTask<String, Void, LeadDetail> {

		@Override
		protected LeadDetail doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(LeadProfile.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			return lead.getLeadDetailById(params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(LeadProfile.this, false, null);
		}

		@Override
		protected void onPostExecute(LeadDetail result) {
			super.onPostExecute(result);
			aEmailList = new ArrayList<EmailDetail>();
			aContactNumberList = new ArrayList<PhoneDetail>();
			
			lead = result;
			first_name.setText(result.getFirstName());
			last_name.setText(result.getLastName());
			company.setText(result.getCompanyName());
			
			if (result.getPhoto() != null) {
				StorageUtility storage = new StorageUtility(LeadProfile.this, Constants.CACHE_FOLDER);
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
				String strSMSNo = aContactNumberList.get(0).getPhoneNo();
				if (strSMSNo.startsWith("6")) {
					strSMSNo = "+" + strSMSNo;
				}
				
				Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("sms:"+strSMSNo));
				startActivity(smsIntent);
			}
		};
		
		private OnClickListener doCall = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doLogActivity(Constants.ACTION_CALL, aContactNumberList.get(0).getPhoneNo());
				String strPhoneNo = aContactNumberList.get(0).getPhoneNo();
				if (strPhoneNo.startsWith("6")) {
					strPhoneNo = "+" + strPhoneNo;
				}
				Intent callIntent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:"+strPhoneNo));
				startActivity(callIntent);
			
			}
		};
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			StorageUtility storage = new StorageUtility(LeadProfile.this, Constants.CACHE_FOLDER);
			if (requestCode == SHOOT_PHOTO && resultCode == Activity.RESULT_OK) {
				//loadingDialog = GuiUtility.getLoadingDialog(LeadProfile.this, false, getString(R.string.processing));
				File fDirectory;
				File fPhoto;
				if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
					fDirectory = new File(android.os.Environment.getExternalStorageDirectory(), Constants.CACHE_FOLDER);
				} else {
					fDirectory = LeadProfile.this.getCacheDir();
				}
				
				if (!fDirectory.exists()) {
					fDirectory.mkdirs();
				}
				
				fPhoto = new File(fDirectory, "firstlink.jpg");
				String strFilename = null;
				if (lead.getPhoto() == null) {
					strFilename = storage.doGenerateUUID();
					lead.setPhoto(strFilename);
				} else {
					strFilename = lead.getPhoto();
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
				}/* finally {
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}*/
				
			} else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
				//loadingDialog = GuiUtility.getLoadingDialog(LeadProfile.this, false, getString(R.string.processing));
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
						if (lead.getPhoto() == null) {
							String strUUID = storage.doStoreImage(mBitmap);
							lead.setPhoto(strUUID);
						} else {
							storage.doStoreImage(mBitmap, lead.getPhoto());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} /*finally {
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}*/
			}
			
			SaveImage saveImage = new SaveImage();
			saveImage.execute(lead);
		} catch (Exception e) {
			
		}
	}
	
	private class SaveImage extends AsyncTask<LeadDetail, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(LeadProfile.this, false, getString(R.string.processing));
		}

		@Override
		protected Void doInBackground(LeadDetail... params) {
			DatabaseUtility.getDatabaseHandler(LeadProfile.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			lead.updatePhoto(params[0]);
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
