package com.example.umborno.schedule;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;

import com.example.umborno.model.reminder_model.Reminder;

import java.util.concurrent.TimeUnit;

public class ReminderWorkerHelper {
    private void parseReminder(Reminder reminder) {
        String repeat = reminder.getRepeat();

    }

    public void createWorkRequest(Reminder reminder){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data data = new Data.Builder()
                .putString(CurrentWeatherWorker.REMINDER_LOCATION_ID_KEY,reminder.getLocationKey())
                .build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CurrentWeatherWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();


        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                CurrentWeatherWorker.class, 2, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();
    }
}
