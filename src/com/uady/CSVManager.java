package com.uady;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CSVManager {

    private static String root;
    private static List<String> lines;
    private static List<String> newLines;
    private static int currentLine;
    private static boolean isCounterStoped = false;

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

    public static String getCurrentLine() { return lines.get(currentLine); }

    public static void initializeNewLines() { newLines = new ArrayList<>(); }

    public static void addNewLine(String newLine) { newLines.add(newLine); }

    public static int nextLine() {
        if (isCounterStoped) {
            isCounterStoped = false;
            return currentLine;
        }
        return currentLine + 1 >= lines.size() ? -1 : ++currentLine;
    }

    public static void createCSV() {
       if (newLines==null) {
           System.out.println("Aun no ha capturado ninguna calificacion");
       } else {
           try {
               System.out.print("Escribe la ruta para el nuevo archivo: ");
               String ruta = new BufferedReader(new InputStreamReader(System.in)).readLine();
               File file = new File(ruta);
               file.createNewFile();

               BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));

               for (String contenido : newLines) {
                   bw.write(contenido);
                   bw.newLine();
               }
               bw.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    }

    public static void stopCounter() { isCounterStoped = true; }
}
