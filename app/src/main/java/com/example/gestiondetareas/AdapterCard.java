package com.example.gestiondetareas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolderCard> {
    ArrayList<Map<String,Object>> ListTareas;

    public AdapterCard(ArrayList<Map<String,Object>> listTareas) {
        ListTareas = listTareas;
    }

    @NonNull
    @Override
    public AdapterCard.ViewHolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtarea,null,false);
        return new ViewHolderCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCard.ViewHolderCard holder, int position) {
        holder.asignarDatos(ListTareas.get(position));
    }

    @Override
    public int getItemCount() {
        return ListTareas.size();
    }

    public class ViewHolderCard extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView estado;
        TextView categoria;
        TextView fecha;
        TextView descripcion;
        LinearLayout cardTarea;
        public ViewHolderCard(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.tareaView);
            //estado = (TextView) itemView.findViewById(R.id.estadoView);
            categoria = (TextView) itemView.findViewById(R.id.categoriaView);
            fecha = (TextView) itemView.findViewById(R.id.fechaView);
            //descripcion = (TextView) itemView.findViewById(R.id.descripcionView);
            cardTarea = (LinearLayout) itemView.findViewById(R.id.cardTarea);
        }

        public void asignarDatos(Map<String, Object> dato) {

            if(Boolean.valueOf(String.valueOf(dato.get("estado")))){
                cardTarea.setBackgroundResource(R.drawable.card_shape);
            }
            else{
                cardTarea.setBackgroundResource(R.drawable.card_shape2);

            }
            titulo.setText(String.valueOf(dato.get("nombreTarea")));
            //estado.setText(String.valueOf(dato.get("estado")));
            categoria.setText(String.valueOf(dato.get("categoriaTarea")));
            fecha.setText(String.valueOf(dato.get("fechaTarea")));
            //descripcion.setText(String.valueOf(dato.get("descripcionTarea")));


        }
    }
}
