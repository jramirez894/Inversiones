package com.example.billy.clientes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.billy.inversiones.R;

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

        arrayList.add(new Item_ClienteHistorial("Fecha: " + "2015/10/06", "Valor:$ "+ "20000"));

        lista.setAdapter(new AdapterLista_ClienteHistorial(getContext(),arrayList));
    }


}
