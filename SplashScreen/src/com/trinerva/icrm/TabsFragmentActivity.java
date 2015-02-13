package com.trinerva.icrm;
//package asia.firstlink;
//
//import java.util.HashMap;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.LocalBroadcastManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TabHost;
//import android.widget.TabHost.TabContentFactory;
//import android.widget.TextView;
//import android.widget.Toast;
//import asia.firstlink.utility.DeviceUtility;
//import asia.firstlink.utility.GuiUtility;
//
//
///**
// * @author mwho
// *
// */
//public class TabsFragmentActivity extends FragmentActivity implements TabHost.OnTabChangeListener {
//
//	private TabHost mTabHost;
//	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
//	private TabInfo mLastTab = null;
//	private int[] iTabTitle = {R.string.home, R.string.nearby_places, R.string.reports, R.string.settings};
//	private int[] iTabLogo = {R.drawable.icon_home, R.drawable.icon_nearby, R.drawable.icon_report, R.drawable.icon_settings};
//	//private RelativeLayout[] rTabtitleLayout;
//	private String TAG = "TabsFragmentActivity";
//	private boolean doubleBackToExitPressedOnce = false;
//
//	private class TabInfo {
//		 private String tag;
//         private Class<?> clss;
//         private Bundle args;
//         private Fragment fragment;
//         TabInfo(String tag, Class<?> clazz, Bundle args) {
//        	 this.tag = tag;
//        	 this.clss = clazz;
//        	 this.args = args;
//         }
//	}
//
//	class TabFactory implements TabContentFactory {
//		private final Context mContext;
//
//	    /**
//	     * @param context
//	     */
//	    public TabFactory(Context context) {
//	        mContext = context;
//	    }
//
//	    /** (non-Javadoc)
//	     * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
//	     */
//	    public View createTabContent(String tag) {
//	        View v = new View(mContext);
//	        v.setMinimumWidth(0);
//	        v.setMinimumHeight(0);
//	        return v;
//	    }
//	}
//	/** (non-Javadoc)
//	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
//	 */
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// Step 1: Inflate layout
//		setContentView(R.layout.tab_layout);
//		// Step 2: Setup TabHost
//		initialiseTabHost(savedInstanceState);
//		if (savedInstanceState != null) {
//            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
//        }
//		
//		/*
//		int iTotalTab = iTabTitle.length;
//		rTabtitleLayout = new RelativeLayout[iTotalTab];
//		for (int iTabCount = 0; iTabCount < iTotalTab; iTabCount++) {
//			rTabtitleLayout[iTabCount] = (RelativeLayout) findViewById(iTabTitle[iTabCount]);
//		}
//		TabHeaderChange("1");
//		*/
//		
//		/*Button announcement = (Button) findViewById(R.id.announcement);
//		Button synchronize = (Button) findViewById(R.id.synchronize);
//		announcement.setOnClickListener(onAnnouncementClick);
//		synchronize.setOnClickListener(onSynchronizeClick);*/
//	}
//	/*
//	private OnClickListener onAnnouncementClick = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Toast.makeText(TabsFragmentActivity.this, "onAnnouncementClick", Toast.LENGTH_LONG).show();
//		}
//	};
//	
//	private OnClickListener onSynchronizeClick = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Toast.makeText(TabsFragmentActivity.this, "onSynchronizeClick", Toast.LENGTH_LONG).show();
//		}
//	};*/
//	
//
//	/** (non-Javadoc)
//     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
//     */
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
//        super.onSaveInstanceState(outState);
//    }
//
//	/**
//	 * Step 2: Setup TabHost
//	 */
//	private void initialiseTabHost(Bundle args) {
//		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
//		Resources res = getResources();
//        mTabHost.setup();
//        TabInfo tabInfo = null;
//        
//        TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("1").setIndicator(getTabView("1")), ( tabInfo = new TabInfo("1", TabHomeFragment.class, args)));
//        this.mapTabInfo.put(tabInfo.tag, tabInfo);
//        TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("2").setIndicator(getTabView("2")), ( tabInfo = new TabInfo("2", TabNearbyFragment.class, args)));
//        this.mapTabInfo.put(tabInfo.tag, tabInfo);
//        TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("3").setIndicator(getTabView("3")), ( tabInfo = new TabInfo("3", TabReportFragment.class, args)));
//        this.mapTabInfo.put(tabInfo.tag, tabInfo);
//        TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("4").setIndicator(getTabView("4")), ( tabInfo = new TabInfo("4", TabSettingFragment.class, args)));
//        this.mapTabInfo.put(tabInfo.tag, tabInfo);
//        // Default to first tab
//        this.onTabChanged("1");
//        //
//        mTabHost.setOnTabChangedListener(this);
//	}
//	
//	private View getTabView(String strTab) {
//		String strTitle = "";
//		int iTotal = iTabTitle.length;
//		int iCurrentTabLogo = 0;
//		for (int iTab = 0; iTab < iTotal; iTab++) {
//			if (strTab.equals(String.valueOf(iTab + 1))) {
//				strTitle = getString(iTabTitle[iTab]);
//				iCurrentTabLogo = iTabLogo[iTab];
//				break;
//			}
//		}
//		
//		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
//		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
//		title.setText(strTitle);
//		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
//		icon.setImageResource(iCurrentTabLogo);
//		return tabIndicator;
//	}
//
//	/**
//	 * @param activity
//	 * @param tabHost
//	 * @param tabSpec
//	 * @param clss
//	 * @param args
//	 */
//	private static void addTab(TabsFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
//		// Attach a Tab view factory to the spec
//		tabSpec.setContent(activity.new TabFactory(activity));
//        String tag = tabSpec.getTag();
//
//        // Check to see if we already have a fragment for this tab, probably
//        // from a previously saved state.  If so, deactivate it, because our
//        // initial state is that a tab isn't shown.
//        tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
//        if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
//            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//            ft.detach(tabInfo.fragment);
//            ft.commit();
//            activity.getSupportFragmentManager().executePendingTransactions();
//        }
//
//        tabHost.addTab(tabSpec);
//	}
//
//	/** (non-Javadoc)
//	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
//	 */
//	public void onTabChanged(String tag) {
//		TabInfo newTab = (TabInfo) this.mapTabInfo.get(tag);
//		if (mLastTab != newTab) {
//			FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//            if (mLastTab != null) {
//                if (mLastTab.fragment != null) {
//                	ft.detach(mLastTab.fragment);
//                }
//            }
//            
//            if (newTab != null) {
//                if (newTab.fragment == null) {
//                    newTab.fragment = Fragment.instantiate(this,
//                            newTab.clss.getName(), newTab.args);
//                    ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
//                } else {
//                    ft.attach(newTab.fragment);
//                }
//            }
//            
//            mLastTab = newTab;
//            ft.commit();
//            this.getSupportFragmentManager().executePendingTransactions();
//		}
//    }
//	/*
//	public void TabHeaderChange(String newTab) {
//		for (int iTabCount = 0; iTabCount < rTabtitleLayout.length; iTabCount++) {
//        	DeviceUtility.log(TAG, "newtab: " + newTab + " vs " + iTabCount);
//        	if (newTab.equals(String.valueOf(iTabCount))) {
//        		DeviceUtility.log(TAG, "They are equal");
//        		rTabtitleLayout[iTabCount].setVisibility(View.GONE);
//        	} else {
//        		DeviceUtility.log(TAG, "They are not equal");
//        		rTabtitleLayout[iTabCount].setVisibility(View.VISIBLE);
//        	}
//        }
//	}*/
//
//	@Override
//	public void onBackPressed() {
//		if (doubleBackToExitPressedOnce) {
//	        super.onBackPressed();
//	        return;
//	    }
//		this.doubleBackToExitPressedOnce = true;
//		GuiUtility.makeToast(TabsFragmentActivity.this, getString(R.string.back_to_exit), Toast.LENGTH_LONG);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		this.doubleBackToExitPressedOnce = false;
//		registerLogoutReceiver();
//	}
//	
//	/*@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(LogoutReceiver);
//	}*/
//
//
//	private void registerLogoutReceiver() {
//		DeviceUtility.log(TAG, "registerLogoutReceiver");
//		IntentFilter oLogoutFilter = new IntentFilter("LOGOUT");
//		LocalBroadcastManager.getInstance(TabsFragmentActivity.this).registerReceiver(LogoutReceiver, oLogoutFilter);
//	}
//
//	BroadcastReceiver LogoutReceiver = new BroadcastReceiver() {
//		public void onReceive(Context context, Intent intent) {
//			DeviceUtility.log(TAG, "get the logout intent");
//			intent = new Intent(TabsFragmentActivity.this, Agreement.class);
//			TabsFragmentActivity.this.startActivity(intent);
//			TabsFragmentActivity.this.finish();
//		}
//	};
//}
//
