package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.empleado.Empleados;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

                for (int i = 0; i < M_DatosCobro.arrayList.size(); i++) {
                    if (posicionItems.getNombre().equalsIgnoreCase(M_DatosCobro.arrayListP.get(i).getNombre())) {
                        precioVenta = Integer.valueOf(M_DatosCobro.arrayListP.get(i).getPrecioVenta());
                    }
                }

                int precioActual = 0;

                //Solo se multiplica cuando la cantidad del producto es mayor a 1 de lo contrario solo se resta

                int total = Integer.valueOf(M_DatosCobro.totalPagar.getText().toString());
                if (Integer.valueOf(posicionItems.getCantidad()) > 1) {
                    precioActual = Integer.valueOf(posicionItems.getCantidad()) * precioVenta;

                    total = total - precioActual;
                    M_DatosCobro.totalPagar.setText(String.valueOf(total));

                    limpiarLista();
                } else {
                    total = total - precioVenta;
                    M_DatosCobro.totalPagar.setText(String.valueOf(total));
                    limpiarLista();
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

        //Fecha Personalizada para la garantia
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        editFechaGarantia = (EditText) dialoglayout.findViewById(R.id.editFecha_Garantia_MDatosCobro);
        editFechaGarantia.setInputType(InputType.TYPE_NULL);
        editFechaGarantia.requestFocus();
        setDateTimeField();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.garantia);
        builder.setTitle("Garantia");
        builder.setView(dialoglayout);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //Capturar variables de la alerta
                String capDescripcion = editDescripcionGarantia.getText().toString();
                String capFecha = editFechaGarantia.getText().toString();
                if (capDescripcion.equals("") || capFecha.equals("")) {
                    Toast.makeText(getContext(), "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Borrar un item de la lista
                    ArrayAdapter adapter = new AdapterLista_Productos_MDatosCobro(getContext(), M_DatosCobro.arrayList);
                    adapter.remove(posicionItems);
                    //Se carga de nuevo la vista
                    M_DatosCobro.lista.setAdapter(adapter);



                    Toast.makeText(getContext(), "Producto Registrado por Garantia", Toast.LENGTH_SHORT).show();
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

    //Clases Asyntask para eliminar una venta

    /*private class TareaDelete extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerVenta.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idVenta", params[0]));
            nameValuePairs.add(new BasicNameValuePair("option", "deleteSale"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);

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
            int precioVenta = 0;

            for (int i = 0; i <M_DatosCobro.arrayList.size(); i++)
            {
                if (posicionItems.getNombre().equalsIgnoreCase(M_DatosCobro.arrayListP.get(i).getNombre()))
                {
                    precioVenta = Integer.valueOf(M_DatosCobro.arrayListP.get(i).getPrecioVenta());
                }
            }

            int precioActual = 0;

            //Solo se multiplica cuando la cantidad del producto es mayor a 1 de lo contrario solo se resta

            int total = Integer.valueOf(M_DatosCobro.totalPagar.getText().toString());
            if (Integer.valueOf(posicionItems.getCantidad()) > 1)
            {
                precioActual = Integer.valueOf(posicionItems.getCantidad()) * precioVenta;

                total =  total - precioActual;
                M_DatosCobro.totalPagar.setText(String.valueOf(total));
            }
            else
            {
                total = total - precioVenta;
                M_DatosCobro.totalPagar.setText(String.valueOf(total));
            }

            limpiarLista();
        }
    }*/
}
