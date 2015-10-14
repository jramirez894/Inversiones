package com.example.billy.interfaces_empleado;

/**
 * Created by Admin_Sena on 14/10/2015.
 */
public class ItemLista_Clientes
{
    String nom;
    int editar;

    public ItemLista_Clientes(String nom, int editar)
    {
        this.nom = nom;
        this.editar = editar;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getEditar() {
        return editar;
    }

    public void setEditar(int editar) {
        this.editar = editar;
    }
}
