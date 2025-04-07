package com.br.buggerFixer.utils;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class MethodFinder {

    public static class FoundMethod {
        public String filePath;
        public String methodCode;

        public FoundMethod(String filePath, String methodCode) {
            this.filePath = filePath;
            this.methodCode = methodCode;
        }
    }

    public static FoundMethod findMethodInProject(File projectDir, String fullMethodName) throws IOException {
        String[] parts = fullMethodName.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("O nome do método deve estar no formato Classe.metodo");
        }

        String className = parts[0];
        String methodName = parts[1];

        Pattern methodPattern = Pattern.compile(
                "(public|protected|private|static|final|\\s)+[\\w<>\\[\\]]+\\s+" + methodName +
                        "\\s*\\([^)]*\\)\\s*(throws\\s+[\\w.,\\s]+)?\\s*\\{",
                Pattern.MULTILINE
        );

        try {
            Files.walk(projectDir.toPath())
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(file -> {
                        try {
                            String content = Files.readString(file);

                            if (!content.contains("class " + className)) {
                                return;
                            }

                            Matcher matcher = methodPattern.matcher(content);

                            if (matcher.find()) {
                                int start = matcher.start();

                                int openBraceIndex = content.indexOf("{", matcher.end() - 1);
                                if (openBraceIndex == -1) {
                                    throw new RuntimeException("Não foi possível encontrar a abertura do método.");
                                }

                                int braceCount = 1;
                                int i = openBraceIndex + 1;

                                for (; i < content.length(); i++) {
                                    char c = content.charAt(i);
                                    if (c == '{') braceCount++;
                                    else if (c == '}') braceCount--;

                                    if (braceCount == 0) break;
                                }

                                String methodCode = content.substring(start, i + 1);
                                throw new FoundMethodException(new FoundMethod(file.toString(), methodCode));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (FoundMethodException e) {
            return e.found;
        }

        return null;
    }


    private static class FoundMethodException extends RuntimeException {
        public final FoundMethod found;
        public FoundMethodException(FoundMethod found) {
            this.found = found;
        }
    }

    static {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            if (e instanceof FoundMethodException ex) {
                throw ex;
            }
        });
    }
}
