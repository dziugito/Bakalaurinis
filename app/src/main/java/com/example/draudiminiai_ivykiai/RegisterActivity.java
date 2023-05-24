package com.example.draudiminiai_ivykiai;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText vardas, pavarde, pastas, telefono_numeris, slaptazodis, pakartotas_slaptazodis;
    Button registruotisM;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        vardas = (EditText) findViewById(R.id.vardas);
        pavarde = (EditText) findViewById(R.id.pavarde);
        telefono_numeris = (EditText) findViewById(R.id.telefono_numeris);
        slaptazodis = (EditText) findViewById(R.id.slaptazodis);
        pastas = (EditText) findViewById(R.id.pastas);
        pakartotas_slaptazodis = (EditText) findViewById(R.id.pakartoti_slaptazodi);
        registruotisM = (Button) findViewById(R.id.registruotis_mygtukas);

        registruotisM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, sname, email, pass, repass;
                Integer isAdmin = 2;
                name = vardas.getText().toString();
                sname = pavarde.getText().toString();
                Integer phone = Integer.parseInt(telefono_numeris.getText().toString().trim());
                email = pastas.getText().toString();
                pass = slaptazodis.getText().toString();
                repass = pakartotas_slaptazodis.getText().toString();


                if (name.equals("")||sname.equals("")||email.equals("")||phone.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(RegisterActivity.this, "Prašome užpildyti visus laukelius", Toast.LENGTH_SHORT).show();
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(RegisterActivity.this, "Blogas el.pašto formatas", Toast.LENGTH_SHORT).show();
                }
                else if (pass.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Slaptažodį turi sudaryti 6 simboliai", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass.equals(repass)){
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.fetchSignInMethodsForEmail(pastas.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                Log.d(TAG,""+task.getResult().getSignInMethods().size());
                                if (task.getResult().getSignInMethods().size() == 0){
                                    registerUser(name, sname, phone, email, pass, isAdmin);}
                                else Toast.makeText(RegisterActivity.this, "El. paštas jau egzistuoja", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();}
                        });
                    }
                    else Toast.makeText(RegisterActivity.this, "Pakartotas slaptaždis nesutampa", Toast.LENGTH_SHORT).show();
                }
            }
       });
    }

    private void registerUser(String name, String second_name, Integer phone_number, String email, String password, Integer isAdmin){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registracija sėkminga", Toast.LENGTH_SHORT).show();
                    firestore = FirebaseFirestore.getInstance();
                    userId = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firestore.collection("Users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", userId);
                    user.put("name", name);
                    user.put("second_name", second_name);
                    user.put("phone_number", phone_number);
                    user.put("email", email);
                    user.put("is_admin", isAdmin);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Vartotojui '" + userId + "' profilis sukurtas", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Nenumatyta klaida", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}