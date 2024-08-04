package com.example.taam_project;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class AlertFragment extends DialogFragment {

    private static final String argText = "";
    private static final int fadeDelay = 1000; // Delay before fade-out starts

    public AlertFragment() {
    }

    public static AlertFragment newInstance(String text) {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();
        args.putString(argText, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        TextView textView = view.findViewById(R.id.textView);

        if (getArguments() != null) {
            String text = getArguments().getString(argText);
            textView.setText(text);
        }

        // Fade out fragment
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            view.animate()
                .alpha(0.0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(this::dismiss)
                .start();
        }, fadeDelay);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.1f);
        }
    }
}