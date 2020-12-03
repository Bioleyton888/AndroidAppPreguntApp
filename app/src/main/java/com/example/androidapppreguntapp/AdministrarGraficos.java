package com.example.androidapppreguntapp;

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

public class AdministrarGraficos extends AppCompatActivity implements View.OnClickListener {
    funciones_varias xamp = new funciones_varias();
    private Spinner spinnerEncuesta, spinnerPregunta, spinnerTipoGrafico;
    RequestQueue requestQueue;
    TextView tvIdEncuesta, tvIDPregunta, tvCantidadDeRespuestaEncuestas;
    Button botonAnadirGrafico, botonGenerarPDF, botonVolver,buttonGenerarTerminarSeleccion;
    private RequestQueue mQueue;

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
        botonVolver = (Button) findViewById(R.id.buttonSalir);
        botonAnadirGrafico = (Button) findViewById(R.id.buttonAnadirGrafico);
        buttonGenerarTerminarSeleccion = (Button) findViewById(R.id.buttonGenerarTerminarSeleccion);

        botonGenerarPDF = (Button) findViewById(R.id.buttonGenerarPDF);
        tvIdEncuesta = (TextView) findViewById(R.id.textViewEncuestaId);
        tvIDPregunta = (TextView) findViewById(R.id.textViewPreguntaId);
        botonVolver.setOnClickListener(this);
        botonGenerarPDF.setOnClickListener(this);
        botonAnadirGrafico.setOnClickListener(this);
        ArrayAdapter<String> adapterPregunta = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pregunta);
        ArrayAdapter<String> adapterDiagramas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Diagramas);
        spinnerPregunta.setAdapter(adapterPregunta);
        spinnerTipoGrafico.setAdapter(adapterDiagramas);
        mQueue = Volley.newRequestQueue(this);

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
                            JSONObject jsonObject = null;


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


                funcionGraficar(listaDeEncuestasParaElGrafico,listaDeIDEncuestasParaElGrafico,listaDePreguntasParaElGrafico2,listaDeIDPreguntasParaElGrafico,listaDeRespuestasParaLosGraficos,listaDeIDRespuestasParaElGrafico,listaDeGraficosParaElGrafico,listaDeCantidadDeRespuestasPorPreguntaParaLosGraficos,listaDeCuantosRespondieronTalRespuestaParaLosGraficos);



            }
        });

        //if (((spinnerPregunta.getSelectedItem() != "Seleccione una Pregunta") || (adapterView.getItemAtPosition(i) != "Seleccionar Encuesta Primero"))) {


    }

    private void funcionGraficar(ArrayList<String> Encuesta, ArrayList<String> idEncuesta, ArrayList<String> preguntas, ArrayList<String> idPreguntas, ArrayList<String> respuesta, ArrayList<String> idRespuestas, ArrayList<String> grafico, ArrayList<Integer> cantidadDeRespuestasPorPregunta, ArrayList<String> cuantosSeleccionaronEsaRespuesta) {


        System.out.println("--< string tituloencuesta"+Encuesta.get(0));
        System.out.println("--< string Preguntas"+preguntas);
        System.out.println("--< int opciones"+cantidadDeRespuestasPorPregunta);
        int posicion = -1;
        String hola;
        for (int i=0; i < cantidadDeRespuestasPorPregunta.size();i++){
            hola = "---< Opciones ["+(i+1)+"] ";
            System.out.print(hola);
            for (int o=0; o<cantidadDeRespuestasPorPregunta.get(i);o++){
                posicion=posicion+1;
                System.out.print(respuesta.get(posicion)+", ");
            }
            System.out.println(" ");
        }

        posicion = -1;
        for (int i=0; i < cantidadDeRespuestasPorPregunta.size();i++){
            hola = "---< Respuestas ["+(i+1)+"] ";
            System.out.print(hola);
            for (int o=0; o<cantidadDeRespuestasPorPregunta.get(i);o++){
                posicion=posicion+1;
                System.out.print(cuantosSeleccionaronEsaRespuesta.get(posicion)+", ");
            }
            System.out.println(" ");
        }
    }

    @Override
    public void onClick(View view) {

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


}
