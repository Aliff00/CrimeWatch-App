package com.example.crimewatch;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "CrimeWatch";
    private static final int RC_NOTIFICATION = 99;
    private static final String PREF_NAME = "ReportStatusPrefs";
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();

        // Request permission for notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, RC_NOTIFICATION);
        }

        createNotificationChannel();

        // Fetch updated status data from Firebase Firestore
        CollectionReference reportRef = db.collection("report");
        reportRef.whereEqualTo("user", currentUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null && !value.isEmpty()) {
                    for (QueryDocumentSnapshot document : value) {
                        // Check if the "status" field has changed
                        String currentStatus = document.getString("status");
                        String previousStatus = getPreviousStatus(getApplicationContext(), document.getId());
                        if (previousStatus == null || !previousStatus.equals(currentStatus)) {
                            // Status has changed, show notification
                            showNotification(currentStatus);
                            // Update the previous status
                            setPreviousStatus(getApplicationContext(), document.getId(), currentStatus);
                        }
                    }
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
        }

        replaceFragment(new HomeFragment());*/

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.Support) {
                replaceFragment(new SupportFragment());
            } else if (id == R.id.Maps) {
                Intent intent = new Intent(getApplicationContext(), MapFragment.class);
                startActivity(intent);
            } else if (id == R.id.My) {
                replaceFragment(new MyFragment());
            } else {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class); // Create an Intent for the ReportActivity
                startActivity(intent); // Start the ReportActivity
            }
        });

    }

    // Create notification channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Crime";
            String description = "Report status update";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // Create a notification channel with the given ID, name, and importance
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    // Get the previous status for a specific report ID
    public static String getPreviousStatus(Context context, String reportId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(reportId, null);
    }
    // Set the previous status for a specific report ID
    public static void setPreviousStatus(Context context, String reportId, String status) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(reportId, status);
        editor.apply();
    }
    // Show Notification for Report Status
    private void showNotification(String current) {
        Intent intent = new Intent(this, HistoryArchiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.emojione_bw_1f989)
                .setContentTitle("Report " + current)
                .setContentText("The status of your report has been updated.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.notify(0, builder.build());
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    public SharedPreferences getMyPreferences() {
        return getSharedPreferences("myPrefs", MODE_PRIVATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_NOTIFICATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission granted
            }
            else{
                // Permission denied, handle accordingly
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
