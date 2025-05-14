package com.example.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @Test
    void testAddition() {
        assertEquals(7.0, service.calculate(4, 3, "+"));
    }

    @Test
    void testSubtraction() {
        assertEquals(1.0, service.calculate(4, 3, "-"));
    }

    @Test
    void testMultiplication() {
        assertEquals(12.0, service.calculate(4, 3, "*"));
    }

    @Test
    void testDivision() {
        assertEquals(2.0, service.calculate(6, 3, "/"));
    }

    @Test
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> service.calculate(5, 0, "/"));
    }

    @Test
    void testInvalidOperator() {
        assertThrows(IllegalArgumentException.class, () -> service.calculate(1, 2, "?"));
    }
}
