package com.socomtec.app.tesis.segve.app.segveapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.socomtec.app.tesis.segve.app.segveapp.Conexion.ConecLogin;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.HttpManager;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.URLs;
import com.socomtec.app.tesis.segve.app.segveapp.R;

public class LoginActivity extends AppCompatActivity {

    public EditText EdtUsuario, EdtPass;
    public AppCompatButton BtnIngresar;
    ConecLogin conecLogin = new ConecLogin();
    URLs urls = new URLs();
    public static final String MyPREFERENCES = "Login" ;
    public static final String Name = "user";
    public static final String Pass = "pass";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EdtUsuario = (EditText) findViewById(R.id.usuario);
        EdtPass = (EditText) findViewById(R.id.pass);
        BtnIngresar = (AppCompatButton) findViewById(R.id.ingresar);
        BtnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (EdtPass.getText().toString().isEmpty() && EdtUsuario.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe ingresar un usuario y contraseña", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "llama al metodo", Toast.LENGTH_SHORT).show();
                    myClickHandler();
                    //boton();
                }
            }
        });

    }

    public void myClickHandler() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            boton();
        } else {
            Toast.makeText(getApplicationContext(), "Sin Conexion", Toast.LENGTH_LONG).show();
            //this.recreate();
        }
    }
    public void boton(){
        progressDialog = ProgressDialog.show(LoginActivity.this, "Iniciando Sesión", "Por favor espere ...",false, false);
        final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String user = EdtUsuario.getText().toString();
                final String pas= EdtPass.getText().toString();
                final String resultado = conecLogin.EnviarDatos(user, pas );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = conecLogin.Valida(resultado);
                        if (r > 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Name, user);
                            editor.putString(Pass, pas);
                            editor.apply();
                            CargarDatos();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Usuario o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }

    public void PedirDatos(){
        MyTask task = new MyTask();
        task.execute();
    }

    public void CargarDatos(){
        progressDialog.dismiss();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

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
            CargarDatos();
        }
    }
}
