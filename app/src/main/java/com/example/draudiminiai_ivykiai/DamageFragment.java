package com.example.draudiminiai_ivykiai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DamageFragment extends Fragment {

    ListView daliuSarasas, pazymetosDalys;
    TextView masinosId;
    Button priekis, kaire, galas, desine, kitas;
    ArrayList<String> apgadintos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_damage, container, false);

        daliuSarasas = view.findViewById(R.id.dalysList);
        pazymetosDalys = view.findViewById(R.id.pazymetosDalys);
        masinosId = view.findViewById(R.id.masinos_id);
        priekis = view.findViewById(R.id.P);
        galas = view.findViewById(R.id.G);
        kaire = view.findViewById(R.id.K);
        desine = view.findViewById(R.id.D);
        kitas = view.findViewById(R.id.kitasB3);

        String numeris = getArguments().getString("Numeris");
        String time = getArguments().getString("Laikas");
        String date = getArguments().getString("Data");
        String address = getArguments().getString("Adresas");
        String uri = getArguments().getString("Uri");

        apgadintos = new ArrayList<>();

        getCarId(numeris);

        priekis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPList();
            }
        });

        galas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGList();
            }
        });

        kaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKList();
            }
        });

        desine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDList();
            }
        });

        kitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                SummaryFragment sfragment=new SummaryFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Numeris",numeris);
                bundle.putString("Laikas",time);
                bundle.putString("Data",date);
                bundle.putString("Adresas", address);
                bundle.putString("Uri", uri);
                bundle.putStringArrayList("Detales", apgadintos);
                sfragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, sfragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    public void getCarId(String nr){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference carRef = rootRef.collection("User_cars");
        carRef.whereEqualTo("valstybinis_numeris", nr)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String masina = document.getString("autoId");
                                masinosId.setText(masina);
                            }
                        }
                    }
                });
    }

    private void getPList(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("Car_parts");
        String id = masinosId.getText().toString();
        ArrayList<String> pavad = new ArrayList<>();
        makeRef.whereEqualTo("lokacija", "P").whereEqualTo("autoId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String pavadinimas = document.getString("pavadinimas");
                        pavad.add(pavadinimas);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(pavad);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> daliu_sarasas = new ArrayAdapter<>(getActivity().getBaseContext(),
                            android.R.layout.simple_list_item_multiple_choice, listWithoutDuplicates);
                    daliuSarasas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    daliuSarasas.setAdapter(daliu_sarasas);
                    daliuSarasas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String dalys = (String) adapterView.getItemAtPosition(i);
                            apgadintos.add(dalys);
                            ArrayAdapter<String> apgadint = new ArrayAdapter<>(getActivity().getBaseContext(),
                                    android.R.layout.simple_list_item_1, apgadintos);
                            pazymetosDalys.setAdapter(apgadint);
                        }
                    });
                }
            }
        });
    }

    private void getGList(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("Car_parts");
        String id = masinosId.getText().toString();
        ArrayList<String> pavad = new ArrayList<>();
        makeRef.whereEqualTo("lokacija", "G").whereEqualTo("autoId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String pavadinimas = document.getString("pavadinimas");
                        pavad.add(pavadinimas);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(pavad);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> daliu_sarasas = new ArrayAdapter<>(getActivity().getBaseContext(),
                            android.R.layout.simple_list_item_multiple_choice, listWithoutDuplicates);
                    daliuSarasas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    daliuSarasas.setAdapter(daliu_sarasas);
                    daliuSarasas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String dalys = (String) adapterView.getItemAtPosition(i);
                            apgadintos.add(dalys);
                            ArrayAdapter<String> apgadint = new ArrayAdapter<>(getActivity().getBaseContext(),
                                    android.R.layout.simple_list_item_1, apgadintos);
                            pazymetosDalys.setAdapter(apgadint);
                        }
                    });
                }
            }
        });
    }

    private void getKList(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("Car_parts");
        String id = masinosId.getText().toString();
        ArrayList<String> pavad = new ArrayList<>();
        makeRef.whereEqualTo("lokacija", "K").whereEqualTo("autoId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String pavadinimas = document.getString("pavadinimas");
                        pavad.add(pavadinimas);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(pavad);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> daliu_sarasas = new ArrayAdapter<>(getActivity().getBaseContext(),
                            android.R.layout.simple_list_item_multiple_choice, listWithoutDuplicates);
                    daliuSarasas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    daliuSarasas.setAdapter(daliu_sarasas);
                    daliuSarasas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String dalys = (String) adapterView.getItemAtPosition(i);
                            apgadintos.add(dalys);
                            ArrayAdapter<String> apgadint = new ArrayAdapter<>(getActivity().getBaseContext(),
                                    android.R.layout.simple_list_item_1, apgadintos);
                            pazymetosDalys.setAdapter(apgadint);
                        }
                    });
                }
            }
        });
    }

    private void getDList(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("Car_parts");
        String id = masinosId.getText().toString();
        ArrayList<String> pavad = new ArrayList<>();
        makeRef.whereEqualTo("lokacija", "D").whereEqualTo("autoId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String pavadinimas = document.getString("pavadinimas");
                        pavad.add(pavadinimas);
                    }
                    LinkedHashSet<String> listToSet = new LinkedHashSet<String>(pavad);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    ArrayAdapter<String> daliu_sarasas = new ArrayAdapter<>(getActivity().getBaseContext(),
                            android.R.layout.simple_list_item_multiple_choice, listWithoutDuplicates);
                    daliuSarasas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    daliuSarasas.setAdapter(daliu_sarasas);
                    daliuSarasas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String dalys = (String) adapterView.getItemAtPosition(i);
                            apgadintos.add(dalys);
                            ArrayAdapter<String> apgadint = new ArrayAdapter<>(getActivity().getBaseContext(),
                                    android.R.layout.simple_list_item_1, apgadintos);
                            pazymetosDalys.setAdapter(apgadint);
                        }
                    });
                }
            }
        });
    }

}