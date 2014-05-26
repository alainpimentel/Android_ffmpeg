package com.example.resources;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class MediaHelper {
	
	public interface Constants {
		  String LOG = "com.vogella.android.first";
		} 
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static File getVideosStorageDir(String folderName) {
	    // Get the directory for the user's public pictures directory.
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_MOVIES), folderName);
	    if (!file.mkdirs()) {
	        Log.e(Constants.LOG, "Directory not created");
	    }
	    return file;
	}
}
