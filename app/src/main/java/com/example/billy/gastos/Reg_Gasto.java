package com.example.billy.gastos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Reg_Gasto extends AppCompatActivity implements View.OnClickListener
{
    Spinner tipoGasto;
    TextView descripcionGasto;
    EditText valor;
    EditText descripcion;
    EditText fecha;
    Button guardar;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__gasto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        tipoGasto = (Spinner)findViewById(R.id.spinnerReg_Gasto_Gasto);
        descripcionGasto = (TextView)findViewById(R.id.texViewDescripcionGasto_Gasto);
        valor = (EditText)findViewById(R.id.editTextValorGasto_Gasto);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_Gasto);
        guardar = (Button)findViewById(R.id.buttonReg_Gasto_Gasto);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fecha = (EditText)findViewById(R.id.editTextFecha_Gasto);
        fecha.setInputType(InputType.TYPE_NULL);
        fecha.requestFocus();

        setDateTimeField();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(Reg_Gasto.this,R.array.tiposGastos,android.R.layout.simple_spinner_dropdown_item);
        tipoGasto.setAdapter(adapter);

        guardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String tipo = tipoGasto.getSelectedItem().toString();
                String val = valor.getText().toString();
                String des = descripcion.getText().toString();
                String fec = fecha.getText().toString();

                if (val.equals("")||
                        des.equals("")||
                        fec.equals(""))
                {
                    Toast.makeText(Reg_Gasto.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegistrarGasto();
                }
            }
        });
    }

    private void setDateTimeField()
    {
        fecha.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fecha.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    //Alerta de Confirmacion
    public void RegistrarGasto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Registrar Gasto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(Reg_Gasto.this, "Su Registro fue Exitoso", Toast.LENGTH_SHORT).show();
                limpiar();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }

    public void limpiar()
    {
        valor.setText("");
        descripcion.setText("");
        fecha.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg__gasto, menu);
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
        switch (item.getItemId())
        {
            case R.id.historial_RegCobro:
                Constantes.atrasHistorial="Reg_Gasto";
                Intent intent = new Intent(Reg_Gasto.this,Historial.class);
                intent.putExtra("Atras",Constantes.atrasHistorial);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View view)
    {
        if(view == fecha) {
            datePickerDialog.show();
        }
    }
}
