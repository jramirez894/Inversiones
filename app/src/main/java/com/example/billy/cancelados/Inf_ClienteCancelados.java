package com.example.billy.cancelados;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PagerAdapter;
import com.example.billy.menu_principal.PrincipalMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Inf_ClienteCancelados extends ActionBarActivity
{
    TextView cedula;
    TextView nombre;
    TextView direccion;
    TextView telefono;
    TextView correo;
    TextView nomEmpresa;
    TextView dirEmpresa;
    TextView sugerencia;
    Spinner estado;
    TextView txtestado;
    Button confirmar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf__cliente_cancelados);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        cedula = (TextView)findViewById(R.id.textViewCedula_VClienteClienteCancelado);
        nombre = (TextView)findViewById(R.id.textViewNombre_VClienteClienteCancelado);
        direccion = (TextView)findViewById(R.id.textViewDireccion_VClienteClienteCancelado);
        telefono = (TextView)findViewById(R.id.textViewTelefono_VClienteClienteCancelado);
        correo = (TextView)findViewById(R.id.textViewCorreo_VClienteClienteCancelado);
        nomEmpresa = (TextView)findViewById(R.id.textViewNombreEmpresa_VClienteClienteCancelado);
        dirEmpresa = (TextView)findViewById(R.id.textViewDireccionEmpresa_ClienteCancelado);
        sugerencia = (TextView)findViewById(R.id.textViewSugerencia_ClienteCancelado);
        estado = (Spinner)findViewById(R.id.spinnerEstado_ClienteCancelado);
        txtestado = (TextView)findViewById(R.id.texViewEstado_ClienteCancelado);
        confirmar = (Button)findViewById(R.id.buttonConfirmarEstado);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(Inf_ClienteCancelados.this, R.array.estado_cliente, android.R.layout.simple_spinner_dropdown_item);
        estado.setAdapter(adapter);

        String capSpinner = estado.getSelectedItem().toString();

        if (capSpinner.equals("Activo"))
        {
            txtestado.setText("Estado del Cliente Actualmente: Activo");
        }
        else
        {
            txtestado.setText("Estado del Cliente Actualmente: Inactivo");
        }

        confirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CambiarEstado();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inf__cliente_cancelados, menu);
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
            case R.id.eliminar_ClienteCancelado:
                Eliminar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Alerta de Confirmacion
    public void CambiarEstado()
    {
        final String capSpinner = estado.getSelectedItem().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
            builder.setTitle("Estado");
        builder.setMessage("¿Cambiar de Estado?"+capSpinner);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {


                if (capSpinner.equals("Activo"))
                {
                    txtestado.setText("Estado del Cliente Actualmente: Activo");
                }
                else
                {
                    txtestado.setText("Estado del Cliente Actualmente: Inactivo");
                }
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }


    //Alerta de Confirmacion para eliminar
    public void Eliminar()
    {
        final String capSpinner = estado.getSelectedItem().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.borrar);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Cliente?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Inf_ClienteCancelados.this,Cancelados.class);
                startActivity(intent);
                finish();
                Toast.makeText(Inf_ClienteCancelados.this,"El Cliente Fue Elimino Correctamente",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }



}
