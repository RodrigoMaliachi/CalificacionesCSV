package com.uady;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        Menu.reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            Menu.readRoot();
            while (Menu.result == 0) Menu.printMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}