package com.example.billy.garantias_product;

/**
 * Created by Carlos Andres on 26/01/2016.
 */
public class Items_Garantia_Visualizar
{
    String idGarantia;
    String nombre;
    String telefono;
    String nombreProducto;
    String cantidad;
    String fecha;
    String descripcion;
    String estado;
    String idVendedor;
    String idCliente;
    String idProducto;


    public Items_Garantia_Visualizar(String idGarantia, String nombre, String telefono, String nombreProducto, String cantidad, String fecha, String descripcion, String estado, String idVendedor, String idCliente, String idProducto) {
        this.idGarantia = idGarantia;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
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

    public String getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(String idGarantia) {
        this.idGarantia = idGarantia;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
