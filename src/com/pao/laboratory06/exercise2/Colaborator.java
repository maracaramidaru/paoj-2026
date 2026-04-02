package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;

    public abstract double calculeazaVenitNetAnual();
    public abstract TipColaborator getTip();
}