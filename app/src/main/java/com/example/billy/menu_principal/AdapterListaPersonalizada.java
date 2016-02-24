package com.example.billy.menu_principal;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.clientes.ItemFactura_AgregarCliente;
import com.example.billy.clientes.ItemsVenta_AgregarCliente;
import com.example.billy.clientes.ModificarCliente;
import com.example.billy.clientes.VisualizarCliente;
import com.example.billy.constantes.Constantes;
import com.example.billy.devolucion.ItemsEstadoDevolucion;
import com.example.billy.devolucion.Items_Devolucion;
import com.example.billy.garantias_product.ItemsEstadoGarantia;
import com.example.billy.garantias_product.Items_Garantia;
import com.example.billy.inversiones.R;
import com.example.billy.productos.ItemsListaProductos_Productos;
import com.example.billy.productos.M_Producto;
import com.example.billy.productos.Productos;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BILLY on 22/09/2015.
 */
public class AdapterListaPersonalizada extends ArrayAdapter
{

    public static ItemListaPersonalizada posicionItems;
    public static ImageView editar;
    public static EditText organizar;
    public static TextView nombreLista;
    public static Spinner spinOrden;

    boolean resul;

    String respuesta = "";
    String idCliente;

    //Alerta Cargando
    ProgressDialog progressDialog;
    boolean existe = false;


    public AdapterListaPersonalizada(Context context, List objects)
    {
        super(context, 0, objects);
    }
    //Tabla Usuarios
    String nombreVendedorUsuarios;
    String telefonoVendedorUsuarios;

    //Tabla DatosPersonales
    String idClienteCliente = "";
    String cedula = "";
    String nombre = "";
    String direccion = "";
    String telefono = "";
    String correo = "";
    String nombreEmpresa = "";
    String direccionEmpresa = "";
    String estado = "";
    String calificacion = "";

