package org.pipbenchmark.runner.results;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;

public class BenchmarkResult {	
    private List<BenchmarkInstance> _benchmarks = new ArrayList<BenchmarkInstance>();
    private long _startTime = System.currentTimeMillis();
    private long _elapsedTime = 0;
    private Measurement _performanceMeasurement = new Measurement(0, 0, 0, 0);
    private Measurement _cpuLoadMeasurement = new Measurement(0, 0, 0, 0);
    private Measurement _memoryUsageMeasurement = new Measurement(0, 0, 0, 0);
    private List<Object> _errors = new ArrayList<Object>();

    public BenchmarkResult(List<BenchmarkInstance> benchmarks) {
    	_benchmarks = benchmarks;
    }

    public List<BenchmarkInstance> getBenchmarks() {
        return _benchmarks;
    }

    public long getStartTime() {
        return _startTime; 
    }
    
    public void setStartTime(long value) {
        _startTime = value;
    }

    public long getElapsedTime() {
        return _elapsedTime;
    }
    
    public void setElapsedTime(long value) {
        _elapsedTime = value;
    }

    public Measurement getPerformanceMeasurement() {
        return _performanceMeasurement; 
    }
    
    public void setPerformanceMeasurement(Measurement value) {
        _performanceMeasurement = value;
    }

    public Measurement getCpuLoadMeasurement() {
        return _cpuLoadMeasurement; 
    }
    
    public void setCpuLoadMeasurement(Measurement value) {
        _cpuLoadMeasurement = value;
    }

    public Measurement getMemoryUsageMeasurement() {
        return _memoryUsageMeasurement;
    }
    
    public void setMemoryUsageMeasurement(Measurement value) {
        _memoryUsageMeasurement = value;
    }
    
    public List<Object> getErrors() {
    	return _errors;
    }
}
