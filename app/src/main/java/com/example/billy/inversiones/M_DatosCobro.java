package com.example.billy.inversiones;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class M_DatosCobro extends Fragment implements View.OnClickListener
{
    public static ListView lista;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public static AutoCompleteTextView buscarProducto;
    public static EditText valorProducto;
    public static EditText fechaVenta;
    public static EditText totalPagar;
    public static EditText abono;
    public static Button okAbono;
    public static EditText valorRestante;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_m__datos_cobro, container, false);

        buscarProducto=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarProducto_DatoCobro_Mcliente);
        valorProducto=(EditText)view.findViewById(R.id.editPrecioProducto_DatosCobro_Mcliente);
        totalPagar=(EditText)view.findViewById(R.id.editTotalPagar_DatosCobro_Mcliente);
        abono=(EditText)view.findViewById(R.id.editTextAbono_DatosCobro_Mcliente);
        okAbono=(Button)view.findViewById(R.id.buttonAbono_DatosCobro_Mcliente);
        valorRestante=(EditText)view.findViewById(R.id.editTextValorRestante_DatosCobro_Mcliente);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaVenta=(EditText)view.findViewById(R.id.editFechaVenta_DatosCobro_Mcliente);
        fechaVenta.setInputType(InputType.TYPE_NULL);
        fechaVenta.requestFocus();

        lista=(ListView)view.findViewById(R.id.listViewListaProductos_DatosCobro_Mcliente);

        setDateTimeField();
        return view;
    }

    private void setDateTimeField()
    {
        fechaVenta.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaVenta.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view)
    {
        if(view == fechaVenta)
        {
            datePickerDialog.show();
        }
    }

}
