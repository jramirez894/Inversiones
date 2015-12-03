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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.billy.cancelados.Cancelados;
import com.example.billy.clientes.AgregarCliente;
import com.example.billy.clientes.DatosCobro;
import com.example.billy.clientes.ItemFactura_AgregarCliente;
import com.example.billy.clientes.ItemsCobro_AgregarCliente;
import com.example.billy.clientes.ItemsVenta_AgregarCliente;
import com.example.billy.clientes.VisualizarCliente;
import com.example.billy.constantes.Constantes;
import com.example.billy.devolucion.Devolucion;
import com.example.billy.empleado.AdapterListaEmpleado;
import com.example.billy.empleado.Empleados;
import com.example.billy.empleado.ItemListaEmpleado;
import com.example.billy.garantias_product.Garantia;
import com.example.billy.gastos.Reg_Gasto;
import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.inversiones.MainActivity;
import com.example.billy.inversiones.SesionUsuarios;
import com.example.billy.perfil.Perfil;
import com.example.billy.productos.AdapterListaProductos_Productos;
import com.example.billy.productos.ItemsListaProductos_Productos;
import com.example.billy.productos.Productos;
import com.example.billy.inversiones.R;
import com.example.billy.productos.V_Producto;
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
    public static ArrayList<String> itemsNombreCliente = new ArrayList<String>();

    MenuItem menuGuardar;
    MenuItem menuAgregar;
    MenuItem menuOrdenar;

    boolean existe = false;

    String respuesta = "";

    AutoCompleteTextView autocompleteBuscarClientes_MenuPrincipal;

    //Array que guardara todas las factura del servidor
    public static ArrayList<ItemFactura_AgregarCliente> itemsFactura = new ArrayList<ItemFactura_AgregarCliente>();

    //Variables para almacenar los datos del cliente que se van a visualizar

    //Tabla Cliente
    String cedulaCliente;
    String nombreCliente;
    String direccionCliente;
    String telefonoCliente;
    String correoCliente;
    String nombreEmpresaCliente;
    String direccionEmpresaCliente;
    String idClienteCliente;

    //Tabla Factura
    String idFactura;
    String fechaFactura;
    String totalFactura;
    String fechaCobroFactura;
    String diaCobroFactura;
    String horaCobroFactura;
    String idVendedorFactura;
    String idClienteFactura;

    //Tabla Venta
    public static ArrayList<ItemsVenta_AgregarCliente> itemsVenta = new ArrayList<ItemsVenta_AgregarCliente>();

    //Tabla Productos
    public static ArrayList<ItemsListaProductos_Productos> itemsProductos = new ArrayList<ItemsListaProductos_Productos>();

    //Tabla Usuarios
    String nombreVendedorUsuarios;

    //Tabla Cobro
    public static ArrayList<ItemsCobro_AgregarCliente> itemsCobro = new ArrayList<ItemsCobro_AgregarCliente>();

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

        final ArrayList<ItemsMenuDrawer> itemsdrawer = new ArrayList<ItemsMenuDrawer>();
        itemsdrawer.add(new ItemsMenuDrawer(titulos[0], R.mipmap.personas));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[1], R.mipmap.personas));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[2], R.mipmap.productos));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[3], R.mipmap.capital));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[4], R.mipmap.saldocaja));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[5], R.mipmap.cancelado));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[6], R.mipmap.garantia));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[7], R.mipmap.devolucion));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[8], R.mipmap.perfil));
        itemsdrawer.add(new ItemsMenuDrawer(titulos[9], R.mipmap.cerrar));

        // Relacionar el adaptador y la escucha de la lista del drawer
        listaDrawer.setAdapter(new AdapterMenuDrawer(this, itemsdrawer));

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

                ItemListaPersonalizada cliente = items.get(position);

                cedulaCliente = cliente.getCedula();
                nombreCliente = cliente.getNombreLista();
                direccionCliente = cliente.getDireccion();
                telefonoCliente = cliente.getTelefono();
                correoCliente = cliente.getCorreo();
                nombreEmpresaCliente = cliente.getNombreEmpresa();
                direccionEmpresaCliente = cliente.getDireccionEmpresa();
                idClienteCliente = cliente.getIdCliente();

                TareaGetBill tareaGetBill = new TareaGetBill();
                tareaGetBill.execute();
            }
        });

        //Listado de Vendedores
        TareaListado tareaListado = new TareaListado();
        tareaListado.execute();

        //Filtro de los clientes
        autocompleteBuscarClientes_MenuPrincipal = (AutoCompleteTextView) findViewById(R.id.autocompleteBuscarClientes_MenuPrincipal);

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
                    itemsNombreCliente.clear();
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        items.add(new ItemListaPersonalizada(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, "", obj.getString("idCliente"), obj.getString("cedula"), obj.getString("direccion"), obj.getString("telefono"), obj.getString("correo"), obj.getString("nombreEmpresa"), obj.getString("direccionEmpresa"), obj.getString("estado"), obj.getString("calificacion")));
                        itemsNombreCliente.add(obj.getString("nombre"));
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
            autocompleteBuscarClientes_MenuPrincipal.setAdapter(new ArrayAdapter<String>(PrincipalMenu.this, android.R.layout.simple_dropdown_item_1line, itemsNombreCliente));
        }
    }

    //Clases Asyntask para traer las facturas

    private class TareaGetBill extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerFactura.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllBill"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFacturas = objItems.getJSONArray(0);

                //String obj
                String respuesta= String.valueOf(objFacturas);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    itemsFactura.clear();
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);
                        itemsFactura.add(new ItemFactura_AgregarCliente(obj.getString("idFactura"), obj.getString("fecha"), obj.getString("total"), obj.getString("estado"), obj.getString("fechaCobro"), obj.getString("diaCobro"), obj.getString("horaCobro"), obj.getString("idVendedor"), obj.getString("idCliente")));
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(AgregarCliente.this, respStr.toString(), Toast.LENGTH_SHORT).show();
            if (existe)
            {
                for(int i = 0; i < itemsFactura.size(); i++)
                {
                    if(idClienteCliente.equalsIgnoreCase(itemsFactura.get(i).getIdCliente()))
                    {
                        idFactura = itemsFactura.get(i).getIdFactura();
                        fechaFactura= itemsFactura.get(i).getFecha();
                        totalFactura= itemsFactura.get(i).getTotal();
                        fechaCobroFactura= itemsFactura.get(i).getFechaCobro();
                        diaCobroFactura= itemsFactura.get(i).getDiaCobro();
                        horaCobroFactura= itemsFactura.get(i).getHoraCobro();
                        idVendedorFactura= itemsFactura.get(i).getIdVendedor();
                        idClienteFactura= itemsFactura.get(i).getIdCliente();

                        TareaGetSale tareaGetSale = new TareaGetSale();
                        tareaGetSale.execute();
                    }
                }
            }
        }
    }

    //Clases Asyntask para traer las ventas

    private class TareaGetSale extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerVenta.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option",  "getAllSale"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objFacturas = objItems.getJSONArray(0);

                //String obj
                String respuesta= String.valueOf(objFacturas);

                if(respuesta.equalsIgnoreCase("No Existen Ventas"))
                {
                    existe = false;
                }
                else
                {
                    itemsVenta.clear();
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);

                        if(idFactura.equalsIgnoreCase(obj.getString("idFactura")))
                        {
                            itemsVenta.add(new ItemsVenta_AgregarCliente(obj.getString("idVenta"),obj.getString("total"),obj.getString("cantidad"),obj.getString("estado"),obj.getString("idFactura"),obj.getString("idProducto")));
                        }

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(AgregarCliente.this, respStr.toString(), Toast.LENGTH_SHORT).show();
            if (existe)
            {
                TareaProductos tarea = new TareaProductos();
                tarea.execute();
            }
        }
    }

    //Clases Asyntask para traer los datos de la tabla productos

    private class TareaProductos extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerProducto.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllProduct"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);

                itemsProductos.clear();

                for(int i = 0; i < itemsVenta.size(); i++)
                {
                    for(int j=0; j<objVendedores.length(); j++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(j);

                        if(obj.getString("idProducto").equalsIgnoreCase(itemsVenta.get(i).getIdProducto()))
                        {
                            itemsProductos.add(new ItemsListaProductos_Productos(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idProducto"), obj.getString("descripcion"), obj.getString("cantidad"), obj.getString("precioCompra"), obj.getString("precioVenta"), obj.getString("idCategoria")));
                        }

                        resul = true;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(Productos.this, respStr, Toast.LENGTH_SHORT).show();
            if(existe)
            {
                TareaListadoVendedores tarea = new TareaListadoVendedores();
                tarea.execute();
            }
        }
    }

    //Clases Asyntask para listar los empleados que hay actualmente en el servidor

    private class TareaListadoVendedores extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "listUssers"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);
                //JSONObject obj = objItems.getJSONObject(0);

                //String obj
                respuesta= String.valueOf(objVendedores);

                if(respuesta.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        if(obj.getString("idUsuario").equalsIgnoreCase(idVendedorFactura))
                        {
                            nombreVendedorUsuarios = obj.getString("nombre");
                        }
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
            //Toast.makeText(Empleados.this, respuesta, Toast.LENGTH_SHORT).show();
        }
    }

    //Clases Asyntask para traer los datos de la tabla productos

    private class TareaCobro extends AsyncTask<String,Integer,Boolean>
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCobro.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", "getAllCharge"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");
                JSONArray objVendedores = objItems.getJSONArray(0);

                itemsCobro.clear();

                for(int j=0; j<objVendedores.length(); j++)
                {
                    JSONObject obj = objVendedores.getJSONObject(j);

                    if(obj.getString("idFactura").equalsIgnoreCase(idFactura))
                    {
                        itemsCobro.add(new ItemsCobro_AgregarCliente(obj.getString("idCobro"),obj.getString("fecha"),obj.getString("abono"),obj.getString("idVendedor"),obj.getString("idFactura")));
                    }

                    resul = true;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            //Toast.makeText(Productos.this, respStr, Toast.LENGTH_SHORT).show();
            if(existe)
            {
                TareaListadoVendedores tarea = new TareaListadoVendedores();
                tarea.execute();
            }
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
