package com.example.assigment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare user inputs
        EditText userEmail = findViewById(R.id.emailPrompt);
        EditText userPassword = findViewById(R.id.passwordPrompt);
        Button signInButton = findViewById(R.id.signInButton);
        TextView displayFailedSignIn = findViewById(R.id.displayFailSignIn);

        //DUMMY INFO
        ArrayList<User> userDatabase = new ArrayList<>();
        User user1 = new User("123","123");
        userDatabase.add(user1); //user stored in database

        //Actions when user click the sign in button
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String usableEmail = userEmail.getText().toString();
                String usablePassword = userPassword.getText().toString();
                User newUser = new User(usableEmail,usablePassword);

                boolean signedIn = false;
                for (int i=0; i<userDatabase.size() ; i++){
                    if (userDatabase.get(i).email.equalsIgnoreCase(newUser.email)){
                        if (userDatabase.get(i).password.equals(newUser.password)){
                            signedIn = true;
                            break;
                        }
                    }
                }

                // Check the value of the signedIn variable before displaying a message
                if (signedIn){
                    displayFailedSignIn.setTextColor(Color.GREEN);
                    displayFailedSignIn.setText("log in success");
                } else {
                    displayFailedSignIn.setTextColor(Color.RED);
                    displayFailedSignIn.setText("log in failed");
                }
            }
        });
    }

}

class User{
    String email;
    String password;


    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
}
