package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
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

    public static ArrayList<ItemsDatosClientes> arrayListDatosClientes = new ArrayList<ItemsDatosClientes>();
    ArrayList<String> arrayListCedulas = new ArrayList<String>();

    //Array para guardar todas las facturas
    public static ArrayList<ItemFactura_AgregarCliente> itemsFactura = new ArrayList<ItemFactura_AgregarCliente>();

    //Para decidir si se actualiza o se inserta un cliente
    public static boolean updateCliente = false;

    //Para saber que cliente fue seleccionado y extraer todos los datos
    public static ItemsDatosClientes cliente;

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

                cliente = arrayListDatosClientes.get(posicion);

                //Determinar si el cliente esta activo o inactivo y notificarle al usuario
                String estado = cliente.getEstado();
                String mensaje = "";

                if(estado.equalsIgnoreCase("Activo"))
                {
                    for(int i = 0; i < itemsFactura.size(); i++)
                    {
                        if(cliente.getIdCliente().equalsIgnoreCase(itemsFactura.get(i).getIdCliente()))
                        {
                            if(itemsFactura.get(i).getEstado().equalsIgnoreCase("Activo"))
                            {
                                mensaje = "Este cliente ya tiene una factura activa, no se pueden tener dos facturas al tiempo debe modificar la factura existente.";
                                AlertaEstadoCliente(mensaje, "");
                                buscarCedula.setText("");
                            }
                        }
                    }

                    if(mensaje.equalsIgnoreCase(""))
                    {
                        updateCliente = true;

                        nom.setText(cliente.getNombreCliente());
                        direccion.setText(cliente.getDireccion());
                        telefono.setText(cliente.getTelefono());
                        correo.setText(cliente.getCorreo());
                        nomEmpresa.setText(cliente.getNombreEmpresa());
                        dircEmpresa.setText(cliente.getDireccionEmpresa());
                    }
                }
                else
                {
                    if(estado.equalsIgnoreCase("Inactivo"))
                    {
                        mensaje = "Este cliente se encuentra Inactivo, ¿Desea activarlo?";
                        AlertaEstadoCliente(mensaje, "Inactivo");
                        updateCliente = true;
                    }
                }
            }
        });
        return view;
    }

    public void AlertaEstadoCliente(String mensaje, final String clic)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.perfil);
        builder.setTitle("Estado del Cliente");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (clic)
                {
                    case "Inactivo":

                        nom.setText(cliente.getNombreCliente());
                        direccion.setText(cliente.getDireccion());
                        telefono.setText(cliente.getTelefono());
                        correo.setText(cliente.getCorreo());
                        nomEmpresa.setText(cliente.getNombreEmpresa());
                        dircEmpresa.setText(cliente.getDireccionEmpresa());

                        break;
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
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

            TareaGetBill tareaGetBill = new TareaGetBill();
            tareaGetBill.execute();
        }
    }

    //Clases Asyntask para traer las facturas
    private class TareaGetBill extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerFactura.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllBill"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFacturas = objItems.getJSONArray(0);

                //String obj
                String respuesta= String.valueOf(objFacturas);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }

                else
                {
                    itemsFactura.clear();
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);
                        itemsFactura.add(new ItemFactura_AgregarCliente(obj.getString("idFactura"), obj.getString("fecha"), obj.getString("total"), obj.getString("valorRestante"), obj.getString("estado"), obj.getString("fechaCobro"), obj.getString("diaCobro"), obj.getString("horaCobro"), obj.getString("idVendedor"), obj.getString("idCliente")));
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
            } catch (JSONException e) {
                e.printStackTrace();
                existe = false;
            }

            return existe;
        }

        protected void onPostExecute(Boolean result)
        {

        }
    }
}
