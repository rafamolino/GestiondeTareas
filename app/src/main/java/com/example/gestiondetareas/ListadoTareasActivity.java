package com.example.gestiondetareas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ListadoTareasActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();

        recyclerView = (RecyclerView) findViewById(R.id.ContenedorTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ArrayList<Map<String,Object>> ListTareas = new ArrayList<>();

        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.forEach(d -> {


                    if(d.getId().contains(correo)){
                        ListTareas.add(d.getData());

                    }
                });
                AdapterCard adapterCard=new AdapterCard(ListTareas);
                recyclerView.setAdapter(adapterCard);
            }
        });

        ImageView verTareas = (ImageView) findViewById(R.id.button2);
        ImageButton btnVolver = (ImageButton) findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        verTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ListadoTareasActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });
    }
}
