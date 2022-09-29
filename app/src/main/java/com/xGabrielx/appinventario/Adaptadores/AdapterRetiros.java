package com.xGabrielx.appinventario.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.xGabrielx.appinventario.Modelos.Retiros;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterRetiros extends RecyclerView.Adapter<AdapterRetiros.retirosViewHolder>{
    private static final String PATH_RET = "Retiros";
    Context context;
    ArrayList<Retiros> listaRetiros;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_RET);



    public AdapterRetiros(Context context, ArrayList<Retiros> listaRetiros) {
        this.context = context;
        this.listaRetiros = listaRetiros;

    }

    @Override
    public retirosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_retiros, null, false);
        return new AdapterRetiros.retirosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterRetiros.retirosViewHolder holder, final int position) {

        holder.tvNomPersona.setText(listaRetiros.get(position).getNomPersona());


        /*holder.ibtnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + listaRetiros.get(position).getCantidad()));
                context.startActivity(intent);
            }
        });

        holder.ibtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {listaRetiros.get(position).getObservacion()}; //aquí pon tu correo
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
        });*/

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
                            builder.setTitle("Actualizar Retiros");
                            View dialogVista = inflater.inflate(R.layout.alert_dialog_retiros, null);
                            builder.setView(dialogVista);

                            final EditText etNomPersona = dialogVista.findViewById(R.id.etNomPersona);
                            final EditText etCantidad = dialogVista.findViewById(R.id.etCantidad);
                            final EditText etObservacion = dialogVista.findViewById(R.id.etObservacion);

                            etNomPersona.setText(listaRetiros.get(position).getNomPersona());
                            etCantidad.setText(listaRetiros.get(position).getCantidad());
                            etObservacion.setText(listaRetiros.get(position).getObservacion());

                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(etNomPersona.getText().toString().equals("") || etCantidad.getText().toString().equals("") || etObservacion.getText().toString().equals("")) {
                                        Toast.makeText(context, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();
                                    } else {

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                Retiros retiros = snapshot.getValue(Retiros.class);
                                                //retiros.setId(snapshot.getKey());
                                                Map<String, Object> registro = new HashMap<>();

                                                registro.put("nomPersona", etNomPersona.getText().toString());
                                                registro.put("Cantidad", etCantidad.getText().toString());
                                                registro.put("Observacion", etObservacion.getText().toString());

                                                reference.child(listaRetiros.get(position).getId()).updateChildren(registro);

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

                                    reference.child(listaRetiros.get(position).getId()).removeValue();

                                    listaRetiros.remove(position);
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
        return listaRetiros.size();
    }

    public class retirosViewHolder extends RecyclerView.ViewHolder {


        TextView tvNomPersona;
        ImageButton ibtnTelefono, ibtnEmail, ibtnAcciones;


        public retirosViewHolder(View itemView) {
            super(itemView);

            tvNomPersona = itemView.findViewById(R.id.tvNomProveedor);
            ibtnTelefono = itemView.findViewById(R.id.ibtnTelefono);
            ibtnEmail = itemView.findViewById(R.id.ibtnEmail);
            ibtnAcciones = itemView.findViewById(R.id.ibtnAcciones);

        }
    }
}
