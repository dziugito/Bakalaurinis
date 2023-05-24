package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.Reports;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class EventViewActivity extends AppCompatActivity {

    ListView sarasas;
    ArrayList<Reports> reportArrayList;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String usrId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        sarasas = findViewById(R.id.ivykiu_sarasas);
        reportArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        usrId = auth.getCurrentUser().getUid();


        loadDatainListview();
    }

    private void loadDatainListview() {

        DocumentReference docRef = db.collection("Users").document(usrId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Integer isAdmin = Math.toIntExact(document.getLong("is_admin"));
                        if(isAdmin == 1) {
                            db.collection("Report").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                for (DocumentSnapshot d : list) {
                                                    Reports reports = d.toObject(Reports.class);
                                                    reportArrayList.add(reports);
                                                }
                                                EventViewAdapter adapter = new EventViewAdapter(EventViewActivity.this, reportArrayList);
                                                sarasas.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(EventViewActivity.this, "Registruotų įvykių nėra", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EventViewActivity.this, "Įvyko klaida", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            db.collection("Report").whereEqualTo("vartotojoId", usrId).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                for (DocumentSnapshot d : list) {
                                                    Reports reports = d.toObject(Reports.class);
                                                    reportArrayList.add(reports);
                                                }
                                                EventViewAdapter adapter = new EventViewAdapter(EventViewActivity.this, reportArrayList);
                                                sarasas.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(EventViewActivity.this, "Registruotų įvykių nėra", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EventViewActivity.this, "Įvyko klaida", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
            }
        });

    }
}