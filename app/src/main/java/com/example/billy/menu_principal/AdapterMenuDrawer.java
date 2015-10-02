package com.example.billy.menu_principal;

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
 * Created by Miguel on 23/09/2015.
 */
public class AdapterMenuDrawer extends ArrayAdapter {

    public AdapterMenuDrawer(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.lista_menu_drawer, null);
        }


        ImageView icon = (ImageView) convertView.findViewById(R.id.icono);
        TextView name = (TextView) convertView.findViewById(R.id.nombre);

        ItemsMenuDrawer item = (ItemsMenuDrawer) getItem(position);

        icon.setImageResource(item.getIcono());
        name.setText(item.getNombre());

        return convertView;
    }
}