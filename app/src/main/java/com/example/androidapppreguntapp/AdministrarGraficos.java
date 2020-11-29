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
    TextView tvIdEncuesta, tvIDPregunta;
    Button botonAnadirGrafico, botonGenerarPDF, botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ArrayList<String> listadeEncuestas;
        listadeEncuestas = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        String TituloEncuesta;
        setContentView(R.layout.activity_administrar_graficos);


        String[] Pregunta = {"Seleccionar Encuesta Primero"};
        String[] Diagramas = {"Diagrama de barras", "Diagrama de pie"};
        spinnerEncuesta = (Spinner) findViewById(R.id.spinnerEncuesta);
        spinnerPregunta = (Spinner) findViewById(R.id.spinnerPregunta);
        spinnerTipoGrafico = (Spinner) findViewById(R.id.spinnerDiagrama);

        botonAnadirGrafico = (Button) findViewById(R.id.buttonAnadirGrafico);
        botonGenerarPDF = (Button) findViewById(R.id.buttonGenerarPDF);
        botonVolver = (Button) findViewById(R.id.buttonSalir);
        tvIdEncuesta = (TextView) findViewById(R.id.textViewEncuestaId);
        tvIDPregunta = (TextView) findViewById(R.id.textViewPreguntaId);
        botonVolver.setOnClickListener(this);
        botonGenerarPDF.setOnClickListener(this);
        botonAnadirGrafico.setOnClickListener(this);
        ArrayAdapter<String> adapterPregunta = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pregunta);
        ArrayAdapter<String> adapterDiagramas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Diagramas);
        spinnerPregunta.setAdapter(adapterPregunta);
        spinnerTipoGrafico.setAdapter(adapterDiagramas);


        botonAnadirGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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
        spinnerPregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (( (!adapterView.getItemAtPosition(i).equals("Seleccionar Encuesta Primero")))) {
                    if ((!adapterView.getItemAtPosition(i).equals("Seleccione una Pregunta"))) {

                        tvIDPregunta.setText(Integer.toString(i));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerEncuesta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).equals("Seleccione una Encuesta")) {
                    Toast.makeText(adapterView.getContext(), "Seleccionado: " + adapterView.getItemAtPosition(i).toString() + " id: " + listaDeIDEncuestas.get(i), Toast.LENGTH_SHORT).show();
                    tvIdEncuesta.setText(listaDeIDEncuestas.get(i));
                    mostrarPreguntas("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_pregunta.php?enc_id=" + tvIdEncuesta.getText() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //if (((spinnerPregunta.getSelectedItem() != "Seleccione una Pregunta") || (adapterView.getItemAtPosition(i) != "Seleccionar Encuesta Primero"))) {


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
}
