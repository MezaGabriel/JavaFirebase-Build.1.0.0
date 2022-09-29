package com.xGabrielx.appinventario.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xGabrielx.appinventario.Modelos.Producto;
import com.xGabrielx.appinventario.Modelos.Proveedor;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.productosViewHolder> {

    private static final String PATH_PROD = "Productos";
    private static final String PATH_PROV = "Proveedores";
    Context context;
    ArrayList<Producto> listaProductos;
    ArrayList<String> listaProveedores = new ArrayList<>();
    Spinner spiProveedor;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_PROD);

    public AdapterProductos(Context context, ArrayList<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public AdapterProductos.productosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_producto, null, false);
        return new AdapterProductos.productosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterProductos.productosViewHolder holder, final int position) {

        holder.tvIdProducto.setText(listaProductos.get(position).getIdProducto());
        holder.tvNomProducto.setText(listaProductos.get(position).getNomProducto());
        //holder.tvNomImagen.setText(listaProductos.get(position).getImagenProducto());
        holder.tvDescripcion.setText(listaProductos.get(position).getDescripcion());
        holder.tvNomProveedor.setText(listaProductos.get(position).getNomProveedor());
        holder.tvModelo.setText(listaProductos.get(position).getModelo());
        holder.tvPrecio.setText(""+listaProductos.get(position).getPrecio());
        holder.tvAlmacen.setText(""+listaProductos.get(position).getAlmacen());



        holder.ibtnAcciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.ibtnAcciones);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        if(item.getTitle().equals("Editar")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                            builder.setTitle("Actualizar Producto");
                            View dialogVista = inflater.inflate(R.layout.alert_dialog_productos, null);
                            builder.setView(dialogVista);

                            final EditText etCodigo = dialogVista.findViewById(R.id.etCodigo);
                            final ImageButton ibtnEscaner = dialogVista.findViewById(R.id.ibtnEscaner);
                            final EditText etNomProducto = dialogVista.findViewById(R.id.etNomProducto);
                            final EditText etDescripcion = dialogVista.findViewById(R.id.etDescripcion);
                            final Spinner spiProveedor = dialogVista.findViewById(R.id.spiProveedor);
                            final EditText etModelo = dialogVista.findViewById(R.id.etModelo);
                            final EditText etPrecio = dialogVista.findViewById(R.id.etPrecio);
                            final EditText etAlmacen = dialogVista.findViewById(R.id.etAlmacen);

                            etCodigo.setEnabled(false);
                            ibtnEscaner.setVisibility(View.INVISIBLE);

                            etCodigo.setText(listaProductos.get(position).getIdProducto());
                            etNomProducto.setText(listaProductos.get(position).getNomProducto());
                            etDescripcion.setText(listaProductos.get(position).getDescripcion());

                            obtenerProveedores();
                            spiProveedor.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaProveedores));
                            for(int j = 0; j < listaProveedores.size() ; j++) {
                                if(listaProveedores.get(j).equals(listaProductos.get(position).getNomProveedor())) {
                                    spiProveedor.setSelection(j);
                                }
                            }

                            etModelo.setText(listaProductos.get(position).getModelo());
                            etPrecio.setText(""+listaProductos.get(position).getPrecio());
                            etAlmacen.setText(""+listaProductos.get(position).getAlmacen());

                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(etNomProducto.getText().toString().equals("") ||
                                            etDescripcion.getText().toString().equals("") ||
                                            etModelo.getText().toString().equals("") ||
                                            etPrecio.getText().toString().equals("") ||
                                            etAlmacen.getText().toString().equals("")
                                    ){
                                        Toast.makeText(context, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();
                                    } else {

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                Map<String, Object> registro = new HashMap<>();

                                                registro.put("nomProducto", etNomProducto.getText().toString());
                                                registro.put("descripcion", etDescripcion.getText().toString());
                                                //registro.put("nomProveedor", spiProveedor.getSelectedItem().toString());
                                                registro.put("modelo", etModelo.getText().toString());
                                                registro.put("precio", etPrecio.getText().toString());
                                                registro.put("almacen", etAlmacen.getText().toString());

                                                reference.child(listaProductos.get(position).getIdProd()).updateChildren(registro);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        ((Activity)context).finish();
                                        context.startActivity(((Activity)context).getIntent());
                                    }
                                }
                            });
                            builder.show();

                        } else if(item.getTitle().equals("Eliminar")) {

                            AlertDialog.Builder dialogo = new AlertDialog.Builder(context);

                            dialogo.setTitle("ELIMINAR");
                            dialogo.setMessage("¿Estas seguro que deseas eliminar el elemento?");
                            dialogo.setCancelable(false);

                            dialogo.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {

                                    reference.child(listaProductos.get(position).getIdProd()).removeValue();

                                    listaProductos.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {

                                }
                            });
                            dialogo.show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public void obtenerProveedores() {
        listaProveedores.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_PROV);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Proveedor proveedor = snapshot.getValue(Proveedor.class);


                if (!Proveedor.ITEMS.contains(listaProveedores)) {
                    listaProveedores.add(proveedor.nomProveedor);
                    // Muestra los proveedores en el Spinner
                    //spiProveedor.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaProveedores));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Proveedor proveedor = snapshot.getValue(Proveedor.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Proveedor proveedor = snapshot.getValue(Proveedor.class);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class productosViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdProducto, tvNomProducto, tvDescripcion, tvNomProveedor, tvModelo, tvPrecio, tvAlmacen;
        ImageButton ibtnAcciones;

        public productosViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAlmacen = itemView.findViewById(R.id.tvAlmacen);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvIdProducto = itemView.findViewById(R.id.tvIdProducto);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvNomProducto = itemView.findViewById(R.id.tvNomProducto);
            tvNomProveedor = itemView.findViewById(R.id.tvNomProveedor);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ibtnAcciones = itemView.findViewById(R.id.ibtnAcciones);

        }
    }
}
