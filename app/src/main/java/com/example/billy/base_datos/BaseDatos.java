package com.example.billy.base_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeniffer on 08/02/2016.
 */
public class BaseDatos extends SQLiteOpenHelper
{
    String pendiente = "CREATE TABLE Pendientes (idPendiente INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, " +
            "idCliente TEXT, " +
            "cedula TEXT, " +
            "direccion TEXT, " +
            "telefono TEXT, " +
            "correo TEXT, " +
            "nombreEmpresa TEXT, " +
            "direccionEmpresa TEXT, " +
            "estado TEXT, " +
            "calificacion TEXT, fechaPendiente TEXT)";

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(pendiente);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Pendientes");

        db.execSQL(pendiente);
    }
}
