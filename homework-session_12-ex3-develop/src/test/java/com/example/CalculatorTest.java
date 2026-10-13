package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    @DisplayName("Kiểm tra phiên bản JDK đang chạy trong Matrix")
    void testJdkVersion() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("Running tests on JDK version: " + javaVersion);
        assertTrue(javaVersion != null && !javaVersion.isBlank());
    }

    @Test
    @DisplayName("Kiểm tra phép cộng hai số nguyên")
    void testAdd() {
        assertEquals(15, calculator.add(10, 5));
    }

    @Test
    @DisplayName("Kiểm tra phép trừ hai số nguyên")
    void testSubtract() {
        assertEquals(5, calculator.subtract(10, 5));
    }

    @Test
    @DisplayName("Kiểm tra phép nhân hai số nguyên")
    void testMultiply() {
        assertEquals(50, calculator.multiply(10, 5));
    }

    @Test
    @DisplayName("Kiểm tra phép chia hai số nguyên")
    void testDivide() {
        assertEquals(2.0, calculator.divide(10, 5));
    }

    @Test
    @DisplayName("Kiểm tra ngoại lệ khi chia cho 0")
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calculator.divide(10, 0));
    }
}
