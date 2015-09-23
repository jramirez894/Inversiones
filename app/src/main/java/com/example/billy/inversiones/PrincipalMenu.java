package com.example.billy.inversiones;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PrincipalMenu extends AppCompatActivity
{
    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle toggle;
    ListView listaDrawer;

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

        String[] titulos = getResources().getStringArray(R.array.array_menu_drawer);

        //Obtener drawer
        menuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Obtener listview
        listaDrawer = (ListView) findViewById(R.id.lista_menu_drawer);

        ArrayList<ItemsMenuDrawer> items = new ArrayList<ItemsMenuDrawer>();
        items.add(new ItemsMenuDrawer(titulos[0], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[1], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[2], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[3], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[4], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[5], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[6], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[7], R.mipmap.ic_launcher));
        items.add(new ItemsMenuDrawer(titulos[8], R.mipmap.ic_launcher));

        // Relacionar el adaptador y la escucha de la lista del drawer
        listaDrawer.setAdapter(new AdapterMenuDrawer(this, items));

        //Activar icono del menu que se despliega
        toggle = new ActionBarDrawerToggle(PrincipalMenu.this, menuDrawer, R.drawable.ic_drawer, R.string.drawer_inicio, R.string.drawer_fin);
        getSupportActionBar().setHomeButtonEnabled(true);

        listaDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l)
            {
                switch (posicion)
                {
                    case 0:

                        break;

                    case 1:

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
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
