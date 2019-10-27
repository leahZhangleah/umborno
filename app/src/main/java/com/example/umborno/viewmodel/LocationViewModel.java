package com.example.umborno.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<String> selectedLocation;

    public LocationViewModel() {
        this.selectedLocation = new MutableLiveData<>();
        selectedLocation.postValue("");
    }

    public MutableLiveData<String> getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String newLocation) {
        selectedLocation.postValue(newLocation);
    }
}
