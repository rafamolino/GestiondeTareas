package com.example.gestiondetareas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolderCard> {
    ArrayList<Map<String,Object>> ListTareas;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public AdapterCard(ArrayList<Map<String,Object>> listTareas) {
        ListTareas = listTareas;
    }

    @NonNull
    @Override
    public AdapterCard.ViewHolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtarea,null,false);
        return new ViewHolderCard(view);
    }

    public void changeStatus(int position){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();
        Map<String, Object> tarea = ListTareas.get(position);
        Log.d("Tareas", correo);
        Log.d("Tareas",  tarea.get("nombreTarea").toString());
        db.collection("tareas")
                .whereEqualTo("correoUsuario", correo)
                .whereEqualTo("nombreTarea", tarea.get("nombreTarea"))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        boolean estadoActual = (boolean) tarea.get("estado");
                        documentSnapshot.getReference().update("estado", !estadoActual);
                        tarea.put("estado", !estadoActual);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AdapterCard", "Error al cambiar estado de tarea", e);
                });


    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCard.ViewHolderCard holder, int position) {

        holder.asignarDatos(ListTareas.get(position));
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();
        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                HashMap<String, int[]> categorias = new HashMap<>();

                queryDocumentSnapshots.forEach(d -> {

                    if(d.getId().contains(correo)){
                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        queryDocumentSnapshots.forEach(d -> {

                            if(d.getId().contains(correo)){
                                if (d.get("nombreTarea").toString().equals(holder.titulo.getText().toString())){
                                    d.getReference().delete();
                                    ListTareas.remove(holder.getAdapterPosition());
                                    notifyDataSetChanged();


                                }
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListTareas.size();
    }

    public class ViewHolderCard extends RecyclerView.ViewHolder {
        TextView titulo;
        ImageView estado;
        TextView estadoTxt;
        Context context;
        TextView categoria;
        ImageButton delete;
        TextView fecha;
        TextView descripcion;
        LinearLayout cardTarea;

        public ViewHolderCard(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.tareaView);
            estadoTxt = (TextView) itemView.findViewById(R.id.textView2);
            estado = (ImageView) itemView.findViewById(R.id.imageView24);
            context = itemView.getContext();
            delete = (ImageButton) itemView.findViewById(R.id.imageView7);
            categoria = (TextView) itemView.findViewById(R.id.categoriaView);
            fecha = (TextView) itemView.findViewById(R.id.fechaView);
            //descripcion = (TextView) itemView.findViewById(R.id.descripcionView);
            cardTarea = (LinearLayout) itemView.findViewById(R.id.cardTarea);
        }


        public void asignarDatos(Map<String, Object> dato) {

            if (Boolean.valueOf(String.valueOf(dato.get("estado")))) {
                cardTarea.setBackgroundResource(R.drawable.card_shape);
                titulo.setTextColor(Color.parseColor("#ff5232"));
                categoria.setTextColor(Color.parseColor("#ff5232"));
                estadoTxt.setTextColor(Color.parseColor("#ff5232"));
                estado.setColorFilter(Color.parseColor("#ff5232"));
                estado.setImageResource(R.drawable.baseline_close_24);
            } else {
                cardTarea.setBackgroundResource(R.drawable.card_shape2);
                titulo.setTextColor(Color.parseColor("#48a259"));
                categoria.setTextColor(Color.parseColor("#48a259"));

                estado.setImageResource(R.drawable.baseline_check_circle_outline_24);
                estado.setColorFilter(Color.parseColor("#48a259"));
                estadoTxt.setTextColor(Color.parseColor("#48a259"));
            }
            titulo.setText(String.valueOf(dato.get("nombreTarea")));
            //estado.setText(String.valueOf(dato.get("estado")));
            categoria.setText(String.valueOf(dato.get("categoriaTarea")));
            fecha.setText(String.valueOf(dato.get("fechaTarea")));
            //descripcion.setText(String.valueOf(dato.get("descripcionTarea")));


        }

    }
}
