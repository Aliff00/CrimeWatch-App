package com.example.crimewatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        TextView userid = view.findViewById(R.id.textView3);
        Button buttonPriv = view.findViewById(R.id.privacy);
        Button logoutButton = view.findViewById(R.id.buttonLogOut);
        TextView nameTV = view.findViewById(R.id.textView2);
        Button changePass = view.findViewById(R.id.changepass);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Fetch the fullName from the 'users' collection in Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(currentUserId);
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String fullName = document.getString("fullName");
                            nameTV.setText("Hello "+fullName);
                            userid.setText("ID: "+ FirebaseAuth.getInstance().getCurrentUser().getUid());
                        } else {
                            // Handle the case where the user document doesn't exist
                            Log.w("TAG", "User document not found");
                            nameTV.setText("User data not found"); // Or any other appropriate message
                        }
                    } else {
                        // Handle the case where the data retrieval failed
                        Log.w("TAG", "Error fetching user data", task.getException());
                        nameTV.setText("Error fetching data"); // Or any other appropriate message
                    }
                }
            });
        } else {
            // Handle the case where there's no current user
            nameTV.setText("Not signed in"); // Or any other appropriate message
        }
        Button statusUpdate = view.findViewById(R.id.myReports);
        statusUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StatusUpdate.class);
                startActivity(intent);
            }
        });
        changePass.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        }));
        buttonPriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String privacyPolicyUrl = "https://doc-hosting.flycricket.io/crimewatch-privacy-policy/71be3708-53d0-48dd-acee-08d657d88f41/privacy";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl));
                startActivity(browserIntent);
            }
        });




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = ((MainActivity) getActivity()).getMyPreferences();
                preferences.edit().clear().apply();


                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut();

                // Navigate to login activity
                Intent intent = new Intent(getActivity().getApplicationContext(), loginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}