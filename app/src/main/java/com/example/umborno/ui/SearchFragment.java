package com.example.umborno.ui;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.umborno.R;
import com.example.umborno.http.Resource;
import com.example.umborno.http.Status;
import com.example.umborno.model.location_search_model.SearchSuggestion;
import com.example.umborno.viewmodel.LocationViewModel;
import com.example.umborno.viewmodel.SearchLocationViewModel;
import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements TextWatcher {
    private static final String TAG = "SearchFragment";
    private Button cancelBtn;
    private EditText searchEt;
    private ListView locationSuggestionsLv;
    private List<String> suggestionsList;
    private List<String> suggestedLocationKeys;
    private NavController navController;
    @Inject
    public WeatherViewModelProviderFactory factory;
    private SearchLocationViewModel searchLocationViewModel;

    public LocationViewModel locationViewModel;

    //private AutoCompleteAdapter autoCompleteAdapter;
    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        ViewModelProvider.Factory locationFactory = new ViewModelProvider.NewInstanceFactory();
        locationViewModel = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.reminder_graph),locationFactory).get(LocationViewModel.class);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if cancelled, make current location the default option
                String current_location = "Current Location";
                locationViewModel.setSelectedLocation(current_location);
                //todo get current location key from sp
                //close fragment
                navController.popBackStack();

            }
        });

        locationSuggestionsLv = view.findViewById(R.id.location_suggestions_lv);
        locationSuggestionsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackground(getResources().getDrawable(R.drawable.pressed_layout_bg,null));
                String selectedLocation = suggestionsList.get(position);
                locationViewModel.setSelectedLocation(selectedLocation);
                locationViewModel.setSelectedLocationKey(suggestedLocationKeys.get(position));
                //getFragmentManager().popBackStack();
                Log.d(TAG, "onclick: "+navController.getGraph().toString());
                navController.popBackStack();

            }
        });

        searchLocationViewModel = ViewModelProviders.of(this,factory).get(SearchLocationViewModel.class);
        searchEt = view.findViewById(R.id.search_edit_text);
        searchEt.requestFocus();
        searchEt.addTextChangedListener(this);

        searchLocationViewModel.getLocationSuggestionsLiveData().observe(this, new Observer<List<SearchSuggestion>>() {
            @Override
            public void onChanged(List<SearchSuggestion> searchSuggestions) {
                suggestionsList = new ArrayList<>();
                suggestedLocationKeys = new ArrayList<>();
                if(searchSuggestions!=null&&!searchSuggestions.isEmpty()){
                    for(SearchSuggestion searchSuggestion:searchSuggestions){
                        String city = searchSuggestion.getLocalizedName();
                        String country = searchSuggestion.getCountry().getLocalizedName();
                        suggestionsList.add(city+" , "+country);
                        suggestedLocationKeys.add(searchSuggestion.getKey());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,suggestionsList);
                    locationSuggestionsLv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length()>=2){
            searchLocationViewModel.setUserInput(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchEt.removeTextChangedListener(this);
    }
}
