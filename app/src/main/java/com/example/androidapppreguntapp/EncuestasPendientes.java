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
import androidx.gridlayout.widget.GridLayout;

import android.widget.LinearLayout;
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

public class EncuestasPendientes extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layoutList;
    funciones_varias xamp = new funciones_varias();
    RequestQueue requestQueue;
    String correo;
    Button buttonVolver;
    private GridLayout mlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas_pendientes);

        buttonVolver = (Button)findViewById(R.id.encuestasPendientesBotonVolver);
        mlayout = (GridLayout)findViewById(R.id.layoutEncuestasPendientes);
        layoutList = findViewById(R.id.LinearLayoutEncuestasPendientes);
        buscarEncuestasPendientes("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_encuestas_Pendientes.php");
    }

    private void buscarEncuestasPendientes(String rutaWebServices) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        mostrarPreguntas(1,jsonObject.getString("enc_id"),jsonObject.getString("enc_titulo"),jsonObject.getString("enc_cantidadpreguntas"));

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

    private void mostrarPreguntas(int cantidad, final String enc_id, String enc_titulo, String enc_cantidadpreguntas) {
        for (int id=1; id <= cantidad; id++) {
            final View preguntaPendiente = getLayoutInflater().inflate(R.layout.row_preguntas_pendientes, null, false);

            TextView tituloEncuesta = (TextView)preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewTituloEncuesta);
            TextView cantidadPreguntas = (TextView)preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewNumeroPreguntas);
            Button responderPreguntas = (Button)preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_buttonResponder);

            tituloEncuesta.setText(enc_titulo);
            cantidadPreguntas.setText(enc_cantidadpreguntas);
            responderPreguntas.setText("Pendiente");

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            responderPreguntas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irAResponderEncuestas(enc_id);
                }
            });
            layoutList.addView(preguntaPendiente);
        }
    }

    public TextView descriptionTextView(Context context, String text) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(context);
        textView.setLayoutParams(lparams);
        textView.setTextSize(10);
        textView.setTextColor(Color.rgb(0,0,0));
        textView.setText("" +text+ "");
        textView.setMaxEms(8);
        return textView;
    }

    public EditText tituloPregunta(Context context){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        int id= 0;
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        return editText;
    }

    public Button botonAgregarPreguntas(final Context context, String text, final int id){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button boton = new Button(context);

        boton.setId(id);
        boton.setMinEms(2);
        //boton.setTextColor(Color.rgb(0,0,0));
        boton.setText("" +text+ "");
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return boton;
    }

    private void irAResponderEncuestas(String idEncuestasPendientes){
        Intent intent = new Intent(this, ResponderEncuestas.class);
        intent.putExtra("idEncuestaPendiente",idEncuestasPendientes);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.encuestasPendientesBotonVolver:
                irAResponderEncuestas("dssds");
                break;

            case R.id.button_submit_list:

                break;

        }


    }

    private void removeView(View view){

        layoutList.removeView(view);

    }

}
