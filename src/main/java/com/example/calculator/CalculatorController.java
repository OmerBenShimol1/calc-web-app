package com.example.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping("/calculate")
    public double calculate(
            @RequestParam double a,
            @RequestParam double b,
            @RequestParam String op
    ) {
        return calculatorService.calculate(a, b, op);
    }
}
