package com.smartretailx.inventory.repository;

import com.smartretailx.inventory.model.Product;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryRepository {
    private final ConcurrentHashMap<String, Product> store = new ConcurrentHashMap<>();

    public void save(Product product) {
        store.put(product.getProductId(), product);
    }

    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(store.get(productId));
    }

    public void update(Product product) {
        store.put(product.getProductId(), product);
    }
}