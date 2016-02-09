package com.example.billy.garantias_product;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.billy.constantes.Constantes;
import com.example.billy.empleado.ItemListaEmpleado;
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
import java.util.ArrayList;
import java.util.List;

public class Garantia extends AppCompatActivity
{
    public static ListView listaGarantia;
    public static ArrayList<Items_Garantia_Visualizar> arrayList = new ArrayList<Items_Garantia_Visualizar>();

    public static ArrayList<ItemsListaProductos_Productos> arrayListProductos = new ArrayList<ItemsListaProductos_Productos>();

    public static ArrayList<ItemListaEmpleado> arrayListVendedores = new ArrayList<ItemListaEmpleado>();
    public static ArrayList<String> arrayListNombresVendedores = new ArrayList<String>();


    AutoCompleteTextView autoCompleteTextViewBuscar;
    RadioGroup radioGroup;
    RadioButton rbVerTodos_Garantia;
    RadioButton rbVendedor_Garantia;

    boolean existe = false;
    String respuesta = "";

    String idVendedor = "";

    //Array que sirve para filtrar por vendedor
    public static ArrayList<Items_Garantia_Visualizar> arrayListFiltroVendedor = new ArrayList<Items_Garantia_Visualizar>();

    boolean verificar;

    //Para sacar los datos de la garantia que sea seleccionada
    String idGarantia = "";

