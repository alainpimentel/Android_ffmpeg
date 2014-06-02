package com.example.ffmpeg_trial;

import java.io.File;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resources.ConversionService;
import com.example.resources.MediaHelper;
import com.example.resources.MyNotification;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class FragmentBrowser extends Fragment implements OnClickListener {

	private static final int REQUEST_CHOOSER = 1234;
	Button browse_button;
	TextView input_res, output_res, fps_res,
			width_res, height_res, process_signal;
	TableRow rowIn, rowOut;
	String in_path = "", 
		   out_path = "",
		   off_color = "#33B5E5", // Blue
		   on_color = "#99CC00",  // Green
		   error_color = "#FF4444", // Red
		   ret_mssg = "",
		   proccessing = "Processing Video";
	SharedPreferences sharedpreferences;
	
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
					process_signal.setText(mssg + "\nCheck your choices and try again.");
				} else {
					// change color of signal to off
					process_signal.setBackgroundColor(Color.parseColor(off_color));
					process_signal.setText(mssg + "\n Choose a new video!");
					
					resetInputs();
					// Hide Outputs
					rowIn.setVisibility(View.GONE);
					rowOut.setVisibility(View.GONE);
				}
				// Send Notification. ALREADY sent in service
				//MyNotification.sendNotification(getActivity(), mssg);
	      }
	    }
	  };

	public FragmentBrowser() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_layout_browser, container, false);
		
		//buttons
		browse_button = (Button) view.findViewById(R.id.button_select_in);
		browse_button.setOnClickListener(this);
		//textviews
		input_res = (TextView) view.findViewById(R.id.input_res);
		output_res = (TextView) view.findViewById(R.id.output_res);
		fps_res = (TextView) view.findViewById(R.id.fps_res);
		width_res = (TextView) view.findViewById(R.id.width_res);
		height_res = (TextView) view.findViewById(R.id.height_res);
		process_signal = (TextView) view.findViewById(R.id.process_signal);
		process_signal.setBackgroundColor(Color.parseColor(off_color));
		process_signal.setText("Choose a video to convert");
		//rows
		rowIn = (TableRow) view.findViewById(R.id.tableRow2);
		rowOut = (TableRow) view.findViewById(R.id.tableRow3);
		// Hide in and out initially
		rowIn.setVisibility(View.GONE);
		rowOut.setVisibility(View.GONE);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter(
				ConversionService.NOTIFICATION));
		if (isMyServiceRunning()) {
			updateViews(proccessing);
		} else {
			sharedpreferences = getActivity().getSharedPreferences(
					"com.example.ffmpeg_trial", Context.MODE_PRIVATE);
			if (sharedpreferences.contains("mssg")) {
				ret_mssg = sharedpreferences.getString("mssg", "");
				updateViews(ret_mssg);
			}
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
					// Create the ACTION_GET_CONTENT Intent
					Intent getContentIntent = FileUtils.createGetContentIntent();

					Intent intent = Intent.createChooser(getContentIntent, "Select a file");
					startActivityForResult(intent, REQUEST_CHOOSER);
					}
				else {
					Toast.makeText(getActivity(), "External Storage not accessible", Toast.LENGTH_LONG).show();
				}
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
	                
	                input_res.setText(in_path);
	                
	                /* CHECK FILE IS A VIDEO */
	                
	                // Get storage directory
	                String str_dir_path = "/ffmpeg/";
	                File str_dir = MediaHelper.getVideosStorageDir(str_dir_path);
	                out_path = str_dir.getAbsolutePath();
	                output_res.setText(out_path);
	                
	                // Show Outputs
					rowIn.setVisibility(View.VISIBLE);
					rowOut.setVisibility(View.VISIBLE);
	            }
	            break;
	    }	
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// action bar
		inflater.inflate(R.menu.fragment_conversion_actions, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection of action bar
		switch (item.getItemId()) {
		case R.id.action_refresh:
			resetInputs();
			return true;
		case R.id.action_start:
			if (in_path.equals("") || out_path.equals(""))
				Toast.makeText(getActivity(), "Please choose input file", Toast.LENGTH_LONG).show();
			else {
				String fps = fps_res.getText().toString();
				String width = width_res.getText().toString();
				String height = height_res.getText().toString();
				try {
					Intent mServiceIntent = new Intent(getActivity(), ConversionService.class);
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
					MyNotification.sendNotification(getActivity(),
							"Conversion is in progress");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// change color of signal to on
				process_signal.setBackgroundColor(Color.parseColor(on_color));
				process_signal.setText(proccessing);
				/*sharedpreferences = getActivity().getSharedPreferences("com.example.ffmpeg_trial", Context.MODE_PRIVATE);
				Editor editor = sharedpreferences.edit();
				editor.putString("mssg", proccessing);
				editor.commit();*/
				// resetInputs();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
		} 
		else if (mssg.equals(proccessing)) {
			process_signal.setBackgroundColor(Color.parseColor(on_color));
			process_signal.setText(mssg);
		}
		else {
			// change color of signal to off
			process_signal.setBackgroundColor(Color.parseColor(off_color));
			process_signal.setText(mssg + "\n Choose a new video!");
		}
	}
	
	private void resetInputs() {
		// set inputs to blank
		input_res.setText("");
		output_res.setText("");
		fps_res.setText("");
		width_res.setText("");
		height_res.setText("");
		in_path = "";
		out_path = "";
		// Hide Outputs
		rowIn.setVisibility(View.GONE);
		rowOut.setVisibility(View.GONE);
	}

	private boolean isMyServiceRunning() {
		//http://stackoverflow.com/questions/7440473/android-how-to-check-if-the-intent-service-is-still-running-or-has-stopped-runni
	    ActivityManager manager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if ("com.example.Resources.ConversionService".equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
