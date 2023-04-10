package com.example.gestiondetareas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gestiondetareas.db.Categoria;
import com.example.gestiondetareas.db.CategoriaAdapter;
import com.example.gestiondetareas.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;



public class InicioActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> listaCategorias;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_main);

        Bundle bundle = getIntent().getExtras();
        TextView labelUser = (TextView) findViewById(R.id.labelUser);
        String correo= bundle.getString("email");


        Log.d("Correo", correo);
        // Obtén una instancia de tu base de datos SQLite
        db = new DatabaseHelper(this);
        String usuario = db.obtenerNombrePorCorreo(correo);
        Log.d("Correo", usuario);
        labelUser.setText("Hola " + usuario +"!");
        // Obtén la lista de categorías de la base de datos
        listaCategorias = db.obtenerCategorias();

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriaAdapter = new CategoriaAdapter(listaCategorias);
        recyclerView.setAdapter(categoriaAdapter);

        ImageButton btnInicio = (ImageButton) findViewById(R.id.button2);
        btnInicio.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent= new Intent(InicioActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnAdd = (ImageButton) findViewById(R.id.button3);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent= new Intent(InicioActivity.this, Nueva_tarea_Activity.class);
                startActivity(intent);
            }
        });

        ImageButton btnProfile = (ImageButton) findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                // Crea un intent para la actividad de inicio
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                // Limpia las actividades anteriores y crea una nueva tarea para la actividad de inicio
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Inicia la actividad de inicio
                startActivity(intent);

                // Finaliza la actividad actual para que no se pueda volver atrás
                finish();
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cierra la conexión con la base de datos al cerrar la actividad
        db.close();
    }




}

