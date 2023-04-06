package com.example.gestiondetareas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gestion_tareas.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_CATEGORIAS = "t_categorias";

    // Sentencia SQL para crear la tabla de categorías
    private static final String SQL_CREATE_CATEGORIAS_TABLE =
            "CREATE TABLE "+ TABLE_CATEGORIAS +" (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "categoria TEXT," +
                    "contador TEXT, porcentaje INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_CATEGORIAS);
        onCreate(db);
    }
}
