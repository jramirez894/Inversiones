package com.example.billy.inversiones;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class ItemListaroductos_MDatosCobro
{
    String nombre;
    int garantia;
    int devolucion;
    int eliminar;

    public ItemListaroductos_MDatosCobro(String nombre, int garantia, int devolucion, int eliminar)
    {
        this.nombre = nombre;
        this.garantia = garantia;
        this.devolucion = devolucion;
        this.eliminar = eliminar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getGarantia() {
        return garantia;
    }

    public void setGarantia(int garantia) {
        this.garantia = garantia;
    }

    public int getDevolucion()
    {
        return devolucion;
    }

    public void setDevolucion(int devolucion)
    {
        this.devolucion = devolucion;
    }

    public int getEliminar()
    {
        return eliminar;
    }

    public void setEliminar(int eliminar)
    {
        this.eliminar = eliminar;
    }
}
