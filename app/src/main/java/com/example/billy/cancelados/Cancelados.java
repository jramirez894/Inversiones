package com.example.billy.cancelados;

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
import android.widget.ListView;

import com.example.billy.clientes.ItemsDatosClientes;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.AdapterListaPersonalizada;
import com.example.billy.menu_principal.ItemListaPersonalizada;

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

public class Cancelados extends AppCompatActivity
{
    ArrayList<ItemsDatosClientes> arrayListClientes = new ArrayList<ItemsDatosClientes>();
    public static ArrayList<String> itemsNombreCliente = new ArrayList<String>();
    ListView lista;

    boolean existe = false;
    String respuesta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelados);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        TareaListado tareaListado = new TareaListado();
        tareaListado.execute();

        lista = (ListView)findViewById(R.id.listViewClientesCancelados);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {

                ItemsDatosClientes cliente = arrayListClientes.get(position);

                //Envio de datos a la interfaz de informacion
                Intent intent = new Intent(Cancelados.this, Inf_ClienteCancelados.class);
                intent.putExtra("idCliente",cliente.getIdCliente());
                intent.putExtra("cedulaCliente",cliente.getCedula());
                intent.putExtra("nombreCliente",cliente.getNombreCliente());
                intent.putExtra("direccion", cliente.getDireccion());
                intent.putExtra("telefono", cliente.getTelefono());
                intent.putExtra("correo", cliente.getCorreo());
                intent.putExtra("nomEmpresa", cliente.getNombreEmpresa());
                intent.putExtra("dirEmpresa", cliente.getDireccionEmpresa());
                intent.putExtra("calificacion", cliente.getCalificacion());
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cancelados, menu);
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

    //Clases Asyntask para traer los clientes
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCliente.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);

                //String obj
                respuesta= String.valueOf(objVendedores);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    arrayListClientes.clear();
                    itemsNombreCliente.clear();
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        if(obj.getString("estado").equalsIgnoreCase("Inactivo"))
                        {
                            arrayListClientes.add(new ItemsDatosClientes(obj.getString("idCliente"), obj.getString("cedula"), obj.getString("nombre"), obj.getString("direccion"), obj.getString("telefono"), obj.getString("correo"), obj.getString("nombreEmpresa"), obj.getString("direccionEmpresa"), obj.getString("estado"), obj.getString("calificacion")));
                            itemsNombreCliente.add(obj.getString("nombre"));
                        }
                    }

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
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(PrincipalMenu.this, respuesta, Toast.LENGTH_SHORT).show();
            lista.setAdapter(new ArrayAdapter<String>(Cancelados.this,android.R.layout.simple_list_item_1, itemsNombreCliente));
        }
    }
}
