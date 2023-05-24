package com.example.draudiminiai_ivykiai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class AutoAndTimeFragment extends Fragment {

    Button kitas;
    Spinner carSpinner;
    TextView useris;
    TimePicker laikas;
    CalendarView data;
    String date, numeris, metai, diena, menesis, time;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_and_time, container, false);
        kitas = (Button)view.findViewById(R.id.kitasB);
        carSpinner = (Spinner)view.findViewById(R.id.spinnerUserAuto);
        useris = (TextView) view.findViewById(R.id.useris);
        laikas = (TimePicker) view.findViewById(R.id.laikas);
        data = (CalendarView) view.findViewById(R.id.data);
        laikas.setIs24HourView(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        loadUserCar();

        data.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                diena = String.valueOf(dayOfMonth);
                metai = String.valueOf(year);
                menesis = String.valueOf(month);
            }
        });


        kitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                numeris = carSpinner.getSelectedItem().toString();
                int valanda = laikas.getCurrentHour();
                int minutes = laikas.getCurrentMinute();
                time = valanda + ":" + minutes;
                date = metai + "/" + menesis + "/" + diena;


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                PlaceFragment mfragment=new PlaceFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Numeris",numeris);
                bundle.putString("Laikas",time);
                bundle.putString("Data",date);
                mfragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, mfragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void loadUserCar(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference plateRef = rootRef.collection("User_cars");
        List<String> plates = new ArrayList<>();
        plateRef.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String plate = document.getString("valstybinis_numeris");
                        plates.add(plate);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, plates);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carSpinner.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}