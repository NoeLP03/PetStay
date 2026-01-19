package com.petstay.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CuidadorAdapter extends RecyclerView.Adapter<CuidadorAdapter.ViewHolder> {

    private List<Cuidador> listaCuidadores;
    private Context context;


    public CuidadorAdapter(List<Cuidador> listaCuidadores, Context context) {
        this.listaCuidadores = listaCuidadores;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuidador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cuidador cuidador = listaCuidadores.get(position);
        holder.txtNombre.setText(cuidador.getNombre());
        holder.txtCiudad.setText("📍 " + cuidador.getCiudad());
        holder.txtDetalles.setText("🐾 Acepta: " + cuidador.getAcepta() + " | Capacidad: " + cuidador.getCapacidad());

        // --- AQUÍ AGREGAMOS EL CLIC ---
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ActivityCita.class);
            // "nombre_cuidador" es la llave para identificar el dato
            intent.putExtra("nombre_cuidador", cuidador.getNombre());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaCuidadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCiudad, txtDetalles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreCuidador);
            txtCiudad = itemView.findViewById(R.id.txtCiudadCuidador);
            txtDetalles = itemView.findViewById(R.id.txtDetallesCuidador);
        }
    }
}