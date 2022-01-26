package com.uady;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        Menu.reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (Menu.result != Menu.Result.OK) Menu.readRoot();
            while (Menu.result != Menu.Result.FINISHED) Menu.printMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}