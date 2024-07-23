package com.example.taam_project;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;


/*
The pop-up login screen.
 */
public class AdminLoginFragment extends DialogFragment {
    private static final String TAG = "AdminLoginFragment";
    private FirebaseAuth mAuth;
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
        Button loginButton = view.findViewById(R.id.login_button);
        Button createAccountButton = view.findViewById(R.id.create_account_button);
        Button closeButton = view.findViewById(R.id.close_button);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            String username = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                login(username, password);
            }
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

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                // Email is verified
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(getActivity(), "Successfully logged in.",
                                        Toast.LENGTH_SHORT).show();
                                parentFragment.setAdminState();
                                dismiss();
                            } else {
                                // Email is not verified
                                Log.w(TAG, "Email not verified");
                                Toast.makeText(getActivity(), "Please verify your email before logging in.",
                                        Toast.LENGTH_SHORT).show();
                                mAuth.signOut(); // Sign out the user
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed." + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                        if (exception != null) {
                            Log.w(TAG, "createUserWithEmail:failure", exception);
                            Toast.makeText(getActivity(), "Authentication failed. " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    private void displayUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Log.d(TAG, "displayUserInfo: " + name + " " + email);
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

// To check whether a user is already logged in:
//public void onStart() {
//    super.onStart();
//    // Check if user is signed in (non-null) and update UI accordingly.
//    FirebaseUser currentUser = mAuth.getCurrentUser();
//    if(currentUser != null){
//        reload(); // or do something else
//    }
//}