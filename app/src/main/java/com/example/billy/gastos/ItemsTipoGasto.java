package com.example.billy.gastos;

/**
 * Created by Admin_Sena on 06/11/2015.
 */
public class ItemsTipoGasto
{
    String idTipoGasto;
    String nombre;
    String descripcion;

    public ItemsTipoGasto(String idTipoGasto, String nombre, String descripcion) {
        this.idTipoGasto = idTipoGasto;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getIdTipoGasto() {
        return idTipoGasto;
    }

    public void setIdTipoGasto(String idTipoGasto) {
        this.idTipoGasto = idTipoGasto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
