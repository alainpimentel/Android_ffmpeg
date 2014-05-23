package com.example.ffmpeg_trial;

import java.io.File;

import android.app.Fragment;
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

import com.ipaulpro.afilechooser.utils.FileUtils;

public class FragmentBrowser extends Fragment implements OnClickListener {

	private static final int REQUEST_CHOOSER = 1234;
	Button browse_button;
	TextView input_res;

	public FragmentBrowser() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_layout_browser, container, false);

		browse_button = (Button) view.findViewById(R.id.button_select_in);
		browse_button.setOnClickListener(this);
		input_res = (TextView) view.findViewById(R.id.input_res);
		return view;
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity(), "Clicked on button", Toast.LENGTH_LONG).show();
		// Create the ACTION_GET_CONTENT Intent
		Intent getContentIntent = FileUtils.createGetContentIntent();

		Intent intent = Intent.createChooser(getContentIntent, "Select a file");
		startActivityForResult(intent, REQUEST_CHOOSER);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	        case REQUEST_CHOOSER:   
	            if (resultCode == getActivity().RESULT_OK) {

	                final Uri uri = data.getData();
	                
	                // Get the File path from the Uri
	                String path = FileUtils.getPath(getActivity(), uri);
	                Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
	                
	                input_res.setText(path);
	                // Alternatively, use FileUtils.getFile(Context, Uri)
	                if (path != null && FileUtils.isLocal(path)) {
	                    File file = new File(path);
	                }
	            }
	            break;
	    }
	}

}
