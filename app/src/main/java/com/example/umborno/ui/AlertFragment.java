package com.example.umborno.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.umborno.R;
import com.example.umborno.viewmodel.AlertViewModel;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertFragment extends Fragment {
    private ListView alertLv;
    private NavController navController;

    private AlertViewModel alertViewModel;

    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        alertLv = view.findViewById(R.id.alert_lv);

        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();
        alertViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),factory).get(AlertViewModel.class);

        String[] alertValues = getResources().getStringArray(R.array.alert_options);
        //ListAdapter customAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_checked,repeatValues);
        CustomAdapter customAdapter = new CustomAdapter(getContext(),alertValues,0);
        alertLv.setAdapter(customAdapter);
        alertLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackground(getResources().getDrawable(R.drawable.pressed_layout_bg,null));
                customAdapter.setPosition(position);
                String mode = alertValues[position];
                alertViewModel.setAlertMode(mode);
                navController.popBackStack();
            }
        });

        alertViewModel.getAlertMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int selectPos = Arrays.asList(alertValues).indexOf(s);
                customAdapter.setPosition(selectPos);
            }
        });
    }
}
