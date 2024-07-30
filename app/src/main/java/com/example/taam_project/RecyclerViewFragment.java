package com.example.taam_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment {
    private ItemAdapter adapter;
    private List<Item> items;
    private Spinner category;
    private Spinner period;
    private String catName;
    private String perName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        category = view.findViewById(R.id.category);
        period = view.findViewById(R.id.period);

        items = new ArrayList<>();
        adapter = new ItemAdapter(items, getContext(), getParentFragmentManager());
        recyclerView.setAdapter(adapter);
        DatabaseReference dbRef = FirebaseDatabase.getInstance("https://cscb07-taam-default-rtdb.firebaseio.com/").getReference("test");
        fetchItems(dbRef);

        ArrayAdapter<CharSequence> catAdap = ArrayAdapter.createFromResource(getContext(),
                R.array.category_arr, android.R.layout.simple_spinner_item);
        catAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(catAdap);
        ArrayAdapter<CharSequence> perAdap= ArrayAdapter.createFromResource(getContext(),
                R.array.period_arr, android.R.layout.simple_spinner_item);
        perAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period.setAdapter(perAdap);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catName = category.getSelectedItem().toString().toLowerCase();
                perName =  period.getSelectedItem().toString().toLowerCase();
                Toast.makeText(getContext(), "update items", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return view;
    }



    private void fetchItems(DatabaseReference dbRef) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    items.add(ds.getValue(Item.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCatName() {
        return catName;
    }

    public String getPerName(){
        return perName;
    }
}