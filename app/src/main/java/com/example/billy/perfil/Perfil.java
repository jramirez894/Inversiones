package com.example.billy.perfil;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.empleado.Empleados;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;

public class Perfil extends AppCompatActivity
{
    EditText cedula;
    EditText nombre;
    EditText telefono;
    EditText correo;
    EditText contrasena;
    EditText vercontrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        cedula = (EditText)findViewById(R.id.editTextCedula_MiPerfil);
        nombre = (EditText)findViewById(R.id.editTextNombre_MiPerfil);
        telefono = (EditText)findViewById(R.id.editTextTelefono_MiPerfil);
        correo = (EditText)findViewById(R.id.editTextCorreo_MiPerfil);
        contrasena = (EditText)findViewById(R.id.editTextContrasena);
        vercontrasena = (EditText)findViewById(R.id.editTextVerContrasena_MiPerfil);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
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
          case R.id.guardar_Miperfil:
              guardarPerfil();
              break;
      }

        return super.onOptionsItemSelected(item);
    }

    public void guardarPerfil()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Guardar Modificaciones?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(Perfil.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Perfil.this, PrincipalMenu.class);
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
