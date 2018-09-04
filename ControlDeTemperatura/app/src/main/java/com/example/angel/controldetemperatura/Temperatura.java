package com.example.angel.controldetemperatura;

public class Temperatura {
    private long id;
    private int noInvernadero;
    private double temp;
    private String hora;
    private String dia;

    public Temperatura(){
        super();
    }

    public Temperatura(long id, int noInvernadero,double temp,String hora,String dia){
        this.id= id;
        this.noInvernadero = noInvernadero;
        this.temp = temp;
        this.hora = hora;
        this.dia = dia;
    }


    public long getId() {
        return id;
    }

    public int getNoInvernadero() {
        return noInvernadero;
    }

    public double getTemp() {
        return temp;
    }

    public String getHora() {
        return hora;
    }

    public String getDia() {
        return dia;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNoInvernadero(int noInvernadero) {
        this.noInvernadero = noInvernadero;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
