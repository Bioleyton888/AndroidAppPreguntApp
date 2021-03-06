package com.example.androidapppreguntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class EditarPregunta extends AppCompatActivity implements View.OnClickListener {
    String enc_id, lastpreg_id;
    FuncionesVarias xamp = new FuncionesVarias();
    LinearLayout layoutList;
    TextView tvID;
    Button buttonAdd,buttonback;
    Button buttonSubmitList;
    EditText etCantidadDeOpciones, etPeguntaEnCuestion;
    RequestQueue requestQueue;
    List<String> teamList = new ArrayList<>();
    ArrayList<Cricketer> cricketersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_preguntas);


        enc_id = getIntent().getStringExtra("enc_id");
        etPeguntaEnCuestion=findViewById(R.id.editTextTituloOpcion);
        layoutList = findViewById(R.id.contenedor);
        buttonAdd = findViewById(R.id.button_add);
        buttonback = findViewById(R.id.cancelar);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        etCantidadDeOpciones = (EditText)findViewById(R.id.editTextCantidadDeOpciones);
        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
        buttonback.setOnClickListener(this);

        etPeguntaEnCuestion.setText(getIntent().getStringExtra("preg_titulo"));


        AgregarOpciones();



        //crearPreguntaEnBlanco("https://preguntappusach.000webhostapp.com/crear_pregunta.php");

//Integer.parseInt(CantidadDeOpciones.getText().toString())
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_add:
                addViewRespuesta(parseInt(etCantidadDeOpciones.getText().toString()), ("res_respuesta"));
                modificarPregunta("https://preguntappusach.000webhostapp.com/editar_pregunta.php");
                break;

            case R.id.button_submit_list:
                //deleteOpciones("https://preguntappusach.000webhostapp.com/eliminar_respuesta.php");
                modificarPregunta("https://preguntappusach.000webhostapp.com/editar_pregunta.php");
                if(checkIfValidAndRead()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list",cricketersList);

                    irAEditarCuestionario();


                }
                break;

            case R.id.cancelar:
                irAEditarCuestionario();
                break;
        }
    }

    private void deleteOpciones(String rutaWebServices) {

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
                parametros.put("preg_id",getIntent().getStringExtra("preg_id"));

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void AgregarOpciones() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_respuestas.php?enc_id=" + getIntent().getStringExtra("enc_id")+"&preg_id="+getIntent().getStringExtra("preg_id")+ "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);



                        addViewRespuesta(1,jsonObject.getString("res_respuesta"));
                        //mostrarEncuestas(1,getIntent().getStringExtra("enc_id"),jsonObject.getString("preg_titulo"),jsonObject.getString("preg_id"));


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

    private boolean checkIfValidAndRead() {
        cricketersList.clear();
        boolean result = true;
        

        for(int i=0;i<layoutList.getChildCount();i++){

            View cricketerView = layoutList.getChildAt(i);

            EditText editTextName = (EditText)cricketerView.findViewById(R.id.edit_cricketer_name);

            Cricketer cricketer = new Cricketer();

            if(!editTextName.getText().toString().equals("")){
                cricketer.setCricketerName(editTextName.getText().toString());
            }else {
                result = false;
                break;
            }

            editarRespuesta("https://preguntappusach.000webhostapp.com/crear_respuesta.php",cricketer.cricketerName,i);

            cricketersList.add(cricketer); //aqui ocurre la magia

        }

        if(cricketersList.size()==0){
            result = false;
            Toast.makeText(this, "Agrega alguna opcion", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "No olvides llenar todas las opciones!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void addViewRespuesta(int cantidad, String res_respuesta) {
        for (int id=1; id <= cantidad; id++) {
            final View cricketerView = getLayoutInflater().inflate(R.layout.row_add_cricketer, null, false);

            EditText editText = (EditText) cricketerView.findViewById(R.id.edit_cricketer_name);
            ImageView imageClose = (ImageView) cricketerView.findViewById(R.id.image_remove);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teamList);

            editText.setText(res_respuesta);

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(cricketerView);
                }
            });
            layoutList.addView(cricketerView);
        }
    }

    private void removeView(View view){

        layoutList.removeView(view);

    }

    private void crearPreguntaEnBlanco(final String rutaWebServices){

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
                parametros.put("preg_id",getIntent().getStringExtra("preg_id"));
                parametros.put("enc_id",getIntent().getStringExtra("enc_id"));
                parametros.put("tipoPregunta","1");
                parametros.put("tituloPregunta","creando pregunta");
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void modificarPregunta(final String rutaWebServices){

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
                parametros.put("idPregunta",getIntent().getStringExtra("preg_id"));
                parametros.put("idEncuesta",getIntent().getStringExtra("enc_id"));
                parametros.put("tipoPregunta","1");
                parametros.put("tituloPregunta",etPeguntaEnCuestion.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void editarRespuesta(final String rutaWebServices, final String contenidoRespuesta, final int i){


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
                parametros.put("preg_id",getIntent().getStringExtra("preg_id"));
                parametros.put("enc_id",getIntent().getStringExtra("enc_id"));
                parametros.put("contenidoRespuesta",contenidoRespuesta);
                parametros.put("res_orden",Integer.toString(i));
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void irACrearPregunta(){
        int preg_id= parseInt(getIntent().getStringExtra("preg_id"));

        preg_id++;



        Intent intent = new Intent(EditarPregunta.this, EditarPregunta.class);


        intent.putExtra("enc_id",getIntent().getStringExtra("enc_id"));
        intent.putExtra("preg_id",Integer.toString(preg_id));
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("enc_cantidadpreguntas",getIntent().getStringExtra("enc_cantidadpreguntas"));
        intent.putExtra("enc_fechatermino",getIntent().getStringExtra("enc_fechatermino"));
        intent.putExtra("enc_fechacreacion",getIntent().getStringExtra("enc_fechacreacion"));
        intent.putExtra("enc_titulo",getIntent().getStringExtra("enc_titulo"));
        intent.putExtra("esCuestionarioNuevo",false);
        startActivity(intent);

    }

    private void irAEditarCuestionario(){
        Intent intent = new Intent(EditarPregunta.this, EditarCuestionario.class);

        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("enc_titulo",getIntent().getStringExtra("enc_titulo"));
        intent.putExtra("enc_id",getIntent().getStringExtra("enc_id"));
        intent.putExtra("enc_fechacreacion",getIntent().getStringExtra("enc_fechacreacion"));
        intent.putExtra("enc_fechatermino",getIntent().getStringExtra("enc_fechatermino"));
        intent.putExtra("enc_cantidadpreguntas",getIntent().getStringExtra("enc_cantidadpreguntas"));


        intent.putExtra("preg_id",getIntent().getStringExtra("preg_id"));
        intent.putExtra("esCuestionarioNuevo",false);


        startActivity(intent);

    }

}