package com.smartretailx.inventory.service;

import com.smartretailx.inventory.dto.StockReservationRequest;
import java.util.Map;

public interface InventoryService {
    boolean checkStock(Map<String, Integer> items);
    void reserveItems(StockReservationRequest request);
    int getAvailableQuantity(String productId);
}