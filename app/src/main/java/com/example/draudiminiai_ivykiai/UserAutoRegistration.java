package com.example.draudiminiai_ivykiai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.Car;
import com.example.draudiminiai_ivykiai.dataStructure.UserCar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class UserAutoRegistration extends AppCompatActivity {

    Spinner marke, modelis, metai, kebulas, pavaruDeze, kuroTipas;
    CheckBox lGera, gera, vidutine, bloga, lBloga;
    EditText identifikacinisNumeris, valstybinisNumeris, kilometrazas;
    Button registruotiB;
    String bukle;
    TextView automobilis;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auto_registration);

        identifikacinisNumeris = (EditText) findViewById(R.id.identifikacinisNumeris);
        valstybinisNumeris = (EditText) findViewById(R.id.valstybinisNumeris);
        marke = (Spinner) findViewById(R.id.markeSpinner);
        modelis = (Spinner) findViewById(R.id.modelisSpinner);
        metai = (Spinner) findViewById(R.id.metaiSpinner);
        kebulas = (Spinner) findViewById(R.id.kebulasSpinner);
        pavaruDeze = (Spinner) findViewById(R.id.pavaruDezeSpinner);
        kuroTipas = (Spinner) findViewById(R.id.kuroTipasSpinner);
        kilometrazas = (EditText) findViewById(R.id.kilometrazas);
        lGera = (CheckBox) findViewById(R.id.labaiGeraBukle);
        gera = (CheckBox) findViewById(R.id.geraBukle);
        vidutine = (CheckBox) findViewById(R.id.vidutineBukle);
        bloga = (CheckBox) findViewById(R.id.blogaBukle);
        lBloga = (CheckBox) findViewById(R.id.labaiBlogaBukle);
        registruotiB = (Button) findViewById(R.id.registruotiVartotojoAutoM);
        automobilis = (TextView) findViewById(R.id.prisijungesUseris);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

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


        lGera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gera.setChecked(false);
                    vidutine.setChecked(false);
                    bloga.setChecked(false);
                    lBloga.setChecked(false);
                }
            }
        });
        gera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    lGera.setChecked(false);
                    vidutine.setChecked(false);
                    bloga.setChecked(false);
                    lBloga.setChecked(false);
                }
            }
        });
        vidutine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gera.setChecked(false);
                    lGera.setChecked(false);
                    bloga.setChecked(false);
                    lBloga.setChecked(false);
                }
            }
        });
        bloga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gera.setChecked(false);
                    lGera.setChecked(false);
                    vidutine.setChecked(false);
                    lBloga.setChecked(false);
                }
            }
        });
        lBloga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gera.setChecked(false);
                    lGera.setChecked(false);
                    bloga.setChecked(false);
                    vidutine.setChecked(false);
                }
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

    private void addUserCar(String autoId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference carsRef = db.collection("User_cars");
        String vin = identifikacinisNumeris.getText().toString();
        String valstNr = valstybinisNumeris.getText().toString();
        Integer kilometraz = Integer.valueOf(kilometrazas.getText().toString());
        String mark = marke.getSelectedItem().toString();
        String model = modelis.getSelectedItem().toString();
        String year = metai.getSelectedItem().toString();
        String body = kebulas.getSelectedItem().toString();
        String fuel = kuroTipas.getSelectedItem().toString();
        String transmission = pavaruDeze.getSelectedItem().toString();


        if(vin.equals("")||valstNr.equals("")||kilometraz.equals("")||mark.equals("")||model.equals("")||year.equals("")||body.equals("")||fuel.equals("")||transmission.equals(""))
            Toast.makeText(UserAutoRegistration.this, "Prašome užpildyti visus laukelius", Toast.LENGTH_SHORT).show();
        else{
            bukle = checkBoxItem();
            if(bukle==null) Toast.makeText(UserAutoRegistration.this, "Nepažymėjote automobilio būklės", Toast.LENGTH_SHORT).show();
            else{
                UserCar userCar = new UserCar(autoId, userId, vin, valstNr, kilometraz, bukle);
                carsRef.add(userCar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(UserAutoRegistration.this, "Automobilis užregistruotas sėkmingai", Toast.LENGTH_SHORT).show();
                        identifikacinisNumeris.getText().clear();
                        valstybinisNumeris.getText().clear();
                        kilometrazas.getText().clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(UserAutoRegistration.this, "Klaida!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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

    private String checkBoxItem(){

        String bukle = null;

        if (lGera.isChecked()){
            bukle = "Labai gera";
        }
        else if (gera.isChecked()){
            bukle = "Gera";
        }
        else if (vidutine.isChecked()){
            bukle = "Vidutinė";
        }
        else if (bloga.isChecked()){
            bukle = "Bloga";
        }
        else if (lBloga.isChecked()){
            bukle = "Labai bloga";
        }

        return bukle;
    }

}