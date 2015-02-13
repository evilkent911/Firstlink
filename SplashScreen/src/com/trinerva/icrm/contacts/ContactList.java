package com.trinerva.icrm.contacts;

import java.lang.reflect.Modifier;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;

import com.trinerva.icrm.CrmLogin;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.SynchronizationData;
import com.trinerva.icrm.calendar.CalendarData;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.FLCalendar;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.event.ArrayEmailSchedule;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.object.CalendarDetail;
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
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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

public class ContactList extends Activity {
	private EditText search;
	private SimpleCursorAdapter adapter;
	// MyListViewAdapter adapter;
	// private Cursor mCursor;
	private ListView list;
	private Dialog loadingDialog;
	private TextView empty_contact_list;
	ImageView add_new;
	private static final int PICK_CONTACT = 120;
	private static String TAG = "ContactList";
	Context context;
	// TextView spinner;

	private PopupWindow popupWindow;
	private LinearLayout layout;
	private ListView listView;
	private String title[] = { "New This Week", "Modified This Week",
			"My Contact" };

	private float x, ux;

	List<CalendarData> cldList;
	CalendarData cldData;
	boolean checkDeleteButton;
	Button allDtnDel;

	SaveData saveData;

	int nowSelectStatus;
	final int newThisWeek = 0;
	final int modifiedThisWeek = 1;
	final int myContact = 2;

	String weekCount;
	TextView mTarget;
	
	TextView mUnSyncText;
	Button mSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		context = this;

		saveData = new SaveData(this);

		nowSelectStatus = myContact;
		mTarget = (TextView) findViewById(R.id.target);
		search = (EditText) findViewById(R.id.search);
		list = (ListView) findViewById(R.id.list);
		empty_contact_list = (TextView) findViewById(R.id.empty_contact_list);
		add_new = (ImageView) findViewById(R.id.add_new);
		add_new.setOnClickListener(addNew);
		search.addTextChangedListener(filterTextWatcher);
		mSync = (Button) findViewById(R.id.synNow);
		mUnSyncText = (TextView) findViewById(R.id.unsyncText);
		cldList = new ArrayList<CalendarData>();

		// Calendar now = Calendar.getInstance();
		// System.out.println("now week "+now.get(Calendar.WEEK_OF_YEAR));

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("date = " + new java.util.Date());
		weekCount = sf.format(new java.util.Date());
		list.setFocusableInTouchMode(true);
		list.setCacheColorHint(0000000);
		list.setFastScrollEnabled(true);
		list.setVerticalFadingEdgeEnabled(true);
		list.setOnItemClickListener(doItemClick);

