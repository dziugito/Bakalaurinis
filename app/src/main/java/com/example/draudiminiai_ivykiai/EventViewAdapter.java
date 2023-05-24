package com.example.draudiminiai_ivykiai;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.draudiminiai_ivykiai.dataStructure.Reports;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventViewAdapter extends ArrayAdapter<Reports> {

    public EventViewAdapter(@NonNull Context context, ArrayList<Reports> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_event_view, parent, false);
        }

        Reports reports = getItem(position);

        TextView savininkas = listitemView.findViewById(R.id.savininkas);
        TextView automobilis = listitemView.findViewById(R.id.automobilis);
        TextView numeris = listitemView.findViewById(R.id.valstybinis);
        TextView vieta = listitemView.findViewById(R.id.adresas);
        TextView laikas = listitemView.findViewById(R.id.laikas);
        TextView data = listitemView.findViewById(R.id.data);
        TextView kaina = listitemView.findViewById(R.id.kaina);

        ImageView nuotrauka = listitemView.findViewById(R.id.nuotrauka);

        savininkas.setText(reports.getSavininkas());
        automobilis.setText(reports.getAutomobilis());
        numeris.setText(reports.getValstybinis_numeris());
        vieta.setText(reports.getAdresas());
        laikas.setText(reports.getLaikas());
        data.setText(reports.getData());
        kaina.setText(String.valueOf(reports.getIsviso_kaina()) + " Eur");
        Picasso.get().load(reports.getNuotrauka()).into(nuotrauka);

        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Įvykio registracija : " + reports.getRegistravimo_data(), Toast.LENGTH_SHORT).show();
            }
        });
        return listitemView;
    }
}
