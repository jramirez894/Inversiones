package com.example.billy.inversiones;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.menu_principal.PrincipalMenu;

public class MainActivity extends AppCompatActivity
{
    EditText cedula;
    EditText contrasena;

    Button ingresar;
    TextView olvidar;

    String ced1 = "1088336137";
    String contra1 = "1234";

    String ced2 = "1088336137";
    String contra2 ="12345";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();

        cedula =(EditText)findViewById(R.id.editTextCedula);
        contrasena =(EditText)findViewById(R.id.editTextContrasena);
        ingresar=(Button)findViewById(R.id.buttonLogin);
        olvidar=(TextView)findViewById(R.id.texViewOlvidarContrasena);

        ingresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String capCedula = cedula.getText().toString();
                String capContrase = contrasena.getText().toString();

                if (capCedula.equals("")||
                        capContrase.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Faltan Datos Por LLenar", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (capCedula.equals(ced1) && capContrase.equals(contra1))
                    {
                        Intent intent=new Intent(MainActivity.this,PrincipalMenu.class);
                        startActivity(intent);
                        finish();
                    }

                    if (capCedula.equals(ced2) && capContrase.equals(contra2))
                    {
                        Intent intent=new Intent(MainActivity.this,PrincipalEmpleado.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Porfavor Verificar Datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        olvidar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,OlvidarContrasena.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
