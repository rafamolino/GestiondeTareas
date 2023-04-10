package com.example.gestiondetareas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestiondetareas.db.Categoria;
import com.example.gestiondetareas.db.DatabaseHelper;
import com.example.gestiondetareas.db.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
            Usuario user_admin= new Usuario("admin", "admin", "admin@admin.com");


            dbHelper.insertarCategoria(categoria1);
            dbHelper.insertarCategoria(categoria2);
            dbHelper.insertarCategoria(categoria3);
            dbHelper.insertarCategoria(categoria4);
            dbHelper.insertarCategoria(categoria5);
            dbHelper.insertarUsuario(user_admin);




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



        Button btnRegistro = (Button) findViewById(R.id.btnReg);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        setup();


    }

    private void setup(){

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        EditText edtCorreo = (EditText) findViewById(R.id.edtLoginEmail);
        EditText edtContraseña = (EditText) findViewById(R.id.edtLoginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtCorreo.getText().toString()) && !TextUtils.isEmpty(edtContraseña.getText().toString())){

                    FirebaseAuth.getInstance().
                            signInWithEmailAndPassword(edtCorreo.getText().toString(), edtContraseña.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Intent intent= new Intent(MainActivity.this, InicioActivity.class);
                                        intent.putExtra("email", task.getResult().getUser().getEmail());
                                        startActivity(intent);


                                    }else {

                                        // La tarea se completó con un error
                                        Exception exception = task.getException();
                                        String mensajeError = "Contraseña incorrecta o usuario no registrado.";

                                        // Muestra una alerta en la pantalla con el mensaje de error
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Error")
                                                .setMessage(mensajeError)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Haz algo cuando se presione el botón "Aceptar"
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();

                                    }
                                }
                            });

                }
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