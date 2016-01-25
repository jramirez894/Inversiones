package com.example.billy.productos;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.billy.inversiones.R;

public class V_Producto extends AppCompatActivity
{
    TextView nombre;
    TextView categoria;
    TextView descripcion;
    TextView cantidad;
    TextView garantia;
    TextView precioCompra;
    TextView precioVenta;

    String nom = "";
    String idC = "";
    String des = "";
    String can = "";
    String gar = "";
    String preC = "";
    String preV = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v__producto);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        nombre = (TextView)findViewById(R.id.textNombreVProducto);
        categoria = (TextView)findViewById(R.id.textCategoria_VProducto);
        descripcion = (TextView)findViewById(R.id.textDescripcion_VProducto);
        cantidad = (TextView)findViewById(R.id.textCantidad_VProducto);
        garantia = (TextView)findViewById(R.id.textGarantia_VProducto);
        precioCompra = (TextView)findViewById(R.id.textPrecioCompra_VProducto);
        precioVenta = (TextView)findViewById(R.id.textPrecioVenta_VProducto);

        Bundle extra = getIntent().getExtras();
        nom = extra.getString("nombre");
        idC = extra.getString("idCategoria");
        des = extra.getString("descripcion");
        can = extra.getString("cantidad");
        gar = extra.getString("garantia");
        preC = extra.getString("precioCompra");
        preV = extra.getString("precioVenta");

        nombre.setText("Nombre: " + nom);
        categoria.setText("Categoria: " + idC);
        descripcion.setText("Descripcion: " + des);
        cantidad.setText("Cantidad: " + can);
        garantia.setText("Garantia: " + gar);
        precioCompra.setText("Precio Compra: " + preC);
        precioVenta.setText("Precio Venta: " + preV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_v__producto, menu);
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
