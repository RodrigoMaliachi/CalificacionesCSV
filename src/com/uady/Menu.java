package com.uady;

import java.io.BufferedReader;
import java.io.IOException;

public class Menu {

    enum Result {FINISHED, INVALID_ROOT, EMPTY_ROOT, OK}

    protected static BufferedReader reader;
    protected static Result result = Result.EMPTY_ROOT;
    protected static CSVManager students;

    public static void printMenu() throws IOException {
        System.out.println("Ingrese una letra correspondiente a una opción disponible:");
        System.out.println("[C]ambiar ruta de acceso al archivo CSV.");
        System.out.println("[R]egistrar calificaciones.");
        System.out.println("[A]rchivar calificaciones.");
        System.out.println("[S]alir.");
        choseInstruction( reader.readLine().toUpperCase().charAt(0) );
    }

    private static void choseInstruction(char instruction) {
        try {
            switch (instruction) {
                case 'C' -> readRoot();
                case 'R' -> captureGrades();
                case 'A' -> students.writeFile(true);
                case 'S' -> result = Result.FINISHED;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected static void readRoot() {
        System.out.println("Escribe la ruta de acceso del archivo CSV.");
        try {
            students = new CSVManager( reader.readLine() );
            result = Result.OK;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            result = Result.INVALID_ROOT;
        }
    }

    private static void captureGrades() throws IOException {

        int calificacion;
        boolean invalidGrade = false;

        students.resetNewLines();

        System.out.println("MATRICULA  | PRIMER APELLIDO | SEGUNDO APELLIDO | NOMBRE");
        do {

            if ( invalidGrade )
                System.err.println("La calificación no es válida");

            String line = students.getCurrentLine();
            String[] columns = line.split(",");

            System.out.print(columns[0] + spaceThisLong(4));
            System.out.print(columns[1] + spaceThisLong(19 - columns[1].length()));
            System.out.print(columns[2] + spaceThisLong(19 - columns[2].length()));
            System.out.println(columns[3]);
            System.out.print("CALIFICACIÓN: ");

            calificacion = -1;

            try {
                calificacion = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            } finally {
                if ( calificacion > 0 && calificacion <= 100 ) {
                    students.addNewLine(columns[0] + "," + "Diseño de software" + "," + calificacion);
                    invalidGrade = false;
                }
                else
                    invalidGrade = true;
            }

        } while ( invalidGrade || students.nextLine() != -1);
    }

    private static String spaceThisLong(int magnitude) {
        StringBuilder space = new StringBuilder();
        while (magnitude > 0) {
            space.append(" ");
            magnitude--;
        }
        return space.toString();
    }
}