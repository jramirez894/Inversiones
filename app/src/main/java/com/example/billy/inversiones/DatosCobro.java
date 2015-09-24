package com.example.billy.inversiones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class DatosCobro extends Fragment
{
    ListView lista;
    ArrayList<ItemListaProdutos_DatosCobro> arrayList = new ArrayList<ItemListaProdutos_DatosCobro>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.fragment_datos_cobro, container, false);

        lista=(ListView)view.findViewById(R.id.listViewListaProductos_DatosCobro);

        ActualizarLista();
        return view;
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemListaProdutos_DatosCobro("Sabana", R.mipmap.eliminar));

        lista.setAdapter(new AdapterLista_DatosCobro(getActivity() , arrayList));
    }

}
