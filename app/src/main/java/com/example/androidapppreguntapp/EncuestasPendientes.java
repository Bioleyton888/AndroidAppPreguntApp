package com.example.androidapppreguntapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    String correo;
    Button buttonVolver;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas_pendientes);

        buttonVolver = findViewById(R.id.encuestasPendientesBotonVolver);

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
                        Toast.makeText(getApplicationContext(), jsonObject.getString("enc_id")+ " " + jsonObject.getString("enc_titulo") , Toast.LENGTH_SHORT).show();
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

    private void removeView(View view){

        layoutList.removeView(view);

    }

    @Override
    public void onClick(View view)   {
        switch (view.getId()){

            case R.id.encuestasPendientesBotonVolver:

                break;

            case R.id.button_submit_list:

                break;

        }


    }

    private void addView(int cantidad) {
        //addView(Integer.parseInt(etCantidadDeOpciones.getText().toString()));
        for (int id=1; id <= cantidad; id++) {
            final View cricketerView = getLayoutInflater().inflate(R.layout.row_add_cricketer, null, false);

            EditText editText = (EditText) cricketerView.findViewById(R.id.edit_cricketer_name);
            ImageView imageClose = (ImageView) cricketerView.findViewById(R.id.image_remove);

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(cricketerView);
                }
            });

            layoutList.addView(cricketerView);
        }
    }

}
