package com.example.billy.clientes;

/**
 * Created by BILLY on 24/09/2015.
 */
public class ItemListaProdutos_DatosCobro
{
    String nomProducto;
    int info;
    int eliminar;

    public ItemListaProdutos_DatosCobro(String nomProducto, int info, int eliminar) {
        this.nomProducto = nomProducto;
        this.info = info;
        this.eliminar = eliminar;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }
}
