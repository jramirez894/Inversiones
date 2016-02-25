package com.example.billy.productos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class M_Producto extends AppCompatActivity
{
    EditText nombre;
    Spinner categoria;
    EditText descripcion;
    EditText cantidad;
    EditText garantia;
    EditText precioCompra;
    EditText precioVenta;

    String idP = "";
    String nom = "";
    String idC = "";
    String des = "";
    String can = "";
    String gar = "";
    String preC = "";
    String preV = "";

    public static ArrayList<ItemsListaCategoria> arrayListCategoria = new ArrayList<ItemsListaCategoria>();
    public static ArrayList<String> arrayListNombresCategoria = new ArrayList<String>();

    ArrayAdapter<String> adapter;

    String respuesta = "";
    boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m__producto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        Bundle extra = getIntent().getExtras();
        idP = extra.getString("idProducto");
        nom = extra.getString("nombre");
        idC = extra.getString("idCategoria");
        des = extra.getString("descripcion");
        can = extra.getString("cantidad");
        gar = extra.getString("garantia");
        preC = extra.getString("precioCompra");
        preV = extra.getString("precioVenta");

        nombre = (EditText)findViewById(R.id.editTextNombre_MProducto);
        categoria = (Spinner)findViewById(R.id.spinnerCategoria_MProducto);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_MProducto);
        cantidad = (EditText)findViewById(R.id.editTextCantidad_MProducto);
        garantia = (EditText)findViewById(R.id.editTextGarantia_MProducto);
        precioCompra = (EditText)findViewById(R.id.editTextPrecioCompra_MProducto);
        precioVenta = (EditText)findViewById(R.id.editTextPrecioVenta_MProducto);

        nombre.setText(nom);
        descripcion.setText(des);
        cantidad.setText(can);
        garantia.setText(gar);
        precioCompra.setText(preC);
        precioVenta.setText(preV);

        cargarCategorias();

        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.categoria_producto,android.R.layout.simple_spinner_dropdown_item);
        //categoria.setAdapter(adapter);
    }

    public void cargarCategoriaActual()
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

        position = adapter.getPosition(nombreCategoria);
        categoria.setSelection(position);
    }

    public void cargarCategorias()
    {
        arrayListCategoria.clear();
        arrayListNombresCategoria.clear();
        TareaCategorias categorias = new TareaCategorias();
        categorias.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_m__producto, menu);
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
            case R.id.modificar_MProducto:

                String nom = nombre.getText().toString();
                String des = descripcion.getText().toString();
                String can = cantidad.getText().toString();
                String gar = garantia.getText().toString();
                String preC = precioCompra.getText().toString();
                String preV = precioVenta.getText().toString();

                if(nom.equalsIgnoreCase("") ||
                        des.equalsIgnoreCase("") ||
                        can.equalsIgnoreCase("") ||
                        gar.equalsIgnoreCase("") ||
                        preC.equalsIgnoreCase("") ||
                        preV.equalsIgnoreCase("") )
                {
                    Toast.makeText(M_Producto.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ModificarProducto();
                }

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public String categoriaSeleccionada()
    {
        String id = "";
        String nombreCategoria = categoria.getSelectedItem().toString();

        for(int i = 0; i < arrayListCategoria.size(); i++)
        {
            if(nombreCategoria.equalsIgnoreCase(arrayListCategoria.get(i).getNombre()))
            {
                id = arrayListCategoria.get(i).getIdCategoria();
            }
        }

        return id;
    }

    //Alerta de Confirmacion
    public void ModificarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Modificar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String idC = categoriaSeleccionada();

                TareaUpdadte updadte = new TareaUpdadte();
                updadte.execute(nombre.getText().toString(), descripcion.getText().toString(), cantidad.getText().toString(), garantia.getText().toString(), precioCompra.getText().toString(), precioVenta.getText().toString(), idC);
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
            adapter = new ArrayAdapter<String>(M_Producto.this, android.R.layout.simple_spinner_dropdown_item, arrayListNombresCategoria);
            categoria.setAdapter(adapter);
            cargarCategoriaActual();
        }
    }

    //Clases Asyntask para actualizar un producto
    private class TareaUpdadte extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idProducto", idP));
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
            //Toast.makeText(Perfil.this, respStr, Toast.LENGTH_SHORT).show();

            if(existe)
            {
                Toast.makeText(M_Producto.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(M_Producto.this, Productos.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(M_Producto.this, "Error al Modificar Usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
