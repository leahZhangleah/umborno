package com.example.umborno.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.umborno.db.DbResponse;
import com.example.umborno.http.ReminderRepository;
import com.example.umborno.http.Resource;
import com.example.umborno.model.reminder_model.Reminder;

import java.util.List;

import javax.inject.Inject;

public class ReminderViewModel extends ViewModel {
    private static final String TAG = "ReminderViewModel";
    //LiveData<Resource<List<Reminder>>> reminderLiveData;
    ReminderRepository reminderRepository;

    @Inject
    public ReminderViewModel(ReminderRepository reminderRepository){
        this.reminderRepository = reminderRepository;
    }

    public LiveData<Resource<List<Reminder>>> getReminderLiveData() {
        return reminderRepository.getAllReminders();
    }

    public MutableLiveData<DbResponse<Reminder>> addReminder(Reminder reminder){
        return reminderRepository.addReminder(reminder);
    }

    public void deleteReminder(Reminder reminder){

    }

    public void updateReminder(Reminder reminder){

    }

    public void deleteAllReminders(){

    }
}
