package com.trinerva.icrm.contacts;
//package asia.firstlink.contacts;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TableLayout;
//import android.widget.TextView;
//import asia.firstlink.R;
//import asia.firstlink.object.ContactDetail;
//import asia.firstlink.object.MasterInfo;
//import asia.firstlink.utility.DeviceUtility;
//
//public class CreateContactInfoTab extends FragmentActivity {
//    @Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		  unregisterReceiver(broadcastReceiver); 
//	}
//
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		
//		 IntentFilter filter = new IntentFilter(); 
//	        filter.addAction("page"); 
//	        registerReceiver(this.broadcastReceiver, filter); 
//	        
//	}
//
//	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { 
//        @Override 
//        public void onReceive(Context context, Intent intent) { 
//        	
//        	
//          mViewPager.setCurrentItem(intent.getIntExtra("page", 0));
//        } 
//    }; 
//    
//	private String TAG = "ContactInfo";
//	private ContactDetail contact = new ContactDetail();
//	private HashMap<String, String> hLoad = new HashMap<String, String>();
//	private String strContactUri = null;
//	private ArrayList<MasterInfo> aPrefix;
//	private static final int MY_DATE_DIALOG_ID = 3990;
//	private Spinner prefixSpinner;
//	private TableLayout others_field;
//	private LinearLayout basic_field;
//	private ScrollView scroll_view;
//	private TextView basic, others, save;
//	private Dialog loadingDialog;
//	private ImageView birthday_clear;
//	private View save_divider;
//	private EditText first_name, last_name, company_name, job_title, department, mobile, work_fax, work_phone, other_phone, email, home_phone, birthday, email2, email3, skype_id, assistant_name, assistant_phone, street, city, state, zip, country, description;
//	private String strSector = "BASIC";
//	boolean bView = false;
//	boolean bActive = true;
//	
//    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
//    ViewPager mViewPager;
//	String strActive;
//	String strContactId;
//	
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_info_tab);
//
//        // ViewPager and its adapters use support library
//        // fragments, so use getSupportFragmentManager.
//        mDemoCollectionPagerAdapter =
//                new DemoCollectionPagerAdapter(
//                        getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
//        
//        save = (TextView) findViewById(R.id.save);
//        
//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null) {
//			if (bundle.containsKey("CONTACT_URI")) {
//				strContactUri = bundle.getString("CONTACT_URI");
//				DeviceUtility.log(TAG, "strContactUri: " + strContactUri);
//				hLoad.put("ACTION", "LOAD");
//				hLoad.put("ID", strContactUri);
//			} else if (bundle.containsKey("ID")) {
//				strContactUri = bundle.getString("ID");
//				DeviceUtility.log(TAG, "EDIT ID: " + strContactUri);
//				hLoad.put("ACTION", "EDIT");
//				hLoad.put("ID", strContactUri);
//			}
//			
//			if (bundle.containsKey("VIEW")) {
//				bView = bundle.getBoolean("VIEW");
//			}
//			
//			if (bundle.containsKey("ACTIVE")) {
//				String strActive = bundle.getString("ACTIVE");
//				if (strActive.equals("1")) {
//					bActive = false;
//				}
//			}
//		}
//		
//		if (bView) {
//			if (bActive) {
//				save.setText(getString(R.string.edit));
//				save.setOnClickListener(doChangeToSave);
//			} else {
//				//hide the save button.
//				save_divider.setVisibility(View.GONE);
//				save.setVisibility(View.GONE);
//			}
//		} else {
//			save.setOnClickListener(saveContact);
//		}
//		
//		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//onBackPressed();				
//			}
//		});
//    }
//    
//	private OnClickListener doChangeToSave = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			save.setText(getString(R.string.save));
//			save.setOnClickListener(saveContact);
//		}
//	};
//	
//	private OnClickListener saveContact = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent();
//			intent.setAction("save");
//			sendBroadcast(intent);
//		}
//	};
//    
//    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
//        public DemoCollectionPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//        	 Fragment fragment = null;
//        	switch (i) {
//			case 0:
//				  fragment = new CreateContactInfoBesiaFragment();
//				break;
//			case 1:
//				  fragment = new CreateContactInfoOtherFragment();
//				break;
//			}
//            Bundle args = new Bundle();
//            // Our object is just an integer :-P
////            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
//            args.putString("ID", strContactUri);
//            args.putBoolean("VIEW", bView);
//            args.putString("ACTIVE", strActive);
//            args.putString("CONTACT_ID", strContactId);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "OBJECT " + (position + 1);
//        }
//    }
//}