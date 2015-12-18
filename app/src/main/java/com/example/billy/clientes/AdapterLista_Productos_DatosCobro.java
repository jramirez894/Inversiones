package com.example.billy.clientes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by BILLY on 24/09/2015.
 */
public class AdapterLista_Productos_DatosCobro extends ArrayAdapter
{

    public static ItemListaProdutos_DatosCobro posicionItems;

    public AdapterLista_Productos_DatosCobro(Context context, List objects)
    {
        super(context ,0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.itemslista_productos_datoscobro,null);
        }

        TextView nomProducto=(TextView)convertView.findViewById(R.id.textViewNombreProducto_DatosCobro);
        TextView cantidad=(TextView)convertView.findViewById(R.id.textViewCantidadProducto_DatosCobro);
        ImageView eliminar=(ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_DatosCobro);
        ImageView info=(ImageView)convertView.findViewById(R.id.imageViewInfoProducto_DatosCobro);

        ItemListaProdutos_DatosCobro items = (ItemListaProdutos_DatosCobro)getItem(position);

        nomProducto.setText(items.getNomProducto());
        cantidad.setText(items.getCantidad());
        eliminar.setImageResource(items.getEliminar());
        info.setImageResource(items.getInfo());

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionItems = (ItemListaProdutos_DatosCobro) getItem(position);
                EliminarProducto();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionItems = (ItemListaProdutos_DatosCobro) getItem(position);

                String nombreProducto = posicionItems.getNomProducto();
                String cantidad = posicionItems.getCantidad();

                String descripcion = "";
                String precioVenta = "";
                String disponibles = "";

                for (int i = 0; i < DatosCobro.arrayList.size(); i++) {
                    if (nombreProducto.equalsIgnoreCase(DatosCobro.arrayList.get(i).getNombre())) {
                        descripcion = DatosCobro.arrayList.get(i).getDescripcion();
                        precioVenta = DatosCobro.arrayList.get(i).getPrecioVenta();
                        disponibles = DatosCobro.arrayList.get(i).getCantidad();

                        break;
                    }
                }

                AlertaInfoProducto(nombreProducto, descripcion, precioVenta, cantidad, disponibles);
            }
        });


        return convertView;
    }

    public void EliminarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                ArrayAdapter adapter = new AdapterLista_Productos_DatosCobro(getContext(), DatosCobro.arrayListItems);
                adapter.remove(posicionItems);
                //Se carga de nuevo la vista
                DatosCobro.lista.setAdapter(adapter);


                int precioVenta = 0;

                for (int i = 0; i <DatosCobro.arrayList.size(); i++)
                {
                    if (posicionItems.getNomProducto().equalsIgnoreCase(DatosCobro.arrayList.get(i).getNombre()))
                    {
                        precioVenta = Integer.valueOf(DatosCobro.arrayList.get(i).getPrecioVenta());
                    }
                }

                int precioActual = 0;

                //Solo se multiplica cuando la cantidad del producto es mayor a 1 de lo contrario solo se resta
                if (Integer.valueOf(posicionItems.getCantidad()) > 1)
                {
                    precioActual = Integer.valueOf(posicionItems.getCantidad()) * precioVenta;

                    DatosCobro.total = DatosCobro.total - precioActual;
                    DatosCobro.totalPagar.setText(String.valueOf(DatosCobro.total));
                }
                else
                {
                    DatosCobro.total = DatosCobro.total - precioVenta;
                    DatosCobro.totalPagar.setText(String.valueOf(DatosCobro.total));
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    public void AlertaInfoProducto(String nom, String descri, String precio,String can, String dispo)
    {
        LayoutInflater inflaterAlert = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflaterAlert.inflate(R.layout.alert_info_producto, null);

        final TextView descripcion = (TextView)dialoglayout.findViewById(R.id.txtDescripcion_AlertInfo);
        final EditText precioVenta = (EditText)dialoglayout.findViewById(R.id.editPrecioVenta_AlertInfo);
        final EditText disponibles = (EditText)dialoglayout.findViewById(R.id.editDisponibles_AlertInfo);
        final EditText cantidad = (EditText)dialoglayout.findViewById(R.id.editCantidad_AlertInfo);

        descripcion.setText(descri);
        precioVenta.setText(precio);
        disponibles.setText(dispo);
        cantidad.setText(can);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.productos);
        builder.setTitle(nom);
        builder.setView(dialoglayout);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String capVenta = precioVenta.getText().toString();
                String capCantidad = cantidad.getText().toString();

                if (capVenta.equals("") ||
                        capCantidad.equals("")) {
                    Toast.makeText(getContext(), "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (posicionItems.getCantidad().equalsIgnoreCase(capCantidad))
                    {
                        //Nada
                    }
                    else
                    {
                        //Metodo para sumar al total, en caso de que un producto sea mas de uno
                        if (Integer.valueOf(capCantidad) >= 1)
                        {
                            int precioVenta = 0;

                            for (int j = 0; j < DatosCobro.arrayList.size(); j++)
                            {
                                if (posicionItems.getNomProducto().equalsIgnoreCase(DatosCobro.arrayList.get(j).getNombre()))
                                {
                                    precioVenta = Integer.valueOf(DatosCobro.arrayList.get(j).getPrecioVenta());
                                }
                            }

                            DatosCobro.total = DatosCobro.total - (Integer.valueOf(posicionItems.getCantidad()) * precioVenta);

                            posicionItems.setCantidad(capCantidad);
                            DatosCobro.lista.setAdapter(new AdapterLista_Productos_DatosCobro(getContext(), DatosCobro.arrayListItems));

                            int precioNuevo = Integer.valueOf(posicionItems.getCantidad()) * precioVenta;
                            DatosCobro.total = DatosCobro.total + precioNuevo;
                            DatosCobro.totalPagar.setText(String.valueOf(DatosCobro.total));
                        }
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}

