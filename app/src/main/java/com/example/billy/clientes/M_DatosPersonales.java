package com.example.billy.clientes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.inversiones.R;


public class M_DatosPersonales extends Fragment
{
    public static EditText cedula;
    public static EditText nombre;
    public static EditText direccion;
    public static EditText telefono;
    public static EditText correo;

    public static EditText nomEmpresa;
    public static EditText dircEmpresa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view= inflater.inflate(R.layout.fragment_m__datos_personales, container, false);

        cedula=(EditText)view.findViewById(R.id.editCedula_DatoPersonales_Mcliente);
        nombre=(EditText)view.findViewById(R.id.editNombre_DatoPersonales_Mcliente);
        direccion=(EditText)view.findViewById(R.id.editDireccion_DatoPersonales_Mcliente);
        telefono=(EditText)view.findViewById(R.id.editTelefono_DatoPersonales_Mcliente);
        correo=(EditText)view.findViewById(R.id.editCorreo_DatoPersonales_Mcliente);
        nomEmpresa=(EditText)view.findViewById(R.id.editNombreEmpresa_DatoPersonales_Mcliente);
        dircEmpresa=(EditText)view.findViewById(R.id.editDireccionEmpresa_DatoPersonales_Mcliente);

        cedula.setText(Constantes.cedulaCliente);
        nombre.setText(Constantes.nombreCliente);
        direccion.setText(Constantes.direccionCliente);
        telefono.setText(Constantes.telefonoCliente);
        correo.setText(Constantes.correoCliente);
        nomEmpresa.setText(Constantes.nombreEmpresaCliente);
        dircEmpresa.setText(Constantes.direccionEmpresaCliente);

        //Toast.makeText(getActivity(), " " + ModificarCliente.cedulaCliente + " " + ModificarCliente.nombreCliente+ " " + ModificarCliente.direccionCliente+ " " +ModificarCliente.telefonoCliente+ " " +ModificarCliente.correoCliente, Toast.LENGTH_SHORT).show();

        return view;
    }
}
