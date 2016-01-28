package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.empleado.Empleados;
import com.example.billy.garantias_product.Items_Garantia;
import com.example.billy.inversiones.R;
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

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class AdapterLista_Productos_MDatosCobro extends ArrayAdapter implements View.OnClickListener
{

    public static ItemListaroductos_MDatosCobro posicionItems;
    //Variables fecha personalizada
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private DatePickerDialog datePickerDialogDevolucion;
    private SimpleDateFormat dateFormatterDevolucion;

    EditText editFechaGarantia;
    EditText editFechaDevolucion;

    boolean resul;
    Object respuesta = "";

    String idProducto = "";
    String idVendedor = "";
    String capDescripcion = "";
    String capFecha = "";
    String cantidadP = "";
    int posicionListaGarantia = 0;

    String respuestaGarantia = "";
    boolean existe = false;

    int sumaCantidad = 0;


    public AdapterLista_Productos_MDatosCobro(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemslista_productos_mdatoscobro,null);
        }


        ItemListaroductos_MDatosCobro items = (ItemListaroductos_MDatosCobro)getItem(position);

        TextView nombre = (TextView) convertView.findViewById(R.id.textViewNombreProducto_MDatosCobro);
        TextView cantidadAdicional = (TextView) convertView.findViewById(R.id.textViewCantidadAdicional_MDatosCobro);
        final ImageView garantia = (ImageView)convertView.findViewById(R.id.imageViewGarantiaProducto_MDatosCobro);
        ImageView devolucion = (ImageView)convertView.findViewById(R.id.imageViewDevolucionProducto_MDatosCobro);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_MDatosCobro);

        nombre.setText(items.getNombre());
        //cantidadAdicional.setText(items.getCantidadAdicional());
        garantia.setImageResource(items.getGarantia());
        devolucion.setImageResource(items.getDevolucion());
        eliminar.setImageResource(items.getEliminar());

        if(items.getEstado().equalsIgnoreCase("insert"))
        {
            garantia.setVisibility(View.GONE);
            devolucion.setVisibility(View.GONE);
        }
        else
        {
            if(items.getCantidadAdicional().equalsIgnoreCase("0"))
            {
                eliminar.setVisibility(View.GONE);
            }
            else
            {
                eliminar.setVisibility(View.GONE);
                cantidadAdicional.setVisibility(View.VISIBLE);

                int resta = Integer.valueOf(items.getCantidadAdicional()) - Integer.valueOf(items.getCantidad());

                cantidadAdicional.setText("+" + String.valueOf(resta));
            }
        }

        garantia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionItems = (ItemListaroductos_MDatosCobro) getItem(position);
                GarantiaProducto();
            }
        });

        devolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionItems = (ItemListaroductos_MDatosCobro) getItem(position);
                DevolucionProducto();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionItems = (ItemListaroductos_MDatosCobro) getItem(position);
                EliminarProducto();
            }
        });



        return convertView;
    }

    public void EliminarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int precioVenta = 0;

                for (int i = 0; i < M_DatosCobro.arrayListP.size(); i++) {
                    if (posicionItems.getNombre().equalsIgnoreCase(M_DatosCobro.arrayListP.get(i).getNombre())) {
                        precioVenta = Integer.valueOf(M_DatosCobro.arrayListP.get(i).getPrecioVenta());
                    }
                }

                int precioActual = 0;

                //Solo se multiplica cuando la cantidad del producto es mayor a 1 de lo contrario solo se resta

                int total = Integer.valueOf(M_DatosCobro.totalPagar.getText().toString());
                if (Integer.valueOf(posicionItems.getCantidad()) > 1)
                {
                    precioActual = Integer.valueOf(posicionItems.getCantidad()) * precioVenta;

                    total = total - precioActual;
                    M_DatosCobro.totalPagar.setText(String.valueOf(total));

                    limpiarLista();

                    //Nuevo valor restante
                    int diferencia = 0;
                    diferencia = Integer.valueOf(M_DatosCobro.saldoRestante.getText().toString()) - precioActual;

                    M_DatosCobro.saldoRestante.setText(String.valueOf(diferencia));
                }
                else
                {
                    total = total - precioVenta;
                    M_DatosCobro.totalPagar.setText(String.valueOf(total));
                    limpiarLista();

                    //Nuevo valor restante
                    int diferencia = 0;
                    diferencia = Integer.valueOf(M_DatosCobro.saldoRestante.getText().toString()) - precioVenta;

                    M_DatosCobro.saldoRestante.setText(String.valueOf(diferencia));
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    public void limpiarLista()
    {
        //Borrar un item de la lista
        ArrayAdapter adapter = new AdapterLista_Productos_MDatosCobro(getContext(), M_DatosCobro.arrayList);
        adapter.remove(posicionItems);
        //Se carga de nuevo la vista
        M_DatosCobro.lista.setAdapter(adapter);
    }

    //Alerta Personalizada Para la garantia del producto
    public void GarantiaProducto()
    {
        LayoutInflater inflaterAlert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflaterAlert.inflate(R.layout.alert_garantia_mdatoscobro, null);

        final EditText editDescripcionGarantia = (EditText) dialoglayout.findViewById(R.id.editDescripcion_Garantia_MDatosCobro);
        final TextView txtTextoDescripcion = (TextView) dialoglayout.findViewById(R.id.txtTiempo_Grarantia_MDatosCobro);
        final Spinner spinCantidadGarantia = (Spinner) dialoglayout.findViewById(R.id.spinCantidad_Garantia_MDatosCobro);

        final View layoutGarantia = (View) dialoglayout.findViewById(R.id.layoutGarantia);

        //Fecha Personalizada para la garantia
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        editFechaGarantia = (EditText) dialoglayout.findViewById(R.id.editFecha_Garantia_MDatosCobro);
        editFechaGarantia.setInputType(InputType.TYPE_NULL);
        editFechaGarantia.requestFocus();
        setDateTimeField();

        //Llenar el spinner segun la cantidad que haya registrada

        String idVenta = posicionItems.getIdVenta();
        String cantidadProductos = "";
        String idP = "";

        //variables para definir las probabilidades del usuario
        boolean accionAceptar = false;
        boolean verificarGarantia = false;

        for(int j = 0; j < Constantes.itemsVenta.size(); j++)
        {
            if(idVenta.equalsIgnoreCase(Constantes.itemsVenta.get(j).getIdVenta()))
            {
                if(Constantes.itemsVenta.get(j).getNuevaCantidad().equalsIgnoreCase("0"))
                {
                    cantidadProductos = Constantes.itemsVenta.get(j).getCantidad();
                    idP = Constantes.itemsVenta.get(j).getIdProducto();
                }
                else
                {
                    cantidadProductos = Constantes.itemsVenta.get(j).getNuevaCantidad();
                    idP = Constantes.itemsVenta.get(j).getIdProducto();
                }
            }
        }

        //Para saber si todas las garantias ya estan registradas
        for(int l = 0; l < Constantes.itemsGarantias.size(); l++)
        {
            if(idP.equalsIgnoreCase(Constantes.itemsGarantias.get(l).getIdProducto()) && Constantes.idClienteCliente.equalsIgnoreCase(Constantes.itemsGarantias.get(l).getIdCliente()))
            {
                if(cantidadProductos.equalsIgnoreCase(Constantes.itemsGarantias.get(l).getCantidad()))
                {
                    editDescripcionGarantia.setVisibility(View.GONE);
                    editFechaGarantia.setVisibility(View.GONE);
                    layoutGarantia.setVisibility(View.GONE);

                    txtTextoDescripcion.setText("Los productos ya se encuentran registrados por garantia");

                    accionAceptar = true;
                }
                else
                {

                    //Para sacar la posicion en la que se encuentra la garantia que fue selecionada.
                    posicionListaGarantia = l;

                    editDescripcionGarantia.setText(Constantes.itemsGarantias.get(l).getDescripcion());
                    editFechaGarantia.setText(Constantes.itemsGarantias.get(l).getFecha());

                    //resta para la cantidad de los productos que estan registrados por garanatia
                    int canP = Integer.valueOf(cantidadProductos) - Integer.valueOf(Constantes.itemsGarantias.get(l).getCantidad());
                    cantidadProductos = String.valueOf(canP);

                    verificarGarantia = true;
                }
            }
        }

        ArrayList<String> arrayListSpin = new ArrayList<String>();
        arrayListSpin.clear();

        for(int m = 1; m <= Integer.valueOf(cantidadProductos); m++)
        {
            arrayListSpin.add(String.valueOf(m));
        }

        spinCantidadGarantia.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

        //Alerta personalizada
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.garantia);
        builder.setTitle("Garantia");
        builder.setView(dialoglayout);
        final boolean finalAccionAceptar = accionAceptar;
        final boolean finalVerificarGarantia = verificarGarantia;
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (finalAccionAceptar)
                {
                    //Sin Accion
                }
                else
                {
                    if (finalVerificarGarantia)
                    {
                        //Capturar variables de la alerta
                        capDescripcion = editDescripcionGarantia.getText().toString();
                        capFecha = editFechaGarantia.getText().toString();
                        cantidadP = spinCantidadGarantia.getSelectedItem().toString();

                        sumaCantidad = Integer.valueOf(Constantes.itemsGarantias.get(posicionListaGarantia).getCantidad()) + Integer.valueOf(cantidadP);
                        cantidadP = String.valueOf(sumaCantidad);
                        //Enviar fecha con hora

                        if(!capFecha.equalsIgnoreCase(Constantes.itemsGarantias.get(posicionListaGarantia).getFecha()))
                        {
                            Date date = new Date();
                            DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                            capFecha = capFecha + " " + hourFormat.format(date);
                        }

                        TareaUpdadteGarantia updadteGarantia = new TareaUpdadteGarantia();
                        updadteGarantia.execute(Constantes.itemsGarantias.get(posicionListaGarantia).getIdGarantia(),"En espera", capDescripcion, capFecha,cantidadP, Constantes.itemsGarantias.get(posicionListaGarantia).getIdVendedor(),Constantes.itemsGarantias.get(posicionListaGarantia).getIdCliente(), Constantes.itemsGarantias.get(posicionListaGarantia).getIdProducto());
                    }
                    else
                    {
                        //Capturar variables de la alerta
                        capDescripcion = editDescripcionGarantia.getText().toString();
                        capFecha = editFechaGarantia.getText().toString();
                        if (capDescripcion.equals("") || capFecha.equals(""))
                        {
                            Toast.makeText(getContext(), "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            for (int j = 0; j < M_DatosCobro.arrayListP.size(); j++)
                            {
                                if (posicionItems.getNombre().equalsIgnoreCase(M_DatosCobro.arrayListP.get(j).getNombre()))
                                {
                                    idProducto = M_DatosCobro.arrayListP.get(j).getIdProducto();
                                }
                            }

                            if(M_DetalleCobro.idVendedor.equalsIgnoreCase(""))
                            {
                                idVendedor = Constantes.idVendedorFactura;
                            }
                            else
                            {
                                idVendedor = M_DetalleCobro.idVendedor;
                            }

                            //Enviar fecha con hora
                            Date date = new Date();
                            DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                            capFecha = capFecha + " " + hourFormat.format(date);

                            cantidadP = spinCantidadGarantia.getSelectedItem().toString();

                            TareaCreateWarranty tareaCreateWarranty = new TareaCreateWarranty();
                            tareaCreateWarranty.execute("En espera", capDescripcion, capFecha, cantidadP, idVendedor, Constantes.idClienteCliente, idProducto);
                        }
                    }
                }

            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    public void DevolucionProducto()
    {
        LayoutInflater inflaterAlert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflaterAlert.inflate(R.layout.alert_devolucion_mdatoscobro, null);

        final EditText editDescripcionDevolucion = (EditText) dialoglayout.findViewById(R.id.editDescripcion_Devolucion_MDatosCobro);

        //Fecha Personalizada para la garantia
        dateFormatterDevolucion = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        editFechaDevolucion = (EditText) dialoglayout.findViewById(R.id.editFecha_Devolucion_MDatosCobro);
        editFechaDevolucion.setInputType(InputType.TYPE_NULL);
        editFechaDevolucion.requestFocus();
        setDateTimeFieldDevolucion();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.devolucion);
        builder.setTitle("Devolcion");
        builder.setView(dialoglayout);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //Capturar variables de la alerta
                String capDescripcion = editDescripcionDevolucion.getText().toString();
                String capFecha = editFechaDevolucion.getText().toString();
                if (capDescripcion.equals("")||
                        capFecha.equals(""))
                {
                    Toast.makeText(getContext(), "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Borrar un item de la lista
                    ArrayAdapter adapter = new AdapterLista_Productos_MDatosCobro(getContext(), M_DatosCobro.arrayList);
                    adapter.remove(posicionItems);
                    //Se carga de nuevo la vista
                    M_DatosCobro.lista.setAdapter(adapter);
                    Toast.makeText(getContext(), "Producto Registrado por Devolucion", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    private void setDateTimeField()
    {
        editFechaGarantia.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editFechaGarantia.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

     private void setDateTimeFieldDevolucion()
    {
        editFechaDevolucion.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogDevolucion = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editFechaDevolucion.setText(dateFormatterDevolucion.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view)
    {
        if(view == editFechaGarantia)
        {
            datePickerDialog.show();
        }

        if(view == editFechaDevolucion)
        {
            datePickerDialogDevolucion.show();
        }
    }

    //Clases Asyntask para agregar una factura
    private class TareaCreateWarranty extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerGarantia.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("estado", params[0]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[1]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[2]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[3]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[4]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idProducto",  params[6]));
            nameValuePairs.add(new BasicNameValuePair("option",  "createWarranty"));

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
            if (resul)
            {
                //Obtener las garantias si las hay
                Constantes.itemsGarantias.clear();
                TareaObtenerGarantias tareaObtenerGarantias = new TareaObtenerGarantias();
                tareaObtenerGarantias.execute();

                Toast.makeText(getContext(), "Producto Registrado por Garantia", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(), "Error de servidor \n" + respuesta, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Clases Asyntask para actualizar una garantia
    private class TareaUpdadteGarantia extends AsyncTask<String,Integer,Boolean>
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
                respuestaGarantia = String.valueOf(objItems);

                if(respuestaGarantia.equalsIgnoreCase("No Existe"))
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
            //Toast.makeText(Perfil.this, respStr, Toast.LENGTH_SHORT).show();

            if(existe)
            {
                //Obtener las garantias si las hay
                Constantes.itemsGarantias.clear();
                TareaObtenerGarantias tareaObtenerGarantias = new TareaObtenerGarantias();
                tareaObtenerGarantias.execute();

                Toast.makeText(getContext(), "Los Cambios Fueron Exitosos (update)", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Error al Modificar Usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Clases Asyntask para traer los datos de la tabla garantias
    private class TareaObtenerGarantias extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("option", "getAllWarranty"));

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

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);

                    if(obj.getString("estado").equalsIgnoreCase("En espera") || obj.getString("estado").equalsIgnoreCase("Pendiente"))
                    {
                        Constantes.itemsGarantias.add(new Items_Garantia(obj.getString("idGarantia"), obj.getString("estado"), obj.getString("descripcion"), obj.getString("fecha"), obj.getString("cantidad"), obj.getString("idVendedor"), obj.getString("idCliente"), obj.getString("idProducto")));
                    }

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
            //Toast.makeText(getActivity(), respStr, Toast.LENGTH_SHORT).show();
        }
    }
}
