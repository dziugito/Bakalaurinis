package com.example.draudiminiai_ivykiai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PlaceFragment extends Fragment implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    TextView t1,t2,t3, pazymetas_adresas;
    EditText adresas;
    Button ieskoti, kitas2;
    MapView map;
    ImageView pinas;
    GoogleMap mMap = null;
    String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    GoogleApiClient client;
    LocationRequest locationRequest;
    Marker currentMarker;
    LatLng startLatLng;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        t1 = (TextView) view.findViewById(R.id.textView);
        t2 = (TextView) view.findViewById(R.id.textView2);
        t3 = (TextView) view.findViewById(R.id.textView3);
        pazymetas_adresas = (TextView) view.findViewById(R.id.pazymetasAdresas);
        map = (MapView) view.findViewById(R.id.zemelapis);
        adresas = (EditText) view.findViewById(R.id.adresas);
        ieskoti = (Button) view.findViewById(R.id.ieskoti);
        pinas = (ImageView) view.findViewById(R.id.pin);
        kitas2 = (Button)view.findViewById(R.id.kitasB2);

        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        map.onCreate(mapViewBundle);
        map.getMapAsync(this);

        String numeris = getArguments().getString("Numeris");
        String time = getArguments().getString("Laikas");
        String date = getArguments().getString("Data");


        ieskoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findOnMap();
            }
        });

        kitas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                PhotoFragment pfragment=new PhotoFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Numeris",numeris);
                bundle.putString("Laikas",time);
                bundle.putString("Data",date);
                bundle.putString("Adresas",pazymetas_adresas.getText().toString());
                pfragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, pfragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map.onResume();
        mMap = googleMap;
        goToLocation(55.1735998,  23.8948016, 6);

        client = new GoogleApiClient.Builder(getActivity().getBaseContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        if(location == null){
            Toast.makeText(getActivity().getBaseContext(), "Lokacija nerasta", Toast.LENGTH_SHORT).show();
        }
        else {
            startLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            if(currentMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(startLatLng);
                currentMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15));
            }
            else{
                currentMarker.setPosition(startLatLng);
            }
        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mMap.getCameraPosition().target;
                if(currentMarker != null) {
                    currentMarker.remove();
                    currentMarker = mMap.addMarker(new MarkerOptions().position(center).title("Nauja pozicija"));
                    startLatLng = currentMarker.getPosition();

                    pazymetas_adresas.setText(getStringAddress(startLatLng.latitude, startLatLng.longitude));
                }
            }
        });

    }

    public String getStringAddress(Double lat, Double lng){
        String address = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;

    }

    public void goToLocation(double lat, double longi, int zoom){
        LatLng latLng = new LatLng(lat, longi);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mMap.moveCamera(update);
    }

    public void findOnMap(){

        Geocoder geocoder = new Geocoder(getActivity().getBaseContext());
        try{
            List<Address> addressList = geocoder.getFromLocationName(adresas.getText().toString(), 1);
            Address address = addressList.get(0);
            double lat = address.getLatitude();
            double lon = address.getLongitude();
            goToLocation(lat,lon,15);

            if(currentMarker!=null){
                currentMarker.remove();
            }

            MarkerOptions options = new MarkerOptions();
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options.position(new LatLng(lat, lon));
            currentMarker = mMap.addMarker(options);
            startLatLng = currentMarker.getPosition();

            pazymetas_adresas.setText(getStringAddress(startLatLng.latitude, startLatLng.longitude));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest().create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);

        if(ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}