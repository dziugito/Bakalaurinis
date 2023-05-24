package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdministrationActivity extends AppCompatActivity {

    Button vartotojuValdymasB, autoValdymasB, daliuValdymasB, ivykiuB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);

        vartotojuValdymasB = (Button) findViewById(R.id.vartotoju_valdymas);
        autoValdymasB = (Button) findViewById(R.id.auto_valdymas);
        daliuValdymasB = findViewById(R.id.auto_dalys);
        ivykiuB = findViewById(R.id.eismo_įvykiai);

        vartotojuValdymasB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                startActivity(intent);
            }
        });

        autoValdymasB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AutoAdministrationActivity.class);
                startActivity(intent);
            }
        });

        daliuValdymasB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PartsActivity.class);
                startActivity(intent);
            }
        });

        ivykiuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventViewActivity.class);
                startActivity(intent);
            }
        });
    }
}