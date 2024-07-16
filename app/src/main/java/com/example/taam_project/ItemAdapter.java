package com.example.taam_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> items;

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
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
        holder.lotNumber.setText("Lot #: " + item.getLotNumber());
        holder.name.setText("Name: " + item.getName());
        holder.category.setText("Category: " + item.getCategory());
        holder.period.setText("Period: " + item.getPeriod());
        holder.description.setText("Description: " + item.getDescription());
        holder.media.setText("Media: " + item.getMedia());
    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView lotNumber, name, category, period, description, media;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lotNumber = itemView.findViewById(R.id.textViewLotNumber);
            name = itemView.findViewById(R.id.textViewName);
            category = itemView.findViewById(R.id.textViewCategory);
            period = itemView.findViewById(R.id.textViewPeriod);
            description = itemView.findViewById(R.id.textViewDescription);
            media = itemView.findViewById(R.id.textViewMedia);
        }
    }

}