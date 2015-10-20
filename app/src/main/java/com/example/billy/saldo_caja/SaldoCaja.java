package com.example.billy.saldo_caja;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.billy.constantes.Constantes;
import com.example.billy.gastos.Historial;
import com.example.billy.inversiones.R;

public class SaldoCaja extends AppCompatActivity
{
    TextView gastoDia;
    TextView cobroDia;
    TextView diferencia;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_caja);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        gastoDia = (TextView)findViewById(R.id.texViewGastoDia_SaldoCaja);
        cobroDia = (TextView)findViewById(R.id.texViewCobroDia_SaldoCaja);
        diferencia = (TextView)findViewById(R.id.texViewDiferencia_SaldoCaja);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saldo_caja, menu);
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
            case R.id.historial_SaldoCaja:
                Constantes.atrasHistorial="SaldoCaja";

                Intent intent = new Intent(SaldoCaja.this,Historial.class);
                intent.putExtra("Atras",Constantes.atrasHistorial);
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
