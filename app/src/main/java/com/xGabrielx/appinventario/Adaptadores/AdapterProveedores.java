package com.xGabrielx.appinventario.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xGabrielx.appinventario.Modelos.Proveedor;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterProveedores extends RecyclerView.Adapter<AdapterProveedores.proveedorViewHolder> {

    private static final String PATH_PROV = "Proveedores";
    Context context;
    ArrayList<Proveedor> listaProveedor;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_PROV);



    public AdapterProveedores(Context context, ArrayList<Proveedor> listaProveedor) {
        this.context = context;
        this.listaProveedor = listaProveedor;

    }

    @Override
    public proveedorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_proveedor, null, false);
        return new AdapterProveedores.proveedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final proveedorViewHolder holder, final int position) {

        holder.tvNomProveedor.setText(listaProveedor.get(position).getNomProveedor());


        holder.ibtnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + listaProveedor.get(position).getTelefono()));
                context.startActivity(intent);
            }
        });

        holder.ibtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {listaProveedor.get(position).getEmail()}; //aquí pon tu correo
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu mensaje");

                try {
                    context.startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                            builder.setTitle("Actualizar Proveedor");
                            View dialogVista = inflater.inflate(R.layout.alert_dialog_proveedores, null);
                            builder.setView(dialogVista);

                            final EditText etNomProveedor = dialogVista.findViewById(R.id.etNomProveedor);
                            final EditText etTelefono = dialogVista.findViewById(R.id.etTelefono);
                            final EditText etEmail = dialogVista.findViewById(R.id.etEmail);

                            etNomProveedor.setText(listaProveedor.get(position).getNomProveedor());
                            etTelefono.setText(listaProveedor.get(position).getTelefono());
                            etEmail.setText(listaProveedor.get(position).getEmail());

                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(etNomProveedor.getText().toString().equals("") || etTelefono.getText().toString().equals("") || etEmail.getText().toString().equals("")) {
                                        Toast.makeText(context, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();
                                    } else {

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                Proveedor proveedor = snapshot.getValue(Proveedor.class);
                                                Map<String, Object> registro = new HashMap<>();

                                                registro.put("nomProveedor", etNomProveedor.getText().toString());
                                                registro.put("telefono", etTelefono.getText().toString());
                                                registro.put("email", etEmail.getText().toString());

                                                reference.child(listaProveedor.get(position).getId()).updateChildren(registro);

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

                                    reference.child(listaProveedor.get(position).getId()).removeValue();

                                    listaProveedor.remove(position);
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

    @Override
    public int getItemCount() {
        return listaProveedor.size();
    }

    public class proveedorViewHolder extends RecyclerView.ViewHolder {


        TextView tvNomProveedor;
        ImageButton ibtnTelefono, ibtnEmail, ibtnAcciones;


        public proveedorViewHolder(View itemView) {
            super(itemView);

            tvNomProveedor = itemView.findViewById(R.id.tvNomProveedor);
            ibtnTelefono = itemView.findViewById(R.id.ibtnTelefono);
            ibtnEmail = itemView.findViewById(R.id.ibtnEmail);
            ibtnAcciones = itemView.findViewById(R.id.ibtnAcciones);

        }
    }

}
