package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

import java.io.*;
import java.util.*;

public class Main {

    private static final String FILE_PATH = "C:\\Users\\marac\\IdeaProjects\\paoj1\\paoj-2026\\src\\com\\pao\\laboratory08\\exercise1\\tests\\studenti.txt";

    public static void main(String[] args) throws Exception {

        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();

                Adresa adresa = new Adresa(oras, strada);
                Student s = new Student(nume, varsta, adresa);

                studenti.add(s);
            }
        }

        Scanner sc = new Scanner(System.in);
        int prag = sc.nextInt();

        List<Student> filtrati = new ArrayList<>();

        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }
        System.out.println("WORKING DIR: " + System.getProperty("user.dir"));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rezultate.txt"))) {

            System.out.println("Filtru: varsta >= " + prag);
            System.out.println("Rezultate: " + filtrati.size() + " studenti\n");

            for (Student s : filtrati) {
                System.out.println(s);
                bw.write(s.toString());
                bw.newLine();
            }

            System.out.println("\nScris in: rezultate.txt");
        }
    }
}