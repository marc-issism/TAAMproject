package com.example.taam_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database linking
        String dbURL = "https://cscb07-taam-default-rtdb.firebaseio.com/";
        db = FirebaseDatabase.getInstance(dbURL);
      //  DatabaseReference dbRef = db.getReference("IDK WHAT GOES HERE");
        //
       if(savedInstanceState == null){
            loadFragment(new HomeFragment());
       }
    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction frag =getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.fragment_container, fragment);
        frag.addToBackStack(null);
        frag.commit();

    }
}