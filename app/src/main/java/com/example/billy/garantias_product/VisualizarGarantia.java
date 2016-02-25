package com.example.billy.garantias_product;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billy.clientes.ItemFactura_AgregarCliente;
import com.example.billy.clientes.ItemsVenta_AgregarCliente;
import com.example.billy.constantes.Constantes;
import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;

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

public class VisualizarGarantia extends AppCompatActivity
{
    String idGarantia = "";
    String tipo = "";

    EditText editTextNombreCliente_VGarantia;
    EditText editTextTelefonoCliente_VGarantia;
    EditText editTextNombreProducto_VGarantia;
    EditText editTextFecha_VGarantia;
    EditText editTextDescripcion_VGarantia;

    Spinner spinEstado_VGarantia;
    Spinner spinCantidadProducto_VGarantia;

    String descripcion = "";
    String fecha = "";
    String cantidad = "";
    String idVendedor = "";
    String idCliente = "";
    String idProducto = "";
    String respuesta = "";

    Object respuestaServidor;

    int cantidadEstadoGarantia = 0;

    int cantidadTotal = 0;
    String estado = "";

    //Alerta Cargando
    ProgressDialog progressDialog;

    boolean verificarGarantiaTerminada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_garantia);

        TareaObtenerState tareaObtenerState = new TareaObtenerState();
        tareaObtenerState.execute();

        TareaGetBill tareaGetBill = new TareaGetBill();
        tareaGetBill.execute();

        AlertaCargando();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        editTextNombreCliente_VGarantia = (EditText) findViewById(R.id.editTextNombreCliente_VGarantia);
        editTextTelefonoCliente_VGarantia = (EditText) findViewById(R.id.editTextTelefonoCliente_VGarantia);
        editTextNombreProducto_VGarantia = (EditText) findViewById(R.id.editTextNombreProducto_VGarantia);
        editTextFecha_VGarantia = (EditText) findViewById(R.id.editTextFecha_VGarantia);
        editTextDescripcion_VGarantia = (EditText) findViewById(R.id.editTextDescripcion_VGarantia);

        spinEstado_VGarantia = (Spinner) findViewById(R.id.spinEstado_VGarantia);
        spinCantidadProducto_VGarantia = (Spinner) findViewById(R.id.spinCantidadProducto_VGarantia);

        Bundle extra = getIntent().getExtras();
        idGarantia = extra.getString("idGarantia");
        tipo = extra.getString("tipo");
        descripcion = extra.getString("descripcion");
        fecha = extra.getString("fecha");
        cantidad = extra.getString("cantidad");
        idVendedor = extra.getString("idVendedor");
        idCliente = extra.getString("idCliente");
        idProducto = extra.getString("idProducto");

        switch (tipo)
        {
            case "Vendedor":

                for(int i = 0; i < Garantia.arrayListFiltroVendedor.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Garantia.arrayListFiltroVendedor.get(i).getIdGarantia()))
                    {
                        editTextNombreCliente_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getNombre());
                        editTextTelefonoCliente_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getTelefono());
                        editTextNombreProducto_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getNombreProducto());
                        editTextFecha_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getFecha());
                        editTextDescripcion_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getDescripcion());
                    }
                }

                break;

            case "Todos":

                for(int i = 0; i < Garantia.arrayList.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Garantia.arrayList.get(i).getIdGarantia()))
                    {
                        editTextNombreCliente_VGarantia.setText(Garantia.arrayList.get(i).getNombre());
                        editTextTelefonoCliente_VGarantia.setText(Garantia.arrayList.get(i).getTelefono());
                        editTextNombreProducto_VGarantia.setText(Garantia.arrayList.get(i).getNombreProducto());

                        ArrayList<String> arrayListSpin = new ArrayList<String>();
                        arrayListSpin.clear();

                        for(int m = 1; m <= Integer.valueOf(cantidad); m++)
                        {
                            arrayListSpin.add(String.valueOf(m));
                        }

                        spinCantidadProducto_VGarantia.setAdapter(new ArrayAdapter<String>(VisualizarGarantia.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

                        editTextFecha_VGarantia.setText(Garantia.arrayList.get(i).getFecha());
                        editTextDescripcion_VGarantia.setText(Garantia.arrayList.get(i).getDescripcion());
                    }
                }

                break;
        }

        //Para decidir que va a contener el spinner del estado

        String [] arrayEstado = getResources().getStringArray(R.array.estado_garantia);
        spinEstado_VGarantia.setAdapter(new ArrayAdapter<String>(VisualizarGarantia.this, android.R.layout.simple_spinner_dropdown_item, arrayEstado));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualizar_garantia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.guardarGarantia_VisualizarGarantia:

                final String cantidadSpin = spinCantidadProducto_VGarantia.getSelectedItem().toString();

                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setIcon(R.mipmap.garantia);
                alerta.setTitle("Garantia");
                alerta.setMessage("¿Enviar productos por garantia?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (cantidad.equalsIgnoreCase(String.valueOf(Integer.valueOf(cantidadTotal + cantidadSpin))))
                        {
                            estado = "Terminado";

                            TareaUpdateGarantia tareaUpdateGarantia = new TareaUpdateGarantia();
                            tareaUpdateGarantia.execute(idGarantia, estado, descripcion, fecha, cantidad,
                                    idVendedor, idCliente, idProducto);

                        } else
                        {
                            estado = "En proceso";

                            TareaUpdateGarantia tareaUpdateGarantia = new TareaUpdateGarantia();
                            tareaUpdateGarantia.execute(idGarantia, estado, descripcion, fecha, cantidad,
                                    idVendedor, idCliente, idProducto);
                        }

                        AlertaCargando();
                    }
                });
                alerta.setNegativeButton("Cancelar",null);
                alerta.setCancelable(false);
                alerta.show();


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TareaUpdateGarantia extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idGarantia", params[0]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[1]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[3]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[4]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[6]));
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[7]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateWarranty"));

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
                if (!verificarGarantiaTerminada)
                {
                    String cantidadSpiner = spinCantidadProducto_VGarantia.getSelectedItem().toString();
                    String estadoSpin = spinEstado_VGarantia.getSelectedItem().toString();
                    int contador = 0;

                    boolean verificar = false;

                    switch (estado)
                    {
                        case "Terminado":

                            for(int j = 0; j < Constantes.itemsEstadoGarantia.size(); j++)
                            {
                                if(contador <= Integer.valueOf(cantidadSpiner))
                                {
                                    if(idGarantia.equalsIgnoreCase(Constantes.itemsEstadoGarantia.get(j).getIdGarantia())
                                            && Constantes.itemsEstadoGarantia.get(j).getNombre().equalsIgnoreCase("En espera"))
                                    {
                                        TareaUpdateEstadoGarantia tareaUpdateEstadoGarantia = new TareaUpdateEstadoGarantia();
                                        tareaUpdateEstadoGarantia.execute(Constantes.itemsEstadoGarantia.get(j).getIdEstadoGarantia(),
                                                estadoSpin, "1", idGarantia);

                                        contador = contador + 1;
                                    }
                                }
                            }

                            if (estadoSpin.equalsIgnoreCase("Sin garantia"))
                            {
                                TareaUpdateSale tareaUpdateSale = new TareaUpdateSale();
                                tareaUpdateSale.execute(Constantes.itemsVenta.get(0).getIdVenta(),
                                        Constantes.itemsVenta.get(0).getTotal(),
                                        Constantes.itemsVenta.get(0).getCantidad(),
                                        cantidadSpiner,
                                        Constantes.itemsVenta.get(0).getCantidadDevolucion(),
                                        Constantes.itemsVenta.get(0).getEstado(),
                                        Constantes.itemsVenta.get(0).getIdFactura(),
                                        Constantes.itemsVenta.get(0).getIdProducto());

                                verificar = true;
                            }

                            break;

                        case "En proceso":

                            for(int i = 0; i < Constantes.itemsEstadoGarantia.size(); i++)
                            {
                                if(idGarantia.equalsIgnoreCase(Constantes.itemsEstadoGarantia.get(i).getIdGarantia())
                                        && Constantes.itemsEstadoGarantia.get(i).getNombre().equalsIgnoreCase("Aceptado"))
                                {
                                    cantidadTotal = cantidadTotal + 1;
                                }
                            }

                            for(int j = 0; j < Constantes.itemsEstadoGarantia.size(); j++)
                            {
                                if(contador < Integer.valueOf(cantidadSpiner))
                                {
                                    if(idGarantia.equalsIgnoreCase(Constantes.itemsEstadoGarantia.get(j).getIdGarantia())
                                            && Constantes.itemsEstadoGarantia.get(j).getNombre().equalsIgnoreCase("En espera"))
                                    {
                                        TareaUpdateEstadoGarantia tareaUpdateEstadoGarantia = new TareaUpdateEstadoGarantia();
                                        tareaUpdateEstadoGarantia.execute(Constantes.itemsEstadoGarantia.get(j).getIdEstadoGarantia(),
                                                estadoSpin, "1", idGarantia);

                                        contador = contador + 1;
                                    }
                                }
                            }

                            if (estadoSpin.equalsIgnoreCase("Sin garantia"))
                            {
                                int suma = 0;
                                suma = Integer.valueOf(Constantes.itemsVenta.get(0).getCantidadGarantia()) + Integer.valueOf(cantidadSpiner);

                                TareaUpdateSale tareaUpdateSale = new TareaUpdateSale();
                                tareaUpdateSale.execute(Constantes.itemsVenta.get(0).getIdVenta(),
                                        Constantes.itemsVenta.get(0).getTotal(),
                                        Constantes.itemsVenta.get(0).getCantidad(),
                                        String.valueOf(suma),
                                        Constantes.itemsVenta.get(0).getCantidadDevolucion(),
                                        Constantes.itemsVenta.get(0).getEstado(),
                                        Constantes.itemsVenta.get(0).getIdFactura(),
                                        Constantes.itemsVenta.get(0).getIdProducto());

                                verificar = true;
                            }

                            int cantidadFinal = cantidadTotal + Integer.valueOf(cantidadSpiner);

                            if(cantidad.equalsIgnoreCase(String.valueOf(cantidadFinal)))
                            {
                                verificarGarantiaTerminada  = true;

                                TareaUpdateGarantia tareaUpdateGarantia = new TareaUpdateGarantia();
                                tareaUpdateGarantia.execute(idGarantia, "Terminado", descripcion, fecha, cantidad,
                                        idVendedor, idCliente, idProducto);
                            }

                            break;
                    }

                    if (!verificar)
                    {
                        Toast.makeText(VisualizarGarantia.this, "La Garantia se modifico correctamente", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                        Intent intent = new Intent(VisualizarGarantia.this, Garantia.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            else
            {
                Toast.makeText(VisualizarGarantia.this, "Error al Modificar la Garantia", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
                for(int i = 0; i < Constantes.itemsEstadoGarantia.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Constantes.itemsEstadoGarantia.get(i).getIdGarantia())
                            && Constantes.itemsEstadoGarantia.get(i).getNombre().equalsIgnoreCase("En espera"))
                    {
                        cantidadEstadoGarantia = cantidadEstadoGarantia + 1;
                    }

                    if(idGarantia.equalsIgnoreCase(Constantes.itemsEstadoGarantia.get(i).getIdGarantia())
                            && Constantes.itemsEstadoGarantia.get(i).getNombre().equalsIgnoreCase("Sin garantia"))
                    {
                        cantidadTotal = cantidadTotal + 1;
                    }
                }

                ArrayList<String> arrayListSpin = new ArrayList<String>();
                arrayListSpin.clear();

                for(int m = 1; m <= Integer.valueOf(cantidadEstadoGarantia); m++)
                {
                    arrayListSpin.add(String.valueOf(m));
                }

                spinCantidadProducto_VGarantia.setAdapter(new ArrayAdapter<String>(VisualizarGarantia.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

                progressDialog.dismiss();
            }
            else
            {
                Toast.makeText(VisualizarGarantia.this, "Error al cargar las garantias", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    private class TareaUpdateEstadoGarantia extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idEstadoGarantia", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[2]));
            nameValuePairs.add(new BasicNameValuePair("idGarantia", params[3]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateState"));

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
                Toast.makeText(VisualizarGarantia.this, "Error al Modificar la Garantia", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
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
                    resul = false;
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
                TareaGetSale tareaGetSale = new TareaGetSale();
                tareaGetSale.execute();
            }
            else
            {
                Toast.makeText(VisualizarGarantia.this,"Error al cargar la factura",Toast.LENGTH_SHORT).show();
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
            }
            else
            {
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
                Toast.makeText(VisualizarGarantia.this, "La Garantia se modifico correctamente", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

                Intent intent = new Intent(VisualizarGarantia.this, Garantia.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(VisualizarGarantia.this, "Error al modificar la venta", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }


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
