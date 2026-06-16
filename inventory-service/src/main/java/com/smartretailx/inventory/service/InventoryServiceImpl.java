package com.smartretailx.inventory.service;

import com.smartretailx.inventory.dto.StockReservationRequest;
import com.smartretailx.inventory.exception.InsufficientStockException;
import com.smartretailx.inventory.exception.ProductNotFoundException;
import com.smartretailx.inventory.model.Product;
import com.smartretailx.inventory.repository.InventoryRepository;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository repository;
    private final ReentrantLock lock = new ReentrantLock();

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkStock(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = repository.findById(entry.getKey())
                .orElseThrow(() -> new ProductNotFoundException(entry.getKey()));
            if (product.getQuantityAvailable() < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void reserveItems(StockReservationRequest request) {
        lock.lock();
        try {
            for (Map.Entry<String, Integer> entry : request.getItems().entrySet()) {
                Product product = repository.findById(entry.getKey())
                    .orElseThrow(() -> new ProductNotFoundException(entry.getKey()));
                product.reduceStock(entry.getValue());
                repository.update(product);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getAvailableQuantity(String productId) {
        return repository.findById(productId)
            .map(Product::getQuantityAvailable)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}