package com.example.taam_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        DatabaseReference dbRef = db.getReference("test");
        dbRef.child("oscar").setValue("pang");
        //
       //if(savedInstanceState == null){
       //     loadFragment(new HomeFragment());
      // }
    }
}