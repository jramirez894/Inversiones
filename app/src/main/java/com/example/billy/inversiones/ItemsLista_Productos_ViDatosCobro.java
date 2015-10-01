package com.example.billy.inversiones;

/**
 * Created by Admin_Sena on 30/09/2015.
 */
public class ItemsLista_Productos_ViDatosCobro
{
    String nombre;
    String precio;

    public ItemsLista_Productos_ViDatosCobro(String nombre, String precio)
    {
        this.nombre = nombre;
        this.precio = precio;
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
}
