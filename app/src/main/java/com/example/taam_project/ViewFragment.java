package com.example.taam_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.fragment.app.DialogFragment;
import android.content.Context;

import com.bumptech.glide.Glide;

public class ViewFragment extends DialogFragment {

    private Item item;
    private TextView name, lot, category, dynasty, description;
    private ImageView media;

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
        media = view.findViewById(R.id.imageViewMedia);

        Context context = getContext();

        if (context != null) {

            name.setText(context.getString(R.string.item_name, item.getName()));
            lot.setText(context.getString(R.string.item_lot_number, item.getLotNumber()));
            category.setText(context.getString(R.string.item_category, item.getCategory()));
            dynasty.setText(context.getString(R.string.item_period, item.getPeriod()));
            description.setText(context.getString(R.string.item_description, item.getDescription()));

            Glide.with(context)
                    .load(item.getMedia())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(media);

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}