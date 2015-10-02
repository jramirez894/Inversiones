package com.example.billy.clientes;

/**
 * Created by BILLY on 24/09/2015.
 */
public class ItemListaProdutos_DatosCobro
{
    String nomProducto;
    int eliminar;

    public ItemListaProdutos_DatosCobro(String nomProducto, int eliminar) {
        this.nomProducto = nomProducto;
        this.eliminar = eliminar;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }
}
