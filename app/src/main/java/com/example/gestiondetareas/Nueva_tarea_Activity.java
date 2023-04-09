package com.example.gestiondetareas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Nueva_tarea_Activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_tarea_main);

        ImageButton btnInicio = (ImageButton) findViewById(R.id.button2);
        btnInicio.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent= new Intent(Nueva_tarea_Activity.this, InicioActivity.class);
                startActivity(intent);
            }
        });


    }
}
