package com.example.androidapppreguntapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class CrearCuestionario extends AppCompatActivity {
    String a,b,c,d,e,f;
    String lastID = new String();
    RequestQueue requestQueue;
    funciones_varias xamp = new funciones_varias();
    private GridLayout mlayout;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    Button addItem, addFecha,buttonsubirEncuesta;
    EditText etCantidadDePreguntas, etFechaTermino,etTituloEncuesta;
    TextView tvID;
    Calendar calendar= Calendar.getInstance();
    static final int DATE_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuestionario);

        sMonthIni = calendar.get(Calendar.MONTH);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sYearIni= calendar.get(Calendar.YEAR);

        tvID =(TextView)findViewById(R.id.textViewIDEncuesta);
        etTituloEncuesta = (EditText)findViewById(R.id.editTextTitulo);
        etCantidadDePreguntas = (EditText)findViewById(R.id.editTextCantidadDePreguntas);
        mlayout = (GridLayout)findViewById(R.id.myLayout);
        buttonsubirEncuesta = (Button)findViewById(R.id.buttonSubirEncuesta);
        addItem = (Button)findViewById(R.id.buttonAgregarPregunta);
        addFecha= (Button)findViewById(R.id.buttonAgregarFecha);
        etFechaTermino = (EditText)findViewById(R.id.editTextDate);

        addFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(etCantidadDePreguntas.getText().toString());

                subirEncuesta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_encuesta.php");
                for (int id=1; id <= cantidad; id++){
                    mlayout.addView(descriptionTextView(getApplicationContext(),"Titulo pregunta No "+(cantidad-id+1)),0);
                    mlayout.addView(tituloPregunta(getApplicationContext()),1);
                    mlayout.addView(botonAgregarPreguntas(getApplicationContext(),"Agregar Opciones",(cantidad-id+1)),2);
                    //mlayout.addView(dnv.recievedQuamtityEditText(getApplicationContext()),4);
                    //mlayout.addView(dnv.priceOfItem(getApplicationContext(),"35"),5);

                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                buscarIdEncuestaCreada("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/conseguir_idencuesta.php?correo="+getIntent().getStringExtra("correo")+"&titulo_encuesta="+etTituloEncuesta.getText()+"&fecha_creacion="+c+"");

            }
        });

        buttonsubirEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirEncuesta("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/crear_encuesta.php");
                //Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), b, Toast.LENGTH_SHORT).show();




                //Toast.makeText(getApplicationContext(), c[0], Toast.LENGTH_LONG).show();


            }
        });
    }

    private void buscarIdEncuestaCreada(String rutaWebServices){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lastID = jsonObject.getString("enc_id");
                        tvID.setText(lastID);

                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
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

    private void subirEncuesta(final String rutaWebServices){

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
                parametros.put("Correo",getIntent().getStringExtra("correo"));
                b =etTituloEncuesta.getText().toString();
                parametros.put("TituloEncuesta",etTituloEncuesta.getText().toString());
                c =formatter.format(currentDate);
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
                irACrearPreguntas(context,Integer.toString(id));
            }
        });
        return boton;
    }

    private void irACrearPreguntas(Context context, String id){

        Intent intent = new Intent(context, CrearPreguntas.class); //Esto te manda a la otra ventana

        Toast.makeText(getApplicationContext(),"Llamada de auxilio"+tvID.getText(),Toast.LENGTH_SHORT).show();
        intent.putExtra("IdEncuesta",lastID);
        intent.putExtra("id",id);
        //startActivity(intent);
        // finish();

    }

    private void colocar_fecha() {
        etFechaTermino.setText(mYearIni+ "-" +(mMonthIni + 1) + "-" + mDayIni +" ");
    }



    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha();

                }

            };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);


        }


        return null;
    }
















}