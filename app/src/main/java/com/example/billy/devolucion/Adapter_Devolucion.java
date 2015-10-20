package com.example.billy.devolucion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 20/10/2015.
 */
public class Adapter_Devolucion extends ArrayAdapter
{

    public Adapter_Devolucion(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
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

        return convertView;
    }
}
