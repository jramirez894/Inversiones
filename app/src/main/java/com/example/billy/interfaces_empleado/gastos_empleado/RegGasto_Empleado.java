package com.example.billy.interfaces_empleado.gastos_empleado;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import com.example.billy.inversiones.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegGasto_Empleado extends AppCompatActivity implements View.OnClickListener
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
        setContentView(R.layout.activity_reg_gasto__empleado);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        tipoGasto = (Spinner)findViewById(R.id.spinnerReg_Gasto_Gasto_Empleado);
        descripcionGasto = (TextView)findViewById(R.id.texViewDescripcionGasto_Gasto_Empleado);
        valor = (EditText)findViewById(R.id.editTextValorGasto_Gasto_Empleado);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_Gasto_Empleado);
        fecha = (EditText)findViewById(R.id.editTextFecha_Gasto_Empleado);
        guardar = (Button)findViewById(R.id.buttonReg_Gasto_Gasto_Empleado);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.tiposGastos,android.R.layout.simple_spinner_dropdown_item);
        tipoGasto.setAdapter(adapter);

        //Fecha Personalizada
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fecha = (EditText)findViewById(R.id.editTextFecha_Gasto);
        fecha.setInputType(InputType.TYPE_NULL);
        fecha.requestFocus();

        setDateTimeField();

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
                    Toast.makeText(RegGasto_Empleado.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegGasto_Empleado.this, "Su Registro fue Exitoso", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_reg_gasto__empleado, menu);
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
        if (id == R.id.action_settings)
        {
            return true;
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
        if(view == fecha)
        {
            datePickerDialog.show();
        }
    }
}
