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

import com.example.billy.inversiones.SesionUsuarios;
import com.example.billy.menu_principal.PrincipalMenu;
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

public class M_Empleado extends AppCompatActivity
{
    EditText cedula;
    EditText nombreE;
    EditText direccion;
    EditText telefono;
    EditText correo;
    EditText contrasena;
    EditText verContrasena;

    String pos = "";
    String ced = "";
    String nom = "";
    String dir = "";
    String tel = "";
    String cor = "";
    String pas = "";
    String vPass = "";

    //Para verificar si ingreso una contraseña nueva
    String passNuevo = "";

    String respuesta = "";
    boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m__empleado);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        Bundle extra = getIntent().getExtras();
        pos = extra.getString("idUsuario");
        ced = extra.getString("cedula");
        nom = extra.getString("nombre");
        dir = extra.getString("direccion");
        tel = extra.getString("telefono");
        cor = extra.getString("correo");
        pas = extra.getString("password");

        cedula =(EditText)findViewById(R.id.editCedula_M_Empleado);
        nombreE =(EditText)findViewById(R.id.editNombre_M_Empleado);
        direccion =(EditText)findViewById(R.id.editDireccion_M_Empleado);
        telefono =(EditText)findViewById(R.id.editTelefono_M_Empleado);
        correo =(EditText)findViewById(R.id.editCorreo_M_Empleado);
        contrasena =(EditText)findViewById(R.id.editContrasena_M_Empleado);
        verContrasena =(EditText)findViewById(R.id.editVerificacionContrasena_M_Empleado);

        cedula.setText(ced);
        nombreE.setText(nom);
        direccion.setText(dir);
        telefono.setText(tel);
        correo.setText(cor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_m__empleado, menu);
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
            case R.id.modificar_MEmpleado:

                ced = cedula.getText().toString();
                nom = nombreE.getText().toString();
                dir = direccion.getText().toString();
                tel = telefono.getText().toString();
                cor = correo.getText().toString();
                passNuevo = contrasena.getText().toString();
                vPass = verContrasena.getText().toString();

                if(ced.equalsIgnoreCase("") ||
                        nom.equalsIgnoreCase("") ||
                        dir.equalsIgnoreCase("") ||
                        tel.equalsIgnoreCase("") ||
                        cor.equalsIgnoreCase(""))
                {
                    Toast.makeText(M_Empleado.this, "Faltan datos por rellenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(passNuevo.equalsIgnoreCase(""))
                    {
                        ModificarEmpleado();
                    }
                    else
                    {
                        if(passNuevo.equalsIgnoreCase(vPass))
                        {
                            pas = passNuevo;
                            ModificarEmpleado();
                        }
                        else
                        {
                            Toast.makeText(M_Empleado.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Alerta de Confirmacion
    public void ModificarEmpleado()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Modificar Empleado?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                TareaUpdadte updadte = new TareaUpdadte();
                updadte.execute(ced, nom, tel, cor, dir, pas);

                //Toast.makeText(M_Empleado.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
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

    //Clases Asyntask para actualizar un empleado

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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idUsuario", pos));
            nameValuePairs.add(new BasicNameValuePair("cedula", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[2]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[3]));
            nameValuePairs.add(new BasicNameValuePair("direccion", params[4]));
            nameValuePairs.add(new BasicNameValuePair("password", params[5]));
            nameValuePairs.add(new BasicNameValuePair("tipoUsuario", "VE"));
            nameValuePairs.add(new BasicNameValuePair("option", "updateUsser"));

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
                Toast.makeText(M_Empleado.this, "Datos Modificados con Exito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(M_Empleado.this, Empleados.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(M_Empleado.this, "Error al Modificar Usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
