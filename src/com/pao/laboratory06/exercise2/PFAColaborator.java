package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double venitLunar;
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM_BRUT_AN = 4050 * 12; // 48600

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        double venit = calculeazaVenitNetAnual();
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei\n", nume, prenume, venit);
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetAnual = (venitLunar - cheltuieliLunare) * 12;

        double impozit = 0.10 * venitNetAnual;

        // CASS
        double cass;
        if (venitNetAnual < 6 * SALARIU_MINIM_BRUT_AN) cass = 0.10 * 6 * SALARIU_MINIM_BRUT_AN;
        else if (venitNetAnual <= 72 * SALARIU_MINIM_BRUT_AN) cass = 0.10 * venitNetAnual;
        else cass = 0.10 * 72 * SALARIU_MINIM_BRUT_AN;

        // CAS
        double cas;
        if (venitNetAnual < 12 * SALARIU_MINIM_BRUT_AN) cas = 0;
        else if (venitNetAnual <= 24 * SALARIU_MINIM_BRUT_AN) cas = 0.25 * 12 * SALARIU_MINIM_BRUT_AN;
        else cas = 0.25 * 24 * SALARIU_MINIM_BRUT_AN;

        return venitNetAnual - impozit - cass - cas;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }
}