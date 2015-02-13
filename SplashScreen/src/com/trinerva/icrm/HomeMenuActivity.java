package com.trinerva.icrm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.trinerva.icrm.calendar.CalendarActivity;
import com.trinerva.icrm.contacts.ContactList;
import com.trinerva.icrm.event.EventList;
import com.trinerva.icrm.leads.LeadList;
import com.trinerva.icrm.model.GetData;
import com.trinerva.icrm.model.SaveData;
import com.trinerva.icrm.tasks.TaskList;
import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import asia.firstlink.icrm.R;

public class HomeMenuActivity extends Activity {
	SaveData saveData;
	Context context;
	List<MyData> myDataList = new ArrayList<MyData>();
	MyData myData;
	LinearLayout mSync, mSetting;

	class MyData {
		int res;
		String name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_home_menu);

		saveData = new SaveData(this);

		context = this;

		GridView gridview = (GridView) findViewById(R.id.gridview);
		mSync = (LinearLayout) findViewById(R.id.syn);
		mSetting = (LinearLayout) findViewById(R.id.setting);

		myData = new MyData();
		myData.name = getString(R.string.contacts);
		myData.res = R.drawable.ic_icon_contact;
		myDataList.add(myData);

		myData = new MyData();
		myData.name = getString(R.string.leads);
		myData.res = R.drawable.ic_icon_lead;
		myDataList.add(myData);

		myData = new MyData();
		myData.name = getString(R.string.events);
		myData.res = R.drawable.ic_icon_event;
		myDataList.add(myData);

		myData = new MyData();
		myData.name = getString(R.string.tasks);
		myData.res = R.drawable.ic_icon_task;
		myDataList.add(myData);

		myData = new MyData();
		myData.name = getString(R.string.calendar);
		myData.res = R.drawable.ic_icon_calendar;
		myDataList.add(myData);
		//
		// myData = new MyData();
		// myData.name = getString(R.string.help);
		// myData.res = R.drawable.ic_icon_help;
		// myDataList.add(myData);

		gridview.setAdapter(new StoresAdapter((Activity) context, myDataList));

		// gridview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// Intent contact;
		// switch (position) {
		// case 0:
		// contact = new Intent(HomeMenuActivity.this,
		// ContactList.class);
		// startActivity(contact);
		// break;
		// case 1:
		// contact = new Intent(HomeMenuActivity.this, LeadList.class);
		// startActivity(contact);
		// break;
		// case 2:
		// contact = new Intent(HomeMenuActivity.this, EventList.class);
		// startActivity(contact);
		// break;
		// case 3:
		// contact = new Intent(HomeMenuActivity.this, TaskList.class);
		// startActivity(contact);
		// break;
		// case 4:
		// contact = new Intent(HomeMenuActivity.this,
		// CalendarActivity.class);
		// startActivity(contact);
		// break;
		// default:
		// break;
		// }
		//
		// }
		// });

		System.out.println("saveData.getBooleanData(GetData.checkLogin) = "
				+ saveData.getBooleanData(GetData.checkLogin));
		if (saveData.getBooleanData(GetData.checkLogin) == false) {
			Intent contact = new Intent(HomeMenuActivity.this, CrmLogin.class);
			startActivity(contact);
		}
		//
		// findViewById(R.id.contact).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent contact = new Intent(HomeMenuActivity.this,
		// ContactList.class);
		// startActivity(contact);
		// }
		// });
		//
		// findViewById(R.id.lead).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent contact = new Intent(HomeMenuActivity.this,
		// LeadList.class);
		// startActivity(contact);
		// }
		// });
		//
		// findViewById(R.id.calendar).setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent contact = new Intent(HomeMenuActivity.this,
		// EventList.class);
		// startActivity(contact);
		// }
		// });
		//
		// findViewById(R.id.task).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent contact = new Intent(HomeMenuActivity.this,
		// TaskList.class);
		// startActivity(contact);
		// }
		// });
		//
		// findViewById(R.id.menuCalendar).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent contact = new Intent(HomeMenuActivity.this,
		// CalendarActivity.class);
		// startActivity(contact);
		// }
		// });

		mSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSetting.setClickable(false);
				Intent contact = new Intent(HomeMenuActivity.this,
						SettingActivity.class);
				startActivity(contact);
			}
		});

		findViewById(R.id.nearby).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent contact = new Intent(HomeMenuActivity.this,
						NearbyActivity.class);
				startActivity(contact);
			}
		});

		findViewById(R.id.help).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ReadPdfFromAssets("help.pdf");
