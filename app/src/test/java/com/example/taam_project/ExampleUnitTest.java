package com.example.taam_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    AdminControlsView view;

    @Mock
    AdminModel model;

    @Test
    public void testAdmin() {
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
    public void testFail() {
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
    public void testCreateLen() {
        AdminPresenter pres = new AdminPresenter(view, model);

        doAnswer(invocation -> {
            view.showCreateAccountSuccess();
            return null;
        }).when(model).createAccountWithEmailAndPassword(
                "hello",
                "fw24242f",
                view
        );

        pres.createAccount("hello", "fw24242f");
        verify(view).showCreateAccountSuccess();
    }
}