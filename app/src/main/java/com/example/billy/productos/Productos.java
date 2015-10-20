package com.example.billy.productos;

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

public class Productos extends AppCompatActivity
{
    AutoCompleteTextView buscarProducto;
    ImageView buscar;
    public static ListView listaProductos;
    public static ArrayList<ItemsListaProductos_Productos> arrayList = new ArrayList<ItemsListaProductos_Productos>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        buscarProducto = (AutoCompleteTextView)findViewById(R.id.autocompleteBuscarProducto_Producto);
        buscar = (ImageView)findViewById(R.id.imgBuscarProducto_Producto);
        listaProductos = (ListView)findViewById(R.id.listaProductos_Producto);
        ActualizarLista();

        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Intent intent = new Intent(Productos.this, V_Producto.class);
                startActivity(intent);
            }
        });
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemsListaProductos_Productos("Toallas", R.mipmap.editar, R.mipmap.eliminar));
        listaProductos.setAdapter(new AdapterListaProductos_Productos(this, arrayList));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_productos, menu);
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
            case R.id.agregarProducto_Productos:
                Intent intent = new Intent(Productos.this,AgregarProducto.class);
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
