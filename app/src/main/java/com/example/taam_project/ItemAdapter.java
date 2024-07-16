package com.example.taam_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private Context context;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    public void setContext(Context context) {
        this.context = context;
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
        holder.lotNumber.setText(String.valueOf(item.getLotNumber()));
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.period.setText(item.getPeriod());
        holder.description.setText(item.getDescription());
        holder.media.setText(item.getMedia());
        holder.remove.setOnClickListener(view -> {
            FirebaseDatabase db = FirebaseDatabase.getInstance("https://cscb07-taam-default-rtdb.firebaseio.com/");
            DatabaseReference ref = db.getReference("test");
            Query query = ref.orderByChild("lotNumber").equalTo(item.getLotNumber());

            // TODO add error checking to this
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 0;
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Toast.makeText(context, ds.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // communicate error
                }
            });
        });
    }

    public void setItems(List<Item> items) { this.items = items; }
    public List<Item> getItems() { return items; }

    @Override
    public int getItemCount() { return items.size(); }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView lotNumber, name, category, period, description, media;
        Button remove;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lotNumber = itemView.findViewById(R.id.textViewLotNumber);
            name = itemView.findViewById(R.id.textViewName);
            category = itemView.findViewById(R.id.textViewCategory);
            period = itemView.findViewById(R.id.textViewPeriod);
            description = itemView.findViewById(R.id.textViewDescription);
            media = itemView.findViewById(R.id.textViewMedia);
            remove = itemView.findViewById(R.id.remove);
        }
    }

}