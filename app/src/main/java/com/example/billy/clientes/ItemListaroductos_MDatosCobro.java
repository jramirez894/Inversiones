package com.example.billy.clientes;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class ItemListaroductos_MDatosCobro
{
    String nombre;
    int garantia;
    int devolucion;
    int eliminar;
    String estado;
    String cantidad;
    String idVenta;

    public ItemListaroductos_MDatosCobro(String nombre, int garantia, int devolucion, int eliminar, String estado, String cantidad, String idVenta, String cantidadAdicional) {
        this.nombre = nombre;
        this.garantia = garantia;
        this.devolucion = devolucion;
        this.eliminar = eliminar;
        this.estado = estado;
        this.cantidad = cantidad;
        this.idVenta = idVenta;
        this.cantidadAdicional = cantidadAdicional;
    }

    String cantidadAdicional;



    public String getCantidadAdicional() {
        return cantidadAdicional;
    }

    public void setCantidadAdicional(String cantidadAdicional) {
        this.cantidadAdicional = cantidadAdicional;
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

    public int getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(int devolucion) {
        this.devolucion = devolucion;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }
}
