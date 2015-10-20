package com.example.billy.clientes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.billy.inversiones.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class M_DatosCobro extends Fragment implements View.OnClickListener
{
    public static ListView lista;
    public static ArrayList<ItemListaroductos_MDatosCobro> arrayList = new ArrayList<ItemListaroductos_MDatosCobro>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private DatePickerDialog datePickerDialogPendiente;
    private SimpleDateFormat dateFormatterPendiente;

    public static AutoCompleteTextView buscarProducto;
    public static EditText valorProducto;
    public static EditText fechaVenta;
    public static EditText totalPagar;
    public static EditText abono;
    public static ImageView pendiente;
    public static EditText fechaPendiente;
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
        pendiente=(ImageView)view.findViewById(R.id.buttonPendiente_DatosCobro_Mcliente);

        valorRestante=(EditText)view.findViewById(R.id.editTextValorRestante_DatosCobro_Mcliente);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaVenta=(EditText)view.findViewById(R.id.editFechaVenta_DatosCobro_Mcliente);
        fechaVenta.setInputType(InputType.TYPE_NULL);
        fechaVenta.requestFocus();

        //Fecha Pendiente
        dateFormatterPendiente = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaPendiente=(EditText)view.findViewById(R.id.editTextFechaPendiente_DatosCobro_Mcliente);
        fechaPendiente.setInputType(InputType.TYPE_NULL);
        fechaPendiente.requestFocus();

        lista=(ListView)view.findViewById(R.id.listViewListaProductos_DatosCobro_Mcliente);
        ActualizarLista();
        setDateTimeField();
        setDateTimeFieldPendiente();

        pendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaPendiente.setVisibility(View.VISIBLE);
            }
        });

        //Metodo para restar el abono ingresado al valor total
        abono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try
                {
                    //con la variable charSequence se captura y se convierte a entero lo que se esta escribiendo en el EditText de abono
                    int abonoIngresado = Integer.valueOf(charSequence.toString());
                    int totalAPagar = Integer.parseInt(totalPagar.getText().toString());
                    int resta = totalAPagar - abonoIngresado;

                    valorRestante.setText(String.valueOf(resta));
                } catch (Exception e) {
                    valorRestante.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

    private void setDateTimeFieldPendiente()
    {
        fechaPendiente.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogPendiente = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaPendiente.setText(dateFormatterPendiente.format(newDate.getTime()));
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

        if(view == fechaPendiente)
        {
            datePickerDialogPendiente.show();
        }
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemListaroductos_MDatosCobro("Sabanas", R.mipmap.garantia,R.mipmap.devolucion,R.mipmap.eliminar));

        lista.setAdapter(new AdapterLista_Productos_MDatosCobro(getContext() , arrayList));
    }

}
