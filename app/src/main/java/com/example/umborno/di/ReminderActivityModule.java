package com.example.umborno.di;

import androidx.lifecycle.ViewModel;

import com.example.umborno.ui.AddReminderFragment;
import com.example.umborno.ui.AlertFragment;
import com.example.umborno.ui.ReminderFragment;
import com.example.umborno.ui.RepeatFragment;
import com.example.umborno.ui.SearchFragment;
import com.example.umborno.viewmodel.ReminderViewModel;
import com.example.umborno.viewmodel.SearchLocationViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ReminderActivityModule {
    @ContributesAndroidInjector()
    abstract ReminderFragment contributeReminderFragment();

    @ContributesAndroidInjector()
    abstract AddReminderFragment contributeAddReminderFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract RepeatFragment contributeRepeatFragment();

    @ContributesAndroidInjector
    abstract AlertFragment contributeAlertFragment();

}
