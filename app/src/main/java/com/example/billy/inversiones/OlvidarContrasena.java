package com.example.billy.inversiones;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.billy.empleado.AdapterListaEmpleado;
import com.example.billy.empleado.Empleados;
import com.example.billy.empleado.ItemListaEmpleado;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OlvidarContrasena extends AppCompatActivity
{
    EditText correo;
    Button enviar;

    boolean existe = false;
    String respuesta = "";

    String capCorreo = "";

    boolean resul;
    Object respuestaCorreo = "";

    Random randomContra;

    //Alerta Cargando
    ProgressDialog progressDialog;

    String idUsuario = "";

    int nuevaContra = 0;

    //Confirmar si el correo ingresado existe
    boolean confirmarCorreo = false;

    public static ArrayList<ItemsUsuario> arrayList = new ArrayList<ItemsUsuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_contrasena);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");

        correo=(EditText)findViewById(R.id.editTextCorreo_OlvidarContrasena);
        enviar=(Button)findViewById(R.id.buttonEnviar_OlvidarContrasena);

        enviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                capCorreo =correo.getText().toString();

                if (capCorreo.equals(""))
                {
                    Toast.makeText(OlvidarContrasena.this,"Escriba su Correo",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(isEmailValid(capCorreo))
                    {
                        AbrirAlerta();
                    }
                    else
                    {
                        Toast.makeText(OlvidarContrasena.this, "Debe Ingresar un Correo Valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        randomContra = new Random();
    }

    public void AbrirAlerta()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_send);
        builder.setTitle("Enviar");
        builder.setMessage("¿Enviar Codigo de Recuperación?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertaCargando();

                TareaListado tareaListado = new TareaListado();
                tareaListado.execute();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_olvidar_contrasena, menu);
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

    public void AlertaCargando()
    {
        //Alerta que carga mientras se cargan los Clientes
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
    }

    private class TareaListado extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAll"));

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

                if(respuesta.equalsIgnoreCase("No Existen Usuarios"))
                {
                    existe = false;
                }
                else
                {
                    arrayList.clear();
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        arrayList.add(new ItemsUsuario(obj.getString("idUsuario"), obj.getString("cedula"), obj.getString("nombre"), obj.getString("telefono"), obj.getString("correo"), obj.getString("direccion"), obj.getString("password"), obj.getString("tipoUsuario")));
                        existe = true;
                    }
                }
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                existe = false;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return existe;
        }

        protected void onPostExecute(Boolean result)
        {
            if(existe)
            {
                for(int i = 0; i < arrayList.size(); i++)
                {
                    if(capCorreo.equalsIgnoreCase(arrayList.get(i).getCorreo()))
                    {
                        idUsuario = arrayList.get(i).getIdUsuario();

                        nuevaContra = randomContra.nextInt(1000000000);

                        TareaCorreo tareaCorreo = new TareaCorreo();
                        tareaCorreo.execute(capCorreo, String.valueOf(nuevaContra));

                        confirmarCorreo = true;
                    }
                }

                if(!confirmarCorreo)
                {
                    progressDialog.dismiss();
                    Toast.makeText(OlvidarContrasena.this, "El correo no es valido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(OlvidarContrasena.this, "Error al Verificar el correo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    //Clases Asyntask para agregar un empleado
    private class TareaCorreo extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("correo", params[0]));
            nameValuePairs.add(new BasicNameValuePair("random", params[1]));
            nameValuePairs.add(new BasicNameValuePair("option", "correo"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuestaCorreo = respJSON.get("items");
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
            if(resul)
            {
                for (int i = 0; i < arrayList.size(); i++)
                {
                    if(idUsuario.equalsIgnoreCase(arrayList.get(i).getIdUsuario()))
                    {
                        TareaUpdadte tareaUpdadte = new TareaUpdadte();
                        tareaUpdadte.execute(arrayList.get(i).getIdUsuario(),
                                arrayList.get(i).getCedula(),
                                arrayList.get(i).getNnombre(),
                                arrayList.get(i).getTelefono(),
                                arrayList.get(i).getCorreo(),
                                arrayList.get(i).getDireccion(),
                                String.valueOf(nuevaContra),
                                arrayList.get(i).getTipoUsuario());
                    }
                }
            }
            else
            {
                Toast.makeText(OlvidarContrasena.this, "Error al Verificar el correo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

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
            nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
            nameValuePairs.add(new BasicNameValuePair("cedula", params[1]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[2]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[3]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[4]));
            nameValuePairs.add(new BasicNameValuePair("direccion", params[5]));
            nameValuePairs.add(new BasicNameValuePair("password", params[6]));
            nameValuePairs.add(new BasicNameValuePair("tipoUsuario", params[7]));
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
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (JSONException e) {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if(existe)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(OlvidarContrasena.this);
                builder.setIcon(R.mipmap.contrasena);
                builder.setTitle("Restablecer Contraseña");
                builder.setMessage("La Contraseña sera restablecida en unos cuantos minutos por favor revisar el correo");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent=new Intent(OlvidarContrasena.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();

                progressDialog.dismiss();
            }
            else
            {
                Toast.makeText(OlvidarContrasena.this, "Error al Verificar el correo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
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
}
