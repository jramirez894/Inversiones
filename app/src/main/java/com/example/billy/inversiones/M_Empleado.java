package com.example.billy.inversiones;

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

public class M_Empleado extends AppCompatActivity
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
        setContentView(R.layout.activity_m__empleado);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        cedula =(EditText)findViewById(R.id.editCedula_M_Empleado);
        nombreE =(EditText)findViewById(R.id.editNombre_M_Empleado);
        direccion =(EditText)findViewById(R.id.editDireccion_M_Empleado);
        telefono =(EditText)findViewById(R.id.editTelefono_M_Empleado);
        correo =(EditText)findViewById(R.id.editCorreo_M_Empleado);
        contrasena =(EditText)findViewById(R.id.editContrasena_M_Empleado);
        verContrasena =(EditText)findViewById(R.id.editVerificacionContrasena_M_Empleado);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_m__empleado, menu);
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
            case R.id.guardarEmpleado_MEmpleado:
                ModificarEmpleado();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ModificarEmpleado()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Modificar Cliente?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(M_Empleado.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(M_Empleado.this, PrincipalMenu.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }
}
