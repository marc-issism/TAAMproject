package com.example.taam_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    AdminControlsView view;

    @Mock
    AdminModel model;

    @Test
    public void testShowLoginSuccess() {
        AdminPresenter pres = new AdminPresenter(view, model);

        doAnswer(invocation -> {
            view.showLoginSuccess();
            return null;
        }).when(model).signInWithEmailAndPassword(
                "patrickhuuu7@gmail.com",
                "123123",
                view
        );

        pres.login("patrickhuuu7@gmail.com", "123123");
        verify(view).showLoginSuccess();
    }

    @Test
    public void testShowLoginError() {
        AdminPresenter pres = new AdminPresenter(view, model);

        doAnswer(invocation -> {
            view.showLoginError("placeholder");
            return null;
        }).when(model).signInWithEmailAndPassword(
                "albert@gmail.com",
                "fw24242f",
                view
        );

        pres.login("albert@gmail.com", "fw24242f");
        verify(view).showLoginError("placeholder");
    }
    @Test
    public void testLoginFail() {
        AdminPresenter pres = new AdminPresenter(view, model);

        doAnswer(invocation -> {
            view.showLoginError("error");
            return null;
        }).when(model).signInWithEmailAndPassword(
                "hello",
                "fw24242f",
                view
        );

        pres.login("hello", "fw24242f");
        verify(view).showLoginError("error");
    }


    @Test
    public void testShowEmailNotVerifiedOnLogin() {
        AdminPresenter pres = new AdminPresenter(view, model);

        doAnswer(invocation -> {
            view.showEmailNotVerified();
            return null;
        }).when(model).createAccountWithEmailAndPassword(
                "hello",
                "fw24242f",
                view
        );

        pres.login("hello", "fw24242f");
        verify(view).showEmailNotVerified();
    }

    @Test
    public void testShowCreateAccountSuccess() {
        AdminPresenter pres = new AdminPresenter(view, model);

        doAnswer(invocation -> {
            view.showCreateAccountSuccess();
            return null;
        }).when(model).createAccountWithEmailAndPassword(
                "hello@gmail.com",
                "fw24242f",
                view
        );

        pres.createAccount("hello@gmail.com", "fw24242f");
        verify(view).showCreateAccountSuccess();
    }

    @Test(expected=Exception.class)
    public void testShowVerificationEmailNotSent() throws Exception {
        AdminPresenter pres = new AdminPresenter(view, model);
        doThrow(new Exception()).when(model).createAccountWithEmailAndPassword(
                "hello@gmail.com",
                "123424",
                view
        );
        pres.createAccount("hello@gmail.com", "123424");
        verify(view).showVerificationEmailNotSent(new Exception());
    }

    @Test(expected=Exception.class)
    public void testShowAccountCollisionMessage() {
        AdminPresenter pres = new AdminPresenter(view, model);
        doThrow(new FirebaseAuthUserCollisionException("", "")).when(model).createAccountWithEmailAndPassword(
                "hello@gmail.com",
                "1242423",
                view
        );
        pres.createAccount("hello@gmail.com", "1242423");
        verify(view).showAccountCollisionMessage(new Exception());
    }

    @Test(expected=Exception.class)
    public void testShowCreateAccountFail() {
        AdminPresenter pres = new AdminPresenter(view, model);
        doThrow(new Exception()).when(model).createAccountWithEmailAndPassword(
                "hello@gmail.com",
                "123424",
                view
        );
        pres.createAccount("hello@gmail.com", "123424");
        verify(view).showCreateAccountFail(new Exception());
    }
}