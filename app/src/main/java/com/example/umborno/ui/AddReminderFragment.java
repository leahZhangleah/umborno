package com.example.umborno.ui;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
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
import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.model.reminder_model.ReminderDate;
import com.example.umborno.util.PreferenceHelper;
import com.example.umborno.viewmodel.LocationViewModel;
import com.example.umborno.viewmodel.RepeatViewModel;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReminderFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddReminderFragment";
    private NavController navController;
    private RelativeLayout dateLayout,timeLayout,repeatLayout,alertLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText newReminderDescription;
    private TextView newReminderLocation,newReminderDate, newReminderTime, newReminderRepeat,newReminderAlert;
    private Reminder newReminder;
    private ReminderDate reminderDate;


    public LocationViewModel locationViewModel;

    RepeatViewModel repeatViewModel;

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
            String json = savedInstanceState.getString(PreferenceHelper.NEW_REMINDER_KEY);
            if(json!=null&&!json.isEmpty()){
                Gson gson = new Gson();
                newReminder = gson.fromJson(json,Reminder.class);
                reminderDate = newReminder.getDateTime();
            }
        }else{
            newReminder = new Reminder();
        }

        initViews(view);

    }

    private void initViews(View view) {
        newReminderDescription = view.findViewById(R.id.new_reminder_description);
        newReminderDate = view.findViewById(R.id.new_reminder_date);
        newReminderTime = view.findViewById(R.id.new_reminder_time);
        newReminderRepeat = view.findViewById(R.id.new_reminder_repeat);
        newReminderAlert = view.findViewById(R.id.new_reminder_alert);
        newReminderLocation = view.findViewById(R.id.get_location_tv);

        dateLayout = view.findViewById(R.id.date_layout);
        timeLayout = view.findViewById(R.id.time_layout);
        repeatLayout = view.findViewById(R.id.repeat_layout);
        alertLayout = view.findViewById(R.id.alert_layout);

        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);

        initPickers();
        initViewValues();
        initClickListeners();
        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();
        locationViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),factory).get(LocationViewModel.class);
        locationViewModel.getSelectedLocation().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                newReminder.setLocation(s);
                //selectedLocation = s;
                newReminderLocation.setText(s);
            }
        });

        repeatViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),factory).get(RepeatViewModel.class);
        repeatViewModel.getRepeatMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                newReminder.setRepeat(s);
                newReminderRepeat.setText(s);
            }
        });

    }

    private void initClickListeners() {
        dateLayout.setOnClickListener(this);

        timeLayout.setOnClickListener(this);

        newReminderLocation.setOnClickListener(this);

        repeatLayout.setOnClickListener(this);

        alertLayout.setOnClickListener(this);
    }

    private void initViewValues() {
        newReminderDescription.setText(newReminder.getDescription());
        newReminderAlert.setText(newReminder.getAlert());
    }

    private void initPickers() {
        //todo, if this fragment is passed with an id from databse, it will get a saved reminder data
        if(reminderDate==null){
            reminderDate = new ReminderDate();
        }
        datePicker.init(reminderDate.getYear(), reminderDate.getMonth(), reminderDate.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                reminderDate.setYear(year);
                reminderDate.setMonth(monthOfYear);
                reminderDate.setDay(dayOfMonth);
                newReminderDate.setText(reminderDate.getDate());
            }
        });
        newReminderDate.setText(reminderDate.getDate());

        timePicker.setIs24HourView(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            timePicker.setHour(reminderDate.getHour());
            timePicker.setMinute(reminderDate.getMinute());
        }else{
            timePicker.setCurrentHour(reminderDate.getHour());
            timePicker.setCurrentMinute(reminderDate.getMinute());
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                reminderDate.setHour(hourOfDay);
                reminderDate.setMinute(minute);
                newReminderTime.setText(reminderDate.getTime());
            }
        });

        newReminderTime.setText(reminderDate.getTime());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        newReminder.setDescription(newReminderDescription.getText().toString());
        newReminder.setLocation(newReminderLocation.getText().toString());
        newReminder.setDateTime(reminderDate);
        newReminder.setAlert(newReminderAlert.getText().toString());

        Gson gson = new Gson();
        String json = gson.toJson(newReminder);
        outState.putString(PreferenceHelper.NEW_REMINDER_KEY,json.toString());
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        //todo, destroy locationviewmodel, redefine its scope to avoid memory leak
        locationViewModel.setSelectedLocation("");
        getViewModelStore().clear();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_layout:
                if(timePicker.getVisibility()==View.VISIBLE){
                    timePicker.setVisibility(View.GONE);
                }
                if(datePicker.getVisibility() == View.GONE){
                    datePicker.setVisibility(View.VISIBLE);
                }else{
                    datePicker.setVisibility(View.GONE);
                }
                break;
            case R.id.time_layout:
                if(datePicker.getVisibility()==View.VISIBLE){
                    datePicker.setVisibility(View.GONE);
                }
                if(timePicker.getVisibility() == View.GONE){
                    timePicker.setVisibility(View.VISIBLE);
                }else{
                    timePicker.setVisibility(View.GONE);
                }
                break;
            case R.id.get_location_tv:
                Log.d(TAG, "onclick: "+navController.getGraph().toString());
                navController.navigate(R.id.action_addReminderFragment_to_searchFragment);
                break;
            case R.id.repeat_layout:
                navController.navigate(R.id.action_addReminderFragment_to_repeatFragment);
                break;
            case R.id.alert_layout:
                navController.navigate(R.id.action_addReminderFragment_to_alertFragment);
                break;
        }
    }
}
