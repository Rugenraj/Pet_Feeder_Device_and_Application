package com.example.petfeederfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        TextView username2 = (TextView) findViewById(R.id.username2);
        TextView password2 = (TextView) findViewById(R.id.password2);

        MaterialButton signbtn2 = (MaterialButton) findViewById(R.id.signbtn2);

        signbtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                String email = username2.getText().toString().trim();
                String password = password2.getText().toString().trim();

                if(email.isEmpty())
                {
                    username2.setError("Email is empty");
                    username2.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    username2.setError("Enter the valid email address");
                    username2.requestFocus();
                    return;
                }

               if(password.isEmpty())
                {
                    password2.setError("Enter the password");
                    password2.requestFocus();
                    return;
                }

               if(password.length()<6)
                {
                    password2.setError("Length of the password should be more than 6");
                    password2.requestFocus();
                    return;
                }

                //firebase
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "You are successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignUp.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
