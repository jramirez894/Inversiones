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
import android.widget.ListView;

import com.example.billy.inversiones.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatosCobro extends Fragment implements View.OnClickListener
{
    public static ListView lista;
    public static ArrayList<ItemListaProdutos_DatosCobro> arrayList = new ArrayList<ItemListaProdutos_DatosCobro>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public static AutoCompleteTextView buscarProducto;
    public static EditText fechaVenta;
    public static EditText totalPagar;
    public static EditText abono;
    public static EditText valorRestante;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.fragment_datos_cobro, container, false);

        buscarProducto=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarProducto_DatosCobro);
        totalPagar=(EditText)view.findViewById(R.id.editTotalPagar_DatosCobro);
        abono=(EditText)view.findViewById(R.id.editTextAbono_DatosCobro);
        valorRestante=(EditText)view.findViewById(R.id.editTextValorRestante_DatosCobro);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaVenta=(EditText)view.findViewById(R.id.editFechaVenta_DatosCobro);
        fechaVenta.setInputType(InputType.TYPE_NULL);
        fechaVenta.requestFocus();

        lista=(ListView)view.findViewById(R.id.listViewListaProductos_DatosCobro);

        ActualizarLista();
        setDateTimeField();

        //Metodo para restar el abono ingresado al valor total
        abono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try
                {
                    //con la variable charSequence se captura y se convierte a entero lo que se esta escribiendo en el EditText de abono
                    int abonoIngresado = Integer.valueOf(charSequence.toString());
                    int totalAPagar = Integer.parseInt(totalPagar.getText().toString());
                    int resta = totalAPagar - abonoIngresado;

                    valorRestante.setText(String.valueOf(resta));
                }
                catch (Exception e)
                {
                    valorRestante.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

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

    @Override
    public void onClick(View view)
    {
        if(view == fechaVenta) {
            datePickerDialog.show();
        }
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemListaProdutos_DatosCobro("Sabana",R.mipmap.informacion, R.mipmap.eliminar));

        lista.setAdapter(new AdapterLista_Productos_DatosCobro(getActivity() , arrayList));
    }

}
