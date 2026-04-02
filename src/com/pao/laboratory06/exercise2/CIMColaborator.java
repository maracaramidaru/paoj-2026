package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private double salariuBrutLunar;
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        salariuBrutLunar = in.nextDouble();
        if (in.hasNext()) {
            String s = in.next();
            bonus = s.equalsIgnoreCase("DA");
        } else {
            bonus = false;
        }
    }

    @Override
    public void afiseaza() {
        double venit = calculeazaVenitNetAnual();
        System.out.printf("CIM: %s %s, venit net anual: %.2f lei\n", nume, prenume, venit);
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venit = salariuBrutLunar * 12 * 0.55;
        if (bonus) venit *= 1.10;
        return venit;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }
}