package com.trinerva.icrm;

import com.trinerva.icrm.utility.Constants;
import com.trinerva.icrm.utility.DeviceUtility;
import com.trinerva.icrm.utility.Utility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import asia.firstlink.icrm.R;

public class SplashScreen extends Activity {
	private String TAG = "SplashScreen";
	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms
	public static enum SCREEN {DEFAULT, ACTIVATE, NORMAL, PASSCODE};
	protected SCREEN screen = SCREEN.DEFAULT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		// thread for displaying the SplashScreen
//	    new Thread(splashTread).start();
		
       	try {
    		DeviceUtility.log(TAG, "START THREAD");
            int waited = 0;
            	DeviceUtility.log(TAG, "waited: " + waited);
            	//check which screen to.
            	//passcode? activate? or directly show the screen.
            	if (screen.equals(SCREEN.DEFAULT)) {
                	String strEmail = Utility.getConfigByText(SplashScreen.this, Constants.USER_EMAIL);
                	String strPin = Utility.getConfigByText(SplashScreen.this, Constants.PHONE_PIN_ENABLED);
                	if (strEmail == null || strEmail.length() == 0) {
                		//activate screen.
                		screen = SCREEN.ACTIVATE;
                	} else if (strPin.equalsIgnoreCase("TRUE")) {
                		screen = SCREEN.PASSCODE;
                	} else {
                		screen = SCREEN.NORMAL;
                	}
            	}

        } finally {
            finish();
            Intent intent;
            if (screen.equals(SCREEN.ACTIVATE)) {
            	DeviceUtility.log(TAG, "ACTIVATE");
            	intent = new Intent(SplashScreen.this, Agreement.class);
            	SplashScreen.this.startActivity(intent);
            } else if (screen.equals(SCREEN.PASSCODE)) {
            	DeviceUtility.log(TAG, "PASSCODE");
            	intent = new Intent(SplashScreen.this, Lock.class);
            	SplashScreen.this.startActivity(intent);
            } else {
            	DeviceUtility.log(TAG, "NORMAL");
            	intent = new Intent(SplashScreen.this, HomeMenuActivity.class);
            	SplashScreen.this.startActivity(intent);
            }
        }
       	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        _active = false;
	    }
	    return true;
	}

	Handler handler = new Handler();

	Runnable splashTread = new Runnable() {

        @Override
        public void run() {
        	try {
        		DeviceUtility.log(TAG, "START THREAD");
	            int waited = 0;
	            while(_active && (waited < _splashTime)) {
	            	DeviceUtility.log(TAG, "waited: " + waited);
	            	//check which screen to.
	            	//passcode? activate? or directly show the screen.
	            	if (screen.equals(SCREEN.DEFAULT)) {
	                	String strEmail = Utility.getConfigByText(SplashScreen.this, Constants.USER_EMAIL);
	                	String strPin = Utility.getConfigByText(SplashScreen.this, Constants.PHONE_PIN_ENABLED);
	                	if (strEmail == null || strEmail.length() == 0) {
	                		//activate screen.
	                		screen = SCREEN.ACTIVATE;
	                	} else if (strPin.equalsIgnoreCase("TRUE")) {
	                		screen = SCREEN.PASSCODE;
	                	} else {
	                		screen = SCREEN.NORMAL;
	                	}
	            	}

	                Thread.sleep(100);
	                if(_active) {
	                    waited += 100;
	                }
	            }
	        } catch(InterruptedException e) {
	            // do nothing
	        } finally {
	            finish();
	            Intent intent;
	            if (screen.equals(SCREEN.ACTIVATE)) {
	            	DeviceUtility.log(TAG, "ACTIVATE");
	            	intent = new Intent(SplashScreen.this, Agreement.class);
	            	SplashScreen.this.startActivity(intent);
	            } else if (screen.equals(SCREEN.PASSCODE)) {
	            	DeviceUtility.log(TAG, "PASSCODE");
	            	intent = new Intent(SplashScreen.this, Lock.class);
	            	SplashScreen.this.startActivity(intent);
	            } else {
	            	DeviceUtility.log(TAG, "NORMAL");
	            	intent = new Intent(SplashScreen.this, HomeMenuActivity.class);
	            	SplashScreen.this.startActivity(intent);
	            }
	        }
        }
    };
}
