package com.example.billy.clientes;

/**
 * Created by Administrador on 03/12/2015.
 */
public class ItemsCobro_AgregarCliente
{
    public String idCobro;
    public String fecha;
    public String abono;
    public String idVendedor;
    public String idFactura;

    public ItemsCobro_AgregarCliente(String idCobro, String fecha, String abono, String idVendedor, String idFactura) {
        this.idCobro = idCobro;
        this.fecha = fecha;
        this.abono = abono;
        this.idVendedor = idVendedor;
        this.idFactura = idFactura;
    }

    public String getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(String idCobro) {
        this.idCobro = idCobro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAbono() {
        return abono;
    }

    public void setAbono(String abono) {
        this.abono = abono;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }
}
