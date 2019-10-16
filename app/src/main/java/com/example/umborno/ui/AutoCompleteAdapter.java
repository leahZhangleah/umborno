package com.example.umborno.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter<T> extends ArrayAdapter<T> implements Filterable {
    private List<T> mResults;
    public AutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return mResults.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        T item = getItem(position);
        TextView city = view.findViewById(android.R.id.text1);
        TextView country = view.findViewById(android.R.id.text2);
        if(item!=null){

        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }
}
