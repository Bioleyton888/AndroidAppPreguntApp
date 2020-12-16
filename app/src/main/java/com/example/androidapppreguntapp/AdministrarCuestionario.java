 package com.example.androidapppreguntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

 public class AdministrarCuestionario extends AppCompatActivity {
    Button botonVolver,botonBuscarEncuesta,botonBajarEncuesta,botonEditarEncuestas;
    TextView EncuestaSeleccionada;
     FuncionesVarias xamp = new FuncionesVarias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_cuestionario);

        botonVolver=(Button)findViewById(R.id.botonVolver);
        botonBuscarEncuesta=(Button)findViewById(R.id.botonirABuscarCuestionario);
        botonBajarEncuesta=(Button)findViewById(R.id.buttonTerminarCuestionario);
        botonEditarEncuestas=(Button)findViewById(R.id.buttonIrAModificarCuestionario);
        EncuestaSeleccionada=(TextView)findViewById(R.id.textViewNombreEncuestaSeleccionada);

        EncuestaSeleccionada.setText(getIntent().getStringExtra("enc_titulo"));

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrAMenuPrincipalAdministrador();
            }
        });

        botonBuscarEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrABuscarEncuesta();
            }
        });

        botonBajarEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EncuestaNoSeleccionada(getIntent().getStringExtra("enc_titulo"))){
                    IrABajarEncuesta();
                }
            }
        });

        botonEditarEncuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EncuestaNoSeleccionada(getIntent().getStringExtra("enc_titulo"))){
                    IrAEditarCuestionario(); 
                }
            }
        });
    }

     private void IrABajarEncuesta() {
         Intent intent = new Intent(this, TerminarEncuesta.class); //Esto te manda a la otra ventana
         intent.putExtra("correo",getIntent().getStringExtra("correo"));
         intent.putExtra("enc_titulo",getIntent().getStringExtra("enc_titulo"));
         intent.putExtra("enc_id",getIntent().getStringExtra("enc_id"));
         intent.putExtra("enc_fechacreacion",getIntent().getStringExtra("enc_fechacreacion"));
         intent.putExtra("cantidadPreguntas",getIntent().getStringExtra("cantidadPreguntas"));
         startActivity(intent);
         finish();
     }

     private void IrABuscarEncuesta() {
         Intent intent = new Intent(this, BuscarEncuestas.class); //Esto te manda a la otra ventana
         intent.putExtra("correo",getIntent().getStringExtra("correo"));
         intent.putExtra("URL","http://" + xamp.ipv4() + ":" + xamp.port() + "/webservicesPreguntAPP/buscar_encuestas_para_modificar.php" );
         startActivity(intent);
         finish();
     }

     private boolean EncuestaNoSeleccionada(String encuesta){
        if ((encuesta.equals("Seleccione una Encuesta"))){
            Toast.makeText(this,"Seleccione una Encuesta primero",Toast.LENGTH_SHORT).show();
            return true;
        }else {return false;}
    }

     private void IrAMenuPrincipalAdministrador(){
         Intent intent = new Intent(this, MenuPrincipalAdministrador.class); //Esto te manda a la otra ventana
         intent.putExtra("correo",getIntent().getStringExtra("correo"));
         startActivity(intent);
         finish();
     }

     private void IrAEditarCuestionario() {
         Intent intent = new Intent(this, EditarCuestionario.class); //Esto te manda a la otra ventana
         intent.putExtra("correo",getIntent().getStringExtra("correo"));
         intent.putExtra("enc_titulo",getIntent().getStringExtra("enc_titulo"));
         intent.putExtra("enc_id",getIntent().getStringExtra("enc_id"));
         intent.putExtra("enc_fechacreacion",getIntent().getStringExtra("enc_fechacreacion"));
         intent.putExtra("enc_fechatermino",getIntent().getStringExtra("enc_fechatermino"));
         intent.putExtra("enc_cantidadpreguntas",getIntent().getStringExtra("cantidadPreguntas"));
         startActivity(intent);
         finish();
     }

}


