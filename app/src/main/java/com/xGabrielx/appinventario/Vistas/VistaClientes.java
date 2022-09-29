package com.xGabrielx.appinventario.Vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.xGabrielx.appinventario.Adaptadores.AdapterClientes;
import com.xGabrielx.appinventario.Modelos.Cliente;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VistaClientes extends AppCompatActivity {

    private static final String PATH_CLI = "Clientes";
    EditText etBusqueda, etNombre, etNumTelefono, etEmail, etDireccion, etRfc, etTipoPersona, etObservaciones;
    ImageButton ibtnBuscar, ibtnAdd;
    RecyclerView rvClientes;

    ArrayList<Cliente> listaClientes = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_CLI);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_clientes);
        getSupportActionBar().setTitle("CLIENTES");

        etBusqueda = findViewById(R.id.etBusqueda);
        ibtnBuscar = findViewById(R.id.ibtnBuscar);
        ibtnAdd = findViewById(R.id.ibtnAdd);

        rvClientes = findViewById(R.id.rvClientes);
        rvClientes.setLayoutManager(new GridLayoutManager(this, 1));

        // Obtiene los clientes
        obtenerClientes();

        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre un AlertDialog para poner la informacion del producto a añadir
                AlertDialog.Builder builder = new AlertDialog.Builder(VistaClientes.this);
                LayoutInflater inflater = VistaClientes.this.getLayoutInflater();
                builder.setTitle("Agregar Cliente");
                View dialogVista = inflater.inflate(R.layout.alert_dialog_clientes, null);
                builder.setView(dialogVista);

                etNombre = dialogVista.findViewById(R.id.etNombre);
                etNumTelefono = dialogVista.findViewById(R.id.etNumTelefono);
                etEmail = dialogVista.findViewById(R.id.etEmail);
                etDireccion = dialogVista.findViewById(R.id.etDireccion);
                etRfc = dialogVista.findViewById(R.id.etRfc);
                etTipoPersona = dialogVista.findViewById(R.id.etTipoPersona);
                etObservaciones = dialogVista.findViewById(R.id.etObservaciones);


                // Accion al presionar aceptar en el AlertDialog
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Si falta algun campo muestra un mensaje informando
                        if(etNombre.getText().toString().equals("") ||
                                etNumTelefono.getText().toString().equals("") ||
                                etEmail.getText().toString().equals("") ||
                                etDireccion.getText().toString().equals("") ||
                                etRfc.getText().toString().equals("") ||
                                etTipoPersona.getText().toString().equals("") ||
                                etObservaciones.getText().toString().equals("")) {

                            Toast.makeText(VistaClientes.this, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();

                        } else {

                            Map<String, Object> registro = new HashMap<>();

                            registro.put("nomCliente", etNombre.getText().toString());
                            registro.put("numTelefono", etNumTelefono.getText().toString());
                            registro.put("email", etEmail.getText().toString());
                            registro.put("direccion", etDireccion.getText().toString());
                            registro.put("rfc", etRfc.getText().toString());
                            registro.put("tipoPersona", etTipoPersona.getText().toString());
                            registro.put("observaciones", etObservaciones.getText().toString());

                            // los inserto en la base de datos
                            reference.push().setValue(registro);

                            //Toast.makeText(VistaProductos.this, spiProveedor.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(VistaClientes.this, "Datos agregados", Toast.LENGTH_LONG).show();

                            // Obtenemos los clientes
                            //obtenerClientes();
                        }
                    }
                });
                builder.show();
            }
        });

    }

    // Metodo para obtener los productos
    public void obtenerClientes() {

        listaClientes.clear();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Cliente cliente = snapshot.getValue(Cliente.class);
                cliente.setIdCliente(snapshot.getKey());


                if (!Cliente.ITEMS.contains(listaClientes)) {
                    listaClientes.add(cliente);
                    AdapterClientes adaptador = new AdapterClientes(VistaClientes.this, listaClientes);
                    rvClientes.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                Cliente cliente = snapshot.getValue(Cliente.class);

                if (Cliente.ITEMS.contains(listaClientes)) {
                    Cliente.updateItem(cliente);
                    AdapterClientes adaptador = new AdapterClientes(VistaClientes.this, listaClientes);
                    rvClientes.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Cliente cliente = snapshot.getValue(Cliente.class);

                if (Cliente.ITEMS.contains(listaClientes)) {
                    Cliente.deleteItem(cliente);
                    AdapterClientes adaptador = new AdapterClientes(VistaClientes.this, listaClientes);
                    rvClientes.setAdapter(adaptador);

                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Toast.makeText(VistaClientes.this, ""+listaClientes.size(), Toast.LENGTH_SHORT).show();
        /*AdapterClientes adaptador = new AdapterClientes(VistaClientes.this, listaClientes);
        rvClientes.setAdapter(adaptador);*/

    }
}
