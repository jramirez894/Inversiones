package com.example.billy.inversiones;

/**
 * Created by Admin_Sena on 21/10/2015.
 */
public class SesionUsuarios
{
    private static String cedula;
    private static String rol;
    private static String nombre;

    public static String getCedula() {
        return cedula;
    }

    public static void setCedula(String cedula) {
        SesionUsuarios.cedula = cedula;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        SesionUsuarios.rol = rol;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        SesionUsuarios.nombre = nombre;
    }
}
