package com.example.assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class loginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declare user inputs
        mAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        EditText editEmail = findViewById(R.id.emailPrompt);
        EditText editPassword = findViewById(R.id.passwordPrompt);
        Button signInButton = findViewById(R.id.signInButton);
        TextView singInText = findViewById(R.id.createAccountClickable2);
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

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar2.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(loginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                                    Intent intent2= new Intent(getApplicationContext(), TestingReport.class);
                                    startActivity(intent2);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(loginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}
