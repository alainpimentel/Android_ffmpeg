package com.example.ffmpeg_trial;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
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
	    */
        setContentView(R.layout.activity_main);
        
        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                    GravityCompat.START);
         
        // Add Drawer Item to dataList
        dataList.add(new DrawerItem("Convert Videos"));
        dataList.add(new DrawerItem("Browse", R.drawable.ic_action_import_export));
        dataList.add(new DrawerItem("Output", R.drawable.ic_action_video));
        dataList.add(new DrawerItem("Processing", R.drawable.ic_action_good));
        dataList.add(new DrawerItem("Other Options"));
        dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
        
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener()); 
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
         
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.ic_drawer, R.string.drawer_open,
                    R.string.drawer_close) {
              public void onDrawerClosed(View view) {
                    getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu(); // creates call to
                                                              // onPrepareOptionsMenu()
              }
         
              public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu(); // creates call to
                                                              // onPrepareOptionsMenu()
              }
        };
         
        mDrawerLayout.setDrawerListener(mDrawerToggle);
         
        if (savedInstanceState == null) {
	        if (dataList.get(0).getTitle() != null) {
	            SelectItem(1);
	        } else {
	            SelectItem(0);
	        }
        }
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//          // Inflate the menu; this adds items to the action bar if it is present.
//          getMenuInflater().inflate(R.menu.main, menu);
//          return true;
//    }
    
    public void SelectItem(int possition) {
    	 
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {
        case 1:
              fragment = new FragmentOne();
              args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                          .getItemName());
              args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
                          .getImgResID());
              break;
        case 2:
              fragment = new FragmentTwo();
              args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
                          .getItemName());
              args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
                          .getImgResID());
              break;
        case 3:
              fragment = new FragmentThree();
              args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                          .getItemName());
              args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                          .getImgResID());
              break;        
        case 5:
              fragment = new FragmentTwo();
              args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
                          .getItemName());
              args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
                          .getImgResID());
              break;
        
        default:
              break;
        }

        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();

        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

  }
    
    @Override
    public void setTitle(CharSequence title) {
          mTitle = title;
          getActionBar().setTitle(mTitle);
    }
     
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
          super.onPostCreate(savedInstanceState);
          // Sync the toggle state after onRestoreInstanceState has occurred.
          mDrawerToggle.syncState();
    }
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          // The action bar home/up action should open or close the drawer.
          // ActionBarDrawerToggle will take care of this.
          if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
          }
     
          return false;
    }
     
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
          super.onConfigurationChanged(newConfig);
          // Pass any configuration change to the drawer toggles
          mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    private class DrawerItemClickListener implements
    ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
		          long id) {
		    if (dataList.get(position).getTitle() == null) {
		          SelectItem(position);
		    }
		
		}
    }
}
