package com.example.draudiminiai_ivykiai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ResourceBundle;

public class HomeActivity extends AppCompatActivity {

    Button administravimasB, profilisB, registruotiIvykiB, atsijungtiB;
    String userId;
    DatabaseReference dbReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        administravimasB = (Button) findViewById(R.id.programos_administravimas);
        profilisB = (Button) findViewById(R.id.vartotojo_profilis);
        registruotiIvykiB = (Button) findViewById(R.id.registruoti_eismo_ivyki);
        atsijungtiB = (Button) findViewById(R.id.atsijungti);
        dbReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        administravimasB.setVisibility(View.GONE);

        firestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();


        DocumentReference docRef = firestore.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Integer isAdmin = Math.toIntExact(document.getLong("is_admin"));
                        if(isAdmin == 1) administravimasB.setVisibility(View.VISIBLE);
                        else administravimasB.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(HomeActivity.this, "Nėra vartotojo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        administravimasB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdministrationActivity.class);
                startActivity(intent);
            }
        });

        profilisB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        registruotiIvykiB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterEventActivity.class);
                startActivity(intent);
            }
        });

        atsijungtiB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(HomeActivity.this, "Atsijungimas sėkmingas", Toast.LENGTH_SHORT).show();
            }
        });
    }

}