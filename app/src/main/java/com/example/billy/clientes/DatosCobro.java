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

import com.example.billy.inversiones.R;
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

public class DatosCobro extends Fragment implements View.OnClickListener
{
    public static ListView lista;

    public static ArrayList<ItemsListaProductos_Productos> arrayList = new ArrayList<ItemsListaProductos_Productos>();
    public static ArrayList<ItemListaProdutos_DatosCobro> arrayListItems = new ArrayList<ItemListaProdutos_DatosCobro>();
    public static ArrayList<String> arrayListNombresProductos = new ArrayList<String>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public static AutoCompleteTextView buscarProducto;
    public static EditText fechaVenta;
    public static EditText totalPagar;
    public static EditText abono;
    public static EditText valorRestante;

    public static int total =0;

    ImageView imgEditar;

    String habilitar = "Habilitar";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.fragment_datos_cobro, container, false);

        buscarProducto=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarProducto_DatosCobro);
        totalPagar=(EditText)view.findViewById(R.id.editTotalPagar_DatosCobro);
        abono=(EditText)view.findViewById(R.id.editTextAbono_DatosCobro);
        valorRestante=(EditText)view.findViewById(R.id.editTextValorRestante_DatosCobro);
        imgEditar = (ImageView) view.findViewById(R.id.imgEditar_DatosCobro);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaVenta=(EditText)view.findViewById(R.id.editFechaVenta_DatosCobro);
        fechaVenta.setInputType(InputType.TYPE_NULL);
        fechaVenta.requestFocus();

        lista=(ListView)view.findViewById(R.id.listViewListaProductos_DatosCobro);

        ActualizarLista();
        setDateTimeField();

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

        arrayListItems.clear();

        buscarProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = buscarProducto.getText().toString();

                int posicion = 0;

                for (int i = 0; i < arrayList.size(); i++) {
                    if (nombre.equalsIgnoreCase(arrayList.get(i).getNombre())) {
                        posicion = i;
                        break;
                    }
                }

                ItemsListaProductos_Productos producto = arrayList.get(posicion);

                buscarProducto.setText("");
                arrayListItems.add(new ItemListaProdutos_DatosCobro(producto.getNombre(), "1", R.mipmap.informacion, R.mipmap.eliminar));
                lista.setAdapter(new AdapterLista_Productos_DatosCobro(getActivity(), arrayListItems));

                //Filtrar Los Precios De los Producto Para Multiplicarlos Por La Cantidad

                for (int j = 0; j < arrayList.size(); j++) {
                    if (nombre.equalsIgnoreCase(arrayList.get(j).getNombre())) {
                        int precio = Integer.valueOf(arrayList.get(j).getPrecioVenta());
                        int cantidad = 0;

                        for (int i = 0; i < arrayListItems.size(); i++) {
                            if (nombre.equalsIgnoreCase(arrayListItems.get(i).getNomProducto())) {
                                cantidad = Integer.valueOf(arrayListItems.get(i).getCantidad());
                            }
                        }

                        int resultado = precio * cantidad;
                        total = Integer.valueOf(totalPagar.getText().toString());
                        total = total + resultado;
                        totalPagar.setText(String.valueOf(total));
                    }
                }
            }
        });



        imgEditar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (habilitar)
                {
                    case "Habilitar":

                        totalPagar.setEnabled(true);
                        habilitar = "Desabilitar";

                        break;

                    case "Desabilitar":

                        totalPagar.setEnabled(false
                        );
                        habilitar = "Habilitar";

                        break;
                }


            }
        });

        return view;
    }

    private void setDateTimeField()
    {
        fechaVenta.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaVenta.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view)
    {
        if(view == fechaVenta) {
            datePickerDialog.show();
        }
    }

    public void ActualizarLista()
    {

        arrayList.clear();
        arrayListNombresProductos.clear();
        TareaProductos productos = new TareaProductos();
        productos.execute();
    }

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

                for(int i=0; i<objVendedores.length(); i++)
                {
                    JSONObject obj = objVendedores.getJSONObject(i);
                    arrayList.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("tiempoGarantia"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));
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
