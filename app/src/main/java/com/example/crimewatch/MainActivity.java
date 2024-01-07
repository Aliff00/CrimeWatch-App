package com.example.crimewatch;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

        FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "FirestoreListener";
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        boolean rememberMeChecked = preferences.getBoolean("rememberMe", false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new HomeFragment()).commit();

        }
        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if(id == R.id.Home){
                replaceFragment(new HomeFragment());
            } else if (id == R.id.Maps) {
                Intent intent = new Intent(getApplicationContext(), MapFragment.class);
                startActivity(intent);
            } else if (id == R.id.My) {
                replaceFragment(new MyFragment());
            } else {
                replaceFragment(new SupportFragment());
            }
            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ReportFragment reportFragment = new ReportFragment();
//                reportFragment.show(getSupportFragmentManager(), reportFragment.getTag());
                Intent intent = new Intent(getApplicationContext(), NewUserReporting.class); // Create an Intent for the ShortsActivity
                startActivity(intent); // Start the ShortsActivity
            }
        });
//        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFMain);
//        NavController navController = host.getNavController();
//        NavigationUI.setupWithNavController(bottomNavigationView,navController);
//
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller,
//                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                if(destination.getId() == R.id.SupportEmergency) {
//                    bottomNavigationView.setVisibility(View.GONE);
//                    bottomNavigationView.setVisibility(View.GONE);
//                    fab.setVisibility(View.GONE);
//                } else {
//                    bottomNavigationView.setVisibility(View.VISIBLE);
//                    bottomNavigationView.setVisibility(View.VISIBLE);
//                    fab.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        db = FirebaseFirestore.getInstance();
//
//        CollectionReference collectionReference = db.collection("report");
//
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error!=null){
//                    Log.w(TAG, "Listen failed.", error);
//                    return;
//                }
//
//                if (value != null && !value.isEmpty()) {
//                    for (DocumentChange dc : value.getDocumentChanges()) {
//                        if (dc.getType() == DocumentChange.Type.ADDED) {
//                            // Logic to handle the new document added
//                            Object messageData = dc.getDocument().getData().get("messageField"); // Replace with the actual field name
//                            String messageString;
//
//                            if (messageData instanceof String) {
//                                messageString = (String) messageData;
//                            } else if (messageData instanceof Number) {
//                                messageString = String.valueOf(messageData);
//                            } else {
//                                // Handle other data types or provide a fallback message
//                                messageString = "Unknown message data type";
//                            }
//                            // You can perform actions like showing an alert dialog here
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                            builder.setCancelable(true);
//                            builder.setMessage(messageString);
//                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                            builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    // go to maps
//                                }
//                            });
//                            if (!isDestroyed() && !isFinishing()) {
//                                builder.show(); // Show dialog only if activity is not destroyed or finishing
//                            }
//                        }
//                    }
//                } else {
//                    System.out.println("No new documents in the collection.");
//                }
//            }
//        });

    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NewUserReporting.class); // Create an Intent for the ShortsActivity
                startActivity(intent); // Start the ShortsActivity

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    public SharedPreferences getMyPreferences() {
        return getSharedPreferences("myPrefs", MODE_PRIVATE);
    }

//    private void showGreenZone() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle("Watch out ! New crimes reported.");
//        builder.setMessage("Location : Bangsar, KL"); // get location?
//        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // go to maps
//            }
//        });
//        builder.show();
//    }

}
