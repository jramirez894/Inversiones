package com.example.billy.gastos;

/**
 * Created by Admin_Sena on 06/10/2015.
 */
public class ItemLista_InfHistorial
{
    String descripcion;
    String valor;
    String tipoGasto;
    int icono;

    public ItemLista_InfHistorial(String descripcion, String valor, String tipoGasto, int icono) {
        this.descripcion = descripcion;
        this.valor = valor;
        this.tipoGasto = tipoGasto;
        this.icono = icono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
