package com.example.partonemadpm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView TVTitle;
    TextView TVDate;
    EditText ETDate;
    TextView TVTime;
    EditText ETTime;
    TextView TVLocation;
    EditText ETLocation;
    TextView TVDescription;
    EditText ETDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TVTitle=(TextView)findViewById(R.id.TVTitle);
        TVDate=(TextView)findViewById(R.id.TVDate);
        ETDate=(EditText)findViewById(R.id.ETDate);
        TVTime=(TextView)findViewById(R.id.TVTime);
        ETTime=(EditText)findViewById(R.id.ETTime);
        TVLocation=(TextView)findViewById(R.id.TVLocation);
        ETLocation=(EditText)findViewById(R.id.ETLocation);
        TVDescription=(TextView)findViewById(R.id.TVDescription);
        ETDescription=(EditText)findViewById(R.id.ETDescription);
    }
}
