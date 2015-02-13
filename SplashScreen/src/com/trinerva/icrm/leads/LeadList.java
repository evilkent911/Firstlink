package com.trinerva.icrm.leads;

import java.text.SimpleDateFormat;

import com.trinerva.icrm.CrmLogin;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.SynchronizationData;
import com.trinerva.icrm.contacts.ContactFilterListActivity;
import com.trinerva.icrm.contacts.ContactList;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DatabaseUtility;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.GuiUtility;
import com.trinerva.icrm.utility.StorageUtility;
import com.trinerva.icrm.utility.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LeadList extends Activity {
	private EditText search;
	private SimpleCursorAdapter adapter;
	// private Cursor mCursor;
	private ListView list;
	private Dialog loadingDialog;
	private TextView empty_lead_list;
	ImageView add_new;
	private static final int PICK_CONTACT = 130;
	private static String TAG = "LeadList";
	SaveData saveData;
	Context context;

	private PopupWindow popupWindow;
	private LinearLayout layout;
	private ListView listView;
	private String title[] = { "New This Week", "Modified This Week",
			"My Lead" };
	TextView mTarget;
	int nowSelectStatus;
	final int newThisWeek = 0;
	final int modifiedThisWeek = 1;
	final int myContact = 2;

	String weekCount;
	
	TextView mUnSyncText;
	Button mSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lead_list);
		context = this;
		saveData = new SaveData(this);
		search = (EditText) findViewById(R.id.search);
		list = (ListView) findViewById(R.id.list);
		empty_lead_list = (TextView) findViewById(R.id.empty_lead_list);
		add_new = (ImageView) findViewById(R.id.add_new);
		add_new.setOnClickListener(addNew);
		search.addTextChangedListener(filterTextWatcher);
		mUnSyncText = (TextView) findViewById(R.id.unsyncText);
		mSync = (Button) findViewById(R.id.synNow);
		
		nowSelectStatus = myContact;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("date = " + new java.util.Date());
		weekCount = sf.format(new java.util.Date());

		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);
		
		if(!Utility.getConfigByText(context, Constants.DELETE_LEAD).equals("0")){
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(context);
//					builder.setMessage(getString(R.string.delete_lead));
//					builder.setPositiveButton(R.string.confirm,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int id) {
	//
//									Cursor cursor = adapter.getCursor();
//									cursor.moveToPosition(position);
	//
//									Lead lead = new Lead(Constants.DBHANDLER);
//									lead.delete(cursor.getString(cursor
//											.getColumnIndex("INTERNAL_NUM")));
	//
//									LoadContact task = new LoadContact();
//									task.execute();
//								}
//							});
//					builder.setNegativeButton(R.string.cancel,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int id) {
//								}
//							});
//					AlertDialog dialog = builder.create();
//					dialog.show();
					
				GuiUtility.alert(LeadList.this, "", getString(R.string.delete_lead), Gravity.CENTER, getString(R.string.cancel), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							
						
							
						}
					}, getString(R.string.delete), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Cursor cursor = adapter.getCursor();
							cursor.moveToPosition(position);

							Lead lead = new Lead(Constants.DBHANDLER);
							lead.delete(cursor.getString(cursor
									.getColumnIndex("INTERNAL_NUM")));

							LoadContact task = new LoadContact();
							task.execute();
						}
					});
					return true;
				}
			});
		}

//		Resources res = getResources();
//		CharSequence[] platforms = res.getTextArray(R.array.arrary);

		mTarget = (TextView) findViewById(R.id.target);
