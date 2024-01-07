package com.example.crimewatch.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CallHelper {

    private static final int REQUEST_CODE = 100;

    public static void callService(Context context, String phoneNumber) {
        if (isCallPermissionGranted(context)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        } else {
            requestCallPermission(context);
        }
    }

    private static boolean isCallPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestCallPermission(Context context) {
        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[]{android.Manifest.permission.CALL_PHONE},
                REQUEST_CODE
        );
    }
}
