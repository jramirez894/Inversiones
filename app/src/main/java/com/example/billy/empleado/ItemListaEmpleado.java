package com.example.billy.empleado;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class ItemListaEmpleado
{
    String nombre;
    int edita;
    int eliminar;

    public ItemListaEmpleado(String nombre, int edita, int eliminar) {
        this.nombre = nombre;
        this.edita = edita;
        this.eliminar = eliminar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdita() {
        return edita;
    }

    public void setEdita(int edita) {
        this.edita = edita;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }
}
