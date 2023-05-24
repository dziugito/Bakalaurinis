package com.example.draudiminiai_ivykiai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText pastas, slaptazodis;
    Button prisijungti_mygtukas, registruotis_mygtukas;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pastas = (EditText) findViewById(R.id.pastasP);
        slaptazodis = (EditText) findViewById(R.id.slaptazodisP);
        prisijungti_mygtukas = (Button) findViewById(R.id.prisijungti_mygtukas);
        registruotis_mygtukas = (Button) findViewById(R.id.registruotis_mygtukas);

        prisijungti_mygtukas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = pastas.getText().toString();
                String pass = slaptazodis.getText().toString();

                if(pastas.equals("")||pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Prašome užpildyti visus laukelius", Toast.LENGTH_SHORT).show();
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(LoginActivity.this, "Blogas el.pašto formatas", Toast.LENGTH_SHORT).show();
                }
                else if (pass.length() < 6){
                    Toast.makeText(LoginActivity.this, "Slaptažodį turi sudaryti 6 simboliai", Toast.LENGTH_SHORT).show();
                }
                else {
                    loginUser(email, pass);
                    }
                }

            });

        registruotis_mygtukas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String pass) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Prisijungimas sėkmingas", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Blogi prisijungimo duomenys", Toast.LENGTH_SHORT).show();
                    try{
                        throw task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
