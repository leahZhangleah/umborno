package com.example.umborno.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.umborno.R;

public class RepeatAdapter extends ArrayAdapter<String> {
    private int selectedPos;

    public RepeatAdapter(@NonNull Context context, @NonNull String[] objects,int position) {
        super(context, 0, objects);
        selectedPos = position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String repeatMode = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.repeat_option_item,parent,false);
        }
        TextView repeatOptionTv = (TextView) convertView.findViewById(R.id.repeat_option_tv);
        ImageView selectedIv = (ImageView) convertView.findViewById(R.id.repeated_option_selected);
        repeatOptionTv.setText(repeatMode);
        if(selectedPos == position){
            selectedIv.setVisibility(View.VISIBLE);
        }else{
            selectedIv.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public void setPosition(int position) {
        this.selectedPos = position;
        notifyDataSetChanged();
    }
}



























