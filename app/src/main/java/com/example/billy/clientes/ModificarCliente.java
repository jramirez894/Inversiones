package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.menu_principal.PagerAdapter;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.inversiones.R;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ModificarCliente extends ActionBarActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener
{
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ModificarCliente.TabInfo>();
    private PagerAdapter mPagerAdapter;

    //Variables fecha personalizada
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText editFecha_AlertaMCliente;
    TextView txtMensaje_AlertaMCliente;
    EditText editCalificacion_AlertaMCliente;

    //Variables para capturar los elementos de la alerta
    int valorRestante = 0;
    String fecha;
    String calificacion;

    //Varibales createClient
    boolean resul;
    Object respuesta = "";

    //Variable para decidir el estado del cliente y de la factura
    String estadoCliente = "";
    String estadoFactura = "Activo";

    //Variable para insertar un abono
    boolean insertarAbono;

    //Variables para insertar en la tabla de cobros
    String idVendedor = "";
    String idFactura = "";

    //Variable para update producto
    String respuestaProducto = "";
    boolean existe = false;
    int posicionListaProductos = 0;

    //Alerta Cargando
    ProgressDialog progressDialog;

    @Override
    public void onClick(View view)
    {
        if(view == editFecha_AlertaMCliente)
        {
            datePickerDialog.show();
        }
    }

    private class TabInfo
    {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args)
        {
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
        public TabFactory(Context context)
        {
            mContext = context;
        }

        /**
         * (non-Javadoc)
         *
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

        @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_cliente);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Volver");
        actionBar.show();

        this.initialiseTabHost(savedInstanceState);
        // Intialise ViewPager
        this.intialiseViewPager();
    }

    protected void onResume(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager()
    {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, M_DatosPersonales.class.getName()));
        fragments.add(Fragment.instantiate(this, M_DatosCobro.class.getName()));
        fragments.add(Fragment.instantiate(this, M_DetalleCobro.class.getName()));
        fragments.add(Fragment.instantiate(this, ClientesHistorial.class.getName()));

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

        ModificarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("", getResources().getDrawable(R.mipmap.perfil)), ( tabInfo = new TabInfo("Tab1", V_DatosPersonales.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        ModificarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("", getResources().getDrawable(R.mipmap.capital)), (tabInfo = new TabInfo("Tab2", V_DatosCobro.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        ModificarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("", getResources().getDrawable(R.mipmap.productos)), (tabInfo = new TabInfo("Tab2", V_DetalleCobro.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        ModificarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("", getResources().getDrawable(R.mipmap.informacion)), (tabInfo = new TabInfo("Tab2", ClientesHistorial.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        mTabHost.setOnTabChangedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modificar_cliente, menu);
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
           case R.id.modificarCliente:
               ModificarCliente();
               break;
       }

        return super.onOptionsItemSelected(item);
    }

    //Alerta de Confirmacion
    public void ModificarCliente()
    {
        LayoutInflater inflaterAlert = (LayoutInflater) ModificarCliente.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflaterAlert.inflate(R.layout.alert_guardar_modificar_cliente, null);

        //Variables DatosPersonales
        final String cedula = M_DatosPersonales.cedula.getText().toString();
        final String nombre = M_DatosPersonales.nombre.getText().toString();
        final String direccion = M_DatosPersonales.direccion.getText().toString();
        final String telefono = M_DatosPersonales.telefono.getText().toString();
        final String correo = M_DatosPersonales.correo.getText().toString();
        final String nomEmpresa = M_DatosPersonales.nomEmpresa.getText().toString();
        final String dirEmpresa = M_DatosPersonales.dircEmpresa.getText().toString();

        //Variables DatosCobro
        String abono = "";

        try
        {
            abono = M_DatosCobro.abono.getText().toString();
        }
        catch (Exception e)
        {
            abono = "0";
        }

        //Variables DetalleCobro
        String fechaCobro;
        String diaCobro;
        String horaCobro;
        try
        {
            fechaCobro = M_DetalleCobro.fechaDeCobro.getSelectedItem().toString();
            diaCobro = M_DetalleCobro.diaCobro.getSelectedItem().toString();
            horaCobro = M_DetalleCobro.horaCobro.getSelectedItem().toString();
        }
        catch (Exception ex)
        {
            fechaCobro = Constantes.fechaCobroFactura;
            diaCobro = Constantes.diaCobroFactura;
            horaCobro = Constantes.horaCobroFactura;
        }

        if (cedula.equals("")||
                nombre.equals("")||
                direccion.equals("")||
                telefono.equals("")||
                correo.equals("")||
                nomEmpresa.equals("")||
                dirEmpresa.equals("")||
                fechaCobro.equalsIgnoreCase("Fecha de Cobro")||
                diaCobro.equalsIgnoreCase("Dia de Cobro")||
                horaCobro.equalsIgnoreCase("Hora de Cobro"))
        {
            Toast.makeText(ModificarCliente.this, "Faltan campos por llenar", Toast.LENGTH_LONG).show();
        }
        else
        {
            txtMensaje_AlertaMCliente = (TextView) dialoglayout.findViewById(R.id.txtMensaje_AlertaMCliente);
            editCalificacion_AlertaMCliente = (EditText) dialoglayout.findViewById(R.id.editCalificacion_AlertaMCliente);
            editFecha_AlertaMCliente = (EditText) dialoglayout.findViewById(R.id.editFecha_AlertaMCliente);

            try
            {
                valorRestante = Integer.valueOf(M_DatosCobro.valorRestante.getText().toString());
            }
            catch (Exception e)
            {
                valorRestante = 1;
            }

            if(valorRestante == 1)
            {
                txtMensaje_AlertaMCliente.setVisibility(View.GONE);
                editCalificacion_AlertaMCliente.setVisibility(View.GONE);
                editFecha_AlertaMCliente.setVisibility(View.GONE);

                estadoCliente = "Activo";
            }
            else
            {
                if(valorRestante != 0)
                {
                    txtMensaje_AlertaMCliente.setVisibility(View.GONE);
                    editCalificacion_AlertaMCliente.setVisibility(View.GONE);
                    estadoCliente = "Activo";

                    insertarAbono = true;

                    if(abono.equalsIgnoreCase("0") || abono.equalsIgnoreCase(""))
                    {
                        editFecha_AlertaMCliente.setVisibility(View.GONE);
                        insertarAbono = false;
                    }
                }
                else
                {
                    if(abono.equalsIgnoreCase("0") || abono.equalsIgnoreCase(""))
                    {
                        editFecha_AlertaMCliente.setVisibility(View.GONE);
                        insertarAbono = false;
                    }

                    estadoCliente = "Inactivo";
                    estadoFactura = "Inactivo";
                    insertarAbono = true;
                }
            }

            //Fecha Personalizada para la garantia
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            editFecha_AlertaMCliente.setInputType(InputType.TYPE_NULL);
            editFecha_AlertaMCliente.requestFocus();
            setDateTimeField();

            AlertDialog.Builder alerta = new AlertDialog.Builder(ModificarCliente.this);
            alerta.setIcon(R.mipmap.guardar);
            alerta.setTitle("Guardar");
            alerta.setView(dialoglayout);
            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    fecha = editFecha_AlertaMCliente.getText().toString();
                    calificacion = editCalificacion_AlertaMCliente.getText().toString();
                    switch (Constantes.interfaz)
                    {
                        case "Administrador":

                            if(valorRestante == 1)
                            {
                                TareaUpdateClient tareaUpdateClient = new TareaUpdateClient();
                                tareaUpdateClient.execute(cedula, nombre, direccion, telefono, correo, nomEmpresa, dirEmpresa, editCalificacion_AlertaMCliente.getText().toString());

                                AlertaCargando();
                            }
                            else
                            {
                                if (valorRestante != 0)
                                {
                                    if (fecha.equals(""))
                                    {
                                        Toast.makeText(ModificarCliente.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        TareaUpdateClient tareaUpdateClient = new TareaUpdateClient();
                                        tareaUpdateClient.execute(cedula, nombre, direccion, telefono, correo, nomEmpresa, dirEmpresa, editCalificacion_AlertaMCliente.getText().toString());

                                        AlertaCargando();
                                    }
                                }
                                else
                                {
                                    if (fecha.equals("") ||
                                            calificacion.equals(""))
                                    {
                                        Toast.makeText(ModificarCliente.this, "Faltan Datos Por Llenar aqui", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        TareaUpdateClient tareaUpdateClient = new TareaUpdateClient();
                                        tareaUpdateClient.execute(cedula, nombre, direccion, telefono, correo, nomEmpresa, dirEmpresa, editCalificacion_AlertaMCliente.getText().toString());

                                        AlertaCargando();
                                    }
                                }
                            }

                            break;

                        case "Empleado":
                            if (valorRestante != 0) {
                                if (fecha.equals("")) {
                                    Toast.makeText(ModificarCliente.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ModificarCliente.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(ModificarCliente.this, PrincipalEmpleado.class);
                                    startActivity(intent1);
                                    finish();
                                }

                            } else {
                                if (fecha.equals("") ||
                                        calificacion.equals("")) {
                                    Toast.makeText(ModificarCliente.this, "Faltan Datos Por Llenar", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ModificarCliente.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(ModificarCliente.this, PrincipalEmpleado.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            }

                            break;
                    }
                }
            });
            alerta.setNegativeButton("Cancelar", null);
            alerta.setCancelable(false);
            alerta.show();
        }
    }

    private void setDateTimeField()
    {
        editFecha_AlertaMCliente.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(ModificarCliente.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editFecha_AlertaMCliente.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private static void AddTab(ModificarCliente activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    //Varaibles de los Tab
    public void capturarVariables()
    {

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

    //Tarea modificar cliente
    private class TareaUpdateClient extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idCliente", Constantes.idClienteCliente));
            nameValuePairs.add(new BasicNameValuePair("cedula", params[0]));
            nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
            nameValuePairs.add(new BasicNameValuePair("direccion", params[2]));
            nameValuePairs.add(new BasicNameValuePair("telefono", params[3]));
            nameValuePairs.add(new BasicNameValuePair("correo", params[4]));
            nameValuePairs.add(new BasicNameValuePair("nombreEmpresa", params[5]));
            nameValuePairs.add(new BasicNameValuePair("direccionEmpresa", params[6]));
            nameValuePairs.add(new BasicNameValuePair("estado", estadoCliente));
            nameValuePairs.add(new BasicNameValuePair("calificacion", params[7]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateClient"));

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
            String idVenta = "";
            String total = "";
            String cantidad = "";
            String estado = "";

            String idProducto = "";

            String precioProducto = "";

            String totalFactura = "";
            int sumaTotalFactura = 0;

            for(int i = 0; i < M_DatosCobro.arrayList.size(); i++)
            {
                String estadoProducto = M_DatosCobro.arrayList.get(i).getEstado();

                switch (estadoProducto)
                {
                    case "update":

                        idVenta = M_DatosCobro.arrayList.get(i).getIdVenta();

                        for(int j = 0; j < Constantes.itemsVenta.size(); j++)
                        {
                            if(Constantes.itemsVenta.get(j).getIdVenta().equalsIgnoreCase(idVenta))
                            {
                                estado = Constantes.itemsVenta.get(j).getEstado();
                                idFactura = Constantes.itemsVenta.get(j).getIdFactura();
                                idProducto = Constantes.itemsVenta.get(j).getIdProducto();

                                for(int k = 0; k < M_DatosCobro.arrayListP.size(); k++)
                                {
                                    if(idProducto.equalsIgnoreCase(M_DatosCobro.arrayListP.get(k).getIdProducto()))
                                    {
                                        precioProducto = M_DatosCobro.arrayListP.get(k).getPrecioVenta();
                                    }
                                }

                                if(M_DatosCobro.arrayList.get(i).getCantidadAdicional().equalsIgnoreCase("0"))
                                {
                                    total = Constantes.itemsVenta.get(j).getTotal();
                                    cantidad = Constantes.itemsVenta.get(j).getCantidad();
                                    posicionListaProductos = j;
                                }
                                else
                                {
                                    cantidad = M_DatosCobro.arrayList.get(i).getCantidadAdicional();

                                    int operacion = Integer.valueOf(cantidad) * Integer.valueOf(precioProducto);
                                    total = String.valueOf(operacion);

                                    //guardar posicion en la lista para poder saber la cantidad que hay que descontar en los productos
                                    posicionListaProductos = j;
                                }
                            }
                        }

                        TareaUpdateSale tareaUpdateSale = new TareaUpdateSale();
                        tareaUpdateSale.execute(idVenta, total, cantidad, estado, idFactura, idProducto);

                        //Para descontar de la cantidad total de productos en caso de que añada nuevos.

                        int restaP = Integer.valueOf(cantidad) - Integer.valueOf(Constantes.itemsVenta.get(posicionListaProductos).getCantidad());

                        //Comprobar si se cambio la cantidad que habia para poder restar de la cantidad total
                        if(!cantidad.equalsIgnoreCase(Constantes.itemsVenta.get(posicionListaProductos).getCantidad()))
                        {
                            for(int k = 0; k < M_DatosCobro.arrayListP.size(); k++)
                            {
                                if(idProducto.equalsIgnoreCase(M_DatosCobro.arrayListP.get(k).getIdProducto()))
                                {
                                    restaP = Integer.valueOf(M_DatosCobro.arrayListP.get(k).getCantidad()) - restaP;

                                    TareaUpdadteProducto tareaUpdadteProducto = new TareaUpdadteProducto();
                                    tareaUpdadteProducto.execute(M_DatosCobro.arrayListP.get(k).getIdProducto(),
                                            M_DatosCobro.arrayListP.get(k).getNombre(),
                                            M_DatosCobro.arrayListP.get(k).getDescripcion(),
                                            String.valueOf(restaP),
                                            M_DatosCobro.arrayListP.get(k).getGarantia(),
                                            M_DatosCobro.arrayListP.get(k).getPrecioCompra(),
                                            M_DatosCobro.arrayListP.get(k).getPrecioVenta(),
                                            M_DatosCobro.arrayListP.get(k).getIdCategoria());
                                }
                            }

                        }

                        break;

                    case "insert":

                        String nombre = M_DatosCobro.arrayList.get(i).getNombre();
                        idFactura = Constantes.itemsVenta.get(0).getIdFactura();

                        for(int k = 0; k < M_DatosCobro.arrayListP.size(); k++)
                        {
                            if(nombre.equalsIgnoreCase(M_DatosCobro.arrayListP.get(k).getNombre()))
                            {
                                idProducto = M_DatosCobro.arrayListP.get(k).getIdProducto();
                                precioProducto = M_DatosCobro.arrayListP.get(k).getPrecioVenta();
                            }
                        }

                        estado = "En Venta";
                        cantidad = M_DatosCobro.arrayList.get(i).getCantidad();

                        int operacion = Integer.valueOf(cantidad) * Integer.valueOf(precioProducto);
                        total = String.valueOf(operacion);

                        TareaCreateSale tareaCreateSale = new TareaCreateSale();
                        tareaCreateSale.execute(total, cantidad, estado, idFactura, idProducto);

                        //Para descontar de la cantidad total de productos en caso de que añada nuevos.

                        for(int k = 0; k < Constantes.itemsProductos.size(); k++)
                        {
                            if(idProducto.equalsIgnoreCase(Constantes.itemsProductos.get(k).getIdProducto()))
                            {
                                int restaProductos = Integer.valueOf(Constantes.itemsProductos.get(k).getCantidad()) - Integer.valueOf(cantidad);

                                TareaUpdadteProducto tareaUpdadteProducto = new TareaUpdadteProducto();
                                tareaUpdadteProducto.execute(Constantes.itemsProductos.get(k).getIdProducto(),
                                        Constantes.itemsProductos.get(k).getNombre(),
                                        Constantes.itemsProductos.get(k).getDescripcion(),
                                        String.valueOf(restaProductos),
                                        Constantes.itemsProductos.get(k).getGarantia(),
                                        Constantes.itemsProductos.get(k).getPrecioCompra(),
                                        Constantes.itemsProductos.get(k).getPrecioVenta(),
                                        Constantes.itemsProductos.get(k).getIdCategoria());
                            }
                        }

                        break;
                }

                sumaTotalFactura = sumaTotalFactura + Integer.valueOf(total);
            }

            //Variables spinner clase detalle cobro
            String fecha = "";
            String fechaCobro = "";
            String diaCobro = "";
            String horaCobro = "";

            try
            {
                fechaCobro = M_DetalleCobro.fechaDeCobro.getSelectedItem().toString();
                diaCobro = M_DetalleCobro.diaCobro.getSelectedItem().toString();
                horaCobro = M_DetalleCobro.horaCobro.getSelectedItem().toString();

                for(int i = 0; i < Constantes.itemsFactura.size(); i++)
                {
                    if(Constantes.idClienteFactura.equalsIgnoreCase(Constantes.itemsFactura.get(i).getIdCliente()))
                    {
                        fecha = Constantes.itemsFactura.get(i).getFecha();
                    }
                }

            }
            catch (Exception e)
            {
                //Identiicar cual es el id del cliente que se selecciono para traer los datos correspondientes

                for(int i = 0; i < Constantes.itemsFactura.size(); i++)
                {
                    if(Constantes.idClienteFactura.equalsIgnoreCase(Constantes.itemsFactura.get(i).getIdCliente()))
                    {
                        fechaCobro = Constantes.itemsFactura.get(i).getFechaCobro();
                        diaCobro = Constantes.itemsFactura.get(i).getDiaCobro();
                        horaCobro = Constantes.itemsFactura.get(i).getHoraCobro();
                        fecha = Constantes.itemsFactura.get(i).getFecha();
                    }
                }
            }

            if(M_DetalleCobro.idVendedor.equalsIgnoreCase(""))
            {
                idVendedor = Constantes.idVendedorFactura;
            }
            else
            {
                idVendedor = M_DetalleCobro.idVendedor;
            }

            if(valorRestante == 1)
            {
                TareaUpdateBill tareaUpdateBill = new TareaUpdateBill();
                tareaUpdateBill.execute(idFactura,
                        fecha,
                        String.valueOf(sumaTotalFactura),
                        M_DatosCobro.saldoRestante.getText().toString(),
                        estadoFactura,
                        fechaCobro,
                        diaCobro,
                        horaCobro,
                        idVendedor,
                        Constantes.idClienteFactura);
            }
            else
            {
                TareaUpdateBill tareaUpdateBill = new TareaUpdateBill();
                tareaUpdateBill.execute(idFactura,
                        fecha,
                        String.valueOf(sumaTotalFactura),
                        M_DatosCobro.valorRestante.getText().toString(),
                        estadoFactura,
                        fechaCobro,
                        diaCobro,
                        horaCobro,
                        idVendedor,
                        Constantes.idClienteFactura);
            }
        }
    }

    //Clases Asyntask para agregar una factura
    private class TareaUpdateSale extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idVenta", params[0]));
            nameValuePairs.add(new BasicNameValuePair("total", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cantidad", params[2]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[3]));
            nameValuePairs.add(new BasicNameValuePair("idFactura", params[4]));
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[5]));
            nameValuePairs.add(new BasicNameValuePair("option",  "updateSale"));

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

    //Clases Asyntask para agregar una factura
    private class TareaUpdateBill extends AsyncTask<String,Integer,Boolean>
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
            nameValuePairs.add(new BasicNameValuePair("idFactura", params[0]));
            nameValuePairs.add(new BasicNameValuePair("fecha", params[1]));
            nameValuePairs.add(new BasicNameValuePair("total", params[2]));
            nameValuePairs.add(new BasicNameValuePair("valorRestante", params[3]));
            nameValuePairs.add(new BasicNameValuePair("estado", params[4]));
            nameValuePairs.add(new BasicNameValuePair("fechaCobro", params[5]));
            nameValuePairs.add(new BasicNameValuePair("diaCobro", params[6]));
            nameValuePairs.add(new BasicNameValuePair("horaCobro", params[7]));
            nameValuePairs.add(new BasicNameValuePair("idVendedor", params[8]));
            nameValuePairs.add(new BasicNameValuePair("idCliente", params[9]));
            nameValuePairs.add(new BasicNameValuePair("option", "updateBill"));

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
            if(insertarAbono)
            {
                Date date = new Date();
                DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                String fechaVentaModificada = fecha + " " + hourFormat.format(date);

                TareaCreateCharge tareaCreateCharge = new TareaCreateCharge();
                tareaCreateCharge.execute(fechaVentaModificada, M_DatosCobro.abono.getText().toString(), idVendedor, idFactura);
            }
            else
            {
                Toast.makeText(ModificarCliente.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ModificarCliente.this, PrincipalMenu.class);
                startActivity(intent);
                finish();

                progressDialog.dismiss();
            }
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
            Toast.makeText(ModificarCliente.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ModificarCliente.this, PrincipalMenu.class);
            startActivity(intent);
            finish();

            progressDialog.dismiss();
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
}
