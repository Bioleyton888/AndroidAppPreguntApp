package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuPrincipalAdministrador extends AppCompatActivity {
    funciones_varias xamp = new funciones_varias();
    TextView tvBienvenida;
    Button botonPerfil,botonCerrarSesion,BotonAdministrarEncuestas;
    String nombre, apellido;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal_administrador);

        tvBienvenida =(TextView)findViewById(R.id.textoBienvenida);
        botonCerrarSesion = (Button)findViewById(R.id.botonVolver);
        BotonAdministrarEncuestas= (Button)findViewById(R.id.botonirAAdministrarEncuestas);

        buscarNombreyApellido("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_NombreYApellidos.php?per_correo="+getIntent().getStringExtra("correo")+"");



        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAlLogin();

            }
        });
        BotonAdministrarEncuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAAdministrarCuestionario();

            }
        });
    }


    private void irAlLogin(){
        Intent intent = new Intent(this, Login.class); //Esto te manda a la otra ventana
        startActivity(intent);
        finish();
    }

    private void irAAdministrarCuestionario(){
        Intent intent = new Intent(this, AdministrarCuestionario.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();
    }

    private void buscarNombreyApellido(String rutaWebServices){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombre= jsonObject.getString("per_nombre");
                        apellido= jsonObject.getString("per_apellidos");
                        tvBienvenida.setText("¡Hola "+nombre.substring(0,1).toUpperCase()+nombre.substring(1) +" "+apellido.substring(0,1).toUpperCase()+apellido.substring(1)+"!");



        //irAMenuPrincipalAdministrador(jsonObject.getString("per_nombre"), jsonObject.getString("per_apellidos"), jsonObject.getString("per_correo"));
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

}



