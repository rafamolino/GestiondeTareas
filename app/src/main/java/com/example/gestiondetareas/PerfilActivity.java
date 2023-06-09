package com.example.gestiondetareas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String NOTIFICATION_ENABLED_KEY = "NotificationEnabled";

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Switch switchNotificaciones;
    private boolean notificationEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        TextView labelUser = (TextView) findViewById(R.id.txtEmailProfile);
        TextView labelName = (TextView) findViewById(R.id.txtNameProfile);
        String correo= currentUser.getEmail();
        labelUser.setText(correo);
        DocumentReference docRef = db.collection("usuarios").document(correo);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       labelName.setText(document.getString("nombre")+ " " + document.getString("apellidos"));
                    } else {
                        labelName.setText("");
                    }
                } else {
                    labelName.setText("Error");
                }
            }
        });

        Button btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager preferenceManager = new PreferenceManager(PerfilActivity.this);
                preferenceManager.clear();

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

        ImageButton btnVolver = (ImageButton) findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switchNotificaciones = findViewById(R.id.switchNotificaciones);

        // Lee el estado actual de las notificaciones desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED_KEY, false);
        switchNotificaciones.setChecked(notificationEnabled);

        switchNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationEnabled = isChecked;

                // Guarda el estado de las notificaciones en SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(NOTIFICATION_ENABLED_KEY, notificationEnabled);
                editor.apply();

                if (notificationEnabled) {
                    Toast.makeText(PerfilActivity.this, "Notificaciones habilitadas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PerfilActivity.this, "Notificaciones deshabilitadas", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
