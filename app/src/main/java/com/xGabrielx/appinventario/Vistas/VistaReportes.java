package com.xGabrielx.appinventario.Vistas;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xGabrielx.appinventario.Adaptadores.AdapterReportes;
import com.xGabrielx.appinventario.Modelos.Reportes;
import com.xGabrielx.appinventario.Modelos.Venta;
import com.xGabrielx.appinventario.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VistaReportes extends AppCompatActivity {

    private static final String PATH_VENT = "Reportes";
    EditText etFechaInicio, etFechaFin;
    Button btnMostarReporte, btnGenerarPDF;
    RecyclerView rvVentas;

    ArrayList<Venta> listaVentas = new ArrayList<>();
    ArrayList<Reportes> listaReportes = new ArrayList<>();
    List<String> arrayVentas = new ArrayList<>();

    Calendar calendario = Calendar.getInstance();
    int dia = calendario.get(Calendar.DAY_OF_MONTH);
    int mes = calendario.get(Calendar.MONTH);
    int anio = calendario.get(Calendar.YEAR);

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(PATH_VENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_reportes);
        getSupportActionBar().setTitle("REPORTE DE VENTAS");

        etFechaInicio = findViewById(R.id.etFechaInicio);
        //etFechaFin = findViewById(R.id.etFechaFin);
        btnMostarReporte = findViewById(R.id.btnMostrarReporte);
        //btnGenerarPDF = findViewById(R.id.btnGenerarPDF);

        rvVentas = findViewById(R.id.rvVentas);
        rvVentas.setLayoutManager(new GridLayoutManager(this, 1));

        // Permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        // Metodo para obtener la fecha inicial del reporte
        etFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha(etFechaInicio);
            }
        });

        // Metodo para obtener la fecha final del reporte
        //etFechaFin.setOnClickListener(new View.OnClickListener() {
        /*    @Override
            public void onClick(View v) {
                obtenerFecha(etFechaFin);
            }
        });*/

        // Accion para el Boton de Mostrar reporte
        btnMostarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Si falta alguna fecha mostramos un aviso
                if((etFechaInicio.getText().toString().equals("") && etFechaFin.getText().toString().equals("")) ||
                        etFechaInicio.getText().toString().equals("")) {

                    Toast.makeText(VistaReportes.this, "Selecciona una fecha.", Toast.LENGTH_SHORT).show();
                    //btnGenerarPDF.setEnabled(false); // Desabilitamos el boton

                } else { // Si se cumplen las fechas del reporte
                    // Si hay fecha de inicio de no fecha final
                    obtenerVentas(etFechaInicio.getText().toString());

                   /* if((etFechaInicio.getText().toString() != "") && (etFechaFin.getText().toString().equals(""))) {

                        // Metodo para obtener las ventas de solo la una fecha
                        obtenerVentas(etFechaInicio.getText().toString());
                        //btnGenerarPDF.setEnabled(listaVentas.size() != 0);

                    } else { // Si hay fecha de inicio y fecha final

                        // Metodo para obtener las ventas de fecha inicial y fecha final
                        obtenerVentas(etFechaInicio.getText().toString(), etFechaFin.getText().toString());
                        //btnGenerarPDF.setEnabled(listaVentas.size() != 0);
                    }*/
                }
            }
        });

        // Accion para el boton que genera el PDF con el reporte de ventas
        /*btnGenerarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String NOMBRE_DIRECTORIO = "AppInventarioArchivos";
                String NOMBRE_DOCUMENTO = "Reporte_"+etFechaInicio.getText().toString()+"_"+etFechaFin.getText().toString()+".pdf";

                crearReportePDF(NOMBRE_DIRECTORIO, NOMBRE_DOCUMENTO);


                *//*String NOMBRE_DIRECTORIO = "MisReportes";
                String NOMBRE_REPORTE = "Reporte_"+etFechaInicio.getText().toString()+"_"+etFechaFin.getText().toString()+".pdf";

                // Llamada al metodo para crear el reporte
                crearReportePDF(NOMBRE_DIRECTORIO, NOMBRE_REPORTE);*//*

            }
        });*/
    }

    /*public void crearReportePDF(String carpeta, String archivo) {
        Document documento = new Document();

        try {
            File file = crearFichero(carpeta, archivo);
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());

            PdfWriter pdfWriter = PdfWriter.getInstance(documento, ficheroPDF);

            documento.open();

            documento.add(new Paragraph(
                    "Reporte de ventas entre las fechas: "
                            + etFechaInicio.getText().toString() +" y "
                            + etFechaFin.getText().toString() + "\n\n",
                    FontFactory.getFont("arial", 16, Font.BOLD)
            ));

            double auxTotal = 0;
            for(int k = 0 ; k < listaVentas.size() ; k++) {
                auxTotal = auxTotal + Double.parseDouble(listaVentas.get(k).getTotal());
            }
            documento.add(new Paragraph("En este periodo se vendio un total de $"+auxTotal+"\n\n"));

            String fecha = listaVentas.get(0).getFechaVenta();

            for(int i = 0 ; i < listaVentas.size() ; i++) {
                //Toast.makeText(this, "SIZE: "+listaVentas.size(), Toast.LENGTH_SHORT).show();

                documento.add(new Paragraph(listaVentas.get(i).getFechaVenta()));

                String[] arrayVentas = listaVentas.get(i).getIdProductos().split(",");

                for(int j = 0 ; j < arrayVentas.length ; j = j + 4) {

                    documento.add(
                      new Paragraph(
                              arrayVentas[j] + "     " + arrayVentas[j+1] + "     " + arrayVentas[j+2] + "      $" + arrayVentas[j+3] + "      $" + (Double.parseDouble(arrayVentas[j]) * Double.parseDouble(arrayVentas[j+3]))
                      )
                    );
                }

                documento.add(new Paragraph("Total de la venta: $"+listaVentas.get(i).getTotal()+"\n\n"));
                documento.add(new Paragraph("\n"));

            }

            documento.close();

            Toast.makeText(this, "Se creo el reporte.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) { }

    }*/

    // Metodo para crear el archivo
    /*public File crearFichero(String NOMBRE_DIRECTORIO, String nombreFichero) {
        File ruta = getRuta(NOMBRE_DIRECTORIO);

        File fichero = null;
        if(ruta != null) {
            fichero = new File(ruta, nombreFichero);
        }

        return fichero;
    }*/

    // Metodo para obtener la ruta
    /*public File getRuta(String NOMBRE_DIRECTORIO) {
        File ruta = null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);
            ruta = new File(Environment.getExternalStorageDirectory(), "/"+NOMBRE_DIRECTORIO);

            if(ruta != null) {
                if(!ruta.mkdirs()) {
                    if(!ruta.exists()) {
                        return null;
                    }
                }
            }

        }
        return ruta;
    }*/


    // Metodo para obtener las ventas con solo fecha de inicio
    public void obtenerVentas(String fechaInicio) {
        listaVentas.clear();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getValue().equals(fechaInicio)) {
                        Reportes reportes = snapshot.getValue(Reportes.class);
                        listaReportes.add(reportes);
                        AdapterReportes adaptador = new AdapterReportes(VistaReportes.this, listaReportes);
                        rvVentas.setAdapter(adaptador);
                        break;
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Metodo para obtener las ventas con solo fecha de inicio y fecha final
    public void obtenerVentas(String fechaInicio, String fechaFin) {
        listaReportes.clear();

        /*AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);

        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("select * from ventas where fechaVenta between ? and ? order by idVenta asc", new String[]{fechaInicio, fechaFin});

        if(fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            do {
                listaVentas.add(
                        new Venta(
                                fila.getInt(0),
                                fila.getString(1),
                                fila.getString(2),
                                fila.getString(3)
                        )
                );
            } while(fila.moveToNext());
        } else {
            //Toast.makeText(this, "No hay registros", Toast.LENGTH_LONG).show();
        }

        db.close();*/

        /*AdapterReportes adaptador = new AdapterReportes(VistaReportes.this, listaReportes);
        rvVentas.setAdapter(adaptador);*/
    }

    // Metodo para obtener las fechas
    public  void obtenerFecha(final EditText etFecha) {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? 0 + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? 0 + String.valueOf(mesActual):String.valueOf(mesActual);
                etFecha.setText(diaFormateado + "-" + mesFormateado + "-" + year);
            }

        },anio, mes, dia);
        recogerFecha.show();
    }
}
