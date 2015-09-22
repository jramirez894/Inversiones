package com.example.billy.inversiones;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CapitalInicial extends AppCompatActivity
{
    EditText capital;
    Button guardar;

    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle toggle;
    ListView listaDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_inicial);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.invalidateOptionsMenu();
        actionBar.setTitle("Menu");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);

        capital =(EditText)findViewById(R.id.editTextCapitalInicial);
        guardar =(Button)findViewById(R.id.buttonEnviar_CapitalInicial);

        //Obtener drawer
        menuDrawer = (DrawerLayout) findViewById(R.id.menuDrawe_Capital);
        //Obtener listview
        listaDrawer = (ListView) findViewById(R.id.lista_menu_drawer_CapitalIncial);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String capCapital = capital.getText().toString();

                if (capCapital.equals("")) {
                    Toast.makeText(CapitalInicial.this, "Escriba su Correo", Toast.LENGTH_SHORT).show();
                } else {
                    AbrirAlerta();
                }
            }
        });

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

        listaDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l)
            {
                switch (posicion)
                {
                    case 0:
                        Intent intent=new Intent(CapitalInicial.this,CapitalInicial.class);
                        startActivity(intent);
                        finish();
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

                menuDrawer.closeDrawer(listaDrawer);
            }
        });
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


    public void AbrirAlerta()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Guardar Capital Inicial?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CapitalInicial.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CapitalInicial.this, OlvidarContrasena.class);
                startActivity(intent);
            }
        });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capital_inicial, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
