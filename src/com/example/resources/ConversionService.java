package com.example.resources;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ConversionService extends IntentService {
	// Defines a custom Intent action
    public static final String BROADCAST_ACTION =
        "com.example.android.threadsample.BROADCAST";
    
 // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS =
        "com.example.android.threadsample.STATUS";
    
	public ConversionService() {
		super("ConversionService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Intent Service", "service created");
	}

	// will be called asynchronously by Android
	@Override
	protected void onHandleIntent(Intent intent) {
		// final String in_path
		// final String out_path
		// String fps
		// String width
		// String height
		// File fileTmp = getCacheDir();
		// File fileAppRoot = new File(getApplicationInfo().dataDir);
		// Gets data from the incoming Intent
		Log.d("Intent Service", "service started");
		synchronized (intent) {

		}
	}
	

}
