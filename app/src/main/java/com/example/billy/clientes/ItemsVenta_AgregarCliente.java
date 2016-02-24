package com.example.billy.clientes;

/**
 * Created by Administrador on 03/12/2015.
 */
public class ItemsVenta_AgregarCliente
{
    public String idVenta;
    public String total;
    public String cantidad;
    public String cantidadGarantia;
    public String cantidadDevolucion;
    public String estado;
    public String idFactura;
    public String idProducto;
    public String nuevaCantidad;

    public ItemsVenta_AgregarCliente(String idVenta, String total, String cantidad, String cantidadGarantia, String cantidadDevolucion, String estado, String idFactura, String idProducto, String nuevaCantidad) {
        this.idVenta = idVenta;
        this.total = total;
        this.cantidad = cantidad;
        this.cantidadGarantia = cantidadGarantia;
        this.cantidadDevolucion = cantidadDevolucion;
        this.estado = estado;
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.nuevaCantidad = nuevaCantidad;
    }

    public String getCantidadGarantia() {
        return cantidadGarantia;
    }

    public void setCantidadGarantia(String cantidadGarantia) {
        this.cantidadGarantia = cantidadGarantia;
    }

    public String getCantidadDevolucion() {
        return cantidadDevolucion;
    }

    public void setCantidadDevolucion(String cantidadDevolucion) {
        this.cantidadDevolucion = cantidadDevolucion;
    }

    public String getNuevaCantidad() {
        return nuevaCantidad;
    }

    public void setNuevaCantidad(String nuevaCantidad) {
        this.nuevaCantidad = nuevaCantidad;
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
