package com.t3h.java.module3;

import org.junit.jupiter.api.Test;

import com.t3h.java.module3.service.SongService;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountCalculatorTest {

    @Test
    public void testValidInputs() {
        // Normal case
        assertEquals(240.0, SongService.calculateDiscount(100, 20, 3));
        
        // No discount
        assertEquals(100.0, SongService.calculateDiscount(100, 0, 1));
        
        // Full discount
        assertEquals(0.0, SongService.calculateDiscount(100, 100, 1));
        
        // Edge case: originalPrice = 0
        assertEquals(0.0, SongService.calculateDiscount(0, 50, 5));
    }

    @Test
    public void testNegativeOriginalPrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SongService.calculateDiscount(-50, 10, 2);
        });
        assertEquals("Original price cannot be negative", exception.getMessage());
    }

    @Test
    public void testInvalidDiscountRate() {
        // discount > 100
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            SongService.calculateDiscount(100, 150, 2);
        });
        assertEquals("Discount rate must be between 0 and 100", exception1.getMessage());

        // discount < 0
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            SongService.calculateDiscount(100, -10, 2);
        });
        assertEquals("Discount rate must be between 0 and 100", exception2.getMessage());
    }

    @Test
    public void testInvalidQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SongService.calculateDiscount(100, 10, 0);
        });
        assertEquals("Quantity must be at least 1", exception.getMessage());
    }
}
