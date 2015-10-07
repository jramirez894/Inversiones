package com.example.billy.empleado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billy.inversiones.R;

public class AgregarEmpleado extends AppCompatActivity
{
    EditText cedula;
    EditText nombreE;
    EditText direccion;
    EditText telefono;
    EditText correo;
    EditText contrasena;
    EditText verContrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_empleado);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        cedula =(EditText)findViewById(R.id.editCedula_AgregarEmpleado);
        nombreE =(EditText)findViewById(R.id.editNombre_AgregarEmpleado);
        direccion =(EditText)findViewById(R.id.editDireccion_AgregarEmpleado);
        telefono =(EditText)findViewById(R.id.editTelefono_AgregarEmpleado);
        correo =(EditText)findViewById(R.id.editCorreo_AgregarEmpleado);
        contrasena =(EditText)findViewById(R.id.editContrasena_AgregarEmpleado);
        verContrasena =(EditText)findViewById(R.id.editVerificacionContrasena_AgregarEmpleado);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_empleado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.guardarEmpleado_Empleado:
                String ced = cedula.getText().toString();
                String nom = nombreE.getText().toString();
                String dir = direccion.getText().toString();
                String tel = telefono.getText().toString();
                String cor = correo.getText().toString();
                String cont = contrasena.getText().toString();
                String verCon = verContrasena.getText().toString();

                if (ced.equals("")||
                    nom.equals("")||
                    dir.equals("")||
                    tel.equals("")||
                    cor.equals("")||
                    cont.equals("")||
                    verCon.equals(""))
                {
                    Toast.makeText(AgregarEmpleado.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    guardarEmpleado();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Alerta de Confirmacion
    public void guardarEmpleado()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Agregar Empleado?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(AgregarEmpleado.this, "Su Registro fue Exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AgregarEmpleado.this, Empleados.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }

}
