package com.example.crimewatch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewUserReporting extends AppCompatActivity implements OnMapReadyCallback {

    private EditText editTextDate, editTextTime, editTextIncident;
    private TextView editTextLocation;
    private ImageView imageViewMedia;
    private Calendar calendar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final String TAG = "NewUserReporting";
    private boolean photoTaken = false;
    private SupportMapFragment mapFragment;
    private GoogleMap gMap;
    double pinnedLatitude;
    double pinnedLongitude;
    private Marker currentMarker;
     Button exitMapButton;
    FirebaseFirestore fStore;
    private StorageReference storageRef;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
        setContentView(R.layout.activity_new_user_reporting);

        calendar = Calendar.getInstance();

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextIncident = findViewById(R.id.editTextIncident);
        imageViewMedia = findViewById(R.id.imageViewMedia);
        exitMapButton = findViewById(R.id.exitMapButton);
        EditText editWitnessTag = findViewById(R.id.editTextTagUsers);
        FrameLayout fl = findViewById(R.id.frame_layout);
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        fStore = FirebaseFirestore.getInstance();
        btnTakePhoto.setOnClickListener(v -> launchCamera());
        storageRef = FirebaseStorage.getInstance().getReference();
        Button btnUploadGallery = findViewById(R.id.btnUploadGallery);
        btnUploadGallery.setOnClickListener(v -> uploadFromGallery());
        editTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    fl.setVisibility(View.VISIBLE);
                    mapFragment.getMapAsync(NewUserReporting.this); // Get the map within the listener

                }

                exitMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fl.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        updateSubmitButtonVisibility();

        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnSubmitReport.setEnabled(false);
        btnSubmitReport.setOnClickListener(v -> {
            if (isInformationComplete()) {

                String dateString = editTextDate.getText().toString();
                String timeString = editTextTime.getText().toString();
                String location = editTextLocation.getText().toString();
                String desc = editTextIncident.getText().toString();
                String witness = editWitnessTag.getText().toString();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                try {
                    // Parse date and time strings into Date objects
                    String combinedDateTime = dateString + " " + timeString;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Date parsedDateTime = sdf.parse(combinedDateTime);

                    // Create Timestamp object
                    Timestamp timestamp = new Timestamp(parsedDateTime);

                    // Create report map with Timestamp, location, and desc
                    Map<String, Object> report = new HashMap<>();
                    report.put("user", currentUser.getUid());
                    report.put("timestamp", timestamp);
                    report.put("location", location);
                    report.put("desc", desc);
                    report.put("witness tag", witness);
                    GeoPoint geoPoint = new GeoPoint(pinnedLatitude, pinnedLongitude);
                    report.put("location", geoPoint);
                    report.put("status", "Pending");

                    fStore.collection("report")
                            .add(report)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    String documentID = documentReference.getId();
                                    Toast.makeText(NewUserReporting.this, "Report successfully sent!", Toast.LENGTH_SHORT).show();
                                    uploadImage(selectedImageUri,documentID, documentReference);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {

                                    Toast.makeText(NewUserReporting.this, "Report failed to send.", Toast.LENGTH_SHORT).show();


                                }
                            });
                }catch (ParseException e) {
                    // Handle parsing errors
                    e.printStackTrace();
                    Toast.makeText(NewUserReporting.this, "Error parsing date or time", Toast.LENGTH_SHORT).show();
                }
            }
             else {
                showToast("Please fill in all required information.");
            }
        });
    }

    private void uploadFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "Request Code: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) {
                // Handle the picked image
                handlePickedImage(data);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle the captured image
                handleCapturedImage(data);
            }
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateEditText();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        updateTimeEditText();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
        );

        timePickerDialog.show();
    }

    private void updateDateEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        editTextTime.setText(sdf.format(calendar.getTime()));
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            showToast("No camera app found on the device.");
        }
    }

    private void handlePickedImage(Intent data) {
        if (data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewMedia.setImageURI(selectedImageUri);
            imageViewMedia.setVisibility(View.VISIBLE);
            photoTaken = true;
            updateSubmitButtonVisibility();

        }
    }

    private void handleCapturedImage(Intent data) {
        if (data != null && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            if (extras.containsKey("data")) {
                // Image captured successfully
                Bitmap photo = (Bitmap) extras.get("data");
                imageViewMedia.setImageBitmap(photo);
                imageViewMedia.setVisibility(View.VISIBLE);
                photoTaken = true;
                updateSubmitButtonVisibility();
                showToast("Image captured!");
            } else {
                showToast("Failed to capture image. Extras does not contain data.");
            }
        } else {
            showToast("Failed to capture image. Data is null.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isInformationComplete() {
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String location = editTextLocation.getText().toString();
        String incident = editTextIncident.getText().toString();

        return !date.isEmpty() && !time.isEmpty() && !location.isEmpty() && !incident.isEmpty();
    }

    private void updateSubmitButtonVisibility() {
        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnSubmitReport.setEnabled(photoTaken && isInformationComplete());
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

                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); // Use device's locale
                    List<Address> addresses = geocoder.getFromLocation(pinnedLatitude, pinnedLongitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String locationName = "";
                        if (address.getThoroughfare() != null) {
                            locationName += address.getThoroughfare() + ", ";
                        }
                        if (address.getSubLocality() != null) {
                            locationName += address.getSubLocality() + ", ";
                        }
                        String state = address.getAdminArea();
                        String country = address.getCountryName();
                        if (state != null) {
                            locationName += state + ", ";
                        }
                        if (country != null) {
                            locationName += country;
                        }
                        locationName = locationName.trim(); // Remove trailing comma
                        editTextLocation.setText(locationName);
                    } else {
                        Log.d("ReportAdapter", "Geocoder returned no addresses"); // Log for debugging
                        editTextLocation.setText("Unknown Location");
                    }
                } catch (IOException e) {
                    Log.e("ReportAdapter", "Error retrieving location: " + e.getMessage()); // Log for debugging
                    editTextLocation.setText("Error retrieving location");
                }
            }


        });
    }

    private void uploadImage(Uri imageUri, String documentID, DocumentReference documentReference) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            if (imageUri != null) {
                // Create a reference to the image file in Firebase Storage
                StorageReference imageRef = storageRef.child(currentUser.getUid() + "/" + documentID + ".jpg");

                // Upload the image
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(NewUserReporting.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                // Get the download URL of the uploaded image
                                Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                                downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        // Get the DocumentReference of the report
                                        DocumentReference reportRef = documentReference;

                                                // Update the report data with the image URL
                                                reportRef.update("imageUrl", downloadUri.toString())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Image URL stored successfully
                                                                Log.d("NewUserReporting", "Image URL stored in report");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Handle error storing image URL
                                                                Log.e("NewUserReporting", "Error storing image URL", e);
                                                            }
                                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewUserReporting.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                                Log.d("NewUserReporting", "Image upload failed");
                            }
                        });
            }
         else {
            Toast.makeText(NewUserReporting.this, "No image selected.", Toast.LENGTH_SHORT).show();
        }}
        else {
            // Handle the case where the user is not signed in
            Toast.makeText(NewUserReporting.this, "User is not signed in.", Toast.LENGTH_SHORT).show();
        }
    }

}