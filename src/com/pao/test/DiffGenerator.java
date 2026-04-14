package com.pao.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class IncorrectExtensionException extends Exception {
    public IncorrectExtensionException(String expectedExtension, String filename) {
        super("Filename must end with '" + expectedExtension + "' but got: " + filename);
    }
}

public class DiffGenerator {

    public static void saveDiff(String expected, String actual, String filename) throws IncorrectExtensionException {
        if (!filename.endsWith(".diff")) {
            throw new IncorrectExtensionException(".diff", filename);
        }

        List<String> expectedLines = expected.lines().toList();
        List<String> actualLines = actual.lines().toList();

        StringBuilder diffContent = new StringBuilder();

        int maxLines = Math.max(expectedLines.size(), actualLines.size());
        for (int i = 0; i < maxLines; i++) {
            String expLine = i < expectedLines.size() ? expectedLines.get(i) : "<missing>";
            String actLine = i < actualLines.size() ? actualLines.get(i) : "<missing>";

            if (!expLine.equals(actLine)) {
                diffContent.append("Line ").append(i + 1).append(":\n");
                diffContent.append("Expected: ").append(expLine).append("\n");
                diffContent.append("Actual  : ").append(actLine).append("\n\n");
            }
        }

        if (diffContent.isEmpty()) {
            diffContent.append("No differences found.\n");
        }

        try {
            Path outputPath = Path.of(filename);
            Files.writeString(outputPath, diffContent.toString());
            System.out.println("Diff file generated successfully at: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write diff file: " + e.getMessage());
        }
    }
}