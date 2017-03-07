package org.pipbenchmark.gui.benchmarks;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;

public interface IBenchmarksView {
    List<BenchmarkSuiteInstance> getAllSuites();
    void setAllSuites(List<BenchmarkSuiteInstance> value);
    
    List<BenchmarkSuiteInstance> getSelectedSuites();
    
    List<BenchmarkInstance> getAllBenchmarks();
    void setAllBenchmarks(List<BenchmarkInstance> value);
    
    List<BenchmarkInstance> getSelectedBenchmarks();

    void setListener(IBenchmarksViewListener listener);
    
    void refreshData();
}
