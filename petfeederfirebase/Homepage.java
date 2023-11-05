package com.example.petfeederfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Homepage extends AppCompatActivity {

    //private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView textView_IRValue;
    private Button button_open_c1,button_open_c2,button_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        textView_IRValue = (TextView) findViewById(R.id.textView_IRValue);
        button_open_c1 = (Button) findViewById(R.id.button_open_c1) ;
        button_open_c2 = (Button) findViewById(R.id.button_open_c2);
        button_close = (Button) findViewById(R.id.button_close);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                String movementstatus = dataSnapshot.child("objstatus").getValue().toString();
                textView_IRValue.setText(movementstatus);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
            }
        });

        button_open_c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("pos").setValue(90);
            }
        });

        button_open_c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("pos").setValue(180);
            }
        });

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("pos").setValue(0);
            }
        });

    }

}


