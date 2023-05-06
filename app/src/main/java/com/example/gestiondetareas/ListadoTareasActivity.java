package com.example.gestiondetareas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        ArrayList<Map<String,Object>> ListTareasCheck = new ArrayList<>();
        ArrayList<Map<String,Object>> ListTareasCruz = new ArrayList<>();
        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.forEach(d -> {


                    if(d.getId().contains(correo)){
                        ListTareas.add(d.getData());

                    }
                });
                ListTareas.forEach(i -> {
                    if(!Boolean.valueOf(String.valueOf(i.get("estado")))){
                        ListTareasCheck.add(i);
                    }
                });
                ListTareas.forEach(i -> {
                    if(Boolean.valueOf(String.valueOf(i.get("estado")))){
                        ListTareasCruz.add(i);
                    }
                });
                AdapterCard adapterCard=new AdapterCard(ListTareas);
                recyclerView.setAdapter(adapterCard);

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        adapterCard.changeStatus(viewHolder.getAdapterPosition());

                    }
                }).attachToRecyclerView(recyclerView);
            }
        });

        ImageButton btnList = (ImageButton) findViewById(R.id.btnListTasks);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdapterCard adapterCard=new AdapterCard(ListTareas);
                recyclerView.setAdapter(adapterCard);
            }
        });

        ImageButton btnCheck = (ImageButton) findViewById(R.id.btnCheck);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdapterCard adapterCard=new AdapterCard(ListTareasCheck);
                recyclerView.setAdapter(adapterCard);
            }
        });

        ImageButton btnCruz = (ImageButton) findViewById(R.id.btnCruz);

        btnCruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdapterCard adapterCard=new AdapterCard(ListTareasCruz);
                recyclerView.setAdapter(adapterCard);
            }
        });

        ImageView verTareas = (ImageView) findViewById(R.id.button2);

        verTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ListadoTareasActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
