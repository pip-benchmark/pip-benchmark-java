package org.pipbenchmark.runner.gui.execution;

import java.util.*;

import org.pipbenchmark.runner.ExecutionType;
import org.pipbenchmark.runner.MeasurementType;

public interface IExecutionView {
    void setStartTime(String value);    
    void setEndTime(String value);
    void setElapsedTime(String value);
    void setMinPerformance(String value);
    void setAveragePerformance(String value);
    void setMaxPerformance(String value);
    void setMinCpuLoad(String value);
    void setAverageCpuLoad(String value);
    void setMaxCpuLoad(String value);
    void setMinMemoryUsage(String value);
    void setAverageMemoryUsage(String value);
    void setMaxMemoryUsage(String value);
    void setExecutionResults(List<ExecutionResult> value);

    int getNumberOfThreads();
    void setNumberOfThreads(int value);
    
    MeasurementType getMeasurementType();
    void setMeasurementType(MeasurementType value);
    
    double getNominalRate();
    void setNominalRate(double value);
    
    ExecutionType getExecutionType();
    void setExecutionType(ExecutionType value);
    
    int getDuration();
    void setDuration(int value);

    boolean getPerformanceChartEnabled();
    void setPerformanceChartEnabled(boolean value);
    
    String getPerformanceChartName();
    void setPerformanceChartName(String value);
    
    String getBenchmarkActionButton();
    void setBenchmarkActionButton(String value);
    
    boolean getShowPerformanceChart();
    void setShowPerformanceChart(boolean value);

    void setListener(IExecutionViewListener listener);
    
    void addCurrentPerformancePoint(double currentPerformance);
    void clearPerformancePoints();
}
