package com.leo.backend.model;

public class Coche {
    private String id;
    private String marca;
    private String modelo;
    private int precio;
    private boolean enStock;

    // Constructor
    public Coche(String id, String marca, String modelo, int precio, boolean enStock) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.enStock = enStock;
    }

    // Getters (Importantes para que Spring pueda leer los datos)
    public String getId() { return id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getPrecio() { return precio; }
    public boolean isEnStock() { return enStock; }
}