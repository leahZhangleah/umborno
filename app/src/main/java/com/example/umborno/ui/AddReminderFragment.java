package com.example.umborno.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.umborno.R;
import com.example.umborno.viewmodel.LocationViewModel;
import com.example.umborno.viewmodel.SearchLocationViewModel;
import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReminderFragment extends Fragment{
    NavController navController;
    RelativeLayout dateLayout,timeLayout;
    DatePicker datePicker;
    TimePicker timePicker;

    @Inject
    public WeatherViewModelProviderFactory factory;
    private SearchLocationViewModel searchLocationViewModel;
    @Inject
    public LocationViewModel locationViewModel;

    public AddReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        //for listening to the value change of location

        initViews(view);
        searchLocationViewModel = ViewModelProviders.of(this,factory).get(SearchLocationViewModel.class);

        TextView locationView = view.findViewById(R.id.get_location_tv);
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_addReminderFragment_to_searchFragment);
            }
        });

        locationViewModel.getSelectedLocation().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                locationView.setText(s);
            }
        });
    }

    private void initViews(View view) {
        dateLayout = view.findViewById(R.id.date_layout);
        timeLayout = view.findViewById(R.id.time_layout);

        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePicker.getVisibility() == View.GONE){
                    datePicker.setVisibility(View.VISIBLE);
                }else{
                    datePicker.setVisibility(View.GONE);
                }
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timePicker.getVisibility() == View.GONE){
                    timePicker.setVisibility(View.VISIBLE);
                }else{
                    timePicker.setVisibility(View.GONE);
                }
            }
        });
    }
}
