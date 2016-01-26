package com.example.billy.constantes;

import android.widget.EditText;

import com.example.billy.clientes.ItemFactura_AgregarCliente;
import com.example.billy.clientes.ItemsVenta_AgregarCliente;
import com.example.billy.garantias_product.Items_Garantia;
import com.example.billy.productos.ItemsListaProductos_Productos;

import java.util.ArrayList;

/**
 * Created by Admin_Sena on 06/10/2015.
 */
public class Constantes
{
    public static String atrasHistorial = "";

    //Variables que contienen el editTex de la fecha
    public static String fechaInicio;
    public static String fechaFin;

    //Variables para ordenar la lista de clientes que tiene que visitar cada dia
    public static String EDITAR_LISTA = "Botones";
    public static String CERRAR_SESION = "CERRAR";
    public static ArrayList<String> arrayListIdVendedor = new ArrayList<String>();

    //-------------------------------------------------------------------------------
    //Tabla Cliente
    public static String idClienteCliente;
    public static String cedulaCliente;
    public static String nombreCliente;
    public static String direccionCliente;
    public static String telefonoCliente;
    public static String correoCliente;
    public static String nombreEmpresaCliente;
    public static String direccionEmpresaCliente;


    //Tabla Factura
    public static String idFactura;
    public static String fechaFactura;
    public static String totalFactura;
    public static String valorRestante;
    public static String fechaCobroFactura;
    public static String diaCobroFactura;
    public static String horaCobroFactura;
    public static String idVendedorFactura;
    public static String idClienteFactura;

    public static String interfaz ="";

    public static String nombreVendedorUsuarios;
    public static String telefonoVendedorUsuarios;

    //Array que guardara todas las factura del servidor
    public static ArrayList<ItemFactura_AgregarCliente> itemsFactura = new ArrayList<ItemFactura_AgregarCliente>();

    //Tabla Venta
    public static ArrayList<ItemsVenta_AgregarCliente> itemsVenta = new ArrayList<ItemsVenta_AgregarCliente>();

    //Tabla Productos
    public static ArrayList<ItemsListaProductos_Productos> itemsProductos = new ArrayList<ItemsListaProductos_Productos>();

    //para diferenciar si le damos click a editar un cliente o visualizar un cliente, para saber que id de factura traer
    public static String tipoConsultaidFactura = "";

    //Tabla Garantia
    public static ArrayList<Items_Garantia> itemsGarantias = new ArrayList<Items_Garantia>();
}
