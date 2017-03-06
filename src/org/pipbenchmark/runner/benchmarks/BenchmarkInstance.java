package org.pipbenchmark.runner.benchmarks;

import org.pipbenchmark.*;

public class BenchmarkInstance {
    private BenchmarkSuiteInstance _suite;
    private Benchmark _benchmark;
    private boolean _selected = false;
    private int _proportion = 100;
    private double _startExecutionTrigger;
    private double _endExecutionTrigger;

    public BenchmarkInstance(BenchmarkSuiteInstance suite, Benchmark benchmark) {
        _suite = suite;
        _benchmark = benchmark;
    }

    public BenchmarkSuiteInstance getSuite() {
    	return _suite;
    }

    public Benchmark getBenchmark() {
    	return _benchmark;
    }
    
    public String getName() {
        return _benchmark.getName();
    }

    public String getFullName() {
        return String.format("%s.%s", getSuite().getName(), getName());
    }

    public String getDescription() {
        return _benchmark.getDescription();
    }

    public boolean isSelected() {
        return _selected;
    }
    
    public void setSelected(boolean value) {
        _selected = value;
    }

    public boolean isPassive() {
        return _benchmark instanceof PassiveBenchmark;
    }

    public int getProportion() {
        return _proportion;
    }
    
    public void setProportion(int value) {
        _proportion = Math.max(0, Math.min(10000, value));
    }

    public double getStartExecutionTrigger() {
    	return _startExecutionTrigger;
    }
    
    public void setStartExecutionTrigger(double value) {
    	_startExecutionTrigger = value;
    }
    
    public double getEndExecutionTrigger() {
    	return _endExecutionTrigger;
    }
    
    public void setEndExecutionTrigger(double value) {
    	_endExecutionTrigger = value;
    }

    public boolean isTriggered(double trigger) {
    	return trigger >= _startExecutionTrigger && trigger < _endExecutionTrigger;
    }
    
    public void setUp(IExecutionContext context) {
    	_benchmark.setContext(context);
    	_benchmark.setUp();
    }
    
    public void execute() {
    	_benchmark.execute();
    }
    
    public void tearDown() {
    	_benchmark.tearDown();
    	_benchmark.setContext(null);
    }
}
