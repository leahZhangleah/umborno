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
import com.example.umborno.viewmodel.RepeatViewModel;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepeatFragment extends Fragment {
    private ListView repeatLv;
    private NavController navController;

    private RepeatViewModel repeatViewModel;

    public RepeatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repeat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        repeatLv = view.findViewById(R.id.repeat_lv);

        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();
        repeatViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),factory).get(RepeatViewModel.class);

        String[] repeatValues = getResources().getStringArray(R.array.repeat_options);
        //ListAdapter customAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_checked,repeatValues);
        CustomAdapter customAdapter = new CustomAdapter(getContext(),repeatValues,0);
        repeatLv.setAdapter(customAdapter);
        repeatLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackground(getResources().getDrawable(R.drawable.pressed_layout_bg,null));
                customAdapter.setPosition(position);
                String repeatMode = repeatValues[position];
                repeatViewModel.setRepeatMode(repeatMode);
                navController.popBackStack();
            }
        });

        repeatViewModel.getRepeatMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int selectPos = Arrays.asList(repeatValues).indexOf(s);
                customAdapter.setPosition(selectPos);
            }
        });

    }
}
