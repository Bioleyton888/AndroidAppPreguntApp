package com.example.androidapppreguntapp;

public class Encuesta {

    public static String correoCreador;
    public static String tituloEncuesta;
    public static String fechaCreacion;
    public static String disponibilidadEncuesta;
    public static String cantidadPreguntas;
    public static String fechaTermino;

    Encuesta(){}

    Encuesta(String newCorreoCreador, String newTituloEncuesta, String newFechaCreacion, String newDisponibilidadEncuesta, String newCantidadPreguntas, String newFechaTermino){
        correoCreador = newCorreoCreador;
        tituloEncuesta = newTituloEncuesta;
        fechaCreacion= newFechaCreacion;
        disponibilidadEncuesta=newDisponibilidadEncuesta;
        cantidadPreguntas=newCantidadPreguntas;
        fechaTermino=newFechaTermino;

    }


}
