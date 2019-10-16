package com.example.umborno.di;

import com.example.umborno.ui.AddReminderFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddReminderModule {

    @ContributesAndroidInjector()
    abstract AddReminderFragment bindAddReminderFragment();
}
