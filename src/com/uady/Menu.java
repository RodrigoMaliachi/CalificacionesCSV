package com.uady;

import java.io.BufferedReader;
import java.io.IOException;

public class Menu {

    protected static BufferedReader reader;
    private static int result = 0;

    public static int printMenu() throws IOException {
        System.out.flush();
        System.out.println("Ingrese una letra correspondiente a una opción disponible:");
        System.out.println("[C]ambiar ruta de acceso al archivo CSV.");
        System.out.println("[R]egistrar calificaciones.");
        System.out.println("[I]mprimir registro de calificaciones");
        System.out.println("[A]rchivar calificaciones.");
        System.out.println("[S]alir.");
        choseInstruction( reader.readLine().toUpperCase().charAt(0) );
        return result;
    }

    private static void choseInstruction(char instruction) throws IOException {
        System.out.flush();
        switch (instruction) {
            case 'C' -> readRoot();
            case 'R' -> captureGrades();
            case 'S' -> result = -1;
        }
    }

    private static void readRoot() throws IOException {
        System.out.println("Escribe la ruta de acceso del archivo CSV.");
        CSVManager.setRoot( reader.readLine() );
        CSVManager.readFile();
    }

    private static void captureGrades() throws IOException {

        int calificacion;

        do {
            String line = CSVManager.getCurrentLine();
            String[] columns = line.split("[,]");

            System.out.println("MATRICULA  | APELLIDO PATERNO | APELLIDO MATERNO | NOMBRE");
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
                e.printStackTrace();
            } finally {
                if ( calificacion > 0 && calificacion <= 100 )
                    CSVManager.addNewLine( line + "," + calificacion);
            }

        } while (CSVManager.nextLine() != -1);
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
