package com.example.androidapppreguntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;

public class GestionarAdministrador extends AppCompatActivity {

    Button ButtonVolver,buttonBuscar,buttondecicion;
    EditText etCorreo;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_administradores);

        ButtonVolver = (Button) findViewById(R.id.botonVolverAlmenu);
        buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        buttondecicion = (Button) findViewById(R.id.buttonDecision);
        etCorreo = (EditText)findViewById(R.id.editTextTextPersonName);

        ButtonVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                IrAMenuPrincipalAdministrador();
            }
        });

        buttondecicion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (buttondecicion.getText().equals("Primero Ingrese un correo")){
                    Toast.makeText(getApplicationContext(),"Primero busque una persona",Toast.LENGTH_SHORT).show();
                }else if (buttondecicion.getText().equals("Quitar Rol de administrador")){
                    modificarRol("https://preguntappusach.000webhostapp.com/editar_esadmin.php","0", String.valueOf(etCorreo.getText()));
                    buttondecicion.setText("Primero Ingrese un correo");
                    buttondecicion.setBackgroundResource(R.color.grey);
                }else {
                    modificarRol("https://preguntappusach.000webhostapp.com/editar_esadmin.php","1", String.valueOf(etCorreo.getText()));
                    buttondecicion.setText("Primero Ingrese un correo");
                    buttondecicion.setBackgroundResource(R.color.grey);
                }
            }
        });

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarPersona("https://preguntappusach.000webhostapp.com/buscar_admin.php?per_correo="+ etCorreo.getText()+"&per_contrasena="+"");
            }
        });

    }


    private void modificarRol(final String rutaWebServices, final String esadmin, final String correo) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, rutaWebServices, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


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

                parametros.put("preg_id", correo);
                parametros.put("per_esadmin",esadmin);

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
        Toast.makeText(getApplicationContext(),"Rol Cambiado",Toast.LENGTH_SHORT).show();

    }

    private void IrAMenuPrincipalAdministrador(){
        Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();
    }

    private void buscarPersona(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        if (FuncionesVarias.convertToBoolean(jsonObject.getString("per_esadmin")) == true){

                            buttondecicion.setText("Quitar Rol de administrador");
                            buttondecicion.setBackgroundResource(R.color.colorPrimary);

                        }else {
                            buttondecicion.setText("Convertir en administrador");
                            buttondecicion.setBackgroundResource(R.color.botonesAdmin);

                        }


                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
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
