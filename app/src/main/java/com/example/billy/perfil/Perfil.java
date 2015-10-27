package com.example.billy.perfil;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.empleado.Empleados;
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
import org.apache.http.client.methods.HttpPut;
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

public class Perfil extends AppCompatActivity
{
    EditText cedula;
    EditText nombre;
    EditText telefono;
    EditText correo;
    EditText contrasenaAdministrador;
    EditText contrasenaNueva;
    EditText contrasenaVerificarContraNueva;

    View layoutContrasena;

    String ocultarContra = "Visible";

    Button btnModificarContra;

    String respuesta = "";
    boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        cedula = (EditText)findViewById(R.id.editTextCedula_MiPerfil);
        nombre = (EditText)findViewById(R.id.editTextNombre_MiPerfil);
        telefono = (EditText)findViewById(R.id.editTextTelefono_MiPerfil);
        correo = (EditText)findViewById(R.id.editTextCorreo_MiPerfil);
        contrasenaAdministrador = (EditText)findViewById(R.id.editTextContrasenaActual_MiPerfil);
        contrasenaNueva = (EditText)findViewById(R.id.editTextContrasenaNueva_MiPerfil);
        contrasenaVerificarContraNueva = (EditText)findViewById(R.id.editTextVerifiContrasena_MiPerfil);

        layoutContrasena = (View) findViewById(R.id.layoutContrasena);

        btnModificarContra = (Button) findViewById(R.id.btnModificarContra_MiPerfil);

        mostrarPerfil();
    }

    public void mostrarPerfil()
    {
        cedula.setText(SesionUsuarios.getCedula());
        nombre.setText(SesionUsuarios.getNombre());
        telefono.setText(SesionUsuarios.getTelefono());
        correo.setText(SesionUsuarios.getCorreo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
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
          case R.id.guardar_Miperfil:

              String cedu = cedula.getText().toString();
              String nomb = nombre.getText().toString();
              String tele = telefono.getText().toString();
              String corr = correo.getText().toString();

              if(cedu.equalsIgnoreCase("") || nomb.equalsIgnoreCase("") || tele.equalsIgnoreCase("") || corr.equalsIgnoreCase(""))
              {
                  Toast.makeText(Perfil.this, "Faltan Datos por Llenar", Toast.LENGTH_SHORT).show();
              }
              else
              {
                  guardarPerfil();
              }

              break;
      }

        return super.onOptionsItemSelected(item);
    }

    public void guardarPerfil()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Guardar Modificaciones?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String contra = contrasenaAdministrador.getText().toString();

                if(contra.equalsIgnoreCase(""))
                {
                    TareaUpdadte updadte = new TareaUpdadte();
                    updadte.execute(cedula.getText().toString(), nombre.getText().toString(), telefono.getText().toString(), correo.getText().toString());
                }
                else
                {
                    if(SesionUsuarios.getContrasena().equalsIgnoreCase(contra))
                    {
                        String ceduNueva = contrasenaNueva.getText().toString();
                        String ceduNuevaVerificar = contrasenaVerificarContraNueva.getText().toString();

                        if(ceduNueva.equalsIgnoreCase("") || ceduNuevaVerificar.equalsIgnoreCase(""))
                        {
                            Toast.makeText(Perfil.this, "La Contraseña Nueva No Puede estar Vacia", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(ceduNueva.equalsIgnoreCase(ceduNuevaVerificar))
                            {
                                SesionUsuarios.setContrasena(ceduNueva);
                                TareaUpdadte updadte = new TareaUpdadte();
                                updadte.execute(cedula.getText().toString(), nombre.getText().toString(), telefono.getText().toString(), correo.getText().toString());
                            }
                            else
                            {
                                Toast.makeText(Perfil.this, "La Contraseña Nueva no Coincide", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(Perfil.this, "La Contraseña de Administrador no Coincide", Toast.LENGTH_SHORT).show();
                    }
                }

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

    public void ModificarContra(View view)
    {
        if(ocultarContra.equalsIgnoreCase("Visible"))
        {
            layoutContrasena.setVisibility(View.VISIBLE);
            ocultarContra = "Invisible";
            btnModificarContra.setText("Ocultar Contraseña");
        }
        else
        {
            if (ocultarContra.equalsIgnoreCase("Invisible"))
            {
                layoutContrasena.setVisibility(View.GONE);
                ocultarContra = "Visible";
                btnModificarContra.setText("Modificar Contraseña");
            }
        }
    }

    //Clases Asyntask para login y rol del usuario que inicia sesion

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
            nameValuePairs.add(new BasicNameValuePair("idUsuario", SesionUsuarios.getIdUsuario()));
            nameValuePairs.add(new BasicNameValuePair("cedula", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[2]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[3]));
            nameValuePairs.add(new BasicNameValuePair("direccion", SesionUsuarios.getDireccion()));
            nameValuePairs.add(new BasicNameValuePair("password", SesionUsuarios.getContrasena()));
            nameValuePairs.add(new BasicNameValuePair("tipoUsuario", SesionUsuarios.getRol()));
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
                    for(int i=0; i<objItems.length(); i++)
                    {
                        JSONObject obj = objItems.getJSONObject(i);

                        SesionUsuarios.setRol(obj.getString("tipoUsuario"));
                        SesionUsuarios.setCedula(obj.getString("cedula"));
                        SesionUsuarios.setNombre(obj.getString("nombre"));
                        SesionUsuarios.setCorreo(obj.getString("correo"));
                        SesionUsuarios.setTelefono(obj.getString("telefono"));
                        SesionUsuarios.setContrasena(obj.getString("password"));
                        SesionUsuarios.setDireccion(obj.getString("direccion"));
                        SesionUsuarios.setIdUsuario(obj.getString("idUsuario"));
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
            //Toast.makeText(Perfil.this, respStr, Toast.LENGTH_SHORT).show();

            if(existe)
            {
                Toast.makeText(Perfil.this, "Datos Modificados con Exito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Perfil.this, PrincipalMenu.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(Perfil.this, "Error al Modificar Usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
