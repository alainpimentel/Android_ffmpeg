package com.example.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class videoHandler {
	
	public void video(Context context, ArrayList<Clip> videos) throws FileNotFoundException, IOException {
		Activity activity = (Activity) context;
		File fileTmp = activity.getCacheDir(); 
		File fileAppRoot = new File(activity.getApplicationInfo().dataDir);
	
	    FfmpegController fc = new FfmpegController(fileTmp, fileAppRoot);
	
	
	    final Clip out = new Clip("compiled.mp4");
	    try {
			fc.concatAndTrimFilesMP4Stream(videos, out, true, false,  new ShellUtils.ShellCallback() {

			    @Override
			    public void shellOut(String shellLine) {
			        System.out.println("MIX> " + shellLine);
			    }

			    @Override
			    public void processComplete(int exitValue) {

			        if (exitValue != 0) {
			            System.err.println("concat non-zero exit: " + exitValue);
			            Log.d("ffmpeg","Compilation error. FFmpeg failed");
			        } else {
			            if(new File(out.path).exists()) {
			                Log.d("ffmpeg","Success file:"+out.path);
			            }
			        }
			    }
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
