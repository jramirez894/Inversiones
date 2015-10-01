package com.example.billy.inversiones;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
        String valorProducto="";
        String fechaVenta="";
        String totalPagar="";
        String abono="";
        String valorRestante="";

    //Variables del tab DetalleCobro
        String nomEmpleado="";
        String fechaCobro="";
        String diaCobro="";
        String horaCobro="";

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
    class TabFactory implements TabHost.TabContentFactory {

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
        public View createTabContent(String tag) {
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
        setContentView(R.layout.activity_agregar_cliente);

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
        try
        {
            int id = item.getItemId();

            //Variables Asociadas tab DatosPersonales
            cedula = DatosPersonales.ced.getText().toString();
            nombre = DatosPersonales.nom.getText().toString();
            direccion = DatosPersonales.direccion.getText().toString();
            telefono = DatosPersonales.telefono.getText().toString();
            correo = DatosPersonales.correo.getText().toString();
            nomEmpresa = DatosPersonales.nomEmpresa.getText().toString();
            dirEmpresa = DatosPersonales.dircEmpresa.getText().toString();

            //Variables Asociadas tab DatosCobro
            buscarProducto =DatosCobro.buscarProducto.getText().toString();
            valorProducto =DatosCobro.valorProducto.getText().toString();
            fechaVenta =DatosCobro.fechaVenta.getText().toString();
            totalPagar =DatosCobro.totalPagar.getText().toString();
            abono =DatosCobro.abono.getText().toString();
            valorRestante =DatosCobro.valorRestante.getText().toString();

            //Variables Asociadas tab DetalleCobro
            nomEmpleado =DetalleCobro.buscarEmpleado.getText().toString();
            fechaCobro = DetalleCobro.fechaDeCobro.getSelectedItem().toString();
            diaCobro = DetalleCobro.diaCobro.getSelectedItem().toString();
            horaCobro = DetalleCobro.horaCobro.getSelectedItem().toString();

            switch (item.getItemId())
            {
                case R.id.guardarCliente_AgregarCliente:
                    if (cedula.equals("")||
                            nombre.equals("")||
                            direccion.equals("")||
                            telefono.equals("")||
                            correo.equals("")||
                            nomEmpresa.equals("")||
                            dirEmpresa.equals("")||
                            buscarProducto.equals("")||
                            valorProducto.equals("")||
                            totalPagar.equals("")||
                            valorRestante.equals("")||
                            nomEmpleado.equals("")||
                            fechaCobro.equalsIgnoreCase("Fecha de Cobro")||
                            diaCobro.equalsIgnoreCase("Dia de Cobro")||
                            horaCobro.equalsIgnoreCase("Hora de Cobro"))
                    {
                        Toast.makeText(AgregarCliente.this,"Faltan Datos Por Llenar",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        guardarCliente();
                    }
                    break;
            }
        }
        catch (Exception e)
        {

        }


        return super.onOptionsItemSelected(item);
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

    public void guardarCliente()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Guardar");
        builder.setMessage("¿Agregar Cliente?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AgregarCliente.this, "Su Registro fue Exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AgregarCliente.this, PrincipalMenu.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar",null );
        builder.setCancelable(false);
        builder.show();
    }

}
