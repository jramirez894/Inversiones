package com.example.billy.interfaces_empleado;

/**
 * Created by Admin_Sena on 08/10/2015.
 */
public class ItemPrincipalMenu
{
    int icono;
    String nombre;

    public ItemPrincipalMenu(int icono, String nombre)
    {
        this.icono = icono;
        this.nombre = nombre;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
