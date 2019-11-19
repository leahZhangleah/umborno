package com.example.umborno.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umborno.R;
import com.example.umborno.db.DbResponse;
import com.example.umborno.schedule.ReminderWorkerHelper;
import com.example.umborno.viewmodel.ReminderViewModel;
import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;
import com.example.umborno.http.Resource;
import com.example.umborno.http.Status;
import com.example.umborno.model.reminder_model.Reminder;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment{
    private static final String TAG = "ReminderFragment";
    NavController navController;
    @Inject
    public WeatherViewModelProviderFactory factory;
    private ReminderViewModel reminderViewModel;
    private RecyclerView reminderRv;
    private TextView emptyReminderView;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;
    private ProgressBar deleteProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        emptyReminderView = view.findViewById(R.id.empty_reminder);
        deleteProgressBar = view.findViewById(R.id.delete_progress_bar);
        reminderRv = view.findViewById(R.id.reminderRv);
        reminderAdapter = new ReminderAdapter();
        //enable swipe
        reminderAdapter.setmOnSwipeListener(new ReminderAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Log.d(TAG, "onDel: delete reminder");
                reminderViewModel.deleteReminder(reminderList.get(pos)).observe(getViewLifecycleOwner(), new Observer<DbResponse<Reminder>>() {
                    @Override
                    public void onChanged(DbResponse<Reminder> reminderDbResponse) {
                        if (reminderDbResponse.getResultCode()==DbResponse.LOADING_CODE){
                            deleteProgressBar.setVisibility(View.VISIBLE);
                        }else{
                            deleteProgressBar.setVisibility(View.INVISIBLE);
                            if(reminderDbResponse.getResultCode()==-1){ //error code returned from db
                                Toast.makeText(getContext(),"Reminder not deleted. Something went wrong",Toast.LENGTH_SHORT).show();
                            }else{
                                deleteBackgroundTaskForReminder(reminderDbResponse.getBody());
                            }
                        }
                    }
                });
            }

            @Override
            public void onEdit(int pos) {
                //todo
                Log.d(TAG, "onEdit: reminder");
                ReminderFragmentDirections.ActionReminderFragmentToAddReminderFragment action =
                        ReminderFragmentDirections.actionReminderFragmentToAddReminderFragment(reminderList.get(pos));
                navController.navigate(action);
            }
        });
        reminderRv.setAdapter(reminderAdapter);
        reminderViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),factory).get(ReminderViewModel.class);
        reminderViewModel.getReminderLiveData().observe(this, new Observer<Resource<List<Reminder>>>() {
            @Override
            public void onChanged(Resource<List<Reminder>> listResource) {
                if(listResource.getStatus()== Status.SUCCESS){
                    Log.d(TAG, "onChanged: reminders retrieved successfully");
                    List<Reminder> reminders = listResource.getData();
                    if(reminders==null||reminders.isEmpty()){
                        emptyReminderView.setVisibility(View.VISIBLE);
                    }else{
                        reminderList = reminders;
                        reminderAdapter.setReminderList(reminders);
                        emptyReminderView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });


    }

    private void deleteBackgroundTaskForReminder(Reminder reminder) {
        WorkManager.getInstance(getContext()).cancelAllWorkByTag(reminder.getLocationKey());
        Toast.makeText(getContext(),"Reminder deleted successfully",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_reminder_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: "+navController.getGraph().getNavigatorName());
        if(item.getItemId()==R.id.add_reminder_btn){
            Log.d(TAG, "onOptionsItemSelected: "+navController.getGraph().toString());
            navController.navigate(R.id.action_reminderFragment_to_addReminderFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
