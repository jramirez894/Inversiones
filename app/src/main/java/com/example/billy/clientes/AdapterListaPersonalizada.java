package com.example.billy.clientes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.menu_principal.ItemListaPersonalizada;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.inversiones.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BILLY on 22/09/2015.
 */
public class AdapterListaPersonalizada extends ArrayAdapter
{

    public static ItemListaPersonalizada posicionItems;
    public static ImageView editar;
    public static ImageView eliminar;
    public static EditText organizar;
    public static TextView nombreLista;
    public static Spinner spinOrden;

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

        switch (Constantes.EDITAR_LISTA)
        {
            case "Botones":

                ItemListaPersonalizada items = (ItemListaPersonalizada)getItem(position);

                nombreLista = (TextView)convertView.findViewById(R.id.textViewNombreListaPersonalizada);
                editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaPersonalizada);
                eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarListaPersonalizada);
                organizar = (EditText)convertView.findViewById(R.id.edit_OrganizarListaPersonalizada);
                spinOrden = (Spinner)convertView.findViewById(R.id.spinOrdenListaPersonalizada);

                nombreLista.setText(items.getNombreLista());
                editar.setImageResource(items.getEditar());
                eliminar.setImageResource(items.getEliminar());
                organizar.setText(items.getEdiOrganizar());

                editar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        posicionItems = (ItemListaPersonalizada) getItem(position);

                        //Abrir ModificarCliente
                        Intent intent= new Intent(getContext(),ModificarCliente.class);
                        intent.putExtra("Interfaz","Administrador");
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

                organizar.setVisibility(View.GONE);
                spinOrden.setVisibility(View.GONE);
                editar.setVisibility(View.VISIBLE);
                eliminar.setVisibility(View.VISIBLE);

                break;

            case "EditText":

                ItemListaPersonalizada items2 = (ItemListaPersonalizada)getItem(position);

                nombreLista = (TextView)convertView.findViewById(R.id.textViewNombreListaPersonalizada);
                editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaPersonalizada);
                eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarListaPersonalizada);
                organizar = (EditText)convertView.findViewById(R.id.edit_OrganizarListaPersonalizada);
                spinOrden = (Spinner)convertView.findViewById(R.id.spinOrdenListaPersonalizada);

                nombreLista.setText(items2.getNombreLista());
                organizar.setText(items2.getEdiOrganizar());

                organizar.setVisibility(View.VISIBLE);
                spinOrden.setVisibility(View.VISIBLE);
                editar.setVisibility(View.GONE);
                eliminar.setVisibility(View.GONE);

                ArrayList<String> arrayLimiteOrden = new ArrayList<String>();
                arrayLimiteOrden.clear();

                for(int i = 0; i < PrincipalMenu.items.size(); i++)
                {
                    arrayLimiteOrden.add(String.valueOf(i+1));
                }

                spinOrden.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayLimiteOrden));

                break;
        }
        organizar = (EditText)convertView.findViewById(R.id.edit_OrganizarListaPersonalizada);

        organizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ItemListaPersonalizada items3 = (ItemListaPersonalizada) getItem(position);

                LayoutInflater inflaterAlert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialoglayout = inflaterAlert.inflate(R.layout.alert_dialog_lista_personalizada_ordenar, null);

                final EditText editOrden = (EditText) dialoglayout.findViewById(R.id.editOrdenAlert);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialoglayout);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ordendAlert = editOrden.getText().toString();
                        items3.setEdiOrganizar(ordendAlert);
                        ArrayAdapter adapter = new AdapterListaPersonalizada(getContext(), PrincipalMenu.items);
                        //Se carga de nuevo la vista
                        PrincipalMenu.listaClientes.setAdapter(adapter);
                    }
                });
                builder.setCancelable(false);
                builder.show();
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
