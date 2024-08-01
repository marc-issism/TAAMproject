package com.example.taam_project;

import com.google.firebase.auth.FirebaseAuth;

public interface AdminContract {
    interface Model {
        FirebaseAuth mAuth = null;
        void signInWithEmailAndPassword(String email, String password, AdminContract.View view);
        void createAccountWithEmailAndPassword(String email, String password, View mView);
    }
    interface View {
        void showLoginSuccess();
        void showLoginError(String errorMessage);
        void showEmailNotVerified();
        void showCreateAccountSuccess();
        void showVerificationEmailNotSent(Exception e);
        void showAccountCollisionMessage(Exception e);
        void showCreateAccountFail(Exception e);
    }
    interface Presenter {
        void login(String email, String password);
        void createAccount(String username, String password);
    }
}