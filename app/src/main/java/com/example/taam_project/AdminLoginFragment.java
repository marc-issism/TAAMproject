package com.example.taam_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.util.Log;

public class AdminLoginFragment extends DialogFragment implements AdminLoginContract.View {
    // The login fragment is the actual screen, so it is the view in MVP.
    // This view will provide the definitions for the View functions specified in AdminLoginContract.View
    private static final String TAG = "AdminLoginFragment";
    private AdminLoginContract.Presenter mPresenter;
    private HomeFragment parentFragment;

    public AdminLoginFragment() {}

    public AdminLoginFragment(HomeFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        EditText emailEditText = view.findViewById(R.id.email_edit_text);
        EditText passwordEditText = view.findViewById(R.id.password_edit_text);
        Button loginButton = view.findViewById(R.id.logout_button);
        Button closeButton = view.findViewById(R.id.close_button);

        mPresenter = new AdminLoginPresenter(this);

        loginButton.setOnClickListener(v -> {
            String username = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                mPresenter.login(username, password);
            }
        });

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void showLoginSuccess() {
        Log.d(TAG, "signInWithEmail:success");
        Toast.makeText(getActivity(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
        parentFragment.setAdminState();
        dismiss();
    }

    @Override
    public void showLoginError(String errorMessage) {
        Log.w(TAG, "signInWithEmail:failure");
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailNotVerified() {
        Log.w(TAG, "Email not verified");
        Toast.makeText(getActivity(), "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}