//		spinner.setText(title[0]);
//		spinner.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				spinner.getTop();
//				int y = spinner.getBottom() * 3 / 2;
//				int x = getWindowManager().getDefaultDisplay().getWidth() / 4;
//
//				showPopupWindow(x, y);
//			}
//		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		mSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (saveData.getBooleanData(GetData.checkLogin)) {
					v.setClickable(false);
					Intent sync = new Intent(LeadList.this,
							SynchronizationData.class);
					Bundle bFlag = new Bundle();
					bFlag.putString("ACTION", "NORMAL");
					bFlag.putString("EMAIL",
							saveData.getStringData(GetData.getUserEmail));
					bFlag.putString("PASSWORD",
							saveData.getStringData(GetData.getUserPassword));
					bFlag.putString("FROM", "CRMLOGIN");
					sync.putExtras(bFlag);
					LeadList.this.startActivity(sync);
				} else {
					Intent contact = new Intent(LeadList.this, CrmLogin.class);
					startActivity(contact);
				}

			}
		});
		
		nowSelectStatus = myContact;

		findViewById(R.id.target).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("nowSelete", nowSelectStatus);
				intent.putExtra("filterPage", 1);
				intent.setClass(LeadList.this,
						ContactFilterListActivity.class);
				startActivityForResult(intent,1);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSync.setClickable(true);
		LoadContact task = new LoadContact();
		task.execute();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		search.removeTextChangedListener(filterTextWatcher);
		if (adapter != null) {
			adapter.getCursor().close();
			adapter = null;
		}
	}

