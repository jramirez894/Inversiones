package com.example.billy.interfaces_empleado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.inversiones.R;

import java.util.List;

/**
 * Created by Admin_Sena on 08/10/2015.
 */
public class Adapter_PrincipalMenu extends ArrayAdapter
{

    public Adapter_PrincipalMenu(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_principalmenu,null);
        }

        ItemPrincipalMenu items = (ItemPrincipalMenu)getItem(position);

        ImageView icono = (ImageView)convertView.findViewById(R.id.imageView_PrincipalMenu);
        TextView nombre = (TextView)convertView.findViewById(R.id.texView_PrincipalMenu);

        icono.setImageResource(items.getIcono());
        nombre.setText(items.getNombre());

        return convertView;
    }
}
