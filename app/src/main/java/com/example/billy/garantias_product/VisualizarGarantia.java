package com.example.billy.garantias_product;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.billy.inversiones.R;

public class VisualizarGarantia extends AppCompatActivity
{
    String idGarantia = "";
    String tipo = "";

    EditText editTextNombreCliente_VGarantia;
    EditText editTextTelefonoCliente_VGarantia;
    EditText editTextNombreProducto_VGarantia;
    EditText editTextCantidadProducto_VGarantia;
    EditText editTextFecha_VGarantia;
    EditText editTextDescripcion_VGarantia;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_garantia);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        editTextNombreCliente_VGarantia = (EditText) findViewById(R.id.editTextNombreCliente_VGarantia);
        editTextTelefonoCliente_VGarantia = (EditText) findViewById(R.id.editTextTelefonoCliente_VGarantia);
        editTextNombreProducto_VGarantia = (EditText) findViewById(R.id.editTextNombreProducto_VGarantia);
        editTextCantidadProducto_VGarantia = (EditText) findViewById(R.id.editTextCantidadProducto_VGarantia);
        editTextFecha_VGarantia = (EditText) findViewById(R.id.editTextFecha_VGarantia);
        editTextDescripcion_VGarantia = (EditText) findViewById(R.id.editTextDescripcion_VGarantia);

        Bundle extra = getIntent().getExtras();
        idGarantia = extra.getString("idGarantia");
        tipo = extra.getString("tipo");

        switch (tipo)
        {
            case "Vendedor":

                for(int i = 0; i < Garantia.arrayListFiltroVendedor.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Garantia.arrayListFiltroVendedor.get(i).getIdGarantia()))
                    {
                        editTextNombreCliente_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getNombre());
                        editTextTelefonoCliente_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getTelefono());
                        editTextNombreProducto_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getNombreProducto());
                        editTextCantidadProducto_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getCantidad());
                        editTextFecha_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getFecha());
                        editTextDescripcion_VGarantia.setText(Garantia.arrayListFiltroVendedor.get(i).getDescripcion());
                    }
                }

                break;

            case "Todos":

                for(int i = 0; i < Garantia.arrayList.size(); i++)
                {
                    if(idGarantia.equalsIgnoreCase(Garantia.arrayList.get(i).getIdGarantia()))
                    {
                        editTextNombreCliente_VGarantia.setText(Garantia.arrayList.get(i).getNombre());
                        editTextTelefonoCliente_VGarantia.setText(Garantia.arrayList.get(i).getTelefono());
                        editTextNombreProducto_VGarantia.setText(Garantia.arrayList.get(i).getNombreProducto());
                        editTextCantidadProducto_VGarantia.setText(Garantia.arrayList.get(i).getCantidad());
                        editTextFecha_VGarantia.setText(Garantia.arrayList.get(i).getFecha());
                        editTextDescripcion_VGarantia.setText(Garantia.arrayList.get(i).getDescripcion());
                    }
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualizar_garantia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
