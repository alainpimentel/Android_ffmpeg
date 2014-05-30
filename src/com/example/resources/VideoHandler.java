package com.example.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class VideoHandler {
	
	private static String mssg = "result: FAIL";
	private static String final_out_path = "";
	
	public static String videoConverter(final Context cntx, File fileTmp, File fileAppRoot, final String in_path, final String out_path,
					String fps, String width, String height) {
		
//		Activity activity = (Activity) context;
//		File fileTmp = activity.getCacheDir();
//		File fileAppRoot = new File(activity.getApplicationInfo().dataDir);
		
		final Clip clip_in = new Clip(in_path);
		
		try {
			// get the input name without extension and datetime to make it unique
			final_out_path = out_path + "/" + splitString(in_path) + getDateTime() + ".mp4";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "error when parsing input name";
		}
		
		final Clip clip_out = new Clip(final_out_path);
		// put flags in clip_out
		clip_out.videoFps = fps;
		int width_int = stringToInt(width);
		if(width_int != -1)
			clip_out.width = width_int;
		int height_int = stringToInt(height);
		if(height_int != -1)
			clip_out.height = height_int;
		clip_out.videoFilter = "transpose=2,transpose=2"; // flip video 180, otherwise is fliped
		clip_out.videoCodec = "libx264";
		clip_out.audioCodec = "copy";
		
		// Conversion
		FfmpegController fc;
		try {
			fc = new FfmpegController(fileTmp, fileAppRoot);
			fc.processVideo(clip_in, clip_out, false, new ShellUtils.ShellCallback() {
				
				
				
				@Override
				public void shellOut(String shellLine) {
					System.out.println("MIX> " + shellLine);
					}
				
				@Override
				public void processComplete(int exitValue) {
				
					if (exitValue != 0) {
							System.err.println("concat non-zero exit: " + exitValue);
							mssg = "Compilation error. FFmpeg failed";
							Log.d("ffmpeg",mssg);
							//Toast.makeText(context, "result: ffmpeg failed", Toast.LENGTH_LONG).show();
					} else {
						if(new File(final_out_path).exists()) {
							mssg = "Success! New File: " + final_out_path;
							Log.d("ffmpeg", mssg);
							//Toast.makeText(context, "result: WIN", Toast.LENGTH_LONG).show();
						}
					}
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Toast.makeText(context, "result: FAIL", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Toast.makeText(context, "result: FAIL", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Toast.makeText(context, "result: FAIL", Toast.LENGTH_LONG).show();
		}
		MyNotification.sendNotification(cntx, mssg);
		return mssg;
	}
	
	private static int stringToInt(String num) {
		int temp_num = 0;
		
		try {
			temp_num = Integer.parseInt(num);
			return temp_num;
		} catch(NumberFormatException nfe) {
			System.out.println("Could not parse " + nfe);
			return -1;
		}
	}
	
	private static String splitString(String str) {
		// take out the file extension and reverse it
		String file_path = new StringBuffer(str.split("\\.")[0]).reverse().toString();
		// split at first "/" and reverse it
		return new StringBuffer(file_path.split("/")[0]).reverse().toString();
	}
	
	@SuppressLint("SimpleDateFormat")
	private static String getDateTime() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("_yyMMdd_HHmmss");
        return df.format(c.getTime());
	}
	
}
