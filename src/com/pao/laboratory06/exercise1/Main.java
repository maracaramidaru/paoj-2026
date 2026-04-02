package com.pao.laboratory06.exercise1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String optiune = scanner.next();

        int numarAngajati = scanner.nextInt();
        Angajat[] angajati = new Angajat[numarAngajati];
        for (int i = 0; i < numarAngajati; i++) {
            angajati[i] = Angajat.citeste(scanner);
        }

        Comparator<Angajat> comparator;
        switch(optiune) {
            case "by_name":
                comparator = (a1, a2) -> a1.getNume().compareTo(a2.getNume());
                break;
            case "by_salary":
                comparator = (a1, a2) -> Double.compare(a1.getSalariu(), a2.getSalariu());
                break;
            case "by_salary_desc":
                comparator = (a1, a2) -> Double.compare(a2.getSalariu(), a1.getSalariu());
                break;
            default:
                comparator = (a1, a2) -> 0;
        }

        Arrays.sort(angajati, comparator);

        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }
}