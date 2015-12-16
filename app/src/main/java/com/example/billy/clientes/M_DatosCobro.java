package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.billy.constantes.Constantes;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.AdapterListaPersonalizada;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.productos.AdapterListaProductos_Productos;
import com.example.billy.productos.ItemsListaProductos_Productos;

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


public class M_DatosCobro extends Fragment implements View.OnClickListener
{
    public static ListView lista;
    public static ArrayList<ItemListaroductos_MDatosCobro> arrayList = new ArrayList<ItemListaroductos_MDatosCobro>();
    public static ArrayList<ItemListaroductos_MDatosCobro> arrayListProductosNuevos = new ArrayList<ItemListaroductos_MDatosCobro>();

    private DatePickerDialog datePickerDialogPendiente;
    private SimpleDateFormat dateFormatterPendiente;

    public static AutoCompleteTextView buscarProducto;
    public static EditText valorProducto;
    public static EditText fechaVenta;
    public static EditText totalPagar;
    public static EditText abono;
    public static ImageView pendiente;
    public static EditText fechaPendiente;
    public static EditText valorRestante;

    String habilitar = "Habilitar";

    public static ArrayList<ItemsListaProductos_Productos> arrayListP = new ArrayList<ItemsListaProductos_Productos>();
    public static ArrayList<String> arrayListNombresProductos = new ArrayList<String>();

    //Layout para los productos nuevos
    public static View layoutProductosNuevos;
    public static ListView listaProductosNuevos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_m__datos_cobro, container, false);

        buscarProducto=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarProducto_DatoCobro_Mcliente);
        valorProducto=(EditText)view.findViewById(R.id.editPrecioProducto_DatosCobro_Mcliente);
        totalPagar=(EditText)view.findViewById(R.id.editTotalPagar_DatosCobro_Mcliente);
        abono=(EditText)view.findViewById(R.id.editTextAbono_DatosCobro_Mcliente);
        pendiente=(ImageView)view.findViewById(R.id.buttonPendiente_DatosCobro_Mcliente);

        valorRestante=(EditText)view.findViewById(R.id.editTextValorRestante_DatosCobro_Mcliente);

        //Total a pagar
        totalPagar.setText(Constantes.totalFactura);

        //Fecha Personalizada
        fechaVenta=(EditText)view.findViewById(R.id.editFechaVenta_DatosCobro_Mcliente);
        fechaVenta.setText(Constantes.fechaFactura);

        //Fecha Pendiente
        dateFormatterPendiente = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaPendiente=(EditText)view.findViewById(R.id.editTextFechaPendiente_DatosCobro_Mcliente);
        fechaPendiente.setInputType(InputType.TYPE_NULL);
        fechaPendiente.requestFocus();

        lista=(ListView)view.findViewById(R.id.listViewListaProductos_DatosCobro_Mcliente);
        ActualizarLista();
        setDateTimeFieldPendiente();

        pendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (habilitar) {
                    case "Habilitar":

                        fechaPendiente.setVisibility(View.VISIBLE);
                        habilitar = "Desabilitar";

                        break;

                    case "Desabilitar":

                        fechaPendiente.setVisibility(View.GONE);
                        habilitar = "Habilitar";

                        break;
                }
            }
        });

        //Metodo para restar el abono ingresado al valor total
        abono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //con la variable charSequence se captura y se convierte a entero lo que se esta escribiendo en el EditText de abono
                    int abonoIngresado = Integer.valueOf(charSequence.toString());
                    int totalAPagar = Integer.parseInt(totalPagar.getText().toString());
                    int resta = totalAPagar - abonoIngresado;

                    valorRestante.setText(String.valueOf(resta));
                } catch (Exception e) {
                    valorRestante.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Cargar los productos en el autocomplete
        TareaProductos tareaProductos = new TareaProductos();
        tareaProductos.execute();

        // //Layout para los productos nuevos
        layoutProductosNuevos = (View) view.findViewById(R.id.layoutProductosNuevos);
        listaProductosNuevos=(ListView)view.findViewById(R.id.listViewListaProductosNuevos_DatosCobro_Mcliente);

        arrayListProductosNuevos.clear();

        buscarProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutProductosNuevos.setVisibility(View.VISIBLE);

                String nombre = buscarProducto.getText().toString();

                int posicion = 0;

                for (int i = 0; i < arrayListP.size(); i++) {
                    if (nombre.equalsIgnoreCase(arrayListP.get(i).getNombre())) {
                        posicion = i;
                        break;
                    }
                }

                ItemsListaProductos_Productos producto = arrayListP.get(posicion);

                buscarProducto.setText("");

                arrayListProductosNuevos.add(new ItemListaroductos_MDatosCobro(producto.getNombre(), R.mipmap.garantia, R.mipmap.devolucion, R.mipmap.eliminar));
                listaProductosNuevos.setAdapter(new AdapterLista_ProductosNuevos_MDatosCobro(getActivity(), arrayListProductosNuevos));
            }
        });


        return view;
    }

    private void setDateTimeFieldPendiente()
    {
        fechaPendiente.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogPendiente = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaPendiente.setText(dateFormatterPendiente.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view)
    {
        if(view == fechaPendiente)
        {
            datePickerDialogPendiente.show();
        }
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        for(int i = 0; i < Constantes.itemsVenta.size(); i++)
        {
            arrayList.add(new ItemListaroductos_MDatosCobro(Constantes.itemsProductos.get(i).getNombre(), R.mipmap.garantia,R.mipmap.devolucion,R.mipmap.eliminar));
        }

        lista.setAdapter(new AdapterLista_Productos_MDatosCobro(getActivity(), arrayList));
    }

    //Clases Asyntask para traer los datos de la tabla productos

    private class TareaProductos extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerProducto.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllProduct"));

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

                arrayListP.clear();
                arrayListNombresProductos.clear();

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);
                    arrayListP.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));
                    arrayListNombresProductos.add(obj.getString("nombre"));

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
            //Toast.makeText(Productos.this, respStr, Toast.LENGTH_SHORT).show();
            buscarProducto.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayListNombresProductos));
        }
    }
}
