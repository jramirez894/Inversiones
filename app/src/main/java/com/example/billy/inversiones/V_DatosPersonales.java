package com.example.billy.inversiones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class V_DatosPersonales extends Fragment
{
    public static TextView cedula;
    public static TextView nombre;
    public static TextView direccion;
    public static TextView telefono;
    public static TextView correo;
    public static TextView nomEmpresa;
    public static TextView dirEmpresa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view= inflater.inflate(R.layout.fragment_v__datos_personales, container, false);

        cedula = (TextView)view.findViewById(R.id.textViewCedula_VCliente);
        nombre = (TextView)view.findViewById(R.id.textViewNombre_VCliente);
        direccion = (TextView)view.findViewById(R.id.textViewDireccion_VCliente);
        telefono = (TextView)view.findViewById(R.id.textViewTelefono_VCliente);
        correo = (TextView)view.findViewById(R.id.textViewCorreo_VCliente);
        nomEmpresa = (TextView)view.findViewById(R.id.textViewNombreEmpresa_VCliente);
        dirEmpresa = (TextView)view.findViewById(R.id.textViewDireccion_VCliente);
        return view;
    }
}
