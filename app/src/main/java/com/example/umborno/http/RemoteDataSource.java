package com.example.umborno.http;

import androidx.lifecycle.LiveData;

import com.example.umborno.model.CurrentWeather;

import javax.inject.Inject;

public class RemoteDataSource {

    public static final int DAYS = 1;
    private final ApiInterface apiInterface;

    @Inject
    public RemoteDataSource(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<ApiResponse<CurrentWeather>> getCurrentWeather(float lat,float lon){
        return apiInterface.getCurrentWeather(lat,lon);
    }
}
