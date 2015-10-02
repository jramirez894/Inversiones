package com.example.billy.clientes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.billy.inversiones.R;


public class DatosPersonales extends Fragment
{
    public static EditText ced;
    public static EditText nom;
    public static EditText direccion;
    public static EditText telefono;
    public static EditText correo;

    public static EditText nomEmpresa;
    public static EditText dircEmpresa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;

        view = inflater.inflate(R.layout.fragment_datos_personales, container, false);

        ced=(EditText)view.findViewById(R.id.editCedula_AgregarCliente);
        nom=(EditText)view.findViewById(R.id.editNombre_AgregarCliente);
        direccion=(EditText)view.findViewById(R.id.editDireccion_AgregarCliente);
        telefono=(EditText)view.findViewById(R.id.editTelefono_AgregarCliente);
        correo=(EditText)view.findViewById(R.id.editCorreo_AgregarCliente);
        nomEmpresa=(EditText)view.findViewById(R.id.editNombreEmpresa_AgregarCliente);
        dircEmpresa=(EditText)view.findViewById(R.id.editDireccionEmpresa_AgregarCliente);
        return view;
    }


}
