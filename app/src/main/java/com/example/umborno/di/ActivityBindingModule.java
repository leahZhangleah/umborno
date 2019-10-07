package com.example.umborno.di;

import com.example.umborno.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {


    //This module is part of appcomponent's modules, main function is to add app's subcomponents into
    //injectorFactories of your DispatchingAndroidInjector (by using @IntoSet)

    //create a MainActivitySubcomponent and use MainActivityModule.class as it's corresponding module
    @ContributesAndroidInjector(modules ={MainActivityModule.class,ViewModelModule.class,LocationModule.class} )
    @PerActivity
    abstract MainActivity contributeMainActivity();


    //todo: if we have second activity
/*    @ContributesAndroidInjector(modules=SecondActivityModule.class)
      abstract SecondActivity contributeSecondActivity();*/

}
