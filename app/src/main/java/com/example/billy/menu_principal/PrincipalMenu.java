package com.example.billy.menu_principal;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.billy.cancelados.Cancelados;
import com.example.billy.clientes.AgregarCliente;
import com.example.billy.clientes.VisualizarCliente;
import com.example.billy.constantes.Constantes;
import com.example.billy.devolucion.Devolucion;
import com.example.billy.empleado.Empleados;
import com.example.billy.garantias_product.Garantia;
import com.example.billy.gastos.Reg_Gasto;
import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.inversiones.MainActivity;
import com.example.billy.inversiones.SesionUsuarios;
import com.example.billy.perfil.Perfil;
import com.example.billy.productos.Productos;
import com.example.billy.inversiones.R;
import com.example.billy.saldo_caja.SaldoCaja;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PrincipalMenu extends AppCompatActivity
{
    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle toggle;
    ListView listaDrawer;

    public static ListView listaClientes;
    public static ArrayList<ItemListaPersonalizada> items = new ArrayList<ItemListaPersonalizada>();

    MenuItem menuGuardar;
    MenuItem menuAgregar;
    MenuItem menuOrdenar;

    boolean existe = false;

    String respuesta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_menu);

        Constantes.EDITAR_LISTA = "Botones";

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.invalidateOptionsMenu();
        actionBar.setTitle("Menu");


        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);

        //Obtener drawer
        menuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Obtener listview
        listaDrawer = (ListView) findViewById(R.id.lista_menu_drawer);

        listaClientes=(ListView)findViewById(R.id.listaClienes_MenuPrincipal);

        String[] titulos = getResources().getStringArray(R.array.array_menu_drawer);

        ArrayList<ItemsMenuDrawer> items = new ArrayList<ItemsMenuDrawer>();
        items.add(new ItemsMenuDrawer(titulos[0], R.mipmap.personas));
        items.add(new ItemsMenuDrawer(titulos[1], R.mipmap.personas));
        items.add(new ItemsMenuDrawer(titulos[2], R.mipmap.productos));
        items.add(new ItemsMenuDrawer(titulos[3], R.mipmap.capital));
        items.add(new ItemsMenuDrawer(titulos[4], R.mipmap.saldocaja));
        items.add(new ItemsMenuDrawer(titulos[5], R.mipmap.cancelado));
        items.add(new ItemsMenuDrawer(titulos[6], R.mipmap.garantia));
        items.add(new ItemsMenuDrawer(titulos[7], R.mipmap.devolucion));
        items.add(new ItemsMenuDrawer(titulos[8], R.mipmap.perfil));
        items.add(new ItemsMenuDrawer(titulos[9], R.mipmap.cerrar));

        // Relacionar el adaptador y la escucha de la lista del drawer
        listaDrawer.setAdapter(new AdapterMenuDrawer(this, items));

        //Activar icono del menu que se despliega
        toggle = new ActionBarDrawerToggle(this, menuDrawer, R.string.drawer_inicio, R.string.drawer_fin);

        listaDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l)
            {
                Intent intent;
                switch (posicion)
                {
                    case 0:
                        intent = new Intent(PrincipalMenu.this, Empleados.class);
                        startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(PrincipalMenu.this, PrincipalMenu.class);
                        startActivity(intent);
                        finish();
                        break;

                    case 2:
                        intent = new Intent(PrincipalMenu.this, Productos.class);
                        startActivity(intent);

                        break;

                    case 3:
                        intent = new Intent(PrincipalMenu.this, Reg_Gasto.class);
                        startActivity(intent);

                        break;

                    case 4:
                        intent = new Intent(PrincipalMenu.this, SaldoCaja.class);
                        startActivity(intent);

                        break;

                    case 5:
                        intent = new Intent(PrincipalMenu.this, Cancelados.class);
                        startActivity(intent);

                        break;

                    case 6:
                        intent = new Intent(PrincipalMenu.this, Garantia.class);
                        startActivity(intent);

                        break;

                    case 7:
                        intent = new Intent(PrincipalMenu.this, Devolucion.class);
                        startActivity(intent);

                        break;

                    case 8:
                        intent = new Intent(PrincipalMenu.this, Perfil.class);
                        startActivity(intent);

                        break;

                    case 9:
                        intent = new Intent(PrincipalMenu.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                menuDrawer.closeDrawer(listaDrawer);
            }
        });

        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                long cap = listaClientes.getItemIdAtPosition(position);
                Intent intent = new Intent(PrincipalMenu.this, VisualizarCliente.class);
                intent.putExtra("Posicion",cap);
                startActivity(intent);
            }
        });

        //Listado de Vendedores
        TareaListado tareaListado = new TareaListado();
        tareaListado.execute();
    }

    public void ActualizarLista()
    {
        //items.clear();
        //items.add(new ItemListaPersonalizada("jeniffer",R.mipmap.editar,R.mipmap.eliminar, ""));
        //items.add(new ItemListaPersonalizada("miguel", R.mipmap.editar, R.mipmap.eliminar, ""));

        listaClientes.setAdapter(new AdapterListaPersonalizada(PrincipalMenu.this, items));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_menu, menu);

        //Elementos del menu item
        menuGuardar = (MenuItem) menu.findItem(R.id.guardarCliente);
        menuAgregar = (MenuItem) menu.findItem(R.id.agregarCliente);
        menuOrdenar = (MenuItem) menu.findItem(R.id.ordenarCliente);

        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item)
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

        switch (item.getItemId())
        {
            case R.id.agregarCliente:
                Intent intent = new Intent(PrincipalMenu.this,AgregarCliente.class);
                intent.putExtra("Interfaz","Administrador");
                startActivity(intent);
                break;

            case R.id.ordenarCliente:
                Constantes.EDITAR_LISTA = "EditText";
                ActualizarLista();
                menuGuardar.setVisible(true);
                menuAgregar.setVisible(false);
                menuOrdenar.setVisible(false);

                break;

            case R.id.guardarCliente:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_menu_save);
                builder.setTitle("Guardar");
                builder.setMessage("¿Confirmar?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        menuGuardar.setVisible(false);
                        menuAgregar.setVisible(true);
                        menuOrdenar.setVisible(true);

                        Constantes.EDITAR_LISTA = "Botones";
                        ActualizarLista();
                    }
                });

                builder.setNegativeButton("Cancelar", null);
                builder.setCancelable(false);
                builder.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Clases Asyntask para traer los clientes

    private class TareaListado extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            boolean resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCliente.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);

                //String obj
                respuesta= String.valueOf(objVendedores);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    items.clear();
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        items.add(new ItemListaPersonalizada(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, ""));
                        existe = true;
                    }
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(PrincipalMenu.this, respuesta, Toast.LENGTH_SHORT).show();
            listaClientes.setAdapter(new AdapterListaPersonalizada(PrincipalMenu.this, items));
        }
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
