package com.example.billy.garantias_product;

/**
 * Created by jeniffer on 18/02/2016.
 */
public class ItemsEstadoGarantia
{
    String idEstadoGarantia;
    String nombre;
    String cantidad;
    String idGarantia;

    public ItemsEstadoGarantia(String idEstadoGarantia, String nombre, String cantidad, String idGarantia) {
        this.idEstadoGarantia = idEstadoGarantia;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.idGarantia = idGarantia;
    }

    public String getIdEstadoGarantia() {
        return idEstadoGarantia;
    }

    public void setIdEstadoGarantia(String idEstadoGarantia) {
        this.idEstadoGarantia = idEstadoGarantia;
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

    public String getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(String idGarantia) {
        this.idGarantia = idGarantia;
    }
}
