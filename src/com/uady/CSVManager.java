package com.uady;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CSVManager {

    private String root;
    private List<String> lines;
    private List<String> newLines;
    private int currentLine;

    public CSVManager(String root) throws IOException {
        this.root = root;
        readFile();
    }

    public void setRoot(String directorio) { root = directorio; }

    public String getRoot() { return root; }

    public void readFile() throws IOException {
        if (root == null || root.isBlank())
            throw new IOException("La ruta del directorio está vacía.");
        BufferedReader reader = new BufferedReader(new FileReader(root));
        lines = reader.lines().toList();
        reader.close();
        currentLine = 0;
        newLines = new ArrayList<>();
    }

    public void setCurrentLine(int index) throws Exception {
        if ( index >= 0 && index < lines.size() )
            currentLine = index;
        else throw new Exception("El índice no es válido");
    }

    public String getCurrentLine() { return lines.get(currentLine); }

    public void resetNewLines() { newLines = new ArrayList<>(); }

    public void addNewLine(String newLine) { newLines.add(newLine); }

    public int nextLine() {
        return currentLine + 1 >= lines.size() ? -1 : ++currentLine;
    }

    public int previousLine(){
        return currentLine - 1 >= 0 ? --currentLine : -1;
    }

    public void writeFile(boolean overwrite) throws NullPointerException, IOException {

        if ( newLines.isEmpty() )
            throw new NullPointerException("No existen nuevas líneas");

        BufferedWriter writer;

        if ( overwrite ) {

            File file;
            String newRoot;
            do {
                System.out.print("Escribe la ruta para el nuevo archivo: ");
                newRoot = new BufferedReader(new InputStreamReader(System.in)).readLine();
                file = new File(newRoot);
            } while ( !file.createNewFile() );

            writer = new BufferedWriter(new FileWriter(file));

            for (String contenido : newLines) {
                writer.write(contenido);
                writer.newLine();
            }
        } else {
            writer = new BufferedWriter(new FileWriter(root));

            for (String line : newLines) {
                writer.newLine();
                writer.write(line);
            }
        }
        writer.close();
    }
}
