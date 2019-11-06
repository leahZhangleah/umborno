package com.example.umborno.ui;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.umborno.R;
import com.example.umborno.db.DbResponse;
import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.model.reminder_model.ReminderDate;
import com.example.umborno.schedule.CurrentWeatherWorker;
import com.example.umborno.util.PreferenceHelper;
import com.example.umborno.viewmodel.AlertViewModel;
import com.example.umborno.viewmodel.LocationViewModel;
import com.example.umborno.viewmodel.ReminderViewModel;
import com.example.umborno.viewmodel.RepeatViewModel;
import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReminderFragment extends Fragment implements View.OnClickListener,TextWatcher {
    private static final String TAG = "AddReminderFragment";
    private NavController navController;
    private RelativeLayout dateLayout,timeLayout,repeatLayout,alertLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText newReminderDescription;
    private TextView newReminderLocation,newReminderDate, newReminderTime, newReminderRepeat,newReminderAlert;
    private ProgressBar progressBar;
    private Reminder newReminder;
    private ReminderDate reminderDate;
    private LocationViewModel locationViewModel;
    private RepeatViewModel repeatViewModel;
    private AlertViewModel alertViewModel;
    private boolean addButtonEnabled;

    @Inject
    public WeatherViewModelProviderFactory factory;
    private ReminderViewModel reminderViewModel;

    public AddReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(addButtonEnabled);
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
        //todo, focus conflict with other views
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

        progressBar = view.findViewById(R.id.progress_bar);

        initPickers();
        initViewValues();
        initClickListeners();
        ViewModelProvider.Factory reminderFactory = new ViewModelProvider.NewInstanceFactory();
        locationViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),reminderFactory).get(LocationViewModel.class);
        locationViewModel.getSelectedLocation().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //newReminder.setLocationKey(s);
                //selectedLocation = s;
                newReminderLocation.setText(s);
            }
        });

        locationViewModel.getSelectedLocationKey().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                newReminder.setLocationKey(s);
            }
        });

        repeatViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),reminderFactory).get(RepeatViewModel.class);
        repeatViewModel.getRepeatMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                newReminder.setRepeat(s);
                newReminderRepeat.setText(s);
            }
        });

        alertViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),reminderFactory).get(AlertViewModel.class);
        alertViewModel.getAlertMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                newReminder.setAlert(s);
                newReminderAlert.setText(s);
            }
        });
        reminderViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.nav_graph),factory).get(ReminderViewModel.class);
        //reminderViewModel = ViewModelProviders.of().get(ReminderViewModel.class);
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
        newReminderDescription.addTextChangedListener(this);
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
        newReminder.setLocationKey(newReminderLocation.getText().toString());
        newReminder.setDateTime(reminderDate);
        newReminder.setAlert(newReminderAlert.getText().toString());

        Gson gson = new Gson();
        String json = gson.toJson(newReminder);
        outState.putString(PreferenceHelper.NEW_REMINDER_KEY,json.toString());
    }

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_add_reminder_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_a_new_reminder_btn:
                newReminder.setDescription(newReminderDescription.getText().toString());
                newReminder.setDateTime(reminderDate);
                reminderViewModel.addReminder(newReminder).observe(this, new Observer<DbResponse<Reminder>>() {
                    @Override
                    public void onChanged(DbResponse<Reminder> reminderDbResponse) {
                        if (reminderDbResponse.getResultCode()==DbResponse.LOADING_CODE){
                            progressBar.setVisibility(View.VISIBLE);
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            if(reminderDbResponse.getResultCode()==-1){ //error code returned from db
                                Toast.makeText(getContext(),"Reminder not saved. Something went wrong",Toast.LENGTH_SHORT).show();
                            }else{
                                createBackgroundTaskForNewReminder(reminderDbResponse.getBody());
                            }
                        }
                    }
                });

        }
        return super.onOptionsItemSelected(item);
    }

    private void createBackgroundTaskForNewReminder(Reminder reminder) {
        //todo: move this to a helper class
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data data = new Data.Builder()
                .putString(CurrentWeatherWorker.REMINDER_LOCATION_ID_KEY,reminder.getLocationKey())
                .build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CurrentWeatherWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(10,TimeUnit.SECONDS)
                .build();


        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                CurrentWeatherWorker.class, 2, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(getContext()).enqueue(periodicWorkRequest);
        Toast.makeText(getContext(),"Reminder saved successfully",Toast.LENGTH_SHORT).show();
        navController.popBackStack();


        //cancel work
        //WorkManager.getInstance(getContext()).cancelWorkById(workRequest.getId());


                                /*Calendar calendar = Calendar.getInstance();
                                Reminder reminder = reminderDbResponse.getBody();
                                ReminderDate reminderDate = reminder.getDateTime();
                                calendar.set(reminderDate.getYear(),reminderDate.getMonth(),reminderDate.getDay(),reminderDate.getHour(),reminderDate.getMinute());
                                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                                PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIntent);*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        newReminderDescription.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()!=0){
            addButtonEnabled = true;
        }else{
            addButtonEnabled = false;
        }
        setHasOptionsMenu(addButtonEnabled);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}
