package com.br.buggerFixer.utils;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JavaFormatterUtil {

    public static void formatFile(File file) throws IOException {
        String content = Files.readString(file.toPath());

        CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
        TextEdit edit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, content, 0, content.length(), 0, null);

        if (edit != null) {
            Document document = new Document(content);
            try {
                edit.apply(document);
                Files.writeString(file.toPath(), document.get());
            } catch (Exception e) {
                System.err.println("Erro ao aplicar formatação: " + e.getMessage());
            }
        } else {
            System.err.println("Não foi possível formatar o código (talvez ele já esteja válido).");
        }
    }
}
