package com.example.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class VideoHandler {
	
	public static void videoConverter(final Context context, final String in_path, final String out_path,
					String fps, String width, String height) {
		int x =1;
		x=2;
		x=3;
		Activity activity = (Activity) context;
		File fileTmp = activity.getCacheDir();
		File fileAppRoot = new File(activity.getApplicationInfo().dataDir);
		
		final Clip clip_in = new Clip(in_path);
		final Clip clip_out = new Clip(out_path + "/TEST.mp4");
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
							Log.d("ffmpeg","Compilation error. FFmpeg failed");
							Toast.makeText(context, "result: ffmpeg failed", Toast.LENGTH_LONG).show();
					} else {
						String path_final = out_path + "/TEST.mp4";
						if(new File(path_final).exists()) {
							Log.d("ffmpeg","Success file:"+ "/storage/emulated/0/Developer/result2.mp4");
							Toast.makeText(context, "result: WIN", Toast.LENGTH_LONG).show();
						}
					}
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "result: FAIL", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "result: FAIL", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "result: FAIL", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	public static int stringToInt(String num) {
		int temp_num = 0;
		
		try {
			temp_num = Integer.parseInt(num);
			return temp_num;
		} catch(NumberFormatException nfe) {
			System.out.println("Could not parse " + nfe);
			return -1;
		}
	}
	
}
