package com.example.draudiminiai_ivykiai;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.CrashEvent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SummaryFragment extends Fragment {

    TextView nr, laikas, data, adresas,  detales, auto, vin, metai, rida, kuras, savininkas, useriAutoId, urlId;
    ImageView imageView;
    Button mygtukas;
    CheckBox sutikimas;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String userId;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        auto = view.findViewById(R.id.t1);
        nr = view.findViewById(R.id.t2);
        vin = view.findViewById(R.id.t3);
        metai = view.findViewById(R.id.t4);
        rida = view.findViewById(R.id.t5);
        kuras = view.findViewById(R.id.t6);
        savininkas = view.findViewById(R.id.t7);
        laikas = view.findViewById(R.id.t8);
        data = view.findViewById(R.id.t9);
        adresas = view.findViewById(R.id.t10);
        detales = view.findViewById(R.id.t11);
        imageView = view.findViewById(R.id.imageView);
        mygtukas = view.findViewById(R.id.registravimoIrApskaiciavimoM);
        sutikimas = view.findViewById(R.id.sutikimas);
        useriAutoId = view.findViewById(R.id.userioMasinosId);
        urlId = view.findViewById(R.id.urlId);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        getUser();

        String numeris = getArguments().getString("Numeris");
        String time = getArguments().getString("Laikas");
        String date = getArguments().getString("Data");
        String address = getArguments().getString("Adresas");
        String uri = getArguments().getString("Uri");
        ArrayList parts = getArguments().getStringArrayList("Detales");

        getUserCarInfo(numeris);
        getUserCarId(numeris);

        nr.setText(numeris);
        laikas.setText(time);
        data.setText(date);
        adresas.setText(address);
        detales.setText(parts.toString());
        imageView.setImageURI(Uri.parse(uri));

        Logger global = Logger.global;
        final int[] click = {0};

        mygtukas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sutikimas.isChecked()) {

                    if (click[0] == 0) {
                        click[0] = 1;
                        String uri = getArguments().getString("Uri");
                        uploadImageToFirebase(Uri.parse(uri));
                        mygtukas.setText("Apskaičiuoti žalą");
                    } else {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        CalculationFragment cfragment = new CalculationFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("IvykioId", useriAutoId.getText().toString());
                        bundle.putString("Automobilis", auto.getText().toString());
                        bundle.putString("ValNumeris", nr.getText().toString());
                        bundle.putString("IdentNr", vin.getText().toString());
                        bundle.putString("Metai", metai.getText().toString());
                        bundle.putString("Kuras", kuras.getText().toString());
                        bundle.putString("Savininkas", savininkas.getText().toString());
                        bundle.putString("Laikas", laikas.getText().toString());
                        bundle.putString("Data", data.getText().toString());
                        bundle.putString("Adresas", adresas.getText().toString());
                        bundle.putString("Url", urlId.getText().toString());
                        bundle.putStringArrayList("dalys", parts);
                        cfragment.setArguments(bundle);

                        fragmentTransaction.replace(R.id.fragment_container, cfragment);
                        fragmentTransaction.commit();
                    }
                }
                else Toast.makeText(getActivity().getBaseContext(), "Reikia sutikti su teisingos informacijos pateikimu", Toast.LENGTH_SHORT).show();
                }
        });
        return view;
    }

    private void saveToDb(Uri url){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventRef = db.collection("Crash_event");
        urlId.setText(String.valueOf(url));
        String time = getArguments().getString("Laikas");
        String date = getArguments().getString("Data");
        String address = getArguments().getString("Adresas");
        ArrayList<String> parts = getArguments().getStringArrayList("Detales");
        String userCarId = useriAutoId.getText().toString();

                CrashEvent crashEvent = new CrashEvent(userId, userCarId, time, date, url, address, parts);
                eventRef.add(crashEvent).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity().getBaseContext(), "Eismo įvykis sėkmingai užregistruotas", Toast.LENGTH_SHORT).show();
                        eventRef.whereEqualTo("adresas", address).whereEqualTo("laikas", time).whereEqualTo("data", date)
                                .whereEqualTo("dalys", parts).whereEqualTo("vartotojoAutoId", userCarId).whereEqualTo("vartotojoId", userId).
                                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String docId = document.getId();
                                        useriAutoId.setText(docId);
                                    }
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(getActivity().getBaseContext(), "Nenumatyta klaida!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUser(){

        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String vardas = document.getString("name");
                        String pavarde = document.getString("second_name");
                        savininkas.setText(vardas + " " + pavarde);
                    }
                }
            }
        });
    }

    private void getUserCarId(String numeris){

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("User_cars");
        makeRef.whereEqualTo("valstybinis_numeris", numeris).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String docId = document.getId();
                        useriAutoId.setText(docId);
                    }
                }
            }
        });
    }

    private void getUserCarInfo(String numeris){

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("User_cars");
        makeRef.whereEqualTo("valstybinis_numeris", numeris).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Integer kilometrazas = document.getLong("kilometrazas").intValue();
                        String ident = document.getString("vin");
                        String autoId = document.getString("autoId");
                        getCarInfo(autoId);
                        vin.setText(ident);
                        rida.setText(kilometrazas.toString());
                    }
                }
            }
        });
    }

    private void getCarInfo(String autoId){

        DocumentReference docRef = db.collection("Cars").document(autoId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String marke = document.getString("marke");
                        String modelis = document.getString("modelis");
                        String kuro = document.getString("kuro_tipas");
                        String met = document.getString("metai");
                        auto.setText(marke + " " + modelis);
                        kuras.setText(kuro);
                        metai.setText(met);
                    }
                }
            }
        });
    }

    private void uploadImageToFirebase(Uri imageUri){

        Uri file = imageUri;
        StorageReference riversRef = storageReference.child("images/"+imageUri.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getImageUrlFirebase(file);
            }
        });


    }

    private void getImageUrlFirebase(Uri uri) {
        Uri file = uri;
        final StorageReference ref = storageReference.child("images/" + uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveToDb(downloadUri);
                }
            }
        });
    }

}