package com.example.taam_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.fragment.app.DialogFragment;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class ViewFragment extends DialogFragment {

    private Item item;
    private TextView name, lot, category, dynasty, description;
    private ImageView imageView;
    private PlayerView playerView;
    private ExoPlayer player;
    private Context context;

    public ViewFragment(Item item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        context = getContext();

        Button closeButton = view.findViewById(R.id.close_button);
        name = view.findViewById(R.id.textViewName);
        lot = view.findViewById(R.id.textViewLotNumber);
        category = view.findViewById(R.id.textViewCategory);
        dynasty = view.findViewById(R.id.textViewPeriod);
        description = view.findViewById(R.id.textViewDescription);
        imageView = view.findViewById(R.id.imageViewMedia);
        playerView = view.findViewById(R.id.playerView);

        if (context != null) {

            name.setText(context.getString(R.string.item_name, item.getName()));
            lot.setText(context.getString(R.string.item_lot_number, item.getLotNumber()));
            category.setText(context.getString(R.string.item_category, item.getCategory()));
            dynasty.setText(context.getString(R.string.item_period, item.getPeriod()));
            description.setText(context.getString(R.string.item_description, item.getDescription()));

//            if (media is an image) {
//                Glide.with(context)
//                    .load(item.getMedia())
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(imageView);
//            }
//            else {
//                ExoPlayer player = new ExoPlayer.Builder(context).build();
//                playerView.setPlayer(player);
//                MediaItem media = MediaItem.fromUri(item.getMedia());
//                player.setMediaItem(media);
//                player.prepare();
//                player.play();
//            }

            // Video player test
            player = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(player);
            MediaItem media = MediaItem.fromUri(item.getMedia());
            player.setMediaItem(media);
            player.prepare();
            player.play();

        }

        closeButton.setOnClickListener(v -> {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
            dismiss();
        });

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