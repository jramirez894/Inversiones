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

public class AgregarProducto extends AppCompatActivity
{

    EditText nombre;
    EditText cantidad;
    EditText precioCompra;
    EditText precioVenta;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        nombre = (EditText)findViewById(R.id.editTextNombre_Producto);
        cantidad = (EditText)findViewById(R.id.editTextCantidad_Producto);
        precioCompra = (EditText)findViewById(R.id.editTextPrecioCompra_Producto);
        precioVenta = (EditText)findViewById(R.id.editTextPrecioVenta_Producto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        String nom = nombre.getText().toString();
        String cant = nombre.getText().toString();
        String preCom = nombre.getText().toString();
        String preVen = nombre.getText().toString();


        switch (item.getItemId())
        {
            case R.id.guardarProducto_Producto:
                if (nom.equals("")||
                        cant.equals("")||
                        preCom.equals("")||
                        preVen.equals(""))
                {
                    Toast.makeText(AgregarProducto.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    guardarProducto();
                }


        }
        return super.onOptionsItemSelected(item);
    }

    public void guardarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Agregar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AgregarProducto.this, "Su Registro fue Exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AgregarProducto.this, Productos.class);
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
