package com.example.gestiondetareas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gestion_tareas.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_CATEGORIAS = "t_categorias";
    public static final String TABLE_USUARIOS = "t_usuarios";


    // Sentencia SQL para crear la tabla de categorías
    private static final String SQL_CREATE_CATEGORIAS_TABLE =
            "CREATE TABLE "+ TABLE_CATEGORIAS +" (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "categoria TEXT," +
                    "contador TEXT, porcentaje INTEGER)";
    // Sentencia SQL para crear la tabla de usuarios
    private static final String SQL_CREATE_USUARIOS_TABLE =
            "CREATE TABLE "+ TABLE_USUARIOS +" (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "apellidos TEXT, correo TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_CATEGORIAS_TABLE);
        db.execSQL(SQL_CREATE_USUARIOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_CATEGORIAS);
        onCreate(db);
    }

    // Método para insertar una nueva categoría en la tabla de "categorias"
    public void insertarCategoria(Categoria categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("categoria", categoria.getNombre());
        values.put("contador", categoria.getContador());
        values.put("porcentaje", categoria.getPorcentaje());

        db.insert(TABLE_CATEGORIAS, null, values);
        db.close();
    }
    // Método para insertar un nuevo usuario en la tabla de "usuarios"
    public void insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("apellidos", usuario.getApellidos());
        values.put("correo", usuario.getCorreo());

        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }
    // Método para obtener el nombre de un usuario dado su correo electrónico
    public String obtenerNombrePorCorreo(String correo) {
        String nombre = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"nombre"};
        String selection = "correo" + " = ?";
        String[] selectionArgs = {correo};
        Cursor cursor = db.query(TABLE_USUARIOS, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int nombreIndex = cursor.getColumnIndex("nombre");
            if (nombreIndex != -1) { // Verificar si la columna existe en el cursor
                nombre = cursor.getString(nombreIndex);
            }
        }
        cursor.close();
        return nombre;
    }

    public ArrayList<Categoria> obtenerCategorias() {
        ArrayList<Categoria> listaCategorias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = {
                "id",
                "categoria",
                "contador",
                "porcentaje"
        };

        Cursor cursor = db.query(
                TABLE_CATEGORIAS,
                columnas,
                null,
                null,
                null,
                null,
                null
        );

        // Recorre el cursor y obtiene los datos de cada fila
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("contador"));
            int porcentaje = cursor.getInt(cursor.getColumnIndexOrThrow("porcentaje"));

            // Crea una nueva instancia de Categoria y agrega a la lista
            Categoria categoria = new Categoria(nombre, descripcion, porcentaje);
            listaCategorias.add(categoria);
        }

        // Cierra el cursor y la conexión con la base de datos
        cursor.close();
        db.close();

        return listaCategorias;
    }
}
