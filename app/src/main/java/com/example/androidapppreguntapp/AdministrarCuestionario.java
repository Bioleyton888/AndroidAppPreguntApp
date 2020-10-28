package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdministrarCuestionario extends AppCompatActivity {
    Button botonIngresar,botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_cuestionario);

        botonVolver=(Button)findViewById(R.id.botonVolver);
        botonIngresar=(Button)findViewById(R.id.buttonIrACrearCuestionario);

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrACrearCuestionario();
            }
        });
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrAMenuPrincipalAdministrador();
            }
        });
    }

    private void IrACrearCuestionario(){
        Intent intent = new Intent(this, CrearCuestionario.class); //Esto te manda a la otra ventana
        intent.putExtra("esCuestionarioNuevo",true);
        intent.putExtra("cantidadDePreguntas","0");
        intent.putExtra("fecha","000-00-00");
        intent.putExtra("fechaCreacion","000-00-00 00:00:00");
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();
    }
    private void IrAMenuPrincipalAdministrador(){
        Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana

        startActivity(intent);
        finish();
    }
}


