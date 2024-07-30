package com.example.taam_project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Datastore {
    private final String url = "https://cscb07-taam-default-rtdb.firebaseio.com/";
    private final FirebaseDatabase db = FirebaseDatabase.getInstance(url);
    private final DatabaseReference ref = db.getReference("test");
    private static final Datastore instance = new Datastore();
    private ItemAdapter adapter;

    private List<Item> allItems, displayItems;
    private String currentQuery;
    private HashMap<SearchableField, FieldPredicate> fieldPredicates;

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

        fieldPredicates = new HashMap<>();
        fieldPredicates.put(SearchableField.NAME,
                (item, query) -> item.getName().toLowerCase().contains(query));
        fieldPredicates.put(SearchableField.LOT,
                (item, query) -> item.getLotNumber().toLowerCase().equals(query));
        fieldPredicates.put(SearchableField.DESCRIPTION,
                (item, query) -> item.getDescription().toLowerCase().contains(query));
        fieldPredicates.put(SearchableField.PERIOD,
                (item, query) -> item.getPeriod().toLowerCase().equals(query));
        fieldPredicates.put(SearchableField.CATEGORY,
                (item, query) -> item.getCategory().toLowerCase().equals(query));
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
        boolean match = false;

        for (FieldPredicate predicate: fieldPredicates.values())
            match |= predicate.match(item, currentQuery);

        return match;
    }

    public void search(String s) {
        currentQuery = s.toLowerCase();
        displayItems.clear();
        for (Item item: allItems) {
            if (matches(item))
                displayItems.add(item);
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public List<Item> filterItems(SearchableField field, String query) {
        List<Item> filtered = new ArrayList<>();

        for (Item item: allItems)
            if (fieldPredicates.get(field).match(item, query.toLowerCase()))
                filtered.add(item);

        return filtered;
    }

    public List<Item> getDisplayItems() {
        return displayItems;
    }

    public void setAdapter(ItemAdapter adapter) {
        this.adapter = adapter;
    }

    public enum SearchableField {
        NAME, CATEGORY, PERIOD, DESCRIPTION, LOT
    }

    private interface FieldPredicate {
        boolean match(Item item, String query);
    }
}
