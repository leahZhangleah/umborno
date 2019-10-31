package com.example.umborno.di;

import androidx.lifecycle.ViewModelProviders;

import com.example.umborno.ui.MainActivity;
import com.example.umborno.viewmodel.LocationViewModel;
import com.example.umborno.viewmodel.RepeatViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderModule {
    @Provides
    static LocationViewModel provideLocationViewModel(MainActivity activity){
        return ViewModelProviders.of(activity).get(LocationViewModel.class);
    }

    @Provides
    static RepeatViewModel provideRepeatViewModel(MainActivity activity){
        return ViewModelProviders.of(activity).get(RepeatViewModel.class);
    }
}
