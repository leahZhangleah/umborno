package com.example.umborno.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.umborno.R;
import com.example.umborno.viewmodel.RepeatViewModel;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepeatFragment extends Fragment {
    private ListView repeatLv;
    private NavController navController;
    @Inject
    RepeatViewModel repeatViewModel;

    public RepeatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repeat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        repeatLv = view.findViewById(R.id.repeat_lv);

        String[] repeatValues = getResources().getStringArray(R.array.repeat_options);
        //ListAdapter repeatAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_checked,repeatValues);
        RepeatAdapter repeatAdapter = new RepeatAdapter(getContext(),repeatValues,0);
        repeatLv.setAdapter(repeatAdapter);
        repeatLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackground(getResources().getDrawable(R.drawable.pressed_layout_bg,null));
                repeatAdapter.setPosition(position);
                String repeatMode = repeatValues[position];
                repeatViewModel.setRepeatMode(repeatMode);
                navController.popBackStack();
            }
        });

        repeatViewModel.getRepeatMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int selectPos = Arrays.asList(repeatValues).indexOf(s);
                repeatAdapter.setPosition(selectPos);
            }
        });

    }
}
