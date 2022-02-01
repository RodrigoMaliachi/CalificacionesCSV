package com.uady.reportecsv;

@SuppressWarnings("unused")
public class Grade {
    private String matricula;
    private int grade;

    public Grade(String matricula, int grade) {
        this.matricula = matricula;
        this.grade = grade;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
