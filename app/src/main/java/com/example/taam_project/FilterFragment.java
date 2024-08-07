package com.example.taam_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

public class FilterFragment extends DialogFragment {
    private Datastore.SearchableField sf;
    private static final Datastore db = Datastore.getInstance();
    private EditText Lot;
    private EditText Name;
    private Spinner Category;
    private Spinner Period;
    private Button Submit;
    private Button Clear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment, container, false);

        Lot = view.findViewById(R.id.Lot);
        Name = view.findViewById(R.id.Name);
        Category = view.findViewById(R.id.Category);
        Period = view.findViewById(R.id.Period);
        Submit = view.findViewById(R.id.Submit);
        Clear = view.findViewById(R.id.Clear);


        ArrayAdapter<CharSequence> catAdap = ArrayAdapter.createFromResource(getContext(),
                R.array.category_arr, android.R.layout.simple_spinner_item);
        Category.setAdapter(catAdap);

        ArrayAdapter<CharSequence> perAdap= ArrayAdapter.createFromResource(getContext(),
                R.array.period_arr, android.R.layout.simple_spinner_item);
        perAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Period.setAdapter(perAdap);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
            }
        });
        return view;
    }

    public void Clear(){
        Lot.setText("");
        Name.setText("");
        Category.setSelection(0);
        Period.setSelection(0);
    }

    public void Submit(){
        String lot = Lot.getText().toString().trim();
        String name = Name.getText().toString().trim();
        String category = Category.getSelectedItem().toString().toLowerCase();
        String period = Period.getSelectedItem().toString().toLowerCase();
        //db.search(lot,name, category, period);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}