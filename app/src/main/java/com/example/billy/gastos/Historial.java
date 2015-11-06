package com.example.billy.gastos;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.inversiones.R;
import com.example.billy.inversiones.SesionUsuarios;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.productos.ItemsListaCategoria;
import com.example.billy.saldo_caja.SaldoCaja;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Historial extends AppCompatActivity implements View.OnClickListener {
    EditText fechaInicio;
    EditText fechaFin;
    Button buscar;

    private DatePickerDialog datePickerDialogInicio;
    private SimpleDateFormat dateFormatterInicio;

    private SimpleDateFormat dateFormatterFin;
    private DatePickerDialog datePickerDialogFin;

    String respuesta = "";

    public static ArrayList<ItemLista_InfHistorial> arrayList = new ArrayList<ItemLista_InfHistorial>();

    String capInicio;
    String capFin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();


        buscar = (Button)findViewById(R.id.buttonBuscar_Historial);

        //Fecha Personalizada Fecha Inicio
        dateFormatterInicio = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatterFin = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fechaInicio = (EditText)findViewById(R.id.edidFechaInicio_Historial);
        fechaFin = (EditText)findViewById(R.id.edidFechaFin_Historial);

        fechaInicio.setInputType(InputType.TYPE_NULL);
        fechaFin.setInputType(InputType.TYPE_NULL);

        fechaInicio.requestFocus();
        fechaFin.requestFocus();

        setDateTimeFieldInicio();
        setDateTimeFieldFin();

        buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                capInicio = fechaInicio.getText().toString().replace("-", "");
                capFin = fechaFin.getText().toString().replace("-", "");

                String capInicio2 = fechaInicio.getText().toString();
                String capFin2 = fechaInicio.getText().toString();

                Constantes.fechaInicio = capInicio2;
                Constantes.fechaFin = capFin2;

                if (capInicio.equals("")||
                        capFin.equals(""))
                {
                    Toast.makeText(Historial.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    arrayList.clear();
                    TareaFecha tareaFecha = new TareaFecha();
                    tareaFecha.execute(capInicio, capFin);
                }
            }
        });
    }

    private void setDateTimeFieldInicio()
    {
        fechaInicio.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogInicio = new DatePickerDialog(Historial.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaInicio.setText(dateFormatterInicio.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setDateTimeFieldFin()
    {
        fechaFin.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogFin = new DatePickerDialog(Historial.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaFin.setText(dateFormatterFin.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
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
        if (id == android.R.id.home)
        {
            Intent intent;
            switch (Constantes.atrasHistorial)
            {
                case "Reg_Gasto":
                    intent = new Intent(Historial.this,Reg_Gasto.class);
                    startActivity(intent);
                    finish();
                    break;

                case "SaldoCaja":
                    intent = new Intent(Historial.this,SaldoCaja.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        if(view == fechaInicio)
        {
            datePickerDialogInicio.show();
        }

        if(view == fechaFin)
        {
            datePickerDialogFin.show();
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

    //Clases Asyntask para filtrar por fecha

    private class TareaFecha extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerGastos.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("fechaUno", params[0]));
            nameValuePairs.add(new BasicNameValuePair("fechaDos", params[1]));
            nameValuePairs.add(new BasicNameValuePair("option", "getAllDate"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFechas = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                //String obj

                for(int i=0; i<objFechas.length(); i++)
                {
                    JSONObject obj = objFechas.getJSONObject(i);
                    arrayList.add(new ItemLista_InfHistorial(obj.getString("descripcion"), obj.getString("valor"), obj.getString("idTipoGasto")));
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
            Intent intent = new Intent(Historial.this,Inf_Historial.class);
            startActivity(intent);
        }
    }
}
