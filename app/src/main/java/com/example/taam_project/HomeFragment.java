package com.example.taam_project;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment {

    private Button searchFragmentButton;
    private Button addFragmentButton;
    private Button reportFragmentButton;
    private Button loginFragmentButton;

    // For basic functionality of admin.
    private Button logoutButton;
    private TextView loginStatusTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchFragmentButton = view.findViewById(R.id.searchFragmentButton);
        addFragmentButton = view.findViewById(R.id.addFragmentButton);
        reportFragmentButton = view.findViewById(R.id.reportFragmentButton);
        loginFragmentButton = view.findViewById(R.id.loginFragmentButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        loginFragmentButton.setOnClickListener(v->{
            AdminLoginFragment loginFrag = new AdminLoginFragment(this);
            loginFrag.show(getParentFragmentManager(), "AdminLoginFragment");
        });

        searchFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new SearchFragment());}
        });

        // Please rename to AddFragment if possible for consistency
        addFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new AddItem());}
        });

        /* UNCOMMENT when Report Activity is converted to fragment
        reportFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ReportFragment());}
        });
        */

        Button ReportButton;
        ReportButton = reportFragmentButton;
        ReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(v -> {
            // Call the signOut method to log out the current user
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "User logged out", Toast.LENGTH_SHORT).show();
            displayWhosSignedIn();
        });

        // Simple check to see if a user is logged in.
        loginStatusTextView = view.findViewById(R.id.loginStatusTextView);

        loadRecyclerView();

        return view;
    }

    private void loadRecyclerView() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.recyclerViewContainer, new RecyclerViewFragment());
        transaction.commit();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void displayWhosSignedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            String email = currentUser.getEmail();
            loginStatusTextView.setText("User (" + email + ") is signed in");
        } else {
            // User is not signed in, display a different message or handle accordingly.
            loginStatusTextView.setText("No user is signed in");
        }
    }

    public void onStart() {
        super.onStart();
        displayWhosSignedIn();
    }
}