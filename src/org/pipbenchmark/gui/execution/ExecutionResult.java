package org.pipbenchmark.gui.execution;

import org.pipbenchmark.runner.results.*;

public class ExecutionResult {
    private String _benchmark;
    private double _performance;
    private double _cpuLoad;
    private double _memoryUsage;

    public ExecutionResult(BenchmarkResult result) {
        _benchmark = result.getBenchmarks().size() != 1
        	? "All" : result.getBenchmarks().get(0).getFullName();
        _performance = result.getPerformanceMeasurement().getAverageValue();
        _cpuLoad = result.getCpuLoadMeasurement().getAverageValue();
        _memoryUsage = result.getMemoryUsageMeasurement().getAverageValue();
    }

    public String getBenchmark() {
        return _benchmark;
    }

    public double getPerformance() {
        return _performance;
    }

    public double getCpuLoad() {
        return _cpuLoad;
    }

    public double getMemoryUsage() {
        return _memoryUsage;
    }

    public void update(BenchmarkResult result) {
        _performance = result.getPerformanceMeasurement().getAverageValue();
        _cpuLoad = result.getCpuLoadMeasurement().getAverageValue();
        _memoryUsage = result.getMemoryUsageMeasurement().getAverageValue();
    }
}
