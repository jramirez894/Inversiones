package com.example.billy.devolucion;

/**
 * Created by Admin_Sena on 20/10/2015.
 */
public class Items_Devolucion
{
    String nombre;
    String nomProducto;
    String fecha;
    String descripcion;
    int eliminar;

    public Items_Devolucion(String nombre, String nomProducto, String fecha, String descripcion, int eliminar) {
        this.nombre = nombre;
        this.nomProducto = nomProducto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.eliminar = eliminar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }
}
