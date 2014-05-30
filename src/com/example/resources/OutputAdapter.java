package com.example.resources;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OutputAdapter<T> extends ArrayAdapter<T> {
	
	Context cntxt;

    public OutputAdapter(Context context, List<T> items) {

        super(context, android.R.layout.simple_list_item_activated_1, android.R.id.text1, items);
        cntxt = context;
    }

    public OutputAdapter(Context context, T[] items) {
    	
        super(context, android.R.layout.simple_list_item_activated_1, android.R.id.text1, items);
        cntxt = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(cntxt.getAssets(), "fonts/Roboto-Light.ttf");
        textView.setTypeface(tf);
        return view;
    }
}
