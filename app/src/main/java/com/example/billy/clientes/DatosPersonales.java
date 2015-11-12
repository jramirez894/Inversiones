package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.billy.empleado.ItemListaEmpleado;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.AdapterListaPersonalizada;
import com.example.billy.menu_principal.ItemListaPersonalizada;

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

public class DatosPersonales extends Fragment
{
    public static AutoCompleteTextView buscarCedula;
    public static EditText nom;
    public static EditText direccion;
    public static EditText telefono;
    public static EditText correo;

    public static EditText nomEmpresa;
    public static EditText dircEmpresa;

    boolean existe = false;
    String respuesta = "";

    ArrayList<ItemsDatosClientes> arrayListDatosClientes = new ArrayList<ItemsDatosClientes>();
    ArrayList<String> arrayListCedulas = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;

        view = inflater.inflate(R.layout.fragment_datos_personales, container, false);

        //Mostrar Alerta Si existe o el cliente inactivo o activo
        buscarCedula = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteCedulaCliente_AgregarCliente);
        nom=(EditText)view.findViewById(R.id.editNombre_AgregarCliente);
        direccion=(EditText)view.findViewById(R.id.editDireccion_AgregarCliente);
        telefono=(EditText)view.findViewById(R.id.editTelefono_AgregarCliente);
        correo=(EditText)view.findViewById(R.id.editCorreo_AgregarCliente);
        nomEmpresa=(EditText)view.findViewById(R.id.editNombreEmpresa_AgregarCliente);
        dircEmpresa=(EditText)view.findViewById(R.id.editDireccionEmpresa_AgregarCliente);

        TareaListado tareaListado= new TareaListado();
        tareaListado.execute();

        buscarCedula.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String cedula = buscarCedula.getText().toString();
                int posicion = 0;

                for(int i = 0; i < arrayListDatosClientes.size(); i++)
                {
                    if(cedula.equalsIgnoreCase(arrayListDatosClientes.get(i).getCedula()))
                    {
                        posicion = i;
                        break;
                    }
                }

                ItemsDatosClientes cliente = arrayListDatosClientes.get(posicion);

                nom.setText(cliente.getNombreCliente());
                direccion.setText(cliente.getDireccion());
                telefono.setText(cliente.getTelefono());
                correo.setText(cliente.getCorreo());
                nomEmpresa.setText(cliente.getNombreEmpresa());
                dircEmpresa.setText(cliente.getDireccionEmpresa());
            }
        });
        return view;
    }

    //Clases Asyntask para traer los clientes

    private class TareaListado extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCliente.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);

                //String obj
                respuesta= String.valueOf(objVendedores);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    arrayListDatosClientes.clear();
                    arrayListCedulas.clear();

                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        arrayListCedulas.add(obj.getString("cedula"));
                        arrayListDatosClientes.add(new ItemsDatosClientes(obj.getString("idCliente"),obj.getString("cedula"),obj.getString("nombre"),obj.getString("direccion"),obj.getString("telefono"),obj.getString("correo"),obj.getString("nombreEmpresa"),obj.getString("direccionEmpresa"),obj.getString("estado"),obj.getString("calificacion")));
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
            //Toast.makeText(PrincipalMenu.this, respuesta, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayListCedulas);
            buscarCedula.setAdapter(arrayAdapter);
        }
    }

}
