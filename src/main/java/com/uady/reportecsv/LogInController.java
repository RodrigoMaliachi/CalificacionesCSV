package com.uady.reportecsv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class LogInController {

    @FXML
    private TextField userField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label signIn;

    public static void start() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log_in_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Iniciar seción");
        stage.setScene(scene);
        stage.show();
    }

    public void onSignInClick() throws IOException {
        Stage stage = (Stage) signIn.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sign_in_view.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Crear cuenta");
        stage.show();
    }

    public void onLogInAction() throws IOException {
        String user = userField.getText();
        String password = passwordField.getText();

        boolean isLogged = false;

        try {
            isLogged = UserValidator.validateAccount(user, password);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (isLogged) {

            Stage logInStage = (Stage) userField.getScene().getWindow();
            boolean repetir;

            do {
                repetir = false;
                File studentsFile = CSVManager.chooseFile(logInStage);

                if (studentsFile != null) {
                    if (GradesRegisterController.setStudentsFile(studentsFile)) {
                        logInStage.close();
                        GradesRegisterController.start();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Archivo inválido");
                        alert.setHeaderText("El archivo no se pudo abrir");
                        alert.setContentText("El archivo CSV no contine una lista de alumnos válida.\n" +
                                "Asegurece que la lista contenga matrícula, apellido paterno, apellido materno, nombres en ese orden.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if ( result.isPresent() )
                            repetir = result.get() == ButtonType.OK;
                    }
                }
            } while ( repetir );

        } else {
            System.out.println("Mostrar error");
        }
    }
}