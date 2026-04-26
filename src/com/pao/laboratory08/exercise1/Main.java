package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "C:\\Users\\marac\\IdeaProjects\\paoj1\\paoj-2026\\src\\com\\pao\\laboratory08\\tests\\studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();

        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine().trim();
        scanner.close();

        if (comanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }

        } else if (comanda.startsWith("SHALLOW")) {
            String nume = comanda.split(" ", 2)[1].trim();
            for (Student s : studenti) {
                if (s.getNume().equals(nume)) {
                    Student clona = s.shallowClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + s);
                    System.out.println("Clona: " + clona);
                    break;
                }
            }

        } else if (comanda.startsWith("DEEP")) {
            String nume = comanda.split(" ", 2)[1].trim();
            for (Student s : studenti) {
                if (s.getNume().equals(nume)) {
                    Student clona = s.deepClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + s);
                    System.out.println("Clona: " + clona);
                    break;
                }
            }
        }
    }

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while ((linie = br.readLine()) != null) {
            linie = linie.trim();
            if (linie.isEmpty()) continue;
            String[] parts = linie.split(",");
            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();
            studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
        }
        br.close();
        return studenti;
    }
}