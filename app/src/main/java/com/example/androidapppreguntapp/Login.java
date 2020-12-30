package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {
    FuncionesVarias xamp = new FuncionesVarias();
    EditText eTMail, eTPassword;
    Button botonIngresar;
    
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTMail = (EditText)findViewById(R.id.editTextEmailAddress);
        eTPassword = (EditText)findViewById(R.id.editTextPassword);
        botonIngresar = (Button)findViewById(R.id.buttonLogin);



        botonIngresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login("https://preguntappusach.000webhostapp.com/login_administrador.php?per_correo="+eTMail.getText()+"&per_contrasena="+eTPassword.getText()+"");






            }
        });

    }

    private void loginUsuario(String rutaWebServices){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;


                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();


                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
                        irAMenuPrincipalUsuario(jsonObject.getString("per_nombre"), jsonObject.getString("per_apellidos"), jsonObject.getString("per_correo"));
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
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void login(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                        if (FuncionesVarias.convertToBoolean(jsonObject.getString("per_esadmin")) == true){

                            irAMenuPrincipalAdministrador(jsonObject.getString("per_nombre"), jsonObject.getString("per_apellidos"), jsonObject.getString("per_correo"));

                        }else {
                            irAMenuPrincipalUsuario(jsonObject.getString("per_nombre"), jsonObject.getString("per_apellidos"), jsonObject.getString("per_correo"));
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
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void irAMenuPrincipalUsuario(String nombre, String apellido, String correo){
        Intent intent = new Intent(this, MenuPrincipalUsuario.class); //Esto te manda a la otra ventana
        intent.putExtra("Nombre",nombre);
        intent.putExtra("Apellido",apellido);
        intent.putExtra("correo",correo);
        startActivity(intent);
        finish();
    }

    private void irAMenuPrincipalAdministrador(String nombre, String apellido, String correo){
        Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana

        intent.putExtra("correo",correo);
        startActivity(intent);
        finish();
    }

}