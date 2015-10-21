package com.example.billy.clientes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.billy.interfaces_empleado.PrincipalEmpleado;
import com.example.billy.menu_principal.PagerAdapter;
import com.example.billy.menu_principal.PrincipalMenu;
import com.example.billy.inversiones.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    String interfaz ="";

        @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_cliente);

        Bundle bundle = getIntent().getExtras();
        interfaz = bundle.getString("Interfaz");

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

        ModificarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("", getResources().getDrawable(R.mipmap.capital)), (tabInfo = new TabInfo("Tab2", V_DetalleCobro.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        ModificarCliente.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("", getResources().getDrawable(R.mipmap.informacion)), (tabInfo = new TabInfo("Tab2", ClientesHistorial.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
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
        int valorRestante = 0;

        String valorRestanteVacio = M_DatosCobro.valorRestante.getText().toString();

        if (valorRestanteVacio.equalsIgnoreCase(""))
        {
            Toast.makeText(ModificarCliente.this, "Aun no se ha registrado el valor restante", Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                valorRestante = Integer.valueOf(M_DatosCobro.valorRestante.getText().toString());
            }
            catch (Exception e)
            {

            }

            if(valorRestante != 0)
            {
                txtMensaje_AlertaMCliente = (TextView) dialoglayout.findViewById(R.id.txtMensaje_AlertaMCliente);
                txtMensaje_AlertaMCliente.setVisibility(View.GONE);

                editCalificacion_AlertaMCliente = (EditText) dialoglayout.findViewById(R.id.editCalificacion_AlertaMCliente);
                editCalificacion_AlertaMCliente.setVisibility(View.GONE);
            }

            //Fecha Personalizada para la garantia
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            editFecha_AlertaMCliente = (EditText) dialoglayout.findViewById(R.id.editFecha_AlertaMCliente);
            editFecha_AlertaMCliente.setInputType(InputType.TYPE_NULL);
            editFecha_AlertaMCliente.requestFocus();
            setDateTimeField();

            AlertDialog.Builder alerta = new AlertDialog.Builder(ModificarCliente.this);
            alerta.setIcon(R.mipmap.garantia);
            alerta.setTitle("Garantia");
            alerta.setView(dialoglayout);
            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (interfaz) {
                        case "Administrador":
                            Toast.makeText(ModificarCliente.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ModificarCliente.this, PrincipalMenu.class);
                            startActivity(intent);
                            finish();
                            break;

                        case "Empleado":
                            Toast.makeText(ModificarCliente.this, "Los Cambios Fueron Exitosos", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(ModificarCliente.this, PrincipalEmpleado.class);
                            startActivity(intent1);
                            finish();
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
        //Variables DatosPersonales
        String cedula = M_DatosPersonales.cedula.getText().toString();
        String nombre = M_DatosPersonales.nombre.getText().toString();
        String direccion = M_DatosPersonales.direccion.getText().toString();
        String telefono = M_DatosPersonales.telefono.getText().toString();
        String correo = M_DatosPersonales.correo.getText().toString();
        String nomEmpresa = M_DatosPersonales.nomEmpresa.getText().toString();
        String dirEmpresa = M_DatosPersonales.dircEmpresa.getText().toString();

        //Variables DatosCobro
        String fechaVenta = M_DatosCobro.fechaVenta.getText().toString();
        String totalPagar = M_DatosCobro.totalPagar.getText().toString();
        String abono = M_DatosCobro.abono.getText().toString();
        String valorRestante = M_DatosCobro.valorRestante.getText().toString();

        //Variables DetalleCobro
        String nombreEmpleado = M_DetalleCobro.buscarEmpleado.getText().toString();
        String fechaCobro = M_DetalleCobro.fechaDeCobro.getSelectedItem().toString();
        String diaCobro = M_DetalleCobro.diaCobro.getSelectedItem().toString();
        String horaCobro = M_DetalleCobro.horaCobro.getSelectedItem().toString();
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
