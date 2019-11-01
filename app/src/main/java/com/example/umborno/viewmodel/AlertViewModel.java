package com.example.umborno.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlertViewModel extends ViewModel {
    private MutableLiveData<String> alertMode;

    public AlertViewModel() {
        this.alertMode = new MutableLiveData<>();
        alertMode.postValue("None");
    }

    public MutableLiveData<String> getAlertMode() {
        return alertMode;
    }

    public void setAlertMode(String newAlertMode) {
        alertMode.postValue(newAlertMode);
    }
}
