package com.example.billy.interfaces_empleado.gastos_empleado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billy.inversiones.R;

public class RegGasto_Empleado extends AppCompatActivity
{
    Spinner tipoGasto;
    EditText valor;
    EditText descripcion;
    Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_gasto__empleado);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        tipoGasto = (Spinner)findViewById(R.id.spinnerReg_Gasto_Gasto_Empleado);
        valor = (EditText)findViewById(R.id.editTextValorGasto_Gasto_Empleado);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_Gasto_Empleado);
        guardar = (Button)findViewById(R.id.buttonReg_Gasto_Gasto_Empleado);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.tiposGastos,android.R.layout.simple_spinner_dropdown_item);
        tipoGasto.setAdapter(adapter);

        guardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String tipo = tipoGasto.getSelectedItem().toString();
                String val = valor.getText().toString();
                String des = descripcion.getText().toString();

                if (val.equals("")||
                        des.equals(""))
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
}
