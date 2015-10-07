package com.example.billy.clientes;

/**
 * Created by Admin_Sena on 07/10/2015.
 */
public class Item_ClienteHistorial
{
    String fecha;
    String valor;

    public Item_ClienteHistorial(String fecha, String valor)
    {
        this.fecha = fecha;
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
