package com.example.umborno;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.umborno.http.ReminderRepository;
import com.example.umborno.http.Resource;
import com.example.umborno.http.WeatherRepository;
import com.example.umborno.model.Reminder;

import java.util.List;

import javax.inject.Inject;

public class ReminderViewModel extends ViewModel {
    private static final String TAG = "ReminderViewModel";
    LiveData<Resource<List<Reminder>>> reminderLiveData;

    @Inject
    public ReminderViewModel(ReminderRepository reminderRepository){
        reminderLiveData =reminderRepository.getAllReminders();
    }

    public LiveData<Resource<List<Reminder>>> getReminderLiveData() {
        return reminderLiveData;
    }

    public void addReminder(Reminder reminder){

    }

    public void deleteReminder(Reminder reminder){

    }

    public void updateReminder(Reminder reminder){

    }

    public void deleteAllReminders(){

    }
}
