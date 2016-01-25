package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

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
    public static ArrayList<ItemListaroductos_MDatosCobro> arrayListInsert = new ArrayList<ItemListaroductos_MDatosCobro>();

    private DatePickerDialog datePickerDialogPendiente;
    private SimpleDateFormat dateFormatterPendiente;

    public static AutoCompleteTextView buscarProducto;
    public static EditText fechaVenta;
    public static EditText totalPagar;
    public static EditText abono;
    public static ImageView pendiente;
    public static EditText fechaPendiente;
    public static EditText valorRestante;
    public static EditText saldoRestante;

    String habilitar = "Habilitar";

    public static ArrayList<ItemsListaProductos_Productos> arrayListP = new ArrayList<ItemsListaProductos_Productos>();
    public static ArrayList<String> arrayListNombresProductos = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_m__datos_cobro, container, false);

        buscarProducto=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarProducto_DatoCobro_Mcliente);
        totalPagar=(EditText)view.findViewById(R.id.editTotalPagar_DatosCobro_Mcliente);
        abono=(EditText)view.findViewById(R.id.editTextAbono_DatosCobro_Mcliente);
        pendiente=(ImageView)view.findViewById(R.id.buttonPendiente_DatosCobro_Mcliente);

        valorRestante=(EditText)view.findViewById(R.id.editTextValorRestante_DatosCobro_Mcliente);
        saldoRestante=(EditText)view.findViewById(R.id.editSaldoRestante_DatosCobro_Mcliente);

        //Total a pagar
        totalPagar.setText(Constantes.totalFactura);

        //Saldo Restante
        saldoRestante.setText(Constantes.valorRestante);

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
                        abono.setEnabled(false);
                        habilitar = "Desabilitar";

                        break;

                    case "Desabilitar":

                        fechaPendiente.setVisibility(View.GONE);
                        abono.setEnabled(true);
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
                    int totalAPagar = Integer.parseInt(saldoRestante.getText().toString());
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

        buscarProducto.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nombre = buscarProducto.getText().toString();
                boolean existe = true;

                for(int i = 0; i < arrayList.size(); i++)
                {
                    if(nombre.equalsIgnoreCase(arrayList.get(i).getNombre()))
                    {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                        alerta.setTitle("Alerta");
                        alerta.setIcon(R.mipmap.informacion);
                        alerta.setMessage("El producto ya existe, debe modificar la cantidad del producto.");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buscarProducto.setText("");
                            }
                        });
                        alerta.show();

                        existe = false;
                    }
                }

                if(existe)
                {
                    int posicion = 0;

                    for (int i = 0; i < arrayListP.size(); i++) {
                        if (nombre.equalsIgnoreCase(arrayListP.get(i).getNombre())) {
                            posicion = i;
                            break;
                        }
                    }

                    ItemsListaProductos_Productos producto = arrayListP.get(posicion);

                    buscarProducto.setText("");

                    arrayList.add(new ItemListaroductos_MDatosCobro(producto.getNombre(), R.mipmap.garantia, R.mipmap.devolucion, R.mipmap.eliminar, "insert", "1", producto.getIdProducto(), ""));
                    lista.setAdapter(new AdapterLista_Productos_MDatosCobro(getActivity(), arrayList));

                    //Sumar el precio del nuevo producto al total
                    String precioVenta = "";

                    for(int i = 0; i < arrayListP.size(); i++)
                    {
                        if(nombre.equalsIgnoreCase(arrayListP.get(i).getNombre()))
                        {
                            precioVenta = arrayListP.get(i).getPrecioVenta();
                        }
                    }

                    int total = 1 * Integer.valueOf(precioVenta);
                    int suma = total + Integer.valueOf(totalPagar.getText().toString());

                    //Nuevo valor restante
                    int diferencia = Integer.valueOf(suma) - Integer.valueOf(totalPagar.getText().toString());
                    diferencia = Integer.valueOf(saldoRestante.getText().toString()) + diferencia;

                    saldoRestante.setText(String.valueOf(diferencia));

                    totalPagar.setText(String.valueOf(suma));


                }
            }
        });

        //Click de las listas de productos
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nombreProducto = "";
                String descripcion = "";
                String precioVenta = "";
                String disponibles = "";
                String cantidad = "";
                String idProducto = "";

                nombreProducto = arrayList.get(position).getNombre();

                for(int i = 0; i < arrayListP.size(); i++)
                {
                    if(nombreProducto.equalsIgnoreCase(arrayListP.get(i).getNombre()))
                    {
                        descripcion = arrayListP.get(i).getDescripcion();
                        precioVenta = arrayListP.get(i).getPrecioVenta();
                        disponibles = arrayListP.get(i).getCantidad();
                        idProducto = arrayListP.get(i).getIdProducto();
                    }
                }

                if(arrayList.get(position).getEstado().equalsIgnoreCase("update"))
                {
                    for(int i = 0; i < Constantes.itemsVenta.size(); i++)
                    {
                        if(idProducto.equalsIgnoreCase(Constantes.itemsVenta.get(i).getIdProducto()))
                        {
                            if(Constantes.itemsVenta.get(i).getNuevaCantidad().equalsIgnoreCase("0"))
                            {
                                cantidad = Constantes.itemsVenta.get(i).getCantidad();
                            }
                            else
                            {
                                int suma = (Integer.valueOf(Constantes.itemsVenta.get(i).getNuevaCantidad()) - Integer.valueOf(Constantes.itemsVenta.get(i).getCantidad())) + Integer.valueOf(Constantes.itemsVenta.get(i).getCantidad());
                                cantidad = String.valueOf(suma);
                            }
                        }
                    }
                }
                else
                {
                    cantidad = arrayList.get(position).getCantidad();
                }

                AlertaInfoProducto(nombreProducto, descripcion, precioVenta, cantidad, disponibles, idProducto, position);
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
            arrayList.add(new ItemListaroductos_MDatosCobro(Constantes.itemsProductos.get(i).getNombre(), R.mipmap.garantia,R.mipmap.devolucion,R.mipmap.eliminar, "update" , "0", Constantes.itemsVenta.get(i).getIdVenta(), "0"));
        }

        lista.setAdapter(new AdapterLista_Productos_MDatosCobro(getActivity(), arrayList));
    }

    public void ModificarLista()
    {
        arrayListInsert.clear();

        //productos con insert
        for(int i = 0; i < arrayList.size(); i++)
        {
            if(arrayList.get(i).getEstado().equalsIgnoreCase("insert"))
            {
                arrayListInsert.add(new ItemListaroductos_MDatosCobro(arrayList.get(i).getNombre(), R.mipmap.garantia,R.mipmap.devolucion,R.mipmap.eliminar, arrayList.get(i).getEstado(), arrayList.get(i).getCantidad(), arrayList.get(i).getIdVenta(), arrayList.get(i).getCantidadAdicional()));
            }
        }

        arrayList.clear();

        //productos con update
        for(int i = 0; i < Constantes.itemsVenta.size(); i++)
        {
            arrayList.add(new ItemListaroductos_MDatosCobro(Constantes.itemsProductos.get(i).getNombre(), R.mipmap.garantia,R.mipmap.devolucion,R.mipmap.eliminar, "update" , Constantes.itemsVenta.get(i).getCantidad(), Constantes.itemsVenta.get(i).getIdVenta(), Constantes.itemsVenta.get(i).getNuevaCantidad()));
        }

        //uniendo los productos update e insert
        for(int i = 0; i < arrayListInsert.size(); i++)
        {
            arrayList.add(new ItemListaroductos_MDatosCobro(arrayListInsert.get(i).getNombre(), R.mipmap.garantia,R.mipmap.devolucion,R.mipmap.eliminar, arrayListInsert.get(i).getEstado(), arrayListInsert.get(i).getCantidad(), arrayListInsert.get(i).getIdVenta(), arrayListInsert.get(i).getCantidadAdicional()));
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
                    arrayListP.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("tiempoGarantia"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));
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

    public void AlertaInfoProducto(final String nom, String descri, String precio, final String can, String dispo, final String idProducto, final int posicionLista)
    {
        LayoutInflater inflaterAlert = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflaterAlert.inflate(R.layout.alert_info_producto, null);

        final TextView descripcion = (TextView)dialoglayout.findViewById(R.id.txtDescripcion_AlertInfo);
        final EditText precioVenta = (EditText)dialoglayout.findViewById(R.id.editPrecioVenta_AlertInfo);
        final EditText disponibles = (EditText)dialoglayout.findViewById(R.id.editDisponibles_AlertInfo);
        final EditText cantidad = (EditText)dialoglayout.findViewById(R.id.editCantidad_AlertInfo);

        descripcion.setText(descri);
        precioVenta.setText(precio);
        disponibles.setText(dispo);
        cantidad.setText(can);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.productos);
        builder.setTitle(nom);
        builder.setView(dialoglayout);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String capVenta = precioVenta.getText().toString();
                String capCantidad = cantidad.getText().toString();

                if (capVenta.equals("") ||
                        capCantidad.equals("")) {
                    Toast.makeText(getActivity(), "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (can.equalsIgnoreCase(capCantidad))
                    {
                        //Nada
                    }
                    else
                    {
                        if(arrayList.get(posicionLista).getEstado().equalsIgnoreCase("insert"))
                        {
                            int precioVenta = 0;

                            for (int j = 0; j < arrayListP.size(); j++) {
                                if (nom.equalsIgnoreCase(arrayListP.get(j).getNombre())) {
                                    precioVenta = Integer.valueOf(arrayListP.get(j).getPrecioVenta());
                                }
                            }

                            int total = Integer.valueOf(totalPagar.getText().toString());
                            int totalNuevo = total - Integer.valueOf(can) * precioVenta;

                            arrayList.get(posicionLista).setCantidad(capCantidad);

                            int precioNuevo = Integer.valueOf(capCantidad) * precioVenta;
                            totalNuevo = totalNuevo + precioNuevo;
                            totalPagar.setText(String.valueOf(totalNuevo));

                            //Nuevo valor restante
                            int diferencia = Integer.valueOf(totalNuevo) - total;
                            diferencia = Integer.valueOf(saldoRestante.getText().toString()) + diferencia;

                            saldoRestante.setText(String.valueOf(diferencia));
                        }
                        else
                        {
                            String cantidadBase = Constantes.itemsVenta.get(posicionLista).getCantidad();

                            if(capCantidad.equalsIgnoreCase(cantidadBase))
                            {
                                int precioVenta = 0;

                                for (int j = 0; j < arrayListP.size(); j++) {
                                    if (nom.equalsIgnoreCase(arrayListP.get(j).getNombre())) {
                                        precioVenta = Integer.valueOf(arrayListP.get(j).getPrecioVenta());
                                    }
                                }

                                int precioAntiguo = (Integer.valueOf(Constantes.itemsVenta.get(posicionLista).getNuevaCantidad()) * precioVenta);
                                int precioActual = Integer.valueOf(totalPagar.getText().toString());

                                precioActual = precioActual - precioAntiguo;

                                precioActual = precioActual + (Integer.valueOf(precioVenta) * Integer.valueOf(cantidadBase));

                                Constantes.itemsVenta.get(posicionLista).setNuevaCantidad("0");
                                totalPagar.setText(String.valueOf(precioActual));
                                ModificarLista();

                                //Nuevo valor restante
                                int diferencia = precioAntiguo - (Integer.valueOf(precioVenta) * Integer.valueOf(cantidadBase));
                                diferencia = Integer.valueOf(saldoRestante.getText().toString()) - diferencia;

                                saldoRestante.setText(String.valueOf(diferencia));
                            }
                            else
                            {
                                if(Integer.valueOf(capCantidad) > Integer.valueOf(cantidadBase))
                                {
                                    int precioVenta = 0;

                                    for (int j = 0; j < arrayListP.size(); j++) {
                                        if (nom.equalsIgnoreCase(arrayListP.get(j).getNombre())) {
                                            precioVenta = Integer.valueOf(arrayListP.get(j).getPrecioVenta());
                                        }
                                    }

                                    //int total = Integer.valueOf(totalPagar.getText().toString());
                                    //total = total - Integer.valueOf(can) * precioVenta;

                                    if (arrayList.get(posicionLista).getEstado().equalsIgnoreCase("update"))
                                    {
                                        for (int j = 0; j < Constantes.itemsVenta.size(); j++)
                                        {
                                            if (idProducto.equalsIgnoreCase(Constantes.itemsVenta.get(j).getIdProducto()))
                                            {
                                                Constantes.itemsVenta.get(j).setNuevaCantidad(capCantidad);
                                                ModificarLista();
                                            }
                                        }
                                    }

                                    int total = Integer.valueOf(totalPagar.getText().toString());
                                    int totalNuevo = total - Integer.valueOf(can) * precioVenta;

                                    int precioNuevo = Integer.valueOf(capCantidad) * precioVenta;
                                    totalNuevo = totalNuevo + precioNuevo;
                                    totalPagar.setText(String.valueOf(totalNuevo));

                                    //Nuevo valor restante
                                    int diferencia = Integer.valueOf(totalNuevo) - total;
                                    diferencia = Integer.valueOf(saldoRestante.getText().toString()) + diferencia;

                                    saldoRestante.setText(String.valueOf(diferencia));
                                }
                                else
                                {
                                    if (Integer.valueOf(capCantidad) < Integer.valueOf(can) && arrayList.get(posicionLista).getEstado().equalsIgnoreCase("update")) {
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                                        alerta.setTitle("Alerta");
                                        alerta.setIcon(R.mipmap.informacion);
                                        alerta.setMessage("La cantidad del producto no puede ser menor a la actual, debe registrar el producto por garantía o devolución.");
                                        alerta.setCancelable(false);
                                        alerta.setPositiveButton("Aceptar", null);
                                        alerta.show();
                                    }
                                    else {
                                        //Metodo para sumar al total, en caso de que un producto sea mas de uno

                                        int precioVenta = 0;

                                        for (int j = 0; j < arrayListP.size(); j++) {
                                            if (nom.equalsIgnoreCase(arrayListP.get(j).getNombre())) {
                                                precioVenta = Integer.valueOf(arrayListP.get(j).getPrecioVenta());
                                            }
                                        }

                                        int total = Integer.valueOf(totalPagar.getText().toString());

                                        int totalNuevo = total - Integer.valueOf(can) * precioVenta;

                                        if (arrayList.get(posicionLista).getEstado().equalsIgnoreCase("update"))
                                        {
                                            for (int j = 0; j < Constantes.itemsVenta.size(); j++)
                                            {
                                                if (idProducto.equalsIgnoreCase(Constantes.itemsVenta.get(j).getIdProducto()))
                                                {
                                                    Constantes.itemsVenta.get(j).setNuevaCantidad(capCantidad);
                                                    ModificarLista();
                                                }
                                            }
                                        }

                                        int precioNuevo = Integer.valueOf(capCantidad) * precioVenta;
                                        totalNuevo = totalNuevo + precioNuevo;
                                        totalPagar.setText(String.valueOf(totalNuevo));

                                        //Nuevo valor restante
                                        int diferencia = Integer.valueOf(totalNuevo) - total;
                                        diferencia = Integer.valueOf(saldoRestante.getText().toString()) + diferencia;

                                        saldoRestante.setText(String.valueOf(diferencia));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
