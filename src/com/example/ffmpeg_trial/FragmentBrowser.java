package com.example.ffmpeg_trial;

import java.io.File;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resources.ConversionService;
import com.example.resources.MediaHelper;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class FragmentBrowser extends Fragment implements OnClickListener {

	private static final int REQUEST_CHOOSER = 1234;
	Button browse_button, button_service, button_reset;
	TextView input_res, output_res, fps_res,
			width_res, height_res, process_signal;
	String in_path = "", 
		   out_path = "",
		   off_color = "#BFBFBF",
		   on_color = "#3FD93F",
		   error_color = "#F7513E",
		   ret_mssg = "",
		   proccessing = "Processing Video";
	
	// Gets results back from Intentservice
	private BroadcastReceiver receiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	      Bundle bundle = intent.getExtras();
	      if (bundle != null) {
	    	  // Return message
	    	  String mssg = bundle.getString(ConversionService.RESULT);
	    	  
	    	  // ERRORS
	    	  String error1 = "Compilation error. FFmpeg failed",
	    			 error2 = "result: FAIL",
	    	  		 error3 = "error when parsing input name";
				if (mssg.equals(error1) || mssg.equals(error2) || mssg.equals(error3)) {
					// change color of signal to off
					process_signal.setBackgroundColor(Color.parseColor(error_color));
					process_signal.setText(mssg + "\nSorry, check your choices and try again.");
				} else {
					// It was a success
					Toast.makeText(getActivity(), mssg, Toast.LENGTH_LONG)
							.show();
					// change color of signal to off
					process_signal.setBackgroundColor(Color.parseColor(off_color));
					process_signal.setText(mssg + "\n Choose a new video!");
					// set inputs to blank
					input_res.setText("");
					output_res.setText("");
					fps_res.setText("");
					width_res.setText("");
					height_res.setText("");
					in_path = "";
				    out_path = "";
				}
				// Send Notification
				sendNotification(mssg);
	      }
	    }
	  };

	public FragmentBrowser() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_layout_browser, container, false);

		browse_button = (Button) view.findViewById(R.id.button_select_in);
		browse_button.setOnClickListener(this);
		input_res = (TextView) view.findViewById(R.id.input_res);
		output_res = (TextView) view.findViewById(R.id.output_res);
		fps_res = (TextView) view.findViewById(R.id.fps_res);
		width_res = (TextView) view.findViewById(R.id.width_res);
		height_res = (TextView) view.findViewById(R.id.height_res);
		process_signal = (TextView) view.findViewById(R.id.process_signal);
		process_signal.setBackgroundColor(Color.parseColor(off_color));
		process_signal.setText("Choose a video to convert");
		button_service = (Button) view.findViewById(R.id.button_service);
		button_service.setOnClickListener(this);
		button_reset = (Button) view.findViewById(R.id.button_reset);
		button_reset.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter(
				ConversionService.NOTIFICATION));
		SharedPreferences sharedpreferences = getActivity().getSharedPreferences("com.example.ffmpeg_trial", 
				Context.MODE_PRIVATE);
		if (sharedpreferences.contains("mssg"))
	      {
			ret_mssg = sharedpreferences.getString("mssg", "");
			updateViews(ret_mssg);
	      }
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.button_select_in:
				if(MediaHelper.isExternalStorageWritable()) {
					Toast.makeText(getActivity(), "Clicked on button", Toast.LENGTH_LONG).show();
					// Create the ACTION_GET_CONTENT Intent
					Intent getContentIntent = FileUtils.createGetContentIntent();

					Intent intent = Intent.createChooser(getContentIntent, "Select a file");
					startActivityForResult(intent, REQUEST_CHOOSER);
					}
				else {
					Toast.makeText(getActivity(), "External Storage not accessible", Toast.LENGTH_LONG).show();
				}
           break;
           case R.id.button_service:
				if (in_path.equals("") || out_path.equals(""))
					Toast.makeText(getActivity(), "Please choose input file", Toast.LENGTH_LONG).show();
				else {
					String fps = fps_res.getText().toString();
					String width = width_res.getText().toString();
					String height = height_res.getText().toString();
					try {
	
						Toast.makeText(getActivity(), "Conversion has started",
								Toast.LENGTH_LONG).show();
						Intent mServiceIntent = new Intent(getActivity(),
								ConversionService.class);
						Bundle extras = new Bundle();
						extras.putString("WIDTH", width);
						extras.putString("HEIGHT", height);
						extras.putString("IN_PATH", in_path);
						extras.putString("OUT_PATH", out_path);
						extras.putString("FPS", fps);
						mServiceIntent.putExtras(extras);
						// Starts the IntentService
						getActivity().startService(mServiceIntent);
						
						// Send Notification
						sendNotification("Conversion is in progress");
						
						// Starts the IntentService
						getActivity().startService(mServiceIntent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// change color of signal to on
					process_signal.setBackgroundColor(Color.parseColor(on_color));
					process_signal.setText(proccessing);
				}
			break;
            case R.id.button_reset:
				// set inputs to blank
				input_res.setText("");
				output_res.setText("");
				fps_res.setText("");
				width_res.setText("");
				height_res.setText("");
				in_path = "";
				out_path = "";
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	        case REQUEST_CHOOSER:   
	            if (resultCode == getActivity().RESULT_OK) {

	                final Uri uri = data.getData();
	                
	                // Get the File path from the Uri
	                in_path = FileUtils.getPath(getActivity(), uri);
	                Toast.makeText(getActivity(), in_path, Toast.LENGTH_LONG).show();
	                
	                input_res.setText(in_path);
	                
	                /* CHECK FILE IS A VIDEO */
	                
	                // Get storage directory
	                String str_dir_path = "/ffmpeg/";
	                File str_dir = MediaHelper.getVideosStorageDir(str_dir_path);
	                out_path = str_dir.getAbsolutePath();
	                output_res.setText(out_path);
	                
	            }
	            break;
	    }
	}
	
	// Update views when returning to fragment, using the last message stored in SharedPreferenes
	private void updateViews(String mssg) {
		// ERRORS
		String error1 = "Compilation error. FFmpeg failed", error2 = "result: FAIL", error3 = "error when parsing input name";
		if (mssg.equals(error1) || mssg.equals(error2) || mssg.equals(error3)) {
			// change color of signal to off
			process_signal.setBackgroundColor(Color.parseColor(error_color));
			process_signal.setText(mssg
					+ "\nSorry, check your choices and try again.");
		} else {
			// change color of signal to off
			process_signal.setBackgroundColor(Color.parseColor(off_color));
			process_signal.setText(mssg + "\n Choose a new video!");
			
		}
	}
	
	private void sendNotification(String mssg) {
		// generate notification: http://developer.android.com/training/notify-user/build-notification.html#click
		final int MY_NOTIFICATION_ID = 1; 
		String notificationText = mssg;
		NotificationCompat.Builder myNotification = new NotificationCompat.Builder(
				getActivity()).setContentTitle("Progress")
				.setContentText(notificationText)
				.setTicker("Notification!")
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_SOUND)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher);
		Intent resultIntent = new Intent(getActivity(), MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
    		getActivity(),
		    0,
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		myNotification.setContentIntent(resultPendingIntent);
		
		NotificationManager notificationManager = 
		        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification.build());
	}

}
