package com.example.billy.menu_principal;

/**
 * Created by BILLY on 22/09/2015.
 */
public class ItemListaPersonalizada
{
    public String nombreLista;
    public int editar;
    public String ediOrganizar;

    public String idCliente;
    public String cedula;
    public String direccion;
    public String telefono;
    public String correo;
    public String nombreEmpresa;
    public String direccionEmpresa;
    public String estado;
    public String calificacion;

    public ItemListaPersonalizada(String nombreLista, int editar, String ediOrganizar, String idCliente, String cedula, String direccion, String telefono, String correo, String nombreEmpresa, String direccionEmpresa, String estado, String calificacion) {
        this.nombreLista = nombreLista;
        this.editar = editar;
        this.ediOrganizar = ediOrganizar;
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.nombreEmpresa = nombreEmpresa;
        this.direccionEmpresa = direccionEmpresa;
        this.estado = estado;
        this.calificacion = calificacion;
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

    public String getEdiOrganizar() {
        return ediOrganizar;
    }

    public void setEdiOrganizar(String ediOrganizar) {
        this.ediOrganizar = ediOrganizar;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }
}

