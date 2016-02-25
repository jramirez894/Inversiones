package com.example.billy.devolucion;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billy.clientes.DatosCobro;
import com.example.billy.clientes.DetalleCobro;
import com.example.billy.clientes.ItemFactura_AgregarCliente;
import com.example.billy.clientes.M_DatosCobro;
import com.example.billy.garantias_product.Garantia;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.productos.AdapterListaProductos_Productos;
import com.example.billy.productos.ItemsListaProductos_Productos;

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


    boolean resul;
    Object respuestaFactura = "";
    //Alerta Cargando
    ProgressDialog progressDialog;


    //Array que guarda todos los items de la factura
    ArrayList<ItemFactura_AgregarCliente> arrayListFactura = new ArrayList<ItemFactura_AgregarCliente>();

    //Array que guarda todos los items de los pruductos
    ArrayList<ItemsListaProductos_Productos> arrayListProductos = new ArrayList<ItemsListaProductos_Productos>();

    //variable valor de la venta de un producto
    int valorProducto = 0;

    //variable para mirar si la devolucion no es aceptada
    boolean devolucionNoAceptada = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_devolucion);

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
        estado = extra.getString("estado");
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

                String estadoSpin = spinEstado_VDevolucion.getSelectedItem().toString();

                TareaUpdateDevolucion tareaUpdateDevolucion;

                switch (estadoSpin)
                {
                    case "Aceptado":

                        tareaUpdateDevolucion = new TareaUpdateDevolucion();
                        tareaUpdateDevolucion.execute(idDevolucion, estadoSpin, descripcion, fecha, cantidad,
                                idVendedor, idCliente, idProducto);

                        AlertaCargando();

                        break;

                    case "Sin Devolucion":

                        tareaUpdateDevolucion = new TareaUpdateDevolucion();
                        tareaUpdateDevolucion.execute(idDevolucion, estadoSpin, descripcion, fecha, cantidad,
                                idVendedor, idCliente, idProducto);

                        AlertaCargando();

                        devolucionNoAceptada = true;

                        break;
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
            if(existe)
            {
                if (devolucionNoAceptada)
                {
                    Toast.makeText(VisualizarDevolucion.this, "La Devolución se modifico correctamente", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    Intent intent = new Intent(VisualizarDevolucion.this, Devolucion.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    TareaListadoBill tareaListadoBill = new TareaListadoBill();
                    tareaListadoBill.execute();
                }

            }
            else
            {
                Toast.makeText(VisualizarDevolucion.this, "Error al Modificar la Devolución", Toast.LENGTH_SHORT).show();
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
                    arrayListFactura.clear();

                    for (int i = 0; i < objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);
                        if (obj.getString("estado").equalsIgnoreCase("Activo"))
                        {
                            arrayListFactura.add(new ItemFactura_AgregarCliente(obj.getString("idFactura"), obj.getString("fecha"), obj.getString("total"), obj.getString("valorRestante"),obj.getString("estado"),obj.getString("fechaCobro"), obj.getString("diaCobro"), obj.getString("horaCobro"), obj.getString("idVendedor"), obj.getString("idCliente")));
                        }

                        existe = true;
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
            TareaProductos tareaProductos = new TareaProductos();
            tareaProductos.execute();
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

            for (int i = 0; i < arrayListFactura.size(); i++ )
            {
                if (arrayListFactura.get(i).getIdCliente().equalsIgnoreCase(idCliente))
                {
                    totalFactura = arrayListFactura.get(i).getTotal();
                    valorRestanteFactura = arrayListFactura.get(i).getValorRestante();

                    for (int j = 0; j < arrayListProductos.size(); j++ )
                    {
                        if (arrayListProductos.get(j).getIdProducto().equalsIgnoreCase(idProducto))
                        {
                            valorProducto = Integer.valueOf(arrayListProductos.get(i).getPrecioVenta()) * Integer.valueOf(cantidad);

                            int restaFactura = Integer.valueOf(totalFactura) - valorProducto;
                            totalFactura = String.valueOf(restaFactura);

                            int restaValorRestante = Integer.valueOf(valorRestanteFactura) - valorProducto;
                            valorRestanteFactura = String.valueOf(restaValorRestante);

                            TareaUpdateBill tareaUpdateBill = new TareaUpdateBill();
                            tareaUpdateBill.execute(arrayListFactura.get(i).getIdFactura(),
                                    arrayListFactura.get(i).getFecha(),
                                    totalFactura,
                                    valorRestanteFactura,
                                    arrayListFactura.get(i).getEstado(),
                                    arrayListFactura.get(i).getFechaCobro(),
                                    arrayListFactura.get(i).getDiaCobro(),
                                    arrayListFactura.get(i).getHoraCobro(),
                                    arrayListFactura.get(i).getIdVendedor(),
                                    arrayListFactura.get(i).getIdCliente());
                        }
                    }
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
            //Hacer la alerta que pregunte si quiere ingresar el producto de nuevo a la cantidad total

            Toast.makeText(VisualizarDevolucion.this, "La Devolución se modifico correctamente", Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();

            Intent intent = new Intent(VisualizarDevolucion.this, Devolucion.class);
            startActivity(intent);
            finish();
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
}
