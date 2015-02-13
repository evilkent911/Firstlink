package com.trinerva.icrm.contacts;

import java.lang.reflect.Modifier;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.trinerva.icrm.CrmLogin;
import asia.firstlink.icrm.R;
import com.trinerva.icrm.SynchronizationData;
import com.trinerva.icrm.calendar.CalendarData;
import com.trinerva.icrm.database.source.Account;
import com.trinerva.icrm.database.source.Contact;
import com.trinerva.icrm.database.source.Lead;
import com.trinerva.icrm.database.source.Master;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.MyApplication;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.object.AccountDetail;
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

public class CompanyList extends Activity {
	private EditText search;
	private SimpleCursorAdapter adapter;
	// MyListViewAdapter adapter;
	// private Cursor mCursor;
	private ListView list;
	private Dialog loadingDialog;
	private TextView empty_contact_list;
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

	final int newThisWeek = 0;
	final int modifiedThisWeek = 1;
	final int myContact = 2;

	String weekCount;
	TextView mTarget;

	Button mCreate;

	String fiterStr;
	
	MyApplication getData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_list);
		context = this;

		saveData = new SaveData(this);
		
		getData = (MyApplication)getApplicationContext();

		mTarget = (TextView) findViewById(R.id.target);
		search = (EditText) findViewById(R.id.search);
		list = (ListView) findViewById(R.id.list);
		mCreate = (Button) findViewById(R.id.create);

		empty_contact_list = (TextView) findViewById(R.id.empty_contact_list);
		search.addTextChangedListener(filterTextWatcher);

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

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.create).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("companyName");
				intent.putExtra("companyName",fiterStr);
				sendBroadcast(intent);			
				finish();
				getData.setCompanyId("");
			}
		});
	}

	private OnItemClickListener doItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			Intent intent = new Intent();
			intent.setAction("companyName");
			intent.putExtra("companyName",
					cursor.getString(cursor.getColumnIndex("ACCOUNT_NAME")));
			intent.putExtra("companyId",
					cursor.getString(cursor.getColumnIndex("ACCOUNT_ID")));
			getData.setCompanyId(cursor.getString(cursor.getColumnIndex("ACCOUNT_ID")));
			sendBroadcast(intent);
			finish();
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
					Intent contact = new Intent(CompanyList.this,
							ContactInfoTab.class);
					contact.putExtra("CONTACT_URI", strUri);
					CompanyList.this.startActivity(contact);
				}
				break;
			}

		}
	}

	private class LoadContact extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... params) {
			DatabaseUtility.getDatabaseHandler(CompanyList.this);
//			Contact contact = new Contact(Constants.DBHANDLER);
			
			Account master = new Account(Constants.DBHANDLER);
//			System.out.println("AAA = "+master.getAllMasterCompany("Company").getCount());
			return master.getAccountListDisplay();
			
//			return contact.getContactListDisplay();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = GuiUtility.getLoadingDialog(CompanyList.this,
					false, null);
		}

		private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int viewId = view.getId();
				switch (viewId) {
				case R.id.company_name:
					TextView company = (TextView) view;
					company.setText(cursor.getString(cursor
							.getColumnIndex("ACCOUNT_NAME")));
					break;
				}
				return true;
			}
		};

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			adapter = new SimpleCursorAdapter(CompanyList.this,
					R.layout.company_item, result, new String[] { "_id",
							"ACCOUNT_NAME" }, new int[] { R.id.company_name});
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
			fiterStr = s.toString();
			if (adapter != null) {
				adapter.getFilter().filter(s);
			}
		}

	};

	private FilterQueryProvider filter = new FilterQueryProvider() {

		@Override
		public Cursor runQuery(final CharSequence constraint) {
			try {
				fiterStr = constraint.toString();
				DeviceUtility.log(TAG, "filter: " + constraint);
				DatabaseUtility.getDatabaseHandler(CompanyList.this);
//				Contact contact = new Contact(Constants.DBHANDLER);
				
				Account master = new Account(Constants.DBHANDLER);
				
				final Cursor cursor = master.getAccountListFilterDisplay(constraint.toString());
				cursor.moveToFirst();
				
				System.out.println("account filter: " + constraint);

				runOnUiThread(new Runnable() {
					public void run() {
						if (constraint.toString().length() == 0) {
							System.out.println("-1 Show");
							mCreate.setVisibility(View.GONE);
						} else if (constraint.toString().length() == 0
								&& cursor.getString(
										cursor.getColumnIndex("ACCOUNT_NAME"))
										.equals(constraint.toString())) {
							mCreate.setVisibility(View.GONE);

//							while (cursor.moveToNext()) {
//								System.out
//										.println("YY = "
//												+ cursor.getString(cursor
//														.getColumnIndex("COMPANY_NAME")));
//							}
//							System.out.println("0 Show");

						} else if (constraint.toString().length() != 0) {

							System.out.println("Show");
							mCreate.setVisibility(View.VISIBLE);
							mCreate.setText(getString(R.string.create,
									constraint.toString()));
							cursor.moveToFirst();
							do {
								if (cursor.getCount() != 0) {
									
									if (cursor
											.getString(
													cursor.getColumnIndex("ACCOUNT_NAME"))
											.equalsIgnoreCase(
													constraint.toString())) {
										mCreate.setVisibility(View.GONE);
										return;
									} else {
										mCreate.setVisibility(View.VISIBLE);
									}
								}
							} while (cursor.moveToNext());

						}
					}
				});

				return cursor;
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

}
