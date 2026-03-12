package com.leo.backend;

public class App {
    public static void main(String[] args) {
        System.out.println(getSaludo());
    }

    // Un método simple que devuelve un String
    public static String getSaludo() {
        return "Hola Mundo!";
    }

    // Un método que suma dos números
    public int sumar(int a, int b) {
        return a + b;
    }
}