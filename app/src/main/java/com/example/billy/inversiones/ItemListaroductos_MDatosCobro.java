package com.example.billy.inversiones;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class ItemListaroductos_MDatosCobro
{
    String nombre;
    int eliminar;

    public ItemListaroductos_MDatosCobro(String nombre, int eliminar)
    {
        this.nombre = nombre;
        this.eliminar = eliminar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }
}
