package com.socomtec.app.tesis.segve.app.segveapp.POJO;

/**
 * Created by Misael on 01-jul-17.
 */

public class Usuario {
    private int IdUsuario;
    private String Rut;
    private String Nombre;
    private String Apellidos;
    private String Direccion;
    private String Correo;
    private int Telefono;
    private String fkUsuario;
    private String fkZona;
    private String Pass;

    public String getFkZona() {
        return fkZona;
    }

    public void setFkZona(String fkZona) {
        this.fkZona = fkZona;
    }

    private int TipoUsuario;
    private int Estado;

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getRut() {
        return Rut;
    }

    public void setRut(String rut) {
        Rut = rut;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int telefono) {
        Telefono = telefono;
    }

    public String getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(String fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public int getTipoUsuario() {
        return TipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        TipoUsuario = tipoUsuario;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }
}
