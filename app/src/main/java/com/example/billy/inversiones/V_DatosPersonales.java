package com.example.billy.inversiones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class V_DatosPersonales extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view= inflater.inflate(R.layout.fragment_v__datos_personales, container, false);
        return view;
    }
}
