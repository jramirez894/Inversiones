package com.example.billy.inversiones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

public class M_DetalleCobro extends Fragment
{
    public static AutoCompleteTextView buscarEmpleado;
    public static TextView nom;
    public static TextView tel;

    public static Spinner fechaDeCobro;
    public static Spinner diaCobro;
    public static Spinner horaCobro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_m__detalle_cobro, container, false);

        buscarEmpleado=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarEmpleado_DetalleCobro_Mcliente);
        nom=(TextView)view.findViewById(R.id.textViewnomEmpleado_DetalleCobro_Mcliente);
        tel=(TextView)view.findViewById(R.id.textViewtelEmpleado_DetalleCobro_Mcliente);
        fechaDeCobro=(Spinner)view.findViewById(R.id.spinnerFechaCobro_DetalleCobro_Mcliente);
        diaCobro=(Spinner)view.findViewById(R.id.spinnerDiaCobro_DetalleCobro_Mcliente);
        horaCobro=(Spinner)view.findViewById(R.id.spinnerHoraCobro_DetalleCobro_Mcliente);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.fechadeCobro, android.R.layout.simple_spinner_item);
        fechaDeCobro.setAdapter(adapter);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.diadeCobro, android.R.layout.simple_spinner_item);
        diaCobro.setAdapter(adapter1);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.horadeCobro, android.R.layout.simple_spinner_item);
        horaCobro.setAdapter(adapter2);
        return view;
    }
}
