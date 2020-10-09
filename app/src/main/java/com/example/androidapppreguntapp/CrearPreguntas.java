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

import java.util.ArrayList;
import java.util.List;

public class CrearPreguntas extends AppCompatActivity implements View.OnClickListener {
    String idEncuesta;
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    EditText etCantidadDeOpciones, etPeguntaEnCuestion;

    List<String> teamList = new ArrayList<>();
    ArrayList<Cricketer> cricketersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_preguntas);

        idEncuesta = getIntent().getStringExtra("idEncuesta");
        layoutList = findViewById(R.id.contenedor);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        etCantidadDeOpciones = (EditText)findViewById(R.id.editTextCantidadDeOpciones);
        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
//Integer.parseInt(CantidadDeOpciones.getText().toString())
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_add:
                addView(Integer.parseInt(etCantidadDeOpciones.getText().toString()));

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


            System.out.println(cricketer.cricketerName);
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
}






