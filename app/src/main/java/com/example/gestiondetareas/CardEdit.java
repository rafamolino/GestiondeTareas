package com.example.gestiondetareas;

import android.content.Context;
import android.graphics.Color;
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
import java.util.Map;

public class CardEdit extends RecyclerView.Adapter<CardEdit.ViewHolderCard> {
    ArrayList<Map<String,Object>> ListTareas;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public CardEdit(ArrayList<Map<String,Object>> listTareas) {
        ListTareas = listTareas;
    }

    public CardEdit.ViewHolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editcard,null,false);
        return new CardEdit.ViewHolderCard(view);
    }

    public void deleteCard(int position){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();
        Map<String, Object> tarea = ListTareas.get(position);
        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.forEach(d -> {
                    if(d.getId().contains(correo)){
                        if (d.get("nombreTarea").toString().equals(tarea.get("nombreTarea").toString())){
                            d.getReference().delete();
                            ListTareas.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

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

    public void onBindViewHolder(@NonNull CardEdit.ViewHolderCard holder, int position) {

        holder.asignarDatos(ListTareas.get(position));
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();



    }


    public int getItemCount() {
        return ListTareas.size();
    }

    public class ViewHolderCard extends RecyclerView.ViewHolder {
        TextView titulo;
        ImageView estado;
        TextView estadoTxt;
        Context context;
        ImageButton check;
        ImageButton elim;
        TextView categoria;
        ImageButton delete;
        TextView fecha;
        TextView descripcion;
        LinearLayout cardTarea;

        public ViewHolderCard(@NonNull View itemView) {
            super(itemView);
            check = (ImageButton) itemView.findViewById(R.id.imageButton5);
            elim = (ImageButton) itemView.findViewById(R.id.imageButton4);
            titulo = (TextView) itemView.findViewById(R.id.tareaView);
            estadoTxt = (TextView) itemView.findViewById(R.id.textView2);
            estado = (ImageView) itemView.findViewById(R.id.imageView24);
            context = itemView.getContext();

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
                check.setImageResource(R.drawable.baseline_check_circle_outline_24);
            } else {
                cardTarea.setBackgroundResource(R.drawable.card_shape2);
                titulo.setTextColor(Color.parseColor("#48a259"));
                categoria.setTextColor(Color.parseColor("#48a259"));
                check.setImageResource(R.drawable.baseline_close_24);
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
