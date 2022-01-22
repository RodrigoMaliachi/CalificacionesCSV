package com.uady;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {

    private static String root;
    private static List<String> lines;
    private static List<String> newLines;
    private static int currentLine;

    public static void setRoot(String directorio) {
        root = directorio;
    }

    public static void readFile() throws IOException {
        if (root == null || root.isBlank())
            throw new IOException("La ruta del directorio está vacía.");
        BufferedReader reader = new BufferedReader(new FileReader(root));
        lines = reader.lines().toList();
        reader.close();
        currentLine = 0;
    }

    public static String getCurrentLine() {
        return lines.get(currentLine);
    }

    public static void initializeNewLines() {
        newLines = new ArrayList<>();
    }

    public static void addNewLine(String newLine) {
        newLines.add(newLine);
    }

    public static int nextLine() {
        return currentLine + 1 >= lines.size() ? -1 : ++currentLine;
    }

    public static void rewroteCSV() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(root));
        writer.flush();
        for (String line : newLines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }
}
