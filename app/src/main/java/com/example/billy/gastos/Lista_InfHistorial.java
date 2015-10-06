package com.example.billy.gastos;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class Lista_InfHistorial extends AppCompatActivity
{
    TextView titulo;
    ArrayList<ItemLista_InfHistorial> arrayList = new ArrayList<ItemLista_InfHistorial>();
    ListView listaInf;
    String txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__inf_historial);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        Bundle bundle = getIntent().getExtras();
        txtTitulo = bundle.getString("Titulo");

        titulo = (TextView)findViewById(R.id.texviewDetalle_ListaInfHistorial);
        listaInf = (ListView)findViewById(R.id.lisViewListaInfHistorial);

        ActualizarLista();

        switch (txtTitulo)
        {
            case "Renta":
                titulo.setText("Detalle Renta");
                break;

            case "Servicios":
                titulo.setText("Detalle Sevicios");
                break;

            case "Empleados":
                titulo.setText("Detalle Empleados");
                break;

            case "Gasolina":
                titulo.setText("Detalle Gasolina");
                break;

            case "Comida":
                titulo.setText("Detalle Comida");
                break;
        }
    }

    public void ActualizarLista()
    {
        arrayList.clear();

        arrayList.add(new ItemLista_InfHistorial("Descripcion: " + "Se gasto Mucho", "Valor: " + "20.000", "Gasolina"));

        listaInf.setAdapter(new Adapter_InfHistorial(Lista_InfHistorial.this,arrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista__inf_historial, menu);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
