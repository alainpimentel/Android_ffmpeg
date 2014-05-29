package com.example.ffmpeg_trial;

import java.io.File;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class FragmentOutput extends ListFragment {
	
	ArrayAdapter<String> adapter;
	int itemPosition = -1; // position of the selected view
	View sel_lv = null; // saves the selected view, passed to popup window
	String sel_file_text = "";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Get directory and populate list
		File dir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES), "/ffmpeg/");
		File[] filesArray = dir.listFiles();
		ArrayList<String> filesList = new ArrayList<String>();
		for (File file : filesArray) {
			String path = file.getAbsolutePath();
			filesList.add(path);
		}
		// adpater for listview, will contain paths of files
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, filesList);
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
			     return true;
			   }
			});
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
	
	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}*/
	
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
					deleteVideo(selection);
					// remove from list and update
					adapter.remove(selection);
					adapter.notifyDataSetChanged();
				}
				mode.finish();
				return true;
			}
			case R.id.info: {
				sel_file_text = adapter.getItem(itemPosition);
				displayPopupWindow(sel_lv);
				mode.finish();
				return true;
			}
			default:
				return false;
			}
		}
		
		// Called when the user selects a contextual menu item
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mode = null;
		}

	};
	
	// Display popup attached to the button as a position anchor
	  private void displayPopupWindow(View anchorView) {
		  View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup_info, null);

		    PopupWindow popupWindow = new PopupWindow(popupView, 
		                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		    // Example: If you have a TextView inside `popup_layout.xml`    
		    TextView tv = (TextView) popupView.findViewById(R.id.tvCaption);

		    tv.setText(sel_file_text);

		    // Initialize more widgets from `popup_layout.xml`

		    // If the PopupWindow should be focusable
		    popupWindow.setFocusable(true);

		    // If you need the PopupWindow to dismiss when when touched outside 
		    popupWindow.setBackgroundDrawable(new ColorDrawable());

		    int location[] = new int[2];

		    // Get the View's(the one that was clicked in the Fragment) location
		    anchorView.getLocationOnScreen(location);

		    // Using location, the PopupWindow will be displayed right under anchorView
		    popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, 
		                                     location[0], location[1] + anchorView.getHeight());
	  }
	
	private void deleteVideo(String path) {
		File file = new File(path);
		if(file.exists()) 
			file.delete();
	}
}