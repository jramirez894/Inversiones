package com.example.billy.gastos;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.constantes.Constantes;
import com.example.billy.inversiones.R;

import java.util.ArrayList;

public class Inf_Historial extends AppCompatActivity
{
    TextView rango;

    TextView renta;
    TextView servicios;
    TextView empleados;
    TextView gasolina;
    TextView comida;

    ImageView imaRenta;
    ImageView imaServicios;
    ImageView imaEmpleados;
    ImageView imaGasolina;
    ImageView imaComida;
    Intent intent;

    public static ArrayList<ItemLista_InfHistorial> arrayList = new ArrayList<ItemLista_InfHistorial>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf__historial);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();


        rango = (TextView)findViewById(R.id.textViewRango_InfHistorial);
        renta = (TextView)findViewById(R.id.textViewRenta_InfHistorial);
        servicios = (TextView)findViewById(R.id.textViewServicios_InfHistorial);
        empleados = (TextView)findViewById(R.id.textViewPagoEmpleados_InfHistorial);
        gasolina = (TextView)findViewById(R.id.textViewGasolina_InfHistorial);
        comida = (TextView)findViewById(R.id.textViewComida_InfHistorial);

        imaRenta = (ImageView)findViewById(R.id.imageInfRenta_InfHistorial);
        imaServicios = (ImageView)findViewById(R.id.imageInfServicios_InfHistorial);
        imaEmpleados = (ImageView)findViewById(R.id.imageInfEmpleados_InfHistorial);
        imaGasolina = (ImageView)findViewById(R.id.imageInfGasolina_InfHistorial);
        imaComida = (ImageView)findViewById(R.id.imageInfComida_InfHistorial);

        rango.setText(Constantes.fechaInicio + " // " + Constantes.fechaFin);



        imaRenta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                arrayList.clear();

                String idTipoGasto = "";

                for(int i = 0; i < Reg_Gasto.arrayListTipoGasto.size(); i++)
                {
                    if("Renta".equalsIgnoreCase(Reg_Gasto.arrayListTipoGasto.get(i).getNombre()))
                    {
                        idTipoGasto = Reg_Gasto.arrayListTipoGasto.get(i).getIdTipoGasto();
                    }
                }

                for(int i = 0; i < Historial.arrayList.size(); i++)
                {
                    if(idTipoGasto.equalsIgnoreCase(Historial.arrayList.get(i).getTipoGasto()))
                    {
                        arrayList.add(new ItemLista_InfHistorial("Descripción: " + Historial.arrayList.get(i).getDescripcion(), "Valor: " +  Historial.arrayList.get(i).getValor(), "Renta", R.mipmap.informacion));
                    }
                }

                intent = new Intent(Inf_Historial.this,Lista_InfHistorial.class);
                intent.putExtra("Titulo","Renta");
                startActivity(intent);
            }
        });

        imaServicios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                arrayList.clear();

                String idTipoGasto = "";

                for(int i = 0; i < Reg_Gasto.arrayListTipoGasto.size(); i++)
                {
                    if("Servicios".equalsIgnoreCase(Reg_Gasto.arrayListTipoGasto.get(i).getNombre()))
                    {
                        idTipoGasto = Reg_Gasto.arrayListTipoGasto.get(i).getIdTipoGasto();
                    }
                }

                for(int i = 0; i < Historial.arrayList.size(); i++)
                {
                    if(idTipoGasto.equalsIgnoreCase(Historial.arrayList.get(i).getTipoGasto()))
                    {
                        arrayList.add(new ItemLista_InfHistorial("Descripción: " + Historial.arrayList.get(i).getDescripcion(), "Valor: " +  Historial.arrayList.get(i).getValor(), "Servicios", R.mipmap.informacion));
                    }
                }

                intent = new Intent(Inf_Historial.this,Lista_InfHistorial.class);
                intent.putExtra("Titulo","Servicios");
                startActivity(intent);
            }
        });

        imaEmpleados.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                arrayList.clear();

                String idTipoGasto = "";

                for(int i = 0; i < Reg_Gasto.arrayListTipoGasto.size(); i++)
                {
                    if("Empleados".equalsIgnoreCase(Reg_Gasto.arrayListTipoGasto.get(i).getNombre()))
                    {
                        idTipoGasto = Reg_Gasto.arrayListTipoGasto.get(i).getIdTipoGasto();
                    }
                }

                for(int i = 0; i < Historial.arrayList.size(); i++)
                {
                    if(idTipoGasto.equalsIgnoreCase(Historial.arrayList.get(i).getTipoGasto()))
                    {
                        arrayList.add(new ItemLista_InfHistorial("Descripción: " + Historial.arrayList.get(i).getDescripcion(), "Valor: " +  Historial.arrayList.get(i).getValor(), "Empleados", R.mipmap.informacion));
                    }
                }

                intent = new Intent(Inf_Historial.this,Lista_InfHistorial.class);
                intent.putExtra("Titulo","Empleados");
                startActivity(intent);
            }
        });

        imaGasolina.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(Inf_Historial.this,Lista_InfHistorial.class);
                intent.putExtra("Titulo","Gasolina");
                startActivity(intent);
            }
        });

        imaComida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(Inf_Historial.this,Lista_InfHistorial.class);
                intent.putExtra("Titulo","Comida");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inf__historial, menu);
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
