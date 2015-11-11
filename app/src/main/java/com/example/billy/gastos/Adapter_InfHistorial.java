package com.example.billy.gastos;

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
import com.example.billy.inversiones.SesionUsuarios;

import java.util.List;

/**
 * Created by Admin_Sena on 06/10/2015.
 */
public class Adapter_InfHistorial extends ArrayAdapter
{

    public Adapter_InfHistorial(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_infhistorial,null);
        }

        ItemLista_InfHistorial items = (ItemLista_InfHistorial)getItem(position);

        TextView tipo = (TextView)convertView.findViewById(R.id.textViewTipo_ListaInfHistorial);
        TextView valor = (TextView)convertView.findViewById(R.id.textViewValor_ListaInfHistorial);
        ImageView icono = (ImageView)convertView.findViewById(R.id.imgIcono_ListaInfHistorial);

        tipo.setText(items.getTipoGasto());
        valor.setText(items.getValor());

        icono.setImageResource(items.getIcono());

        icono.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //ItemLista_InfHistorial itemDescripcion = (ItemLista_InfHistorial)getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.informacion);
                builder.setTitle("Descripción");
                builder.setMessage(Inf_Historial.arrayList.get(position).getDescripcion());
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                
                builder.setCancelable(false);
                builder.show();
            }
        });

        return convertView;
    }
}
