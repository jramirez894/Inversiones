package com.example.billy.productos;

/**
 * Created by Admin_Sena on 02/10/2015.
 */
public class ItemsListaProductos_Productos
{
    String nombre;
    int editar;
    int eliminar;

    public ItemsListaProductos_Productos(String nombre, int editar, int eliminar)
    {
        this.nombre = nombre;
        this.editar = editar;
        this.eliminar = eliminar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEditar() {
        return editar;
    }

    public void setEditar(int editar) {
        this.editar = editar;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }
}
