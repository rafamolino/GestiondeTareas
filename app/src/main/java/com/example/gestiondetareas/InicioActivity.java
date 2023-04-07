package com.example.gestiondetareas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;

import com.example.gestiondetareas.db.Categoria;
import com.example.gestiondetareas.db.CategoriaAdapter;
import com.example.gestiondetareas.db.DatabaseHelper;

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

        // Obtén una instancia de tu base de datos SQLite
        db = new DatabaseHelper(this);

        // Obtén la lista de categorías de la base de datos
        listaCategorias = db.obtenerCategorias();

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriaAdapter = new CategoriaAdapter(listaCategorias);
        recyclerView.setAdapter(categoriaAdapter);



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cierra la conexión con la base de datos al cerrar la actividad
        db.close();
    }
}
