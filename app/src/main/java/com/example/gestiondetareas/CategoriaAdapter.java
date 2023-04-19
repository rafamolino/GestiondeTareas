package com.example.gestiondetareas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    private HashMap<String, int[]> categorias;

    public CategoriaAdapter(HashMap<String, int[]> categorias) {
        this.categorias = categorias;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public ProgressBar tv3;

        public ViewHolder(View v) {
            super(v);
            tv1 = v.findViewById(R.id.tvNombre);
            tv2 = v.findViewById(R.id.tvDescripcion);
            tv3 = v.findViewById(R.id.progresoTareas);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_categoria, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String categoria = (String) categorias.keySet().toArray()[position];
        int[] estadoContador = categorias.get(categoria);

        holder.tv1.setText(categoria);
        holder.tv2.setText("Completadas: " + estadoContador[1]+"/"+ (estadoContador[0]+estadoContador[1]));
        holder.tv3.setProgress((estadoContador[1]*100)/(estadoContador[0]+estadoContador[1]));
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
}

