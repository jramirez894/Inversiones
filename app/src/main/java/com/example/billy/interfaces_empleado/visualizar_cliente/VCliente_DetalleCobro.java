package com.example.billy.interfaces_empleado.visualizar_cliente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.billy.inversiones.R;


public class VCliente_DetalleCobro extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_vcliente__detalle_cobro, container, false);
        return view;
    }


}
