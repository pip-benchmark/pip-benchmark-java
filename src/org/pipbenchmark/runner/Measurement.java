package org.pipbenchmark.runner;

public class Measurement {
    private double _currentValue;
    private double _minValue;
    private double _averageValue;
    private double _maxValue;

    public Measurement(double currentValue, double minValue,
        double averageValue, double maxValue) {
        _currentValue = currentValue;
        _minValue = minValue;
        _averageValue = averageValue;
        _maxValue = maxValue;
    }

    public double getCurrentValue() {
        return _currentValue;
    }

    public double getMinValue() {
        return _minValue;
    }

    public double getAverageValue() {
        return _averageValue;
    }

    public double getMaxValue() {
        return _maxValue;
    }
}
