package com.example.taam_project;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {
    private Button addFragmentButton;
    private Button reportFragmentButton;
    private Button adminFragmentButton;

    // For basic functionality of admin.
    private TextView loginStatusTextView;
    private SearchView searchView;
    private RecyclerViewFragment recyclerViewFragment;
    private Datastore datastore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        datastore = Datastore.getInstance();
        recyclerViewFragment = new RecyclerViewFragment();

        addFragmentButton = view.findViewById(R.id.addFragmentButton);
        reportFragmentButton = view.findViewById(R.id.reportFragmentButton);
        adminFragmentButton = view.findViewById(R.id.adminFragmentButton);
        loginStatusTextView = view.findViewById(R.id.loginStatusTextView);
        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                datastore.search(s);
                return true;
            }
        });

        adminFragmentButton.setOnClickListener(v->{
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null && currentUser.isEmailVerified()) {
                AdminControlsFragment controlsFrag = new AdminControlsFragment(this);
                controlsFrag.show(getParentFragmentManager(), "AdminControlsFragment");
            }
            else {
                AdminLoginFragment loginFrag = new AdminLoginFragment(this);
                loginFrag.show(getParentFragmentManager(), "AdminLoginFragment");
            }
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

        loadRecyclerView();

        return view;
    }

    private void loadRecyclerView() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.recyclerViewContainer, recyclerViewFragment);
        transaction.commit();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                .setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void setAdminState() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            // User is signed in.
            String email = currentUser.getEmail();
            addFragmentButton.setVisibility(View.VISIBLE);
            reportFragmentButton.setVisibility(View.VISIBLE);
            loginStatusTextView.setText("Admin (" + email + ") is signed in");
        } else {
            // User is not signed in.
            addFragmentButton.setVisibility(GONE);
            reportFragmentButton.setVisibility(GONE);
            loginStatusTextView.setText("No admin signed in");
        }
    }

    public void onStart() {
        super.onStart();
        setAdminState();
    }
}