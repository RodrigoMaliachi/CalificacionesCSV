package com.uady.reportecsv;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.itextpdf.text.Document;

import java.io.*;
import java.net.URL;
import java.util.*;

public class GradesRegisterController implements Initializable {

    private static List<Student> studentsList;

    public Label counterLabel;

    public TextField matriculaField;
    public TextField lastNameField;
    public TextField nameField;
    public TextField gradeField;
    public Button saveCSVButton;

    public TableView<Grade> gradesTable;
    public TableColumn<Grade, Integer> matriculaGradeColumn;
    public TableColumn<Grade, Integer> gradeColumn;
    private ObservableList<Grade> gradeObservableList;
    private List<Grade> gradeList;

    public TableView<Student> studentsTable;
    public TableColumn<Student, String> matriculaColumn;
    public TableColumn<Student, String> paternoColumn;
    public TableColumn<Student, String> maternoColumn;
    public TableColumn<Student, String> nombresColumn;

    private int tableIndex = 0;
    private int gradedCounter = 0;

    public static void start() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("grades_register_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registro de calificaciones");
        stage.setScene(scene);
        stage.show();
    }

    public static boolean setStudentsFile(File file) {

        if ( file != null ) {

            studentsList = new ArrayList<>();
            List<String[]> students;

            try {

                CSVReader reader = new CSVReader(new FileReader(file));
                students = reader.readAll();
                for (String[] s : students) {
                    studentsList.add(new Student(s[0], s[1], s[2], s[3]));
                }
                return true;
            } catch (IOException | CsvException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        updateCounter();
    }

    private void initializeTable() {

        //Students table

        ObservableList<Student> studentObservableList = FXCollections.observableList(studentsList);

        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        paternoColumn.setCellValueFactory(new PropertyValueFactory<>("paterno"));
        maternoColumn.setCellValueFactory(new PropertyValueFactory<>("materno"));
        nombresColumn.setCellValueFactory(new PropertyValueFactory<>("nombres"));

        studentsTable.setItems(studentObservableList);

        //Grades Table
        initializeGradeList();
        gradeObservableList = FXCollections.observableList( gradeList );

        matriculaGradeColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        gradesTable.setItems(gradeObservableList);

        studentsTable.getSelectionModel().select(0);
        focusStudentRow();
    }

    private void initializeGradeList() {
        gradeList = new ArrayList<>();
        for (Student s: studentsList) {
            Grade grade = new Grade( s.getMatricula(), 0);
            gradeList.add(grade);
        }
    }

    private void focusStudentRow() {
        studentsTable.getSelectionModel().select(tableIndex);
        onFocusStudentRow();
    }

    public void onFocusStudentRow() {
        Student student = studentsTable.getSelectionModel().getSelectedItem();
        matriculaField.setText(student.getMatricula());
        lastNameField.setText(student.getPaterno());
        nameField.setText(student.getNombres().split(",")[0]);
        tableIndex = studentsTable.getSelectionModel().getFocusedIndex();
    }

    public void onFocusGradeRow() {
        Grade grade = gradesTable.getSelectionModel().getSelectedItem();
        for (Student student: studentsList) {
            if (student.getMatricula().equals(grade.getMatricula())) {
                studentsTable.getSelectionModel().select(student);
                studentsTable.scrollTo(student);
                onFocusStudentRow();
            }
        }
    }

    public void onUpAction() {
        tableIndex = ( tableIndex + studentsList.size() - 1 ) % studentsList.size();
        focusStudentRow();
    }

    public void onDownAction() {
        tableIndex = ( tableIndex + 1 ) % studentsList.size();
        focusStudentRow();
    }

    private void updateCounter() {
        counterLabel.setText("Alumnos calificados " + gradedCounter + "/" + studentsList.size());

        if ( gradedCounter == studentsList.size() ) {
            saveCSVButton.setDisable(false);
        }
    }

    public void onSaveAction() {
        int calificacion = 0;

        try {
            calificacion = Integer.parseInt(gradeField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if ( calificacion > 0 && calificacion <= 100 ) {

            String matricula = matriculaField.getText();
            for (Grade grade: gradeObservableList) {
                if ( grade.getMatricula().equals(matricula) ) {
                    if ( grade.getGrade() == 0 ) {
                        gradedCounter++;
                    }
                    grade.setGrade(calificacion);
                    gradesTable.refresh();
                    gradesTable.getSelectionModel().select(grade);
                    gradesTable.scrollTo(grade);
                }
            }
            updateCounter();
            onDownAction();
        }
    }

    public void onCreatePDF() {

        List<Grade> gradedStudents = gradeObservableList.stream().toList();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));

        String path = chooser.showSaveDialog(studentsTable.getScene().getWindow()).getAbsolutePath();

        if ( !path.endsWith(".pdf") ) {
            path += ".pdf";
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            PdfPTable table = new PdfPTable(3);
            table.addCell("Matricula");
            table.addCell("Nombre completo");
            table.addCell("Calificación");

            int index;
            Student student;
            for (Grade grade: gradedStudents) {
                table.addCell( grade.getMatricula() );
                index = gradedStudents.indexOf(grade);
                student = studentsList.get(index);
                table.addCell( student.getNombres() + " " + student.getPaterno() + " " + student.getMaterno() );
                if (grade.getGrade() > 0) {
                    table.addCell( String.valueOf( grade.getGrade() ) );
                } else {
                    table.addCell("S/C");
                }
            }

            document.add(table);
            document.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onCreateCSV() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));

        String path = chooser.showSaveDialog(studentsTable.getScene().getWindow()).getAbsolutePath();

        if ( !path.endsWith(".csv") ) {
            path += ".csv";
        }

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(path));

            for (Grade g : gradeObservableList.stream().toList()) {
                writer.writeNext(new String[]{g.getMatricula(), "Diseño de Software", String.valueOf(g.getGrade())});
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
