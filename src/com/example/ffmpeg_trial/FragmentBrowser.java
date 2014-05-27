package com.example.ffmpeg_trial;

import java.io.File;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resources.MediaHelper;
import com.example.resources.VideoHandler;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class FragmentBrowser extends Fragment implements OnClickListener {

	private static final int REQUEST_CHOOSER = 1234;
	Button browse_button, start_button, button_service;
	TextView input_res, output_res, fps_res,
			width_res, height_res;
	String in_path = "", 
		   out_path = "";

	public FragmentBrowser() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_layout_browser, container, false);

		browse_button = (Button) view.findViewById(R.id.button_select_in);
		browse_button.setOnClickListener(this);
		start_button = (Button) view.findViewById(R.id.button_start);
		start_button.setOnClickListener(this);
		input_res = (TextView) view.findViewById(R.id.input_res);
		output_res = (TextView) view.findViewById(R.id.output_res);
		fps_res = (TextView) view.findViewById(R.id.fps_res);
		width_res = (TextView) view.findViewById(R.id.width_res);
		height_res = (TextView) view.findViewById(R.id.height_res);
		button_service = (Button) view.findViewById(R.id.button_service);
		button_service.setOnClickListener(this);
		return view;
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
           case R.id.button_start:
        	   if(in_path == "" || out_path == "")
        		   Toast.makeText(getActivity(), "Please choose input file", Toast.LENGTH_LONG).show();
        	   else{
        		   // TODO add checks to fps,w,h
        		   Context context = getActivity();
        		   String fps = fps_res.getText().toString();
        		   String width = width_res.getText().toString();
        		   String height = height_res.getText().toString();
        		   try {
        			   
        			   Toast.makeText(getActivity(), "Processing", Toast.LENGTH_LONG).show();
        			   VideoHandler.videoConverter(context, in_path, out_path, fps, width, height);
				   } catch (Exception e) {
					   // TODO Auto-generated catch block
					   e.printStackTrace();
				   }
        		   Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
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
	                Toast.makeText(getActivity(), in_path, Toast.LENGTH_LONG).show();
	                
	                input_res.setText(in_path);
	                // Alternatively, use FileUtils.getFile(Context, Uri)
	                if (in_path != null && FileUtils.isLocal(in_path)) {
	                    File file = new File(in_path);
	                }
	                
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

}
