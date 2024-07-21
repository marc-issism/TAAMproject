package com.example.taam_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewFragment extends Fragment {
    private Item item;
    private TextView name, lot, category, dynasty, description;

    public ViewFragment(Item item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        name = view.findViewById(R.id.textViewName);
        lot = view.findViewById(R.id.textViewLotNumber);
        category = view.findViewById(R.id.textViewCategory);
        dynasty = view.findViewById(R.id.textViewPeriod);
        description = view.findViewById(R.id.textViewDescription);

        name.setText(item.getName());
        lot.setText(item.getLotNumber());
        category.setText(item.getCategory());
        dynasty.setText(item.getPeriod());
        description.setText(item.getDescription());

        return view;
    }
}