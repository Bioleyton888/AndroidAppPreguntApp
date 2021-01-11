package com.example.androidapppreguntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Registrarse_correo_contrasena extends AppCompatActivity {

    FuncionesVarias xamp = new FuncionesVarias();
    EditText eTMail, eTPassword,eTPassword2,etNombre,etApellido;
    Button botonIngresar,botonRegistrarse,buttonVolver;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_correo_contrasena);


        eTMail=(EditText)findViewById(R.id.editTextEmail);
        etNombre=(EditText)findViewById(R.id.editTextNombre);
        etApellido=(EditText)findViewById(R.id.editTextApellido);
        eTPassword=(EditText)findViewById(R.id.editTextTextNewPassword);
        eTPassword2=(EditText)findViewById(R.id.editTextTextRepeatNewPassword);
        botonIngresar=(Button)findViewById(R.id.buttonGuardarDatos);
        buttonVolver=(Button)findViewById(R.id.buttonCancelarRegistro);

        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAlLogin();
            }
        });

        botonIngresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (String.valueOf(eTMail.getText()).equals("")||String.valueOf(etNombre.getText()).equals("")||String.valueOf(etApellido.getText()).equals("")||String.valueOf(eTPassword.getText()).equals("")){
                    Toast.makeText(getApplicationContext(), "Por favor, llene todas las casillas", Toast.LENGTH_SHORT).show();
                }else{
                if (String.valueOf(eTPassword.getText()).equals(String.valueOf(eTPassword2.getText()))){
                    verificarCorreo("https://preguntappusach.000webhostapp.com/buscar_correo.php?per_correo="+eTMail.getText()+"");
                }else {
                    Toast.makeText(getApplicationContext(),"Las contraseñas no son iguales",Toast.LENGTH_SHORT).show();
                }}


            }
        });


    }

    private void verificarCorreo(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);


                        verificarCorreoExistente("https://preguntappusach.000webhostapp.com/buscar_correo_usuario.php?per_correo="+eTMail.getText()+"",jsonObject.getString("correo"));
                        //iralaparte2(jsonObject.getString("correo"),String.valueOf(etNombre.getText()),String.valueOf(etApellido.getText()),String.valueOf(eTPassword.getText()));
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("correo"), Toast.LENGTH_SHORT).show();


                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "EL CORREO NO EXISTE HERMANO", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "EL CORREO NO EXISTE HERMANO", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void verificarCorreoExistente(String rutaWebServices, final String correo){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);



                        Toast.makeText(getApplicationContext(), "El correo ya se encuentra en el sistema", Toast.LENGTH_SHORT).show();


                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "EL CORREO NO EXISTE HERMANO", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iralaparte2(correo,String.valueOf(etNombre.getText()),String.valueOf(etApellido.getText()),String.valueOf(eTPassword.getText()));
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void iralaparte2(String correo, String nombre, String apellido, String contrasena) {
        Intent intent = new Intent(this, Registrarse_datos_perfil.class); //Esto te manda a la otra ventana

   

        intent.putExtra("nombre",nombre);
        intent.putExtra("apellido",apellido);
        intent.putExtra("correo",correo);
        intent.putExtra("contrasena",contrasena);

        startActivity(intent);
        finish();
    }

    private void irAlLogin(){
        Intent intent = new Intent(this, Login.class); //Esto te manda a la otra ventana
        startActivity(intent);



        finish();
    }


}
