package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearPreguntas extends AppCompatActivity implements View.OnClickListener {
    String idEncuesta;
    funciones_varias xamp = new funciones_varias();
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    EditText etCantidadDeOpciones, etPeguntaEnCuestion;
    RequestQueue requestQueue;

    List<String> teamList = new ArrayList<>();
    ArrayList<Cricketer> cricketersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_preguntas);


        idEncuesta = getIntent().getStringExtra("idEncuesta");
        etPeguntaEnCuestion=findViewById(R.id.editTextTituloOpcion);
        layoutList = findViewById(R.id.contenedor);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        etCantidadDeOpciones = (EditText)findViewById(R.id.editTextCantidadDeOpciones);
        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
        subirPregunta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_pregunta.php");

//Integer.parseInt(CantidadDeOpciones.getText().toString())
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_add:
                addView(Integer.parseInt(etCantidadDeOpciones.getText().toString()));
                subirPregunta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_pregunta.php");
                break;

            case R.id.button_submit_list: 

                if(checkIfValidAndRead()){

                    Intent intent = new Intent(CrearPreguntas.this,ActivityCricketers.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list",cricketersList);



                            /*
                    intent.putExtras(bundle);
                    startActivity(intent);
*/
                }

                break;

        }


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

            System.out.println("---> "+cricketer.cricketerName);
            subirPregunta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_pregunta.php");
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

    private void subirPregunta(final String rutaWebServices){

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
                parametros.put("idEncuesta",getIntent().getStringExtra("idEncuesta"));
                parametros.put("tipoPregunta","1");
                parametros.put("tituloPregunta","creandopregunta");

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
                parametros.put("idEncuesta",getIntent().getStringExtra("idEncuesta"));
                parametros.put("tipoPregunta","1");
                parametros.put("tituloPregunta",etPeguntaEnCuestion.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);//procesar las peticiones hechas por la app para que la libreria se encague de ejecutarlas
        requestQueue.add(stringRequest);//enviar las solicitud enviando el string request
    }
}






