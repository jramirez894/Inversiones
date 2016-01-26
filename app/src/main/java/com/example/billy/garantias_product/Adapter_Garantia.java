package com.example.billy.garantias_product;

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

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombre_ItemsGarantia);
        TextView telefono = (TextView)convertView.findViewById(R.id.textViewTelefono_ItemsGarantia);
        TextView nomProducto = (TextView)convertView.findViewById(R.id.textViewNombreProducto_ItemsGarantia);
        TextView cantidad = (TextView)convertView.findViewById(R.id.textViewCantidadProducto_ItemsGarantia);
        TextView fecha = (TextView)convertView.findViewById(R.id.textViewFecha_ItemsGarantia);
        TextView descripcion = (TextView)convertView.findViewById(R.id.textViewDescripcion_ItemsGarantia);
        TextView estado = (TextView)convertView.findViewById(R.id.textViewEstado_ItemsGarantia);


        nombre.setText("Nombre: " + items.getNombre());
        telefono.setText("Telefono: " + items.getTelefono());
        nomProducto.setText("Producto: " + items.getNombreProducto());
        cantidad.setText("Cantidad: " + items.getCantidad());
        fecha.setText("Fecha: " + items.getFecha());
        descripcion.setText("Descripcion: " + items.getDescripcion());
        estado.setText(items.getEstado());

        return convertView;
    }
}
