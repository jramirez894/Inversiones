package com.example.billy.devolucion;

/**
 * Created by jeniffer on 18/02/2016.
 */
public class ItemsEstadoDevolucion
{
    String idEstadoDevolucion;
    String nombre;
    String cantidad;
    String idDevolucion;

    public ItemsEstadoDevolucion(String idEstadoDevolucion, String nombre, String cantidad, String idDevolucion) {
        this.idEstadoDevolucion = idEstadoDevolucion;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.idDevolucion = idDevolucion;
    }

    public String getIdEstadoDevolucion() {
        return idEstadoDevolucion;
    }

    public void setIdEstadoDevolucion(String idEstadoDevolucion) {
        this.idEstadoDevolucion = idEstadoDevolucion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(String idDevolucion) {
        this.idDevolucion = idDevolucion;
    }
}
