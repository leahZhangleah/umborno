package com.example.umborno.ui;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.umborno.R;
import com.example.umborno.util.PreferenceHelper;
import com.example.umborno.viewmodel.LocationViewModel;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReminderFragment extends Fragment{
    private static final String TAG = "AddReminderFragment";
    private NavController navController;
    private RelativeLayout dateLayout,timeLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private String description,selectedLocation,date="",time="",alert;
    private EditText newReminderDescription;
    private TextView newReminderLocation,newReminderDate, newReminderTime, newReminderAlert;
    private int mYear, mMonth, mDay,mAmPm,mHour,mMinute,mDayOfWeek;

    @Inject
    public LocationViewModel locationViewModel;

    public AddReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        //for listening to the value change of location

        if(savedInstanceState!=null){
            description = savedInstanceState.getString(PreferenceHelper.NEW_REMINDER_DESCRIPTION_KEY);
            selectedLocation = savedInstanceState.getString(PreferenceHelper.NEW_REMINDER_LOCATION_KEY);
            mYear = savedInstanceState.getInt(PreferenceHelper.NEW_REMINDER_YEAR_KEY);
            mMonth = savedInstanceState.getInt(PreferenceHelper.NEW_REMINDER_MONTH_KEY);
            mDay = savedInstanceState.getInt(PreferenceHelper.NEW_REMINDER_YEAR_KEY);
            mHour = savedInstanceState.getInt(PreferenceHelper.NEW_REMINDER_HOUR_KEY);
            mMinute = savedInstanceState.getInt(PreferenceHelper.NEW_REMINDER_MINUTE_KEY);
            mDayOfWeek = savedInstanceState.getInt(PreferenceHelper.NEW_REMINDER_DAY_OF_WEEK_KEY);
            date = mYear + " "+ getMonthNameForInt(mMonth) + " "+ mDay;
            time = mHour + ":"+mMinute;
            alert = savedInstanceState.getString(PreferenceHelper.NEW_REMINDER_ALERT_KEY);
        }

        initViews(view);


    }

    private void initViews(View view) {
        newReminderDescription = view.findViewById(R.id.new_reminder_description);
        newReminderDate = view.findViewById(R.id.new_reminder_date);
        newReminderTime = view.findViewById(R.id.new_reminder_time);
        newReminderAlert = view.findViewById(R.id.new_reminder_alert);
        newReminderLocation = view.findViewById(R.id.get_location_tv);

        dateLayout = view.findViewById(R.id.date_layout);
        timeLayout = view.findViewById(R.id.time_layout);

        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);

        initPickers();

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timePicker.getVisibility()==View.VISIBLE){
                    timePicker.setVisibility(View.GONE);
                }
                if(datePicker.getVisibility() == View.GONE){
                    datePicker.setVisibility(View.VISIBLE);
                }else{
                    datePicker.setVisibility(View.GONE);
                }
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePicker.getVisibility()==View.VISIBLE){
                    datePicker.setVisibility(View.GONE);
                }
                if(timePicker.getVisibility() == View.GONE){
                    timePicker.setVisibility(View.VISIBLE);
                }else{
                    timePicker.setVisibility(View.GONE);
                }
            }
        });

        newReminderLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+navController.getGraph().getNavigatorName());
                navController.navigate(R.id.action_addReminderFragment_to_searchFragment);
            }
        });

        locationViewModel.getSelectedLocation().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selectedLocation = s;
                newReminderLocation.setText(selectedLocation);
            }
        });

        setValues();
    }

    private void initPickers() {
        Calendar mCalendar = Calendar.getInstance();
        if(date.equals("")){
            mYear = mCalendar.get(Calendar.YEAR);
            mMonth = mCalendar.get(Calendar.MONTH);
            mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            date = mYear + " "+ getMonthNameForInt(mMonth) + " "+ mDay;
        }
        datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                date = mYear + " "+ getMonthNameForInt(mMonth) + " "+ mDay;
                newReminderDate.setText(date);
            }
        });
        newReminderDate.setText(date);

        if(time.equals("")){
            mAmPm = mCalendar.get(Calendar.AM_PM);
            mHour = mCalendar.get(Calendar.HOUR);
            mMinute = mCalendar.get(Calendar.MINUTE);
            mDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
            time = getDayOfWeekNameForInt(mDayOfWeek) + " "+mHour + ":"+mMinute;
        }
        timePicker.setIs24HourView(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            timePicker.setHour(mHour);
            timePicker.setMinute(mMinute);
        }else{
            timePicker.setCurrentHour(mHour);
            timePicker.setCurrentMinute(mMinute);
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                time = getDayOfWeekNameForInt(mDayOfWeek) + " "+mHour + ":"+mMinute;
                newReminderTime.setText(time);
            }
        });

        newReminderTime.setText(time);
    }

    private String getMonthNameForInt(int m){
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if(m>=0&&m<=11){
            month = months[m];
        }
        return month;
    }

    private String getDayOfWeekNameForInt(int d){
        String day = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] daysOfWeek = dfs.getWeekdays();
        if(d>=1&&d<=7){
            day = daysOfWeek[d];
        }
        return day;
    }

    private void setValues() {
        newReminderDescription.setText(description);
        newReminderAlert.setText(alert);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PreferenceHelper.NEW_REMINDER_DESCRIPTION_KEY,description);
        outState.putString(PreferenceHelper.NEW_REMINDER_LOCATION_KEY,selectedLocation);
        outState.putInt(PreferenceHelper.NEW_REMINDER_YEAR_KEY,mYear);
        outState.putInt(PreferenceHelper.NEW_REMINDER_MONTH_KEY,mMonth);
        outState.putInt(PreferenceHelper.NEW_REMINDER_DAY_KEY,mDay);
        outState.putInt(PreferenceHelper.NEW_REMINDER_HOUR_KEY,mHour);
        outState.putInt(PreferenceHelper.NEW_REMINDER_MINUTE_KEY,mMinute);
        outState.putInt(PreferenceHelper.NEW_REMINDER_DAY_OF_WEEK_KEY,mDayOfWeek);
        outState.putString(PreferenceHelper.NEW_REMINDER_ALERT_KEY,alert);
    }
}
