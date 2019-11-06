package com.example.umborno.schedule;

import android.content.Context;

import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

public interface CurrentWeatherWorkerFactory {
    public ListenableWorker create(Context appContext, WorkerParameters params);
}
