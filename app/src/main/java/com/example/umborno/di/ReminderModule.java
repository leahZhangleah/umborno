package com.example.umborno.di;

import androidx.lifecycle.ViewModelProviders;

import com.example.umborno.ui.ReminderActivity;
import com.example.umborno.viewmodel.LocationViewModel;
import com.example.umborno.viewmodel.RepeatViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderModule {
    @Provides
    @PerReminderActivity
    static LocationViewModel provideLocationViewModel(ReminderActivity activity){
        return ViewModelProviders.of(activity).get(LocationViewModel.class);
    }

    @Provides
    @PerReminderActivity
    static RepeatViewModel provideRepeatViewModel(ReminderActivity activity){
        return ViewModelProviders.of(activity).get(RepeatViewModel.class);
    }
}
