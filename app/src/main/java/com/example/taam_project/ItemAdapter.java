package com.example.taam_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private Context context;
    private FragmentManager parentFragmentManager;

    public ItemAdapter(List<Item> items, Context context, FragmentManager fm) {
        this.items = items;
        this.context = context;
        this.parentFragmentManager = fm;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);

        // Will use resource strings instead of concatenation later
        // holder.lotNumber.setText("Lot #: " + item.getLotNumber());
        holder.name.setText("Name: " + item.getName());
        // holder.category.setText("Category: " + item.getCategory());
        // holder.period.setText("Period: " + item.getPeriod());
        // holder.description.setText("Description: " + item.getDescription());

        Glide.with(context)
            .load(item.getMedia())
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.media);

        holder.remove.setOnClickListener(view -> {
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
            media.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Deletion successful ", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Could not delete media", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Make each item container pop-ups
        holder.itemView.setOnClickListener(view -> {
            ViewFragment viewFrag = new ViewFragment(item);
            viewFrag.show(parentFragmentManager, "view_dialog_fragment");
        });

//        holder.view.setOnClickListener(view -> {
//            loadFragment(new ViewFragment(item));
//        });

    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView lotNumber, name, category, period, description;
        ImageView media;
        Button remove, view;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // lotNumber = itemView.findViewById(R.id.textViewLotNumber);
            name = itemView.findViewById(R.id.textViewName);
            // category = itemView.findViewById(R.id.textViewCategory);
            // period = itemView.findViewById(R.id.textViewPeriod);
            // description = itemView.findViewById(R.id.textViewDescription);
            media = itemView.findViewById(R.id.imageViewMedia);
            remove = itemView.findViewById(R.id.remove);
//            view = itemView.findViewById(R.id.view);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = parentFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}