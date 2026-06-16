package com.smartretailx.inventory.service;

import com.smartretailx.inventory.dto.StockReservationRequest;
import com.smartretailx.inventory.exception.InsufficientStockException;
import com.smartretailx.inventory.model.Product;
import com.smartretailx.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceImplTest {
    private InventoryRepository repository;
    private InventoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InventoryRepository();
        service = new InventoryServiceImpl(repository);
        repository.save(new Product("P001", "Laptop", 10, 999.99));
        repository.save(new Product("P002", "Mouse", 50, 19.99));
    }

    @Test
    void checkStock_ShouldReturnTrue_WhenStockSufficient() {
        Map<String, Integer> items = Map.of("P001", 2, "P002", 5);
        assertTrue(service.checkStock(items));
    }

    @Test
    void checkStock_ShouldReturnFalse_WhenStockInsufficient() {
        Map<String, Integer> items = Map.of("P001", 20);
        assertFalse(service.checkStock(items));
    }

    @Test
    void checkStock_ShouldThrowException_WhenProductNotFound() {
        Map<String, Integer> items = Map.of("P999", 1);
        assertThrows(ProductNotFoundException.class, () -> service.checkStock(items));
    }

    @Test
    void reserveItems_ShouldReduceStockCorrectly() {
        StockReservationRequest request = new StockReservationRequest("order-123", Map.of("P001", 3));
        service.reserveItems(request);
        assertEquals(7, service.getAvailableQuantity("P001"));
    }

    @Test
    void reserveItems_ShouldThrowException_WhenStockInsufficient() {
        StockReservationRequest request = new StockReservationRequest("order-123", Map.of("P001", 20));
        assertThrows(InsufficientStockException.class, () -> service.reserveItems(request));
    }

    @Test
    void getAvailableQuantity_ShouldReturnCorrectValue() {
        assertEquals(10, service.getAvailableQuantity("P001"));
    }
}