package org.pipbenchmark.runner.execution;

import org.pipbenchmark.runner.results.*;

public abstract class BenchmarkMeter {
    private long _lastMeasuredTime;
    private double _currentValue;
    private double _minValue;
    private double _maxValue;
    private double _averageValue;
    private double _sumOfValues;
    private double _numberOfMeasurements;

    public BenchmarkMeter() {
        clear();
    }

    public Measurement getMeasurement() {
        return new Measurement(getCurrentValue(), getMinValue(), 
        		getAverageValue(), getMaxValue());
    }

    public long getLastMeasuredTime() {
        return _lastMeasuredTime; 
    }
    
    protected void setLastMeasuredTime(long value) {
        _lastMeasuredTime = value;
    }

    public double getCurrentValue() {
        return _currentValue;
    }
    
    protected void setCurrentValue(double value) {
        _currentValue = value;
    }

    public double getMinValue() {
        return _minValue < Double.MAX_VALUE ? _minValue : 0;
    }
    
    protected void setMinValue(double value) {
        _minValue = value;
    }

    public double getMaxValue() {
        return _maxValue > Double.MIN_VALUE ? _maxValue : 0; 
    }
    
    protected void setMaxValue(double value) {
        _minValue = value;
    }

    public double getAverageValue() {
        return _averageValue; 
    }
    
    protected void setAverageValue(double value) {
        _averageValue = value;
    }

    public void clear() {
        _lastMeasuredTime = System.currentTimeMillis();
        _currentValue = performMeasurement();
        _minValue = Double.MAX_VALUE;
        _maxValue = Double.MIN_VALUE;
        _averageValue = 0;
        _sumOfValues = 0;
        _numberOfMeasurements = 0;
    }

    protected void calculateAggregates() {
        _sumOfValues += _currentValue;
        _numberOfMeasurements++;
        _averageValue = _sumOfValues / _numberOfMeasurements;
        _maxValue = Math.max(_maxValue, _currentValue);
        _minValue = Math.min(_minValue, _currentValue);
    }

    public double measure() {
        _currentValue = performMeasurement();
        _lastMeasuredTime = System.currentTimeMillis();
        calculateAggregates();
        return _currentValue;
    }

    protected abstract double performMeasurement();
}
