package com.example.umborno.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.umborno.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ReminderActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String TAG = "ReminderActivity";
    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private NavController navController;
    private Toolbar toolbar;
    private FrameLayout contentFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        setUpNavigation();
    }

   private void setUpNavigation() {
        toolbar = findViewById(R.id.reminder_toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this,R.id.nav_reminder_fragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
