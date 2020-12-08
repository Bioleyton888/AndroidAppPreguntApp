package com.example.androidapppreguntapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import java.util.Calendar;

public class EditarCuestionarios extends AppCompatActivity {
    String a,b,c,d,e,f;
    String lastID = new String();
    RequestQueue requestQueue;
    FuncionesVarias xamp = new FuncionesVarias();
    private GridLayout mlayout;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    Button addItem, addFecha,buttonsubirEncuesta;
    EditText etCantidadDePreguntas, etFechaTermino,etTituloEncuesta;
    TextView tvID;
    Calendar calendar= Calendar.getInstance();
    static final int DATE_ID = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cuestionario);

        sMonthIni = calendar.get(Calendar.MONTH);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sYearIni= calendar.get(Calendar.YEAR);

        tvID =(TextView)findViewById(R.id.editar_cuestionario_textViewIDEncuesta);
        mlayout = (GridLayout)findViewById(R.id.editar_cuestionario_myLayout);
        etTituloEncuesta = (EditText)findViewById(R.id.editar_cuestionario_editTextTitulo);
        etCantidadDePreguntas = (EditText)findViewById(R.id.editar_cuestionario_editTextCantidadDePreguntas);
        etFechaTermino = (EditText)findViewById(R.id.editar_cuestionario_editTextDate);
        buttonsubirEncuesta = (Button)findViewById(R.id.editar_cuestionario_buttonSubirEncuesta);
        addItem = (Button)findViewById(R.id.editar_cuestionario_buttonAgregarPregunta);
        addFecha= (Button)findViewById(R.id.editar_cuestionario_buttonAgregarFecha);

        etTituloEncuesta.setText(getIntent().getStringExtra("enc_titulo"));
        etCantidadDePreguntas.setText(getIntent().getStringExtra("enc_cantidadpreguntas"));
        etFechaTermino.setText(getIntent().getStringExtra("enc_fechatermino"));
        tvID.setText(getIntent().getStringExtra("enc_id"));


    }
}
