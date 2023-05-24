package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.draudiminiai_ivykiai.dataStructure.Car;
import com.example.draudiminiai_ivykiai.dataStructure.Users;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserListActivity extends AppCompatActivity {

    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        usersRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Users users = documentSnapshot.toObject(Users.class);
                    users.setId(documentSnapshot.getId());

                    String userId = users.getId();
                    String name = users.getName();
                    String second_name = users.getSecond_name();
                    Integer phone_number = users.getPhone_number();
                    String email = users.getEmail();

                    data += "ID: " + userId
                            + "\nVardas: " + name + "\nPavardė: " + second_name + "\nTel. numeris: " + phone_number + "\nEl. paštas: " + email + "\n\n";
                }

                textViewData.setText(data);
            }
        });
    }
}