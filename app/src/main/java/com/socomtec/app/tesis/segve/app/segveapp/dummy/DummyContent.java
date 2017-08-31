package com.socomtec.app.tesis.segve.app.segveapp.dummy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.socomtec.app.tesis.segve.app.segveapp.Conexion.HttpManager;
import com.socomtec.app.tesis.segve.app.segveapp.POJO.Numero;
import com.socomtec.app.tesis.segve.app.segveapp.Parsers.NumeroJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
    List<Numero> numeroList;
    ProgressDialog progressDialog;
    Context context;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    public void PedirDatos(String uri){
        MyTask task = new MyTask();
        task.execute(uri);
    }

    public void CargarDatos(){
        if (numeroList != null){
            ITEMS.clear();
            for (final Numero numero:numeroList) {
                //Log.e("Cuidador", "biene "+ cuidador.getNombre());
                addItem(new DummyItem(numero.getNombre(),numero.getNumero()));
            }
        }else {
            Toast.makeText(context,"Error al Cargar Cuidadores", Toast.LENGTH_LONG).show();
        }
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
            numeroList = NumeroJson.parse(result);
            CargarDatos();
            //progressDialog.dismiss();
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.nombre, item);
    }
    /*
    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String nombre;
        public final String numero;


        public DummyItem(String nombre, String numero) {
            this.nombre = nombre;
            this.numero = numero;
        }

        @Override
        public String toString() {

            return nombre +" "+ numero;
        }
    }
}
