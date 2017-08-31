package com.socomtec.app.tesis.segve.app.segveapp.Parsers;

import com.socomtec.app.tesis.segve.app.segveapp.POJO.Numero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misael on 15-jul-17.
 */

public class NumeroJson {
    public static List<Numero> parse(String content){
        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Numero> numeroListList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Numero numero = new Numero();

                numero.setNombre(jsonObject.getString("nombre"));
                numero.setNumero(jsonObject.getString("numero"));

                numeroListList.add(numero);
            }
            return numeroListList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
