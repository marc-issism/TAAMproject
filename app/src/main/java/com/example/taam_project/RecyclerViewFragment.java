package com.example.taam_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewFragment extends Fragment {
    private ItemAdapter adapter;
    private List<Item> items, displayItems;
    private Datastore ds;

    public RecyclerViewFragment() {
        ds = Datastore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ItemAdapter(ds.getDisplayItems(), getContext(), getParentFragmentManager());
        ds.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        return view;
    }
}