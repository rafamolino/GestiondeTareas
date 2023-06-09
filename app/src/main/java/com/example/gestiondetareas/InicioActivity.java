package com.example.gestiondetareas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class InicioActivity extends AppCompatActivity {

    private static final int NOTIFICATION_REQUEST_CODE = 1;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String NOTIFICATION_ENABLED_KEY = "NotificationEnabled";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_main);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        TextView verTareas = (TextView) findViewById(R.id.verTareas);
        TextView labelUser = (TextView) findViewById(R.id.labelUser);
        String correo= currentUser.getEmail();

        // Lee el estado actual de las notificaciones desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED_KEY, false);

        if (notificationEnabled) {
            // Programa la notificación diaria a las 9 AM
            scheduleNotification(this);
        }

        RecyclerView contenedorCategorias = (RecyclerView) findViewById(R.id.contenedorCategorias);
        contenedorCategorias.setLayoutManager(new GridLayoutManager(this, 2));

        DocumentReference docRef = db.collection("usuarios").document(correo);
        //Lista de Tareas
        ArrayList<Map<String,Object>> ListTareas = new ArrayList<>();
        AtomicInteger tareasPendientes = new AtomicInteger(0);
        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            Integer contador=0;
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.forEach(d -> {

                    if(d.getId().contains(correo)){
                        ListTareas.add(d.getData());
                        if(d.get("estado").toString()=="true"){
                            Log.d("Tareas", tareasPendientes.toString());
                            tareasPendientes.incrementAndGet();
                        }


                    }
                });
                Log.d("Tareas", "Num tareas: " + tareasPendientes.toString());
                verTareas.setText("Tienes "+ tareasPendientes.toString() + " tareas pendientes");

            }


        });



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

        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                HashMap<String, int[]> categorias = new HashMap<>();

                queryDocumentSnapshots.forEach(d -> {

                    if(d.getId().contains(correo)){

                        String categoria = d.get("categoriaTarea").toString();
                        String estado = d.get("estado").toString();

                        Log.d("Tareas", "categoria: " + categoria + " y estado: "+ estado);
                        if(categorias.containsKey(categoria)){
                            int[] estadoContador = categorias.get(categoria);
                            if (estado=="true") {
                                estadoContador[0]++; // Contador para estado "true"
                            } else {
                                estadoContador[1]++; // Contador para estado "false"
                            }

                        }
                        else{
                            int[] estadoContador = new int[2];
                            if (estado=="true") {
                                estadoContador[0] = 1; // Contador para estado "true"
                                estadoContador[1] = 0; // Contador para estado "false"
                            } else {
                                estadoContador[0] = 0; // Contador para estado "true"
                                estadoContador[1] = 1; // Contador para estado "false"
                            }
                            categorias.put(categoria, estadoContador);

                        }

                    }





                });

                CategoriaAdapter adapterCard=new CategoriaAdapter(categorias);
                contenedorCategorias.setAdapter(adapterCard);
                for (String categoria : categorias.keySet()) {
                    int[] estadoContador = categorias.get(categoria);
                    Log.d("CATEGORIA_COMPLETA", categoria + " true:" + estadoContador[0] + " false:" + estadoContador[1]);
                }



            }
        });



        // Configura el RecyclerView
       /* recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriaAdapter = new CategoriaAdapter(listaCategorias);
        recyclerView.setAdapter(categoriaAdapter);*/

        verTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(InicioActivity.this, EditarTareas.class);
                startActivity(intent);
            }
        });

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

        ImageButton btnTasks = (ImageButton) findViewById(R.id.imageButton);
        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(InicioActivity.this, ListadoTareasActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnAnalisis = (ImageButton) findViewById(R.id.imageButton2);
        btnAnalisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(InicioActivity.this, Analisis.class);
                startActivity(intent);
            }
        });


    }

    private void scheduleNotification(Context context) {
        Intent intent = new Intent(context, MyFirebaseMessagingReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Obtén una instancia de AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Configura el calendario para la hora deseada (9 AM)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 46);
        calendar.set(Calendar.SECOND, 30);

        Log.d("Tareas", calendar.toString());
        // Programa la notificación diaria a las 9 AM
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }




}

