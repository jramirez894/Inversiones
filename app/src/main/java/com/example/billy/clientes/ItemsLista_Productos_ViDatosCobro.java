package com.example.billy.clientes;

/**
 * Created by Admin_Sena on 30/09/2015.
 */
public class ItemsLista_Productos_ViDatosCobro
{
    String nombre;
    String precio;
    String cantidad;

    public ItemsLista_Productos_ViDatosCobro(String nombre, String precio, String cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
