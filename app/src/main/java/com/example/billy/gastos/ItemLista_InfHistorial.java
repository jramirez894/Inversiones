package com.example.billy.gastos;

/**
 * Created by Admin_Sena on 06/10/2015.
 */
public class ItemLista_InfHistorial
{
    String descripcion;
    String valor;
    String tipoGasto;

    public ItemLista_InfHistorial(String descripcion, String valor, String tipoGasto)
    {
        this.descripcion = descripcion;
        this.valor = valor;
        this.tipoGasto = tipoGasto;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
