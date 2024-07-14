package com.example.taam_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;


public class SearchFragment extends Fragment {

    private EditText lotNumberInput;
    private EditText nameInput;
    private Spinner categorySpinner;
    private Spinner periodSpinner;
    private TextInputEditText descriptionInput;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        lotNumberInput = view.findViewById(R.id.lotNumberInput);
        nameInput = view.findViewById(R.id.nameInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        periodSpinner = view.findViewById(R.id.periodSpinner);
        descriptionInput = view.findViewById(R.id.descriptionInput);
        searchButton = view.findViewById(R.id.searchButton);

        // Set category spinner options
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.category_arr, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter1);

        // Set period spinner options
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.period_arr, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter2);


        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}