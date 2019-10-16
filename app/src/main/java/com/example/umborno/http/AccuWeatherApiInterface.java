package com.example.umborno.http;

import androidx.lifecycle.LiveData;

import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;
import com.example.umborno.model.location_search_model.SearchSuggestion;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccuWeatherApiInterface {

    @GET("locations/v1/cities/geoposition/search")
    Maybe<LocationKey> getLocationKey(@Query("q") String latLng);

    //"http://dataservice.accuweather.com/currentconditions/v1/60811?apikey=MWbZWE2Z9aG34U8WVuIcPuR6aVbY9HFA
    @GET("currentconditions/v1/{key}")
    Maybe<List<CurrentWeather>> getCurrentWeather(@Path("key") String key);

    @GET("locations/v1/cities/autocomplete")
    Call<List<SearchSuggestion>> getLocationSuggestions(@Query("q") String userInput);
}