    //Alerta Cargando
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garantia);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        listaGarantia=(ListView)findViewById(R.id.listView_Garantia);
        ActualizarLista();

        autoCompleteTextViewBuscar = (AutoCompleteTextView)findViewById(R.id.complete_Garantia);
        radioGroup = (RadioGroup)findViewById(R.id.radioButton_Garantias);

        autoCompleteTextViewBuscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = autoCompleteTextViewBuscar.getText().toString();
                int posicion = 0;

                for (int i = 0; i < arrayListVendedores.size(); i++) {
                    if (nombre.equalsIgnoreCase(arrayListVendedores.get(i).getNombre())) {
                        posicion = i;
                        break;
                    }
                }

                ItemListaEmpleado empleado = arrayListVendedores.get(posicion);
                idVendedor = empleado.getIdUsuario();

                arrayListFiltroVendedor.clear();

                for (int j = 0; j < arrayList.size(); j++)
                {
                    if (idVendedor.equalsIgnoreCase(arrayList.get(j).getIdVendedor()))
                    {
                        arrayListFiltroVendedor.add(new Items_Garantia_Visualizar(arrayList.get(j).getIdGarantia(), arrayList.get(j).getNombre(), arrayList.get(j).getTelefono(), arrayList.get(j).getNombreProducto(), arrayList.get(j).getCantidad(), arrayList.get(j).getFecha(), arrayList.get(j).getDescripcion(), arrayList.get(j).getEstado(), arrayList.get(j).getIdVendedor(), arrayList.get(j).getIdCliente(), arrayList.get(j).getIdProducto()));
                    }
                }

                listaGarantia.setAdapter(new Adapter_Garantia(Garantia.this, arrayListFiltroVendedor));
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.rbVerTodos_Garantia)
                {
                    listaGarantia.setAdapter(new Adapter_Garantia(Garantia.this, arrayList));
                    autoCompleteTextViewBuscar.setEnabled(false);
                    autoCompleteTextViewBuscar.setText("");
                }
                else
                {
                    if (checkedId == R.id.rbVendedor_Garantia)
                    {
                        autoCompleteTextViewBuscar.setEnabled(true);
                    }
                }
            }
        });

        //para buscar la garantia que haya sido seleccionada

        rbVendedor_Garantia = (RadioButton) findViewById(R.id.rbVendedor_Garantia);
        rbVerTodos_Garantia = (RadioButton) findViewById(R.id.rbVerTodos_Garantia);

        listaGarantia.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(rbVendedor_Garantia.isChecked())
                {
                    idGarantia = arrayListFiltroVendedor.get(position).getIdGarantia();

                    Intent intent = new Intent(Garantia.this, VisualizarGarantia.class);
                    intent.putExtra("idGarantia", idGarantia);
                    intent.putExtra("estado", arrayListFiltroVendedor.get(position).getEstado());
                    intent.putExtra("descripcion", arrayListFiltroVendedor.get(position).getDescripcion());
                    intent.putExtra("fecha", arrayListFiltroVendedor.get(position).getFecha());
                    intent.putExtra("cantidad", arrayListFiltroVendedor.get(position).getCantidad());
                    intent.putExtra("idVendedor", arrayListFiltroVendedor.get(position).getIdVendedor());
                    intent.putExtra("idCliente", arrayListFiltroVendedor.get(position).getIdCliente());
                    intent.putExtra("idProducto", arrayListFiltroVendedor.get(position).getIdProducto());
                    intent.putExtra("tipo", "Vendedor");
                    startActivity(intent);
                }
                else
                {
                    if (rbVerTodos_Garantia.isChecked())
                    {
                        idGarantia = arrayList.get(position).getIdGarantia();

                        Intent intent = new Intent(Garantia.this, VisualizarGarantia.class);
                        intent.putExtra("idGarantia", idGarantia);
                        intent.putExtra("estado", arrayList.get(position).getEstado());
                        intent.putExtra("descripcion", arrayList.get(position).getDescripcion());
                        intent.putExtra("fecha", arrayList.get(position).getFecha());
                        intent.putExtra("cantidad", arrayList.get(position).getCantidad());
                        intent.putExtra("idVendedor", arrayList.get(position).getIdVendedor());
                        intent.putExtra("idCliente", arrayList.get(position).getIdCliente());
                        intent.putExtra("idProducto", arrayList.get(position).getIdProducto());
                        intent.putExtra("tipo", "Todos");
                        startActivity(intent);
                    }
                }
            }
        });
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

    public void ActualizarLista()
    {
        AlertaCargando();

        arrayListProductos.clear();
        arrayList.clear();

        TareaProductos tareaProductos = new TareaProductos();
        tareaProductos.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_garantia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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

                //String obj

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
            TareaObtenerGarantias tareaObtenerGarantias = new TareaObtenerGarantias();
            tareaObtenerGarantias.execute();
        }
    }

    //Clases Asyntask para traer los datos de la tabla productos

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
                JSONArray objGarantias = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                for(int i=0; i<objGarantias.length(); i++)
                {
                    JSONObject obj = objGarantias.getJSONObject(i);

                    if(obj.getString("estado").equalsIgnoreCase("En espera") || obj.getString("estado").equalsIgnoreCase("Pendiente"))
                    {
                        //Para sacar el nombre y el telefono de cada cliente
                        String idCliente = obj.getString("idCliente");
                        String nom = "";
                        String tel = "";

                        for(int j = 0; j < PrincipalMenu.items.size(); j++)
                        {
                            if(idCliente.equalsIgnoreCase(PrincipalMenu.items.get(j).getIdCliente()))
                            {
                                nom = PrincipalMenu.items.get(j).getNombreLista();
                                tel = PrincipalMenu.items.get(j).getTelefono();
                            }
                        }

                        //Para sacar el nombre del producto
                        String idProducto = obj.getString("idProducto");
                        String nomPr = "";

                        for(int k = 0; k < arrayListProductos.size(); k++)
                        {
                            if(idProducto.equalsIgnoreCase(arrayListProductos.get(k).getIdProducto()))
                            {
                                nomPr = arrayListProductos.get(k).getNombre();
                            }
                        }

                        arrayList.add(new Items_Garantia_Visualizar(obj.getString("idGarantia"), nom, tel, nomPr, obj.getString("cantidad"), obj.getString("fecha"), obj.getString("descripcion"), obj.getString("estado"),obj.getString("idVendedor"), obj.getString("idCliente"), obj.getString("idProducto")));
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            listaGarantia.setAdapter(new Adapter_Garantia(Garantia.this, arrayList));

            TareaListado tareaListado = new TareaListado();
            tareaListado.execute();
        }
    }

    //Clases Asyntask para listar los empleados que hay actualmente en el servidor
    private class TareaListado extends AsyncTask<String,Integer,Boolean>
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
                }
                else
                {
                    arrayListVendedores.clear();
                    arrayListNombresVendedores.clear();

                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        arrayListVendedores.add(new ItemListaEmpleado(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idUsuario"), obj.getString("cedula"), obj.getString("nombre"), obj.getString("direccion"), obj.getString("telefono"), obj.getString("correo"), obj.getString("password")));
                        arrayListNombresVendedores.add(obj.getString("nombre"));
                        existe = true;
                    }
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
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(Empleados.this, respuesta, Toast.LENGTH_SHORT).show();
            autoCompleteTextViewBuscar.setAdapter(new ArrayAdapter<String>(Garantia.this, android.R.layout.simple_dropdown_item_1line, arrayListNombresVendedores));

            progressDialog.dismiss();
        }
    }
}
