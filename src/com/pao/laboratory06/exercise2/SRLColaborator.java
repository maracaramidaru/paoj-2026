package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {
    private String denumire;
    private double venitLunar;
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        denumire = in.next();
        prenume = in.next();
        venitLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        double venit = calculeazaVenitNetAnual();
        System.out.printf("SRL: %s %s, venit net anual: %.2f lei\n", denumire, prenume, venit);
    }

    @Override
    public String tipContract() {
        return "SRL";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.SRL;
    }
}