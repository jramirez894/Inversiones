package com.example.billy.empleado;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
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

public class Empleados extends AppCompatActivity
{
    AutoCompleteTextView buscarEmpleado;
    public static ListView listaEmpleado;
    ImageView buscar;

    public static ArrayList<ItemListaEmpleado> arrayList = new ArrayList<ItemListaEmpleado>();
    public static ArrayList<String> arrayListNombresVe = new ArrayList<String>();

    boolean existe = false;
    String respuesta = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        buscarEmpleado = (AutoCompleteTextView)findViewById(R.id.autocompleteBuscarEmpleado_Empleado);
        listaEmpleado = (ListView)findViewById(R.id.listaEmpleados_Empleado);
        buscar = (ImageView)findViewById(R.id.imgBuscarEmpleado_Empleado);

        //ActualizarLista();

        listaEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                ItemListaEmpleado empleado = arrayList.get(position);

                Intent intent = new Intent(Empleados.this, V_Empleado.class);
                intent.putExtra("nombre", empleado.getNombreEmp());
                intent.putExtra("telefono", empleado.getTelefonoEmp());
                startActivity(intent);
            }
        });

        //Listado de Vendedores
        TareaListado tareaListado = new TareaListado();
        tareaListado.execute();

        //AutocompleteView
        buscarEmpleado.setAdapter(new ArrayAdapter<String>(Empleados.this, android.R.layout.simple_dropdown_item_1line, arrayListNombresVe));

        buscarEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nombre = buscarEmpleado.getText().toString();
                int posicion = 0;

                for(int i = 0; i < arrayList.size(); i++)
                {
                    if(nombre.equalsIgnoreCase(arrayList.get(i).getNombre()))
                    {
                        posicion = i;
                        break;
                    }
                }

                ItemListaEmpleado empleado = arrayList.get(posicion);

                Intent intent = new Intent(Empleados.this, V_Empleado.class);
                intent.putExtra("nombre", empleado.getNombreEmp());
                intent.putExtra("telefono", empleado.getTelefonoEmp());
                startActivity(intent);
            }
        });
    }

    public void ActualizarLista()
    {
        listaEmpleado.setAdapter(new AdapterListaEmpleado(this, arrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empleados, menu);
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
            case R.id.agregarEmpleado_Empleado:
                Intent intent = new Intent(Empleados.this, AgregarEmpleado.class);
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
                    arrayList.clear();
                    arrayListNombresVe.clear();
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        arrayList.add(new ItemListaEmpleado(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idUsuario"), obj.getString("cedula"), obj.getString("nombre"), obj.getString("direccion"), obj.getString("telefono"), obj.getString("correo"), obj.getString("password")));
                        arrayListNombresVe.add(obj.getString("nombre"));
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
            listaEmpleado.setAdapter(new AdapterListaEmpleado(Empleados.this, arrayList));
        }
    }
}
