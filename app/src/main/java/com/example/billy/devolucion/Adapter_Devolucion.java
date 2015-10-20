package com.example.billy.devolucion;

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

import com.example.billy.garantias_product.Garantia;
import com.example.billy.garantias_product.Items_Garantia;
import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 20/10/2015.
 */
public class Adapter_Devolucion extends ArrayAdapter
{
    public static Items_Devolucion posicionItems;

    public Adapter_Devolucion(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater =(LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_devolucion,null);
        }

        Items_Devolucion items = (Items_Devolucion)getItem(position);

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombre_ItemsDevolucion);
        TextView nomProducto = (TextView)convertView.findViewById(R.id.textViewNombreProducto_ItemsDevolucion);
        TextView fecha = (TextView)convertView.findViewById(R.id.textViewFecha_ItemsDevolucion);
        TextView descripcion = (TextView)convertView.findViewById(R.id.textViewDescripcion_ItemsDevolucion);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageEliminar_ItemsDevolucion);

        nombre.setText(items.getNombre());
        nomProducto.setText(items.getNomProducto());
        fecha.setText(items.getFecha());
        descripcion.setText(items.getDescripcion());
        eliminar.setImageResource(items.getEliminar());

        eliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                posicionItems = (Items_Devolucion) getItem(position);
                EliminarDevolucion();
            }
        });

        return convertView;
    }

    //Alerta de Confirmacion
    public void EliminarDevolucion()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.borrar);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Devolucion?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new Adapter_Devolucion(getContext(), Devolucion.arrayList);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                Devolucion.listaDevolucion.setAdapter(adapter);
                Toast.makeText(getContext(), "Se Elimino Devolucion Correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }
}
