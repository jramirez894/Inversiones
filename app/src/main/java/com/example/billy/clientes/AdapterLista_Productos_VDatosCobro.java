package com.example.billy.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 30/09/2015.
 */
public class AdapterLista_Productos_VDatosCobro extends ArrayAdapter
{

    public AdapterLista_Productos_VDatosCobro(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater =(LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.itemslista_productos_vdatoscobro,null);

        }

        ItemsLista_Productos_ViDatosCobro items =(ItemsLista_Productos_ViDatosCobro)getItem(position);

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombre_listaDatosCobro);
        TextView precio = (TextView)convertView.findViewById(R.id.textViewPrecio_listaDatosCobro);

        nombre.setText(items.getNombre());
        precio.setText(items.getPrecio());

        return convertView;
    }
}
