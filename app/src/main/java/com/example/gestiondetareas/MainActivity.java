package com.example.gestiondetareas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.gestiondetareas.db.Categoria;
import com.example.gestiondetareas.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper; // Declarar una variable para el objeto DatabaseHelper
    private static final String PREFS_NAME = "MyPrefs"; // Nombre de las preferencias compartidas
    private static final String DB_CREATED_KEY = "db_created"; // Clave para la bandera de creación de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Obtener las preferencias compartidas
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isDbCreated = prefs.getBoolean(DB_CREATED_KEY, false); // Obtener el valor de la bandera de creación de la base de datos


        if(!isDbCreated) {
            // Si la base de datos no se ha creado todavía, crear una nueva instancia de DatabaseHelper
            dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Realizar las operaciones de creación inicial de la base de datos aquí
            // ...

            // Insertar datos en la tabla de categorias
            Categoria categoria1 = new Categoria("Trabajo", "2/5", 40);
            Categoria categoria2 = new Categoria("Universidad", "1/3", 33);
            Categoria categoria3 = new Categoria("Casa", "3/3", 100);
            Categoria categoria4 = new Categoria("Alimentacion", "3/3", 100);
            Categoria categoria5 = new Categoria("Mascota", "3/3", 100);


            dbHelper.insertarCategoria(categoria1);
            dbHelper.insertarCategoria(categoria2);
            dbHelper.insertarCategoria(categoria3);
            dbHelper.insertarCategoria(categoria4);
            dbHelper.insertarCategoria(categoria5);




            Toast.makeText(MainActivity.this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();

            // Actualizar la bandera de creación de la base de datos en las preferencias compartidas
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(DB_CREATED_KEY, true);
            editor.apply();
        } else {
            // Si la base de datos ya se ha creado, simplemente crear una instancia de DatabaseHelper sin realizar ninguna operación
            dbHelper = new DatabaseHelper(this);
            Toast.makeText(MainActivity.this, "LA BASE DE DATOS YA ESTABA CREADA", Toast.LENGTH_LONG).show();

        }



        Button btnInicio = (Button) findViewById(R.id.btnLogin);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        Button btnRegistro = (Button) findViewById(R.id.btnReg);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });


    }

    // Recuerda cerrar la base de datos en el onDestroy() u otro lugar apropiado para evitar fugas de memoria
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}