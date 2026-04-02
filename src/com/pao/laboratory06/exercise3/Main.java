package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ion", "0712345678", 8000, 1000),
                new Inginer("Ionescu", "Vlad", "0723456789", 7000, 500),
                new Inginer("Georgescu", "Maria", "0734567890", 9000, 1200)
        };

        System.out.println("Sortare natural (alfabetic nume):");
        Arrays.sort(ingineri);
        for (Inginer i : ingineri) System.out.println(i);

        System.out.println("\nSortare dupa salariu (descrescator):");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer i : ingineri) System.out.println(i);

        PlataOnline p = ingineri[0];
        p.autentificare("user1", "parola1");
        System.out.println("Sold consultat prin PlataOnline: " + p.consultareSold());
        PersoanaJuridica srl = new PersoanaJuridica("SRLTech", "SRL", "0741234567", 5000);
        PlataOnlineSMS smsRef = srl;
        smsRef.trimiteSMS("Factura emisa");
        smsRef.trimiteSMS("");
        System.out.println("\nSMS-uri trimise: " + srl.getSmsTrimise());

        PersoanaJuridica srlFaraTel = new PersoanaJuridica("MicroSRL", "SRL", null, 3000);
        System.out.println("Trimitere SMS fara telefon: " + srlFaraTel.trimiteSMS("Salut"));

        System.out.println("\nTVA: " + ConstanteFinanciare.TVA.getValoare());
    }
}