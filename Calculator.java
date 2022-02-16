package com.company;

public class Calculator {
    private CalculatorInterface calculator;

    public void setCalculator(CalculatorInterface calculator) {
        this.calculator = calculator;
    }

    public void sum(String[] array) {
        this.calculator.sum(array);
    }
}
