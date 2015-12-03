package com.example.billy.clientes;

/**
 * Created by Administrador on 03/12/2015.
 */
public class ItemsVenta_AgregarCliente
{
    public String idVenta;
    public String total;
    public String cantidad;
    public String estado;
    public String idFactura;
    public String idProducto;

    public ItemsVenta_AgregarCliente(String idVenta, String total, String cantidad, String estado, String idFactura, String idProducto) {
        this.idVenta = idVenta;
        this.total = total;
        this.cantidad = cantidad;
        this.estado = estado;
        this.idFactura = idFactura;
        this.idProducto = idProducto;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
}
