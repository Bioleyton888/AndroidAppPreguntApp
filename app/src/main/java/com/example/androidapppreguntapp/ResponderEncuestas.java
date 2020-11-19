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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
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

public class ResponderEncuestas extends AppCompatActivity implements View.OnClickListener {
    ImageView trumo;
    TextView tvTituloEncuesta, tvCantidadPregunta,tvTituloPregunta;
    GridLayout lolcete;
    funciones_varias xamp = new funciones_varias();
    RequestQueue requestQueue;
    LinearLayout layoutListPregunta,layoutListRespuesta;
    Button SiguientePregunta;
    //lolcete = (GridLayout)findViewById(R.id.gridlayoutpreguntas);


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_encuestas);

        tvTituloEncuesta =(TextView)findViewById(R.id.tituloEncuesta);
        tvCantidadPregunta=(TextView)findViewById(R.id.tvCantidadPreguntas);
        tvTituloPregunta=(TextView)findViewById(R.id.tvpregunta);
        layoutListPregunta = findViewById(R.id.LinearLayoutPreguntas);
        SiguientePregunta = (Button)findViewById(R.id.buttonSiguientePregunta);
        SiguientePregunta.setOnClickListener(this);


        mostrarEncuesta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_encuesta.php?enc_id="+getIntent().getStringExtra("idEncuestaPendiente")+"");



    }

    @Override
    public void onClick(View view){
        ResponderEncuestas r = new  ResponderEncuestas();
        switch (view.getId()){

            case R.id.buttonSiguientePregunta:
                //getIntent().getIntExtra("preguntaNumero",1)
                //getIntent.getString("cantidadPreguntas")
                Toast.makeText(this,"3",Toast.LENGTH_SHORT);

                if (getIntent().getIntExtra("preguntaNumero", 1) == (getIntent().getIntExtra("cantidadPreguntas", 1))) {
                    Toast.makeText(this,"FELICIDADES",Toast.LENGTH_SHORT).show();

                }else{
                    irASiguientePregunta();
                }
                break;

            case R.id.button_submit_list:
                irASiguientePregunta();
                break;

        }
    }

    private void mostrarEncuesta(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                    try {


                            jsonObject = response.getJSONObject(0);


                        tvTituloEncuesta.setText(jsonObject.getString("enc_titulo"));
                        mostrarPregunta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_pregunta.php?enc_id="+getIntent().getStringExtra("idEncuestaPendiente")+"",jsonObject.getString("enc_cantidadpreguntas"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void mostrarPregunta(String rutaWebServices, final String CantidadPreguntas){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {

                    jsonObject = response.getJSONObject(getIntent().getIntExtra("preguntaNumero",1)-1);

                    tvCantidadPregunta.setText(getIntent().getIntExtra("preguntaNumero",1)+"/"+CantidadPreguntas);
                    tvTituloPregunta.setText("Pregunta"+getIntent().getIntExtra("preguntaNumero",1)+": "+jsonObject.getString("preg_titulo"));
                    mostrarRespuestaPosibles("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_respuestas.php?enc_id="+getIntent().getStringExtra("idEncuestaPendiente")+"&preg_id="+getIntent().getIntExtra("preguntaNumero",1)+"");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void mostrarRespuestaPosibles(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        mostrarPreguntas(1,jsonObject.getString( "res_respuesta"));



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

    private void mostrarPreguntas(int cantidad, String res_respuesta) {
        for (int id=1; id <= cantidad; id++) {
            //final View preguntaPendiente = getLayoutInflater().inflate(R.layout.row_respuesta_test, null, false);

            CheckBox ch1 = new CheckBox(getApplicationContext());
            //CheckBox respuesta = (CheckBox)preguntaPendiente.findViewById(R.id.rowResponderpreguntaChecbox);
            //TextView cantidadPreguntas = (TextView)preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewNumeroPreguntas);
            //Button responderPreguntas = (Button)preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_buttonResponder);

            ch1.setText(res_respuesta);
            //cantidadPreguntas.setText(enc_cantidadpreguntas);
            //responderPreguntas.setText("Pendiente");

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
            //mostrarRespuestas(cantidad,enc_id,enc_titulo,enc_cantidadpreguntas);

            /*responderPreguntas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //irAResponderEncuestas(enc_id);
                }
            });*/
            layoutListPregunta.addView(ch1);
        }
    }

    private void irASiguientePregunta(){
        Intent intent = new Intent(this, ResponderEncuestas.class);
        Toast.makeText(this,String.valueOf(getIntent().getIntExtra("preguntaNumero",1)+1),Toast.LENGTH_SHORT).show();

        intent.putExtra("idEncuestaPendiente",getIntent().getStringExtra("idEncuestaPendiente"));
        intent.putExtra("preguntaNumero",getIntent().getIntExtra("preguntaNumero",1)+1);
        intent.putExtra("cantidadPreguntas",getIntent().getIntExtra("cantidadPreguntas",1));
        startActivity(intent);
        finish();
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

}