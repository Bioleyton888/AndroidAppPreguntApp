package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipalUsuario extends AppCompatActivity {
    TextView tvBienvenida;
    Button botonPerfil,botonCerrarSesion,BotonEncuestas;
    String nombre, apellido,correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal_usuario);

        tvBienvenida =(TextView)findViewById(R.id.textoBienvenida);
        botonPerfil = (Button)findViewById(R.id.buttonIrAPerfil);
        botonCerrarSesion= (Button)findViewById(R.id.botonVolver);
        BotonEncuestas=(Button)findViewById(R.id.botonirAEncuestasPendienes);

        nombre = getIntent().getStringExtra("Nombre").toLowerCase();
        apellido = getIntent().getStringExtra("Apellido").toLowerCase();
        correo = getIntent().getStringExtra("correo");

        //esto es para hacer la primera letra mayuscula
        tvBienvenida.setText("¡Hola "+nombre.substring(0,1).toUpperCase()+nombre.substring(1) +" "+apellido.substring(0,1).toUpperCase()+apellido.substring(1)+"!");

        BotonEncuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAErncuestasPendientes(correo);

            }
        });


        botonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAPerfilDeUsuario(correo);

            }
        });

        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAlLogin();

            }
        });



    }

    private void irAPerfilDeUsuario(String correo){
        Intent intent = new Intent(this, PerfilDeUsuario.class); //Esto te manda a la otra ventana
        intent.putExtra("Nombre",nombre);
        intent.putExtra("Apellido",apellido);
        intent.putExtra("correo",correo);
        startActivity(intent);
        finish();
    }

    private void irAErncuestasPendientes(String correo){
        Intent intent = new Intent(this, EncuestasPendientes.class); //Esto te manda a la otra ventana
        intent.putExtra("correo",correo);
        startActivity(intent);
        finish();
    }

    private void irAlLogin(){
        Intent intent = new Intent(this, Login.class); //Esto te manda a la otra ventana
        startActivity(intent);
        finish();
    }

}