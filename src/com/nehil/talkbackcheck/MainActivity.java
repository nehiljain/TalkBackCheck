package com.nehil.talkbackcheck;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.accessibility.AccessibilityManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import java.util.List;


public class MainActivity extends Activity {
	private final static String SCREENREADER_INTENT_ACTION = "android.accessibilityservice.AccessibilityService";
	private final static String SCREENREADER_INTENT_CATEGORY = "android.accessibilityservice.category.FEEDBACK_SPOKEN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        boolean isAccessibilityEnabled = am.isEnabled();
        boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
        if(isAccessibilityEnabled == true)
        {
        	Log.d("Nehil", "Accessibilty is Turned On");
        	if(isExploreByTouchEnabled == true)
        	{
        		Log.d("Nehil", "Touch By Exploration is Turned ON");
        	}
        }
        else
        {
        	Log.d("Nehil", "Accessibilty Disabled");
        }
        
        //this.isScreenReaderActive();
    }

   
    
    /**
     * This code was derived from eyes free group implementation
     * The above method is Easier to implement 
     * This cursor returned in the below code is empty
     * 
     * 
     * @return
     */
    private boolean isScreenReaderActive() {
        // Restrict the set of intents to only accessibility services that have
        // the category FEEDBACK_SPOKEN (aka, screen readers).
        Intent screenReaderIntent = new Intent(SCREENREADER_INTENT_ACTION);
        screenReaderIntent.addCategory(SCREENREADER_INTENT_CATEGORY);
        List<ResolveInfo> screenReaders = getPackageManager().queryIntentServices(
                screenReaderIntent, 0);
        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        int status = 0;
        for (ResolveInfo screenReader : screenReaders) {
            // All screen readers are expected to implement a content provider
            // that responds to
            // content://<nameofpackage>.providers.StatusProvider
            cursor = cr.query(Uri.parse("content://" + screenReader.serviceInfo.packageName
                    + ".providers.StatusProvider"), null, null, null, null);
            if (cursor != null) {
            	 Log.d("Nehil", "Cursor Null");
                cursor.moveToFirst();
                // These content providers use a special cursor that only has one element,
                // an integer that is 1 if the screen reader is running.
                status = cursor.getInt(0);
                cursor.close();
                if (status == 1) {
                    Log.d("Nehil", "ScreenReader is running");
                	return true;
                }
            }
        }
        return false;
    }

}
