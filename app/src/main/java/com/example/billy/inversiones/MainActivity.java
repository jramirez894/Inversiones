package com.example.billy.inversiones;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();

        cedula =(EditText)findViewById(R.id.editTextCedula);
        contrasena =(EditText)findViewById(R.id.editTextContrasena);
        ingresar=(Button)findViewById(R.id.buttonLogin);
        olvidar=(TextView)findViewById(R.id.texViewOlvidarContrasena);

        ingresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TareaLogin tareaRol = new TareaLogin();
                tareaRol.execute(cedula.getText().toString(), contrasena.getText().toString());
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
    public boolean onOptionsItemSelected(MenuItem item) {
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
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            /*boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost login = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php/");
            login.setHeader("content-type", "application/json");

            try
            {
                msg = new JSONObject();
                msg.put("cedula", params[0]);
                msg.put("password", params[1]);
                msg.put("option", "signInUsser");

                StringEntity entity = new StringEntity(msg.toString());
                login.setEntity(entity);

                HttpResponse resp = httpClient.execute(login);
                respStr = EntityUtils.toString(resp.getEntity());

                respuesta = respStr;

                /*JSONObject respJSON = new JSONObject(respStr);
                JSONObject objItems = respJSON.getJSONObject("items");

                String obj = String.valueOf(objItems);

                if(obj.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    SesionUsuarios.setRol(objItems.getString("tipoUsuario"));
                    SesionUsuarios.setCedula(objItems.getString("cedula"));
                    SesionUsuarios.setNombre(objItems.getString("nombre"));
                    existe = true;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;*/

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

            //return false;

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            Toast.makeText(MainActivity.this, respuesta
                    , Toast.LENGTH_SHORT).show();

            if(existe)
            {
                if(SesionUsuarios.getRol().equalsIgnoreCase("AD"))
                {
                    Intent clientApp = new Intent(MainActivity.this, PrincipalMenu.class);
                    startActivity(clientApp);
                }
                else
                {
                    if(SesionUsuarios.getRol().equalsIgnoreCase("VE"))
                    {
                        Intent clientApp = new Intent(MainActivity.this, PrincipalEmpleado.class);
                        startActivity(clientApp);
                    }
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "El Usuario no Existe", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