//	public void showPopupWindow(int x, int y) {
//		layout = (LinearLayout) LayoutInflater.from(LeadList.this).inflate(
//				R.layout.pop_out_dialog, null);
//		listView = (ListView) layout.findViewById(R.id.lv_dialog);
//		listView.setAdapter(new ArrayAdapter<String>(LeadList.this,
//				R.layout.text, R.id.tv_text, title));
//
//		popupWindow = new PopupWindow(layout, (getWindowManager()
//				.getDefaultDisplay().getWidth() / 2), LayoutParams.WRAP_CONTENT);
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		// popupWindow
//		// .setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
//		// popupWindow.setHeight(300);
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.setFocusable(true);
//		popupWindow.setContentView(layout);
//		// showAsDropDown会把里�?�的view作为�?�照物，所以�?那满�?幕parent
//		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
//		popupWindow.showAtLocation(spinner, Gravity.LEFT | Gravity.TOP, x,
//				y + 10);// 需�?指定Gravity，默认情况是center.
//
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				spinner.setText(title[arg2]);
//
//				// switch (arg2) {
//				// case newThisWeek:
//				//
//				// break;
//				// case Modifier
//				// default:
//				// break;
//				// }
//				nowSelectStatus = arg2;
//				LoadContact task = new LoadContact();
//				task.execute();
//
//				popupWindow.dismiss();
//				popupWindow = null;
//			}
//		});
//	}

	private OnClickListener addNew = new OnClickListener() {
		
		// private DialogInterface.OnClickListener importContact = new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new Intent(Intent.ACTION_PICK,
		// ContactsContract.Contacts.CONTENT_URI);
		// startActivityForResult(intent, PICK_CONTACT);
		// }
		// };
		//
		// private DialogInterface.OnClickListener manualEntry = new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Intent contact = new Intent(LeadList.this, LeadInfo.class);
		// LeadList.this.startActivity(contact);
		// }
		// };

		@Override
		public void onClick(View v) {
			// GuiUtility.alert(LeadList.this,
			// getString(R.string.new_lead_creation),
			// getString(R.string.import_lead_instruction),
			// Gravity.CENTER, getString(R.string.address_book),
			// importContact, getString(R.string.manual_entry),
			// manualEntry);

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(R.array.string_array_name,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// The 'which' argument contains the index
							// position
							// of the selected item

							switch (which) {
							case 0:
								Intent intent = new Intent(Intent.ACTION_PICK,
										ContactsContract.Contacts.CONTENT_URI);
								startActivityForResult(intent, PICK_CONTACT);
								break;
							case 1:
								Intent contact = new Intent(LeadList.this,
										LeadInfo.class);
								LeadList.this.startActivity(contact);
								break;
							case 2:

								break;
							default:
								break;
							}
						}
					});
			builder.show();
		}
	};

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (adapter != null) {
				adapter.getFilter().filter(s);
			}
		}
	};

	private FilterQueryProvider filter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			DeviceUtility.log(TAG, "filter: " + constraint);
			DatabaseUtility.getDatabaseHandler(LeadList.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			Cursor cursor;

			switch (nowSelectStatus) {
			case newThisWeek:
				return lead.getNewThisWeekLeadListDisplayByFilter(
						constraint.toString(), weekCount);
			case modifiedThisWeek:
				return lead.getModifiedThisWeekLeadListDisplayByFilter(
						constraint.toString(), weekCount);
			default:
				return lead.getLeadListDisplayByFilter(constraint.toString());
			}
		}
	};

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			String strActive = cursor
					.getString(cursor.getColumnIndex("ACTIVE"));
			String strFirstName = cursor.getString(cursor
					.getColumnIndex("FIRST_NAME"));
			String strLastName = cursor.getString(cursor
					.getColumnIndex("LAST_NAME"));
			String strInternalNum = cursor.getString(cursor
					.getColumnIndex("INTERNAL_NUM"));
			if (strFirstName == null) {
				strFirstName = strLastName;
			} else {
				strFirstName = strFirstName + " " + strLastName;
			}
			if (strActive.equalsIgnoreCase("1")) {
				// view only. pass to contact info.
				Intent info = new Intent(LeadList.this, LeadInfo.class);
				info.putExtra("ID",
						cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
				info.putExtra("VIEW", true);
				info.putExtra("ACTIVE", strActive);
				info.putExtra("NAME_TO", "3-"+strInternalNum);
				LeadList.this.startActivity(info);
			} else {
				// pass to contact profile
				Intent info = new Intent(LeadList.this, LeadInfo.class);
				info.putExtra("ID",
						cursor.getString(cursor.getColumnIndex("INTERNAL_NUM")));
				info.putExtra("VIEW", true);
				info.putExtra("ACTIVE", strActive);
				info.putExtra("NAME_TO", "3-"+strInternalNum);
				LeadList.this.startActivity(info);
			}
		}

	};

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case PICK_CONTACT:
			if (resultCode == Activity.RESULT_OK) {
//				String strUri = data.getDataString();
//				DeviceUtility.log(TAG, "strUri: " + strUri);
//				Intent contact = new Intent(LeadList.this, LeadInfo.class);
//				contact.putExtra("CONTACT_URI", strUri);
//				LeadList.this.startActivity(contact);
				
				nowSelectStatus = data.getIntExtra("selete", 0);
				
				System.out.println("haha = "+nowSelectStatus);
				
				switch (requestCode) {
				case PICK_CONTACT:
					if (resultCode == Activity.RESULT_OK) {
						String strUri = data.getDataString();
						DeviceUtility.log(TAG, "strUri: " + strUri);
						Intent contact = new Intent(LeadList.this, LeadInfo.class);
						contact.putExtra("CONTACT_URI", strUri);
						LeadList.this.startActivity(contact);
					}
				break;
				default:
					switch (nowSelectStatus) {
					case 0:
						mTarget.setText(getString(R.string.new_this_week));
						break;
					case 1:
						mTarget.setText(getString(R.string.modified_this_week));
						break;
					case 2:
						mTarget.setText(getString(R.string.my_lead));
						break;
					
					}
					break;
				}
			
				
			}
