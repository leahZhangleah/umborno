package com.example.umborno.schedule;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.umborno.R;
import com.example.umborno.http.AccuWeatherApiInterface;
import com.example.umborno.model.current_weather_model.CurrentWeather;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CurrentWeatherWorker extends Worker {
    private static final String TAG = "CurrentWeatherWorker";
    public static final String CHANNEL_ID = "umborno";
    public static final String CHANNEL_NAME = "umborno";
    public static final String REMINDER_LOCATION_ID_KEY="reminder_location_id_key";
    public AccuWeatherApiInterface apiInterface;
    private ListenableWorker.Result result = Result.failure();

    private CurrentWeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams,AccuWeatherApiInterface apiInterface) {
        super(context, workerParams);
        this.apiInterface = apiInterface;
    }

    public static class Factory implements CurrentWeatherWorkerFactory{
        Provider<AccuWeatherApiInterface> apiInterfaceProvider;

        @Inject
        public Factory(Provider<AccuWeatherApiInterface> apiInterfaceProvider) {
            this.apiInterfaceProvider = apiInterfaceProvider;
        }

        @Override
        public ListenableWorker create(Context appContext, WorkerParameters params) {
            return new CurrentWeatherWorker(appContext,params,apiInterfaceProvider.get());
        }
    }

    @NonNull
    @Override
    public Result doWork() {

        String locationKey = getInputData().getString(REMINDER_LOCATION_ID_KEY);
        fetchWeather(locationKey)
                .subscribe(new Consumer<List<CurrentWeather>>() {
                    @Override
                    public void accept(List<CurrentWeather> currentWeathers) throws Exception {
                        if (currentWeathers != null && !currentWeathers.isEmpty()) {
                            CurrentWeather currentWeather = currentWeathers.get(0);
                            displayNotification("today's weather",currentWeather.getWeatherText());
                            result = Result.success();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "fetch failed");
                        result = Result.retry();
                    }
                });
        //displayNotification("Current weather worker","new reminder for location key "+locationKey);
        return result;
    }


    private Maybe<List<CurrentWeather>> fetchWeather(String locationKey) {
        Log.d(TAG, "fetchWeather: " + apiInterface.toString());
        Log.d(TAG, "fetchWeather: current location key: "+locationKey);
        return apiInterface.getCurrentWeather(locationKey).subscribeOn(Schedulers.io());
    }

    private void displayNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher_round);
        notificationManager.notify(1,notificationBuilder.build());

    }
}





















