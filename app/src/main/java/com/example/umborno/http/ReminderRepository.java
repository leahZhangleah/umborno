package com.example.umborno.http;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.model.reminder_model.Reminder;

import java.util.List;

import javax.inject.Inject;

public class ReminderRepository {
    private static final String TAG = "ReminderRepository";
    private MutableLiveData<Resource<List<Reminder>>> remindersLiveData = new MutableLiveData<>();
    private LocalDataSource localDataSource;
    private AppExecutors executor;

    @Inject
    public ReminderRepository(LocalDataSource localDataSource, AppExecutors executor) {
        this.localDataSource = localDataSource;
        this.executor = executor;
    }

    public LiveData<Resource<List<Reminder>>> getAllReminders(){
        executor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                remindersLiveData.postValue(Resource.success(localDataSource.getReminders()));
            }
        });
        return remindersLiveData;
    }

    public void addReminder(final Reminder reminder){
        executor.getDiskIO().execute(() -> localDataSource.addReminder(reminder));
    }
}
