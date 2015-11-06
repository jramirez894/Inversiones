package com.example.billy.productos;

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

import com.example.billy.empleado.Empleados;
import com.example.billy.empleado.ItemListaEmpleado;
import com.example.billy.empleado.M_Empleado;
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
 * Created by Admin_Sena on 02/10/2015.
 */
public class AdapterListaProductos_Productos extends ArrayAdapter
{
    public static ItemsListaProductos_Productos itemsPosicion;
    boolean resul;
    Object respuesta = "";

    public AdapterListaProductos_Productos(Context context, List objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemsproducto_productos,null);
        }

        TextView nombre = (TextView)convertView.findViewById(R.id.textViewNombreProducto_Productos);
        ImageView editar = (ImageView)convertView.findViewById(R.id.imageViewEditarProducto_Productos);
        ImageView eliminar = (ImageView)convertView.findViewById(R.id.imageViewEliminarProducto_Productos);

        ItemsListaProductos_Productos items = (ItemsListaProductos_Productos)getItem(position);

        nombre.setText(items.getNombre());
        editar.setImageResource(items.getEditar());
        eliminar.setImageResource(items.getEliminar());

        editar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ItemsListaProductos_Productos productos = (ItemsListaProductos_Productos) getItem(position);

                String idProducto = productos.getIdProducto();
                String nombre = productos.getNombre();
                String idCategoria = productos.getIdCategoria();
                String descripcion = productos.getDescripcion();
                String cantidad = productos.getCantidad();
                String precioCompra = productos.getPrecioCompra();
                String precioVenta = productos.getPrecioVenta();

                Intent intent = new Intent(getContext(),M_Producto.class);

                intent.putExtra("idProducto", idProducto);
                intent.putExtra("nombre", nombre);
                intent.putExtra("idCategoria", idCategoria);
                intent.putExtra("descripcion", descripcion);
                intent.putExtra("cantidad", cantidad);
                intent.putExtra("precioCompra", precioCompra);
                intent.putExtra("precioVenta", precioVenta);
                getContext().startActivity(intent);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                itemsPosicion = (ItemsListaProductos_Productos) getItem(position);
                EliminarProducto();
            }
        });

        return convertView;
    }



    public void EliminarProducto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.eliminar);
        builder.setTitle("Elimina");
        builder.setMessage("¿Eliminar Producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Captura la Posicion del item de la lista

                //Borrar un item de la lista
                String idProducto = itemsPosicion.getIdProducto();

                TareaDelete delete = new TareaDelete();
                delete.execute(idProducto);
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
            httpPost = new HttpPost("http://inversiones.aprendicesrisaralda.com/Controllers/ControllerProducto.php");

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idProducto", params[0]));
            nameValuePairs.add(new BasicNameValuePair("option", "deleteProduct"));

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
                    ArrayAdapter adapter = new AdapterListaProductos_Productos(getContext(), Productos.arrayList);
                    adapter.remove(itemsPosicion);
                    //Se carga de nuevo la vista
                    Productos.listaProductos.setAdapter(adapter);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

}
