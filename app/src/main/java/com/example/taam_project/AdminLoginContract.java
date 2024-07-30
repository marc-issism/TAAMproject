package com.example.taam_project;

public interface AdminLoginContract {
    interface View {
        void showLoginSuccess();
        void showLoginError(String errorMessage);
        void showEmailNotVerified();
    }

    interface Presenter {
        void login(String email, String password);
    }
}