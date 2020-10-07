package com.example.androidapppreguntapp;

public class encuesta {

    public static String correoCreador;
    public static String tituloEncuesta;
    public static String fechaCreacion;
    public static String disponibilidadEncuesta;
    public static String cantidadPreguntas;
    public static String fechaTermino;

    encuesta(){}

    encuesta(String newCorreoCreador,String newTituloEncuesta,String newFechaCreacion,String newDisponibilidadEncuesta,String newCantidadPreguntas,String newFechaTermino){
        correoCreador = newCorreoCreador;
        tituloEncuesta = newTituloEncuesta;
        fechaCreacion= newFechaCreacion;
        disponibilidadEncuesta=newDisponibilidadEncuesta;
        cantidadPreguntas=newCantidadPreguntas;
        fechaTermino=newFechaTermino;

    }


}
