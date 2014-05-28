package com.example.ffmpeg_trial;

import java.io.File;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentOutput extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Get directory and populate list
		File dir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES), "/ffmpeg/");
		File[] filesArray = dir.listFiles();
		String[] filesList = new String[filesArray.length];
		int i = 0;
		for (File file : filesArray) {
			String path = file.getAbsolutePath();
			filesList[i] = path;
			i++;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, filesList);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Open file with available apps
		String filePath = l.getItemAtPosition(position).toString();
		File file = new File(filePath);
	    MimeTypeMap map = MimeTypeMap.getSingleton();
	    String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
	    String type = map.getMimeTypeFromExtension(ext);

	    if (type == null)
	        type = "*/*";

	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    Uri data = Uri.fromFile(file);

	    intent.setDataAndType(data, type);

	    startActivity(intent);
	}
}