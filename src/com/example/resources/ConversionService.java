package com.example.resources;

import java.io.File;

import android.app.IntentService;
import android.content.Intent;

public class ConversionService extends IntentService {
	
	public static final String SOME = "result";
	public static final String NOTIFICATION = "com.example.resources";
	
	public ConversionService() {
		super("ConversionService");
	}

	// will be called asynchronously by Android
	@Override
	protected void onHandleIntent(Intent intent) {
//		final String in_path
//		final String out_path
//		String fps
//		String width
//		String height
		File fileTmp = getCacheDir();
		File fileAppRoot = new File(getApplicationInfo().dataDir);
		publishResults("somepath", 200);
	}
	
	private void publishResults(String outputPath, int result) {
	    Intent intent = new Intent(NOTIFICATION);
	    intent.putExtra(SOME, result);
	    sendBroadcast(intent);
	  }
}
