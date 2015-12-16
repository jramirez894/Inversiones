package com.example.billy.clientes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.billy.inversiones.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrador on 15/12/2015.
 */
public class AdapterLista_ProductosNuevos_MDatosCobro extends ArrayAdapter implements View.OnClickListener
{

    public static ItemListaroductos_MDatosCobro posicionItems;
    //Variables fecha personalizada
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private DatePickerDialog datePickerDialogDevolucion;
    private SimpleDateFormat dateFormatterDevolucion;

    EditText editFechaGarantia;
    EditText editFechaDevolucion;


    public AdapterLista_ProductosNuevos_MDatosCobro(Context context, List objects)
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
        final ImageView garantia = (ImageView)convertView.findViewById(R.id.imageViewGarantiaProducto_MDatosCobro);
        ImageView devolucion = (ImageView)convertView.findViewById(R.id.imageViewDevolucionProducto_MDatosCobro);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_MDatosCobro);

        nombre.setText(items.getNombre());
        garantia.setImageResource(items.getGarantia());
        devolucion.setImageResource(items.getDevolucion());
        eliminar.setImageResource(items.getEliminar());

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
            public void onClick(DialogInterface dialog, int which) {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new AdapterLista_ProductosNuevos_MDatosCobro(getContext(), M_DatosCobro.arrayListProductosNuevos);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                M_DatosCobro.listaProductosNuevos.setAdapter(adapter);

                if(M_DatosCobro.arrayListProductosNuevos.isEmpty())
                {
                    M_DatosCobro.layoutProductosNuevos.setVisibility(View.GONE);
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
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
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Capturar variables de la alerta
                String capDescripcion = editDescripcionGarantia.getText().toString();
                String capFecha = editFechaGarantia.getText().toString();
                if (capDescripcion.equals("") ||
                        capFecha.equals("")) {
                    Toast.makeText(getContext(), "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                } else {
                    //Borrar un item de la lista
                    ArrayAdapter adapter = new AdapterLista_ProductosNuevos_MDatosCobro(getContext(), M_DatosCobro.arrayListProductosNuevos);
                    adapter.remove(posicionItems);
                    //Se carga de nuevo la vista
                    M_DatosCobro.listaProductosNuevos.setAdapter(adapter);
                    Toast.makeText(getContext(), "Producto Registrado por Garantia", Toast.LENGTH_SHORT).show();

                    if(M_DatosCobro.arrayListProductosNuevos.isEmpty())
                    {
                        M_DatosCobro.layoutProductosNuevos.setVisibility(View.GONE);
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
                    ArrayAdapter adapter = new AdapterLista_ProductosNuevos_MDatosCobro(getContext(), M_DatosCobro.arrayListProductosNuevos);
                    adapter.remove(posicionItems);
                    //Se carga de nuevo la vista
                    M_DatosCobro.listaProductosNuevos.setAdapter(adapter);
                    Toast.makeText(getContext(), "Producto Registrado por Devolucion", Toast.LENGTH_SHORT).show();

                    if(M_DatosCobro.arrayListProductosNuevos.isEmpty())
                    {
                        M_DatosCobro.layoutProductosNuevos.setVisibility(View.GONE);
                    }
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


}