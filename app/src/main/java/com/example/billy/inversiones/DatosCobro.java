package com.example.billy.inversiones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DatosCobro extends Fragment
{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.fragment_datos_cobro, container, false);
        return view;
    }


}
