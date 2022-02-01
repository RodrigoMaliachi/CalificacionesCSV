package com.uady.reportecsv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.util.Optional;

public class CSVManager {

    private final String root;
    private List<String[]> lines;
    private List<String[]> newLines;
    private int currentLine;

    public CSVManager(String root) throws Exception {
        this.root = root;
        readFile();
    }

    public void readFile() throws Exception {
        if (root == null || root.isBlank())
            throw new IOException("La ruta del directorio está vacía.");
        CSVReader reader = new CSVReader(new FileReader( root ) );
        lines = new ArrayList<>();
        String[] values;
        while ( ( values = reader.readNext() ) != null ){
            lines.add(values);
        }
        reader.close();
        currentLine = 0;
        newLines = new ArrayList<>();
    }

    public List<String[]> getLines() { return lines; }

    public void setCurrentLine(int index) throws Exception {
        if ( index >= 0 && index < lines.size() )
            currentLine = index;
        else throw new Exception("El índice no es válido");
    }

    public String[] getCurrentLine() { return lines.get(currentLine); }

    public void addLineOfValues(String[] newLine) { newLines.add(newLine); }

    public int nextLine() {
        return currentLine + 1 >= lines.size() ? -1 : ++currentLine;
    }

    public void writeFile(boolean overwrite) throws NullPointerException, IOException {

        if ( newLines.isEmpty() )
            throw new NullPointerException("No existen nuevas líneas");

        CSVWriter writer;

        if ( overwrite ) {

            File file;
            String newRoot;
            do {
                System.out.print("Escribe la ruta para el nuevo archivo: ");
                newRoot = new BufferedReader(new InputStreamReader(System.in)).readLine();
                file = new File(newRoot);
            } while ( !file.createNewFile() );

            writer = new CSVWriter(new FileWriter(file));

        } else
            writer = new CSVWriter(new FileWriter(root));

        writer.writeAll(newLines);
        writer.close();
    }

    public boolean isLinesEmpty() { return lines.isEmpty(); }

    public static File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lista de alumnos CSV");
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter( "Archivos CSV", "*.csv") );

        File file;
        boolean repetir;

        do {
            repetir = false;
            file = fileChooser.showOpenDialog(stage);
            if ( file == null ) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("");
                alert.setHeaderText("Archivo CSV no encontrado");
                alert.setContentText("Se requiere de un archivo CSV para continuar\n¿Desea abrir de nuevo la ventana de búsqueda?");

                Optional<ButtonType> result = alert.showAndWait();
                if ( result.isPresent() )
                    repetir = result.get() == ButtonType.OK;
            }

        } while (repetir);
        return file;
    }
}
