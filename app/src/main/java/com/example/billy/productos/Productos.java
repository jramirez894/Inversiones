package com.example.billy.productos;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.empleado.AdapterListaEmpleado;
import com.example.billy.empleado.ItemListaEmpleado;
import com.example.billy.empleado.V_Empleado;
import com.example.billy.inversiones.R;

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

public class Productos extends AppCompatActivity
{
    AutoCompleteTextView buscarProducto;
    ImageView buscar;
    public static ListView listaProductos;
    public static ArrayList<ItemsListaProductos_Productos> arrayList = new ArrayList<ItemsListaProductos_Productos>();
    public static ArrayList<String> arrayListNombresProductos = new ArrayList<String>();

    public static ArrayList<ItemsListaCategoria> arrayListCategoria = new ArrayList<ItemsListaCategoria>();
    public static ArrayList<String> arrayListNombresCategoria = new ArrayList<String>();

    Spinner categoria;
    TextView descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        buscarProducto = (AutoCompleteTextView)findViewById(R.id.autocompleteBuscarProducto_Producto);
        buscar = (ImageView)findViewById(R.id.imgBuscarProducto_Producto);
        categoria =(Spinner)findViewById(R.id.spinnerCategoria_FiltarProducto);
        descripcion =(TextView)findViewById(R.id.textDescripcion_Producto);
        listaProductos = (ListView)findViewById(R.id.listaProductos_Producto);

        ActualizarLista();

        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                ItemsListaProductos_Productos producto = arrayList.get(position);

                Intent intent = new Intent(Productos.this, V_Producto.class);

                intent.putExtra("nombre", producto.getNombre());
                intent.putExtra("descripcion", producto.getDescripcion());
                intent.putExtra("cantidad", producto.getCantidad());
                intent.putExtra("garantia", producto.getGarantia());
                intent.putExtra("precioCompra", producto.getPrecioCompra());
                intent.putExtra("precioVenta", producto.getPrecioVenta());

                //Para sacar el nombre de la categoria
                String idCat = producto.getIdCategoria();
                idCat = cargarCategoriaActual(idCat);

                intent.putExtra("idCategoria", idCat);

                startActivity(intent);
            }
        });

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ItemsListaCategoria categoria = arrayListCategoria.get(position);
                descripcion.setText(categoria.getDescripcion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Autocomplete de los productos
        buscarProducto.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nombre = buscarProducto.getText().toString();
                int posicion = 0;

                for(int i = 0; i < arrayList.size(); i++)
                {
                    if(nombre.equalsIgnoreCase(arrayList.get(i).getNombre()))
                    {
                        posicion = i;
                        break;
                    }
                }

                ItemsListaProductos_Productos producto = arrayList.get(posicion);

                Intent intent = new Intent(Productos.this, V_Producto.class);

                intent.putExtra("nombre", producto.getNombre());
                intent.putExtra("descripcion", producto.getDescripcion());
                intent.putExtra("cantidad", producto.getCantidad());
                intent.putExtra("garantia", producto.getGarantia());
                intent.putExtra("precioCompra", producto.getPrecioCompra());
                intent.putExtra("precioVenta", producto.getPrecioVenta());

                //Para sacar el nombre de la categoria
                String idCat = producto.getIdCategoria();
                idCat = cargarCategoriaActual(idCat);

                intent.putExtra("idCategoria", idCat);

                startActivity(intent);
            }
        });
    }

    public String cargarCategoriaActual(String idC)
    {
        int position = 0;
        String nombreCategoria = "";

        for(int i = 0; i < arrayListCategoria.size(); i++)
        {
            if(idC.equalsIgnoreCase(arrayListCategoria.get(i).getIdCategoria()))
            {
                nombreCategoria = arrayListCategoria.get(i).getNombre();
            }
        }

        return nombreCategoria;
    }

    public void cargarCategorias()
    {
        arrayListCategoria.clear();
        arrayListNombresCategoria.clear();
        TareaCategorias categorias = new TareaCategorias();
        categorias.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCategorias();
    }

    public void ActualizarLista()
    {
        arrayList.clear();
        arrayListNombresProductos.clear();
        TareaProductos productos = new TareaProductos();
        productos.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_productos, menu);
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
            case R.id.agregarProducto_Productos:
                Intent intent = new Intent(Productos.this,AgregarProducto.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
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

    //Clases Asyntask para traer los datos de la tabla categorias

    private class TareaCategorias extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCategoria.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllCategory"));

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
                    arrayListCategoria.add(new ItemsListaCategoria(obj.getString("idCategoria"), obj.getString("nombre"), obj.getString("descripcion")));
                    arrayListNombresCategoria.add(obj.getString("nombre"));
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
            //Toast.makeText(Productos.this, respStr, Toast.LENGTH_SHORT).show();
            categoria.setAdapter(new ArrayAdapter<String>(Productos.this, android.R.layout.simple_spinner_dropdown_item, arrayListNombresCategoria));
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

                //String obj

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);
                    arrayList.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("tiempoGarantia"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));
                    arrayListNombresProductos.add(obj.getString("nombre"));

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
            //Toast.makeText(Productos.this, respStr, Toast.LENGTH_SHORT).show();
            listaProductos.setAdapter(new AdapterListaProductos_Productos(Productos.this, arrayList));
            buscarProducto.setAdapter(new ArrayAdapter<String>(Productos.this, android.R.layout.simple_dropdown_item_1line, arrayListNombresProductos));
        }
    }
}
