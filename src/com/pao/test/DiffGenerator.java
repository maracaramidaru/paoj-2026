package com.pao.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class IncorrectExtensionException extends Exception {
    public IncorrectExtensionException(String expectedExtension, String filename) {
        super("Filename must end with '" + expectedExtension + "' but got: " + filename);
    }
}

public class DiffGenerator {

    public static void saveDiff(String expected, String actual, String filename)
            throws IncorrectExtensionException {

        if (!filename.endsWith(".diff")) {
            throw new IncorrectExtensionException(".diff", filename);
        }

        StringBuilder result = new StringBuilder();

        String[] expectedLines = expected.split("\n");
        String[] actualLines = actual.split("\n");

        int max = Math.max(expectedLines.length, actualLines.length);

        for (int i = 0; i < max; i++) {
            String e = i < expectedLines.length ? expectedLines[i] : "";
            String a = i < actualLines.length ? actualLines[i] : "";

            if (!e.equals(a)) {
                result.append("Line ").append(i + 1).append(":\n");
                result.append("Expected: ").append(e).append("\n");
                result.append("Actual:   ").append(a).append("\n\n");
            }
        }

        try {
            Files.writeString(Path.of(filename), result.toString());
            System.out.println("Diff file generated (simplified) at: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing diff file: " + e.getMessage());
        }
    }
}