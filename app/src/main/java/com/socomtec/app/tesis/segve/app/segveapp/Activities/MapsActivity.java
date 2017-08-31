package com.socomtec.app.tesis.segve.app.segveapp.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.socomtec.app.tesis.segve.app.segveapp.R;
import com.socomtec.app.tesis.segve.app.segveapp.Servicios.MyFirebaseMessagingService;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    LatLng Alerta;
    LinearLayout linearLayout;
    private GoogleMap mMap;
    private CameraUpdate camera;
    private final int MY_PERMISSIONS = 100;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    Location location;
    //String lat, lon;
    double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //linearLayout = (LinearLayout) findViewById(R.id.Linermap);


        SharedPreferences sharedPreferences = getSharedPreferences(MyFirebaseMessagingService.MyPREFERENCES, Context.MODE_PRIVATE);
        final String lat = sharedPreferences.getString("lat", null);
        final String lon = sharedPreferences.getString("lon", null);

        if(lat != null && lon != null){
            latitud = Double.parseDouble(lat);
            longitud = Double.parseDouble(lon);
            Alerta = new LatLng(latitud,longitud);
            //Toast.makeText(getApplicationContext(), "Alogo mal" + lat + " " +lon, Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        /*Alerta = new LatLng(-36.606835, -72.103385);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Alerta));
        camera = CameraUpdateFactory.newLatLngZoom(Alerta, 14);
        mMap.animateCamera(camera);*/
       if (Alerta != null){
            mMap.addMarker(new MarkerOptions().position(Alerta).title("Ayuda!!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Alerta));
            camera = CameraUpdateFactory.newLatLngZoom(Alerta, 15);
            mMap.animateCamera(camera);
            //mMap.setMyLocationEnabled(true);
        }else {
            Alerta = new LatLng(-36.606835, -72.103385);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Alerta));
            camera = CameraUpdateFactory.newLatLngZoom(Alerta, 14);
            mMap.animateCamera(camera);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Alerta")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }
        });

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Alerta!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }

    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedpreferences = getSharedPreferences(MyFirebaseMessagingService.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lat", null);
        editor.putString("lon", null);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedpreferences = getSharedPreferences(MyFirebaseMessagingService.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lat", null);
        editor.putString("lon", null);
        editor.apply();
    }

    /*private boolean mayRequestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) || (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION))) {
            Snackbar.make(linearLayout, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MY_PERMISSIONS);
        }

        return false;
    }*/

}
