package com.example.taam_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database linking
        // String dbURL = "https://cscb07-taam-default-rtdb.firebaseio.com/";
        // db = FirebaseDatabase.getInstance(dbURL);
        // DatabaseReference dbRef = db.getReference("IDK WHAT GOES HERE");

       if(savedInstanceState == null){
            loadFragment(new AddItem());

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

        // Patrick - Admin Login Button
//        findViewById(R.id.show_login_button).setOnClickListener(v->{
//            AdminLoginFragment loginFrag = new AdminLoginFragment();
//            loginFrag.show(getSupportFragmentManager(), "AdminLoginFragment");
//        });
      

//        ReportButton = findViewById(R.id.reportbutton);
//        ReportButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}