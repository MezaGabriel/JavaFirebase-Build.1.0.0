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
import com.xGabrielx.appinventario.Adaptadores.AdapterProveedores;
import com.xGabrielx.appinventario.Modelos.Proveedor;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VistaProveedores extends AppCompatActivity {

    private static final String PATH_PROV = "Proveedores";

    EditText etBusqueda;
    ImageButton ibtnAdd;
    RecyclerView rvProveedores;

    ArrayList<Proveedor> listaProveedores = new ArrayList<>();
    ArrayList<Proveedor> lista = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_PROV);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_proveedores);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("PROVEEDORES");

        etBusqueda = findViewById(R.id.etBusqueda);
        ibtnAdd = findViewById(R.id.ibtnAdd);
        rvProveedores = findViewById(R.id.rvProveedores);
        rvProveedores.setLayoutManager(new GridLayoutManager(this, 1));



        // Metodo para obtener los proveedores
        obtenerProveedores();

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
                    AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, listaProveedores);
                    rvProveedores.setAdapter(adaptador);
                } else {

                    for (int i = 0; i < listaProveedores.size(); i++) {
                        if (etBusqueda.getText().toString().trim().equalsIgnoreCase(listaProveedores.get(i).getNomProveedor().trim())) {
                            lista.add(listaProveedores.get(i));
                        }
                    }

                    AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, lista);
                    rvProveedores.setAdapter(adaptador);
                }
            }
        });

        // Accion para el ImageButton
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VistaProveedores.this);
                LayoutInflater inflater = VistaProveedores.this.getLayoutInflater();
                builder.setTitle("Agregar Proveedor");
                View dialogVista = inflater.inflate(R.layout.alert_dialog_proveedores, null);
                builder.setView(dialogVista);

                final EditText etNomProveedor = dialogVista.findViewById(R.id.etNomProveedor);
                final EditText etTelefono = dialogVista.findViewById(R.id.etTelefono);
                final EditText etEmail = dialogVista.findViewById(R.id.etEmail);

                // Accion para cuando se pulsa aceptar en el AlertDialog
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Si falta algun campo se manda un aviso informando
                        if (etNomProveedor.getText().toString().equals("") || etTelefono.getText().toString().equals("") || etEmail.getText().toString().equals("")) {

                            Toast.makeText(VistaProveedores.this, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();

                        } else { // Si se cumplen los campos registramos en la base de datos

                            Map<String, Object> registro = new HashMap<>();

                            registro.put("nomProveedor", etNomProveedor.getText().toString());
                            registro.put("telefono", etTelefono.getText().toString());
                            registro.put("email", etEmail.getText().toString());

                            // los inserto en la base de datos
                            reference.push().setValue(registro);

                            Toast.makeText(VistaProveedores.this, "Datos agregados", Toast.LENGTH_LONG).show();

                            //obtenerProveedores();

                            AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, listaProveedores);
                            rvProveedores.setAdapter(adaptador);

                        }
                    }

                });
                builder.show();
            }
        });
    }

    // Metodo para obtener los proveedores
    public void obtenerProveedores() {
        //listaProveedores.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_PROV);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Proveedor proveedor = snapshot.getValue(Proveedor.class);
                proveedor.setId(snapshot.getKey());

                if (!Proveedor.ITEMS.contains(listaProveedores)) {
                    listaProveedores.add(proveedor);
                    AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, listaProveedores);
                    rvProveedores.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Proveedor proveedor = snapshot.getValue(Proveedor.class);

                if (Proveedor.ITEMS.contains(listaProveedores)) {
                    Proveedor.updateItem(proveedor);
                    AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, listaProveedores);
                    rvProveedores.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Proveedor proveedor = snapshot.getValue(Proveedor.class);

                if (Proveedor.ITEMS.contains(listaProveedores)) {
                    Proveedor.deleteItem(proveedor);
                    AdapterProveedores adaptador = new AdapterProveedores(VistaProveedores.this, listaProveedores);
                    rvProveedores.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @OnClick(R.id.ibtnAdd)
    public void onClick() {
    }
}
