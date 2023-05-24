package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.draudiminiai_ivykiai.dataStructure.Car;
import com.example.draudiminiai_ivykiai.dataStructure.CarParts;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PartsViewActivity extends AppCompatActivity {

    TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_view);

        textViewData = findViewById(R.id.text_view_data);

        loadParts();
    }

    public void loadParts(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference carsRef = db.collection("Car_parts");
        carsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CarParts carParts = documentSnapshot.toObject(CarParts.class);
                            carParts.setAutoId(documentSnapshot.getId());

                            String carId = carParts.getAutoId();
                            String pav = carParts.getPavadinimas();
                            String lok = carParts.getLokacija();
                            Integer kain = carParts.getKaina();
                            Integer tkain = carParts.getTvarkymo_kaina();
                            Integer dkain = carParts.getDazymo_kaina();

                            data += "Automobilio ID: " + carId
                                    + "\nDetalės pavadinimas: " + pav + "\nLokacija: " + lok + "\nKaina: " + kain + "\nKeitimo kaina: " + tkain + "\nDažymo kaina: " + dkain + "\n\n";
                        }

                        textViewData.setText(data);
                    }
                });
    }
}