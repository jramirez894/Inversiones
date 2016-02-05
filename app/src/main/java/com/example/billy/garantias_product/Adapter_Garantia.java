package com.example.billy.garantias_product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;

import java.util.List;

/**
 * Created by Admin_Sena on 20/10/2015.
 */
public class Adapter_Garantia extends ArrayAdapter
{
    public Adapter_Garantia(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater =(LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_garantia,null);
        }

        Items_Garantia_Visualizar items = (Items_Garantia_Visualizar)getItem(position);

        TextView nomProducto = (TextView)convertView.findViewById(R.id.textViewNombreProducto_ItemsGarantia);
        TextView fecha = (TextView)convertView.findViewById(R.id.textViewFecha_ItemsGarantia);
        TextView estado = (TextView)convertView.findViewById(R.id.textViewEstado_ItemsGarantia);

        View layout = (View)convertView.findViewById(R.id.layout);

        nomProducto.setText("Producto: " + items.getNombreProducto());
        fecha.setText("Fecha: " + items.getFecha());
        estado.setText(items.getEstado());

        //Para cambiar el color de fondo segun el estado de la garantia
        if(items.getEstado().equalsIgnoreCase("En espera"))
        {
            layout.setBackgroundColor(Color.parseColor("#8fff2109"));
        }
        else
        {
            if(items.getEstado().equalsIgnoreCase("Pendiente"))
            {
                layout.setBackgroundColor(Color.parseColor("#8f2868ff"));
            }
        }

        return convertView;
    }
}
