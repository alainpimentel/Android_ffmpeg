package com.example.resources;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ffmpeg_trial.R;

public class OutputAdapter<T> extends ArrayAdapter<T> {
	
	Context cntxt;
	ArrayList<String> sizes;
	ArrayList<String> dates;
	List<T> files;

    public OutputAdapter(Context context, List<T> items, ArrayList<String> sizesList, ArrayList<String> datesList, int viewResourceId) {
        super(context, viewResourceId, items);
        cntxt = context;
        files = items;
        sizes = sizesList;
        dates = datesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
		LayoutInflater inflater = (LayoutInflater) cntxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.listfragment_output, parent, false);
		Typeface tf = Typeface.createFromAsset(cntxt.getAssets(), "fonts/Roboto-Light.ttf");
		Typeface tf_thin = Typeface.createFromAsset(cntxt.getAssets(), "fonts/Roboto-Thin.ttf");
		
		// file
		TextView fileName = (TextView) row.findViewById(R.id.file);
		fileName.setText(files.get(position).toString());
		fileName.setTypeface(tf);
		//size
		TextView size = (TextView) row.findViewById(R.id.size);
		size.setText(sizes.get(position).toString());
		size.setTypeface(tf_thin);
		//date
		TextView date = (TextView) row.findViewById(R.id.date);
		date.setText(dates.get(position).toString());
		date.setTypeface(tf_thin);
		
		return row;
    }
}


