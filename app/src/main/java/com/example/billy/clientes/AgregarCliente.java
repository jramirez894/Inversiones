package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.billy.empleado.Empleados;
import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.menu_principal.AdapterListaPersonalizada;
import com.example.billy.menu_principal.ItemListaPersonalizada;
import com.example.billy.menu_principal.PagerAdapter;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.productos.Productos;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgregarCliente extends ActionBarActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, AgregarCliente.TabInfo>();
    private PagerAdapter mPagerAdapter;
    /**
     *
     * @author mwho
     * Maintains extrinsic info of a tab's construct
     */

    //Variables del tab Datos Personales
    String cedula="";
    String nombre="";
    String direccion="";
    String telefono="";
    String correo="";
    String nomEmpresa="";
    String dirEmpresa="";

    //Variables del tab DatosCobro
    String buscarProducto="";
    String fechaVenta="";
    String totalPagar="";
    String abono="";
    String valorRestante="";

    //Variables del tab DetalleCobro
    String nomEmpleado="";
    String fechaCobro="";
    String horaCobro="";

    //Varibales createClient
    boolean resul;
    Object respuesta = "";

    //Variable para update producto
    String respuestaProducto = "";

    //Variables listadoClientes
    boolean existe = false;
    public static ArrayList<ItemListaPersonalizada> items = new ArrayList<ItemListaPersonalizada>();

    //Variable para saber el id de la factura que se creo
    String idFactura = "";

    //variable para saber si hay que insertar un abono o no
    boolean insertarAbono;

    String idClienteUpdate = "";

    //Alerta Cargando
    ProgressDialog progressDialog;

    private class TabInfo
    {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabHost.TabContentFactory
    {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag)
        {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    String interfaz ="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        Bundle bundle = getIntent().getExtras();
        interfaz = bundle.getString("Interfaz");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        this.initialiseTabHost(savedInstanceState);

        // Intialise ViewPager
        this.intialiseViewPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        /*if (id == android.R.id.home)
        {
            Intent intent;
           switch (interfaz)
               {
                   case "Administrador":
                       intent = new Intent(AgregarCliente.this, PrincipalMenu.class);
                       startActivity(intent);
                       finish();
                       break;

                   case "Empleado":
                       intent = new Intent(AgregarCliente.this, PrincipalEmpleado.class);
                       startActivity(intent);
                       finish();
                       break;

               }
        }*/

        try
        {
            switch (item.getItemId())
            {
                case R.id.guardarCliente_AgregarCliente:

                    //Variables Asociadas tab DatosPersonales
                    cedula = DatosPersonales.buscarCedula.getText().toString();
                    nombre = DatosPersonales.nom.getText().toString();
                    direccion = DatosPersonales.direccion.getText().toString();
                    telefono = DatosPersonales.telefono.getText().toString();
                    correo = DatosPersonales.correo.getText().toString();
                    nomEmpresa = DatosPersonales.nomEmpresa.getText().toString();
                    dirEmpresa = DatosPersonales.dircEmpresa.getText().toString();

                    //Variables Asociadas tab DatosCobro
                    fechaVenta =DatosCobro.fechaVenta.getText().toString();
                    totalPagar =DatosCobro.totalPagar.getText().toString();
                    abono =DatosCobro.abono.getText().toString();
                    valorRestante =DatosCobro.valorRestante.getText().toString();

                    //Variables Asociadas tab DetalleCobro
                    nomEmpleado =DetalleCobro.buscarEmpleado.getText().toString();
                    fechaCobro = DetalleCobro.fechaDeCobro.getText().toString();
                    horaCobro = DetalleCobro.horaCobro.getSelectedItem().toString();

                    if (cedula.equals("")||
                            nombre.equals("")||
                            direccion.equals("")||
                            telefono.equals("")||
                            correo.equals("")||
                            nomEmpresa.equals("")||
                            dirEmpresa.equals("")||
                            fechaVenta.equals("")||
                            totalPagar.equals("")||
                            nomEmpleado.equals("")||
                            fechaCobro.equals("")||
                            horaCobro.equalsIgnoreCase("Hora de Cobro"))
                    {
                        Toast.makeText(AgregarCliente.this,"Faltan Datos Por Llenar",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String fechaActual = sdf.format(new Date());

                        if(fechaActual.equalsIgnoreCase(fechaCobro))
                        {
                            Toast.makeText(AgregarCliente.this,"La fecha del proximo cobro no puede ser la actual",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(DatosCobro.arrayListItems.isEmpty())
                            {
                                Toast.makeText(AgregarCliente.this,"Debe añadir productos a la lista",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(isEmailValid(correo))
                                {
                                    guardarCliente(cedula, nombre, direccion, telefono, correo, nomEmpresa, dirEmpresa);
                                }
                                else
                                {
                                    Toast.makeText(AgregarCliente.this,"Debe Ingresar un Correo Valido",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(AgregarCliente.this,"Faltan Datos Por Llenar",Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    protected void onResume(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, DatosPersonales.class.getName()));
        fragments.add(Fragment.instantiate(this, DatosCobro.class.getName()));
        fragments.add(Fragment.instantiate(this, DetalleCobro.class.getName()));

        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args)
    {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;

        AgregarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("", getResources().getDrawable(R.mipmap.perfil)), ( tabInfo = new TabInfo("Tab1", DatosPersonales.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        AgregarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("", getResources().getDrawable(R.mipmap.capital)), (tabInfo = new TabInfo("Tab2", DatosCobro.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        AgregarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("", getResources().getDrawable(R.mipmap.capital)), (tabInfo = new TabInfo("Tab2", DetalleCobro.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
       mTabHost.setOnTabChangedListener(this);
    }


    private static void AddTab(AgregarCliente activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag)
    {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position)
    {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state)
    {
        // TODO Auto-generated method stub

    }

    //Alerta de Confirmacion
    public void guardarCliente(final String cedula, final String nombre, final String direccion, final String telefono, final String correo, final String nombreEmpresa, final String direccionEmpresa)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Agregar Cliente?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Para saber si el cliente ya se agrego
                boolean createClient = false;

                //Para informarle al usuario que no se puede agregar el cliente seleccionado
                boolean stop = false;


                //Determinar si el cliente esta activo o inactivo y notificarle al usuario
                String cedula = DatosPersonales.buscarCedula.getText().toString();
                String estado = "";
                String mensaje = "";

                String calificacion = "";

                for(int i = 0; i < DatosPersonales.arrayListDatosClientes.size(); i++)
                {
                    if(cedula.equalsIgnoreCase(DatosPersonales.arrayListDatosClientes.get(i).getCedula()))
                    {
                        estado = DatosPersonales.arrayListDatosClientes.get(i).getEstado();
                        idClienteUpdate = DatosPersonales.arrayListDatosClientes.get(i).getIdCliente();
                        calificacion = DatosPersonales.arrayListDatosClientes.get(i).getCalificacion();
                    }
                }

                if(estado.equalsIgnoreCase(""))
                {
                    createClient = true;

                    TareaCreateCient createCient = new TareaCreateCient();
                    createCient.execute(cedula, nombre, direccion, telefono, correo, nombreEmpresa, direccionEmpresa);

                    AlertaCargando();
                }
                else
                {
                    if(estado.equalsIgnoreCase("Activo"))
                    {
                        for(int i = 0; i < DatosPersonales.itemsFactura.size(); i++)
                        {
                            if(idClienteUpdate.equalsIgnoreCase(DatosPersonales.itemsFactura.get(i).getIdCliente()))
                            {
                                if(DatosPersonales.itemsFactura.get(i).getEstado().equalsIgnoreCase("Activo"))
                                {
                                    mensaje = "Este cliente ya tiene una factura activa, no se pueden tener dos facturas al tiempo debe modificar la factura existente.";
                                    AlertaEstadoCliente(mensaje, "Activo con Factura");
                                    stop = true;
                                }
                            }
                        }

                        if(mensaje.equalsIgnoreCase(""))
                        {
                            DatosPersonales.updateCliente = true;
                        }
                    }
                    else
                    {
                        if(estado.equalsIgnoreCase("Inactivo"))
                        {
                            DatosPersonales.updateCliente = true;
                        }
                    }
                }

                if(!stop)
                {
                    if(DatosPersonales.updateCliente)
                    {
                        TareaUpdateClient tareaUpdateClient = new TareaUpdateClient();
                        tareaUpdateClient.execute(idClienteUpdate, cedula, nombre, direccion, telefono, correo, nombreEmpresa,
                                direccionEmpresa, "Activo", calificacion);

                        AlertaCargando();
                    }
                    else
                    {
                        if(!createClient)
                        {
                            TareaCreateCient createCient = new TareaCreateCient();
                            createCient.execute(cedula, nombre, direccion, telefono, correo, nombreEmpresa, direccionEmpresa);

                            AlertaCargando();
                        }
                    }
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    public void AlertaCargando()
    {
        //Alerta que carga mientras se cargan los Clientes
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
    }

    public void AlertaEstadoCliente(String mensaje, final String clic)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AgregarCliente.this);
        builder.setIcon(R.mipmap.perfil);
        builder.setTitle("Estado del Cliente");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (clic)
                {
                    case "Inactivo":

                        break;

                    case "Activo con Factura":


                        break;
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
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

    //Clases Asyntask para agregar un cliente
    private class TareaCreateCient extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;

            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCliente.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("cedula", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("direccion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[3]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[4]));
            nameValuePairs.add(new BasicNameValuePair("nombreEmpresa", params[5]));
            nameValuePairs.add(new BasicNameValuePair("direccionEmpresa", params[6]));
            nameValuePairs.add(new BasicNameValuePair("estado", "Activo"));
            nameValuePairs.add(new BasicNameValuePair("calificacion", ""));
            nameValuePairs.add(new BasicNameValuePair("option", "createClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= respJSON.get("items");
                resul = true;

                existe = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (existe)
            {
                TareaListado listado = new TareaListado();
                listado.execute();
            }
            else
            {
                Toast.makeText(AgregarCliente.this, "Error al Crear el Cliente", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
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
                String respuesta= String.valueOf(objVendedores);

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
                        items.add(new ItemListaPersonalizada(obj.getString("nombre"), R.mipmap.editar, "", obj.getString("idCliente"), obj.getString("cedula"), obj.getString("direccion"), obj.getString("telefono"), obj.getString("correo"), obj.getString("nombreEmpresa"), obj.getString("direccionEmpresa"), obj.getString("estado"), obj.getString("calificacion")));
                        existe = true;
                    }
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (existe)
            {
                String idCliente = "";
                //Toast.makeText(PrincipalMenu.this, respuesta, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < items.size(); i++)
                {
                    if(nombre.equalsIgnoreCase(items.get(i).getNombreLista()))
                    {
                        idCliente = items.get(i).getIdCliente();
                    }
                }

                Date date = new Date();
                DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                String fechaVentaModificada = fechaVenta + " " + hourFormat.format(date);

                if(abono.equalsIgnoreCase(""))
                {
                    if(DatosPersonales.updateCliente)
                    {
                        TareaCreateBill createBill = new TareaCreateBill();
                        createBill.execute(fechaVentaModificada, totalPagar, totalPagar, fechaCobro, horaCobro, DetalleCobro.idVendedor, idClienteUpdate);
                        insertarAbono = false;
                    }
                    else
                    {
                        TareaCreateBill createBill = new TareaCreateBill();
                        createBill.execute(fechaVentaModificada, totalPagar, totalPagar, fechaCobro, horaCobro, DetalleCobro.idVendedor, idCliente);
                        insertarAbono = false;
                    }
                }
                else
                {
                    if(DatosPersonales.updateCliente)
                    {
                        TareaCreateBill createBill = new TareaCreateBill();
                        createBill.execute(fechaVentaModificada, totalPagar, valorRestante, fechaCobro, horaCobro, DetalleCobro.idVendedor, idClienteUpdate);
                        insertarAbono = true;
                    }
                    else
                    {
                        TareaCreateBill createBill = new TareaCreateBill();
                        createBill.execute(fechaVentaModificada, totalPagar, valorRestante, fechaCobro, horaCobro, DetalleCobro.idVendedor, idCliente);
                        insertarAbono = true;
                    }
                }
            }
            else
            {
                Toast.makeText(AgregarCliente.this, "Error al Crear el Cliente", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    //Clases Asyntask para agregar una factura
    private class TareaCreateBill extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;

            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerFactura.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("fecha", params[0]));
            nameValuePairs.add(new BasicNameValuePair("total", params[1]));
            nameValuePairs.add(new BasicNameValuePair("valorRestante", params[2]));
            nameValuePairs.add(new BasicNameValuePair("estado", "Activo"));
            nameValuePairs.add(new BasicNameValuePair("fechaCobro", params[3]));
            nameValuePairs.add(new BasicNameValuePair("horaCobro", params[4]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[5]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[6]));
            nameValuePairs.add(new BasicNameValuePair("option", "createBill"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= respJSON.get("items");
                resul = true;
                existe = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (existe)
            {
                TareaListadoBill listadoBill = new TareaListadoBill();
                listadoBill.execute();
            }
            else
            {
                Toast.makeText(AgregarCliente.this, "Error al Crear el Cliente", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }
    }

    //Clases Asyntask para traer las facturas
    private class TareaListadoBill extends AsyncTask<String,Integer,Boolean>
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
                    for(int i=0; i<objFacturas.length(); i++)
                    {
                        JSONObject obj = objFacturas.getJSONObject(i);
                        idFactura = obj.getString("idFactura");
                        existe = true;
                    }
                }

                resul = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (existe)
            {
                for (int i = 0; i < DatosCobro.arrayListItems.size(); i++)
                {
                    String nombre = DatosCobro.arrayListItems.get(i).getNomProducto();
                    int precioProducto = 0;
                    int posicionProducto = 0;

                    for (int j = 0; j < DatosCobro.arrayList.size(); j++)
                    {
                        if(nombre.equalsIgnoreCase(DatosCobro.arrayList.get(j).getNombre()))
                        {
                            precioProducto = Integer.valueOf(DatosCobro.arrayList.get(j).getPrecioVenta());
                            posicionProducto = j;
                        }
                    }

                    int totalPorProducto = 0;

                    totalPorProducto = Integer.valueOf(DatosCobro.arrayListItems.get(i).getCantidad()) * precioProducto;

                    TareaCreateSale createSale = new TareaCreateSale();
                    createSale.execute(String.valueOf(totalPorProducto), DatosCobro.arrayListItems.get(i).getCantidad(), "En Venta", idFactura, DatosCobro.arrayList.get(posicionProducto).getIdProducto());
                }

                //Descontar la cantidad de productos
                for (int i = 0; i < DatosCobro.arrayListItems.size(); i++)
                {
                    String nombre = DatosCobro.arrayListItems.get(i).getNomProducto();
                    String cantidadBD = "";
                    String cantidadRegistrada = DatosCobro.arrayListItems.get(i).getCantidad();

                    for (int j = 0; j < DatosCobro.arrayList.size(); j++)
                    {
                        if(nombre.equalsIgnoreCase(DatosCobro.arrayList.get(j).getNombre()))
                        {
                            cantidadBD = DatosCobro.arrayList.get(j).getCantidad();
                            int resta = Integer.valueOf(cantidadBD) - Integer.valueOf(cantidadRegistrada);

                            TareaUpdadteProducto tareaUpdadteProducto = new TareaUpdadteProducto();
                            tareaUpdadteProducto.execute(DatosCobro.arrayList.get(j).getIdProducto(),
                                    DatosCobro.arrayList.get(j).getNombre(),
                                    DatosCobro.arrayList.get(j).getDescripcion(),
                                    String.valueOf(resta),
                                    DatosCobro.arrayList.get(j).getGarantia(),
                                    DatosCobro.arrayList.get(j).getPrecioCompra(),
                                    DatosCobro.arrayList.get(j).getPrecioVenta(),
                                    DatosCobro.arrayList.get(j).getIdCategoria());
                        }
                    }
                }

                if(insertarAbono)
                {
                    TareaCreateCharge tareaCreateCharge = new TareaCreateCharge();
                    tareaCreateCharge.execute(fechaVenta, abono, DetalleCobro.idVendedor, idFactura);
                }
                else
                {
                    DetalleCobro.telefono = "";
                    DetalleCobro.direccion = "";

                    Intent intent = new Intent(AgregarCliente.this, PrincipalMenu.class);
                    startActivity(intent);
                    finish();

                    progressDialog.dismiss();
                }
            }
            else
            {
                Toast.makeText(AgregarCliente.this, "Error al Crear el Cliente", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    //Clases Asyntask para agregar una factura
    private class TareaCreateSale extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;

            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerVenta.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("total", params[0]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cantidadGarantia", "0"));
            nameValuePairs.add(new BasicNameValuePair("cantidadDevolucion", "0"));
            nameValuePairs.add(new BasicNameValuePair("estado", params[2]));
            nameValuePairs.add(new BasicNameValuePair("idFactura", params[3]));
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[4]));
            nameValuePairs.add(new BasicNameValuePair("option",  "createSale"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= respJSON.get("items");
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

            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {

        }
    }

    //Clases Asyntask para agregar un abono
    private class TareaCreateCharge extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;

            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerCobro.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("fecha", params[0]));
            nameValuePairs.add(new BasicNameValuePair("abono", params[1]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[2]));
            nameValuePairs.add(new BasicNameValuePair("idFactura", params[3]));
            nameValuePairs.add(new BasicNameValuePair("option",  "createCharge"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);
                //JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuesta= respJSON.get("items");
                resul = true;
                existe = true;
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                resul = false;
                existe = false;
            }

            catch (JSONException e)
            {
                e.printStackTrace();
                existe = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (existe)
            {
                DetalleCobro.telefono = "";
                DetalleCobro.direccion = "";

                Toast.makeText(AgregarCliente.this, "Cliente agregado satisfactoriamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AgregarCliente.this, PrincipalMenu.class);
                startActivity(intent);
                finish();

                progressDialog.dismiss();
            }
            else
            {
                Toast.makeText(AgregarCliente.this, "Error al Crear el Cliente", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    //Clases Asyntask para actualizar la cantidad de un producto
    private class TareaUpdadteProducto extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("descripcion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[3]));
            nameValuePairs.add(new BasicNameValuePair("tiempoGarantia", params[4]));
            nameValuePairs.add(new BasicNameValuePair("precioCompra", params[5]));
            nameValuePairs.add(new BasicNameValuePair("precioVenta", params[6]));
            nameValuePairs.add(new BasicNameValuePair("idCategoria", params[7]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateProduct"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                JSONArray objItems = respJSON.getJSONArray("items");

                //String obj
                respuestaProducto= String.valueOf(objItems);

                if(respuestaProducto.equalsIgnoreCase("No Existe"))
                {
                    existe = false;
                }
                else
                {
                    existe = true;
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
            //Toast.makeText(Perfil.this, respStr, Toast.LENGTH_SHORT).show();
        }
    }

    private class TareaUpdateClient extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        JSONObject respJSON;

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
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[0]));
            nameValuePairs.add(new BasicNameValuePair("cedula", params[1]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[2]));
            nameValuePairs.add(new BasicNameValuePair("direccion", params[3]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[4]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[5]));
            nameValuePairs.add(new BasicNameValuePair("nombreEmpresa", params[6]));
            nameValuePairs.add(new BasicNameValuePair("direccionEmpresa", params[7]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[8]));
            nameValuePairs.add(new BasicNameValuePair("calificacion", params[9]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateClient"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

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

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            if (existe)
            {
                TareaListado listado = new TareaListado();
                listado.execute();
            }
            else
            {
                Toast.makeText(AgregarCliente.this, "Error al Crear el Cliente", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }
}
