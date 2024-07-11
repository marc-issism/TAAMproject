package com.example.taam_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

public class AddItem extends Fragment {
    private EditText lotNumber, name, description;
    private Spinner category, period;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        lotNumber = view.findViewById(R.id.EditLotNumber);
        name = view.findViewById(R.id.EditName);
        category = view.findViewById(R.id.EditCategory);
        period = view.findViewById(R.id.EditPeriod);
        description = view.findViewById(R.id.EditDescription);

        return view;
    }

}