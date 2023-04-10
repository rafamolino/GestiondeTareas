package com.example.gestiondetareas.db;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondetareas.R;

import java.util.List;

public class UsuarioAdapter{

    private List<Categoria> listaCategorias;

    public UsuarioAdapter(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

   /* @NonNull
    @Override
    public UsuarioAdapter.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_categoria, parent, false);
        return new UsuarioAdapter.UsuarioViewHolder(view);
    }*/

    /*@Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.CategoriaViewHolder holder, int position) {
        Categoria categoria = listaCategorias.get(position);
        holder.tvNombre.setText(categoria.getNombre());
        holder.tvDescripcion.setText(categoria.getContador());
    }*/

    /*@Override
    public int getItemCount() {
        return listaCategorias.size();
    }*/

   /* static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvDescripcion;

        UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }*/
}
