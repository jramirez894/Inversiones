package com.example.billy.garantias_product;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class Garantia extends AppCompatActivity
{
    public static ListView listaGarantia;
    public static ArrayList<Items_Garantia> arrayList = new ArrayList<Items_Garantia>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garantia);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        listaGarantia=(ListView)findViewById(R.id.listView_Garantia);
        ActualizarLista();
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new Items_Garantia("Cliente: Miguel", "Producto: Sabanas", "Fecha: 20-10-2015", "Descripcion: Que pereza", R.mipmap.eliminar));
        arrayList.add(new Items_Garantia("Cliente: Colson", "Producto: Sabanas", "Fecha: 20-10-2015", "Descripcion: Que pereza", R.mipmap.eliminar));

        listaGarantia.setAdapter(new Adapter_Garantia(this, arrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_garantia, menu);
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
