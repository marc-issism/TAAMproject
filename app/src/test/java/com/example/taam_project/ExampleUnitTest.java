package com.example.taam_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    AdminControlsView view;

    @Mock
    AdminModel model;

    @Test
    public void testAdmin(){

        AdminPresenter pres = new AdminPresenter(view, model);
        pres.login("patrickhuuu7@gmail.com", "123123");
        verify(view).showLoginSuccess();
    }
    public void testFail(){
        AdminPresenter pres = new AdminPresenter(view, model);
        pres.login("albert@gmail.com", "fw24242f");
        verify(view).showLoginSuccess();
    }
    public void testCreateLen(){
        AdminPresenter pres = new AdminPresenter(view, model);
        pres.createAccount("hello", "fw24242f");

        verify(view).showCreateAccountFail(null);
    }
}