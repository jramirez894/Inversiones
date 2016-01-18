package com.example.billy.clientes;

/**
 * Created by Administrador on 02/12/2015.
 */
public class ItemFactura_AgregarCliente
{
    public String idFactura;
    public String fecha;
    public String total;
    public String valorRestante;
    public String estado;
    public String fechaCobro;
    public String diaCobro;
    public String horaCobro;
    public String idVendedor;
    public String idCliente;

    public ItemFactura_AgregarCliente(String idFactura, String fecha, String total, String valorRestante, String estado, String fechaCobro, String diaCobro, String horaCobro, String idVendedor, String idCliente) {
        this.idFactura = idFactura;
        this.fecha = fecha;
        this.total = total;
        this.valorRestante = valorRestante;
        this.estado = estado;
        this.fechaCobro = fechaCobro;
        this.diaCobro = diaCobro;
        this.horaCobro = horaCobro;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
    }

    public String getValorRestante() {
        return valorRestante;
    }

    public void setValorRestante(String valorRestante) {
        this.valorRestante = valorRestante;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(String fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public String getDiaCobro() {
        return diaCobro;
    }

    public void setDiaCobro(String diaCobro) {
        this.diaCobro = diaCobro;
    }

    public String getHoraCobro() {
        return horaCobro;
    }

    public void setHoraCobro(String horaCobro) {
        this.horaCobro = horaCobro;
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
}
