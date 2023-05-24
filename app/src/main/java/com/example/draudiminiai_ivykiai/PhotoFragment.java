package com.example.draudiminiai_ivykiai;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    Button kitas;
    private static final int SELECT_PICTURE = 200;
    FrameLayout frameLayout;
    ImageView imageView;
    Uri imageUri;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        kitas = (Button)view.findViewById(R.id.kitasB3);
        frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.selectImage);

        String numeris = getArguments().getString("Numeris");
        String time = getArguments().getString("Laikas");
        String date = getArguments().getString("Data");
        String address = getArguments().getString("Adresas");

        floatingActionButton.setOnClickListener(this);

        kitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                DamageFragment dfragment=new DamageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Numeris",numeris);
                bundle.putString("Laikas",time);
                bundle.putString("Data",date);
                bundle.putString("Adresas", address);
                bundle.putString("Uri", String.valueOf(imageUri));
                dfragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, dfragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {

        if (checkPermission()) {
            Toast.makeText(getActivity().getBaseContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            openImageChooser();
        } else {
            requestPermission();
        }
    }

    private void openImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pasirinkite nuotrauką"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                imageUri = data.getData();
                if(imageUri != null){
                    imageView.setImageURI(imageUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, SELECT_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SELECT_PICTURE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getActivity(), "Leidimas duotas..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Leidimas atmestas.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }

}