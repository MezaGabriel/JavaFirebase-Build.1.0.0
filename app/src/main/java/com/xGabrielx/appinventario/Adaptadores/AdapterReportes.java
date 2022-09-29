package com.xGabrielx.appinventario.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xGabrielx.appinventario.Modelos.Reportes;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;

public class AdapterReportes extends RecyclerView.Adapter<AdapterReportes.reportesViewHolder> {

    Context context;
    ArrayList<Reportes> listaReportes;

    public AdapterReportes(Context context, ArrayList<Reportes> listaReportes) {
        this.context = context;
        this.listaReportes = listaReportes;
    }

    @NonNull
    @Override
    public reportesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_reporte, null, false);
        return new AdapterReportes.reportesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reportesViewHolder holder, int position) {

        holder.tvFecha.setText(listaReportes.get(position).getFechaVenta());
        holder.tvTotal.setText(""+listaReportes.get(position).getTotal());

    }


    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public class reportesViewHolder extends RecyclerView.ViewHolder {

        TextView tvFecha,/*, tvCodProductos, tvNomProducto, tvCantidad, tvPrecio, */tvTotal;

        public reportesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFecha = itemView.findViewById(R.id.tvFechaVenta);
            tvTotal = itemView.findViewById(R.id.tvTotal);

        }
    }
}
