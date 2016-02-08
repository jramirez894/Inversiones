package com.example.billy.inversiones;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.base_datos.BaseDatos;
import com.example.billy.base_datos.ConexionBD;
import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.menu_principal.PrincipalMenu;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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

public class MainActivity extends AppCompatActivity
{
    EditText cedula;
    EditText contrasena;

    Button ingresar;
    TextView olvidar;

    boolean existe = false;

    String respuesta = "";

    ProgressBar progressLogin;
    View layoutProgress;
    View layoutLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Base de datos para fechas pendientes
        ConexionBD conexionBD = new ConexionBD(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        cedula =(EditText)findViewById(R.id.editTextCedula);
        contrasena =(EditText)findViewById(R.id.editTextContrasena);
        ingresar=(Button)findViewById(R.id.buttonLogin);
        olvidar=(TextView)findViewById(R.id.texViewOlvidarContrasena);

        progressLogin = (ProgressBar) findViewById(R.id.progressLogin);

        //Vistas del login
        layoutProgress = (View) findViewById(R.id.layoutProgress);
        layoutLogin = (View) findViewById(R.id.layoutLogin);

        ingresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String cedu = cedula.getText().toString();
                String contra = contrasena.getText().toString();

                if(cedu.equalsIgnoreCase("") || contra.equalsIgnoreCase(""))
                {
                    Toast.makeText(MainActivity.this, "Faltan Campos por Rellenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    layoutProgress.setVisibility(View.VISIBLE);
                    layoutLogin.setVisibility(View.GONE);
                    TareaLogin tareaRol = new TareaLogin();
                    tareaRol.execute(cedula.getText().toString(), contrasena.getText().toString());
                }
            }
        });

        olvidar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,OlvidarContrasena.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    //Clases Asyntask para login y rol del usuario que inicia sesion
    private class TareaLogin extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;

        int progreso;

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
            nameValuePairs.add(new BasicNameValuePair("cedula", params[0]));
            nameValuePairs.add(new BasicNameValuePair("password", params[1]));
            nameValuePairs.add(new BasicNameValuePair("option",  "signInUsser"));

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
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //return false;

            while (progreso<100)
            {
                progreso++;
                publishProgress(progreso);
                SystemClock.sleep(20);
            }

            return resul;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progreso = 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            progressLogin.setProgress(values[0]);
        }

        protected void onPostExecute(Boolean result)
        {
            /*Toast.makeText(MainActivity.this, SesionUsuarios.getCorreo() + "\n"
                    + SesionUsuarios.getRol() + "\n"
                    + SesionUsuarios.getCedula() + "\n"
                    + SesionUsuarios.getContrasena() + "\n"
                    + SesionUsuarios.getDireccion() + "\n"
                    + SesionUsuarios.getIdUsuario() + "\n"
                    + SesionUsuarios.getNombre() + "\n"
                    + SesionUsuarios.getTelefono() + "\n", Toast.LENGTH_SHORT).show();*/

            if(existe)
            {
                if(SesionUsuarios.getRol().equalsIgnoreCase("AD"))
                {
                    Intent clientApp = new Intent(MainActivity.this, PrincipalMenu.class);
                    startActivity(clientApp);
                    layoutProgress.setVisibility(View.GONE);
                    layoutLogin.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(SesionUsuarios.getRol().equalsIgnoreCase("VE"))
                    {
                        Intent clientApp = new Intent(MainActivity.this, PrincipalEmpleado.class);
                        startActivity(clientApp);
                        layoutProgress.setVisibility(View.GONE);
                        layoutLogin.setVisibility(View.VISIBLE);
                    }
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "El Usuario no Existe", Toast.LENGTH_SHORT).show();
                layoutProgress.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);
            }
        }
    }
}
