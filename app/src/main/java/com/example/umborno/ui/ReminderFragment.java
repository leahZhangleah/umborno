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

import com.example.umborno.R;
import com.example.umborno.ReminderViewModel;
import com.example.umborno.WeatherViewModelProviderFactory;
import com.example.umborno.http.Resource;
import com.example.umborno.http.Status;
import com.example.umborno.model.Reminder;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;
import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ReminderFragment";
    NavController navController;
    @Inject
    public WeatherViewModelProviderFactory factory;
    private ReminderViewModel reminderViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.add_reminder).setOnClickListener(this);
        reminderViewModel = ViewModelProviders.of(this,factory).get(ReminderViewModel.class);
        reminderViewModel.getReminderLiveData().observe(this, new Observer<Resource<List<Reminder>>>() {
            @Override
            public void onChanged(Resource<List<Reminder>> listResource) {
                if(listResource.getStatus()== Status.SUCCESS){
                    List<Reminder> reminders = listResource.getData();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        navController.navigate(R.id.action_reminderFragment_to_addReminderFragment);
    }
}
