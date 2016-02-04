package com.example.billy.empleado;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //Alerta Cargando
    ProgressDialog progressDialog;

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
                    if(isEmailValid(cor))
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
                    else
                    {
                        Toast.makeText(AgregarEmpleado.this, "Debe Ingresar un Correo Valido", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void AlertaCargando()
    {
        //Alerta que carga mientras se cargan los Clientes
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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
                AlertaCargando();

                String ced = cedula.getText().toString();
                String nom = nombreE.getText().toString();
                String dir = direccion.getText().toString();
                String tel = telefono.getText().toString();
                String cor = correo.getText().toString();
                String cont = contrasena.getText().toString();

                TareaCreate tareaCreate = new TareaCreate();
                tareaCreate.execute(ced, nom, dir, tel, cor, cont);
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

    //Clases Asyntask para agregar un empleado
    private class TareaCreate extends AsyncTask<String,Integer,Boolean>
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
            }

            catch (JSONException e)
            {
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

                    progressDialog.dismiss();
                }
                else
                {
                    if(resp.equalsIgnoreCase("Vendedor agregado exitosamente"))
                    {
                        Toast.makeText(AgregarEmpleado.this, resp, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AgregarEmpleado.this, Empleados.class);
                        startActivity(intent);
                        finish();

                        progressDialog.dismiss();
                    }
                }
            }
            else
            {
                Toast.makeText(AgregarEmpleado.this, "Error al Agregar Vendedor", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }
}