    //Tabla Factura
    public static String idFactura;
    String fechaFactura;
    String totalFactura;
    String valorRestante;
    String fechaCobroFactura;
    String diaCobroFactura;
    String horaCobroFactura;
    String idVendedorFactura;
    String idClienteFactura;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_personalizada,null);
        }

        switch (Constantes.EDITAR_LISTA)
        {
            case "Botones":

                ItemListaPersonalizada items = (ItemListaPersonalizada)getItem(position);

                nombreLista = (TextView)convertView.findViewById(R.id.textViewNombreListaPersonalizada);
                editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaPersonalizada);
                organizar = (EditText)convertView.findViewById(R.id.edit_OrganizarListaPersonalizada);
                spinOrden = (Spinner)convertView.findViewById(R.id.spinOrdenListaPersonalizada);

                nombreLista.setText(items.getNombreLista());
                editar.setImageResource(items.getEditar());
                organizar.setText(items.getEdiOrganizar());

                editar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        posicionItems = (ItemListaPersonalizada) getItem(position);

                        //Abrir ModificarCliente
                        idClienteCliente = posicionItems.getIdCliente();
                        cedula =  posicionItems.getCedula();
                        nombre =  posicionItems.getNombreLista();
                        direccion = posicionItems.getDireccion();
                        telefono = posicionItems.getTelefono();
                        correo = posicionItems.getCorreo();
                        nombreEmpresa = posicionItems.getNombreEmpresa();
                        direccionEmpresa = posicionItems.getDireccionEmpresa();
                        estado = posicionItems.getEstado();
                        calificacion = posicionItems.getCalificacion();

                        AlertaCargando();

                        TareaGetBill tareaGetBill = new TareaGetBill();
                        tareaGetBill.execute();

                        Constantes.tipoConsultaidFactura = "Modificar";
                    }
                });

                organizar.setVisibility(View.GONE);
                spinOrden.setVisibility(View.GONE);
                editar.setVisibility(View.VISIBLE);

                break;

            case "EditText":

                ItemListaPersonalizada items2 = (ItemListaPersonalizada)getItem(position);

                nombreLista = (TextView)convertView.findViewById(R.id.textViewNombreListaPersonalizada);
                editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaPersonalizada);
                organizar = (EditText)convertView.findViewById(R.id.edit_OrganizarListaPersonalizada);
                spinOrden = (Spinner)convertView.findViewById(R.id.spinOrdenListaPersonalizada);

                nombreLista.setText(items2.getNombreLista());
                organizar.setText(items2.getEdiOrganizar());

                organizar.setVisibility(View.VISIBLE);
                spinOrden.setVisibility(View.VISIBLE);
                editar.setVisibility(View.GONE);

                ArrayList<String> arrayLimiteOrden = new ArrayList<String>();
                arrayLimiteOrden.clear();

                for(int i = 0; i < PrincipalMenu.items.size(); i++)
                {
                    arrayLimiteOrden.add(String.valueOf(i+1));
                }

                spinOrden.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayLimiteOrden));

                break;
        }

        /*organizar = (EditText)convertView.findViewById(R.id.edit_OrganizarListaPersonalizada);

        organizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final ItemListaPersonalizada items3 = (ItemListaPersonalizada) getItem(position);

                LayoutInflater inflaterAlert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialoglayout = inflaterAlert.inflate(R.layout.alert_dialog_lista_personalizada_ordenar, null);

                final EditText editOrden = (EditText) dialoglayout.findViewById(R.id.editOrdenAlert);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialoglayout);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String ordendAlert = editOrden.getText().toString();
                        items3.setEdiOrganizar(ordendAlert);
                        ArrayAdapter adapter = new AdapterListaPersonalizada(getContext(), PrincipalMenu.items);
                        //Se carga de nuevo la vista
                        PrincipalMenu.listaClientes.setAdapter(adapter);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });*/

        return convertView;
    }

    public void AlertaCargando()
    {
        //Alerta que carga mientras se cargan los Clientes
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
    }

    //Clases Asyntask para traer las facturas
    private class TareaGetBill extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerFactura.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllBill"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFacturas = objItems.getJSONArray(0);

                //String obj
                String respuesta= String.valueOf(objFacturas);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }

                else
                {
                    Constantes.itemsFactura.clear();
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);
                        Constantes.itemsFactura.add(new ItemFactura_AgregarCliente(obj.getString("idFactura"), obj.getString("fecha"), obj.getString("total"), obj.getString("valorRestante"), obj.getString("estado"), obj.getString("fechaCobro"), obj.getString("diaCobro"), obj.getString("horaCobro"), obj.getString("idVendedor"), obj.getString("idCliente")));
                        existe = true;
                    }
                }
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                existe = false;
            } catch (JSONException e) {
                e.printStackTrace();
                existe = false;
            }

            return existe;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(AgregarCliente.this, respStr.toString(), Toast.LENGTH_SHORT).show();
            if (existe)
            {
                for(int i = 0; i < Constantes.itemsFactura.size(); i++)
                {
                    if(idClienteCliente.equalsIgnoreCase(Constantes.itemsFactura.get(i).getIdCliente()))
                    {
                        idFactura = Constantes.itemsFactura.get(i).getIdFactura();
                        fechaFactura= Constantes.itemsFactura.get(i).getFecha();
                        totalFactura= Constantes.itemsFactura.get(i).getTotal();
                        valorRestante= Constantes.itemsFactura.get(i).getValorRestante();
                        fechaCobroFactura= Constantes.itemsFactura.get(i).getFechaCobro();
                        diaCobroFactura= Constantes.itemsFactura.get(i).getDiaCobro();
                        horaCobroFactura= Constantes.itemsFactura.get(i).getHoraCobro();
                        idVendedorFactura= Constantes.itemsFactura.get(i).getIdVendedor();
                        idClienteFactura= Constantes.itemsFactura.get(i).getIdCliente();

                        TareaGetSale tareaGetSale = new TareaGetSale();
                        tareaGetSale.execute();
                    }
                }
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al cargar el cliente", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Clases Asyntask para traer las ventas
        private class TareaGetSale extends AsyncTask<String,Integer,Boolean>
        {
            private String respStr;
            private JSONObject msg;

            @TargetApi(Build.VERSION_CODES.KITKAT)
            protected Boolean doInBackground(String... params)
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient= new DefaultHttpClient();
                httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerVenta.php");

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("option",  "getAllSale"));

                try
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse resp= httpClient.execute(httpPost);

                    respStr = EntityUtils.toString(resp.getEntity());

                    JSONObject respJSON = new JSONObject(respStr);
                    JSONArray objItems = respJSON.getJSONArray("items");
                    JSONArray objFacturas = objItems.getJSONArray(0);

                    //String obj
                    String respuesta= String.valueOf(objFacturas);

                    if(respuesta.equalsIgnoreCase("No Existen Ventas"))
                    {
                        existe = false;
                    }
                    else
                    {
                        Constantes.itemsVenta.clear();
                        for(int i=0; i<objFacturas.length(); i++)
                        {
                            JSONObject obj = objFacturas.getJSONObject(i);

                            if(idFactura.equalsIgnoreCase(obj.getString("idFactura")) && obj.getString("estado").equalsIgnoreCase("En Venta"))
                            {
                                Constantes.itemsVenta.add(new ItemsVenta_AgregarCliente(obj.getString("idVenta"),obj.getString("total"),obj.getString("cantidad"), obj.getString("cantidadGarantia"), obj.getString("cantidadDevolucion"), obj.getString("estado"),obj.getString("idFactura"),obj.getString("idProducto"), "0"));
                            }

                            existe = true;
                        }
                    }
                }
                catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                    existe = false;
                }

                catch(ClientProtocolException e)
                {
                    e.printStackTrace();
                    existe = false;
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                    existe = false;
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    existe = false;
                }

                return existe;
            }

            protected void onPostExecute(Boolean result)
            {
                if (existe)
                {
                    TareaProductos tarea = new TareaProductos();
                    tarea.execute();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error al cargar el cliente", Toast.LENGTH_LONG).show();
                }
            }
    }

    //Clases Asyntask para traer los datos de la tabla productos

    private class TareaProductos extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerProducto.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllProduct"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);

                Constantes.itemsProductos.clear();

                for(int i = 0; i < Constantes.itemsVenta.size(); i++)
                {
                    for(int j=0; j<objVendedores.length(); j++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(j);

                        //Productos Filtrados
                        if(obj.getString("idProducto").equalsIgnoreCase(Constantes.itemsVenta.get(i).getIdProducto()))
                        {
                            Constantes.itemsProductos.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("tiempoGarantia"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));
                        }

                        existe = true;
                    }
                }
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                existe = false;
            } catch (JSONException e) {
                e.printStackTrace();
                existe = false;
            }

            return existe;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(Productos.this, respStr, Toast.LENGTH_SHORT).show();
            if(existe)
            {
                TareaListadoVendedores tarea = new TareaListadoVendedores();
                tarea.execute();
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al cargar el cliente", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Clases Asyntask para listar los empleados que hay actualmente en el servidor

    private class TareaListadoVendedores extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean respu = false;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "listUssers"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                //String obj
                respuesta= String.valueOf(objVendedores);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                    respu = false;
                }
                else
                {
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        if(obj.getString("idUsuario").equalsIgnoreCase(idVendedorFactura))
                        {
                            nombreVendedorUsuarios = obj.getString("nombre");
                            telefonoVendedorUsuarios = obj.getString("telefono");
                        }
                        existe = true;
                        respu = true;
                    }
                }
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                existe = false;
                respu = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                existe = false;
                respu = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                existe = false;
                respu = false;
            }
            catch (JSONException e) {
                e.printStackTrace();
                existe = false;
                respu = false;
            }

            return respu;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(Empleados.this, respuesta, Toast.LENGTH_SHORT).show();
            if(existe)
            {
                //Obtener las garantias y devoluciones si las hay
                Constantes.itemsGarantias.clear();
                TareaObtenerGarantias tareaObtenerGarantias = new TareaObtenerGarantias();
                tareaObtenerGarantias.execute();
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al cargar el cliente", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Clases Asyntask para traer los datos de la tabla garantias
    private class TareaObtenerGarantias extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerGarantia.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllWarranty"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                //String obj

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);

                    if(obj.getString("estado").equalsIgnoreCase("En espera") || obj.getString("estado").equalsIgnoreCase("Pendiente"))
                    {
                        Constantes.itemsGarantias.add(new Items_Garantia(obj.getString("idGarantia"), obj.getString("estado"), obj.getString("descripcion"), obj.getString("fecha"), obj.getString("cantidad"), obj.getString("idVendedor"), obj.getString("idCliente"), obj.getString("idProducto")));
                    }

                    resul = true;
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
            } catch (JSONException e)
            {
                e.printStackTrace();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                TareaObtenerState tareaObtenerState = new TareaObtenerState();
                tareaObtenerState.execute();
            }
            else
            {
                progressDialog.dismiss();
                //Toast.makeText(getContext(), "Error al cargar el cliente", Toast.LENGTH_LONG).show();
                TareaObtenerState tareaObtenerState = new TareaObtenerState();
                tareaObtenerState.execute();
            }
        }
    }

    //Clases Asyntask para traer los datos de la tabla garantias
    private class TareaObtenerState extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerEstadoGarantia.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllState"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                Constantes.itemsEstadoGarantia.clear();

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);

                    Constantes.itemsEstadoGarantia.add(new ItemsEstadoGarantia(obj.getString("idEstadoGarantia"), obj.getString("nombre"), obj.getString("cantidad"), obj.getString("idGarantia")));
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
            } catch (JSONException e)
            {
                e.printStackTrace();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                Constantes.itemsDevoluciones.clear();
                TareaObtenerDevoluciones tareaObtenerDevoluciones = new TareaObtenerDevoluciones();
                tareaObtenerDevoluciones.execute();
            }
            else
            {
                progressDialog.dismiss();
                TareaObtenerDevoluciones tareaObtenerDevoluciones = new TareaObtenerDevoluciones();
                tareaObtenerDevoluciones.execute();
            }
        }
    }

    //Clases Asyntask para traer los datos de la tabla devoluciones
    private class TareaObtenerDevoluciones extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerDevolucion.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllReturn"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                //String obj

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);

                    if(obj.getString("estado").equalsIgnoreCase("En espera"))
                    {
                        Constantes.itemsDevoluciones.add(new Items_Devolucion(obj.getString("idDevolucion"), obj.getString("estado"), obj.getString("descripcion"), obj.getString("fecha"), obj.getString("cantidad"), obj.getString("idVendedor"), obj.getString("idCliente"), obj.getString("idProducto")));
                    }

                    resul = true;
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
            } catch (JSONException e)
            {
                e.printStackTrace();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                TareaObtenerStateReturn tareaObtenerStateReturn = new TareaObtenerStateReturn();
                tareaObtenerStateReturn.execute();
            }
            else
            {
                progressDialog.dismiss();
                TareaObtenerStateReturn tareaObtenerStateReturn = new TareaObtenerStateReturn();
                tareaObtenerStateReturn.execute();
            }
        }
    }

    //Clases Asyntask para traer los datos de la tabla garantias
    private class TareaObtenerStateReturn extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerEstadoDevolucion.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllReturn"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                Constantes.itemsEstadoDevolucion.clear();

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);

                    Constantes.itemsEstadoDevolucion.add(new ItemsEstadoDevolucion(obj.getString("idEstadoDevolucion"), obj.getString("nombre"), obj.getString("cantidad"), obj.getString("idDevolucion")));

                    resul = true;
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                progressDialog.dismiss();
                //Toast.makeText(getContext(), idClienteCliente + " " + cedula + " " + nombre+ " " + direccion+ " " +telefono+ " " +correo, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), ModificarCliente.class);

                Constantes.cedulaCliente = cedula;
                Constantes.nombreCliente = nombre;
                Constantes.direccionCliente = direccion;
                Constantes.telefonoCliente = telefono;
                Constantes.correoCliente = correo;
                Constantes.nombreEmpresaCliente = nombreEmpresa;
                Constantes.direccionEmpresaCliente = direccionEmpresa;
                Constantes.idClienteCliente = idClienteCliente;
                Constantes.estadoCliente = estado;
                Constantes.calificacionCliente = calificacion;
                Constantes.idFactura = idFactura;
                Constantes.fechaFactura = fechaFactura;
                Constantes.totalFactura = totalFactura;
                Constantes.valorRestante = valorRestante;
                Constantes.fechaCobroFactura = fechaCobroFactura;
                Constantes.diaCobroFactura = diaCobroFactura;
                Constantes.horaCobroFactura = horaCobroFactura;
                Constantes.idVendedorFactura = idVendedorFactura;
                Constantes.idClienteFactura = idClienteFactura;
                Constantes.nombreVendedorUsuarios = nombreVendedorUsuarios;
                Constantes.telefonoVendedorUsuarios = telefonoVendedorUsuarios;
                Constantes.interfaz = "Administrador";
                getContext().startActivity(intent);
            }
            else
            {
                progressDialog.dismiss();
                //Toast.makeText(getContext(), "Error al cargar el cliente", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), ModificarCliente.class);

                Constantes.cedulaCliente = cedula;
                Constantes.nombreCliente = nombre;
                Constantes.direccionCliente = direccion;
                Constantes.telefonoCliente = telefono;
                Constantes.correoCliente = correo;
                Constantes.nombreEmpresaCliente = nombreEmpresa;
                Constantes.direccionEmpresaCliente = direccionEmpresa;
                Constantes.idClienteCliente = idClienteCliente;
                Constantes.estadoCliente = estado;
                Constantes.calificacionCliente = calificacion;
                Constantes.idFactura = idFactura;
                Constantes.fechaFactura = fechaFactura;
                Constantes.totalFactura = totalFactura;
                Constantes.valorRestante = valorRestante;
                Constantes.fechaCobroFactura = fechaCobroFactura;
                Constantes.diaCobroFactura = diaCobroFactura;
                Constantes.horaCobroFactura = horaCobroFactura;
                Constantes.idVendedorFactura = idVendedorFactura;
                Constantes.idClienteFactura = idClienteFactura;
                Constantes.nombreVendedorUsuarios = nombreVendedorUsuarios;
                Constantes.telefonoVendedorUsuarios = telefonoVendedorUsuarios;
                Constantes.interfaz = "Administrador";
                getContext().startActivity(intent);
            }
        }
    }
}
