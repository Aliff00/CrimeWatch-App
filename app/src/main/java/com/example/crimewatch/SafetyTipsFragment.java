package com.example.crimewatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimewatch.adapter.SafetyTipsAdapter;
import com.example.crimewatch.data.SafetyTip;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SafetyTipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SafetyTipsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SafetyTipsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SafetyTipsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SafetyTipsFragment newInstance(String param1, String param2) {
        SafetyTipsFragment fragment = new SafetyTipsFragment();
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
        View view = inflater.inflate(R.layout.fragment_safety_tips, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSafetyTips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SafetyTipsAdapter(getSampleSafetyTips()));

        return view;
    }
    private List<SafetyTip> getSampleSafetyTips() {
        List<SafetyTip> safetyTips = new ArrayList<>();

        safetyTips.add(new SafetyTip("Be Aware of Your Surroundings:", "Stay alert and pay attention to your surroundings, especially in unfamiliar or poorly lit areas."));
        safetyTips.add(new SafetyTip("Trust Your Instincts", "If something feels wrong or makes you uncomfortable, trust your instincts and take appropriate action."));
        safetyTips.add(new SafetyTip("Personal Safety Devices", "Carry personal safety devices like whistles, alarms, or pepper spray, and know how to use them."));
        safetyTips.add(new SafetyTip("Self-Defense Training", "Consider taking self-defense classes to empower yourself with basic techniques."));
        safetyTips.add(new SafetyTip("Emergency Contacts", "Keep a list of emergency contacts in your phone and share them with family or friends."));
        safetyTips.add(new SafetyTip("Socializing Safety", "Let someone know your plans when meeting new people. \n Be cautious about leaving your drink unattended."));
        safetyTips.add(new SafetyTip("Emergency Contacts", "Keep a list of emergency contacts in your phone and share them with family or friends."));
        safetyTips.add(new SafetyTip("Emergency Contacts", "Keep a list of emergency contacts in your phone and share them with family or friends."));

        // Add more safety tips as needed
        return safetyTips;
    }
}