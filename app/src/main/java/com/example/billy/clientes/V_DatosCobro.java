package com.example.billy.clientes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class V_DatosCobro extends Fragment
{
    public static ListView lista;
    public static TextView fechaVenta;
    public static TextView totalPagar;
    public static TextView abono;
    public static TextView valorRestante;
    ArrayList<ItemsLista_Productos_ViDatosCobro> arrayList = new ArrayList<ItemsLista_Productos_ViDatosCobro>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_v__datos_cobro, container, false);

        lista = (ListView)view.findViewById(R.id.listViewListaProductos_VDatosCobro);
        ActualizarLista();

        fechaVenta = (TextView)view.findViewById(R.id.textViewFechaVenta_VDatosCobro);
        totalPagar = (TextView)view.findViewById(R.id.textViewTotalPagar_VDatosCobro);
        abono = (TextView)view.findViewById(R.id.textViewtAbono_VDatosCobro);
        valorRestante = (TextView)view.findViewById(R.id.textViewValorRestante_VDatosCobro);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });

        return view;
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemsLista_Productos_ViDatosCobro("Nombre: " + "Sabanas", "Precio:$ " + "5.000"));
        arrayList.add(new ItemsLista_Productos_ViDatosCobro("Nombre: " + "Sabanas", "Precio:$ " + "5.000"));

        lista.setAdapter(new AdapterLista_Productos_VDatosCobro(getActivity(),arrayList));
    }


}
