package com.example.billy.interfaces_empleado.cliente_empleados;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.billy.inversiones.R;

public class A_DatosPersonales_Empleado extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_a__datos_personales__empleado, container, false);
        return view;
    }



}
