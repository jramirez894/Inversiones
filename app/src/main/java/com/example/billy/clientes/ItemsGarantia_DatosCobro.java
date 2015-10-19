package com.example.billy.clientes;

/**
 * Created by Admin_Sena on 19/10/2015.
 */
public class ItemsGarantia_DatosCobro
{
    String fechaDevolucion;
    String fechaGarantia;
    String descripcionDevolucion;
    String descripcionGarantia;

    public ItemsGarantia_DatosCobro(String fechaDevolucion, String fechaGarantia, String descripcionDevolucion, String descripcionGarantia) {
        this.fechaDevolucion = fechaDevolucion;
        this.fechaGarantia = fechaGarantia;
        this.descripcionDevolucion = descripcionDevolucion;
        this.descripcionGarantia = descripcionGarantia;
    }

    public String getFechaDevolucion()
    {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getFechaGarantia() {
        return fechaGarantia;
    }

    public void setFechaGarantia(String fechaGarantia) {
        this.fechaGarantia = fechaGarantia;
    }

    public String getDescripcionDevolucion() {
        return descripcionDevolucion;
    }

    public void setDescripcionDevolucion(String descripcionDevolucion) {
        this.descripcionDevolucion = descripcionDevolucion;
    }

    public String getDescripcionGarantia() {
        return descripcionGarantia;
    }

    public void setDescripcionGarantia(String descripcionGarantia) {
        this.descripcionGarantia = descripcionGarantia;
    }
}
