package com.uady.reportecsv;

@SuppressWarnings("unused")
public class Student {

    private String matricula;
    private String paterno;
    private String materno;
    private String nombres;

    public Student(String matricula, String paterno, String materno, String nombres) {
        this.matricula = matricula;
        this.paterno = paterno;
        this.materno = materno;
        this.nombres = nombres;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
