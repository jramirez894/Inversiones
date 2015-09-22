package com.example.billy.inversiones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BILLY on 22/09/2015.
 */
public class AdapterListaPersonalizada extends ArrayAdapter
{

    public AdapterListaPersonalizada(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_personalizada,null);
        }

        ItemListaPersonalizada items = (ItemListaPersonalizada)getItem(position);

        TextView nombreLista = (TextView)convertView.findViewById(R.id.textViewNombreListaPersonalizada);
        ImageView editar = (ImageView)convertView.findViewById(R.id.imageViewEditar);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminar);

        nombreLista.setText(items.getNombreLista());
        editar.setImageResource(items.getEditar());
        eliminar.setImageResource(items.getEliminar());
        return convertView;
    }
}
