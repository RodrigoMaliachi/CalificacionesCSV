module com.uady.registrocalificaciones {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    requires org.apache.commons.codec;
    requires com.opencsv;
    requires itextpdf;

    opens com.uady.reportecsv to javafx.fxml;
    exports com.uady.reportecsv;
}