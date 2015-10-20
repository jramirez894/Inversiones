package com.example.billy.gastos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.inversiones.R;
import com.example.billy.saldo_caja.SaldoCaja;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Historial extends AppCompatActivity implements View.OnClickListener {
    EditText fechaInicio;
    EditText fechaFin;
    Button buscar;

    private DatePickerDialog datePickerDialogInicio;
    private SimpleDateFormat dateFormatterInicio;

    private SimpleDateFormat dateFormatterFin;
    private DatePickerDialog datePickerDialogFin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();


        buscar = (Button)findViewById(R.id.buttonBuscar_Historial);

        //Fecha Personalizada Fecha Inicio
        dateFormatterInicio = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatterFin = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fechaInicio = (EditText)findViewById(R.id.edidFechaInicio_Historial);
        fechaFin = (EditText)findViewById(R.id.edidFechaFin_Historial);

        fechaInicio.setInputType(InputType.TYPE_NULL);
        fechaFin.setInputType(InputType.TYPE_NULL);

        fechaInicio.requestFocus();
        fechaFin.requestFocus();

        setDateTimeFieldInicio();
        setDateTimeFieldFin();

        buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String capInico = fechaInicio.getText().toString();
                String capFin = fechaFin.getText().toString();

                Constantes.fechaInicio = capInico;
                Constantes.fechaFin = capFin;

                if (capInico.equals("")||
                        capFin.equals(""))
                {
                    Toast.makeText(Historial.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(Historial.this,Inf_Historial.class);
                    intent.putExtra("Inicio",capInico);
                    intent.putExtra("Fin",capFin);
                    startActivity(intent);
                }

            }
        });
    }

    private void setDateTimeFieldInicio()
    {
        fechaInicio.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogInicio = new DatePickerDialog(Historial.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaInicio.setText(dateFormatterInicio.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setDateTimeFieldFin()
    {
        fechaFin.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialogFin = new DatePickerDialog(Historial.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaFin.setText(dateFormatterFin.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
        {
            Intent intent;
            switch (Constantes.atrasHistorial)
            {
                case "Reg_Gasto":
                    intent = new Intent(Historial.this,Reg_Gasto.class);
                    startActivity(intent);
                    finish();
                    break;

                case "SaldoCaja":
                    intent = new Intent(Historial.this,SaldoCaja.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        if(view == fechaInicio)
        {
            datePickerDialogInicio.show();
        }

        if(view == fechaFin)
        {
            datePickerDialogFin.show();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
