package com.example.billy.garantias_product;

/**
 * Created by Admin_Sena on 20/10/2015.
 */
public class Items_Garantia
{
    String idGarantia;
    String estado;
    String descripcion;
    String fecha;
    String cantidad;
    String idVendedor;
    String idCliente;
    String idProducto;

    public Items_Garantia(String idGarantia, String estado, String descripcion, String fecha, String cantidad, String idVendedor, String idCliente, String idProducto) {
        this.idGarantia = idGarantia;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(String idGarantia) {
        this.idGarantia = idGarantia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
}
