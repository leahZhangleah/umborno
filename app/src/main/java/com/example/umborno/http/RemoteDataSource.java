package com.example.umborno.http;

import androidx.lifecycle.LiveData;

import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;
import com.example.umborno.model.location_search_model.SearchSuggestion;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import retrofit2.Call;

public class RemoteDataSource {

    public static final int DAYS = 1;
    private final AccuWeatherApiInterface accuWeatherApiInterface;

    @Inject
    public RemoteDataSource(AccuWeatherApiInterface accuWeatherApiInterface) {
        this.accuWeatherApiInterface = accuWeatherApiInterface;
    }

    public Maybe<List<CurrentWeather>> getCurrentWeather(String key){
        return accuWeatherApiInterface.getCurrentWeather(key);
    }

    public Maybe<LocationKey> getLocationKey(float lat, float lon){
        String location = lat+","+lon;
        return accuWeatherApiInterface.getLocationKey(location);
    }

    public Call<List<SearchSuggestion>> getLocationSuggestions(String userInput){
        return accuWeatherApiInterface.getLocationSuggestions(userInput);
    }
}
