package com.example.billy.devolucion;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billy.clientes.DatosCobro;
import com.example.billy.clientes.DetalleCobro;
import com.example.billy.clientes.ItemFactura_AgregarCliente;
import com.example.billy.clientes.ItemsVenta_AgregarCliente;
import com.example.billy.clientes.M_DatosCobro;
import com.example.billy.clientes.M_DetalleCobro;
import com.example.billy.constantes.Constantes;
import com.example.billy.garantias_product.Garantia;
import com.example.billy.garantias_product.ItemsEstadoGarantia;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.productos.AdapterListaProductos_Productos;
import com.example.billy.productos.ItemsListaProductos_Productos;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisualizarDevolucion extends AppCompatActivity
{
    String idDevolucion = "";
    String tipo = "";

    EditText editTextNombreCliente_VDevolucion;
    EditText editTextTelefonoCliente_VDevolucion;
    EditText editTextNombreProducto_VDevolucion;
    Spinner  spinnerCantidadProducto_VDevolucion;
    EditText editTextFecha_VDevolucion;
    EditText editTextDescripcion_VDevolucion;

    Spinner spinEstado_VDevolucion;

    String estado = "";
    String descripcion = "";
    String fecha = "";
    String cantidad = "";
    String idVendedor = "";
    String idCliente = "";
    String idProducto = "";

    String respuesta = "";
    boolean existe = false;

    String cantidadFactura = "";

    boolean resul;
    Object respuestaFactura = "";
    //Alerta Cargando
    ProgressDialog progressDialog;

    //Array que guarda todos los items de los pruductos
    ArrayList<ItemsListaProductos_Productos> arrayListProductos = new ArrayList<ItemsListaProductos_Productos>();

    //variable valor de la venta de un producto
    int valorProducto = 0;

    //variable para mirar si la devolucion no es aceptada
    boolean verificarUpdate = false;

    int cantidadEstadoDevolucion = 0;

    int cantidadTotal = 0;

    Object respuestaServidor;

    boolean verificarDevolucionTerminada = false;

    String cantidadSpin = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_devolucion);

        TareaObtenerReturn tareaObtenerReturn = new TareaObtenerReturn();
        tareaObtenerReturn.execute();
        AlertaCargando();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        editTextNombreCliente_VDevolucion = (EditText) findViewById(R.id.editTextNombreCliente_VDevolucion);
        editTextTelefonoCliente_VDevolucion = (EditText) findViewById(R.id.editTextTelefonoCliente_VDevolucion);
        editTextNombreProducto_VDevolucion = (EditText) findViewById(R.id.editTextNombreProducto_VDevolucion);
        editTextFecha_VDevolucion = (EditText) findViewById(R.id.editTextFecha_VDevolucion);
        editTextDescripcion_VDevolucion = (EditText) findViewById(R.id.editTextDescripcion_VDevolucion);

        spinnerCantidadProducto_VDevolucion = (Spinner) findViewById(R.id.spinCantidadProducto_VDevolucion);
        spinEstado_VDevolucion = (Spinner) findViewById(R.id.spinEstado_VDevolucion);

        Bundle extra = getIntent().getExtras();
        idDevolucion = extra.getString("idDevolucion");
        descripcion = extra.getString("descripcion");
        fecha = extra.getString("fecha");
        cantidad = extra.getString("cantidad");
        idVendedor = extra.getString("idVendedor");
        idCliente = extra.getString("idCliente");
        idProducto = extra.getString("idProducto");
        tipo = extra.getString("tipo");

        switch (tipo)
        {
            case "Vendedor":

                for(int i = 0; i < Devolucion.arrayListFiltroVendedor.size(); i++)
                {
                    if(idDevolucion.equalsIgnoreCase(Devolucion.arrayListFiltroVendedor.get(i).getIdDevolucion()))
                    {
                        editTextNombreCliente_VDevolucion.setText(Devolucion.arrayListFiltroVendedor.get(i).getNombre());
                        editTextTelefonoCliente_VDevolucion.setText(Devolucion.arrayListFiltroVendedor.get(i).getTelefono());
                        editTextNombreProducto_VDevolucion.setText(Devolucion.arrayListFiltroVendedor.get(i).getNombreProducto());
                        editTextFecha_VDevolucion.setText(Devolucion.arrayListFiltroVendedor.get(i).getFecha());
                        editTextDescripcion_VDevolucion.setText(Devolucion.arrayListFiltroVendedor.get(i).getDescripcion());
                    }
                }

                break;

            case "Todos":

                for(int i = 0; i < Devolucion.arrayList.size(); i++)
                {
                    if(idDevolucion.equalsIgnoreCase(Devolucion.arrayList.get(i).getIdDevolucion()))
                    {
                        editTextNombreCliente_VDevolucion.setText(Devolucion.arrayList.get(i).getNombre());
                        editTextTelefonoCliente_VDevolucion.setText(Devolucion.arrayList.get(i).getTelefono());
                        editTextNombreProducto_VDevolucion.setText(Devolucion.arrayList.get(i).getNombreProducto());

                        ArrayList<String> arrayListSpin = new ArrayList<String>();
                        arrayListSpin.clear();

                        for(int m = 1; m <= Integer.valueOf(cantidad); m++)
                        {
                            arrayListSpin.add(String.valueOf(m));
                        }

                        spinnerCantidadProducto_VDevolucion.setAdapter(new ArrayAdapter<String>(VisualizarDevolucion.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));


                        editTextFecha_VDevolucion.setText(Devolucion.arrayList.get(i).getFecha());
                        editTextDescripcion_VDevolucion.setText(Devolucion.arrayList.get(i).getDescripcion());
                    }
                }

                break;
        }

        //Para decidir que va a contener el spinner del estado

        String [] arrayEstado = getResources().getStringArray(R.array.estado_devolucion);
        spinEstado_VDevolucion.setAdapter(new ArrayAdapter<String>(VisualizarDevolucion.this, android.R.layout.simple_spinner_dropdown_item, arrayEstado));

    }

    public void AlertaCargando()
    {
        //Alerta que carga mientras se cargan los Clientes
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualizar_devolucion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.guardarDevolucion_VisualizarDevolucion:

                String cantidadSpin = spinnerCantidadProducto_VDevolucion.getSelectedItem().toString();

                TareaUpdateDevolucion tareaUpdateDevolucion;

                if(cantidad.equalsIgnoreCase(String.valueOf(Integer.valueOf(cantidadTotal + cantidadSpin))))
                {
                    estado = "Terminado";

                    tareaUpdateDevolucion = new TareaUpdateDevolucion();
                    tareaUpdateDevolucion.execute(idDevolucion, estado, descripcion, fecha, cantidad,
                            idVendedor, idCliente, idProducto);

                    AlertaCargando();
                }
                else
                {
                    estado = "En proceso";

                    tareaUpdateDevolucion = new TareaUpdateDevolucion();
                    tareaUpdateDevolucion.execute(idDevolucion, estado, descripcion, fecha, cantidad,
                            idVendedor, idCliente, idProducto);

                    AlertaCargando();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private class TareaUpdateDevolucion extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;

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
            nameValuePairs.add(new BasicNameValuePair("idDevolucion", params[0]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[1]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[3]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[4]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[6]));
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[7]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateReturn"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= String.valueOf(objItems);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    existe = true;
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                if (!verificarDevolucionTerminada)
                {
                    String cantidadSpiner = spinnerCantidadProducto_VDevolucion.getSelectedItem().toString();
                    String estadoSpin = spinEstado_VDevolucion.getSelectedItem().toString();
                    int contador = 0;

                    boolean verificar = false;

                    switch (estado)
                    {
                        case "Terminado":

                            for(int j = 0; j < Constantes.itemsEstadoDevolucion.size(); j++)
                            {
                                if(contador <= Integer.valueOf(cantidadSpiner))
                                {
                                    if(idDevolucion.equalsIgnoreCase(Constantes.itemsEstadoDevolucion.get(j).getIdDevolucion())
                                            && Constantes.itemsEstadoDevolucion.get(j).getNombre().equalsIgnoreCase("En espera"))
                                    {
                                        TareaUpdateEstadoDevolucion tareaUpdateEstadoDevolucion = new TareaUpdateEstadoDevolucion();
                                        tareaUpdateEstadoDevolucion.execute(Constantes.itemsEstadoDevolucion.get(j).getIdEstadoDevolucion(),
                                                estadoSpin, "1", idDevolucion);

                                        contador = contador + 1;
                                    }
                                }
                            }

                            if (estadoSpin.equalsIgnoreCase("Sin devolucion"))
                            {
                                TareaUpdateSale tareaUpdateSale = new TareaUpdateSale();
                                tareaUpdateSale.execute(Constantes.itemsVenta.get(0).getIdVenta(),
                                        Constantes.itemsVenta.get(0).getTotal(),
                                        Constantes.itemsVenta.get(0).getCantidad(),
                                        Constantes.itemsVenta.get(0).getCantidadGarantia(),
                                        cantidadSpiner,
                                        Constantes.itemsVenta.get(0).getEstado(),
                                        Constantes.itemsVenta.get(0).getIdFactura(),
                                        Constantes.itemsVenta.get(0).getIdProducto());

                                verificarUpdate = true;
                            }
                            else
                            {
                                verificarUpdate = true;
                                TareaProductos tareaProductos = new TareaProductos();
                                tareaProductos.execute();
                            }

                            break;

                        case "En proceso":

                            for(int i = 0; i < Constantes.itemsEstadoDevolucion.size(); i++)
                            {
                                if(idDevolucion.equalsIgnoreCase(Constantes.itemsEstadoDevolucion.get(i).getIdDevolucion())
                                        && Constantes.itemsEstadoDevolucion.get(i).getNombre().equalsIgnoreCase("Aceptado"))
                                {
                                    cantidadTotal = cantidadTotal + 1;
                                }
                            }

                            for(int j = 0; j < Constantes.itemsEstadoDevolucion.size(); j++)
                            {
                                if(contador < Integer.valueOf(cantidadSpiner))
                                {
                                    if(idDevolucion.equalsIgnoreCase(Constantes.itemsEstadoDevolucion.get(j).getIdDevolucion())
                                            && Constantes.itemsEstadoDevolucion.get(j).getNombre().equalsIgnoreCase("En espera"))
                                    {
                                        TareaUpdateEstadoDevolucion tareaUpdateEstadoDevolucion = new TareaUpdateEstadoDevolucion();
                                        tareaUpdateEstadoDevolucion.execute(Constantes.itemsEstadoDevolucion.get(j).getIdEstadoDevolucion(),
                                                estadoSpin, "1", idDevolucion);

                                        contador = contador + 1;
                                    }
                                }
                            }

                            if (estadoSpin.equalsIgnoreCase("Sin devolucion"))
                            {
                                int suma = 0;
                                suma = Integer.valueOf(Constantes.itemsVenta.get(0).getCantidadDevolucion()) + Integer.valueOf(cantidadSpiner);

                                TareaUpdateSale tareaUpdateSale = new TareaUpdateSale();
                                tareaUpdateSale.execute(Constantes.itemsVenta.get(0).getIdVenta(),
                                        Constantes.itemsVenta.get(0).getTotal(),
                                        Constantes.itemsVenta.get(0).getCantidad(),
                                        Constantes.itemsVenta.get(0).getCantidadGarantia(),
                                        String.valueOf(suma),
                                        Constantes.itemsVenta.get(0).getEstado(),
                                        Constantes.itemsVenta.get(0).getIdFactura(),
                                        Constantes.itemsVenta.get(0).getIdProducto());

                                verificarUpdate = true;
                            }
                            else
                            {
                                verificarUpdate = true;
                                TareaProductos tareaProductos = new TareaProductos();
                                tareaProductos.execute();
                            }

                            int cantidadFinal = cantidadTotal + Integer.valueOf(cantidadSpiner);

                            if(cantidad.equalsIgnoreCase(String.valueOf(cantidadFinal)))
                            {
                                verificarDevolucionTerminada  = true;

                                TareaUpdateDevolucion tareaUpdateDevolucion = new TareaUpdateDevolucion();
                                tareaUpdateDevolucion.execute(idDevolucion, "Terminado", descripcion, fecha, cantidad,
                                        idVendedor, idCliente, idProducto);
                            }

                            break;
                    }
                }
            }
            else
            {
                Toast.makeText(VisualizarDevolucion.this, "Error al Modificar la Garantia", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    //Clases Asyntask para traer las facturas
    private class TareaListadoBill extends AsyncTask<String,Integer,Boolean>
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
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerFactura.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllBill"));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp = httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFacturas = objItems.getJSONArray(0);

                //String obj
                String respuesta = String.valueOf(objFacturas);

                if (respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    Constantes.itemsFactura.clear();
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);
                        if (idCliente.equalsIgnoreCase(obj.getString("idCliente")) && obj.getString("estado").equalsIgnoreCase("Activo"))
                        {
                            Constantes.itemsFactura.add(new ItemFactura_AgregarCliente(obj.getString("idFactura"), obj.getString("fecha"), obj.getString("total"), obj.getString("valorRestante"), obj.getString("estado"), obj.getString("fechaCobro"), obj.getString("diaCobro"), obj.getString("horaCobro"), obj.getString("idVendedor"), obj.getString("idCliente")));
                            resul = true;
                        }
                    }
                }

                resul = true;
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                TareaGetSale tareaGetSale = new TareaGetSale();
                tareaGetSale.execute();
            }
            else
            {
                Toast.makeText(VisualizarDevolucion.this,"Error al cargar la factura",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
            boolean resul = true;
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
                //JSONObject obj = objItems.getJSONObject(0);

                arrayListProductos.clear();

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);

                    arrayListProductos.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("tiempoGarantia"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));

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
            String totalFactura = "";
            String valorRestanteFactura = "";

            totalFactura = Constantes.itemsFactura.get(0).getTotal();
            valorRestanteFactura = Constantes.itemsFactura.get(0).getValorRestante();

            for (int j = 0; j < arrayListProductos.size(); j++ )
            {
                if (arrayListProductos.get(j).getIdProducto().equalsIgnoreCase(idProducto))
                {
                    valorProducto = Integer.valueOf(arrayListProductos.get(j).getPrecioVenta()) * Integer.valueOf(spinnerCantidadProducto_VDevolucion.getSelectedItem().toString());

                    int restaFactura = Integer.valueOf(totalFactura) - valorProducto;
                    totalFactura = String.valueOf(restaFactura);

                    int restaValorRestante = Integer.valueOf(valorRestanteFactura) - valorProducto;
                    valorRestanteFactura = String.valueOf(restaValorRestante);

                    TareaUpdateBill tareaUpdateBill = new TareaUpdateBill();
                    tareaUpdateBill.execute(Constantes.itemsFactura.get(0).getIdFactura(),
                            Constantes.itemsFactura.get(0).getFecha(),
                            totalFactura,
                            valorRestanteFactura,
                            Constantes.itemsFactura.get(0).getEstado(),
                            Constantes.itemsFactura.get(0).getFechaCobro(),
                            Constantes.itemsFactura.get(0).getDiaCobro(),
                            Constantes.itemsFactura.get(0).getHoraCobro(),
                            Constantes.itemsFactura.get(0).getIdVendedor(),
                            Constantes.itemsFactura.get(0).getIdCliente());
                }
            }
        }
    }


    //Clases Asyntask para modificar una factura
    private class TareaUpdateBill extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;

            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerFactura.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idFactura", params[0]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[1]));
            nameValuePairs.add(new BasicNameValuePair("total", params[2]));
            nameValuePairs.add(new BasicNameValuePair("valorRestante", params[3]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[4]));
            nameValuePairs.add(new BasicNameValuePair("fechaCobro", params[5]));
            nameValuePairs.add(new BasicNameValuePair("diaCobro", params[6]));
            nameValuePairs.add(new BasicNameValuePair("horaCobro", params[7]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[8]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[9]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateBill"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuestaFactura= respJSON.get("items");
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
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            progressDialog.dismiss();

            LayoutInflater inflaterAlert = (LayoutInflater) VisualizarDevolucion.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialoglayout = inflaterAlert.inflate(R.layout.alerta_registro_producto, null);

            final Spinner spinCantidad = (Spinner) dialoglayout.findViewById(R.id.spinCantidad);

            //Llenar el spinner con los productos disponibles
            ArrayList<String> arrayListSpin = new ArrayList<String>();
            arrayListSpin.clear();

            for(int m = 1; m <= Integer.valueOf(spinnerCantidadProducto_VDevolucion.getSelectedItem().toString()); m++)
            {
                arrayListSpin.add(String.valueOf(m));
            }

            spinCantidad.setAdapter(new ArrayAdapter<String>(VisualizarDevolucion.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

            //Alerta personalizada
            AlertDialog.Builder builder = new AlertDialog.Builder(VisualizarDevolucion.this);
            builder.setIcon(R.mipmap.productos);
            builder.setTitle("Producto");
            builder.setView(dialoglayout);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    for(int j = 0; j < arrayListProductos.size(); j++)
                    {
                        if(idProducto.equalsIgnoreCase(arrayListProductos.get(j).getIdProducto()))
                        {
                            String cantidadInventario = arrayListProductos.get(j).getCantidad();
                            cantidadSpin = spinCantidad.getSelectedItem().toString();

                            int sumaCantidad = Integer.valueOf(cantidadInventario) + Integer.valueOf(cantidadSpin);

                            TareaUpdadteProducto tareaUpdadteProducto = new TareaUpdadteProducto();
                            tareaUpdadteProducto.execute(
                                    arrayListProductos.get(j).getNombre(),
                                    arrayListProductos.get(j).getDescripcion(),
                                    String.valueOf(sumaCantidad),
                                    arrayListProductos.get(j).getGarantia(),
                                    arrayListProductos.get(j).getPrecioCompra(),
                                    arrayListProductos.get(j).getPrecioVenta(),
                                    arrayListProductos.get(j).getIdCategoria());

                            AlertaCargando();
                        }
                    }
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(VisualizarDevolucion.this, "La Devolución se modifico correctamente", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(VisualizarDevolucion.this, Devolucion.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    //Clases Asyntask para actualizar un producto
    private class TareaUpdadteProducto extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerProducto.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idProducto", idProducto));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[0]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[2]));
            nameValuePairs.add(new BasicNameValuePair("tiempoGarantia", params[3]));
            nameValuePairs.add(new BasicNameValuePair("precioCompra", params[4]));
            nameValuePairs.add(new BasicNameValuePair("precioVenta", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idCategoria", params[6]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateProduct"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= String.valueOf(objItems);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    resul = false;
                }
                else
                {
                    resul = true;
                }
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
            }
            catch (JSONException e) {
                e.printStackTrace();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                int resta = Integer.valueOf(Constantes.itemsVenta.get(0).getCantidad()) - Integer.valueOf(spinnerCantidadProducto_VDevolucion.getSelectedItem().toString());

                TareaUpdateSale tareaUpdateSale = new TareaUpdateSale();
                tareaUpdateSale.execute(Constantes.itemsVenta.get(0).getIdVenta(),
                        Constantes.itemsVenta.get(0).getTotal(),
                        String.valueOf(resta),
                        Constantes.itemsVenta.get(0).getCantidadGarantia(),
                        Constantes.itemsVenta.get(0).getCantidadDevolucion(),
                        Constantes.itemsVenta.get(0).getEstado(),
                        Constantes.itemsVenta.get(0).getIdFactura(),
                        Constantes.itemsVenta.get(0).getIdProducto());
            }
            else
            {

            }
        }
    }

    //Clic del boton de atras del celular
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    //Clases Asyntask para traer los datos de la tabla garantias
    private class TareaObtenerReturn extends AsyncTask<String,Integer,Boolean>
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
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                for(int i = 0; i < Constantes.itemsEstadoDevolucion.size(); i++)
                {
                    if(idDevolucion.equalsIgnoreCase(Constantes.itemsEstadoDevolucion.get(i).getIdDevolucion())
                            && Constantes.itemsEstadoDevolucion.get(i).getNombre().equalsIgnoreCase("En espera"))
                    {
                        cantidadEstadoDevolucion = cantidadEstadoDevolucion + 1;
                    }

                    if(idDevolucion.equalsIgnoreCase(Constantes.itemsEstadoDevolucion.get(i).getIdDevolucion())
                            && Constantes.itemsEstadoDevolucion.get(i).getNombre().equalsIgnoreCase("Sin devolucion"))
                    {
                        cantidadTotal = cantidadTotal + 1;
                    }
                }

                ArrayList<String> arrayListSpin = new ArrayList<String>();
                arrayListSpin.clear();

                for(int m = 1; m <= Integer.valueOf(cantidadEstadoDevolucion); m++)
                {
                    arrayListSpin.add(String.valueOf(m));
                }

                spinnerCantidadProducto_VDevolucion.setAdapter(new ArrayAdapter<String>(VisualizarDevolucion.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

                //Obtener todas las ventas
                TareaListadoBill tareaListadoBill = new TareaListadoBill();
                tareaListadoBill.execute();
            }
            else
            {
                Toast.makeText(VisualizarDevolucion.this, "Error al cargar las devoluciones", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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
            boolean resul = false;
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
                    resul = false;
                }
                else
                {
                    Constantes.itemsVenta.clear();
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);

                        if(Constantes.itemsFactura.get(0).getIdFactura().equalsIgnoreCase(obj.getString("idFactura")) && idProducto.equalsIgnoreCase(obj.getString("idProducto")))
                        {
                            Constantes.itemsVenta.add(new ItemsVenta_AgregarCliente(obj.getString("idVenta"),obj.getString("total"),obj.getString("cantidad"), obj.getString("cantidadGarantia"), obj.getString("cantidadDevolucion"), obj.getString("estado"),obj.getString("idFactura"),obj.getString("idProducto"), "0"));
                        }

                        resul = true;
                    }
                }
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
            }
            catch (JSONException e)
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
                progressDialog.dismiss();

                if(verificarUpdate)
                {
                    Toast.makeText(VisualizarDevolucion.this, "La Devolucion se modifico correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VisualizarDevolucion.this, Devolucion.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                progressDialog.dismiss();
            }
        }
    }

    private class TareaUpdateEstadoDevolucion extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idEstadoDevolucion", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[2]));
            nameValuePairs.add(new BasicNameValuePair("idDevolucion", params[3]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateReturn"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= String.valueOf(objItems);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    resul = false;
                }
                else
                {
                    resul = true;
                }
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
            }
            catch (JSONException e)
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
                //nadita
            }
            else
            {
                Toast.makeText(VisualizarDevolucion.this, "Error al Modificar la Devolucion", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    //Clases Asyntask para actualizar una venta
    private class TareaUpdateSale extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = false;

            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;

            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerVenta.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idVenta", params[0]));
            nameValuePairs.add(new BasicNameValuePair("total", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[2]));
            nameValuePairs.add(new BasicNameValuePair("cantidadGarantia", params[3]));
            nameValuePairs.add(new BasicNameValuePair("cantidadDevolucion", params[4]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idFactura", params[6]));
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[7]));
            nameValuePairs.add(new BasicNameValuePair("option",  "updateSale"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuestaServidor = respJSON.get("items");
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
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                TareaGetSale tareaGetSale = new TareaGetSale();
                tareaGetSale.execute();
            }
            else
            {
                Toast.makeText(VisualizarDevolucion.this, "Error al modificar la venta", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }
}
