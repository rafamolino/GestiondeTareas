package com.example.gestiondetareas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.gestiondetareas.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper; // Declarar una variable para el objeto DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crear una nueva instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(MainActivity.this);
        //Ya podemos insertar modificar o eliminar datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db!=null){
            Toast.makeText(MainActivity.this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "ERROR AL CREAL LA BASE DE DATOS", Toast.LENGTH_LONG).show();

        }

        Button btnInicio = (Button) findViewById(R.id.btnLogin);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
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