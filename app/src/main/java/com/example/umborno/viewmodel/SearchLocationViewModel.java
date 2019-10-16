package com.example.umborno.viewmodel;

import android.widget.ArrayAdapter;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.umborno.http.Resource;
import com.example.umborno.http.SearchRepository;
import com.example.umborno.model.location_search_model.SearchSuggestion;


import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;


public class SearchLocationViewModel extends ViewModel {
    private SearchRepository searchRepository;
    private MutableLiveData<CharSequence> userInputLiveData;
    private LiveData<List<SearchSuggestion>> locationSuggestionsLiveData;

    @Inject
    public SearchLocationViewModel(final SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
        userInputLiveData = new MutableLiveData<>();
        locationSuggestionsLiveData = Transformations.switchMap(userInputLiveData, new Function<CharSequence, LiveData<List<SearchSuggestion>>>() {
            @Override
            public LiveData<List<SearchSuggestion>> apply(CharSequence input) {
                return searchRepository.getLocationSuggestions(input);
            }
        });
    }

    public LiveData<List<SearchSuggestion>> getLocationSuggestionsLiveData() {
        return locationSuggestionsLiveData;
    }

    public void setUserInput(CharSequence input){
        userInputLiveData.postValue(input);
    }

}
