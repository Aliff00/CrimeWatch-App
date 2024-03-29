package com.example.crimewatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupportFragment extends Fragment {

    public MainActivity main;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SupportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupportFragment newInstance(String param1, String param2) {
        SupportFragment fragment = new SupportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        Button feedboardButton = view.findViewById(R.id.feedboard);
        Button safetyTipsButton = view.findViewById(R.id.SafetyTipsBoard);
        Button emergencyHotlineButton = view.findViewById(R.id.EmergencyHelpline);
        main = (MainActivity) getActivity();
        feedboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with FeedboardFragment
                replaceFragment(new NewsFragment());
                main.bottomNavigationView.setVisibility(View.GONE);
                main.fab.setVisibility(View.GONE);
            }
        });

        safetyTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with SafetyTipsFragment
                replaceFragment(new SafetyTipsFragment());
                main.bottomNavigationView.setVisibility(View.GONE);
                main.fab.setVisibility(View.GONE);
            }
        });
        emergencyHotlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with EmergencyHotlineFragment
                replaceFragment(new EmergencyFragment());
                main.bottomNavigationView.setVisibility(View.GONE);
                main.fab.setVisibility(View.GONE);
            }
        });

        main.bottomNavigationView.setVisibility(View.VISIBLE);
        main.fab.setVisibility(View.VISIBLE);

        Button incidentHistory = view.findViewById(R.id.incidentHistory);
        incidentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), HistoryArchiveActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add transaction to the back stack
        fragmentTransaction.commit();
    }
}