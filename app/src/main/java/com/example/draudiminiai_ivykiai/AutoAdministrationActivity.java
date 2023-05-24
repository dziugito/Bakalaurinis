package com.example.draudiminiai_ivykiai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.Car;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AutoAdministrationActivity extends AppCompatActivity {

    EditText rinkosKaina, modelis;
    Spinner marke, metai, kuroTipas, pavaruDeze, kebuloTipas;
    Button pridetiB;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference carsRef = db.collection("Cars");
    private TextView textViewData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_administration);

        marke = findViewById(R.id.autoMarke);
        modelis = findViewById(R.id.autoModelis);
        metai = findViewById(R.id.autoMetai);
        rinkosKaina = findViewById(R.id.autoRinkosKaina);
        kuroTipas = findViewById(R.id.autoKuroTipas);
        pavaruDeze = findViewById(R.id.autoPavaruDeze);
        kebuloTipas = findViewById(R.id.autoKebuloTipas);
        pridetiB = findViewById(R.id.issaugotiAuto);
        textViewData = findViewById(R.id.text_view_data);


        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this,
                R.array.kuro_tipas, android.R.layout.simple_spinner_item);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kuroTipas.setAdapter(spinnerAdapter1);

        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.pavaru_deze, android.R.layout.simple_spinner_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pavaruDeze.setAdapter(spinnerAdapter2);

        ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this,
                R.array.kebulo_tipas, android.R.layout.simple_spinner_item);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kebuloTipas.setAdapter(spinnerAdapter3);

        ArrayAdapter<CharSequence> spinnerAdapter4 = ArrayAdapter.createFromResource(this,
                R.array.marke, android.R.layout.simple_spinner_item);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marke.setAdapter(spinnerAdapter4);

        ArrayAdapter<CharSequence> spinnerAdapter5 = ArrayAdapter.createFromResource(this,
                R.array.metai, android.R.layout.simple_spinner_item);
        spinnerAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metai.setAdapter(spinnerAdapter5);
    }

    @Override
    protected void onStart() {
        super.onStart();
        carsRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Car car = documentSnapshot.toObject(Car.class);
                    car.setAutoId(documentSnapshot.getId());

                    String carId = car.getAutoId();
                    String make = car.getMarke();
                    String model = car.getModelis();
                    String year = car.getMetai();
                    String body = car.getKebulo_tipas();
                    data += "ID: " + carId
                            + "\nMarkė: " + make + "\nModelis: " + model + "\nMetai: " + year + "\nKėbulas: " + body + "\n\n";
                }

                textViewData.setText(data);
            }
        });
    }

    public void addCar(View v){
        String make = marke.getSelectedItem().toString();
        String model = modelis.getText().toString();
        String year = metai.getSelectedItem().toString();
        Integer price = Integer.parseInt(rinkosKaina.getText().toString());
        String fuelType = kuroTipas.getSelectedItem().toString();
        String transmission = pavaruDeze.getSelectedItem().toString();
        String body = kebuloTipas.getSelectedItem().toString();

        Car car = new Car(make, model, year, fuelType, transmission, body, price);


        if (make.equals("")||model.equals("")||year.equals("")||price.equals("")||fuelType.equals("")||transmission.equals("")||body.equals(""))
            Toast.makeText(AutoAdministrationActivity.this, "Prašome užpildyti visus laukelius", Toast.LENGTH_SHORT).show();
        else{
            carsRef.add(car).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AutoAdministrationActivity.this, "Automobilis pridėtas sėkmingai", Toast.LENGTH_SHORT).show();
                    modelis.getText().clear();
                    rinkosKaina.getText().clear();
                    loadCars();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AutoAdministrationActivity.this, "Klaida!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void loadCars(){
        carsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Car car = documentSnapshot.toObject(Car.class);
                            car.setAutoId(documentSnapshot.getId());

                            String carId = car.getAutoId();
                            String make = car.getMarke();
                            String model = car.getModelis();
                            String year = car.getMetai();
                            String body = car.getKebulo_tipas();

                            data += "ID: " + carId
                                    + "\nMarkė: " + make + "\nModelis: " + model + "\nMetai: " + year + "\nKėbulas: " + body + "\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }
}