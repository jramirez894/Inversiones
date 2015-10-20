package com.example.billy.empleado;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class Empleados extends AppCompatActivity
{
    AutoCompleteTextView buscarEmpleado;
    public static ListView listaEmpleado;
    ImageView buscar;

    public static ArrayList<ItemListaEmpleado> arrayList = new ArrayList<ItemListaEmpleado>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        buscarEmpleado = (AutoCompleteTextView)findViewById(R.id.autocompleteBuscarEmpleado_Empleado);
        listaEmpleado = (ListView)findViewById(R.id.listaEmpleados_Empleado);
        buscar = (ImageView)findViewById(R.id.imgBuscarEmpleado_Empleado);

        ActualizarLista();

        listaEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Intent intent = new Intent(Empleados.this, V_Empleado.class);
                startActivity(intent);
            }
        });
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemListaEmpleado("Jeniffer", R.mipmap.editar, R.mipmap.eliminar));
        arrayList.add(new ItemListaEmpleado("Migue", R.mipmap.editar, R.mipmap.eliminar));

        listaEmpleado.setAdapter(new AdapterListaEmpleado(this, arrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empleados, menu);
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
            case R.id.agregarEmpleado_Empleado:
                Intent intent = new Intent(Empleados.this, AgregarEmpleado.class);
                startActivity(intent);
                break;

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
