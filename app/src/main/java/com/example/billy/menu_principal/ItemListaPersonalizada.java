package com.example.billy.menu_principal;

/**
 * Created by BILLY on 22/09/2015.
 */
public class ItemListaPersonalizada
{
    public String nombreLista;
    public int editar;
    public int eliminar;
    public String ediOrganizar;

    public ItemListaPersonalizada(String nombreLista, int editar, int eliminar, String ediOrganizar)
    {
        this.nombreLista = nombreLista;
        this.editar = editar;
        this.eliminar = eliminar;
        this.ediOrganizar = ediOrganizar;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public int getEditar() {
        return editar;
    }

    public void setEditar(int editar) {
        this.editar = editar;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }

    public String getEdiOrganizar()
    {
        return ediOrganizar;
    }

    public void setEdiOrganizar(String ediOrganizar)
    {
        this.ediOrganizar = ediOrganizar;
    }
}

