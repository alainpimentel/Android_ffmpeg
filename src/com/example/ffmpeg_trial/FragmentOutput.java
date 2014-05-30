package com.example.ffmpeg_trial;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.resources.OutputAdapter;

public class FragmentOutput extends ListFragment {
	
	ArrayAdapter<String> adapter;
	ListView lv;
	int itemPosition = -1; // position of the selected view
	View sel_lv = null; // saves the selected view, passed to popup window
	String sel_file_text = ""; // text of seected item
	String fileRoot = ""; // keep track of the root of the videos
	SharedPreferences sharedpreferences;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Get directory and populate list
		File dir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES), "/ffmpeg/");
		File[] filesArray = dir.listFiles();
		ArrayList<String> filesList = new ArrayList<String>(); // holds file names
		ArrayList<String> dateList = new ArrayList<String>(); // holds dates
		ArrayList<String> sizeList = new ArrayList<String>(); // holds size
		for (File file : filesArray) {
			String path = file.getName();
			fileRoot = file.getParent();
			filesList.add(path);
			dateList.add(getDate(file));
			sizeList.add(getSize(file));
		}
		// adpater for listview, will contain paths of files
		adapter = new OutputAdapter<String>(getActivity(), filesList, sizeList, dateList, R.layout.listfragment_output);
		//setListAdapter(adapter);
		setListAdapter(adapter);
		
		//registerForContextMenu(getListView()); //link listview to context menu
		
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			   public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
			     System.out.println("Long click");
			     getActivity().startActionMode(modeCallBack);
			     itemPosition = position;
			     view.setTag(itemPosition);
			     view.setSelected(true);
			     sel_lv = view;
			     view.setBackgroundResource(android.R.color.holo_blue_light);
			     return true;
			   }
			});
		lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setSelector(R.drawable.listitem_background); // can do different kind of colors in diff states
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Open file with available apps
		String filePath = l.getItemAtPosition(position).toString();
		File file = new File(fileRoot + "/" + filePath);
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
	
	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
		
		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("Options");
			mode.getMenuInflater().inflate(R.menu.context_menu, menu);
			return true;
		}
		
		// Called each time the action mode is shown. Always called after onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		// Called when the user exits the action mode
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			int id = item.getItemId();
			
			//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			switch (id) {
			case R.id.delete: {
				// remove from device
				if(itemPosition != -1) {
					String selection = adapter.getItem(itemPosition);
					// remove from phone
					deleteVideo(fileRoot+ "/" + selection);
					// remove from list and update
					adapter.remove(selection);
					adapter.notifyDataSetChanged();
				}
				//sel_lv.setBackgroundResource(android.R.color.transparent); // transparent background
				mode.finish();
				return true;
			}
			case R.id.info: {
				sel_file_text = adapter.getItem(itemPosition); // content of item
				sharedpreferences = getActivity().getSharedPreferences("com.example.ffmpeg_trial", Context.MODE_PRIVATE);
				Editor editor = sharedpreferences.edit();
				editor.putString("file", fileRoot + "/" +sel_file_text);
				editor.commit();
				new MyDialogFragment().show(getFragmentManager(), "MyDialog");
				//sel_lv.setBackgroundResource(android.R.color.transparent);
				mode.finish();
				return true;
			}
			default:
				//sel_lv.setBackgroundResource(android.R.color.transparent);
				return false;
			}
		}
		
		// Called when the user selects a contextual menu item
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			sel_lv.setBackgroundResource(android.R.color.transparent);
			mode = null;
		}

	};
	
	private void deleteVideo(String path) {
		File file = new File(path);
		if(file.exists()) 
			file.delete();
	}
	
	private String getSize(File file) {
		//size
		Double value = file.length()/1024.0/1024.0; // convert B to MB
		DecimalFormat df=new DecimalFormat("0.00");
		String formate = df.format(value); 
		double finalValue = 0;
		try {
			finalValue = (Double)df.parse(formate) ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(finalValue) + " MB";
	}
	
	private String getDate(File file) {
		Date lastModDate = new Date(file.lastModified());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return String.valueOf(formatter.format(lastModDate));
	}
	
}