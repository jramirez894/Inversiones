package com.example.billy.productos;

/**
 * Created by Admin_Sena on 02/10/2015.
 */
public class ItemsListaProductos_Productos
{
    String nombre;
    int editar;
    int eliminar;
    String idProducto;
    String descripcion;
    String cantidad;
    String garantia;
    String precioCompra;
    String precioVenta;
    String idCategoria;

    public ItemsListaProductos_Productos(String nombre, int editar, int eliminar, String idProducto, String descripcion, String cantidad, String garantia, String precioCompra, String precioVenta, String idCategoria) {
        this.nombre = nombre;
        this.editar = editar;
        this.eliminar = eliminar;
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.garantia = garantia;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.idCategoria = idCategoria;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(String precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }
}
