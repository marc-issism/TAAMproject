package com.example.taam_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RemovePopupFragment extends DialogFragment {

    private Item item;
    private TextView remove_confirmation;
    private Button yes, no;
    private final Context context;

    public RemovePopupFragment(Item item, Context context) {
        this.item = item;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remove_popup, container, false);
        remove_confirmation = view.findViewById(R.id.textView);
        yes = view.findViewById(R.id.yesButton);
        no = view.findViewById(R.id.noButton);
        remove_confirmation.setText("Are you sure you want to delete them item with \nLot Number: " + item.getLotNumber() + "\nName: " + item.getName());
        yes.setOnClickListener(v -> {
            removeItem();
            dismiss();
        });

        no.setOnClickListener(v -> dismiss());

        return view;
    }

    public void removeItem() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://cscb07-taam-default-rtdb.firebaseio.com/");
        DatabaseReference ref = db.getReference("test");
        Query query = ref.orderByChild("lotNumber").equalTo(item.getLotNumber());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference sb = storage.getReference().child("ItemImages/");
        // TODO add error checking to this
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AlertFragment.newInstance("Deletion failed").show(((MainActivity) context).getSupportFragmentManager(), "alert_fragment");
            }
        });
        StorageReference media = sb.child(item.getLotNumber());

        if (item.getMedia().isEmpty()) {
            AlertFragment.newInstance("Successfully removed " + item.getName()).show(((MainActivity) context).getSupportFragmentManager(), "alert_fragment");
            return;
        }

        media.delete().addOnSuccessListener(unused -> {
            AlertFragment.newInstance("Successfully removed " + item.getName()).show(((MainActivity) context).getSupportFragmentManager(), "alert_fragment");
        }).addOnFailureListener(e -> {
            AlertFragment.newInstance("Deletion failed").show(((MainActivity) context).getSupportFragmentManager(), "alert_fragment");
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}