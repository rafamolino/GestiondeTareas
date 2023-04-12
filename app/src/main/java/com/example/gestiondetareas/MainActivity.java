package com.example.gestiondetareas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistro = (Button) findViewById(R.id.btnReg);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        setup();


    }

    private void setup(){

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        EditText edtCorreo = (EditText) findViewById(R.id.edtLoginEmail);
        EditText edtContraseña = (EditText) findViewById(R.id.edtLoginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtCorreo.getText().toString()) && !TextUtils.isEmpty(edtContraseña.getText().toString())){

                    FirebaseAuth.getInstance().
                            signInWithEmailAndPassword(edtCorreo.getText().toString(), edtContraseña.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Intent intent= new Intent(MainActivity.this, InicioActivity.class);
                                        intent.putExtra("email", task.getResult().getUser().getEmail());
                                        startActivity(intent);


                                    }else {

                                        // La tarea se completó con un error
                                        Exception exception = task.getException();
                                        String mensajeError = "Contraseña incorrecta o usuario no registrado.";

                                        // Muestra una alerta en la pantalla con el mensaje de error
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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