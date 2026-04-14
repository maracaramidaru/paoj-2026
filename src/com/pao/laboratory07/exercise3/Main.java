package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tokens = sc.nextLine().split(" ");

            switch (tokens[0]) {
                case "STANDARD" -> {
                    comenzi.add(new ComandaStandard(
                            tokens[1],
                            Double.parseDouble(tokens[2]),
                            tokens[3]
                    ));
                }
                case "DISCOUNTED" -> {
                    comenzi.add(new ComandaRedusa(
                            tokens[1],
                            Double.parseDouble(tokens[2]),
                            Integer.parseInt(tokens[3]),
                            tokens[4]
                    ));
                }
                case "GIFT" -> {
                    comenzi.add(new ComandaGratuita(
                            tokens[1],
                            tokens[2]
                    ));
                }
            }
        }

        comenzi.forEach(c -> System.out.println(c.descriere()));
        System.out.println();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("QUIT")) break;

            if (line.equals("STATS")) {
                System.out.println("--- STATS ---");

                Map<String, Double> medii = comenzi.stream()
                        .collect(Collectors.groupingBy(
                                c -> c.getClass().getSimpleName(),
                                Collectors.averagingDouble(Comanda::pretFinal)
                        ));

                medii.forEach((k, v) ->
                        System.out.printf("%s: medie = %.2f lei\n",
                                k.replace("Comanda", "").toUpperCase(), v));

                System.out.println();
            }

            else if (line.startsWith("FILTER")) {
                double threshold = Double.parseDouble(line.split(" ")[1]);

                System.out.println("--- FILTER ---");

                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.println(c.descriere()));

                System.out.println();
            }

            else if (line.equals("SORT")) {
                System.out.println("--- SORT ---");

                comenzi.stream()
                        .sorted(Comparator
                                .comparing(Comanda::getClient)
                                .thenComparing(Comanda::pretFinal))
                        .forEach(c -> System.out.println(c.descriere()));

                System.out.println();
            }

            else if (line.equals("SPECIAL")) {
                System.out.println("--- SPECIAL ---");

                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscount() > 15)
                        .forEach(c -> System.out.println(c.descriere()));

                System.out.println();
            }
        }
    }
}