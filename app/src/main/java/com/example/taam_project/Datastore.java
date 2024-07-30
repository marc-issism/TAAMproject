package com.example.taam_project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Datastore {
    private final String url = "https://cscb07-taam-default-rtdb.firebaseio.com/";
    private final FirebaseDatabase db = FirebaseDatabase.getInstance(url);
    private final DatabaseReference ref = db.getReference("test");
    private static final Datastore instance = new Datastore();
    private ItemAdapter adapter;

    private List<Item> allItems, displayItems;
    private String currentQuery;

    private Datastore() {
        allItems = new ArrayList<>();
        displayItems = new ArrayList<>();
        currentQuery = "";

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allItems.clear();
                for (DataSnapshot ds: snapshot.getChildren())
                    allItems.add(ds.getValue(Item.class));
                syncWithDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        syncWithDatabase();
    }

    public static Datastore getInstance() {
        return instance;
    }

    public void syncWithDatabase() {
        if (!currentQuery.equals("")) return;
        displayItems.clear();
        displayItems.addAll(allItems);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private boolean matches(Item item) {
        String contents = item.getCategory()
                + " " + item.getLotNumber()
                + " " + item.getName()
                + " " + item.getDescription()
                + " " + item.getPeriod();

        return contents.toLowerCase().contains(currentQuery.toLowerCase());
    }

    public void search(String s) {
        currentQuery = s;
        displayItems.clear();
        for (Item item: allItems) {
            if (matches(item))
                displayItems.add(item);
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public List<Item> getDisplayItems() {
        return displayItems;
    }

    public void setAdapter(ItemAdapter adapter) {
        this.adapter = adapter;
    }
}
