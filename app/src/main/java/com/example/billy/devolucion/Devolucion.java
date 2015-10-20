package com.example.billy.devolucion;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class Devolucion extends AppCompatActivity
{
    ListView  listaDevolucion;
    ArrayList<Items_Devolucion>arrayList = new ArrayList<Items_Devolucion>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        listaDevolucion =(ListView)findViewById(R.id.listView_Devolucion);
        ActualizarLista();
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new Items_Devolucion("Cliente: Miguel", "Producto: Sabanas", "Fecha: 20-10-2015", "Descripcion: Que pereza", R.mipmap.eliminar));
        arrayList.add(new Items_Devolucion("Cliente: Jeniffer", "Producto: Sabanas", "Fecha: 20-10-2015", "Descripcion: Que pereza", R.mipmap.eliminar));

        listaDevolucion.setAdapter(new Adapter_Devolucion(this,arrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_devolucion, menu);
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
