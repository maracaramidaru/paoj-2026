package com.pao.test;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IOTest {

    public static void runFlat(String testsDir, MainMethod main) {
        File dir = new File(testsDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("EROARE: directorul de teste nu există: " + dir.getAbsolutePath());
            return;
        }

        System.out.println("\n=== Teste flat ===");
        runPartDir(dir, main, false);
    }

    @FunctionalInterface
    public interface MainMethod {
        void run(String[] args) throws Exception;
    }

    // ── Rulări generale ──────────────────────────────────────────────
    public static void runParts(String testsDir, MainMethod main) {
        runParts(testsDir, main, false);
    }

    public static void runParts(String testsDir, MainMethod main, boolean printFullOutput) {
        File dir = new File(testsDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("EROARE: directorul de teste nu există: " + dir.getAbsolutePath());
            return;
        }

        File[] partDirs = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isDirectory)
                .sorted(Comparator.comparing(File::getName))
                .toArray(File[]::new);

        for (File partDir : partDirs) {
            System.out.println("\n=== Partea: " + partDir.getName() + " ===");
            runPartDir(partDir, main, printFullOutput);
        }
    }

    // ── Internal helpers ────────────────────────────────────────────

    private static int[] runPartDir(File dir, MainMethod main, boolean printFullOutput) {
        File[] inFiles = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(f -> f.getName().endsWith(".in"))
                .sorted(Comparator.comparing(File::getName))
                .toArray(File[]::new);

        int passed = 0, failed = 0;
        for (File inFile : inFiles) {
            String base = inFile.getName().replace(".in", "");
            File outFile = new File(dir, base + ".out");

            String input, expected;
            try {
                input = Files.readString(inFile.toPath()).replace("\r\n", "\n");
                expected = Files.readString(outFile.toPath()).stripTrailing().replace("\r\n", "\n");
            } catch (IOException e) {
                System.out.println("  [ERROR] " + base + " — nu s-a putut citi: " + e.getMessage());
                failed++;
                continue;
            }

            String actual = capture(input, main);
            if (actual == null) {
                System.out.println("  [FAIL] " + base + " — excepție la rulare");
                failed++;
                continue;
            }
            actual = actual.stripTrailing().replace("\r\n", "\n");

            if (actual.equals(expected)) {
                System.out.println("  [PASS] " + base);
                passed++;
            } else {
                System.out.println("  [FAIL] " + base);
                if (printFullOutput) {
                    System.out.println("  Expected:\n" + expected);
                    System.out.println("  Actual:\n" + actual);
                }
                saveDiff(expected, actual, dir, base);
                failed++;
            }
        }

        System.out.printf("  Rezultat parte: %d/%d teste trecute.%n", passed, passed + failed);
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
            System.out.println("  Excepție: " + e);
            return null;
        } finally {
            System.setIn(savedIn);
            System.setOut(savedOut);
        }
    }

    // ── Dif generator simplificat fără difflib ───────────────────────
    private static void saveDiff(String expected, String actual, File partDir, String base) {
        List<String> expectedLines = Arrays.asList(expected.split("\n", -1));
        List<String> actualLines = Arrays.asList(actual.split("\n", -1));

        StringBuilder diff = new StringBuilder();
        int max = Math.max(expectedLines.size(), actualLines.size());

        for (int i = 0; i < max; i++) {
            String eLine = i < expectedLines.size() ? expectedLines.get(i) : "<missing>";
            String aLine = i < actualLines.size() ? actualLines.get(i) : "<missing>";
            if (!eLine.equals(aLine)) {
                diff.append("Line ").append(i + 1).append(":\n");
                diff.append("Expected: ").append(eLine).append("\n");
                diff.append("Actual  : ").append(aLine).append("\n\n");
            }
        }

        if (diff.isEmpty()) diff.append("No differences found.\n");

        try {
            File resultsDir = new File(partDir, "../../results").getAbsoluteFile();
            resultsDir.mkdirs();
            File diffFile = new File(resultsDir, partDir.getName() + "-" + base + ".diff");
            Files.writeString(diffFile.toPath(), diff.toString());
            System.out.println("  Diff salvat în: " + diffFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("  Eroare la scrierea diff: " + e.getMessage());
        }
    }
}