//				CopyReadAssets();
//					Uri path=Uri.parse(getAssets()+"/help.pdf");
//				Intent intent = new Intent(HomeMenuActivity.this, PdfViewerActivity.class);
//			     intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, path.getPath());
//			     startActivity(intent);
//					System.out.println("pad = "+path.getPath());
				Intent intent = new Intent(HomeMenuActivity.this, HelpActivity.class);
				startActivity(intent);
			}
		});

		mSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSync.setClickable(false);
				System.out.println("xxxx");
				if (saveData.getBooleanData(GetData.checkLogin)) {
					// Intent contact = new Intent(HomeMenuActivity.this,
					// CrmLogin.class);
					// startActivity(contact);
					Intent sync = new Intent(HomeMenuActivity.this,
							SynchronizationData.class);
					Bundle bFlag = new Bundle();
					bFlag.putString("ACTION", "NORMAL");
					bFlag.putString("EMAIL",
							saveData.getStringData(GetData.getUserEmail));
					bFlag.putString("PASSWORD",
							saveData.getStringData(GetData.getUserPassword));
					bFlag.putString("FROM", "CRMLOGIN");
					sync.putExtras(bFlag);
					HomeMenuActivity.this.startActivity(sync);
				} else {
					Intent contact = new Intent(HomeMenuActivity.this,
							CrmLogin.class);
					startActivity(contact);
				}
			}
		});

		findViewById(R.id.announcement).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent contact = new Intent(HomeMenuActivity.this,
								Announcement.class);
						startActivity(contact);
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSync.setClickable(true);
		mSetting.setClickable(true);
		IntentFilter filter = new IntentFilter();
		filter.addAction("homeMenuFinish");
		filter.addAction("LOGOUT");
		registerReceiver(this.broadcastReceiver, filter);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("homeMenuFinish")) {
				System.out.println("OOOO");
				finish();
			} else if (intent.getAction().equals("LOGOUT")) {
				intent = new Intent(HomeMenuActivity.this, Agreement.class);
				HomeMenuActivity.this.startActivity(intent);
				HomeMenuActivity.this.finish();
			}
		}
	};

	public class StoresAdapter extends ArrayAdapter<MyData> {

		private LayoutInflater mInflater;

		public StoresAdapter(Activity context, List<MyData> uris) {
			super(context, 0, uris);

			mInflater = context.getLayoutInflater();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_image, null);
				holder.mIcon = (ImageView) convertView
						.findViewById(R.id.iconImage);
				holder.mTitle = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final MyData uri = getItem(position);
			holder.mIcon.setImageResource(uri.res);
			holder.mTitle.setText(uri.name);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent contact;
					switch (position) {
					case 0:
						contact = new Intent(HomeMenuActivity.this,
								ContactList.class);
						startActivity(contact);
						break;
					case 1:
						contact = new Intent(HomeMenuActivity.this,
								LeadList.class);
						startActivity(contact);
						break;
					case 2:
						contact = new Intent(HomeMenuActivity.this,
								EventList.class);
						startActivity(contact);
						break;
					case 3:
						contact = new Intent(HomeMenuActivity.this,
								TaskList.class);
						startActivity(contact);
						break;
					case 4:
						contact = new Intent(HomeMenuActivity.this,
								CalendarActivity.class);
						startActivity(contact);
						break;
					case 5:

						break;
					default:
						break;
					}
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView mTitle;
			ImageView mIcon;
		}
	}

//	 private void ReadPdfFromAssets(String filename)  // "filename = "abc.pdf""
//     {
//     /*    AssetManager assetManager = getAssets();
//
//         InputStream in = null;
//         OutputStream out = null;
//         File file = new File(getFilesDir(), filename)  );
//      */ 
//			Uri path=Uri.parse("file:///android:asset/help/"+filename);
//		 AssetManager am = getAssets();
//		 InputStream inputStream = null;
//		try {
//			inputStream = am.open(path.getPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 File file = createFileFromInputStream(inputStream);
//
//	
//
//		
//
////		 Uri path = Uri.parse("android.resource://"+ getPackageName() +"/" + R.raw.help);
////		 Uri path = Uri.parse("android.resource://" + getPackageName() + "/" R.raw.help);
//		
//	 final Intent intent = new Intent(HomeMenuActivity.this, ViewPdf.class);
//	 System.out.println("path = "+file.getPath());
//     intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,  file.getPath());
//     startActivity(intent);
//     
//     }
	  private void CopyReadAssets()
      {
          AssetManager assetManager = getAssets();

          InputStream in = null;
          OutputStream out = null;
          File file = new File(getFilesDir(), "help.pdf");
          try
          {
              in = assetManager.open("help/help.pdf");
              out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
          } catch (Exception e)
          {
          }

//          Intent intent = new Intent(Intent.ACTION_VIEW);
//          intent.setDataAndType(
//                  Uri.parse("file://" + getFilesDir() + "/abc.pdf"),
//                  "application/pdf");
//
//          startActivity(intent);
          
//     	 final Intent intent = new Intent(HomeMenuActivity.this, ViewPdf.class);
//     	 System.out.println("path = "+file.getPath());
//          intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,   Uri.parse("file://" + getFilesDir() + "/help.pdf").getPath());
//          startActivity(intent);
          
          
//          String googleDocsUrl = "http://docs.google.com/viewer?url=";
//System.out.println("path = "+googleDocsUrl +  Uri.parse("file://" + getFilesDir() + "/help.pdf").getPath());
//          Intent intent = new Intent(Intent.ACTION_VIEW);
//          intent.setDataAndType(Uri.parse(googleDocsUrl +  Uri.parse("file://" + getFilesDir() + "/help.pdf").getPath()), "text/html");
//          startActivity(intent);
          
Intent intent = new Intent();
intent.setClass(HomeMenuActivity.this, MyWebView.class);
startActivity(intent);
      }

      private void copyFile(InputStream in, OutputStream out) throws IOException
      {
          byte[] buffer = new byte[1024];
          int read;
          while ((read = in.read(buffer)) != -1)
          {
              out.write(buffer, 0, read);
          }
      }
}
