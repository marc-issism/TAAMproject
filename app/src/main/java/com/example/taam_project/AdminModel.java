package com.example.taam_project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class AdminModel implements AdminContract.Model {
    // Firebase Authentication will handle all the database processes for us.
    public FirebaseAuth mAuth;

    AdminModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signInWithEmailAndPassword(String email, String password, AdminContract.View view) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                view.showLoginSuccess();

                            } else {
                                view.showEmailNotVerified();
                                mAuth.signOut();
                            }
                        }
                    } else {
                        view.showLoginError(task.getException().getMessage());

                    }
                }
            });
    }

    public void createAccountWithEmailAndPassword(String email, String password, AdminContract.View view) {
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
                                                view.showCreateAccountSuccess();
                                            } else {
                                                view.showVerificationEmailNotSent(task.getException());
                                            }
                                        }
                                    });
                        }
                    } else {
                        // Account creation failed
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            view.showAccountCollisionMessage(e);
                        }
                        else if (e != null) {
                            view.showCreateAccountFail(e);
                        }
                    }
                }
            });
    }


}
