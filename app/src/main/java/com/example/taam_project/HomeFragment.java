package com.example.taam_project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {

    private Button viewFragmentButton;
    private Button searchFragmentButton;
    private Button addFragmentButton;
    private Button removeFragmentButton;
    private Button reportFragmentButton;
    private Button loginFragmentButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewFragmentButton = view.findViewById(R.id.viewFragmentButton);
        searchFragmentButton = view.findViewById(R.id.searchFragmentButton);
        addFragmentButton = view.findViewById(R.id.addFragmentButton);
        removeFragmentButton = view.findViewById(R.id.removeFragmentButton);
        reportFragmentButton = view.findViewById(R.id.reportFragmentButton);
        loginFragmentButton = view.findViewById(R.id.loginFragmentButton);

        loginFragmentButton.setOnClickListener(v->{
            AdminLoginFragment loginFrag = new AdminLoginFragment();
            loginFrag.show(getParentFragmentManager(), "AdminLoginFragment");
        });

        // Temporary (should load automatically when the app launches)
        viewFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new RecyclerViewFragment());}
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

        /* UNCOMMENT when Remove Fragment implemented
        removeFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new RemoveFragment());}
        });
        */

        /* UNCOMMENT when Report Activity is converted to fragment
        reportFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ReportFragment());}
        });
        */

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}