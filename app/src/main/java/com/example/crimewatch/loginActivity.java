package com.example.crimewatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar2;
    CheckBox rememberMeCheckBox;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        //Declare user inputs
        mAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        EditText editEmail = findViewById(R.id.emailPrompt);
        EditText editPassword = findViewById(R.id.passwordPrompt);
        Button signInButton = findViewById(R.id.signInButton);
        TextView singInText = findViewById(R.id.createAccountClickable2);
        CheckBox rememberMeCheckBox = findViewById(R.id.checkRemember);
        String savedEmail = preferences.getString("email", null);
        String savedPassword = preferences.getString("password", null);
        if (savedEmail != null && savedPassword != null) {
            loginUser(savedEmail,savedPassword,rememberMeCheckBox);
        }
        singInText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), registerActivity.class);
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
                    Toast.makeText(loginActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(loginActivity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email,password,rememberMeCheckBox);
            }
        });
    }

    private void loginUser(String email, String password,CheckBox rememberMeCheckBox) {
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar2.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(loginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
