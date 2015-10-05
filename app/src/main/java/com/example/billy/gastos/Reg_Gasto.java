package com.example.billy.gastos;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.billy.inversiones.R;

public class Reg_Gasto extends AppCompatActivity
{
    Spinner tipoGasto;
    EditText valor;
    EditText descripcion;
    Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__gasto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();


        tipoGasto = (Spinner)findViewById(R.id.spinnerReg_Gasto_Gasto);
        valor = (EditText)findViewById(R.id.editTextValorGasto_Gasto);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion_Gasto);
        guardar = (Button)findViewById(R.id.buttonReg_Gasto_Gasto);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(Reg_Gasto.this,R.array.tiposGastos,android.R.layout.simple_spinner_dropdown_item);
        tipoGasto.setAdapter(adapter);

        guardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String val = valor.getText().toString();
                String des = descripcion.getText().toString();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg__gasto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
