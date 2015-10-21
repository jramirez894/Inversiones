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

import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.menu_principal.PrincipalMenu;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    EditText cedula;
    EditText contrasena;

    Button ingresar;
    TextView olvidar;

    boolean existe = false;

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
                TareaRol tareaRol = new TareaRol();
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

    private class TareaRol extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            String cedu = params[0];

            boolean result = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpRol = new HttpGet("http://algo/");
            httpRol.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(httpRol);
                respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    if(obj.getString("cedula").equalsIgnoreCase(cedu))
                    {
                        SesionUsuarios.setRol(obj.getString("tipoUsuario"));
                        SesionUsuarios.setCedula(obj.getString("cedula"));
                        SesionUsuarios.setNombre(obj.getString("nombre"));
                        existe = true;
                    }
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                result = false;
            }

            return result;
        }

        protected void onPostExecute(Boolean result)
        {
            if (result && existe)
            {
                TareaWSLogin login = new TareaWSLogin();
                login.execute(cedula.getText().toString(), contrasena.getText().toString());
            }

            else
            {
                if(!existe)
                {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                    alerta.setTitle("Inicio de Sesion");
                    alerta.setMessage("El Usuario no Existe en el Sistema, Compruebe su Usuario y Contraseña");
                    alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alerta.setCancelable(false);
                    alerta.show();
                }
            }
        }
    }

    private class TareaWSLogin extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost login = new HttpPost("http://server.disruptiva.company:8001/token-auth/");
            login.setHeader("content-type", "application/json");

            try
            {
                msg = new JSONObject();
                msg.put("cedula", params[0]);
                msg.put("password", params[1]);

                StringEntity entity = new StringEntity(msg.toString());
                login.setEntity(entity);

                HttpResponse resp = httpClient.execute(login);
                respStr = EntityUtils.toString(resp.getEntity());
            }

            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
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
    }
}
