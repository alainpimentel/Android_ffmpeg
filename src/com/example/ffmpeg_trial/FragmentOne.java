package com.example.ffmpeg_trial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
public class FragmentOne extends Fragment {
 
      ImageView ivIcon;
      TextView tvItemName;
 
      public static final String IMAGE_RESOURCE_ID = "iconResourceID";
      public static final String ITEM_NAME = "itemName";
 
      public FragmentOne() {
 
      }
 
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
 
            View view = inflater.inflate(R.layout.fragment_layout_one, container,
                        false);
 
            ivIcon = (ImageView) view.findViewById(R.id.frag1_icon);
            tvItemName = (TextView) view.findViewById(R.id.frag1_text);
 
            tvItemName.setText(getArguments().getString(ITEM_NAME));
            ivIcon.setImageDrawable(view.getResources().getDrawable(
                        getArguments().getInt(IMAGE_RESOURCE_ID)));
            return view;
      }
 
}