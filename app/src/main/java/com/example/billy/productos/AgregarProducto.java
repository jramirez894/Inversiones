package com.example.billy.productos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billy.clientes.M_DatosCobro;
import com.example.billy.empleado.Empleados;
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

public class AgregarProducto extends AppCompatActivity
{
    EditText nombre;
    Spinner categoria;
    ImageView agregarCat;
    EditText descripcion;
    EditText cantidad;
    EditText precioCompra;
    EditText precioVenta;

    public static ArrayList<ItemsListaCategoria> arrayListCategoria = new ArrayList<ItemsListaCategoria>();
    public static ArrayList<String> arrayListNombresCategoria = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        nombre = (EditText)findViewById(R.id.editTextNombre_Producto);
        categoria = (Spinner)findViewById(R.id.spinnerCategoria_Producto);
        agregarCat = (ImageView)findViewById(R.id.imageAddCategoria_Producto);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_Producto);
        cantidad = (EditText)findViewById(R.id.editTextCantidad_Producto);
        precioCompra = (EditText)findViewById(R.id.editTextPrecioCompra_Producto);
        precioVenta = (EditText)findViewById(R.id.editTextPrecioVenta_Producto);

        TareaCategorias categorias = new TareaCategorias();
        categorias.execute();

        agregarCat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertaAgregarCategoria();
            }
        });
    }

    //Alerta para agregar una nueva categoria
    public void AlertaAgregarCategoria()
    {
        LayoutInflater inflaterAlert = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflaterAlert.inflate(R.layout.alert_categoriaproducto, null);

        final EditText categoria = (EditText)dialoglayout.findViewById(R.id.editCategoria_aler_producto);
        final EditText descripcion = (EditText)dialoglayout.findViewById(R.id.editDescripcionCategoria_aler_producto);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.guardar);
        builder.setTitle("Nueva Categoria");
        builder.setView(dialoglayout);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String capCategoria = categoria.getText().toString();
                String capDescripcion = descripcion.getText().toString();
                if (capCategoria.equals("")||
                        capDescripcion.equals(""))
                {
                    Toast.makeText(AgregarProducto.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AgregarProducto.this, "Categoria Agregada Correctamente", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        String nom = nombre.getText().toString();
        String cate = categoria.getSelectedItem().toString();
        String des = descripcion.getText().toString();
        String cant = nombre.getText().toString();
        String preCom = nombre.getText().toString();
        String preVen = nombre.getText().toString();


        switch (item.getItemId())
        {
            case R.id.guardarProducto_Producto:
                if (nom.equals("")||
                    des.equals("")||
                    cant.equals("")||
                    preCom.equals("")||
                    preVen.equals(""))
                {
                    Toast.makeText(AgregarProducto.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    guardarProducto();
                }


        }
        return super.onOptionsItemSelected(item);
    }

    public void guardarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Agregar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AgregarProducto.this, "Su Registro fue Exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AgregarProducto.this, Productos.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
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

                arrayListCategoria.clear();

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
            categoria.setAdapter(new ArrayAdapter<String>(AgregarProducto.this, android.R.layout.simple_spinner_dropdown_item, arrayListNombresCategoria));
        }
    }
}
