package com.example.androidapppreguntapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registrarse_datos_perfil extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerOrientacionSexual, spinnerFacultad,spinnerCarrera,spinnerComuna,spinnerEstadoCivil,spinnerGenero,spinnerSemestre;
    Button botonTerminarEncuesta,botonVolver;
    FuncionesVarias xamp = new FuncionesVarias();
    RequestQueue requestQueue;
    EditText EtAnoIngreso;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_datos_perfil);


        spinnerCarrera = (Spinner)findViewById(R.id.spinner_Carrera);
        spinnerComuna = (Spinner)findViewById(R.id.spinner_Comuna);
        spinnerEstadoCivil = (Spinner)findViewById(R.id.spinner_Estado_Civil);
        spinnerFacultad = (Spinner)findViewById(R.id.spinner_Facultad);
        spinnerGenero = (Spinner)findViewById(R.id.spinner_Genero);
        spinnerOrientacionSexual = (Spinner)findViewById(R.id.spinner_Orientacion_Sexual);
        spinnerSemestre= (Spinner)findViewById(R.id.spinner_semestre);

        botonTerminarEncuesta= (Button)findViewById(R.id.button_Terminar_Filtros);
        botonVolver= (Button)findViewById(R.id.button_volver);

        botonVolver.setOnClickListener(this);
        botonTerminarEncuesta.setOnClickListener(this);
        EtAnoIngreso=(EditText)findViewById(R.id.editTextAnoingreso) ;


        crearPresona("https://preguntappusach.000webhostapp.com/crear_persona.php");

        mostrarDatosSpinnerCarrera();
        mostrarDatosSpinnerComuna("https://preguntappusach.000webhostapp.com/buscar_comunas.php");
        mostrarDatosSpinnerEstadoCivil("https://preguntappusach.000webhostapp.com/buscar_estados_civiles.php");
        mostrarDatosSpinnerFacultad();
        mostrarDatosSpinnerGenero("https://preguntappusach.000webhostapp.com/buscar_generos.php");
        mostrarDatosSpinnerOrientacionSexual("https://preguntappusach.000webhostapp.com/buscar_orientaciones_sexuales.php");
        mostrarDatosSpinnerSemestre();




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.botonVolver:
                irAlLogin();

                break;

            case R.id.button_Terminar_Filtros:

                //IngresarFiltro("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_usuario.php");
                IngresarFiltro("https://preguntappusach.000webhostapp.com/crear_usuario.php");
//                Toast.makeText(this,"Persona creada Ingresados, Encuesta Subida",Toast.LENGTH_SHORT).show();
//                irAlLogin();
                break;
        }
    }

    private void irAlLogin(){
        Intent intent = new Intent(this, Login.class); //Esto te manda a la otra ventana
        startActivity(intent);



        finish();
    }

    private void IngresarFiltro(final String rutaWebServices){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, rutaWebServices, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               Toast.makeText(getApplicationContext(),"BIENVENIDO A PREGUNTAPP",Toast.LENGTH_SHORT).show();
               irAlLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                Editable anio=EtAnoIngreso.getText();

                parametros.put("car_nombre", (String) spinnerCarrera.getSelectedItem());
                parametros.put("per_correo", getIntent().getStringExtra("correo"));
                parametros.put("com_nombre",(String) spinnerComuna.getSelectedItem());
                parametros.put("eciv_nombre",(String) spinnerEstadoCivil.getSelectedItem());
                parametros.put("fac_nombre",(String) spinnerFacultad.getSelectedItem());
                parametros.put("gen_nombre",(String) spinnerGenero.getSelectedItem());
                parametros.put("sex_nombre",(String) spinnerOrientacionSexual.getSelectedItem());
                parametros.put("ano_ingreso", String.valueOf(anio));
                if (spinnerSemestre.getSelectedItem().equals("no aplica")){
                    parametros.put("semestre", "1");
                }else {
                    parametros.put("semestre", (String) spinnerSemestre.getSelectedItem());
                }


                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void irACrearCuestionario() {

    }


    private void mostrarDatosSpinnerSemestre() {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();

        for (int i=0;i<11;i++){

            if (i==0){

                listaDeCarreras.add("no aplica");

            }else{
                listaDeCarreras.add(String.valueOf(i));
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
        spinnerSemestre.setAdapter(adapter);
    }

    private void mostrarDatosSpinnerCarrera( ) {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        listaDeCarreras.add("Analista en Computacion Cientifica / Licenciatura en Ciencia de la Computacion");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
        spinnerCarrera.setAdapter(adapter);

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

    private void mostrarDatosSpinnerFacultad() {
        final ArrayList<String> listaDeCarreras;
        listaDeCarreras = new ArrayList<String>();
        listaDeCarreras.add("Facultad de ciencias");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,listaDeCarreras);
        spinnerFacultad.setAdapter(adapter);

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

    private void crearPresona(final String rutaWebServices) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, rutaWebServices, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"CREADO HERMANO",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();



                parametros.put("per_correo", getIntent().getStringExtra("correo"));
                parametros.put("per_contrasena", getIntent().getStringExtra("contrasena"));
                parametros.put("per_nombre", getIntent().getStringExtra("nombre"));
                parametros.put("per_apellidos", getIntent().getStringExtra("apellido"));
                parametros.put("per_esadmin","0");

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request


    }


}
