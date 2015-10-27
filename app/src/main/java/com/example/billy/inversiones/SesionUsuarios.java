package com.example.billy.inversiones;

/**
 * Created by Admin_Sena on 21/10/2015.
 */
public class SesionUsuarios
{
    private static String cedula;
    private static String nombre;
    private static String telefono;
    private static String correo;
    private static String rol;
    private static String contrasena;
    private static String direccion;
    private static String idUsuario;

    public static String getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(String idUsuario) {
        SesionUsuarios.idUsuario = idUsuario;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion(String direccion) {
        SesionUsuarios.direccion = direccion;
    }

    public static String getContrasena() {
        return contrasena;
    }

    public static void setContrasena(String contrasena) {
        SesionUsuarios.contrasena = contrasena;
    }

    public static String getCedula() {
        return cedula;
    }

    public static void setCedula(String cedula) {
        SesionUsuarios.cedula = cedula;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        SesionUsuarios.nombre = nombre;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static void setTelefono(String telefono) {
        SesionUsuarios.telefono = telefono;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        SesionUsuarios.correo = correo;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        SesionUsuarios.rol = rol;
    }
}
