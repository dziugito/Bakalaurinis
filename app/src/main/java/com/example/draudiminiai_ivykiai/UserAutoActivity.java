package com.example.draudiminiai_ivykiai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.draudiminiai_ivykiai.dataStructure.CrashEvent;
import com.example.draudiminiai_ivykiai.dataStructure.UserCar;
import com.example.draudiminiai_ivykiai.dataStructure.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserAutoActivity extends AppCompatActivity {

    TextView auto;
    ListView auto2;
    FirebaseAuth firebaseAuth;
    ArrayList<String> carList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auto);

        auto = findViewById(R.id.automobiliai);
        auto2 = findViewById(R.id.automobiliai2);

        carList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carList);
        auto2.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userId = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference userCarRef = rootRef.collection("User_cars");
        userCarRef.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserCar userCar = document.toObject(UserCar.class);
                        userCar.setUserAutoId(document.getId());
                        carList.add(String.valueOf(userCar));
                        adapter.notifyDataSetChanged();
                    }
                    }
                }

        });
    }
}