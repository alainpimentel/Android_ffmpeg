package com.example.resources;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ffmpeg_trial.R;

public class OutputAdapter<T> extends ArrayAdapter<T> {
	
	Context cntxt;
	ArrayList<Bitmap> thumbs;
	List<T> files;

    public OutputAdapter(Context context, List<T> items, ArrayList<Bitmap> bitmapsList, int viewResourceId) {
        super(context, viewResourceId, items);
        cntxt = context;
        thumbs = bitmapsList;
        files = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
		LayoutInflater inflater = (LayoutInflater) cntxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.listfragment_output, parent, false);
		TextView fileName = (TextView) row.findViewById(R.id.file);
		fileName.setText(files.get(position).toString());
		Typeface tf = Typeface.createFromAsset(cntxt.getAssets(), "fonts/Roboto-Light.ttf");
		fileName.setTypeface(tf);
		
		ImageView thumb = (ImageView) row.findViewById(R.id.thumb);
		// Customize your icon here
		thumb.setImageBitmap(thumbs.get(position));

		return row;
        /*View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(cntxt.getAssets(), "fonts/Roboto-Light.ttf");
        textView.setTypeface(tf);
        return view;*/
    }
}
