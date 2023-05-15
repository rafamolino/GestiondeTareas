package com.example.gestiondetareas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nueva_tarea_Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> nombresDocumentos = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_tarea_main);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correoUsuarioLogueado= currentUser.getEmail();
        ImageButton fecha = (ImageButton) findViewById(R.id.imageButton3);
        EditText fechaEdt = (EditText) findViewById(R.id.edtFecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

            private void showDatePickerDialog() {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + " / " + (month+1) + " / " + year;

                        fechaEdt.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });



        db.collection("categorias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            nombresDocumentos.clear(); // Limpiar la lista antes de agregar nuevos nombres
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Obtén el nombre del documento (ID)
                                String nombreDocumento = document.getId();
                                // Agrega el nombre del documento a la lista
                                nombresDocumentos.add(nombreDocumento);
                            }

                            // Llama al método para configurar el adaptador del Spinner
                            configurarSpinner();
                        } else {
                            Log.d("TAG", "Error obteniendo documentos: ", task.getException());
                        }
                    }
                });

        Button cancelarTarea = (Button)  findViewById(R.id.cancelarTarea);

        cancelarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Nueva_tarea_Activity.this, InicioActivity.class);
                startActivity(intent);
            }
        });


        Button btnAñadirTarea = (Button) findViewById(R.id.btnAñadirTarea);

        btnAñadirTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtNombreTarea = (EditText) findViewById(R.id.edtNombreTarea);
                EditText edtDescripcionTarea = (EditText) findViewById(R.id.edtDescripcion);
                EditText edtFechaTarea = (EditText) findViewById(R.id.edtFecha);
                Spinner listaTareas = (Spinner) findViewById(R.id.listaCategorias);

                Map<String, Object> datos = new HashMap<>();
                datos.put("nombreTarea", edtNombreTarea.getText().toString());
                datos.put("descripcionTarea", edtDescripcionTarea.getText().toString());
                datos.put("categoriaTarea", listaTareas.getSelectedItem().toString());
                datos.put("fechaTarea", edtFechaTarea.getText().toString());
                datos.put("correoUsuario", correoUsuarioLogueado);
                datos.put("estado", true);




                db.collection("tareas").document(edtNombreTarea.getText().toString()+"("+correoUsuarioLogueado+")").set(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent intent= new Intent(Nueva_tarea_Activity.this, InicioActivity.class);
                            Toast.makeText(Nueva_tarea_Activity.this, "Tarea añadida correctamente", Toast.LENGTH_LONG).show();
                            startActivity(intent);

                        } else {
                            Toast.makeText(Nueva_tarea_Activity.this, "Error al añadir la tarea.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });








    }
    // Método para configurar el adaptador del Spinner
    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresDocumentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.listaCategorias);
        spinner.setAdapter(adapter);
    }
}
