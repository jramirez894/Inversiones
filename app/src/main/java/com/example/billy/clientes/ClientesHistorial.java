package com.example.billy.clientes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;

import java.util.ArrayList;

public class ClientesHistorial extends Fragment
{
    ListView lista;
    ArrayList<Item_ClienteHistorial> arrayList = new ArrayList<Item_ClienteHistorial>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_clientes_historial, container, false);

        lista = (ListView)view.findViewById(R.id.listViewLista_ClienteHistorial);
        ActualizarLista();

        return view;
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        for(int i = 0; i < PrincipalMenu.itemsCobro.size(); i++)
        {
            String fecha = PrincipalMenu.itemsCobro.get(i).getFecha();
            String[] separated = fecha.split(" ");
            String fechaSplit = separated[0]; // this will contain "Fruit"

            arrayList.add(new Item_ClienteHistorial("Fecha: " + fechaSplit, "Valor: $"+ PrincipalMenu.itemsCobro.get(i).getAbono()));
        }

        lista.setAdapter(new AdapterLista_ClienteHistorial(getActivity(), arrayList));
    }

}
