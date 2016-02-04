package com.example.billy.saldo_caja;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.gastos.Historial;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaldoCaja extends AppCompatActivity
{
    TextView gastoDia;
    TextView cobroDia;
    TextView diferencia;

    ImageView imgDiferencia;

    ArrayList<Items> arrayListGastos = new ArrayList<Items>();
    ArrayList<Items> arrayListCobros = new ArrayList<Items>();
    boolean existe = false;

    //Alerta Cargando
    ProgressDialog progressDialog;

    //Para guardar la fecha actual
    String fechaActual = "";

    //Total de Gastos
    int gastos = 0;
    int cobros = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_caja);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        gastoDia = (TextView)findViewById(R.id.texViewGastoDia_SaldoCaja);
        cobroDia = (TextView)findViewById(R.id.texViewCobroDia_SaldoCaja);
        diferencia = (TextView)findViewById(R.id.texViewDiferencia_SaldoCaja);

        imgDiferencia = (ImageView)findViewById(R.id.imgDiferencia_SaldoCaja);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaActual = sdf.format(new Date());

        //Cargar los gastos que se han guardado en el servidor
        TareaGastos tareaGastos = new TareaGastos();
        tareaGastos.execute();

        AlertaCargando();
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

    public String  filtrarGastos()
    {
        gastos = 0;

        for(int i = 0; i < arrayListGastos.size(); i++)
        {
            String fechaHora = arrayListGastos.get(i).getFecha();
            String[] separar = fechaHora.split(" ");
            String fecha = separar[0];

            if(fechaActual.equalsIgnoreCase(fecha))
            {
                gastos = gastos + Integer.valueOf(arrayListGastos.get(i).getValor());
            }
        }

        return String.valueOf(gastos);
    }

    public String  filtrarCobros()
    {
        cobros = 0;

        for(int i = 0; i < arrayListCobros.size(); i++)
        {
            String fechaHora = arrayListCobros.get(i).getFecha();
            String[] separar = fechaHora.split(" ");
            String fecha = separar[0];

            if(fechaActual.equalsIgnoreCase(fecha))
            {
                cobros = cobros + Integer.valueOf(arrayListCobros.get(i).getValor());
            }
        }

        return String.valueOf(cobros);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saldo_caja, menu);
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
            case R.id.historial_SaldoCaja:
                Constantes.atrasHistorial="SaldoCaja";

                Intent intent = new Intent(SaldoCaja.this,Historial.class);
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

    private class TareaGastos extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;

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
            nameValuePairs.add(new BasicNameValuePair("option", "getAllSpending"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFechas = objItems.getJSONArray(0);

                arrayListGastos.clear();

                for(int i=0; i<objFechas.length(); i++)
                {
                    JSONObject obj = objFechas.getJSONObject(i);
                    arrayListGastos.add(new Items(obj.getString("idGasto"), obj.getString("valor"), obj.getString("fecha"),  obj.getString("descripcion"), obj.getString("idUsuario"), obj.getString("idTipoGasto")));
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
            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }


        protected void onPostExecute(Boolean result)
        {
            if(existe)
            {
                TareaCobros tareaCobros = new TareaCobros();
                tareaCobros.execute();
            }
            else
            {
                Toast.makeText(SaldoCaja.this, "Error al cargar los gastos", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }
    }

    private class TareaCobros extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCobro.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllCharge"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFechas = objItems.getJSONArray(0);

                arrayListCobros.clear();

                for(int i=0; i<objFechas.length(); i++)
                {
                    JSONObject obj = objFechas.getJSONObject(i);
                    arrayListCobros.add(new Items(obj.getString("abono"), obj.getString("fecha")));
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
            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }


        protected void onPostExecute(Boolean result)
        {
            if(existe)
            {
                progressDialog.dismiss();

                gastoDia.setText("Gastos Por Dia: $ " + filtrarGastos());
                cobroDia.setText("Cobros Por Dia: $ " + filtrarCobros());

                //Calcular la diferencia
                int resta = 0;
                resta = cobros - gastos;

                diferencia.setText("Diferencia: $ " + String.valueOf(resta));

                if(resta < 0)
                {
                    imgDiferencia.setImageDrawable(getResources().getDrawable(R.drawable.dislike));
                }
                else
                {
                    imgDiferencia.setImageDrawable(getResources().getDrawable(R.drawable.like));
                }
            }
            else
            {
                Toast.makeText(SaldoCaja.this, "Error al cargar los cobros", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }
    }
}
