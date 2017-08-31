package com.socomtec.app.tesis.segve.app.segveapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.HttpManager;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.RequestPackage;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.URLs;
import com.socomtec.app.tesis.segve.app.segveapp.POJO.Usuario;
import com.socomtec.app.tesis.segve.app.segveapp.Parsers.UsuarioJson;
import com.socomtec.app.tesis.segve.app.segveapp.R;
import com.socomtec.app.tesis.segve.app.segveapp.dummy.DummyContent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {
    String token;
    TextView txtNombre, txtCorreo;
    List<Usuario> usuarioList;
    URLs urls = new URLs();
    CardView numeros, cdvAlertaRapida, cdvIncendio, cdvRobo, cdvViolencia, cdvRoboV;

    TextView tokensito;
    int TipoUsuario;
    String Latitud, Longitud, Fecha;

    DummyContent dummyContent = new DummyContent();

    private static final String LOGTAG = "android-localizacion";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    private GoogleApiClient apiClient;

    public String Incendio = "Ayuda, incendio!!!";
    public String Asalto = "Alerta Rapida, Ayuda!!";
    public String Violencia = "Alerta de violencia intrafamiliar!";
    public String Robo = "Ayuda estan robando mi casa!";
    public String RoboV = "Ayuda estan robando mi vehiculo!";

    private LocationRequest locRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseMessaging.getInstance().subscribeToTopic("TopicName");
        dummyContent.PedirDatos(urls.urlNumeros("2"));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MM");
        SimpleDateFormat year_date = new SimpleDateFormat("yyyy");
        SimpleDateFormat day_date = new SimpleDateFormat("dd");
        Fecha = year_date.format(calendar.getTime())+ "-" +month_date.format(calendar.getTime())+ "-" +day_date.format(calendar.getTime());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        txtNombre = (TextView) header.findViewById(R.id.Nombre);
        txtCorreo = (TextView) header.findViewById(R.id.Correo);

        numeros = (CardView) findViewById(R.id.numeros);
        cdvAlertaRapida = (CardView) findViewById(R.id.AlertaRapidaCard);
        cdvIncendio = (CardView) findViewById(R.id.IncendioCard);
        cdvRobo = (CardView) findViewById(R.id.RoboCard);
        cdvRoboV = (CardView) findViewById(R.id.RoboVCard);
        cdvViolencia = (CardView) findViewById(R.id.ViolenciaCard);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final String User = sharedPreferences.getString("user", null);
        final String Pass = sharedPreferences.getString("pass", null);

        Log.d("FECHAAAAAA!!!!", Fecha);

        PedirDatos(urls.urlLogin(User, Pass));

        cdvAlertaRapida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocationUpdates();
                PedirAlerta(urls.urlAlerta(), txtNombre.getText().toString(), Asalto ,Latitud , Longitud);
                GuardarAlerta(urls.urlGuardarAlerta(), "1");
            }
        });
        cdvIncendio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocationUpdates();
                PedirAlerta(urls.urlAlerta(), txtNombre.getText().toString(), Incendio,Latitud , Longitud);
                GuardarAlerta(urls.urlGuardarAlerta(), "5");
            }
        });
        cdvRobo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocationUpdates();
                PedirAlerta(urls.urlAlerta(), txtNombre.getText().toString(), Robo,Latitud , Longitud);
                GuardarAlerta(urls.urlGuardarAlerta(), "3");
            }
        });

        cdvRoboV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocationUpdates();
                PedirAlerta(urls.urlAlerta(), txtNombre.getText().toString(), RoboV,Latitud , Longitud);
                GuardarAlerta(urls.urlGuardarAlerta(), "2");
            }
        });
        cdvViolencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocationUpdates();
                PedirAlerta(urls.urlAlerta(), txtNombre.getText().toString(), Violencia,Latitud , Longitud);
                GuardarAlerta(urls.urlGuardarAlerta(), "4");
            }
        });

        numeros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NumerosActivity.class);
                startActivity(i);
            }
        });

        //Construcción cliente API Google
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void toggleLocationUpdates() {
        //if (enable) {
            enableLocationUpdates();
        //} else {
            disableLocationUpdates();
        //}
    }

    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(MainActivity.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            //btnActualizar.setChecked(false);
                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        //btnActualizar.setChecked(false);
                        break;
                }
            }
        });
    }

    private void disableLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, locRequest, MainActivity.this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGTAG, "Recibida nueva ubicación!");

        //Mostramos la nueva ubicación recibida
        updateUI(location);
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            final String User = sharedPreferences.getString("paciente", null);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Paciente/"+User);


            Map<String,Object> ubica=  new HashMap<String, Object>();
            ubica.put("latitud",loc.getLatitude());
            ubica.put("longitud",loc.getLongitude());
            myRef.updateChildren(ubica);

            Latitud = String.valueOf(loc.getLatitude());
            Longitud = String.valueOf(loc.getLongitude());
            //PedirDatos(urls.server+"ubicacion_paciente.php", loc.getLatitude(), loc.getLongitude());
        } else {
            Latitud = "Latitud: (desconocida)";
            Longitud = "Longitud: (desconocida)";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        //btnActualizar.setChecked(false);
                        break;
                }
                break;
        }
    }

    private class MyTaskAlerta extends AsyncTask<RequestPackage, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //lo que se muestra al inicio
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            //lo que se ve al final
            String content = HttpManager.getDataEnvio(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //int re = result.length();
            Toast.makeText(getApplicationContext(), "Alerta Emitida", Toast.LENGTH_LONG).show();
            //tokensito.setText(token);
            /*if (re == 3) {
                CargarAlerta();
            } else {
                Toast.makeText(getApplicationContext(), "Error al Cargar Alerta", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    public void PedirAlerta(String uri, String Titulo, String Cuerpo, String latitud, String longitud) {
        MyTaskAlerta task = new MyTaskAlerta();
        RequestPackage Req = new RequestPackage();
        Req.setMethod("GET");
        token = FirebaseInstanceId.getInstance().getToken();
        //String tokenDos = FirebaseApp;
        //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
        //Log.d("noti", "Token: " + token);
        Req.setParam("id", token);
        Req.setParam("ti", Titulo);
        Req.setParam("bo", Cuerpo);
        Req.setParam("lat", latitud);
        Req.setParam("lon", longitud);
        Req.setUri(uri);
        task.execute(Req);
    }

    public void CargarAlerta() {
        Toast.makeText(getApplicationContext(), "Coordenadas Guardadas", Toast.LENGTH_LONG).show();
    }

    public void PedirDatos(String uri){
        MyTask task = new MyTask();
        task.execute(uri);
    }

    public void CargarDatos(){
        if (usuarioList != null){
            for (final Usuario usuario:usuarioList) {
                txtCorreo.setText(usuario.getCorreo());
                txtNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
                //tokensito.setText(usuario.getTipoUsuario());
                TipoUsuario = usuario.getTipoUsuario();
                Log.d("noQQQQQQQQti", "Tipo; +" + TipoUsuario );
                //Picasso.with(getApplicationContext()).load(cuidador.getRutaImagen()).into(imgUser);
            }
        }else {
            Toast.makeText(getApplicationContext(),"No se pudo Cargar Datos",Toast.LENGTH_LONG).show();
        }
    }

    /*public void CargarCuidadores(){
        Intent i = new Intent(getApplicationContext(), CuidadorListActivity.class);
        startActivity(i);
    }*/

    private class MyTask extends AsyncTask<String, String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //lo que se muestra al inicio
        }
        @Override
        protected String doInBackground(String... params) {
            //lo que se ve al final
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            usuarioList = UsuarioJson.parse(result);
            CargarDatos();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.logout){
            Logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ChatMenu) {
            // Handle the camera action
            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
            i.putExtra("nombre",txtNombre.getText());
            startActivity(i);
        } else if (id == R.id.VideoVigilanciaMenu) {
            Intent i = new Intent(getApplicationContext(), VideoActivity.class);
            startActivity(i);
        } else if (id == R.id.MapaMenu) {
            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void Logout(){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user", null);
        editor.putString("pass", null);
        editor.apply();

        Toast.makeText(getApplicationContext(),"Session Cerrada",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void GuardarAlerta(String uri, String tipoA){
        MyTaskAlarma task = new MyTaskAlarma();
        RequestPackage Req =new RequestPackage();
        Req.setMethod("GET");
        Req.setUri(uri);
        Req.setParam("tip", tipoA);
        Req.setParam("usu", TipoUsuario+"");
        Req.setParam("lat", Latitud);
        Req.setParam("lon", Longitud);
        Req.setParam("fec", Fecha);
        task.execute(Req);
    }

    private class MyTaskAlarma extends AsyncTask<RequestPackage, String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //lo que se muestra al inicio
        }
        @Override
        protected String doInBackground(RequestPackage... params) {
            //lo que se ve al final
            String content = HttpManager.getDataEnvio(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int re = result.length();
            if (re ==3){
                Toast.makeText(getApplicationContext(),"Se guardo Alerta",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Error al actualizar Datos",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
