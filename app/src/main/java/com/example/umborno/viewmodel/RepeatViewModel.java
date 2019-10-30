package com.example.umborno.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RepeatViewModel extends ViewModel {
    private MutableLiveData<String> repeatMode;

    public RepeatViewModel() {
        this.repeatMode = new MutableLiveData<>();
        repeatMode.postValue("Never");
    }

    public MutableLiveData<String> getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(String newRepeatMode) {
        repeatMode.postValue(newRepeatMode);
    }
}
