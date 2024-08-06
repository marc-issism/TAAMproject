package com.example.taam_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {
    //private TextView loginStatusTextView;
    private RecyclerViewFragment recyclerViewFragment;
    private static final Datastore datastore = Datastore.getInstance();

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewFragment = new RecyclerViewFragment();
        loadRecyclerView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_app_bar, menu);

        Datastore.SearchableField f = Datastore.SearchableField.ALL;

        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearchButton).getActionView();
        MenuItem add = menu.findItem(R.id.menuAddButton);
        MenuItem report = menu.findItem(R.id.menuReportButton);
        MenuItem admin = menu.findItem(R.id.menuAdminButton);
        MenuItem filter = menu.findItem(R.id.menuFilterButton);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("SEARCH", s);
                Log.d("FILTER", String.valueOf(datastore.getFilter()));
                datastore.search(s);
                return true;
            }
        });

        add.setOnMenuItemClickListener(menuItem -> {
            loadFragment(new AddItem());
            return true;
        });

        report.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(getActivity(), ReportActivity.class);
            startActivity(intent);
            return true;
        });

        admin.setOnMenuItemClickListener(menuItem -> {
            loadAdminFragment();
            return true;
        });

        filter.setOnMenuItemClickListener(menuItem -> {
            FilterFragment frag = new FilterFragment();
            frag.show(getParentFragmentManager(), "remove_filter_fragment");
            return true;
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null && user.isEmailVerified()) {
                String email = user.getEmail();
                report.setVisible(true);
                add.setVisible(true);
            } else {
                report.setVisible(false);
                add.setVisible(false);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadAdminFragment() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            AdminControlsView controlsFrag = new AdminControlsView(this);
            controlsFrag.show(getParentFragmentManager(), "AdminControlsView");
        } else {
            AdminView loginFrag = new AdminView(this);
            loginFrag.show(getParentFragmentManager(), "AdminView");
        }
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
            //loginStatusTextView.setText("Admin (" + email + ") is signed in");
        } else {
            // User is not signed in.
            //loginStatusTextView.setText("No admin signed in");
        }
    }

    public void onStart() {
        super.onStart();
        setAdminState();
    }
}