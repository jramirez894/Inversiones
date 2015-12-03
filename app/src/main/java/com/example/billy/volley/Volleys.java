package com.example.billy.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrador on 03/12/2015.
 */
public class Volleys
{
    private static Volleys mVolleyS = null;
    //Este objeto es la cola que usará la aplicación
    private RequestQueue mRequestQueue;

    private Volleys(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static Volleys getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new Volleys(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