		if (!Utility.getConfigByText(context, Constants.DELETE_CONTACT).equals(
				"0")) {
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {

					GuiUtility.alert(ContactList.this, "",
							getString(R.string.delete_contact), Gravity.CENTER,
							getString(R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}, getString(R.string.delete),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Cursor cursor = adapter.getCursor();
									cursor.moveToPosition(position);
									String strInternalNum = cursor.getString(cursor
											.getColumnIndex("INTERNAL_NUM"));

									Contact contact = new Contact(
											Constants.DBHANDLER);
									contact.delete(strInternalNum);

									LoadContact task = new LoadContact();
									task.execute(new String[] { null });
								}
							});
					return true;
				}
			});

		}

		// list.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View view, MotionEvent event) {
		// // TODO Auto-generated method stub
		// // if (btnDel == null)
		// btnDel = (Button) view.findViewById(R.id.del);
		//
		// view.setOnTouchListener(new OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// // 当按下时处�?�
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// // 设置背景为选中状�?
		// // v.setBackgroundResource(R.drawable.mm_listitem_pressed);
		// // 获�?�按下时的x轴�??标
		// x = event.getX();
		// // 判断之�?是�?�出现了删除按钮如果存在就�?�?
		//
		// } else if (event.getAction() == MotionEvent.ACTION_UP) {// �?�开处�?�
		// // 设置背景为未选中正常状�?
		// // v.setBackgroundResource(R.drawable.mm_listitem_simple);
		// // 获�?��?�开时的x�??标
		// ux = event.getX();
		// // 判断当�?项中按钮控件�?为空时
		// if (btnDel != null) {
		// // 按下和�?�开�?对值差当大于20时显示删除按钮，�?�则�?显示
		//
		// if (btnDel.getVisibility() == View.VISIBLE) {
		// if (btnDel != null) {
		// btnDel.setVisibility(View.GONE);
		// }
		// } else {
		// if (Math.abs(x - ux) > 20) {
		// System.out.println("Sdddddddooo = "
		// + btnDel);
		// btnDel.setVisibility(View.VISIBLE);
		// curDel_btn = btnDel;
		// curDel_btn.setVisibility(View.VISIBLE);
		// } else {
		// int pos = list.pointToPosition(
		// (int) event.getX(),
		// (int) event.getY());
		// System.out.println("hahahaha = " + pos);
		// selectInfo(pos);
		// }
		// }
		//
		// }
		// }
		//
		// // else if (event.getAction() ==
		// // MotionEvent.ACTION_MOVE)
		// // {//当滑动时背景为选中状�?
		// // v.setBackgroundResource(R.drawable.mm_listitem_pressed);
		// // } else {//其他模�?
		// // //设置背景为未选中正常状�?
		// // v.setBackgroundResource(R.drawable.mm_listitem_simple);
		// // }
		// return true;
		// }
		// });
		//
		// return false;
		// }
		// });

		// Resources res = getResources();
		// CharSequence[] platforms = res.getTextArray(R.array.arrary);
		//
		// spinner = (TextView) findViewById(R.id.target);
		// spinner.setText(title[0]);
		// spinner.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// spinner.getTop();
		// int y = spinner.getBottom() * 3 / 2;
		// int x = getWindowManager().getDefaultDisplay().getWidth() / 4;
		//
		// showPopupWindow(x, y);
		// }
		// });

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		mSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Click");
				v.setClickable(false);
				if (saveData.getBooleanData(GetData.checkLogin)) {
					Intent sync = new Intent(ContactList.this,
							SynchronizationData.class);
					Bundle bFlag = new Bundle();
					bFlag.putString("ACTION", "NORMAL");
					bFlag.putString("EMAIL",
							saveData.getStringData(GetData.getUserEmail));
					bFlag.putString("PASSWORD",
							saveData.getStringData(GetData.getUserPassword));
					bFlag.putString("FROM", "CRMLOGIN");
					sync.putExtras(bFlag);
					ContactList.this.startActivity(sync);
				} else {
					Intent contact = new Intent(ContactList.this,
							CrmLogin.class);
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
				intent.putExtra("filterPage", 0);
				intent.setClass(ContactList.this,
						ContactFilterListActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		
//		CalendarDetail myAdd = new CalendarDetail("10:10");
//		ArrayEmailSchedule sci = new ArrayEmailSchedule();
//		sci.add(myAdd);
//		PropertyInfo tabProp1 = new PropertyInfo();
//	       tabProp1.setName("sqecol");
//	       tabProp1.setValue(sci);
//	       tabProp1.setType(ArrayEmailSchedule.class);
//System.out.println("tabProp1 = "+tabProp1);
		
		
	}

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			String strActive = cursor
					.getString(cursor.getColumnIndex("ACTIVE"));
			System.out.println("YY = " + strActive);
			String strInternalNum = cursor.getString(cursor
					.getColumnIndex("INTERNAL_NUM"));
			String strContactId = cursor.getString(cursor
					.getColumnIndex("CONTACT_ID"));
			
			if (strActive.equalsIgnoreCase("1")) {
				// view only. pass to contact info.
				Intent info = new Intent(ContactList.this, ContactInfoTab.class);
				info.putExtra("ID", strInternalNum);
				info.putExtra("VIEW", true);
				info.putExtra("ACTIVE", strActive);
				info.putExtra("NAME_TO", "2-"+strInternalNum);
				ContactList.this.startActivity(info);
			} else {
				// pass to contact profile
				Intent info = new Intent(ContactList.this, ContactInfoTab.class);
				info.putExtra("ID", strInternalNum);
				info.putExtra("ACTIVE", strActive);
				info.putExtra("VIEW", true);
				info.putExtra("CONTACT_ID", strContactId);
				info.putExtra("NAME_TO", "2-"+strInternalNum);
				ContactList.this.startActivity(info);
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// String strUri = data.getDataString();
			// DeviceUtility.log(TAG, "strUri: " + strUri);
			// Intent contact = new Intent(ContactList.this,
			// ContactInfoTab.class);
			// contact.putExtra("CONTACT_URI", strUri);
			// ContactList.this.startActivity(contact);

			switch (requestCode) {
			case PICK_CONTACT:
				if (resultCode == Activity.RESULT_OK) {
					String strUri = data.getDataString();
					DeviceUtility.log(TAG, "strUri: " + strUri);
					Intent contact = new Intent(ContactList.this,
							ContactInfoTab.class);
					contact.putExtra("CONTACT_URI", strUri);
					ContactList.this.startActivity(contact);
				}
				break;
			}

			nowSelectStatus = data.getIntExtra("selete", 0);

			System.out.println("haha = " + nowSelectStatus);

			switch (nowSelectStatus) {
			case 0:
				mTarget.setText(getString(R.string.new_this_week));
				break;
			case 1:
				mTarget.setText(getString(R.string.modified_this_week));
				break;
			case 2:
				mTarget.setText(getString(R.string.my_contact));
				break;
			default:
				break;
			}
		}
	}

	private OnClickListener addNew = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
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
								Intent contact = new Intent(ContactList.this,
										ContactInfoTab.class);
								Bundle mBundle = new Bundle();
								mBundle.putString("lastName", "");
								contact.putExtras(mBundle);
								ContactList.this.startActivity(contact);
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
		// Intent contact = new Intent(ContactList.this, ContactInfo.class);
		// ContactList.this.startActivity(contact);
		// }
		// };
		//
		// @Override
		// public void onClick(View v) {
		// GuiUtility.alert(ContactList.this,
		// getString(R.string.new_contact_creation),
		// getString(R.string.import_contact_instruction), Gravity.CENTER,
		// getString(R.string.address_book), importContact,
		// getString(R.string.manual_entry), manualEntry);
		// }
	};

	private class LoadContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(ContactList.this);
			Contact contact = new Contact(Constants.DBHANDLER);
			switch (nowSelectStatus) {
			case newThisWeek:
				return contact.getNewThisWeekContactListDisplay(weekCount);
			case modifiedThisWeek:
				return contact.getModifiedThisWeekContactListDisplay(weekCount);
			default:
				return contact.getContactListDisplay();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(ContactList.this,
					false, null);
			
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
								ContactList.this, Constants.CACHE_FOLDER);
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
//											.getColumnIndex("IS_UPDATE"))
//									+ " ACTIVE: "
//									+ cursor.getString(cursor
//											.getColumnIndex("ACTIVE")));
//					String strIsUpdate = cursor.getString(cursor
//							.getColumnIndex("IS_UPDATE"));
//					String strActive = cursor.getString(cursor
//							.getColumnIndex("ACTIVE"));
//					String strOppUpdate = cursor.getString(cursor
//							.getColumnIndex("OPP_UPDATE"));
//
//					if (strActive.equalsIgnoreCase("0")
//							&& strIsUpdate.equalsIgnoreCase("true")) {
//						if (strOppUpdate != null
//								&& strOppUpdate.equalsIgnoreCase("false")) {
//							cSync.setVisibility(View.VISIBLE);
//							cSync.setImageResource(R.drawable.btn_sync);
//						} else {
//							cSync.setVisibility(View.GONE);
//						}
//					} else {
//						cSync.setVisibility(View.VISIBLE);
//						if (strActive.equalsIgnoreCase("1")) {
//							cSync.setImageResource(R.drawable.btn_sync_deleted);
//						} else {
//							DeviceUtility.log(TAG, "show sync button");
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

			adapter = new SimpleCursorAdapter(ContactList.this,
					R.layout.contact_item, result, new String[] { "_id",
							"CONTACT_ID", "FIRST_NAME", "LAST_NAME",
							"COMPANY_NAME", "PHOTO", "IS_UPDATE", "ACTIVE",
							"IS_UPDATE" }, new int[] { R.id.photo,
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
				empty_contact_list.setVisibility(View.VISIBLE);
			} else {
				empty_contact_list.setVisibility(View.GONE);
			}

			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

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
			try {
				DeviceUtility.log(TAG, "filter: " + constraint);
				DatabaseUtility.getDatabaseHandler(ContactList.this);
				Contact contact = new Contact(Constants.DBHANDLER);
				Cursor cursor;

				switch (nowSelectStatus) {
				case newThisWeek:
					// return contact.getNewThisWeekContactListDisplayByFilter(
					// constraint.toString(), weekCount);
					return contact.getNewThisWeekContactListDisplayByFilter(
							constraint.toString(), weekCount);
				case modifiedThisWeek:
					return contact
							.getModifiedThisWeekContactListDisplayByFilter(
									constraint.toString(), weekCount);
				default:
					return contact.getContactListDisplayByFilter(constraint
							.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		search.removeTextChangedListener(filterTextWatcher);
		if (adapter != null) {
			adapter.getCursor().close();
			adapter = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSync.setClickable(true);
		LoadContact task = new LoadContact();
		task.execute(new String[] { null });
	}

	// public void showPopupWindow(int x, int y) {
	// layout = (LinearLayout) LayoutInflater.from(ContactList.this).inflate(
	// R.layout.pop_out_dialog, null);
	// listView = (ListView) layout.findViewById(R.id.lv_dialog);
	// listView.setAdapter(new ArrayAdapter<String>(ContactList.this,
	// R.layout.text, R.id.tv_text, title));
	//
	// popupWindow = new PopupWindow(layout, (getWindowManager()
	// .getDefaultDisplay().getWidth() / 2), LayoutParams.WRAP_CONTENT);
	// popupWindow.setBackgroundDrawable(new BitmapDrawable());
	// // popupWindow
	// // .setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
	// // popupWindow.setHeight(300);
	// popupWindow.setOutsideTouchable(true);
	// popupWindow.setFocusable(true);
	// popupWindow.setContentView(layout);
	// // showAsDropDown会把里�?�的view作为�?�照物，所以�?那满�?幕parent
	// // popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
	// popupWindow.showAtLocation(spinner, Gravity.LEFT | Gravity.TOP, x,
	// y + 10);// 需�?指定Gravity，默认情况是center.
	//
	// listView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// spinner.setText(title[arg2]);
	//
	// // switch (arg2) {
	// // case newThisWeek:
	// //
	// // break;
	// // case Modifier
	// // default:
	// // break;
	// // }
	// nowSelectStatus = arg2;
	// LoadContact task = new LoadContact();
	// task.execute(new String[] { null });
	//
	// popupWindow.dismiss();
	// popupWindow = null;
	// }
	// });
	// }
	boolean getUnsyncData(){
		Contact contact = new Contact(Constants.DBHANDLER);
		Cursor cursor = contact.getContactListDisplay();
		do {
			if (cursor.getCount() != 0) {
				System.out.println("is sync = "+cursor.getString(cursor.getColumnIndex("IS_UPDATE")));
				String oppUpdate = "true";
				if(cursor.getString(cursor.getColumnIndex("OPP_UPDATE")) != null){
					oppUpdate = cursor.getString(cursor.getColumnIndex("OPP_UPDATE"));
				}
				
				if (cursor.getString(cursor.getColumnIndex("IS_UPDATE"))
						.equals("false") || oppUpdate.equals("false")) {
					return false;
				}
			}
		} while (cursor.moveToNext());
		return true;
	}
}
