package com.example.billy.empleado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class AdapterListaEmpleado extends ArrayAdapter
{
    public static ItemListaEmpleado posicionItems;

    public AdapterListaEmpleado(Context context,List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemlista_empleado,null);
        }

        ItemListaEmpleado items =(ItemListaEmpleado)getItem(position);

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombreListaEmpleado);
        ImageView editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaEmpleado);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarListaEmpleado);

        nombre.setText(items.getNombre());
        editar.setImageResource(items.getEdita());
        eliminar.setImageResource(items.getEliminar());

        editar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), M_Empleado.class);
                getContext().startActivity(intent);

            }
        });

        eliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                posicionItems = (ItemListaEmpleado) getItem(position);
                EliminarEmpleado();
            }
        });

        return convertView;
    }


    public void EliminarEmpleado()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Empleado?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new AdapterListaEmpleado(getContext(), Empleados.arrayList);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                Empleados.listaEmpleado.setAdapter(adapter);
                Toast.makeText(getContext(), "Se Elimino el Cliente Correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }


}
