package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class CrearPreguntas extends AppCompatActivity implements View.OnClickListener {
    String idEncuesta, lastIDPregunta;
    FuncionesVarias xamp = new FuncionesVarias();
    LinearLayout layoutList;
    TextView tvID;
    Button buttonAdd,buttonCancelar;
    private Spinner spinnerEscala;
    Button buttonSubmitList;
    EditText etCantidadDeOpciones, etPeguntaEnCuestion;
    RequestQueue requestQueue;
    List<String> teamList = new ArrayList<>();
    ArrayList<Cricketer> cricketersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_preguntas);

        spinnerEscala = (Spinner) findViewById(R.id.spinnerTipoDeEscala);
        idEncuesta = getIntent().getStringExtra("idEncuesta");
        etPeguntaEnCuestion=findViewById(R.id.editTextTituloOpcion);
        layoutList = findViewById(R.id.contenedor);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        buttonCancelar = findViewById(R.id.cancelar);
        etCantidadDeOpciones = (EditText)findViewById(R.id.editTextCantidadDeOpciones);
        buttonAdd.setOnClickListener(this);
        buttonCancelar.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);

        crearPreguntaEnBlanco("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_pregunta.php");
        funcionMostrarEscala();


        spinnerEscala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Escala de likert")) {
                    layoutList.removeAllViews();
                    addView2(5);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//Integer.parseInt(CantidadDeOpciones.getText().toString())
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_add:
                layoutList.removeAllViews();
                addView(parseInt(etCantidadDeOpciones.getText().toString()));
                modificarPregunta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/editar_pregunta.php");
                break;

            case R.id.cancelar:
                irACrearCuestionario();

                break;

            case R.id.button_submit_list:
                if(checkIfValidAndRead()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list",cricketersList);
                    modificarPregunta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/editar_pregunta.php");
                    if (getIntent().getStringExtra("idPregunta").equals(getIntent().getStringExtra("cantidadDePreguntas"))){
                        irACrearCuestionario();

                    }else{
                        irACrearPregunta();

                    }

                }
                break;
        }
    }

    private void addView2(int cantidad) {



        for (int id=1; id <= cantidad; id++) {
            final View cricketerView = getLayoutInflater().inflate(R.layout.row_add_cricketer, null, false);

            EditText editText = (EditText) cricketerView.findViewById(R.id.edit_cricketer_name);
            ImageView imageClose = (ImageView) cricketerView.findViewById(R.id.image_remove);

            switch (id){
                case 1:
                    editText.setText("Totalmente en desacuerdo");
                    break;
                case 2:
                    editText.setText("En desacuerdo");
                    break;
                case 3:
                    editText.setText("Ni acuerdo, ni en desacuerdo");
                    break;
                case 4:
                    editText.setText("De acuerdo");
                    break;
                case 5:
                    editText.setText("Totalmente De acuerdo");
                    break;


            }


            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teamList);

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(cricketerView);
                }
            });
            layoutList.addView(cricketerView);
        }
    }

    private void funcionMostrarEscala() {
        String [] escalas ={"Opcion Multiple","Casillas","Escala de likert" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, escalas);
        spinnerEscala.setAdapter(adapter);



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

            crearRespuesta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_respuesta.php",cricketer.cricketerName,i);

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

    private void addView(int cantidad) {
        for (int id=1; id <= cantidad; id++) {
            final View cricketerView = getLayoutInflater().inflate(R.layout.row_add_cricketer, null, false);

            EditText editText = (EditText) cricketerView.findViewById(R.id.edit_cricketer_name);
            ImageView imageClose = (ImageView) cricketerView.findViewById(R.id.image_remove);

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teamList);

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
                parametros.put("idPregunta",getIntent().getStringExtra("idPregunta"));
                parametros.put("idEncuesta",getIntent().getStringExtra("idEncuesta"));
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
                String selection = spinnerEscala.getSelectedItem().toString();
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("idPregunta",getIntent().getStringExtra("idPregunta"));
                parametros.put("idEncuesta",getIntent().getStringExtra("idEncuesta"));

                if (selection.equals("Opcion Multiple")){
                    parametros.put("tipoPregunta","1");
                }else if (selection.equals("Casillas")){
                    parametros.put("tipoPregunta","2");
                }else if (selection.equals("Escala de likert")){
                    parametros.put("tipoPregunta","3");
                }
                parametros.put("tituloPregunta",etPeguntaEnCuestion.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void crearRespuesta(final String rutaWebServices, final String contenidoRespuesta, final int i){


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
                parametros.put("idPregunta",getIntent().getStringExtra("idPregunta"));
                parametros.put("idEncuesta",getIntent().getStringExtra("idEncuesta"));
                parametros.put("contenidoRespuesta",contenidoRespuesta);
                parametros.put("res_orden",Integer.toString(i));


                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }

    private void irACrearPregunta(){
        int idPregunta= parseInt(getIntent().getStringExtra("idPregunta"));

        idPregunta++;



        Intent intent = new Intent(CrearPreguntas.this,CrearPreguntas.class);


        intent.putExtra("idEncuesta",getIntent().getStringExtra("idEncuesta"));
        intent.putExtra("idPregunta",Integer.toString(idPregunta));
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("cantidadDePreguntas",getIntent().getStringExtra("cantidadDePreguntas"));
        intent.putExtra("fecha",getIntent().getStringExtra("fecha"));
        intent.putExtra("fechaCreacion",getIntent().getStringExtra("fechaCreacion"));
        intent.putExtra("tituloEncuesta",getIntent().getStringExtra("tituloEncuesta"));
        intent.putExtra("esCuestionarioNuevo",false);


        startActivity(intent);

    }

    private void irACrearCuestionario(){
        Intent intent = new Intent(CrearPreguntas.this,CrearCuestionario.class);


        intent.putExtra("idEncuesta",getIntent().getStringExtra("idEncuesta"));
        intent.putExtra("idPregunta",getIntent().getStringExtra("idPregunta"));
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        intent.putExtra("cantidadDePreguntas",getIntent().getStringExtra("cantidadDePreguntas"));
        intent.putExtra("fecha",getIntent().getStringExtra("fecha"));
        intent.putExtra("fechaCreacion",getIntent().getStringExtra("fechaCreacion"));
        intent.putExtra("tituloEncuesta",getIntent().getStringExtra("tituloEncuesta"));
        intent.putExtra("esCuestionarioNuevo",false);


        startActivity(intent);

    }

}