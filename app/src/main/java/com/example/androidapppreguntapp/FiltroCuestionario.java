package com.example.androidapppreguntapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FiltroCuestionario extends AppCompatActivity {

    private Spinner spinnerOrientacionSexual, spinnerFacultad,spinnerCarrera,spinnerComuna,spinnerEstadoCivil,spinnerGenero;
    Button botonTerminarEncuesta,botonSiguenteFiltro,botonVolver;
    funciones_varias xamp = new funciones_varias();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_cuestionario);

        spinnerCarrera = (Spinner)findViewById(R.id.spinner_Carrera);
        spinnerComuna = (Spinner)findViewById(R.id.spinner_Comuna);
        spinnerEstadoCivil = (Spinner)findViewById(R.id.spinner_Estado_Civil);
        spinnerFacultad = (Spinner)findViewById(R.id.spinner_Facultad);
        spinnerGenero = (Spinner)findViewById(R.id.spinner_Genero);
        spinnerOrientacionSexual = (Spinner)findViewById(R.id.spinner_Orientacion_Sexual);


        mostrarDatosSpinnerCarrera("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_Carreras.php");
        mostrarDatosSpinnerComuna("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_Comunas.php");
        mostrarDatosSpinnerEstadoCivil("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_Estados_civiles.php");
        mostrarDatosSpinnerFacultad("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_Facultades.php");
        mostrarDatosSpinnerGenero("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_Generos.php");
        mostrarDatosSpinnerOrientacionSexual("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_Orientaciones_Sexuales.php");


    }

    private void mostrarDatosSpinnerCarrera(String rutaWebServices) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeCarreras.add(jsonObject.getString("car_nombre"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
                spinnerCarrera.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void mostrarDatosSpinnerComuna(String rutaWebServices) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeCarreras.add(jsonObject.getString("com_nombre"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
                spinnerComuna.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void mostrarDatosSpinnerEstadoCivil(String rutaWebServices) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeCarreras.add(jsonObject.getString("eciv_nombre"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
                spinnerEstadoCivil.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void mostrarDatosSpinnerFacultad(String rutaWebServices) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeCarreras.add(jsonObject.getString("fac_nombre"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
                spinnerFacultad.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void mostrarDatosSpinnerGenero(String rutaWebServices) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeCarreras.add(jsonObject.getString("gen_nombre"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
                spinnerGenero.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void mostrarDatosSpinnerOrientacionSexual(String rutaWebServices) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeCarreras.add(jsonObject.getString("sex_nombre"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
                spinnerOrientacionSexual.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
