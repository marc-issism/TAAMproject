package com.example.taam_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.BlurTransformation;


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
        holder.name.setText(item.getName());

        Glide.with(context)
            .load(item.getMedia())
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageView);

        Glide.with(context)
            .load(item.getMedia())
            .transform(new BlurTransformation(150))
            .into(holder.blurredImageView);

        holder.remove.setOnClickListener(view -> {
            RemovePopupFragment frag = new RemovePopupFragment(item, context);
            frag.show(parentFragmentManager, "remove_dialog_fragment");
        });

        // Make each item container pop-ups
        holder.itemView.setOnClickListener(view -> {
            ViewFragment viewFrag = new ViewFragment(item);
            viewFrag.show(parentFragmentManager, "view_dialog_fragment");
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        ImageView blurredImageView;
        Button remove;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textViewName);
            imageView = itemView.findViewById(R.id.imageView);
            blurredImageView = itemView.findViewById(R.id.blurredImageView);
            remove = itemView.findViewById(R.id.remove);

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.addAuthStateListener(firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified())
                    remove.setVisibility(View.VISIBLE);
                else remove.setVisibility(View.GONE);
            });
        }
    }
}