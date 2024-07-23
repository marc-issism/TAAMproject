package com.example.taam_project;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private static String DIR = "https://cscb07-taam-default-rtdb.firebaseio.com/";

    // View components
    private EditText lotNumberInput;
    private EditText nameInput;
    private Spinner categorySpinner;
    private Spinner periodSpinner;
    private TextInputEditText descriptionInput;
    private Button searchButton;

    // Firebase
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private List<Item> results;

    // Default fields
    private String lotNumber;
    private String name;
    private String category;
    private String period;
    private String description;

    private RecyclerViewFragment recyclerView;

    public SearchFragment(RecyclerViewFragment recyclerView) {
        lotNumber = "";
        name = "";
        category = "Any";
        period = "Any";
        description = "";
        this.recyclerView = recyclerView;
        results = new ArrayList<>();
    }

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

        searchButton.setOnClickListener(view1 -> {
            search();
            recyclerView.updateItems(results);
        });

        return view;
    }

    public void fetchItems() {
        db = FirebaseDatabase.getInstance(DIR);
        ref = db.getReference("test");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                results.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Item item = ds.getValue(Item.class);
                    Log.d("ITEM PARSE", item.getName());

                    boolean isMatch = matchesQuery(item, lotNumber, name, category, period, description);
                    Log.d("SEARCH MATCH", String.valueOf(isMatch));
                    if (isMatch) {
                        results.add(item);
                    }
                }

                for (int i = 0; i < results.size(); i++) {
                    Log.d("SEARCH RESULT", results.get(i).getName());
                }

                recyclerView.updateItems(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SEARCH", "Search cancelled");
            }
        });
    }

    public void search() {
        resetSearchFields(); // Set all fields to default values of empty string

        lotNumber = lotNumberInput.getText().toString();
        name = nameInput.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        period = periodSpinner.getSelectedItem().toString();
        try {
            description = descriptionInput.getText().toString();
        } catch (Exception e) {
        }

        Log.d("SEARCH CRITERIA", lotNumber + "|" + name + "|" + category + "|" + period + "|" + description);

        fetchItems();
    }

    public static boolean matchesQuery(Item item, String lotNumber, String name, String category, String period, String description) {

        if (!lotNumber.isEmpty() && !item.getLotNumber().equals(lotNumber)) {

            return false;
        }
        if (!name.isEmpty() && !item.getName().equals(name)) {
            return false;
        }
        if (!category.equals("Any") && !item.getCategory().equals(category)) {
            return false;
        }
        if (!period.equals("Any") && !item.getPeriod().equals(period)) {
            return false;
        }
        if (!description.isEmpty() && !item.getDescription().contains(description)) {
            return false;
        }

        return true;
    }

    private void resetSearchFields() {

        lotNumber = "";
        name = "";
        category = "";
        period = "";
        description = "";

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}