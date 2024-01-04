package com.example.crimewatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserReporting extends AppCompatActivity implements DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener,
        OnMapReadyCallback {
    TextView dateTextView,timeTextView;
    EditText ETDescription;
    Button sendButton,ETLocation,exitMapButton;
    FirebaseFirestore fStore;
    private SupportMapFragment mapFragment;
    private GoogleMap gMap;
    double pinnedLatitude;
    double pinnedLongitude;
    private Marker currentMarker;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
        setContentView(R.layout.activity_user_reporting);
        dateTextView = findViewById(R.id.ETDate2);
        timeTextView = findViewById(R.id.ETTime);
        ETLocation=findViewById(R.id.ETLocation);
        ETDescription=findViewById(R.id.ETDescription);
        sendButton = findViewById(R.id.sendReportButton);
        exitMapButton = findViewById(R.id.exitMapButton);
        FrameLayout fl = findViewById(R.id.frame_layout);
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
        datePickerFragment.setOnDateSelectedListener(this);
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
        timePickerFragment.setOnTimeSelectedListener(this);
        fStore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ETLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    fl.setVisibility(View.VISIBLE);
                    mapFragment.getMapAsync(UserReporting.this); // Get the map within the listener
                }

                exitMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fl.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        FirebaseUser user = auth.getCurrentUser();
                if (user==null){
                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                    startActivity(intent);
                    finish();
                }

        dateTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        }));

        timeTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        }));
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String dateString = dateTextView.getText().toString();
                String timeString = timeTextView.getText().toString();
                String location = ETLocation.getText().toString();
                String desc = ETDescription.getText().toString();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                try {
                    // Parse date and time strings into Date objects
                    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString);
                    Date time = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(timeString);

                    // Create Calendar object with combined date and time
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
                    calendar.set(Calendar.MINUTE, time.getMinutes());

                    // Create Timestamp object
                    Timestamp timestamp = new Timestamp(calendar.getTime());

                    // Create report map with Timestamp, location, and desc
                    Map<String, Object> report = new HashMap<>();
                    report.put("user", currentUser.getUid());
                    report.put("timestamp", timestamp);
                    report.put("location", location);
                    report.put("desc", desc);
                    GeoPoint geoPoint = new GeoPoint(pinnedLatitude, pinnedLongitude);
                    report.put("location", geoPoint);

                    fStore.collection("report")
                            .add(report)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(UserReporting.this, "Report successfully sent!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {

                                    Toast.makeText(UserReporting.this, "Report failed to send.", Toast.LENGTH_SHORT).show();


                                }
                            });
                }catch (ParseException e) {
                    // Handle parsing errors
                    e.printStackTrace();
                    Toast.makeText(UserReporting.this, "Error parsing date or time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onDateSelected(Date date)

    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        dateTextView.setText(formattedDate);
    }

    @Override
    public void onTimeSelected(String time) {
        timeTextView.setText(time);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        LatLng loc = new LatLng(3.1219964307870622, 101.6565650421024);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Pinned Location");
                currentMarker = gMap.addMarker(markerOptions);

                pinnedLatitude = latLng.latitude;
                pinnedLongitude = latLng.longitude;
            }


    });
}}