package com.uady.reportecsv;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.IOException;

public class UserValidator {

    protected static CSVManager users;

    static {
        try {
            File file = new File("Users.csv");
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            users = new CSVManager("Users.csv");
        } catch (Exception e) {
            System.err.println( e.getMessage() );
        }
    }

    public static boolean validateUser(String newUser) {
        if ( users.isLinesEmpty() ) return true;

        do {
            String user = users.getCurrentLine()[0];
            if ( newUser.equals(user) ) {
                System.out.println("El nombre de usuario ya está en uso, por favor, ingrese uno diferente.");
                return false;
            }

        } while ( users.nextLine() != -1);

        return true;
    }

    public static boolean validatePassword(String password, String password2) {

        if ( !password.equals(password2) ) {
            System.out.println("Las contraseñas no coinciden");
            return false;
        }

        if ( password.isBlank() || password.isEmpty() ) {
            System.out.println("La contraseña está vacía");
            return false;
        }
        
        if ( password.contains( " " ) ) {
            System.out.println("La contraseña no puede llevar espacios");
            return false;
        }
        
        if ( password.length() < 8 ) {
            System.out.println("La contraseña debe ser de al menos 8 caractéres");
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (char letra : password.toCharArray()) {
            if ( Character.isUpperCase(letra) )
                hasUpperCase = true;
            else if ( Character.isLowerCase(letra) )
                hasLowerCase = true;
            else if ( Character.isDigit(letra) )
                hasDigit = true;
        }

        boolean validPassword = hasUpperCase && hasLowerCase && hasDigit;

        if ( !validPassword ) {
            System.out.println("La contraseña debe tener mayúsculas, minúsculas y números");
        }

        return validPassword;
    }

    public static boolean validateAccount(String user, String password) throws Exception {

        if ( users.isLinesEmpty() ) {
            System.out.println("No existen usuarios registrados");
            return false;
        }

        do {

            String[] oldUser = users.getCurrentLine();

            if ( oldUser[0].equals(user) ) {
                String sha256 = DigestUtils.sha256Hex(password);

                if ( sha256.equals( oldUser[1] ) )
                    return  true;
                else {
                    users.setCurrentLine( users.getLines().size() - 1 );
                }
            }

        } while ( users.nextLine() != -1);

        System.out.println("Usuario o contraseña inválido");

        return false;
    }

    public static boolean createAccount(String user, String password, String password2) throws IOException {

        boolean isValidUser = validateUser( user );
        boolean isValidPassword = validatePassword( password, password2 );

        if ( isValidUser && isValidPassword ) {
            users.addLineOfValues(new String[]{user, DigestUtils.sha256Hex(password)});
            users.writeFile(false);
        }

        return isValidUser && isValidPassword;
    }
}