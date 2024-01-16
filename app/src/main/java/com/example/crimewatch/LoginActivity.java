package com.example.crimewatch;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar2;
    private SharedPreferences preferences;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        //Declare user inputs
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        EditText editEmail = findViewById(R.id.emailPrompt);
        EditText editPassword = findViewById(R.id.passwordPrompt);
        Button signInButton = findViewById(R.id.signInButton);
        TextView singInText = findViewById(R.id.createAccountClickable2);
        TextView forgetPass = findViewById(R.id.forgotPasswordClickable);
        CheckBox rememberMeCheckBox = findViewById(R.id.checkRemember);
        String savedEmail = preferences.getString("email", null);
        String savedPassword = preferences.getString("password", null);
        boolean rememberMe = preferences.getBoolean("rememberMe", false); // Default to false if not set
        if (rememberMe && savedEmail != null && savedPassword != null) {
            rememberMeCheckBox.setChecked(true);
            loginUser(savedEmail, savedPassword, rememberMeCheckBox); // Pass rememberMeCheckBox as true
        }

        if (!rememberMe && savedEmail != null && savedPassword != null) {
            loginUser(savedEmail,savedPassword,rememberMeCheckBox);
        }
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("NRIC", "030622050019");
        user.put("fullname", "muhammad iqbal danial bin firdaus");


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


        singInText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Actions when user click the sign in button
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                progressBar2.setVisibility(View.VISIBLE);
                String email, password;
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email,password,rememberMeCheckBox);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void loginUser(String email, String password,CheckBox rememberMeCheckBox) {
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar2.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent2);
                            finish();
                            if (rememberMeCheckBox.isChecked()) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.putBoolean("rememberMe", true);
                                editor.apply();
                            } else {
                                preferences.edit().clear().apply();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences = null; // Clear the reference when the activity is destroyed
    }


}
