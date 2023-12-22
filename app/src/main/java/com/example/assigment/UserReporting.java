package com.example.assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class UserReporting extends AppCompatActivity {
    TextView TVTitle,TVDate,TVTime,TVLocation,TVDescription;
    EditText ETDate,ETTime,ETLocation,ETDescription;
    Button sendButton;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reporting);
        TVTitle=findViewById(R.id.TVTitle);
        TVDate=findViewById(R.id.TVDate);
        ETDate=findViewById(R.id.ETDate);
        TVTime=findViewById(R.id.TVTime);
        ETTime=findViewById(R.id.ETTime);
        TVLocation=findViewById(R.id.TVLocation);
        ETLocation=findViewById(R.id.ETLocation);
        TVDescription=findViewById(R.id.TVDescription);
        ETDescription=findViewById(R.id.ETDescription);
        sendButton = findViewById(R.id.sendReportButton);
        fStore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button logoutbutton = findViewById(R.id.logoutbutton);
        FirebaseUser user = auth.getCurrentUser();
//                if (user==null){
//                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String date = ETDate.getText().toString();
                String time = ETTime.getText().toString();
                String location = ETLocation.getText().toString();
                String desc = ETDescription.getText().toString();

                Map<String, Object> report = new HashMap<>();
                report.put("date",date);
                report.put("time",time);
                report.put("location",location);
                report.put("desc",desc);

                fStore.collection("report")
                        .add(report)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(UserReporting.this,"Report successfully sent!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                                Toast.makeText(UserReporting.this,"Report failed to send.",Toast.LENGTH_SHORT).show();


                            }
                        });

                //temp log out


            }
        });
    }
}