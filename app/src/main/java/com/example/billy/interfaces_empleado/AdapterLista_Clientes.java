package com.example.billy.interfaces_empleado;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.clientes.ModificarCliente;
import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 14/10/2015.
 */
public class AdapterLista_Clientes extends ArrayAdapter
{
    public static ItemLista_Clientes positionEditar;

    public AdapterLista_Clientes(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView== null)
        {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listaclientes_empleado,null);
        }

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombreListaClientes_Empleados);
        ImageView editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaClientes_Empleados);

        final ItemLista_Clientes items =(ItemLista_Clientes)getItem(position);

        nombre.setText(items.getNom());
        editar.setImageResource(items.getEditar());

        editar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                positionEditar = (ItemLista_Clientes)getItem(position);
                Intent intent = new Intent(getContext(), ModificarCliente.class);
                intent.putExtra("Interfaz","Empleado");
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}
