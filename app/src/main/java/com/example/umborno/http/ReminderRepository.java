package com.example.umborno.http;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.umborno.db.DbFunction;
import com.example.umborno.db.DbResponse;
import com.example.umborno.db.LocalDataSource;
import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.schedule.CurrentWeatherWorker;

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

    public MutableLiveData<DbResponse<Reminder>> addReminder(final Reminder reminder){
        MutableLiveData<DbResponse<Reminder>> dbResponseMutableLiveData = new MutableLiveData<>(DbResponse.loading(null,DbFunction.INSERT));
        executor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                long index = localDataSource.addReminder(reminder);
                if(index!= -1){ //not successful insertion
                    dbResponseMutableLiveData.postValue(DbResponse.success(index,reminder, DbFunction.INSERT));
                }else{
                    dbResponseMutableLiveData.postValue(DbResponse.error(index,null,DbFunction.INSERT));
                }
            }
        });
        return dbResponseMutableLiveData;
    }

    public MutableLiveData<DbResponse<Reminder>> deleteReminder(final Reminder reminder){
        MutableLiveData<DbResponse<Reminder>> dbResponseMutableLiveData = new MutableLiveData<>(DbResponse.loading(null,DbFunction.DELETE));
        executor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int index =localDataSource.deleteReminder(reminder);
                if(index !=-1){
                    dbResponseMutableLiveData.postValue(DbResponse.success(index,reminder,DbFunction.DELETE));
                }else{
                    dbResponseMutableLiveData.postValue(DbResponse.error(index,null,DbFunction.DELETE));
                }
            }
        });
        return dbResponseMutableLiveData;
    }
}





















