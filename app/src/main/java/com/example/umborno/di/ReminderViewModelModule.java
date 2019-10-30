package com.example.umborno.di;

import androidx.lifecycle.ViewModel;

import com.example.umborno.viewmodel.ReminderViewModel;
import com.example.umborno.viewmodel.SearchLocationViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {ReminderRepoModule.class,ViewModelFactoryModule.class})
public abstract class ReminderViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReminderViewModel.class)
    abstract ViewModel bindReminderViewModel(ReminderViewModel reminderViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(SearchLocationViewModel.class)
    abstract ViewModel bindSearchLocationViewModel(SearchLocationViewModel searchLocationViewModel);
}
