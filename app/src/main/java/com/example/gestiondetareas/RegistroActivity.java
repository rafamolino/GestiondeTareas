package com.example.gestiondetareas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondetareas.db.DatabaseHelper;
import com.example.gestiondetareas.db.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper; // Declarar una variable para el objeto DatabaseHelper

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_main);

        setup();
    }

    private void setup(){

        Button btnRegistro = (Button) findViewById(R.id.signUpButton);
        EditText edtCorreo = (EditText) findViewById(R.id.edtEmail);
        EditText edtContraseña = (EditText) findViewById(R.id.edtPassword);
        EditText edtSurname = (EditText) findViewById(R.id.edtSurname);
        EditText edtName = (EditText) findViewById(R.id.edtName);


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtCorreo.getText().toString()) && !TextUtils.isEmpty(edtContraseña.getText().toString())){

                    FirebaseAuth.getInstance().
                            createUserWithEmailAndPassword(edtCorreo.getText().toString(), edtContraseña.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        dbHelper = new DatabaseHelper(RegistroActivity.this);
                                        Usuario user = new Usuario(edtName.getText().toString(), edtSurname.getText().toString(),edtCorreo.getText().toString());
                                        dbHelper.insertarUsuario(user);
                                        Intent intent= new Intent(RegistroActivity.this, InicioActivity.class);
                                        intent.putExtra("email", task.getResult().getUser().getEmail());
                                        startActivity(intent);


                                    }else {

                                        // La tarea se completó con un error
                                        Exception exception = task.getException();
                                        String mensajeError = "Se produjo un error: " + exception.getMessage();

                                        // Muestra una alerta en la pantalla con el mensaje de error
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                                        builder.setTitle("Error")
                                                .setMessage(mensajeError)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Haz algo cuando se presione el botón "Aceptar"
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();

                                    }
                                }
                            });

                }
            }
        });

    }


}
