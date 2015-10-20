package com.example.billy.interfaces_empleado;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.billy.clientes.AgregarCliente;
import com.example.billy.clientes.VisualizarCliente;
import com.example.billy.interfaces_empleado.gastos_empleado.RegGasto_Empleado;
import com.example.billy.inversiones.MainActivity;
import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class PrincipalEmpleado extends AppCompatActivity
{
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ListView listaDrawer;
    ListView listaClientes;


    ArrayList<ItemLista_Clientes> arrayList = new ArrayList<ItemLista_Clientes>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_empleado);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Menu");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.invalidateOptionsMenu();

        drawerLayout = (DrawerLayout)findViewById(R.id.menuDrawer);
        listaDrawer = (ListView)findViewById(R.id.listViewDrawer_PrincipalEmpleado);
        listaClientes= (ListView)findViewById(R.id.listaClienes_PrincipalEmpleado);
        ActualizarLista();

        String[] items = getResources().getStringArray(R.array.item_PrincipalMenu);

        ArrayList<ItemPrincipalMenu> arrayList = new ArrayList<ItemPrincipalMenu>();
        arrayList.add(new ItemPrincipalMenu(R.mipmap.personas,items[0]));
        arrayList.add(new ItemPrincipalMenu(R.mipmap.saldocaja,items[1]));
        arrayList.add(new ItemPrincipalMenu(R.mipmap.cerrar,items[2]));
        listaDrawer.setAdapter(new Adapter_PrincipalMenu(this, arrayList));

        toggle =new ActionBarDrawerToggle(PrincipalEmpleado.this,drawerLayout,R.string.drawer_inicio,R.string.drawer_fin);

        listaDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Intent intent;
                switch (position)
                {
                    case 0:
                        break;

                    case 1:
                        intent = new Intent(PrincipalEmpleado.this, RegGasto_Empleado.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(PrincipalEmpleado.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(listaDrawer);
            }
        });

        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                long cap = listaClientes.getItemIdAtPosition(position);
                Intent intent = new Intent(PrincipalEmpleado.this, VisualizarCliente.class);
                intent.putExtra("Posicion",cap);
                startActivity(intent);
            }
        });

    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemLista_Clientes("Miguel", R.mipmap.editar));

        listaClientes.setAdapter(new AdapterLista_Clientes(this,arrayList));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_empleado, menu);
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
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch (item.getItemId())
        {
            case R.id.agregarCliente_Empleado:
                Intent intent = new Intent(PrincipalEmpleado.this, AgregarCliente.class);
                intent.putExtra("Interfaz","Empleado");
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
