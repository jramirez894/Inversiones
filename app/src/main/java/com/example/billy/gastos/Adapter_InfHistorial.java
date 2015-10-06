package com.example.billy.gastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 06/10/2015.
 */
public class Adapter_InfHistorial extends ArrayAdapter
{

    public Adapter_InfHistorial(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_infhistorial,null);
        }

        ItemLista_InfHistorial items = (ItemLista_InfHistorial)getItem(position);

        TextView tipo = (TextView)convertView.findViewById(R.id.textViewTipo_ListaInfHistorial);
        TextView valor = (TextView)convertView.findViewById(R.id.textViewValor_ListaInfHistorial);
        TextView descripcion = (TextView)convertView.findViewById(R.id.textViewDescripcion_ListaInfHistorial);

        tipo.setText(items.getTipoGasto());
        valor.setText(items.getValor());
        descripcion.setText(items.getDescripcion());
        return convertView;
    }
}
