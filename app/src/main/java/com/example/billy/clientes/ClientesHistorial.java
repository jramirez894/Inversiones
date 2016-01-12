package com.example.billy.clientes;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.billy.constantes.Constantes;
import com.example.billy.inversiones.R;
import com.example.billy.menu_principal.AdapterListaPersonalizada;
import com.example.billy.menu_principal.PrincipalMenu;

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

public class ClientesHistorial extends Fragment
{
    ListView lista;
    ArrayList<Item_ClienteHistorial> arrayList = new ArrayList<Item_ClienteHistorial>();
    //Tabla Cobro
    public static ArrayList<ItemsCobro_AgregarCliente> itemsCobro = new ArrayList<ItemsCobro_AgregarCliente>();
    boolean existe = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_clientes_historial, container, false);

        lista = (ListView)view.findViewById(R.id.listViewLista_ClienteHistorial);
        ActualizarLista();

        return view;
    }

    public void ActualizarLista()
    {
        TareaCobro tareaCobro = new TareaCobro();
        tareaCobro.execute();
    }

    //Clases Asyntask para traer los datos de la tabla cobros

    private class TareaCobro extends AsyncTask<String,Integer,Boolean>
    {
        private String respStr;
        private JSONObject msg;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params)
        {
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

                if(objVendedores.length() > 0)
                {
                    for(int j=0; j<objVendedores.length(); j++)
                    {
                        JSONObject obj = objVendedores.getJSONObject(j);

                        switch (Constantes.tipoConsultaidFactura)
                        {
                            case "Visualizar":

                                if(obj.getString("idFactura").equalsIgnoreCase(VisualizarCliente.idFactura))
                                {
                                    itemsCobro.add(new ItemsCobro_AgregarCliente(obj.getString("idCobro"),obj.getString("fecha"),obj.getString("abono"),obj.getString("idVendedor"),obj.getString("idFactura")));
                                }

                                existe= true;

                                break;

                            case "Modificar":

                                if(obj.getString("idFactura").equalsIgnoreCase(AdapterListaPersonalizada.idFactura))
                                {
                                    itemsCobro.add(new ItemsCobro_AgregarCliente(obj.getString("idCobro"),obj.getString("fecha"),obj.getString("abono"),obj.getString("idVendedor"),obj.getString("idFactura")));
                                }

                                existe= true;

                                break;
                        }
                    }
                }
                else
                {
                    existe = true;
                }

            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
                existe= false;
            }

            catch(ClientProtocolException e)
            {
                e.printStackTrace();
                existe= false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                existe= false;
            } catch (JSONException e) {
                e.printStackTrace();
                existe= false;
            }

            return existe;
        }

        protected void onPostExecute(Boolean result)
        {
            arrayList.clear();

            for(int i = 0; i < itemsCobro.size(); i++)
            {
                String fecha = itemsCobro.get(i).getFecha();
                String[] separated = fecha.split(" ");
                String fechaSplit = separated[0]; // this will contain "Fruit"

                arrayList.add(new Item_ClienteHistorial("Fecha: " + fechaSplit, "Valor: $"+ itemsCobro.get(i).getAbono()));
            }

            lista.setAdapter(new AdapterLista_ClienteHistorial(getActivity(), arrayList));
        }
    }

}
