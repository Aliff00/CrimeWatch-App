package com.example.assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class TestingReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_report);
        TextView tv = findViewById(R.id.textView2);
        FirebaseFirestore fStore;
        fStore = FirebaseFirestore.getInstance();
        CollectionReference reportsRef = fStore.collection("report");

        reportsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String formattedData = "";
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> reportData = document.getData();
                            // Extract and format the data, handling location as a GeoPoint
                            Timestamp timestamp = (Timestamp) reportData.get("timestamp");
                            String formattedTimestamp = timestamp.toDate().toString();

                            // Handle location as a GeoPoint
                            GeoPoint location = (GeoPoint) reportData.get("location");
                            String formattedLocation = (location != null) ? "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude() : "Location: Unavailable";
                            String desc = (String) reportData.get("desc");

                            formattedData += "Timestamp: " + formattedTimestamp + "\n"
                                    + formattedLocation + "\n"
                                    + "Description: " + desc + "\n\n";
                        }
                        tv.setText(formattedData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Log.w("TAG", "Error fetching reports: ", e);
                    }
                });



    }
}