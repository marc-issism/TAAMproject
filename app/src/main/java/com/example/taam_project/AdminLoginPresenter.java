package com.example.taam_project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoginPresenter implements AdminLoginContract.Presenter {
    // Presenter is the middleman between the View and Model (Firebase Authentication)
    // Presenter will have the logic to handle the actual logging in.
    private FirebaseAuth mAuth;
    private AdminLoginContract.View mView; // Compose the view inside the presenter.

    public AdminLoginPresenter(AdminLoginContract.View view) {
        this.mView = view;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                mView.showLoginSuccess();
                            } else {
                                mView.showEmailNotVerified();
                                mAuth.signOut();
                            }
                        }
                    } else {
                        mView.showLoginError(task.getException().getMessage());
                    }
                }
            });
    }
}
