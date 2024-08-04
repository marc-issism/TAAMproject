package com.example.taam_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    // View components
    private Button searchButton;
    private Spinner filterSpinner;
    private EditText searchTextInput;

    // Firebase
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private List<Item> results;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchButton = view.findViewById(R.id.searchButton);
        filterSpinner = view.findViewById(R.id.filterSpinner);
        searchTextInput = view.findViewById(R.id.searchTextInput);

        // Set filter spinner options
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter_arr, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        filterSpinner.setAdapter(filterAdapter);
        filterSpinner.setAdapter(filterAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { search(); }
        });


        return view;
    }

    public void search() {

        final String DIR = "https://cscb07-taam-default-rtdb.firebaseio.com/";
        db = FirebaseDatabase.getInstance(DIR);
        ref = db.getReference("test");

        results = new ArrayList<>();

        String filter = filterSpinner.getSelectedItem().toString();
        String query = searchTextInput.getText().toString();

        Log.d("SEARCH CRITERIA", filter + "|" + query);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                results.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Item item = ds.getValue(Item.class);
                    Log.d("ITEM PARSE", item.getName());

                    boolean isMatch = false;

                    if (filter.equals("All")) {
                        isMatch = containsQuery(item, query);
                    } else if (filter.equals("Lot Number")) {
                        isMatch = containsLotNumber(item, query);
                    } else if (filter.equals("Name")) {
                        isMatch = containsName(item, query);
                    } else if (filter.equals("Category")) {
                        isMatch = hasCategory(item, query);
                    } else if (filter.equals("Description")) {
                        isMatch = containsDescription(item, query);
                    } else {
                        isMatch = hasPeriod(item, query);
                    }

                    Log.d("SEARCH", String.valueOf(isMatch));
                    if (isMatch) {
                        results.add(item);
                    }

                }

                for (Item item : results) {
                    Log.d("SEARCH RESULT", item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SEARCH", "Search cancelled");
            }
        });

    }

    public static boolean containsQuery(Item item, String query) {

        if (containsName(item, query)) {
            return true;
        }
        if (containsDescription(item, query)) {
            return true;
        }
        if (hasCategory(item, query)) {
            return true;
        }
        if (hasPeriod(item, query)) {
            return true;
        }
        if (containsLotNumber(item, query)) {
            return true;
        }

        return false;
    }

    public static boolean containsLotNumber(@NonNull Item item, String lotNumber) {
        return item.getLotNumber().contains(lotNumber) || lotNumber.isEmpty();
    }

    public static boolean containsName(@NonNull Item item, String name) {
        return item.getName().contains(name) || name.isEmpty();
    }

    public static boolean hasCategory(@NonNull Item item, String category) {
        return item.getCategory().equals(category) || category.isEmpty();
    }

    public static boolean hasPeriod(@NonNull Item item, String period) {
        return item.getPeriod().equals(period) || period.isEmpty();
    }

    public static boolean containsDescription(@NonNull Item item, String description) {
        return item.getDescription().contains(description);
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}