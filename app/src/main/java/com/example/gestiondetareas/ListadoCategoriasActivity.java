package com.example.gestiondetareas;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ListadoCategoriasActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_categorias);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo = currentUser.getEmail();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ContenedorCategorias);
        String tv = getIntent().getStringExtra("tvNombre");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TextView label = (TextView) findViewById(R.id.labelCategoria);
        label.setText(tv);
        ArrayList<Map<String, Object>> ListTareas = new ArrayList<>();

        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.forEach(d -> {


                    if(d.getId().contains(correo) && d.getData().get("categoriaTarea").equals(tv)){
                        ListTareas.add(d.getData());

                    }
                });

                AdapterCard adapterCard=new AdapterCard(ListTareas);
                recyclerView.setAdapter(adapterCard);
            }
        });


    }
}
