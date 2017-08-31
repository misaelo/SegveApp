package com.socomtec.app.tesis.segve.app.segveapp.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.socomtec.app.tesis.segve.app.segveapp.Conexion.ConecLogin;
import com.socomtec.app.tesis.segve.app.segveapp.R;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;

public class LoadActivity extends AppCompatActivity {

    ConecLogin conecLogin = new ConecLogin();
    private final int MY_PERMISSIONS = 100;
    RelativeLayout relative;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        relative = (RelativeLayout) findViewById(R.id.activity_load);
        if (mayRequestStoragePermission()){
            myClickHandler(this.getApplicationContext());
        }else {
            Toast.makeText(getApplicationContext(),"Debe otorgar los Permisos necesarios",Toast.LENGTH_LONG).show();
        }
    }

    public void myClickHandler(Context view) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            conec();
        } else {
            Toast.makeText(getApplicationContext(),"Sin Conexion",Toast.LENGTH_LONG).show();
            //this.recreate();
        }
    }

    public void conec(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                final String User = sharedPreferences.getString("user", null);
                final String Pass = sharedPreferences.getString("pass", null);

                if (User == null && Pass == null){
                    //Toast.makeText(getApplicationContext(),"no hay session",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String resultado = conecLogin.EnviarDatos(User, Pass );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int r = conecLogin.Valida(resultado);
                                    if (r > 0) {
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        //Toast.makeText(getApplicationContext(), "Usuario o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        }, 3000);
    }

    private boolean mayRequestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(INTERNET)) || (shouldShowRequestPermissionRationale(ACCESS_NETWORK_STATE))) {
            Snackbar.make(relative, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{INTERNET,ACCESS_NETWORK_STATE}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{INTERNET,ACCESS_NETWORK_STATE}, MY_PERMISSIONS);
        }

        return false;
    }
}
