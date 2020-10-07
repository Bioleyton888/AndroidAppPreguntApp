package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipalAdministrador extends AppCompatActivity {
    TextView tvBienvenida;
    Button botonPerfil,botonCerrarSesion,BotonAdministrarEncuestas;
    String nombre, apellido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal_administrador);

        tvBienvenida =(TextView)findViewById(R.id.textoBienvenida);
        botonCerrarSesion = (Button)findViewById(R.id.botonVolver);
        BotonAdministrarEncuestas= (Button)findViewById(R.id.botonirAAdministrarEncuestas);

        nombre = getIntent().getStringExtra("Nombre").toLowerCase();
        apellido = getIntent().getStringExtra("Apellido").toLowerCase();

        //esto es para hacer la primera letra mayuscula
        tvBienvenida.setText("¡Hola "+nombre.substring(0,1).toUpperCase()+nombre.substring(1) +" "+apellido.substring(0,1).toUpperCase()+apellido.substring(1)+"!");


        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAlLogin();

            }
        });
        BotonAdministrarEncuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAAdministrarCuestionario();

            }
        });
    }


    private void irAlLogin(){
        Intent intent = new Intent(this, Login.class); //Esto te manda a la otra ventana
        startActivity(intent);
        finish();
    }

    private void irAAdministrarCuestionario(){
        Intent intent = new Intent(this, AdministrarCuestionario.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",getIntent().getStringExtra("correo"));
        startActivity(intent);
        finish();
    }
}



