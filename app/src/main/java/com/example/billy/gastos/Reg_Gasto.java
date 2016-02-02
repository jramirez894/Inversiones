package com.example.billy.gastos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;
import com.example.billy.inversiones.SesionUsuarios;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.productos.ItemsListaCategoria;
import com.example.billy.productos.Productos;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Reg_Gasto extends AppCompatActivity implements View.OnClickListener
{
    Spinner tipoGasto;
    TextView descripcionGasto;
    EditText valor;
    EditText descripcion;
    EditText fecha;
    Button guardar;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public static ArrayList<ItemsTipoGasto> arrayListTipoGasto = new ArrayList<ItemsTipoGasto>();
    public static ArrayList<String> arrayListNombresTipoGasto = new ArrayList<String>();

    boolean resul;
    Object respuesta = "";

    //Alerta Cargando
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__gasto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        tipoGasto = (Spinner)findViewById(R.id.spinnerReg_Gasto_Gasto);
        descripcionGasto = (TextView)findViewById(R.id.texViewDescripcionGasto_Gasto);
        valor = (EditText)findViewById(R.id.editTextValorGasto_Gasto);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_Gasto);
        guardar = (Button)findViewById(R.id.buttonReg_Gasto_Gasto);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fecha = (EditText)findViewById(R.id.editTextFecha_Gasto);
        fecha.setInputType(InputType.TYPE_NULL);
        fecha.requestFocus();

        setDateTimeField();

        guardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String val = valor.getText().toString();
                String des = descripcion.getText().toString();
                String fec = fecha.getText().toString();

                if (val.equals("")||
                        des.equals("")||
                        fec.equals(""))
                {
                    Toast.makeText(Reg_Gasto.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegistrarGasto();
                }
            }
        });

        //Buscar la descripcion de cada tipo de gasto
        tipoGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ItemsTipoGasto tipoGasto = arrayListTipoGasto.get(position);
                descripcionGasto.setText(tipoGasto.getDescripcion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void setDateTimeField()
    {
        fecha.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fecha.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public String cargarTipoGastoActual()
    {
        String id = "";
        String nombreGasto = tipoGasto.getSelectedItem().toString();

        for(int i = 0; i < arrayListTipoGasto.size(); i++)
        {
            if(nombreGasto.equalsIgnoreCase(arrayListTipoGasto.get(i).getNombre()))
            {
                id = arrayListTipoGasto.get(i).getIdTipoGasto();
            }
        }

        return id;
    }

    //Alerta de Confirmacion
    public void RegistrarGasto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Registrar Gasto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String idTipoGasto = cargarTipoGastoActual();

                //Para capturar la fecha y concatenar la hora
                Date date = new Date();
                DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                String fechaHoraModificada= fecha.getText().toString() + " " + hourFormat.format(date);

                //Clase AsynTask
                TareaCreateGasto createGasto = new TareaCreateGasto();
                createGasto.execute(valor.getText().toString(), fechaHoraModificada, descripcion.getText().toString(), idTipoGasto);

                AlertaCargando();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg__gasto, menu);
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
            case R.id.historial_RegCobro:
                Constantes.atrasHistorial="Reg_Gasto";
                Intent intent = new Intent(Reg_Gasto.this,Historial.class);
                intent.putExtra("Atras",Constantes.atrasHistorial);
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

    @Override
    public void onClick(View view)
    {
        if(view == fecha) {
            datePickerDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTipoGasto();
    }

    public void cargarTipoGasto()
    {
        AlertaCargando();

        arrayListNombresTipoGasto.clear();
        arrayListTipoGasto.clear();
        TareaTipoGasto tipoGasto = new TareaTipoGasto();
        tipoGasto.execute();
    }

    //Clases Asyntask para traer los datos de la tabla categorias

    private class TareaTipoGasto extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerTipoGasto.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllType"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objTipoGasto = objItems.getJSONArray(0);

                for(int i=0; i<objTipoGasto.length(); i++)
                {
                    JSONObject obj = objTipoGasto.getJSONObject(i);
                    arrayListTipoGasto.add(new ItemsTipoGasto(obj.getString("idTipoGasto"), obj.getString("nombre"), obj.getString("descripcion")));
                    arrayListNombresTipoGasto.add(obj.getString("nombre"));
                    resul = true;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(Reg_Gasto.this, respStr, Toast.LENGTH_SHORT).show();
            tipoGasto.setAdapter(new ArrayAdapter<String>(Reg_Gasto.this, android.R.layout.simple_spinner_dropdown_item, arrayListNombresTipoGasto));
            progressDialog.dismiss();
        }
    }

    //Clases Asyntask para agregar un gasto
    private class TareaCreateGasto extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerGastos.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("valor", params[0]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[1]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("idUsuario", SesionUsuarios.getIdUsuario()));
            nameValuePairs.add(new BasicNameValuePair("idTipoGasto", params[3]));
            nameValuePairs.add(new BasicNameValuePair("option", "createSpending"));

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
            //Toast.makeText(AgregarProducto.this, respStr.toString(), Toast.LENGTH_SHORT).show();

            String resp = respuesta.toString();

            if(resul)
            {
                if(resp.equalsIgnoreCase("Gasto agregado exitosamente"))
                {
                    Toast.makeText(Reg_Gasto.this, resp, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Reg_Gasto.this, Reg_Gasto.class);
                    startActivity(intent);
                    finish();

                    progressDialog.dismiss();
                }

            }
            else
            {
                Toast.makeText(Reg_Gasto.this, "Error al Agregar el Gasto", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }
}
