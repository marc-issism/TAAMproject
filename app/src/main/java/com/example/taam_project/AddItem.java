package com.example.taam_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends Fragment {
    private EditText lotNumber, name, description;
    private Spinner category, period;
    private Button submit;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
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
        db = FirebaseDatabase.getInstance("https://cscb07-taam-default-rtdb.firebaseio.com/");
        ArrayAdapter<CharSequence> catAdap = ArrayAdapter.createFromResource(getContext(),
                R.array.category_arr, android.R.layout.simple_spinner_item);
        catAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(catAdap);
        ArrayAdapter<CharSequence> perAdap= ArrayAdapter.createFromResource(getContext(),
                R.array.period_arr, android.R.layout.simple_spinner_item);
        perAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period.setAdapter(perAdap);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        return view;
    }

    private void addItem(){
        int tmpLot = Integer.parseInt(lotNumber.getText().toString().trim());
        String tmpName = name.getText().toString().trim();
        String tmpCategory = category.getSelectedItem().toString().toLowerCase();
        String tmpPeriod = period.getSelectedItem().toString().toLowerCase();
        String tmpDisc = description.getText().toString().trim();

        if (tmpLot == 0 || tmpName.isEmpty() || tmpCategory.isEmpty() || tmpPeriod.isEmpty() || tmpDisc.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = db.getReference("categories/" + tmpCategory);
        String id = itemsRef.push().getKey();
        Item item = new Item(tmpLot, tmpName, tmpCategory, tmpPeriod, tmpDisc, "");

        itemsRef.child(id).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }
}