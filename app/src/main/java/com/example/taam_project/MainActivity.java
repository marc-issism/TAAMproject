package com.example.taam_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.FirebaseDatabase;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    Button ReportButton;

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database linking
        String dbURL = "";
        // db = FirebaseDatabase.getInstance(dbURL);
        // DatabaseReference dbRef = db.getReference("IDK WHAT GOES HERE");

       if(savedInstanceState == null) {
           loadFragment(new HomeFragment());
       }
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction frag =getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.fragment_container, fragment);
        frag.addToBackStack(null);
        frag.commit();

        /*teddy's code (will move to appropriate position later if required)*/
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ReportButton = findViewById(R.id.reportbutton);
        ReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        // Patrick - Admin Login Button
        findViewById(R.id.show_login_button).setOnClickListener(v-> {
            AdminLoginFragment loginFrag = new AdminLoginFragment();
            loginFrag.show(getSupportFragmentManager(), "AdminLoginFragment");
        });
    }
}