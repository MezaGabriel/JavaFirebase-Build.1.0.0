package com.xGabrielx.appinventario.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xGabrielx.appinventario.Modelos.Producto;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;

public class AdapterVentas extends RecyclerView.Adapter<AdapterVentas.ventasViewHolder> {

    private static final String PATH_PROD = "Productos";
    Context context;
    ArrayList<Producto> listaProductos;
    TextView tvTotalVenta;

    double  suma = 0.0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_PROD);

    public AdapterVentas(Context context, ArrayList<Producto> listaProductos, TextView tvTotal) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.tvTotalVenta = tvTotal;
    }

    @NonNull
    @Override
    public AdapterVentas.ventasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_venta, null, false);
        return new AdapterVentas.ventasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVentas.ventasViewHolder holder, final int position) {

        //holder.tvCantidad.setText(""+listaProductos.get(position).getCantidad());
        holder.tvIdProducto.setText(listaProductos.get(position).getIdProducto());
        holder.tvNomProducto.setText(listaProductos.get(position).getNomProducto());
        holder.tvPrecio.setText(""+listaProductos.get(position).getPrecio());

        double aux = /*listaProductos.get(position).getCantidad() **/ Double.parseDouble(listaProductos.get(position).getPrecio());
        //holder.tvTotal.setText(""+aux);

        holder.ibtnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaProductos.remove(position);
                notifyDataSetChanged();
                suma = 0.0;
            }
        });

        suma += aux;
        tvTotalVenta.setText(""+suma);

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ventasViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdProducto, tvNomProducto, tvCantidad, tvPrecio, tvTotal;
        ImageButton ibtnEliminar, ibtnRestar;

        public ventasViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIdProducto = itemView.findViewById(R.id.tvIdProducto);
            tvNomProducto = itemView.findViewById(R.id.tvNomProducto);
            //tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            ibtnEliminar = itemView.findViewById(R.id.ibtnEliminar);
            //ibtnRestar = itemView.findViewById(R.id.ibtnRestar);

        }
    }
}
