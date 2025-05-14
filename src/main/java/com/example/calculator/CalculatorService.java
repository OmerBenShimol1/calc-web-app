package com.example.calculator;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculate(double a, double b, String op) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }
}
