package com.example.billy.garantias_product;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;
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

public class VisualizarGarantia extends AppCompatActivity
{
    String idGarantia = "";
    String tipo = "";

    EditText editTextNombreCliente_VGarantia;
    EditText editTextTelefonoCliente_VGarantia;
    EditText editTextNombreProducto_VGarantia;
    EditText editTextCantidadProducto_VGarantia;
    EditText editTextFecha_VGarantia;
    EditText editTextDescripcion_VGarantia;

    Spinner spinEstado_VGarantia;

    String estado = "";
    String descripcion = "";
    String fecha = "";
    String cantidad = "";
    String idVendedor = "";
    String idCliente = "";
    String idProducto = "";

    String respuesta = "";
    boolean existe = false;

    //Alerta Cargando
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_garantia);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        editTextNombreCliente_VGarantia = (EditText) findViewById(R.id.editTextNombreCliente_VGarantia);
        editTextTelefonoCliente_VGarantia = (EditText) findViewById(R.id.editTextTelefonoCliente_VGarantia);
        editTextNombreProducto_VGarantia = (EditText) findViewById(R.id.editTextNombreProducto_VGarantia);
        editTextCantidadProducto_VGarantia = (EditText) findViewById(R.id.editTextCantidadProducto_VGarantia);
        editTextFecha_VGarantia = (EditText) findViewById(R.id.editTextFecha_VGarantia);
        editTextDescripcion_VGarantia = (EditText) findViewById(R.id.editTextDescripcion_VGarantia);

        spinEstado_VGarantia = (Spinner) findViewById(R.id.spinEstado_VGarantia);

        Bundle extra = getIntent().getExtras();
        idGarantia = extra.getString("idGarantia");
        estado = extra.getString("estado");
        descripcion = extra.getString("descripcion");
        fecha = extra.getString("fecha");
        cantidad = extra.getString("cantidad");
        idVendedor = extra.getString("idVendedor");
        idCliente = extra.getString("idCliente");
        idProducto = extra.getString("idProducto");
        tipo = extra.getString("tipo");

        switch (tipo)
        {
            case "Vendedor":

                for(int i = 0; i < Garantia.arrayListFiltroVendedor.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Garantia.arrayListFiltroVendedor.get(i).getIdGarantia()))
                    {
                        editTextNombreCliente_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getNombre());
                        editTextTelefonoCliente_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getTelefono());
                        editTextNombreProducto_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getNombreProducto());
                        editTextCantidadProducto_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getCantidad());
                        editTextFecha_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getFecha());
                        editTextDescripcion_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getDescripcion());
                    }
                }

                break;

            case "Todos":

                for(int i = 0; i < Garantia.arrayList.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Garantia.arrayList.get(i).getIdGarantia()))
                    {
                        editTextNombreCliente_VGarantia.setText(Garantia.arrayList.get(i).getNombre());
                        editTextTelefonoCliente_VGarantia.setText(Garantia.arrayList.get(i).getTelefono());
                        editTextNombreProducto_VGarantia.setText(Garantia.arrayList.get(i).getNombreProducto());
                        editTextCantidadProducto_VGarantia.setText(Garantia.arrayList.get(i).getCantidad());
                        editTextFecha_VGarantia.setText(Garantia.arrayList.get(i).getFecha());
                        editTextDescripcion_VGarantia.setText(Garantia.arrayList.get(i).getDescripcion());
                    }
                }

                break;
        }

        //Para decidir que va a contener el spinner del estado

        String [] arrayEstado = getResources().getStringArray(R.array.estado_garantia);

        switch (estado)
        {
            case "En espera":

                spinEstado_VGarantia.setAdapter(new ArrayAdapter<String>(VisualizarGarantia.this, android.R.layout.simple_spinner_dropdown_item, arrayEstado));

                break;

            case "Pendiente":

                ArrayList<String> arrayEstado2 =  new ArrayList<String>();
                arrayEstado2.clear();
                arrayEstado2.add(arrayEstado[1]);
                arrayEstado2.add(arrayEstado[2]);

                spinEstado_VGarantia.setAdapter(new ArrayAdapter<String>(VisualizarGarantia.this, android.R.layout.simple_spinner_dropdown_item, arrayEstado2));

                break;
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualizar_garantia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.guardarGarantia_VisualizarGarantia:

                String estadoSpin = spinEstado_VGarantia.getSelectedItem().toString();

                TareaUpdateGarantia tareaUpdateGarantia = new TareaUpdateGarantia();
                tareaUpdateGarantia.execute(idGarantia, estadoSpin, descripcion, fecha, cantidad,
                        idVendedor, idCliente, idProducto);

                AlertaCargando();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TareaUpdateGarantia extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerGarantia.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idGarantia", params[0]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[1]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[3]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[4]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[6]));
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[7]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateWarranty"));

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
                Toast.makeText(VisualizarGarantia.this, "La Garantia se modifico correctamente", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

                Intent intent = new Intent(VisualizarGarantia.this, Garantia.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(VisualizarGarantia.this, "Error al Modificar la Garantia", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }
}
