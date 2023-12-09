package com.example.assigment;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView editTextEmail, editTextPassword, singInText, editFullName, editUsername;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail =  findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        Button registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        singInText = findViewById(R.id.signInText);
        editFullName = findViewById(R.id.editFullName);
        editUsername = findViewById(R.id.editUsername);
        singInText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email , password, fullName, username;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                fullName = editFullName.getText().toString();
                username = editUsername.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(registerActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(registerActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("reports").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("fullName",fullName);
                                    user.put("username",username);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "OnSuccess: user profile is created for "+userID);
                                        }
                                    });
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(registerActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(registerActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}