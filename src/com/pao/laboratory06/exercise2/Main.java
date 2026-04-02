package com.pao.laboratory06.exercise2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt(in.nextLine()); // nr colab
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String linie = in.nextLine();
            Scanner linieScanner = new Scanner(linie);
            String tip = linieScanner.next();
            Colaborator c;
            switch(tip) {
                case "CIM":
                    c = new CIMColaborator();
                    break;
                case "PFA":
                    c = new PFAColaborator();
                    break;
                case "SRL":
                    c = new SRLColaborator();
                    break;
                default:
                    throw new IllegalArgumentException("Tip necunoscut: " + tip);
            }
            c.citeste(linieScanner);
            colaboratori.add(c);
        }

        colaboratori.sort((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()));

        // colab de tip
        for (TipColaborator tip : TipColaborator.values()) {
            for (Colaborator c : colaboratori) {
                if (c.getTip() == tip) {
                    c.afiseaza();
                }
            }
        }

        Colaborator max = colaboratori.get(0);
        System.out.println("\nColaborator cu venit net maxim: ");
        max.afiseaza();

        System.out.println("\nColaboratori persoane juridice:");
        for (Colaborator c : colaboratori) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }

        System.out.println("\nSume și număr colaboratori pe tip:");
        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
        for (TipColaborator t : TipColaborator.values()) {
            suma.put(t, 0.0);
            numar.put(t, 0);
        }
        for (Colaborator c : colaboratori) {
            TipColaborator t = c.getTip();
            suma.put(t, suma.get(t) + c.calculeazaVenitNetAnual());
            numar.put(t, numar.get(t) + 1);
        }
        for (TipColaborator t : TipColaborator.values()) {
            System.out.printf("%s: suma = %.2f lei, numr = %d\n", t, suma.get(t), numar.get(t));
        }
    }
}