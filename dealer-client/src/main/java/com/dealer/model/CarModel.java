package com.dealer.model;

public class CarModel {
    private Long id;
    private String name;
    private double price;
    private Brand brand;

    public CarModel() {}

    public CarModel(Long id, String name, double price, Brand brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }
}
