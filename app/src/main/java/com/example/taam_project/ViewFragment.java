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
import android.content.DialogInterface;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class ViewFragment extends DialogFragment {

    private Item item;
    private TextView name, lot, category, dynasty, description;
    private ImageView blurredImageView;
    private ImageView imageView;
    private StyledPlayerView playerView;
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
        imageView = view.findViewById(R.id.imageView);
        blurredImageView = view.findViewById(R.id.blurredImageView);
        playerView = view.findViewById(R.id.playerView);

        if (context != null) {

            name.setText(item.getName());
            lot.setText(context.getString(R.string.item_lot_number, item.getLotNumber()));
            category.setText(context.getString(R.string.item_category, item.getCategory()));
            dynasty.setText(context.getString(R.string.item_period, item.getPeriod()));
            description.setText(item.getDescription().replace("\n", " "));

            if (item.getMediaType().startsWith("video")) {
                playerView.setVisibility(View.VISIBLE);
                player = new ExoPlayer.Builder(context).build();
                playerView.setPlayer(player);
                MediaItem media = MediaItem.fromUri(item.getMedia());
                player.setMediaItem(media);
                player.prepare();
                player.play();
            }
            else {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                    .load(item.getMedia())
                    .placeholder(R.drawable.media_not_found)
                    .into(imageView);

                Glide.with(context)
                    .load(item.getMedia())
                    .transform(new BlurTransformation(75))
                    .into(blurredImageView);
            }
        }

        closeButton.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}