package com.example.billy.empleado;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.inversiones.R;
import com.example.billy.inversiones.SesionUsuarios;
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

public class AgregarEmpleado extends AppCompatActivity
{
    EditText cedula;
    EditText nombreE;
    EditText direccion;
    EditText telefono;
    EditText correo;
    EditText contrasena;
    EditText verContrasena;

    boolean resul;

    Object respuesta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_empleado);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        cedula =(EditText)findViewById(R.id.editCedula_AgregarEmpleado);
        nombreE =(EditText)findViewById(R.id.editNombre_AgregarEmpleado);
        direccion =(EditText)findViewById(R.id.editDireccion_AgregarEmpleado);
        telefono =(EditText)findViewById(R.id.editTelefono_AgregarEmpleado);
        correo =(EditText)findViewById(R.id.editCorreo_AgregarEmpleado);
        contrasena =(EditText)findViewById(R.id.editContrasena_AgregarEmpleado);
        verContrasena =(EditText)findViewById(R.id.editVerificacionContrasena_AgregarEmpleado);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_empleado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.guardarEmpleado_Empleado:
                String ced = cedula.getText().toString();
                String nom = nombreE.getText().toString();
                String dir = direccion.getText().toString();
                String tel = telefono.getText().toString();
                String cor = correo.getText().toString();
                String cont = contrasena.getText().toString();
                String verCon = verContrasena.getText().toString();

                if (ced.equals("")||
                    nom.equals("")||
                    dir.equals("")||
                    tel.equals("")||
                    cor.equals("")||
                    cont.equals("")||
                    verCon.equals(""))
                {
                    Toast.makeText(AgregarEmpleado.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(cont.equalsIgnoreCase(verCon))
                    {
                        guardarEmpleado();
                    }
                    else
                    {
                        Toast.makeText(AgregarEmpleado.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Alerta de Confirmacion
    public void guardarEmpleado()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Agregar Empleado?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String ced = cedula.getText().toString();
                String nom = nombreE.getText().toString();
                String dir = direccion.getText().toString();
                String tel = telefono.getText().toString();
                String cor = correo.getText().toString();
                String cont = contrasena.getText().toString();

                TareaUpdadte tareaUpdadte = new TareaUpdadte();
                tareaUpdadte.execute(ced, nom, dir, tel, cor, cont);
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

    //Clases Asyntask para login y rol del usuario que inicia sesion

    private class TareaUpdadte extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("cedula", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("direccion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[3]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[4]));
            nameValuePairs.add(new BasicNameValuePair("password", params[5]));
            nameValuePairs.add(new BasicNameValuePair("option", "createUsser"));

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(AgregarEmpleado.this, respuesta.toString(), Toast.LENGTH_SHORT).show();

            String resp = respuesta.toString();

            if(resul)
            {
                if(resp.equalsIgnoreCase("La cedula ya existe"))
                {
                    Toast.makeText(AgregarEmpleado.this, resp, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(resp.equalsIgnoreCase("Vendedor agregado exitosamente"))
                    {
                        Toast.makeText(AgregarEmpleado.this, resp, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AgregarEmpleado.this, Empleados.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            else
            {
                Toast.makeText(AgregarEmpleado.this, "Error al Agregar Vendedor", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
