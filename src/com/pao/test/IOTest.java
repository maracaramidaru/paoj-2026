package com.pao.test;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utilitar pentru testarea automată a exercițiilor I/O,
 * fără dependențe externe.
 */
public class IOTest {

    @FunctionalInterface
    public interface MainMethod {
        void run(String[] args) throws Exception;
    }

    public static void runParts(String testsDir, MainMethod main) {
        runParts(testsDir, main, false);
    }

    public static void runParts(String testsDir, MainMethod main, boolean printFullOutput) {
        File dir = new File(testsDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("EROARE: directorul de teste nu există: " + dir.getAbsolutePath());
            return;
        }

        File[] partDirs = Arrays.stream(Optional.ofNullable(dir.listFiles()).orElse(new File[0]))
                .filter(File::isDirectory)
                .sorted(Comparator.comparing(File::getName))
                .toArray(File[]::new);

        List<String> partNames = new ArrayList<>();
        List<Integer> partPassed = new ArrayList<>();
        List<Integer> partTotal = new ArrayList<>();

        for (File partDir : partDirs) {
            String partName = partDir.getName();
            System.out.println("\n=== Partea: " + partName + " ===");
            int[] results = runPartDir(partDir, main, printFullOutput);
            partNames.add(partName);
            partPassed.add(results[0]);
            partTotal.add(results[1]);
        }

        System.out.println("\n=== SUMAR FINAL ===");
        int totalP = 0, totalT = 0;
        for (int i = 0; i < partNames.size(); i++) {
            int p = partPassed.get(i), t = partTotal.get(i);
            String status = (p == t && t > 0) ? "[OK]" : (p == 0 ? "[FAIL]" : "[PARTIAL]");
            System.out.printf("%s %s: %d/%d teste%n", status, partNames.get(i), p, t);
            totalP += p;
            totalT += t;
        }
        System.out.printf("Total: %d/%d teste trecute%n", totalP, totalT);
    }

    private static int[] runPartDir(File dir, MainMethod main, boolean printFullOutput) {
        File[] inFiles = Arrays.stream(Optional.ofNullable(dir.listFiles()).orElse(new File[0]))
                .filter(f -> f.getName().endsWith(".in"))
                .sorted(Comparator.comparing(File::getName))
                .toArray(File[]::new);

        int passed = 0, failed = 0;

        for (int idx = 0; idx < inFiles.length; idx++) {
            File inFile = inFiles[idx];
            File outFile = new File(dir, inFile.getName().replace(".in", ".out"));
            System.out.println("\n*** Test " + (idx + 1) + ": " + inFile.getName() + " ***");

            if (!outFile.exists()) {
                System.out.println("SKIP — lipseste " + outFile.getName());
                continue;
            }

            String input, expected;
            try {
                input = Files.readString(inFile.toPath()).replace("\r\n", "\n");
                expected = Files.readString(outFile.toPath()).stripTrailing().replace("\r\n", "\n");
            } catch (IOException e) {
                System.out.println("[ERROR] Nu s-a putut citi: " + e.getMessage());
                failed++;
                continue;
            }

            String actual = capture(input, main);
            if (actual == null) {
                System.out.println("[FAIL] — excepție la rularea Main.main()");
                failed++;
                continue;
            }
            actual = actual.stripTrailing().replace("\r\n", "\n");

            if (actual.equals(expected)) {
                System.out.println("[PASS]");
                passed++;
            } else {
                System.out.println("[FAIL]");
                if (printFullOutput) {
                    System.out.println("Expected:\n" + expected);
                    System.out.println("Actual:\n" + actual);
                }
                // Salvează diferențe simple
                saveDiffSimplified(expected, actual, dir, inFile.getName().replace(".in",""));
                failed++;
            }
        }

        System.out.printf("Rezultat parte: %d/%d teste trecute.%n", passed, passed + failed);
        return new int[]{passed, passed + failed};
    }

    private static String capture(String input, MainMethod main) {
        InputStream savedIn = System.in;
        PrintStream savedOut = System.out;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            main.run(new String[0]);
            System.out.flush();
            return baos.toString();
        } catch (Exception e) {
            System.setIn(savedIn);
            System.setOut(savedOut);
            System.out.println("Excepție: " + e);
            return null;
        } finally {
            System.setIn(savedIn);
            System.setOut(savedOut);
        }
    }

    private static void saveDiffSimplified(String expected, String actual, File partDir, String base) {
        try {
            Path resultsDir = Path.of(partDir.getParentFile().getAbsolutePath(), "results");
            if (!Files.exists(resultsDir)) Files.createDirectories(resultsDir);

            List<String> diffLines = new ArrayList<>();
            String[] eLines = expected.split("\n", -1);
            String[] aLines = actual.split("\n", -1);
            int max = Math.max(eLines.length, aLines.length);
            for (int i = 0; i < max; i++) {
                String e = i < eLines.length ? eLines[i] : "";
                String a = i < aLines.length ? aLines[i] : "";
                if (!e.equals(a)) {
                    diffLines.add("Line " + (i+1));
                    diffLines.add("Expected: " + e);
                    diffLines.add("Actual:   " + a);
                }
            }
            Files.write(resultsDir.resolve(base + ".diff"), diffLines);
        } catch (IOException ex) {
            System.out.println("[Eroare la scrierea diff-ului] " + ex.getMessage());
        }
    }
}