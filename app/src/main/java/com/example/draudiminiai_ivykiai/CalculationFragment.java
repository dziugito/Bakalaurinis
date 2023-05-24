package com.example.draudiminiai_ivykiai;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draudiminiai_ivykiai.dataStructure.CarDamage;
import com.example.draudiminiai_ivykiai.dataStructure.Reports;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class CalculationFragment extends Fragment {
    TextView ivykis, lygonamoAutoKaina, lyginamoAutoRida, autoRida, autoBukle,
            rinkosKainosApskaiciavimas, galutineRinkosKaina, nuvertejimoProcentas,
            apgadintosDalys, daliuK, daliuKeitimoK, daliuDazymoK, ivertintaZala, procentass, s1, s2, s3, s4, damageId;
    String ivykioId;
    Integer lygRida = 100000;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Button mygtukas, generuotiPDF;
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculation, container, false);

        ivykis = view.findViewById(R.id.ivykioId);
        lygonamoAutoKaina = view.findViewById(R.id.lyginamoAutoKaina);
        lyginamoAutoRida = view.findViewById(R.id.lyginamoAutoRida);
        autoBukle = view.findViewById(R.id.autoBukle);
        autoRida = view.findViewById(R.id.autoRida);
        rinkosKainosApskaiciavimas = view.findViewById(R.id.rinkosKainosApskaičiavimas);
        galutineRinkosKaina = view.findViewById(R.id.galutineKaina);
        nuvertejimoProcentas = view.findViewById(R.id.nuvertejimoProc);
        apgadintosDalys = view.findViewById(R.id.apgadintosDalys);
        daliuK = view.findViewById(R.id.daliuKainos);
        daliuKeitimoK = view.findViewById(R.id.daliuKeitimoKainos);
        daliuDazymoK = view.findViewById(R.id.daliuDazymoKainos);
        ivertintaZala = view.findViewById(R.id.ivertintaZala);
        s1 = view.findViewById(R.id.s1);
        s2 = view.findViewById(R.id.s2);
        s3 = view.findViewById(R.id.s3);
        s4 = view.findViewById(R.id.s4);
        damageId = view.findViewById(R.id.damageId);
        mygtukas = view.findViewById(R.id.button2);
        procentass = view.findViewById(R.id.procentas);
        generuotiPDF = view.findViewById(R.id.pdf);

        auth = FirebaseAuth.getInstance();

        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.carlogo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        if (checkPermission()) {
            Toast.makeText(getActivity().getBaseContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        generuotiPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    generatePDF();
                } else {
                    requestPermission();
                }
            }
        });

        db = FirebaseFirestore.getInstance();

        ivykioId = getArguments().getString("IvykioId");
        List<String> dalys = getArguments().getStringArrayList("dalys");

        apgadintosDalys.setText(dalys.toString());
        lyginamoAutoRida.setText(lygRida.toString());
        getUserCarId(ivykioId);

        Logger global = Logger.global;
        final int[] click = {0};

        mygtukas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[0] == 0) {
                    click[0] = 1;
                    saveDamageCalculation();
                    saveReportInfo();
                    mygtukas.setText("Grįžti į meniu");
                    generuotiPDF.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }

        });

        return view;
    }

    private void saveDamageCalculation(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventRef = db.collection("Car_damage");
        String ivykioId = getArguments().getString("IvykioId");
        ArrayList<String> parts = getArguments().getStringArrayList("dalys");
        Integer partsPrice = Integer.valueOf(s1.getText().toString());
        Integer partsRepairPrice = Integer.valueOf(s2.getText().toString());
        Integer partsPaintPrice = Integer.valueOf(s3.getText().toString());
        double settleProcentege = Double.parseDouble((procentass.getText().toString()));
        double totalPrice = Double.parseDouble(s4.getText().toString());

        CarDamage carDamage = new CarDamage(ivykioId, parts, partsPrice, partsRepairPrice, partsPaintPrice, settleProcentege, totalPrice);
        eventRef.add(carDamage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                eventRef.whereEqualTo("apgadintos_dalys", parts).whereEqualTo("daliu_kaina", partsPrice).whereEqualTo("dazymo_kaina", partsPaintPrice)
                        .whereEqualTo("isskaiciavimo_procentas", settleProcentege).whereEqualTo("isviso_suma", totalPrice).whereEqualTo("ivykioId", ivykioId).whereEqualTo("remonto_kaina", partsRepairPrice).
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String docId = document.getId();
                                        damageId.setText(docId);
                                    }
                                    Toast.makeText(getActivity().getBaseContext(), "Žalos apskaičiavimas sėkmingai išsaugotas", Toast.LENGTH_SHORT).show();
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

    private void saveReportInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String vartotojoId = auth.getCurrentUser().getUid();
        String zalosId = damageId.getText().toString();
        String savininkas = getArguments().getString("Savininkas");
        String automobilis = getArguments().getString("Automobilis");
        String valst_numeris = getArguments().getString("ValNumeris");
        String adresas = getArguments().getString("Adresas");
        String laikas = getArguments().getString("Laikas");
        String data = getArguments().getString("Data");
        ArrayList<String> dalys = getArguments().getStringArrayList("dalys");
        Integer daliuKaina = Integer.valueOf(s1.getText().toString());
        Integer daliuTvarkymoKaina = Integer.valueOf(s2.getText().toString());
        Integer daliuDazymoKaina = Integer.valueOf(s3.getText().toString());
        double totalPrice = Double.parseDouble(s4.getText().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String sukurimoData = dateFormat.format(date);
        String url = getArguments().getString("Url");

        CollectionReference eventRef = db.collection("Report");


        Reports report = new Reports(vartotojoId, ivykioId, zalosId,savininkas, automobilis, valst_numeris, adresas, laikas, data, dalys, daliuKaina, daliuTvarkymoKaina, daliuDazymoKaina, totalPrice, url, sukurimoData);
        eventRef.add(report).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(getActivity().getBaseContext(), "Nenumatyta klaida!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPartsPrice(String carId){

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference makeRef = rootRef.collection("Car_parts");
        ArrayList<Integer> daliuKainos = new ArrayList<>();
        ArrayList<Integer> daliuPakeitimoKainos = new ArrayList<>();
        ArrayList<Integer> daliuDazymoKainos = new ArrayList<>();
        List<String> dalys = getArguments().getStringArrayList("dalys");

        for (int counter = 0; counter < dalys.size(); counter++) {
            makeRef.whereEqualTo("autoId", carId).whereEqualTo("pavadinimas", dalys.get(counter)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Integer kaina = document.getLong("kaina").intValue();
                            Integer keitimoKaina = document.getLong("tvarkymo_kaina").intValue();
                            Integer dazymoKaina = document.getLong("dazymo_kaina").intValue();
                            daliuKainos.add(kaina);
                            daliuPakeitimoKainos.add(keitimoKaina);
                            daliuDazymoKainos.add(dazymoKaina);
                        }
                        String daliuKainosSuma = String.valueOf(getSum(daliuKainos));
                        daliuK.setText(daliuKainos + " = " + daliuKainosSuma + " Eur");
                        s1.setText(daliuKainosSuma);
                        String keitimoKainosSuma = String.valueOf(getSum(daliuPakeitimoKainos));
                        daliuKeitimoK.setText(daliuPakeitimoKainos + " = " + keitimoKainosSuma + " Eur");
                        s2.setText(keitimoKainosSuma);
                        String dazymoKainosSuma = String.valueOf(getSum(daliuDazymoKainos));
                        daliuDazymoK.setText(daliuDazymoKainos + " = " + dazymoKainosSuma + " Eur");
                        s3.setText(dazymoKainosSuma);

                        double proc= Double.parseDouble((procentass.getText().toString()));
                        double suma = getSum(daliuKainos) + getSum(daliuPakeitimoKainos) + getSum(daliuDazymoKainos);
                        double ivertinimas = suma - ((suma * proc)/100);
                        DecimalFormat df = new DecimalFormat("#.00");
                        ivertintaZala.setText("(" +getSum(daliuKainos) + " Eur + " + getSum(daliuPakeitimoKainos) + " Eur + " + getSum(daliuDazymoKainos) + " Eur) - " + proc + "% = " + df.format(ivertinimas) + " Eur");
                        s4.setText(String.valueOf(df.format(ivertinimas)));

                    }
                }
            });
        }
    }

    public Integer getSum(ArrayList<Integer> kaina){
        Integer sum = 0;
        for(int i = 0; i < kaina.size(); i++)
            sum += kaina.get(i);
        return sum;
    }

    private void getUserCarId(String ivykioId){
        DocumentReference docRef = db.collection("Crash_event").document(ivykioId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String userCarId = document.getString("vartotojoAutoId");
                        getCarId(userCarId);
                        getUserCarInfo(userCarId);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getBaseContext(), "Eismo įvykis dar saugojamas, bandykite po kelių sekundžių vėl", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCarId(String userCarId){
        DocumentReference docRef = db.collection("User_cars").document(userCarId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String carId = document.getString("autoId");
                        getCarPrice(carId);
                    }
                }
            }
        });
    }

    private void getCarPrice(String carId){
        DocumentReference docRef = db.collection("Cars").document(carId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Integer carPrice = document.getLong("rinkos_kaina").intValue();
                        lygonamoAutoKaina.setText(carPrice.toString());
                        calculateCarPrice();
                        getPartsPrice(carId);
                    }
                }
            }
        });
    }

    private void getUserCarInfo(String userCarId){
        DocumentReference docRef = db.collection("User_cars").document(userCarId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Integer rida = document.getLong("kilometrazas").intValue();
                        String bukle = document.getString("bukle");
                        autoRida.setText(rida.toString());
                        autoBukle.setText(bukle);
                    }
                }
            }
        });
    }

    public void calculateCarPrice(){
        DecimalFormat df = new DecimalFormat("#.00");
        String pKaina;
        double procentas;
        double pirmineKaina;
        double galutineKaina;
        Integer rida = Integer.valueOf(autoRida.getText().toString());
        Integer lygKaina = Integer.valueOf(lygonamoAutoKaina.getText().toString());

        pirmineKaina = rida - lygRida;
        pirmineKaina = pirmineKaina/10000;
        pirmineKaina = pirmineKaina*lygKaina/100;
        pirmineKaina = lygKaina - pirmineKaina;

        pKaina = "Pirminė kaina = " + lygKaina +" Eur - (" + rida + " km - " + lygRida + " km)/10000 km * (" + lygKaina + " Eur/100 Eur) = "+  df.format(pirmineKaina) + " Eur";

        rinkosKainosApskaiciavimas.setText(pKaina);

        if(autoBukle.getText().toString().contains("Labai gera")){
            galutineKaina = pirmineKaina * 0.98;
            galutineRinkosKaina.setText("Galutinė kaina = " + df.format(pirmineKaina) + " Eur * 0,95 = " + df.format(galutineKaina) + " Eur");
        }
        else if(autoBukle.getText().toString().contains("Gera")){
            galutineKaina = pirmineKaina * 0.95;
            galutineRinkosKaina.setText("Galutinė kaina = " + df.format(pirmineKaina) + " Eur * 0,9 = " + df.format(galutineKaina) + " Eur");
        }
        else if(autoBukle.getText().toString().contains("Vidutinė")){
            galutineKaina = pirmineKaina * 0.9;
            galutineRinkosKaina.setText("Galutinė kaina = " + df.format(pirmineKaina) + " Eur * 0,85 = " + df.format(galutineKaina) + " Eur");
        }
        else if(autoBukle.getText().toString().contains("Bloga")){
            galutineKaina = pirmineKaina * 0.85;
            galutineRinkosKaina.setText("Galutinė kaina = " + df.format(pirmineKaina) + " Eur * 0,8 = " + df.format(galutineKaina) + " Eur");
        }
        else{
            galutineKaina = pirmineKaina * 0.8;
            galutineRinkosKaina.setText("Galutinė kaina = " + df.format(pirmineKaina) + " Eur * 0,75 = " + df.format(galutineKaina) + " Eur");
        }

        procentas = ((lygKaina-galutineKaina)*100)/lygKaina;
        procentass.setText(df.format(procentas));
        String nuvertejimas = "Nuvertėjimas = (" + df.format(lygKaina - galutineKaina) + " Eur * 100%)/ " + lygKaina + " Eur = " + df.format(procentas) + "%";

        nuvertejimoProcentas.setText(nuvertejimas);
    }

    private void generatePDF(){
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();
        Paint skyr = new Paint();
        Paint pavad = new Paint();
        Paint bold_text = new Paint();
        Paint normal_text = new Paint();
        Paint line = new Paint();
        Paint data = new Paint();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 120, 20, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(40);
        title.setTextAlign(Paint.Align.CENTER);
        title.setColor(ContextCompat.getColor(getActivity(), R.color.green));
        canvas.drawText("Eismo įvykiai", pagewidth/2, 120, title);

        pavad.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        pavad.setTextSize(25);
        pavad.setTextAlign(Paint.Align.CENTER);
        pavad.setColor(ContextCompat.getColor(getActivity(), R.color.black));

        normal_text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        normal_text.setTextSize(15);
        normal_text.setColor(ContextCompat.getColor(getActivity(), R.color.black));
        normal_text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

        bold_text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        bold_text.setTextSize(15);
        bold_text.setColor(ContextCompat.getColor(getActivity(), R.color.black));

        skyr.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        skyr.setTextSize(20);

        line.setColor(Color.BLACK);
        line.setStrokeWidth(2f);

        float startX = 50;
        float endX = 742;

        data.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        data.setTextSize(14);
        data.setTextAlign(Paint.Align.CENTER);
        data.setColor(ContextCompat.getColor(getActivity(), R.color.black));
        canvas.drawText("Kelių transporto priemonės vertinimo ataskaita nr. L" + gen(), pagewidth/2, 200, pavad);
        canvas.drawText(dateFormat.format(date), pagewidth/2, 220, normal_text);
        canvas.drawLine(startX, 230, endX, 230, line);
        canvas.drawText("Duomenys apie transporto priemonę", 50, 255, skyr);
        canvas.drawText("Markė, modelis: ", 50, 275, normal_text);
        canvas.drawText(getArguments().getString("Automobilis"), 160, 275, bold_text);
        canvas.drawText("Valstybinis numeris: ", 50, 295, normal_text);
        canvas.drawText(getArguments().getString("ValNumeris"), 190, 295, bold_text);
        canvas.drawText("Identifikacinis numeris: ", 50, 315, normal_text);
        canvas.drawText(getArguments().getString("IdentNr"), 205, 315, bold_text);
        canvas.drawText("Registracijos metai: ", 50, 335, normal_text);
        canvas.drawText(getArguments().getString("Metai"), 190, 335, bold_text);
        canvas.drawText("Kuro rūšis: ", 50, 355, normal_text);
        canvas.drawText(getArguments().getString("Kuras"), 130, 355, bold_text);
        canvas.drawText("Transporto priemonės sąvininkas: ", 50, 375, normal_text);
        canvas.drawText(getArguments().getString("Savininkas"), 270, 375, bold_text);
        canvas.drawLine(startX, 385, endX, 385, line);
        canvas.drawText("Duomenys apie eismo įvykį", 50, 405, skyr);
        canvas.drawText("Data: ", 50, 425, normal_text);
        canvas.drawText(getArguments().getString("Data"), 90, 425, bold_text);
        canvas.drawText("Laikas: ", 50, 445, normal_text);
        canvas.drawText(getArguments().getString("Laikas") + "h", 100, 445, bold_text);
        canvas.drawText("Adresas: ", 50, 465, normal_text);
        canvas.drawText(getArguments().getString("Adresas"), 110, 465, bold_text);
        canvas.drawLine(startX, 475, endX, 475, line);
        canvas.drawText("Automobilio rinkos apskaičiavimas ", 50, 495, skyr);
        canvas.drawText("Lyginamo automobilio kaina: ", 50, 515, normal_text);
        canvas.drawText(lygonamoAutoKaina.getText().toString() + " Eur", 250, 515, bold_text);
        canvas.drawText("Lyginamo automobilio rida: ", 50, 535, normal_text);
        canvas.drawText(lyginamoAutoRida.getText().toString() + " km", 240, 535, bold_text);
        canvas.drawText("Eismo įvykyje dalyvavusio automobilio rida: ", 50, 555, normal_text);
        canvas.drawText(autoRida.getText().toString() + " km", 345, 555, bold_text);
        canvas.drawText("Eismo įvykyje dalyvavusio automobilio būklė: ", 50, 575, normal_text);
        canvas.drawText(autoBukle.getText().toString(), 350, 575, bold_text);
        canvas.drawText("Tarpinis rinkos kainos apskaičiavimas pagal lyginamą automobilį: ", 50, 595, normal_text);
        canvas.drawText(rinkosKainosApskaiciavimas.getText().toString(), 50, 615, bold_text);
        canvas.drawText("Galutinis rinkos kainos apskaičiavimas pagal techninę ir kosmetinę būklę: ", 50, 635, normal_text);
        canvas.drawText(galutineRinkosKaina.getText().toString(), 50, 655, bold_text);
        canvas.drawText("Automobilio nuvertėjimas: ", 50, 675, normal_text);
        canvas.drawText(nuvertejimoProcentas.getText().toString(), 50, 695, bold_text);
        canvas.drawLine(startX, 715, endX, 715, line);
        canvas.drawText("Eismo įvykyje nukentėjusių detalių remonto sąmata  ", 50, 735, skyr);
        canvas.drawText("Įvykyje apgadintos automobilio dalys: ", 50, 755, normal_text);
        canvas.drawText(removeFirstandLast(apgadintosDalys.getText().toString()), 50, 775, bold_text);
        canvas.drawText("Automobilio apgadintų dalių kainos: ", 50, 795, normal_text);
        canvas.drawText(daliuK.getText().toString(), 50, 815, bold_text);
        canvas.drawText("Automobilio apgadintų dalių remonto kainos: ", 50, 835, normal_text);
        canvas.drawText(daliuKeitimoK.getText().toString(), 50, 855, bold_text);
        canvas.drawText("Automobilio apgadintų dalių dažymo kainos: ", 50, 875, normal_text);
        canvas.drawText(daliuDazymoK.getText().toString(), 50, 895, bold_text);
        canvas.drawLine(startX, 905, endX, 905, line);
        canvas.drawText("Automobilio žalos kainos įvertinimas: ", 50, 925, skyr);
        canvas.drawText("Gautinė įvertinta žala: ", 50, 945, normal_text);
        canvas.drawText(ivertintaZala.getText().toString(), 50, 965, bold_text);

        pdfDocument.finishPage(myPage);

        File externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (!externalDir.exists()) {
            externalDir.mkdirs();
        }

        File file = new File(externalDir, "Ataskaita"+atasGen()+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(), "PDF failas sugeneruotas", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Įvyko klaida", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    public int gen()
    {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }
    public int atasGen()
    {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));
    }

    public static String removeFirstandLast(String str)
    {
        str = str.substring(1, str.length() - 1);
        return str;
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getActivity(), "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }
}