package com.example.gestiondetareas;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class EditarTareas extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_tareas);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();

        recyclerView = (RecyclerView) findViewById(R.id.ContenedorTareas2);
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
                CardEdit adapterCard=new CardEdit(ListTareas);
                recyclerView.setAdapter(adapterCard);
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            // Calcular la posición de la vista personalizada
                            View itemView = viewHolder.itemView;
                            float width = (float) itemView.getWidth();
                            float alpha = 1.0f - Math.abs(dX) / width;
                            float textSize = 80f;
                            float textPadding = 50f;
                            Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            paint.setTextSize(textSize);
                            paint.setAlpha((int) (255 * alpha));
                            paint.setTextAlign(Paint.Align.LEFT); // alinear el texto a la izquierda
                            paint.setTypeface(Typeface.DEFAULT_BOLD); // establecer el tipo de fuente en negrita
                            paint.setStyle(Paint.Style.FILL);
                            paint.setShadowLayer(10f, 10f, 10f, Color.BLACK); // agregar sombra al texto

                            // Dibujar la vista personalizada en la posición de la tarjeta
                            c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);
                            c.drawText("Eliminar tarea", itemView.getLeft() + textPadding, itemView.getTop() + itemView.getHeight() / 2f + textSize / 2f, paint); // establecer la posición del texto en la posición central de la "card"
                        }
                    }



                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        adapterCard.deleteCard(viewHolder.getAdapterPosition());
                        if(ListTareas.get(viewHolder.getAdapterPosition()).get("estado").toString().equals("false")){
                            ListTareasCheck.remove(viewHolder.getAdapterPosition());

                        }else{
                            ListTareasCruz.remove(viewHolder.getAdapterPosition());

                        }
                    }
                }).attachToRecyclerView(recyclerView);


                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            // Calcular la posición de la vista personalizada
                            View itemView = viewHolder.itemView;
                            float width = (float) itemView.getWidth();
                            float alpha = 1.0f - Math.abs(dX) / width;
                            float textSize = 80f;
                            float textPadding = 50f;
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setTextSize(textSize);
                            paint.setTypeface(Typeface.DEFAULT_BOLD); // establecer el tipo de fuente en negrita
                            paint.setStyle(Paint.Style.FILL);
                            paint.setShadowLayer(10f, 10f, 10f, Color.BLACK); // agregar sombra al texto
                            paint.setAlpha((int) (255 * alpha));
                            paint.setTextAlign(Paint.Align.RIGHT); // alinear el texto a la derecha

                            // Dibujar la vista personalizada en la posición de la tarjeta
                            c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);
                            c.drawText("Cambiar estado", itemView.getRight() - textPadding, itemView.getTop() + (itemView.getHeight() / 2f) + (textSize / 2f), paint); // establecer la posición del texto a la derecha
                        }
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        adapterCard.changeStatus(viewHolder.getAdapterPosition());

                    }
                }).attachToRecyclerView(recyclerView);


            }

        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

