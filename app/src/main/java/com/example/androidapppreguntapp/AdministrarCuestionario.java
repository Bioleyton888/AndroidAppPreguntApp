package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdministrarCuestionario extends AppCompatActivity {
    Button botonIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_cuestionario);

        botonIngresar=(Button)findViewById(R.id.buttonIrACrearCuestionario);
        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrACrearCuestionario();
            }
        });
    }

    private void IrACrearCuestionario(){
        Intent intent = new Intent(this, CrearCuestionario.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();
    }
}


