package com.example.billy.base_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jeniffer on 08/02/2016.
 */
public class ConexionBD
{
    public static SQLiteDatabase db;

    public ConexionBD(Context context)
    {
        BaseDatos baseDatos = new BaseDatos(context, "BDPendientes", null, 1);
        db = baseDatos.getWritableDatabase();
    }

    public static SQLiteDatabase getDb() {
        return db;
    }
}
