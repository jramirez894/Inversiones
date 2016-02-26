package com.example.billy.clientes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.billy.inversiones.R;

public class V_DetalleCobro extends Fragment
{
    public static TextView nomEmpleado;
    public static TextView fechaCobro;
    public static TextView horaCobro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view= inflater.inflate(R.layout.fragment_v__detalle_cobro, container, false);

        nomEmpleado =(TextView)view.findViewById(R.id.textViewNombreEmpleado_VDetalleCobro);
        fechaCobro =(TextView)view.findViewById(R.id.textViewfechaCobro_VDetalleCobro);
        horaCobro =(TextView)view.findViewById(R.id.textViewtHoraCobro_VDetalleCobro);

        nomEmpleado.setText("Nombre Empleado: \n" + VisualizarCliente.nombreVendedorUsuarios);
        fechaCobro.setText("Fecha de Cobro: \n" + VisualizarCliente.fechaCobroFactura);
        horaCobro.setText("hora de Cobro: \n" + VisualizarCliente.horaCobroFactura);
        return view;
    }

}
