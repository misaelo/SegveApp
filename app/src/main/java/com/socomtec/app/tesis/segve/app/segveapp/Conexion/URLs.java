package com.socomtec.app.tesis.segve.app.segveapp.Conexion;

/**
 * Created by Misael on 01-jul-17.
 */

public class URLs {

    //public String server = "http://192.168.56.1:80/segve/webservice/";
    //public String imgServer = "http://192.168.56.1:80/segve/imagen/";
    //public String pagina = "http://www.segve.xyz:80/";
    //public String pagina = "http://186.78.105.193/segve/";
    public String pagina = "http://192.168.1.104:80/";
    public String server = pagina +"webservice/";

    public String urlLogin(String idUsuario, String pass){
        String url = server+"login.php?id="+idUsuario+"&pas="+pass;
        return url;
    }

    public String urlAlerta(){
        String url = server+"alerta.php";
        return url;
    }

    public String urlGuardarAlerta(){
        String url = server+"ingresa_alarma.php";
        return  url;
    }

    public String urlCaptura(){
        String url = server+"captura_imagen.php";
        return  url;
    }

    public String urlNumeros(String zona){
        String url = server+"numero_emergencia.php?zona="+zona;
        return  url;
    }

}
