package com.xGabrielx.appinventario.Vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xGabrielx.appinventario.Adaptadores.AdapterRetiros;
import com.xGabrielx.appinventario.Modelos.Retiros;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

public class VistaCotizaciones extends AppCompatActivity {

    private static final String PATH_RET = "Retiros";

    EditText etBusqueda;
    ImageButton ibtnAdd;
    RecyclerView rvRetiros;

    ArrayList<Retiros> listaRetiros = new ArrayList<>();
    ArrayList<Retiros> lista = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_RET);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_cotizaciones);
        getSupportActionBar().setTitle("RETIROS");

        etBusqueda = findViewById(R.id.etBusqueda);
        ibtnAdd = findViewById(R.id.ibtnAdd);
        rvRetiros = findViewById(R.id.rvRetiros);
        rvRetiros.setLayoutManager(new GridLayoutManager(this, 1));



        // Metodo para obtener los proveedores
        obtenerRetiros();

        // Lectura de eventos del EditText para la busqueda
        etBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lista.clear();


                if (etBusqueda.getText().toString().trim().equals("")) {
                    AdapterRetiros adaptador = new AdapterRetiros(VistaCotizaciones.this, listaRetiros);
                    rvRetiros.setAdapter(adaptador);
                } else {

                    for (int i = 0; i < listaRetiros.size(); i++) {
                        if (etBusqueda.getText().toString().trim().equalsIgnoreCase(listaRetiros.get(i).getNomPersona().trim())) {
                            lista.add(listaRetiros.get(i));
                        }
                    }

                    AdapterRetiros adaptador = new AdapterRetiros(VistaCotizaciones.this, lista);
                    rvRetiros.setAdapter(adaptador);
                }
            }
        });

        // Accion para el ImageButton
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VistaCotizaciones.this);
                LayoutInflater inflater = VistaCotizaciones.this.getLayoutInflater();
                builder.setTitle("Agregar Retiros");
                View dialogVista = inflater.inflate(R.layout.alert_dialog_retiros, null);
                builder.setView(dialogVista);

                final EditText etNomPersona = dialogVista.findViewById(R.id.etNomPersona);
                final EditText etCantidad = dialogVista.findViewById(R.id.etCantidad);
                final EditText etObservacion = dialogVista.findViewById(R.id.etObservacion);

                // Accion para cuando se pulsa aceptar en el AlertDialog
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Si falta algun campo se manda un aviso informando
                        if (etNomPersona.getText().toString().equals("") || etCantidad.getText().toString().equals("") || etObservacion.getText().toString().equals("")) {

                            Toast.makeText(VistaCotizaciones.this, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();

                        } else { // Si se cumplen los campos registramos en la base de datos

                            //listaRetiros.clear();

                            //Retiros registro = new Retiros(ITEMS.get(item).id, etNomPersona.getText().toString().trim(), etCantidad.getText().toString().trim(), etObservacion.getText().toString().trim());

                            Map<String, Object> registro = new HashMap<>();

                            registro.put("nomPersona", etNomPersona.getText().toString());
                            registro.put("Cantidad", etCantidad.getText().toString());
                            registro.put("Observacion", etObservacion.getText().toString());

                            // los inserto en la base de datos
                            reference.push().setValue(registro);

                            Toast.makeText(VistaCotizaciones.this, "Datos agregados", Toast.LENGTH_LONG).show();

                            //obtenerRetiros();

                            /*AdapterRetiros adaptador = new AdapterRetiros(VistaCotizaciones.this, listaRetiros);
                            rvRetiros.setAdapter(adaptador);*/

                        }
                    }

                });
                builder.show();
            }
        });
    }

    // Metodo para obtener los proveedores
    public void obtenerRetiros() {
        //listaRetiros.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_RET);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Retiros retiros = snapshot.getValue(Retiros.class);
                retiros.setId(snapshot.getKey());


                if (!Retiros.ITEMS.contains(listaRetiros)) {
                    listaRetiros.add(retiros);
                    AdapterRetiros adaptador = new AdapterRetiros(VistaCotizaciones.this, listaRetiros);
                    rvRetiros.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Retiros retiros = snapshot.getValue(Retiros.class);
                //proveedor.setId(snapshot.getKey());


                if (Retiros.ITEMS.contains(listaRetiros)) {
                    Retiros.updateItem(retiros);
                    AdapterRetiros adaptador = new AdapterRetiros(VistaCotizaciones.this, listaRetiros);
                    rvRetiros.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Retiros retiros = snapshot.getValue(Retiros.class);
                //proveedor.setId(snapshot.getKey());


                if (Retiros.ITEMS.contains(listaRetiros)) {
                    Retiros.deleteItem(retiros);
                    AdapterRetiros adaptador = new AdapterRetiros(VistaCotizaciones.this, listaRetiros);
                    rvRetiros.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Toast.makeText(VistaProveedores.this, ""+listaProveedores.size(), Toast.LENGTH_SHORT).show();
        /*AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, listaProveedores);
        rvProveedores.setAdapter(adaptador);*/
    }

    @OnClick(R.id.ibtnAdd)
    public void onClick() {
    }
}
