package com.example.billy.inversiones;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PrincipalMenu extends AppCompatActivity
{
    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle toggle;
    ListView listaDrawer;

    public static ListView listaClientes;
    public static ArrayList<ItemListaPersonalizada> items = new ArrayList<ItemListaPersonalizada>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_menu);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.invalidateOptionsMenu();
        actionBar.setTitle("Menu");

        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);

        //Obtener drawer
        menuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Obtener listview
        listaDrawer = (ListView) findViewById(R.id.lista_menu_drawer);

        listaClientes=(ListView)findViewById(R.id.listaClienes_MenuPrincipal);
        ActualizarLista();

        String[] titulos = getResources().getStringArray(R.array.array_menu_drawer);

        ArrayList<ItemsMenuDrawer> items = new ArrayList<ItemsMenuDrawer>();
        items.add(new ItemsMenuDrawer(titulos[0], R.mipmap.capital));
        items.add(new ItemsMenuDrawer(titulos[1], R.mipmap.personas));
        items.add(new ItemsMenuDrawer(titulos[2], R.mipmap.personas));
        items.add(new ItemsMenuDrawer(titulos[3], R.mipmap.productos));
        items.add(new ItemsMenuDrawer(titulos[4], R.mipmap.capital));
        items.add(new ItemsMenuDrawer(titulos[5], R.mipmap.saldocaja));
        items.add(new ItemsMenuDrawer(titulos[6], R.mipmap.cancelado));
        items.add(new ItemsMenuDrawer(titulos[7], R.mipmap.perfil));
        items.add(new ItemsMenuDrawer(titulos[8], R.mipmap.cerrar));

        // Relacionar el adaptador y la escucha de la lista del drawer
        listaDrawer.setAdapter(new AdapterMenuDrawer(this, items));

        //Activar icono del menu que se despliega
        toggle = new ActionBarDrawerToggle(this, menuDrawer, R.string.drawer_inicio, R.string.drawer_fin);
        getSupportActionBar().setHomeButtonEnabled(true);

        listaDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l)
            {
                switch (posicion)
                {
                    case 0:
                        Intent intent = new Intent(PrincipalMenu.this, CapitalInicial.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(PrincipalMenu.this, Empleados.class);
                        startActivity(intent1);
                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                    case 4:

                        break;

                    case 5:

                        break;

                    case 6:

                        break;

                    case 7:

                        break;

                    case 8:

                        break;
                }
                menuDrawer.closeDrawer(listaDrawer);
            }
        });

        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                long cap = listaClientes.getItemIdAtPosition(i);
                Intent intent = new Intent(PrincipalMenu.this, VisualizarCliente.class);
                intent.putExtra("Posicion",cap);
                startActivity(intent);
            }
        });
    }

    public void ActualizarLista()
    {
        items.add(new ItemListaPersonalizada("jeniffer",R.mipmap.editar,R.mipmap.eliminar));
        items.add(new ItemListaPersonalizada("miguel",R.mipmap.editar,R.mipmap.eliminar));

        listaClientes.setAdapter(new AdapterListaPersonalizada(this,items));
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.agregarCliente:
                Intent intent=new Intent(PrincipalMenu.this,AgregarCliente.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
