package com.xGabrielx.appinventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xGabrielx.appinventario.Vistas.VistaClientes;
import com.xGabrielx.appinventario.Vistas.VistaCotizaciones;
import com.xGabrielx.appinventario.Vistas.VistaProductos;
import com.xGabrielx.appinventario.Vistas.VistaProveedores;
import com.xGabrielx.appinventario.Vistas.VistaReportes;
import com.xGabrielx.appinventario.Vistas.VistaVentas;

public class Navegacion extends AppCompatActivity {


    CardView cvProveedores, cvClientes, cvProductos, cvRetiros, cvVentas, cvReportes;
    ImageButton ibtnBaseDatos, ibtnContacto;
    TextView tvVersion, tvEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        //ibtnBaseDatos = findViewById(R.id.ibtnBaseDatos);
        cvProveedores = findViewById(R.id.cvProveedores);
        cvClientes = findViewById(R.id.cvClientes);
        cvProductos = findViewById(R.id.cvProductos);
        cvRetiros = findViewById(R.id.cvRetiros);
        cvVentas = findViewById(R.id.cvVentas);
        cvReportes = findViewById(R.id.cvReportes);
        //ibtnContacto = findViewById(R.id.ibtnContacto);
        tvVersion = findViewById(R.id.tvVersion);
        tvEmpresa = findViewById(R.id.tvEmpresa);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Verifica permisos para Android 6.0+
            checkExternalStoragePermission();
        }

        // Declaracion para la fuente externa
        Typeface face= Typeface.createFromAsset(getAssets(),"fonts/komtit.ttf");
        tvVersion.setTypeface(face);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        tvEmpresa.setTypeface(face);


        cvProveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre la seccion de proveedores
                Intent intent = new Intent(Navegacion.this, VistaProveedores.class);
                startActivity(intent);


            }
        });

        cvClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la seccion de clientes
                Intent intent = new Intent(Navegacion.this, VistaClientes.class);
                startActivity(intent);
            }
        });

        cvProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre la seccion de productos
                Intent intent = new Intent(Navegacion.this, VistaProductos.class);
                startActivity(intent);

            }
        });

        cvRetiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre seccion de retiros
                Intent intent = new Intent(Navegacion.this, VistaCotizaciones.class);
                startActivity(intent);
            }
        });

        cvVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre la seccion de ventas
                Intent intent = new Intent(Navegacion.this, VistaVentas.class);
                startActivity(intent);

            }
        });

        cvReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Abre la seccion de reportes
                Intent intent = new Intent(Navegacion.this, VistaReportes.class);
                startActivity(intent);

            }
        });

        /*ibtnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Acción para el envio de email
                String[] TO = {"xcheko51x@gmail.com"}; //aquí pon tu correo
                String[] CC = {""};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu mensaje");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Navegacion.this, "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }
}