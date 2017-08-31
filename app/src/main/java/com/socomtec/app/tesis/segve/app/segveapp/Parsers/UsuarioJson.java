package com.socomtec.app.tesis.segve.app.segveapp.Parsers;

import com.socomtec.app.tesis.segve.app.segveapp.POJO.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misael on 01-jul-17.
 */

public class UsuarioJson {

    public static List<Usuario> parse(String content){
        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Usuario> usuarioList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Usuario usuario = new Usuario();

                usuario.setRut(jsonObject.getString("runP"));
                usuario.setNombre(jsonObject.getString("nombresP"));
                usuario.setIdUsuario(jsonObject.getInt("idUsuario"));
                usuario.setApellidos(jsonObject.getString("apellidosP"));
                usuario.setDireccion(jsonObject.getString("direccionP"));
                usuario.setCorreo(jsonObject.getString("correoP"));
                usuario.setTelefono(jsonObject.getInt("telefonoP"));
                usuario.setIdUsuario(jsonObject.getInt("idUsuario"));
                usuario.setFkUsuario(jsonObject.getString("fkPersona"));
                usuario.setPass(jsonObject.getString("contrasenaU"));
                usuario.setTipoUsuario(jsonObject.getInt("fkTipoUsuario"));
                usuario.setEstado(jsonObject.getInt("fkTipoEstado"));
                usuario.setFkZona(jsonObject.getString("fkZona"));

                usuarioList.add(usuario);
            }
            return usuarioList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
