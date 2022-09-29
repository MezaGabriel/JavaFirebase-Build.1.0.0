package com.xGabrielx.appinventario.Vistas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xGabrielx.appinventario.Adaptadores.AdapterProductos;
import com.xGabrielx.appinventario.Modelos.Producto;
import com.xGabrielx.appinventario.Modelos.Proveedor;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VistaProductos extends AppCompatActivity {

    private static final String PATH_PROV = "Proveedores";
    private static final String PATH_PROD = "Productos";

    EditText etBusqueda;
    ImageButton ibtnBuscar, ibtnAdd;
    RecyclerView rvProductos;
    EditText etCodigo, etNomProducto, etDescripcion, etModelo, etPrecio, etAlmacen;
    Spinner spiProveedor;

    ArrayList<String> listaProveedores = new ArrayList<>();
    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<Producto> lista = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_PROD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_productos);
        getSupportActionBar().setTitle("PRODUCTOS");

        etBusqueda = findViewById(R.id.etBusqueda);
        ibtnBuscar = findViewById(R.id.ibtnBuscar);
        ibtnAdd = findViewById(R.id.ibtnAdd);
        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(this, 1));

        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        // Llamada al metodo para obtener los productos
        obtenerProductos();

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

                // Si el EditText esta vacio muestra todos los productos
                if(etBusqueda.getText().toString().trim().equals("")) {
                    AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, listaProductos);
                    rvProductos.setAdapter(adaptador);
                } else {

                    // Si el EditText no esta vacio recorre la lista para solo mostrar los productos que concuerden con el nombre o id del producto
                    for (int i = 0; i < listaProductos.size(); i++) {
                        if (etBusqueda.getText().toString().trim().equalsIgnoreCase(listaProductos.get(i).getNomProducto().trim()) ||
                                etBusqueda.getText().toString().trim().toLowerCase().equals(listaProductos.get(i).getIdProducto())) {
                            lista.add(listaProductos.get(i));
                        }
                    }

                    AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, lista);
                    rvProductos.setAdapter(adaptador);
                }
            }
        });

        // Accion para el ImageButton de buscar
        ibtnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre un AlertDialog para escanear el codigo de barras a buscar
                AlertDialog.Builder builder = new AlertDialog.Builder(VistaProductos.this);
                LayoutInflater inflater = VistaProductos.this.getLayoutInflater();
                builder.setTitle("Buscar Producto");
                View dialogVista = inflater.inflate(R.layout.alert_dialog_buscar_productos, null);
                builder.setView(dialogVista);

                etCodigo = dialogVista.findViewById(R.id.etCodigo);

                IntentIntegrator intent = new IntentIntegrator(VistaProductos.this);
                intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intent.setPrompt("ESCANEAR CÓDIGO");
                intent.setCameraId(0);
                intent.setBeepEnabled(false);
                intent.setBarcodeImageEnabled(false);
                intent.initiateScan();

                builder.setPositiveButton("BUSCAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(etCodigo.getText().toString().trim().equals("")) {
                            AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, listaProductos);
                            rvProductos.setAdapter(adaptador);
                        } else {

                            etBusqueda.setText(etCodigo.getText().toString());

                        }
                    }
                });

                builder.show();
            }
        });

        // Accion para añadir un producto
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre un AlertDialog para poner la informacion del producto a añadir
                AlertDialog.Builder builder = new AlertDialog.Builder(VistaProductos.this);
                LayoutInflater inflater = VistaProductos.this.getLayoutInflater();
                builder.setTitle("Agregar Producto");
                View dialogVista = inflater.inflate(R.layout.alert_dialog_productos, null);
                builder.setView(dialogVista);

                etCodigo = dialogVista.findViewById(R.id.etCodigo);
                final ImageButton ibtnEscaner = dialogVista.findViewById(R.id.ibtnEscaner);
                etNomProducto = dialogVista.findViewById(R.id.etNomProducto);
                etDescripcion = dialogVista.findViewById(R.id.etDescripcion);
                spiProveedor = dialogVista.findViewById(R.id.spiProveedor);
                etModelo = dialogVista.findViewById(R.id.etModelo);
                etPrecio = dialogVista.findViewById(R.id.etPrecio);
                etAlmacen = dialogVista.findViewById(R.id.etAlmacen);

                // Obtiene los proveedores
                obtenerProveedores();

                // Accion al presionar el ImageButton para obtener el codigo de barras del producto
                ibtnEscaner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator intent = new IntentIntegrator(VistaProductos.this);
                        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                        intent.setPrompt("ESCANEAR CÓDIGO");
                        intent.setCameraId(0);
                        intent.setBeepEnabled(false);
                        intent.setBarcodeImageEnabled(false);
                        intent.initiateScan();
                    }
                });

                // Accion al presionar aceptar en el AlertDialog
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Si falta algun campo muestra un mensaje informando
                        if(etCodigo.getText().toString().equals("") ||
                                etNomProducto.getText().toString().equals("") ||
                                etDescripcion.getText().toString().equals("") ||
                                etModelo.getText().toString().equals("") ||
                                etPrecio.getText().toString().equals("") ||
                                etAlmacen.getText().toString().equals("")) {

                            Toast.makeText(VistaProductos.this, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show();

                        } else {
                            // Si cumple con el llenado de los campos hacemos el registro del producto en la base de datos

                            Map<String, Object> registro = new HashMap<>();

                            registro.put("idProducto", etCodigo.getText().toString());
                            registro.put("nomProducto", etNomProducto.getText().toString());
                            registro.put("descripcion", etDescripcion.getText().toString());
                            registro.put("nomProveedor", spiProveedor.getSelectedItem().toString());
                            registro.put("modelo", etModelo.getText().toString());
                            registro.put("precio", etPrecio.getText().toString());
                            registro.put("almacen", etAlmacen.getText().toString());

                            // los inserto en la base de datos
                            reference.push().setValue(registro);

                            //Toast.makeText(VistaProductos.this, spiProveedor.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(com.xGabrielx.appinventario.Vistas.VistaProductos.this, "Datos agregados", Toast.LENGTH_LONG).show();

                            // Obtenemos los productos
                            //obtenerProductos();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    // Metodo para obtener los productos
    public void obtenerProductos() {

        listaProductos.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_PROD);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Producto producto = snapshot.getValue(Producto.class);
                producto.setIdProd(snapshot.getKey());

                if (!Producto.ITEMS.contains(listaProductos)) {
                    listaProductos.add(producto);
                    AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, listaProductos);
                    rvProductos.setAdapter(adaptador);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Producto producto = snapshot.getValue(Producto.class);

                if (Producto.ITEMS.contains(listaProductos)) {
                    Producto.updateItem(producto);
                    AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, listaProductos);
                    rvProductos.setAdapter(adaptador);

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Producto producto = snapshot.getValue(Producto.class);

                if (Producto.ITEMS.contains(listaProductos)) {
                    Producto.deleteItem(producto);
                    AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, listaProductos);
                    rvProductos.setAdapter(adaptador);

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Toast.makeText(VistaProductos.this, ""+listaProductos.size(), Toast.LENGTH_SHORT).show();
        /*AdapterProductos adaptador = new AdapterProductos(VistaProductos.this, listaProductos);
        rvProductos.setAdapter(adaptador);*/

    }

    // Metodo para obtener los proveedores
    public void obtenerProveedores(){
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
                    spiProveedor.setAdapter(new ArrayAdapter<String>(com.xGabrielx.appinventario.Vistas.VistaProductos.this, android.R.layout.simple_spinner_item, listaProveedores));

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

    // Leemos la respuesta del Activity de el escaneo y toma de fotos para poder tomar la desicion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelaste el escaneo", Toast.LENGTH_SHORT).show();
            } else {
                etCodigo.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
