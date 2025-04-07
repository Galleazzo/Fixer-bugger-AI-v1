package com.br.buggerFixer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MethodReplacer {

    public static void replaceMethodInFile(File file, String oldMethod, String newMethod) throws IOException {
        String content = Files.readString(file.toPath());

        if (!content.contains(oldMethod)) {
            throw new RuntimeException("Código do método original não encontrado no arquivo.");
        }

        String updatedContent = content.replace(oldMethod, newMethod);
        Files.writeString(file.toPath(), updatedContent);
    }
}