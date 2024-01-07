package com.example.crimewatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class historyArchive extends AppCompatActivity  {
    private LocationManager locationManager;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private double deviceLatitude;
    private double deviceLongitude;
    private RecyclerView recyclerView;
    private List<Report> reports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_archive); // Using updated XML with RecyclerView
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch reports
        getDeviceLocation();
        fetchReports();
    }


    private void fetchReports() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            Log.d("TAG", "user is logged in");
            CollectionReference reportsRef = fStore.collection("report");
            Query query = reportsRef;

            query.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d("TAG", "success");
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Report report = document.toObject(Report.class);

                                // Check if the report is within 10 km from the device's location
                                if (isWithinDistance(report)) {
                                    reports.add(report);
                                    Log.d("TAG", "eeeeeeeeeeeeee");
                                }
                                else{
                                    Log.d("TAG", "noooooo");
                                }
                            }

                            // Set adapter for RecyclerView
                            ReportAdapter adapter = new ReportAdapter(historyArchive.this, reports);
                            recyclerView.setAdapter(adapter);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(historyArchive.this, "Error fetching reports", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error fetching reports: ", e);
                        }
                    });
        } else {
            Toast.makeText(this, "User is not signed in", Toast.LENGTH_SHORT).show();
            Log.w("TAG", "User is not signed in");
        }
    }

    // Helper method to check if a report is within the specified distance
    private boolean isWithinDistance(Report report) {
        // Replace these values with the actual latitude and longitude of the device
        double deviceLatitude = this.deviceLatitude; // Use the values from getDeviceLocation
        double deviceLongitude = this.deviceLongitude;

        GeoPoint reportLocation = report.getLocation();

        // Ensure the report has a valid GeoPoint
        if (reportLocation != null) {
            double reportLatitude = reportLocation.getLatitude();
            double reportLongitude = reportLocation.getLongitude();

            // Haversine formula to calculate distance
            double lat1 = Math.toRadians(deviceLatitude);
            double lon1 = Math.toRadians(deviceLongitude);
            double lat2 = Math.toRadians(reportLatitude);
            double lon2 = Math.toRadians(reportLongitude);

            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;

            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double distance = 6371 * c; // Radius of the Earth in kilometers

            return distance <= 10;
        }

        // If the report does not have a valid GeoPoint, consider it outside the distance
        return false;
    }

    private void getDeviceLocation() {
        Log.d("TAG", "Device Location: Lat=" + deviceLatitude + ", Lng=" + deviceLongitude);
        try {
            // Check for location permission
            if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Request last known location
                Log.d("TAG", "Device Location: Lat=" + deviceLatitude + ", Lng=" + deviceLongitude);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastLocation != null) {
                    deviceLatitude = lastLocation.getLatitude();
                    deviceLongitude = lastLocation.getLongitude();
                    Log.d("TAG", "Device Location: Lat=" + deviceLatitude + ", Lng=" + deviceLongitude);
                } else {
                    // Handle the case where last known location is not available
                    Log.w("TAG", "Last known location not available");
                    // Consider providing a fallback mechanism here
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.d("TAG", "Last known location not available");
        }
    }

}
