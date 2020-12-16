package com.example.androidapppreguntapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class AdministrarGraficos extends AppCompatActivity implements View.OnClickListener {
    FuncionesVarias xamp = new FuncionesVarias();
    private Spinner spinnerEncuesta, spinnerPregunta, spinnerTipoGrafico;
    RequestQueue requestQueue;
    TextView tvIdEncuesta, tvIDPregunta, tvCantidadDeRespuestaEncuestas;
    Button botonAnadirGrafico, botonGenerarPDF, AdministrarGraficosbotonVolver,buttonGenerarTerminarSeleccion;
    private RequestQueue mQueue;

    //Variables para implementacion de Graficos & PDF
    private static final int STORAGE_CODE = 1000;
    PieChart pieChart;
    BarChart barChart;
    Bitmap logofeusach, logousach;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ArrayList<String> listaDeEncuestasParaElGrafico = new ArrayList<String>();
        final ArrayList<String> listaDeIDEncuestasParaElGrafico = new ArrayList<String>();
        final ArrayList<String> listaDePreguntasParaElGrafico = new ArrayList<String>();
        final ArrayList<String> listaDePreguntasParaElGrafico2 = new ArrayList<String>();
        final ArrayList<String> listaDeIDPreguntasParaElGrafico = new ArrayList<String>();
        final ArrayList<String> listaDeRespuestasParaLosGraficos = new ArrayList<String>();
        final ArrayList<String> listaDeIDRespuestasParaElGrafico = new ArrayList<String>();
        final ArrayList<String> listaDeGraficosParaElGrafico = new ArrayList<String>();
        final ArrayList<Integer> listaDeCantidadDeRespuestasPorPreguntaParaLosGraficos = new ArrayList<Integer>();
        final ArrayList<String> listaDeCuantosRespondieronTalRespuestaParaLosGraficos = new ArrayList<String>();


        super.onCreate(savedInstanceState);
        String TituloEncuesta;
        setContentView(R.layout.activity_administrar_graficos);


        String[] Pregunta = {"Seleccionar Encuesta Primero"};
        String[] Diagramas = {"Diagrama de barras", "Diagrama de pie"};
        spinnerEncuesta = (Spinner) findViewById(R.id.spinnerEncuesta);
        spinnerPregunta = (Spinner) findViewById(R.id.spinnerPregunta);
        spinnerTipoGrafico = (Spinner) findViewById(R.id.spinnerDiagrama);
        tvCantidadDeRespuestaEncuestas = (TextView) findViewById(R.id.textViewCantidadDeRespuestas);
        AdministrarGraficosbotonVolver = (Button) findViewById(R.id.buttonSalir);
        botonAnadirGrafico = (Button) findViewById(R.id.buttonAnadirGrafico);
        buttonGenerarTerminarSeleccion = (Button) findViewById(R.id.buttonGenerarTerminarSeleccion);

        botonGenerarPDF = (Button) findViewById(R.id.buttonGenerarPDF);
        tvIdEncuesta = (TextView) findViewById(R.id.textViewEncuestaId);
        tvIDPregunta = (TextView) findViewById(R.id.textViewPreguntaId);
        AdministrarGraficosbotonVolver.setOnClickListener(this);
        botonGenerarPDF.setOnClickListener(this);
        botonAnadirGrafico.setOnClickListener(this);
        ArrayAdapter<String> adapterPregunta = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pregunta);
        ArrayAdapter<String> adapterDiagramas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Diagramas);
        spinnerPregunta.setAdapter(adapterPregunta);
        spinnerTipoGrafico.setAdapter(adapterDiagramas);
        mQueue = Volley.newRequestQueue(this);

        //Variables para implementacion de Graficos & PDF
        pieChart = findViewById(R.id.pieChart);
        pieChart.setVisibility(View.INVISIBLE);
        barChart = findViewById(R.id.barChart);
        barChart.setVisibility(View.INVISIBLE);

        ///////////////////////////////////////////////////////////////////////
        final ArrayList<String> listaDeEncuestas;
        listaDeEncuestas = new ArrayList<String>();
        final ArrayList<String> listaDeIDEncuestas;
        listaDeIDEncuestas = new ArrayList<String>();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_titulo_encuesta.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                listaDeEncuestas.add("Seleccione una Encuesta");
                listaDeIDEncuestas.add("");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeEncuestas.add(jsonObject.getString("enc_titulo"));
                        listaDeIDEncuestas.add(jsonObject.getString("enc_id"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaDeEncuestas);
                spinnerEncuesta.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


        /////////////////////////////////////////////////////////


        spinnerEncuesta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).equals("Seleccione una Encuesta")) {
                    Toast.makeText(adapterView.getContext(), "Seleccionado: " + adapterView.getItemAtPosition(i).toString() + " id: " + listaDeIDEncuestas.get(i), Toast.LENGTH_SHORT).show();
                    tvIdEncuesta.setText(listaDeIDEncuestas.get(i));
                    mostrarPreguntas("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_pregunta.php?enc_id=" + tvIdEncuesta.getText() + "");
                    GentequerespondioLaEncuesta("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_encuesta_veces_respondidas.php?enc_id=" + tvIdEncuesta.getText() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((!adapterView.getItemAtPosition(i).equals("Seleccionar Encuesta Primero")))) {
                    if ((!adapterView.getItemAtPosition(i).equals("Seleccione una Pregunta"))) {

                        tvIDPregunta.setText(Integer.toString(i));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        botonAnadirGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaDeEncuestasParaElGrafico.add((String) spinnerEncuesta.getSelectedItem());
                listaDePreguntasParaElGrafico.add((String) spinnerPregunta.getSelectedItem());

                listaDeIDEncuestasParaElGrafico.add((String) tvIdEncuesta.getText());
                listaDeIDPreguntasParaElGrafico.add((String) tvIDPregunta.getText());
                listaDeGraficosParaElGrafico.add((String) spinnerTipoGrafico.getSelectedItem());



            }
        });

        buttonGenerarTerminarSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaDeEncuestasParaElGrafico.add((String) spinnerEncuesta.getSelectedItem());
                listaDePreguntasParaElGrafico.add((String) spinnerPregunta.getSelectedItem());
                listaDeIDEncuestasParaElGrafico.add((String) tvIdEncuesta.getText());
                listaDeIDPreguntasParaElGrafico.add((String) tvIDPregunta.getText());
                listaDeGraficosParaElGrafico.add((String) spinnerTipoGrafico.getSelectedItem());


                final int[] contador = {0};
                for (int o = 0; o < listaDePreguntasParaElGrafico.size(); o++) {

                    final int finalO = o;
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_respuestas_respondidas.php?enc_id=" + listaDeIDEncuestasParaElGrafico.get(o) + "&preg_id=" + listaDeIDPreguntasParaElGrafico.get(o) + "", new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jsonObject;


                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    listaDeRespuestasParaLosGraficos.add(jsonObject.getString("res_respuesta"));
                                    listaDeIDRespuestasParaElGrafico.add(jsonObject.getString("res_id"));
                                    listaDeCuantosRespondieronTalRespuestaParaLosGraficos.add(jsonObject.getString("veces_respondidas"));

                                    contador[0]++;





                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            listaDePreguntasParaElGrafico2.add(listaDePreguntasParaElGrafico.get(finalO));
                            listaDeCantidadDeRespuestasPorPreguntaParaLosGraficos.add(contador[0]);
                            contador[0]=0;

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    );
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonArrayRequest);

                }


            }
        });



        botonGenerarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Implementacion de Permiso acceder a archivos locales en el dispositivo para guardar PDF
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        funcionGraficar(listaDeEncuestasParaElGrafico
                                ,listaDePreguntasParaElGrafico2
                                ,listaDeRespuestasParaLosGraficos
                                ,listaDeCuantosRespondieronTalRespuestaParaLosGraficos
                                ,listaDeCantidadDeRespuestasPorPreguntaParaLosGraficos);

                    }
                } else {
                    funcionGraficar(listaDeEncuestasParaElGrafico
                            ,listaDePreguntasParaElGrafico2
                            ,listaDeRespuestasParaLosGraficos
                            ,listaDeCuantosRespondieronTalRespuestaParaLosGraficos
                            ,listaDeCantidadDeRespuestasPorPreguntaParaLosGraficos);

                }

            }
        });

        //if (((spinnerPregunta.getSelectedItem() != "Seleccione una Pregunta") || (adapterView.getItemAtPosition(i) != "Seleccionar Encuesta Primero"))) {


    }

    public void funcionGraficar(ArrayList<String> Encuesta,
                                 ArrayList<String> preguntas,
                                 ArrayList<String> respuesta,
                                 ArrayList<String> resultadospregunt,
                                 ArrayList<Integer> cantopcionespregunt) {

        String CantidadDePersonas = (String) tvCantidadDeRespuestaEncuestas.getText();
        String[] totalopciones[] = new String[20][20];
        int[] totalresultado[] = new int[20][20];

        System.out.println("--< string tituloencuesta"+Encuesta.get(0));
        System.out.println("--< string Preguntas"+preguntas);
        System.out.println("--< int opciones"+cantopcionespregunt);

        int posicion = 0;
        String hola;
        for (int i=0; i < cantopcionespregunt.size();i++){
            hola = "---< Opciones ["+(i+1)+"] ";
            System.out.print(hola);
            for (int o=0; o<cantopcionespregunt.get(i);o++){
                System.out.print(respuesta.get(posicion)+", ");
                posicion=posicion+1;
            }
            System.out.println(" ");
        }

        posicion = 0;
        for (int i=0; i < cantopcionespregunt.size();i++) {
            System.out.println("<<<-<<<" + i);
            hola = "---< Respuestas [" + (i) + "] ";
            System.out.print(hola);
            for (int o = 0; o < cantopcionespregunt.get(i); o++) {
                System.out.print(resultadospregunt.get(posicion) + ", ");
                totalopciones[i][o] = respuesta.get(posicion);
                totalresultado[i][o] = Integer.parseInt(resultadospregunt.get(posicion));
                posicion = posicion + 1;
            }
            System.out.println(" ");
        }
            try {
                //Inicializando Documento PDF
                Document mDoc = new Document(PageSize.LETTER);

                //Nombre y direccion donde se guardara el archivo PDF en el almacenamiento del celular
                String mFileName = Encuesta.get(0);
                String mFilePath = Environment.getExternalStorageDirectory() + "/" +mFileName + ".pdf";
                PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
                //Font dir = new Font(Font.FontFamily.HELVETICA, 11);

                //SE ABRE EL DOCUMENTO PDF
                mDoc.open();


                //SE CREAN LAS TABLAS PARA AYUDARNOS A ORGANIZAR EL PDF
                PdfPTable logos = new PdfPTable(3);
                PdfPTable GraPie = new PdfPTable(5);
                PdfPTable GraBarr = new PdfPTable(1);
                PdfPTable datos = new PdfPTable(2);


                //SE ENVIA LA TABLA PARA QUE LOS LOGOS PUEDAN SER AÑADIDOS EN LA FUNCION insertarlogos()
                insertarlogos(logos);
                //Finalmente se añade la tabla logos al PDF
                mDoc.add(logos);

                //SE INDICA EL NOMBRE DE LA ENCUESTA
                Paragraph titulopdf = new Paragraph(mFileName);
                titulopdf.setAlignment(Element.ALIGN_CENTER);
                mDoc.add(titulopdf);
                mDoc.add(new Phrase(" "));

                Paragraph genteapdf = new Paragraph(CantidadDePersonas);
                titulopdf.setAlignment(Element.ALIGN_CENTER);
                mDoc.add(genteapdf);
                mDoc.add(new Phrase(" "));

                //SE PROCEDE A AÑADIR LOS GRAFICOS DEPENDIENDO DE LAS PREGUNTAS, PARA ESTO ES NECESARIO UN BUCLE QUE LEA EL TIPO DE PREGUNTA Y
                //LO DERIVE A LA FUNCION CORRESPONDIENTE, PARA ESTO ES NECESARIA LA TABLA GRAPIE, EL NOMBRE DE LAS OPCIONES
                //LOS RESULTADOS POR CADA UNA DE LAS OPCIONES Y LA CANTIDAD DE OPCIONES DE LA PREGUNTA
                for (int i=0; i<cantopcionespregunt.size();i++){
                    //PRIMERO SE AÑADE EL TITULO DE LA PREGUNTA
                    titulopdf = new Paragraph(preguntas.get(i));
                    titulopdf.setAlignment(Element.ALIGN_CENTER);
                    mDoc.add(titulopdf);
                    mDoc.add(new Phrase(" "));


                    //Se calcuna el numero de respuestas a enviar al grafico
                    //LUEGO SE INSERTA EL GRAFICO
                    if(cantopcionespregunt.get(i)<4){
                        graficoPie(GraPie, totalopciones[i], totalresultado[i], cantopcionespregunt.get(i));
                        mDoc.add(GraPie);
                    }else{
                        graficoBarras(GraBarr, totalopciones[i], totalresultado[i], cantopcionespregunt.get(i));
                        mDoc.add(GraBarr);
                    }
                    //Se añade la tabla al documento PDF
                    //Y ahora agregamos la tabla de datos
                    añadirtabla(datos, totalopciones[i], totalresultado[i], cantopcionespregunt.get(i));
                    mDoc.add(datos);
                    mDoc.newPage();

                    //Y se reinicia el proceso
                    GraPie = new PdfPTable(5);
                    GraBarr = new PdfPTable(1);
                    datos = new PdfPTable(2);
                }

                mDoc.close();
                Toast.makeText(this,mFileName + ".pdf\nesta guardado en\n"+ mFilePath, Toast.LENGTH_SHORT ).show();
            }catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonSalir:
                irAMenuPrincipalAdministrador();
                break;

        }
    }

    private void irAMenuPrincipalAdministrador() {

        Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();

    }

    private void mostrarPreguntas(String rutaWebServices) {
        final ArrayList<String> listaDePreguntas;
        listaDePreguntas = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                listaDePreguntas.add("Seleccione una Pregunta");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDePreguntas.add(jsonObject.getString("preg_titulo"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaDePreguntas);
                spinnerPregunta.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void GentequerespondioLaEncuesta(String rutaWebServices) {
        final ArrayList<String> listaDePreguntas;
        listaDePreguntas = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        tvCantidadDeRespuestaEncuestas.setText("Gente que ha respondido la encuesta: " + jsonObject.getString("VecesRespondidas"));
                        listaDePreguntas.add(jsonObject.getString("preg_titulo"));


                    } catch (JSONException e) {
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    /*Funcion que ayuda al permiso para sobreescribir en el almacenamiento externo del celular
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    funcionGraficar(listaDeEncuestasParaElGrafico
                            ,listaDePreguntasParaElGrafico2
                            ,listaDeRespuestasParaLosGraficos
                            ,listaDeCuantosRespondieronTalRespuestaParaLosGraficos
                            ,listaDeCantidadDeRespuestasPorPreguntaParaLosGraficos);
                }
                else{
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/

    //Funcion que añade los logos de USACH y FEUSACH al documento PDF
    public void insertarlogos(PdfPTable logos) throws IOException, BadElementException {
        //Se crea una celda
        PdfPCell cell = new PdfPCell();

        //EL ByteArray ayuda en el proceso de traspaso de imagen desde un minimap a una variable Image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //Preparando logos para poder añadirlos al pdf
        logousach= BitmapFactory.decodeResource(getResources(), R.mipmap.logousach);
        stream = new ByteArrayOutputStream();
        logousach.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        Image img1 = Image.getInstance(stream.toByteArray());

        logofeusach= BitmapFactory.decodeResource(getResources(), R.mipmap.logofeusach);
        stream = new ByteArrayOutputStream();
        logofeusach.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        Image img2 = Image.getInstance(stream.toByteArray());

        //Se agrega logo USACH
        cell= new PdfPCell();
        cell.addElement(img1);
        cell.setBorderColor(BaseColor.WHITE);
        logos.addCell(cell);

        //Se deja espacio en el centro
        cell = new PdfPCell();
        cell.setBorderColor(BaseColor.WHITE);
        //String currentDate = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(new Date());
        logos.addCell(cell);

        //Se agrega logo FEUSACH
        cell = new PdfPCell();
        cell.addElement(img2);
        cell.setBorderColor(BaseColor.WHITE);
        logos.addCell(cell);

    }

    //Funcion que añade un grafico circular al documento PDF
    public void graficoPie(PdfPTable GraPie, String respuesta[], int resultadospregunt[] ,
                           int cantopciones) throws IOException, BadElementException {
        //SE INICIALIZA LA CELDA Y LA TABLA
        PdfPCell cell = new PdfPCell();

        //EL ByteArray sirve en el proceso de traspaso de imagen desde un minimap a una variable Image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //PARA GRAFICO CIRCULAR - PARTE 2: AÑADIENDO DATOS AL GRAFICO
        ArrayList<PieEntry> datapie = new ArrayList<>();
        for (int i = 0; i < cantopciones; i++){
            datapie.add(new PieEntry(resultadospregunt[i], respuesta[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(datapie, " ");

        //PARA GRAFICO CIRCULAR - PARTE 3: EDITANDO CARACTERISTICAS DEL GRAFICO (COLORES, ETC)
        pieDataSet.setColors(new int[]{Color.parseColor("#F74949"),
                Color.parseColor("#49A0F7"),
                Color.parseColor("#F7B849"),
                Color.parseColor("#49F7C8"),
                Color.parseColor("#F749DA"),
                Color.parseColor("#51F749"),
                Color.parseColor("#F2ED59")});
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setSliceSpace(10f);
        pieDataSet.setSelectionShift(5f);
        //pieDataSet.setValueFormatter(new com.github.mikephil.charting.formatter.PercentFormatter());

        //PARA GRAFICO CIRCULAR - PARTE 4: CREANDO GRAFICO
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        //pieData.setValueFormatter(new com.github.mikephil.charting.formatter.PercentFormatter());
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueTextSize(15f);

        //PARA GRAFICO CIRCULAR - PARTE 5: EDITANDO CARACTERISTICAS DE SALIDA DEL GRAFICO
        pieChart.setTransparentCircleRadius(75f);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);



        //PARA GRAFICO CIRCULAR - PARTE 6: CREANDO IMAGEN A PARTIR DE SALIDA FINAL DEL GRAFICO, ESTE SE MUESTRA SOLO EN INTERFAZ
        //POR LO TANTO, DEBEMOS SETTIAR INVISIBLE EN INTERFAZ Y CAPTURARLO PARA PASARLO A IMG Y A NUESTRO PDF
        //pieData.setValueFormatter(new com.github.mikephil.charting.formatter.PercentFormatter(pieChart));

        Bitmap bitmap = pieChart.getChartBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        Image myImg = Image.getInstance(stream.toByteArray());

        //PARA GRAFICO CIRCULAR - PARTE 7: AÑADIENDO IMAGEN A PDF, COMO LA HERRAMIENTA ITEXT NO FUNCIONA MUY BIEN, PARA QUE QUEDE LA IMAGEN CENTRADA
        //SE HACEN 2 CELDAS, IZQ Y DER, PARA ASI PODER AGREGAR EN LA DEL MEDIO LA IMAGEN

        //Se deja espacio a la izq
        cell = new PdfPCell();
        cell.setBorderColor(BaseColor.WHITE);
        GraPie.addCell(cell);

        //Se agrega la imagen
        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.addElement(myImg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.WHITE);
        GraPie.addCell(cell);

        //Se deja espacio a la der
        cell = new PdfPCell();
        cell.setBorderColor(BaseColor.WHITE);
        GraPie.addCell(cell);

        //FINALMENTE, COMO YA INGRESAMOS LAS 3 CELDAS A LA TABLA GRANPIE, ESTA ALMACENARA TODOS LOS DATOS EN EL PDF
    }
    //Funcion que añade un grafico de barras al documento PDF
    public void graficoBarras(PdfPTable GraBarr, String respuesta[], int resultadospregunt[],
                              int cantopciones) throws IOException, BadElementException {
        //SE INICIALIZA LA CELDA Y LA TABLA
        PdfPCell cell = new PdfPCell();

        //Se prepara el arreglo de resultados para poder mostrar con porcentaje
        float total=0;

        float resuporcent[] = new float[cantopciones];
        for (int i = 0; i<cantopciones; i++){
            float aux = (float) resultadospregunt[i];
            total=total+aux;
        }
        for (int i = 0; i<cantopciones; i++){
            resuporcent[i] = ((resultadospregunt[i]*100)/total);
        }

        //EL ByteArray sirve en el proceso de traspaso de imagen desde un minimap a una variable Image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //PARA GRAFICO DE BARRAS - PARTE 2: RELLENANDO LA INFO EN LOS ARREGLOS
        ArrayList<BarEntry> databarr = new ArrayList<>();
        ArrayList<String> nombarr = new ArrayList<>();

        for (int i = 0; i < cantopciones; i++){
            databarr.add(new BarEntry(i, resuporcent[i]));
            nombarr.add(respuesta[i]);
        }
        //PARA GRAFICO DE BARRAS - PARTE 3: GENERANDO EL GRAFICO
        //EDITANDO CARACTERISTICAS DEL GRAFICO
        BarDataSet barDataSet = new BarDataSet(databarr, "");
        barDataSet.setColors(new int[]{Color.parseColor("#F74949"),
                Color.parseColor("#49A0F7"),
                Color.parseColor("#F7B849"),
                Color.parseColor("#49F7C8"),
                Color.parseColor("#F749DA"),
                Color.parseColor("#51F749"),
                Color.parseColor("#F2ED59")});
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(true);
        barDataSet.setValueFormatter(new com.github.mikephil.charting.formatter.PercentFormatter());

        //GENERANDO EL GRAFICO
        BarData data = new BarData(barDataSet);
        //EDITANDO CARACTERISTICAS DEL GRAFICO DE SALIDA

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(nombarr));
        xAxis.setTextSize(1.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setLabelCount(cantopciones, false);
        xAxis.setDrawGridLines(true);

        //barChart.setXAxisRenderer(new CustomXAxisRenderer(barChart.getViewPortHandler(), barChart.getXAxis(), barChart.getTransformer(YAxis.AxisDependency.LEFT)));
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(false);
        barChart.setData(data);



        //PARA GRAFICO DE BARRAS - PARTE 4: CREANDO IMAGEN A PARTIR DE SALIDA FINAL DEL GRAFICO, ESTE SE MUESTRA SOLO EN INTERFAZ
        //POR LO TANTO, DEBEMOS SETTIAR INVISIBLE EN INTERFAZ Y CAPTURARLO PARA PASARLO A IMG Y A NUESTRO PDF
        Bitmap bitmap = barChart.getChartBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        Image myImg = Image.getInstance(stream.toByteArray());


        //PARA GRAFICO DE BARRAS - PARTE 5: AÑADIENDO IMAGEN A PDF, COMO LA HERRAMIENTA ITEXT NO FUNCIONA MUY BIEN, PARA QUE QUEDE LA IMAGEN CENTRADA
        //SE HACEN 2 CELDAS, IZQ Y DER, PARA ASI PODER AGREGAR EN LA DEL MEDIO LA IMAGEN

        //Se deja espacio a la izq
        //cell = new PdfPCell();
        //cell.setBorderColor(BaseColor.WHITE);
        //GraPie.addCell(cell);

        //Se agrega la imagen
        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.addElement(myImg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.WHITE);
        GraBarr.addCell(cell);

        //Se deja espacio a la der
        //cell = new PdfPCell();
        //cell.setBorderColor(BaseColor.WHITE);
        //GraPie.addCell(cell);

        //FINALMENTE, COMO YA INGRESAMOS LAS 3 CELDAS A LA TABLA GRANPIE, ESTA ALMACENARA TODOS LOS DATOS EN EL PDF

    }

    //Funcion que, por cada grafico, añade la tabla de datos correspondiente para la confeccion este
    public void añadirtabla(PdfPTable datos, String respuesta[], int resultadospregunt[] ,
                            int cantopciones){
        PdfPCell cell = new PdfPCell();
        Font encabezado = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLACK);
        Font resto = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        for (int i=0; i<cantopciones; i++){
            if(i==0){
                cell = new PdfPCell(new Phrase("Opción", encabezado));
                datos.addCell(cell);
                cell = new PdfPCell(new Phrase("Cantidad de votos",encabezado));
                datos.addCell(cell);

            }
            cell = new PdfPCell(new Phrase(respuesta[i],resto));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            datos.addCell(cell);
            cell = new PdfPCell(new Phrase(String.valueOf(resultadospregunt[i]),resto));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            datos.addCell(cell);


        }

    }

    // Funcion que permite escribir el simbolo % en el grafico circular
    public class PercentFormatter extends ValueFormatter {

        public DecimalFormat mFormat;
        private PieChart pieChart;

        public PercentFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }
        public PercentFormatter(PieChart pieChart) {
            this();
            this.pieChart = pieChart;
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + " %";
        }


        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
                // Converted to percent
                return getFormattedValue(value);
            } else {
                // raw value, skip percent sign
                return mFormat.format(value)+ " %";
            }
        }

    }


}
