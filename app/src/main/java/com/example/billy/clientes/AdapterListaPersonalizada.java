package com.example.billy.clientes;

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

import com.example.billy.menu_principal.ItemListaPersonalizada;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by BILLY on 22/09/2015.
 */
public class AdapterListaPersonalizada extends ArrayAdapter
{

    public static ItemListaPersonalizada posicionItems;

    public AdapterListaPersonalizada(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
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

        editar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                posicionItems = (ItemListaPersonalizada) getItem(position);

                //Abrir ModificarCliente
                Intent intent= new Intent(getContext(),ModificarCliente.class);
                getContext().startActivity(intent);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                posicionItems = (ItemListaPersonalizada) getItem(position);
                EliminarCliente();
            }
        });
        return convertView;
    }

    public void EliminarCliente()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Cliente?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new AdapterListaPersonalizada(getContext(), PrincipalMenu.items);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                PrincipalMenu.listaClientes.setAdapter(adapter);
                Toast.makeText(getContext(), "Se Elimino el Cliente Correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }
}
