package com.smartretailx.inventory.model;

import java.util.Objects;

public class Product {
    private String productId;
    private String name;
    private int quantityAvailable;
    private double price;

    public Product(String productId, String name, int quantityAvailable, double price) {
        this.productId = productId;
        this.name = name;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public int getQuantityAvailable() { return quantityAvailable; }
    public double getPrice() { return price; }

    public void reduceStock(int quantity) {
        if (quantity > quantityAvailable) {
            throw new InsufficientStockException("Not enough stock for product: " + productId);
        }
        this.quantityAvailable -= quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() { return Objects.hash(productId); }
}