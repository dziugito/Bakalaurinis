package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.CarParts;
import com.example.draudiminiai_ivykiai.dataStructure.UserCar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class PartsActivity extends AppCompatActivity {

    Spinner marke, modelis, metai, kebulas, pavaruDeze, kuroTipas, daliesLokacija;
    EditText daliesPavadinimas, daliesKaina, keitimoKaina, dazymoKaina;
    Button registruotiB, dalysB;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);

        marke = findViewById(R.id.markeSpinner);
        modelis = findViewById(R.id.modelisSpinner);
        metai = findViewById(R.id.metaiSpinner);
        kebulas = findViewById(R.id.kebulasSpinner);
        pavaruDeze = findViewById(R.id.pavaruDezeSpinner);
        kuroTipas = findViewById(R.id.kuroTipasSpinner);
        daliesPavadinimas =findViewById(R.id.daliesPavadinimas);
        daliesLokacija =findViewById(R.id.daliesLokacija);
        daliesKaina =findViewById(R.id.daliesKaina);
        keitimoKaina =findViewById(R.id.daliesKeitimoKaina);
        dazymoKaina =findViewById(R.id.daliesDazymoKaina);
        registruotiB =findViewById(R.id.registruotiAutoDaliM);
        dalysB =findViewById(R.id.dalysM);

        firestore = FirebaseFirestore.getInstance();
        loadCarMake();

        marke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String make = marke.getSelectedItem().toString();
                loadCarModel(make);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        modelis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String make = marke.getSelectedItem().toString();
                String model = modelis.getSelectedItem().toString();
                loadCarYear(make, model);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        metai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String make = marke.getSelectedItem().toString();
                String model = modelis.getSelectedItem().toString();
                String year = metai.getSelectedItem().toString();
                loadCarBody(make, model, year);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        kebulas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String make = marke.getSelectedItem().toString();
                String model = modelis.getSelectedItem().toString();
                String year = metai.getSelectedItem().toString();
                String body = kebulas.getSelectedItem().toString();
                loadCarFuel(make, model, year, body);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        kuroTipas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String make = marke.getSelectedItem().toString();
                String model = modelis.getSelectedItem().toString();
                String year = metai.getSelectedItem().toString();
                String body = kebulas.getSelectedItem().toString();
                String fuel = kuroTipas.getSelectedItem().toString();
                loadCarTransmission(make, model, year, body, fuel);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this,
                R.array.dalies_lokacija, android.R.layout.simple_spinner_item);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daliesLokacija.setAdapter(spinnerAdapter1);

        dalysB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PartsViewActivity.class);
                startActivity(intent);
            }
        });

        registruotiB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mark = marke.getSelectedItem().toString();
                String model = modelis.getSelectedItem().toString();
                String year = metai.getSelectedItem().toString();
                String body = kebulas.getSelectedItem().toString();
                String fuel = kuroTipas.getSelectedItem().toString();
                String transmission = pavaruDeze.getSelectedItem().toString();
                getCarId( mark, model, year, body, fuel, transmission);
            }
        });
    }

    public void getCarId(String mark,String model,String year,String body,String fuel,String transmission){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference carRef = rootRef.collection("Cars");
        carRef.whereEqualTo("marke", mark).whereEqualTo("modelis", model).whereEqualTo("metai", year)
                .whereEqualTo("kebulo_tipas", body).whereEqualTo("kuro_tipas", fuel).whereEqualTo("pavaru_deze", transmission)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                addUserCar(id);
                            }
                        }
                    }
                });
    }

    private void addUserCar(String autoId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference partsRef = db.collection("Car_parts");
        String dalis = daliesPavadinimas.getText().toString();
        String lokacija = daliesLokacija.getSelectedItem().toString();
        Integer kaina = Integer.valueOf(daliesKaina.getText().toString());
        Integer keitimas = Integer.valueOf(keitimoKaina.getText().toString());
        Integer dazymas = Integer.valueOf(dazymoKaina.getText().toString());



        if(dalis.equals("")||lokacija.equals("")||kaina == null||keitimas == null||dazymas == null)
            Toast.makeText(PartsActivity.this, "Reikia u=pildyti visus laukelius", Toast.LENGTH_SHORT).show();
        else{
                CarParts parts = new CarParts(autoId, dalis, lokacija, kaina, keitimas, dazymas);
                partsRef.add(parts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PartsActivity.this, "Detalė pridėta sėkmingai", Toast.LENGTH_SHORT).show();
                        daliesPavadinimas.getText().clear();
                        daliesKaina.getText().clear();
                        dazymoKaina.getText().clear();
                        keitimoKaina.getText().clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(PartsActivity.this, "Klaida!", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private void loadCarMake(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("Cars");
        List<String> makes = new ArrayList<>();
        makeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String make = document.getString("marke");
                        makes.add(make);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(makes);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listWithoutDuplicates);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marke.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadCarModel(String make){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference modelRef = rootRef.collection("Cars");
        List<String> models = new ArrayList<>();
        modelRef.whereEqualTo("marke", make).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String model = document.getString("modelis");
                        models.add(model);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(models);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listWithoutDuplicates);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelis.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadCarYear(String make, String model){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference yearRef = rootRef.collection("Cars");
        List<String> years = new ArrayList<>();
        yearRef.whereEqualTo("marke", make).whereEqualTo("modelis", model).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String year = document.getString("metai");
                        years.add(year);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(years);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listWithoutDuplicates);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    metai.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadCarBody(String make, String model, String year){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference bodyRef = rootRef.collection("Cars");
        List<String> bodies = new ArrayList<>();
        bodyRef.whereEqualTo("marke", make).whereEqualTo("modelis", model).whereEqualTo("metai", year).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String body = document.getString("kebulo_tipas");
                        bodies.add(body);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(bodies);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listWithoutDuplicates);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kebulas.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadCarFuel(String make, String model, String year, String body){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference bodyRef = rootRef.collection("Cars");
        List<String> bodies = new ArrayList<>();
        bodyRef.whereEqualTo("marke", make).whereEqualTo("modelis", model).whereEqualTo("metai", year)
                .whereEqualTo("kebulo_tipas", body).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String body = document.getString("kuro_tipas");
                                bodies.add(body);
                            }
                            LinkedHashSet<String> listToSet = new LinkedHashSet<String>(bodies);
                            List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listWithoutDuplicates);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            kuroTipas.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void loadCarTransmission(String make, String model, String year, String body, String fuel){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference bodyRef = rootRef.collection("Cars");
        List<String> bodies = new ArrayList<>();
        bodyRef.whereEqualTo("marke", make).whereEqualTo("modelis", model).whereEqualTo("metai", year)
                .whereEqualTo("kebulo_tipas", body).whereEqualTo("kuro_tipas", fuel).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String body = document.getString("pavaru_deze");
                                bodies.add(body);
                            }
                            LinkedHashSet<String> listToSet = new LinkedHashSet<String>(bodies);
                            List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listWithoutDuplicates);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            pavaruDeze.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}