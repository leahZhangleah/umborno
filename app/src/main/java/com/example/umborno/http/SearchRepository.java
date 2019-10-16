package com.example.umborno.http;

import android.content.Context;
import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.umborno.model.location_search_model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchRepository {
    private static final String TAG = "SearchRepository";
    private RemoteDataSource remoteDataSource;
    private MutableLiveData<List<SearchSuggestion>> locationSuggestionsLiveData;

    @Inject
    public SearchRepository(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public LiveData<List<SearchSuggestion>> getLocationSuggestions(CharSequence s){
        locationSuggestionsLiveData = new MutableLiveData<>();
        remoteDataSource.getLocationSuggestions(s.toString()).enqueue(new Callback<List<SearchSuggestion>>() {
            @Override
            public void onResponse(Call<List<SearchSuggestion>> call, Response<List<SearchSuggestion>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    locationSuggestionsLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SearchSuggestion>> call, Throwable t) {

            }
        });
        return locationSuggestionsLiveData;
    }

}
