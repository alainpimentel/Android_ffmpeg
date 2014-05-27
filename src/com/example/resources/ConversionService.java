package com.example.resources;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ConversionService extends IntentService {
	public static final String RESULT = "result";
	  public static final String NOTIFICATION = "com.example.resources";
    
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
		publishResults("","done");
	}
	
	private void publishResults(String outputPath, String result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(RESULT, result);
		sendBroadcast(intent);
	}
}
