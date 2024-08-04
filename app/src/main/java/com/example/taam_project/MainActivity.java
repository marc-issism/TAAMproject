package com.example.taam_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("TAAM");

        if(savedInstanceState == null){
            loadFragment(new HomeFragment());
        }
    }

    private void loadFragment(Fragment fragment){
        Fragment animFrag = new Fragment();
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
    }
}