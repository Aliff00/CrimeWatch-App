package com.example.newmediaupload;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDate, editTextTime, editTextLocation, editTextIncident;
    private ImageView imageViewMedia;
    private Calendar calendar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final String TAG = "MainActivity";
    private boolean photoTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextIncident = findViewById(R.id.editTextIncident);
        imageViewMedia = findViewById(R.id.imageViewMedia);

        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(v -> launchCamera());

        Button btnUploadGallery = findViewById(R.id.btnUploadGallery);
        btnUploadGallery.setOnClickListener(v -> uploadFromGallery());

        updateSubmitButtonVisibility();

        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnSubmitReport.setEnabled(false);
        btnSubmitReport.setOnClickListener(v -> {
            if (isInformationComplete()) {
                showToast("Report submitted!");
            } else {
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
            Uri selectedImageUri = data.getData();
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
}
