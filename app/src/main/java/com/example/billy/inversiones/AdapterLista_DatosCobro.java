package com.example.billy.inversiones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BILLY on 24/09/2015.
 */
public class AdapterLista_DatosCobro extends ArrayAdapter
{

    public AdapterLista_DatosCobro(Context context, List objects)
    {
        super(context ,0, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.itemslista_productos_datoscobro,null);
        }

        ItemListaProdutos_DatosCobro items =(ItemListaProdutos_DatosCobro)getItem(position);

        TextView nomProducto=(TextView)convertView.findViewById(R.id.textViewNombreProducto_DatosCobro);
        ImageView eliminar=(ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_DatosCobro);

        nomProducto.setText(items.getNomProducto());
        eliminar.setImageResource(items.getEliminar());
        return convertView;
    }
}

