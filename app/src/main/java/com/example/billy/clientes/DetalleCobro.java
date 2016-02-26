
package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.billy.empleado.AdapterListaEmpleado;
import com.example.billy.empleado.ItemListaEmpleado;
import com.example.billy.empleado.V_Empleado;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetalleCobro extends Fragment implements View.OnClickListener
{
    public static AutoCompleteTextView buscarEmpleado;
    public static TextView dir;
    public static TextView tel;

    public static EditText fechaDeCobro;
    public static Spinner horaCobro;

    boolean existe = false;
    String respuesta = "";

    public static ArrayList<ItemListaEmpleado> arrayList = new ArrayList<ItemListaEmpleado>();
    public static ArrayList<String> arrayListNombresVe = new ArrayList<String>();

    public static String idVendedor = "";
    public static String direccion = "";
    public static String telefono = "";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_detalle_cobro, container, false);

        buscarEmpleado=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteBuscarEmpleado_DetalleCobro);
        dir=(TextView)view.findViewById(R.id.textViewdirEmpleado_DetalleCobro);
        tel=(TextView)view.findViewById(R.id.textViewtelEmpleado_DetalleCobro);
        horaCobro=(Spinner)view.findViewById(R.id.spinnerHoraCobro_DetalleCobro);

        //Fecha Personalizada
        fechaDeCobro=(EditText)view.findViewById(R.id.editFechaCobro_DetalleCobro);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaDeCobro.setInputType(InputType.TYPE_NULL);
        fechaDeCobro.requestFocus();

        setDateTimeField();

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.horadeCobro, android.R.layout.simple_spinner_item);
        horaCobro.setAdapter(adapter2);

        TareaListado listado = new TareaListado();
        listado.execute();

        buscarEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nombre = buscarEmpleado.getText().toString();
                int posicion = 0;

                for (int i = 0; i < arrayList.size(); i++) {
                    if (nombre.equalsIgnoreCase(arrayList.get(i).getNombre())) {
                        posicion = i;
                        break;
                    }
                }

                ItemListaEmpleado empleado = arrayList.get(posicion);
                idVendedor = empleado.getIdUsuario();
                direccion = empleado.getDireccionEmp();
                telefono = empleado.getTelefonoEmp();
                dir.setText("Direccion: " + direccion);
                tel.setText("Telefono: " + telefono);
            }
        });

        return view;

    }

    private void setDateTimeField()
    {
        fechaDeCobro.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaDeCobro.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view)
    {
        if(view == fechaDeCobro) {
            datePickerDialog.show();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        dir.setText("Direccion: " + direccion);
        tel.setText("Telefono: " + telefono);
    }

    //Clases Asyntask para listar los empleados que hay actualmente en el servidor
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
                    arrayList.clear();
                    arrayListNombresVe.clear();
                    for(int i=0; i<objVendedores.length(); i++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(i);
                        arrayList.add(new ItemListaEmpleado(obj.getString("nombre"), R.mipmap.editar, R.mipmap.eliminar, obj.getString("idUsuario"), obj.getString("cedula"), obj.getString("nombre"), obj.getString("direccion"), obj.getString("telefono"), obj.getString("correo"), obj.getString("password")));
                        arrayListNombresVe.add(obj.getString("nombre"));
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
            buscarEmpleado.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayListNombresVe));
        }
    }
}
