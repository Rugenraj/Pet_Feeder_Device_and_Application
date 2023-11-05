package com.example.petfeederfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        //signup button

        MaterialButton signupbtn = (MaterialButton) findViewById(R.id.signupbtn);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        //login button

        TextView username = (TextView) findViewById(R.id.username);
        TextView passcode = (TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        //login

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
                {
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainActivity.this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();*/

                String email = username.getText().toString().trim();
                String password = passcode.getText().toString().trim();
                if (email.isEmpty()) {
                    username.setError("Email is empty");
                    username.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    username.setError("Enter the valid email");
                    username.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passcode.setError("Password is empty");
                    passcode.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    passcode.setError("Length of password is more than 6");
                    passcode.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, Homepage.class));
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Please Check Your login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}