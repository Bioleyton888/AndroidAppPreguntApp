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
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EncuestasPendientes extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layoutList;
    FuncionesVarias xamp = new FuncionesVarias();
    RequestQueue requestQueue;
    Button buttonVolver, comenzarBusqueda;
    private GridLayout mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ArrayList<String> listaDeEncuestas;
        listaDeEncuestas = new ArrayList<String>();

        final ArrayList<String> listaDeDatosUsuario;
        listaDeDatosUsuario = new ArrayList<String>();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas_pendientes);

        comenzarBusqueda = (Button) findViewById(R.id.encuestasPendientesBotonComenzar);
        buttonVolver = (Button) findViewById(R.id.encuestasPendientesBotonVolver);
        mlayout = (GridLayout) findViewById(R.id.layoutEncuestasPendientes);
        layoutList = findViewById(R.id.LinearLayoutEncuestasPendientes);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_encuestas_respondidas.php?per_correo=" + getIntent().getStringExtra("correo") + "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeEncuestas.add(jsonObject.getString("enc_id"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_datos_usuario.php?per_correo=" + getIntent().getStringExtra("correo") + "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject2 = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject2 = response.getJSONObject(i);
                        listaDeDatosUsuario.add(jsonObject2.getString("sex_id"));
                        listaDeDatosUsuario.add(jsonObject2.getString("fac_id"));
                        listaDeDatosUsuario.add(jsonObject2.getString("car_id"));
                        listaDeDatosUsuario.add(jsonObject2.getString("com_id"));
                        listaDeDatosUsuario.add(jsonObject2.getString("eciv_id"));
                        listaDeDatosUsuario.add(jsonObject2.getString("gen_id"));

                        listaDeDatosUsuario.add(jsonObject2.getString("usu_anoingreso"));
                        listaDeDatosUsuario.add(jsonObject2.getString("usu_semestre"));
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
        requestQueue.add(jsonArrayRequest2);


/////////////////////////////////////////////////////////////////////// esta seria la funcion buscarEncuestasRespondidas() pero por alguna razon no funciona si es funcion


        comenzarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutList.removeAllViewsInLayout();
                buscarEncuestasPendientes("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_encuestas_Pendientes.php", listaDeEncuestas, listaDeDatosUsuario);
            }
        });


        //buscarEncuestasPendientes("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_encuestas_Pendientes.php",listaDeEncuestas);


        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAMenuPrincipalUsuario(getIntent().getStringExtra("correo"));
            }
        });

    }

    private void buscarEncuestasRespondidas(String rutaWebServices) {
        final ArrayList<String> listaDeEncuestas = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        listaDeEncuestas.add(jsonObject.getString("enc_id"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

    private void buscarEncuestasPendientes(String rutaWebServices, final ArrayList<String> listaDeEncuestas, final ArrayList<String> listaDeDatosUsuario) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);


                        if (!seRespondioEstaEncuesta(jsonObject.getString("enc_id"), listaDeEncuestas)) {


                            ObtenerIdfiltro("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_filtro_id.php?enc_id=" + jsonObject.getString("enc_id") + "", jsonObject.getString("enc_id"), jsonObject.getString("enc_titulo"), jsonObject.getString("enc_cantidadpreguntas"), listaDeDatosUsuario);

                            //filtros("http://"+ xamp.ipv4()+":"+ xamp.port()+"/webservicesPreguntAPP/buscar_filtros_encuesta.php?enc_id="+jsonObject.getString("enc_id")+"",jsonObject.getString("enc_id"),jsonObject.getString("enc_titulo"), jsonObject.getString("enc_cantidadpreguntas"));
                            //mostrarEncuestas(1, jsonObject.getString("enc_id"), jsonObject.getString("enc_titulo"), jsonObject.getString("enc_cantidadpreguntas"));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private boolean seRespondioEstaEncuesta(String enc_id, ArrayList<String> listaDeEncuestas) {

        for (int i = 0; i < listaDeEncuestas.size(); i++) {


            if (listaDeEncuestas.get(i).equals(enc_id)) {
                return true;
            }
        }
        return false;
    }

    private void mostrarEncuestas(int cantidad, final String enc_id, String enc_titulo, final String enc_cantidadpreguntas) {
        for (int id = 1; id <= cantidad; id++) {
            final View preguntaPendiente = getLayoutInflater().inflate(R.layout.row_preguntas_pendientes, null, false);

            TextView tituloEncuesta = (TextView) preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewTituloEncuesta);
            TextView cantidadPreguntas = (TextView) preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_textViewNumeroPreguntas);
            Button responderPreguntas = (Button) preguntaPendiente.findViewById(R.id.rowPreguntasPendientes_buttonResponder);

            tituloEncuesta.setText(enc_titulo);
            cantidadPreguntas.setText(enc_cantidadpreguntas);
            responderPreguntas.setText("Pendiente");

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            responderPreguntas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irAResponderEncuestas(enc_id, enc_cantidadpreguntas);
                }
            });
            layoutList.addView(preguntaPendiente);
        }
    }

    private void irAResponderEncuestas(String idEncuestasPendientes, String enc_cantidadpreguntas) {
        Intent intent = new Intent(this, ResponderEncuestas.class);
        intent.putExtra("idEncuestaPendiente", idEncuestasPendientes);
        intent.putExtra("preguntaNumero", 1);
        intent.putExtra("cantidadPreguntas", Integer.parseInt(enc_cantidadpreguntas));
        intent.putExtra("correo", getIntent().getStringExtra("correo"));
        intent.putExtra("Nombre", getIntent().getStringExtra("Nombre"));
        intent.putExtra("Apellido", getIntent().getStringExtra("Apellido"));

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

    private void removeView(View view) {

        layoutList.removeView(view);

    }

    private void irAMenuPrincipalUsuario(String correo) {
        Intent intent = new Intent(this, MenuPrincipalUsuario.class); //Esto te manda a la otra ventana
        intent.putExtra("Nombre", getIntent().getStringExtra("Nombre"));
        intent.putExtra("Apellido", getIntent().getStringExtra("Apellido"));
        intent.putExtra("correo", correo);

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

    }

    private void ObtenerIdfiltro(String rutaWebServices, final String enc_id, final String enc_titulo, final String enc_cantidadpreguntas, final ArrayList<String> listaDeDatosUsuario) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);


                        filtros("http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_filtros_encuesta.php?filt_id=" + jsonObject.getString("filt_id") + "", enc_id, enc_titulo, enc_cantidadpreguntas, listaDeDatosUsuario);

                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
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

    private void filtros(String rutaWebServices, final String enc_id, final String enc_titulo, final String enc_cantidadpreguntas, final ArrayList<String> listaDeDatosUsuario) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(rutaWebServices, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        if ((jsonObject.getString("sex_id").equals(listaDeDatosUsuario.get(0))) || (jsonObject.getString("sex_id").equals("1"))) {

                            if ((jsonObject.getString("fac_id").equals(listaDeDatosUsuario.get(1))) || (jsonObject.getString("fac_id").equals("1"))) {

                                if ((jsonObject.getString("car_id").equals(listaDeDatosUsuario.get(2))) || (jsonObject.getString("car_id").equals("1"))) {

                                    if ((jsonObject.getString("com_id").equals(listaDeDatosUsuario.get(3))) || (jsonObject.getString("com_id").equals("1"))) {

                                        if ((jsonObject.getString("eciv_id").equals(listaDeDatosUsuario.get(4))) || (jsonObject.getString("eciv_id").equals("1"))) {

                                            if ((jsonObject.getString("gen_id").equals(listaDeDatosUsuario.get(5))) || (jsonObject.getString("gen_id").equals("1"))) {

                                                if ((jsonObject.getString("filt_anoingreso").equals(listaDeDatosUsuario.get(6))) || (Integer.parseInt(jsonObject.getString("filt_anoingreso"))<1980 )) {

                                                    if ((jsonObject.getString("filt_semestre").equals(listaDeDatosUsuario.get(7))) || (jsonObject.getString("filt_semestre").equals("1"))) {
                                                        mostrarEncuestas(1, enc_id, enc_titulo, enc_cantidadpreguntas);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        //System.out.println("--<"+jsonObject.getString("fac_id"));
                        //System.out.println("--<"+jsonObject.getString("car_id"));
                        // System.out.println("--<"+jsonObject.getString("com_id"));
                        //System.out.println("--<"+jsonObject.getString("eciv_id"));
                        //System.out.println("--<"+jsonObject.getString("gen_id"));
                        //System.out.println("--<"+jsonObject.getString("filt_anoingreso"));
                        //System.out.println("--<"+jsonObject.getString("filt_semestre"));
                        //mostrarEncuestas(1,enc_id , enc_titulo,enc_cantidadpreguntas);

                        //la funcion siguiente mete el nombre, el rut y el apellido osease siguiente(nombre,apellido,rut)
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

    private void perfilDeUsuario(String rutaWebServices) {


    }

}
