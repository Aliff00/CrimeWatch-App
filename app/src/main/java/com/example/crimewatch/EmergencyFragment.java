package com.example.crimewatch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmergencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyFragment extends Fragment {

    private RecyclerView recyclerView;
    private EmergencyAdapter adapter;
    private FloatingActionButton fab;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmergencyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyFragment newInstance(String param1, String param2) {
        EmergencyFragment fragment = new EmergencyFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);
        recyclerView = view.findViewById(R.id.RVEmergency);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //contactlist
        List<ContactModel> dataList = new ArrayList<>();
        dataList.add(new ContactModel("One Stop Crisis Center (OSCC)","03 2615 3333",getResources().getString(R.string.OSCC)));

        dataList.add(new ContactModel("All Women Action Society Malaysia (AWAM)","03 7877 4221",getResources().getString(R.string.AWAM)));

        dataList.add(new ContactModel("Befrienders","03 7956 8144",getResources().getString(R.string.BF)));

        dataList.add(new ContactModel("Malaysian Mental Health Association","03 2780 6803",getResources().getString(R.string.HAWA)));

        dataList.add(new ContactModel("Talian NUR","15999",getResources().getString(R.string.NUR)));

        dataList.add(new ContactModel("Woman Aid Organisation (WAO)","03 7956 3488",getResources().getString(R.string.WAO)));
        adapter = new EmergencyAdapter(dataList);
        recyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting instance of Intent with action as ACTION_CALL
                Intent phone_intent = new Intent(Intent.ACTION_CALL);

                // Set data of Intent through Uri by parsing phone number
                phone_intent.setData(Uri.parse("tel:" + "999"));

                // start Intent
                startActivity(phone_intent);
            }
        });
    }
}