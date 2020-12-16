package com.example.androidapppreguntapp;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class EditarCuestionario extends AppCompatActivity {
    String a,b,c,d,e,f;
    LinearLayout layoutList;
    String lastID = new String();
    RequestQueue requestQueue;
    FuncionesVarias xamp = new FuncionesVarias();
    private GridLayout mlayout;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    Button addItem, addFecha,buttonsubirEncuesta,buttonCancelar;
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
        buttonCancelar = (Button)findViewById(R.id.editar_cuestionario_buttonCancelar);
        addItem = (Button)findViewById(R.id.editar_cuestionario_buttonAgregarPregunta);
        addFecha= (Button)findViewById(R.id.editar_cuestionario_buttonAgregarFecha);
        layoutList = findViewById(R.id.editar_cuestionario_contenedor);
        etTituloEncuesta.setText(getIntent().getStringExtra("enc_titulo"));
        etCantidadDePreguntas.setText(getIntent().getStringExtra("enc_cantidadpreguntas"));
        etFechaTermino.setText(getIntent().getStringExtra("enc_fechatermino"));
        tvID.setText(getIntent().getStringExtra("enc_id"));

        buscarCuestionario();


        buttonsubirEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarEncuesta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/editar_encuesta.php");
                Intent intent = new Intent(EditarCuestionario.this,FiltroCuestionario.class);
                Bundle bundle = new Bundle();

                intent.putExtra("correo",getIntent().getStringExtra("correo"));
                intent.putExtra("idEncuesta",getIntent().getStringExtra("enc_id"));
                intent.putExtra("cantidadDePreguntas",getIntent().getStringExtra("enc_cantidadpreguntas"));
                intent.putExtra("fechaCreacion",getIntent().getStringExtra("enc_fechacreacion"));
                intent.putExtra("fecha",getIntent().getStringExtra("enc_fechatermino"));
                intent.putExtra("tituloEncuesta",getIntent().getStringExtra("enc_titulo"));

                intent.putExtra("idPregunta",getIntent().getStringExtra("enc_titulo"));
                intent.putExtra("esCuestionarioNuevo",false);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        buttonCancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(EditarCuestionario.this, AdministrarCuestionario.class); //Esto te manda a la otra ventana
            intent.putExtra("correo",getIntent().getStringExtra("correo"));
            intent.putExtra("enc_titulo",getIntent().getStringExtra("enc_titulo"));
            intent.putExtra("enc_id",getIntent().getStringExtra("enc_id"));
            intent.putExtra("enc_fechacreacion",getIntent().getStringExtra("enc_fechacreacion"));
            intent.putExtra("enc_fechatermino",getIntent().getStringExtra("enc_fechatermino"));
            intent.putExtra("enc_cantidadpreguntas",getIntent().getStringExtra("cantidadPreguntas"));
            startActivity(intent);
            finish();

        }
    });
}






    private void editarEncuesta(final String rutaWebServices){

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date currentDate = new Date();
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
                a =getIntent().getStringExtra("correo");
                parametros.put("idEncuesta", (String) tvID.getText());
                parametros.put("correo",getIntent().getStringExtra("correo"));
                b =etTituloEncuesta.getText().toString();
                parametros.put("tituloEncuesta",etTituloEncuesta.getText().toString());
                parametros.put("fechaCreacion",c);
                d ="1";
                parametros.put("disponibilidad","1");
                e =etCantidadDePreguntas.getText().toString();
                parametros.put("cantidadPreguntas",etCantidadDePreguntas.getText().toString());
                f =etFechaTermino.getText().toString();
                parametros.put("fechaTermino",etFechaTermino.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
        Toast.makeText(getApplicationContext(), "datos ingresados ", Toast.LENGTH_SHORT).show();
    }



    private void buscarCuestionario() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_pregunta.php?enc_id=" + getIntent().getStringExtra("enc_id")+ "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        mostrarCuestionario(1,getIntent().getStringExtra("enc_id"),jsonObject.getString("preg_titulo"),jsonObject.getString("preg_id"));


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

    private void mostrarCuestionario(int cantidad, final String enc_id, final String preg_titulo, final String preg_id) {

            final View preguntaCreada = getLayoutInflater().inflate(R.layout.row_pregunta, null, false);

            TextView tituloPregunta = (TextView) preguntaCreada.findViewById(R.id.row_pregunta_nombrePregunta);
            Button EditarPregunta = (Button) preguntaCreada.findViewById(R.id.row_pregunta_buttonIraEditarPregunta);

            tituloPregunta.setText(preg_titulo);
            EditarPregunta.setText("Editar");

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);


            EditarPregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irAEditarPregunta(enc_id, preg_id,preg_titulo);
                }
            });
            layoutList.addView(preguntaCreada);

    }

    private void irAEditarPregunta(String enc_id, String preg_id, String preg_titulo) {

        Intent intent = new Intent(this, EditarPregunta.class);
        intent.putExtra("enc_id", enc_id);
        intent.putExtra("preg_id", preg_id);
        intent.putExtra("preg_titulo",preg_titulo);
        intent.putExtra("correo", getIntent().getStringExtra("correo"));
        intent.putExtra("enc_titulo", getIntent().getStringExtra("enc_titulo"));


        intent.putExtra("enc_fechacreacion",getIntent().getStringExtra("enc_fechacreacion"));
        intent.putExtra("enc_fechatermino",getIntent().getStringExtra("enc_fechatermino"));
        intent.putExtra("enc_cantidadpreguntas",getIntent().getStringExtra("enc_cantidadpreguntas"));

        startActivity(intent);
        finish();

    }


}
