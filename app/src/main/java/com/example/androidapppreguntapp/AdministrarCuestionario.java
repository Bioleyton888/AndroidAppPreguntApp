 package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdministrarCuestionario extends AppCompatActivity {
    Button botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_cuestionario);

        botonVolver=(Button)findViewById(R.id.botonVolver);



        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrAMenuPrincipalAdministrador();
            }
        });
    }



    private void IrAMenuPrincipalAdministrador(){
        Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();
    }
}


