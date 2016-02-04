package com.example.billy.cancelados;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.clientes.M_DatosCobro;
import com.example.billy.clientes.M_DetalleCobro;
import com.example.billy.constantes.Constantes;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PagerAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Inf_ClienteCancelados extends ActionBarActivity
{
    TextView cedula;
    TextView nombre;
    TextView direccion;
    TextView telefono;
    TextView correo;
    TextView nomEmpresa;
    TextView dirEmpresa;
    TextView sugerencia;
    Spinner estado;
    TextView txtestado;
    Button confirmar;

    String idCliente;
    String ced;
    String nom;
    String dir;
    String tel;
    String corr;
    String nomEm;
    String dirEm;
    String calificacion;


    boolean resul;
    Object respuesta = "";

    Object respuestaObj = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf__cliente_cancelados);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        Bundle bundle = getIntent().getExtras();
        idCliente = bundle.getString("idCliente");
        ced = bundle.getString("cedulaCliente");
        nom = bundle.getString("nombreCliente");
        dir = bundle.getString("direccion");
        tel = bundle.getString("telefono");
        corr = bundle.getString("correo");
        nomEm = bundle.getString("nomEmpresa");
        dirEm = bundle.getString("dirEmpresa");
        calificacion = bundle.getString("calificacion");


        cedula = (TextView)findViewById(R.id.textViewCedula_VClienteClienteCancelado);
        nombre = (TextView)findViewById(R.id.textViewNombre_VClienteClienteCancelado);
        direccion = (TextView)findViewById(R.id.textViewDireccion_VClienteClienteCancelado);
        telefono = (TextView)findViewById(R.id.textViewTelefono_VClienteClienteCancelado);
        correo = (TextView)findViewById(R.id.textViewCorreo_VClienteClienteCancelado);
        nomEmpresa = (TextView)findViewById(R.id.textViewNombreEmpresa_VClienteClienteCancelado);
        dirEmpresa = (TextView)findViewById(R.id.textViewDireccionEmpresa_ClienteCancelado);
        sugerencia = (TextView)findViewById(R.id.textViewSugerencia_ClienteCancelado);
        estado = (Spinner)findViewById(R.id.spinnerEstado_ClienteCancelado);
        txtestado = (TextView)findViewById(R.id.texViewEstado_ClienteCancelado);
        confirmar = (Button)findViewById(R.id.buttonConfirmarEstado);

        cedula.setText(ced);
        nombre.setText(nom);
        direccion.setText(dir);
        telefono.setText(tel);
        correo.setText(corr);
        nomEmpresa.setText(nomEm);
        dirEmpresa.setText(dirEm);
        sugerencia.setText(calificacion);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(Inf_ClienteCancelados.this, R.array.estado_cliente, android.R.layout.simple_spinner_dropdown_item);
        estado.setAdapter(adapter);

        //Texto que esta por defecto
        txtestado.setText("Estado del Cliente Actualmente:");

        confirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CambiarEstado();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inf__cliente_cancelados, menu);
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

    //Alerta de Confirmacion
    public void CambiarEstado()
    {
        final String capSpinner = estado.getSelectedItem().toString();

        if(capSpinner.equalsIgnoreCase("Inactivo"))
        {
            Toast.makeText(Inf_ClienteCancelados.this, "Este cliente ya se encuentra Inactivo", Toast.LENGTH_LONG).show();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_menu_save);
            builder.setTitle("Estado");
            builder.setMessage("¿Cambiar de Estado?"+capSpinner);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    TareaUpdateClient tareaUpdateClient = new TareaUpdateClient();
                    tareaUpdateClient.execute(capSpinner);
                }
            });

            builder.setNegativeButton("Cancelar",null );
            builder.setCancelable(false);
            builder.show();
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

    //Tarea modificar cliente
    private class TareaUpdateClient extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCliente.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idCliente", idCliente));
            nameValuePairs.add(new BasicNameValuePair("cedula", ced));
            nameValuePairs.add(new BasicNameValuePair("nombre", nom));
            nameValuePairs.add(new BasicNameValuePair("direccion", dir));
            nameValuePairs.add(new BasicNameValuePair("telefono", tel));
            nameValuePairs.add(new BasicNameValuePair("correo", corr));
            nameValuePairs.add(new BasicNameValuePair("nombreEmpresa", nomEm));
            nameValuePairs.add(new BasicNameValuePair("direccionEmpresa", dirEm));
            nameValuePairs.add(new BasicNameValuePair("estado", params[0]));
            nameValuePairs.add(new BasicNameValuePair("calificacion", calificacion));
            nameValuePairs.add(new BasicNameValuePair("option", "updateClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= respJSON.get("items");
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
            Intent intent = new Intent(Inf_ClienteCancelados.this,Cancelados.class);
            startActivity(intent);
            finish();
            Toast.makeText(Inf_ClienteCancelados.this,"El Estado del Cliente se Modifico Correctamente",Toast.LENGTH_SHORT).show();
        }
    }

    private class TareaDelete extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCliente.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[0]));
            nameValuePairs.add(new BasicNameValuePair("option", "deleteClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);

                respuestaObj= respJSON.get("items");
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

        }
    }
}
