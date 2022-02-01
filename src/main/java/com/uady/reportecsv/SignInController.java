package com.uady.reportecsv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class SignInController {
    @FXML
    public TextField userField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField confirmField;

    @FXML
    public Button cancelButton;

    @FXML
    public Button acceptButton;

    @FXML
    protected void onAcceptAction() throws Exception {
        String user = userField.getText();
        String password = passwordField.getText();
        String confirmP = confirmField.getText();

        boolean isValid = UserValidator.createAccount( user, password, confirmP );

        if ( isValid ) {

            Stage signInStage = (Stage) userField.getScene().getWindow();
            boolean repetir;

            do {
                repetir = false;
                File studentsFile = CSVManager.chooseFile(signInStage);

                if (studentsFile != null) {
                    if (GradesRegisterController.setStudentsFile(studentsFile)) {
                        signInStage.close();
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

    @FXML
    public void onCancelAction() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("log_in_view.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Iniciar seción");
        stage.show();
    }
}
