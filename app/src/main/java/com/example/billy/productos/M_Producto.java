package com.example.billy.productos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;

public class M_Producto extends AppCompatActivity
{
    EditText nombre;
    EditText cantidad;
    EditText precioCompra;
    EditText precioVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m__producto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        nombre = (EditText)findViewById(R.id.editTextNombre_MProducto);
        cantidad = (EditText)findViewById(R.id.editTextCantidad_MProducto);
        precioCompra = (EditText)findViewById(R.id.editTextPrecioCompra_MProducto);
        precioVenta = (EditText)findViewById(R.id.editTextPrecioVenta_MProducto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_m__producto, menu);
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
            case R.id.modificar_MProducto:
                ModificarProducto();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    //Alerta de Confirmacion
    public void ModificarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Modificar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(M_Producto.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(M_Producto.this, Productos.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
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
