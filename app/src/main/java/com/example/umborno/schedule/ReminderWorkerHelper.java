package com.example.umborno.schedule;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;

import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.model.reminder_model.ReminderDate;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReminderWorkerHelper {

    private static long parseReminderDateTime(Reminder reminder){
        ReminderDate reminderDate = reminder.getDateTime();
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        calendar.set(reminderDate.getYear(),reminderDate.getMonth(),reminderDate.getDay(),reminderDate.getHour(),reminderDate.getMinute());
        long reminderTimeInMillis =calendar.getTimeInMillis();
        return reminderTimeInMillis-currentTimeInMillis;
    }

    private static long parseReminderInitialDelay(Reminder reminder){
        String alert = reminder.getAlert();
        long timeDifference = parseReminderDateTime(reminder);
        switch (alert){
            case "None":
                return timeDifference;
            case "At time of event":
                return timeDifference;
            case "5 minutes before":
                return timeDifference - TimeUnit.MINUTES.toMillis(5);
            case "15 minutes before":
                return timeDifference - TimeUnit.MINUTES.toMillis(15);
            case "30 minutes before":
                return timeDifference - TimeUnit.MINUTES.toMillis(30);
            default:
                return timeDifference;
        }
    }

    private static long parseReminderAlertTime(Reminder reminder){
        String alert = reminder.getAlert();
        ReminderDate reminderDate = reminder.getDateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(reminderDate.getYear(),reminderDate.getMonth(),reminderDate.getDay(),reminderDate.getHour(),reminderDate.getMinute());
        long alertTime = calendar.getTimeInMillis();
        switch (alert){
            case "None":
                return alertTime;
            case "At time of event":
                return alertTime;
            case "5 minutes before":
                return alertTime - TimeUnit.MINUTES.toMillis(5);
            case "15 minutes before":
                return alertTime - TimeUnit.MINUTES.toMillis(15);
            case "30 minutes before":
                return alertTime - TimeUnit.MINUTES.toMillis(30);
            default:
                return alertTime;
        }
    }

    private static WorkRequest.Builder createOneTimeWorkRequest(long duration){
        return  new OneTimeWorkRequest.Builder(CurrentWeatherWorker.class)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS);
    }

    private static WorkRequest.Builder createPeriodicWorkRequest(long repeatInterval, long duration){
        return new PeriodicWorkRequest.Builder(
                CurrentWeatherWorker.class, repeatInterval, TimeUnit.DAYS,5,TimeUnit.MINUTES)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS);
    }

    private static WorkRequest.Builder parseReminderRepeat(Reminder reminder){
        String repeat = reminder.getRepeat();
        switch (repeat){
            case "Never":
                if (parseReminderInitialDelay(reminder) >= 0) {
                    return createOneTimeWorkRequest(parseReminderInitialDelay(reminder));
                } else {
                    //no request
                    return null;
                }
            case "Every Day":
                if (parseReminderInitialDelay(reminder) >= 0) {
                    return createPeriodicWorkRequest(1, parseReminderInitialDelay(reminder));
                } else {
                    return createPeriodicWorkRequest(1, TimeUnit.DAYS.toMillis(1) + parseReminderInitialDelay(reminder));
                }
            case "Every Week":
                if (parseReminderInitialDelay(reminder) >= 0) {
                    return createPeriodicWorkRequest(7, parseReminderInitialDelay(reminder));
                } else {
                    return createPeriodicWorkRequest(7, TimeUnit.DAYS.toMillis(7) + parseReminderInitialDelay(reminder));
                }
            default:
                    return null;

        }
    }


    public static WorkRequest createWorkRequest(Reminder reminder){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data data = new Data.Builder()
                .putString(CurrentWeatherWorker.REMINDER_LOCATION_ID_KEY,reminder.getLocationKey())
                .putString(CurrentWeatherWorker.REMINDER_ALERT_KEY,reminder.getAlert())
                .putLong(CurrentWeatherWorker.REMINDER_ALERT_TIME_KEY,parseReminderAlertTime(reminder))
                .build();
        if(parseReminderRepeat(reminder)==null){
            return null;
        }else{
            return parseReminderRepeat(reminder)
                    .setConstraints(constraints)
                    .setInputData(data)
                    .build();
        }
    }

}
