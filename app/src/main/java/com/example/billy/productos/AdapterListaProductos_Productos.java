package com.example.billy.productos;

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

import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 02/10/2015.
 */
public class AdapterListaProductos_Productos extends ArrayAdapter
{
    public static ItemsListaProductos_Productos itemsPosicion;

    public AdapterListaProductos_Productos(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemsproducto_productos,null);
        }

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombreProducto_Productos);
        ImageView editar = (ImageView)convertView.findViewById(R.id.imageViewEditarProducto_Productos);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_Productos);

        ItemsListaProductos_Productos items = (ItemsListaProductos_Productos)getItem(position);

        nombre.setText(items.getNombre());
        editar.setImageResource(items.getEditar());
        eliminar.setImageResource(items.getEliminar());

        editar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(),M_Producto.class);
                getContext().startActivity(intent);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                itemsPosicion = (ItemsListaProductos_Productos) getItem(position);
                EliminarProducto();
            }
        });

        return convertView;
    }


    public void EliminarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.eliminar);
        builder.setTitle("Elimina");
        builder.setMessage("¿Eliminar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayAdapter adapter = new AdapterListaProductos_Productos(getContext(),Productos.arrayList);
                adapter.remove(itemsPosicion);
                Productos.listaProductos.setAdapter(adapter);
                Toast.makeText(getContext(), "El Producto se Borro Correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

}
