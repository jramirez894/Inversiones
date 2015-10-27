package com.example.billy.empleado;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.billy.inversiones.R;

public class V_Empleado extends AppCompatActivity
{
    TextView textViewNombre_VEmpleado;
    TextView textViewTelefono_VEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v__empleado);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        textViewNombre_VEmpleado = (TextView) findViewById(R.id.textViewNombre_VEmpleado);
        textViewTelefono_VEmpleado = (TextView) findViewById(R.id.textViewTelefono_VEmpleado);

        Bundle bundle = getIntent().getExtras();
        textViewNombre_VEmpleado.setText("Nombre: " + bundle.getString("nombre"));
        textViewTelefono_VEmpleado.setText("Telefono: " + bundle.getString("telefono"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_v__empleado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


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
