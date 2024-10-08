package com.example.taam_project;

public class AdminPresenter implements AdminContract.Presenter {
    // Presenter is the middleman between the View and Model (Firebase Authentication)
    // Presenter will have the logic to handle the actual logging in.
    AdminContract.Model model;
    AdminContract.View view; // Compose the view inside the presenter.

    public AdminPresenter(AdminContract.View view, AdminContract.Model model) {
        this.view = view;
        this.model = model;
        // Note the presenter is constructed with both the view and model because it needs to connect them.
    }

    @Override
    public void login(String email, String password) {
        model.signInWithEmailAndPassword(email, password, view);
        // Ideally we control mView here in the presenter but the logic must be inside the model's method.
    }

    @Override
    public void createAccount(String email, String password) {
        model.createAccountWithEmailAndPassword(email, password, view);
    }

}
