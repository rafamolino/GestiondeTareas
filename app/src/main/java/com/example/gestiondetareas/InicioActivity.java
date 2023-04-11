package com.example.gestiondetareas;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;



public class InicioActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> listaCategorias;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_main);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        TextView labelUser = (TextView) findViewById(R.id.labelUser);
        String correo= currentUser.getEmail();
        //labelUser.setText(correo);

        DocumentReference docRef = db.collection("usuarios").document(correo);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        labelUser.setText("Hola " +document.getString("nombre")+"!");
                    } else {
                        labelUser.setText("");
                    }
                } else {
                    labelUser.setText("Error");
                }
            }
        });



        // Obtén una instancia de tu base de datos SQLite
       // db = new DatabaseHelper(this);
        //String usuario = db.obtenerNombrePorCorreo(correo);
        //Log.d("Correo", usuario);
        //labelUser.setText("Hola " + usuario +"!");
        // Obtén la lista de categorías de la base de datos
        dbHelper = new DatabaseHelper(this);
        listaCategorias = dbHelper.obtenerCategorias();

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
                Intent intent= new Intent(InicioActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cierra la conexión con la base de datos al cerrar la actividad
        dbHelper.close();
    }




}

