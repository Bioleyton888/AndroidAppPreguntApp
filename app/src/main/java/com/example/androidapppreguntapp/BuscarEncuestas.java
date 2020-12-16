package com.example.androidapppreguntapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

public class BuscarEncuestas extends AppCompatActivity implements View.OnClickListener {

    RequestQueue requestQueue;
    LinearLayout layoutList;
    Button botonVolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_encuestas);

        botonVolver= findViewById(R.id.EncuestasBotonVolver);
        layoutList= findViewById(R.id.LinearLayoutEncuestas);
        botonVolver.setOnClickListener(this);

        mostrarpreguntas(getIntent().getStringExtra("URL"));



    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.EncuestasBotonVolver:
                irAAdministrarCuestionario();
                break;

        }

    }

    private void mostrarpreguntas(String rutaWebServices) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        mostrarEncuestas(1, jsonObject.getString("enc_id"), jsonObject.getString("enc_titulo"), jsonObject.getString("enc_cantidadpreguntas"),jsonObject.getString("enc_fechacreacion"),jsonObject.getString("enc_fechatermino"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void mostrarEncuestas(int cantidad, final String enc_id, final String enc_titulo, final String enc_cantidadpreguntas, final String enc_fechacreacion, final String enc_fechatermino) {
        for (int id = 1; id <= cantidad; id++) {
            final View preguntaPendiente = getLayoutInflater().inflate(R.layout.row_preguntas_pendientes, null, false);

            TextView tituloEncuesta = (TextView) preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewTituloEncuesta);
            TextView cantidadPreguntas = (TextView) preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewNumeroPreguntas);
            Button responderPreguntas = (Button) preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_buttonResponder);

            tituloEncuesta.setText(enc_titulo);
            cantidadPreguntas.setText(enc_fechacreacion);
            responderPreguntas.setText("Seleccionar");

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            responderPreguntas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irAAdministrarCuestionario2(enc_id, enc_cantidadpreguntas,enc_titulo,enc_fechacreacion,enc_fechatermino);
                }
            });
            layoutList.addView(preguntaPendiente);
        }
    }

    private void irAAdministrarCuestionario2(String enc_id, String enc_cantidadpreguntas, String enc_titulo, String enc_fechacreacion, String enc_fechatermino) {
        Intent intent = new Intent(this, AdministrarCuestionario .class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("enc_titulo",enc_titulo);
        intent.putExtra("enc_fechacreacion",enc_fechacreacion);
        intent.putExtra("enc_fechatermino",enc_fechatermino);
        intent.putExtra("cantidadPreguntas",enc_cantidadpreguntas);
        intent.putExtra("enc_id",enc_id);
        startActivity(intent);
        finish();
    }

    public TextView descriptionTextView(Context context, String text) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(context);
        textView.setLayoutParams(lparams);
        textView.setTextSize(10);
        textView.setTextColor(Color.rgb(0, 0, 0));
        textView.setText("" + text + "");
        textView.setMaxEms(8);
        return textView;
    }

    public EditText tituloPregunta(Context context) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        int id = 0;
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextColor(Color.rgb(0, 0, 0));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        return editText;
    }

    public Button botonAgregarPreguntas(final Context context, String text, final int id) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button boton = new Button(context);
        boton.setId(id);
        boton.setMinEms(2);
        //boton.setTextColor(Color.rgb(0,0,0));
        boton.setText("" + text + "");
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return boton;
    }

    private void irAAdministrarCuestionario(){
        Intent intent = new Intent(this, AdministrarCuestionario.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("enc_titulo","Seleccione una Encuesta");
        intent.putExtra("enc_id","1");
        intent.putExtra("enc_fechacreacion"," ");
        intent.putExtra("cantidadPreguntas"," ");
        startActivity(intent);
        finish();
    }

}
