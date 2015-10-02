package com.example.billy.inversiones;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class AdapterLista_Productos_MDatosCobro extends ArrayAdapter
{

    public static ItemListaroductos_MDatosCobro posicionItems;

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
        final ImageView garantia = (ImageView)convertView.findViewById(R.id.imageViewGarantiaProducto_MDatosCobro);
        ImageView devolucion = (ImageView)convertView.findViewById(R.id.imageViewDevolucionProducto_MDatosCobro);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_MDatosCobro);

        nombre.setText(items.getNombre());
        garantia.setImageResource(items.getGarantia());
        devolucion.setImageResource(items.getDevolucion());
        eliminar.setImageResource(items.getEliminar());

        garantia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                posicionItems = (ItemListaroductos_MDatosCobro) getItem(position);
                GarantiaProducto();
            }
        });

        devolucion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                posicionItems = (ItemListaroductos_MDatosCobro) getItem(position);
                DevolucionProducto();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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
                ArrayAdapter adapter = new AdapterLista_Productos_MDatosCobro(getContext(), M_DatosCobro.arrayList);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                M_DatosCobro.lista.setAdapter(adapter);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    public void GarantiaProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.garantia);
        builder.setTitle("Garantia");
        builder.setMessage("¿Registrar Producto por Garantia?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new AdapterLista_Productos_MDatosCobro(getContext(),M_DatosCobro.arrayList);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                M_DatosCobro.lista.setAdapter(adapter);
                Toast.makeText(getContext(),"Producto Registrado por Garantia",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    public void DevolucionProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.devolucion);
        builder.setTitle("Devolucion");
        builder.setMessage("¿Registrar Producto Por Devolucion?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new AdapterLista_Productos_MDatosCobro(getContext(), M_DatosCobro.arrayList);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                M_DatosCobro.lista.setAdapter(adapter);
                Toast.makeText(getContext(), "Producto Registrado por Devolucion", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }
}
