package com.example.billy.devolucion;

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

        TextView nomProducto = (TextView)convertView.findViewById(R.id.textViewNombreProducto_ItemsDevolucion);
        TextView fecha = (TextView)convertView.findViewById(R.id.textViewFecha_ItemsDevolucion);
        TextView estado = (TextView)convertView.findViewById(R.id.textViewEstado_ItemsDevolucion);


        View layout = (View)convertView.findViewById(R.id.layout);

        nomProducto.setText("Producto: " + items.getNombreProducto());
        fecha.setText("Fecha: " + items.getFecha());
        estado.setText(items.getEstado());

        //Para cambiar el color de fondo segun el estado de la garantia
        layout.setBackgroundColor(Color.parseColor("#8fff2109"));

        return convertView;
    }


}
