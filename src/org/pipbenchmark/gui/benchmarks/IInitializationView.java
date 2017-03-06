package org.pipbenchmark.gui.benchmarks;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;

public interface IInitializationView {
    List<BenchmarkSuiteInstance> getAllSuites();
    void setAllSuites(List<BenchmarkSuiteInstance> value);
    
    List<BenchmarkSuiteInstance> getSelectedSuites();
    
    List<BenchmarkInstance> getAllBenchmarks();
    void setAllBenchmarks(List<BenchmarkInstance> value);
    
    List<BenchmarkInstance> getSelectedBenchmarks();

    void setListener(IInitializationViewListener listener);
    
    void refreshData();
}
