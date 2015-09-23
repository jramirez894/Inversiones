package com.example.billy.inversiones;

/**
 * Created by Miguel on 23/09/2015.
 */
public class ItemsMenuDrawer
{
    private String nombre;
    private int icono;

    public ItemsMenuDrawer(String nombre, int icono) {
        this.nombre = nombre;
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
