package com.socomtec.app.tesis.segve.app.segveapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.HttpManager;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.RequestPackage;
import com.socomtec.app.tesis.segve.app.segveapp.Conexion.URLs;
import com.socomtec.app.tesis.segve.app.segveapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class VideoActivity extends AppCompatActivity {

    URLs urls = new URLs();
    private Bitmap myBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final String User = sharedPreferences.getString("user", null);

        final WebView myWebView = (WebView) this.findViewById(R.id.webViewqq);
        myWebView.loadUrl("http://192.168.1.82/mjpeg.cgi");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myBitmap = captureScreen(myWebView);

                Toast.makeText(getApplicationContext(), "Screenshot captured..!", Toast.LENGTH_LONG).show();

                try {
                    if(myBitmap!=null){
                        //save image to SD card
                        saveImage(myBitmap);
                    }
                    Toast.makeText(getApplicationContext(), "Screenshot saved..!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               // Captura(urls.urlCaptura(), User);

            }
        });
    }

    public static Bitmap captureScreen(View v) {

        Bitmap screenshot = null;
        try {

            if(v!=null) {

                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(),v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }

        }catch (Exception e){
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }

        return screenshot;
    }

    public static void saveImage(Bitmap bitmap) throws IOException{

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.png");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
    }


    private class MyTask extends AsyncTask<RequestPackage, String, String> {
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
            //Toast.makeText(getApplicationContext(), "Alerta Emitida", Toast.LENGTH_LONG).show();
            //tokensito.setText(token);
            /*if (re == 3) {
                CargarAlerta();
            } else {
                Toast.makeText(getApplicationContext(), "Error al Cargar Alerta", Toast.LENGTH_LONG).show();
            }*/
        }
    }
/*
    private void captureScreen(View view) {
        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment
                    .getExternalStorageDirectory().toString(), "SCREEN"
                    + System.currentTimeMillis() + ".jpg"));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Snackbar.make(view, "Se a guardado imagen", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeScreenshot(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().getAbsoluteFile().toString() + "/screenshots/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Snackbar.make(view, "Se a guardado imagen"+ mPath, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }*/

    public void Captura(String uri, String id) {
        VideoActivity.MyTask task = new VideoActivity.MyTask();
        RequestPackage Req = new RequestPackage();
        Req.setMethod("GET");
        Req.setParam("id", id);
        Req.setUri(uri);
        task.execute(Req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
