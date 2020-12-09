package com.example.androidapppreguntapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
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

import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class EditarCuestionarios extends AppCompatActivity {
    String a,b,c,d,e,f;
    LinearLayout layoutList;
    String lastID = new String();
    RequestQueue requestQueue;
    FuncionesVarias xamp = new FuncionesVarias();
    private GridLayout mlayout;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    Button addItem, addFecha,buttonsubirEncuesta;
    EditText etCantidadDePreguntas, etFechaTermino,etTituloEncuesta;
    TextView tvID;
    Calendar calendar= Calendar.getInstance();
    static final int DATE_ID = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cuestionario);

        sMonthIni = calendar.get(Calendar.MONTH);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sYearIni= calendar.get(Calendar.YEAR);

        tvID =(TextView)findViewById(R.id.editar_cuestionario_textViewIDEncuesta);
        mlayout = (GridLayout)findViewById(R.id.editar_cuestionario_myLayout);
        etTituloEncuesta = (EditText)findViewById(R.id.editar_cuestionario_editTextTitulo);
        etCantidadDePreguntas = (EditText)findViewById(R.id.editar_cuestionario_editTextCantidadDePreguntas);
        etFechaTermino = (EditText)findViewById(R.id.editar_cuestionario_editTextDate);
        buttonsubirEncuesta = (Button)findViewById(R.id.editar_cuestionario_buttonSubirEncuesta);
        addItem = (Button)findViewById(R.id.editar_cuestionario_buttonAgregarPregunta);
        addFecha= (Button)findViewById(R.id.editar_cuestionario_buttonAgregarFecha);
        layoutList = findViewById(R.id.editar_cuestionario_contenedor);

        etTituloEncuesta.setText(getIntent().getStringExtra("enc_titulo"));
        etCantidadDePreguntas.setText(getIntent().getStringExtra("enc_cantidadpreguntas"));
        etFechaTermino.setText(getIntent().getStringExtra("enc_fechatermino"));
        tvID.setText(getIntent().getStringExtra("enc_id"));

        funcionlamao();


    }

    private void funcionlamao() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_pregunta.php?enc_id=" + getIntent().getStringExtra("enc_id")+ "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        System.out.println("----> id:"+jsonObject.getString("preg_id")+" "+jsonObject.getString("preg_titulo"));
                        mostrarEncuestas(1,getIntent().getStringExtra("enc_id"),jsonObject.getString("preg_titulo"),"7");


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



    private void mostrarEncuestas(int cantidad, final String enc_id, String enc_titulo, final String enc_cantidadpreguntas) {
        for (int id = 1; id <= cantidad; id++) {
            final View preguntaCreada = getLayoutInflater().inflate(R.layout.row_pregunta, null, false);

            TextView tituloPregunta = (TextView) preguntaCreada.findViewById(R.id.row_pregunta_nombrePregunta);
            Button EditarPregunta = (Button) preguntaCreada.findViewById(R.id.row_pregunta_buttonIraEditarPregunta);

            tituloPregunta.setText(enc_titulo);
            EditarPregunta.setText("Editar");

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            EditarPregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irAResponderEncuestas(enc_id, enc_cantidadpreguntas);
                }
            });
            layoutList.addView(preguntaCreada);
        }
    }

    private void irAResponderEncuestas(String enc_id, String enc_cantidadpreguntas) {
        System.out.println("---> lamao");
    }


}
