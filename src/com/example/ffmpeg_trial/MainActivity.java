package com.example.ffmpeg_trial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.resources.videoHandler;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoHandler vh = new videoHandler();
        ArrayList<Clip> in = new ArrayList<Clip>();
        final Clip clip_in = new Clip("/storage/emulated/0/Developer/test.mp4"); // input source
        clip_in.isVideo(); 
        in.add(clip_in);
        boolean video = new File( "/storage/emulated/0/Developer/result.mp4").exists();
        Toast.makeText(MainActivity.this, "result: " + video, Toast.LENGTH_LONG).show();
        
        Activity activity = (Activity) MainActivity.this;
		File fileTmp = activity.getCacheDir(); 
		File fileAppRoot = new File(activity.getApplicationInfo().dataDir);
		//String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
		//Toast.makeText(MainActivity.this, "result: " + dir, Toast.LENGTH_LONG).show();
		final Clip clip_out = new Clip("/storage/emulated/0/Developer/result2.mp4");
		//put flags in clip
		clip_out.videoFps = "30";
		clip_out.width = 480;
		clip_out.height = 320;
		clip_out.videoCodec = "libx264";
		clip_out.audioCodec = "copy";
		
	    try {
			FfmpegController fc = new FfmpegController(fileTmp, fileAppRoot);
			// works! //fc.convertToMPEG(clip_in, "/storage/emulated/0/Developer/result.mp4", 
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
			            Toast.makeText(MainActivity.this, "result: ffmpeg failed", Toast.LENGTH_LONG).show();
			        } else {
			            if(new File( "/storage/emulated/0/Developer/result2.mp4").exists()) {
			                Log.d("ffmpeg","Success file:"+ "/storage/emulated/0/Developer/result2.mp4");
			                Toast.makeText(MainActivity.this, "result: WIN", Toast.LENGTH_LONG).show();
			            }
			        }
			    }
			});
			//fc.getInfo(clip_in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   
        
//        try {
//			vh.video(MainActivity.this, in);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        setContentView(R.layout.activity_main);
    }
}
