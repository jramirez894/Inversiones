package com.example.billy.empleado;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class ItemListaEmpleado
{
    String nombre;
    int edita;
    int eliminar;
    String idUsuario;
    String cedulaEmp;
    String nombreEmp;
    String direccionEmp;
    String telefonoEmp;
    String correoEmp;
    String password;

    public ItemListaEmpleado(String nombre, int edita, int eliminar, String idUsuario, String cedulaEmp, String nombreEmp, String direccionEmp, String telefonoEmp, String correoEmp, String password)
    {
        this.nombre = nombre;
        this.edita = edita;
        this.eliminar = eliminar;
        this.idUsuario = idUsuario;
        this.cedulaEmp = cedulaEmp;
        this.nombreEmp = nombreEmp;
        this.direccionEmp = direccionEmp;
        this.telefonoEmp = telefonoEmp;
        this.correoEmp = correoEmp;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdita() {
        return edita;
    }

    public void setEdita(int edita) {
        this.edita = edita;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCedulaEmp() {
        return cedulaEmp;
    }

    public void setCedulaEmp(String cedulaEmp) {
        this.cedulaEmp = cedulaEmp;
    }

    public String getNombreEmp() {
        return nombreEmp;
    }

    public void setNombreEmp(String nombreEmp) {
        this.nombreEmp = nombreEmp;
    }

    public String getDireccionEmp() {
        return direccionEmp;
    }

    public void setDireccionEmp(String direccionEmp) {
        this.direccionEmp = direccionEmp;
    }

    public String getTelefonoEmp() {
        return telefonoEmp;
    }

    public void setTelefonoEmp(String telefonoEmp) {
        this.telefonoEmp = telefonoEmp;
    }

    public String getCorreoEmp() {
        return correoEmp;
    }

    public void setCorreoEmp(String correoEmp) {
        this.correoEmp = correoEmp;
    }
}
