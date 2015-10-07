package com.example.billy.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.billy.gastos.ItemLista_InfHistorial;
import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 07/10/2015.
 */
public class AdapterLista_ClienteHistorial extends ArrayAdapter
{

    public AdapterLista_ClienteHistorial(Context context,  List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_clientehistorial,null);
        }

        Item_ClienteHistorial items = (Item_ClienteHistorial)getItem(position);

        TextView fecha = (TextView)convertView.findViewById(R.id.textViewFecha_ListaClienteHistorial);
        TextView valor = (TextView)convertView.findViewById(R.id.textViewValor_ListaClienteHistorial);

        fecha.setText(items.getFecha());
        valor.setText(items.getValor());

        return convertView;
    }
}
