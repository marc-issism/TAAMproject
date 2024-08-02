package com.example.taam_project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
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
    private Button yes, no;
    private final Context context;

    public RemovePopupFragment(Item item, Context context) {
        this.item = item;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remove_popup, container, false);
        yes = view.findViewById(R.id.yesButton);
        no = view.findViewById(R.id.noButton);

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
                    Item item = ds.getValue(Item.class);
                    Toast.makeText(context, "Removing " + item.getName(), Toast.LENGTH_SHORT).show();
                    ds.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Deletion failed ", Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference media = sb.child(item.getLotNumber());
        media.delete().addOnSuccessListener(unused -> {
            Toast.makeText(context, "Deletion successful ", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Could not delete media", Toast.LENGTH_SHORT).show();
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