package com.example.umborno.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<String> selectedLocation;
    private MutableLiveData<String> selectedLocationKey;

    public LocationViewModel() {
        this.selectedLocation = new MutableLiveData<>();
        this.selectedLocationKey = new MutableLiveData<>();
        selectedLocation.postValue("");
        selectedLocationKey.postValue("");

    }

    public MutableLiveData<String> getSelectedLocation() {
        return selectedLocation;
    }

    public MutableLiveData<String> getSelectedLocationKey() {
        return selectedLocationKey;
    }

    public void setSelectedLocation(String newLocation) {
        selectedLocation.postValue(newLocation);
    }

    public void setSelectedLocationKey(String newLocationKey) {
        this.selectedLocationKey.postValue(newLocationKey);
    }
}
