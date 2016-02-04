package com.example.billy.saldo_caja;

/**
 * Created by jeniffer on 04/02/2016.
 */
public class Items
{
    public String idGasto;
    public String valor;
    public String fecha;
    public String descripcion;
    public String idUsuario;
    public String idTipoGasto;

    public Items(String idGasto, String valor, String fecha, String descripcion, String idUsuario, String idTipoGasto) {
        this.idGasto = idGasto;
        this.valor = valor;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.idUsuario = idUsuario;
        this.idTipoGasto = idTipoGasto;
    }

    public Items(String valor, String fecha) {
        this.valor = valor;
        this.fecha = fecha;
    }

    public String getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(String idGasto) {
        this.idGasto = idGasto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdTipoGasto() {
        return idTipoGasto;
    }

    public void setIdTipoGasto(String idTipoGasto) {
        this.idTipoGasto = idTipoGasto;
    }
}
