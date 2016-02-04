package com.example.billy.inversiones;

/**
 * Created by jeniffer on 04/02/2016.
 */
public class ItemsUsuario
{
    String idUsuario;
    String cedula;
    String nnombre;
    String telefono;
    String correo;
    String direccion;
    String password;
    String tipoUsuario;

    public ItemsUsuario(String idUsuario, String cedula, String nnombre, String telefono, String correo, String direccion, String password, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.nnombre = nnombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNnombre() {
        return nnombre;
    }

    public void setNnombre(String nnombre) {
        this.nnombre = nnombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