//			break;
//		}
	}

	private class LoadContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(LeadList.this);
			Lead lead = new Lead(Constants.DBHANDLER);
			// return lead.getLeadListDisplay();s

			switch (nowSelectStatus) {
			case newThisWeek:
				return lead.getNewThisWeekLeadListDisplay(weekCount);
			case modifiedThisWeek:
				return lead.getModifiedThisWeekLeadListDisplay(weekCount);
			default:
				return lead.getLeadListDisplay();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(LeadList.this, false,
					null);
			
			if(getUnsyncData()){
				mUnSyncText.setVisibility(View.GONE);
			}else{
				mUnSyncText.setVisibility(View.VISIBLE);
			}
			
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int viewId = view.getId();
				switch (viewId) {
				case R.id.company_name:
					TextView company = (TextView) view;
					company.setText("");
					company.setText(cursor.getString(cursor
							.getColumnIndex("COMPANY_NAME")));
					break;
				case R.id.contact_name:
					TextView name = (TextView) view;
					name.setText("");
					String strFirstName = cursor.getString(cursor
							.getColumnIndex("FIRST_NAME"));
					String strLastName = cursor.getString(cursor
							.getColumnIndex("LAST_NAME"));
					if (strFirstName == null) {
						name.setText(strLastName);
					} else {
						name.setText(strFirstName + " " + strLastName);
					}
					break;
				case R.id.photo:
					ImageView photo = (ImageView) view;
					String photoId = cursor.getString(cursor
							.getColumnIndex("PHOTO"));
					if (photoId != null) {
						StorageUtility storage = new StorageUtility(
								LeadList.this, Constants.CACHE_FOLDER);
						Bitmap bitmap = storage.doReadImage(photoId);
						if (bitmap != null) {
							photo.setImageBitmap(bitmap);
						} else {
							photo.setImageResource(R.drawable.contacts);
						}
					} else {
						photo.setImageResource(R.drawable.contacts);
					}
					break;
				case R.id.contact_sync:
//					ImageView cSync = (ImageView) view;
//					DeviceUtility.log(
//							TAG,
//							"IS_UPDATE: "
//									+ cursor.getString(cursor
//											.getColumnIndex("IS_UPDATE")));
//					String strIsUpdate = cursor.getString(cursor
//							.getColumnIndex("IS_UPDATE"));
//					String strActive = cursor.getString(cursor
//							.getColumnIndex("ACTIVE"));
//
//					if (strActive.equalsIgnoreCase("0")
//							&& strIsUpdate.equalsIgnoreCase("true")) {
//						cSync.setVisibility(View.GONE);
//					} else {
//						cSync.setVisibility(View.VISIBLE);
//						if (strActive.equalsIgnoreCase("1")) {
//							cSync.setImageResource(R.drawable.btn_sync_deleted);
//						} else {
//							cSync.setImageResource(R.drawable.btn_sync);
//						}
//					}
					break;
				}
				return true;
			}
		};

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			adapter = new SimpleCursorAdapter(LeadList.this,
					R.layout.lead_item, result, new String[] { "_id",
							"FIRST_NAME", "LAST_NAME", "COMPANY_NAME", "PHOTO",
							"IS_UPDATE", "ACTIVE" }, new int[] { R.id.photo,
							R.id.contact_name, R.id.company_name,
							R.id.contact_sync });
			adapter.setViewBinder(binder);
			DeviceUtility.log(TAG, "cursor count: " + result.getCount());
			adapter.setFilterQueryProvider(filter);
			list.setAdapter(adapter);
			String strSearch = search.getText().toString();
			if (strSearch.length() > 0) {
				adapter.getFilter().filter(strSearch);
			}

			if (result.getCount() == 0) {
				empty_lead_list.setVisibility(View.VISIBLE);
			} else {
				empty_lead_list.setVisibility(View.GONE);
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	
	boolean getUnsyncData(){
		Lead lead = new Lead(Constants.DBHANDLER);
		Cursor cursor = lead.getLeadListDisplay();
		
		do {
			if (cursor.getCount() != 0) {
				if (cursor.getString(cursor.getColumnIndex("IS_UPDATE"))
						.equals("false")) {
					return false;
				}
			}
		} while (cursor.moveToNext());
		return true;
	}

}
