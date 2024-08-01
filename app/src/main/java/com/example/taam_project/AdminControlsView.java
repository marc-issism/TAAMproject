package com.example.taam_project;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminControlsView extends DialogFragment implements AdminContract.View {
    private static final String TAG = "AdminControlsView";
    private AdminContract.Presenter mPresenter;
    private HomeFragment parentFragment;
    public AdminControlsView() {}

    public AdminControlsView(HomeFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_controls, container, false);
        Button logoutButton = view.findViewById(R.id.logout_button);
        EditText emailEditText = view.findViewById(R.id.email_edit_text);
        EditText passwordEditText = view.findViewById(R.id.password_edit_text);
        Button createAccountButton = view.findViewById(R.id.create_account_button);
        Button closeButton = view.findViewById(R.id.close_button);
        mPresenter = new AdminPresenter(this, new AdminModel());

        logoutButton.setOnClickListener(v -> {
            // Call the signOut method to log out the current user
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "User logged out", Toast.LENGTH_SHORT).show();
            parentFragment.setAdminState();
            dismiss();
        });

        createAccountButton.setOnClickListener(v -> {
            String username = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                mPresenter.createAccount(username, password);
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
    public void showCreateAccountSuccess() {
        Log.d(TAG, "Verification email sent.");
        Toast.makeText(getActivity(), "Account successfully created. Please check your email to verify your account.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showVerificationEmailNotSent(Exception e) {
        Log.e(TAG, "Failed to send verification email.", e);
        Toast.makeText(getActivity(), "Failed to send verification email.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAccountCollisionMessage(Exception e) {
        Log.w(TAG, "createUserWithEmail:failure", e);
        Toast.makeText(getActivity(), "Account already exists", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCreateAccountFail(Exception e) {
        Log.w(TAG, "createUserWithEmail:failure", e);
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}