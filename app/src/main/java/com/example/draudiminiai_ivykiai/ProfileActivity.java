package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    Button registruotiAuto, vartotojoAuto, registruotiIvykiai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        registruotiAuto = (Button) findViewById(R.id.registruotiAuto);
        vartotojoAuto = (Button) findViewById(R.id.manoAuto);
        registruotiIvykiai = (Button) findViewById(R.id.manoIvykiai);

        registruotiAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserAutoRegistration.class);
                startActivity(intent);
            }
        });

        vartotojoAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserAutoActivity.class);
                startActivity(intent);
            }
        });

        registruotiIvykiai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventViewActivity.class);
                startActivity(intent);
            }
        });
    }
}