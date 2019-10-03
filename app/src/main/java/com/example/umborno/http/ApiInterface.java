package com.example.umborno.http;


import androidx.lifecycle.LiveData;

import com.example.umborno.model.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

    //https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&APPID=13ecab2c0d1527b53d6df6509f7c9498
    @GET("weather")
    LiveData<ApiResponse<CurrentWeather>> getCurrentWeather(@Query("lat") float lat, @Query("lon") float lon);


}
