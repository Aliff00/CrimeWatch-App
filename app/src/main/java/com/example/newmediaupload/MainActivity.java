package com.example.newmediaupload;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDate, editTextTime, editTextLocation, editTextIncident;
    private ImageView imageViewMedia;
    private Calendar calendar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private Uri cameraImageUri;
    private ExecutorService cameraExecutor;
    private PreviewView viewFinder;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private ImageCapture imageCapture;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraExecutor = Executors.newSingleThreadExecutor();
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextIncident = findViewById(R.id.editTextIncident);
        imageViewMedia = findViewById(R.id.imageViewMedia);
        viewFinder = findViewById(R.id.previewView);

        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(v -> takePhoto());

        Button btnUploadGallery = findViewById(R.id.btnUploadGallery);
        btnUploadGallery.setOnClickListener(v -> uploadFromGallery());

        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnSubmitReport.setEnabled(false);
        btnSubmitReport.setOnClickListener(v -> {
            if (isInformationComplete()) {
                showToast("Report submitted!");
            } else {
                showToast("Please fill in all required information.");
            }
        });

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder().build();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Error getting camera provider: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the cameraExecutor when not needed
        cameraExecutor.shutdown();
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

    @SuppressLint("QueryPermissionsNeeded")
    private void takePhoto() {
        Log.d(TAG, "Button clicked");
        if (imageCapture == null) {
            return;
        }

        // Hide imageViewMedia initially
        imageViewMedia.setVisibility(View.GONE);

        File photoFile = new File(getBatchDirectoryName(), "IMG_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        runOnUiThread(() -> {
                            cameraImageUri = Uri.fromFile(photoFile);
                            // Make imageViewMedia visible after capturing the image
                            imageViewMedia.setVisibility(View.VISIBLE);
                            showToast("Image captured!");
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "Error capturing image: " + exception.getMessage());
                    }
                }
        );
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDir != null && !storageDir.exists() && !storageDir.mkdirs()) {
            throw new IOException("Failed to create storage directory.");
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
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
                handleCapturedImage();
            }
        }
    }

    private void handlePickedImage(Intent data) {
        if (data != null) {
            imageViewMedia.setImageURI(data.getData());
            imageViewMedia.setVisibility(View.VISIBLE);
        }
    }

    private void handleCapturedImage() {
        imageViewMedia.setImageURI(cameraImageUri);
        imageViewMedia.setVisibility(View.VISIBLE);

        scanMediaFile(cameraImageUri.getPath());

        showToast("Capture image functionality has been implemented!");
    }

    private void scanMediaFile(String path) {
        MediaScannerConnection.scanFile(
                this,
                new String[]{path},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // Do something after the media file is scanned (optional)
                    }
                }
        );
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
    private String getBatchDirectoryName() {
        return Environment.getExternalStorageDirectory()
                + File.separator + "DCIM"
                + File.separator + "Camera";
    }
}
