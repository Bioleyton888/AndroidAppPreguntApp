package com.example.androidapppreguntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class PerfilDeAdministrador extends AppCompatActivity {
    TextView tvAnoIngreso, tvHijos, tvSemestre, tvSexo,tvCarrera,tvFacultad,tvEstadoCivil,tvComuna,tvGenero;
    Button botonVolver;
    String correo,nombre,apellido;
    RequestQueue requestQueue;
    FuncionesVarias xamp = new FuncionesVarias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_de_usuario);

        nombre = getIntent().getStringExtra("Nombre").toLowerCase();
        apellido = getIntent().getStringExtra("Apellido").toLowerCase();
        correo = getIntent().getStringExtra("correo");

        botonVolver = (Button) findViewById(R.id.botonVolverAlmenu);
        tvAnoIngreso = (TextView)findViewById(R.id.textViewAnongreso);
        tvHijos = (TextView)findViewById(R.id.textViewHijos);
        tvSemestre = (TextView)findViewById(R.id.textViewSemestre);
        tvSexo = (TextView)findViewById(R.id.textViewSexo);
        tvCarrera = (TextView)findViewById(R.id.textViewCarrera);
        tvFacultad = (TextView)findViewById(R.id.textViewFacultad);
        tvEstadoCivil = (TextView)findViewById(R.id.textViewEstadoCivil);
        tvComuna = (TextView)findViewById(R.id.textViewComuna);
        tvGenero = (TextView) findViewById(R.id.textViewGenero);

        correo = getIntent().getStringExtra("correo");

        perfilDeUsuario("https://preguntappusach.000webhostapp.com/buscar_perfil_usuario.php?per_correo="+correo+"");
        tvAnoIngreso.setText(correo);


        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAlmenuPrincipal();

            }
        });


    }

    private void perfilDeUsuario(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        tvAnoIngreso.setText("Año de ingreso: "+jsonObject.getString("usu_anoingreso"));
                        tvHijos.setText("Hijos: "+jsonObject.getString("usu_hijos"));
                        tvSemestre.setText("Semestre: "+jsonObject.getString("usu_semestre"));
                        tvSexo.setText("Orientacion Sexual: "+jsonObject.getString("sex_nombre"));
                        tvCarrera.setText("Carrera: "+jsonObject.getString("car_nombre"));
                        tvFacultad.setText("Facultad: "+jsonObject.getString("fac_nombre"));
                        tvComuna.setText("Comuna: "+jsonObject.getString("com_nombre"));
                        tvEstadoCivil.setText("Estado civil: "+jsonObject.getString("eciv_nombre"));
                        tvGenero.setText("Genero: "+jsonObject.getString("gen_nombre"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void irAlmenuPrincipal(){
        Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana
        intent.putExtra("Nombre",nombre);
        intent.putExtra("Apellido",apellido);
        intent.putExtra("correo",correo);
        startActivity(intent);
        finish();
    }
}