package com.example.taam_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class AdminControlsFragment extends DialogFragment {
    private static final String TAG = "AdminLoginFragment";
    private FirebaseAuth mAuth;
    private HomeFragment parentFragment;

    public AdminControlsFragment() {}

    public AdminControlsFragment(HomeFragment parentFragment) {
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

        mAuth = FirebaseAuth.getInstance();

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
                createAccount(username, password);
            }
        });

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Account creation success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Send verification email
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Verification email sent.");
                                                    Toast.makeText(getActivity(), "Account successfully created. Please check your email to verify your account.",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e(TAG, "Failed to send verification email.", task.getException());
                                                    Toast.makeText(getActivity(), "Failed to send verification email.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Account creation failed
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthUserCollisionException) {
                                Log.w(TAG, "createUserWithEmail:failure", exception);
                                Toast.makeText(getActivity(), "Account already exists", Toast.LENGTH_SHORT).show();
                            }
                            else if (exception != null) {
                                Log.w(TAG, "createUserWithEmail:failure", exception);
                                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}