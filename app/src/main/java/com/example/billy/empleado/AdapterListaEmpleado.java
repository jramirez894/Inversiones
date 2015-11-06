package com.example.billy.empleado;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.constantes.Constantes;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin_Sena on 01/10/2015.
 */
public class AdapterListaEmpleado extends ArrayAdapter
{
    public static ItemListaEmpleado posicionItems;
    boolean resul;
    Object respuesta = "";

    public AdapterListaEmpleado(Context context,List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemlista_empleado,null);
        }

        ItemListaEmpleado items =(ItemListaEmpleado)getItem(position);

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombreListaEmpleado);
        ImageView editar = (ImageView)convertView.findViewById(R.id.imageViewEditarListaEmpleado);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarListaEmpleado);

        nombre.setText(items.getNombre());
        editar.setImageResource(items.getEdita());
        eliminar.setImageResource(items.getEliminar());

        editar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ItemListaEmpleado posicion = (ItemListaEmpleado) getItem(position);

                String idUsuario = posicion.getIdUsuario();
                String cedula = posicion.getCedulaEmp();
                String nombre = posicion.getNombre();
                String direccion = posicion.getDireccionEmp();
                String telefono = posicion.getTelefonoEmp();
                String correo = posicion.getCorreoEmp();
                String password = posicion.getPassword();

                Intent intent = new Intent(getContext(), M_Empleado.class);
                intent.putExtra("idUsuario", idUsuario);
                intent.putExtra("cedula", cedula);
                intent.putExtra("nombre", nombre);
                intent.putExtra("direccion", direccion);
                intent.putExtra("telefono", telefono);
                intent.putExtra("correo", correo);
                intent.putExtra("password", password);
                getContext().startActivity(intent);

            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionItems = (ItemListaEmpleado) getItem(position);
                EliminarEmpleado();
            }
        });

        return convertView;
    }

    public void EliminarEmpleado()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(android.R.drawable.ic_menu_save);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar Empleado?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                String idUsuario = posicionItems.getIdUsuario();

                TareaDelete delete = new TareaDelete();
                delete.execute(idUsuario);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.setCancelable(false);
        builder.show();
    }

    //Clases Asyntask para eliminar un empleado

    private class TareaDelete extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;
        JSONObject respJSON;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
            resul = true;
            HttpClient httpClient;
            List<NameValuePair> nameValuePairs;
            HttpPost httpPost;
            httpClient= new DefaultHttpClient();
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerLogin.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
            nameValuePairs.add(new BasicNameValuePair("option", "deleteUsser"));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse resp= httpClient.execute(httpPost);

                respStr = EntityUtils.toString(resp.getEntity());

                respJSON = new JSONObject(respStr);

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result)
        {
            String resp = respuesta.toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(android.R.drawable.ic_menu_save);
            builder.setTitle("Eliminar");
            builder.setMessage(resp);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArrayAdapter adapter = new AdapterListaEmpleado(getContext(), Empleados.arrayList);
                    adapter.remove(posicionItems);
                    //Se carga de nuevo la vista
                    Empleados.listaEmpleado.setAdapter(adapter);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
}
