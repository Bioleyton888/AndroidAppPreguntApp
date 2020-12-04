package com.example.androidapppreguntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class TerminarEncuesta extends AppCompatActivity implements View.OnClickListener {
    RequestQueue requestQueue;
    TextView tvTitulo,tvCantidadPreguntas,tvFechaTermino;
    Button botonBajarEncuestas,botonVolver;
    FuncionesVarias xamp = new FuncionesVarias();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminar_encuesta);

        tvTitulo=(TextView)findViewById(R.id.textViewTitulo);
        tvCantidadPreguntas=(TextView)findViewById(R.id.textViewCantidadPreguntas);
        tvFechaTermino=(TextView) findViewById(R.id.textViewFechaTermino);
        botonBajarEncuestas=(Button)findViewById(R.id.buttonbajarEncuesta);
        botonVolver=(Button)findViewById(R.id.Terminar_encuesta_button_Volver);
        botonVolver.setOnClickListener(this);
        botonBajarEncuestas.setOnClickListener(this);

        tvTitulo.setText(getIntent().getStringExtra("enc_titulo"));
        tvCantidadPreguntas.setText(getIntent().getStringExtra("enc_cantidadpreguntas"));
        tvFechaTermino.setText(getIntent().getStringExtra("enc_fechacreacion"));





    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonbajarEncuesta:
                if (botonVolver.getText().equals("Bajar Encuesta")){
                    botonVolver.setText("Subir Encuesta");
                    bajarEncuesta("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/bajar_encuesta.php","0");
                }else {
                    botonVolver.setText("Bajar Encuesta");
                    bajarEncuesta("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/bajar_encuesta.php", "1");
                }

                break;

            case R.id.Terminar_encuesta_button_Volver:
                irAAdministrarCuestionario();
                break;

        }
    }

    private void bajarEncuesta(final String rutaWebServices, final String disponibilidad){

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
                parametros.put("enc_id",getIntent().getStringExtra("enc_id"));
                parametros.put("disponibilidad",disponibilidad);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
        Toast.makeText(getApplicationContext(), "Encuestas bajadas", Toast.LENGTH_SHORT).show();
    }

    private void irAAdministrarCuestionario(){
        Intent intent = new Intent(this, AdministrarCuestionario.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("encuesta",getIntent().getStringExtra("enc_titulo"));
        intent.putExtra("enc_id",getIntent().getStringExtra("enc_id"));
        intent.putExtra("fecha",getIntent().getStringExtra("enc_fechacreacion"));
        intent.putExtra("cantidadPreguntas",getIntent().getStringExtra("enc_cantidadpreguntas"));
        startActivity(intent);
        finish();
    }


}
